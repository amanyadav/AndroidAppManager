package com.cs2guru.appmanager.fragments;

import com.cs2guru.appmanager.R;
import com.cs2guru.appmanager.adapter.InstalledFragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class InstalledFragment extends Fragment {
	GridView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_installed,
				container, false);
		lv = (GridView) rootView.findViewById(R.id.list);
		lv.setAdapter(new InstalledFragmentAdapter(getActivity()));

		return rootView;
	}
}