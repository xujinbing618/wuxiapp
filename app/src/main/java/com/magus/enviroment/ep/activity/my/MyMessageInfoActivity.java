package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.MyMessageInfoAdapter;
import com.magus.enviroment.ep.bean.AlarmSixMessageInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

public class MyMessageInfoActivity extends SwipeBackActivity {
    private ListView mDetailListView;
    private ProgressDialog progressDialog;//进度条
    private CustomActionBar mActionBar;
    private List<AlarmSixMessageInfo> mAlarmDetailList = new ArrayList<AlarmSixMessageInfo>();
    Intent intent;
    MyMessageInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_push_message_info);
        initView();
        initActionBar();
    }
    public void initView(){
        intent=getIntent();
        adapter = new MyMessageInfoAdapter(this, mAlarmDetailList);
        progressDialog = UIUtil.initDialog(MyMessageInfoActivity.this, "正在加载，请稍后...");
        mDetailListView=(ListView)findViewById(R.id.me_message_info_list);
        mDetailListView.setAdapter(adapter);
        sendRequest();
    }

    /**
     * 请求
     */
    private void sendRequest() {
        progressDialog.show();
        final String url = URLConstant.HEAD_URL+URLConstant.URL_MY_MESSAGE_INFO + "?sId="+intent.getStringExtra("sid");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(MyMessageInfoActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);

    }

    /**
     * 解析response
     *
     * @param response
     */
    private void parseResponse(final String response) {
        AlarmSixMessageInfo.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                //如果上啦刷新则加入更多
                mAlarmDetailList = (List<AlarmSixMessageInfo>) list;
                adapter.setList(mAlarmDetailList);
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailed() {
                super.onFailed();
                Toast.makeText(MyMessageInfoActivity.this, "没有数据", Toast.LENGTH_SHORT).show();

                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }

    /**
     * 请求报警列表
     */
    private void updateRequest(String alarmLogId) {
        //facilityBasId=472&&alarmCode=12&&start=0&&alarmTime=2015-05-29
        String url = URLConstant.HEAD_URL+URLConstant.URL_MY_MESSAGE_UPDATE + "?alarmLogId=" + alarmLogId;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getRequestQueue().add(request);
    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(MyMessageInfoActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateRequest(intent.getStringExtra("sid"));
    }
}
