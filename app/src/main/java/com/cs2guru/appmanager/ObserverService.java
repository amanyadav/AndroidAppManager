package com.cs2guru.appmanager;

import com.cs2guru.appmanager.reciever.PackageChangeReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ObserverService extends Service {

	String TAG = "ObserverService";
	PackageChangeReceiver mReceiver;
	IntentFilter filter;

	@Override
	public void onCreate() {
		Log.d(TAG, "oncreate");
		filter = new IntentFilter();
		filter.addDataScheme("package");
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
		mReceiver = new PackageChangeReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		// get an instance of the receiver in your service
		try {
			registerReceiver(mReceiver, filter);
		} catch (Exception e) {

		}
		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
