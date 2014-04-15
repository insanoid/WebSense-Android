package com.uob.websense.ui;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uob.contextframework.support.LocationsHelper;
import com.uob.websense.R;
import com.uob.websense.adapter.AppTrendsListAdapter;
import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;
import com.uob.websense.ui_components.ListProgressFragment;
import com.uob.websense.web_service_manager.WebSenseRestClient;

public class AppTrendsFragment extends ListProgressFragment {

	private AppTrendsListAdapter appListAdapter;
	ArrayList<AppUsageInformationModel> appList;


	public static AppTrendsFragment newInstance() {
		AppTrendsFragment fragment = new AppTrendsFragment();
		return fragment;
	}

	public static AppTrendsFragment newInstance(int _type) {
		AppTrendsFragment fragment = new AppTrendsFragment();
		fragment.navigationTabIndex = _type;
		fragment.isLocalized = false;
		return fragment;
	}

	public static AppTrendsFragment newInstance(int _type, boolean _isLocalized) {
		AppTrendsFragment fragment = new AppTrendsFragment();
		fragment.navigationTabIndex = _type;
		fragment.isLocalized = _isLocalized;
		return fragment;
	}


	public AppTrendsFragment() {

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

		if(!isLocalized){
			if(navigationTabIndex==0){
				requestMethod = Constants.APP_TRENDS_DAILY;
			}else if(navigationTabIndex==1){
				requestMethod = Constants.APP_TRENDS_WEEKLY;
			}else if(navigationTabIndex==2){
				requestMethod = Constants.APP_TRENDS_MONTHLY;
			}else{
				requestMethod = Constants.APP_TRENDS_DAILY;
			}

		}else{
			if(navigationTabIndex==0){
				requestMethod = Constants.APP_NEARBY_DAILY;
			}else if(navigationTabIndex==1){
				requestMethod = Constants.APP_NEARBY_WEEKLY;
			}else if(navigationTabIndex==2){
				requestMethod = Constants.APP_NEARBY_MONTHLY;
			}else{
				requestMethod = Constants.APP_NEARBY_DAILY;
			}
		}

		Location currentLocation = LocationsHelper.getLatestLocation(getActivity());

		RequestParams params = new RequestParams();
		params.put("auth_token", (Util.getSecurePreference(getActivity().getApplicationContext(),Constants.AUTH_KEY_TOKEN)));
		params.put("limit","15");

		if(isLocalized){
			params.put("lat", String.valueOf(currentLocation.getLatitude()));
			params.put("lng",String.valueOf(currentLocation.getLongitude()));
		}

		WebSenseRestClient.get(requestMethod, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

				Object response;
				try {
					response = Util.parseResponse(responseString);
					appList = new ArrayList<AppUsageInformationModel>();

					if (response instanceof JSONArray) {

						for(int i=0;i<((JSONArray)response).length();i++){
							JSONObject j = ((JSONArray) response).getJSONObject(i);
							AppUsageInformationModel app = new AppUsageInformationModel(j);
							appList.add(app);
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
				if(getActivity()==null){
					return;
				}
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						reloadAdapter();

					}
				});

			}


			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				showListAnimated(applist, loadingSpinner);
				try{
					Util.showAlert(R.string.server_error,getActivity());
				}catch(Exception e){

				}
				showListAnimated(applist, loadingSpinner);
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						reloadAdapter();

					}
				});
			}


		});
	}

	private void reloadAdapter() {
		if(appList!=null){
			appListAdapter  = new AppTrendsListAdapter(getActivity(),appList, getActivity().getApplicationContext());
			applist.setAdapter(appListAdapter);
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