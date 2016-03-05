package com.magus.enviroment.ep.fragment.attention;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.bean.MonitorData;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

public class TotalMonitoringLinechartFragment extends Fragment {
    private LineChart lineChart;//折线图
    private View mRootView;
    private LinearLayout mLoadingPage;//正在加载
    private LinearLayout barvchar_fail_page;//加载失败
    private TextView chart_fail_tv;//加载失败提示
    private List<MonitorData> mMonitorList = new ArrayList<MonitorData>();
    private String pollSourceId="";//工厂id
    private String pollutantCode="";//污染源id

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_total__monitoring__linechart_, null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        return mRootView;

    }

    private void initView() {
        chart_fail_tv=(TextView)mRootView.findViewById(R.id.chart_fail_tv);
        lineChart = (LineChart) mRootView.findViewById(R.id.monitoring_line_chart);
        mLoadingPage = (LinearLayout) mRootView.findViewById(R.id.barvchar_loading_now);
        barvchar_fail_page = (LinearLayout) mRootView.findViewById(R.id.barvchar_fail_page);
        barvchar_fail_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForPollutionData();
                barvchar_fail_page.setVisibility(View.GONE);
            }
        });
        pollSourceId= getArguments().getString("pollSourceId");
        pollutantCode= getArguments().getString("pollutantCode");
        sendForPollutionData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 请求折线图数据
     */
    private void sendForPollutionData() {
        mLoadingPage.setVisibility(View.VISIBLE);
        String url = URLConstant.HEAD_URL+URLConstant.URL_MONITOR_POLLUTION_DATA + "?pollSourceId=" + pollSourceId + "&pollutantCode=" + pollutantCode;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("折线图数据"+response);
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
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response) {
        MonitorData.parseMonitorData(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                mMonitorList = (List<MonitorData>) list;
                if (isAdded()) {
                    initChart();
                }
                LineData data = new LineData(getxVals(mMonitorList), getDataSet(mMonitorList));
                lineChart.setData(data);
                lineChart.setVisibility(View.VISIBLE);
                mLoadingPage.setVisibility(View.GONE);
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


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
//        //格式化y轴数据格式，以百分号
//        leftAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                String a = new DecimalFormat("0").format(value);
//                return a + "%";
//            }
//        });
        lineChart.invalidate();
    }

    public ArrayList<LineDataSet> getDataSet(List<MonitorData> ConstantList) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        ArrayList<Entry> valueSet1 = new ArrayList<Entry>();
        for (int i=0;i<ConstantList.size();i++){
            String value=ConstantList.get(i).getRtTotalDayValue();
            if("".equals(value.trim())){
                value="0f";
            }
            Entry entry=new Entry(Float.parseFloat(value),i);
            valueSet1.add(entry);
        }
        LineDataSet barDataSet1 = new LineDataSet(valueSet1, "七天");
        barDataSet1.setColor(Color.parseColor("#8B008B"));
        barDataSet1.setDrawValues(false);
        dataSets.add(barDataSet1);


        return dataSets;
    }
    /**
     * 获取x轴数据
     *
     * @param list
     * @return
     */
    public List<String> getxVals(List<MonitorData> list) {
        List<String> name = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            name.add(list.get(i).getCountTime()+"日");
        }
        return name;

    }
}
