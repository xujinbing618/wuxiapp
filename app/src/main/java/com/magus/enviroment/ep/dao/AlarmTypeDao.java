package com.magus.enviroment.ep.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.magus.enviroment.ep.bean.AlarmTypeInfo;
import com.magus.enviroment.ep.dbHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pau on 15/6/9.
 */
public class AlarmTypeDao {
    private static final String TAG = "ZoneManagerDao";

    private DatabaseHelper dbHelper;

    private static AlarmTypeDao instance;

    public AlarmTypeDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public static AlarmTypeDao getInstance(Context context) {
        if (null == instance) {
            instance = new AlarmTypeDao(context);
        }
        return instance;
    }


    /**
     * 插入一条报警类型
     *
     * @param alarmTypeInfo
     */
    public synchronized void insertAlarmType(final AlarmTypeInfo alarmTypeInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ALARM_TYPE_INFO + " where "
                + DatabaseHelper.COLUMN_ALARM_CODE + "='" + alarmTypeInfo.getAlarmCode()+"';";
        String insertSql = "insert into " + DatabaseHelper.TABLE_ALARM_TYPE_INFO + " values(?,?,?);";
        Cursor cursor = db.rawQuery(querySql, null);
        if (cursor.getCount() != 0) {//如果有数据
            Log.e(TAG, "getcount" + cursor.getCount() + "已有数据不需要插入");
            //如果有这条数据  则修改
            db.execSQL(" update " + DatabaseHelper.TABLE_ALARM_TYPE_INFO + " set " + DatabaseHelper.COLUMN_ALARM_CHECK + " = '" + alarmTypeInfo.getChecked() + "' where " +DatabaseHelper.COLUMN_ALARM_CODE + "='" + alarmTypeInfo.getAlarmCode()+"'");
                    cursor.close();
            db.close();
            return;
        } else {
            db.execSQL(insertSql, new Object[]{alarmTypeInfo.getAlarmCode(), alarmTypeInfo.getAlarmTypeName(), alarmTypeInfo.getChecked()});
           // Log.e(TAG, "insert" + alarmTypeInfo.getAlarmCode()+","+alarmTypeInfo.getAlarmTypeName()+","+alarmTypeInfo.getChecked());
            cursor.close();
            db.close();
        }
    }


    /**
     * 删除一条报警类型
     *
     * @param alarmTypeInfo
     */
    public void deleteAlarmType(final AlarmTypeInfo alarmTypeInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteSql = "delete from " + DatabaseHelper.TABLE_ALARM_TYPE_INFO + " where "
                + DatabaseHelper.COLUMN_ALARM_CODE + "='" + alarmTypeInfo.getAlarmCode() + "';";
        Log.e(TAG, "delete"+alarmTypeInfo.getAlarmCode()+","+alarmTypeInfo.getAlarmTypeName());
        db.execSQL(deleteSql);
        db.close();
    }


    /**
     * 获得所以类型
     *
     * @return
     */
    public List<AlarmTypeInfo> findAllAlarmType() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ALARM_TYPE_INFO;
        List<AlarmTypeInfo> list = new ArrayList<AlarmTypeInfo>();
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            AlarmTypeInfo alarmTypeInfo = new AlarmTypeInfo();
            alarmTypeInfo.setAlarmCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ALARM_CODE)));
            alarmTypeInfo.setAlarmTypeName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ALARM_TYPE_NAME)));
            alarmTypeInfo.setChecked(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ALARM_CHECK)));
            list.add(alarmTypeInfo);
        }
        return list;
    }
    /**
     * 获得所以关注类型
     *
     * @return
     */
    public List<AlarmTypeInfo> findCheckAlarmType() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_ALARM_TYPE_INFO + " where " + DatabaseHelper.COLUMN_ALARM_CHECK + "='true'";
        List<AlarmTypeInfo> list = new ArrayList<AlarmTypeInfo>();
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            AlarmTypeInfo alarmTypeInfo = new AlarmTypeInfo();
            alarmTypeInfo.setAlarmCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ALARM_CODE)));
            alarmTypeInfo.setAlarmTypeName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ALARM_TYPE_NAME)));
            alarmTypeInfo.setChecked(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ALARM_CHECK)));
            list.add(alarmTypeInfo);
        }
        return list;
    }
}
