package com.uob.websense;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.uob.websense.adapter.WebTrendsListAdapter;
import com.uob.websense.data_models.WebVistModel;
import com.uob.websense.support.Util;
import com.uob.websense.web_service_manager.WebSenseRestClient;

public class WebTrendsFragment extends Fragment {

	private WebTrendsListAdapter appListAdapter;
	ListView applist;
	ProgressBar loadingSpinner;
	ArrayList<WebVistModel> appList;
	
	public static WebTrendsFragment newInstance() {
		WebTrendsFragment fragment = new WebTrendsFragment();
		return fragment;
	}

	public WebTrendsFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    setHasOptionsMenu(true);
	    
		View rootView = inflater.inflate(R.layout.fragment_app_trends, container,
				false);
		
		applist = (ListView) rootView.findViewById(R.id.app_trends_list);
		loadingSpinner = (ProgressBar)rootView.findViewById(R.id.loading_spinner);
		
        applist.setVisibility(View.GONE);
        
        loadContent();
        
		return rootView;
	}

	private void loadContent() {
		WebSenseRestClient.get("web/trends", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

				Object response;
				try {
					response = Util.parseResponse(responseString);
					appList = new ArrayList<WebVistModel>();
					if (response instanceof JSONArray) {
						
						for(int i=0;i<((JSONArray)response).length();i++){
							JSONObject j = ((JSONArray) response).getJSONObject(i);
							WebVistModel app = new WebVistModel(j);
							appList.add(app);
						}

					}else{
						showAlert(getActivity().getApplicationContext().getString(R.string.server_error));
					}

				} catch (JSONException e) {
					showAlert(getActivity().getApplicationContext().getString(R.string.server_error));
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				crossfade();
				loadAdapter();
			}


			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				crossfade();
				showAlert(getActivity().getApplicationContext().getString(R.string.server_error));
				loadAdapter();
			}


		});
	}
	
	
	public void showAlert(String alertMessage){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(alertMessage);
		builder.setNegativeButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void loadAdapter() {
		
		if(appList!=null){
			appListAdapter  = new WebTrendsListAdapter(getActivity(),appList, getActivity().getApplicationContext());
			applist.setAdapter(appListAdapter);
			applist.setOnItemClickListener(new OnItemClickListener() {

	            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
	                    long arg3) {
	            	
	            	Uri uri = Uri.parse(appList.get(pos).getCompleteURL());
	            	Log.d("---->",uri+"-");
	    	        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    	        startActivity(intent);

	            }
	        });
		}
		
		
	}
	
	@SuppressLint("NewApi")
	private void crossfade() {

	    // Set the content view to 0% opacity but visible, so that it is visible
	    // (but fully transparent) during the animation.
		applist.setAlpha(0f);
		applist.setVisibility(View.VISIBLE);

	    // Animate the content view to 100% opacity, and clear any animation
	    // listener set on the view.
		applist.animate()
	            .alpha(1f)
	            .setDuration(2)
	            .setListener(null);

	    // Animate the loading view to 0% opacity. After the animation ends,
	    // set its visibility to GONE as an optimization step (it won't
	    // participate in layout passes, etc.)
	    loadingSpinner.animate()
	            .alpha(0f)
	            .setDuration(2)
	            .setListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                	loadingSpinner.setVisibility(View.GONE);
	                }
	            });
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			loadAdapter();
			applist.setAdapter(appListAdapter);
			crossfade();
			
		}
		return super.onOptionsItemSelected(item);
	}
}