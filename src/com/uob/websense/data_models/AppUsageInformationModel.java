package com.uob.websense.data_models;

import java.io.Serializable;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.uob.websense.support.Util;

import android.content.ContentValues;

public class AppUsageInformationModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 10000L;
	
	
	String applicationPackageName, applicationName, applicationIconURL;
	URL associatedURIResource;
	long currentAcitivtyRunningTime = 0;
	long startTime;
	long endTime;
	String category;
	
	
	public AppUsageInformationModel(JSONObject j) {
		try {
			setApplicationName(j.has("app_name")?j.getString("app_name"):"");
			setApplicationPackageName(j.has("package_name")?j.getString("package_name"):"");
			setCategory(j.has("category")?j.getString("category"):"");
			setApplicationIconURL(j.has("app_icon")?j.getString("app_icon"):"");
			
		} catch (JSONException e) {
			Util.logv("---------->>>>>>>>>>>>>ISSUE<<<--");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public AppUsageInformationModel() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the applicationPackageName
	 */
	public String getApplicationPackageName() {
		return applicationPackageName;
	}
	/**
	 * @param applicationPackageName the applicationPackageName to set
	 */
	public void setApplicationPackageName(String applicationPackageName) {
		this.applicationPackageName = applicationPackageName;
	}
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the associatedURIResource
	 */
	public URL getAssociatedURIResource() {
		return associatedURIResource;
	}
	/**
	 * @param associatedURIResource the associatedURIResource to set
	 */
	public void setAssociatedURIResource(URL associatedURIResource) {
		this.associatedURIResource = associatedURIResource;
	}
	/**
	 * @return the currentAcitivtyRunningTime
	 */
	public long getCurrentAcitivtyRunningTime() {
		return currentAcitivtyRunningTime;
	}
	/**
	 * @param currentAcitivtyRunningTime the currentAcitivtyRunningTime to set
	 */
	public void setCurrentAcitivtyRunningTime(long currentAcitivtyRunningTime) {
		this.currentAcitivtyRunningTime = currentAcitivtyRunningTime;
	}
	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	
	public ContentValues getContentValues(){
		
		ContentValues values = new ContentValues();

		values.put("package_name", getApplicationPackageName());
		values.put("app_name", getApplicationName());
		values.put("associated_url", getAssociatedURIResource()!=null?getAssociatedURIResource().toString():"-");
		values.put("active_time", String.valueOf(getCurrentAcitivtyRunningTime()));
		values.put("start_time", String.valueOf(getStartTime()));
		values.put("end_time", String.valueOf(getEndTime()));
		values.put("synced", false);
		return values;
		
	}
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the applicationIconURL
	 */
	public String getApplicationIconURL() {
		return applicationIconURL;
	}
	/**
	 * @param applicationIconURL the applicationIconURL to set
	 */
	public void setApplicationIconURL(String app_icon) {
		this.applicationIconURL = app_icon;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
