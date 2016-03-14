package com.magus.enviroment.ep.fragment.attention.ui;

import com.magus.enviroment.ep.base.BaseFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.AlarmGridPercentage;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.magusutils.DateUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class PreviewTabDayFragmeng extends BaseFragment {

    private View mRootView;
    private BarChart chart;
    private TextView chart_fail_tv;//加载失败
    private List<AlarmGridPercentage> mRateList = new ArrayList<AlarmGridPercentage>();
    private LinearLayout mLoadingPage;//正在加载
    private LinearLayout barvchar_fail_page;//加载失败
    private ImageView previousDay;//前一天
    private ImageView afterDay;//后一天
    private static int ID_PREVIOUS_DAY=0;
    private static int ID_AFTER_DAY=1;
    private String dayString;
    public static final String ACTION_CHANGE_DATE="com.action.change.date";
    public static final String ACTION_CHANGE_TABLE="com.action.change.table";
    public static final String KEY_DATE="KEY_DATE";//传递日期键值
    private boolean tableshow=false;

    private TextView txtTabDayDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_preview_tabday, null);

        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;

    }

    public void initView(){

        //chart_fail_tv=(TextView)mRootView.findViewById(R.id.chart_fail_tv);
        dayString = DateUtil.getSpecifiedDayBefore(DateUtil.getCurrentDay());
        // mLoadingPage = (LinearLayout) mRootView.findViewById(R.id.barvchar_loading_now);
        previousDay = (ImageView) mRootView.findViewById(R.id.previous_day);
        afterDay = (ImageView) mRootView.findViewById(R.id.after_day);
        previousDay.setOnClickListener(onClickListener);
        afterDay.setOnClickListener(onClickListener);
        //sendRequest(dayString);

        txtTabDayDate = (TextView) mRootView.findViewById(R.id.tabday_date);
        txtTabDayDate.setText(dayString);
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
            txtTabDayDate.setText(dayString);
        }
    };

    //改变日期，图表会发生变化
    private void changeDate(int id){
        if (id==ID_PREVIOUS_DAY) {
            dayString = DateUtil.getSpecifiedDayBefore(dayString);
            sendRequest(dayString);
            txtTabDayDate.setText(dayString);

        }else if (id==ID_AFTER_DAY){
            dayString = DateUtil.getSpecifiedDayAfter(dayString);
//            android.util.Log.e(TAG,dayString);
            if (dayString.compareTo(DateUtil.getCurrentDay())>0){ //比较日期 不能超过当前日期
                Toast.makeText(getActivity(), "超过当前日期", Toast.LENGTH_SHORT).show();
                dayString = DateUtil.getCurrentDay();
            }else {
                sendRequest(dayString);
                txtTabDayDate .setText(dayString);
            }
        }

    }

    /**
     * 刷新数据广播
     */
    private void sendBroadCastReceiver(String date) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        Intent intent = new Intent(ACTION_CHANGE_DATE);
        intent.putExtra(KEY_DATE, date);
        mActivity.sendBroadcast(intent);

    }
    /**
     * 刷新数据广播
     */
    private void sendBroadCastReceiverTable(boolean tableshow) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        Intent intent = new Intent(ACTION_CHANGE_TABLE);
        intent.putExtra("tableshow", tableshow);
        mActivity.sendBroadcast(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


    private void sendRequest(final String day) {
        //chart.setVisibility(View.GONE);
        sendBroadCastReceiver(day);
        //barvchar_fail_page.setVisibility(View.GONE);
        //mLoadingPage.setVisibility(View.VISIBLE);
        String url = URLConstant.HEAD_URL+URLConstant.URL_ENTERPRISE_RATE + "?userId=" + MyApplication.mUid + "&time=" + dayString;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mLoadingPage.setVisibility(View.GONE);
//                barvchar_fail_page.setVisibility(View.VISIBLE);
//                tableshow=false;
//                chart_fail_tv.setText("加载失败,请检查网络");
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response) {
    }

}
