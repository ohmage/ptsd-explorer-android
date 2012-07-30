package gov.va.ptsd.ptsdcoach.activities;

import java.util.ArrayList;
import java.util.TreeMap;

import gov.va.ptsd.ptsdcoach.ContentDBHelper;
import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.animation.DisplayNextView;
import gov.va.ptsd.ptsdcoach.animation.Flip3DAnimation;
import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;
import android.app.Activity;
import android.app.ActivityGroup;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import android.widget.ViewAnimator;
import android.util.Log;
import gov.va.ptsd.ptsdcoach.controllers.ButtonGridController;

import java.util.Locale;
import java.util.Random;
import android.content.Intent;

public class NavigationController extends Activity {


	ContentDBHelper db;
	UserDBHelper userDb;
	FrameLayout topView;
	View currentView;
	View oldView;
	MenuItem helpItem;
	MenuItem setupItem;
	
	TreeMap<String,String> variables = new TreeMap<String, String>();
	ArrayList<View> stack = new ArrayList<View>();

	class StackFrame {
		Content content;
		ContentViewControllerBase view;
	}


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = ContentDBHelper.instance(this);
		userDb = UserDBHelper.instance(this);
		topView = new ViewAnimator(this);
		setTheme(R.style.MinimalTheme);
		setContentView(topView);
		
		Uri uri = getIntent().getData();
		String name = uri.getSchemeSpecificPart();
		Content content = db.getContentForName(name);
		ContentViewControllerBase cv = content.createContentView(this);

