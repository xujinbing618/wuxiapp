package com.magus.enviroment.ep.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * 用法
 * DatabaseHelper helper = DatabaseHelper.getInstance(this);
 * SQLiteDatabase db = helper.getWritableDatabase();
 * Created by pau on 15/5/26.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "environment.db";//数据库名称
    public static final String PATH = android.os.Environment.getExternalStorageDirectory() + "/Environment/";
    private static final int DATABASE_VERSION = 6;

    //添加的城市表
    public static final String TABLE_CITY_NAME = "TABLE_CITY_NAME"; // 添加的城市名字表
    public static final String COLUMN_CITY_NAME = "COLUMN_CITY_NAME"; // 城市名
    public static final String COLUMN_PROVINCE_NAME = "COLUMN_PROVINCE_NAME";//省名
    public static final String COLUMN_CITY_PINYIN = "COLUMN_CITY_PINYIN"; // 城市名

    //关注的城市表
    public static final String TABLE_ATTENTION_CITY_INFO = "TABLE_ATTENTION_ZONE_INFO";//关注的城市信息表
    public static final String COLUMN_ZONE_ID = "COLUMN_ZONE_ID";//区域id
    public static final String COLUMN_ZONE_NAME = "COLUMN_ZONE_NAME";//区域名称
    public static final String COLUMN_ZONE_STATUS = "COLUMN_ZONE_STATUS";//状态 0:全不选,1:全选,2:选中部分

    //关注的企业表
    public static final String TABLE_ATTENTION_ENTERPRISE_INFO = "TABLE_ATTENTION_ENTERPRISE";//关注的企业表
    public static final String COLUMN_POLL_ID = "COLUMN_POLL_ID";//企业id
    public static final String COLUMN_POLL_NAME = "COLUMN_POLL_NAME";//企业名称
    public static final String COLUMN_POLL_ZONE_ID = "COLUMN_POLL_ZONE_ID";//关注企业的区域id

    //关注的报警类型
    public static final String TABLE_ALARM_TYPE_INFO = "TABLE_ALARM_TYPE_INFO";
    public static final String COLUMN_ALARM_CODE = "COLUMN_ALARM_CODE"; //报警代码
    public static final String COLUMN_ALARM_TYPE_NAME = "COLUMN_ALARM_TYPE_NAME"; //报警类型名称
    public static final String COLUMN_ALARM_CHECK = "COLUMN_ALARM_CHECK"; //报警类型是否选中

    //创建表
    private final String CREATE_TABLE_PROVINCE_CITY =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CITY_NAME + " ("
                    + COLUMN_CITY_NAME + " VARCHAR(20) NOT NULL,"
                    + COLUMN_PROVINCE_NAME + " VARCHAR(20) NOT NULL,"
                    + COLUMN_CITY_PINYIN + " VARCHAR(20) NOT NULL );";

    private final String CREATE_TABLE_ATTENTION_CITY_INFO =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENTION_CITY_INFO + " ("
                    + COLUMN_ZONE_ID + " VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + COLUMN_ZONE_NAME + " VARCHAR(20) NOT NULL,"
                    + COLUMN_ZONE_STATUS + " INTEGER(10) NOT NULL);";

    private final String CREATE_TABLE_ATTENTION_ENTERPRISE_INFO =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENTION_ENTERPRISE_INFO + " ("
                    + COLUMN_POLL_ID + " VARCHAR(20) NOT NULL,"
                    + COLUMN_POLL_NAME + " VARCHAR(20) NOT NULL,"
                    + COLUMN_POLL_ZONE_ID + " VARCHAR(20) NOT NULL);";

    private final String CREATE_TABLE_ALARM_TYPE_INFO =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ALARM_TYPE_INFO + " ("
                    + COLUMN_ALARM_CODE + " VARCHAR(20) NOT NULL,"
                    + COLUMN_ALARM_TYPE_NAME + " VARCHAR(20) NOT NULL,"
                    + COLUMN_ALARM_CHECK + " VARCHAR(20) NOT NULL);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取数dbhelper
     */
//    public static DatabaseHelper getInstance(Context context) {
//        if (null == instance) {
//            DatabaseContext dbcontext = new DatabaseContext(context);
//            instance = new DatabaseHelper(dbcontext);
//        }
//        return instance;
//    }
    public static DatabaseHelper getInstance(Context context) {
        if (null == instance) {
           // DatabaseContext dbcontext = new DatabaseContext(context);
            instance = new DatabaseHelper(context);
        }
        return instance;
    }
    public static int getVersion() {
        return DATABASE_VERSION;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROVINCE_CITY);
        db.execSQL(CREATE_TABLE_ATTENTION_CITY_INFO);
        db.execSQL(CREATE_TABLE_ATTENTION_ENTERPRISE_INFO);
        db.execSQL(CREATE_TABLE_ALARM_TYPE_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENTION_CITY_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENTION_ENTERPRISE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM_TYPE_INFO);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //每次成功打开数据库后首先被执行
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /**
         执行数据库的降级操作
         1、只有新版本比旧版本低的时候才会执行
         2、如果不执行降级操作，会抛出异常
         */
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

}
