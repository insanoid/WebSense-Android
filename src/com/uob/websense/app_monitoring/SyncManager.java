package com.uob.websense.app_monitoring;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.uob.contextframework.support.NetworkHelper;
import com.uob.websense.data_storage.SensorDataWriter;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;
import com.uob.websense.web_service_manager.WebSenseRestClient;

public class SyncManager extends IntentService {

	public SyncManager(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public SyncManager() {
		super("SYNC_TASK");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	} 

	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startid) {
		if(ifAppSyncRequired()==true && Util.checkForLogin(getApplicationContext())){
			syncRecords();
		}
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		super.onStartCommand(intent, flags, startId);
		setAlarmToRedoLoad();
		return Service.START_STICKY;
	}

	@SuppressLint("NewApi")
	@Override
	public void onTaskRemoved(Intent rootIntent){
		super.onTaskRemoved(rootIntent);
	}

	/* Reloading Background Service */
	private void setAlarmToRedoLoad() {
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(
				AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + Constants.BG_SERVICE_ALARM_RELOAD_TIME,
				makeSelfPendingIntent(getApplicationContext()));
	}

	private static PendingIntent makeSelfPendingIntent(Context context) {

		PendingIntent intent = PendingIntent.getService(context, 0, makeSelfIntent(context), 0);
		return intent;
	}

	private static Intent makeSelfIntent(Context context) {
		Intent intent = new Intent(context, SyncManager.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setAction("RELAUNCH_INSTANCE");
		return intent;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public boolean networkSyncRecommended(int count){

		boolean isWifi = NetworkHelper.getInstance(getApplicationContext()).isWiFiOn(getApplicationContext());
		if(count > Constants.MIN_RECORD_FOR_SYNC && isWifi==true){
			return true;
		}else if
		(count > Constants.RECORD_THRESHOLD_FOR_FORCED_SYNCED &&
				NetworkHelper.getInstance(getApplicationContext()).isWiFiOn(getApplicationContext())==false){

			return true;
		}else{
			return false;
		}
	}

	//App Usage Monitors.
	public boolean ifAppSyncRequired(){

		if(Util.getSecurePreference(getApplicationContext(), Constants.APP_INFO_TABLE)==null){
			Util.updateSyncRecordCount(getApplicationContext());
		}
		int recordCount = Integer.parseInt(Util.getSecurePreference(getApplicationContext(), Constants.APP_INFO_TABLE));


		Util.logi("Unsycned Records For Apps: " + recordCount);
		if(networkSyncRecommended(recordCount)==true){
			return true;
		}

		return false;
	}


	public void syncRecords() {

		SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(getApplicationContext());
		final JSONArray records = appDataProvider.getUnSyncedAppRecords(Constants.RECORD_BATCH_COUNT);
		appDataProvider.close();

		if(records.length()>0){
			JSONObject finalRecord = new JSONObject();

			try {
				finalRecord.put("auth_token", (Util.getSecurePreference(getApplicationContext(),Constants.AUTH_KEY_TOKEN)));
				finalRecord.put("app_info", records);
			} catch (JSONException e) {

				Util.loge("Error Making Packet: "+ e.toString());
			}
			String data = finalRecord.toString();
			StringEntity entity = null;
			try {
				entity = new StringEntity(data);
				entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				entity.setContentEncoding("gzip");
			} catch(Exception e) {
				Util.loge("Error Making Packet: "+ e.toString());
			}


			WebSenseRestClient.post(getApplicationContext(),Constants.APP_USAGE_METHOD,entity,"application/json",new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, String responseString) {

					Util.logi("Success Sending Packet: "+ responseString);
					SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(getApplicationContext());
					if(appDataProvider.updateRecords(markRecords(records))==true){
						syncRecords();
					}
					appDataProvider.close();
				}


				@SuppressWarnings("unused")
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					Util.loge("Error Sending Packet: "+ responseString);
					if(statusCode!=404){
						syncRecords();
					}
				}
			});

		}else{
			Util.logi("Nothing to syncronize - Apps");
		}

	}

	public String markRecords(JSONArray jArray){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<jArray.length();i++){
			try {
				JSONObject j = jArray.getJSONObject(i);
				ids.add(j.getInt("record_id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(ids.size()>0){
			StringBuilder result = new StringBuilder();
			for(Integer id : ids) {
				result.append(id);
				result.append(",");
			}
			return result.length() > 0 ? result.substring(0, result.length() - 1): "";
		}
		return null;

	}
}
