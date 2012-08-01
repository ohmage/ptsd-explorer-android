package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import java.util.HashMap;

import com.openmhealth.ohmage.core.EventRecord;
import com.openmhealth.ohmage.core.Campaign;

public class VaPtsdExplorerCampaign extends Campaign {
	
	static private HashMap<Integer,Class<? extends EventRecord>> eventMappings = new HashMap<Integer,Class<? extends EventRecord>>();
	
	static {
        eventMappings.put(Integer.valueOf(0), DailyAssessmentEvent.class);
        eventMappings.put(Integer.valueOf(1), FunctioningAssessmentEvent.class);
        eventMappings.put(Integer.valueOf(2), PclAssessmentEvent.class);
        eventMappings.put(Integer.valueOf(3), Phq9SurveyEvent.class);
        eventMappings.put(Integer.valueOf(4), PclAssessmentStartedEvent.class);
        eventMappings.put(Integer.valueOf(5), PclQuestionAnsweredEvent.class);
        eventMappings.put(Integer.valueOf(6), ButtonPressedEvent.class);
        eventMappings.put(Integer.valueOf(7), ContentScreenViewedEvent.class);
        eventMappings.put(Integer.valueOf(8), ContentObjectSelectedEvent.class);
        eventMappings.put(Integer.valueOf(9), PreExerciseSudsEvent.class);
        eventMappings.put(Integer.valueOf(10), PostExerciseSudsEvent.class);
        eventMappings.put(Integer.valueOf(11), AppLaunchedEvent.class);
        eventMappings.put(Integer.valueOf(12), AppExitedEvent.class);
        eventMappings.put(Integer.valueOf(13), PclReminderScheduledEvent.class);
        eventMappings.put(Integer.valueOf(14), PclAssessmentAbortedEvent.class);
        eventMappings.put(Integer.valueOf(15), PclAssessmentCompletedEvent.class);
        eventMappings.put(Integer.valueOf(16), TotalTimeOnAppEvent.class);
        eventMappings.put(Integer.valueOf(17), TimePerScreenEvent.class);
        eventMappings.put(Integer.valueOf(18), TimeElapsedBetweenSessionsEvent.class);
        eventMappings.put(Integer.valueOf(19), TimeElapsedBetweenPCLAssessmentsEvent.class);
        eventMappings.put(Integer.valueOf(20), ToolAbortedEvent.class);
	}

	public VaPtsdExplorerCampaign() {
		eventIDToEventRecordClass.putAll(eventMappings);
	}
}
