package com.wbc.wbcapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wbc.wbc.R;

public class LoginActivity extends Activity {
	
	Button loginButton;
	EditText userNameInput, passwordInput;
	String userName, password;
	Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mActivity = this;
		loginButton = (Button)findViewById(R.id.loginButton);
		userNameInput = (EditText)findViewById(R.id.username);
		passwordInput = (EditText)findViewById(R.id.password);
		userName = userNameInput.getText().toString();
		password = passwordInput.getText().toString();
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(userName != null && password != null){
					Intent intent = new Intent(mActivity, UserContentsActivity.class);
					startActivity(intent);
				}
				
			}
		});
	}

}
