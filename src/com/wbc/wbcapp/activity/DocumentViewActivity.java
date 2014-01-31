package com.wbc.wbcapp.activity;

import android.app.Activity;
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

		
		documentView.loadUrl("file:///android_asset/slideshow.htm");
		
		
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
