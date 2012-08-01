package com.openmhealth.ohmage.core;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class EventLog {

	static public void log(EventRecord event) {
		try {
			JSONObject obj = event.toJSON();
			// Do something useful...
			Log.v("ptsdexplorer",obj.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
