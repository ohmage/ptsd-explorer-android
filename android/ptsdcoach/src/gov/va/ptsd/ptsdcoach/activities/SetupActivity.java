package gov.va.ptsd.ptsdcoach.activities;

import java.util.List;

import gov.va.ptsd.ptsdcoach.ContentDBHelper;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.audio.Audio;
import gov.va.ptsd.ptsdcoach.audio.AudioUtil;
import gov.va.ptsd.ptsdcoach.contact.Contact;
import gov.va.ptsd.ptsdcoach.contact.ContactUtil;
import gov.va.ptsd.ptsdcoach.image.Image;
import gov.va.ptsd.ptsdcoach.image.ImageUtil;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SetupActivity extends ListActivity {

	ContentDBHelper db;
	UserDBHelper userDb;
	final static int SELECT_PHOTO=10;
	final static int SELECT_AUDIO=11;
	final static int PICK_CONTACT=12;
	final static int SETUP_FINISH=13;
	static private String[] OPTIONS = {
		"Choose Images",
		"Choose Songs or Audio",
		"Choose Support Contacts"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setTitle("PTSD Coach Setup");
		
		db = ContentDBHelper.instance(this);
		userDb = UserDBHelper.instance(this);
		
		// Now create a new list adapter bound to the cursor.
		// SimpleListAdapter is designed for binding to a Cursor.
		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,OPTIONS) {
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		       
		        
		        if (convertView == null) {
		            LayoutInflater inflater = LayoutInflater.from(this.getContext());
		            
		            convertView = inflater.inflate(R.layout.list_item,null);
		        }
        
		        TextView label = (TextView)convertView.findViewById(R.id.term);
		        label.setText(this.getItem(position));
		        
		        return convertView;
		    }			
		};
		setListAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, returnedIntent); 
	    
	   
	    switch(requestCode) { 
	    case SETUP_FINISH:
	    	return;
	    case SELECT_PHOTO:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = returnedIntent.getData();
	            userDb.addImage(selectedImage.toString());
	            Intent intent = new Intent("gov.va.ptsd.ptsdcoach.activities.ImageEditListActivity");
	            startActivityForResult(intent, SETUP_FINISH);
	        }
	   
	    break;
	    case SELECT_AUDIO:
	    	 if(resultCode == RESULT_OK){  
		            Uri selectedAudio= returnedIntent.getData();
		            Log.e("PTSD",selectedAudio.toString());
		            userDb.addAudio(selectedAudio.toString());
		            Intent intent = new Intent("gov.va.ptsd.ptsdcoach.activities.AudioEditListActivity");
		            startActivityForResult(intent, SETUP_FINISH);
		            
		            
			 }
	    	  
	    break;
	    case PICK_CONTACT:
	    	 if(resultCode == RESULT_OK){  
		            Uri selectedAudio= returnedIntent.getData();
		            Log.e("PTSD",selectedAudio.toString());
		            userDb.addContact(selectedAudio.toString());
		            Intent intent = new Intent("gov.va.ptsd.ptsdcoach.activities.ContactsEditListActivity");
		            startActivityForResult(intent, SETUP_FINISH);
			 }
	    }
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (position == 0) {
			List<Image> images=ImageUtil.UriListToImageList(userDb.getAllImages(),this);

            Intent intent = new Intent("gov.va.ptsd.ptsdcoach.activities.ImageEditListActivity");
            startActivityForResult(intent, SETUP_FINISH);
		} else if (position == 1) {
			List<Audio> audios=AudioUtil.UriListToAudioList(userDb.getAllAudio(),this);

			Intent intent = new Intent("gov.va.ptsd.ptsdcoach.activities.AudioEditListActivity");
			startActivityForResult(intent, SETUP_FINISH);
		} else if (position == 2) {
			userDb.getAllContacts();
			List<Contact> contacts=ContactUtil.UriListToContactList(userDb.getAllContacts(),this);
			
			Intent intent = new Intent("gov.va.ptsd.ptsdcoach.activities.ContactsEditListActivity");
			startActivityForResult(intent, SETUP_FINISH);
		}
		super.onListItemClick(l, v, position, id);
	}
}
