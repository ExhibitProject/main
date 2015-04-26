package com.yetwish.tourismguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.yetwish.libs.BeaconSearcher;
import com.yetwish.libs.BeaconSearcher.OnRangingListener;
import com.yetwish.libs.distance.DistanceUtils;
import com.yetwish.tourismguide.R;
import com.yetwish.tourismguide.view.HeaderLayout;
import com.yetwish.tourismguide.view.LyricView;

/**
 * 随身导游页面，包含与Beacon相关的操作，根据停留时间获取最近的beacon，并让相应的音频文件自动播放，并显示文字
 * @author yetwish
 * @date 2015-4-25
 */
public class FollowGuideFragment extends Fragment implements OnRangingListener{

	/**
	 * Store the layoutView of this fragment,by which we can get all other views of the fragment.
	 */
	private View rootView;
	
	/**
	 * store the header view of the fragment.
	 */
	private HeaderLayout header;
	
	/**
	 * the view which show the lyric.
	 */
	private LyricView mLyricView;
	
	/**
	 * the button that control playing the media.
	 */
	private Button btnStart;
	
	/**
	 * the image button that can select media resource
	 */
	private ImageView ivBack;
	
	/**
	 * button to set min stay time
	 */
	private Button btnStayTime;
	
	/**
	 * button to set broadcast distance
	 */
	private Button btnDistance;
	
	/**
	 * get min stay time from user
	 */
	private EditText etStayTime;
	
	/**
	 * get braodcast distance from user
	 */
	private EditText etDistance;
	
	/**
	 * store the resource of current playing
	 */
	private int current = 0;
	
	/**
	 * click listener 
	 */
	private MyClickListener listener;
	
	/**
	 * store BeaconSearcher instance, we use it to range beacon,and get the minBeacon from it.
	 */
	private BeaconSearcher beaconSearcher;
	
	/**
	 * defined several file path of media resources
	 */
	private static final String FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/TourismGuide/LyricSync/";
	private static final String[] MP3_NAMES = { "shanqiu.mp3", "libai.mp3","mote.mp3" };
	private static final String[] LYRIC_NAMES = { "shanqiu.lrc", "libai.lrc", "mote.lrc" };

	
	/**
	 * init beaconSearcher————get BeaconSearcher instance ,
	 *  set minStayTime ,openSearcher and setBeaconRangingListener
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		beaconSearcher = BeaconSearcher.getInstance(activity);
		//设置最小停留时间 
		BeaconSearcher.setMinStayTime(5);
		beaconSearcher.openSearcher();
		beaconSearcher.setBeaconRangingListener(this);
		
	}
	
	/**
	 * get RootView and init View .
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_follow_guide, null);
			initViews();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;

	}
	
	/**
	 * init view, find all other views by rootView.
	 */
	private void initViews() {
		if (rootView == null)
			return;
		// set header
		header = (HeaderLayout) rootView
				.findViewById(R.id.fragment_follow_guide_header);
		header.setTitle("青铜绝唱——莲鹤方壶");
		header.setSearchingVisible(false);

		// initViews
		mLyricView = (LyricView) rootView
				.findViewById(R.id.fragment_follow_guide_lyricview);
		btnStart = (Button) rootView
				.findViewById(R.id.fragment_follow_guide_btn_start);
		ivBack = (ImageView) rootView
				.findViewById(R.id.fragment_follow_guide_iv_back);

		btnDistance = (Button)rootView.findViewById(R.id.fragment_follow_guide_btn_set_distance);
		btnStayTime = (Button)rootView.findViewById(R.id.fragment_follow_guide_btn_set_staytime);
		etDistance = (EditText)rootView.findViewById(R.id.fragment_follow_guide_et_broadcast_distance);
		etStayTime = (EditText)rootView.findViewById(R.id.fragment_follow_guide_et_staytime);
		
		btnStart.setText("Start");
		listener = new MyClickListener();

		btnStart.setOnClickListener(listener);
		ivBack.setOnClickListener(listener);
		btnDistance.setOnClickListener(listener);
		btnStayTime.setOnClickListener(listener);
		
		btnStart.setClickable(false);
		ivBack.setClickable(false);
	}
	
	public void onRangeIn(Beacon beacon) {
		
		Toast.makeText(this.getActivity(), "RANGEIN"+beacon.getName(), Toast.LENGTH_SHORT).show();
		if(beacon.getName().contains("140")){
			mLyricView.prepare(FILE_PATH + MP3_NAMES[current], FILE_PATH
					+ LYRIC_NAMES[current]);
			
			mLyricView.start();
		}
		btnStart.setText("Pause");
		btnStart.setClickable(true);
		ivBack.setClickable(true);
	}
	
	public void onRangeOut(Beacon beacon) {
		Toast.makeText(this.getActivity(), "RANGEOUT"+beacon.getName(),Toast.LENGTH_SHORT).show();
		if(beacon.getName().contains("140")){
			mLyricView.stop();
		}
		btnStart.setText("Start");
		btnStart.setClickable(false);
		ivBack.setClickable(false);
	}
	
	/**
	 * when the fragment is on the top of the stack ,start ranging the beacon
	 */
	@Override
	public void onResume() {
		super.onResume();
		beaconSearcher.startRanging();
	}
	
	/**
	 * when the fragment is not on the top of the stack , stop ranging the beacon
	 */
	@Override
	public void onPause() {
		super.onPause();
		beaconSearcher.stopRanging();
	}
	
	/**
	 * destroy the fragment, close the beaconSearcher.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		beaconSearcher.closeSearcher();
	}
	
	/**
	 * when the fragment calling destroy view, release all the resource that all the view hold.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mLyricView.stop();
	}

	/**
	 * custom  clickListener
	 * @author yetwish
	 * @date 2015-4-25
	 */
	private class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.fragment_follow_guide_btn_start:
				// TODO Auto-generated method stub
				if (mLyricView.isPlaying()) {
					mLyricView.pause();
					btnStart.setText("Start");
				} else {
					mLyricView.start();
					btnStart.setText("Pause");
				}
				break;
			case R.id.fragment_follow_guide_iv_back:
				current++;
				if (mLyricView.isPlaying()) {
					mLyricView.pause();
					btnStart.setText("Start");
					btnStart.setClickable(false);
				}
				mLyricView.prepare(FILE_PATH + MP3_NAMES[current], FILE_PATH
						+ LYRIC_NAMES[current]);
				mLyricView.start();
				btnStart.setText("Pause");
				btnStart.setClickable(true);
				
				break;
			case R.id.fragment_follow_guide_btn_set_distance:
				String str = etDistance.getText().toString().trim();
				if("".equals(str)) break;
				double distance = Double.parseDouble(etDistance.getText().toString());
				DistanceUtils.setBroadcastDistance(distance);
				break;
			case R.id.fragment_follow_guide_btn_set_staytime:
				String str2 = etStayTime.getText().toString().trim();
				if("".equals(str2)) break;
				int stayTime = Integer.parseInt(etStayTime.getText().toString());
				BeaconSearcher.setMinStayTime(stayTime);
				break;
			}
		}
	}

}
