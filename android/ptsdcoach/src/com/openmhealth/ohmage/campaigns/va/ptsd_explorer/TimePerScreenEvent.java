
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.ProbeRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.ohmage.probemanager.ProbeBuilder;

public class TimePerScreenEvent extends ProbeRecord {
    public String id;
    public long start;
    public long time;

    @Override
    protected String getStreamId() {
        return "timePerScreen";
    }

    @Override
    protected int getStreamVersion() {
        return 2;
    }

    @Override
    public ProbeBuilder buildProbe(String observerName, int observerVersion) {
        ProbeBuilder probe = super.buildProbe(observerName, observerVersion);
        try {
            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("start", start);
            data.put("time", time);
            probe.setData(data.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return probe;
    }
}
