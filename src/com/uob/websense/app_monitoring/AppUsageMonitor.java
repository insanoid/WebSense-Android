package com.uob.websense.app_monitoring;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.data_storage.SensorDataWriter;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;


public class AppUsageMonitor extends IntentService {

	private AppUsageInformationModel currentTask = new AppUsageInformationModel();
	private Date screenActivityDate = null;
	private static long screenActiveTime = 0L;
	private static long screenClosedTime = 0L;
	private BroadcastReceiver mPowerKeyReceiver = null;
	private Timer timer = null;
	private Location knownLocation;

	LocationChangeReceiver mLocationChangeReciever;

	public AppUsageMonitor(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public AppUsageMonitor() {
		super("USAGE_MONITOR");

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		unregisterReceiver(mLocationChangeReciever);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startid) {

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
		pushCurrentItem();
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
		Intent intent = new Intent(context, AppUsageMonitor.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setAction("RELAUNCH_INSTANCE");
		return intent;
	}
	/**/

	@Override
	public void onCreate() {
		super.onCreate();

		Log.i(Constants.LOG_TAG, "Created Service.");
		screenActivityDate = new Date();

		final IntentFilter theFilter = new IntentFilter();
		theFilter.addAction(Intent.ACTION_SCREEN_ON);
		theFilter.addAction(Intent.ACTION_SCREEN_OFF);

		mPowerKeyReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String strAction = intent.getAction();

				if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {
					screenActiveTime = screenActiveTime + new Date().getTime() - screenActivityDate.getTime();
					screenActivityDate = new Date();
					pushCurrentItem();
				}else if(strAction.equals(Intent.ACTION_SCREEN_ON)){
					screenClosedTime = screenClosedTime + new Date().getTime() - screenActivityDate.getTime();
					screenActivityDate = new Date();
				}
			}
		};


		mLocationChangeReciever = new LocationChangeReceiver();
		IntentFilter filterProx = new IntentFilter(com.uob.contextframework.support.Constants.CONTEXT_CHANGE_NOTIFY);
		filterProx.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(mLocationChangeReciever, filterProx);


		getApplicationContext().registerReceiver(mPowerKeyReceiver, theFilter);
		timer = new Timer(Constants.LOG_TAG);

		if(Util.checkForLogin(getApplicationContext())){
			timer.schedule(updateTask, 1000L, Constants.TASK_POLLER_TIMER);
		}else{
			Util.killServices(getApplicationContext());
		}
	}

	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {

			PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
			boolean isScreenOn = powerManager.isScreenOn();

			if (isScreenOn) {
				AppMonitorUtil appMonitor = new AppMonitorUtil();
				HashMap<String, String>response = appMonitor.getTopPackageName(AppUsageMonitor.this);


				if(!(currentTask!=null && currentTask.getApplicationPackageName()!=null)){
					initialiseCurrentItem(response);
				}

				if(currentTask.getApplicationPackageName().equalsIgnoreCase(response.get(Constants.APP_PACKAGE_NAME_TAG))){
					currentTask.setCurrentAcitivtyRunningTime(currentTask.getCurrentAcitivtyRunningTime()+ Constants.TASK_POLLER_TIMER);	
				}else{
					pushCurrentItem();
					initialiseCurrentItem(response);
				}
				//Log.i("Log App:","<"+currentTask.getApplicationName()+">:["+currentTask.getApplicationPackageName()+"]");

				if(appMonitor.isBrowser(currentTask.getApplicationPackageName())){
					try {
						String last_accessed_url = appMonitor.getLastAccessedBrowserPage(AppUsageMonitor.this);
						URL hostURL = new URL(last_accessed_url);

						if(hostURL.equals(currentTask.getAssociatedURIResource()) || currentTask.getAssociatedURIResource()==null){
							currentTask.setAssociatedURIResource(hostURL);
						}else{
							pushCurrentItem();
							initialiseCurrentItem(response);
							currentTask.setAssociatedURIResource(hostURL);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				sendIntentNotification();

			}

		}

	};

	public void pushCurrentItem() {
		exportDB();
		if(currentTask!=null){
			long seconds = System.currentTimeMillis();
			currentTask.setEndTime(seconds);

			if(currentTask.getPosition()==null && knownLocation!=null)
				currentTask.setPosition(knownLocation.getLatitude()+","+knownLocation.getLongitude());

			SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(getApplicationContext());
			appDataProvider.createDatabase();
			appDataProvider.open();
			if(currentTask.getApplicationPackageName()!=null)
				appDataProvider.save(currentTask);
			appDataProvider.close();
			currentTask = null;
		}

		Util.updateSyncRecordCount(getApplicationContext());
	}

	@SuppressWarnings("resource")
	public void exportDB(){
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {


				String currentDBPath = "/data/data/com.uob.websense/databases/APP_INFO.sqlite";
				String backupDBPath = sd + "/temp/filename.db";
				File currentDB = new File(currentDBPath);
				File backupDB = new File(backupDBPath);

				if (currentDB.exists()) {

					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendIntentNotification() {
		//Send Broadcast notifying about new app event.
		Intent proxIntent = new Intent(Constants.APP_CONTENT_CHANGE);  
		proxIntent.putExtra(Constants.APP_INFO_KEY,currentTask);
		sendBroadcast(proxIntent);
	}

	public void updateCurrentItem() {
		//TODO: update current item with the new context.
	}

	public void initialiseCurrentItem(HashMap<String, String> appInfo) {

		currentTask = new AppUsageInformationModel();
		currentTask.setCurrentAcitivtyRunningTime(0L);
		long seconds = System.currentTimeMillis();
		if(knownLocation!=null)
			currentTask.setPosition(knownLocation.getLatitude()+","+knownLocation.getLongitude());

		currentTask.setStartTime(seconds);
		currentTask.setApplicationName(appInfo.get(Constants.APP_NAME_TAG));
		currentTask.setApplicationPackageName(appInfo.get(Constants.APP_PACKAGE_NAME_TAG));
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

	// Handler for receiving changes in points.
	public class LocationChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String contentType = intent.getStringExtra(com.uob.contextframework.support.Constants.INTENT_TYPE);
			Util.loge("Context Intent Called: "+ contentType);
			if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.LOC_NOTIFY)){
				Location newLocation = (Location) intent.getExtras().get(com.uob.contextframework.support.Constants.LOC_NOTIFY);
				Util.loge("-> "+ newLocation);
				knownLocation = newLocation;
				pushCurrentItem();
			}

		}
	}
}