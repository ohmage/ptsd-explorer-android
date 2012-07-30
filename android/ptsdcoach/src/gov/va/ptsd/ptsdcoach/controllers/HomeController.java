package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.PTSDCoach;
import android.app.Activity;
import android.content.Context;

public class HomeController extends ButtonGridController {

	public HomeController(Context ctx) {
		super(ctx);
	}
	
	public void buttonTapped(int id) {
		PTSDCoach a = (PTSDCoach)(getNavigator().getParent());
		a.getTabHost().setCurrentTab(1+id);
	}

}
