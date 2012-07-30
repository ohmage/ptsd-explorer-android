package gov.va.ptsd.ptsdcoach.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;

import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.audio.Audio;
import gov.va.ptsd.ptsdcoach.audio.AudioUtil;
import gov.va.ptsd.ptsdcoach.contact.Contact;
import gov.va.ptsd.ptsdcoach.contact.ContactUtil;
import gov.va.ptsd.ptsdcoach.content.Content;

import gov.va.ptsd.ptsdcoach.image.Image;
import gov.va.ptsd.ptsdcoach.image.ImageUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class CrisisController extends SimpleExerciseController {

	ContactToStringAdapter adapter ;
	UserDBHelper userDb;
	Context con;
	Spinner contactList;
	public CrisisController(Context ctx) {
		super(ctx);
		con = ctx;
		userDb = UserDBHelper.instance(ctx);
	}

	class JSInterface {
		public void listen() {
			playAudio();
		}
	}
	
	public String checkPrerequisites() {
		if (userDb.getAllContacts().size() > 0) return null;
		return "You haven't chosen any support contacts.  Go to Setup and choose some contacts before you can use this tool.";
	}

	@Override
	public void build() {
		if ("seekSupport".equals(getContent().getName())) {
			super.build();
		} else {
			nonExerciseBuild();
		}
		
		for (Content child : getContent().getChildren()) {

            String name = child.getName();

            if ((name == null) || name.startsWith("@")) {

                            String special = child.getStringAttribute("special");

                            Content headerContent = getContent().getChildByName("@"+special);

                            addMainTextContent(headerContent.getMainText());

                            if (special.equals("contacts")) 
                            {

                                            // Add in the contacts
                        		final List<Contact> contacts=ContactUtil.UriListToContactList(userDb.getAllContacts(),con);
                        		
                        		if(contacts.size()>0)
                        		{
                        			contactList = new Spinner(getContext());
                        			adapter = new ContactToStringAdapter(con, R.layout.contact_picker, contactArrayToStringArray(contacts));
                        		
                         			LinearLayout.LayoutParams spinnerLayout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
                        			contactList.setLayoutParams(spinnerLayout);
               
                        			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        			clientView.addView(contactList);
                        			
                        			contactList.setPrompt("Choose a support contact");
                        			
                        			contactList.setAdapter(adapter);
                        			
                        			Button call= new Button(con);
                        			call.setText("Call this contact");
                        			call.setOnClickListener(new View.OnClickListener()
                        			{
                        				public void onClick(View v)
                        				{
                        						int pos=contactList.getSelectedItemPosition();
                        						Contact cont=contacts.get(pos);
                        						if(cont==null){
                        							return;
                        						}
                        						String number = cont.getNumber();
                        						if(number!=null)
                        						{
                               						Log.e("PTSD-contact","-num-->"+cont.getNumber());
                        							Intent in=new Intent(Intent. ACTION_DIAL,Uri.parse("tel:"+cont.getNumber()));
                        							con.startActivity(in);
                        						}
                        						
                        				
                        				}
                        			}
                        		
                        			);
                        			clientView.addView(call);
                        			
                        		 }
                        		
                            } else {
                            	
                        		LinearLayout.LayoutParams buttonlayout = new LinearLayout.LayoutParams(
                        				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                        		Button button= new Button(this.con);
                        		button.setLayoutParams(buttonlayout);
                        		final String button_1_Name=child.getDisplayName();
                        		
                        		button.setText(button_1_Name);
                        		clientView.addView(button);
                        		button.setOnClickListener(new View.OnClickListener() {
                        			public void onClick(View v) {
                        					Intent in=new Intent(Intent. ACTION_DIAL,Uri.parse("tel:"+button_1_Name.split(" ")[1]));
                        					con.startActivity(in);
                        			}
                        		});

                            }

            }

}
		
	
	}
	List<String> contactArrayToStringArray(List<Contact> contacts){
		List<String> audioNames=new ArrayList<String>();
		for(Contact c : contacts){
			String name = "(no name)";
			if(c==null) continue;

			if (c.getName()!=null){
				name = c.getName();
			}
			
			if (c.getNumber()!=null){
				name = name +" - "+c.getNumber();
			}
			
			audioNames.add(name);
		}
		return audioNames;
	}

	private class ContactToStringAdapter extends ArrayAdapter<String> {

		private LayoutInflater li;
		private List<String> Strings;

		public ContactToStringAdapter(Context context, int textViewResourceId,
				List<String> items) {
			super(context, textViewResourceId, items);

			this.Strings = items;
			li = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ViewHolder holder;
			final String s = this.Strings.get(position);
			
			if (convertView == null) {
				
				convertView = li.inflate(R.layout.contact_picker, null);
				//holder = new ViewHolder();
				if(s==null){
					return convertView;
				}
				TextView tv = (TextView) convertView.findViewById(R.id.contact_name);
//				tv.setTextColor(Color.BLACK);
//				if(s.length()>25){
	//				tv.setText(s.substring(0, 25));
		//		}else {
					tv.setText(s);
		//		}
				convertView.setTag(tv);
			}else{
				
			}
			
			

			return convertView;
		}


	}

	@Override
	public void parentActivityPaused()
	{
		PTSDCoach a =(PTSDCoach)getNavigator().getParent();
		
		if(a.fromBackground)
		{
			//we are going into the background,go back
			getNavigator().popToRoot();
			getNavigator().goBack();
		}
	}
}
