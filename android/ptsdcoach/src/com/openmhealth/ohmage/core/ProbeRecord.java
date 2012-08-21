
package com.openmhealth.ohmage.core;

import org.ohmage.probemanager.ProbeBuilder;

import java.util.TimeZone;
import java.util.UUID;

abstract public class ProbeRecord {
    private final long timestamp;

    public ProbeRecord() {
        timestamp = System.currentTimeMillis();
    }

    protected abstract String getStreamId();

    protected abstract int getStreamVersion();

    // abstract public String ohmageSurveyID();
    // abstract public void addAttributesToOhmageJSON(JSONArray into);
    //
    // public JSONObject toJSON() throws JSONException {
    // JSONObject obj = new JSONObject();
    // JSONObject surveyLaunchContext = new JSONObject();
    // String uniqueID = UUID.randomUUID().toString();
    //
    // cal.setTimeInMillis(timestamp);
    // String formattedDateString = timestampFormat.format(cal.getTime());
    //
    // obj.put("survey_key", uniqueID);
    // obj.put("date", formattedDateString);
    // obj.put("time", timestamp);
    // obj.put("timezone", TimeZone.getDefault().getID());
    // obj.put("location_status", "unavailable");
    // surveyLaunchContext.put("active_triggers", new JSONArray());
    // surveyLaunchContext.put("launch_time", timestamp);
    // surveyLaunchContext.put("launch_timezone",
    // TimeZone.getDefault().getID());
    // obj.put("survey_launch_context", surveyLaunchContext);
    // obj.put("survey_id", ohmageSurveyID());
    //
    // JSONArray attributes = new JSONArray();
    // addAttributesToOhmageJSON(attributes);
    // obj.put("responses", attributes);
    //
    // return obj;
    // }

    public ProbeBuilder buildProbe(String observerName, int observerVersion) {
        return new ProbeBuilder(observerName, observerVersion)
                .setStream(getStreamId(), getStreamVersion())
                .withTime(timestamp, TimeZone.getDefault().getID())
                .withId(UUID.randomUUID().toString());
    }
}
