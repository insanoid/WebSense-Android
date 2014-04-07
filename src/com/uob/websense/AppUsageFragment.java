package com.uob.websense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uob.websense.adapter.AppListAdapter;
import com.uob.websense.data_storage.SensorDataWriter;

public class AppUsageFragment extends Fragment {

	private AppListAdapter appListAdapter;
	ListView applist;
	View rootView;
	public static AppUsageFragment newInstance() {
		AppUsageFragment fragment = new AppUsageFragment();
		return fragment;
	}

	public AppUsageFragment() {
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    setHasOptionsMenu(true);
	    
	    
		rootView = inflater.inflate(R.layout.fragment_app_usage, container,
				false);

		applist = (ListView) rootView.findViewById(R.id.app_usage_list);
		loadAdapter();
		applist.setAdapter(appListAdapter);
		return rootView;
	}

	private void loadAdapter() {
		SensorDataWriter.AppDataProvider appDataProvider = new SensorDataWriter.AppDataProvider(getActivity().getApplicationContext());
		appListAdapter  = new AppListAdapter(getActivity(),appDataProvider.getAppUsageInformation(), getActivity().getApplicationContext());
	
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
			
		}
		return super.onOptionsItemSelected(item);
	}
}