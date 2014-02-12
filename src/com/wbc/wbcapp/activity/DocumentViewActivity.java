package com.wbc.wbcapp.activity;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
import org.pptx4j.convert.out.svginhtml.SvgExporter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wbc.wbc.R;

public class DocumentViewActivity extends Activity {
	
	WebView documentView; 
	Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_document_view);
		mActivity = this;

		try {
			
			AssetManager aMgr = mActivity.getAssets();
			InputStream ipStream = aMgr.open("slideshow.pptx");
			String filePath = getFilesDir().getAbsolutePath();
			InputStream inputStream = null;
			OutputStream outputStream = null;
		 
//			try {
//				// read this file into InputStream
//				inputStream = ipStream;
//		 
//				// write the inputStream to a FileOutputStream
//				outputStream = 
//		                    new FileOutputStream(new File("/android-assets/slideshow1.pptx"));
//		 
//				int read = 0;
//				byte[] bytes = new byte[1024];
//		 
//				while ((read = inputStream.read(bytes)) != -1) {
//					outputStream.write(bytes, 0, read);
//				}
//		 
//				System.out.println("Done!");
//		 
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (inputStream != null) {
//					try {
//						inputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (outputStream != null) {
//					try {
//						// outputStream.flush();
//						outputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//		 
//				}
//			}
			
//			PresentationMLPackage presentationMLPackage = 
//				(PresentationMLPackage)OpcPackage.load(new java.io.File("assets/slideshow.pptx"));
			
//			int resourceId = getResources().getIdentifier("com.your.package:raw/somefile.txt");
//			File f = new File(getResources().openRawResource(resourceId));
			
			
			
			 File f = new File(getCacheDir()+"/slideshow.pptx");
			  if (!f.exists()) try {

//			    InputStream is = getAssets().open("slideshow.pptx");
				
				  InputStream is = getAssets().open("slideshow.pptx");

				  //Reader is = new BufferedReader(new InputStreamReader(raw, "UTF8"));
				
			    int size = is.available();
			    byte[] buffer = new byte[size];
			    is.read(buffer);
			    is.close();


			    FileOutputStream fos = new FileOutputStream(f);
			    fos.write(buffer);
			    fos.close();
			  } catch (Exception e) { throw new RuntimeException(e); }
			
			File inFile = new java.io.File(System.getProperty("user.dir") + "/slideshow.pptx");
			boolean okay = inFile.exists();
			
		    InputStream is = getAssets().open("slideshow.pptx");
		    //final LoadFromZipNG loader = new LoadFromZipNG();
			
//		    PresentationMLPackage presentationMLPackage = (PresentationMLPackage)loader.get(is);
			PresentationMLPackage presentationMLPackage = (PresentationMLPackage) PresentationMLPackage.load(getAssets().open("slideshow.pptx"));
			//String inputfilepath = System.getProperty("user.dir") + "/sample-docs/pptx/pptx-basic.xml";
//			String inputfilepath = System.getProperty("user.dir") + "/sample-docs/pptx/lines.pptx";

	    	// Where to save images
	    	SvgExporter.setImageDirPath(getCacheDir() + "/slideshow.xhtml");	

			// TODO - render slides in document order!
			Iterator partIterator = presentationMLPackage.getParts().getParts().entrySet().iterator();
		    while (partIterator.hasNext()) {

		        Map.Entry pairs = (Map.Entry)partIterator.next();

		        Part p = (Part)pairs.getValue();
		        if (p instanceof SlidePart) {

		        	try {
						System.out.println(
								SvgExporter.svg(presentationMLPackage, (SlidePart)p)
								);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }

		    // NB: file suffix must end with .xhtml in order to see the SVG in a browser
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		System.out.println("\n\n done .. \n\n");
		
		documentView = (WebView) findViewById(R.id.document_view);

		 documentView.getSettings().setJavaScriptEnabled(true);

		 final Activity activity = this;
		 documentView.setWebChromeClient(new WebChromeClient() {
		   public void onProgressChanged(WebView view, int progress) {
		     // Activities and WebViews measure progress with different scales.
		     // The progress meter will automatically disappear when we reach 100%
		     activity.setProgress(progress * 1000);
		   }
		 });
		 documentView.setWebViewClient(new WebViewClient() {
		   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		   }
		 });

		
		documentView.loadUrl(getCacheDir() + "/slideshow.xhtml");
		
		
//		try {
//			XMLSlideShow ppt = new XMLSlideShow(new FileInputStream("slideshow.pptx"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_view, menu);
		return true;
	}

}
