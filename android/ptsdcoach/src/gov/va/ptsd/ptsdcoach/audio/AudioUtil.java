package gov.va.ptsd.ptsdcoach.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


public class AudioUtil {
	public static Audio UriToAudio(Uri uri,Context con){
		String[] projection = new String[] {
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DISPLAY_NAME
		};
		
		
		 Log.e("PTSD","UriToAudio_URI--"+uri.toString());
		Cursor managedCursor = con.getContentResolver().query(uri,
				projection, // Which columns to return 
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                // Put the results in ascending order by name
                MediaStore.Audio.Media.DISPLAY_NAME + " ASC");

		if(managedCursor==null)
		{

			File f = new File("" + uri);

        	return new Audio(uri,f.getName(),"");			 
		}
		
		 if (managedCursor!=null && managedCursor.moveToFirst())
		 {
		        int artistColumn = 0;//managedCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST); 
		        int nameColumn =1;// managedCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
	        	String artist = managedCursor.getString(artistColumn);
	        	String name = managedCursor.getString(nameColumn);
	        	managedCursor.close();
	        	return new Audio(uri,name,artist);
		 }
		 if (managedCursor!=null)
		 {
				 managedCursor.close();
		 }
		return null;
	}
	
	public static ArrayList<Audio> UriListToAudioList(List<Uri> uris,Context con){
		ArrayList<Audio> audios= new ArrayList<Audio>();
		for(Uri each : uris){
			Audio a=UriToAudio(each,con);
			if(a!=null)
				audios.add(a);
		}
		return audios;
	}
	
}
