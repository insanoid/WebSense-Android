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

/**
 * Web trend view's adapter.
 * @author karthikeyaudupa
 *
 */
public class WebTrendsListAdapter  extends BaseAdapter {

	private Activity activity;
	private ArrayList<WebVistModel> webDataArray;
	private static LayoutInflater inflater = null;
	Context mContext;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.build();

	public WebTrendsListAdapter(Activity _activity, ArrayList<WebVistModel> _data,Context _ctx) {
		activity = _activity;
		webDataArray = _data;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = _ctx;

		if(imageLoader.isInited()==false){
			imageLoader.init(ImageLoaderConfiguration.createDefault(_ctx));
		}
	}



	public int getCount() {
		return webDataArray.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi=convertView;
		if(convertView==null){
			vi = inflater.inflate(R.layout.web_trends_list_item, null);
		}
		final TextView title = (TextView)vi.findViewById(R.id.title);
		final TextView sub_title = (TextView)vi.findViewById(R.id.sub_title);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);

		WebVistModel app = new WebVistModel();
		app = webDataArray.get(position);

		title.setText(app.getPageTitle());
		sub_title.setText(app.getHostURL());

		if(app.getContentImageURL()!=null){
			thumb_image.setVisibility(View.VISIBLE);
			thumb_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_web_site));
			try {
				if(app.getContentImageURL().length()>0){
					imageLoader.displayImage(app.getContentImageURL().getString(0), thumb_image,imageOptions);
				}
			} catch (JSONException e) {

			}
		}else{
			thumb_image.setVisibility(View.GONE);
		}	
		return vi;
	}


}