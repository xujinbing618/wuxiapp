/*
package com.magus.enviroment.ep.activity.attention;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AttentionCauseAdapter;
import com.magus.enviroment.ep.adapter.AttentionDetailTimeAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmCauseInfo;
import com.magus.enviroment.ep.bean.AlarmDetailInfo;
import com.magus.enviroment.ep.bean.AlarmSimpleInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.CodeConstant;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * 六类报警详情正文
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.attention
 * 2015-15/5/10-上午11:43.
 *//*

public class AttentionDetailContentActivity extends SwipeBaseActivity {
    private static final String TAG = "AttentionDetailContent";
    private CustomActionBar mActionBar;

    private ImageView mPhoneImage; //拨号
    private ImageView mMessageImage; //短信
    private ListView mListView;//异常详情时间列表

    //报警原因
    private CheckBox[] checkBoxes;

    private RadioGroup mRadioGroup;
    private Button mSubmitButton;//提交
    private EditText mNoteText;//备注信息


    public static final String KEY_FACILITY_BASEID = "KEY_FACILITY_BASEID";
    public static final String KEY_FACILITY_NAME = "KEY_FACILITY_NAME";
    public static final String KEY_ALARMCODE = "KEY_ALARMCODE";
    public static final String KEY_STATUS = "KEY_STATUS";
    public static final String KEY_ALARM_TIME = "KEY_ALARM_TIME";

    private String mFacilityBaseId = ""; //设备id
    private String mAlarmCode = "";//报警code 14 超标  9脱硫停运
    private String mStart = "0"; //开始条数
    private String mAlarmTime = ""; //报警时间
    private String mDealStatus = ""; //处理状态
    private String mDealDetails = ""; //处理详情
    private String mFacilityName = ""; //设备名称

    private List<AlarmDetailInfo> mAlarmDetailList = new ArrayList<AlarmDetailInfo>();

    private AttentionDetailTimeAdapter mAdapter;

    private List<AlarmCauseInfo> mAlarmCauseList = new ArrayList<AlarmCauseInfo>();

    private AttentionCauseAdapter mAlarmCauseAdapter;

    private ListView mCauseListView;

    private TextView txtFacilityName;

    private LinearLayout mSubmitLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_detail_content);
        initActionBar();
        initData();
        initView();
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionDetailContentActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mFacilityBaseId = bundle.getString(KEY_FACILITY_BASEID);
                mAlarmCode = bundle.getString(KEY_ALARMCODE);
                mAlarmTime = bundle.getString(KEY_ALARM_TIME);
                mFacilityName = bundle.getString(KEY_FACILITY_NAME);
                mDealDetails = bundle.getString(KEY_STATUS);
                if(bundle.get("Message").equals("1")){
                    String alarmLogId=bundle.getString("alarmLogId");
                    updateRequest(alarmLogId);
                }
                Log.e(TAG,"mDealDetails="+mDealDetails);
            }
        }
    }
    */
/**
     * 请求报警列表
     *//*

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
    private void initView() {
        mSubmitLayout = (LinearLayout) findViewById(R.id.submit_layout);
        txtFacilityName = (TextView) findViewById(R.id.facility_name);
        txtFacilityName.setText(mFacilityName);
        mPhoneImage = (ImageView) findViewById(R.id.phone);
        mMessageImage = (ImageView) findViewById(R.id.message);
        mSubmitButton = (Button) findViewById(R.id.btn_submit);
        mListView = (ListView) findViewById(R.id.detail_time_list);
        mNoteText = (EditText) findViewById(R.id.et_note_content);

        mAdapter = new AttentionDetailTimeAdapter(this, mAlarmDetailList);
        mListView.setAdapter(mAdapter);

        mCauseListView = (ListView) findViewById(R.id.alarm_cause_list);
        mAlarmCauseAdapter = new AttentionCauseAdapter(this, mAlarmCauseList);
        mCauseListView.setAdapter(mAlarmCauseAdapter);
        mCauseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (AlarmCauseInfo info : mAlarmCauseList) {
                    info.setIsChecked(false);
                }
                mAlarmCauseList.get(position).setIsChecked(true);
                mAlarmCauseAdapter.setList(mAlarmCauseList);
            }
        });
        //发短信
        mMessageImage.setOnClickListener(onClickListener);
        //拨号
        mPhoneImage.setOnClickListener(onClickListener);
        //提交
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAlarmDealInfo();
                Toast.makeText(AttentionDetailContentActivity.this, "提交", Toast.LENGTH_LONG).show();
            }
        });

        if (CodeConstant.STATUS_OVERDUE_UNHANDLED.equals(mDealDetails)||CodeConstant.STATUS_HANDLED.equals(mDealDetails)||CodeConstant.STATUS_OVERDUE_HANDLED.equals(mDealDetails)){//逾期未处理已处理和逾期已处理隐藏提交按钮
            mSubmitLayout.setVisibility(View.GONE);
        }
        if ("5".equals(mAlarmCode)){  //机组停运不能处理
            mSubmitLayout.setVisibility(View.GONE);
        }
        sendRequest();
        sendRequestAlarmCause();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.message://短信
                    //系统默认的action，用来打开默认的短信界面
                    intent.setAction(Intent.ACTION_SENDTO);
                    //需要发短息的号码
                    intent.setData(Uri.parse("smsto:" + "13127923360"));
                    //短信内容
                    intent.putExtra("sms_body", "需要发送的内容");
                    break;
                case R.id.phone://拨号
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:13850734494"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;
            }
            startActivity(intent);
        }
    };

    */
