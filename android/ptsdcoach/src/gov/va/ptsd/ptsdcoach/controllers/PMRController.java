package gov.va.ptsd.ptsdcoach.controllers;

import java.util.List;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class PMRController extends KeyframeExerciseController {

	FrameLayout body;
	ImageView mainBody;
	ImageView overlay;
	PointF lastCenter;
	float lastScale;

	public PMRController(Context ctx) {
		super(ctx);
	}
	
	public void focusBodyPart(final String part, final float at, final PointF center, final float scale) {

		postAtTimeFromStart(at, new CancelableRunnable() {
			@Override
			public void run() {
				Drawable d = Util.makeDrawable(getContext(), part+".png", false);
				overlay.setImageDrawable(d);

				AlphaAnimation alpha = new AlphaAnimation(0, 1);
				alpha.setInterpolator(new AccelerateDecelerateInterpolator());
				alpha.setDuration(8000);
				alpha.setFillAfter(true);
				alpha.setFillBefore(true);
				alpha.setFillEnabled(true);
				overlay.setVisibility(VISIBLE);
				overlay.startAnimation(alpha);
				
				super.run();
			}
		});
		
		postAtTimeFromStart(at+1.0, new CancelableRunnable() {
			@Override
			public void run() {
				AnimationSet scaleAndPan = new AnimationSet(true);
				int width = topView.getRight();
				int height = topView.getBottom();
				ScaleAnimation scaleAnim = new ScaleAnimation(1, scale, 1, scale);
				TranslateAnimation panAnim = new TranslateAnimation(0,(0.5f * width) - (center.x * width * scale),0,(0.5f * height) - (center.y * height * scale));
				scaleAndPan.addAnimation(scaleAnim);
				scaleAndPan.addAnimation(panAnim);
				scaleAndPan.setDuration(8000);
				scaleAndPan.setInterpolator(new AccelerateDecelerateInterpolator());
				scaleAndPan.setFillAfter(true);
				scaleAndPan.setFillBefore(true);
				scaleAndPan.setFillEnabled(true);
				body.startAnimation(scaleAndPan);

				super.run();
			}
		});

		lastCenter = center;
		lastScale = scale;
	}

	public void unfocusBodyPart(final float at) {

		final PointF center = lastCenter;
		final float scale = lastScale;

		postAtTimeFromStart(at, new CancelableRunnable() {
			@Override
			public void run() {
				AnimationSet scaleAndPan = new AnimationSet(true);
				ScaleAnimation scaleAnim = new ScaleAnimation(scale, 1, scale, 1);
				int width = topView.getRight();
				int height = topView.getBottom();
				TranslateAnimation panAnim = new TranslateAnimation((0.5f * width) - (center.x * width * scale),0,(0.5f * height) - (center.y * height * scale),0);
				scaleAndPan.addAnimation(scaleAnim);
				scaleAndPan.addAnimation(panAnim);
				scaleAndPan.setDuration(5000);
				scaleAndPan.setInterpolator(new AccelerateDecelerateInterpolator());
				scaleAndPan.setFillAfter(true);
				scaleAndPan.setFillBefore(true);
				scaleAndPan.setFillEnabled(true);
				body.startAnimation(scaleAndPan);

				super.run();
			}
		});
				
		postAtTimeFromStart(at+5.0, new CancelableRunnable() {
			@Override
			public void run() {
				AlphaAnimation alpha = new AlphaAnimation(1, 0);
				alpha.setInterpolator(new AccelerateDecelerateInterpolator());
				alpha.setDuration(2000);
				alpha.setFillAfter(true);
				alpha.setFillBefore(true);
				alpha.setFillEnabled(true);
				overlay.setAnimation(alpha);

				super.run();
			}
		});
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		playAudio();
		animStart = SystemClock.uptimeMillis();
		animStartAltTimebase = AnimationUtils.currentAnimationTimeMillis();
		
		PointF armCenter = new PointF(0.7f, 0.45f);
		PointF headCenter = new PointF(0.5f, 0.15f);
		PointF shouldersCenter = new PointF(0.5f,0.275f);
		PointF stomachCenter = new PointF(0.5f,0.425f);
		PointF buttCenter = new PointF(0.5f,0.55f);
		PointF feetCenter = new PointF(0.5f,0.85f);

		focusBodyPart("body_arms", 62, armCenter, 2.8f);
		unfocusBodyPart(102);

		focusBodyPart("body_head", 115, headCenter, 5);
		unfocusBodyPart(175);

		focusBodyPart("body_shoulders", 184, shouldersCenter, 2.8f);
		unfocusBodyPart(236);

		focusBodyPart("body_stomach", 245, stomachCenter, 5);
		unfocusBodyPart(282);

		focusBodyPart("body_butt", 297, buttCenter, 3);
		unfocusBodyPart(337);

		focusBodyPart("body_feet", 357, feetCenter, 3);
		unfocusBodyPart(411);
		
		postAtTimeFromStart(458, new CancelableRunnable() {
			@Override
			public void run() {
				Drawable d = Util.makeDrawable(getContext(), "body_all.png", false);
				overlay.setImageDrawable(d);

				AlphaAnimation alpha = new AlphaAnimation(0, 1);
				alpha.setInterpolator(new AccelerateDecelerateInterpolator());
				alpha.setDuration(20000);
				alpha.setFillAfter(true);
				alpha.setFillBefore(true);
				alpha.setFillEnabled(true);
				overlay.setVisibility(VISIBLE);
				overlay.startAnimation(alpha);

				super.run();
			}
		});
	}
	
	@Override
	public void build() {
		body = new FrameLayout(getContext());
		
		mainBody = new ImageView(getContext());
		mainBody.setImageDrawable(Util.makeDrawable(getContext(), "body.png",false));
		body.addView(mainBody);

		overlay = new ImageView(getContext());
		overlay.setImageDrawable(Util.makeDrawable(getContext(), "body_arms.png",false));
		overlay.setVisibility(INVISIBLE);
		body.addView(overlay);
		
		addView(body);
		
		super.build();
		setBackgroundDrawable(null);

		addThumbs();		
		addButton("I'm Done", ManageNavigationController.BUTTON_DONE_EXERCISE);		
	}
}
