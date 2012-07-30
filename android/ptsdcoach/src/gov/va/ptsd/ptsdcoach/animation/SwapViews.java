package gov.va.ptsd.ptsdcoach.animation;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public final class SwapViews implements Runnable {
	FrameLayout parent;
	View image1;
	View image2;

	public SwapViews(FrameLayout parent, View image1, View image2) {
		this.parent = parent;
		this.image1 = image1;
		this.image2 = image2;
	}

	public void run() {
		final float centerX = image1.getWidth() / 2.0f;
		final float centerY = image1.getHeight() / 2.0f;
		Flip3DAnimation rotation;

		parent.removeView(image1);
		image2.setVisibility(View.VISIBLE);
		image2.requestFocus();

		rotation = new Flip3DAnimation(-90, 0, centerX, centerY);

		rotation.setDuration(250);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new DecelerateInterpolator());

		image2.startAnimation(rotation);
	}
}