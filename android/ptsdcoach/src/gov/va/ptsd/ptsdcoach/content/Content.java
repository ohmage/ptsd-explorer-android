package gov.va.ptsd.ptsdcoach.content;

import gov.va.ptsd.ptsdcoach.ContentDBHelper;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewController;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;
import gov.va.ptsd.ptsdcoach.controllers.SetupSupportController;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Content {
	public long id;
	public long parent;
	public long help;
	public long theme;
	public long buttonTheme;
	public long tableTheme;

	public int ordering;
	public float weight;
	
	public String name;
	public String displayName;
	
	public String ui;
	public String uniqueID;

	private boolean textLoaded = false;
	private TreeMap<String,Object> attributes;
	private String mainText;
	
	private ContentDBHelper db;
	
	boolean helpChildChecked = false;
	private Content helpChild = null;
	
	public Content(ContentDBHelper db, Cursor c) throws OptionalDataException, ClassNotFoundException, IOException {
		this.db = db;
		id = c.getLong(c.getColumnIndex("_id"));
		parent = c.getLong(c.getColumnIndex("parent"));
		help = c.getLong(c.getColumnIndex("help"));
		ordering = c.getInt(c.getColumnIndex("ordering"));
		name = c.getString(c.getColumnIndex("name"));
		displayName = c.getString(c.getColumnIndex("displayName"));
		ui = c.getString(c.getColumnIndex("ui"));
		uniqueID = c.getString(c.getColumnIndex("uniqueID"));
	}
	
	public Uri uriByUniqueID() {
		return Uri.parse("contentUniqueID:"+uniqueID);
	}
	
	public ContentViewControllerBase createContentView(Context ctx) {
		String className = "ContentViewController";
		if (ui != null) className = ui;
		if ("Setup Support".equals(getTitle())) {
			className = "SetupSupportController";
		}
		ContentViewControllerBase v = null;
		try {
			Class k = Class.forName("gov.va.ptsd.ptsdcoach.controllers."+className);
			Constructor cons = k.getConstructor(Context.class);
			v = (ContentViewControllerBase)cons.newInstance(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			v = new ContentViewController(ctx);
		}
		v.setContent(this);
		return v;
	}

	public long getID() {
		return id;
	}
	
	public long getParentID() {
		return parent;
	}

	public String getUIDescriptor() {
		return ui;
	}

	public Content getParent() {
		if (parent > 0) {
			return db.getContentForID(parent);
		}
		
		return null;
	}

	public Content getHelp() {
		if (help > 0) {
			return db.getContentForID(help);
		}
		
		if (!helpChildChecked) {
			helpChild = getChildByName("@help");
			helpChildChecked = true;
		}
		return helpChild;
	}
	
	public boolean hasHelp() {
		if (help > 0) return true;
		return getHelp() != null;
	}
	
	private void loadText() {
		Cursor text = db.sql().query("contentText", new String[] {"body","attributes"}, "_id=?", new String[] {""+id}, null, null, null);
		if (text.moveToFirst()) {
			mainText = text.getString(0);
			byte[] extras = text.getBlob(1);
			if (extras != null) {
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(new ByteArrayInputStream(extras));
					attributes = (TreeMap<String,Object>)ois.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		text.close();
		
		textLoaded = true;
	}
	
	public String getMainText() {
		if (!textLoaded) loadText();
		return mainText;
	}
	
	public Map<String,Object> getAttributes() {
		if (!textLoaded) loadText();
		if (attributes == null) return Collections.emptyMap();
		return attributes;
	}
	
	public String getTitle() {
		String title = (String)getAttributes().get("title");
		if (title == null) title = displayName;
		return title;
	}

	public Integer getIntAttribute(String attrName) {
		String attr = (String)getAttributes().get(attrName);
		if (attr == null) return null;
		return Integer.parseInt(attr);
	}

	public String getStringAttribute(String attrName) {
		String attr = (String)getAttributes().get(attrName);
		return attr;
	}

	public Drawable getButtonImage() {
		String image = (String)getAttributes().get("buttongrid_image");
		if (image != null) {
			return Util.makeDrawable(db.getContext(), image, false);
		}
		
		return null;
	}

	public Drawable getImage() {
		String image = (String)getAttributes().get("image");
		if (image != null) {
			return Util.makeDrawable(db.getContext(), image, false);
		}
		
		return null;
	}

	public Integer getScore() {
		Integer score = null;
		Cursor c = UserDBHelper.instance(db.getContext()).sql().query("exercisescore", new String[] {"score"}, "exerciseUniqueID=?", new String[] {uniqueID}, null, null, null);
		if (c.moveToFirst()) {
			score = c.getInt(0);
		}
	
		c.close();
		return score;
	}

	public void setScore(Integer score) {
    	ContentValues values = new ContentValues(3);
    	values.put("score", score);
    	String dname = displayName;
    	if (dname == null) {
    		Content parent = getParent();
    		dname = String.format("%s #%d",parent.getDisplayName(),ordering+1);
    	}
    	values.put("name", dname);
    	values.put("exerciseUniqueID", uniqueID);
    	
    	if (UserDBHelper.instance(db.getContext()).sql().update("exercisescore", values, "exerciseUniqueID=?", new String[] {uniqueID}) == 0) {
    		UserDBHelper.instance(db.getContext()).sql().insert("exercisescore", null, values);
    	}
	}

	public String getDisplayName() {
		String title = displayName;
		if (title == null) title = name;
		return title;
	}

    public Cursor getExercisesForSymptom() {
    	Cursor c = db.sql().rawQuery("select * from content left join on symptom_map content._id=symptom_map._referrer where symptom_map._referree=?", new String[] {""+id});
    	return c;
    }

	public Content getChildByName(String name) {
		Cursor c = db.sql().query("content", null, "parent=? AND name=?", new String[] {""+id,name}, null, null, null);
		Content child = null;
		if (c.moveToFirst()) {
			try {
				child = new Content(db, c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		c.close();
		return child;
	}

	public Content getNext() {
		return getChildByName("@next");
	}

	public Caption[] getCaptions() {
		ArrayList<Caption> list = null;
		Cursor c = db.sql().query("caption", null, "parent=?", new String[] {""+id}, null, null, "ordering");
		if (c.moveToFirst()) {
			while (true) {
				try {
					Caption caption = new Caption(c);
					if (list == null) list = new ArrayList<Caption>();
					list.add(caption);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!c.moveToNext()) break;					
			}
		}
		c.close();
		
		Caption[] a = new Caption[(list == null) ? 0 : list.size()];
		if (list != null) {
			int i=0;
			for (Caption cap : list) {
				a[i] = cap;
				i++;
			}
		}
		
		return a;
	}
	
	public List<Content> getChildren() {
		Cursor c = db.sql().query("content", null, "parent=?", new String[] {""+id}, null, null, "ordering");
		ArrayList<Content> list = new ArrayList<Content>();
		if (c.moveToFirst()) {
			while (true) {
				try {
					String name = c.getString(c.getColumnIndex("name"));
					if ((name == null) || !name.startsWith("@")) {
						Content child = new Content(db, c);
						list.add(child);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (!c.moveToNext()) break;
			}
		}
		c.close();
		return list;
	}

	public boolean hasAudio() {
		String audio = (String)getAttributes().get("audio");
		return (audio != null);
	}
	
	public AssetFileDescriptor getAudio() {
		String audio = (String)getAttributes().get("audio");
		if (audio != null) {
			AssetFileDescriptor fd;
			try {
				fd = db.getContext().getAssets().openFd("Content/"+audio);
				return fd;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	public String getName() {
		return name;
	}

}
