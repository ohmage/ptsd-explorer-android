
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

public class TimePerScreenEvent extends EventRecord {
	public String screenId;
	public long screenStartTime;
	public long timeSpentOnScreen;
	
	public TimePerScreenEvent() {
		super(17);
	}
	
	public String ohmageSurveyID() {
	    return "timePerScreenProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("screenId",screenId);
		into.put("screenStartTime",screenStartTime);
		into.put("timeSpentOnScreen",timeSpentOnScreen);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","screenId");
			obj.put("value",(screenId!=null) ? screenId : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","screenStartTime");
			cal.setTimeInMillis(screenStartTime);
			obj.put("value", timestampFormat.format(cal.getTime()));
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","timeSpentOnScreen");
			obj.put("value",timeSpentOnScreen==-1 ? "NOT_DISPLAYED" : timeSpentOnScreen);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
