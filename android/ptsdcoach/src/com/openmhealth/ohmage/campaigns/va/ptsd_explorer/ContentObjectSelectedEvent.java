
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

public class ContentObjectSelectedEvent extends EventRecord {
	public String contentObjectName;
	public String contentObjectDisplayName;
	public String contentObjectId;
	
	public ContentObjectSelectedEvent() {
		super(8);
	}
	
	public String ohmageSurveyID() {
	    return "contentObjectSelectedProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("contentObjectName",contentObjectName);
		into.put("contentObjectDisplayName",contentObjectDisplayName);
		into.put("contentObjectId",contentObjectId);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentObjectName");
			obj.put("value",(contentObjectName!=null) ? contentObjectName : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentObjectDisplayName");
			obj.put("value",(contentObjectDisplayName!=null) ? contentObjectDisplayName : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentObjectId");
			obj.put("value",(contentObjectId!=null) ? contentObjectId : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
