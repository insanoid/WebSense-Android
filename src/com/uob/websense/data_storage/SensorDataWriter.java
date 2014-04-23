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

package com.uob.websense.data_storage;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;

import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.data_models.ContextModel;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;

/**
 * Extension of adapter for specific tasks.
 * @author karthikeyaudupa
 *
 */
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
			String query = "DELETE FROM "+ Constants.APP_INFO_TABLE + " WHERE end_time < "+ String.valueOf(time)+" AND synced = 1";
			createDatabase();
			open();
			
			if(super.execute(query)==true){
				Util.logi("Cleared Synced Outdated Records: ["+ Constants.APP_INFO_TABLE + "]");
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

	public static class ContextDataProvider extends DBAdapter {

		public ContextDataProvider(Context context) {
			super(context, Constants.APP_INFO);

		}

		public void save(ContextModel model) {
			if(model==null){
				
			}else{
				super.insert(model.getContentValues(),Constants.CONTEXT_INFO_TABLE);
			}
		}

		public void purgeSyncedRecords(){
			
			long currentSeconds = System.currentTimeMillis();
			long time = currentSeconds - (Constants.DAY_MONTH);
			String query = "DELETE FROM "+ Constants.CONTEXT_INFO_TABLE + " WHERE timestamp < "+ String.valueOf(time)+" AND synced = 1";
			createDatabase();
			open();
			
			if(super.execute(query)==true){
				Util.logi("Cleared Synced Outdated Records: ["+ Constants.CONTEXT_INFO_TABLE + "]");
			}
			
		}
		
		public int getUnsyncedRecordCount(){

			return super.getUnsyncedRecordCount(Constants.CONTEXT_INFO_TABLE);
		}
		
		public Boolean updateRecords(String ids){
			return super.updateRecords(ids, Constants.CONTEXT_INFO_TABLE);
		}

		public JSONArray getUnSyncedAppRecords(int count){
			return super.getUnSyncedAppRecords(count, Constants.CONTEXT_INFO_TABLE);
		}
		
	}
}