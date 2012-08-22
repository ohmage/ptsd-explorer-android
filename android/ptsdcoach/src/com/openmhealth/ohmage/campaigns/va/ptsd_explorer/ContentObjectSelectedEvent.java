
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import com.openmhealth.ohmage.core.ProbeRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.ohmage.probemanager.ProbeBuilder;

public class ContentObjectSelectedEvent extends ProbeRecord {
    public String name;
    public String displayName;
    public String id;

    @Override
    protected String getStreamId() {
        return "contentObjectSelected";
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
            data.put("name", name);
            data.put("display_name", displayName);
            data.put("id", id);
            probe.setData(data.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return probe;
    }
}
