package gov.va.ptsd.ptsdcoach;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.AppExitedEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.AppLaunchedEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.TimeElapsedBetweenSessionsEvent;
import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.TotalTimeOnAppEvent;
import com.openmhealth.ohmage.core.EventLog;

import gov.va.ptsd.ptsdcoach.activities.AssessNavigationController;
import gov.va.ptsd.ptsdcoach.activities.HomeNavigationController;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.activities.NavigationController;
import gov.va.ptsd.ptsdcoach.activities.SetupActivity;
import gov.va.ptsd.ptsdcoach.services.TtsContentProvider;

public class PTSDCoach extends TabActivity implements OnInitListener{

	ContentDBHelper db;
	public boolean fromBackground;
	private static final int TTS_CHECK_CODE = 4;
	private TextToSpeech tts;
	private final int MY_DATA_CHECK_CODE = 0;
	private final String lastActiveTab = "home";
	private long sessionStartTime;
	
	gov.va.ptsd.ptsdcoach.Util dummy = new gov.va.ptsd.ptsdcoach.Util();

	@Override
	public void onInit(int status) {		
		if (status == TextToSpeech.SUCCESS) {
			tts.speak("this is a new sentence", TextToSpeech.QUEUE_ADD, null);
			tts.speak("this is a new sentence", TextToSpeech.QUEUE_ADD, null);
		}
		else if (status == TextToSpeech.ERROR) {
		}
	}
	private boolean checkForTTS()
	{
		   Intent checkIntent = new Intent();
			checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

	    return true;
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			
				tts.speak("testing", TextToSpeech.QUEUE_ADD, null);
			} 
			else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}

	}
	
	class TabFactory implements TabHost.TabContentFactory {
		@Override
		public View createTabContent(String tag) {
			return db.getContentForName(tag).createContentView(PTSDCoach.this);
		}
	}
/*	
	private void addTab(String label, int drawableId) {
		Intent intent = new Intent(this, MockActivity.class);
		TabHost.TabSpec spec = tabHost.newTabSpec(label);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout., getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(label);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
*/	
	
	public View makeTab(String name, Drawable image) {
		LinearLayout tab = new LinearLayout(this);
		tab.setOrientation(LinearLayout.VERTICAL);
		
		ImageView iv = new ImageView(this);
		iv.setImageDrawable(image);
		tab.addView(iv);
		
		TextView tv = new TextView(this);
		tv.setText(name);
		tab.addView(tv);
		
		return tab;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fromBackground=false;
		setTheme(R.style.MinimalTheme);

		db = ContentDBHelper.instance(this);
		
		final TabHost tabs = getTabHost();
		TabFactory tabFactory = new TabFactory();
		
		tabs.addTab(tabs.newTabSpec("home").setIndicator("Home", Util.makeDrawable(this, "53-house.png")).setContent(
				new Intent(this,HomeNavigationController.class).setData(Uri.parse("content:home"))));
		tabs.addTab(tabs.newTabSpec("learn").setIndicator("Learn", Util.makeDrawable(this, "84-lightbulb.png")).setContent(
				new Intent(this,NavigationController.class).setData(Uri.parse("content:learn"))));
		tabs.addTab(tabs.newTabSpec("assess").setIndicator("Assess", Util.makeDrawable(this, "16-line-chart.png")).setContent(
				new Intent(this,AssessNavigationController.class).setData(Uri.parse("content:assess"))));
		tabs.addTab(tabs.newTabSpec("manage").setIndicator("Manage", Util.makeDrawable(this, "28-star.png")).setContent(
				new Intent(this,ManageNavigationController.class).setData(Uri.parse("content:manage"))));
		tabs.addTab(tabs.newTabSpec("support").setIndicator("Find\nSupport", Util.makeDrawable(this, "10-medical-reduced.png")).setContent(
				new Intent(this,NavigationController.class).setData(Uri.parse("content:support"))));
		
		TabWidget tabWidget = getTabWidget();
		for(int i = 0; i < tabWidget.getChildCount(); i++) {
			View parent = tabWidget.getChildAt(i);

			final int index = i;
			parent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (tabs.getCurrentTab() == index) {
						String tabId = tabs.getCurrentTabTag();
						try {
							Activity activity = getLocalActivityManager().getActivity(tabId);
							NavigationController nc = (NavigationController)activity;
							nc.popToRoot();
						} catch (ClassCastException e) {}
					} else {
						tabs.setCurrentTab(index);
					}
				}
			});
			
			View v = parent.findViewById(android.R.id.title);
			if (v instanceof TextView) {
				TextView tv = (TextView)v;
				if (i == 4) {
					tv.setSingleLine(false);
					tv.setLines(2);
					tv.setGravity(Gravity.CENTER);
					tv.setTextSize(12);
					tv.setWidth(80);
					tv.setEllipsize(null);
					tv.setHorizontalFadingEdgeEnabled(false);
					tv.setHorizontallyScrolling(false);
				} else {
					tv.setTextSize(12);
				}
			}
			
			if (i == 4) {
				v = parent.findViewById(android.R.id.icon);
				ImageView iconView = (ImageView)v;
				ViewGroup.LayoutParams params = iconView.getLayoutParams();
				if (params instanceof ViewGroup.MarginLayoutParams) {
					ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams)params;
					mparams.bottomMargin = 5;
					iconView.setLayoutParams(mparams);
				}
				iconView.setScaleType(ScaleType.CENTER_INSIDE);
			}

