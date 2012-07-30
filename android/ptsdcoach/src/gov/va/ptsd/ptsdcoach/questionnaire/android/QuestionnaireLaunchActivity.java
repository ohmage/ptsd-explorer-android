package gov.va.ptsd.ptsdcoach.questionnaire.android;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.questionnaire.Settings;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionnaireLaunchActivity extends QuestionnaireActivityBase {

    private static final int HELLO_ID = 1;

    public void onSurveyDone() {
		startActivity(getSurveyIntent());
		finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		if (top == null) {
			// survey triggered, but random activation said
			// no go.
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout titleGroup = new LinearLayout(this);
		titleGroup.setOrientation(LinearLayout.HORIZONTAL);
		titleGroup.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams titleGroupLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		titleGroupLayout.setMargins(10,6,10,9);
		titleGroup.setLayoutParams(titleGroupLayout);
		top.addView(titleGroup);

		ImageView logoView = new ImageView(this);
		logoView.setImageResource(R.drawable.icon);
		logoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		logoView.setPadding(0,6,10,0);
		titleGroup.addView(logoView);

        TextView titleView = new TextView(this);
        titleView.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		titleView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		titleView.setEllipsize(TextUtils.TruncateAt.END);
		titleView.setText(player.getGlobalVariable(Settings.VAR_TITLE));
		titleView.setTextSize(22);
		titleGroup.addView(titleView);

		View ruler = new View(this); 
		ruler.setBackgroundColor(0xFF888888);
		ruler.setPadding(5, 0, 5, 0);
		top.addView(ruler, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 1));

		startIntro();
    }

}
