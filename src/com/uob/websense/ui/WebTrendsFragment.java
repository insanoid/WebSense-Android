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

package com.uob.websense.ui;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uob.contextframework.support.LocationsHelper;
import com.uob.websense.R;
import com.uob.websense.adapter.WebTrendsListAdapter;
import com.uob.websense.data_models.WebVistModel;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;
import com.uob.websense.ui_components.ListProgressFragment;
import com.uob.websense.web_service_manager.WebSenseRestClient;

public class WebTrendsFragment extends ListProgressFragment {

	private WebTrendsListAdapter webListAdapter;
	ArrayList<WebVistModel> webList;


	public static WebTrendsFragment newInstance() {
		WebTrendsFragment fragment = new WebTrendsFragment();
		return fragment;
	}

	public static WebTrendsFragment newInstance(int _type) {
		WebTrendsFragment fragment = new WebTrendsFragment();
		fragment.navigationTabIndex = _type;
		return fragment;
	}
	
	public static WebTrendsFragment newInstance(int _type, boolean _isLocalized) {
		WebTrendsFragment fragment = WebTrendsFragment.newInstance(_type);
		fragment.isLocalized = _isLocalized;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_app_trends);		
		applist.setVisibility(View.GONE);
		loadRemoteContent();
		return rootView;
	}

	private void loadRemoteContent() {

		String requestMethod = null;
		
		if(isLocalized){
			if(navigationTabIndex==0){
				requestMethod = Constants.WEB_NEARBY_DAILY;
			}else if(navigationTabIndex==1){
				requestMethod = Constants.WEB_NEARBY_WEEKLY;
			}else if(navigationTabIndex==2){
				requestMethod = Constants.WEB_NEARBY_MONTHLY;
			}else{
				requestMethod = Constants.WEB_NEARBY_DAILY;
			}

		}else{
			if(navigationTabIndex==0){
				requestMethod = Constants.WEB_TRENDS_DAILY;
			}else if(navigationTabIndex==1){
				requestMethod = Constants.WEB_TRENDS_WEEKLY;
			}else if(navigationTabIndex==2){
				requestMethod = Constants.WEB_TRENDS_MONTHLY;
			}else{
				requestMethod = Constants.WEB_TRENDS_DAILY;
			}
		}

		Location currentLocation = LocationsHelper.getLatestLocation(getActivity());

		RequestParams params = new RequestParams();
		params.put("auth_token", (Util.getSecurePreference(getActivity().getApplicationContext(),Constants.AUTH_KEY_TOKEN)));
		params.put("limit","15");

		if(isLocalized){
			try{
			params.put("lat", String.valueOf(currentLocation.getLatitude()));
			params.put("lng",String.valueOf(currentLocation.getLongitude()));
			}catch(Exception e){
				
			}
		}

		
		
		WebSenseRestClient.get(requestMethod, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				
				Object response;
				try {
					response = Util.parseResponse(responseString);
					webList = new ArrayList<WebVistModel>();
					if (response instanceof JSONArray) {

						for(int i=0;i<((JSONArray)response).length();i++){
							JSONObject j = ((JSONArray) response).getJSONObject(i);
							WebVistModel app = new WebVistModel(j);
							webList.add(app);
						}

					}else{
						try{
							Util.showAlert(R.string.server_error,getActivity());
						}catch(Exception e){

						}
					}

				} catch (JSONException e) {
					try{
						Util.showAlert(R.string.server_error,getActivity());
					}catch(Exception e1){

					}
					e.printStackTrace();
				}
				showListAnimated(applist, loadingSpinner);
				reloadAdapter();
			}


			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				showListAnimated(applist, loadingSpinner);
				try{
					Util.showAlert(R.string.server_error,getActivity());
				}catch(Exception e){

				}
				reloadAdapter();
			}


		});
	}

	private void reloadAdapter() {
		
		if(webList!=null){
			if(getActivity()==null){
				return;
			}
			webListAdapter  = new WebTrendsListAdapter(getActivity(),webList, getActivity().getApplicationContext());
			applist.setAdapter(webListAdapter);

			applist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Uri uri = Uri.parse(webList.get(position).getCompleteURL());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			});

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			showListAnimated(loadingSpinner,applist);
			loadRemoteContent();
		}
		return super.onOptionsItemSelected(item);
	}

}
