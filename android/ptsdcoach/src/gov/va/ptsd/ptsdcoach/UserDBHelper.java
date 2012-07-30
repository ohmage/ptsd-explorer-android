package gov.va.ptsd.ptsdcoach;

import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.content.PCLScore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.format.DateFormat;

public class UserDBHelper extends SQLiteOpenHelper {

	Context context;
	TreeMap<String, String> settings = new TreeMap<String, String>();
	
	static final String NO_VALUE = "NO_VALUE";
	
	private static final int DATABASE_VERSION = 1;
	private static String DB_NAME = "user.db";

    private static String DROP_PCL_SCORE_TABLE = "drop table if exists pclscore;";
    private static String PCL_SCORE_TABLE_CREATE = "create table pclscore (_id INTEGER PRIMARY KEY ASC, score INT, time INT);";

    private static String SETTING_TABLE_CREATE = "create table settings (_id INTEGER PRIMARY KEY ASC, name TEXT, value TEXT);";
    
    private static String IMAGE_TABLE_CREATE = "create table image (_id INTEGER PRIMARY KEY ASC, uri TEXT );";
    private static String AUDIO_TABLE_CREATE = "create table audio (_id INTEGER PRIMARY KEY ASC, uri TEXT);";
    private static String CONTACTS_TABLE_CREATE = "create table contact (_id INTEGER PRIMARY KEY ASC, uri TEXT );";
    
    
    private static String DROP_EXERCISE_SCORE_TABLE = "drop table if exists exercisescore;";
    private static String EXERCISE_SCORE_TABLE_CREATE = "create table exercisescore (_id INTEGER PRIMARY KEY ASC, name TEXT, score INT, exerciseUniqueID BLOB);";
    private static String EXERCISE_SCORE_INDEX1_CREATE = "create index exercisescore_score_idx on exercisescore (score);";
    private static String EXERCISE_SCORE_INDEX2_CREATE = "create index exercisescore_uniqueID_idx on exercisescore (exerciseUniqueID);";

    private ContentDBHelper contentDB;
    private static UserDBHelper instance;
    
    public static UserDBHelper instance(Context ctx) {
    	if (instance == null) {
    		instance = new UserDBHelper(ctx);
    	}
    	
    	return instance;
    }
    
    public Context getContext() {
		return context;
	}

	public UserDBHelper(Context ctx) {
		super(ctx, DB_NAME, null, DATABASE_VERSION);
		context = ctx;
		contentDB = ContentDBHelper.instance(ctx);
		createDemoData(getWritableDatabase());
	}
	
	private String[] demoFavorites = {
		"progressiveRelaxation",	
		"deepBreathing",
		"soothWithMyPictures",
		"soothWithMyAudio",
		"takeWalk",
		"rid",
		"positiveImagery1"
	};
	
