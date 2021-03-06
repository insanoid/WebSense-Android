/* **************************************************
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, kxu356@bham.ac.uk

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.uob.websense.app_monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.media.AudioManager;
import android.provider.Browser;

import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Constants;

/**
 * App monitoring utilities.
 * @author Karthikeya Udupa K M
 *
 */

public class AppMonitorUtil {

	/**
	 * Possible browser strings.
	 */
	String[] browserPackagenames = {
			"com.android.chrome",
			"me.android.browser"
	};
	/*
	 * 
			"org.mozilla.firefox",
			"com.opera.mini.android",
			"mobi.mgeek.TunnyBrowser",
	 * */

	/**
	 * Call checker
	 * @param context
	 * @return true if a call is going on.
	 */
	public static boolean isACallActive(Context context) {
		AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		if(manager.getMode()==AudioManager.MODE_IN_CALL){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Checks if the current application is a browser.
	 * @param packageName
	 * @return
	 */
	public boolean isBrowser(String packageName) {

		for(String s: browserPackagenames){
			if(packageName.equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	 * Gets the name and package-name of the application on top of the stack.
	 * @param ctx
	 * @return
	 */
	public HashMap<String, String> getTopPackageName(Context ctx) {

		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(android.content.Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1); 
		PackageManager manager = ctx.getPackageManager();

		CharSequence appNameSequence = null;

		try {

			appNameSequence = manager.getApplicationLabel(manager.getApplicationInfo(taskInfo.get(0).baseActivity.getPackageName() , PackageManager.GET_META_DATA));
			HashMap<String, String> response = new HashMap<String, String>();
			response.put(Constants.APP_NAME_TAG, appNameSequence.toString());
			response.put(Constants.APP_PACKAGE_NAME_TAG, taskInfo.get(0).baseActivity.getPackageName());
			return response;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Fetches the page being accessed by Google Chrome.
	 * @param ctx
	 * @return
	 */
	public String getLastAccessedBrowserPage(Context ctx) {

		Cursor webLinksCursor = ctx.getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, null, null, Browser.BookmarkColumns.DATE + " DESC");
		int row_count = webLinksCursor.getCount();

		int title_column_index = webLinksCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE);
		int url_column_index = webLinksCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.URL);

		if ((title_column_index > -1) && (url_column_index > -1) && (row_count > 0))
		{
			webLinksCursor.moveToFirst();
			while (webLinksCursor.isAfterLast() == false)
			{
				if (webLinksCursor.getInt(Browser.HISTORY_PROJECTION_BOOKMARK_INDEX) != 1)
				{
					if (!webLinksCursor.isNull(url_column_index))
					{
						return webLinksCursor.getString(url_column_index);
					}
				}
				webLinksCursor.moveToNext();
			}            
		}
		webLinksCursor.close();
		return null;
	}

	/**
	 * Checks if the app's front-end is running.
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isMyServiceRunning(Context ctx) {

		ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (AppUsageMonitor.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sends a notification with the current task information.
	 * @param currentTaskObject
	 * @param ctx
	 */
	public static void sendCurrentTaskNotification(AppUsageInformationModel currentTaskObject, Context ctx){

	}
	
	public static long getTotalForTasks(ArrayList<AppUsageInformationModel> apps){
		long sum = 0;
		
		for(AppUsageInformationModel app: apps){
			sum +=app.getCurrentAcitivtyRunningTime();
		}
		
		return sum;
	}
	
	

}
