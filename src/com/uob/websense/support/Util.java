package com.uob.websense.support;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
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
import com.uob.websense.data_storage.SensorDataWriter;

public class Util {

	private static final String MY_PREFS_FILE_NAME = "SEC_PREF";

	
	public final static void logi(String text) {
		if(Constants.IS_DEBUG==true){
			Log.i(Constants.SYNC_LOG_TAG,text);
		}
		
	}
	
	public final static void logd(String text) {
		if(Constants.IS_DEBUG==true){
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

	public static void saveSecurePreference(Context ctx,String value, String key){
		final SharedPreferences prefs = new ObscuredSharedPreferences( 
				ctx, 
				ctx.getSharedPreferences(MY_PREFS_FILE_NAME, Context.MODE_PRIVATE)
				);    
		prefs.edit().putString(key,value).commit();
	}

	public static String getSecurePreference(Context ctx, String key){
		final SharedPreferences prefs = new ObscuredSharedPreferences( 
				ctx, 
				ctx.getSharedPreferences(MY_PREFS_FILE_NAME, Context.MODE_PRIVATE)
				);    
		return prefs.getString(key, null);
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
	
	public static void updateSyncRecordCount(Context ctx){

		SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(ctx);
		int recordCount = appDataProvider.getUnsyncedRecordCount();
		appDataProvider.close();
		Util.logi("Unsycned Records For Apps Updated: " + recordCount);
		Util.saveSecurePreference(ctx, String.valueOf(recordCount), Constants.APP_INFO_TABLE);
	}

}