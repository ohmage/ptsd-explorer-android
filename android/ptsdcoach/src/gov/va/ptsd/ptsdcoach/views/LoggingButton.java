package gov.va.ptsd.ptsdcoach.views;

import android.content.Context;
import android.widget.Button;

import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.ButtonPressedEvent;
import com.openmhealth.ohmage.core.EventLog;

public class LoggingButton extends Button {
	public LoggingButton(Context ctx) {
		super(ctx);
	}
	
	@Override
	public boolean performClick() {
		ButtonPressedEvent e = new ButtonPressedEvent();
		e.buttonPressedButtonId = ""+this.getId();
		CharSequence seq = getText();
		if (seq == null) seq = getContentDescription();
		e.buttonPressedButtonTitle = (seq==null) ? null : ""+seq;
		EventLog.log(e);
		
		return super.performClick();
	}
}
