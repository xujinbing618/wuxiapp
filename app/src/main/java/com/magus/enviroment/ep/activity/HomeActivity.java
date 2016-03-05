package com.magus.enviroment.ep.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.BaseActivity;
import com.magus.enviroment.ep.bean.AlarmTypeInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dbHelper.DatabaseHelper;
import com.magus.enviroment.ep.fragment.AttentionFragment;
import com.magus.enviroment.ep.fragment.HomeFragment;
import com.magus.enviroment.ep.fragment.KnowledgeFragment;
import com.magus.enviroment.ep.fragment.MeFragment;
import com.magus.enviroment.ep.service.MessagePushService;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * 主activity
 * 底部标签切换在这里进行控制
 */
public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    public static final String HOME = "HOME";
    public static final String WORK = "WORK";
    public static final String KNOWLEDGE = "KNOWLEDGE";
    public static final String ME = "ME";

    private LayoutInflater mLayoutInflater;
    private FragmentManager mFragmentManager;
    private TabHost mTabHost;
    private View mRootView;

    private HomeFragment mHomeFragment;
    private AttentionFragment mAttentionFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private MeFragment mMeFragment;
    private ArrayList<View> mTabViewList = new ArrayList<View>();

    //脚标
    private TextView mCurrentTextView;
    //默认第一个
    private int mLastTabIndex = 0;
    //当前tab array
    public static final String[] mTabTags = {HOME, WORK, KNOWLEDGE, ME};
    private String mCurrentTag;

    //退出提示
    private long mLastPressed;
    private final static int EXIT_TIME = 1400;

    //百度定位
    public Vibrator mVibrator;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;

    private String tempcoor = "gcj02";
    public LocationClient mLocationClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);
        mRootView = mLayoutInflater.inflate(R.layout.activity_home, null);
        setContentView(mRootView);
        mFragmentManager = getSupportFragmentManager();
        mTabHost = (TabHost) mRootView.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        sendRequest();
        /**
         * 判断服务未启动 则启动服务
         */
        ActivityManager activityManager = (ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        String classNames="";
        for (int i=0; i<serviceList.size(); i++) {
            classNames =classNames+ serviceList.get(i).service.getClassName()+",";
        }
        if (!classNames.contains(MessagePushService.class.getName())&& MyApplication.mIsLogin){
            Intent intent = new Intent(HomeActivity.this, MessagePushService.class);

            if (SharedPreferenceUtil.get("Time", "10") != null) {
                intent.putExtra("time", SharedPreferenceUtil.get("Time", "10"));
            } else {
                intent.putExtra("time", "10");
               // SharedPreferenceUtil.get("Time", "10");
            }


            this.startService(intent);//开启服务
        }



        initTabHost(savedInstanceState);
        initFragment();
        initLocate();
        initLocation();
    }


    private void initFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mHomeFragment = new HomeFragment();
        transaction.replace(R.id.tab_content, mHomeFragment, HOME);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 定位
     */
    private void initLocate() {
        mLocationClient = mApp.mLocationClient;
        mLocationClient.start();
    }

    /**
     * 初始化选项卡
     *
     * @param savedInstanceState
     */
    private void initTabHost(Bundle savedInstanceState) {
        //添加选项卡导航页
        mTabHost.addTab(mTabHost
                .newTabSpec(HOME)
                .setContent(mNullContentFactory)
                .setIndicator(createTabIndicator(
                        R.drawable.navigation_indicator_background_home,
                        R.string.navigation_home)));
        mTabHost.addTab(mTabHost
                .newTabSpec(WORK)
                .setContent(mNullContentFactory)
                .setIndicator(createTabIndicator(
                        R.drawable.navigation_indicator_background_work,
                        R.string.navigation_work)));
        mTabHost.addTab(mTabHost
                .newTabSpec(KNOWLEDGE)
                .setContent(mNullContentFactory)
                .setIndicator(createTabIndicator(
                        R.drawable.navigation_indicator_background_knowledge,
                        R.string.navigation_knowledge)));
        mTabHost.addTab(mTabHost
                .newTabSpec(ME)
                .setContent(mNullContentFactory)
                .setIndicator(createTabIndicator(
                        R.drawable.navigation_indicator_background_me,
                        R.string.navigation_me)));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                final FragmentTransaction transaction = mFragmentManager.beginTransaction();

                if (mHomeFragment != null) {
                    transaction.hide(mHomeFragment);
                }
                if (mAttentionFragment != null) {
                    transaction.hide(mAttentionFragment);
                }
                if (mKnowledgeFragment != null) {
                    transaction.hide(mKnowledgeFragment);
                }
                if (mMeFragment != null) {
                    transaction.hide(mMeFragment);
                }
//                mHomeFragment = null;
//                mAttentionFragment = null;
//                mKnowledgeFragment = null;
//                mMeFragment = null;
                if (tabId.equals(HOME)) {
                    if (mHomeFragment == null) {
                        mHomeFragment = new HomeFragment();
                        transaction.add(R.id.tab_content, mHomeFragment, HOME);
                    } else {
                        transaction.show(mHomeFragment);
                    }
                    mCurrentTag = HOME;
                } else if (tabId.equals(WORK)) {
                    if (mAttentionFragment == null) {
                        mAttentionFragment = new AttentionFragment();
                        transaction.add(R.id.tab_content, mAttentionFragment, WORK);
                    } else {
                        transaction.show(mAttentionFragment);
                    }
                    mCurrentTag = WORK;
                } else if (tabId.equals(KNOWLEDGE)) {
                    if (mKnowledgeFragment == null) {
                        mKnowledgeFragment = new KnowledgeFragment();
                        transaction.add(R.id.tab_content, mKnowledgeFragment, KNOWLEDGE);
                    } else {
                        transaction.show(mKnowledgeFragment);
                    }
                    mCurrentTag = KNOWLEDGE;
                } else if (tabId.equals(ME)) {
                    if (mMeFragment == null) {
                        mMeFragment = new MeFragment();
                        transaction.add(R.id.tab_content, mMeFragment, ME);
                    } else {
                        transaction.show(mMeFragment);
                    }
                }
                transaction.commit();
            }
        });

        mTabHost.setCurrentTab(mLastTabIndex);
        mCurrentTag = mTabTags[mLastTabIndex];
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if(SharedPreferenceUtil.get("noPush","true").equals("false")){
            Intent intent = new Intent(HomeActivity.this, MessagePushService.class);
            HomeActivity.this.stopService(intent);//关闭服务
        }
    }

    private TabHost.TabContentFactory mNullContentFactory = new TabHost.TabContentFactory() {

        @Override
        public View createTabContent(String tag) {
            return new TextView(HomeActivity.this);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 设置选项卡相关属性
     *
     * @param imageResId
     * @param stringResId
     * @return
     */
    private LinearLayout createTabIndicator(int imageResId, int stringResId) {
        LinearLayout indicator = (LinearLayout) mLayoutInflater.inflate(
                R.layout.tab_navigation_indicator, null);
        FrameLayout frame = (FrameLayout) indicator.getChildAt(0);

        ((ImageView) frame.getChildAt(0)).setImageDrawable(getResources()
                .getDrawable(imageResId));
        ((TextView) indicator.getChildAt(1)).setTextColor(getResources()
                .getColorStateList(R.color.select_blue_default_white));
        ((TextView) indicator.getChildAt(1)).setText(stringResId);

        mTabViewList.add(indicator);

        return indicator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //设置定位参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        int span = 1000;
        option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onBackPressed() {
        Long now = System.currentTimeMillis();
        if (Math.abs(now - mLastPressed) <= EXIT_TIME) {
            super.onBackPressed();
        } else {
            mLastPressed = now;
            Toast.makeText(this, getResources().getString(R.string.exit_info), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SharedPreferenceUtil.get("noPush","true").equals("false")){
            Intent intent = new Intent(HomeActivity.this, MessagePushService.class);
            intent.putExtra("time",SharedPreferenceUtil.get("Time", "10"));
            this.startService(intent);//开启服务
        }





    }

    /**
     * 请求我的关注报警类型数据
     */
    private void sendRequest() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // db.execSQL("DROP TABLE TABLE_ALARM_TYPE_INFO");
       // db.execSQL("DELETE FROM TABLE_ATTENTION_ENTERPRISE");
        String url = URLConstant.HEAD_URL+URLConstant.URL_ALARM_TYPE + "?userId=" + MyApplication.mUid;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getRequestQueue().add(request);

    }
    private void parseResponse(String response) {
        AlarmTypeInfo.parseAlarmTypeInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, this);
    }


}
