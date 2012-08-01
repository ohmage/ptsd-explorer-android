package com.openmhealth.ohmage.core;

import java.util.HashMap;

public class Campaign {
	
	protected HashMap<Integer,Class<? extends EventRecord>> eventIDToEventRecordClass = new HashMap<Integer,Class<? extends EventRecord>>();
	
}
