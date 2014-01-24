package com.wbc.wbcapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wbc.wbc.R;


public class UserContentsListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	
	int positionInVideoList = 0;

	static class ViewHolder {
		public TextView documentName;
	}

	public UserContentsListAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.list_item_single_content, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_single_content, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.documentName = (TextView) rowView.findViewById(R.id.documentTitle);
			rowView.setTag(viewHolder);
		}

		final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.documentName.setText(values.get(position));
		

		return rowView;
	}
}
