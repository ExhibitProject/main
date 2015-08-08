package com.app.guide.offline;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.guide.Constant;
import com.app.guide.bean.MuseumBean;
import com.app.guide.download.DownloadBean;
import com.app.guide.download.DownloadInfo;
import com.app.guide.sql.DatabaseContext;
import com.app.guide.sql.DownloadManagerHelper;
import com.app.guide.utils.FastJsonArrayRequest;
import com.app.guide.utils.FileUtils;
import com.j256.ormlite.dao.Dao;

/**
 * 
 * TODO 给代码加注释， 
 * 展品列表view，  
 * download Activity  待测试
 * 地图界面  从专题导航 转入 时  数据有些问题。
 * 归并bean 管理获取bean的方式 ， 
 * 音乐播放器，
 * 完成后期工作 见文档。
 * 下载数据包辅助类，从服务器获取bean类，数据库中写入数据
 * 
 * @author joe_c
 * 
 */
public class OfflineDownloadHelper {

	private static final String TAG = OfflineDownloadHelper.class
			.getSimpleName();
	// TODO

	private Context mContext;

	private String museumId;

	private OnFinishedListener onFinishedListener;
	private RequestQueue mQueue;
	private ErrorListener mErrorListener;

	private DownloadBean downloadBean;
	private List<DownloadInfo> infoList;
	private Set<String> mSet;

