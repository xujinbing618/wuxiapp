package com.magus.enviroment.ep.fragment.attention;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.magus.enviroment.R;

import java.util.ArrayList;

public class TotalMonitoringBarchartFragment extends Fragment {
    private BarChart barChart;//柱状图
    private BarData mBarData;//柱状图数据
    private View mRootView;
    private LinearLayout mLoadingPage;//正在加载
    private LinearLayout barvchar_fail_page;//加载失败
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_total__monitoring__barchart_, null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        return mRootView;

    }
    public void initView(){
        barChart=(BarChart) mRootView.findViewById(R.id.total_monitoring_barchart);
        mLoadingPage = (LinearLayout) mRootView.findViewById(R.id.barvchar_loading_now);
        barvchar_fail_page = (LinearLayout) mRootView.findViewById(R.id.barvchar_fail_page);
        barvchar_fail_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sendRequest(DateUtil.getCurrentDay());
                barvchar_fail_page.setVisibility(View.GONE);
            }
        });
        mBarData = getBarData(4, 100);
        showBarChart(barChart,mBarData);
    }
    private void showBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(true);  ////是否在添加边框

        barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        // barChart.setNoDataTextDescription("You need to provide data for the chart.");
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setDrawValueAboveBar(false);//显示绘制值
        // 隐藏右边的坐标轴
        barChart.getAxisRight().setEnabled(false);
        // 动画
        barChart.animateY(1000);
        barChart.setPinchZoom(false);//
        YAxis leftAxis = barChart.getAxisLeft();
        LimitLine ll = new LimitLine(100, "许可排放量");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setTextColor(Color.RED);
        ll.setTextSize(10f);

        leftAxis.addLimitLine(ll);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setDrawBarShadow(true);
        barChart.setData(barData); // 设置数据
        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        if(barChart.getYMax()<120){
            leftAxis.setAxisMaxValue(120);
        }
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

    }
    private BarData getBarData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add("第" + (i + 1) + "季度");
        }
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float value = (float) (Math.random() * range/*100以内的随机数*/) + 10;
            yValues.add(new BarEntry(value, i));
        }
        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "总量预览");
        barDataSet.setBarShadowColor(android.R.color.white);
        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }
}
