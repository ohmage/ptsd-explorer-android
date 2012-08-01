package gov.va.ptsd.ptsdcoach.controllers;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.views.LoggingButton;
import gov.va.ptsd.ptsdcoach.views.LoggingImageButton;

public class BaseExerciseController extends ContentViewController implements SensorEventListener {

	Integer score;
	LoggingImageButton thumbsUp;
	LoggingImageButton thumbsDown;
	LoggingImageButton ccToggle;

	boolean accelSupported;
	long lastUpdate=-1;
	float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 800;
	
	public BaseExerciseController(Context ctx) {
		super(ctx);
		viewTypeID = 0; // tool
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
/*
		long curTime = System.currentTimeMillis();
		// only allow one update every 100ms.
		if ((curTime - lastUpdate) > 100) {
			long diffTime = (curTime - lastUpdate);
			lastUpdate = curTime;

			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];

			float speed = Math.abs(x+y+z - last_x - last_y - last_z)
			/ diffTime * 10000;
			if (speed > SHAKE_THRESHOLD) {
				buttonTapped(ManageNavigationController.BUTTON_NEW_TOOL);
			}
			last_x = x;
			last_y = y;
			last_z = z;
		}
*/		
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		SensorManager sensorMgr = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		if (sensor == null) return;
		
		accelSupported = sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		if (!accelSupported) {
		    sensorMgr.unregisterListener(this);
		}	
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if (accelSupported) {
			SensorManager sensorMgr = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
			sensorMgr.unregisterListener(this);
		}
		super.onDetachedFromWindow();
	}

	@Override
    public void toggleCC() {
		super.toggleCC();
		updateThumbs();
	}
	
	public void updateThumbs() {
		if ((score == null) || (score.intValue() == 0)) {
			thumbsUp.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00000000));
			thumbsDown.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00000000));
			thumbsUp.setContentDescription("thumbs up");
			thumbsDown.setContentDescription("thumbs down");
		} else if (score.intValue() > 0) {
			thumbsUp.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0040b000));
			thumbsDown.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00000000));
			thumbsUp.setContentDescription("thumbs up selected");
			thumbsDown.setContentDescription("thumbs down");
		} else {
			thumbsUp.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00000000));
			thumbsDown.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00FF0000));
			thumbsUp.setContentDescription("thumbs up");
			thumbsDown.setContentDescription("thumbs down selected");
		}
		
		if (ccToggle != null) {
			String cc =  UserDBHelper.instance(getContext()).getSetting("cc");
			if ("true".equals(cc)) {
				ccToggle.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00FFFF00));
			} else {
				ccToggle.setColorFilter(null);
			}
		}
	}
	
	public void addThumbs() {
		score = getContent().getScore();
		
		LinearLayout.LayoutParams layout;
		LoggingButton b = new LoggingButton(getContext());
		b.setText("Some Text");
		b.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
		int height = b.getMeasuredHeight();

		thumbsUp = new LoggingImageButton(getContext());
		thumbsUp.setScaleType(ScaleType.CENTER_INSIDE);
		thumbsUp.setContentDescription("thumbs up");
		Drawable thumbsUpImage = Util.makeDrawable(getContext(), "thumbsup.png", true);
		thumbsUp.setImageDrawable(thumbsUpImage);
		layout = new LinearLayout.LayoutParams(height,height);
		thumbsUp.setLayoutParams(layout);
		thumbsUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((score != null) && (score == 1))
					score = 0;
				else 
					score = 1;
				getContent().setScore(score);
				updateThumbs();
			}
		});
		getLeftButtons().addView(thumbsUp);

		thumbsDown = new LoggingImageButton(getContext());
		thumbsDown.setScaleType(ScaleType.CENTER_INSIDE);
		thumbsDown.setContentDescription("thumbs down");
		Drawable thumbsDownImage = Util.makeDrawable(getContext(), "thumbsdown.png", true);
		thumbsDown.setImageDrawable(thumbsDownImage);
		layout = new LinearLayout.LayoutParams(height,height);
		thumbsDown.setLayoutParams(layout);
		thumbsDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((score != null) && (score == -1))
					score = 0;
				else 
					score = -1;
				getContent().setScore(score);
				updateThumbs();
			}
		});
		getLeftButtons().addView(thumbsDown);

		if (hasCaptions()) {
			ccToggle = new LoggingImageButton(getContext());
			ccToggle.setContentDescription("toggle closed captioning");
			ccToggle.setScaleType(ScaleType.CENTER_INSIDE);
			Drawable ccToggleImage = Util.makeDrawable(getContext(), "closed_captioning_symbol.png", true);
			ccToggle.setImageDrawable(ccToggleImage);
			layout = new LinearLayout.LayoutParams(height,height);
			ccToggle.setLayoutParams(layout);
			ccToggle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleCC();
				}
			});
			getLeftButtons().addView(ccToggle);		
		}

		updateThumbs();
	}
	
	@Override
    public boolean shouldAddListenButton() {
		return false;
	}

}
