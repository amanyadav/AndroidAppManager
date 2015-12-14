package com.cs2guru.appmanager.reciever;

import java.io.ByteArrayOutputStream;

import com.cs2guru.appmanager.R;
import com.cs2guru.appmanager.Utils;
import com.cs2guru.appmanager.database.AppManagerInfo;
import com.cs2guru.appmanager.database.DatabaseHandler;

import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PackageChangeReceiver extends BroadcastReceiver {
	String TAG = "PackageChangeReceiver";
	private PackageManager packageManager = null;

	@Override
	public void onReceive(Context ctx, Intent intent) {
		DatabaseHandler db = DatabaseHandler.getInstance(ctx);
		packageManager = ctx.getPackageManager();
		String pkg = intent.getDataString().split(":")[1];

		AppManagerInfo infoFromDb = db.getAppInfo(pkg);

		if (intent.getAction() == Intent.ACTION_PACKAGE_ADDED) {

			if (infoFromDb == null) {// Case when app not added to list then add
										// it to list
				PackageInfo packageInfo = null;
				ApplicationInfo appInfo = null;
				try {
					packageInfo = packageManager.getPackageInfo(pkg, 0);
					appInfo = packageInfo.applicationInfo;
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
						packageManager).toString(), pkg, 0, Utils
						.getCurrentDate(), imageInByte));

			} else if (infoFromDb.getCount() < 0) {// This is case when app was
				// added but uninstalled later so update the app info in db
				infoFromDb.setCount(0);
				infoFromDb.setDate(Utils.getCurrentDate());
				db.updateAppInfo(infoFromDb);
			}

		} else if (intent.getAction() == Intent.ACTION_PACKAGE_REPLACED) {
			Log.d(TAG, "ACTION_PACKAGE_CHANGED");
			infoFromDb.setCount(infoFromDb.getCount() + 1);
			infoFromDb.setDate(Utils.getCurrentDate());
			db.updateAppInfo(infoFromDb);
		} else if (intent.getAction() == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
			infoFromDb.setCount(-1);
			infoFromDb.setDate(Utils.getCurrentDate());
			db.updateAppInfo(infoFromDb);
		}
	}
}