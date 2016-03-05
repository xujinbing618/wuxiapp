package com.magus.enviroment.ep.activity.knowledge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.CaseFileAdapter;
import com.magus.enviroment.ep.bean.CaseBean;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.pulltorefresh.PullToRefreshBase;
import com.magus.pulltorefresh.PullToRefreshListView;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;
/**
 * 执法案例
 *
 */
public class CaseActivity extends SwipeBackActivity{
    private static String TAG="CaseActivity";
    private CustomActionBar mActionBar;
    private PullToRefreshListView mCaseList;
    private int mStartPosition=0;
    private CaseFileAdapter mAdapter;
    private ProgressDialog progressDialog;//进度条
    private List<CaseBean> mList = new ArrayList<>();
    OnItemClickListener myOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CaseBean info = mList.get(position-1);
            Intent intent = new Intent();
            intent.setClass(CaseActivity.this,CaseDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CaseDetailActivity.KEY_CASE_REMARK,info.getRemark());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        initActionBar();
        initView();
    }

    private void initView() {
        progressDialog = UIUtil.initDialog(CaseActivity.this, "正在加载，请稍后...");
        this.mCaseList = (PullToRefreshListView) findViewById(R.id.lv_caselist);
        mAdapter = new CaseFileAdapter(this,mList);
        mCaseList.setAdapter(mAdapter);
        mCaseList.setOnItemClickListener(myOnItemClickListener);
        mCaseList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mStartPosition = 0;
                sendRequest();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                sendRequest();
            }
        });

        sendRequest();

    }

    private void sendRequest() {
        progressDialog.show();
        String url =URLConstant.HEAD_URL+ URLConstant.URL_CASE_FILE+"?start="+mStartPosition;
        StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCaseList.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(CaseActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response) {
        CaseBean.parseCaseBean(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord) {
                super.onSuccess(list, startRecord);
                mList = (List<CaseBean>) list;
                mStartPosition = startRecord;
                mAdapter.setList(mList);
                mCaseList.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                Toast.makeText(CaseActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(CaseActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

}
