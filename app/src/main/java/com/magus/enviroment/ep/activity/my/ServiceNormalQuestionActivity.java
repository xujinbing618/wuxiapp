package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.ServiceNormalQuestionAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmProblem;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务中心常见问题解决
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.my
 * 2015-15/5/7-下午3:13.
 */
public class ServiceNormalQuestionActivity extends SwipeBaseActivity {
    private CustomActionBar mActionBar;
    private PullToRefreshListView my_service_centre_question_lv;//常见问题列表
    private List<AlarmProblem> mAlarmDetailList = new ArrayList<AlarmProblem>();
    private String mStartPosition = "0";
    private String firstStartRecord = "0";
    private ProgressDialog progressDialog;//进度条
    private int requestNum = 20;//请求数量
    private int mtotalRecord = 0;//数据总数
    ServiceNormalQuestionAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_normal_question);
        initActionBar();
        initView();
        initData();
    }

    private void initData() {
        sendRequest(firstStartRecord, false);
    }

    private final void initView() {
        progressDialog = UIUtil.initDialog(ServiceNormalQuestionActivity.this, "正在加载，请稍后...");
        my_service_centre_question_lv = (PullToRefreshListView) findViewById(R.id.my_service_centre_question_lv);
        adapter = new ServiceNormalQuestionAdapter(this, mAlarmDetailList);
        my_service_centre_question_lv.setAdapter(adapter);
        my_service_centre_question_lv.setMode(PullToRefreshBase.Mode.BOTH);
        //  mDetailListView.setOnItemClickListener(this);
        my_service_centre_question_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(ServiceNormalQuestionActivity.this);
    }

    /**
     * 请求
     */
    private void sendRequest(String position, final boolean isUpRefresh) {
        String url = URLConstant.HEAD_URL + URLConstant.URL_FAQ_LIST + "?start=" + position;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response, isUpRefresh);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                my_service_centre_question_lv.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ServiceNormalQuestionActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();

            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response, final boolean isUpRefresh) {
        AlarmProblem.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord, int totalRecord) {
                super.onSuccess(list);
                mStartPosition = "" + (startRecord + requestNum);
                mtotalRecord = totalRecord;
                //如果上啦刷新则加入更多
                if (isUpRefresh) {
                    mAlarmDetailList.addAll((List<AlarmProblem>) list);
                } else {
                    mAlarmDetailList = (List<AlarmProblem>) list;
                    // SharedPreferenceUtil.saveObject(SpKeyConstant.ALARM_DETAIL_LIST, ContextUtil.getContext(), mAlarmDetailList);
                }
                // mLoadingPage.setVisibility(View.GONE);
                adapter.setList(mAlarmDetailList);
                my_service_centre_question_lv.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                my_service_centre_question_lv.onRefreshComplete();
                if (!isUpRefresh) {
                    mAlarmDetailList.clear();
                    adapter.setList(mAlarmDetailList);
                    Toast.makeText(ServiceNormalQuestionActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }
}
