package com.app.guide.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.guide.R;

public class MenuFragment extends Fragment {

	private HomeClick homeClick;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_sliding_menu, null);
		((Button) view.findViewById(R.id.menu_btn_back))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (homeClick != null) {
							homeClick.home();
						}
					}
				});
		return view;
	}

	public void setHomeClick(HomeClick homeClick) {
		this.homeClick = homeClick;
	}

	public interface HomeClick {
		public void home();
	}

}
