package gov.va.ptsd.ptsdcoach.activities;

import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.questionnaire.SurveyUtil;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnaireActivityBase;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnaireManager;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

public class QuestionnaireActivity extends QuestionnaireActivityBase {

	boolean done = false;
	
/*
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setMessage("Are you sure you want to defer this su");
		b.setPositiveButton("Ok", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startQuestionnaire();
			}
		});

		b.setNegativeButton("Ask me later", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				deferQuestionnaire();
			}
		});
		
		return b.create();
	}
*/	
	public void onSurveyDone() {
		
		Hashtable answers = player.getAnswers();
		int score = 0;
		for (Object o : answers.entrySet()) {
			Map.Entry entry = (Map.Entry)o;
			String str = SurveyUtil.answerToString(entry.getValue());
			int val = Integer.parseInt(str);
			score += val;
		}
		
		Date now = new Date();
		UserDBHelper.instance(this).addPCLScore(now.getTime(), score);

		int notificationID = pendingQuestionnaire.notificationID;
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(notificationID);
		QuestionnaireManager.getInstance(this).getDB().removePendingQuestionnaire(pendingQuestionnaire.key);
		done = true;
		finish();
	}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startQuestionnaire();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if (!done) {
    		onQuestionnaireDeferred(player);
    	}
    }
    
}