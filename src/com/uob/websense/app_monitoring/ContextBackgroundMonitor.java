package com.uob.websense.app_monitoring;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.uob.contextframework.ContextManager;
import com.uob.contextframework.support.ContextManagerServices;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;

public class ContextBackgroundMonitor extends IntentService {

	

	private ContextBroadCastReceiver mContextBroadCastReceiver;
	
	
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
		mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION, 5*10*1000L);
		mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_BATTERY, 5*10*1000L);
		mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_WIFI, 5*10*1000L);
		mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_SIGNALS, 5*10*1000L);
		mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_EVENTS, 5*10*1000L);
		
		mContextBroadCastReceiver = new ContextBroadCastReceiver();
		IntentFilter filterProx = new IntentFilter(com.uob.contextframework.support.Constants.CONTEXT_CHANGE_NOTIFY);
		filterProx.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(mContextBroadCastReceiver, filterProx);
		
		
	}
	
	// Handler for receiving changes in points.
		public class ContextBroadCastReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {;
				
				String contentType = intent.getStringExtra(com.uob.contextframework.support.Constants.INTENT_TYPE);
				Util.loge("Context Intent Called: "+ contentType);
				if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.LOC_NOTIFY)){
					Location newLocation = (Location) intent.getExtras().get(com.uob.contextframework.support.Constants.LOC_NOTIFY);
					Util.loge("-> "+ newLocation);
				}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.BATTERY_NOTIFY)){
					Util.loge("-> "+ intent.getStringExtra(com.uob.contextframework.support.Constants.BATTERY_NOTIFY));
				}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.SIGNAL_NOTIFY)){
					Util.loge("-> "+ intent.getExtras().get(com.uob.contextframework.support.Constants.SIGNAL_NOTIFY));
				}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.WIFI_NOTIFY)){
					Util.loge("-> "+ intent.getExtras().get(com.uob.contextframework.support.Constants.WIFI_NOTIFY));
				}else if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.EVENT_NOTIFY)){
					Util.loge("-> "+ intent.getExtras().get(com.uob.contextframework.support.Constants.EVENT_NOTIFY));
				}
				
			}
		}
}
