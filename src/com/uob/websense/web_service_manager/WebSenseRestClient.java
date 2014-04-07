package com.uob.websense.web_service_manager;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebSenseRestClient {

		  private static final String BASE_URL = "http://54.186.15.10:3001/";

		  private static AsyncHttpClient client = new AsyncHttpClient();

		  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
			//  Log.d("WebSenseRestClient:",getAbsoluteUrl(url)+" - "+params!=null?params.toString():"");
		      client.get(getAbsoluteUrl(url), params, responseHandler);
		  }

		  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
			 // Log.d("WebSenseRestClient:",getAbsoluteUrl(url)+" - "+params!=null?params.toString():"");
		      client.post(getAbsoluteUrl(url), params, responseHandler);
		  }

		  private static String getAbsoluteUrl(String relativeUrl) {
		      return BASE_URL + relativeUrl;
		  }
		}
