package com.app.guide.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.app.guide.R;
import com.app.guide.ui.DownloadListFragment.OnToggleListener;
import com.app.guide.widget.MyToggleButton;
import com.app.guide.widget.MyToggleButton.OnStateChangedListener;

public class DownloadActivity extends BaseActivity {

	private DownloadManageFragment downloadManageFragment;

	private DownloadListFragment downloadListFragment;

	private Fragment mCurrentFragment;

	private FragmentManager mFragmentManager;

	private MyToggleButton tbTitle;

	private ImageView ivBack;

	// 在onCreate()和onSaveInstanceState()之间传递参数值，以记录Activity上一次关闭前的状态
	private static final String Bundle_key = "state";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		initViews();
		// 恢复到上一次关闭状态
		if (savedInstanceState != null) {
			tbTitle.setCurrentState(savedInstanceState.getInt(Bundle_key));
		}

	}

	/**
	 * 初始化视图
	 */
	private void initViews() {

		tbTitle = (MyToggleButton) findViewById(R.id.toggle_btn);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish(); // 终止该Activity
			}
		});
		tbTitle.setStateChangedListener(new OnStateChangedListener() {

			@Override
			public void onSwitchOn() {
				beginTransaction(downloadListFragment, downloadManageFragment);
			}

			@Override
			public void onSwitchOff() {
				beginTransaction(downloadManageFragment, downloadListFragment);

			}
		});
		mFragmentManager = getSupportFragmentManager();
		downloadManageFragment = new DownloadManageFragment();
		downloadListFragment = new DownloadListFragment();
		downloadListFragment.setToggleListener(new OnToggleListener() {
			
			@Override
			public void onToggle() {
				Log.w("TAG", "点击了按钮");
				tbTitle.toggle();
			}
		});
		
		if (tbTitle.getCurrentState() == MyToggleButton.STATE_ON)
			init(downloadManageFragment);
		else
			init(downloadListFragment);
	}

	/**
	 *  
	 * @param fragment
	 */
	private void init(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.add(R.id.download_container, fragment);
		ft.commit();
	}

	private void beginTransaction(Fragment from, Fragment to) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		// setCustomAnimations产生Fragment切换时的动画效果
		if(to == downloadManageFragment){
			ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
		}else{
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		}
		if (to.isAdded()) {
			ft.hide(from).show(to).commit();
		} else {
			ft.hide(from).add(R.id.download_container, to).commit();
		}
	}

	/** 通过Bundle记录状态 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(Bundle_key, tbTitle.getCurrentState());
	}

	@Override
	protected boolean isFullScreen() {
		// TODO Auto-generated method stub
		return true;
	}

}
