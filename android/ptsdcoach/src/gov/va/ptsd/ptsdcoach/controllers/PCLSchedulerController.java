package gov.va.ptsd.ptsdcoach.controllers;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.activities.AssessNavigationController;
import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.fragments.ReminderPickerFragment;

import java.util.List;

public class PCLSchedulerController extends ContentViewControllerBase {

	List<Content> children;
	boolean needsReset = false;
	private String[] tags;
	private RadioButton[] radios;

	public PCLSchedulerController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		setBackgroundColor(0xFF000000);
		setDrawingCacheBackgroundColor(0xFF000000);
//		setBackgroundDrawable(getBackground());

		children = getContent().getChildren();

	    // show correct reminders for EMA
        if(PTSDCoach.EMA) {
            children = children.subList(0, 2);
        } else {
            children.remove(1);
        }

		String[] items = new String[children.size()];
		tags = new String[children.size()];
		radios = new RadioButton[children.size()];
		for (int i=0;i<items.length;i++) {
			items[i] = children.get(i).getDisplayName();
			tags[i] = children.get(i).getName();
		}

		LinearLayout screenView = new LinearLayout(getContext());
        screenView.setOrientation(LinearLayout.VERTICAL);
        screenView.setBackgroundColor(0);

		final RadioGroup choicesView = new RadioGroup(getContext());
		choicesView.setOrientation(LinearLayout.VERTICAL);

		int id = 0;
		for (Content c : children) {
			RadioButton radio = new RadioButton(getContext());
			radio.setText(c.getDisplayName());
			radio.setTag(c.getName());
			radio.setTextSize(17);
			radio.setTextColor(0xFFFFFFFF);
			radio.setId(id+2000);
			radios[id] = radio;
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 45);
			radio.setLayoutParams(layout);
			choicesView.addView(radio);
			id++;
		}

		choicesView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				AssessNavigationController nc = (AssessNavigationController)getNavigator();
				nc.schedulePCLReminder(tags[checkedId-2000], new ReminderPickerFragment.ReminderPickerListener() {
					
					@Override
					public void onTimeSelected(ReminderPickerFragment fragment) {
					}
					
					@Override
					public void onCancelled(ReminderPickerFragment fragment) {
						setItemChecked();
					}
				});
			}
		});

		String htmlBody = getContent().getMainText();
		WebView wv = createWebView(htmlBody);
		wv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		screenView.addView(wv);
		screenView.addView(choicesView);
		addView(screenView);
/*		
		
		final ListView list = new ListView(getContext());
		list.setBackgroundColor(0xFF000000);
		list.setDrawingCacheBackgroundColor(0xFF000000);
		list.setCacheColorHint(0xFF000000);
		addView(list);
		
		RectShape rect = new RectShape();
		ShapeDrawable shapeDrawable = new ShapeDrawable(rect);
		shapeDrawable.setColorFilter(0xFF808080, Mode.SRC);
		list.setDivider(shapeDrawable);
		list.setDividerHeight(1);

		list.setItemsCanFocus(true);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		String htmlBody = getContent().getMainText();
		WebView wv = createWebView(htmlBody);
		wv.setLayoutParams(new ListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		list.addHeaderView(wv);

		list.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_single_choice,items) {
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View row = super.getView(position, convertView, parent);

//	    		row.setFocusable(true);

	    		TextView label = (TextView)row.findViewById(android.R.id.text1);
		        label.setTextColor(0xFFFFFFFF);
		        return row;
		    }			
			
		});
*/
		setItemChecked();
		

		/*
		list.setItemChecked(sel, true);

		list.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					needsReset = true;
				}
			}
		});
		
		list.setOnItemSelectedListener(new ListView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
				if ((pos == 5) && needsReset) {
					list.setSelection(1);
					needsReset = false;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		list.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(android.widget.AdapterView<?> list, View child, int position, long rowid) {
				AssessNavigationController nc = (AssessNavigationController)getNavigator();
				nc.schedulePCLReminder(tags[position-1]);
			}
		});
	*/
	}

	private void setItemChecked() {
		AssessNavigationController nc = (AssessNavigationController)getNavigator();
		String schedule = nc.getPCLReminderSchedule();
		boolean checked = false;
		if(TextUtils.isEmpty(schedule))
			schedule="none";
		for (int i = 0;i<tags.length;i++) {
			if (tags[i].equals(schedule)) {
			    checked = true;
				radios[i].setChecked(true);
			} else {
				radios[i].setChecked(false);
			}
		}
		// If nothing is checked, check the None
		if(!checked)
		    radios[0].setChecked(true);
	}
}
