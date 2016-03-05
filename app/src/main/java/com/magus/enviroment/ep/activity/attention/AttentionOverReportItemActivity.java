package com.magus.enviroment.ep.activity.attention;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AttentionOverReportItemAdapter;
import com.magus.enviroment.ep.bean.AlarmOverReport;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

public class AttentionOverReportItemActivity extends SwipeBackActivity {
    private ProgressDialog progressDialog;//进度条
    private PullToRefreshListView over_report_item_list;
    AttentionOverReportItemAdapter adapter;
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private int requestNum = 20;
    private CustomActionBar mActionBar;
    private int mtotalRecord = 0;//数据总数
    private String pollutant_name;
    private String begin_time;
    private String facility_bas_id;
    private List<AlarmOverReport> mOverAlarmList = new ArrayList<AlarmOverReport>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_attention_over_report);
        initView();
        initActionBar();
        sendRequest(firstStartRecord,false);
    }
    private void initView(){
        progressDialog = UIUtil.initDialog(AttentionOverReportItemActivity.this, "正在加载，请稍后...");
        intent=getIntent();
        pollutant_name=intent.getStringExtra("pollutant_name");
        begin_time=intent.getStringExtra("begin_time");
        facility_bas_id=intent.getStringExtra("facility_bas_id");
        over_report_item_list=(PullToRefreshListView)findViewById(R.id.over_report_item_list);
        adapter=new AttentionOverReportItemAdapter(this, mOverAlarmList);
        over_report_item_list.setAdapter(adapter);
        over_report_item_list.setMode(PullToRefreshBase.Mode.BOTH);
        over_report_item_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("");
                sendRequest(firstStartRecord, false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mtotalRecord <= Integer.parseInt(mStartPosition)) {
                    pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("没有更多了");
                }
                sendRequest(mStartPosition, true);
            }
        });
    }

    /**
     * 请求超标报告列表
     */
    private void sendRequest(String position, final boolean isUpRefresh) {
        progressDialog.show();
        //facilityBasId=472&&alarmCode=12&&start=0&&alarmTime=2015-05-29
        String url = "";
        url = URLConstant.HEAD_URL+URLConstant.URL_REPORT_OVERALRAM_CONTENT_ITEM + "?pollutantName=" + pollutant_name + "&checkDate="+ begin_time+ "&start="+ position + "&crewId="+facility_bas_id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, isUpRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                over_report_item_list.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionOverReportItemActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response, final boolean isUpRefresh) {
        AlarmOverReport.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord, int totalRecord) {
                super.onSuccess(list);
                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord = totalRecord;
                if (isUpRefresh) {
                    mOverAlarmList.addAll((List<AlarmOverReport>) list);
                } else {
                    mOverAlarmList = (List<AlarmOverReport>) list;
                    // SharedPreferenceUtil.saveObject(SpKeyConstant.ALARM_DETAIL_LIST, ContextUtil.getContext(), mAlarmDetailList);
                }
                adapter.setList(mOverAlarmList);
                over_report_item_list.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                over_report_item_list.onRefreshComplete();
            }
        }, this);
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionOverReportItemActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}
