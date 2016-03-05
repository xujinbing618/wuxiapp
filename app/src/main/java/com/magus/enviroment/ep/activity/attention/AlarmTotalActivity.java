package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmTotalAdapter;
import com.magus.enviroment.ep.bean.AlarmTotal;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.SearchDialogTotal;
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
 * 总量预警
 */
public class AlarmTotalActivity extends SwipeBackActivity {
    private static final String TAG = "AlarmTotalActivity";
    private AlarmTotalAdapter mAdapter;
    private PullToRefreshListView mDetailListView;
    private List<AlarmTotal> mAlarmDetailList = new ArrayList<AlarmTotal>();
    private ProgressDialog progressDialog;//进度条
    private LinearLayout mFailPage;
    private CustomActionBar mActionBar;
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private String companyId = "";//企业id
    private String source = "";//污染源
    private int requestNum=20;
    private int mtotalRecord=0;//数据总数
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_total);
        progressDialog = UIUtil.initDialog(AlarmTotalActivity.this, "正在加载，请稍后...");
        initActionBar();
        initView();

    }
    private void initView() {
        mDetailListView = (PullToRefreshListView) findViewById(R.id.attention_detail_list);
        mAdapter = new AlarmTotalAdapter(this, mAlarmDetailList);
        mDetailListView.setAdapter(mAdapter);
        mDetailListView.setMode(PullToRefreshBase.Mode.BOTH);
       // mDetailListView.setOnItemClickListener(this);
        mFailPage = (LinearLayout) findViewById(R.id.ll_fail_page);
        mFailPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFailPage.setVisibility(View.GONE);
                sendRequest(firstStartRecord, false);
            }
        });
        mDetailListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("");
                sendRequest(firstStartRecord, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mtotalRecord<=Integer.parseInt(mStartPosition)) {
                    pullToRefreshBase.getLoadingLayoutProxy().setPullLabel("没有更多了");
                }
                sendRequest(mStartPosition, true);
            }
        });
        sendRequest(firstStartRecord, false);

    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e(TAG, "position=" + position);
//        AlarmTotal info = mAlarmDetailList.get(position - 1);
//        Intent intent = new Intent();
//        intent.setClass(this, OverContentActivity.class);
//        intent.putExtra("pollSourceName", info.getPoll_source_name());
//        intent.putExtra("pollutantName", info.getType());
//        intent.putExtra("fact_out_nd", info.getNum());
//
//        startActivity(intent);
//    }

    /**
     * 请求
     *
     * @param position    请求位置
     * @param isUpRefresh 是否上啦刷新
     */
    private void sendRequest(String position, final boolean isUpRefresh) {
        progressDialog.show();
        final String url = URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_ALARM_TOTAL + "?userId=" + MyApplication.mUid+ "&start=" + position+"&pollSourceId="+companyId+"&typeName="+source;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, isUpRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AlarmTotalActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                mDetailListView.onRefreshComplete();
            }

        });
        // 设置是否缓存
        request.setShouldCache(true);
        //设置请求超时
        request.setRetryPolicy(new DefaultRetryPolicy(60*60*1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);

    }
    private void parseResponse(String response, final boolean isUpRefresh) {
        AlarmTotal.parseTotalAlarmInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list,int startRecord,int totalRecord) {
                super.onSuccess(list);
                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord=totalRecord;
                //如果上啦刷新则加入更多
                if (isUpRefresh) {
                    mAlarmDetailList.addAll((List<AlarmTotal>) list);
                } else {
                    mAlarmDetailList = (List<AlarmTotal>) list;
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
                if(!isUpRefresh){
                    mAlarmDetailList.clear();
                    mAdapter.setList(mAlarmDetailList);
                    Toast.makeText(AlarmTotalActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.getMiddleTextView().setText("总量预警");
        mActionBar.setLeftImageClickListener(AlarmTotalActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
        mActionBar.getRightImageView().setVisibility(View.VISIBLE);
        mActionBar.getRightImageView().setImageResource(R.mipmap.search);
        mActionBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SearchDialogTotal dialog = new SearchDialogTotal(AlarmTotalActivity.this);
                dialog.show();
                dialog.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        companyId = dialog.getName();
                        source = dialog.getSource();
                        sendRequest(firstStartRecord, false);
                    }
                });
            }
        });
    }
}
