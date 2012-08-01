
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

public class PclAssessmentEvent extends EventRecord {
	public int pcl1;
	public int pcl2;
	public int pcl3;
	public int pcl4;
	public int pcl5;
	public int pcl6;
	public int pcl7;
	public int pcl8;
	public int pcl9;
	public int pcl10;
	public int pcl11;
	public int pcl12;
	public int pcl13;
	public int pcl14;
	public int pcl15;
	public int pcl16;
	public int pcl17;
	
	public PclAssessmentEvent() {
		super(2);
	}
	
	public String ohmageSurveyID() {
	    return "pclAssessment";
	}

	public void toMap(Map<String,Object> into) {
		into.put("pcl1",pcl1);
		into.put("pcl2",pcl2);
		into.put("pcl3",pcl3);
		into.put("pcl4",pcl4);
		into.put("pcl5",pcl5);
		into.put("pcl6",pcl6);
		into.put("pcl7",pcl7);
		into.put("pcl8",pcl8);
		into.put("pcl9",pcl9);
		into.put("pcl10",pcl10);
		into.put("pcl11",pcl11);
		into.put("pcl12",pcl12);
		into.put("pcl13",pcl13);
		into.put("pcl14",pcl14);
		into.put("pcl15",pcl15);
		into.put("pcl16",pcl16);
		into.put("pcl17",pcl17);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl1");
			obj.put("value",pcl1==-1 ? "NOT_DISPLAYED" : pcl1);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl2");
			obj.put("value",pcl2==-1 ? "NOT_DISPLAYED" : pcl2);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl3");
			obj.put("value",pcl3==-1 ? "NOT_DISPLAYED" : pcl3);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl4");
			obj.put("value",pcl4==-1 ? "NOT_DISPLAYED" : pcl4);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl5");
			obj.put("value",pcl5==-1 ? "NOT_DISPLAYED" : pcl5);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl6");
			obj.put("value",pcl6==-1 ? "NOT_DISPLAYED" : pcl6);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl7");
			obj.put("value",pcl7==-1 ? "NOT_DISPLAYED" : pcl7);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl8");
			obj.put("value",pcl8==-1 ? "NOT_DISPLAYED" : pcl8);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl9");
			obj.put("value",pcl9==-1 ? "NOT_DISPLAYED" : pcl9);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl10");
			obj.put("value",pcl10==-1 ? "NOT_DISPLAYED" : pcl10);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl11");
			obj.put("value",pcl11==-1 ? "NOT_DISPLAYED" : pcl11);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl12");
			obj.put("value",pcl12==-1 ? "NOT_DISPLAYED" : pcl12);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl13");
			obj.put("value",pcl13==-1 ? "NOT_DISPLAYED" : pcl13);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl14");
			obj.put("value",pcl14==-1 ? "NOT_DISPLAYED" : pcl14);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl15");
			obj.put("value",pcl15==-1 ? "NOT_DISPLAYED" : pcl15);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl16");
			obj.put("value",pcl16==-1 ? "NOT_DISPLAYED" : pcl16);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","pcl17");
			obj.put("value",pcl17==-1 ? "NOT_DISPLAYED" : pcl17);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
