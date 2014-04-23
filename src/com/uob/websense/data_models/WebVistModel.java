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

package com.uob.websense.data_models;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Model to store information about web usage.
 * @author karthikeyaudupa
 *
 */
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