		stack.add(cv);
		topView.addView(cv);
		
	}


	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		inFromRight.setDuration(300);
		inFromRight.setInterpolator(new AccelerateDecelerateInterpolator());
		return inFromRight;
	}
	
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		outtoLeft.setDuration(300);
		outtoLeft.setInterpolator(new AccelerateDecelerateInterpolator());
		return outtoLeft;
	}
	
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		inFromLeft.setDuration(300);
		inFromLeft.setInterpolator(new AccelerateDecelerateInterpolator());
		return inFromLeft;
	}

	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		outtoRight.setDuration(300);
		outtoRight.setInterpolator(new AccelerateDecelerateInterpolator());
		return outtoRight;
	}

	public void flipReplaceViewForContent(Content c) {
		flipReplaceViewForContent(c,stack.size()-1);
	}

	public void flipReplaceViewForContent(Content c, int toKeep) {
		ContentViewControllerBase cv = c.createContentView(this);
		flipReplaceView(cv,toKeep);
	}

	public void flipReplaceView(View cv) {
		flipReplaceView(cv,stack.size()-1);
	}

	public void flipReplaceView(View cv, int toKeep) {
		oldView = stack.remove(stack.size()-1);
		if (oldView instanceof ContentViewControllerBase) {
			ContentViewControllerBase cvcb = (ContentViewControllerBase)oldView;
			cvcb.blockInput();
		}
		while (stack.size() > toKeep) {
			stack.remove(stack.size()-1);
		}
		stack.add(cv);
		topView.addView(cv);
		currentView = cv;
		currentView.setVisibility(View.INVISIBLE);

		ContentViewControllerBase.OnViewReadyListener listener = new ContentViewControllerBase.OnViewReadyListener() {
			@Override
			public void onViewReady() {
				final float centerX = oldView.getWidth() / 2.0f;
				final float centerY = oldView.getHeight() / 2.0f;

				Flip3DAnimation rotation = new Flip3DAnimation(0, 90, centerX, centerY);
				rotation.setDuration(250);
				rotation.setFillAfter(true);
				rotation.setInterpolator(new AccelerateInterpolator());
				rotation.setAnimationListener(new DisplayNextView(topView, oldView, currentView));

				oldView.startAnimation(rotation);
			}
		};
		
		if (currentView instanceof ContentViewControllerBase) {
			ContentViewControllerBase cvc = (ContentViewControllerBase)currentView;
			cvc.setOnViewReadyListener(listener);
		} else {
			listener.onViewReady();
		}
	}

	public void pushReplaceViewForContent(Content c) {
		ContentViewControllerBase cv = c.createContentView(this);
		pushView(cv,true);
	}

	public void pushViewForContent(Content c) {
		ContentViewControllerBase cv = c.createContentView(this);
		pushView(cv,false);
	}

	public void pushReplaceView(View cv) {
		pushView(cv, true);
	}

	public void pushView(View cv) {
		pushView(cv, false);
	}

	public void pushView(View cv, boolean removeOld) {
		pushView(cv, removeOld ? stack.size()-1 : stack.size());
	}

	public void pushView(View cv, int toRemain) {
		oldView = stack.get(stack.size()-1);
		if (oldView instanceof ContentViewControllerBase) {
			ContentViewControllerBase cvcb = (ContentViewControllerBase)oldView;
			cvcb.blockInput();
		}
		while (stack.size() > toRemain) {
			stack.remove(stack.size()-1);
		}
		stack.add(cv);
		topView.addView(cv);
		currentView = cv;
		
		oldView.setVisibility(View.VISIBLE);
//		oldView.setDrawingCacheEnabled(true);
		currentView.setVisibility(View.INVISIBLE);
//		currentView.setDrawingCacheEnabled(true);
		
		ContentViewControllerBase.OnViewReadyListener listener = new ContentViewControllerBase.OnViewReadyListener() {
			@Override
			public void onViewReady() {
				currentView.setVisibility(View.VISIBLE);

				Animation in = inFromRightAnimation();
				Animation out = outToLeftAnimation();
				out.setAnimationListener(new Animation.AnimationListener() {
					public void onAnimationStart(Animation animation) {}
					public void onAnimationRepeat(Animation animation) {}
					public void onAnimationEnd(Animation animation) {
						topView.post(new Runnable() {
							@Override
							public void run() {
//								oldView.setDrawingCacheEnabled(false);
//								currentView.setDrawingCacheEnabled(false);
								topView.removeView(oldView);
								oldView = null;
								currentView.setAnimation(null);
							}
						});
					}
				});

				in.setFillAfter(true);
				in.setFillBefore(true);
				in.setFillEnabled(true);
				out.setFillAfter(true);
				out.setFillBefore(true);
				out.setFillEnabled(true);

				if (oldView != null) {
					oldView.invalidate();
//					oldView.buildDrawingCache();
				}
				currentView.invalidate();
//				currentView.buildDrawingCache();

				long time = AnimationUtils.currentAnimationTimeMillis();

				in.setStartTime(time);
				out.setStartTime(time);
				
				if (oldView != null) {
					oldView.setAnimation(out);
				}
				currentView.setAnimation(in);
			}
		};
		
		if (currentView instanceof ContentViewControllerBase) {
			ContentViewControllerBase cvc = (ContentViewControllerBase)currentView;
			cvc.setOnViewReadyListener(listener);
		} else {
			listener.onViewReady();
		}
	}
	
	public void goBack() {
		
		PTSDCoach a = (PTSDCoach)getParent();
		a.getTabHost().setCurrentTab(0);
	}
	
	@Override
	public void onBackPressed() {
		if (stack.size() < 2) {
			goBack();
			return;
		}

		popView();
	}
	
	public void popToRoot() {
		while (stack.size() > 2) {
			stack.remove(1);
		}
		popView();
	}
	
	public void popView() {
		if (stack.size() <= 1) return;
		oldView = stack.remove(stack.size()-1);
		currentView = stack.get(stack.size()-1);
		topView.addView(currentView);
		
		Animation in = inFromLeftAnimation();
		Animation out = outToRightAnimation();
		out.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				topView.post(new Runnable() {
					@Override
					public void run() {
						topView.removeView(oldView);
						oldView = null;
						currentView.setAnimation(null);
					}
				});
			}
		});
		
		long time = AnimationUtils.currentAnimationTimeMillis();
		in.setStartTime(time);
		out.setStartTime(time);

		in.setFillAfter(true);
		in.setFillBefore(true);
		in.setFillEnabled(true);
		out.setFillAfter(true);
		out.setFillBefore(true);
		out.setFillEnabled(true);
		
		oldView.invalidate();
		currentView.invalidate();
		oldView.setAnimation(out);
		currentView.setAnimation(in);
	}
	
	public View getCurrentView() {
		return stack.get(stack.size()-1);
	}

	public ContentViewControllerBase getCurrentContentView() {
		View v = stack.get(stack.size()-1);
		if (v instanceof ContentViewControllerBase) {
			return (ContentViewControllerBase)v;
		}
		return null;
	}

	public Content getCurrentContent() {
		View v = stack.get(stack.size()-1);
		if (v instanceof ContentViewControllerBase) {
			return ((ContentViewControllerBase)v).getContent();
		}
		return null;
	}

	public void buttonTapped(int id) {
		getCurrentContentView().handleButtonTap(id);
	}

	public void contentSelected(Content c) {
		pushViewForContent(c);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == helpItem) {
			ContentViewControllerBase cvc = getCurrentContentView();
			if (cvc != null) {
				Content help = cvc.getContent().getHelp();
				if (help != null) {
					pushViewForContent(help);
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		helpItem = menu.add("Help");
		helpItem.setIcon(android.R.drawable.ic_menu_help);
		
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		ContentViewControllerBase cvc = getCurrentContentView();
		if (cvc != null) {
			Content c = cvc.getContent();
			if(c!=null) //check if content is null
			{
				helpItem.setVisible(cvc.getContent().hasHelp());
			}
		} else {
			helpItem.setVisible(false);
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}
	
	public void setVariable(String name, String value) {
		variables.put(name, value);
	}
	
	public String getVariable(String name) {
		return variables.get(name);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(!(this instanceof ManageNavigationController) && !(this instanceof HomeNavigationController))
		{
			ContentViewControllerBase cvc = getCurrentContentView();
			cvc.parentActivityPaused();		
		}
	}
}
