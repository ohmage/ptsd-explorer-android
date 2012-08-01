package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.activities.EULA;
import gov.va.ptsd.ptsdcoach.activities.FirstLaunch;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.views.LoggingButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class CrisisIntroController extends ContentViewController {

	static final int ACCEPT_EULA = 1001;
	static final int REJECT_EULA = 1002;
	
	public CrisisIntroController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		super.build();
		
		LinearLayout.LayoutParams params;

		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 20, 20, 20);
		LoggingButton gimmeTool = new LoggingButton(getContext());
		gimmeTool.setText("No, give me a tool");
		gimmeTool.setLayoutParams(params);
		gimmeTool.setTextSize(18);
		gimmeTool.setPadding(20, 30, 20, 30);
		gimmeTool.setId(ManageNavigationController.BUTTON_NEW_TOOL);
		gimmeTool.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonTapped(v.getId());
			}
		});
		clientView.addView(gimmeTool);

		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 20, 20, 20);
		LoggingButton yesTalk = new LoggingButton(getContext());
		yesTalk.setText("Yes, talk to someone now");
		yesTalk.setLayoutParams(params);
		yesTalk.setTextSize(18);
		yesTalk.setPadding(20, 30, 20, 30);
		yesTalk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getNavigator().pushView(getContent().getNext().createContentView(getContext()),1);
			}
		});

		clientView.addView(yesTalk);
	}
	
}
