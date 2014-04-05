package com.uob.websense.support;

import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

public class Util {

	private static final String MY_PREFS_FILE_NAME = "SEC_PREF";

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

}
