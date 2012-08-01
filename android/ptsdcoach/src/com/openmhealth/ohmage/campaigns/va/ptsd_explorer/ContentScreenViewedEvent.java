
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

public class ContentScreenViewedEvent extends EventRecord {
	public long contentScreenTimestampStart;
	public long contentScreenTimestampDismissal;
	public String contentScreenName;
	public String contentScreenDisplayName;
	public int contentScreenType;
	public String contentScreenId;
	
	public ContentScreenViewedEvent() {
		super(7);
	}
	
	public String ohmageSurveyID() {
	    return "contentScreenViewedProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("contentScreenTimestampStart",contentScreenTimestampStart);
		into.put("contentScreenTimestampDismissal",contentScreenTimestampDismissal);
		into.put("contentScreenName",contentScreenName);
		into.put("contentScreenDisplayName",contentScreenDisplayName);
		into.put("contentScreenType",contentScreenType);
		into.put("contentScreenId",contentScreenId);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentScreenTimestampStart");
			cal.setTimeInMillis(contentScreenTimestampStart);
			obj.put("value", timestampFormat.format(cal.getTime()));
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentScreenTimestampDismissal");
			cal.setTimeInMillis(contentScreenTimestampDismissal);
			obj.put("value", timestampFormat.format(cal.getTime()));
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentScreenName");
			obj.put("value",(contentScreenName!=null) ? contentScreenName : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentScreenDisplayName");
			obj.put("value",(contentScreenDisplayName!=null) ? contentScreenDisplayName : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentScreenType");
			obj.put("value",contentScreenType==-1 ? "NOT_DISPLAYED" : contentScreenType);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","contentScreenId");
			obj.put("value",(contentScreenId!=null) ? contentScreenId : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
