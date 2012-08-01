package gov.va.ptsd.ptsdcoach.activities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.androidplot.ui.layout.AnchorPosition;
import com.androidplot.ui.layout.XLayoutStyle;
import com.androidplot.ui.layout.YLayoutStyle;
import com.androidplot.ui.widget.Widget;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.LineAndPointRenderer;
import com.androidplot.xy.XYPlot;
import com.flurry.android.FlurryAgent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.PclAssessmentAbortedEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.PclAssessmentCompletedEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.PclAssessmentStartedEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.PclReminderScheduledEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.TimeElapsedBetweenPCLAssessmentsEvent;
import com.openmhealth.ohmage.core.EventLog;

import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.content.ContentActivity;
import gov.va.ptsd.ptsdcoach.content.PCLScore;
import gov.va.ptsd.ptsdcoach.content.PCLSeries;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewController;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;
import gov.va.ptsd.ptsdcoach.controllers.PCLHistoryController;
import gov.va.ptsd.ptsdcoach.questionnaire.Questionnaire;
import gov.va.ptsd.ptsdcoach.questionnaire.QuestionnaireHandler;
import gov.va.ptsd.ptsdcoach.questionnaire.Settings;
import gov.va.ptsd.ptsdcoach.questionnaire.SurveyUtil;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnaireManager;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer;
import gov.va.ptsd.ptsdcoach.questionnaire.android.QuestionnairePlayer.QuestionnaireListener;
import android.app.ActivityGroup;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.Menu;

public class AssessNavigationController extends NavigationController implements QuestionnaireListener {

	QuestionnairePlayer player;
	MenuItem favoritesItem;
	boolean inQuestionnaire = false;
	
	static private AssessNavigationController instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
	}
	
	static public void takeAssessment() {
		if (instance != null) {
			instance.takeAssessment(false);
		}
	}
	
	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	public static final int BUTTON_REMIND_ME = 100;
	public static final int BUTTON_TAKE_IT_ANYWAY = 101;
	public static final int BUTTON_PROMPT_TO_SCHEDULE = 102;
	public static final int BUTTON_SEE_HISTORY = 103;
	public static final int BUTTON_RETURN_TO_ROOT = 104;
	public static final int BUTTON_SCHEDULE_IN_MONTH = 105;

	static final String[] numbersToWords = {
		null,
		"one",
		"two",
		"three",
		"four",
		"five",
		"six",
		"seven",
		"eight",
		"nine",
		"ten",
		"eleven",
		"twelve",
		"thirteen",
		"fourteen",
		"fifteen",
		"sixteen",
		"seventeen",
		"eighteen",
		"nineteen",
		"twenty",
		"twenty-one",
		"twenty-two",
		"twenty-three",
		"twenty-four"
	};

