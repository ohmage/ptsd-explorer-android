/*
 * Copyright (C) 2010 The IDEAL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.va.ptsd.ptsdcoach.services;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

/**
 * Content Provider for wrapping the TextToSpeech library.
 * 
 * Usage:
 * content://com.ideal.webaccess.tts/MODE/RANDOMNUMBER/STRING_TO_BE_SPOKEN 
 * where
 * MODE is 0 for flush, 1 for queue, 
 * RANDOMNUMBER is a random number that is used to force the content provider
 * to speak the message (even if it was previously spoken because it will 
 * have a different URI),
 * and STRING_TO_BE_SPOKEN is the actual string that should be spoken to 
 * the user.
 * 
 * What is served back is currently not used, but it should be possible to
 * use that to communicate back whether or not the TTS is currently speaking.
 */
public class TtsContentProvider extends ContentProvider {
    private static final String URI_PREFIX = "content://gov.va.ptsd.ptsdcoach.services.tts";

    static private TextToSpeech mTts;
    static private AccessibilityManager mAccessibilityMgr = null;

    public static String constructUri(String url) {
        Uri uri = Uri.parse(url);
        return uri.isAbsolute() ? url : URI_PREFIX + url;
    }

    static class TaggedString {
    	public Object tag;
    	public String text;
    	
    	public TaggedString(Object tag, String text) {
    		this.tag = tag;
    		this.text = text;
		}
    }
    
    static TaggedString thisOne = null;
    static private ArrayList<TaggedString> startupMessage = new ArrayList<TaggedString>();
    static private int startupQueueMode = 0;
    static boolean started = false;

    static HashMap<String, String> speakKeys = new HashMap<String, String>();
    static {
    	speakKeys.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"dummy");
    }

    public static boolean shouldSpeak(Context ctx) {
    	if (mAccessibilityMgr == null) {
    		mAccessibilityMgr = (AccessibilityManager)ctx.getSystemService(Context.ACCESSIBILITY_SERVICE);
    	}
    	
    	return mAccessibilityMgr.isEnabled();
    }
    
    public static void stopSpeech(Object tag) {
		synchronized (startupMessage) {
			Log.d("tts","stopping");
			Iterator<TaggedString> iter = startupMessage.iterator();
			while (iter.hasNext()) {
				TaggedString o = iter.next();
				if ((tag == null) || (o.tag == tag)) {
					iter.remove();
				}
			}
			
			if ((thisOne != null) && ((tag == null) && (thisOne.tag == tag))) {
				started = false;
				thisOne = null;
				mTts.stop();
			}

		}
    }

    public static void speak(Object tag, String text, int queueMode) {
    	try {
			synchronized (startupMessage) {
				Log.d("tts","adding to queue: "+text);
				if (queueMode == TextToSpeech.QUEUE_FLUSH) {
					startupMessage.clear();
				}
				
				startupMessage.add(new TaggedString(tag, text));

				if (!started) {
					playNext();
				}
			}
    	} catch (Exception e) {
			Log.d("tts","ERROR!");
    		e.printStackTrace();
    	}
    }

    static private void playNext() {
		if (mTts != null) {
			mTts.stop();
			if (startupMessage.size() > 0) {
				TaggedString speech = startupMessage.remove(0);
				Log.d("tts", speech.text);
				started = true;
				thisOne = speech;
				mTts.speak(speech.text, TextToSpeech.QUEUE_FLUSH, speakKeys);
			}
		}
    }
    
    static private OnInitListener mTtsInitListener = new OnInitListener() {
        @Override
        public void onInit(int status) {
			synchronized (startupMessage) {
				mTts.setOnUtteranceCompletedListener(mTtsUtteranceCompletedListener);
				playNext();
			}
        }
    };

    static private OnUtteranceCompletedListener mTtsUtteranceCompletedListener = new OnUtteranceCompletedListener() {
    	@Override
    	public void onUtteranceCompleted(String utteranceId) {
			synchronized (startupMessage) {
				started = false;
				thisOne = null;
				playNext();
			}
    	}
    };

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        String text = "";
        int queueMode = 0;
        try {
            text = uri.toString();
            if (text.length() > URI_PREFIX.length()) {
                text = text.substring(URI_PREFIX.length() + 1);
                if (text.startsWith("1")) {
                    queueMode = 1;
                }
                // Throwaway the random number generated by the JS.
                // We are using the random number to force a content refresh.
                int stringStart = text.indexOf("/", 2) + 1;
                text = URLDecoder.decode(text.substring(stringStart), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        speak(this,text,queueMode);
        return null;
    }

    @Override
    public boolean onCreate() {
		if (mTts == null) {
			mTts = new TextToSpeech(getContext(), mTtsInitListener);
			Log.d("tts", "starting up");
		}

        return true;
    }

    @Override
    public int delete(Uri uri, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

}
