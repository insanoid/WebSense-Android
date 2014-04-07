package com.uob.websense;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.uob.websense.adapter.AppListAdapter;
import com.uob.websense.data_storage.SensorDataWriter;

public class AppUsageFragment extends ListProgressFragment {

	private AppListAdapter appListAdapter;

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
		rootView = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_app_usage);
		reloadAdapter();
		applist.setAdapter(appListAdapter);
		return rootView;
	}

	private void reloadAdapter() {
		
		SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(getActivity().getApplicationContext());
		appListAdapter  = new AppListAdapter(getActivity(),appDataProvider.getAppUsageInformation(), getActivity().getApplicationContext());
		appDataProvider.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			reloadAdapter();
			applist.setAdapter(appListAdapter);


		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume() {
		
		Log.d("---->","AAAAPPPPPPPPPPPPPPPPP");
		super.onResume();
		reloadAdapter();
		applist.setAdapter(appListAdapter);
	}

}