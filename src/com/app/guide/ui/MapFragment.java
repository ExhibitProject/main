package com.app.guide.ui;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.app.guide.R;
import com.app.guide.adapter.MapExhibitAdapter;
import com.app.guide.bean.MapExhibitBean;
import com.app.guide.widget.MarkObject;
import com.app.guide.widget.MarkObject.MarkClickListener;
import com.app.guide.widget.MyMap;

public class MapFragment extends Fragment {

	private MyMap sceneMap;
	private Bitmap bitmap;
	private ListView mListView;
	private ToggleButton mToggleButton;
	private Button loactionButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_map, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		sceneMap = (MyMap) view.findViewById(R.id.map_sceneMap);
		mListView = (ListView) view.findViewById(R.id.map_list_exhibit);
		mToggleButton = (ToggleButton) view.findViewById(R.id.map_tog_list);
		loactionButton = (Button) view.findViewById(R.id.map_btn_location);
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
		ArrayList<MapExhibitBean> list = new ArrayList<MapExhibitBean>();
		for (int i = 0; i < 100; i++) {
			MapExhibitBean bean = new MapExhibitBean();
			bean.setAddress("A厅B展333");
			bean.setName("青花人物笔海");
			list.add(bean);
		}
		MapExhibitAdapter adapter = new MapExhibitAdapter(getActivity(), list);
		mListView.setAdapter(adapter);
		
		mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mListView.setVisibility(View.VISIBLE);
					
				}else {
					mListView.setVisibility(View.GONE);
				}
				sceneMap.setShowMark(isChecked);
			}
		});
		loactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sceneMap.adjust(0.5f, 0.9f, 0, 0);
				Log.w("Map", "click");
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sceneMap.onDestory();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
		sceneMap.setBitmap(bitmap);
	}

}
