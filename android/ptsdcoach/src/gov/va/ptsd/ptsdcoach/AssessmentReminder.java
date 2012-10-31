package gov.va.ptsd.ptsdcoach;

import java.util.ArrayList;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.activities.AssessNavigationController;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AssessmentReminder extends BroadcastReceiver {

	public void notifyPCL(Context context) {
		
		UserDBHelper udb = UserDBHelper.instance(context);
		ArrayList<String> surveys = AssessNavigationController.getSurveys(udb, null);

		if (surveys.isEmpty())
			return;
		
		// Set up the notification
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

		int icon = R.drawable.icon;
		long when = System.currentTimeMillis();

		Intent notificationIntent = new Intent("gov.va.ptsd.ptsdcoach.TAKE_ASSESSMENT");
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Notification notification = new Notification(icon, "PTSD Coach Assessment", when);
		notification.setLatestEventInfo(context, "PTSD Coach Assessment", "It is time to take your periodic PTSD assessment.", contentIntent);
		notification.defaults |= Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_NO_CLEAR;

		try {
			mNotificationManager.notify(1, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		notifyPCL(context);
	}
}
