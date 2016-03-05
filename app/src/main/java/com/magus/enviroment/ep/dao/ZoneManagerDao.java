package com.magus.enviroment.ep.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.magus.enviroment.ep.bean.AttentionZone;
import com.magus.enviroment.ep.dbHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by pau on 15/6/8.
 */
public class ZoneManagerDao {
    private static final String TAG = "ZoneManagerDao";

    private DatabaseHelper dbHelper;

    private static ZoneManagerDao instance;

    public ZoneManagerDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public static ZoneManagerDao getInstance(Context context) {
        if (null == instance) {
            instance = new ZoneManagerDao(context);
        }
        return instance;
    }

    /**
     * 断开数据库
     */
    public void close() {
        dbHelper.close();
    }

    public void insertZone(AttentionZone zone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    /**
     * 如果没有则插入 如果有则更新
     *
     * @param zone
     */
    public synchronized void updateZoneStatus(final AttentionZone zone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_CITY_INFO + " where "
                + DatabaseHelper.COLUMN_ZONE_ID + "= '" + zone.getZoneId() + "';";
        String updateSql = "update " + DatabaseHelper.TABLE_ATTENTION_CITY_INFO + " set "
                + DatabaseHelper.COLUMN_ZONE_STATUS + "=" + "'" + zone.getStatus() + "' where " + DatabaseHelper.COLUMN_ZONE_ID + "='" + zone.getZoneId() + "';";
        String insertSql = "insert into " + DatabaseHelper.TABLE_ATTENTION_CITY_INFO + " values(?,?,?);";

        Cursor cursor = db.rawQuery(querySql, null);
        if (cursor.getCount() != 0) {//表示已经有这条数据了，则更新状态
            db.execSQL(updateSql);
            cursor.close();
            db.close();
            return;
        } else {
            Log.e(TAG, "当前状态status-insert:" + zone.getStatus() + " zoneId:" + zone.getZoneId());
            db.execSQL(insertSql, new Object[]{zone.getZoneId(), zone.getZoneName(), zone.getStatus()});
            cursor.close();
            db.close();
        }
    }

    /**
     * 根据zoneId获得关注的企业
     *
     * @return
     */
    public List<AttentionZone> findZoneList() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_CITY_INFO + ";";
        List<AttentionZone> list = new ArrayList<AttentionZone>();
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            AttentionZone zone = new AttentionZone();
            zone.setZoneId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ZONE_ID)));
            zone.setZoneName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ZONE_NAME)));
            zone.setStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ZONE_STATUS)));
            list.add(zone);
        }
        return list;
    }


    /**
     * 已添加的企业列表
     *
     * @return
     */
    public synchronized HashSet<String> queryZoneIds() {
        HashSet<String> zoneIds = new HashSet<String>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ATTENTION_CITY_INFO;
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            zoneIds.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ZONE_ID)));
        }
        cursor.close();
        db.close();
        return zoneIds;
    }

    /**
     * 根据zoneId查询状态
     *
     * @param zoneId
     * @return
     */
    public String queryStatusByZoneId(String zoneId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select " + DatabaseHelper.COLUMN_ZONE_STATUS + " from " + DatabaseHelper.TABLE_ATTENTION_CITY_INFO
                + " where " + DatabaseHelper.COLUMN_ZONE_ID + " = '" + zoneId + "';";
        Cursor cursor = db.rawQuery(querySql, null);
        String status = "";
        cursor.moveToFirst();
        status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ZONE_STATUS));
        Log.e(TAG, "status-query:" + status);
        return status;
    }

}
