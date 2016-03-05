package com.magus.enviroment.ep.fragment.attention;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.AttentionZone;
import com.magus.enviroment.ep.bean.ZoneDealRate;
import com.magus.enviroment.ep.bean.ZoneRateInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.magusutils.DateUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.JsonObjectRequest;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 折线图
 */
public class PreviewLineChartFragment extends BaseFragment {
    private View mRootView;
    private LineChart lineChart;
    private TextView chart_fail_tv;//加载失败
    //overList, stopList, lostList, stop2List
    private List<ZoneDealRate> mOverList = new ArrayList<ZoneDealRate>();//超标预警
    private List<ZoneDealRate> mStopList = new ArrayList<ZoneDealRate>();//机组停运
    private List<ZoneDealRate> mLostList = new ArrayList<ZoneDealRate>();//数据缺失
    private List<ZoneDealRate> mStop2List = new ArrayList<ZoneDealRate>();//治污设施停运
    private List<ZoneDealRate> mOver2List = new ArrayList<ZoneDealRate>();//超限报警
    private List<ZoneDealRate> mConstantList = new ArrayList<ZoneDealRate>();//数据恒定
    private LinearLayout mLoadingPage;//正在加载
    private LinearLayout barvchar_fail_page;//加载失败
    private ImageView previousCity;//前个城市
    private ImageView afterCity;//后个城市
    private List<AttentionZone> mZoneNameList = new ArrayList<AttentionZone>();//城市列表
    private int cityCountNum;//城市总数
    private int cityNum = 0;//记录当前城市
    public static final String ACTION_CHANGE_DATE = "com.action.change.line.data";
    public static final String KEY_CITY = "KEY_CITY";//传递城市键值
    private static int ID_PREVIOUS_CITY = 0;
    private static int ID_AFTER_CITY = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_preview_line_chart, null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    /**
     * 初始化控件
     */
    public void initView() {
        chart_fail_tv=(TextView)mRootView.findViewById(R.id.chart_fail_tv);
        lineChart = (LineChart) mRootView.findViewById(R.id.priview_linechart);
        mLoadingPage = (LinearLayout) mRootView.findViewById(R.id.linechart_loading_now);
        barvchar_fail_page = (LinearLayout) mRootView.findViewById(R.id.linechart_fail_page);
        barvchar_fail_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest(cityNum);
                barvchar_fail_page.setVisibility(View.GONE);
            }
        });
        previousCity = (ImageView) mRootView.findViewById(R.id.previous_day);
        afterCity = (ImageView) mRootView.findViewById(R.id.after_day);
        previousCity.setOnClickListener(onClickListener);
        afterCity.setOnClickListener(onClickListener);
        sendCityRequest();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.previous_day:
                    cityNum--;
                    changeDate(ID_PREVIOUS_CITY);
                    break;
                case R.id.after_day:
                    cityNum++;
                    changeDate(ID_AFTER_CITY);
                    break;
                default:
                    break;
            }
        }
    };

    //城市改变，图表会发生变化
    private void changeDate(int id) {
        if (id == ID_PREVIOUS_CITY) {
            if (cityNum < 0) {
                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                cityNum = 0;
            } else {
                sendRequest(cityNum);
            }


        } else if (id == ID_AFTER_CITY) {
            if (cityNum == cityCountNum) {
                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                cityNum = cityCountNum - 1;
            } else {
                sendRequest(cityNum);
            }
        }

    }

    /**
     * 刷新数据广播
     */
    private void sendBroadCastReceiver(String city) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        Intent intent = new Intent(ACTION_CHANGE_DATE);
        intent.putExtra(KEY_CITY, city);
        mActivity.sendBroadcast(intent);
    }

    /**
     * 发送数据请求
     */
    private void sendRequest(int cn) {
        mLoadingPage.setVisibility(View.VISIBLE);
//        previousCity.setVisibility(View.GONE);
//        afterCity.setVisibility(View.GONE);
        sendBroadCastReceiver(mZoneNameList.get(cn).getZoneName());
        //?userId=18043&&alarmTime=2015-05-29
        final String url = URLConstant.HEAD_URL + URLConstant.URL_ZONE_RATE + "?userId=" + MyApplication.mUid + "&zoneId=" + mZoneNameList.get(cn).getZoneId();
        com.magus.enviroment.global.log.Log.e("url", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadingPage.setVisibility(View.GONE);
                chart_fail_tv.setText("加载失败,请检查网络");
                barvchar_fail_page.setVisibility(View.VISIBLE);

            }
        });
        MyApplication.getRequestQueue().add(request);
    }


    private void parseResponse(String response) {
        ZoneRateInfo.parseZoneRateInfo(response, new RequestCallBack() {
            //overList, stopList, lostList, stop2List
            @Override
            public void onSuccess(List<?> overList, List<?> stopList, List<?> lostList, List<?> stop2List, List<?> Over2List, List<?> ConstantList) {
                super.onSuccess(overList, stopList, lostList, stop2List, Over2List, ConstantList);
                mOverList = (List<ZoneDealRate>) overList;
                mStopList = (List<ZoneDealRate>) stopList;
                mLostList = (List<ZoneDealRate>) lostList;
                mStop2List = (List<ZoneDealRate>) stop2List;
                mOver2List = (List<ZoneDealRate>) Over2List;
                mConstantList = (List<ZoneDealRate>) ConstantList;
                if (isAdded()) {
                    initChart();
                }
                int size=0;
                if(mOverList.size()>0){
                    size=mOverList.size();
                }
                if(mStopList.size()>0){
                    size=mStopList.size();
                }
                if(mLostList.size()>0){
                    size=mLostList.size();
                }
                if(mStop2List.size()>0){
                    size=mStop2List.size();
                }
                if(mOver2List.size()>0){
                    size=mOver2List.size();
                }
                if(mConstantList.size()>0){
                    size=mConstantList.size();
                }
                LineData data = new LineData(getxVals(size), getDataSet(mOverList, mStopList, mLostList, mStop2List, mOver2List, mConstantList));
                // 设置数据
                lineChart.setData(data);
                lineChart.setVisibleXRange(1, 15);
                lineChart.setVisibility(View.VISIBLE);
                mLoadingPage.setVisibility(View.GONE);
//                previousCity.setVisibility(View.VISIBLE);
//                afterCity.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed() {
                super.onFailed();
                mLoadingPage.setVisibility(View.GONE);
                chart_fail_tv.setText("没有数据");
                barvchar_fail_page.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 初始化折线图
     */
    public void initChart() {
        // 数据描述
        lineChart.setDescription("");
        lineChart.setGridBackgroundColor(getResources().getColor(android.R.color.transparent));
        // 动画
        lineChart.animateY(1000);
        // 设置是否可以触摸
        lineChart.setTouchEnabled(true);
        // 是否可以拖拽
        lineChart.setDragEnabled(true);
        // 是否可以缩放
        lineChart.setScaleEnabled(false);
        // 集双指缩放
        lineChart.setPinchZoom(false);
        // 隐藏右边的坐标轴
        lineChart.getAxisRight().setEnabled(false);
        // 隐藏左边的左边轴
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.setNoDataText("");

        Legend mLegend = lineChart.getLegend(); // 设置比例图标示
        // 设置窗体样式
        mLegend.setForm(Legend.LegendForm.SQUARE);
        // 字体
        mLegend.setFormSize(4f);
        // 字体颜色
        mLegend.setTextColor(Color.parseColor("#7e7e7e"));
        mLegend.setTextSize(6);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaxValue(100);
        leftAxis.setDrawGridLines(false);
        //格式化y轴数据格式，以百分号
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String a = new DecimalFormat("0").format(value);
                return a + "%";
            }
        });
        lineChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 解析List<EnterpriseRateInfo>数据  为折线图添加数据
     */
    public ArrayList<LineDataSet> getDataSet(List<ZoneDealRate> overList, List<ZoneDealRate> stopList, List<ZoneDealRate> lostList, List<ZoneDealRate> stop2List, List<ZoneDealRate> Over2List, List<ZoneDealRate> ConstantList) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        ArrayList<Entry> valueSet1 = new ArrayList<Entry>();
        ArrayList<Entry> valueSet2 = new ArrayList<Entry>();
        ArrayList<Entry> valueSet3 = new ArrayList<Entry>();
        ArrayList<Entry> valueSet4 = new ArrayList<Entry>();
        ArrayList<Entry> valueSet5 = new ArrayList<Entry>();
        ArrayList<Entry> valueSet6 = new ArrayList<Entry>();
        if (lostList.size() != 0) {
            for (int i = 0; i < lostList.size(); i++) {
                Entry entry = new Entry(Float.parseFloat(lostList.get(i).getRate()), i);
                valueSet1.add(entry);
            }
            LineDataSet barDataSet1 = new LineDataSet(valueSet1, "数据缺失");
            barDataSet1.setColor(Color.parseColor("#C0FF3E"));
            barDataSet1.setDrawValues(false);
            dataSets.add(barDataSet1);

        }
        if (stopList.size() != 0) {
            for (int i = 0; i < stopList.size(); i++) {
                Entry entry = new Entry(Float.parseFloat(stopList.get(i).getRate()), i);
                valueSet2.add(entry);
            }
            LineDataSet barDataSet2 = new LineDataSet(valueSet2, "机组停运");
            barDataSet2.setColor(Color.parseColor("#00FFFF"));
            barDataSet2.setDrawValues(false);
            dataSets.add(barDataSet2);

        }
        if (overList.size() != 0) {
            for (int i = 0; i < overList.size(); i++) {
                Entry entry = new Entry(Float.parseFloat(overList.get(i).getRate()), i);
                valueSet3.add(entry);
            }
            LineDataSet barDataSet3 = new LineDataSet(valueSet3, "超标报警");
            barDataSet3.setColor(Color.parseColor("#00FFFF"));
            barDataSet3.setDrawValues(false);
            dataSets.add(barDataSet3);

        }

        if (stop2List.size() != 0) {
            for (int i = 0; i < stop2List.size(); i++) {
                Entry entry = new Entry(Float.parseFloat(stop2List.get(i).getRate()), i);
                valueSet4.add(entry);
            }
            LineDataSet barDataSet4 = new LineDataSet(valueSet4, "治污设施停运");
            barDataSet4.setColor(Color.parseColor("#F26077"));
            barDataSet4.setDrawValues(false);
            dataSets.add(barDataSet4);

        }
        if (Over2List.size() != 0) {
            for (int i = 0; i < Over2List.size(); i++) {
                Entry entry = new Entry(Float.parseFloat(Over2List.get(i).getRate()), i);
                valueSet5.add(entry);
            }
            LineDataSet barDataSet5 = new LineDataSet(valueSet5, "超限报警");
            barDataSet5.setColor(Color.parseColor("#FFC0CB"));
            barDataSet5.setDrawValues(false);
            dataSets.add(barDataSet5);

        }
        if (ConstantList.size() != 0) {
            for (int i = 0; i < ConstantList.size(); i++) {
                Entry entry = new Entry(Float.parseFloat(ConstantList.get(i).getRate()), i);
                valueSet6.add(entry);
            }
            LineDataSet barDataSet6 = new LineDataSet(valueSet6, "数据恒定");
            barDataSet6.setColor(Color.parseColor("#9932CC"));
            barDataSet6.setDrawValues(false);
            dataSets.add(barDataSet6);

        }

        return dataSets;

    }

    /**
     * 获取x轴数据
     * @return
     */
    public List<String> getxVals(int size) {
        List<String> name = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            name.add(i + 1 + "日");
        }
        return name;

    }

    //请求城市
    private void sendCityRequest() {
        previousCity.setVisibility(View.GONE);
        afterCity.setVisibility(View.GONE);
        String url = URLConstant.HEAD_URL + URLConstant.URL_ATTENTION_ZONE + "?userId=" + MyApplication.mUid + "&roleId=" + MyApplication.mRoleId;
        //根据给定的URL新建一个请求
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        //获得请求数据之后先保存到数据库
                        AttentionZone.parseZoneInfo(response.toString(), new RequestCallBack() {
                            @Override
                            public void onFailed(String errorMessage) {
                                super.onFailed(errorMessage);
                            }

                            @Override
                            public void onSuccess(List<?> list) {
                                super.onSuccess(list);
                                mZoneNameList = (List<AttentionZone>) list;
                                cityCountNum = mZoneNameList.size();
                                sendRequest(cityNum);
                                previousCity.setVisibility(View.VISIBLE);
                                afterCity.setVisibility(View.VISIBLE);
                                //请求到之后在初始化
                                //initViewPager();
                            }
                        }, getActivity());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getRequestQueue().add(request);
    }
}
