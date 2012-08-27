
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer;

public class Phq9SurveyEvent extends SurveyEvent {

    public Phq9SurveyEvent(QuestionnairePlayer player) {
        super(player);
    }

    @Override
    public int getPromptCount() {
        return 9;
    }

    @Override
    public String getSurveyPrefix() {
        return "phq9";
    }

    @Override
    public String ohmageSurveyID() {
        return "phq9Survey";
    }
}
