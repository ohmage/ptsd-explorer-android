package gov.va.ptsd.ptsdcoach.activities;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.image.Image;
import gov.va.ptsd.ptsdcoach.image.ImageUtil;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



public class ImageEditListActivity extends ListActivity {

	UserDBHelper userDb ;
	
	ImagesUriAdapter adapter;
	private final int MENU_ITEM_DELETE= Menu.FIRST;
	Button add,delete;
	final static int SELECT_PHOTO=10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTitle("Soothing Images");

		//R.layout.
		setContentView(R.layout.imageedit_list);
		

		userDb = UserDBHelper.instance(this);
		// Inform the list that we provide context menus for items
		getListView().setOnCreateContextMenuListener(this);
		
		
		LinearLayout lay = (LinearLayout) findViewById(R.id.audiolist);
		// Add Upgrade Button
		
		// Add Footer
		//lay.addView(getFooterView(this));
		
		// Add click listener to Header's Add button
		findViewById(R.id.add_btn).setOnClickListener(clickHandler);
		//findViewById(R.id.delete_btn).setOnClickListener(clickHandler);

		// UI thread handler
	

		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.AXIS_SPECIFIED;
		LayoutInflater linflater = LayoutInflater.from(this);

	
		
		

		fillData();
		

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {

		//menu.setHeaderTitle(R.string.menu_title);

		menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	    
	   
	    switch(requestCode) { 
	  
	    case SELECT_PHOTO:
	    	 if(resultCode == RESULT_OK){  
		            Uri selectedImage= imageReturnedIntent.getData();
		            Log.e("PTSD",selectedImage.toString());
		            userDb.addImage(selectedImage.toString());
		            fillData();
		            
			 }
	    	  
	    }
	  
	    
	}
	

	@Override
	public void onResume() {
		super.onResume();
		
		fillData();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void fillData() {
		
	
		List<Image> images=ImageUtil.UriListToImageList(userDb.getAllImages(),this);
		adapter = new ImagesUriAdapter(this, R.layout.row_image,images);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	

	

	private final Runnable listUpdater = new Runnable() {
		public void run() {
			fillData();

		}
	};
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		List<Image> images=ImageUtil.UriListToImageList(userDb.getAllImages(),this);

		Image im=images.get(position);
		Intent intent = new Intent();  
		intent.setAction(android.content.Intent.ACTION_VIEW);  

		intent.setDataAndType(im.getimageUril(), "image/*");  
		this.startActivity(intent);
		
	}

	
	
	/*
	 * Dialog(s)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		
		}
		return null;
	}

	

	/*
	 * Options Menu
	 */
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	
	

	/*
	 * Event handler(s)
	 */
	View.OnClickListener clickHandler = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.add_btn:
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, SELECT_PHOTO);
					break;
				
				default:
					return;
			}
		}
	};

	

	

	

	
	class ViewHolder {
		
		TextView name;
		//TextView delete;
	}

	/**
	 * Customized Adapter Class
	 * ####################################################################
	 */
	private class ImagesUriAdapter extends ArrayAdapter<Image> {

		private LayoutInflater li;
		private List<Image> images;

		public ImagesUriAdapter(Context context, int textViewResourceId,
				List<Image> items) {
			super(context, textViewResourceId, items);

			this.images = items;
			li = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ViewHolder holder;
			final Image a = this.images.get(position);

			if (convertView == null) {
				convertView = li.inflate(R.layout.row_image, null);
				//holder = new ViewHolder();
				TextView tv = (TextView) convertView.findViewById(R.id.image_name);
				tv.setBackgroundDrawable(null);
				tv.setBackgroundColor(0);
				ImageView iv = (ImageView) convertView.findViewById(R.id.image);
				Log.e("PTSD","--->"+a.getName());
				iv.setImageBitmap(a.getBitmap());
				if(a.getName().length()>30){
					tv.setText(a.getName().substring(0, 30));
				}else{
					tv.setText(a.getName());
				}
				convertView.setTag(tv);
			}else{
				//holder = (ViewHolder) convertView.getTag();
			}
			
			

			return convertView;
		}


	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo itemInfo;
		try {
			itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			return false;
		}

		Uri uri=userDb.getAllImages().get(itemInfo.position);
		if (uri == null) {
			return false;
		}

		switch (item.getItemId()) {
		
		case MENU_ITEM_DELETE:
			try {
				userDb.deleteImage(uri.toString());
			} catch (Exception e) {
				
			}
			fillData();
			return true;
		}
		return false;
	}
}
