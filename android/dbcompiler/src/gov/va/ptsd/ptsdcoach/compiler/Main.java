package gov.va.ptsd.ptsdcoach.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Main {

	static final int TYPE_FLOAT = 1;
	static final int TYPE_STRING = 2;
	static final int TYPE_THEME_REFERENCE = 3;
	static final int TYPE_CONTENT_REFERENCE = 4;
	static final int TYPE_MULTI_CONTENT_REFERENCE = 5;
	
	static class FieldDescriptor {
		String columnName;
		int columnIndex;
		int columnType;
		
		public FieldDescriptor(String name, int index, int type) {
			columnName = name;
			columnIndex = index;
			columnType = type;
		}
	}
	
	static TreeMap<String,FieldDescriptor> contentFields = new TreeMap<String, Main.FieldDescriptor>();

	static {
		contentFields.put("name",new FieldDescriptor("name", 4, TYPE_STRING));
		contentFields.put("displayName",new FieldDescriptor("displayName", 5, TYPE_STRING));
		contentFields.put("help",new FieldDescriptor("help", 6, TYPE_CONTENT_REFERENCE));
		contentFields.put("weight",new FieldDescriptor("weight", 7, TYPE_FLOAT));
		contentFields.put("ui",new FieldDescriptor("ui", 8, TYPE_STRING));
		contentFields.put("helpsWithSymptoms",new FieldDescriptor("symptom_map", -1, TYPE_MULTI_CONTENT_REFERENCE));
	}
	
	static Connection conn;
	static PreparedStatement insertContent;
	static PreparedStatement insertContentText;
	static PreparedStatement insertCaption;
	static PreparedStatement queryContentByName;
	
	static void init(String dbname) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbname);
			Statement stat = conn.createStatement();

			stat.executeUpdate("drop table if exists android_metadata;");
			stat.executeUpdate("create table android_metadata (locale TEXT DEFAULT 'en_US');");
			stat.executeUpdate("insert into android_metadata VALUES ('en_US');");

			stat.executeUpdate("drop table if exists contentText;");
			stat.executeUpdate(	"create table contentText (_id INTEGER PRIMARY KEY ASC, body TEXT, attributes BLOB);");

			stat.executeUpdate("drop table if exists content;");
			stat.executeUpdate(	"create table content (_id INTEGER PRIMARY KEY ASC, parent INT, ordering INT, uniqueID TEXT, name TEXT, displayName TEXT, help INT, weight REAL, ui TEXT);");
			stat.executeUpdate(	"create index content_name_idx on content (name);");
			stat.executeUpdate(	"create index content_parent_idx on content (parent);");
			stat.executeUpdate(	"create index content_uniqueID_idx on content (uniqueID);");

			stat.executeUpdate("drop table if exists caption;");
			stat.executeUpdate(	"create table caption (_id INTEGER PRIMARY KEY ASC, parent INT, ordering INT, start INT, end INT, text TEXT);");
			stat.executeUpdate(	"create index caption_parent_idx on caption (parent,ordering);");

			stat.executeUpdate("drop table if exists symptom_map;");
			stat.executeUpdate(	"create table symptom_map (_id INTEGER PRIMARY KEY ASC, referrer INT, referree INT);");
			stat.executeUpdate(	"create index symptom_map_referree_idx on symptom_map (referree);");
			stat.executeUpdate(	"create index symptom_map_referrer_idx on symptom_map (referrer);");

			stat.executeUpdate("drop table if exists theme;");
			stat.executeUpdate(	"create table theme (_id INTEGER PRIMARY KEY ASC, bgColor, bgImage, font, fontSize, textColor);");

			queryContentByName = conn.prepareStatement("select * from content where name=?;");
			insertContent = conn.prepareStatement("insert into content (parent,ordering,uniqueID,name,displayName,help,weight,ui) VALUES (?,?,?,?,?,?,?,?);");
			insertContentText = conn.prepareStatement("insert into contentText (_id,body,attributes) VALUES (?,?,?);");
			insertCaption = conn.prepareStatement("insert into caption (parent,ordering,start,end,text) VALUES (?,?,?,?,?);");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static byte[] serialize(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(o);
		out.close();
		return baos.toByteArray();
	}
	
	static public void addTheme(Element elm) {

	}

	static public void addElement(long parent, int order, Map<String,String> defaults, Element elm) throws SQLException, DataConversionException, IOException {
		if (elm.getName().equals("Content") || elm.getName().equals("ExerciseCategory")) {
			addContent(parent,order,defaults,elm);
		} else if (elm.getName().equals("Caption")) {
			addCaption(parent,order,elm);
		} else if (elm.getName().equals("Theme")) {
			addTheme(elm);
		}
	}

	public static String getHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
				Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}

	static public void addCaption(long parent, int order, Element elm) throws SQLException, DataConversionException, IOException {
		insertCaption.clearParameters();

		insertCaption.setLong(1, parent);
		insertCaption.setInt(2, order);

		String[] s;
		int minutes,seconds;
		
		s = elm.getAttributeValue("start").split(":");
		minutes = Integer.parseInt(s[0],10);
		seconds = Integer.parseInt(s[1],10);
		int startTime = ((minutes * 60) + seconds) * 1000;
		insertCaption.setInt(3, startTime);

		s = elm.getAttributeValue("end").split(":");
		minutes = Integer.parseInt(s[0],10);
		seconds = Integer.parseInt(s[1],10);
		int endTime = ((minutes * 60) + seconds) * 1000;
		insertCaption.setInt(4, endTime);
		
		String text = elm.getText().trim();
		insertCaption.setString(5, text);
		
		System.out.println("caption("+parent+","+order+","+startTime+","+endTime+"):'"+text+"'");
		
		insertCaption.execute();
	}

	static public void addContent(long parent, int order, Map<String,String> defaults, Element elm) throws SQLException, DataConversionException, IOException {
		TreeMap<String,Object> extras = new TreeMap<String, Object>();
		TreeMap<String,String> childDefaults = new TreeMap<String, String>();
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		insertContent.clearParameters();
		insertContent.setLong(1, parent);
		insertContent.setInt(2, order);
		
		TreeMap<String,String> values = new TreeMap<String, String>(defaults);
		for (Object _attr : elm.getAttributes()) {
			Attribute attr = (Attribute)_attr;
			String name = attr.getName();
			values.put(name,attr.getValue());
		}
		
		String elmName = null;
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			md5.update(name.getBytes());
			md5.update(value.getBytes());
			FieldDescriptor fd = contentFields.get(name);
			if (name.equals("name")) elmName = value;
			if (fd != null) {
				int index = fd.columnIndex;
				switch (fd.columnType) {
				case TYPE_FLOAT:
					insertContent.setFloat(index, Float.parseFloat(value));
					break;
				case TYPE_STRING:
					insertContent.setString(index, value);
					break;
				case TYPE_THEME_REFERENCE:
					break;
				case TYPE_CONTENT_REFERENCE: {
					queryContentByName.setString(1, value);
					ResultSet rs = queryContentByName.executeQuery();
					if (rs.next()) {
						long id = rs.getLong("_id");
						insertContent.setLong(index, id);
					} else {
						throw new RuntimeException("Could not find content for referenced name '"+value+"'");
					}
					rs.close();
					break;
				}
				case TYPE_MULTI_CONTENT_REFERENCE:
					break;
				}
			} else {
				if (name.startsWith("child_")) {
					childDefaults.put(name.substring(6), value);
				} else {
					extras.put(name, value);
				}
			}
		}

		String text = elm.getText().trim();
		if (text.matches("^\\p{Space}*$")) {
			text = null;
		} else {
			md5.update(text.getBytes());
		}
		
		System.out.println(""+elmName+": '"+text+"'");
		
		byte[] digest = md5.digest();
		
		insertContent.setString(3, getHexString(digest));

		insertContent.execute();
		ResultSet rs = insertContent.getGeneratedKeys();
		long id = -1;
		if (rs.next()) {
			id = rs.getLong("last_insert_rowid()");
		}
		rs.close();
		
		insertContentText.setLong(1, id);
		insertContentText.setString(2, text);
		if (extras.isEmpty()) {
			insertContentText.setNull(3, java.sql.Types.BLOB);
		} else {
			insertContentText.setBytes(3, serialize(extras));
		}
		insertContentText.execute();
		
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			FieldDescriptor fd = contentFields.get(name);
			if (fd != null) {
				switch (fd.columnType) {
					case TYPE_MULTI_CONTENT_REFERENCE: {
						String[] refs = value.split("\\p{Space}");
						PreparedStatement insertReference = conn.prepareStatement("insert into "+fd.columnName+" (referrer,referree) VALUES (?,?);");
						for (String ref : refs) {
							queryContentByName.setString(1, ref);
							rs = queryContentByName.executeQuery();
							if (rs.next()) {
								long refid = rs.getLong("_id");
								insertReference.setLong(1, id);
								insertReference.setLong(2, refid);
								insertReference.execute();
							} else {
								throw new RuntimeException("Could not find content for referenced name '"+value+"'");
							}
							rs.close();
						}
						break;
					}
				}
			}
		}
		
		int childOrder = 0;
		for (Object _child : elm.getChildren()) {
			Element child = (Element)_child;
			if (child.getName().equals("Content") || child.getName().equals("ExerciseCategory")) {
				addContent(id,childOrder,childDefaults,child);
			} else if (child.getName().equals("Caption")) {
				addCaption(id,childOrder,child);
			}
			childOrder++;
		}

	}

	static public void main(String[] args) throws JDOMException, IOException, SQLException {
		init(args[1]);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new File(args[0]));
		Element root = doc.getRootElement();
		int order = 0;
		for (Object _elm : root.getChildren()) {
			Element elm = (Element)_elm;
			addElement(-1, order, new TreeMap<String, String>(), elm);
			order++;
		}
	}
}
