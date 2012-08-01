package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.views.LoggingButton;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class SimpleExerciseIntroController extends BaseExerciseController {

	public SimpleExerciseIntroController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		super.build();
		
		LoggingButton beginExerciseButton = new LoggingButton(getContext());
		beginExerciseButton.setText("Begin Exercise");
		beginExerciseButton.setTextSize(17);
		beginExerciseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Content next = getContent().getNext();
				getNavigator().pushViewForContent(next);
			}
		});
		beginExerciseButton.setId(ManageNavigationController.BUTTON_NEXT);
		clientView.addView(beginExerciseButton);
	
		addThumbs();
		addButton("New Tool", ManageNavigationController.BUTTON_NEW_TOOL);
	}
	
}
