package gov.va.ptsd.ptsdcoach.questionnaire.android;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.questionnaire.Questionnaire;
import gov.va.ptsd.ptsdcoach.questionnaire.Settings;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnaireDatabase.PendingQuestionnaire;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer.QuestionnaireListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionnaireActivityBase extends Activity implements QuestionnaireListener {

	protected PendingQuestionnaire pendingQuestionnaire;
	protected Questionnaire questionnaire;
	protected QuestionnairePlayer player;
	protected ViewGroup veryTop;
	protected ViewGroup top;
	protected View currentScreen;

	static int currentSurveyID = 1;

	static public final int RUN_SURVEY_NOW = 1;
	
	static public final String VIEW_SURVEY_ACTION = ".service.survey.android.VIEW_SURVEY";

	@Override
	public void onShowScreen(QuestionnairePlayer player, View newScreen) {
		if (currentScreen != null) {
			top.removeView(currentScreen);			
		}

		top.addView(newScreen);
		
		if (currentScreen == null) {
			setContentView(veryTop);
		}
		currentScreen = newScreen;
	}

	private void setPlayerAndQuestionnaire() {
		try {
			QuestionnaireManager mgr = QuestionnaireManager.getInstance(this);
			questionnaire = mgr.getQuestionaire(getSurveyURL());
			player = new QuestionnairePlayer(getApplicationContext(), questionnaire);
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String dataStr = intent.getDataString();
		if (dataStr == null) dataStr = "pcl.xml";
		if (dataStr.startsWith("pendingQuestionnaire://")) {
			String key = dataStr.substring(23);
			pendingQuestionnaire = QuestionnaireManager.getInstance(this).getDB().getPendingQuestionnaire(key);
			setPlayerAndQuestionnaire(); 
		} else {
			pendingQuestionnaire = new PendingQuestionnaire();
			pendingQuestionnaire.triggerTime = System.currentTimeMillis();
			pendingQuestionnaire.key = Long.toString(pendingQuestionnaire.triggerTime);
			pendingQuestionnaire.url = dataStr;
			pendingQuestionnaire.notificationID = (int)pendingQuestionnaire.triggerTime;
			setPlayerAndQuestionnaire(); 
			Settings settings = questionnaire.getSettings();
			// check activation percentage here, finish if we shouldn't do it.
			float percentActive = Float.parseFloat(questionnaire.getSettings().getGlobal(player, Settings.VAR_PERCENT_ACTIVATION));
			Random numgen = new Random();
			float guess = numgen.nextFloat() * 100.0f;
			if (guess < percentActive) {
				QuestionnaireManager.getInstance(this).getDB().addPendingQuestionnaire(pendingQuestionnaire);
				Log.d("PTSDCoach","Play "+getSurveyURL()+" survey "+guess);
			} else {
				Log.d("PTSDCoach","Skip "+getSurveyURL()+" survey "+guess);
				return;
			}
		}
				
		LinearLayout topLayout = new LinearLayout(this);
		topLayout.setOrientation(LinearLayout.VERTICAL);
		top = topLayout;
		
		FrameLayout veryTopLayout = new FrameLayout(this);
		veryTop = veryTopLayout;

		RelativeLayout bg = new RelativeLayout(this);
		bg.setGravity(Gravity.LEFT | Gravity.BOTTOM);
/*
		ImageView bgLogo = new ImageView(this);
		bgLogo.setImageResource(R.drawable.logo_big_cropped);
		bgLogo.setAlpha(96);
		bg.addView(bgLogo);
		veryTop.addView(bg);
*/
		veryTop.addView(top);
		player.setQuestionnaireListener(this);
		player.setTriggerTime(pendingQuestionnaire.triggerTime);

	}

	protected void startQuestionnaire() {
		player.play();
	}

	protected void startIntro() {
		player.playIntro();
	}

	@Override
	public void onQuestionnaireCompleted(QuestionnairePlayer player) {
		//finish();
	}

	public String getSurveyURL() {
		if (pendingQuestionnaire != null) return pendingQuestionnaire.url;
		return null;
	}
	
	public Intent getSurveyIntent() {
		String packageName = getApplicationContext().getPackageName();
		Intent intent = getIntent();
		if ((intent.getAction() != null) && intent.getAction().equals(
			packageName+VIEW_SURVEY_ACTION)) return intent;
		Intent notificationIntent = new Intent();
		notificationIntent.setClassName(packageName,
			packageName+".service.survey.android.QuestionnaireActivity");
		notificationIntent.setData(Uri.parse("pendingQuestionnaire://"+pendingQuestionnaire.key));
		return notificationIntent;
	}

	@Override
	public void onQuestionnaireSkipped(QuestionnairePlayer player) {
		finish();
	}

	@Override
	public void onQuestionnaireDeferred(QuestionnairePlayer player) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.icon;
		CharSequence title = player.getGlobalVariable(Settings.VAR_TITLE);
		CharSequence tickerText = title;
		long when = System.currentTimeMillis();

		Intent notificationIntent = getSurveyIntent();

		Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		CharSequence contentTitle = title;
		CharSequence contentText = player.getGlobalVariable(Settings.VAR_NOTIFICATION);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		// notification.flags |= Notification.FLAG_INSISTENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;

		try {
			mNotificationManager.notify(pendingQuestionnaire.notificationID, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}
}
