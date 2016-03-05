//package com.magus.enviroment.ep.fragment.attention;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.magus.enviroment.R;
//import com.magus.enviroment.ep.MyApplication;
//import com.magus.enviroment.ep.base.BaseFragment;
//import com.magus.enviroment.ep.bean.AttentionZone;
//import com.magus.enviroment.ep.bean.EnterpriseRateInfo;
//import com.magus.enviroment.ep.callback.RequestCallBack;
//import com.magus.enviroment.ep.constant.SpKeyConstant;
//import com.magus.enviroment.ep.constant.URLConstant;
//import com.magus.enviroment.ep.fragment.home.HomeInfoFragment;
//import com.magus.magusutils.ContextUtil;
//import com.magus.magusutils.DateUtil;
//import com.magus.magusutils.SharedPreferenceUtil;
//import com.magus.volley.Request;
//import com.magus.volley.Response;
//import com.magus.volley.VolleyError;
//import com.magus.volley.toolbox.JsonObjectRequest;
//import com.magus.volley.toolbox.StringRequest;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * 异常总览
// * Created by pau
// * Packagename com.magus.enviroment.ep.fragment.attention
// * 2015-15/5/10-上午9:44.
// */
//public class PreviewFragment extends BaseFragment {
//    private View mRootView;
//    private static final String TAG = "AttentionPreview";
//
//    private ViewPager mViewPager;
//    private MyViewPagerAdapter mPagerAdapter;
//    private ArrayList<Fragment> mViewPagerChildList = new ArrayList<Fragment>();
//    private int mLastItemIndex = -1;//最后一次选中的fragment index
//    private int defaultPageSize = 1;//默认显示页数
//
//    private FragmentManager mFragmentManager;
//    private PreviewChartFragment mChartFragment;//柱状图
//    private PreviewDiagramFragment mDiagramFragment;//表格
//    private PreviewCurveFragment mCurveFragment;//折线图
//
//    private static final int ID_CHART_FRAGMENT = 0;
//    private static final int ID_DIAGRAM_FRAGMENT = 1;
//
//    private Button btnChart;
//    private Button btnDiagram;
//
//
//    private List<AttentionZone> mZoneNameList = new ArrayList<AttentionZone>();
//    private List<EnterpriseRateInfo> mRateList = new ArrayList<EnterpriseRateInfo>();
//
//    private TextView txtChartTitle;//柱状图标题
//    private TextView txtCurveTitle;//折线图标题
//
//    private LinearLayout mFailPage;
//    private LinearLayout mLoadingPage;
//
//
//    private LinearLayout mFailPage2;
//    private LinearLayout mLoadingPage2;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mRootView == null) {
//            mRootView = inflater.inflate(R.layout.fragment_attention_preview, null);
//        }
//        ViewGroup parent = (ViewGroup) mRootView.getParent();
//        if (parent != null) {
//            parent.removeView(mRootView);
//        }
//
//        return mRootView;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        init();
//    }
//
//    private void init() {
//        initView();
////        sendCityRequest();//发送城市列表，用于初始化下面的viewpager
//        sendChartRequest(DateUtil.getCurrentDay());//发送当前柱状图数据，用于初始化柱状图
//    }
//
//    private void initView() {
//
//        mFailPage = (LinearLayout) mRootView.findViewById(R.id.ll_fail_page);
//        mLoadingPage = (LinearLayout) mRootView.findViewById(R.id.ll_loading_now);
//
//        mFailPage2 = (LinearLayout) mRootView.findViewById(R.id.ll_fail_page2);
//        mLoadingPage2 = (LinearLayout) mRootView.findViewById(R.id.ll_loading_now2);
//
//
//
//        mFailPage2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFailPage2.setVisibility(View.GONE);
//                sendCityRequest();
//            }
//        });
//        mFailPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFailPage.setVisibility(View.GONE);
//                sendChartRequest(DateUtil.getCurrentDay());
//            }
//        });
//
//
//        mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);
//        mFragmentManager = getChildFragmentManager();
//        btnChart = (Button) mRootView.findViewById(R.id.chart);
//        btnDiagram = (Button) mRootView.findViewById(R.id.diagram);
//        btnChart.setOnClickListener(onClickListener);
//        btnDiagram.setOnClickListener(onClickListener);
//        txtChartTitle = (TextView) mRootView.findViewById(R.id.chart_title);
//        txtChartTitle.setText(DateUtil.getCurrentDay() + "异常处理率");
//        txtCurveTitle = (TextView) mRootView.findViewById(R.id.curve_title);
//    }
//
//
//    //初始化柱状图
//    private void initTopFragment() {
//        setSelected(btnChart);
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        mChartFragment = new PreviewChartFragment();
//        transaction.replace(R.id.chart_content, mChartFragment);
//        transaction.commitAllowingStateLoss();
//        mLoadingPage.setVisibility(View.GONE);
//    }
//
//
//    //第二部分折线图就在这儿初始化
//    private void initViewPager() {
//        mViewPagerChildList.clear();
//
//        for (int i = 0; i < mZoneNameList.size(); i++) {
//            //初始化fragment
//            PreviewCurveFragment mInfoFragment = new PreviewCurveFragment();
//            mInfoFragment.setCity(mZoneNameList.get(i).getZoneId());
//            mViewPagerChildList.add(mInfoFragment);
//        }
//        if (mZoneNameList.size() > 0) {
//            setCurveTitle(mZoneNameList.get(0).getZoneName());//设置标题名字
//        }
//        mPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
//        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setOnPageChangeListener(mPageChangeListener);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getReceiver();
//        sendCityRequest();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (receiver != null) {
//            getActivity().unregisterReceiver(receiver);
//        }
//    }
//
//    //图标切换监听事件
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()) {
//                case R.id.chart:
//                    changeFragment(ID_CHART_FRAGMENT);
//                    break;
//                case R.id.diagram:
//                    changeFragment(ID_DIAGRAM_FRAGMENT);
//                    break;
//            }
//        }
//    };
//
//    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            mLastItemIndex = position;
//            setCurveTitle(mZoneNameList.get(position).getZoneName());
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };
//
//    //viewpager适配器
//    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        public MyViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mViewPagerChildList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mViewPagerChildList.size();
//        }
//    }
//
//    private void changeFragment(int id) {
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//
//        if (id == ID_CHART_FRAGMENT) {
//            if (mChartFragment == null) {
//                mChartFragment = new PreviewChartFragment();
//            }
//            transaction.replace(R.id.chart_content, mChartFragment);
//            setSelected(btnChart);
//        } else if (id == ID_DIAGRAM_FRAGMENT) {
//            if (mDiagramFragment == null) {
//                mDiagramFragment = new PreviewDiagramFragment();
//            }
//            transaction.replace(R.id.chart_content, mDiagramFragment);
//            setSelected(btnDiagram);
//        }
//        transaction.commit();
//
//    }
//
//    private void setSelected(Button button) {
//        if (button == btnChart) {
//            btnChart.setSelected(true);
//            btnDiagram.setSelected(false);
//        } else {
//            btnChart.setSelected(false);
//            btnDiagram.setSelected(true);
//        }
//    }
//
//
//    //请求城市
//    private void sendCityRequest() {
//        mLoadingPage2.setVisibility(View.VISIBLE);
//        String url = URLConstant.URL_ATTENTION_ZONE + "?userId=" + MyApplication.mUid + "&roleId=" + MyApplication.mRoleId;
//        Log.e(TAG, url);
//        //根据给定的URL新建一个请求
//        JsonObjectRequest request = new JsonObjectRequest(url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //获得请求数据之后先保存到数据库
//                        AttentionZone.parseZoneInfo(response.toString(), new RequestCallBack() {
//                            @Override
//                            public void onFailed(String errorMessage) {
//                                super.onFailed(errorMessage);
//                            }
//
//                            @Override
//                            public void onSuccess(List<?> list) {
//                                super.onSuccess(list);
//                                mZoneNameList = (List<AttentionZone>) list;
//                                //请求到之后在初始化
//                                initViewPager();
//                                mLoadingPage2.setVisibility(View.GONE);
//                            }
//                        }, getActivity());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mFailPage2.setVisibility(View.VISIBLE);
//                mLoadingPage2.setVisibility(View.GONE);
//            }
//        });
//
//        MyApplication.getRequestQueue().add(request);
//    }
//
//
//    //请求柱状图数据
//    private void sendChartRequest(String day) {
//        mLoadingPage.setVisibility(View.VISIBLE);
//        //?userId=18043&&alarmTime=2015-05-29
//        String url = URLConstant.URL_ENTERPRISE_RATE + "?userId=" + MyApplication.mUid + "&alarmTime=" + day;
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                EnterpriseRateInfo.parseEnterpriseRateInfo(response, new RequestCallBack() {
//                    @Override
//                    public void onSuccess(List<?> list) {
//                        super.onSuccess(list);
//                        //存到本地，给隔壁的表格用
//                        mRateList = (List<EnterpriseRateInfo>) list;
//                        SharedPreferenceUtil.saveObject(SpKeyConstant.ENTERPRISE_RATE_LIST, ContextUtil.getContext(), list);
//                        //保存数据完了之后在初始化
//                        initTopFragment();
//                    }
//
//                    @Override
//                    public void onFailed() {
//                        super.onFailed();
//                    }
//                });
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mLoadingPage.setVisibility(View.GONE);
//                mFailPage.setVisibility(View.VISIBLE);
//            }
//        });
//        MyApplication.getRequestQueue().add(request);
//    }
//
//
//    /**
//     * 广播接收更新刷新数据
//     */
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String date = intent.getStringExtra(PreviewChartFragment.KEY_DATE);
//            txtChartTitle.setText(date + "异常处理率");
//        }
//    };
//
//    private void getReceiver() {
//        IntentFilter filter = new IntentFilter(
//                PreviewChartFragment.ACTION_CHANGE_DATE);
//        if (getActivity() != null) {
//            getActivity().registerReceiver(receiver, filter);
//        }
//
//
//    }
//
//    private void setCurveTitle(String zoneName) {
//
//        txtCurveTitle.setText(zoneName + DateUtil.getLastMonth() + "异常处理率");//设置折线图标题
//    }
//}
