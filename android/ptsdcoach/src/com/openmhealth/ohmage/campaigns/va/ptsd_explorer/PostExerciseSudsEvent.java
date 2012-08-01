
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

public class PostExerciseSudsEvent extends EventRecord {
	public long initialSudsScore;
	public long postExerciseSudsScore;
	
	public PostExerciseSudsEvent() {
		super(10);
	}
	
	public String ohmageSurveyID() {
	    return "postExerciseSudsProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("initialSudsScore",initialSudsScore);
		into.put("postExerciseSudsScore",postExerciseSudsScore);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","initialSudsScore");
			obj.put("value",initialSudsScore==-1 ? "NOT_DISPLAYED" : initialSudsScore);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","postExerciseSudsScore");
			obj.put("value",postExerciseSudsScore==-1 ? "NOT_DISPLAYED" : postExerciseSudsScore);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
