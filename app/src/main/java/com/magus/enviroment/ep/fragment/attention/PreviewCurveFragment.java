//package com.magus.enviroment.ep.fragment.attention;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.magus.enviroment.R;
//import com.magus.enviroment.ep.MyApplication;
//import com.magus.enviroment.ep.base.BaseFragment;
//import com.magus.enviroment.ep.bean.ZoneDealRate;
//import com.magus.enviroment.ep.bean.ZoneRateInfo;
//import com.magus.enviroment.ep.callback.RequestCallBack;
//import com.magus.enviroment.ep.constant.URLConstant;
//import com.magus.enviroment.ep.fragment.attention.ui.CurveView;
//import com.magus.enviroment.global.log.Log;
//import com.magus.magusutils.DateUtil;
//import com.magus.volley.Request;
//import com.magus.volley.Response;
//import com.magus.volley.VolleyError;
//import com.magus.volley.toolbox.StringRequest;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 异常总览下部分图
// * Created by pau
// * Packagename com.magus.enviroment.ep.fragment.attention
// * 2015-15/5/13-上午9:44.
// */
//public class PreviewCurveFragment extends BaseFragment {
//    private View mRootView;
//    private CurveView mCurveView;
//
//    //overList, stopList, lostList, stop2List
//    private List<ZoneDealRate> mOverList = new ArrayList<ZoneDealRate>();
//    private List<ZoneDealRate> mStopList = new ArrayList<ZoneDealRate>();
//    private List<ZoneDealRate> mLostList = new ArrayList<ZoneDealRate>();
//    private List<ZoneDealRate> mStop2List = new ArrayList<ZoneDealRate>();
//
//    private String mCityCode="";
//
//    private TextView mLastDay;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mRootView == null) {
//            mRootView = inflater.inflate(R.layout.fragment_attention_preview_curve, null);
//        }
//        ViewGroup parent = (ViewGroup) mRootView.getParent();
//        if (parent != null) {
//            parent.removeView(mRootView);
//        }
//
//        return mRootView;
//    }
//
//    public void setCity(String cityCode){
//        mCityCode = cityCode;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        intiCurveView();
//        sendRequest();
//    }
//
//    private void intiCurveView(){
//        mCurveView = (CurveView) mRootView.findViewById(R.id.curve);
//        mLastDay = (TextView) mRootView.findViewById(R.id.last_day);
//        mLastDay.setText(DateUtil.getLastDayOfMonth()+"日");
//
//    }
//
//
//    /**
//     * 发送数据请求
//     */
//    private void sendRequest() {
//        //?userId=18043&&alarmTime=2015-05-29
//        String url = URLConstant.URL_ZONE_RATE + "?userId=" + MyApplication.mUid + "&alarmTime=" + DateUtil.getCurrentDay()+"&zoneId="+mCityCode;
//        Log.e("url",url);
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("******88*"+response);
//                parseResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        MyApplication.getRequestQueue().add(request);
//    }
//
//
//    private void parseResponse(String response){
//        ZoneRateInfo.parseZoneRateInfo(response, new RequestCallBack() {
//            //overList, stopList, lostList, stop2List
//            @Override
//            public void onSuccess(List<?> overList, List<?> stopList, List<?> lostList, List<?> stop2List) {
//                super.onSuccess(overList, stopList, lostList, stop2List);
//                mOverList = (List<ZoneDealRate>) overList;
//                mStopList = (List<ZoneDealRate>) stopList;
//                mLostList = (List<ZoneDealRate>) lostList;
//                mStop2List = (List<ZoneDealRate>) stop2List;
//                mCurveView.setList(mOverList, mStopList, mLostList, mStop2List);
//                mCurveView.invalidate();
//            }
//
//            @Override
//            public void onFailed() {
//                super.onFailed();
//            }
//        });
//    }
//
//
//
//}
