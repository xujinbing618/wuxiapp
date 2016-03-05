package com.magus.enviroment.ep.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.HomeWaterStationAdapter;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.magusutils.UnitUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 水质质量fragment
 * Created by pau
 * Packagename com.magus.enviroment.fragment.home
 * 2015-15/4/15-下午3:22.
 */
public class HomeInfoWaterFragment extends BaseFragment {

    private static final String TAG = "HomeInfoWaterFragment";

    private View mRootView;

    private TextView adviceText;
    private LinearLayout waterInfoLayout;//首页布局

    private ListView mStationListView;//监测站列表

    private HomeWaterStationAdapter mAdapter;

    private String[] strs={"待处理","待处理","待处理","待处理","待处理","待处理","待处理","待处理","待处理"};

    private List<String> mList ;

    private String mCityName="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_home_info_water, null);
            initView();
            initData();
            Log.e("TAG", "初始化");
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
        getReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiver!=null){
            mActivity.unregisterReceiver(receiver);
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

    private void initView() {
        mList = Arrays.asList(strs);
        adviceText = (TextView) mRootView.findViewById(R.id.advice);
        waterInfoLayout = (LinearLayout) mRootView.findViewById(R.id.water_info_main);
        mStationListView = (ListView) mRootView.findViewById(R.id.water_station_list);
        mAdapter = new HomeWaterStationAdapter(mActivity,mList);
        mStationListView.setAdapter(mAdapter);
        //47：tabhost高度，71城市名称高度，30，文字padding高度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                MyApplication.mHeight - UnitUtil.dip2px(47 + 70 + 30));
        waterInfoLayout.setLayoutParams(params);

    }
    private void initData(){
        Bundle bundle = getArguments();
        if (bundle!=null){
            mCityName = bundle.getString(HomeInfoFragment.CITY_NAME);
            Log.e(TAG,"WATER--"+mCityName);
        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adviceText.setText("啊哈哈哈");
        }
    };

    private void getReceiver() {
        IntentFilter filter = new IntentFilter(
                HomeInfoFragment.ACTION_AQI_REFRESH);
        if (mActivity != null) {
            mActivity.registerReceiver(receiver, filter);
        }
    }
}