/*	
	private String timeIntervalToString(long interval) {
		double secondsInterval = interval / 1000.0;
		double minutesInternal = (secondsInterval / 60);
		double hoursInterval = (minutesInternal / 60);
		double daysInterval = (hoursInterval / 24);
		if (interval < 50) {
			int seconds = (int)secondsInterval;
			if (seconds == 0) seconds = 1;
			return String.format("%s second%s",numbersToWords[seconds],(seconds > 1) ? "s" : "");
		} else if (minutesInternal < 20) {
			int minutes = minutesInternal;
			if (minutes == 0) minutes = 1;
			return [NSString stringWithFormat:"%@ minute%",numbersToWords[minutes],(minutes > 1) ? "s" : "" ];
		} else if (hoursInterval < 20) {
			int hours = hoursInterval;
			if (hours == 0) hours = 1;
			return [NSString stringWithFormat:"%@ hour%",numbersToWords[hours],(hours > 1) ? "s" : "" ];
		} else if (daysInterval < 6) {
			int days = daysInterval;
			return [NSString stringWithFormat:"%@ day%",numbersToWords[days],(days > 1) ? "s" : "" ];
		} else if ((daysInterval < 27)) {
			int weeks = daysInterval / 7;
			if (weeks == 0) weeks = 1;
			return [NSString stringWithFormat:"%@ week%",numbersToWords[weeks],(weeks > 1) ? "s" : "" ];
		} else {
			int months = daysInterval / 30;
			if (months == 0) months = 1;
			return [NSString stringWithFormat:"%@ month%",numbersToWords[months],(months > 1) ? "s" : "" ];
		}
	}
*/	

	private PCLScore getLastPCLScore() {
		return userDb.getLastPCLScore();
	}

	@Override
	public void onBackPressed() {
		if (stack.size() < 2) {
			goBack();
			return;
		}

		if (inQuestionnaire) {
			PCLScore lastScoreObj = userDb.getLastPCLScore();
			int lastScore = (lastScoreObj != null) ? lastScoreObj.score : -1;

			TreeMap<String,String> map = new TreeMap<String, String>();
			map.put("lastScore",""+lastScore);
			map.put("score","-1");
			map.put("completed","no");
			FlurryAgent.logEvent("ASSESSMENT",map);
		}
		popView();
	}
	
	public void takeAssessment(boolean force) {
		boolean tooSoon = false;
		String pclSince = "in the time since you last took this assessment";
		String pclLastTime = "just recently";
		PCLScore lastScoreObj = getLastPCLScore();
		if (lastScoreObj == null) {
			pclSince = "in the past month";
			pclLastTime = "a month ago";
		} else {
			Date now = new Date();
			Date lastScoreTime = new Date(lastScoreObj.time);
			long secondsSinceLastTime = (now.getTime() - lastScoreTime.getTime())/1000L;
			long daysSinceLastTime = ((secondsSinceLastTime / 60)/60)/24;
			if (daysSinceLastTime < 6) tooSoon = true;
			if (daysSinceLastTime < 1) {
				pclSince = "in the time since you last took this assessment";
				pclLastTime = "less than a day ago";
			} else if (daysSinceLastTime < 6) {
				int days = (int)daysSinceLastTime;
				pclSince = String.format("in the past %s day%s",numbersToWords[days],(days > 1) ? "s" : "");
				pclLastTime = String.format("%s day%s ago",numbersToWords[days],(days > 1) ? "s" : "" );
			} else if ((daysSinceLastTime < 27)) {
				int weeks = (int)(daysSinceLastTime / 7);
				if (weeks == 0) weeks = 1;
				pclSince = String.format("in the past %s week%s",numbersToWords[weeks],(weeks > 1) ? "s" : "");
				pclLastTime = String.format("%s week%s ago",numbersToWords[weeks],(weeks > 1) ? "s" : "");
			} else {
				int months = (int)(daysSinceLastTime / 30);
				if (months == 0) months = 1;
				pclSince = String.format("in the past %s month%s",numbersToWords[months],(months > 1) ? "s" : "");
				pclLastTime = String.format("%s month%s ago",numbersToWords[months],(months > 1) ? "s" : "");
			}
		}

		String pclSinceCap = pclSince.substring(0, 1).toUpperCase() +  pclSince.substring(1);

		setVariable("pclSince", pclSince);
		setVariable("pclSinceCap", pclSinceCap);
		setVariable("pclLastTime", pclLastTime);

		if (tooSoon && !force) {
			ContentViewController cvc = (ContentViewController)db.getContentForName("pclTooSoon").createContentView(this);
			cvc.addButton("Remind me after a week", BUTTON_REMIND_ME);
			cvc.addButton("Take it now", BUTTON_TAKE_IT_ANYWAY);
			pushView(cvc);
			return;
		}
		
		startQuestionnaire();
	}
	
	public void startQuestionnaire() {
		PclAssessmentStartedEvent e = new PclAssessmentStartedEvent();
		e.pclAssessmentStarted = System.currentTimeMillis();
		EventLog.log(e);

		try {
			QuestionnaireHandler handler = new QuestionnaireHandler();
			QuestionnaireManager.parseQuestionaire(getAssets().open("pcl.xml"), handler);
			Questionnaire q = handler.getQuestionaire();
			player = new QuestionnairePlayer(this, q) {
				public String getGlobalVariable(String key) {
					String val = getVariable(key);
					if (val != null) return val;
					return super.getGlobalVariable(key);
				};
			};
			player.setQuestionnaireListener(this);
			inQuestionnaire = true;
			player.play();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void seeHistory() {
		List<PCLScore> history = userDb.getPCLScores();
		if (history.size() == 0) {
			ContentViewController cvc = (ContentViewController)db.getContentForName("pclNoHistory").createContentView(this);
			pushView(cvc);
			return;
		}

		PCLHistoryController historyView = new PCLHistoryController(this, history);
		pushView(historyView);
	}

	@Override
	public void buttonTapped(int id) {
		if (id == BUTTON_SEE_HISTORY) {
			seeHistory();
		} else if (id == BUTTON_TAKE_IT_ANYWAY) {
			takeAssessment(true);
		} else if (id == BUTTON_REMIND_ME) {
			userDb.setSetting("pclScheduled", "week");
			schedulePCLReminder(7*24*60*60,true,true);
			setVariable("pclScheduledWhen","one week");
			ContentViewController cvc = (ContentViewController)db.getContentForName("pclScheduled").createContentView(this);
			cvc.addButton("Ok",BUTTON_RETURN_TO_ROOT);
			pushReplaceView(cvc);
		} else if (id == BUTTON_PROMPT_TO_SCHEDULE) {
			ContentViewController cvc = (ContentViewController)db.getContentForName("pclSchedulePrompt").createContentView(this);
			cvc.addButton("No, thanks",BUTTON_SEE_HISTORY);
			cvc.addButton("Schedule the reminder",BUTTON_SCHEDULE_IN_MONTH);
			pushReplaceView(cvc);
		} else if (id == BUTTON_SCHEDULE_IN_MONTH) {
			userDb.setSetting("pclScheduled", "month");
			schedulePCLReminder(30*24*60*60,true,true);
			setVariable("pclScheduledWhen","one month");
			ContentViewController cvc = (ContentViewController)db.getContentForName("pclScheduled").createContentView(this);
			cvc.addButton("See Assessment History",BUTTON_SEE_HISTORY);
			pushReplaceView(cvc);
		} else if (id == BUTTON_RETURN_TO_ROOT) {
			popToRoot();
		} else {
			super.buttonTapped(id);
		}
	}
	
	@Override
	public void contentSelected(Content c) {
		if (c.getName().equals("takeAssessment")) {
			takeAssessment(false);
		} else if (c.getName().equals("trackHistory")) {
			seeHistory();
		} else {
			super.contentSelected(c);
		}
	}

	public void schedulePCLReminder(double secondsFromLast, boolean before, boolean repeat) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(1);

		long when = System.currentTimeMillis();
		PCLScore lastScoreObj = getLastPCLScore();
		if (lastScoreObj != null) when = lastScoreObj.time;
		long interval = (long)(secondsFromLast * 1000L);
		when += interval;
		
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent reminderIntent = new Intent("gov.va.ptsd.ptsdcoach.REMIND_ASSESSMENT");
		PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, reminderIntent, 0);
		am.cancel(reminderPendingIntent);
		am.set(AlarmManager.RTC_WAKEUP, when, reminderPendingIntent);
		
		PclReminderScheduledEvent e = new PclReminderScheduledEvent();
		e.pclReminderScheduledTimestamp = when;
		EventLog.log(e);
	}
	
