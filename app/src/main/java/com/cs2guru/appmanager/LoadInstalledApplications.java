package com.cs2guru.appmanager;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.cs2guru.appmanager.database.AppManagerInfo;
import com.cs2guru.appmanager.database.DatabaseHandler;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class LoadInstalledApplications {
	String TAG = "LoadInstalledApplications";
	Activity context;
	private PackageManager packageManager = null;

	public LoadInstalledApplications(Activity activity) {
		context = activity;
		packageManager = context.getPackageManager();

	}

	public void loadApplicationToDB() {
		new LoadApplications().execute();
	}

	private class LoadApplications extends
			AsyncTask<Void, Void, List<ApplicationInfo>> {

		@Override
		protected List<ApplicationInfo> doInBackground(Void... params) {
			int flags = PackageManager.GET_META_DATA
					| PackageManager.GET_SHARED_LIBRARY_FILES
					| PackageManager.GET_UNINSTALLED_PACKAGES;
			List<ApplicationInfo> appList = packageManager
					.getInstalledApplications(flags);
			DatabaseHandler db = DatabaseHandler.getInstance(context);
			for (ApplicationInfo appInfo : appList) {
				if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
					// For this case only system apps will be added
				} else {
					byte[] imageInByte = null;
					try {
						Bitmap icon = Utils.drawableToBitmap(packageManager
								.getApplicationIcon(appInfo.packageName));
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
						imageInByte = stream.toByteArray();
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					db.addAppInfo(new AppManagerInfo(appInfo.loadLabel(
							packageManager).toString(), appInfo.packageName, 0,
							Utils.getCurrentDate(), imageInByte));

				}
			}

			return appList;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(List<ApplicationInfo> result) {

			super.onPostExecute(result);

		}

		@Override
		protected void onPreExecute() {
			Log.d(TAG, "onpre total installed apps :");
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
