package gov.va.ptsd.ptsdcoach.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class ContactUtil {
	

	public static Contact UriToContact(Uri uri, Context con) {

		// get the contact id from the Uri  
		String id = uri.getLastPathSegment(); 
		Cursor phoneCursor = null;  
		String phoneNum = ""; 
		String contact = "";
		// query for everything email  
		phoneCursor = con.getContentResolver().query(Phone.CONTENT_URI, null, "_id=?", new String[] { id }, null);  
//		phoneCursor = con.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);  

		int phoneIdx = phoneCursor.getColumnIndex(Phone.DATA);  

		// grab the first phone number 
		if (phoneCursor.moveToFirst()) {  
			phoneNum = phoneCursor.getString(phoneIdx);  
			Log.v("PTSD", "Got phone: " + phoneNum);  
		} else {  
			Log.w("PTSD", "No phone");  
		}  
		phoneCursor.close();

		//now grab the display name
		Cursor info = con.getContentResolver().query(uri, null, null, null, null);

		while(info.moveToNext()) {
			int nameFieldColumnIndex = info.getColumnIndex(Contacts.DISPLAY_NAME);
			contact = info.getString(nameFieldColumnIndex);
			Log.e("PTSD","--NAME-"+contact);
			break;
		}

		info.close();

		return new Contact(uri, contact,phoneNum);                 
	}

	public static ArrayList<Contact> UriListToContactList(List<Uri> uris,
			Context con) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		for (Uri each : uris) {
			Contact a = UriToContact(each, con);
			if(a!=null)
				contacts.add(a);
		}
		return contacts;
	}
}
