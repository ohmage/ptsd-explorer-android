
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

public class PclAssessmentStartedEvent extends EventRecord {
	public long pclAssessmentStarted;
	
	public PclAssessmentStartedEvent() {
		super(4);
	}
	
	public String ohmageSurveyID() {
	    return "pclAssessmentStartedProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("pclAssessmentStarted",pclAssessmentStarted);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pclAssessmentStarted");
			cal.setTimeInMillis(pclAssessmentStarted);
			obj.put("value", timestampFormat.format(cal.getTime()));
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
