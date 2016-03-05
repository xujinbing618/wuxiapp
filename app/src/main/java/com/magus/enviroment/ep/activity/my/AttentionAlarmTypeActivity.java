package com.magus.enviroment.ep.activity.my;

import android.os.Bundle;
import android.widget.ListView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.MyAlarmTypeAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmTypeInfo;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dao.AlarmTypeDao;
import com.magus.enviroment.global.log.Log;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 常见异常类型
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.my
 * 2015-15/5/8-下午2:40.
 */
public class AttentionAlarmTypeActivity extends SwipeBaseActivity {
    private static final String TAG = "AlarmTypeActivity";
    private CustomActionBar mActionBar;
  //  private TextView selectAll;
    private boolean isSelectedAll = false;
    private ListView mAlarmListView;
    private MyAlarmTypeAdapter mAdapter;
    private List<AlarmTypeInfo> TypeList = new ArrayList<AlarmTypeInfo>();
    private AlarmTypeDao mAlarmDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_unusual_type);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionAlarmTypeActivity.this);
    }

    private void initView() {
      //  selectAll = (TextView) findViewById(R.id.my_select_all);
//        selectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                submit();
//            }
//        });
        mAlarmDao = AlarmTypeDao.getInstance(this);
        mAlarmListView = (ListView) findViewById(R.id.alarm_list);

        TypeList =mAlarmDao.findAllAlarmType();
        mAdapter = new MyAlarmTypeAdapter(this, TypeList);
        mAlarmListView.setAdapter(mAdapter);

        Log.e(TAG, mAlarmDao.findAllAlarmType().toString());
    }

//    private void sendRequest() {
//        String url = URLConstant.URL_ALARM_TYPE + "?userId=" + MyApplication.mUid + "&roleId=" + MyApplication.mRoleId;
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                parseResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        MyApplication.getRequestQueue().add(request);
//
//    }
//
//    private void parseResponse(String response) {
//        AlarmTypeInfo.parseAlarmTypeInfo(response, new RequestCallBack() {
//            @Override
//            public void onSuccess(List<?> list) {
//                super.onSuccess(list);
//                mAlarmTypeList = (List<AlarmTypeInfo>) list;
//                mAdapter.setList(mAlarmTypeList, false, false);
//            }
//
//            @Override
//            public void onFailed() {
//                super.onFailed();
//            }
//        }, this);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        //退出的时候提交信息
        submitAlarmType();
    }

    /**
     * 提交信息
     */
    private void submitAlarmType() {
        String url = URLConstant.HEAD_URL+URLConstant.URL_SUBMIT_ALARM_TYPE +"?userId=" + MyApplication.mUid + "&checkedIds=" + getMessage();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        android.util.Log.e(TAG,response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AttentionAlarmTypeActivity.this, "上传失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        });
// {
//            @Override
//            protected Map<String, String> getParams() {
//                //在这里设置需要post的参数
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("userAlarmList", getMessage());
//                return map;
//            }
//        };
        MyApplication.getRequestQueue().add(stringRequest);
    }

    private String getMessage() {
        List<AlarmTypeInfo> list = mAlarmDao.findCheckAlarmType();
        if(list.size()>0){
            String codeId="'"+list.get(0).getAlarmCode()+"'";
            for(int i=1;i<list.size();i++){
                codeId=codeId+",'"+list.get(i).getAlarmCode()+"'";
            }
            // String str = "{userId:" + MyApplication.mUid + ",roleId:" + MyApplication.mRoleId + ",resultList:" + JSON.toJSONString(list) + "}";
            return codeId;
        }
      else{
            return "";
        }
    }

}
