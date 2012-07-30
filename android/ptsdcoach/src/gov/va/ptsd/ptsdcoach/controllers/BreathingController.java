package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class BreathingController extends KeyframeExerciseController {

	FrameLayout balloonContainerView;
	FrameLayout balloonSubcontainer;
	ImageView balloonOutline;
	ImageView balloonGreen;
	ImageView balloonYellow;
	ImageView balloonRed;
	View currentVisible;
	TextView labelView;

	PointF lastCenter;
	float lastScale;

	public BreathingController(Context ctx) {
		super(ctx);
	}
	
	static private float BREATH_TIME = 5.0f;
	static private float HOLD_TIME = 2.0f;
	static private float PAUSE_TIME = 2.0f;
	static private float INITIAL_FADE_IN_TIME = 38.0f;
	static private float INITIAL_BREATH_TIME = 43.0f;
	static private float SECOND_BREATH_TIME = 58.0f;
	static private float INITIAL_COUNTING_BREATH_TIME = 71.0f;
	static private float PULSE_DURATION = 4.0f;
	static private float PULSE_FADE_DURATION = 1.0f;

	protected void pulseMessage(final String text, float interval, final float duration) {
		final float fadeDuration = duration / 4;
		
		postAtTimeFromStart(interval-0.01f, new CancelableRunnable() {
			@Override
			public void run() {
				labelView.setText(text);
//				labelView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

//				int width = labelView.getMeasuredWidth();
//				int height = labelView.getMeasuredHeight();

				int width = 256;
				int height = 256;
				float scale = 0.5f;

				View parent = (View)labelView.getParent();
				int parentWidth = parent.getWidth();
				int parentHeight = parent.getHeight();

				AnimationSet pulse = new AnimationSet(false);
				TranslateAnimation panAnim = new TranslateAnimation((0.5f * parentWidth) - (0.5f * width * scale),0,(0.5f * parentHeight) - (0.5f * height * scale),0);
				ScaleAnimation scaleAnim = new ScaleAnimation(scale, 1f, scale, 1f);
				AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
				AlphaAnimation fadeOut = new AlphaAnimation(1, 0);

				scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
				scaleAnim.setDuration((long)(duration * 1000L));
				panAnim.setInterpolator(new AccelerateDecelerateInterpolator());
				panAnim.setDuration((long)(duration * 1000L));
				fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
				fadeIn.setDuration((long)(fadeDuration * 1000L));
				fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
				fadeOut.setDuration((long)(fadeDuration * 1000L));
				
				long now = AnimationUtils.currentAnimationTimeMillis()+10;
				scaleAnim.setStartOffset(0);
				scaleAnim.setFillEnabled(true);
				scaleAnim.setFillBefore(true);
				scaleAnim.setFillAfter(true);

				panAnim.setStartOffset(0);
				panAnim.setFillEnabled(true);
				panAnim.setFillBefore(true);
				panAnim.setFillAfter(true);

				fadeIn.setStartOffset(0);
				fadeIn.setFillEnabled(true);
				fadeIn.setFillBefore(true);
				fadeIn.setFillAfter(true);

				fadeOut.setStartOffset(((long)((duration - fadeDuration) * 1000L)));
				fadeOut.setFillEnabled(true);
				fadeOut.setFillBefore(false);
				fadeOut.setFillAfter(true);

				pulse.setFillEnabled(true);
				pulse.setFillBefore(true);
				pulse.setFillAfter(true);
				pulse.addAnimation(scaleAnim);
				pulse.addAnimation(panAnim);
				pulse.addAnimation(fadeIn);
				pulse.addAnimation(fadeOut);
				pulse.setStartTime(now);
				
				labelView.invalidate();
				labelView.setAnimation(pulse);

				super.run();
			}
		});
	}


	protected void inflateAt(float interval, final float duration) {
		postAtTimeFromStart(interval, new CancelableRunnable() {
			@Override
			public void run() {
				int width = 256;
				int height = 256;
				float scale = 0.2f;
				AnimationSet scaleAndPan = new AnimationSet(true);
				ScaleAnimation scaleAnim = new ScaleAnimation(scale, 1f, scale, 1f);
				TranslateAnimation panAnim = new TranslateAnimation((0.5f * width) - (0.5f * width * scale),0,(0.5f * height) - (0.5f * height * scale),0);
				scaleAndPan.addAnimation(scaleAnim);
				scaleAndPan.addAnimation(panAnim);
				scaleAndPan.setInterpolator(new AccelerateDecelerateInterpolator());
				scaleAndPan.setDuration((long)(duration * 1000L));
				scaleAndPan.setFillAfter(true);
				scaleAndPan.setFillBefore(true);
				scaleAndPan.setFillEnabled(true);
				balloonSubcontainer.startAnimation(scaleAndPan);

				super.run();
			}
		});
	}

	protected void deflateAt(float interval, final float duration) {
		postAtTimeFromStart(interval, new CancelableRunnable() {
			@Override
			public void run() {
				int width = 256;
				int height = 256;
				float scale = 0.2f;
				AnimationSet scaleAndPan = new AnimationSet(true);
				ScaleAnimation scaleAnim = new ScaleAnimation(1f, scale, 1f, scale);
				TranslateAnimation panAnim = new TranslateAnimation(0,(0.5f * width) - (0.5f * width * scale),0,(0.5f * height) - (0.5f * height * scale));
				scaleAndPan.addAnimation(scaleAnim);
				scaleAndPan.addAnimation(panAnim);
				scaleAndPan.setInterpolator(new AccelerateDecelerateInterpolator());
				scaleAndPan.setDuration((long)(duration * 1000L));
				scaleAndPan.setFillAfter(true);
				scaleAndPan.setFillBefore(true);
				scaleAndPan.setFillEnabled(true);
				balloonSubcontainer.startAnimation(scaleAndPan);

				super.run();
			}
		});
	}

	protected void fadeTo(final View fadingIn, float interval, final float duration) {
		postAtTimeFromStart(interval, new CancelableRunnable() {
			@Override
			public void run() {
				if (currentVisible != fadingIn) {
					AlphaAnimation alpha = new AlphaAnimation(0, 1);
					alpha.setInterpolator(new AccelerateDecelerateInterpolator());
					alpha.setDuration((long)(duration * 1000L));
					alpha.setFillAfter(true);
					alpha.setFillBefore(true);
					alpha.setFillEnabled(true);
					alpha.setStartTime(AnimationUtils.currentAnimationTimeMillis());
					fadingIn.bringToFront();
					fadingIn.invalidate();
					fadingIn.setAnimation(alpha);
					currentVisible = fadingIn;
				}

				super.run();
			}
		});
	}

	protected void scheduleBreathAt(float t, String fullText, String emptyText) {
		inflateAt(t,BREATH_TIME);
		fadeTo(balloonYellow, t+BREATH_TIME-1, 2);
		if (fullText != null) pulseMessage(fullText,t+BREATH_TIME,HOLD_TIME);
		fadeTo(balloonRed,t+BREATH_TIME+HOLD_TIME-1,2);
		deflateAt(t+BREATH_TIME+HOLD_TIME,BREATH_TIME);
		if (emptyText != null) pulseMessage(emptyText,t+BREATH_TIME+HOLD_TIME+BREATH_TIME-1,PAUSE_TIME);
		fadeTo(balloonGreen,t+BREATH_TIME+HOLD_TIME+BREATH_TIME+PAUSE_TIME-1,1);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		playAudio();
		animStart = SystemClock.uptimeMillis();
		animStartAltTimebase = AnimationUtils.currentAnimationTimeMillis();

		int width = 256;
		int height = 256;
		float scale = 0.2f;
		AnimationSet scaleAndPan = new AnimationSet(true);
		ScaleAnimation scaleAnim = new ScaleAnimation(scale, scale, scale, scale);
		TranslateAnimation panAnim = new TranslateAnimation(
				(0.5f * width) - (0.5f * width * scale),
				(0.5f * width) - (0.5f * width * scale),
				(0.5f * height) - (0.5f * height * scale),
				(0.5f * height) - (0.5f * height * scale));
		scaleAndPan.addAnimation(scaleAnim);
		scaleAndPan.addAnimation(panAnim);
		scaleAndPan.setInterpolator(new LinearInterpolator());
		scaleAndPan.setDuration(1);
		scaleAndPan.setFillAfter(true);
		scaleAndPan.setFillBefore(true);
		scaleAndPan.setFillEnabled(true);
		balloonSubcontainer.startAnimation(scaleAndPan);

		AlphaAnimation alpha = new AlphaAnimation(0, 0);
		alpha.setInterpolator(new LinearInterpolator());
		alpha.setDuration(0);
		alpha.setFillAfter(true);
		alpha.setFillBefore(true);
		alpha.setFillEnabled(true);
		balloonOutline.startAnimation(alpha);

		alpha = new AlphaAnimation(0, 0);
		alpha.setInterpolator(new LinearInterpolator());
		alpha.setDuration(0);
		alpha.setFillAfter(true);
		alpha.setFillBefore(true);
		alpha.setFillEnabled(true);
		balloonGreen.startAnimation(alpha);
		
		alpha = new AlphaAnimation(0, 0);
		alpha.setInterpolator(new LinearInterpolator());
		alpha.setDuration(0);
		alpha.setFillAfter(true);
		alpha.setFillBefore(true);
		alpha.setFillEnabled(true);
		balloonYellow.startAnimation(alpha);

		alpha = new AlphaAnimation(0, 0);
		alpha.setInterpolator(new LinearInterpolator());
		alpha.setDuration(0);
		alpha.setFillAfter(true);
		alpha.setFillBefore(true);
		alpha.setFillEnabled(true);
		balloonRed.startAnimation(alpha);

		alpha = new AlphaAnimation(0, 0);
		alpha.setInterpolator(new LinearInterpolator());
		alpha.setDuration(0);
		alpha.setFillAfter(true);
		alpha.setFillBefore(true);
		alpha.setFillEnabled(true);
		labelView.startAnimation(alpha);

		postAtTimeFromStart(INITIAL_FADE_IN_TIME, new CancelableRunnable() {
			@Override
			public void run() {
				AlphaAnimation alpha = new AlphaAnimation(0, 0.3f);
				alpha.setInterpolator(new AccelerateDecelerateInterpolator());
				alpha.setDuration(3000L);
				alpha.setFillAfter(true);
				alpha.setFillBefore(true);
				alpha.setFillEnabled(true);
				balloonOutline.startAnimation(alpha);

				alpha = new AlphaAnimation(0, 1);
				alpha.setInterpolator(new AccelerateDecelerateInterpolator());
				alpha.setDuration(3000L);
				alpha.setFillAfter(true);
				alpha.setFillBefore(true);
				alpha.setFillEnabled(true);
				balloonGreen.startAnimation(alpha);

				super.run();
			}
		});

		scheduleBreathAt(INITIAL_BREATH_TIME,null,null);
		scheduleBreathAt(SECOND_BREATH_TIME,null,null);
		
		float t = INITIAL_COUNTING_BREATH_TIME;
		for (int i=0;i<8;i++) {
			scheduleBreathAt(t, ""+(i+1),"Relax");
			t += BREATH_TIME+HOLD_TIME+BREATH_TIME+PAUSE_TIME;
		}
		for (int i=7;i>=0;i--) {
			scheduleBreathAt(t,""+(i+1),"Relax");
			t += BREATH_TIME+HOLD_TIME+BREATH_TIME+PAUSE_TIME;
		}

	}

	public boolean hasAudioLink() {
		return false;
	}

	@Override
	public void build() {
		AlphaAnimation alpha;
		FrameLayout.LayoutParams layout;

		balloonContainerView = new FrameLayout(getContext());
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		balloonSubcontainer = new FrameLayout(getContext());
		balloonContainerView.addView(balloonSubcontainer,layout);

		balloonOutline = new ImageView(getContext());
		balloonOutline.setImageDrawable(Util.makeDrawable(getContext(), "glass_outline.png",false));
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		balloonContainerView.addView(balloonOutline,layout);

		balloonGreen = new ImageView(getContext());
		balloonGreen.setImageDrawable(Util.makeDrawable(getContext(), "glass_green.png",false));
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		balloonSubcontainer.addView(balloonGreen,layout);
		
		balloonRed = new ImageView(getContext());
		balloonRed.setImageDrawable(Util.makeDrawable(getContext(), "glass_red.png",false));
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		balloonSubcontainer.addView(balloonRed,layout);

		balloonYellow = new ImageView(getContext());
		balloonYellow.setImageDrawable(Util.makeDrawable(getContext(), "glass_yellow.png",false));
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		balloonSubcontainer.addView(balloonYellow,layout);

		super.build();
				
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		addView(balloonContainerView,layout);

		labelView = new TextView(getContext());
		labelView.setTextColor(0xFFFFFFFF);
		labelView.setShadowLayer(2, 2, 2, 0xFF505050);
		labelView.setGravity(Gravity.CENTER);
		labelView.setTypeface(labelView.getTypeface(), Typeface.BOLD);
		labelView.setTextSize(56);
		layout = new LayoutParams(256,256);
		layout.gravity = Gravity.CENTER;
		balloonContainerView.addView(labelView,layout);

		addThumbs();
		addButton("I'm Done", ManageNavigationController.BUTTON_DONE_EXERCISE);		
	}
}
