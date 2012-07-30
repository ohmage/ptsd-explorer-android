package gov.va.ptsd.ptsdcoach.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gov.va.ptsd.ptsdcoach.PTSDCoach;
import gov.va.ptsd.ptsdcoach.R;
import gov.va.ptsd.ptsdcoach.UserDBHelper;
import gov.va.ptsd.ptsdcoach.Util;

import gov.va.ptsd.ptsdcoach.activities.ManageNavigationController;
import gov.va.ptsd.ptsdcoach.audio.Audio;
import gov.va.ptsd.ptsdcoach.audio.AudioUtil;

import gov.va.ptsd.ptsdcoach.image.Image;
import gov.va.ptsd.ptsdcoach.image.ImageUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class SoothingPictureController extends SimpleExerciseController {

	Spinner imageList;

	ImagesUriAdapter adapter;
	UserDBHelper userDb;
	Context con;
	//Bitmap currentImage;
	List<Image> images;
	ListView lView;

	Runnable handlePostedImage = new Runnable(){
		@Override
		public void run() {
			if(images!=null && images.size()>0){
				adapter = new ImagesUriAdapter(con, R.layout.row_image, images);
				lView.setAdapter(adapter);
			}else{
				Toast.makeText(con,R.string.loading_images_failed, Toast.LENGTH_LONG).show();
			}
		}
	};

	public OnItemClickListener listener= new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			List<Image> images=ImageUtil.UriListToImageList(userDb.getAllImages(),con);
			Image im=images.get(position-1);
			Intent intent = new Intent();  
			intent.setAction(android.content.Intent.ACTION_VIEW);  

			intent.setDataAndType(im.getimageUril(), "image/*");  
			con.startActivity(intent);
		}
	};

	public SoothingPictureController(Context ctx) {
		super(ctx);
		con = ctx;
		userDb = UserDBHelper.instance(ctx);
	}

	public String getContentMainText() {
		return "Look at a picture that is meaningful or soothing to you.  Tap on an image to zoom in.";
	}

	@Override
	public void build() {
		topView = new LinearLayout(getContext());
		topView.setOrientation(LinearLayout.VERTICAL);
		topView.setBackgroundColor(0);
		FrameLayout.LayoutParams topLayout = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		topView.setLayoutParams(topLayout);

		clientView = new LinearLayout(getContext());
		clientView.setOrientation(LinearLayout.VERTICAL);
		clientView.setBackgroundColor(0);

		buildClientViewFromContent();

		lView = new ListView(getContext());
		lView.setFocusableInTouchMode(true);
		lView.addHeaderView(clientView);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		lView.setBackgroundColor(0);
		lView.setCacheColorHint(0);
		lView.setDivider(null);
		lView.setLayoutParams(params);
		lView.setOnItemClickListener(listener);
		
		LinearLayout.LayoutParams scrollerLayout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		scrollerLayout.weight = 1;
		lView.setLayoutParams(scrollerLayout);
		adapter = new ImagesUriAdapter(con, R.layout.row_image, new ArrayList<Image>());
		lView.setAdapter(adapter);
		topView.addView(lView);

		setBackgroundColor(0);
		setBackgroundDrawable(getBackground());
		addView(topView);
		
		addThumbs();
		addButton("New Tool", ManageNavigationController.BUTTON_NEW_TOOL);
		addButton("I'm Done", ManageNavigationController.BUTTON_DONE_EXERCISE);
	}

	public String checkPrerequisites() {
		if (userDb.getAllImages().size() > 0) return null;
		return "You haven't chosen any soothing pictures from your photo library.  Go to Setup and choose some pictures before you can use this tool.";
	}

	@Override
	public void onViewReady() {
		super.onViewReady();
		Toast.makeText(con,R.string.loading_images, Toast.LENGTH_SHORT).show();
		new Thread(){
			public void run() {
				try {

					images=ImageUtil.UriListToImageList(userDb.getAllImages(),con);
					getHandler().post(handlePostedImage);
				}catch(Exception e){
					Log.e("PTSD","DECODE FAILED");
				}
			}
		}.start();
	}
	
	class ViewHolder {
		TextView text;
		ImageView image;
	}

	private class ImagesUriAdapter extends ArrayAdapter<Image> {

		private LayoutInflater li;
		private List<Image> images;

		public ImagesUriAdapter(Context context, int textViewResourceId,
				List<Image> items) {
			super(context, textViewResourceId, items);

			this.images = items;
			li = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final Image a = this.images.get(position);

			if (convertView == null) {
				convertView = li.inflate(R.layout.row_image, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.image_name);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				if(a==null){
					holder.text.setText("USB in use");
				}else{
					holder.image.setImageBitmap(a.getBitmap());
					if (a.getName().length() > 30) {
						holder.text.setText(a.getName().substring(0, 30));
					} else {
						holder.text.setText(a.getName());
					}
				}

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			return convertView;
		}

	}

	List<String> imageArrayToStringArray(List<Image> images) {
		List<String> imageNames = new ArrayList<String>();
		for (Image a : images) {
			if(a==null){
				continue;
			}else if (a.getName() == null) {
				imageNames.add("No Name");
			} else {
				imageNames.add(a.getName());
			}
		}
		return imageNames;
	}



}
