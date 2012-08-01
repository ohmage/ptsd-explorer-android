
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

public class PreExerciseSudsEvent extends EventRecord {
	public long preExerciseSudsScore;
	
	public PreExerciseSudsEvent() {
		super(9);
	}
	
	public String ohmageSurveyID() {
	    return "preExerciseSudsProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("preExerciseSudsScore",preExerciseSudsScore);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","preExerciseSudsScore");
			obj.put("value",preExerciseSudsScore==-1 ? "NOT_DISPLAYED" : preExerciseSudsScore);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
