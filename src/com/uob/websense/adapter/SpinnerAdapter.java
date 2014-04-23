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

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uob.websense.support.Constants;

@SuppressWarnings("rawtypes")
/**
 * Custom spinner with special font.
 * @author karthikeyaudupa
 *
 */
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

		View v = convertView;
		if (v == null) {
			v = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}

		((TextView) v).setTypeface(currentFont);
		((TextView) v).setText("" + items[position]);
		return (TextView) v;
	} 

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