	public OfflineDownloadHelper(Context context, String museumId) {
		super();
		this.mContext = context;
		this.museumId = museumId;
		downloadBean = new DownloadBean();
		downloadBean.setMuseumId(museumId);
		downloadBean.setName("首都博物馆");
		infoList = new ArrayList<DownloadInfo>();
		mSet = new HashSet<String>();
		mQueue = Volley.newRequestQueue(context);
		mErrorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (onFinishedListener != null) {
					onFinishedListener.onFailed(error.getMessage());
				}
			}
		};
	}

	/**
	 * 下载博物馆中的展品信息
	 * 
	 * @param context
	 * @param museumid
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void downloadExhibit(final Context context, final String museumid)
			throws SQLException, NumberFormatException, IOException {
//		String url = Constant.HOST_HEAD
//				+ "/a/api/exhibit/treeData?musumId=" + museumId;
		String url = "http://182.92.82.70/a/api/exhibit/treeData?museum.id="+museumid;
		FastJsonArrayRequest<OfflineExhibitBean> request = new FastJsonArrayRequest<OfflineExhibitBean>(
				url, OfflineExhibitBean.class,
				new Response.Listener<List<OfflineExhibitBean>>() {

					@Override
					public void onResponse(List<OfflineExhibitBean> response) {
						// TODO Auto-generated method stub
						OfflineBeanSqlHelper helper = new OfflineBeanSqlHelper(
								context, museumid + ".db");
						Dao<OfflineExhibitBean, String> exhibitDao;
						try {
							exhibitDao = helper.getOfflineExhibitDao();
							for (OfflineExhibitBean bean : response) {
								// addAudioDownloadInfo(bean.getAudiourl());
								Log.w(TAG, bean.getId());
//								if (!bean.getImgsurl().contains(
//										bean.getIconurl())) {
//									// addImageDownloadInfo(bean.getIconurl());
//								}
								// TODO 处理imgsurl
//								String imgOptions[] = bean.getImgsurl().split(
//										",");
//								for (int i = 0; i < imgOptions.length; i++) {
//									// Log.w("Volley",imgOptions[i]);
//									String url = (imgOptions[i].split("\\*"))[0];
//									// TODO add image info
//									// addImageDownloadInfo(url);
//								}
								// updateDownloadBean(bean.getFilesize());
								// addLrcDownloadInfo(bean.getTexturl());
								exhibitDao.createIfNotExists(bean);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, mErrorListener);
		mQueue.add(request);
	}

	/**
	 * 下载博物馆下的地图信息
	 * 
	 * @param context
	 * @param museumId
	 * @throws SQLException
	 */
	private void downloadMap(final Context context, final String museumId)
			throws SQLException {
//		String url = Constant.HOST_HEAD
//				+ "/a/api/museumMap/treeData?musumId=" + museumId;
		String url = "http://182.92.82.70/a/api/museumMap/treeData?museum.id="+museumId;
		FastJsonArrayRequest<OfflineMapBean> request = new FastJsonArrayRequest<OfflineMapBean>(
				url, OfflineMapBean.class,
				new Response.Listener<List<OfflineMapBean>>() {

					@Override
					public void onResponse(List<OfflineMapBean> response) {
						// TODO Auto-generated method stub
						OfflineBeanSqlHelper helper = new OfflineBeanSqlHelper(
								context, museumId + ".db");
						Dao<OfflineMapBean, String> mapDao;
						try {
							mapDao = helper.getOfflineMapDao();
							for (OfflineMapBean bean : response) {
								// addImageDownloadInfo(bean.getImgurl());
								// updateDownloadBean(bean.getFilesize());
								mapDao.createIfNotExists(bean);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, mErrorListener);
		mQueue.add(request);
	}

	/**
	 * 下载博物馆数据
	 * 
	 * @param context
	 * @param museumId
	 */
	private void downloadMuseum(final Context context, final String museumId) {
//		String url = Constant.HOST_HEAD
//				+ "/a/api/museum/treeData?id=" + museumId;
		String url = "http://182.92.82.70/a/api/museum/treeData?id="+museumId;
		FastJsonArrayRequest<OfflineMuseumBean> jsonRequest = new FastJsonArrayRequest<OfflineMuseumBean>(
				url, OfflineMuseumBean.class,
				new Response.Listener<List<OfflineMuseumBean>>() {

					@Override
					public void onResponse(List<OfflineMuseumBean> response) {
						// TODO Auto-generated method stub
						if(response.size() == 0)
							return ;
						OfflineMuseumBean museumBean = response.get(0);
						OfflineBeanSqlHelper helper = new OfflineBeanSqlHelper(
								context, museumId + ".db");
						museumBean.setId(museumId);
						try {
							Dao<OfflineMuseumBean, String> museumDao = helper
									.getOfflineMuseumDao();
							museumDao.createOrUpdate(museumBean);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// // TODO 路径问题 相对路径和绝对路径
						// if (!museumBean.getImgurl().contains(
						// museumBean.getIconurl())) {
						// // addImageDownloadInfo(museumBean.getIconurl());
						// }
						// String imgsurl[] = museumBean.getImgurl().split(",");
						// for (int i = 0; i < imgsurl.length; i++) {
						// // TODO add image
						// // addImageDownloadInfo(imgsurl[i]);
						// }
						// 更新downloadedBean
						// updateDownloadBean(bean.getFilesize());
						MuseumBean bean = new MuseumBean();
						bean.setAddress(museumBean.getAddress());
						// TODO
						bean.setIconUrl(Constant.getImageDownloadPath(
								Constant.HOST_HEAD + museumBean.getIconurl(),
								museumId));
						bean.setLongitudX(museumBean.getLongtiudex());
						bean.setLongitudY(museumBean.getLongtiudey());
						bean.setName(museumBean.getName());
						bean.setOpentime(museumBean.getOpentime());
						bean.setOpen(true);
						bean.setVersion(museumBean.getVersion());
						bean.setMuseumId(museumId);
						DownloadManagerHelper helper1 = new DownloadManagerHelper(
								mContext);
						try {
							helper1.getDownloadedDao().createOrUpdate(bean);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, mErrorListener);
		mQueue.add(jsonRequest);
	}

	/**
	 * 下载博物馆信标设备信息
	 * 
	 * @param context
	 * @param museumId
	 */
	private void downloadBeacon(final Context context, final String museumId) {
//		String url = Constant.HOST_HEAD
//				+ "/a/api/beacon/treeData?musumId=" + museumId;
		String url = "http://182.92.82.70/a/api/beacon/treeData?museum.id="+museumId;
		FastJsonArrayRequest<OfflineBeaconBean> request = new FastJsonArrayRequest<OfflineBeaconBean>(
				url, OfflineBeaconBean.class,
				new Response.Listener<List<OfflineBeaconBean>>() {

					@Override
					public void onResponse(List<OfflineBeaconBean> response) {
						// TODO Auto-generated method stub
						OfflineBeanSqlHelper helper = new OfflineBeanSqlHelper(
								context, museumId + ".db");
						Dao<OfflineBeaconBean, String> beaconDao;
						try {
							beaconDao = helper.getOfflineBeaconDao();
							for (OfflineBeaconBean bean : response) {
								beaconDao.createOrUpdate(bean);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, mErrorListener);
		mQueue.add(request);
	}

	/**
	 * 下载博物馆标签信息
	 * 
	 * @param context
	 * @param museumId
	 * @throws SQLException
	 */
	private void downloadLabel(final Context context, final String museumId)
			throws SQLException {

//		String url = Constant.HOST_HEAD
//				+ "/a/api/labels/treeData?musumId=" + museumId;
		String url  = "http://182.92.82.70/a/api/labels/treeData?museum.id="+museumId;
		FastJsonArrayRequest<OfflineLabelBean> request = new FastJsonArrayRequest<OfflineLabelBean>(
				url, OfflineLabelBean.class,
				new Response.Listener<List<OfflineLabelBean>>() {

					@Override
					public void onResponse(List<OfflineLabelBean> response) {
						// TODO Auto-generated method stub
						OfflineBeanSqlHelper helper = new OfflineBeanSqlHelper(
								context, museumId + ".db");
						Dao<OfflineLabelBean, String> labelDao;
						try {
							labelDao = helper.getOfflineLabelDao();
							for (OfflineLabelBean bean : response) {
								Log.w(TAG, bean.toString());
								labelDao.createOrUpdate(bean);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, mErrorListener);
		mQueue.add(request);
	}

	/**
	 * 下载博物馆下所有的文件
	 * 
	 */
	private void downloadFiles() {

//		String url = Constant.HOST_HEAD + "/a/api/assets/download?id="
//				+ museumId;
		String url = "http://182.92.82.70/a/api/assets/download?id="+museumId;
		JsonObjectRequest request = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							// 获取下载大小
							String size = response.getString("size");
							// 设置总的下载大小 (字节)
							downloadBean.setTotal(Long.valueOf(size));
							// 获取url
							JSONArray array = response.getJSONArray("url");
							for (int i = 0; i < array.length(); i++) {
								String url = array.getString(i);
								Log.w(TAG, url.toString());
								// 获取合法的下载地址
								url = Constant.getURL(url);
								// 根据url的不同，创建不同的下载任务。
								if (url.contains("img")) {
									addImageDownloadInfo(url);
								} else if (url.contains("audio")) {
									addAudioDownloadInfo(url);
								} else if (url.contains("lrc")) {
									addLrcDownloadInfo(url);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finished();
					}
				}, mErrorListener);
		mQueue.add(request);
	}

	/**
	 * 下载离线数据
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void download() throws SQLException, NumberFormatException,
			IOException {

		FileUtils.deleteDirectory(Constant.FLODER + museumId);
		Context dContext = new DatabaseContext(mContext, Constant.FLODER_NAME
				+ museumId);
		downloadExhibit(dContext, museumId);
		downloadMap(dContext, museumId);
		downloadLabel(dContext, museumId);
		downloadMuseum(dContext, museumId);
		downloadBeacon(dContext, museumId);
		downloadFiles();
	}

	/**
	 * 数据插入数据库后调用该方法，在回调接口中调用DownloadClient的download方法开始下载
	 */
	private void finished() {
		Log.d(TAG, "start download files!");
		if (onFinishedListener != null) {
			onFinishedListener.onSuccess(infoList, downloadBean);
		}
	}

	/**
	 * 添加一个歌词文件下载任务
	 */
	private void addLrcDownloadInfo(String url) {
		if (url == null || url.equals(""))
			return;
		if (mSet.contains(url))
			return;
		if (!url.contains("/"))
			return;
		mSet.add(url);
		DownloadInfo info = new DownloadInfo();
		info.setMuseumId(museumId);
		info.setUrl(url);
		info.setTarget(Constant.getLrcDownloadPath(url, museumId));
		infoList.add(info);
	}

	/**
	 * 添加一个音频文件下载任务
	 * 
	 * @param url
	 */
	private void addAudioDownloadInfo(String url) {
		if (url == null || url.equals("")) {
			return;
		}
		if (mSet.contains(url)) {
			return;
		}
		mSet.add(url);
		DownloadInfo info = new DownloadInfo();
		info.setMuseumId(museumId);
		info.setUrl(url);
		info.setTarget(Constant.getAudioDownloadPath(url, museumId));
		infoList.add(info);
	}

	/**
	 * 添加一个图片下载任务
	 * 
	 * @param url
	 */
	private void addImageDownloadInfo(String url) {
		if (url == null || url.equals("")) {
			return;
		}
		if (mSet.contains(url)) {
			return;
		}
		mSet.add(url);
		DownloadInfo info = new DownloadInfo();
		info.setMuseumId(museumId);
		info.setUrl(url);
		info.setTarget(Constant.getImageDownloadPath(url, museumId));
		infoList.add(info);
	}

//	/**
//	 * 更新DownloadBean的总长度
//	 * 
//	 * @param fileSize
//	 */
//	private void updateDownloadBean(long fileSize) {
//		downloadBean.setTotal(downloadBean.getTotal() + fileSize);
//	}

	public OnFinishedListener getOnFinishedListener() {
		return onFinishedListener;
	}

	public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
		this.onFinishedListener = onFinishedListener;
	}

	public interface OnFinishedListener {
		
		public void onSuccess(List<DownloadInfo> list, DownloadBean downloadBean);

		public void onFailed(String msg);
	}

}
