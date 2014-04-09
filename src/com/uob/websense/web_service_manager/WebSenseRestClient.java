package com.uob.websense.web_service_manager;

import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uob.websense.support.Constants;

public class WebSenseRestClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return Constants.BASE_URL + relativeUrl;
	}

	public static void post(Context context, String url,
			StringEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {

		client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);

	}
}
