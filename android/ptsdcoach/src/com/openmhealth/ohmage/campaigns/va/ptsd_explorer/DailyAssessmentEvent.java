
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

public class DailyAssessmentEvent extends EventRecord {
	public int overallMood;
	public int sleepWell;
	public int howMuchAnger;
	public int conflictWithOthers;
	public int needForCoping;
	public String copingSituations;
	public int overallCoping;
	public int qualityOfGettingThingsDone;
	public List<Integer> copingToolsUsed;
	public int copingSupport;
	public int takePrescribedMedications;
	public int medicationSideEffects;
	public int drinkAlcohol;
	public int howMuchAlcohol;
	public int takeNonPrescribedDrug;
	
	public DailyAssessmentEvent() {
		super(0);
	}
	
	public String ohmageSurveyID() {
	    return "dailyAssessment";
	}

	public void toMap(Map<String,Object> into) {
		into.put("overallMood",overallMood);
		into.put("sleepWell",sleepWell);
		into.put("howMuchAnger",howMuchAnger);
		into.put("conflictWithOthers",conflictWithOthers);
		into.put("needForCoping",needForCoping);
		into.put("copingSituations",copingSituations);
		into.put("overallCoping",overallCoping);
		into.put("qualityOfGettingThingsDone",qualityOfGettingThingsDone);
		into.put("copingToolsUsed",copingToolsUsed);
		into.put("copingSupport",copingSupport);
		into.put("takePrescribedMedications",takePrescribedMedications);
		into.put("medicationSideEffects",medicationSideEffects);
		into.put("drinkAlcohol",drinkAlcohol);
		into.put("howMuchAlcohol",howMuchAlcohol);
		into.put("takeNonPrescribedDrug",takeNonPrescribedDrug);
	}
	
	public void addAttributesToOhmageJSON(JSONArray into) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","overallMood");
			obj.put("value",overallMood==-1 ? "NOT_DISPLAYED" : overallMood);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","sleepWell");
			obj.put("value",sleepWell==-1 ? "NOT_DISPLAYED" : sleepWell);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","howMuchAnger");
			obj.put("value",howMuchAnger==-1 ? "NOT_DISPLAYED" : howMuchAnger);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","conflictWithOthers");
			obj.put("value",conflictWithOthers==-1 ? "NOT_DISPLAYED" : conflictWithOthers);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","needForCoping");
			obj.put("value",needForCoping==-1 ? "NOT_DISPLAYED" : needForCoping);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","copingSituations");
			obj.put("value",(copingSituations!=null) ? copingSituations : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","overallCoping");
			obj.put("value",overallCoping==-1 ? "NOT_DISPLAYED" : overallCoping);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","qualityOfGettingThingsDone");
			obj.put("value",qualityOfGettingThingsDone==-1 ? "NOT_DISPLAYED" : qualityOfGettingThingsDone);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","copingToolsUsed");
			obj.put("value",(copingToolsUsed!=null) ? copingToolsUsed : "NONE");
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","copingSupport");
			obj.put("value",copingSupport==-1 ? "NOT_DISPLAYED" : copingSupport);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","takePrescribedMedications");
			obj.put("value",takePrescribedMedications==-1 ? "NOT_DISPLAYED" : takePrescribedMedications);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","medicationSideEffects");
			obj.put("value",medicationSideEffects==-1 ? "NOT_DISPLAYED" : medicationSideEffects);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","drinkAlcohol");
			obj.put("value",drinkAlcohol==-1 ? "NOT_DISPLAYED" : drinkAlcohol);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","howMuchAlcohol");
			obj.put("value",howMuchAlcohol==-1 ? "NOT_DISPLAYED" : howMuchAlcohol);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject();
			obj.put("prompt_id","takeNonPrescribedDrug");
			obj.put("value",takeNonPrescribedDrug==-1 ? "NOT_DISPLAYED" : takeNonPrescribedDrug);
			into.put(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
