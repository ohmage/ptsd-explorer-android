package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public class RelaxationController extends BaseExerciseController {

	public RelaxationController(Context ctx) {
		super(ctx);
	}

	public void nonExerciseBuild() {
		super.build();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		playAudio();
	}

	@Override
	public void build() {
		super.build();
		addThumbs();
		addButton("I'm Done", ManageNavigationController.BUTTON_DONE_EXERCISE);
	}
}
