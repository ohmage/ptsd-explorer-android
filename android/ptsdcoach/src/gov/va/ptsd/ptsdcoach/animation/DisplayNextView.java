package gov.va.ptsd.ptsdcoach.animation;

import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public final class DisplayNextView implements Animation.AnimationListener {
	FrameLayout parent;
	View image1;
	View image2;

	public DisplayNextView(FrameLayout parent, View image1, View image2) {
		this.parent = parent;
		this.image1 = image1;
		this.image2 = image2;
	}

	public void onAnimationStart(Animation animation) {
	}

	public void onAnimationEnd(Animation animation) {
		image1.post(new SwapViews(parent, image1, image2));
	}

	public void onAnimationRepeat(Animation animation) {
	}
}