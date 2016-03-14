package com.magus.enviroment.ep.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.activity.home.CityManagerActivity;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.CityInfoBean;
import com.magus.enviroment.ep.dao.CityManagerDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示首页中详情页
 * Created by pau
 * Packagename com.magus.enviroment.fragment.home
 * 2015-15/4/16-上午11:04.
 */
public class HomeInfoFragment extends BaseFragment {
    private static final String TAG = "HomeInfoFragment";
    private View mRootView;
    private LinearLayout mHomeLayout;
    private Button mAirButton;
    private ImageView mRefreshImage;

    private FrameLayout mFrameLayout;
    private RelativeLayout mCityManager;

    private HomeInfoAirFragment mAirFragment;
    private HomeInfoWaterFragment mWaterFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private List<CityInfoBean> mCityList = new ArrayList<CityInfoBean>();
    private CityManagerDao mManagerDao;
    private String cityName;//城市名
    private String cityPinYin;//城市拼音
    private TextView mCityText;
    public static final String CITY_NAME = "city_name";//viewpager传递城市名称的key
    public static final String CITY_PINYIN = "city_pinyin";//viewpager传递城市拼音的key
    public static final String ACTION_AQI_REFRESH = "com.action.aqi.refresh";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_home_info, null);

            initDate();
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

    private void initDate() {
        mManagerDao = CityManagerDao.getInstance(mActivity);
        mCityList = mManagerDao.queryCityList();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (mCityList.size() > 0) {
              //  cityName = mCityList.get(bundle.getInt(CITY_NAME)).getCity();
                cityName = "wuxi";
                cityPinYin=mCityList.get(bundle.getInt(CITY_NAME)).getCityPinYin();

            }else{
              //  cityName=bundle.getString(CITY_NAME);
                cityName = "wuxi";
                cityPinYin=bundle.getString(CITY_PINYIN);
            }
    }
    }

    //初始化
    private void initView() {

//        mHomeLayout = (LinearLayout) mRootView.findViewById(R.id.home_bg);
        mAirButton = (Button) mRootView.findViewById(R.id.air);
        // mAirButton.setOnClickListener(changedListener);
//        mWaterButton = (Button) mRootView.findViewById(R.id.water);
//        mWaterButton.setOnClickListener(changedListener);


        mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.info_content);
        mCityManager = (RelativeLayout) mRootView.findViewById(R.id.city_manager);
        //mCityManager.setOnClickListener(onclickListener);

        mRefreshImage = (ImageView) mRootView.findViewById(R.id.refresh);
        mRefreshImage.setOnClickListener(onclickListener);
        mCityText = (TextView) mRootView.findViewById(R.id.city_name);
        mCityText.setText(cityName);
        initFragment();
    }

    private View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.city_manager:
                    Intent intent = new Intent();
                    intent.setClass(mActivity, CityManagerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.refresh:
                    sendBroadCastReceiver();
                    break;


            }
        }
    };

    //初始化第一次进入的fragment
    private void initFragment() {
        mFragmentManager = getChildFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        if (mAirFragment == null) {
            mAirFragment = new HomeInfoAirFragment();
        }
//        if (mWaterFragment==null){
//            mWaterFragment = new HomeInfoWaterFragment();
//        }
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME, cityPinYin);
        mAirFragment.setArguments(bundle);
        mTransaction.replace(R.id.info_content, mAirFragment);
        mTransaction.commitAllowingStateLoss();
        mAirButton.setSelected(true);
//        mWaterButton.setSelected(false);
    }

    //切换fragment  viewpager中fragment如果用add可能会出现两层
    private View.OnClickListener changedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(CITY_NAME, cityPinYin);
            mTransaction = mFragmentManager.beginTransaction();
//            if (mAirFragment != null) {
//                mTransaction.detach(mAirFragment);
//                Log.e(TAG, "hide-mAirFragment");
//            }
//            if (mWaterFragment != null) {
//                mTransaction.detach(mWaterFragment);
//                Log.e(TAG,"detach-mWaterFragment");
//            }
            switch (v.getId()) {
                case R.id.air:
                    if (mAirFragment == null) {
                        mAirFragment = new HomeInfoAirFragment();
                        mAirFragment.setArguments(bundle);
                        mTransaction.replace(R.id.info_content, mAirFragment);
                    } else {
//                        mTransaction.attach(mAirFragment);
                        mTransaction.replace(R.id.info_content, mAirFragment);
                    }
                    mAirButton.setSelected(true);
//                    mWaterButton.setSelected(false);
                    break;
//                case R.id.water:
//                    if (mWaterFragment == null) {
//                        mWaterFragment = new HomeInfoWaterFragment();
//                        mWaterFragment.setArguments(bundle);
//                        mTransaction.replace(R.id.info_content, mWaterFragment);
//                    }else {
////                        mTransaction.attach(mWaterFragment);
//                        mTransaction.replace(R.id.info_content, mWaterFragment);
//                    }
//                    mAirButton.setSelected(false);
//                    mWaterButton.setSelected(true);
//                    break;
            }
            mTransaction.commit();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "homeinfo_onresume");
       // getReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mActivity.unregisterReceiver(receiver);
    }

    /**
     * 刷新数据广播
     */
    private void sendBroadCastReceiver() {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        Intent intent = new Intent(ACTION_AQI_REFRESH);
        mActivity.sendBroadcast(intent);
    }

    /**
     * 广播接收更新背景图
     */
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int level = intent.getIntExtra(HomeInfoAirFragment.KEY_LEVEL, 0);
//            if (level == 1) {
//                mHomeLayout.setBackgroundResource(R.mipmap.home_bg);
//            } else if (level == 2) {
//                mHomeLayout.setBackgroundResource(R.mipmap.home_bg2);
//            } else if (level == 3) {
//                mHomeLayout.setBackgroundResource(R.mipmap.home_bg3);
//            } else if (level == 4) {
//                mHomeLayout.setBackgroundResource(R.mipmap.home_bg4);
//            } else if (level == 5) {
//                mHomeLayout.setBackgroundResource(R.mipmap.home_bg5);
//            } else if (level == 6) {
//                mHomeLayout.setBackgroundResource(R.mipmap.home_bg6);
//            }
//        }
//    };

//    private void getReceiver() {
//        IntentFilter filter = new IntentFilter(
//                HomeInfoAirFragment.ACTION_CHANGE_BG);
//        if (mActivity != null) {
//            mActivity.registerReceiver(receiver, filter);
//        }
//    }

}
