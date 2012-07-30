package gov.va.ptsd.ptsdcoach.content;

import java.util.Calendar;
import java.util.List;

import android.text.format.DateFormat;

import com.androidplot.series.XYSeries;

public class PCLSeries implements XYSeries {

	List<PCLScore> scores;
	
	public PCLSeries(List<PCLScore> scores) {
		this.scores = scores;
	}
	
	@Override
	public String getTitle() {
		return "";
	}

	@Override
	public int size() {
		return scores.size();
	}

	@Override
	public Number getX(int i) {
		long time = scores.get(i).time;
		return time;
	}

	@Override
	public Number getY(int i) {
		return scores.get(i).score;
	}
	
}
