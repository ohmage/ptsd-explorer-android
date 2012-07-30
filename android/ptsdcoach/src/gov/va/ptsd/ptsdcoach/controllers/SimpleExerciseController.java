package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public class SimpleExerciseController extends BaseExerciseController {

	public SimpleExerciseController(Context ctx) {
		super(ctx);
	}

	public void nonExerciseBuild() {
		super.build();
	}
	
	@Override
	public void build() {
		super.build();
		addThumbs();
		addButton("New Tool", ManageNavigationController.BUTTON_NEW_TOOL);
		addButton("I'm Done", ManageNavigationController.BUTTON_DONE_EXERCISE);
	}
}
