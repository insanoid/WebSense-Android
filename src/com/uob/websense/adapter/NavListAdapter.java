package com.uob.websense.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uob.websense.R;

public class NavListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	Context ctx;
	double totalRunningTime;


	public NavListAdapter(Activity a, ArrayList<HashMap<String, String>> d,Context _ctx) {
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = _ctx;


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
			vi = inflater.inflate(R.layout.nav_list_item, null);

		final TextView title = (TextView)vi.findViewById(R.id.title);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);

		HashMap<String, String> app = new HashMap<String, String>();
		app = data.get(position);

		title.setText(app.get("TITLE"));

		try {
			int iconId = Integer.parseInt(app.get("ICON"));
			Drawable icon;
			icon = ctx.getResources().getDrawable(iconId);
			thumb_image.setImageDrawable(icon);
		} catch (Exception e) {
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
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
					title.setTextColor(ctx.getResources().getColor(R.color.black));
					v.setBackground(ctx.getResources().getDrawable(R.drawable.apptheme_list_selector_holo_light));
				}

				return true;
			}
		});

		return vi;
	}


}