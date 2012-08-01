
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.EventRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeElapsedBetweenSessionsEvent extends EventRecord {
	public long timeElapsedBetweenSessions;
	
	public TimeElapsedBetweenSessionsEvent() {
		super(18);
	}
	
	public String ohmageSurveyID() {
	    return "timeElapsedBetweenSessionsProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("timeElapsedBetweenSessions",timeElapsedBetweenSessions);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","timeElapsedBetweenSessions");
			obj.put("value",timeElapsedBetweenSessions==-1 ? "NOT_DISPLAYED" : timeElapsedBetweenSessions);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
