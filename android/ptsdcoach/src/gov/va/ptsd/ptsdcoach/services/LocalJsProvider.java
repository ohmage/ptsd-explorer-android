/*
 * Copyright (C) 2010 The IDEAL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.va.ptsd.ptsdcoach.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/**
 * Content Provider for serving JavaScript.
 * JavaScript files must be stored under:
 *   /sdcard/ideal-webaccess/js/
 * for example:
 *   /sdcard/ideal-webaccess/js/MY_JS_FILE.js
 *   
 * JavaScript files can be accessed by in the WebView by using:
 *   content://com.ideal.webaccess.localjs/MY_JS_FILE.js
 */
public class LocalJsProvider extends ContentProvider {
    private static final String URI_PREFIX = "content://gov.va.ptsd.ptsdcoach.services.localjs";

    public static String constructUri(String url) {
        Uri uri = Uri.parse(url);
        return uri.isAbsolute() ? url : URI_PREFIX + url;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        String filename = uri.toString();
        if (filename.length() > URI_PREFIX.length()) {
            filename = filename.substring(URI_PREFIX.length() + 1);
            if ((filename.indexOf("//") != -1) || (filename.indexOf("..") != -1)) {
                return null;
            }
            
    		File dataDir = getContext().getDir("accessibility", Context.MODE_PRIVATE);
            File file = new File(dataDir,"ideal-webaccess/js/" + filename);
/*
            try {
				InputStream is = (InputStream)file.toURL().getContent();
				byte[] b = new byte[is.available()];
				is.read(b);
				is.close();
				String s = new String(b);
				Log.d("js", s);
			} catch (Exception e) {
				e.printStackTrace();
			}
*/            
            ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file,
                    ParcelFileDescriptor.MODE_READ_ONLY);
            return parcel;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int delete(Uri uri, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public String getType(Uri uri) {
    	return "text/javascript";
//        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

}
