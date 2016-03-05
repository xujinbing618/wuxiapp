package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmSixListAdapter;
import com.magus.enviroment.ep.bean.AlarmSixListInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.global.log.Log;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.SearchDialogSix;
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
/**
 * 六类报警
 * Created by pau on 15/7/21.
 */
public class AlarmSixActivity extends SwipeBackActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "AlarmSixActivity";
    private PullToRefreshListView mDetailListView;
    private String companyId = "";//企业id
    private String alarmTypeId = "";//异常类型id
    private String stateId = "";//处理状态id
    private String startTime = "";//开始时间
    private String stopTime = "";//结束时间
    private CustomActionBar mActionBar;
    private ProgressDialog progressDialog;//进度条
    private List<AlarmSixListInfo> mAlarmDetailList = new ArrayList<AlarmSixListInfo>();
    private AlarmSixListAdapter mAdapter;
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private int requestNum = 20;
    private int mtotalRecord = 0;//数据总数
    private int clickNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_six);

        initActionBar();
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        clickNum = 0;
        sendRequest(firstStartRecord, false);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        progressDialog = UIUtil.initDialog(AlarmSixActivity.this, "正在加载，请稍后...");
        mDetailListView = (PullToRefreshListView) findViewById(R.id.attention_detail_list);
        mAdapter = new AlarmSixListAdapter(this, mAlarmDetailList);
        mDetailListView.setAdapter(mAdapter);
        mDetailListView.setOnItemClickListener(this);
        mDetailListView.setMode(PullToRefreshBase.Mode.BOTH);//是否上拉加载
        mDetailListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("");
                sendRequest(firstStartRecord, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mtotalRecord <= Integer.parseInt(mStartPosition)) {
                    pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("没有数据");
                }
                sendRequest(mStartPosition, true);
            }
        });
    }

    /**
     * 请求
     *
     * @param position    请求位置
     * @param isUpRefresh 是否上啦刷新
     */
    private void sendRequest(String position, final boolean isUpRefresh) {
        progressDialog.show();
        final String url =URLConstant.HEAD_URL+ URLConstant.URL_ALARM_DETAIL_LIST + "?userId=" + MyApplication.mUid+"&start=" + position + "&startTime=" + startTime +"&endTime=" + stopTime +"&pollSourceId=" + companyId + "&alarmTypeCode=" + alarmTypeId + "&dealStatus=" + stateId;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, isUpRefresh);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDetailListView.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AlarmSixActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);

    }
    /**
     * 解析response
     *
     * @param response
     * @param isUpRefresh
     */
    private void parseResponse(String response, final boolean isUpRefresh) {
        AlarmSixListInfo.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord, int totalRecord) {
                super.onSuccess(list);
                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord = totalRecord;
                //如果上啦刷新则加入更多
                if (isUpRefresh) {
                    mAlarmDetailList.addAll((List<AlarmSixListInfo>) list);
                } else {
                    mAlarmDetailList = (List<AlarmSixListInfo>) list;
                    //SharedPreferenceUtil.saveObject(SpKeyConstant.ALARM_DETAIL_LIST, ContextUtil.getContext(), mAlarmDetailList);
                }
                mAdapter.setList(mAlarmDetailList);
                mDetailListView.onRefreshComplete();
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                mDetailListView.onRefreshComplete();
                if (!isUpRefresh) {
                    mAlarmDetailList.clear();
                    mAdapter.setList(mAlarmDetailList);
                    Toast.makeText(AlarmSixActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//AttentionDetailContentActivity.class
        Log.e(TAG, "position=" + position);
        AlarmSixListInfo info = mAlarmDetailList.get(position - 1);
        Intent intent = new Intent();
        intent.setClass(this, AlarmSixInfoActivity.class);
        intent.putExtra("facilityBasId", info.getFacility_bas_id());
        intent.putExtra("alarmTypeCode",info.getAlarm_type_code());
        intent.putExtra("alarmTime",info.getAlarmtime());
        intent.putExtra("name",info.getPoll_source_name());
        intent.putExtra("alarmTypeName",info.getAlarm_type_name());
        intent.putExtra("longTime",info.getTimecou());
        startActivity(intent);
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AlarmSixActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
        mActionBar.getRightImageView().setVisibility(View.VISIBLE);
        mActionBar.getRightImageView().setImageResource(R.mipmap.search);
        mActionBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SearchDialogSix dialog = new SearchDialogSix(AlarmSixActivity.this);
                dialog.show();
                dialog.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        companyId = dialog.getName();
                        alarmTypeId = dialog.getType();
                        stateId = dialog.getState();
                        startTime = dialog.getStartTime();
                        stopTime = dialog.getStopTime();
                        sendRequest(firstStartRecord, false);
                    }
                });

            }
        });
    }
}
