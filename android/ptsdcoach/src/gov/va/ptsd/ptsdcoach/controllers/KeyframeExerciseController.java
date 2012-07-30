package gov.va.ptsd.ptsdcoach.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

public class KeyframeExerciseController extends BaseExerciseController {

	Handler handler;
	long animStart;
	long animStartAltTimebase;
	List<CancelableRunnable> runnableList = new ArrayList<CancelableRunnable>();

	public class CancelableRunnable implements Runnable {

		public CancelableRunnable() {
			runnableList.add(this);
		}
		
		@Override
		public void run() {
			runnableList.remove(this);
		}
	}
		
	public KeyframeExerciseController(Context ctx) {
		super(ctx);
	}
	
    public boolean postAtTime(long time, CancelableRunnable action) {
        return handler.postAtTime(action, time);
    }

    public boolean postAtTimeFromStart(double relativeTime, CancelableRunnable action) {
    	return postAtTime((long)(animStart+(relativeTime*1000)), action);
    }

    public void cancelAllRunnables() {
    	for (CancelableRunnable r : runnableList) {
    		handler.removeCallbacks(r);
    	}
    	runnableList.clear();
    }
    
    @Override
    protected void onAttachedToWindow() {
    	super.onAttachedToWindow();
        handler = getHandler();
    }
    
    @Override
    protected void onDetachedFromWindow() {
    	cancelAllRunnables();
    	handler = null;
    	super.onDetachedFromWindow();
    }
    
}
