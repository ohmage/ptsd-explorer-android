package gov.va.ptsd.ptsdcoach.questionnaire.android;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

public class QuestionnaireDatabase extends SQLiteOpenHelper {

    private static final String KEY_KEY = "key";
    private static final String KEY_URL = "url";
    private static final String KEY_TRIGGERTIME = "time";
    private static final String KEY_NOTIFICATION_ID = "notification_id";
    private static final String KEY_REMINDERS = "reminder";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_FLAGS = "flags";

    private static final String DATABASE_NAME = "surveys.db";
    private static final int DATABASE_VERSION = 4;
    
    private static final String RESOURCES_TABLE_NAME = "resources";
    private static final String RESOURCES_TABLE_CREATE =
                "CREATE TABLE " + RESOURCES_TABLE_NAME + " (" +
                KEY_KEY + " TEXT, " +
                KEY_FLAGS + " INTEGER, " +
                KEY_CONTENT + " BLOB" +
                ");";

    private static final String QUESTIONNAIRE_TABLE_NAME = "questionnaires";
    private static final String QUESTIONNAIRE_TABLE_CREATE =
                "CREATE TABLE " + QUESTIONNAIRE_TABLE_NAME + " (" +
                KEY_KEY + " TEXT, " +
                KEY_URL + " TEXT, " +
                KEY_NOTIFICATION_ID + " INTEGER, " +
                KEY_TRIGGERTIME + " INTEGER," +
                KEY_REMINDERS + " INTEGER" +
                ");";

    public static class PendingQuestionnaire {
    	public String key;
    	String url;
    	long triggerTime;
    	public int notificationID;
    	int reminder;
    }
    
    QuestionnaireDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUESTIONNAIRE_TABLE_CREATE);
        db.execSQL(RESOURCES_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	db.execSQL("DROP TABLE IF EXISTS "+QUESTIONNAIRE_TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS "+RESOURCES_TABLE_NAME);
    	onCreate(db);
    }
    
    public PendingQuestionnaire getPendingQuestionnaire(String key) {
    	Cursor c = getReadableDatabase().query(QUESTIONNAIRE_TABLE_NAME, new String[] {KEY_KEY,KEY_URL,KEY_TRIGGERTIME,KEY_NOTIFICATION_ID,KEY_REMINDERS}, "key=?", new String[] {key}, null, null, null);
    	if (c == null) return null;
    	if (c.move(1)) {
    		PendingQuestionnaire p = new PendingQuestionnaire();
    		p.key = key;
    		p.url = c.getString(1);
    		p.triggerTime = c.getLong(2);
    		p.notificationID = c.getInt(3);
    		p.reminder = c.getInt(4);
    		c.close();
    		return p;
    	}
    	
    	c.close();
    	return null;
    }

    public void removePendingQuestionnaire(String key) {
    	getWritableDatabase().delete(QUESTIONNAIRE_TABLE_NAME, "key=?", new String[] {key});
    }

    public void addPendingQuestionnaire(PendingQuestionnaire p) {
    	ContentValues vals = new ContentValues();
    	vals.put(KEY_KEY, p.key);
    	vals.put(KEY_URL, p.url);
    	vals.put(KEY_TRIGGERTIME, p.triggerTime);
    	vals.put(KEY_NOTIFICATION_ID, p.notificationID);
    	vals.put(KEY_REMINDERS, p.reminder);
    	try {
    		getWritableDatabase().insert(QUESTIONNAIRE_TABLE_NAME, KEY_KEY, vals);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public AndroidResource getResource(String key) {
    	Cursor c = getReadableDatabase().query(RESOURCES_TABLE_NAME, new String[] {KEY_KEY,KEY_FLAGS,KEY_CONTENT}, "key=?", new String[] {key}, null, null, null);
    	if (c == null) return null;
    	if (c.move(1)) {
    		AndroidResource r = new AndroidResource(this,c.getString(0),c.getInt(1),c.getBlob(2));
        	c.close();
    		return r;
    	}
    	
    	c.close();
    	return null;
    }

    public synchronized List<AndroidResource> getNullResources() {
    	Cursor c = getReadableDatabase().query(RESOURCES_TABLE_NAME, new String[] {KEY_KEY, KEY_FLAGS}, "content is null", null, null, null, null);
    	if (c == null) return null;
    	ArrayList<AndroidResource> list = new ArrayList<AndroidResource>();
    	while (c.move(1)) {
    		list.add(new AndroidResource(this,c.getString(0),c.getInt(1)));
    	}
    	
    	c.close();
    	return list.size() > 0 ? list : null;
    }

    public synchronized void removeResource(String key) {
    	getWritableDatabase().delete(RESOURCES_TABLE_NAME, "key=?", new String[] {key});
    }

	public synchronized void removeAllResources() {
    	getWritableDatabase().delete(RESOURCES_TABLE_NAME, null, null);
	}

    public synchronized void addResource(AndroidResource rsrc) {
    	removeResource(rsrc.getUrl());
    	
    	ContentValues vals = new ContentValues();
    	vals.put(KEY_KEY, rsrc.getUrl());
    	vals.put(KEY_FLAGS, rsrc.getFlags());
    	vals.put(KEY_CONTENT, rsrc.getContent());
    	try {
    		getWritableDatabase().insert(RESOURCES_TABLE_NAME, KEY_KEY, vals);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public synchronized void updateResource(AndroidResource rsrc) {
    	ContentValues vals = new ContentValues();
    	vals.put(KEY_FLAGS, rsrc.getFlags());
    	vals.put(KEY_CONTENT, rsrc.getContent());
    	try {
    		getWritableDatabase().update(RESOURCES_TABLE_NAME, vals, "key=?", new String[] {rsrc.getUrl()});
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}