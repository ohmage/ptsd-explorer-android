package gov.va.ptsd.ptsdcoach.image;

import android.graphics.Bitmap;
import android.net.Uri;

public class Image {
	public Image(Uri imageUril, String name, Bitmap bitmap) {
		super();
		this.imageUril = imageUril;
		this.name = name;
		this.bitmap=bitmap;
		
		
	}
	Uri imageUril;
	public Uri getimageUril() {
		return imageUril;
	}
	public void setimageUril(Uri imageUril) {
		this.imageUril = imageUril;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getBitmap(){
		return this.bitmap;
	}
	Bitmap bitmap;
	String name;
	
	
}
