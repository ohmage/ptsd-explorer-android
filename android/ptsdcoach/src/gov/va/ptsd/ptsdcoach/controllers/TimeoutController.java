package gov.va.ptsd.ptsdcoach.controllers;


import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeoutController extends SimpleExerciseController {

	TextView timer;
	long timeout;
	Runnable timerUpdateRunnable;
	Handler handler;
	
	public TimeoutController(Context ctx) {
		super(ctx);
	}

	
	public void updateTimer() {
		String label;
		long now = SystemClock.elapsedRealtime();
		
		long delta = timeout - now;
		if (delta <= 0) {
			label = "00:00";
			if (handler != null) {
				handler.removeCallbacks(timerUpdateRunnable);
			}
		} else {
			long minutes;
			long seconds;
			
			seconds=(delta/1000);
			
			minutes = seconds / 60;
			seconds -= (minutes*60);
			label = String.format("%02d:%02d",new Long(minutes), new Long(seconds));
			if (handler != null) {
				handler.postDelayed(timerUpdateRunnable, 500);
			}
		}
		timer.setText(label);
	}
	
	@Override
	public void build() {
		super.build();
		
		timer = new TextView(getContext());
		timer.setGravity(Gravity.CENTER);
		timer.setTextColor(0xFFFFFFFF);
		timer.setTextSize(64);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		clientView.addView(timer,params);

		timerUpdateRunnable = new Runnable() {
			@Override
			public void run() {
				updateTimer();
			}
		};
		
	}
	
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		handler = getHandler();
		timeout = SystemClock.elapsedRealtime() + (300*1000L)+500; //set it to 5 minutes
		updateTimer();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if (handler != null) {
			handler.removeCallbacks(timerUpdateRunnable);
			handler = null;
		}
		super.onDetachedFromWindow();
	}
}
