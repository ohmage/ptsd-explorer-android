package gov.va.ptsd.ptsdcoach.content;

import android.database.Cursor;

public class PCLScore {
	public int score;
	public long time;
	
	public PCLScore(Cursor c) {
		score = c.getInt(c.getColumnIndex("score"));
		time = c.getLong(c.getColumnIndex("time"));
	}
}
