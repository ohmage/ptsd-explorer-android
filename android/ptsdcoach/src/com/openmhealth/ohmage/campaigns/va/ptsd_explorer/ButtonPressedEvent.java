
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.ProbeRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.ohmage.probemanager.ProbeBuilder;

public class ButtonPressedEvent extends ProbeRecord {
    public int buttonId;
    public String buttonTitle;

    @Override
    protected String getStreamId() {
        return "buttonPressed";
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
            data.put("id", buttonId);
            data.put("title", buttonTitle);
            probe.setData(data.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return probe;
    }
}
