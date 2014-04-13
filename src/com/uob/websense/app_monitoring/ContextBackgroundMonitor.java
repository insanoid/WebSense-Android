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

	

	private LocationChangeReciever mLocChangeReciever;
	
	
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
		
		Log.i(Constants.LOG_TAG, "background Created Service.");
		
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		super.onStartCommand(intent, flags, startId);
		initateMonitoring();
		return Service.START_STICKY;
	}
	
	private void initateMonitoring() {

		ContextManager mContextManager = new ContextManager(getApplicationContext());
		mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION, 5*10*1000L);
		
		mLocChangeReciever = new LocationChangeReciever();
		IntentFilter filterProx = new IntentFilter(com.uob.contextframework.support.Constants.LOC_NOTIY);
		filterProx.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(mLocChangeReciever, filterProx);
		
	}
	
	// Handler for receiving changes in points.
		public class LocationChangeReciever extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {;
				
				String contentType = intent.getStringExtra(com.uob.contextframework.support.Constants.INTENT_TYPE);
				
				if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.LOC_NOTIY)){
					//Location newLocation = (Location) intent.getExtras().get(com.uob.contextframework.support.Constants.LOC_NOTIY);
					//Do something.
				}
				
			}
		}
}