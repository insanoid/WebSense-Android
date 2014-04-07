package com.uob.websense.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

public class AppListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<AppUsageInformationModel> data;
	private static LayoutInflater inflater=null;
	Context ctx;
	double totalRunningTime;


	public AppListAdapter(Activity a, ArrayList<AppUsageInformationModel> d,Context _ctx) {
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = _ctx;

		totalRunningTime = AppMonitorUtil.getTotalForTasks(data);

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.app_info_list_item, null);
		
		
		Util.fixCellFont(ctx, vi);
		
		final TextView title = (TextView)vi.findViewById(R.id.title);
		final TextView sub_title = (TextView)vi.findViewById(R.id.sub_title);
		final TextView acc_txt = (TextView)vi.findViewById(R.id.acc_txt);
		final ProgressBar progressBar = (ProgressBar)vi.findViewById(R.id.total_progress);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);

		AppUsageInformationModel app = new AppUsageInformationModel();
		app = data.get(position);

		title.setText(app.getApplicationName());
		Log.d("--->",app.getCurrentAcitivtyRunningTime()+"");
		sub_title.setText(Util.calculateTime(app.getCurrentAcitivtyRunningTime()/1000));
		final CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString(app.getEndTime());
		acc_txt.setText(relativeTimeSpan);

		int progress = (int)Math.round(((app.getCurrentAcitivtyRunningTime()/totalRunningTime * 100)));
		progressBar.setProgress(progress);
		try {
			Drawable icon;
			icon = ctx.getPackageManager().getApplicationIcon(app.getApplicationPackageName());
			thumb_image.setImageDrawable(icon);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}


		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		vi.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundColor(ctx.getResources().getColor(R.color.brand_green));
					title.setTextColor(ctx.getResources().getColor(R.color.white));
					sub_title.setTextColor(ctx.getResources().getColor(R.color.gray));
					acc_txt.setTextColor(ctx.getResources().getColor(R.color.white));
					progressBar.setProgressDrawable(ctx.getResources().getDrawable(R.drawable.apptheme_progress_horizontal_holo_light_hightlighted));
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
					title.setTextColor(ctx.getResources().getColor(R.color.black));
					sub_title.setTextColor(ctx.getResources().getColor(R.color.light_gray));
					v.setBackground(ctx.getResources().getDrawable(R.drawable.apptheme_list_selector_holo_light));
					acc_txt.setTextColor(ctx.getResources().getColor(R.color.dark_green));
					progressBar.setProgressDrawable(ctx.getResources().getDrawable(R.drawable.apptheme_progress_horizontal_holo_light));
					
				}

				return true;
			}
		});

		return vi;
	}


}