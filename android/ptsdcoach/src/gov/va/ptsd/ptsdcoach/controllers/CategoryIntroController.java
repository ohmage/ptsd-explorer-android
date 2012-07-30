package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.content.Content;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class CategoryIntroController extends BaseExerciseController {

	
	public CategoryIntroController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		super.build();
		
		Button beginExerciseButton = new Button(getContext());
		beginExerciseButton.setText("Take a Timeout");
		beginExerciseButton.setTextSize(17);
		beginExerciseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getNavigator().pushViewForContent(selectedContent);
			}
		});
		beginExerciseButton.setId(ManageNavigationController.BUTTON_NEXT);
		clientView.addView(beginExerciseButton);
	
		addThumbs();
		addButton("New Tool", ManageNavigationController.BUTTON_NEW_TOOL);
	}
	
}
