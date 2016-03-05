package com.magus.enviroment.ep;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.magus.enviroment.ep.bean.PagerInfo;
import com.magus.enviroment.ep.bean.User;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.service.MessagePushService;
import com.magus.enviroment.global.crash.MyCrashHandler;
import com.magus.magusutils.ContextUtil;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.RequestQueue;
import com.magus.volley.toolbox.Volley;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作为全局的application
 * 全局变量的初始化都最好在这里进行
 * Created by pau
 * Packagename com.magus.enviroment
 * 2015-15/4/14-下午4:43.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    //volley请求队列
    private static RequestQueue mRequestQueue;
    private static String mlocation = "";//当前定位地址
    public static final String ACTION_CHANGE_CITY = "com.action.change.city";
    public static final String CITY_NAME = "city_name";
    // 全局线程池（使用volley之后这个就用不到了）
    private ThreadPoolExecutor mThreadPool;
    private final int CORE_THREAD_NUM = 5;
    private final int MAX_THREAD_NUM = 10;
    private final int KEEP_ALIVE_TIME = 5000;
    private final int BLOCK_QUEUE_SIZE = 20;
    private final BlockingQueue<Runnable> blockQueue = new ArrayBlockingQueue<Runnable>(
            BLOCK_QUEUE_SIZE);

    //屏幕尺寸信息
    private WindowManager wm;
    public static int mWidth = 0;
    public static int mHeight = 0;

    //百度地图定位相关
    public static LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    public GeofenceClient mGeofenceClient;


    //用户信息
    public static User mUser;
    public static String mUid = "";
    public static String mRoleId = "";
    public static String mUserName = "";
    public static boolean mIsLogin = false;
    public static String mRolePagerInfos = "";

    //用户消息条数
    private static int messageNum = 0;

    public static int getMessageNum() {
        return messageNum;
    }

    public static void setMessageNum(int messageNum) {
        MyApplication.messageNum = messageNum;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initGlobal();
        initBaidu();
        initUser();
    }

    private void initUser() {
        User user = (User) SharedPreferenceUtil.getObject(SpKeyConstant.USER_OBJECT, this);
        if (user != null) {
            initUserInfo(user);
            Log.e(TAG, user.toString());
        }
    }

    /**
     * 初始化用户信息
     *
     * @param user
     */
    public static void initUserInfo(User user) {
        mIsLogin = true;
        mUser = user;
        mUserName = user.getUserFullName();
        mUid = user.getUserId();
        mRoleId = user.getRoleId();
        initRolePager(user);

    }

    /**
     * 退出清除用户信息
     */
    public static void clearUserInfo() {
        mIsLogin = false;
        mUser = null;
        mUserName = "";
        mUid = "";
        mRoleId = "";
        mRolePagerInfos = "";

    }

    private static void initRolePager(User user) {

        List<PagerInfo> pagerInfoList = user.getPagerInfos();
        mRolePagerInfos = "";
        for (int i = 0; i < pagerInfoList.size(); i++) {
            mRolePagerInfos = mRolePagerInfos + (pagerInfoList.get(i).getUrl()) + ",";
        }
    }

    /**
     * 初始化请求队列
     */
    private void initGlobal() {
        Log.e(TAG, "init");
        ActivityManager activityManager = (ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        String classNames = "";
        for (int i = 0; i < serviceList.size(); i++) {
            classNames = classNames + serviceList.get(i).service.getClassName() + ",";
        }
        if (!classNames.contains(MessagePushService.class.getName())) {
            // this.startService(new Intent(this, MessagePushService.class));//开启服务
            Log.e(TAG, "start-service" + classNames);
        }

//以下两条注释Xuer所加,程序崩溃
        MyCrashHandler crashHandler = MyCrashHandler.getInstance();
        crashHandler.init(getApplicationContext());


        //初始化全局context
        ContextUtil.init(getApplicationContext());
        //初始化volley队列
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        if (mThreadPool == null) {
            mThreadPool = new ThreadPoolExecutor(CORE_THREAD_NUM,
                    MAX_THREAD_NUM, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                    blockQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
    }

    /**
     * 百度定位
     */
    private void initBaidu() {
        mLocationClient = new LocationClient(this.getApplicationContext()); //声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        mGeofenceClient = new GeofenceClient(getApplicationContext());

    }

    //百度定位监听
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null)
                return;
            if (location.getCity() != null) {
                mLocationClient.stop();
                sendBroadCastReceiver(location.getCity());
            }
            // sendLocationRequest(location);
        }
    }

    //根据经纬度请求详细地址信息
//    private void sendLocationRequest(BDLocation location) {
//        String url = BaiduApi.LOCATION_URL_HEAD
//                + location.getLatitude() + "," + location.getLongitude()
//                + "&key=" + BaiduApi.BAIDU_MAP_KEY;
//        //根据给定的URL新建一个请求
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
////                        android.util.Log.e("MyApplication", s);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        MyApplication.getRequestQueue().add(request);
//    }

    public ThreadPoolExecutor getThreadPool() {
        return mThreadPool;
    }

    /**
     * 获得volley请求队列
     *
     * @return
     */
    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


    public String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 改变背景
     */
    private void sendBroadCastReceiver(String cityName) {
        Intent intent = new Intent(ACTION_CHANGE_CITY);
        intent.putExtra(CITY_NAME, cityName);
        this.sendBroadcast(intent);
    }

}
