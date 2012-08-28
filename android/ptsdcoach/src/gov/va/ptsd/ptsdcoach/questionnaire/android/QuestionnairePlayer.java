package gov.va.ptsd.ptsdcoach.questionnaire.android;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewController;
import gov.va.ptsd.ptsdcoach.questionnaire.AbstractQuestionnairePlayer;
import gov.va.ptsd.ptsdcoach.questionnaire.Choice;
import gov.va.ptsd.ptsdcoach.questionnaire.Questionnaire;
import gov.va.ptsd.ptsdcoach.views.LoggingButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

public class QuestionnairePlayer extends AbstractQuestionnairePlayer {

		private final Context context;
		ContentViewController topView = null;
		LinearLayout screenView = null;
		ScrollView scrollView = null;
		LoggingButton nextButton = null;
		LoggingButton deferButton = null;
		LoggingButton doneButton = null;
		LoggingButton skipButton = null;
		QuestionnaireListener listener = null;
		ArrayList<IEnablement> enablements = null;
		
		public interface QuestionnaireListener {
			public void onQuestionnaireCompleted(QuestionnairePlayer player);
			public void onQuestionnaireDeferred(QuestionnairePlayer player);
			public void onQuestionnaireSkipped(QuestionnairePlayer player);
			public void onShowScreen(QuestionnairePlayer player, View screen);
		}
		
		interface IEnablement {
			public boolean checkEnablement();
		}
		
		public QuestionnairePlayer(Context ctx, Questionnaire... q) {
			super(q);
			context = ctx;
		}
				
		@Override
		public String getLocale() {
			String lang = Locale.getDefault().getLanguage();
			return lang;
		}
		
