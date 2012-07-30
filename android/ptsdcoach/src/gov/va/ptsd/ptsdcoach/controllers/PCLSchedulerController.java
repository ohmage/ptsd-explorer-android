package gov.va.ptsd.ptsdcoach.controllers;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.activities.AssessNavigationController;
import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.questionnaire.Choice;

public class PCLSchedulerController extends ContentViewControllerBase {

	List<Content> children;
	boolean needsReset = false;

	public PCLSchedulerController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		setBackgroundColor(0xFF000000);
		setDrawingCacheBackgroundColor(0xFF000000);
//		setBackgroundDrawable(getBackground());

		children = getContent().getChildren();
		String[] items = new String[children.size()];
		final String[] tags = new String[children.size()];
		final RadioButton[] radios = new RadioButton[children.size()];
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
				nc.schedulePCLReminder(tags[checkedId-2000]);
				RadioButton radio = (RadioButton)group.findViewById(checkedId);
				String value = (String)radio.getTag();
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
		AssessNavigationController nc = (AssessNavigationController)getNavigator();
		String schedule = nc.getPCLReminderSchedule();
		for (int i = 0;i<tags.length;i++) {
			if (tags[i].equals(schedule)) {
				radios[i].setChecked(true);
				break;
			}
		}
		

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
}
