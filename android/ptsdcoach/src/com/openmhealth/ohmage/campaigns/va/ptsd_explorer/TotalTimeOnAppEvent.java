
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.ProbeRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.ohmage.probemanager.ProbeBuilder;

public class TotalTimeOnAppEvent extends ProbeRecord {
    public long totalTimeOnApp;

    @Override
    protected String getStreamId() {
        return "totalTimeOnApp";
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
            data.put("time", totalTimeOnApp);
            probe.setData(data.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return probe;
    }
}
