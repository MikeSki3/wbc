package com.wbc.wbcapp.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wbc.wbc.R;
import com.wbc.wbcapp.adapter.UserContentsListAdapter;
import com.wbc.wbcapp.model.UsersDocument;

public class UserContentsActivity extends Activity {

	Activity mActivity;

	UserContentsListAdapter adapter;
	ArrayList<UsersDocument> documents = new ArrayList<UsersDocument>();
	ListView contentList;
	ProgressDialog pd;

	String files_url = "http://hardindd.com/stage/wbc/backend/file_list.php?email=";
	String auth_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_contents);
		mActivity = this;
		contentList = (ListView) findViewById(R.id.content_list);

		// Get auth email
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (prefs.getBoolean("auth", true)) {
			// Authenticated
			auth_email = prefs.getString("email", "DEFAULT");
		} else {
			finish();
		}

		RequestQueue queue = Volley.newRequestQueue(mActivity);
		pd = ProgressDialog.show(mActivity, "", "Loading...");

		/*
		 * test data char letter = 'A'; for(int i = 0; i < 26; i++){
		 * documents.add("Doc " + letter); letter++; }
		 */

		// Call JSON
		StringRequest request = new StringRequest(Request.Method.GET, files_url
				+ auth_email, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				pd.dismiss();
				JSONArray jArray;
				try {
					jArray = new JSONArray(response);

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject object = jArray.getJSONObject(i);
						UsersDocument document = new UsersDocument(
								object.getString("file"),
								object.getString("date"),
								object.getString("size"),
								object.getString("extension"));
						String file = object.getString("file");
						String date = object.getString("date");
						String size = object.getString("size");
						String extension = object.getString("extension");
						documents.add(document);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Populate list

				adapter = new UserContentsListAdapter(mActivity, documents);
				contentList.setAdapter(adapter);
				contentList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(mActivity,
								DocumentViewActivity.class);
						intent.putExtra("fileName", documents.get(arg2).getFile());
						intent.putExtra("extension", documents.get(arg2).getExtension());
						startActivity(intent);

					}
				});
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				pd.dismiss();
				// let user know that something failed
			}
		});
		// Get JSON
		queue.add(request);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_contents, menu);
		return true;
	}

}
