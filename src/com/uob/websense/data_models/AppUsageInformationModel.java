package com.uob.websense.data_models;

import java.net.URL;
import java.util.Date;

public class AppUsageInformationModel {

	String applicationPackageName, applicationName, contextualInformation;
	URL associatedURIResource;
	long currentAcitivtyRunningTime = 0;
	Date startTime;
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
	 * @return the contextualInformation
	 */
	public String getContextualInformation() {
		return contextualInformation;
	}
	/**
	 * @param contextualInformation the contextualInformation to set
	 */
	public void setContextualInformation(String contextualInformation) {
		this.contextualInformation = contextualInformation;
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
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	
}
