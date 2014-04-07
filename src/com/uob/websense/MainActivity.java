package com.uob.websense;


import com.uob.websense.adapter.SpinnerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;


public class MainActivity 
extends ActionBarActivity implements
ActionBar.OnNavigationListener,
NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private int selectedTab = 0;

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		
		if(position==0){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					AppUsageFragment.newInstance(0)).commit();
		}else if(position==1){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					AppTrendsFragment.newInstance(0)).commit();
		}else if(position==2){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					WebTrendsFragment.newInstance(0)).commit();
		}

		selectedTab = position;
		
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		selectedTab = 0;
		Intent i = new Intent();  
		i.setClass(this, com.uob.websense.app_monitoring.AppUsageMonitor.class);
		startService(i);

		setUpNavigationDrawer();
		setUpActionBarList();


	}

	public void setUpNavigationDrawer() {

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	public void setUpActionBarList() {

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		SpinnerAdapter mySpinnerArrayAdapter = new SpinnerAdapter(actionBar.getThemedContext(), new String[] {
			getString(R.string.sub_section_title1),
			getString(R.string.sub_section_title2),
			getString(R.string.sub_section_title3), });  
		//mySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		actionBar.setListNavigationCallbacks(mySpinnerArrayAdapter, this);

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		restoreActionBar();
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {

		if(selectedTab==0){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					AppUsageFragment.newInstance(position)).commit();
		}else if(selectedTab==1){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					AppTrendsFragment.newInstance(position)).commit();
		}else if(selectedTab==2){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					WebTrendsFragment.newInstance(position)).commit();
		}
		return true;
	}

}
