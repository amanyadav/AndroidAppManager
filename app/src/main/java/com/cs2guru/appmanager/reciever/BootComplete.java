package com.cs2guru.appmanager.reciever;

import com.cs2guru.appmanager.ObserverService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootComplete extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, ObserverService.class);
		context.startService(i);

	}

}
