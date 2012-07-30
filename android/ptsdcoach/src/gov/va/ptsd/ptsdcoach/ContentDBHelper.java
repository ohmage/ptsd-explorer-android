package gov.va.ptsd.ptsdcoach;

import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.view.View;

public class ContentDBHelper extends SQLiteOpenHelper {
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/gov.va.ptsd.ptsdcoach/databases/";
 
    private static String DB_NAME = "content.db";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    private static ContentDBHelper instance;
    
    public static ContentDBHelper instance(Context ctx) {
    	if (instance == null) {
    		instance = new ContentDBHelper(ctx);
    		
    		try {
    			instance.createDataBase();
    		} catch (IOException ioe) {
    			throw new Error("Unable to create database");
    		}

    		try {
    			instance.openDataBase();
    		} catch(SQLException sqle){
    			throw sqle;
    		}
    	}
    	
    	return instance;
    }
    
    public Context getContext() {
		return myContext;
	}
    
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public ContentDBHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    public SQLiteDatabase sql() {
    	return myDataBase;
    }

    public Content getContentForName(String name) {
    	Content content = null;
    	Cursor c = sql().query("content", null, "name=?", new String[] {name}, null, null, null);
    	if (c.moveToFirst()) {
    		try {
    			content = new Content(this,c);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	c.close();
    	return content;
    }

    public Content getContentForID(long id) {
    	Content content = null;
    	Cursor c = sql().query("content", null, "_id=?", new String[] {""+id}, null, null, null);
    	if (c.moveToFirst()) {
    		try {
    			content = new Content(this,c);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	c.close();
    	return content;
    }

    public Content getContentForUniqueID(String uniqueID) {
    	Content content = null;
    	Cursor c = sql().query("content", null, "uniqueID=?", new String[] {uniqueID}, null, null, null);
    	if (c.moveToFirst()) {
    		try {
    			content = new Content(this,c);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	c.close();
    	return content;
    }

	public Content getContentForUri(Uri data) {
		String scheme = data.getScheme();
		if (scheme.equals("contentUniqueID")) {
			return getContentForUniqueID(data.getSchemeSpecificPart());
		} else if (scheme.equals("contentName:")) {
			return getContentForName(data.getSchemeSpecificPart());
		} else if (scheme.equals("contentID:")) {
			return getContentForID(Long.parseLong(data.getSchemeSpecificPart()));
		}
		return null;
	}
    
    public void createDataBase() throws IOException {
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
			copyDataBase();
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException {
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}

}
