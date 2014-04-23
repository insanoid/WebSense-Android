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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.uob.websense.R;
import com.uob.websense.adapter.AppListAdapter;
import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.data_storage.SensorDataWriter;
import com.uob.websense.support.Constants;
import com.uob.websense.ui_components.ListProgressFragment;

/**
 * Application usage fragment.
 * @author karthikeyaudupa
 *
 */
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
		ArrayList<AppUsageInformationModel> appInfo = null;
		if(navigationTabIndex == 0){
			appInfo = appDataProvider.getAppUsageInformationForToday();
		}else if(navigationTabIndex == 1){
			appInfo = appDataProvider.getAppUsageInformationForNDays(Constants.DAY_WEEK);
		}else if(navigationTabIndex == 2){
			appInfo = appDataProvider.getAppUsageInformationForNDays(Constants.DAY_MONTH);
		}
		
		appListAdapter  = new AppListAdapter(activity,appInfo, activity.getApplicationContext());
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