
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

public class Phq9SurveyEvent extends EventRecord {
	public int phq91;
	public int phq92;
	public int phq93;
	public int phq94;
	public int phq95;
	public int phq96;
	public int phq97;
	public int phq98;
	public int phq99;
	
	public Phq9SurveyEvent() {
		super(3);
	}
	
	public String ohmageSurveyID() {
	    return "phq9Survey";
	}

	public void toMap(Map<String,Object> into) {
		into.put("phq91",phq91);
		into.put("phq92",phq92);
		into.put("phq93",phq93);
		into.put("phq94",phq94);
		into.put("phq95",phq95);
		into.put("phq96",phq96);
		into.put("phq97",phq97);
		into.put("phq98",phq98);
		into.put("phq99",phq99);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq91");
			obj.put("value",phq91==-1 ? "NOT_DISPLAYED" : phq91);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq92");
			obj.put("value",phq92==-1 ? "NOT_DISPLAYED" : phq92);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq93");
			obj.put("value",phq93==-1 ? "NOT_DISPLAYED" : phq93);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq94");
			obj.put("value",phq94==-1 ? "NOT_DISPLAYED" : phq94);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq95");
			obj.put("value",phq95==-1 ? "NOT_DISPLAYED" : phq95);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq96");
			obj.put("value",phq96==-1 ? "NOT_DISPLAYED" : phq96);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq97");
			obj.put("value",phq97==-1 ? "NOT_DISPLAYED" : phq97);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq98");
			obj.put("value",phq98==-1 ? "NOT_DISPLAYED" : phq98);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","phq99");
			obj.put("value",phq99==-1 ? "NOT_DISPLAYED" : phq99);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
