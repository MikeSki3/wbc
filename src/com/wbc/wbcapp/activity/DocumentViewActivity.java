package com.wbc.wbcapp.activity;

import com.wbc.wbc.R;
import com.wbc.wbc.R.layout;
import com.wbc.wbc.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DocumentViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_view, menu);
		return true;
	}

}
