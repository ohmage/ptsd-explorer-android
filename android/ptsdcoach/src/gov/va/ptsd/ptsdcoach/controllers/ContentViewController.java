package gov.va.ptsd.ptsdcoach.controllers;

import java.util.ArrayList;
import java.util.Map;

import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.EULA;
import gov.va.ptsd.ptsdcoach.activities.FirstLaunch;
import gov.va.ptsd.ptsdcoach.services.TtsContentProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.util.Log;

public class ContentViewController extends ContentViewControllerBase {

	static final int TAP_TO_LISTEN = 1100;

	LinearLayout topView;
	ScrollView scroller;
	LinearLayout clientView;

	LinearLayout buttonBar;
	LinearLayout rightButtons;
	LinearLayout leftButtons;
	
	public ContentViewController(Context ctx) {
		super(ctx);
	}
	
	public void addMainTextContent(String text) {
		WebView wv = createWebView(text);
		clientView.addView(wv);
	}

	public String getContentMainText() {
		return content.getMainText();
	}
	
	public void buildClientViewFromContent() {
		if (content != null) {
			for (Map.Entry<String,Object> entry : content.getAttributes().entrySet()) {
				if (entry.getKey().startsWith("variable_")) {
					String key = entry.getKey().substring("variable_".length());
					String value = entry.getValue().toString();
					getNavigator().setVariable(key,value);
				}
			}
			
			String title = content.getTitle();
			if (title != null) {
				clientView.addView(makeTitleView(title));
			}

			Drawable image = content.getImage();
			if (image != null) {
				int height = image.getIntrinsicHeight();
				int width = image.getIntrinsicWidth();
				float scaledHeight = height;
				float scaledWidth = width;
				if (scaledHeight > 150) {
					scaledHeight = 150;
					scaledWidth = (scaledHeight / height) * width;
				}
				image.setBounds(0, 0, (int)scaledWidth, (int)scaledHeight);

				ImageView imageView = new ImageView(getContext());
				imageView.setImageDrawable(image);

				LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams((int)scaledWidth,(int)scaledHeight);
				layout.setMargins(10, 10, 10, 10);
				layout.gravity = Gravity.CENTER;
				imageView.setLayoutParams(layout);
				clientView.addView(imageView);
			}

			String text = getContentMainText();
			if (text != null) {

				if(hasAudioLink() && shouldAddListenButton())
				{
					LinearLayout.LayoutParams params;
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 10, 5, 10);
							
					addButton("Tap To Listen", TAP_TO_LISTEN).setLayoutParams(params);

				}
					
				addMainTextContent(text);
				Log.v("PTSD","text content=" + text);
			}
		}
	}

	public boolean shouldAddListenButton() {
		return true;
	}
	
	public void buttonTapped(int id) {
		if (id == TAP_TO_LISTEN) {
			playAudio();
		}
		else
		{
			super.buttonTapped(id);
		}
	}
	
	public void build() {
		topView = new LinearLayout(getContext());
		topView.setOrientation(LinearLayout.VERTICAL);
		topView.setBackgroundColor(0);
		FrameLayout.LayoutParams topLayout = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		topView.setLayoutParams(topLayout);

		clientView = new LinearLayout(getContext());
		clientView.setOrientation(LinearLayout.VERTICAL);
		clientView.setBackgroundColor(0);

		scroller = new ScrollView(getContext());
		scroller.setFillViewport(true);
		scroller.setBackgroundColor(0);
		scroller.setHorizontalScrollBarEnabled(false);
		scroller.setVerticalScrollBarEnabled(true);
		scroller.addView(clientView);
		
		LinearLayout.LayoutParams scrollerLayout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		scrollerLayout.weight = 1;
		scroller.setLayoutParams(scrollerLayout);
		topView.addView(scroller);

		setBackgroundColor(0);
		setBackgroundDrawable(getBackground());
		addView(topView);

		
		buildClientViewFromContent();
	}

	public boolean hasAudioLink() {
		return content.hasAudio();
	}
	
	public void viewLoaded() {
		if (scroller != null) {
			scroller.fullScroll(ScrollView.FOCUS_UP);
		}

		View v1=null,v2=null,v3=null;
		ArrayList<View> list = new ArrayList<View>();
		if (leftButtons != null) {
			for (int i=0;i<leftButtons.getChildCount();i++) {
				list.add(leftButtons.getChildAt(i));
			}
		}
		if (rightButtons != null) {
			for (int i=0;i<rightButtons.getChildCount();i++) {
				list.add(rightButtons.getChildAt(i));
			}
		}
		
		for (View v : list) {
			v1 = v2;
			v2 = v3;
			v3 = v;
			if (v2 != null) {
				if (v1 != null) v2.setNextFocusUpId(v1.getId());
				if (v3 != null) v2.setNextFocusDownId(v3.getId());
			}
		}
		
		while ((v1 != null) || (v2 != null) || (v3 != null)) {
			v1 = v2;
			v2 = v3;
			v3 = null;
			if (v2 != null) {
				if (v1 != null) v2.setNextFocusUpId(v1.getId());
			}
		}

		super.viewLoaded();

	}

	public boolean shouldAddButtonsInScroller() {
		return false;
	}
	
	public ViewGroup getButtonBar() {
		if (buttonBar == null) {
			leftButtons = new LinearLayout(getContext());
			leftButtons.setOrientation(LinearLayout.HORIZONTAL);
			leftButtons.setGravity(Gravity.LEFT);
			LinearLayout.LayoutParams leftLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
			leftLayout.weight = 1;
			leftButtons.setLayoutParams(leftLayout);

			rightButtons = new LinearLayout(getContext());
			rightButtons.setOrientation(LinearLayout.HORIZONTAL);
			rightButtons.setGravity(Gravity.RIGHT);
			LinearLayout.LayoutParams rightLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
			rightLayout.weight = 1;
			rightButtons.setLayoutParams(rightLayout);
			
			buttonBar = new LinearLayout(getContext());
			buttonBar.setBackgroundColor(0);
			buttonBar.setGravity(Gravity.FILL_HORIZONTAL);
			
			buttonBar.addView(leftButtons);
			buttonBar.addView(rightButtons);

			LinearLayout.LayoutParams buttonBarLayout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			buttonBarLayout.weight = 0;
			buttonBar.setLayoutParams(buttonBarLayout);
			
			if (shouldAddButtonsInScroller()) {
				clientView.addView(buttonBar);
			} else {
				topView.addView(buttonBar);
			}
		}
		return buttonBar;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//		if (buttonBar != null) clientView.setMinimumHeight(scroller.getHeight());
		super.onLayout(changed, left, top, right, bottom);
	}
	
	public LinearLayout getLeftButtons() {
		if (leftButtons == null) {
			getButtonBar();
		}

		return leftButtons;
	}

	public LinearLayout getRightButtons() {
		if (rightButtons == null) {
			getButtonBar();
		}
		
		return rightButtons;
	}

	public Button addButton(String text) {
		return addButton(text,-1);
	}

	public Button addButton(String text, int id) {
		Button b = new Button(getContext());
//		b.setBackgroundResource(R.drawable.button_bg);
		b.setMinWidth(80);
		b.setText(text);
		b.setId(id);
		if (id != -1) {
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TtsContentProvider.stopSpeech(ContentViewController.this);
					buttonTapped(v.getId());
				}
			});
			
			b.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					TtsContentProvider.stopSpeech(ContentViewController.this);
				}
			});
		}
		getRightButtons().addView(b);
		return b;
	}

}
