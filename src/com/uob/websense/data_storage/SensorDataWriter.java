package com.uob.websense.data_storage;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;

import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;


public class SensorDataWriter {

	public static class AppDataProvider extends DBAdapter {

		public AppDataProvider(Context context) {
			super(context, Constants.APP_INFO);

		}

		public void save(AppUsageInformationModel model) {
			if(model==null){
			
			}else{
				super.insert(model.getContentValues(),Constants.APP_INFO_TABLE);
			}
		}

		public void purgeSyncedRecords(){
			
			long currentSeconds = System.currentTimeMillis();
			long time = currentSeconds - (Constants.DAY_MONTH);
			String query = "DELETE FROM "+ Constants.APP_INFO + " WHERE end_time < "+ String.valueOf(time)+" AND synced = 1";
			createDatabase();
			open();
			
			if(super.execute(query)==true){
				Util.logi("Cleared Synced Outdated Records: ["+ Constants.APP_INFO + "]");
			}
			
		}
		
		public int getUnsyncedRecordCount(){

			return super.getUnsyncedRecordCount(Constants.APP_INFO_TABLE);
		}
		
		public Boolean updateRecords(String ids){
			return super.updateRecords(ids, Constants.APP_INFO_TABLE);
		}

		public JSONArray getUnSyncedAppRecords(int count){
			return super.getUnSyncedAppRecords(count, Constants.APP_INFO_TABLE);
		}
		
		public ArrayList<AppUsageInformationModel> getAppUsageInformationAll(){

			createDatabase();
			open();

			Cursor c = super.fetch("SELECT app_name, package_name, sum(active_time) as active_time, max(end_time) as end_time FROM APP_INFO group by package_name ORDER BY active_time", null);
			ArrayList<AppUsageInformationModel> apps = new ArrayList<AppUsageInformationModel>();

			if(c==null){
				return apps;
			}
			if (c.moveToLast()){
				do{
					AppUsageInformationModel app = new AppUsageInformationModel();
					app.setApplicationName(c.getString(c.getColumnIndex("app_name")));
					app.setApplicationPackageName(c.getString(c.getColumnIndex("package_name")));
					app.setCurrentAcitivtyRunningTime(Long.parseLong(c.getString(c.getColumnIndex("active_time"))));
					app.setEndTime(Long.parseLong(c.getString(c.getColumnIndex("end_time"))));
					apps.add(app);

				}while(c.moveToPrevious());
			}
			c.close();
			return apps;
		}

		public ArrayList<AppUsageInformationModel> getAppUsageInformationForToday(){
			Calendar rightNow = Calendar.getInstance();
			long offset = rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET);
			long sinceMidnight = (rightNow.getTimeInMillis() + offset) %(24 * 60 * 60 * 1000L);
			return getAppUsageInformationForNDays(sinceMidnight);
		}

		public ArrayList<AppUsageInformationModel> getAppUsageInformationForNDays(long n){

			createDatabase();
			open();

			long currentSeconds = System.currentTimeMillis();
			long time = currentSeconds - (n);
			String query = "SELECT app_name, package_name, sum(active_time) as active_time, max(end_time) as end_time "
					+ "FROM APP_INFO WHERE start_time>="+ String.valueOf(time)+" group by package_name ORDER BY active_time";

			Cursor c = super.fetch(query, null);
			ArrayList<AppUsageInformationModel> apps = new ArrayList<AppUsageInformationModel>();

			if(c==null){
				return apps;
			}
			if (c.moveToLast()){
				do{
					AppUsageInformationModel app = new AppUsageInformationModel();
					app.setApplicationName(c.getString(c.getColumnIndex("app_name")));
					app.setApplicationPackageName(c.getString(c.getColumnIndex("package_name")));
					app.setCurrentAcitivtyRunningTime(Long.parseLong(c.getString(c.getColumnIndex("active_time"))));
					app.setEndTime(Long.parseLong(c.getString(c.getColumnIndex("end_time"))));
					apps.add(app);

				}while(c.moveToPrevious());
			}
			c.close();
			return apps;
		}
	}
}