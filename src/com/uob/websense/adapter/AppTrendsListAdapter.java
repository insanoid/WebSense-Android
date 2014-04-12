package com.uob.websense.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.uob.websense.R;
import com.uob.websense.app_monitoring.AppMonitorUtil;
import com.uob.websense.data_models.AppUsageInformationModel;
import com.uob.websense.support.Util;

public class AppTrendsListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<AppUsageInformationModel> data;
	private static LayoutInflater inflater=null;
	Context ctx;
	double totalRunningTime;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public AppTrendsListAdapter(Activity a, ArrayList<AppUsageInformationModel> d,Context _ctx) {
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = _ctx;

		if(imageLoader.isInited()==false){
			imageLoader.init(ImageLoaderConfiguration.createDefault(_ctx));
		}

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
			vi = inflater.inflate(R.layout.app_usage_list_item, null);

		final TextView title = (TextView)vi.findViewById(R.id.title);
		final TextView sub_title = (TextView)vi.findViewById(R.id.sub_title);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
		
		thumb_image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
		title.setText("");
		sub_title.setText("");
		AppUsageInformationModel app = new AppUsageInformationModel();
		app = data.get(position);

		
		
		if(app.getApplicationName().equalsIgnoreCase("")){
			ApplicationInfo appInfo =null;
			try {
				appInfo = ctx.getPackageManager().getApplicationInfo(app.getApplicationPackageName(),0);
			} catch (NameNotFoundException e) {
				title.setText(app.getApplicationPackageName());
				sub_title.setText("");
				e.printStackTrace();
			}

			if(appInfo!=null){
				title.setText(ctx.getPackageManager().getApplicationLabel(appInfo));
				sub_title.setText("");
				try {
					if(app.getApplicationPackageName().equalsIgnoreCase(ctx.getPackageName())){
						thumb_image.setImageDrawable(ctx.getPackageManager().getApplicationIcon(app.getApplicationPackageName()));
					}else{
						thumb_image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
					}
					Util.loge("image set for with: "+app.getApplicationPackageName());
					
				} catch (NameNotFoundException e) {
					Util.loge("Issue with: "+app.getApplicationPackageName());
					//e.printStackTrace();
				}
			}
		}else{

			title.setText(app.getApplicationName());
			sub_title.setText(app.getCategory()!=null?app.getCategory():"");
			//acc_txt.setText("Installed");

			//TODO: add loading icon here instead.
			thumb_image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
			imageLoader.displayImage(app.getApplicationIconURL(), thumb_image);
		}
		vi.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundColor(ctx.getResources().getColor(R.color.brand_green));
					title.setTextColor(ctx.getResources().getColor(R.color.white));
					sub_title.setTextColor(ctx.getResources().getColor(R.color.gray));
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
					title.setTextColor(ctx.getResources().getColor(R.color.black));
					sub_title.setTextColor(ctx.getResources().getColor(R.color.light_gray));
					v.setBackground(ctx.getResources().getDrawable(R.drawable.apptheme_list_selector_holo_light));
				}

				return true;
			}
		});

		return vi;
	}


}