		public void setQuestionnaireListener(QuestionnaireListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void recordAnswer(String id, Object answer) {
			super.recordAnswer(id, answer);
			TreeMap<String,String> map = new TreeMap<String, String>();
			map.put("question",id);
			map.put("answer",answer.toString());
			FlurryAgent.logEvent("PCL_ANSWER",map);
		}
		
		@Override
		public void beginScreen(String title) {
			scrollView = new ScrollView(context);
			scrollView.setBackgroundColor(0);

	        screenView = new LinearLayout(context);
	        screenView.setOrientation(LinearLayout.VERTICAL);
	        screenView.setBackgroundColor(0);

			scrollView.addView(screenView);
			
			topView = new ContentViewController(context);
			topView.setBackgroundColor(0);
			topView.setBackgroundDrawable(Util.makeDrawable(context, "table_bg_darker.png",true));
			topView.addView(scrollView);
			
			if (title != null) {
				View v = topView.makeTitleView(title);
	    		v.setFocusable(true);
	    		v.setFocusableInTouchMode(false);
				screenView.addView(v);
			}
			
	        nextButton = null;
	        deferButton = null;
	        doneButton = null;
	        skipButton = null;
			enablements = new ArrayList<IEnablement>();
		}

		public void addSpokenText(String text) {
			topView.addSpokenText(text);
		}

		public void setEnablements(boolean enabled) {
			if (nextButton != null) nextButton.setEnabled(enabled);
			if (doneButton != null) doneButton.setEnabled(enabled);
		}
		
		public void updateEnablements() {
			for (IEnablement enablement : enablements) {
				if (!enablement.checkEnablement()) {
					setEnablements(false);
					return;
				}
			}
			setEnablements(true);
		}
		
		@Override
		public void addChoiceQuestion(final String id, String question,
				final int minChoices, final int maxChoices, Choice[] choices) {
    		TextView label = new TextView(context);
    		//addSpokenText(question);
    		label.setText(question);
    		label.setFocusable(true);
    		label.setFocusableInTouchMode(false);
    		label.setTextSize(17);
    		label.setPadding(10, 10, 10, 10);
    		label.setTextColor(0xFFFFFFFF);
    		screenView.addView(label);

    		if (maxChoices == 1) {
				final RadioGroup choicesView = new RadioGroup(context);
				choicesView.setOrientation(LinearLayout.VERTICAL);

				for (Choice c : choices) {
					RadioButton radio = new RadioButton(context);
					radio.setText(c.getText(this));
					radio.setTag(c.getValue());
					radio.setTextSize(17);
					radio.setTextColor(0xFFFFFFFF);
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 35);
					radio.setLayoutParams(layout);
					choicesView.addView(radio);
				}

				choicesView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton radio = (RadioButton)group.findViewById(checkedId);
						String value = (String)radio.getTag();
						recordAnswer(id, value);
						updateEnablements();
					}
				});
				
				enablements.add(new IEnablement() {
					@Override
                    public boolean checkEnablement() {
						if (minChoices == 0) return true;
						return choicesView.getCheckedRadioButtonId() != -1;
					}
				});
				screenView.addView(choicesView);
			} else {
				final LinearLayout choicesView = new LinearLayout(context);
				choicesView.setOrientation(LinearLayout.VERTICAL);
				final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();

				for (Choice c : choices) {
					CheckBox radio = new CheckBox(context);
					radio.setText(c.getText(this));
					radio.setTextSize(17);
					radio.setTag(c.getValue());
					radio.setTextColor(0xFFFFFFFF);
					choicesView.addView(radio);
					checkboxes.add(radio);

					radio.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							String[] values = new String[0];
							for (int i=0; i<choicesView.getChildCount();i++) {
								View v = choicesView.getChildAt(i);
								if (v instanceof CheckBox) {
									CheckBox cb = (CheckBox)v;
									if (cb.isChecked()) {
										String[] a = new String[values.length+1];
										System.arraycopy(values,0,a,0,values.length);
										values = a;
										values[values.length-1] = (String)cb.getTag();
									}
								}
							}
							recordAnswer(id, values);
							updateEnablements();
						}
					});
				}			
				enablements.add(new IEnablement() {
					@Override
                    public boolean checkEnablement() {
						int total = checkboxes.size();
						int checked = 0;
						for (CheckBox checkbox : checkboxes) {
							if (checkbox.isChecked()) checked++;
						}
						return (checked >= minChoices) && (checked <= maxChoices);
					}
				});
				screenView.addView(choicesView);
			}
		}

		@Override
		public void addFreeformQuestion(final String id, String question, int lines, final boolean mandatory) {
    		TextView label = new TextView(context);
//    		addSpokenText(question);
    		label.setText(question);
    		label.setTextSize(14);
    		label.setTextColor(0xFFFFFFFF);
    		label.setFocusable(true);
    		label.setFocusableInTouchMode(false);
    		label.setPadding(10, 10, 10, 10);
    		screenView.addView(label);

    		final EditText edit = new EditText(context);
			edit.setLines(lines);
			edit.setGravity(Gravity.TOP);
			edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					String txt = v.getText().toString();
					recordAnswer(id, txt);
					updateEnablements();
					return false;
				}
			});
			edit.addTextChangedListener(new TextWatcher() {

			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			        recordAnswer(id, s.toString());
			        updateEnablements();
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			        // TODO Auto-generated method stub

			    }

			    @Override
			    public void afterTextChanged(Editable s) {
			        // TODO Auto-generated method stub

			    }
			});
			enablements.add(new IEnablement() {
				@Override
                public boolean checkEnablement() {
					if (!mandatory) return true;
					if ((edit.getText() == null) || "".equals(edit.getText())) return false;
					return true;
				}
			});
			screenView.addView(edit);
		}

		@Override
		public void addImage(String url) {
            ImageView imageView = new ImageView(context);
            imageView.setPadding(5, 5, 5, 5);
            //imageView.setImageResource(R.drawable.full_logo);
            screenView.addView(imageView);
			InputStream in;
			boolean success = false;
			try {
				in = context.getApplicationContext().getAssets().open(url);
				if (in != null) {
					BitmapDrawable bmp = new BitmapDrawable(in);
					if (bmp != null) {
						imageView.setImageDrawable(bmp);
						success = true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (!success) {
				imageView.setImageURI(Uri.parse(url));
			}
		}

		@Override
		public void addText(String text) {
			WebView label = topView.createWebView(text);
    		label.setPadding(10, 10, 10, 10);
    		screenView.addView(label);
		}

		@Override
		public void addButton(final int buttonType, String label) {
			final LoggingButton button = new LoggingButton(context);
			button.setText(label);
			button.setMinWidth(150);
			button.setOnClickListener(new Button.OnClickListener() {
				@Override
                public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)QuestionnairePlayer.this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
					IBinder token = button.getWindowToken();
					imm.hideSoftInputFromWindow(token, 0);
					switch (buttonType) {
						case BUTTON_NEXT:
							nextPressed();
							break;
						case BUTTON_DEFER:
							deferPressed();
							break;
						case BUTTON_SKIP:
							skipPressed();
							break;
						case BUTTON_DONE:
							donePressed();
							break;
					}
				}
			});
			
			switch (buttonType) {
				case BUTTON_NEXT:
					nextButton = button;
					break;
				case BUTTON_DEFER:
					deferButton = button;
					break;
				case BUTTON_SKIP:
					skipButton = button;
					break;
				case BUTTON_DONE:
					doneButton = button;
					break;
			}
		}
		
		@Override
		public void deferPressed() {
			if (listener != null) listener.onQuestionnaireDeferred(this);
		}

		@Override
		public void skipPressed() {
			if (listener != null) listener.onQuestionnaireSkipped(this);
		}
		
		
		@Override
		public void finish() {
			if (listener != null) listener.onQuestionnaireCompleted(this);
		}

		@Override
		public void donePressed() {
			if (listener != null) listener.onQuestionnaireCompleted(this);
		}

		@Override
		public void showScreen() {
			LinearLayout buttons = new LinearLayout(context);
			buttons.setPadding(5, 10, 5, 10);
			buttons.setOrientation(LinearLayout.HORIZONTAL);
			buttons.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
			buttons.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
			ArrayList<LoggingButton> allbuttons = new ArrayList<LoggingButton>(); 
			if (deferButton != null) allbuttons.add(deferButton);
			if (skipButton != null) allbuttons.add(skipButton);
			if (nextButton != null) allbuttons.add(nextButton);
			if (doneButton != null) allbuttons.add(doneButton);
			for (int i=0;i<allbuttons.size();i++) {
				LoggingButton b = allbuttons.get(i);
				if (b != null) {
					LinearLayout group = new LinearLayout(context);
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
					layout.weight = 1;
					if (i == allbuttons.size()-1) {
						group.setGravity(Gravity.RIGHT);
					} else if (i == 0) {
						group.setGravity(Gravity.LEFT);
					} else {
						group.setGravity(Gravity.CENTER);
					}
					group.setLayoutParams(layout);
					group.addView(b);
					buttons.addView(group);
				}
			}
			screenView.addView(buttons);
						
			updateEnablements();
				
			listener.onShowScreen(this,topView);

			if (nextButton != null) nextButton.requestFocus();
			else if (doneButton != null) doneButton.requestFocus();
			
			topView.onViewReady();
		}
		
	}
