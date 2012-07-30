package gov.va.ptsd.ptsdcoach.content;

import java.io.IOException;
import java.io.OptionalDataException;

import android.database.Cursor;

public class Caption {
	public long startTime;
	public long endTime;
	public String text;
	
	public Caption(Cursor c) throws OptionalDataException, ClassNotFoundException, IOException {
		startTime = c.getLong(c.getColumnIndex("start"));
		endTime = c.getLong(c.getColumnIndex("end"));
		text = c.getString(c.getColumnIndex("text"));
	}

}
