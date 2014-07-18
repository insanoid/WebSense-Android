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

package com.uob.websense.support;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.uob.websense.R;
import com.uob.websense.app_monitoring.AppUsageMonitor;
import com.uob.websense.app_monitoring.SyncManager;
import com.uob.websense.data_storage.SensorDataWriter;
import com.uob.websense.support.SecurePreferences.Editor;

public class Util {
	
	public final static void logi(String text) {
		if(Constants.IS_DEBUG==true){
			Log.i(Constants.SYNC_LOG_TAG,text);
		}
		
	}
	
	public final static void logd(String text) {
		if (Constants.IS_DEBUG ==true){
			Log.d(Constants.SYNC_LOG_TAG,text);
		}
		
	}
	
	public final static void loge(String text) {
		if(Constants.IS_DEBUG==true){
			Log.e(Constants.SYNC_LOG_TAG,text);
		}
		
	}
	
	public final static void logv(String text) {
		if(Constants.IS_DEBUG==true){
			Log.v(Constants.SYNC_LOG_TAG,text);
		}
		
	}
	
	public final static void logw(String text) {
		if(Constants.IS_DEBUG==true){
			Log.w(Constants.SYNC_LOG_TAG,text);
		}
		
	}
	
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public static Object parseResponse(String jsonString) throws JSONException {
		Object result = null;
		if (jsonString != null) {
			jsonString = jsonString.trim();
			if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
				result = new JSONTokener(jsonString).nextValue();
			}
		}
		if (result == null) {
			result = jsonString;
		}
		return result;
	}

	public static void removeSecurePreference(Context ctx,String key){
		
		SecurePreferences mSecurePrefs;
		mSecurePrefs = new SecurePreferences(ctx);
		final Editor secureEditor = mSecurePrefs.edit();
		secureEditor.remove(key);
		secureEditor.commit();
	}
	
	public static void saveSecurePreference(Context ctx,String value, String key){
		
		SecurePreferences mSecurePrefs;
		mSecurePrefs = new SecurePreferences(ctx);
		final Editor secureEditor = mSecurePrefs.edit();
		secureEditor.remove(key);
		secureEditor.commit();
		secureEditor.putString(key, value);
		secureEditor.commit();

	}

	public static String getSecurePreference(Context ctx, String key){
		SecurePreferences mSecurePrefs;
		mSecurePrefs = new SecurePreferences(ctx);
		String val = mSecurePrefs.getString(key,null);
		return val;
		

	      
	}

	@SuppressLint("NewApi")
	public static String calculateTime(long seconds) {

		long hours = TimeUnit.SECONDS.toHours(seconds);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);

		if(minute <= 0 ){
			return "less then a min.";
		}else if(hours <= 0){
			return minute + " min.";
		}else{
			return + hours+ " hr " + minute + " min.";
		}

	}

	public static void overrideFonts(final Context context, final View v) {
		try {
			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			} else if (v instanceof EditText) {
				((EditText)v).setTypeface( Typeface.createFromAsset(context.getAssets(),Constants.FONT_REGLUAR));
			}else if (v instanceof TextView) {
				((TextView)v).setTypeface( Typeface.createFromAsset(context.getAssets(), Constants.FONT_LIGHT));
			}else if (v instanceof CheckBox) {
				((CheckBox)v).setTypeface( Typeface.createFromAsset(context.getAssets(), Constants.FONT_LIGHT));
			}else if (v instanceof Button) {
				((Button)v).setTypeface( Typeface.createFromAsset(context.getAssets(), Constants.FONT_BOLD));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// ignore
		}
	}

	public static void fixCellFont(final Context context, final View v) {
		try {

			TextView title = (TextView)v.findViewById(R.id.title);
			if(title!=null)
				((TextView)title).setTypeface( Typeface.createFromAsset(context.getAssets(), Constants.FONT_BOLD));

			TextView sub_title = (TextView)v.findViewById(R.id.sub_title);

			if(sub_title!=null)
				((TextView)sub_title).setTypeface( Typeface.createFromAsset(context.getAssets(), Constants.FONT_REGLUAR));

			TextView accTxt = (TextView)v.findViewById(R.id.acc_txt);

			if(accTxt!=null)
				((TextView)accTxt).setTypeface( Typeface.createFromAsset(context.getAssets(), Constants.FONT_BOLD)); 	


		} catch (Exception e) {
			e.printStackTrace();
			// ignore
		}
	}

	@SuppressLint("NewApi")
	public static JSONArray cursorToJSONArray(Cursor crs) {
		JSONArray arr = new JSONArray();
		crs.moveToFirst();
		while (!crs.isAfterLast()) {
			int nColumns = crs.getColumnCount();
			JSONObject row = new JSONObject();
			for (int i = 0 ; i < nColumns ; i++) {
				String colName = crs.getColumnName(i);
				if (colName != null) {
					//String val = "";
					try {
						switch (crs.getType(i)) {
						case Cursor.FIELD_TYPE_BLOB   : row.put(colName, crs.getBlob(i).toString()); break;
						case Cursor.FIELD_TYPE_FLOAT  : row.put(colName, crs.getDouble(i))         ; break;
						case Cursor.FIELD_TYPE_INTEGER: row.put(colName, crs.getLong(i))           ; break;
						case Cursor.FIELD_TYPE_NULL   : row.put(colName, null)                     ; break;
						case Cursor.FIELD_TYPE_STRING : row.put(colName, crs.getString(i))         ; break;
						}
					} catch (JSONException e) {
					}
				}
			}
			arr.put(row);
			if (!crs.moveToNext())
				break;
		}
		return arr;
	}

	public static void showAlert(int alertMessage, Context ctx){

		if(ctx==null)
			return;

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(ctx.getApplicationContext().getString(alertMessage));
		builder.setNegativeButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void updateAppSyncRecordCount(Context ctx){

		SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(ctx);
		int recordCount = appDataProvider.getUnsyncedRecordCount();
		appDataProvider.close();
		Util.logi("Unsycned Records For Apps Updated: " + recordCount);
		Util.saveSecurePreference(ctx, String.valueOf(recordCount), Constants.APP_INFO_TABLE);
	}

	public static void updateContextSyncRecordCount(Context ctx){

		SensorDataWriter.ContextDataProvider contextDataProvider = new SensorDataWriter.ContextDataProvider(ctx);
		int recordCount = contextDataProvider.getUnsyncedRecordCount();
		contextDataProvider.close();
		Util.logi("Unsycned Records For Context Updated: " + recordCount);
		Util.saveSecurePreference(ctx, String.valueOf(recordCount), Constants.CONTEXT_INFO_TABLE);
	}
	
	/*
	 * Checks if the user is already logged in.
	 */
	public static boolean checkForLogin(Context ctx){
		
		String authKey = Util.getSecurePreference(ctx, Constants.AUTH_KEY_TOKEN);
		if(authKey==null){
			return false;
		}
		if(authKey.length()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public static void killServices(Context ctx){

		ctx.stopService(new Intent(ctx,AppUsageMonitor.class));
		ctx.stopService(new Intent(ctx,SyncManager.class));
		
		AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

	    Intent updateServiceIntent = new Intent(ctx, AppUsageMonitor.class);
	    PendingIntent pendingUpdateIntent = PendingIntent.getService(ctx, 0, updateServiceIntent, 0);

	    
	    Intent syncServiceIntent = new Intent(ctx, SyncManager.class);
	    PendingIntent pendingSyncIntent = PendingIntent.getService(ctx, 0, syncServiceIntent, 0);

	    
	    // Cancel alarms
	    try {
	        alarmManager.cancel(pendingUpdateIntent);
	        alarmManager.cancel(pendingSyncIntent);
	    } catch (Exception e) {
	        Util.loge("Alarms were not canncelled!");
	    }
		
	}
	
	
	public static void restart(Context context, int delay) {
	    if (delay == 0) {
	        delay = 1;
	    }
	    removeSecurePreference(context, Constants.AUTH_KEY_TOKEN);
	    Intent restartIntent = context.getPackageManager()
	            .getLaunchIntentForPackage(context.getPackageName());
	    PendingIntent intent = PendingIntent.getActivity(
	            context, 0,
	            restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
	    System.exit(2);
	}
	
}