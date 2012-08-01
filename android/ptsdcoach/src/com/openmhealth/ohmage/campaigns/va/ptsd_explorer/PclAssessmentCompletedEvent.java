
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

public class PclAssessmentCompletedEvent extends EventRecord {
	public long pclAssessmentCompletedFinalScore;
	public int pclAssessmentCompleted;
	
	public PclAssessmentCompletedEvent() {
		super(15);
	}
	
	public String ohmageSurveyID() {
	    return "pclAssessmentCompletedProbe";
	}

	public void toMap(Map<String,Object> into) {
		into.put("pclAssessmentCompletedFinalScore",pclAssessmentCompletedFinalScore);
		into.put("pclAssessmentCompleted",pclAssessmentCompleted);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pclAssessmentCompletedFinalScore");
			obj.put("value",pclAssessmentCompletedFinalScore==-1 ? "NOT_DISPLAYED" : pclAssessmentCompletedFinalScore);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pclAssessmentCompleted");
			obj.put("value",pclAssessmentCompleted==-1 ? "NOT_DISPLAYED" : pclAssessmentCompleted);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
