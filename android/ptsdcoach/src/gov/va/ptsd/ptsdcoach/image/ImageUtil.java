package gov.va.ptsd.ptsdcoach.image;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtil {
	public static Image UriToImage(Uri uri, Context con) {
		String[] projection = new String[] { MediaStore.Images.Media.DISPLAY_NAME,

		};

		Cursor managedCursor =  con.getContentResolver().query(uri, projection, // Which
																		// columns
																		// to
																		// return
				null, // Which rows to return (all rows)
				null, // Selection arguments (none)
				// Put the results in ascending order by name
				MediaStore.Images.Media.DISPLAY_NAME + " ASC");
		if (managedCursor!=null && managedCursor.moveToFirst()) {
			int nameColumn = managedCursor
					.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
			String name = managedCursor.getString(nameColumn);
			String uri_string = uri.toString();
			int originalImageId = Integer.parseInt(uri_string.substring(
					uri_string.lastIndexOf("/") + 1, uri_string.length()));
			Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
					con.getContentResolver(), originalImageId,
					MediaStore.Images.Thumbnails.MINI_KIND, null);
			managedCursor.close();
			return new Image(uri, name, bm);
		}
		if(managedCursor!=null){
			managedCursor.close();
		}
		return null;
	}

	

	public static ArrayList<Image> UriListToImageList(List<Uri> uris,
			Context con) {
		ArrayList<Image> images = new ArrayList<Image>();
		for (Uri each : uris) {
			Image a = UriToImage(each, con);
			images.add(a);
		}
		return images;
	}
}
