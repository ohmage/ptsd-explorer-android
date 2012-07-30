package gov.va.ptsd.ptsdcoach.content;

import gov.va.ptsd.ptsdcoach.ContentDBHelper;
import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContentActivity extends Activity {

	Content content;
	ContentViewControllerBase view;
	ContentDBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		db = ContentDBHelper.instance(this);

		Uri uri = getIntent().getData();
		String name = uri.getSchemeSpecificPart();
		Content c = db.getContentForName(name);
		setContent(c);
	}

	public ContentViewControllerBase createContentView() {
		return content.createContentView(this);
	}
	
	public void setContent(Content content) {
		this.content = content;

		view = createContentView();
		setContentView(view);
	}
}
