package gov.va.ptsd.ptsdcoach.questionnaire.android;

import gov.va.ptsd.ptsdcoach.questionnaire.Questionnaire;
import gov.va.ptsd.ptsdcoach.questionnaire.QuestionnaireHandler;
import gov.va.ptsd.ptsdcoach.questionnaire.SurveyUtil;
import gov.va.ptsd.ptsdcoach.questionnaire.UrlScanningHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class QuestionnaireManager {

	private static QuestionnaireManager instance;

	private static final String QUESTIONNAIRE_CACHER_INTENT_ACTION = ".questionnaire.RUN_CACHER";
	private static final long QUESTIONNAIRE_CACHER_INITIAL_DELAY = 3000;//5*60000;
	private static final long QUESTIONNAIRE_CACHER_RETRY_DELAY = 10000;//5*60000;

	static public final int RESOURCE_FLAG_QUESTIONNAIRE = 1;
	static public final int RESOURCE_FLAG_MANUALLY_TRIGGERED = 2;

	private QuestionnaireDatabase dbHelper;
	private Context context;
	private Intent runCacherIntent;
	private PendingIntent runCacherPendingIntent;
	private BroadcastReceiver runCacherBroadcastReceiver;

	class CacherRunnable implements Runnable {
		@Override
		public void run() {
    		Log.d("PCL", "CacherRunnable");
			AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

			List<AndroidResource> resourceList = getDB().getNullResources();
			if ((resourceList == null) || (resourceList.size() == 0)) {
				alarmMgr.cancel(runCacherPendingIntent);
				return;
			}

			int countFetched = 0;
			for (AndroidResource r : resourceList) {
				try {
					byte[] b = fetchURL(r.getUrl());
					if (b != null) {
						r.setContent(b);
						if ((r.getFlags() & RESOURCE_FLAG_QUESTIONNAIRE) != 0) {
							parseQuestionaire(new ByteArrayInputStream(b), new UrlScanningHandler() {
								@Override
								public void registerResource(String url) {
									QuestionnaireManager.this.registerResource(url, 0);
								}
							});
						}
						countFetched++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			List<AndroidResource> after = getDB().getNullResources();
			if ((after == null) || (after.size() == 0)) {
	    		Log.d("PCL", "CacherRunnable cancelling");
				alarmMgr.cancel(runCacherPendingIntent);
				return;
			}
/*			
			if (after.size() != resourceList.size() - countFetched) {
				// Stop cycling if it looks like something is wrong and we aren't making progress
				// ... to avoid cycling endlessly and draining battery
				alarmMgr.cancel(runCacherPendingIntent);
				return;
			}
*/			
    		Log.d("PCL", "CacherRunnable rescheduling");
			alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+QUESTIONNAIRE_CACHER_RETRY_DELAY, runCacherPendingIntent);
		}
	}
	CacherRunnable cacher = new CacherRunnable();

    public class RunCacherBroadcastReceiver extends BroadcastReceiver {
    	public RunCacherBroadcastReceiver() {
    	}

    	public void onReceive(Context context, Intent intent) {
    		cacher.run();
    	}
    }

	private QuestionnaireManager(Context ctx) {
		Log.d("PCL", "QuestionnaireManager created");

		context = ctx;
		dbHelper = new QuestionnaireDatabase(ctx);

		runCacherIntent = new Intent(ctx.getPackageName()+QUESTIONNAIRE_CACHER_INTENT_ACTION);
		runCacherPendingIntent = PendingIntent.getBroadcast(context, 0, runCacherIntent, 0);

		runCacherBroadcastReceiver = new RunCacherBroadcastReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(ctx.getPackageName()+QUESTIONNAIRE_CACHER_INTENT_ACTION);
    	context.registerReceiver(runCacherBroadcastReceiver, filter);
	}

	public void unRegister() {
	}

	@Override
	protected void finalize() throws Throwable {
		// not an ideal place to unregisterReceiver
		// because we are not promised to be finalized
		// before whoever created us is destroyed and
		// they will generate a big "you leaked a
		// receiver" message.
		context.unregisterReceiver(runCacherBroadcastReceiver);
		// unRegister();
	}
	
	public static QuestionnaireManager getInstance(Context ctx) {
		if (instance == null) instance = new QuestionnaireManager(ctx);
		return instance;
	}
	
	public QuestionnaireDatabase getDB() {
		return dbHelper;
	}
	
	/* Resource management */
	

	public Questionnaire getQuestionaire(String url) {
		Log.d("PCL", "getQuestionaire('"+url+"')");
		AndroidResource r = getDB().getResource(url);
		if (r == null) {
			r = registerResource(url, RESOURCE_FLAG_QUESTIONNAIRE);
		}
		if (r.getContent() == null) {
			Log.d("PCL", "Fetching content, not in cache");
			byte[] content = fetchURL(url);
			if (content != null) {
				r.setContent(content);
			}
		}
		if (r.getContent() == null) return null;

		QuestionnaireHandler handler = new QuestionnaireHandler();
		parseQuestionaire(new ByteArrayInputStream(r.getContent()), handler);
		return handler.getQuestionaire();
	}

	public byte[] getResource(String url) {
		Log.d("PCL", "getResource('"+url+"')");
		AndroidResource r = getDB().getResource(url);
		if (r == null) {
			r = registerResource(url, 0);
		}
		
		return r.getContent();
	}

	public AndroidResource registerResource(String url, int flags) {
		Log.d("PCL", "registerResource('"+url+"',"+flags+")");
		AndroidResource r = getDB().getResource(url);
		if (r != null) return r;
		r = new AndroidResource(getDB(), url, flags);
		getDB().addResource(r);
		
		AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+QUESTIONNAIRE_CACHER_INITIAL_DELAY, runCacherPendingIntent);
		return r;
	}

	public void unregisterResources() {
		getDB().removeAllResources();
	}

	public byte[] fetchURL(String urlStr) {
		Log.d("PCL", "fetchURL('"+urlStr+"')");
		Object o = null;
		try {
			try {
				URL url = new URL(urlStr);
				o = url.getContent();
			// probably also need a "can't find the surver" exception
			// as well.  Maybe requeue the request to cache.
			// for now, just fix the no asset issue
			} catch (MalformedURLException e) {
				try {
					o = context.getApplicationContext().getAssets().open(urlStr);
				} catch (java.io.FileNotFoundException noFile) {
					return null;
				}
			}

			if (o instanceof InputStream) {
				return SurveyUtil.readAll((InputStream)o);
			} else if (o instanceof byte[]) {
				return (byte[])o;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void parseQuestionaire(InputStream in, ContentHandler handler) {
		try {
			Log.d("PCL", "parseQuestionaire");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader reader = sp.getXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(in));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
