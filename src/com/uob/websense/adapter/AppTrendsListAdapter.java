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

package com.uob.websense.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.uob.websense.R;
import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Util;

/**
 * Adapter for trends.
 * @author karthikeyaudupa
 *
 */
public class AppTrendsListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<AppUsageInformationModel> appDataArray;
	private static LayoutInflater inflater=null;
	private Context mContext;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.build();

	/**
	 * Constructor.
	 * @param _activity
	 * @param _data
	 * @param _ctx
	 */
	public AppTrendsListAdapter(Activity _activity, ArrayList<AppUsageInformationModel> _data,Context _ctx) {
		activity = _activity;
		appDataArray = _data;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = _ctx;

		if(imageLoader.isInited()==false){
			imageLoader.init(ImageLoaderConfiguration.createDefault(_ctx));
		}

	}

	public int getCount() {
		return appDataArray.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if(convertView == null){
			vi = inflater.inflate(R.layout.app_usage_list_item, null);
		}

		final TextView title = (TextView)vi.findViewById(R.id.title);
		final TextView sub_title = (TextView)vi.findViewById(R.id.sub_title);
		final ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
		final ImageButton download_image = (ImageButton)vi.findViewById(R.id.download_app);

		thumb_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.empty_icon));

		title.setText("");
		sub_title.setText("");

		AppUsageInformationModel app = new AppUsageInformationModel();
		app = appDataArray.get(position);
		final String appURL = app.getApplicationPackageName();
		try {
			ApplicationInfo currentAppInfo = mContext.getPackageManager().getApplicationInfo(app.getApplicationPackageName(),0);
			if(currentAppInfo!=null){
				download_image.setVisibility(View.GONE);
			}else{
				if(app.getApplicationName().length()>0)
					download_image.setVisibility(View.VISIBLE);
				else
					download_image.setVisibility(View.GONE);
			}
		} catch (NameNotFoundException e1) {
			if(app.getApplicationName().length()>0)
				download_image.setVisibility(View.VISIBLE);
			else
				download_image.setVisibility(View.GONE);
		}

		if(app.getApplicationName().length()<=0){
			//try getting local information.
			ApplicationInfo appInfo = null;
			try {
				appInfo = mContext.getPackageManager().getApplicationInfo(app.getApplicationPackageName(),0);
				if(appInfo != null) {

					title.setText(mContext.getPackageManager().getApplicationLabel(appInfo));

					try {
						thumb_image.setImageDrawable(mContext.getPackageManager().getApplicationIcon(app.getApplicationPackageName()));
						if(app.getApplicationPackageName().equalsIgnoreCase(mContext.getPackageName())){
							sub_title.setText("Monitoring Application");
						}else{
							sub_title.setText("Android System Application");
						}

					} catch (Exception e) {thumb_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.empty_icon));
					sub_title.setText("Android System Application");
					Util.loge("Issue with: " + app.getApplicationPackageName());
					}
				}
			} catch (NameNotFoundException e) {
				title.setText(app.getApplicationPackageName());
			}
		}else{
			title.setText(app.getApplicationName());
			sub_title.setText(app.getCategory()!=null?app.getCategory():"Android System App.");
			if(app.getApplicationIconURL().length()>0){
				imageLoader.displayImage(app.getApplicationIconURL(), thumb_image, imageOptions);
			}
		}


		vi.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundColor(mContext.getResources().getColor(R.color.brand_green));
					title.setTextColor(mContext.getResources().getColor(R.color.white));
					sub_title.setTextColor(mContext.getResources().getColor(R.color.gray));
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
					title.setTextColor(mContext.getResources().getColor(R.color.black));
					sub_title.setTextColor(mContext.getResources().getColor(R.color.light_gray));
					v.setBackground(mContext.getResources().getDrawable(R.drawable.apptheme_list_selector_holo_light));
				}

				return true;
			}
		});

		download_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(appURL.length()>0){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setData(Uri.parse("market://details?id="+appURL));
					mContext.startActivity(intent);
				}
			}
		});

		return vi;
	}


}