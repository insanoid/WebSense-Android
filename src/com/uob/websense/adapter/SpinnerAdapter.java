package com.uob.websense.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uob.websense.support.Constants;

@SuppressWarnings("rawtypes")
public class SpinnerAdapter extends ArrayAdapter {

	Typeface currentFont;
	private String[] items;
	private LayoutInflater mInflater;

	@SuppressWarnings("unchecked")
	public SpinnerAdapter(Context context, String[] _items) {
		super(context, android.R.layout.simple_list_item_1,
				android.R.id.text1, _items);
		mInflater = LayoutInflater.from(context);
		currentFont = Typeface.createFromAsset(context.getAssets(), Constants.FONT_BOLD);
		items = _items;
	}

	 public TextView getView(int position, View convertView, ViewGroup parent) {
		  View v = convertView;
			if (v == null) {
				v = mInflater.inflate(android.R.layout.simple_list_item_1, null);
			}
			
			((TextView) v).setTypeface(currentFont);
			((TextView) v).setText("" + items[position]);
	        return (TextView) v;
	    }

	    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
	        //TextView v = (TextView) super.getView(position, convertView, parent);
	        
	        View v = convertView;
			if (v == null) {
				v = mInflater.inflate(android.R.layout.simple_list_item_1, null);
			}
			
			((TextView) v).setTypeface(currentFont);
			((TextView) v).setText("" + items[position]);
	        return (TextView) v;
	    }
/*
	    
	public View getView(int position, View convertView, ViewGroup parent) {
		final ListContent holder;
		View v = convertView;
		if (v == null) {
			v = mInflater.inflate(android.R.layout.simple_dropdown_item_1line, null);
			holder = new ListContent();

			holder.name = (TextView) v.findViewById(android.R.id.text1);

			v.setTag(holder);
		} else {

			holder = (ListContent) v.getTag();
		}

		holder.name.setTypeface(currentFont);
		holder.name.setText("" + items[position]);

		return v;   
	}
	*/
//	  public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
//		  View v = convertView;
//		  final ListContent holder;
//			if (v == null) {
//				v = mInflater.inflate(android.R.layout.simple_dropdown_item_1line, null);
//				holder = new ListContent();
//
//				holder.name = (TextView) v.findViewById(android.R.id.text1);
//
//				v.setTag(holder);
//			} else {
//
//				holder = (ListContent) v.getTag();
//			}
//
//			holder.name.setTypeface(currentFont);
//			holder.name.setText("" + items[position]);
//
//			return v;   
//	    }

	  

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	class ListContent {

		TextView name;

	}

	public void setDropDownViewResource(int simpleSpinnerDropdownItem) {
		// TODO Auto-generated method stub
		
	}


}
