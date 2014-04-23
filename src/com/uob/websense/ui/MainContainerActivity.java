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


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.uob.websense.R;
import com.uob.websense.adapter.SpinnerAdapter;
import com.uob.websense.app_monitoring.ContextBackgroundMonitor;
import com.uob.websense.ui_components.NavigationDrawerFragment;


public class MainContainerActivity 
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
		}else if(position==3){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					AppTrendsFragment.newInstance(0, true)).commit();
		}else if(position==4){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container,
					WebTrendsFragment.newInstance(0, true)).commit();
		}

		selectedTab = position;

	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		selectedTab = 0;
		setUpNavigationDrawer();
		setUpActionBarList();

		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				Intent i = new Intent();  
				i.setClass(getApplicationContext(), com.uob.websense.app_monitoring.AppUsageMonitor.class);
				startService(i);

				Intent i2 = new Intent();  
				i2.setClass(getApplicationContext(), com.uob.websense.app_monitoring.SyncManager.class);
				startService(i2);


				Intent serviceIntent = new Intent();  
				serviceIntent.setClass(getApplicationContext(), ContextBackgroundMonitor.class);
				startService(serviceIntent);
				
			}
		};
		
		r.run();

	}

	public void setUpNavigationDrawer() {

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	public void setUpActionBarList() {

		final ActionBar actionBar = getSupportActionBar();

		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		SpinnerAdapter mySpinnerArrayAdapter = new SpinnerAdapter(actionBar.getThemedContext(), new String[] {
			getString(R.string.sub_section_title1),
			getString(R.string.sub_section_title2),
			getString(R.string.sub_section_title3), });  
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
		try{
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
			
			else if(selectedTab==3){
				getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.container,
						AppTrendsFragment.newInstance(position, true)).commit();
			}
			
			else if(selectedTab==4){
				getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.container,
						WebTrendsFragment.newInstance(position, true)).commit();
			}
			

		}catch(Exception e){

		}
		return true;
	}

}
