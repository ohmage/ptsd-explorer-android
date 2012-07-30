package gov.va.ptsd.ptsdcoach.activities;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.audio.Audio;
import gov.va.ptsd.ptsdcoach.audio.AudioUtil;
import gov.va.ptsd.ptsdcoach.image.Image;
import gov.va.ptsd.ptsdcoach.image.ImageUtil;

import java.util.List;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class AudioEditListActivity extends ListActivity {

	UserDBHelper userDb ;
	MediaPlayer mp=null;

	
	private Handler handler;
	AudioUriAdapter adapter;
	private final int MENU_ITEM_DELETE= Menu.FIRST;
	Button add,delete;
	final static int SELECT_AUDIO=11;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//
		setTitle("Soothing Audio");

		//R.layout.
		setContentView(R.layout.audioedit_list);
		

		userDb = UserDBHelper.instance(this);
		// Inform the list that we provide context menus for items
		getListView().setOnCreateContextMenuListener(this);
		
		
		LinearLayout lay = (LinearLayout) findViewById(R.id.audiolist);
	
		
		// Add click listener to Header's Add button
		findViewById(R.id.add_btn).setOnClickListener(clickHandler);
		//findViewById(R.id.delete_btn).setOnClickListener(clickHandler);

		// UI thread handler
		handler = new Handler();

		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.AXIS_SPECIFIED;
		LayoutInflater linflater = LayoutInflater.from(this);

		fillData();
		

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {

		menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent audioReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, audioReturnedIntent); 
	    
	   
	    switch(requestCode) { 
	  
	    case SELECT_AUDIO:
	    	 if(resultCode == RESULT_OK){  
		            Uri selectedAudio= audioReturnedIntent.getData();
		            Log.e("PTSD",selectedAudio.toString());
		            userDb.addAudio(selectedAudio.toString());
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
		
		if(mp!=null && mp.isPlaying())
			mp.stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void fillData() {
		
		List<Audio> audios=AudioUtil.UriListToAudioList(userDb.getAllAudio(),this);
		adapter = new AudioUriAdapter(this, R.layout.list_item,audios);
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
		List<Audio> audios=AudioUtil.UriListToAudioList(userDb.getAllAudio(),this);

		Audio audio= audios.get(position);
		
		Uri uri=audio.getAudioUril();
		if(uri==null){
			return;
		}
		
		try{
			if(mp!=null && mp.isPlaying())
				mp.stop();
			
			mp=MediaPlayer.create(this, uri);
			mp.start();
			
		}catch(Exception e){
		//	Log.e("PTSD","Playing :"+e.getMessage());
		}

		
/*		
		Intent intent = new Intent();  
		intent.setAction(android.content.Intent.ACTION_VIEW);  

		intent.setDataAndType(audio.getAudioUril(), "audio/*");  
		this.startActivity(intent);
		*/
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
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		            intent.setType("audio/*");
		           // startActivityForResult(Intent.createChooser(i, "Select audio file"), Enums.REQUEST_CODE_SELECT_ATTACHMENT);
		            startActivityForResult(intent, SELECT_AUDIO);
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
	private class AudioUriAdapter extends ArrayAdapter<Audio> {

		private LayoutInflater li;
		private List<Audio> Audios;

		public AudioUriAdapter(Context context, int textViewResourceId,
				List<Audio> items) {
			super(context, textViewResourceId, items);

			this.Audios = items;
			li = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ViewHolder holder;
			final Audio a = this.Audios.get(position);
			
			if (convertView == null) {
				convertView = li.inflate(R.layout.list_item, null);
				//holder = new ViewHolder();

				TextView tv = (TextView) convertView.findViewById(R.id.term);
				Log.e("PTSD-Audio","-Audio-->"+a.getName());
				
				tv.setText(a.getName());
				
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

		Uri uri=userDb.getAllAudio().get(itemInfo.position);
		if (uri == null) {
			return false;
		}

		switch (item.getItemId()) {
		
		case MENU_ITEM_DELETE:
			try {
				userDb.deleteAudio(uri.toString());
			} catch (Exception e) {
				
			}
			fillData();
			return true;
		}
		return false;
	}
}
