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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.BaseFragment;
import com.magus.enviroment.ep.bean.AlarmGridPercentage;
import com.magus.enviroment.ep.bean.DealRate;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.SpKeyConstant;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.magusutils.ContextUtil;
import com.magus.magusutils.DateUtil;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuer on 2015/10/16.
 */
public class PreviewBarChartFragement extends BaseFragment {
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

    private TextView txtChartDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_preview_barchart, null);

        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    public void initView() {
        chart_fail_tv=(TextView)mRootView.findViewById(R.id.chart_fail_tv);
        dayString = DateUtil.getSpecifiedDayBefore(DateUtil.getCurrentDay());
        chart = (BarChart) mRootView.findViewById(R.id.priview_barchart);
        mLoadingPage = (LinearLayout) mRootView.findViewById(R.id.barvchar_loading_now);
        barvchar_fail_page = (LinearLayout) mRootView.findViewById(R.id.barvchar_fail_page);
        barvchar_fail_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(dayString);
                txtChartDate.setText(dayString);
            }
        });
        previousDay = (ImageView) mRootView.findViewById(R.id.previous_day);
        afterDay = (ImageView) mRootView.findViewById(R.id.after_day);
        previousDay.setOnClickListener(onClickListener);
        afterDay.setOnClickListener(onClickListener);
        sendRequest(dayString);

        txtChartDate = (TextView) mRootView.findViewById(R.id.chart_date);
        txtChartDate.setText(dayString);



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
            txtChartDate.setText(dayString);
        }
    };
    //改变日期，图表会发生变化
    private void changeDate(int id){
        if (id==ID_PREVIOUS_DAY) {
            dayString = DateUtil.getSpecifiedDayBefore(dayString);
            sendRequest(dayString);
            txtChartDate.setText(dayString);

        }else if (id==ID_AFTER_DAY){
            dayString = DateUtil.getSpecifiedDayAfter(dayString);
//            android.util.Log.e(TAG,dayString);
            if (dayString.compareTo(DateUtil.getCurrentDay())>0){ //比较日期 不能超过当前日期
                Toast.makeText(getActivity(),"超过当前日期",Toast.LENGTH_SHORT).show();
                dayString = DateUtil.getCurrentDay();
            }else {
                sendRequest(dayString);
                txtChartDate.setText(dayString);
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


    /**
     * 初始化柱状图
     */
    public void initChart() {
        // 数据描述
        chart.setDescription("");
        chart.setGridBackgroundColor(getResources().getColor(android.R.color.transparent));
        // 动画
        chart.animateY(1000);
        // 设置是否可以触摸
        chart.setTouchEnabled(true);
        // 是否可以拖拽
        chart.setDragEnabled(true);
        // 是否可以缩放
        chart.setScaleEnabled(false);
        // 集双指缩放
        chart.setPinchZoom(false);
        // 隐藏右边的坐标轴
        chart.getAxisRight().setEnabled(false);
        // 隐藏左边的左边轴
        chart.getAxisLeft().setEnabled(true);
        chart.setNoDataText("");
        Legend mLegend = chart.getLegend(); // 设置比例图标示
        // 设置窗体样式
        mLegend.setForm(Legend.LegendForm.SQUARE);
        // 字体
        mLegend.setFormSize(4f);
        // 字体颜色
        mLegend.setTextColor(Color.parseColor("#7e7e7e"));
        mLegend.setTextSize(6);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);

        YAxis leftAxis = chart.getAxisLeft();
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
        chart.invalidate();
    }
    @Override
    public void onResume() {
        super.onResume();
        initView();
    }
    //请求柱状图数据
    private void sendRequest(final String day) {
        chart.setVisibility(View.GONE);
        sendBroadCastReceiver(day);
        barvchar_fail_page.setVisibility(View.GONE);
        mLoadingPage.setVisibility(View.VISIBLE);
        String url = URLConstant.HEAD_URL+URLConstant.URL_ENTERPRISE_RATE + "?userId=" + MyApplication.mUid + "&time=" + dayString;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadingPage.setVisibility(View.GONE);
                barvchar_fail_page.setVisibility(View.VISIBLE);
                tableshow=false;
                chart_fail_tv.setText("加载失败,请检查网络");
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    /**
     * 解析请求到的数据
     *
     * @param response
     */
    private void parseResponse(final String response) {
        AlarmGridPercentage.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                //存到本地，给隔壁的表格用
                mRateList = (List<AlarmGridPercentage>) list;
                SharedPreferenceUtil.saveObject(SpKeyConstant.ENTERPRISE_RATE_LIST, ContextUtil.getContext(),list);
                if (isAdded()) {
                    //initChart();
                }
                BarData data = new BarData(getxVals(mRateList), getDataSet(mRateList));
                // 设置数据
                chart.setData(data);
                chart.setVisibleXRange(1, 6);
                mLoadingPage.setVisibility(View.GONE);
                //chart.setVisibility(View.VISIBLE);
                // previousDay.setVisibility(View.VISIBLE);
                // afterDay.setVisibility(View.VISIBLE);
                tableshow = true;
                sendBroadCastReceiverTable(tableshow);


            }

            @Override
            public void onFailed() {
                super.onFailed();
                mLoadingPage.setVisibility(View.GONE);
                chart_fail_tv.setText("没有数据");
                barvchar_fail_page.setVisibility(View.VISIBLE);
                tableshow = false;

            }
        });
    }

    /**
     * 解析List<EnterpriseRateInfo>数据  为柱状图添加数据
     */
    public ArrayList<BarDataSet> getDataSet(List<AlarmGridPercentage> list) {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> valueSet4 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> valueSet5 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> valueSet6 = new ArrayList<BarEntry>();

        for (int i = 0; i < list.size(); i++) {
            List<DealRate> dealRates = list.get(i).getDealRates();
            for (int j = 0; j < dealRates.size(); j++) {
                if (dealRates.get(j).getAlarmName().trim().equals("数据缺失")) {
                    BarEntry entry = new BarEntry(Float.parseFloat(dealRates.get(j).getRate()), i);
                    valueSet1.add(entry);
                }
                if (dealRates.get(j).getAlarmName().trim().equals("机组停运")) {
                    BarEntry entry = new BarEntry(Float.parseFloat(dealRates.get(j).getRate()), i);
                    valueSet2.add(entry);
                }
                if (dealRates.get(j).getAlarmName().trim().equals("超标报警")) {
                    BarEntry entry = new BarEntry(Float.parseFloat(dealRates.get(j).getRate()), i);
                    valueSet3.add(entry);
                }
                if (dealRates.get(j).getAlarmName().trim().equals("治污设施停运")) {
                    BarEntry entry = new BarEntry(Float.parseFloat(dealRates.get(j).getRate()), i);
                    valueSet4.add(entry);
                }
                if (dealRates.get(j).getAlarmName().trim().equals("超限报警")) {
                    BarEntry entry = new BarEntry(Float.parseFloat(dealRates.get(j).getRate()), i);
                    valueSet5.add(entry);
                }
                if (dealRates.get(j).getAlarmName().trim().equals("数据恒定")) {
                    BarEntry entry = new BarEntry(Float.parseFloat(dealRates.get(j).getRate()), i);
                    valueSet6.add(entry);
                }
            }

        }
        if(valueSet1.size()!=0){
            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "数据缺失");
            barDataSet1.setColor(Color.parseColor("#C0FF3E"));
            barDataSet1.setBarShadowColor(Color.parseColor("#01000000"));
            barDataSet1.setDrawValues(false);
            dataSets.add(barDataSet1);
        }
        if(valueSet2.size()!=0){
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "机组停运");
            barDataSet2.setColor(Color.parseColor("#00FFFF"));
            barDataSet2.setBarShadowColor(Color.parseColor("#01000000"));
            barDataSet2.setDrawValues(false);
            dataSets.add(barDataSet2);
        }
        if(valueSet3.size()!=0){
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "超标报警");
            barDataSet3.setColor(Color.parseColor("#EEAD0E"));
            barDataSet3.setBarShadowColor(Color.parseColor("#01000000"));
            barDataSet3.setDrawValues(false);
            dataSets.add(barDataSet3);
        }
        if(valueSet4.size()!=0){
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "治污设施停运");
            barDataSet4.setColor(Color.parseColor("#F26077"));
            barDataSet4.setBarShadowColor(Color.parseColor("#01000000"));
            barDataSet4.setDrawValues(false);
            dataSets.add(barDataSet4);
        }
        if(valueSet5.size()!=0){
            BarDataSet barDataSet5 = new BarDataSet(valueSet5, "超限报警");
            barDataSet5.setColor(Color.parseColor("#FFC0CB"));
            barDataSet5.setBarShadowColor(Color.parseColor("#01000000"));
            barDataSet5.setDrawValues(false);
            dataSets.add(barDataSet5);
        }
        if(valueSet6.size()!=0){
            BarDataSet barDataSet6 = new BarDataSet(valueSet6, "数据恒定");
            barDataSet6.setColor(Color.parseColor("#9932CC"));
            barDataSet6.setBarShadowColor(Color.parseColor("#01000000"));
            barDataSet6.setDrawValues(false);
            dataSets.add(barDataSet6);
        }

        return dataSets;

    }

    /**
     * 获取x轴数据
     *
     * @param list
     * @return
     */
    public List<String> getxVals(List<AlarmGridPercentage> list) {
        List<String> name = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            name.add(list.get(i).getPoll_source_name());
        }
        return name;

    }


}
