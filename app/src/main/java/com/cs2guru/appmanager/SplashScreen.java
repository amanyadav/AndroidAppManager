package com.cs2guru.appmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	private static int SPLASH_TIME_OUT = 2000;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash_screen);

		// populate db for first time while splash screen is shown
		populateSqliteDB();

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreen.this,
						MainActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	// we have to populate db only on first start of app
	private void populateSqliteDB() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!prefs.getBoolean("firstTime", false)) {// This function will run
													// only once
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("firstTime", true);
			editor.commit();
			// loading all applications only for once
			LoadInstalledApplications loader = new LoadInstalledApplications(
					this);
			loader.loadApplicationToDB();
		}

	}

}
