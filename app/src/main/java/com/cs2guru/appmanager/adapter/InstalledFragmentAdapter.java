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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstalledFragmentAdapter extends BaseAdapter implements
		DataBaseChangeObserver {
	Context context;
	List<AppManagerInfo> rowItems;
	DatabaseHandler db;
	String TAG = "InstalledFragmentAdapter";

	public InstalledFragmentAdapter(Context context) {
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
			if (info.getCount() == 0)// only installed apps filtered
				rowItems.add(info);

	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.grid_single_item,
					null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.app_name);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.app_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppManagerInfo rowItem = (AppManagerInfo) getItem(position);

		holder.txtTitle.setText(rowItem.getName());
		byte[] blob = rowItem.getImage();
		holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
				blob, 0, blob.length));

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
		Log.d(TAG, "updated ");
		getRowList();
		// since it is called from different thread
		// need to call from UI thread
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