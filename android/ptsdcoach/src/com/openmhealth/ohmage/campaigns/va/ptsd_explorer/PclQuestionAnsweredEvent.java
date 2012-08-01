
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

public class PclQuestionAnsweredEvent extends EventRecord {
	public long pclNumberOfQuestionsAnswered;
	
	public PclQuestionAnsweredEvent() {
		super(5);
	}
	
	public String ohmageSurveyID() {
	    return "pclQuestionAnsweredProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("pclNumberOfQuestionsAnswered",pclNumberOfQuestionsAnswered);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pclNumberOfQuestionsAnswered");
			obj.put("value",pclNumberOfQuestionsAnswered==-1 ? "NOT_DISPLAYED" : pclNumberOfQuestionsAnswered);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