/**
     * 请求报警列表
     *//*

    private void sendRequest() {
        //facilityBasId=472&&alarmCode=12&&start=0&&alarmTime=2015-05-29
        String url = URLConstant.HEAD_URL+URLConstant.URL_ALARM_DETAIL_CONTENT + "?facilityBasId=" + mFacilityBaseId + "&alarmCode=" + mAlarmCode + "&alarmTime=" + mAlarmTime + "&start=" + mStart;
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
        MyApplication.getRequestQueue().add(request);
    }

    */
/**
     * 请求报警原因
     *//*

    private void sendRequestAlarmCause() {
        String url =URLConstant.HEAD_URL+ URLConstant.URL_ALARM_COURSE + "?userType=Operation&alarmCode=" + mAlarmCode;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponseCause(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getRequestQueue().add(request);

    }

    private void parseResponse(String response) {
        AlarmDetailInfo.parseAlarmDetailInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list, int startRecord) {
                super.onSuccess(list);
                mAlarmDetailList = (List<AlarmDetailInfo>) list;
                mAdapter.setList(mAlarmDetailList);
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, this);


    }

    private void parseResponseCause(String response) {
        AlarmCauseInfo.parseAlarmCauseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                mAlarmCauseList = (List<AlarmCauseInfo>) list;
                mAlarmCauseAdapter.setList(mAlarmCauseList);
            }

            @Override
            public void onFailed() {
                super.onFailed();

            }

        });
    }

    */
/**
     * 提交信息
     *//*

    private void submitAlarmDealInfo() {
        String url = URLConstant.HEAD_URL+URLConstant.URL_SUBMIT_DEAL_ALARM;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AttentionAlarmTypeActivity.this, "上传失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("alarmInfoList", getMessage());
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
    }

    private String getMessage() {
        String dicValue="";
        for (AlarmCauseInfo cause:mAlarmCauseList){
            if (cause.isChecked()){
                dicValue=cause.getDicValue();
                break;
            }
        }
        if (dicValue.equals("2") || dicValue.equals("3") || dicValue.equals("4") || dicValue.equals("5")) {
            mDealStatus = dicValue;
        } else {
            mDealStatus = "2";
            for (AlarmCauseInfo info : mAlarmCauseList) {
                if (info.isChecked()) {
                    mDealDetails = info.getDicName();
                }
            }
        }
        List<AlarmSimpleInfo> stringList = new ArrayList<AlarmSimpleInfo>();
        for (int i = 0; i < mAlarmDetailList.size(); i++) {
            AlarmDetailInfo info = mAlarmDetailList.get(i);
            AlarmSimpleInfo simpleInfo = new AlarmSimpleInfo();
            if (info.isChecked()) {
                simpleInfo.setAlarmLogId(info.getAlarmLogId());
                stringList.add(simpleInfo);
            }
        }


        String str = "[{\"userId\":"+MyApplication.mUid+",\"facilityBasId\":\"" + mFacilityBaseId + "\",\"alarmCode\":\"" + mAlarmCode + "\",\"alarmTime\":\"" + mAlarmTime + "\",\"dealStatus\":\"" + mDealStatus + "\",\"dealDetail\":\"" + mDealDetails + "\",\"resultList\":" + JSON.toJSONString(stringList) + "}]";
        Log.e(TAG, str);
        return str;
    }


}
*/
