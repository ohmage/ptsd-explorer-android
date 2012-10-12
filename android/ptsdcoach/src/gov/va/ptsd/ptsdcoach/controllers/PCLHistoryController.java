package gov.va.ptsd.ptsdcoach.controllers;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.accessibility.AccessibilityManager;

import com.androidplot.Plot.BorderStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.LineAndPointRenderer;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.content.PCLScore;
import gov.va.ptsd.ptsdcoach.content.PCLSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PCLHistoryController extends ContentViewController {

	List<PCLScore> scores;
	
	static final int CLEAR_HISTORY = 501;
	
	public PCLHistoryController(Context ctx, List<PCLScore> scores) {
		super(ctx);
		this.scores = scores;
		
		SimpleDateFormat df = new SimpleDateFormat("MMM dd");
		SimpleDateFormat tf = new SimpleDateFormat("hh:mma");
		if (scores != null) {
			for (PCLScore s : scores) {
				Date d = new Date(s.time);
				String text = "Score of "+s.score+" on "+df.format(d) + " at " + tf.format(d)+".";
				addSpokenText(text);
			}
		}
		build();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		AccessibilityManager mgr = (AccessibilityManager)getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
		if (mgr.isEnabled()) {
			speakSpokenText();		
		}
	}
	
	@Override
	public void buttonTapped(int id) {
		if (id == CLEAR_HISTORY) {
			Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Clear History");
            builder.setIcon(R.drawable.icon);

            builder.setMessage("Are you sure you want to clear your assessment history?  You will be unable to view any past assessment progress or compare with future results.");
            builder.setPositiveButton("Yes, delete history", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					UserDBHelper.instance(getContext()).clearPCLScores();
					getNavigator().popToRoot();
				}
			});
            builder.setNegativeButton("Nevermind", null);

            builder.show();
            
		} else {
			super.buttonTapped(id);
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void build() {
		super.build();

		Date now = new Date();
		clientView.addView(makeTitleView("Symptom History"));
		
		XYPlot plot = new XYPlot(getContext(), "");
		
		// add a new series
        plot.addSeries(new PCLSeries(scores), LineAndPointRenderer.class, new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(200, 0, 0)));
 
        // reduce the number of range labels
        plot.setRangeLabel("");
        plot.setRangeBoundaries(17, 17*5, BoundaryMode.FIXED);
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 17);
        plot.setRangeValueFormat(new Format() {
			@Override
			public Object parseObject(String string, ParsePosition position) {
				return null;
			}
			@Override
			public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
				Number n = (Number)object;
				int score = n.intValue();
				
				if (score == 17) {
					buffer.append("Low");
				} else if (score == 17*2) {
				} else if (score == 17*3) {
					buffer.append("Med");
				} else if (score == 17*4) {
				} else if (score == 17*5) {
					buffer.append("High");
				}
				
				return buffer;
			}
		});

        plot.setDomainLabel("");
        plot.setDomainBoundaries(now.getTime() - DateUtils.DAY_IN_MILLIS * 56, now.getTime() + DateUtils.DAY_IN_MILLIS, BoundaryMode.FIXED);
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, DateUtils.DAY_IN_MILLIS * 14);
        plot.setDomainValueFormat(new Format() {
			@Override
			public Object parseObject(String string, ParsePosition position) {
				return null;
			}
			@Override
			public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
				Number n = (Number)object;
				Calendar cal = Calendar.getInstance(); 
				cal.setTimeInMillis(n.longValue());
				DateFormat df = new DateFormat();
				buffer.append(df.format("MMM dd", cal));
				return buffer;
			}
		});
/*         
        // reposition the domain label to look a little cleaner:
        Widget domainLabelWidget = plot.getDomainLabelWidget();
 
        plot.position(domainLabelWidget,                     // the widget to position
        			  45,                                    // x position value, in this case 45 pixels
        			  XLayoutStyle.ABSOLUTE_FROM_LEFT,       // how the x position value is applied, in this case from the left
        			  0,                                     // y position value
        			  YLayoutStyle.ABSOLUTE_FROM_BOTTOM,     // how the y position is applied, in this case from the bottom
        			  AnchorPosition.LEFT_BOTTOM);           // point to use as the origin of the widget being positioned
*/
        // get rid of the visual aids for positioning:
        Paint transparent = new Paint();
        transparent.setAlpha(0);
        plot.setBackgroundColor(0);
        plot.setBackgroundDrawable(null);
        plot.setBackgroundPaint(transparent);
        plot.setBorderStyle(BorderStyle.NO_BORDER, null, null);
        plot.setBorderPaint(transparent);
        plot.getLayoutManager().setPaddingPaint(transparent);
        plot.getLayoutManager().setMarginPaint(transparent);
        plot.getLayoutManager().setDrawMarginsEnabled(false);
        plot.getLayoutManager().setDrawOutlinesEnabled(false);
        plot.getLegendWidget().setHeight(0);
        plot.getGraphWidget().setPaddingRight(10);
        plot.getGraphWidget().setBorderPaint(transparent);
        plot.getGraphWidget().setBackgroundPaint(transparent);
        plot.getGraphWidget().setDrawBackgroundEnabled(false);
        
        plot.disableAllMarkup();
        
        clientView.addView(plot);
        
        addButton("Clear History",CLEAR_HISTORY);
	}

}
