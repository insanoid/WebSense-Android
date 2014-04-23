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
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uob.websense.R;
import com.uob.websense.app_monitoring.AppMonitorUtil;
import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Util;

/**
 * Adapter for application usage list.
 * @author karthikeyaudupa
 *
 */
public class AppListAdapter  extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<AppUsageInformationModel> appDataArray;
	private static LayoutInflater inflater = null;
	private Context mContext;
	private double totalRunningTime;

	/**
	 * Constructor.
	 * @param mActivity activity name.
	 * @param appDataArray data file consiting all the information about the apps.
	 * @param _ctx
	 */
	public AppListAdapter(Activity _activty, ArrayList<AppUsageInformationModel> _data,Context _ctx) {
		mActivity = _activty;
		appDataArray = _data;
		inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = _ctx;
		totalRunningTime = AppMonitorUtil.getTotalForTasks(appDataArray);

	}

	/**
	 * Returns the count of the array.
	 */
	public int getCount() {
		return appDataArray.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if(convertView == null){
			vi = inflater.inflate(R.layout.app_info_list_item, null);
		}

		Util.fixCellFont(mContext, vi);

		final TextView title = (TextView)vi.findViewById(R.id.title);
		final TextView sub_title = (TextView)vi.findViewById(R.id.sub_title);
		final TextView acc_txt = (TextView)vi.findViewById(R.id.acc_txt);
		final ProgressBar progressBar = (ProgressBar)vi.findViewById(R.id.total_progress);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);

		AppUsageInformationModel app = new AppUsageInformationModel();
		app = appDataArray.get(position);

		title.setText(app.getApplicationName());
		sub_title.setText(Util.calculateTime(app.getCurrentAcitivtyRunningTime()/1000));
		final CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString(app.getEndTime());
		acc_txt.setText(relativeTimeSpan);

		int progress = (int)Math.round(((app.getCurrentAcitivtyRunningTime()/totalRunningTime * 100)));
		progressBar.setProgress(progress);
		try {
			Drawable icon;
			icon = mContext.getPackageManager().getApplicationIcon(app.getApplicationPackageName());
			thumb_image.setImageDrawable(icon);
		} catch (NameNotFoundException e) {

		}

		vi.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundColor(mContext.getResources().getColor(R.color.brand_green));
					title.setTextColor(mContext.getResources().getColor(R.color.white));
					sub_title.setTextColor(mContext.getResources().getColor(R.color.gray));
					acc_txt.setTextColor(mContext.getResources().getColor(R.color.white));
					progressBar.setProgressDrawable(mContext.getResources().getDrawable(
							R.drawable.apptheme_progress_horizontal_holo_light_hightlighted));

				} 
				else if (event.getAction() == MotionEvent.ACTION_UP ||
						event.getAction() == MotionEvent.ACTION_CANCEL) {

					title.setTextColor(mContext.getResources().getColor(R.color.black));
					sub_title.setTextColor(mContext.getResources().getColor(R.color.light_gray));
					v.setBackground(mContext.getResources().getDrawable(R.drawable.apptheme_list_selector_holo_light));
					acc_txt.setTextColor(mContext.getResources().getColor(R.color.dark_green));
					progressBar.setProgressDrawable(mContext.getResources().getDrawable(
							R.drawable.apptheme_progress_horizontal_holo_light));

				}

				return true;
			}
		});

		return vi;
	}


}