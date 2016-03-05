package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.magus.enviroment.ep.bean.AlarmGrid;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.magusutils.DateUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 网格考核
 * Created by pau on 15/7/21.
 */
public class OverGridCheckActivity extends SwipeBackActivity {
    private CustomActionBar mActionBar;
    private BarChart barChart;
    private TextView grid_title;
    private LinearLayout mLoadingPage;//正在加载
    private LinearLayout barvchar_fail_page;//加载失败
    private ProgressDialog progressDialog;//进度条
    private List<AlarmGrid> mRateList = new ArrayList<AlarmGrid>();
    private ImageView previousDay;//前一月
    private ImageView afterDay;//后一月
    private String gridId = "";
    private Spinner spinner_grid_check;
    private static int ID_PREVIOUS_DAY = 0;
    private static int ID_AFTER_DAY = 1;
    private String dayString;
    List<AlarmGrid> gridList = new ArrayList<AlarmGrid>();//网格列表
    List<String> gridString=new ArrayList<>();//网格名称列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_grid_check);
        initActionBar();
        initView();
        sendRequestList();

    }

    private void initView() {
        progressDialog = UIUtil.initDialog(OverGridCheckActivity.this, "正在加载，请稍后...");
        dayString = DateUtil.getCurrentMon();
        spinner_grid_check = (Spinner) findViewById(R.id.spinner_grid_check);
        barChart = (BarChart) findViewById(R.id.grid_bar_chart);
        mLoadingPage = (LinearLayout) findViewById(R.id.barvchar_loading_now);
        barvchar_fail_page = (LinearLayout) findViewById(R.id.barvchar_fail_page);
        barvchar_fail_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(dayString, gridId);
                barvchar_fail_page.setVisibility(View.GONE);
            }
        });
        grid_title = (TextView) findViewById(R.id.grid_title);
        previousDay = (ImageView) findViewById(R.id.previous_day);
        afterDay = (ImageView) findViewById(R.id.after_day);
        previousDay.setOnClickListener(onClickListener);
        afterDay.setOnClickListener(onClickListener);
       // sendRequest(dayString, gridId);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
    private void changeDate(int id) {
        if (id == ID_PREVIOUS_DAY) {
            dayString = DateUtil.getSpecifiedMonBefore(dayString);
            sendRequest(dayString, gridId);
        } else if (id == ID_AFTER_DAY) {
            dayString = DateUtil.getSpecifiedMonAfter(dayString);
//            android.util.Log.e(TAG,dayString);
            if (dayString.compareTo(DateUtil.getCurrentMon()) > 0) { //比较日期 不能超过当前日期
                Toast.makeText(this, "超过当前日期", Toast.LENGTH_SHORT).show();
                dayString = DateUtil.getCurrentMon();
            } else {
                sendRequest(dayString, gridId);
            }
        }

    }

    //请求柱状图数据
    private void sendRequest(final String dayString, String gridId) {
        progressDialog.show();
        barvchar_fail_page.setVisibility(View.GONE);
        grid_title.setText(dayString + "异常处理率");
        String url = URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_ALARM_GRID + "?userId=" + MyApplication.mUid + "&checkDate=" + dayString + "&gridId=" + gridId;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlarmGrid.parseGridInfo(response, new RequestCallBack() {
                    @Override
                    public void onSuccess(List<?> list) {
                        super.onSuccess(list);
                        mRateList = (List<AlarmGrid>) list;
                        initChart();
                        BarData data = new BarData(getxVals(mRateList), getDataSet(mRateList));
                        // 设置数据
                        barChart.setData(data);
                        mLoadingPage.setVisibility(View.GONE);
                        barChart.setVisibility(View.VISIBLE);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailed() {
                        mLoadingPage.setVisibility(View.GONE);
                        barChart.setVisibility(View.GONE);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(OverGridCheckActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        super.onFailed();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }, OverGridCheckActivity.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadingPage.setVisibility(View.GONE);
                barvchar_fail_page.setVisibility(View.VISIBLE);
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    /**
     * 初始化柱状图
     */
    public void initChart() {
        // 数据描述
        barChart.setDescription("");
        barChart.setGridBackgroundColor(getResources().getColor(android.R.color.transparent));
        // 动画
        barChart.animateY(1000);
        // 设置是否可以触摸
        barChart.setTouchEnabled(false);
        // 是否可以拖拽
        barChart.setDragEnabled(true);
        // 是否可以缩放
        barChart.setScaleEnabled(false);
        // 集双指缩放
        barChart.setPinchZoom(false);
        // 隐藏右边的坐标轴
        barChart.getAxisRight().setEnabled(false);
        // 隐藏左边的左边轴
        barChart.getAxisLeft().setEnabled(true);
        barChart.setNoDataText("");
        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        // 设置窗体样式
        mLegend.setForm(Legend.LegendForm.SQUARE);
        // 字体
        mLegend.setFormSize(4f);
        // 字体颜色
        mLegend.setTextColor(Color.parseColor("#7e7e7e"));


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);

        YAxis leftAxis = barChart.getAxisLeft();
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
        barChart.invalidate();
    }

    /**
     * 获取x轴数据
     *
     * @param list
     * @return
     */
    public List<String> getxVals(List<AlarmGrid> list) {
        List<String> name = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            name.add(list.get(i).getAlarmtypename());
        }
        return name;

    }

    /**
     * 解析List<EnterpriseRateInfo>数据  为柱状图添加数据
     */
    public ArrayList<BarDataSet> getDataSet(List<AlarmGrid> list) {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        ArrayList<BarEntry> valueSet = new ArrayList<BarEntry>();

        for (int i = 0; i < list.size(); i++) {
            BarEntry v1e1 = new BarEntry(Float.parseFloat(list.get(i).getPercent()), i);
            valueSet.add(v1e1);
        }
        BarDataSet barDataSet = new BarDataSet(valueSet, "处理率");
        barDataSet.setColor(Color.parseColor("#00FFFF"));
        barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
        barDataSet.setDrawValues(false);
        dataSets.add(barDataSet);
        return dataSets;

    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(OverGridCheckActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

    /**
     * 请求网格列表
     */
    private void sendRequestList() {
        String url = URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_ALARM_GRID_ITEM + "?userId=" + MyApplication.mUid;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);
                    if (o.has("resultCode")) {
                        if (o.getString("resultCode").equals("true")) {
                            if (o.has("resultEntity")) {
                                JSONArray array = new JSONArray(o.getString("resultEntity"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    AlarmGrid item = new AlarmGrid();
                                    if (object.has("grid_id")) {
                                        item.setGrid_id(object.getString("grid_id"));
                                    }
                                    if (object.has("grid_name")) {
                                        item.setGrid_name(object.getString("grid_name"));
                                    }
                                    gridString.add(object.getString("grid_name"));
                                    gridList.add(item);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
                gridString.add(0, "全部");
                spinner_grid_check.setAdapter(new ArrayAdapter(OverGridCheckActivity.this, android.R.layout.simple_spinner_item, gridString));

                spinner_grid_check.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            gridId = gridList.get(position-1).getGrid_id();
                        }else{
                            gridId="";
                        }
                        sendRequest(dayString, gridId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getRequestQueue().add(request);

    }
}
