package com.app.guide.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.altbeacon.beacon.Beacon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.android.volley.toolbox.NetworkImageView;
import com.app.guide.AppContext;
import com.app.guide.AppContext.OnGuideModeChangedListener;
import com.app.guide.Constant;
import com.app.guide.R;
import com.app.guide.adapter.HorizontalScrollViewAdapter;
import com.app.guide.bean.Exhibit;
import com.app.guide.bean.ImageOption;
import com.app.guide.offline.GetBeanFromSql;
import com.app.guide.ui.HomeActivity.onBeaconSearcherListener;
import com.app.guide.utils.BitmapUtils;
import com.app.guide.widget.DialogManagerHelper;
import com.app.guide.widget.LyricView;
import com.app.guide.widget.LyricView.onProgressChangedListener;
import com.app.guide.widget.MyHorizontalScrollView;
import com.app.guide.widget.MyHorizontalScrollView.OnItemClickListener;
import com.app.guide.widget.MyHorizontalScrollView.OnLoadingMoreListener;
import com.app.guide.widget.TopBar;

import edu.xidian.NearestBeacon.NearestBeacon;

/**
 * 两种进入该界面的方式，底部导航进入 和选择某一展品进入
 * 随身导游页面，包含与Beacon相关的操作，根据停留时间获取最近的beacon，并让相应的音频文件自动播放，并显示文字
 * 
 * TODO 监听 电话 设置声音 TODO view visible gone 不会绘制 无法加载后面的 TODO 博物馆选择界面 对话框 TODO
 * 展品加载有些问题
 * 
 * @author yetwish
 * @date 2015-4-25
 * 
 */