/*		
		UILocalNotification *n = [[UILocalNotification alloc] init];
		NSManagedObject *lastScoreObj = [AssessNavigationController getLastPCLScore];
		NSDate *lastTime = lastScoreObj ? (NSDate*)[lastScoreObj valueForKey:@"time"] : [NSDate date];
		n.fireDate = [NSDate dateWithTimeInterval:secondsFromLast sinceDate:lastTime];
		n.timeZone = [NSTimeZone defaultTimeZone];
		if (before) {
			n.alertBody = [NSString stringWithFormat:@"It has been %@ since you took your iStressLess assessment.  Would you like to take it now?", [AssessNavigationController timeIntervalToString:secondsFromLast]];
		} else {
			n.alertBody = @"You asked me to remind you to take your iStressLess assessment around now.  Would you like to take it now?";
		}
		n.alertAction = @"Do it";
		n.soundName = UILocalNotificationDefaultSoundName;
		n.applicationIconBadgeNumber = 1;
		n.repeatInterval = NSDayCalendarUnit;
		[[UIApplication sharedApplication] cancelAllLocalNotifications];
		[[UIApplication sharedApplication] scheduleLocalNotification:n];
		[n release];
*/		

	public String getPCLReminderSchedule() {
		return userDb.getSetting("pclScheduled");
	}

	public void schedulePCLReminder(String interval) {
		userDb.setSetting("pclScheduled", interval);
		
		if (interval.equals("none")) {
			// Cancel any notifications
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			mNotificationManager.cancel(1);
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			Intent reminderIntent = new Intent("gov.va.ptsd.ptsdcoach.REMIND_ASSESSMENT");
			PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, reminderIntent, 0);
			am.cancel(reminderPendingIntent);

			PclReminderScheduledEvent e = new PclReminderScheduledEvent();
			e.pclReminderScheduledTimestamp = 0;
			EventLog.log(e);
		} else {
			PCLScore lastScoreObj = getLastPCLScore();
			boolean before = (lastScoreObj != null);
			if (interval.equals("minute")) {
				schedulePCLReminder(60, before, true);
			} else if (interval.equals("week")) {
				schedulePCLReminder(7*24*60*60, before, true);
			} else if (interval.equals("month")) {
				schedulePCLReminder(30*24*60*60, before, true);
			} else if (interval.equals("twoweek")) {
				schedulePCLReminder(14*24*60*60, before, true);
			} else if (interval.equals("threemonth")) {
				schedulePCLReminder(90*24*60*60, before, true);
			}
		}
	}

	@Override
	public void onQuestionnaireCompleted(QuestionnairePlayer player) {
		inQuestionnaire = false;
		
		Hashtable answers = player.getAnswers();
		int totalScore = 0;
		for (Object o : answers.entrySet()) {
			Map.Entry entry = (Map.Entry)o;
			String str = SurveyUtil.answerToString(entry.getValue());
			int val = Integer.parseInt(str);
			totalScore += val;
		}

		PCLScore lastScoreObj = userDb.getLastPCLScore();
		int lastScore = (lastScoreObj != null) ? lastScoreObj.score : -1;

		Date now = new Date();
		userDb.addPCLScore(now.getTime(), totalScore);
		
		TreeMap<String,String> map = new TreeMap<String, String>();
		map.put("lastScore",""+lastScore);
		map.put("score",""+totalScore);
		map.put("completed","yes");
		FlurryAgent.logEvent("ASSESSMENT",map);
		
		{
			PclAssessmentCompletedEvent e = new PclAssessmentCompletedEvent();
			e.pclAssessmentCompleted = 1;
			e.pclAssessmentCompletedFinalScore = totalScore;
			EventLog.log(e);
		}
		
		if (lastScoreObj != null) {
			TimeElapsedBetweenPCLAssessmentsEvent e = new TimeElapsedBetweenPCLAssessmentsEvent();
			e.timeElapsedBetweenPCLAssessments = now.getTime() - lastScoreObj.time;
			EventLog.log(e);
		}

		player = null;
		
		String absStr = null;
		String relStr = null;

		if (totalScore >= 50) {
			absStr = "High";
		} else if (totalScore >= 30) {
			absStr = "Mid";
		} else if (totalScore == 17) {
			absStr = "Bottom";
		} else {
			absStr = "Low";
		}
		
		if (lastScore == -1) {
			relStr = "First";
		} else if (totalScore > lastScore) {
			relStr = "Higher";
		} else if (totalScore == lastScore) {
			relStr = "Same";
		} else {
			relStr = "Lower";
		}
		
		String pclResultName = String.format("pcl%s%s",absStr,relStr);
		
		ContentViewController cvc = (ContentViewController)db.getContentForName(pclResultName).createContentView(this);

		String currentPCLScheduling = userDb.getSetting("pclScheduled");
		if ((currentPCLScheduling == null) || currentPCLScheduling.equals("") || currentPCLScheduling.equals("none")) {
			cvc.addButton("Next", BUTTON_PROMPT_TO_SCHEDULE);
		} else {
			schedulePCLReminder(currentPCLScheduling);
			cvc.addButton("See Symptom History", BUTTON_SEE_HISTORY);
		}
		pushReplaceView(cvc);
	}
	
	@Override
	public void onQuestionnaireDeferred(QuestionnairePlayer player) {
		{
			PclAssessmentAbortedEvent e = new PclAssessmentAbortedEvent();
			e.pclAssessmentAbortedTimestamp = System.currentTimeMillis();
			EventLog.log(e);
		}
		
		{
			PclAssessmentCompletedEvent e = new PclAssessmentCompletedEvent();
			e.pclAssessmentCompleted = 0;
			e.pclAssessmentCompletedFinalScore = -1;
			EventLog.log(e);
		}
	}

	@Override
	public void onQuestionnaireSkipped(QuestionnairePlayer player) {
	}

	@Override
	public void onShowScreen(QuestionnairePlayer player, View screen) {
		if (stack.size() > 1) {
			pushReplaceView(screen);
		} else {
			pushView(screen);
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		PTSDCoach a = (PTSDCoach)getParent();
		if(a.fromBackground)
		{
			//we are going into the background, so pop to root and 
			if(stack.size() >1)
				this.popToRoot();
			this.goBack();
		}
	}

}
