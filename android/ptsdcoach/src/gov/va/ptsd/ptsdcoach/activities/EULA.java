package gov.va.ptsd.ptsdcoach.activities;



import gov.va.ptsd.ptsdcoach.ContentDBHelper;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.content.Content;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class EULA extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Content c = ContentDBHelper.instance(this).getContentForName("EULA");
		setContentView(c.createContentView(this));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}	
	
}
	
	
