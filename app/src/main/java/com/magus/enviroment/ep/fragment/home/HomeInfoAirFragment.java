package com.magus.enviroment.ep.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.HomeAirStationAdapter;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.AirInfoBean;
import com.magus.enviroment.ep.bean.AirInfoBeanTest;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.magusutils.UnitUtil;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 空气质量fragment
 * Created by pau
 * Packagename com.magus.enviroment.fragment.home
 * 2015-15/4/15-下午3:22.
 */
public class HomeInfoAirFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "HomeInfoAirFragment";
    private View mRootView;


    private LinearLayout mAqiInfoLayout;//首页布局
    private ListView mStationListView;//监测站列表

    private HomeAirStationAdapter mAdapter;
    private List<AirInfoBean> mAirInfoList = new ArrayList<AirInfoBean>();

    private AirInfoBeanTest airInfoBeanTest = new AirInfoBeanTest();

    private ScrollView mScrollView;
    private LinearLayout mLinearLayout; //存放列表

    private LinearLayout airRankBg;
    private TextView txtStation;//站点地址
    private TextView txtAqi1;//第一个aqi
    private TextView txtAqi2;//第二个aqi
    private TextView txtDate;//更新日期
    private TextView txtTime;//更新时间
    private TextView txtAdvice;//建议
    private TextView txtAqiLevel1;//第一个aqi等级
    private TextView txtAqiLevel2;//第二个aqi等级2
    private TextView txtMainPollution;//首要污染物
    private TextView txtPM10;//pm10
    private TextView txtPM25;//pm2.5
    private TextView txtSO2;//so2
    private TextView txtNO2;//no2
    private TextView txtCO;//co
    private TextView txtO3;//o3

    private String mCityName="wuxi";//用于请求空气质量指数
    private String mCacheAirInfo="";

    private PopupWindow mPopupWindow;

    private TextView mPollutionText;//站点列表污染物名称
    private LinearLayout home_pollution;//切换污染物名称

    //用于站点列表排序
    private static final String S_AQI = "S_AQI";
    private static final String S_PM25 = "S_PM25";
    private static final String S_PM10 = "S_PM10";
    private static final String S_O3 = "S_O3";
    private static final String S_SO2 = "S_SO2";
    private static final String S_NO2 = "S_NO2";
    private static final String S_CO = "S_CO";


    public static final String ACTION_CHANGE_BG = "com.action.change.bg";
    public static final String KEY_LEVEL = "key_level";
    public static final String KEY_CITY = "key_city";
    private int mLevelt=0;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_home_info_air, null);
            initData();
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
        getReceiver();
        Log.e(TAG, "ONRESUME");
       // sendBroadCastReceiver(mLevelt);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiver != null) {
            mActivity.unregisterReceiver(receiver);
        }
    }


    private void initData(){
        Bundle bundle = getArguments();
        if (bundle!=null){
            mCityName = bundle.getString(HomeInfoFragment.CITY_NAME);
        }
    }

    //缓存
    private void initCacheInfo(){
        mCacheAirInfo = SharedPreferenceUtil.get(mCityName,"");
        if (!mCacheAirInfo.equals("")){
            JSONObject object = null;
            try {
                object = new JSONObject(mCacheAirInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            parseResponse(object);
        }
    }

    private void initView() {
        initText();
        home_pollution=(LinearLayout)mRootView.findViewById(R.id.home_pollution);
        mPollutionText = (TextView) mRootView.findViewById(R.id.pollution_name);
        home_pollution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow();
            }
        });

        mLinearLayout = (LinearLayout) mRootView.findViewById(R.id.air_list_layout);
        mScrollView = (ScrollView) mRootView.findViewById(R.id.scroll);
        mAqiInfoLayout = (LinearLayout) mRootView.findViewById(R.id.air_info_main);
        mStationListView = (ListView) mRootView.findViewById(R.id.air_station_list);
        mAdapter = new HomeAirStationAdapter(mActivity, mAirInfoList);
        mStationListView.setAdapter(mAdapter);
        mStationListView.setOnItemClickListener(this);
        //设置第一页的位置。。。47：tabhost高度，71城市名称高度，30，文字padding高度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                MyApplication.mHeight - UnitUtil.dip2px(47 + 70 + 30));
        mAqiInfoLayout.setLayoutParams(params);
        //发送请求之前先使用缓存数据
        initCacheInfo();
        //发送数据请求
        sendRequest();
    }

    private void initText() {
        airRankBg = (LinearLayout) mRootView.findViewById(R.id.air_range_bg);
        txtStation = (TextView) mRootView.findViewById(R.id.station);
        txtAqi1 = (TextView) mRootView.findViewById(R.id.aqi1);
        txtAqi2 = (TextView) mRootView.findViewById(R.id.aqi2);
        txtDate = (TextView) mRootView.findViewById(R.id.date);
        txtTime = (TextView) mRootView.findViewById(R.id.time);
        txtAqiLevel1 = (TextView) mRootView.findViewById(R.id.aqi_level1);
        txtAqiLevel2 = (TextView) mRootView.findViewById(R.id.aqi_level2);
        txtMainPollution = (TextView) mRootView.findViewById(R.id.air_main_pollution);
        txtPM10 = (TextView) mRootView.findViewById(R.id.air_index_pm10);
        txtPM25 = (TextView) mRootView.findViewById(R.id.air_index_pm25);
        txtAdvice = (TextView) mRootView.findViewById(R.id.advice);
        txtSO2 = (TextView) mRootView.findViewById(R.id.air_index_so2);
        txtNO2 = (TextView) mRootView.findViewById(R.id.air_index_no2);
        txtCO = (TextView) mRootView.findViewById(R.id.air_index_co);
        txtO3 = (TextView) mRootView.findViewById(R.id.air_index_o3);
    }


    //volley网络请求
    private void sendRequest() {
        String url = null;
//        try {
//            url = URLConstant.URL_AIR_STATION_INFO + URLEncoder.encode("'"+mCityName+"'", "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

            //url =URLConstant.HEAD_URL+ URLConstant.URL_AIR_STATION_INFO +"?cityName="+ mCityName;
                url =URLConstant.TEST_URL;
        //根据给定的URL新建一个请求
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //获得请求数据之后先保存到数据库
                        Log.d("response",response.toString());
                        //SharedPreferenceUtil.save(mCityName, response.toString());

                        parseResponse(response);
                        Toast.makeText(mActivity, "网络请求成功", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mActivity, "网络请求失败", Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    /**
     * 解析
     * @param response
     */
    private void parseResponse(JSONObject response) {

        AirInfoBeanTest.test_paresAirInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(Object object) {
                airInfoBeanTest = (AirInfoBeanTest) object;
                txtAqi1.setText(airInfoBeanTest.getTest_aqi());
            }
        });

//        AirInfoBean.parseAirInfo(response, new RequestCallBack() {
//            @Override
//            public void onSuccess(List<?> list) {
//                mAirInfoList = (List<AirInfoBean>) list;
//                if (mLinearLayout.getChildCount() > 0) {
//                    mLinearLayout.removeAllViews();
//                }
//
//                //循环添加站点信息，原来用listview 与scrollview会有冲突
//                for (int i = 0; i < mAirInfoList.size(); i++) {
//                    LayoutInflater inflater = LayoutInflater.from(mActivity);
//                    View root = inflater.inflate(R.layout.item_air_staion_layout, null);
//                    TextView number = (TextView) root.findViewById(R.id.air_num);
//                    TextView station = (TextView) root.findViewById(R.id.air_station);
//                    TextView aqi = (TextView) root.findViewById(R.id.air_aqi);
//                    number.setText((i+1)+ "");
//                    if(!"null".equals(mAirInfoList.get(i).getPosition_name())){
//                        station.setText(mAirInfoList.get(i).getPosition_name());
//                    }else{
//                        station.setText(mAirInfoList.get(i).getArea());
//                    }
//
//                    aqi.setText(mAirInfoList.get(i).getAqi());
//                    int aqiIndex = Integer.parseInt(mAirInfoList.get(i).getAqi().trim());
//                    if (aqiIndex >=0 && aqiIndex <= 50) {
//                        aqi.setBackgroundResource(R.drawable.air_range_sq_bg_1);
//
//                    } else if (aqiIndex > 50 && aqiIndex <= 100) {
//                        aqi.setBackgroundResource(R.drawable.air_range_sq_bg_2);
//
//                    } else if (aqiIndex > 100 && aqiIndex <= 150) {
//                        aqi.setBackgroundResource(R.drawable.air_range_sq_bg_3);
//
//                    } else if (aqiIndex > 150 && aqiIndex <= 200) {
//                        aqi.setBackgroundResource(R.drawable.air_range_sq_bg_4);
//
//                    } else if (aqiIndex > 200 && aqiIndex <= 300) {
//                        aqi.setBackgroundResource(R.drawable.air_range_sq_bg_5);
//
//                    } else {
//                        aqi.setBackgroundResource(R.drawable.air_range_sq_bg_6);
//                    }
//                    root.setOnClickListener(onClickListener);
//                    mLinearLayout.addView(root);
//
//                }
////
////                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UnitUtil.dip2px(50) * mAirInfoList.size());
////                mStationListView.setLayoutParams(params);
////                mAdapter.setList(mAirInfoList);
//                //判断一下防止请求成功但是没有数据
//                if (mAirInfoList.size() > 0) {
//                    AirInfoBean airInfoBean = mAirInfoList.get(0);
//                    reset(airInfoBean);
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailed() {
////                Toast.makeText(mActivity, "网络请求失败", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            for (int i=0;i<mAirInfoList.size();i++){
//                if (v==mLinearLayout.getChildAt(i)){
//                    AirInfoBean airInfoBean = mAirInfoList.get(i);
//                    reset(airInfoBean);
//                    mScrollView.fullScroll(ScrollView.FOCUS_UP);
//                }
//            }
//        }
//    };


    //初始化popupWindow
    private void initPopWindow(){
        //showAsDropDown(View anchor)：相对某个控件的位置（正左下方），无偏移
        //showAsDropDown(View anchor, int xoff, int yoff)：相对某个控件的位置，有偏移
        //showAtLocation(View parent, int gravity, int x, int y)：相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        View popView = mLayoutInflater.inflate(R.layout.aqi_popup_window_layout, null);
        mPopupWindow = new PopupWindow(popView, MyApplication.mWidth/3, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置popupwindow动画
//        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        TextView aqi = (TextView) popView.findViewById(R.id.pop_aqi);
        aqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetStationList(S_AQI);
                mPopupWindow.dismiss();
            }
        });
        TextView pm25 = (TextView) popView.findViewById(R.id.pop_pm25);
        pm25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetStationList(S_PM25);
                mPopupWindow.dismiss();
            }
        });
        TextView pm10 = (TextView) popView.findViewById(R.id.pop_pm10);
        pm10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetStationList(S_PM10);
                mPopupWindow.dismiss();
            }
        });
        TextView o3 = (TextView) popView.findViewById(R.id.pop_o3);
        o3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetStationList(S_O3);
                mPopupWindow.dismiss();
            }
        });
        TextView so2 = (TextView) popView.findViewById(R.id.pop_so2);
        so2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetStationList(S_SO2);
                mPopupWindow.dismiss();
            }
        });
        TextView no2 = (TextView) popView.findViewById(R.id.pop_no2);
        no2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // resetStationList(S_NO2);
                mPopupWindow.dismiss();
            }
        });
        TextView co = (TextView) popView.findViewById(R.id.pop_co);
        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetStationList(S_CO);
                mPopupWindow.dismiss();
            }
        });


        // 需要设置一下此参数，点击外边可消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        mPopupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(mPollutionText);

    }
