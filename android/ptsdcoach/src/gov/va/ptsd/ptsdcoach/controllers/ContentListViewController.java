package gov.va.ptsd.ptsdcoach.controllers;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.content.Content;

public class ContentListViewController extends ContentViewControllerBase {

	List<Content> children;

	public ContentListViewController(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void build() {
		setBackgroundColor(0xFF000000);
		setDrawingCacheBackgroundColor(0xFF000000);
//		setBackgroundDrawable(getBackground());
		
		final ListView list = new ListView(getContext());
		list.setBackgroundColor(0xFF000000);
		list.setDrawingCacheBackgroundColor(0xFF000000);
		list.setCacheColorHint(0xFF000000);
		addView(list);
		
		children = getContent().getChildren();
		String[] items = new String[children.size()];
		for (int i=0;i<items.length;i++) {
			items[i] = children.get(i).displayName;
		}
		RectShape rect = new RectShape();
		ShapeDrawable shapeDrawable = new ShapeDrawable(rect);
		shapeDrawable.setColorFilter(0xFF808080, Mode.SRC);
		list.setDivider(shapeDrawable);
		list.setDividerHeight(1);

		StateListDrawable selector = new StateListDrawable();

		ShapeDrawable selectedDrawable = new ShapeDrawable(rect);
		selectedDrawable.setColorFilter(0xFF80FF80, Mode.SRC);
		selector.addState(new int[] {android.R.attr.state_pressed}, selectedDrawable);
		selector.addState(new int[] {android.R.attr.state_selected}, selectedDrawable);

		final ShapeDrawable focusedDrawable = new ShapeDrawable(rect);
		focusedDrawable.setColorFilter(0xFF80FF80, Mode.SRC);
		selector.addState(new int[] {android.R.attr.state_focused}, focusedDrawable);

		list.setSelector(selector);
//		list.setFocusable(true);
		list.setItemsCanFocus(true);

		list.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.list_item_with_disclosure,items) {
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View row = convertView;
		        
		        if (row == null) {
		            LayoutInflater inflater = LayoutInflater.from(this.getContext());
		            row = inflater.inflate(R.layout.list_item_with_disclosure,null);
		    		
		    		RectShape rect = new RectShape();
		            final ShapeDrawable focusedDrawable = new ShapeDrawable(rect);
		    		focusedDrawable.setColorFilter(0xFF60A060, Mode.SRC);

		    		row.setOnFocusChangeListener(new OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								v.setBackgroundDrawable(focusedDrawable);
							} else {
								v.setBackgroundDrawable(null);
							}
						}
					});
		            
//		            ImageView disclosure = (ImageView)row.findViewById(R.id.disclosure);
	//	            disclosure.setImageResource(R.drawable.disclosure);
		        }
		        
		        TextView label = (TextView)row.findViewById(R.id.term);
		        label.setText(this.getItem(position));
		        
		        row.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						v.requestFocus();
						int position = list.getPositionForView(v);
						pushContent(children.get((int)position));
					}
				});
		        row.setFocusable(true);
		        
		        return row;
		    }			
			
		});
		list.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(android.widget.AdapterView<?> list, View child, int position, long rowid) {
				pushContent(children.get((int)position));
			}
		});
	}
	
	@Override
	public void parentActivityPaused()
	{
		PTSDCoach a =(PTSDCoach)getNavigator().getParent();
		
		if(a.fromBackground)
		{
			//we are going into the background,go back 
			getNavigator().popToRoot();
			getNavigator().goBack();
		}
	}
}
