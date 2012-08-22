
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.ProbeRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.ohmage.probemanager.ProbeBuilder;

public class PclQuestionAnsweredEvent extends ProbeRecord {
    public long pclNumberOfQuestionsAnswered;

    @Override
    protected String getStreamId() {
        return "pclQuestionAnswered";
    }

    @Override
    protected int getStreamVersion() {
        return 1;
    }

    @Override
    public ProbeBuilder buildProbe(String observerName, int observerVersion) {
        ProbeBuilder probe = super.buildProbe(observerName, observerVersion);
        try {
            JSONObject data = new JSONObject();
            data.put("answered_count", pclNumberOfQuestionsAnswered);
            probe.setData(data.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return probe;
    }
}
