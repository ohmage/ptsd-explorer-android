package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.views.SUDSMeter;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class SUDSController extends ContentViewController {

	SUDSMeter meter;
	
	public SUDSController(Context ctx) {
		super(ctx);
	}
	
	public void addMainTextContent(String text) {
		WebView wv = createWebView(text);
		meter = new SUDSMeter(getContext());
		meter.setId(100);
		wv.addJavascriptInterface(new JSInterface(), "ptsdcoach");

		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.leftMargin = 20;
		
		LinearLayout leftRightClientView = new LinearLayout(getContext());
		leftRightClientView.setOrientation(LinearLayout.HORIZONTAL);
		leftRightClientView.setBackgroundColor(0);
		leftRightClientView.addView(meter,layout);
		leftRightClientView.addView(wv);
		
		clientView.addView(leftRightClientView);
		wv.setNextFocusLeftId(meter.getId());
	}

	@Override
	public void buttonTapped(int id) {
		if (id == ManageNavigationController.BUTTON_OK_SUDS) {
			Integer distress = meter.getScore();
			if (distress == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Rate Your Distress");
				builder.setMessage("Please either rate your distress using the meter or tap 'Skip'.");
				builder.setPositiveButton("Ok", null);
				builder.show();
				return;
			}
		}
		super.buttonTapped(id);
	}
	
	public Integer getScore() {
		return meter.getScore();
	}
	
}
