package com.magus.enviroment.ep.fragment.attention;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.EnterpriseRateInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.fragment.attention.ui.HistogramCharView;
import com.magus.enviroment.ep.fragment.attention.ui.HistogramScaleView;
import com.magus.enviroment.global.log.Log;
import com.magus.magusutils.ContextUtil;
import com.magus.magusutils.DateUtil;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常总览上部分图
 * Created by pau
 * Packagename com.magus.enviroment.ep.fragment.attention
 * 2015-15/5/13-上午9:44.
 */
public class PreviewChartFragment extends BaseFragment {
    private static final String TAG="PreviewChartFragment";

    private View mRootView;

    private ImageView previousDay;
    private ImageView afterDay;

    private static int ID_PREVIOUS_DAY=0;
    private static int ID_AFTER_DAY=1;
    private HistogramCharView histogramCharView;
    private HistogramScaleView histogramScaleView;

    private String dayString;
    private List<EnterpriseRateInfo> mRateList= new ArrayList<EnterpriseRateInfo>();

    public static final String ACTION_CHANGE_DATE="com.action.change.date";
    public static final String KEY_DATE="KEY_DATE";//传递日期键值


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_attention_preview_chart, null);
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



    private void initView() {
        histogramCharView=null;
        histogramScaleView=null;
        dayString = DateUtil.getCurrentDay();
        histogramCharView = (HistogramCharView)mRootView.findViewById(R.id.histogram);
        histogramCharView.setNum((List<EnterpriseRateInfo>) SharedPreferenceUtil.getObject(SpKeyConstant.ENTERPRISE_RATE_LIST,ContextUtil.getContext()));
        histogramScaleView = (HistogramScaleView) mRootView.findViewById(R.id.scale);
        previousDay = (ImageView) mRootView.findViewById(R.id.previous_day);
        afterDay = (ImageView) mRootView.findViewById(R.id.after_day);
        previousDay.setOnClickListener(onClickListener);
        afterDay.setOnClickListener(onClickListener);
        sendRequest(dayString);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.previous_day:
                    changeDate(ID_PREVIOUS_DAY);
                    break;
                case R.id.after_day:
                    changeDate(ID_AFTER_DAY);
                    break;
                default:
                    break;
            }
        }
    };

    //改变日期，图表会发生变化
    private void changeDate(int id){
        if (id==ID_PREVIOUS_DAY) {
            dayString = DateUtil.getSpecifiedDayBefore(dayString);
            sendRequest(dayString);
            sendBroadCastReceiver(dayString);
        }else if (id==ID_AFTER_DAY){
            dayString = DateUtil.getSpecifiedDayAfter(dayString);
//            android.util.Log.e(TAG,dayString);
            if (dayString.compareTo(DateUtil.getCurrentDay())>0){ //比较日期 不能超过当前日期
                Toast.makeText(getActivity(),"超过当前日期",Toast.LENGTH_SHORT).show();
                dayString = DateUtil.getCurrentDay();
            }else {
                sendRequest(dayString);
                sendBroadCastReceiver(dayString);
            }
        }

    }

    //请求柱状图数据
    private void sendRequest(String day) {
        //?userId=18043&&alarmTime=2015-05-29

        String url = URLConstant.HEAD_URL+URLConstant.URL_ENTERPRISE_RATE + "?userId=" + MyApplication.mUid + "&alarmTime=" + day;
        Log.e(TAG,url);
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


    private void parseResponse(String response){

        Log.e(TAG,response);
        EnterpriseRateInfo.parseEnterpriseRateInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                //存到本地，给隔壁的表格用
                mRateList = (List<EnterpriseRateInfo>) list;
                SharedPreferenceUtil.saveObject(SpKeyConstant.ENTERPRISE_RATE_LIST, ContextUtil.getContext(), list);
                histogramCharView.setNum(mRateList);
                histogramCharView.invalidate();
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        Log.e(TAG,"ONRESUME");
    }

    /**
     * 刷新数据广播
     */
    private void sendBroadCastReceiver(String date) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        Intent intent = new Intent(ACTION_CHANGE_DATE);
        intent.putExtra(KEY_DATE,date);
        mActivity.sendBroadcast(intent);
    }
}
