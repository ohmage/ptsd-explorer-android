
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

public class PclReminderScheduledEvent extends EventRecord {
	public long pclReminderScheduledTimestamp;
	
	public PclReminderScheduledEvent() {
		super(13);
	}
	
	public String ohmageSurveyID() {
	    return "pclReminderScheduledProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("pclReminderScheduledTimestamp",pclReminderScheduledTimestamp);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pclReminderScheduledTimestamp");
			cal.setTimeInMillis(pclReminderScheduledTimestamp);
			obj.put("value", timestampFormat.format(cal.getTime()));
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
