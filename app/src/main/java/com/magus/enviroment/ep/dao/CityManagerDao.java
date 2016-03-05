package com.magus.enviroment.ep.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.magus.enviroment.ep.bean.CityInfoBean;
import com.magus.enviroment.ep.dbHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 添加城市数据管理
 * Created by pau on 15/5/26.
 */
public class CityManagerDao {

    private DatabaseHelper dbHelper;

    private static CityManagerDao instance;

    public CityManagerDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public static CityManagerDao getInstance(Context context) {
        if (null == instance) {
            instance = new CityManagerDao(context);
        }
        return instance;
    }

    /**
     * 断开数据库
     */
    public void close() {
        dbHelper.close();
    }

    public void insertCity(final CityInfoBean city) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_CITY_NAME + " where "
                + DatabaseHelper.COLUMN_CITY_NAME + "=" + "'" + city.getCity() + "'" + ";";
        Cursor cursor = db.rawQuery(querySql, null);
        if (cursor.getCount() != 0) {
            return;
        }
        String insertSql = "insert into " + DatabaseHelper.TABLE_CITY_NAME + " values(?,?,?);";
        db.execSQL(insertSql, new Object[]{city.getCity(),city.getProvince(),city.getCityPinYin()});
        cursor.close();
        db.close();
    }

    /**
     * 删除城市
     * @param city
     */
    public void deleteCity(final CityInfoBean city) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        HashSet<String> cityNames = new HashSet<String>();
        //查询城市名称
        String querySql = "select * from " + DatabaseHelper.TABLE_CITY_NAME;
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            cityNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY_NAME)));
        }
        //如果存在则删除
        if (cityNames.contains(city.getCity())) {
            String sql = "delete from " + DatabaseHelper.TABLE_CITY_NAME + " where "
                    + DatabaseHelper.COLUMN_CITY_NAME + " = ?;";
            db.execSQL(sql, new Object[]{city.getCity()});
        }
        db.close();
    }

    public List<CityInfoBean> queryCityList() {
        List<CityInfoBean> list = new ArrayList<CityInfoBean>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_CITY_NAME;
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            CityInfoBean infoBean = new CityInfoBean();
            infoBean.setCity(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY_NAME)));
            infoBean.setProvince(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PROVINCE_NAME)));
            infoBean.setCityPinYin(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY_PINYIN)));
            list.add(infoBean);
        }
        return list;
    }


    /**
     * 已添加城市的列表
     *
     * @return
     */
    public synchronized HashSet<String> queryCityNames() {
        HashSet<String> cityNames = new HashSet<String>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String querySql = "select * from " + DatabaseHelper.TABLE_CITY_NAME;
        Cursor cursor = db.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            cityNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY_NAME)));
        }
        cursor.close();
        db.close();
        return cityNames;
    }

}
