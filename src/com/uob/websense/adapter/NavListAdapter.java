package com.uob.websense.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uob.websense.R;
import com.uob.websense.support.Util;

public class NavListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	Context ctx;
	double totalRunningTime;
	OnItemClickListener mOnItemClickListener;

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

		Util.fixCellFont(ctx, vi);
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


		return vi;
	}
	

}