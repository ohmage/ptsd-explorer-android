
package gov.va.ptsd.ptsdcoach.fragments;

import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.content.PCLScore;

import java.util.Calendar;

import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.PclReminderScheduledEvent;
import com.openmhealth.ohmage.core.EventLog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class ReminderPickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    ReminderPickerListener mListener;

    public interface ReminderPickerListener {
        void onTimeSelected(ReminderPickerFragment fragment);

        void onCancelled(ReminderPickerFragment fragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        setReminder(getActivity(), c.getTimeInMillis());

        if (mListener != null)
            mListener.onTimeSelected(this);
    }

    public static void setReminder(Context context, long when) {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(ns);
        mNotificationManager.cancel(1);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent reminderIntent = new Intent("gov.va.ptsd.ptsdcoach.REMIND_ASSESSMENT");
        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(context, 0,
                reminderIntent, 0);
        am.cancel(reminderPendingIntent);

        am.setRepeating(AlarmManager.RTC, when, AlarmManager.INTERVAL_DAY, reminderPendingIntent);

        PclReminderScheduledEvent e = new PclReminderScheduledEvent();
        e.time = when;
        EventLog.log(e);

        UserDBHelper userDb = UserDBHelper.instance(context);
        userDb.setSetting("pclScheduled", "day");
        userDb.setSetting("pclTime", String.valueOf(when));

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mListener != null)
            mListener.onCancelled(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null)
            mListener.onCancelled(this);
    }

    public void setListener(ReminderPickerListener l) {
        mListener = l;
    }
}
