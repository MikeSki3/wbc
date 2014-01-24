package com.wbc.wbcapp.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wbc.wbc.R;
import com.wbc.wbcapp.adapter.UserContentsListAdapter;

public class UserContentsActivity extends Activity {
	
	Activity mActivity;
	
	UserContentsListAdapter adapter;
	ArrayList<String> documents = new ArrayList<String>();
	ListView contentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_contents);
		mActivity = this;
		contentList = (ListView)findViewById(R.id.content_list);
		char letter = 'A';
		for(int i = 0; i < 26; i++){
			documents.add("Doc " + letter);
			letter++;
		}
		adapter = new UserContentsListAdapter(mActivity, documents);
		contentList.setAdapter(adapter);
		contentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(mActivity, DocumentViewActivity.class);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_contents, menu);
		return true;
	}

}