	public void createDemoData(SQLiteDatabase db) {
		db.execSQL(DROP_PCL_SCORE_TABLE);
		db.execSQL(PCL_SCORE_TABLE_CREATE);

		int i;
		float fy = (float)(17*4.5);
		long startingTime = -120L * 24*60*60*1000;
		Date now = new Date();
		for ( i = 0; i < 15; i++ ) {
			long fx = i*8;
			int score = (int)(fy + (14 * Math.random() - 7));
			if (score < 17) score = 17;

			long ts = (long)(now.getTime() + startingTime+(fx*24*60*60*1000));
			long delta = (long)(now.getTime() - ts);
			long deltaDays = delta / (24*60*60*1000);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(ts);
			DateFormat df = new DateFormat();
			CharSequence s = df.format("MMMM dd, yyyy h:mmaa",cal);
			
	    	ContentValues values = new ContentValues(2);
	    	values.put("score", score);
	    	values.put("time", ts);
	    	db.insert("pclscore", null, values);
			
			fy = (float)(fy * 0.9);
		}
		
		db.execSQL(DROP_EXERCISE_SCORE_TABLE);
		db.execSQL(EXERCISE_SCORE_TABLE_CREATE);
		db.execSQL(EXERCISE_SCORE_INDEX1_CREATE);
		db.execSQL(EXERCISE_SCORE_INDEX2_CREATE);
		for (String fave : demoFavorites) {
			Content c = contentDB.getContentForName(fave);
	    	ContentValues values = new ContentValues(2);
	    	values.put("score", 1);
	    	values.put("name", c.getDisplayName());
	    	values.put("exerciseUniqueID", c.uniqueID);
	    	db.insert("exercisescore", null, values);
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SETTING_TABLE_CREATE);
		
		db.execSQL(AUDIO_TABLE_CREATE);
		db.execSQL(IMAGE_TABLE_CREATE);
		db.execSQL(CONTACTS_TABLE_CREATE);
		db.execSQL(PCL_SCORE_TABLE_CREATE);
		db.execSQL(EXERCISE_SCORE_TABLE_CREATE);
		db.execSQL(EXERCISE_SCORE_INDEX1_CREATE);
		db.execSQL(EXERCISE_SCORE_INDEX2_CREATE);
		createDemoData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

    public SQLiteDatabase sql() {
    	return getWritableDatabase();
    }
    
    public void addImage(String uri){
    	ContentValues values = new ContentValues(1);
    	values.put("uri", uri);
    	sql().insert("image", null, values);
    }
    public void addAudio(String uri){
    	ContentValues values = new ContentValues(1);
    	values.put("uri", uri);
    	sql().insert("audio", null, values);
    }
    public void deleteAudio(String uri){
    	String uriToDelete[]=new String[] {uri.toString()};
    	sql().delete("audio", "uri=?", uriToDelete);
    }
    
    public void deleteContact(String uri){
    	String uriToDelete[]=new String[] {uri.toString()};
    	sql().delete("contact", "uri=?", uriToDelete);
    }
    
    public void deleteImage(String uri){
    	String uriToDelete[]=new String[] {uri.toString()};
    	sql().delete("image", "uri=?", uriToDelete);
    }
    
   
    public List<Uri> getAllImages() {
		ArrayList<Uri> faves = new ArrayList<Uri>();
		Cursor c = sql().query("image", null, null, null, null, null, null);
		int index = c.getColumnIndex("uri");
		if (c.moveToFirst()) {
			while (true) {
				String uri_string = c.getString(index);
				Uri uri=Uri.parse(uri_string);
				faves.add(uri);
				if (!c.moveToNext()) break;
			}
		}
		c.close();
		return faves;
	}
    
    
    public List<Uri> getAllContacts() {
		ArrayList<Uri> faves = new ArrayList<Uri>();
		Cursor c = sql().query("contact", null, null, null, null, null, null);
		int index = c.getColumnIndex("uri");
		if (c.moveToFirst()) {
			while (true) {
				String uri_string = c.getString(index);
				Uri uri=Uri.parse(uri_string);
				faves.add(uri);
				if (!c.moveToNext()) break;
			}
		}
		c.close();
		return faves;
	}
    
    public List<Uri> getAllAudio() {
		ArrayList<Uri> faves = new ArrayList<Uri>();
		Cursor c = sql().query("audio", null, null, null, null, null, null);
		int index = c.getColumnIndex("uri");
		if (c.moveToFirst()) {
			while (true) {
				String uri_string = c.getString(index);
				Uri uri=Uri.parse(uri_string);
				faves.add(uri);
				if (!c.moveToNext()) break;
			}
		}
		c.close();
		return faves;
	}
    
    public void addContact(String uri){
    	ContentValues values = new ContentValues(1);
    	values.put("uri", uri);
    	sql().insert("contact", null, values);
    }
    public void addPCLScore(long timestamp, int score) {
    	ContentValues values = new ContentValues(2);
    	values.put("score", score);
    	values.put("time", timestamp);
    	sql().insert("pclscore", null, values);
    	
    }
    
	public PCLScore getLastPCLScore() {
		PCLScore score = null;
		Cursor c = sql().query("pclscore", null, null, null, null, null, "time DESC");
		if (c.moveToFirst()) {
			score = new PCLScore(c);
		}
		c.close();
		return score;
	}

	public void clearPCLScores() {
		sql().delete("pclscore", null, null);
	}
	
	public List<PCLScore> getPCLScores() {
		ArrayList<PCLScore> scores = new ArrayList<PCLScore>();
		Cursor c = sql().query("pclscore", null, null, null, null, null, "time ASC");
		if (c.moveToFirst()) {
			while (true) {
				PCLScore score = new PCLScore(c);
				scores.add(score);
				if (!c.moveToNext()) break;
			}
		}
		c.close();
		return scores;
	}

	public Cursor getFavorites() {
		return sql().query("exercisescore", null, "score > 0", null, null, null, null);
	}

	public List<String> getFavoriteIDs() {
		ArrayList<String> faves = new ArrayList<String>();
		Cursor c = getFavorites();
		int index = c.getColumnIndex("exerciseUniqueID");
		if (c.moveToFirst()) {
			while (true) {
				String id = c.getString(index);
				faves.add(id);
				if (!c.moveToNext()) break;
			}
		}
		c.close();
		return faves;
	}
	
    public void setSetting(String name, String value) {
    	settings.put(name, value);
    	
    	ContentValues values = new ContentValues(2);
    	values.put("value", value);
    	if (sql().update("settings", values, "name=?", new String[] {name}) == 0) {
        	values.put("name", name);
        	sql().insert("settings", null, values);
    	}
    }
    
    public String getSetting(String name) {
    	String value = settings.get(name);

    	if (value == null) {
    		Cursor c = sql().query("settings", null, "name=?", new String[] {name}, null, null, null);
    		if (c.moveToFirst()) {
    			value = c.getString(c.getColumnIndex("value"));
    	    	settings.put(name, value);
    		} else {
    	    	settings.put(name, NO_VALUE);
    		}
    		c.close();
    	} else if (value == NO_VALUE) {
    		value = null;
    	}
    	
		return value;
    }
    
}
