package com.magus.enviroment.ep.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidu.location.LocationClient;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.CityInfoBean;
import com.magus.enviroment.ep.dao.CityManagerDao;
import com.magus.enviroment.ep.fragment.home.HomeInfoFragment;
import com.magus.enviroment.ui.CircleIndicator;
import com.magus.enviroment.ui.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * 控制 不同城市环境质量的页面切换
 * Created by pau on 3/15/15.
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";
    public static final String PAGER_NAME = "pager_name";//当前显示的页面
    private boolean isreceive=false;//定位BroadcastReceiver是否注册
    private ViewPager mViewPager;
    private MyViewPagerAdapter mPagerAdapter;
    private ArrayList<Fragment> mViewPagerChildList = new ArrayList<Fragment>();
    private LocationClient mLocationClient = null;
    private ProgressDialog progressDialog;//进度条
    private int mLastItemIndex = -1;//最后一次选中的fragment index
    private int defaultPageSize = 1;//默认显示页数
    private String locationCityName = "呼和浩特";
    private View mRootView;
    private LinearLayout mHomeLayout;

    private CircleIndicator mViewPagerIndicator;

    private CityManagerDao mManagerDao;
    private List<CityInfoBean> mCityList = new ArrayList<CityInfoBean>();

    private int mSelectedPositio = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_home, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        isreceive=false;
        initViewPager(locationCityName);//添加完城市之后重新初始化
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isreceive==true) {
            mActivity.unregisterReceiver(locationCity);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        progressDialog = UIUtil.initDialog(getActivity(), "定位中，请稍后...");
        mLocationClient = new LocationClient(getActivity()); //声明LocationClient类
        mHomeLayout = (LinearLayout) mRootView.findViewById(R.id.home_layout);
        mManagerDao = CityManagerDao.getInstance(mActivity);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);
        initViewPager(locationCityName);
    }

    private void initViewPager(String locationCityName) {
        //mViewPagerIndicator = (CircleIndicator) mRootView.findViewById(R.id.indicator);
        mViewPagerChildList.clear();
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mCityList = mManagerDao.queryCityList();
        if (mCityList.size() > 0) {
            for (int i = 0; i < mCityList.size(); i++) {
                //初始化fragment
                HomeInfoFragment mInfoFragment = new HomeInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(HomeInfoFragment.CITY_NAME, i);
                mInfoFragment.setArguments(bundle);
                mViewPagerChildList.add(mInfoFragment);
            }
        } else {
            getCityReceiver();
            //初始化fragment
            HomeInfoFragment mInfoFragment = new HomeInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(HomeInfoFragment.CITY_NAME, locationCityName);
            if (!"呼和浩特".equals(locationCityName)){
                if ("呼和浩特市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "huhehaote");
                }
                if ("包头市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "baotou");
                }
                if ("乌海市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "wuhai");
                }
                if ("赤峰市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "chifeng");
                }
                if ("通辽市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "tongliao");
                }
                if ("鄂尔多斯市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "eerduosi");
                }
                if ("呼伦贝尔市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "hulunbeier");
                }
                if ("巴彦淖尔市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "bayannaoer");
                }
                if ("乌兰察布市".equals(locationCityName)){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "wulanchabu");
                }
                if (locationCityName.contains("兴安盟")){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "xinganmeng");
                }
                if (locationCityName.contains("锡林郭勒盟")){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "xilinguolemeng");
                }
                if (locationCityName.contains("阿拉善")){
                    bundle.putString(HomeInfoFragment.CITY_PINYIN, "alashanmeng");
                }
            }else{
                bundle.putString(HomeInfoFragment.CITY_PINYIN, "huhehaote");
            }

            mInfoFragment.setArguments(bundle);
            mViewPagerChildList.add(mInfoFragment);
        }
        mPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
       // mViewPagerIndicator.setViewPager(mViewPager);
    }


    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mLastItemIndex = position;
            mSelectedPositio = position;//选中的
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

//    @Override
//    public void onReceiveLocation(BDLocation location) {
//        if (location == null)
//            return;
//        if (location.getCity() != null) {
//               MyApplication.mLocationClient.stop();            MyApplication.setMlocation(location.getCity());
//             //  progressDialog.dismiss();
//        }
//        // sendLocationRequest(location);
//    }


    //viewpager适配器
    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mViewPagerChildList.get(position);
        }

        @Override
        public int getCount() {
            return mViewPagerChildList.size();
        }
    }

    /**
     * 广播接收更新背景图
     */
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int level = intent.getIntExtra(HomeInfoAirFragment.KEY_LEVEL, 0);
//            String city  = intent.getStringExtra(HomeInfoAirFragment.KEY_CITY);
//
//            Log.e(TAG,"city="+city+",selectedcity="+mCityList.get(mSelectedPositio).getCity());
//            if (mCityList.get(mSelectedPositio).getCity().equals(city)) {
//                if (level == 1) {
//                    mHomeLayout.setBackgroundResource(R.mipmap.home_bg);
//                } else if (level == 2) {
//                    mHomeLayout.setBackgroundResource(R.mipmap.home_bg2);
//                } else if (level == 3) {
//                    mHomeLayout.setBackgroundResource(R.mipmap.home_bg3);
//                } else if (level == 4) {
//                    mHomeLayout.setBackgroundResource(R.mipmap.home_bg4);
//                } else if (level == 5) {
//                    mHomeLayout.setBackgroundResource(R.mipmap.home_bg5);
//                } else if (level == 6) {
//                    mHomeLayout.setBackgroundResource(R.mipmap.home_bg6);
//                }
//            }
//
//        }
//    };

    /**
     * 广播接收定位城市
     */
    private BroadcastReceiver locationCity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cityName = intent.getStringExtra(MyApplication.CITY_NAME);
            locationCityName = cityName;
            initViewPager(locationCityName);


        }
    };

    private void getCityReceiver() {
        IntentFilter filter = new IntentFilter(
                MyApplication.ACTION_CHANGE_CITY);
        if (mActivity != null) {
            isreceive=true;
            mActivity.registerReceiver(locationCity, filter);
        }
    }
}
