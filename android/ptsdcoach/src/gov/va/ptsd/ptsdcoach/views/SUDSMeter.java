package gov.va.ptsd.ptsdcoach.views;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.services.TtsContentProvider;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.Path.FillType;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SUDSMeter extends RelativeLayout implements AccessibilityEventSource {

	Drawable bgd;
	Drawable marker;
	Drawable mercury;
	Integer reading = null;
	boolean selected = false;
	
	int thermW, thermH;
	int markerW, markerH;
	int mercuryW, mercuryH;
	
	final static private float SCALE = 0.75f;
	
	public SUDSMeter(Context ctx) {
		super(ctx);
		bgd = ctx.getApplicationContext().getResources().getDrawable(R.drawable.thermometer);
		marker = ctx.getApplicationContext().getResources().getDrawable(R.drawable.therm_marker);
		mercury = ctx.getApplicationContext().getResources().getDrawable(R.drawable.mercury);
		
		thermW = (int)(bgd.getIntrinsicWidth()*SCALE);
		thermH = (int)(bgd.getIntrinsicHeight()*SCALE);
		markerW = (int)(marker.getIntrinsicWidth()*SCALE);
		markerH = (int)(marker.getIntrinsicHeight()*SCALE);
		mercuryW = (int)(mercury.getIntrinsicWidth()*SCALE);
		mercuryH = (int)(mercury.getIntrinsicHeight()*SCALE);
		
		ImageView bg = new ImageView(ctx);
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(bgd.getIntrinsicWidth(),bgd.getIntrinsicHeight());
		bg.setImageDrawable(bgd);
		addView(bg,layout);
		
		setWillNotDraw(false);
		setFocusableInTouchMode(false);
		setFocusable(true);
		
		setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			selected = !selected;
			invalidate();
			sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		} else if (selected) {
			if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) || 
				(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT)) {
				if (reading == null) updateReading(5);
				else updateReading(reading - 1);
			} else if (	(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) || 
						(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT)) {
				if (reading == null) updateReading(5);
				else updateReading(reading + 1);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		TtsContentProvider.stopSpeech(null);
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
	
	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		if (reading == null) {
			event.setContentDescription("Distress meter, unset");
		} else {
			event.setContentDescription("Distress meter at level "+reading);
		}
		if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SELECTED) {
			TtsContentProvider.stopSpeech(null);
			if (selected) {
				event.setContentDescription("Distress meter selected");
			} else {
				event.setContentDescription("Distress meter unselected");
			}
		} else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
			event.setContentDescription("Distress level "+reading);
		}
		return true;
	}
	
	public Integer getScore() {
		return reading;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = thermW+40;
		int h = thermH;
		setMeasuredDimension(w, h);
	}
	
	public void updateReading(Integer newReading) {
		Integer oldReading = reading;
		reading = newReading;
		if (reading.intValue() > 10) reading = 10;
		if (reading.intValue() < 1) reading = 1;
		if (((oldReading == null) || (oldReading.intValue() != reading.intValue()))) {
			invalidate();
			if (TtsContentProvider.shouldSpeak(getContext())) {
				TtsContentProvider.stopSpeech(null);
				TtsContentProvider.speak(this, "distress level "+reading, TextToSpeech.QUEUE_FLUSH);
//				sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
			}
		}

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		float height = 245-y;
		int dibit = (int)((height+(pixPerMarker*0.5)) / pixPerMarker);
		if (dibit < 0) dibit = 0;
		if (dibit > 10) dibit = 10;
		updateReading(dibit);
		return true;
	}
	
	private static final int pixPerMarker = 21;
	
	@Override
	protected void dispatchDraw(Canvas canvas) {		
		if (isFocused() || selected) {
			int width = getWidth();
			int height = getHeight();
			RectF bounds = new RectF(0,0,width,height);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(0x8000FF00);
			paint.setStrokeWidth(2);
			if (selected) {
				paint.setStyle(Style.FILL_AND_STROKE);
			} else {
				paint.setStyle(Style.STROKE);
			}
			canvas.drawRoundRect(bounds, 10, 10, paint);
		}
		
		super.dispatchDraw(canvas);
		
		Rect r = new Rect(0,0,thermW,thermH);
		bgd.setBounds(r);
		bgd.draw(canvas);
		
		int level = (reading == null) ? 5 : reading.intValue();
		
		int left = 18;
		int top = 239-(level*pixPerMarker);
		int bottom = 245;
		r = new Rect(left,top,left+mercuryW,bottom);
		mercury.setBounds(r);
		mercury.draw(canvas);

		r = new Rect(0,0,markerW,markerH);
		for (int i=0;i<11;i++) {
			int y = 239 - (i * pixPerMarker);
			r.offsetTo(29, y);
			marker.setBounds(r);
			marker.draw(canvas);
		}
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Path outline = new Path();
		left = 48;
		RectF rf = new RectF(left+0.5f,top-10+0.5f,left+30+0.5f,top+10+0.5f);
		outline.addRect(rf, Direction.CW);
		paint.setColor(0xFFFFFF00);
		paint.setStrokeWidth(0);
		paint.setStyle(Style.FILL);
		canvas.drawPath(outline, paint);
		paint.setColor(0xFF000000);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(outline, paint);
		
		int red = 255 * level / 10;
		int green = (200 - (200 * level / 10));
		paint.setARGB(255, red, green, 0);
		paint.setStyle(Style.FILL);
		paint.setTextSize(10+level);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextAlign(Align.CENTER);
		String str = (reading == null) ? "?" : reading.toString();
		Rect textBounds = new Rect();
		paint.getTextBounds(str, 0, str.length(), textBounds);
		canvas.drawText(str, left+15,top + (-textBounds.top / 2),paint);
	}
}
