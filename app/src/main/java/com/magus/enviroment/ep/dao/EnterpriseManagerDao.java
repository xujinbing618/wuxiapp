package com.magus.enviroment.ep.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.magus.enviroment.ep.bean.AttentionEnterprise;
import com.magus.enviroment.ep.dbHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by pau on 15/6/8.
 */
public class EnterpriseManagerDao {
    private static final String TAG = "ZoneManagerDao";

    private DatabaseHelper dbHelper;

    private static EnterpriseManagerDao instance;

    public EnterpriseManagerDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public static EnterpriseManagerDao getInstance(Context context) {
        if (null == instance) {
            instance = new EnterpriseManagerDao(context);
        }
        return instance;
    }

    /**
     * 断开数据库
     */
    public void close() {
        dbHelper.close();
    }


    /**
     * 插入一条企业信息
     *
     * @param enterprise
     */
    public synchronized void insertEnterprise(final AttentionEnterprise enterprise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO + " where "
                + DatabaseHelper.COLUMN_POLL_ID + "='" + enterprise.getPollId() + "';";
        String insertSql = "insert into " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO + " values(?,?,?);";
        Cursor cursor = db.rawQuery(querySql, null);
        if (cursor.getCount() != 0) {//如果有数据
            Log.e(TAG, "getcount" + cursor.getCount() + "已有数据不需要插入");
            cursor.close();
            db.close();
            return;
        } else {
            db.execSQL(insertSql, new Object[]{enterprise.getPollId(), enterprise.getPollName(), enterprise.getZoneId()});
            Log.e(TAG, "insert" + enterprise.getPollName());
            cursor.close();
            db.close();
        }

    }

    /**
     * 删除一条企业信息
     *
     * @param enterprise
     */
    public void deleteEnterprise(final AttentionEnterprise enterprise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO + " where "
                + DatabaseHelper.COLUMN_POLL_ID + "=" + enterprise.getPollId() + ";";
        String deleteSql = "delete from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO + " where "
                + DatabaseHelper.COLUMN_POLL_ID + "=" + enterprise.getPollId() + ";";
        Log.e(TAG, enterprise.getPollName());
        db.execSQL(deleteSql);
        db.close();
    }

    /**
     * 删除企业
     */
    public void deleteAllEnterpriseByZoneId(String zoneId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteSql = "delete from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO + " where "
                + DatabaseHelper.COLUMN_POLL_ZONE_ID + "=" + zoneId + ";";
        Log.e(TAG, zoneId);
        db.execSQL(deleteSql);
        db.close();
    }

    /**
     * 根据zoneId获得关注的企业
     *
     * @param zoneId
     * @return
     */
    public List<AttentionEnterprise> findByZoneId(String zoneId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO + " where " + DatabaseHelper.COLUMN_POLL_ZONE_ID + "=" + zoneId;
        List<AttentionEnterprise> list = new ArrayList<AttentionEnterprise>();
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            AttentionEnterprise enterprise = new AttentionEnterprise();
            enterprise.setPollName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_POLL_NAME)));
            enterprise.setZoneId(zoneId);
            enterprise.setPollId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_POLL_ID)));
            list.add(enterprise);
        }
        return list;
    }


    /**
     * 已添加的企业列表
     *
     * @return
     */
    public synchronized HashSet<String> queryEnterpriseIds() {
        HashSet<String> enterpriseIds = new HashSet<String>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO;
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            enterpriseIds.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_POLL_ID)));
        }
        cursor.close();
        db.close();
        return enterpriseIds;
    }




    /**
     * 获得全部企业
     * @return
     */
    public synchronized List<AttentionEnterprise> getAllFactory() {
        List<AttentionEnterprise> list = new ArrayList<AttentionEnterprise>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_ENTERPRISE_INFO;
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            AttentionEnterprise enterprise = new AttentionEnterprise();
            enterprise.setPollName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_POLL_NAME)));
            enterprise.setPollId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_POLL_ID)));
            list.add(enterprise);
        }
        cursor.close();
        db.close();
        return list;
    }
}
