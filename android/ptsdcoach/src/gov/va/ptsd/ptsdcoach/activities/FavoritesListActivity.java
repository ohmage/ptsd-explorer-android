package gov.va.ptsd.ptsdcoach.activities;

import gov.va.ptsd.ptsdcoach.ContentDBHelper;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FavoritesListActivity extends ListActivity {

	ContentDBHelper db;
	UserDBHelper userDb;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setTitle("Favorite Exercises");
		
		db = ContentDBHelper.instance(this);
		userDb = UserDBHelper.instance(this);
		
		// We'll define a custom screen layout here (the one shown above), but
		// typically, you could just use the standard ListActivity layout.
		setContentView(R.layout.favorites_list);
		WebView wv = (WebView)getWindow().findViewById(android.R.id.empty);
		ContentViewControllerBase.fillWebViewWithContent(wv, db.getContentForUri(getIntent().getData()));

		// Query for all people contacts using the Contacts.People convenience class.
		// Put a managed wrapper around the retrieved cursor so we don't have to worry about
		// requerying or closing it as the activity changes state.
		
		cursor = userDb.getFavorites();
		startManagingCursor(cursor);

		// Now create a new list adapter bound to the cursor.
		// SimpleListAdapter is designed for binding to a Cursor.
		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				android.R.layout.simple_list_item_1,  // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
				cursor,                                              // Pass in the cursor to bind to.
				new String[] {"name"},           // Array of cursor columns to bind to.
				new int[] {android.R.id.text1});  // Parallel array of which template objects to bind to those columns.

				// Bind to our new adapter.
				setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		cursor.moveToPosition(position);
		String uniqueID = cursor.getString(cursor.getColumnIndex("exerciseUniqueID"));
		Intent resultIntent = new Intent((String)null);
		resultIntent.setData(Uri.parse("contentUniqueID:"+uniqueID));
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
