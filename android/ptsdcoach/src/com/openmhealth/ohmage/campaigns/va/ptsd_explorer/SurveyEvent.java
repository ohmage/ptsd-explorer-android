
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.EventRecord;

import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Helper class which creates a survey based on the answers of a
 * {@link QuestionnairePlayer}
 * 
 * @author cketcham
 */
public abstract class SurveyEvent extends EventRecord {

    private final Hashtable<String, Integer> prompts = new Hashtable<String, Integer>();

    public SurveyEvent(QuestionnairePlayer player) {
        if (player == null || player.getAnswers() == null)
            return;

        Hashtable answers = player.getAnswers();
        for (Object key : answers.keySet()) {
            Object value = answers.get(key);
            if (key.toString().startsWith(getSurveyPrefix()) && value != null) {
                try {
                    prompts.put(key.toString(), Integer.parseInt(answers.get(key).toString()));
                } catch (NumberFormatException e) {
                    // If we don't know what it is, we wont add it.
                }
            }
        }
    }

    @Override
    public void addAttributesToOhmageJSON(JSONArray into) {
        JSONObject obj;
        String key;
        for (int i = startingIndex(); i <= getPromptCount(); i++) {
            try {
                obj = new JSONObject();
                key = getSurveyPrefix() + i;
                obj.put("prompt_id", key);
                obj.put("value", prompts.get(key) == -1 ? "NOT_DISPLAYED" : prompts.get(key));
                into.put(obj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * The index of the first prompt
     * 
     * @return
     */
    protected int startingIndex() {
        return 1;
    }

    /**
     * The total number of prompts
     * 
     * @return
     */
    public abstract int getPromptCount();

    /**
     * The survey prefix
     * 
     * @return
     */
    public abstract String getSurveyPrefix();
}
