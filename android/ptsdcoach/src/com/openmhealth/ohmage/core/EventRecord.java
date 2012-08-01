package com.openmhealth.ohmage.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract public class EventRecord {
	private int eventID;
	private long timestamp;
	
	static protected DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	static protected Calendar cal = Calendar.getInstance();
	
	public EventRecord(int id) {
		eventID = id;
		timestamp = System.currentTimeMillis();
	}
	
	abstract public String ohmageSurveyID();
	abstract public void addAttributesToOhmageJSON(JSONArray into);
	
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		JSONObject surveyLaunchContext = new JSONObject();
		String uniqueID = UUID.randomUUID().toString();
	    
	    cal.setTimeInMillis(timestamp);
	    String formattedDateString = timestampFormat.format(cal.getTime());

	    obj.put("survey_key", uniqueID);
	    obj.put("date", formattedDateString);
	    obj.put("time", timestamp);
	    obj.put("timezone", TimeZone.getDefault().getID());
	    obj.put("location_status", "unavailable");
	    surveyLaunchContext.put("active_triggers", new JSONArray());
	    surveyLaunchContext.put("launch_time", timestamp);
	    surveyLaunchContext.put("launch_timezone", TimeZone.getDefault().getID());
	    obj.put("survey_launch_context", surveyLaunchContext);
	    obj.put("survey_id", ohmageSurveyID());

	    JSONArray attributes = new JSONArray();
	    addAttributesToOhmageJSON(attributes);
	    obj.put("responses", attributes);
	    
	    return obj;
	}
}
