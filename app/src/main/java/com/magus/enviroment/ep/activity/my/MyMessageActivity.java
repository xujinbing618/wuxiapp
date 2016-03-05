package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.MyMessageAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.PushBean;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.magusutils.SharedPreferenceUtil;
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
 * 我的消息
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity
 * 2015-15/4/23-上午11:09.
 */
public class MyMessageActivity extends SwipeBaseActivity implements AdapterView.OnItemClickListener {
    private CustomActionBar mActionBar;
    private PullToRefreshListView mMessageListView;
    private MyMessageAdapter mAdapter;
    private ProgressDialog progressDialog;//进度条
    private List<PushBean> adapterList = new ArrayList<PushBean>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        progressDialog = UIUtil.initDialog(MyMessageActivity.this, "正在加载，请稍后...");
        initActionBar();
        initView();
        sendRequest();
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MyMessageActivity.this);
    }
    private void initView() {
        mMessageListView = (PullToRefreshListView) findViewById(R.id.me_message_list);
        mAdapter = new MyMessageAdapter(this, adapterList);
        mMessageListView.setAdapter(mAdapter);
        mMessageListView.setOnItemClickListener(this);
        mMessageListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                sendRequest();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                //  sendRequest(mStartPosition, true);
            }
        });
    }

    /**
     * 请求推送数据
     */
    private void sendRequest() {
        progressDialog.show();
        String url = URLConstant.HEAD_URL+URLConstant.URL_MY_PUSHMESSAGES + "?userId=" + MyApplication.mUid + "&minute=" + SharedPreferenceUtil.get("Time", "10");//time;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);

    }

    private void parseResponse(String response) {

        PushBean.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);

                adapterList = (List<PushBean>) list;
                MyApplication.setMessageNum(list.size());
                mAdapter.setList(adapterList);
                mMessageListView.onRefreshComplete();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailed() {
                super.onFailed();
                mMessageListView.onRefreshComplete();
                Toast.makeText(MyMessageActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PushBean info = adapterList.get(position - 1);
        Intent intent = new Intent();
        intent.setClass(this, MyMessageInfoActivity.class);
        intent.putExtra("sid", info.getSid());
        //点击消息，将消息标注为已读“1”，消息条数显示-1
        MyApplication.setMessageNum(MyApplication.getMessageNum()-1);
        adapterList.remove(position-1);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setList(adapterList);
    }
}
