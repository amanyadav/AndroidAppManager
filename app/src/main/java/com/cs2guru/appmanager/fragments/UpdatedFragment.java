package com.cs2guru.appmanager.fragments;

import com.cs2guru.appmanager.R;
import com.cs2guru.appmanager.adapter.InstalledFragmentAdapter;
import com.cs2guru.appmanager.adapter.UpdatedFragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class UpdatedFragment extends Fragment {
	ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_updated,
				container, false);
		lv = (ListView) rootView.findViewById(R.id.list);
		lv.setAdapter(new UpdatedFragmentAdapter(getActivity()));

		return rootView;
	}
}