package com.cs2guru.appmanager.adapter;

import com.cs2guru.appmanager.fragments.InstalledFragment;
import com.cs2guru.appmanager.fragments.UninstalledFragment;
import com.cs2guru.appmanager.fragments.UpdatedFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new InstalledFragment();
		case 1:
			// Games fragment activity
			return new UpdatedFragment();
		case 2:
			// Movies fragment activity
			return new UninstalledFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}