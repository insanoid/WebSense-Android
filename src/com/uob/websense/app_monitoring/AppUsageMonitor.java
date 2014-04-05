package com.uob.websense.app_monitoring;


import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Constants;


public class AppUsageMonitor extends IntentService {

	private AppUsageInformationModel currentTask = new AppUsageInformationModel();
	private Date screenActivityDate = null;
	private static long screenActiveTime = 0L;
	private static long screenClosedTime = 0L;
	private BroadcastReceiver mPowerKeyReceiver = null;
	private Timer timer = null;
	
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
		//Write to file whatever u have.

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

		Log.i(Constants.APP_SERVICE_TAG, "Created Service.");
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

		getApplicationContext().registerReceiver(mPowerKeyReceiver, theFilter);
		timer = new Timer("SERVICE_MONITOR_TAG");
		timer.schedule(updateTask, 1000L, Constants.TASK_POLLER_TIMER);

	}

	
	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {

			PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
			boolean isScreenOn = powerManager.isScreenOn();

			if (isScreenOn) {
				AppMonitorUtil appMonitor = new AppMonitorUtil();
				HashMap<String, String>response = appMonitor.getTopPackageName(UsageMonitoringService.this);
				
				if(!(currentTask!=null && currentTask.getApplicationPackageName()!=null)){
					initialiseCurrentItem(response);
				}

				if(currentTask.getApplicationPackageName().equalsIgnoreCase(response.get(Constants.APP_PACKAGE_NAME_TAG))){
					currentTask.setCurrentAcitivtyRunningTime(currentTask.getCurrentAcitivtyRunningTime()+kTimerInterval);	
				}else{
					pushCurrentItem();
					initialiseCurrentItem(response);
				}


				if(appMonitor.isBrowser(currentTask.getApplicationPackageName())){
					try {
						String last_accessed_url = appMonitor.getLastAccessedBrowserPage(UsageMonitoringService.this);
						URL hostURL = new URL(last_accessed_url);

						if(hostURL.equals(currentTask.getAssociatedURIResource()) || currentTask.getAssociatedURIResource()==null){
							//Do nothing for now if same URL. 
							//In future if too long it has been on the same page then gather more information.
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
		taskList.add(currentTask);
		currentTask = null;
	}

	public void sendIntentNotification() {

		StringBuilder b = new StringBuilder();


		HashMap<String, Long>timeMap = new HashMap<String, Long>();
		timeMap.put(currentTask.getApplicationName(), currentTask.getCurrentAcitivtyRunningTime());
		for(AppUsageInformationModel m:taskList){
			if(timeMap.containsKey(m.getApplicationName())){
				timeMap.put(m.getApplicationName(), timeMap.get(m.getApplicationName())+m.getCurrentAcitivtyRunningTime());
			}else{
				timeMap.put(m.getApplicationName(),m.getCurrentAcitivtyRunningTime());
			}
		}

		for(String m:timeMap.keySet()){
			try{
				b.append(m + " - "+String.format("%d min, %d sec\n", 
						TimeUnit.MILLISECONDS.toMinutes(timeMap.get(m)),
						TimeUnit.MILLISECONDS.toSeconds(timeMap.get(m)) - 
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMap.get(m)))
						));
			}catch(Exception e){

			}
		}


		Intent proxIntent = new Intent("CONTENT_CHANGE");  
		proxIntent.putExtra("CONTENT",b.toString());
		sendBroadcast(proxIntent);
	}


	public void updateCurrentItem() {
		//TODO: update current item with the new context.
	}

	public void initialiseCurrentItem(HashMap<String, String> appInfo) {

		currentTask = new AppUsageInformationModel();
		currentTask.setCurrentAcitivtyRunningTime(0L);
		currentTask.setApplicationName(appInfo.get(Constants.APP_NAME_TAG));
		currentTask.setApplicationPackageName(appInfo.get(Constants.APP_PACKAGE_NAME_TAG));
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

}