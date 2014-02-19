package com.wbc.wbcapp.activity;

import groovyx.net.http.URIBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aspose.cloud.sdk.common.AsposeApp;
import com.wbc.wbc.R;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
//import org.docx4j.openpackaging.packages.PresentationMLPackage;
//import org.docx4j.openpackaging.parts.Part;
//import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
//import org.pptx4j.convert.out.svginhtml.SvgExporter;

public class DocumentViewActivity extends Activity {

	WebView documentView;
	Activity mActivity;

	String appSID, appKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_document_view);
		mActivity = this;
		documentView = (WebView) findViewById(R.id.document_view);

		appSID = "11d55855-f6bd-4b84-8c79-e10be6931d25";
		appKey = "bcec854ec2a184f41483ae0ce68dcde0";

		AsposeApp.setAppSID(appSID);
		AsposeApp.setAppKey(appKey);

		uploadFileToStoarge(appSID, appKey);

		String strURI = "http://api.aspose.com/v1.1/slides/Protection.pptx?format=html";
		String signedURI = Sign(strURI, "11d55855-f6bd-4b84-8c79-e10be6931d25", "bcec854ec2a184f41483ae0ce68dcde0");

		File document = new File(getFilesDir(), "Protection.htm");
		document.delete();
		if (document.exists())
			documentView.loadUrl("file:///" + document.getAbsolutePath());
		else
			Toast.makeText(mActivity, "File not found", Toast.LENGTH_LONG).show();

		InputStream responseStream;
		try {
			new AsyncTask<String, Void, File>() {

				String filename;

				@Override
				protected File doInBackground(String... args) {
					InputStream inputStream = null;
					String strURI = args[0];
					String strHttpCommand = args[1];
					File outputPath = new File(args[2], args[3]);
					filename = args[3];
					URL address;
					try {
						if (isNetworkConnected()) {
							address = new URL(strURI);

							HttpURLConnection httpCon = (HttpURLConnection) address.openConnection();
							// httpCon.setDoOutput(true);
							//
							// httpCon.setRequestProperty("Content-Type",
							// "application/json");
							// httpCon.setRequestProperty("Accept",
							// "text/json");
							httpCon.setRequestMethod(strHttpCommand);
							// if (strHttpCommand.equals("PUT") ||
							// strHttpCommand.equals("POST"))
							// httpCon.setFixedLengthStreamingMode(0);
							// String d = httpCon.getResponseMessage();
							// System.out.println(d);
							inputStream = httpCon.getInputStream();
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					FileOutputStream outputStream;
					if (inputStream != null) {
						try {
							outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
							int read = 0;
							byte[] bytes = new byte[1024];

							while ((read = inputStream.read(bytes)) != -1) {
								outputStream.write(bytes, 0, read);
							}
							outputStream.flush();
							outputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// OutputStream outputStream = null;
					// try {
					// // read this file into InputStream
					//
					// // write the inputStream to a FileOutputStream
					// outputStream = new FileOutputStream(outputPath);
					//
					// int read = 0;
					// byte[] bytes = new byte[8192];
					//
					// while ((read = inputStream.read(bytes)) != -1) {
					// outputStream.write(bytes, 0, read);
					// }
					//
					// inputStream.close();
					// outputStream.flush();
					// outputStream.close();
					//
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					return outputPath;
				}

				protected void onPostExecute(File file) {
					if (file.canRead())
						if (file.exists()) {
							documentView.loadUrl("file:///" + file.getAbsolutePath());
							deleteFileFromStorage(filename);
						}
				}

			}.execute(signedURI, "GET", getFilesDir().getAbsolutePath(), "Protection.htm");

			// File outputPath = new File(getCacheDir() + "/Protection.html");
			// SaveStreamToFile(outputPath, responseStream);
			// responseStream.close();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		documentView.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		documentView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		documentView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});

		// documentView.loadUrl(getCacheDir() + "/Protection.pdf");
		// "file:///android_asset/protection/Protection.html"
		// getCacheDir() + "/Protection.html"
	}

	protected void deleteFileFromStorage(final String fileName) {

		new AsyncTask<String, Void, InputStream>() {
			@Override
			protected InputStream doInBackground(String... params) {
				// build URI to delete file from the Aspose cloud storage
				String strURI = "http://api.aspose.com/v1.1/storage/file/" + fileName;

				// Use the following line to use API version 1.1
				// String strURI = "http://api.aspose.com/v1.1/storage/file/" +
				// fileName;

				String signedURI = Sign(strURI, appSID, appKey);
				InputStream result = null;
				try {
					URL address = new URL(signedURI);
					HttpURLConnection httpCon = (HttpURLConnection) address.openConnection();
					// httpCon.setDoOutput(true);

					httpCon.setRequestProperty("Content-Type", "application/json");
					httpCon.setRequestProperty("Accept", "text/json");
					httpCon.setRequestMethod("DELETE");
					// if (strHttpCommand.equals("PUT") ||
					// strHttpCommand.equals("POST"))
					// httpCon.setFixedLengthStreamingMode(0);
					String d = httpCon.getResponseMessage();
					System.out.println(d);
					result = httpCon.getInputStream();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}
		}.execute();
//		InputStream responseStream;
//		try {
//			responseStream = ProcessCommand(signedURI, "DELETE");
//			//String strJSON = IOUtils.toString(responseStream);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public static InputStream ProcessCommand(final String strURI, final String strHttpCommand) throws Exception {

		new AsyncTask<String, Void, InputStream>() {
			@Override
			protected InputStream doInBackground(String... params) {
				InputStream result = null;
				try {
					URL address = new URL(strURI);
					HttpURLConnection httpCon = (HttpURLConnection) address.openConnection();
					// httpCon.setDoOutput(true);

					httpCon.setRequestProperty("Content-Type", "application/json");
					httpCon.setRequestProperty("Accept", "text/json");
					httpCon.setRequestMethod(strHttpCommand);
					// if (strHttpCommand.equals("PUT") ||
					// strHttpCommand.equals("POST"))
					// httpCon.setFixedLengthStreamingMode(0);
					String d = httpCon.getResponseMessage();
					System.out.println(d);
					result = httpCon.getInputStream();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}
		}.execute();
		return null;

	}

	private void uploadFileToStoarge(String appSID, String appKey) {
		// build URI to upload file in the Aspose cloud storage
		String strURI = "http://api.aspose.com/v1.1/storage/file/Protection.pptx";

		File file = new File(getFilesDir(), "Protection.pptx");
		try {
			SaveStreamToFile(file, getAssets().open("Protection.pptx"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Use the following line to use API version 1.1
		// String strURI = "http://api.aspose.com/v1.1/storage/file/" +
		// folderName + "/" + fileName;

		String signedURI = Sign(strURI, appSID, appKey);

		String strResponse = UploadFileBinary(file, signedURI, "PUT", mActivity);

	}

	public static String Sign(String UnsignedURL, String AppSID, String AppKey) {
		try {
			UnsignedURL = UnsignedURL.replace(" ", "%20");
			Map<String, String> query = new HashMap<String, String>(1);
			query.put("appSID", AppSID);
			URIBuilder uri = new URIBuilder(UnsignedURL);

			String url = uri.toURI().getPath();
			url = url.replace(" ", "%20");
			uri.setPath(url);
			uri.addQueryParams(query);
			url = uri.toURI().getPath();
			// Remove final slash here as it can be added automatically.
			if (url.charAt(url.length() - 1) == '/') {
				String tempUrl = url.substring(0, url.length() - 1);
				uri.setPath(tempUrl);
			}
			url = uri.toURI().toString();

			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(AppKey.getBytes(), "HMAC_SHA1_ALGORITHM");

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			// HMAC_SHA1_ALGORITHM
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(url.getBytes());
			byte newresult[] = Base64.encodeBase64(rawHmac);

			// Remove invalid symbols.
			String result = new String(newresult);
			result = result.substring(0, result.length() - 1);

			String encodedUrl = URLEncoder.encode(result, "UTF-8");
			return uri.toURI().toString() + "&signature=" + encodedUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// public static InputStream ProcessCommand(String strURI, String
	// strHttpCommand) throws Exception {
	//
	//
	// // URL address = new URL(strURI);
	// // HttpURLConnection httpCon = (HttpURLConnection)
	// address.openConnection();
	// // httpCon.setDoOutput(true);
	// //
	// // httpCon.setRequestProperty("Content-Type", "application/json");
	// // httpCon.setRequestProperty("Accept", "text/json");
	// // httpCon.setRequestMethod(strHttpCommand);
	// // if (strHttpCommand.equals("PUT") || strHttpCommand.equals("POST"))
	// // httpCon.setFixedLengthStreamingMode(0);
	// // String d = httpCon.getResponseMessage();
	// // System.out.println(d);
	// // return httpCon.getInputStream();
	// }

	public void SaveStreamToFile(File outputPath, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			// read this file into InputStream

			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(outputPath);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			System.out.println("Done!");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_view, menu);
		return true;
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	public static String UploadFileBinary(final File localFile, final String uploadUrl, String strHttpCommand,
			final Activity mActivity) {
		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					URL url = new URL(uploadUrl);
					byte[] buf = FileUtils.readFileToByteArray(localFile);
					HttpURLConnection m_connection;
					m_connection = (HttpURLConnection) url.openConnection();
					String parameters = "data=some_post_data";
					m_connection.setDoOutput(true);
					m_connection.setRequestMethod("PUT");
					m_connection.setRequestProperty("Accept", "text/json");
					m_connection.setRequestProperty("Content-Type", "MultiPart/Form-Data");
					byte bytes[] = parameters.getBytes();
					m_connection.setRequestProperty("Content-length", "" + buf.length);
					m_connection.connect();
					java.io.OutputStream out = m_connection.getOutputStream();
					out.write(buf);
					out.flush();

					InputStream response = m_connection.getInputStream();
					String res = IOUtils.toString(response);
					//Toast.makeText(mActivity, res, Toast.LENGTH_SHORT).show();
					return res;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

		}.execute();
		return null;

	}

}
