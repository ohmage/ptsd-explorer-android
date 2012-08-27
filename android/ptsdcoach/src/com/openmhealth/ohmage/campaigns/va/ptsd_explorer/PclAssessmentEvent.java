
package com.openmhealth.ohmage.campaigns.va.ptsd_explorer;

import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer;

public class PclAssessmentEvent extends SurveyEvent {

    public PclAssessmentEvent(QuestionnairePlayer player) {
        super(player);
    }

    @Override
    public int getPromptCount() {
        return 17;
    }

    @Override
    public String getSurveyPrefix() {
        return "pcl";
    }

    @Override
    public String ohmageSurveyID() {
        return "pclAssessment";
    }
}
