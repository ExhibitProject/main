package com.app.guide.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.guide.R;
import com.app.guide.widget.MarkObject;
import com.app.guide.widget.MarkObject.MarkClickListener;
import com.app.guide.widget.MyMap;


public class MapFragment extends Fragment {

	private MyMap sceneMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_map, null);
		sceneMap = (MyMap) view.findViewById(R.id.map_sceneMap);
		Bitmap b = BitmapFactory
				.decodeResource(getResources(), R.drawable.test);
		sceneMap.setBitmap(b);
		MarkObject markObject = new MarkObject();
		markObject.setMapX(0.34f);
		markObject.setMapY(0.5f);
		markObject.setmBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_marka));
		markObject.setMarkListener(new MarkClickListener() {

			@Override
			public void onMarkClick(MarkObject object, int x, int y) {
				// TODO Auto-generated method stub
			}
		});

		sceneMap.addMark(markObject);
		sceneMap.setShowMark(true);
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sceneMap.onDestory();
	}
	
}