//    private void resetStationList(String pNname){
//        if (pNname.equals(S_AQI)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getAqi());
//                mPollutionText.setText("AQI");
//            }
//        }else if (pNname.equals(S_PM25)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getPm25());
//                mPollutionText.setText("PM2.5");
//            }
//        }else if (pNname.equals(S_PM10)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getPm10());
//                mPollutionText.setText("PM10");
//            }
//        }else if (pNname.equals(S_O3)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getO3());
//                mPollutionText.setText("03");
//            }
//        }else if (pNname.equals(S_SO2)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getSo2());
//                mPollutionText.setText("SO2");
//            }
//        }else if (pNname.equals(S_NO2)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getNo2());
//                mPollutionText.setText("NO2");
//            }
//        }else if (pNname.equals(S_CO)){
//            for (int i=0;i<mAirInfoList.size();i++){
//                View rootView = mLinearLayout.getChildAt(i);
//                TextView tv = (TextView) rootView.findViewById(R.id.air_aqi);
//                tv.setText(mAirInfoList.get(i).getCo());
//                mPollutionText.setText("CO");
//            }
//        }
//    }

    /**
     * 设置数据
     *
     * @param airInfoBean
     */
    private void reset(AirInfoBean airInfoBean) {
        resetAirIndex(airInfoBean.getAqi(), airInfoBean.getDateTime(), airInfoBean.getHourTime(), airInfoBean.getQuality(), airInfoBean.getPrimaryPollutant(),
                airInfoBean.getPm10(), airInfoBean.getPm25(), airInfoBean.getSo2(), airInfoBean.getNo2()
                , airInfoBean.getCo(), airInfoBean.getO3(), airInfoBean.getAdvice(), airInfoBean.getPosition_name());
    }
    private  void  reset_test(AirInfoBeanTest air){
        resetAirIndes_test(air.getTest_aqi(),air.getTest_date(),air.getTest_time(),air.getTest_quality());
    }
    private void resetAirIndes_test(String aqi,String date,String time,String rank){
        txtAqi1.setText(aqi);
        txtAqi2.setText(aqi);
        int aqiIndex = Integer.parseInt(aqi);
        txtDate.setText(date);
        String releaseTime=time.replaceAll("[T]", " ");
        releaseTime=releaseTime.replaceAll("[Z]", " ");
        txtTime.setText(releaseTime + " 发布");

        if (aqiIndex >= 0 && aqiIndex <= 50) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_1);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_1);
            //sendBroadCastReceiver(1);
            mScrollView.setBackgroundResource(R.mipmap.home_bg);
            mLevelt =1;
        } else if (aqiIndex > 50 && aqiIndex <= 100) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_2);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_2);
            // sendBroadCastReceiver(2);
            mScrollView.setBackgroundResource(R.mipmap.home_bg2);
            mLevelt =2;
        } else if (aqiIndex > 100 && aqiIndex <= 150) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_3);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_3);
            mScrollView.setBackgroundResource(R.mipmap.home_bg3);
            // sendBroadCastReceiver(3);
            mLevelt =3;
        } else if (aqiIndex > 150 && aqiIndex <= 200) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_4);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_4);
            mScrollView.setBackgroundResource(R.mipmap.home_bg4);
            // sendBroadCastReceiver(4);
            mLevelt =4;
        } else if (aqiIndex > 200 && aqiIndex <= 300) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_5);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_5);
            mScrollView.setBackgroundResource(R.mipmap.home_bg5);
            //  sendBroadCastReceiver(5);
            mLevelt =5;
        } else {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_6);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_6);
            mScrollView.setBackgroundResource(R.mipmap.home_bg6);
            //  sendBroadCastReceiver(6);
            mLevelt =6;
        }
        txtAqiLevel1.setText(rank);
        txtAqiLevel2.setText(rank);
    }

    //设置数据
    private void resetAirIndex(String aqi, String date, String time, String rank, String mainPollution,
                               String pm10, String pm25, String so2, String no2, String co, String o3, String advice, String station) {
        txtAqi1.setText(aqi);
        txtAqi2.setText(aqi);
        int aqiIndex = Integer.parseInt(aqi);
        txtDate.setText(date);
        String releaseTime=time.replaceAll("[T]", " ");
        releaseTime=releaseTime.replaceAll("[Z]", " ");
        txtTime.setText(releaseTime + " 发布");
        if (aqiIndex >= 0 && aqiIndex <= 50) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_1);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_1);
            //sendBroadCastReceiver(1);
            mScrollView.setBackgroundResource(R.mipmap.home_bg);
            mLevelt =1;
        } else if (aqiIndex > 50 && aqiIndex <= 100) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_2);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_2);
           // sendBroadCastReceiver(2);
            mScrollView.setBackgroundResource(R.mipmap.home_bg2);
            mLevelt =2;
        } else if (aqiIndex > 100 && aqiIndex <= 150) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_3);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_3);
            mScrollView.setBackgroundResource(R.mipmap.home_bg3);
           // sendBroadCastReceiver(3);
            mLevelt =3;
        } else if (aqiIndex > 150 && aqiIndex <= 200) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_4);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_4);
            mScrollView.setBackgroundResource(R.mipmap.home_bg4);
           // sendBroadCastReceiver(4);
            mLevelt =4;
        } else if (aqiIndex > 200 && aqiIndex <= 300) {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_5);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_5);
            mScrollView.setBackgroundResource(R.mipmap.home_bg5);
          //  sendBroadCastReceiver(5);
            mLevelt =5;
        } else {
            txtAqiLevel1.setBackgroundResource(R.drawable.air_range_sq_bg_6);
            airRankBg.setBackgroundResource(R.drawable.air_range_bg_6);
            mScrollView.setBackgroundResource(R.mipmap.home_bg6);
          //  sendBroadCastReceiver(6);
            mLevelt =6;
        }
        txtAqiLevel1.setText(rank);
        txtAqiLevel2.setText(rank);
        txtMainPollution.setText(mainPollution);
        txtPM10.setText(pm10 + "μg/m³");
        txtPM25.setText(pm25 + "μg/m³");
        txtSO2.setText(so2 + "μg/m³");
        txtNO2.setText(no2 + "μg/m³");
        txtCO.setText(co + "mg/m³");
        txtO3.setText(o3 + "μg/m³");
        //airInfoBeanTest.getTest_advice()
        txtAdvice.setText(airInfoBeanTest.getTest_advice());
        if(!"null".equals(station)){
            txtStation.setText(station);
        }else{
            txtStation.setText("");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // reset(mAirInfoList.get(position));
        reset_test(airInfoBeanTest);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    /**
     * 广播接收更新刷新数据
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendRequest();
        }
    };

    private void getReceiver() {
        IntentFilter filter = new IntentFilter(
                HomeInfoFragment.ACTION_AQI_REFRESH);
        if (mActivity != null) {
            mActivity.registerReceiver(receiver, filter);
        }
    }
    /**
     * 改变背景
     */
//    private void sendBroadCastReceiver(int level) {
//        if (mActivity == null) {
//            mActivity = getActivity();
//        }
//        Intent intent = new Intent(ACTION_CHANGE_BG);
//        intent.putExtra(KEY_LEVEL,level);
//        intent.putExtra(KEY_CITY,mCityName);
//        mActivity.sendBroadcast(intent);
//    }
}
