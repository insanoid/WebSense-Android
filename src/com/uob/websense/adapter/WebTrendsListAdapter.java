package com.uob.websense.adapter;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.uob.websense.R;
import com.uob.websense.data_models.WebVistModel;

public class WebTrendsListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<WebVistModel> data;
	private static LayoutInflater inflater=null;
	Context ctx;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
    .cacheInMemory(true)
    .cacheOnDisc(true)
    .build();
	
	public WebTrendsListAdapter(Activity a, ArrayList<WebVistModel> d,Context _ctx) {
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = _ctx;
		
		if(imageLoader.isInited()==false){
		imageLoader.init(ImageLoaderConfiguration.createDefault(_ctx));
		}
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.web_trends_list_item, null);

		final TextView title = (TextView)vi.findViewById(R.id.title);
		final TextView sub_title = (TextView)vi.findViewById(R.id.sub_title);
		//final TextView acc_txt = (TextView)vi.findViewById(R.id.acc_txt);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);

		WebVistModel app = new WebVistModel();
		app = data.get(position);

		title.setText(app.getPageTitle());
		sub_title.setText(app.getHostURL());
		//acc_txt.setText("Installed");
		if(app.getContentImageURL()!=null){
			thumb_image.setVisibility(View.VISIBLE);
			thumb_image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_action_web_site));
			try {
				if(app.getContentImageURL().length()>0){
				imageLoader.displayImage(app.getContentImageURL().getString(0), thumb_image,imageOptions);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			thumb_image.setVisibility(View.GONE);
		}
	
		/*
		 vi.setOnTouchListener(new OnTouchListener() {
		 

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					
					v.setBackgroundColor(ctx.getResources().getColor(R.color.brand_green));
					title.setTextColor(ctx.getResources().getColor(R.color.white));
					sub_title.setTextColor(ctx.getResources().getColor(R.color.gray));
					//acc_txt.setTextColor(ctx.getResources().getColor(R.color.white));
				
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
					
					title.setTextColor(ctx.getResources().getColor(R.color.black));
					sub_title.setTextColor(ctx.getResources().getColor(R.color.light_gray));
					v.setBackground(ctx.getResources().getDrawable(R.drawable.apptheme_list_selector_holo_light));
					//acc_txt.setTextColor(ctx.getResources().getColor(R.color.brand_green));
				
				}

				return true;
			}
		});
*/
		
		
		return vi;
	}


}