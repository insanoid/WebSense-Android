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

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.uob.contextframework.ContextManager;
import com.uob.contextframework.support.ContextManagerServices;
import com.uob.websense.data_models.ContextModel;
import com.uob.websense.data_storage.SensorDataWriter;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;

/**
 * Monitoring context information in the background.
 * @author karthikeyaudupa
 *
 */
public class ContextBackgroundMonitor extends IntentService {

	private ContextBroadCastReceiver mContextBroadCastReceiver;
	private Location currentlocationInfo;

	public ContextBackgroundMonitor(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public ContextBackgroundMonitor() {
		this("BACKGROUND_SERVICE");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		ContextManager mContextManager = new ContextManager(getApplicationContext());
		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION);
		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_BATTERY);
		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_WIFI);
		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_SIGNALS);
		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_EVENTS);
		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_BLUETOOTH);

		unregisterReceiver(mContextBroadCastReceiver);
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startid) {

	}


	@Override
	public void onCreate() {
		super.onCreate();
		currentlocationInfo = new Location(LocationManager.PASSIVE_PROVIDER);
		currentlocationInfo.setLatitude(0.0);
		currentlocationInfo.setLongitude(0.0);
		Log.i(Constants.LOG_TAG, " > background Created Service.");
		initateMonitoring();
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		super.onStartCommand(intent, flags, startId);

		return Service.START_STICKY;
	}

	private void initateMonitoring() {

		ContextManager mContextManager = new ContextManager(getApplicationContext());
		try {
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION, 60*60*1000L);
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_BATTERY, 10*60*1000L);
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_WIFI, 30*60*1000L);
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_SIGNALS, 15*60*1000L);
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_EVENTS, 2*60*1000L);
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_BLUETOOTH, 10*60*1000L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mContextBroadCastReceiver = new ContextBroadCastReceiver();
		IntentFilter filterProx = new IntentFilter(com.uob.contextframework.support.Constants.CONTEXT_CHANGE_NOTIFY);
		filterProx.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(mContextBroadCastReceiver, filterProx);


	}

	private void pushData(String contentType, String content) {

		if(content!=null){
			ContextModel contextModel = new ContextModel();
			contextModel.setContextType(contentType);
			contextModel.setContextValue(content);
			contextModel.setPosition(currentlocationInfo.getLatitude()+","+currentlocationInfo.getLongitude());
			contextModel.setTimestamp(String.valueOf( System.currentTimeMillis()));
			SensorDataWriter.ContextDataProvider contextDataProvider = new SensorDataWriter.ContextDataProvider(getApplicationContext());
			contextDataProvider.createDatabase();
			contextDataProvider.open();
			contextDataProvider.save(contextModel);
			contextDataProvider.close();
			Util.updateContextSyncRecordCount(getApplicationContext());
		}
	}

	// Handler for receiving changes in points.
	public class ContextBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {;

		String contentType = intent.getStringExtra(com.uob.contextframework.support.Constants.INTENT_TYPE);
		Util.loge("Context broadcast received: "+contentType);
		if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.LOC_NOTIFY)){
			Location newLocation = (Location) intent.getExtras().get(com.uob.contextframework.support.Constants.LOC_NOTIFY);
			currentlocationInfo = newLocation;

		}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.BATTERY_NOTIFY)){
			pushData(com.uob.contextframework.support.Constants.BATTERY_NOTIFY,intent.getStringExtra(com.uob.contextframework.support.Constants.BATTERY_NOTIFY));

		}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.SIGNAL_NOTIFY)){
			pushData(com.uob.contextframework.support.Constants.SIGNAL_NOTIFY,intent.getStringExtra(com.uob.contextframework.support.Constants.SIGNAL_NOTIFY));

		}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.WIFI_NOTIFY)){
			pushData(com.uob.contextframework.support.Constants.WIFI_NOTIFY,intent.getStringExtra(com.uob.contextframework.support.Constants.WIFI_NOTIFY));

		}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.EVENT_NOTIFY)){
			pushData(com.uob.contextframework.support.Constants.EVENT_NOTIFY,intent.getStringExtra(com.uob.contextframework.support.Constants.EVENT_NOTIFY));

		}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.BLUETOOTH_NOTIFY)){
			pushData(com.uob.contextframework.support.Constants.BLUETOOTH_NOTIFY,intent.getStringExtra(com.uob.contextframework.support.Constants.BLUETOOTH_NOTIFY));

		}

		}
	}
}
