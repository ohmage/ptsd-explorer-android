package gov.va.ptsd.ptsdcoach.controllers;

import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.audio.Audio;
import gov.va.ptsd.ptsdcoach.audio.AudioUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SoothingAudioController extends SimpleExerciseController {

	
	ScrollView scroller;
	
	Spinner audioList;
	
	Button play;
	private AudioStringAdapter adapter = null;
	UserDBHelper userDb ;
	Context con;
	MediaPlayer mp=null;
	public SoothingAudioController(Context ctx) {
		super(ctx);
		con=ctx;
		userDb = UserDBHelper.instance(ctx);
	}
	
	class JSInterface {
		public void listen() {
			playAudio();
		}
	}

	public View makeTitleView(String title) {
		TextView titleView = new TextView(getContext());
		titleView.setText(title);
		titleView.setTextColor(0xFFFFFFFF);
		titleView.setShadowLayer(1, 1, 1, 0xFF000000);
		titleView.setTextSize(24);
		titleView.setTypeface(titleView.getTypeface(), Typeface.BOLD);
		titleView.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setMargins(10,20,10,10);
		layout.gravity = Gravity.CENTER;
		return titleView;
	}

	
	
	@Override
	protected void onDetachedFromWindow() {
		if(mp!=null){
			mp.stop();
		}
		super.onDetachedFromWindow();
	}
	
	@Override
	public void build() {
		//
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

		if (content != null) {
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

			String text = content.getMainText();
			if (text != null) {
				if (content.hasAudio()) {
					String listenLink = "<a href=\"javascript:ptsdcoach.listen();\" style=\"float:right;margin:10px;\"><img src=\"Content/listen.png\"/></a>";
					text = listenLink + text;
				}

				//addMainTextContent(text);
			}
		}
		
		
		audioList = new Spinner(getContext());
	
		
		List<Audio> audios=AudioUtil.UriListToAudioList(userDb.getAllAudio(),con);
		if(audios.size()>0){
			
			adapter = new AudioStringAdapter(con, R.layout.row_audio, audioArrayToStringArray(audios));
		
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			clientView.addView(audioList);
			audioList.setAdapter(adapter);
			play= new Button(con);
			play.setText("Play");
			clientView.addView(play);

			play.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					if(play.getText().toString().equals("Play")){
						List<Audio> audios=AudioUtil.UriListToAudioList(userDb.getAllAudio(),con);
						if(audios==null || audios.size()==0){
							return;
						}
						int pos=audioList.getSelectedItemPosition();
						Uri uri=audios.get(pos).getAudioUril();
						if(uri==null){
							return;
						}
						
						try{
							mp=MediaPlayer.create(con, uri);
							Log.e("PTSD","Will Play :"+uri);
							//mp.prepare();
							mp.start();
							play.setText("Stop");
						}catch(Exception e){
							Log.e("PTSD","Playing :"+e.getMessage());
						}
					}else{
						if(mp!=null){
							mp.stop();
						}
						play.setText("Play");
					}
				}
			}
		
		);
		}
		else
		{
			// This is no longer necessary, since checkPrerequisites will make sure this case never occurs...
			// Also, if it is left in, the Toast comes up whenever the view is created, even if it gets rejected due to
			// missing prerequisites.
//			Toast.makeText(con,R.string.loading_audio_failed, Toast.LENGTH_LONG).show();
		}
	
	
		addThumbs();
		addButton("New Tool", ManageNavigationController.BUTTON_NEW_TOOL);
		addButton("I'm Done", ManageNavigationController.BUTTON_DONE_EXERCISE);
	}
	
	public String checkPrerequisites() {
		if (userDb.getAllAudio().size() > 0) return null;
		return "You haven't chosen any soothing songs or audio clips from your audio library.  Go to Setup and choose some audio before you can use this tool.";
	}
	
	class ViewHolder {
		TextView text;
	}
	
	List<String> audioArrayToStringArray(List<Audio> audios){
		List<String> audioNames=new ArrayList<String>();
		for(Audio a : audios){
			if(a==null){
				continue;
			}else if(a.getName()==null){
				audioNames.add("No Name");
			}else{
				audioNames.add(a.getName());
			}
		}
		return audioNames;
	}
	
	private class AudioStringAdapter extends ArrayAdapter<String> {

		private LayoutInflater li;
		private List<String> Strings;

		public AudioStringAdapter(Context context, int textViewResourceId,
				List<String> items) {
			super(context, textViewResourceId, items);

			this.Strings = items;
			li = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ViewHolder holder;
			final String s = this.Strings.get(position);
			
			if (convertView == null) {
				convertView = li.inflate(R.layout.row_audio, null);
				//holder = new ViewHolder();
				TextView tv = (TextView) convertView.findViewById(R.id.audio_name);
				tv.setText(s);
				
				convertView.setTag(tv);
			}else{
				//holder = (ViewHolder) convertView.getTag();
			}

			return convertView;
		}
	}
	
	@Override
	public void parentActivityPaused()
	{	
		if(mp!=null){
			mp.stop();
		}
		if(play.getText().toString().equals("Stop"))
			play.setText("Play");
	}


}
