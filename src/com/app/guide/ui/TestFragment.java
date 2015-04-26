package com.app.guide.ui;

import com.app.guide.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_test, null);

		String text = getArguments().getString("title");
		((TextView) view.findViewById(R.id.textView1)).setText(text);
		return view;
	}

}
