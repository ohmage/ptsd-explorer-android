package gov.va.ptsd.ptsdcoach.controllers;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.Util;
import gov.va.ptsd.ptsdcoach.activities.HomeNavigationController;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.activities.NavigationController;
import gov.va.ptsd.ptsdcoach.activities.SetupActivity;
import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.views.LoggingButton;

public class ButtonGridController extends ContentViewControllerBase {

	TableLayout grid;
	List<Content> buttons;
	LinearLayout linear;
	
	final static int SETUP_BUTTON = 1001;
	final static int FAVORITES_BUTTON = 1002;
	
	public ButtonGridController(Context ctx) {
		super(ctx);
	}
	
	class PictureButton extends LoggingButton {
		public PictureButton(Context ctx) {
			super(ctx);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
//			Drawable[] d = getCompoundDrawables();
//			String text = getText();
			int padding = getCompoundPaddingTop();
			super.onDraw(canvas);
		}
	}
	
	@Override
	public void build() {
		setBackgroundColor(0);
		setBackgroundDrawable(getBackground());
		
		linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.VERTICAL);
		addView(linear);

		grid = new TableLayout(getContext());
		grid.setBackgroundColor(0);
		grid.setId(100);
		
		Integer cellsPerRow = getContent().getIntAttribute("buttongrid_cellsPerRow");
		Integer outerMarginX = getContent().getIntAttribute("buttongrid_outerMarginX");
		Integer outerMarginY = getContent().getIntAttribute("buttongrid_outerMarginY");
		Integer cellMarginY = getContent().getIntAttribute("buttongrid_cellMarginX");
		Integer cellMarginX = getContent().getIntAttribute("buttongrid_cellMarginY");

		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		layout.weight = 1;
		grid.setLayoutParams(layout);

		grid.setStretchAllColumns(true);

		int columns = (cellsPerRow == null) ? 2 : cellsPerRow;
		
		buttons = getContent().getChildren();
		TableRow row = null;
		int count = 0;
		boolean firstRow = true;
		boolean lastRow = false;
		int rowCount = 0;
		int totalRows = (buttons.size() + columns-1) / columns;
		for (Content button : buttons) {
			boolean firstInRow = false;
			boolean lastInRow = false;
			if ((count % columns) == 0) {
				rowCount++;
				firstRow = (count == 0);
				lastRow = rowCount == totalRows;
				firstInRow = true;
				row = new TableRow(getContext());
				TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
				        ViewGroup.LayoutParams.FILL_PARENT,
				        ViewGroup.LayoutParams.FILL_PARENT,
				        1.0f);
//				if (cellMarginY != null) rowLp.topMargin = rowLp.bottomMargin = cellMarginY;
				layout.gravity = Gravity.CENTER;
				row.setLayoutParams(rowLp);
				grid.addView(row);
			}
			
			if ((count % columns) == (columns-1)) {
				lastInRow = true;
			}

			PictureButton b = new PictureButton(getContext());
//			b.setBackgroundResource(R.drawable.button_bg);
			b.setMaxLines(2);
			b.setId(count);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = v.getId();
					buttonTapped(id);
				}
			});
			b.setText(button.getDisplayName());
			Drawable image = button.getButtonImage();
			if (image != null) {
				int iwidth = image.getIntrinsicWidth();
				int iheight = image.getIntrinsicHeight();
				int width = iwidth;
				int height = iheight;
				if (columns == 1) {
//					if (width > 100) {
						width = 100;
						height = width * iheight / iwidth;
//					}
				} else {
//					if (height > 100) {
						height = 120;
						width = height * iwidth / iheight;
//					}
				}
				image.setBounds(new Rect(0,0,width,height));
				if (columns == 1) {
					b.setCompoundDrawables(image, null, null, null);
				} else {
					b.setCompoundDrawables(null, image, null, null);
					b.setTypeface(b.getTypeface(), Typeface.BOLD);
					b.setTextSize(17);
				}
			}

			TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
			        ViewGroup.LayoutParams.FILL_PARENT,
			        ViewGroup.LayoutParams.FILL_PARENT,
			        1.0f);
			
			int left=0,right=0,top=0,bottom=0;

			if (firstInRow) {
				if (outerMarginX != null) left += outerMarginX;
			} else {
				if (cellMarginX != null) left += cellMarginX;
			}

			if (lastInRow) {
				if (outerMarginX != null) right += outerMarginX;
			} else {
				if (cellMarginX != null) right += cellMarginX;
			}
			
			if (firstRow) {
				if (outerMarginY != null) top += outerMarginY;
			} else {
				if (cellMarginY != null) top += cellMarginY;
			}

			if (lastRow) {
				if (outerMarginY != null) bottom += outerMarginY;
			} else {
				if (cellMarginY != null) bottom += cellMarginY;
			}

			cellLp.setMargins(left,top,right,bottom);
			
			b.setLayoutParams(cellLp);
			row.addView(b);
			count++;
		}
		
		linear.addView(grid);

		if (getContent() != null) {
			Content c = getContent();
			if ("home".equals(c.getName())) {
				LoggingButton setupButton = new LoggingButton(getContext());
				setupButton.setText("Setup");
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 50);
				params.weight = 0;
				setupButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						handleButtonTap(SETUP_BUTTON);
					}
				});
				linear.addView(setupButton, params);
			} else if ("manage".equals(c.getName())) {
				LoggingButton favoritesButton = new LoggingButton(getContext());
				favoritesButton.setText("Favorites");
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 50);
				params.weight = 0;
				favoritesButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						handleButtonTap(FAVORITES_BUTTON);
					}
				});
				linear.addView(favoritesButton, params);
			}
			
		}
	}

	public void contentSelected(Content c) {
		getNavigator().contentSelected(c);
	}
	
	public void handleButtonTap(int id) {
		if (id == SETUP_BUTTON) {
			HomeNavigationController nc = (HomeNavigationController)getNavigator();
			nc.gotoSetup();
			return;
		}

		if (id == FAVORITES_BUTTON) {
			ManageNavigationController nc = (ManageNavigationController)getNavigator();
			nc.gotoFavorites();
			return;
		}
		
		contentSelected(buttons.get(id));
	}
	
	@Override
	public void parentActivityPaused()
	{
		PTSDCoach a =(PTSDCoach)getNavigator().getParent();
		
		if(a.fromBackground)
		{
			//we are going into the background,go back 
			getNavigator().goBack();
		}
	}
}
