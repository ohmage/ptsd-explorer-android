package gov.va.ptsd.ptsdcoach.activities;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.services.TtsContentProvider;
import gov.va.ptsd.ptsdcoach.services.Unzipper;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class SplashActivity extends Activity {

	File dataDir;
	
	gov.va.ptsd.ptsdcoach.Util dummy = new gov.va.ptsd.ptsdcoach.Util();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		dataDir = getDir("accessibility", MODE_PRIVATE);
		new InitializeApp().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}
	
	private class InitializeApp extends AsyncTask<Void, String, Void> {

		@Override
		protected void onProgressUpdate(String... values) {
			
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {
			long then = System.currentTimeMillis();
			
			try {
				TtsContentProvider.constructUri("/0/ ");
				ContentResolver contentResolver = getContentResolver();
				contentResolver.acquireContentProviderClient(TtsContentProvider.constructUri("/0/ "));
//				URL url = new URL("content://gov.va.ptsd.ptsdcoach.services.tts/0/ ");
//				url.getContent();
				
				File idealLoaderScript = new File(dataDir, "ideal-webaccess/js/ideal-webaccess.user.js");
				boolean result = idealLoaderScript.exists();
				if (!result) {
					Resources res = getResources();
					AssetFileDescriptor jsZipFd = res.openRawResourceFd(R.raw.ideal_js);
					InputStream stream = jsZipFd.createInputStream();
					result = Unzipper.unzip(dataDir,stream);
				}
			} catch (Exception e) {}

			long now = System.currentTimeMillis();
			long delta = now-then;
			if (delta < 2000) {
				try {
					Thread.sleep(2000-delta);
				} catch (Exception e) {}
			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			
			
			String launchedOnce = UserDBHelper.instance(SplashActivity.this).getSetting("launchedOnce");
			if (launchedOnce == null) {
				startActivityForResult(new Intent(getApplicationContext(), EULA.class), RESULT_FIRST_USER);
			} else {
				startActivityForResult(new Intent("gov.va.ptsd.ptsdcoach.PTSDCoach"), RESULT_FIRST_USER);
			}
			
			super.onPostExecute(result);		
		}
	}
	
}
	
	
