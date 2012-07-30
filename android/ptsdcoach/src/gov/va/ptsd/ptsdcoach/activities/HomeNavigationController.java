package gov.va.ptsd.ptsdcoach.activities;

import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.content.ContentActivity;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class HomeNavigationController extends NavigationController{

	MenuItem setupItem; 
	MenuItem aboutItem; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void goBack() {
		finish();
	}

	public void gotoSetup() {
		Intent intent = new Intent(this,SetupActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == aboutItem) {
			Intent intent = new Intent(this,ContentActivity.class);
			intent.setData(Uri.parse("contentName:about"));
			startActivity(intent);
			return true;
		}

		if (item == setupItem) {
			gotoSetup();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		aboutItem = menu.add("About");
		aboutItem.setIcon(android.R.drawable.ic_menu_info_details);
		setupItem = menu.add("Setup");
		setupItem.setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

}
