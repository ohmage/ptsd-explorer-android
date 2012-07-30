package gov.va.ptsd.ptsdcoach.contact;

import android.graphics.Bitmap;
import android.net.Uri;

public class Contact {
	public Contact(Uri contactUril, String name,String number) {
		super();
		this.contactUril = contactUril;
		this.name = name;
		this.number=number;
		
		
		
	}
	String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setContactUril(Uri contactUril) {
		this.contactUril = contactUril;
	}
	Uri contactUril;
	public Uri getContactUril() {
		return contactUril;
	}
	public void setimageUril(Uri contactUril) {
		this.contactUril = contactUril;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	String name;
	
	
}
