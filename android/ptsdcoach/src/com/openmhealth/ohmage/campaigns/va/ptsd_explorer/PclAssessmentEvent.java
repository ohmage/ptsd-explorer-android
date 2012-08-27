
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.EventRecord;

import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class PclAssessmentEvent extends EventRecord {

    private static final int PROMPT_COUNT = 17;
    private final Hashtable<String, Integer> pcl = new Hashtable<String, Integer>();

    public PclAssessmentEvent(QuestionnairePlayer player) {
        if (player == null || player.getAnswers() == null)
            return;

        Hashtable answers = player.getAnswers();
        for (Object key : answers.keySet()) {
            Object value = answers.get(key);
            if (value != null) {
                try {
                    pcl.put(key.toString(), Integer.parseInt(answers.get(key).toString()));
                } catch (NumberFormatException e) {
                    // If we don't know what it is, we wont add it.
                }
            }
        }
    }

    @Override
    public String ohmageSurveyID() {
        return "pclAssessment";
    }

    @Override
    public void addAttributesToOhmageJSON(JSONArray into) {
        JSONObject obj;
        String key;
        for (int i = 1; i <= PROMPT_COUNT; i++) {
            try {
                obj = new JSONObject();
                key = "pcl" + i;
                obj.put("prompt_id", key);
                obj.put("value", pcl.get(key) == -1 ? "NOT_DISPLAYED" : pcl.get(key));
                into.put(obj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
