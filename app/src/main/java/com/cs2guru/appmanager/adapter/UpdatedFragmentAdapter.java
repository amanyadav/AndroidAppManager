package com.cs2guru.appmanager.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cs2guru.appmanager.R;
import com.cs2guru.appmanager.database.AppManagerInfo;
import com.cs2guru.appmanager.database.DatabaseHandler;
import com.cs2guru.appmanager.interfaces.DataBaseChangeObserver;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdatedFragmentAdapter extends BaseAdapter implements
		DataBaseChangeObserver {
	Context context;
	List<AppManagerInfo> rowItems;
	DatabaseHandler db;

	public UpdatedFragmentAdapter(Context context) {
		this.context = context;
		db = DatabaseHandler.getInstance(context);
		DatabaseHandler.registerObserver(this);
		rowItems = new ArrayList<AppManagerInfo>();
		getRowList();

	}

	private void getRowList() {
		List<AppManagerInfo> temp = db.getAllAppInfo();
		rowItems.clear();
		for (AppManagerInfo info : temp)
			if (info.getCount() > 0)// only updated apps filtered
				rowItems.add(info);

	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
		TextView updatedCount;
		TextView lastUpdatedDate;
		TextView lastUpdatedText;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_single_item_updated, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.app_name);
			holder.updatedCount = (TextView) convertView
					.findViewById(R.id.updatedCount);
			holder.lastUpdatedText = (TextView) convertView
					.findViewById(R.id.dateStatus);
			holder.lastUpdatedDate = (TextView) convertView
					.findViewById(R.id.lastUpdatedDate);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.app_icons);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppManagerInfo rowItem = (AppManagerInfo) getItem(position);

		holder.txtTitle.setText(rowItem.getName());
		holder.updatedCount.setText("Updated " + rowItem.getCount()
				+ " time");
		holder.lastUpdatedText.setText("Last Updated");
		holder.lastUpdatedDate.setText(rowItem.getDate());
		holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
				rowItem.getImage(), 0, rowItem.getImage().length));

		return convertView;
	}

	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(getItem(position));
	}

	@Override
	public void dataChanged() {
		getRowList();

		if (context instanceof Activity) {
			Activity a = (Activity) context;
			a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					notifyDataSetChanged();
				}
			});
		}

	}
}