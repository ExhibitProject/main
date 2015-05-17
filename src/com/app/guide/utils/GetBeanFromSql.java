package com.app.guide.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.app.guide.bean.MapExhibitBean;
import com.app.guide.bean.OfflineExhibitBean;
import com.app.guide.sql.DatabaseContext;
import com.app.guide.sql.OfflineBeanSqlHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class GetBeanFromSql {
	private static final String TAG = GetBeanFromSql.class.getSimpleName();

	public static List<MapExhibitBean> getMapExhibit(Context context)
			throws SQLException {
		List<MapExhibitBean> list = new ArrayList<MapExhibitBean>();

		OfflineBeanSqlHelper helper = new OfflineBeanSqlHelper(
				new DatabaseContext(context, "Test"), "test.db");
		Dao<OfflineExhibitBean, Integer> oDao = helper.getOfflineExhibitDao();
		QueryBuilder<OfflineExhibitBean, Integer> builder = oDao.queryBuilder();
		builder.offset(10);
		builder.limit(10);
		List<OfflineExhibitBean> offlineExhibitBeans =builder.query();
		Log.w(TAG, "" + offlineExhibitBeans.size());
		for (OfflineExhibitBean offlineExhibitBean : offlineExhibitBeans) {
			MapExhibitBean bean = new MapExhibitBean();
			bean.setAddress(offlineExhibitBean.getAddress());
			bean.setName(offlineExhibitBean.getName());
			bean.setMapX(offlineExhibitBean.getMapX());
			bean.setMapY(offlineExhibitBean.getMapY());
			list.add(bean);
			Log.w(TAG, "add-once");
		}
		offlineExhibitBeans.clear();
		offlineExhibitBeans = null;
		return list;
	}

}
