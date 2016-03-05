package com.magus.enviroment.ep.activity.attention;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AttentionReportOverAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmOverReport;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.util.DateUtilFormat;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.SearchOverReportDialog;
import com.magus.enviroment.ui.UIUtil;
import com.magus.magusutils.DateUtil;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 超标报告
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.attention
 * 2015-15/5/10-下午1:08.
 */
public class AttentionOverReportActivity extends SwipeBaseActivity implements AdapterView.OnItemClickListener {

    private CustomActionBar mActionBar;
    private PullToRefreshListView mListView;
    private List<AlarmOverReport> mOverAlarmList = new ArrayList<AlarmOverReport>();
    private AttentionReportOverAdapter mAdapter;
    private ProgressDialog progressDialog;//进度条
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private int requestNum = 20;
    private int mtotalRecord = 0;//数据总数
    private String dayString;//当前月份
    int i=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_over_report);
        progressDialog = UIUtil.initDialog(AttentionOverReportActivity.this, "正在加载，请稍后...");
        initActionBar();
        initView();
        initData();
    }


    private void initData() {
        dayString = DateUtil.getBeforeMon();
        sendRequest(firstStartRecord, false);
    }

    private void initView() {
        mListView = (PullToRefreshListView) findViewById(R.id.over_report_list);
        mAdapter = new AttentionReportOverAdapter(this, mOverAlarmList);
        mListView.setAdapter(mAdapter);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        mListView.setOnItemClickListener(this);


    }

    /**
     * 请求超标报告列表
     */
    private void sendRequest(String position, final boolean isUpRefresh) {
        progressDialog.show();
        //facilityBasId=472&&alarmCode=12&&start=0&&alarmTime=2015-05-29
        String url = "";
        url = URLConstant.HEAD_URL+URLConstant.URL_REPORT_OVERALRAM_CONTENT + "?userId=" + MyApplication.mUid + "&checkDate=" + dayString + "&start=" + position;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, isUpRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListView.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionOverReportActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();

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
                mAdapter.setList(mOverAlarmList);
                mListView.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                mListView.onRefreshComplete();
                if(!isUpRefresh){
                    mOverAlarmList.clear();
                    mAdapter.setList(mOverAlarmList);
                    Toast.makeText(AttentionOverReportActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }


            }
        }, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, AttentionOverReportItemActivity.class);
        intent.putExtra("begin_time", mOverAlarmList.get(position - 1).getBegin_time());
        intent.putExtra("facility_bas_id", mOverAlarmList.get(position - 1).getFacility_bas_id());
        intent.putExtra("pollutant_name", mOverAlarmList.get(position - 1).getPollutant_name());
        startActivity(intent);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionOverReportActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
        mActionBar.getRightImageView().setVisibility(View.VISIBLE);
        mActionBar.getRightImageView().setImageResource(R.mipmap.search);
        mActionBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SearchOverReportDialog dialog=new SearchOverReportDialog(AttentionOverReportActivity.this);
                dialog.show();
                dialog.search_over_report_dialog_sbumit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dayString=dialog.getMonth();
                        sendRequest(firstStartRecord, false);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

//    public void showMonPicker() {
//        final Calendar localCalendar = Calendar.getInstance();
//        final DateUtilFormat dateUtil = new DateUtilFormat();
//        localCalendar.setTime(dateUtil.strToDate("yyyy-MM", dayString));
//        SearchOverReportDialog dialog = new SearchOverReportDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                localCalendar.set(1, year);
//                localCalendar.set(2, monthOfYear);
//                dayString = dateUtil.clanderTodatetime(localCalendar, "yyyy-MM");
//                if(i%2==0){
//                    sendRequest(firstStartRecord, false);
//                }
//                i++;
//
//
//            }
//        }, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
//
////        //取消按钮，如果不需要直接不设置即可
////        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////
////
////            }
////        });
////        //取消按钮，如果不需要直接不设置即可
////        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////            }
////        });
//
//        dialog.show();
//

    }


