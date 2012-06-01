package com.buildingblocks.camerapoc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class LoginActivity extends Activity {

	Facebook facebook = new Facebook("371825396199782");
	private SharedPreferences mPrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		/*
		 * Get an access token if there is one
		 */
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token",null);
		long expires = mPrefs.getLong("access_expires",0);
		if(access_token!=null) {
			facebook.setAccessToken(access_token);
		}
		if(expires!=0) {
			facebook.setAccessExpires(expires);
		}
		
		if(facebook.isSessionValid()) {
			Intent intent = new Intent(this,CameraPOCActivity.class);
			startActivity(intent);
		}
		
		
	}
	
	public void facebookLogin(View view) {
		if(!facebook.isSessionValid()) {
			
			facebook.authorize(this, new DialogListener() {
				
				@Override
				public void onComplete(Bundle values) {
					if(facebook.isSessionValid()) {
						Intent intent = new Intent(getBaseContext(),CameraPOCActivity.class);
						startActivity(intent);
					}
				}
				
				@Override
				public void onFacebookError(FacebookError error) {}
				
				@Override 
				public void onError(DialogError e) {}
				
				@Override
				public void onCancel() {}
			});		
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);		
		facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
}
