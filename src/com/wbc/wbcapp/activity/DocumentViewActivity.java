package com.wbc.wbcapp.activity;

import groovyx.net.http.URIBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_document_view);
		mActivity = this;

		AsposeApp.setAppSID("11d55855-f6bd-4b84-8c79-e10be6931d25");
		AsposeApp.setAppKey("bcec854ec2a184f41483ae0ce68dcde0");

		String strURI = "http://api.aspose.com/v1.1/slides/Protection.pptx?format=html";
		String signedURI = Sign(strURI, "11d55855-f6bd-4b84-8c79-e10be6931d25", "bcec854ec2a184f41483ae0ce68dcde0");
		InputStream responseStream;
		try {
			//responseStream = ProcessCommand(signedURI, "GET");
			new GetDocumentFromServer().execute(signedURI, "GET", getCacheDir() + "/Protection.html");
//			File outputPath = new File(getCacheDir() + "/Protection.html");
//			SaveStreamToFile(outputPath, responseStream);
//			responseStream.close();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

//		try {
//
//			AssetManager aMgr = mActivity.getAssets();
//			InputStream ipStream = aMgr.open("slideshow.pptx");
//			String filePath = getFilesDir().getAbsolutePath();
//			InputStream inputStream = null;
//			OutputStream outputStream = null;
//
//			// try {
//			// // read this file into InputStream
//			// inputStream = ipStream;
//			//
//			// // write the inputStream to a FileOutputStream
//			// outputStream =
//			// new FileOutputStream(new
//			// File("/android-assets/slideshow1.pptx"));
//			//
//			// int read = 0;
//			// byte[] bytes = new byte[1024];
//			//
//			// while ((read = inputStream.read(bytes)) != -1) {
//			// outputStream.write(bytes, 0, read);
//			// }
//			//
//			// System.out.println("Done!");
//			//
//			// } catch (IOException e) {
//			// e.printStackTrace();
//			// } finally {
//			// if (inputStream != null) {
//			// try {
//			// inputStream.close();
//			// } catch (IOException e) {
//			// e.printStackTrace();
//			// }
//			// }
//			// if (outputStream != null) {
//			// try {
//			// // outputStream.flush();
//			// outputStream.close();
//			// } catch (IOException e) {
//			// e.printStackTrace();
//			// }
//			//
//			// }
//			// }
//
//			// PresentationMLPackage presentationMLPackage =
//			// (PresentationMLPackage)OpcPackage.load(new
//			// java.io.File("assets/slideshow.pptx"));
//
//			// int resourceId =
//			// getResources().getIdentifier("com.your.package:raw/somefile.txt");
//			// File f = new File(getResources().openRawResource(resourceId));
//
//			File f = new File(getCacheDir() + "/slideshow.pptx");
//			if (!f.exists())
//				try {
//
//					// InputStream is = getAssets().open("slideshow.pptx");
//
//					InputStream is = getAssets().open("slideshow.pptx");
//
//					// Reader is = new BufferedReader(new InputStreamReader(raw,
//					// "UTF8"));
//
//					int size = is.available();
//					byte[] buffer = new byte[size];
//					is.read(buffer);
//					is.close();
//
//					FileOutputStream fos = new FileOutputStream(f);
//					fos.write(buffer);
//					fos.close();
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//
//			File inFile = new java.io.File(System.getProperty("user.dir") + "/slideshow.pptx");
//			boolean okay = inFile.exists();
//
//			InputStream is = getAssets().open("slideshow.pptx");
//			// final LoadFromZipNG loader = new LoadFromZipNG();
//
//			// PresentationMLPackage presentationMLPackage =
//			// (PresentationMLPackage)loader.get(is);
//			PresentationMLPackage presentationMLPackage = (PresentationMLPackage) PresentationMLPackage
//					.load(getAssets().open("slideshow.pptx"));
//			// String inputfilepath = System.getProperty("user.dir") +
//			// "/sample-docs/pptx/pptx-basic.xml";
//			// String inputfilepath = System.getProperty("user.dir") +
//			// "/sample-docs/pptx/lines.pptx";
//
//			// Where to save images
//			SvgExporter.setImageDirPath(getCacheDir() + "/slideshow.xhtml");
//
//			// TODO - render slides in document order!
//			Iterator partIterator = presentationMLPackage.getParts().getParts().entrySet().iterator();
//			while (partIterator.hasNext()) {
//
//				Map.Entry pairs = (Map.Entry) partIterator.next();
//
//				Part p = (Part) pairs.getValue();
//				if (p instanceof SlidePart) {
//
//					try {
//						System.out.println(SvgExporter.svg(presentationMLPackage, (SlidePart) p));
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//
//			// NB: file suffix must end with .xhtml in order to see the SVG in a
//			// browser
//		} catch (Docx4JException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		System.out.println("\n\n done .. \n\n");

		documentView = (WebView) findViewById(R.id.document_view);

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

		documentView.loadUrl(getCacheDir() + "/Protection.html");
		// "file:///android_asset/protection/Protection.html"
		//getCacheDir() + "/Protection.html"

		// try {
		// XMLSlideShow ppt = new XMLSlideShow(new
		// FileInputStream("slideshow.pptx"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
			//HMAC_SHA1_ALGORITHM
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

//	public static InputStream ProcessCommand(String strURI, String strHttpCommand) throws Exception {
//
//
////		URL address = new URL(strURI);
////		HttpURLConnection httpCon = (HttpURLConnection) address.openConnection();
////		httpCon.setDoOutput(true);
////
////		httpCon.setRequestProperty("Content-Type", "application/json");
////		httpCon.setRequestProperty("Accept", "text/json");
////		httpCon.setRequestMethod(strHttpCommand);
////		if (strHttpCommand.equals("PUT") || strHttpCommand.equals("POST"))
////			httpCon.setFixedLengthStreamingMode(0);
////		String d = httpCon.getResponseMessage();
////		System.out.println(d);
////		return httpCon.getInputStream();
//	}

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

}

class GetDocumentFromServer extends AsyncTask<String, Void, InputStream> {
	
	File outputPath;

	@Override
	protected InputStream doInBackground(String... args) {
		InputStream result = null;
		String strURI = args[0];
		String strHttpCommand = args[1];
		outputPath = new File(args[2]);
		URL address;
		try {
			address = new URL(strURI);
		
		HttpURLConnection httpCon = (HttpURLConnection) address.openConnection();
//		httpCon.setDoOutput(true);
//
//		httpCon.setRequestProperty("Content-Type", "application/json");
//		httpCon.setRequestProperty("Accept", "text/json");
//		httpCon.setRequestMethod(strHttpCommand);
//		if (strHttpCommand.equals("PUT") || strHttpCommand.equals("POST"))
//			httpCon.setFixedLengthStreamingMode(0);
//		String d = httpCon.getResponseMessage();
//		System.out.println(d);
		result = httpCon.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
    protected void onPostExecute(InputStream inputStream) {
        // TODO: check this.exception 
        // TODO: do something with the feed
		OutputStream outputStream = null;
		try {
			// read this file into InputStream

			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(outputPath);

            int read = 0;
            byte[] bytes = new byte[8192];

            while ((read = inputStream.read(bytes)) != -1) {
               outputStream.write(bytes, 0, read);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
