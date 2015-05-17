package com.app.guide.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.app.guide.bean.DownloadBean;
import com.app.guide.bean.OfflineExhibitBean;
import com.app.guide.sql.DatabaseContext;
import com.app.guide.sql.DownloadManagerHelper;
import com.app.guide.sql.OfflineBeanSqlHelper;
import com.j256.ormlite.dao.Dao;

public class OfflineDownloadHelper {

	private static final String TAG = OfflineDownloadHelper.class
			.getSimpleName();

	public static void downloadExhibit(Context context, String museumid) {

		DownloadManagerHelper helper = new DownloadManagerHelper(context);
		boolean count = false;
		try {
			count = helper.getDownloadDao().countOf() > 0;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (count) {
			Log.w(TAG, "have_download");
			return;
		}
		Log.w(TAG, "start_download");
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/Test/position.txt");
		BufferedReader br = null;
		OfflineBeanSqlHelper sqlHelper = new OfflineBeanSqlHelper(
				new DatabaseContext(context, "Test"), museumid + ".db");
		Dao<OfflineExhibitBean, Integer> exhibitDao;
		try {
			exhibitDao = sqlHelper.getOfflineExhibitDao();
			br = new BufferedReader(new FileReader(file));
			String readString;
			while ((readString = br.readLine()) != null) {
				String items[] = readString.split(",");// divided to an array
				float mapX = Integer.parseInt(items[0]) / 600.0f;
				float mapY = Integer.parseInt(items[1]) / 865.0f;
				OfflineExhibitBean bean = new OfflineExhibitBean();
				bean.setMapX(mapX);
				bean.setMapY(mapY);
				bean.setAddress("1展厅1号");
				bean.setName("古青花瓷");
				exhibitDao.createOrUpdate(bean);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DownloadBean bean = new DownloadBean();
		bean.setMuseumId("test");
		try {
			helper.getDownloadDao().createIfNotExists(bean);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
