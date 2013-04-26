
package gov.va.ptsd.ptsdcoach;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gov.va.ptsd.ptsdcoach.fragments.ReminderPickerFragment;

import java.util.Calendar;

public class OnBootReceiver extends BroadcastReceiver {

    public static final String TAG = "BOOT_RECEIVER";

    @Override
    public void onReceive(final Context context, Intent intent) {

        // Reset the reminder if it exists
        UserDBHelper userDb = UserDBHelper.instance(context);
        String whenStr = userDb.getSetting("pclTime");
        String interval = userDb.getSetting("pclScheduled");

        if (whenStr != null) {
            long when = Long.valueOf(whenStr);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(when);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
            today.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            ReminderPickerFragment.setReminder(context, interval, today.getTimeInMillis());
        }
    }

}
