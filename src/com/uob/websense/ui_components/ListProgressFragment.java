package com.uob.websense.ui_components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.uob.websense.R;
import com.uob.websense.support.Constants;

public class ListProgressFragment  extends Fragment {

	protected  View rootView;
	protected  ListView applist;
	protected  int navigationTabIndex;
	protected  ProgressBar loadingSpinner;
	
	public static ListProgressFragment newInstance() {
		ListProgressFragment fragment = new ListProgressFragment();
		return fragment;
	}

	public static ListProgressFragment newInstance(int _type) {
		ListProgressFragment fragment = new ListProgressFragment();
		fragment.navigationTabIndex = 0;
		return fragment;
	}

	public ListProgressFragment() {
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState, int layout) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		rootView = inflater.inflate(layout, container,false);
		loadingSpinner = (ProgressBar)rootView.findViewById(R.id.loading_spinner);
		applist = (ListView) rootView.findViewById(R.id.app_list);
		if(navigationTabIndex==0){
			final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setSelectedNavigationItem(0);
		}
		return rootView;
	}

	
	@SuppressLint("NewApi")
	protected void showListAnimated(View toShowControl, final View toHideControl) {

		toShowControl.setAlpha(0f);
		toShowControl.setVisibility(View.VISIBLE);
		toShowControl.animate()
		.alpha(1f)
		.setDuration(Constants.ANIMATION_DURATION)
		.setListener(null);

		toHideControl.animate()
		.alpha(0f)
		.setDuration(2)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				toHideControl.setVisibility(View.GONE);
			}
		});
	}

	@SuppressWarnings("unused")
	private void reloadAdapter() {}
	@SuppressWarnings("unused")
	private void loadRemoteContent() {}


}