//			RelativeLayout tabLayout = (RelativeLayout) tabWidget.getChildAt(i);
//			tabLayout.setBackgroundDrawable(res.getDrawable(R.drawable.tab_indicator));
			
		}
/*		
		tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (!tabId.equals(lastActiveTab)) return;
				try {
					Activity activity = getLocalActivityManager().getActivity(tabId);
					NavigationController nc = (NavigationController)activity;
					nc.popToRoot();
				} catch (ClassCastException e) {}
				lastActiveTab = tabId;
			}
		});
*/		
		UserDBHelper.instance(this).setSetting("launchedOnce", "true");
		
		if ("gov.va.ptsd.ptsdcoach.ENTER_SETUP".equals(getIntent().getAction())) {
			Intent intent = new Intent(this,SetupActivity.class);
			startActivity(intent);
		} else if ("gov.va.ptsd.ptsdcoach.TAKE_ASSESSMENT".equals(getIntent().getAction())) {
			getTabHost().setCurrentTab(2);
			AssessNavigationController.takeAssessment();
		}
	}
	
	


	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if ("gov.va.ptsd.ptsdcoach.TAKE_ASSESSMENT".equals(intent.getAction())) {
			getTabHost().setCurrentTab(2);
			AssessNavigationController.takeAssessment();
		}
	}
	
	@Override
	protected void onResume() {
		sessionStartTime = System.currentTimeMillis();
		
		String lastSessionTSStr = UserDBHelper.instance(this).getSetting("lastSessionEndTime");
		if (lastSessionTSStr != null) {
			long lastSessionTS = Long.parseLong(lastSessionTSStr);
			TimeElapsedBetweenSessionsEvent e = new TimeElapsedBetweenSessionsEvent();
			e.elapsedTime = System.currentTimeMillis() - lastSessionTS;
			EventLog.log(e);
		}
		
		AppLaunchedEvent e = new AppLaunchedEvent();
		e.accessibilityFeaturesActive = TtsContentProvider.shouldSpeak(this);
		EventLog.log(e);
	
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
        String uptimeStr = UserDBHelper.instance(this).getSetting("totalUptime");
        long uptime = (uptimeStr == null) ? 0 : Long.parseLong(uptimeStr);
        uptime += System.currentTimeMillis() - sessionStartTime;
        
        {
        	TotalTimeOnAppEvent e = new TotalTimeOnAppEvent();
        	e.totalTimeOnApp = uptime;
        	EventLog.log(e);
        }
        UserDBHelper.instance(this).setSetting("totalUptime",""+uptime);

        UserDBHelper.instance(this).setSetting("lastSessionEndTime", ""+System.currentTimeMillis());

        {
        	AppExitedEvent e = new AppExitedEvent();
        	e.accessibilityFeaturesActive = TtsContentProvider.shouldSpeak(this);
        	EventLog.log(e);
        }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "15TJQ1LZBD8MNZTRNF3K");
		
//		if(tts==null)
	//		checkForTTS();

	}
	
	@Override
	protected void onStop() {
		FlurryAgent.onEndSession(this);

		super.onStop();
	}
}