package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.activities.EULA;
import gov.va.ptsd.ptsdcoach.activities.FirstLaunch;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;

public class EULAController extends ContentViewController {

	static final int ACCEPT_EULA = 1001;
	static final int REJECT_EULA = 1002;
	
	public EULAController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		super.build();
		
		LinearLayout.LayoutParams params;

		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 5, 10);
		
		addButton("Accept", ACCEPT_EULA).setLayoutParams(params);

		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 10, 10, 10);
		addButton("Reject", REJECT_EULA).setLayoutParams(params);
	}

	public boolean shouldAddButtonsInScroller() {
		return false;
	}
	
	public void buttonTapped(int id) {
		EULA activity = (EULA)getContext();
		if (id == ACCEPT_EULA) {
			activity.startActivityForResult(new Intent(getContext().getApplicationContext(), FirstLaunch.class), Activity.RESULT_FIRST_USER);
		} else {
			activity.finish();
		}
	}

}