public class FollowGuideFragment extends Fragment implements
		onBeaconSearcherListener, OnGuideModeChangedListener {

	private static final String TAG = FollowGuideFragment.class.getSimpleName();

	/**
	 * 两个常量，表示加载更多时失败/成功
	 */
	private static final int LOAD_ERROR = 0;
	private static final int LOAD_SUCCESS = 1;
	/**
	 * 上下文对象
	 */
	private Context mContext;

	private SweetAlertDialog pDialog;

	/**
	 * 存储屏幕像素信息
	 */
	private DisplayMetrics dm;

	/**
	 * 标题栏
	 */
	private TopBar fragHeader;

	/**
	 * the view which show the lyric.
	 */
	private LyricView mLyricView;

	/**
	 * the parent layout of lyric view
	 */
	private FrameLayout mLyricContainer;

	/**
	 * 歌词背景
	 */
	private NetworkImageView ivLyricBg;

	/**
	 * the height of the lyric view.
	 */
	private int mLyricHeight;

	/**
	 * the button that control playing the media.
	 */
	private ImageView ivStart;

	/**
	 * 上一个进度iv
	 */
	private ImageView ivPre;

	/**
	 * 下一个进度iv
	 */
	private ImageView ivNext;

	/**
	 * 播放进度条
	 */
	private ProgressBar pbMusic;

	/**
	 * 多角度图片tv
	 */
	private TextView tvTitlePics;

	/**
	 * 展品图片tv
	 */
	private TextView tvTitleExhibits;

	/**
	 * 多角度图片tv的展开iv
	 */
	private ImageView ivPicExpand;

	/**
	 * 展品图片tv的展开iv
	 */
	private ImageView ivExhibitExpand;

	/**
	 * 多角度图片gallery view
	 */
	private MyHorizontalScrollView mPicGallery;

	/**
	 * 展品图片gallery view
	 */
	private MyHorizontalScrollView mExhibitGallery;

	/**
	 * 存储多角度图片gallery的数据
	 */
	private List<ImageOption> mGalleryList = null;

	/**
	 * 存储展品图片数据
	 */
	private List<ImageOption> mExhibitImages = new ArrayList<ImageOption>();

	/**
	 * gallery adapter
	 */
	private HorizontalScrollViewAdapter mGalleryAdapter;

	/**
	 * exhibit Adapter;
	 */
	private HorizontalScrollViewAdapter mExhibitAdapter;

	/**
	 * 标识多角度图片列表是否展开
	 */
	private boolean picFlag = true;

	/**
	 * 标识展品图片列表是否展开
	 */
	private boolean exhibitFlag = false;

	/**
	 * 当前显示的展品
	 */
	private Exhibit mCurrentExhibit;

	// /**
	// * 已加载的最坐标的展品
	// */
	// private Exhibit lExhibit;
	//
	// /**
	// * 已加载的最右边的展品
	// */
	// private Exhibit rExhibit;

	// /**
	// * 最左展品坐标
	// */
	// private int left;
	//
	// /**
	// * 最右展品坐标
	// */
	// private int right;
	//
	// /**
	// * 判断是否在初始化数据
	// */
	// private boolean isInitData = false;

	private static final int MIN_PER_COUNT = 4;

	/**
	 * 附近的展品列表
	 */
	private List<Exhibit> mExhibitsList = new ArrayList<Exhibit>();

	/**
	 * 当前exhibit id
	 */
	private String mCurrentExhibitId;

	/**
	 * 当前博物馆的id
	 */
	private String mMuseumId;

	/**
	 * click listener
	 */
	private MyClickListener mClickListener;

	private boolean isFirst = true;

	/**
	 * 自动导航时 手动选择展品 优先播放完选择的展品，再返回自动导航
	 */
	private boolean isChosed = false;

	private static final int MSG_PROGRESS_CHANGED = 0x200;
	private static final int MSG_EXHIBIT_CHANGED = 0x201;
	private static final int MSG_MEDIA_PLAY_COMPLETE = 0x202;

	private DialogManagerHelper mDialogHelper;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_PROGRESS_CHANGED:
				int progress = msg.arg1;
				// 更新progressBar 和 图集
				pbMusic.setProgress(progress);
				// 匹配图集的时间
				int index = 0;
				for (int i = 0; i < mGalleryList.size(); i++) {
					if (progress >= mGalleryList.get(i).getStartTime()) {
						index = i;
					}
				}
				if (index != mPicGallery.getCurrentSelectedIndex() || isFirst) {
					// Log.w("TAG",
					// "SET START TIME" + " index " + index + " current "
					// + mPicGallery.getCurrentSelectedIndex());
					mPicGallery.setCurrentSelectedItem(true, index);
					isFirst = false;
				}
				break;
			case MSG_MEDIA_PLAY_COMPLETE:
				// TODO
				// 播放完，进入下一首
				// notifyExhibitChanged(rExhibit.getId());
				Log.w(TAG, "播放完重复播放" + isChosed + ", " + mCurrentExhibitId);
				if (isChosed)
					isChosed = false;
				else {
					// 循环
					String id = mCurrentExhibitId;
					mCurrentExhibitId = null;
					notifyExhibitChanged(id);
				}
				break;
			case MSG_EXHIBIT_CHANGED:
				// 若当前beacon改变了
				notifyExhibitChanged((String) msg.obj);
				break;

			}
		}
	};

	/**
	 * init beaconSearcher————get BeaconSearcher instance , set minStayTime
	 * ,openSearcher and setBeaconRangingListener
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		// 获取屏幕信息
		getScreenDisplayMetrix();
		// 获取intent
		mMuseumId = ((AppContext) getActivity().getApplication()).currentMuseumId;
		// 获取exhibit id
		mCurrentExhibitId = ((AppContext) getActivity().getApplication()).currentExhibitId;
		// 根据exhibit id 获取数据
		initData(mCurrentExhibitId);
		// initGalleryData();
		// 初始化clickListener
		mClickListener = new MyClickListener();

		mDialogHelper = new DialogManagerHelper(activity);

		HomeActivity.setBeaconLocateType(NearestBeacon.GET_EXHIBIT_BEACON);

		((AppContext) getActivity().getApplication())
				.addGuideModeChangedListener(this);
	}

	/**
	 * 获取屏幕信息
	 */
	private void getScreenDisplayMetrix() {
		dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
	}

	/**
	 * l:左边展品 r:右边展品 ll:左边的左边 rr:右边的右边
	 * 
	 * @param id
	 */
	private void initData(String id) {
		// 判断是否是从点击展品跳转过来的
		if (id != null) {
			// 开始手动模式
			// 根据传入的展品id，获取选中的展品信息
			try {
				mCurrentExhibit = GetBeanFromSql.getExhibit(mContext,
						mMuseumId, id);
				mCurrentExhibitId = id;
				// 更新appContext中的id
				((AppContext) getActivity().getApplication()).currentExhibitId = id;
				if (mCurrentExhibit != null) {
					// 获取展品图片数据
					mGalleryList = mCurrentExhibit.getImgList();
					// 重置两个列表
					mExhibitsList.clear();
					mExhibitImages.clear();
					// 获取左右展品
					mExhibitsList.add(mCurrentExhibit);
					mExhibitImages.add(new ImageOption(mCurrentExhibit
							.getIconUrl(), 0));
					// TODO load next and load pre
					// 保证一开始有4个或以上个展品在列表中
					loadPreExhibit(mCurrentExhibit);
					if (loadNextExhibit(mCurrentExhibit)) {
						for (int i = 0; i < 2; i++) {
							loadNextExhibit(mExhibitsList.get(mExhibitsList
									.size() - 1));
						}
					}
					if (mExhibitsList.size() < MIN_PER_COUNT) {// 后面没有了
						loadPreExhibit(mExhibitsList.get(0));
					}
					// 初始化标题
					if (mCurrentExhibit != null && fragHeader != null) {
						fragHeader.setTitle(mCurrentExhibit.getName());
					}
				}
				// 获取audio数据
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载某一展品的右展品
	 * 
	 * @param mExhibit
	 * @return
	 */
	private boolean loadNextExhibit(Exhibit mExhibit) {

		try {
			Exhibit exhibt = GetBeanFromSql.getExhibit(mContext, mMuseumId,
					mExhibit.getrExhibitBeanId());
			if (exhibt == null)
				return false;
			Log.w(TAG, "Load next " + "传进来的" + mExhibit.getName() + ","
					+ exhibt.getName());
			// 将展品加入展品列表
			mExhibitsList.add(exhibt);
			// 将展品图片加入展品图片列表
			mExhibitImages.add(new ImageOption(exhibt.getIconUrl(), 0));
			if (mExhibitAdapter != null) {
				mExhibitAdapter.notifyDataSetChanged();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 加载某一展品的左展品
	 * 
	 * @param mExhibit
	 * @return
	 */
	private boolean loadPreExhibit(Exhibit mExhibit) {
		try {
			Exhibit exhibt = GetBeanFromSql.getExhibit(mContext, mMuseumId,
					mExhibit.getlExhibitBeanId());
			if (exhibt == null)
				return false;
			Log.w(TAG,
					"Load pre " + "传进来的" + mExhibit.getName() + ","
							+ exhibt.getName());
			// 将展品加入展品列表
			mExhibitsList.add(0, exhibt);
			// 将展品图片加入展品图片列表
			mExhibitImages.add(0, new ImageOption(exhibt.getIconUrl(), 0));
			if (mExhibitAdapter != null) {
				mExhibitAdapter.notifyDataSetChanged();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_follow_guide, null);
		return view;

	}

	/**
	 * init views
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// init fragment header
		fragHeader = (TopBar) view.findViewById(R.id.frag_header_follow_guide);
		if (mCurrentExhibit != null)
			fragHeader.setTitle(mCurrentExhibit.getName());
		else
			fragHeader.setTitle("");
		fragHeader.setSearchingVisible(false);

		// find Views
		mLyricView = (LyricView) view
				.findViewById(R.id.frag_follow_guide_lyricview);

		ivLyricBg = (NetworkImageView) view
				.findViewById(R.id.frag_follow_guide_iv_bg);

		mLyricContainer = (FrameLayout) view
				.findViewById(R.id.frag_follow_guide_lyric_container);

		ivStart = (ImageView) view
				.findViewById(R.id.frag_follow_guide_iv_start);

		ivPre = (ImageView) view.findViewById(R.id.frag_follow_guide_iv_pre);

		ivNext = (ImageView) view.findViewById(R.id.frag_follow_guide_iv_next);

		pbMusic = (ProgressBar) view
				.findViewById(R.id.frag_follow_guide_progressbar);

		tvTitlePics = (TextView) view
				.findViewById(R.id.frag_follow_guide_tv_title_pics);

		tvTitleExhibits = (TextView) view
				.findViewById(R.id.frag_follow_guide_tv_title_exhibits);

		ivPicExpand = (ImageView) view
				.findViewById(R.id.frag_follow_guide_iv_gallery_expand);

		ivExhibitExpand = (ImageView) view
				.findViewById(R.id.frag_follow_guide_iv_exhibit_expand);

		mPicGallery = (MyHorizontalScrollView) view
				.findViewById(R.id.frag_follow_guide_pic_gallery_container);

		mExhibitGallery = (MyHorizontalScrollView) view
				.findViewById(R.id.frag_follow_guide_exhibit_gallery_container);

		ivStart.setOnClickListener(mClickListener);
		ivPre.setOnClickListener(mClickListener);
		ivNext.setOnClickListener(mClickListener);

		ivStart.setClickable(false);
		ivPre.setClickable(false);
		ivNext.setClickable(false);

		initLyricView();

		initGalleryViews();		
		// 如果当前已有展品信息，则加载gallery
		if (mCurrentExhibit != null) {
			notifyStartPlaying();
		}

	}

	/**
	 * when the fragment is on the top of the stack ,start ranging the beacon
	 */
	@Override
	public void onResume() {
		super.onResume();
		Log.w("LifeCycle", "resume ");
		// 从AppContext中获取全局exhibit id
		if (((AppContext) getActivity().getApplication()).isAutoGuide()
				&& !isChosed) {
			if (pDialog == null)
				pDialog = mDialogHelper.showSearchingProgressDialog();
			// 设置beacon监听
			HomeActivity.setBeaconSearcherListener(this);
		} else if (!((AppContext) getActivity().getApplication()).isAutoGuide()) {
			HomeActivity.setBeaconSearcherListener(null);
		}
		notifyExhibitChanged(((AppContext) getActivity().getApplication()).currentExhibitId);
	}

	private void notifyExhibitChanged(String exhibitId) {
		if (exhibitId != null && !exhibitId.equals(mCurrentExhibitId)) {
			if (((AppContext) getActivity().getApplication()).isAutoGuide()
					&& isChosed) {
				return;
			}
			if (mLyricView != null)
				mLyricView.stop();
			initData(exhibitId);
			if (mCurrentExhibit != null) {
				// 重新加载数据
				updateGalleryAdapter();
				updateExhibitAdapter();
				notifyStartPlaying();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.w("LifeCycle", "stop");
		((AppContext) getActivity().getApplication())
				.removeGuideModeListener(this);
		HomeActivity.setBeaconSearcherListener(null);
	}

	/**
	 * when the fragment is destroy , close the beaconSearcher,and release
	 * resources
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.w("LifeCycle", "destroy");
		if (mLyricView != null) {
			mLyricView.destroy();
		}
		if (mPicGallery != null) {
			mPicGallery.destroy();
		}
		if (mExhibitGallery != null) {
			mExhibitGallery.destroy();
		}
		clear();
	}

	private void clear() {
		mCurrentExhibit = null;
		mCurrentExhibitId = null;
		((AppContext) getActivity().getApplication()).currentExhibitId = null;
		if (mDialogHelper != null) {
			mDialogHelper = null;
		}
		if (pDialog != null)
			pDialog = null;
	}

	private void initLyricView() {
		// 设置监听
		mLyricView.setProgressChangedListener(new onProgressChangedListener() {

			@Override
			public void onProgressChanged(int progress) {
				Message msg = Message.obtain();
				msg.what = MSG_PROGRESS_CHANGED;
				msg.arg1 = progress;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onMediaPlayCompleted() {
				mHandler.sendEmptyMessage(MSG_MEDIA_PLAY_COMPLETE);
			}
		});

		// 设置lyric height
		mLyricContainer.post(new Runnable() {
			@Override
			public void run() {

				int titleHeight = (int) ((tvTitlePics.getHeight()
						+ tvTitleExhibits.getHeight() + 20) * dm.density);
				Log.w(TAG, titleHeight + " title");
				int playBarHeight = (int) (getResources().getDimension(
						R.dimen.progressbar_height)
						+ getResources().getDimension(R.dimen.icon_height) + 20 * dm.density);

				Log.w(TAG, playBarHeight + " play bar");

				int headerHeight = (int) getResources().getDimension(
						R.dimen.frag_header_height);

				mLyricHeight = dm.heightPixels - titleHeight - playBarHeight
						- headerHeight - 45;

				mLyricContainer.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, mLyricHeight));

				Log.w(TAG, mLyricHeight + " lyric");
			}
		});
	}

	private void initGalleryViews() {

		/**
		 * 处理与slidingMenu滚动冲突
		 */
		HomeActivity.getMenu().addIgnoredView(mPicGallery);

		HomeActivity.getMenu().addIgnoredView(mExhibitGallery);

		mPicGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position, boolean isByLyric) {
				ivLyricBg.setImageUrl(mGalleryList.get(position).getImgUrl(),
						BitmapUtils.getImageLoader(mContext));
				if (!isByLyric) {
					mLyricView.setCurrentPosition(mGalleryList.get(position)
							.getStartTime());
				}
			}

		});

		/**
		 * 设置tvPic可点击
		 */
		tvTitlePics.setOnClickListener(mClickListener);
		ivPicExpand.setOnClickListener(mClickListener);

		ivPicExpand.setImageResource(R.drawable.title_normal);
		mPicGallery.setVisibility(View.VISIBLE);

		if(mCurrentExhibit != null){
			updateGalleryAdapter();
		}
		
		mExhibitGallery.setShownInCenter();
		
		mExhibitGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position, boolean isByLyric) {
				// 切换展品
				notifyExhibitChanged(mExhibitsList.get(position).getId());
				isChosed = true;
			}
		});

		// TODO bug
		mExhibitGallery.seOnLoadingMoreListener(new OnLoadingMoreListener() {
			@Override
			public int onRightLoadingMore() {
				// if(shouldUpdate && mExhibitsList.size() >= MIN_PER_COUNT)
				// updateExhibitAdapter();
				String names = "";
				for (int i = 0; i < mExhibitsList.size(); i++) {
					names += mExhibitsList.get(i).getName() + "\t";
				}
				Log.w(TAG, names);
				if (loadNextExhibit(mExhibitsList.get(mExhibitsList.size() - 1))) {

					return LOAD_SUCCESS;
				} else
					return LOAD_ERROR;
			}

			@Override
			public int onLeftLoadingMore() {
				// if(shouldUpdate && mExhibitsList.size() >= MIN_PER_COUNT)
				// updateExhibitAdapter();
				String names = "";
//				for (int i = 0; i < mExhibitsList.size(); i++) {
//					names += mExhibitsList.get(i).getName() + "\t";
//				}
//				Log.w(TAG, names);
				if (loadPreExhibit(mExhibitsList.get(0))) {
					return LOAD_SUCCESS;
				} else
					return LOAD_ERROR;
			}
		});
		if(mCurrentExhibit != null){
			updateExhibitAdapter();
		}
		/**
		 * 设置tvExhibit 可点击
		 */
		tvTitleExhibits.setOnClickListener(mClickListener);
		ivExhibitExpand.setOnClickListener(mClickListener);
	}
	
	private void updateGalleryAdapter(){
		mGalleryAdapter = null;
		mGalleryAdapter = new HorizontalScrollViewAdapter(mContext,
				mGalleryList, R.layout.item_gallery);
		mPicGallery.setBackToBegin();
		mPicGallery.initData(mGalleryAdapter);
		mPicGallery.setCurrentSelectedItem(false, 0);
	}

	private void updateExhibitAdapter() {
		mExhibitAdapter = null;
		mExhibitAdapter = new HorizontalScrollViewAdapter(mContext,
				mExhibitImages, R.layout.item_gallery);
		mPicGallery.setBackToBegin();
		mExhibitGallery.initData(mExhibitAdapter);
		mExhibitGallery.setCurrentSelectedItem(false, mExhibitsList.indexOf(mCurrentExhibit));

	}

	private void changeCurrentExhibitById(String i) {
		if (mCurrentExhibitId.equals(i)) {
			Message msg = Message.obtain();
			msg.what = MSG_EXHIBIT_CHANGED;
			msg.obj = i;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 开始播放
	 */
	private void notifyStartPlaying() {
		if (mCurrentExhibit == null)
			return;
		if (mLyricView == null)
			return;
		Log.w(TAG, mCurrentExhibit.getTextUrl());
		// 准备音频文件
		mLyricView.prepare(Constant.getAudioDownloadPath(
				mCurrentExhibit.getAudioUrl(), mMuseumId), Constant
				.getLrcDownloadPath(mCurrentExhibit.getTextUrl(), mMuseumId));
		// 设置progressBar的最大值
		pbMusic.setMax(mLyricView.getDuration());
		// 开始播放
		mLyricView.start();
		ivStart.setImageResource(R.drawable.play_btn_pause);
		ivStart.setClickable(true);
		ivPre.setClickable(true);
		ivNext.setClickable(true);
	}

	/**
	 * custom clickListener
	 * 
	 * @author yetwish
	 * @date 2015-4-25
	 */
	private class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.frag_follow_guide_iv_start:
				// TODO Auto-generated method stub
				if (mLyricView.isPlaying()) {
					mLyricView.pause();
					ivStart.setImageResource(R.drawable.play_btn_play);
				} else {
					mLyricView.start();
					ivStart.setImageResource(R.drawable.play_btn_pause);
				}
				break;
			case R.id.frag_follow_guide_iv_pre:
				if (mCurrentExhibit != null) {
					int index = mPicGallery.getCurrentSelectedIndex();
					mPicGallery.setCurrentSelectedItem(false, index - 1);
				}
				break;
			case R.id.frag_follow_guide_iv_next:
				if (mCurrentExhibit != null) {
					int index = mPicGallery.getCurrentSelectedIndex();
					mPicGallery.setCurrentSelectedItem(false, index + 1);
				}
				break;
			case R.id.frag_follow_guide_tv_title_pics:
			case R.id.frag_follow_guide_iv_gallery_expand:
				// 展开多角度列表
				if (!picFlag) {
					// gvGalleryPics.setVisibility(View.VISIBLE);
					mPicGallery.setVisibility(View.VISIBLE);
					ivPicExpand.setImageResource(R.drawable.title_normal);
					picFlag = true;

				} else {
					mPicGallery.setVisibility(View.GONE);
					// gvGalleryPics.setVisibility(View.GONE);
					ivPicExpand.setImageResource(R.drawable.title_expanded);
					picFlag = false;
				}
				break;
			case R.id.frag_follow_guide_tv_title_exhibits:
			case R.id.frag_follow_guide_iv_exhibit_expand:
				if (!exhibitFlag) {
					mExhibitGallery.setVisibility(View.VISIBLE);
					// gvGalleryExhibits.setVisibility(View.VISIBLE);
					ivExhibitExpand.setImageResource(R.drawable.title_normal);
					exhibitFlag = true;
				} else {
					mExhibitGallery.setVisibility(View.GONE);
					// gvGalleryExhibits.setVisibility(View.GONE);
					ivExhibitExpand.setImageResource(R.drawable.title_expanded);
					exhibitFlag = false;
				}
				break;

			}
		}
	}

	/**
	 * 当传入的beacon与当前beacon不同时，则证明已经进入其他展品区域，则自动播放其他展品 ？还是弹出转换？
	 */
	@Override
	public void onNearestBeaconDiscovered(int type, Beacon beacon) {
		if (beacon == null)// 不做处理
			return;
		if (pDialog != null) {
			pDialog.dismiss();
		}
		// Log.w("BeaconSearcher", beacon.getId2().toString());
		// if (beacon.getId2().toString().contains("44")) {
		// changeCurrentExhibitById(1);
		// } else if (beacon.getId2().toString().contains("47")) {
		// changeCurrentExhibitById(2);
		// } else if (beacon.getId2().toString().contains("58")) {
		// changeCurrentExhibitById(3);
		// }
	}

	@Override
	public void onGuideModeChanged(boolean isAutoGuide) {
		if (isAutoGuide) {
			HomeActivity.getMenu().toggle();
			RadioButton rb = (RadioButton) HomeActivity.mRadioGroup
					.findViewById(R.id.home_tab_follow);
			rb.setChecked(true);
			// 进入自动导航
			if (pDialog != null)
				pDialog.show();
			else {
				pDialog = mDialogHelper.showSearchingProgressDialog();
			}
			// 设置beacon监听
			HomeActivity.setBeaconSearcherListener(this);
			// TODO
		}
	}

}
