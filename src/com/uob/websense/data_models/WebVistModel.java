package com.uob.websense.data_models;

import java.net.URL;

import org.json.JSONObject;

public class WebVistModel {

	String pageTitle, completeURL, hostURL, contentImageURL;

	public WebVistModel(JSONObject j){
		try {
			pageTitle = j.has("title")?j.getString("title"):"";
			completeURL =j.has("url")?j.getString("url"):"";
			hostURL = new URL(completeURL).getHost().toString();
			contentImageURL = j.has("content_image")?j.getString("content_image"):"";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public WebVistModel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the completeURL
	 */
	public String getCompleteURL() {
		return completeURL;
	}

	/**
	 * @param completeURL the completeURL to set
	 */
	public void setCompleteURL(String completeURL) {
		this.completeURL = completeURL;
	}

	/**
	 * @return the hostURL
	 */
	public String getHostURL() {
		return hostURL;
	}

	/**
	 * @param hostURL the hostURL to set
	 */
	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	/**
	 * @return the contentImageURL
	 */
	public String getContentImageURL() {
		return contentImageURL;
	}

	/**
	 * @param contentImageURL the contentImageURL to set
	 */
	public void setContentImageURL(String contentImageURL) {
		this.contentImageURL = contentImageURL;
	}
}
