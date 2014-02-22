package com.wbc.wbcapp.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wbc.wbc.R;

public class LoginActivity extends Activity {
	
	Button loginButton;
	EditText userNameInput;
	TextView authText;
	String userName;
	Activity mActivity;
	ProgressDialog pd;
	String auth_url = "http://hardindd.com/stage/wbc/backend/auth.php?email=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mActivity = this;
		
		// Check to see if user has logged in before
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (prefs.getBoolean("auth", false)){
		    // Already authenticated
			Intent intent = new Intent(mActivity, UserContentsActivity.class);
			startActivity(intent);
		}
		loginButton = (Button)findViewById(R.id.loginButton);
		userNameInput = (EditText)findViewById(R.id.username);
		authText = (TextView)findViewById(R.id.loginErr);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userName = userNameInput.getText().toString();
				RequestQueue queue = Volley.newRequestQueue(mActivity);
				pd = ProgressDialog.show(mActivity, "", "Loading..."); //set progressdialog here
				
				StringRequest request = new StringRequest(Request.Method.GET,
						auth_url + userName,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
		                    pd.dismiss();
							try {
			    				JSONObject object = new JSONObject(response);
			    				String status = object.getString("status");
			    				if(status.equals("valid")){

				                    // Check auth here
				                	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				                	Editor editor = prefs.edit();
				                	editor.putString("email", userName);
				                	editor.putBoolean("auth", true);
				                	editor.commit();
				                	
									Intent intent = new Intent(mActivity, UserContentsActivity.class);
									startActivity(intent);
			    				
			    				} else {
			    					authText.setText("That email is invalid. Please try again.");
			    				}
			    				
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						pd.dismiss();
	                                                //let user know that something failed
					}
				});

				queue.add(request);
				
			}
		});
		
	}

}
