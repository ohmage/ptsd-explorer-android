
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.EventRecord;

import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DailyAssessmentEvent extends EventRecord {
	public int overallMood = -1;
	public int sleepWell = -1;
	public int howMuchAnger = -1;
	public int conflictWithOthers = -1;
	public int needForCoping = -1;
	public String copingSituations;
	public int overallCoping = -1;
	public int qualityOfGettingThingsDone = -1;
	public List<Integer> copingToolsUsed;
	public int copingSupport = -1;
	public int takePrescribedMedications = -1;
	public int medicationSideEffects = -1;
	public int drinkAlcohol = -1;
	public int howMuchAlcohol = -1;
	public int takeNonPrescribedDrug = -1;

	public DailyAssessmentEvent(QuestionnairePlayer player) {
	    if (player == null || player.getAnswers() == null)
	        return;

	    Hashtable answers = player.getAnswers();
	    overallMood = getInt(answers.get("overallMood"));
	    sleepWell = getInt(answers.get("sleepWell"));
	    howMuchAnger = getInt(answers.get("howMuchAnger"));
	    conflictWithOthers = getInt(answers.get("conflictWithOthers"));
	    needForCoping = getInt(answers.get("needForCoping"));
	    if(answers.get("copingSituations") instanceof String)
	        copingSituations = (String)answers.get("copingSituations");
	    overallCoping = getInt(answers.get("overallCoping"));
	    qualityOfGettingThingsDone = getInt(answers.get("qualityOfGettingThingsDone"));
	    copingToolsUsed = getIntList(answers.get("copingToolsUsed"));
	    copingSupport = getInt(answers.get("copingSupport"));
	    takePrescribedMedications = getInt(answers.get("takePrescribedMedications"));
	    medicationSideEffects = getInt(answers.get("medicationSideEffects"));
	    drinkAlcohol = getInt(answers.get("drinkAlcohol"));
	    howMuchAlcohol = getInt(answers.get("howMuchAlcohol"));
	    takeNonPrescribedDrug = getInt(answers.get("takeNonPrescribedDrug"));
	}

    public int getInt(Object value) {
        try {
            if (value != null)
                return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            // Ignore this
        }
        return -1;
    }

    public List<Integer> getIntList(Object value) {
        List<Integer> list = null;
        if (value instanceof String[]) {
            list = new ArrayList<Integer>();
            for (String s : (String[]) value) {
                try {
                    if (s != null)
                        list.add(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e) {
                    // Ignore this one, we will continue trying others
                }
            }
        }
        return list;
    }

    @Override
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
	
	@Override
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
