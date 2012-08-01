
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

public class AppExitedEvent extends EventRecord {
	public int appExitedAccessibilityFeaturesActive;
	
	public AppExitedEvent() {
		super(12);
	}
	
	public String ohmageSurveyID() {
	    return "appExitedProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("appExitedAccessibilityFeaturesActive",appExitedAccessibilityFeaturesActive);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","appExitedAccessibilityFeaturesActive");
			obj.put("value",appExitedAccessibilityFeaturesActive==-1 ? "NOT_DISPLAYED" : appExitedAccessibilityFeaturesActive);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
