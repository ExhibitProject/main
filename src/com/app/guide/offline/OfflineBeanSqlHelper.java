package com.app.guide.offline;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.guide.offline.OfflineBeaconBean;
import com.app.guide.offline.OfflineExhibitBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 数据库管理和维护类
 * @author joe_c
 *
 */
public class OfflineBeanSqlHelper extends OrmLiteSqliteOpenHelper {

	public static final String TAG = OfflineBeanSqlHelper.class.getSimpleName();

	public static int DATABASE_VERSION = 1;
	private Dao<OfflineExhibitBean, String> exhibitDao;
	private Dao<OfflineBeaconBean, String> beaconDao;
	private Dao<OfflineMapBean, String> mapDao;
	private Dao<OfflineMuseumBean, String> museumDao;
	private Dao<OfflineLabelBean, String> labelDao;

	public OfflineBeanSqlHelper(Context context, String name) {
		super(context, name, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource arg1) {
		try {
			TableUtils.createTableIfNotExists(arg1, OfflineExhibitBean.class);
			TableUtils.createTableIfNotExists(arg1, OfflineBeaconBean.class);
			TableUtils.createTableIfNotExists(arg1, OfflineMapBean.class);
			TableUtils.createTableIfNotExists(arg1, OfflineMuseumBean.class);
			TableUtils.createTableIfNotExists(arg1, OfflineLabelBean.class);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
	}

	public Dao<OfflineExhibitBean, String> getOfflineExhibitDao()
			throws SQLException {
		if (exhibitDao == null) {
			exhibitDao = getDao(OfflineExhibitBean.class);
		}
		return exhibitDao;
	}

	public Dao<OfflineBeaconBean, String> getOfflineBeaconDao()
			throws SQLException {
		if (beaconDao == null) {
			beaconDao = getDao(OfflineBeaconBean.class);
		}
		return beaconDao;
	}

	public Dao<OfflineMapBean, String> getOfflineMapDao() throws SQLException {
		if (mapDao == null) {
			mapDao = getDao(OfflineMapBean.class);
		}
		return mapDao;
	}

	public Dao<OfflineMuseumBean, String> getOfflineMuseumDao()
			throws SQLException {
		if (museumDao == null) {
			museumDao = getDao(OfflineMuseumBean.class);
		}
		return museumDao;
	}

	public Dao<OfflineLabelBean, String> getOfflineLabelDao()
			throws SQLException {
		if (labelDao == null) {
			labelDao = getDao(OfflineLabelBean.class);
		}
		return labelDao;
	}
}
