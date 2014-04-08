package com.uob.websense;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.uob.websense.adapter.AppListAdapter;
import com.uob.websense.data_storage.SensorDataWriter;

public class AppUsageFragment extends ListProgressFragment {

	private AppListAdapter appListAdapter;
	Handler handler;
	public static AppUsageFragment newInstance() {
		AppUsageFragment fragment = new AppUsageFragment();
		return fragment;
	}

	public static AppUsageFragment newInstance(int _type) {
		AppUsageFragment fragment = new AppUsageFragment();
		fragment.navigationTabIndex = _type;
		return fragment;
	}

	public AppUsageFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_app_trends);

		applist.setVisibility(View.GONE);
		Thread t1 = new Thread(new DoThread(getActivity()));
		t1.start();;
		return rootView;
	}


	class DoThread implements Runnable {

		private final Activity activity;

		DoThread(Activity _activity) {
			this.activity = _activity;
		}

		public void run() {
			reloadAdapter(activity);
		}
	}

	private void reloadAdapter(Activity activity) {

		SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(activity.getApplicationContext());
		appListAdapter  = new AppListAdapter(activity,appDataProvider.getAppUsageInformation(), activity.getApplicationContext());
		appDataProvider.close();

		activity.runOnUiThread(new Runnable() {
			public void run() {
				showListAnimated(applist, loadingSpinner);
				applist.setAdapter(appListAdapter);
			}
		});


	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			Thread t1 = new Thread(new DoThread(getActivity()));
			t1.start();
		}
		return super.onOptionsItemSelected(item);
	}


}