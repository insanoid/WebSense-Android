package com.uob.websense;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.uob.websense.adapter.WebTrendsListAdapter;
import com.uob.websense.data_models.WebVistModel;
import com.uob.websense.support.Constants;
import com.uob.websense.support.Util;
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

	public WebTrendsFragment() {

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
		if(navigationTabIndex==0){
			requestMethod = Constants.WEB_TRENDS_DAILY;
		}else if(navigationTabIndex==0){
			requestMethod = Constants.WEB_TRENDS_WEEKLY;
		}else if(navigationTabIndex==0){
			requestMethod = Constants.WEB_TRENDS_MONTHLY;
		}else{
			requestMethod = Constants.WEB_TRENDS_DAILY;
		}

		WebSenseRestClient.get(requestMethod, null, new JsonHttpResponseHandler() {
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
			webListAdapter  = new WebTrendsListAdapter(getActivity(),webList, getActivity().getApplicationContext());
			applist.setAdapter(webListAdapter);

			applist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Uri uri = Uri.parse(webList.get(position).getCompleteURL());
					//Log.d("---->",uri+"-");
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
