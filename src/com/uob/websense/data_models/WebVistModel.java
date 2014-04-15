package com.uob.websense.data_models;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebVistModel {

	String pageTitle, completeURL, hostURL;
	JSONArray contentImageURL;

	public WebVistModel(JSONObject j){
		try {
			pageTitle = j.has("title")?j.getString("title"):"";
			completeURL =j.has("_id")?j.getString("_id"):"";
			contentImageURL = j.has("content_image")?j.getJSONArray("content_image"):new JSONArray();
			hostURL = new URL(completeURL).getHost().toString();
			
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
	public JSONArray getContentImageURL() {
		return contentImageURL;
	}

	/**
	 * @param contentImageURL the contentImageURL to set
	 */
	public void setContentImageURL(JSONArray contentImageURL) {
		this.contentImageURL = contentImageURL;
	}
}
