package gov.va.ptsd.ptsdcoach;

import gov.va.ptsd.ptsdcoach.content.Content;
import gov.va.ptsd.ptsdcoach.controllers.ContentViewControllerBase;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Util {

	static HashMap<String,Drawable> drawables = new HashMap<String, Drawable>();

	public static Drawable makeDrawable(Context ctx, String name) {
		return makeDrawable(ctx, name, false);
	}

	public static Drawable makeDrawable(Context ctx, String name, boolean cache) {
		Drawable d = drawables.get(name);
		if (d != null) return d;
		
		String path = "Content/"+name;
		try {
			InputStream is = ctx.getAssets().open(path);
			d = Drawable.createFromStream(is, name);
			drawables.put(name, d);
			return d;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
