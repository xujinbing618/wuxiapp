package com.magus.enviroment.ep.activity.knowledge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.PolicyFileAdapter;
import com.magus.enviroment.ep.bean.PolicyFile;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.global.log.Log;
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
 * 政策文件activity
 * Created by pau on 15/7/17.
 */
public class PolicyFileActivity extends SwipeBackActivity {
    private static String TAG="PolicyFileActivity";
    private PullToRefreshListView policyFileList;
    private int mStartPosition=0;
    private PolicyFileAdapter mAdapter;
    private ProgressDialog progressDialog;//进度条
    private List<PolicyFile> mList = new ArrayList<PolicyFile>();
    private CustomActionBar mActionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_policyfile);
        initActionBar();
        initView();
    }

    private void initView(){
        progressDialog = UIUtil.initDialog(PolicyFileActivity.this, "正在加载，请稍后...");
        policyFileList = (PullToRefreshListView) findViewById(R.id.policy_file_list);
        mAdapter = new PolicyFileAdapter(this,mList);
        policyFileList.setAdapter(mAdapter);
        policyFileList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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



    /**
     * 发送数据请求
     */
    private void sendRequest() {
        //?userId=18043&&alarmTime=2015-05-29
        progressDialog.show();
        String url =URLConstant.HEAD_URL+ URLConstant.URL_POLICY_FILE+"?start="+mStartPosition;
        Log.e("url", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                policyFileList.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(PolicyFileActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getRequestQueue().add(request);
    }



    private void parseResponse(String response){
        PolicyFile.parsePolicyFile(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord) {
                super.onSuccess(list, startRecord);
                mList = (List<PolicyFile>) list;
                mStartPosition = startRecord;
                mAdapter.setList(mList);
                policyFileList.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                Toast.makeText(PolicyFileActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(PolicyFileActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}
