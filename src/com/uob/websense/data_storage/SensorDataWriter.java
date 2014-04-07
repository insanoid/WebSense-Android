package com.uob.websense.data_storage;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Constants;


public class SensorDataWriter {
	
	
	public static class AppDataProvider extends DBAdapter {


		public AppDataProvider(Context context) {
			super(context, Constants.APP_INFO);
			
		}
	
		public void save(AppUsageInformationModel model) {
			super.insert(model.getContentValues(),Constants.APP_INFO_TABLE);
		}
		
		public ArrayList<AppUsageInformationModel> getAppUsageInformation(){
			
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
		
	}
}