package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmSixInfoAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmSixMessageInfo;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.SixHandle;
import com.magus.enviroment.ui.UIUtil;
import com.magus.volley.DefaultRetryPolicy;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmSixInfoActivity extends SwipeBaseActivity {
    private ProgressDialog progressDialog;//进度条
    private ListView alarm_six_info_list;
    private CustomActionBar mActionBar;
    private List<AlarmSixMessageInfo> mAlarmDetailList = new ArrayList<AlarmSixMessageInfo>();
    AlarmSixInfoAdapter adapter;
    Button checkAll;//全选
    int checkNum;//全选按钮点击次数
    Intent intent;
    String facilityBasId = "";//机组id
    String alarmTypeCode = "";//异常类型id
    String alarmTime = "";//报警时间
    String name = "";//工厂名称
    String alarmTypeName = "";//异常类型名称
    String longTime;//报警时长
    SixHandle sixHandle;
    private ImageView alarm_six_info_message;//发短信
    private ImageView alarm_six_info_call;//打电话
    public boolean isCheck=false;//是否已选择
    private boolean isCheckAll = false;//标记是否全选

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_six_info);
        initView();
        initData();
        initActionBar();
    }

    private void initView() {
        alarm_six_info_message = (ImageView) findViewById(R.id.alarm_six_info_message);
        alarm_six_info_call = (ImageView) findViewById(R.id.alarm_six_info_call);
        checkAll = (Button) findViewById(R.id.alarm_six_info_checkAll);
        progressDialog = UIUtil.initDialog(AlarmSixInfoActivity.this, "正在加载，请稍后...");
        alarm_six_info_list = (ListView) findViewById(R.id.alarm_six_info_list);
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNum++;
                if (checkNum % 2 != 0) {
                    checkAll.setText("全不选");
                    isCheckAll = true;
                    // 遍历list的长度，将MyAdapter中的map值全部设为true
                    for (int i = 0; i < mAlarmDetailList.size(); i++) {
                        adapter.getIsSelected().put(i, true);
                    }
                    isCheck=true;
                    adapter.setSid();
                } else {
                    checkAll.setText("全选");
                    isCheckAll = false;
                    for (int i = 0; i < mAlarmDetailList.size(); i++) {
                        adapter.getIsSelected().put(i, false);
                    }
                    adapter.clearSid();
                    isCheck=false;
                }
                // 刷新listview和TextView的显示
                adapter.notifyDataSetChanged();
            }
        });
        alarm_six_info_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cintent = new Intent(Intent.ACTION_DIAL, Uri
                        .parse("tel:" + ""));
                AlarmSixInfoActivity.this.startActivity(cintent);
            }
        });
        alarm_six_info_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri smsToUri = Uri.parse("smsto:" + "");
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("sms_body", "XX您好，我是XXX单位的XXX,你单位某机组发生" + alarmTypeName + "异常。请尽快处理。");
                AlarmSixInfoActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        intent = getIntent();
        facilityBasId = intent.getStringExtra("facilityBasId");
        alarmTypeCode = intent.getStringExtra("alarmTypeCode");
        longTime=intent.getStringExtra("longTime");
        alarmTime = intent.getStringExtra("alarmTime");
        name = intent.getStringExtra("name");
        alarmTypeName = intent.getStringExtra("alarmTypeName");
        adapter = new AlarmSixInfoAdapter(this, mAlarmDetailList);
        alarm_six_info_list.setAdapter(adapter);
        sendRequest();
    }


    /**
     * 请求
     */
    private void sendRequest() {
        isCheckAll = false;//标记是否全选
        checkAll.setText("全选");
        progressDialog.show();
        final String url = URLConstant.HEAD_URL + URLConstant.URL_ALARM_SIX_INFO_LIST + "?facilityBasId=" + facilityBasId + "&alarmTypeCode=" + alarmTypeCode + "&alarmTime=" + alarmTime+"&langTime="+longTime;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                parseResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AlarmSixInfoActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AlarmSixInfoActivity.this, "没有数据", Toast.LENGTH_SHORT).show();

                if (progressDialog.isShowing() && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AlarmSixInfoActivity.this);
        mActionBar.getMiddleTextView().setText(name);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
        mActionBar.getRightImageView().setVisibility(View.VISIBLE);
        mActionBar.getRightImageView().setImageResource(R.mipmap.handle);
        mActionBar.getRightImageView().setMaxWidth(130);
        //标题栏搜索按钮点击事件
        mActionBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSid().size() > 0) {
                    sixHandle = new SixHandle(AlarmSixInfoActivity.this, mAlarmDetailList.get(0).getAlarm_type_name());
                    sixHandle.show();
                    //确定搜索按钮点击事件
                    sixHandle.dialog_six_handle_handle_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitAlarmType(adapter.getSid(), (sixHandle.dialog_six_handle_handle_type.getSelectedItemPosition() + 2) + "", sixHandle.dialog_six_handle_edit.getText().toString());
                        }
                    });
                } else {
                    Toast.makeText(AlarmSixInfoActivity.this, "请选择要处理的异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 提交信息
     */
    private void submitAlarmType(final List<String> sId, final String dealStatus, final String handleRemark) {
        String url = URLConstant.HEAD_URL + URLConstant.URL_SUBMIT_ALARM_SIX;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject o = new JSONObject(response);
                            if (o.has("resultCode")) {
                                if (o.getString("resultCode").equals("true")) {
                                    Toast.makeText(AlarmSixInfoActivity.this, "处理成功", Toast.LENGTH_SHORT).show();
                                    sendRequest();
                                } else {
                                    Toast.makeText(AlarmSixInfoActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(AlarmSixInfoActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
                        }

                        sixHandle.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AlarmSixInfoActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                String sid = sId.toString().substring(1, sId.toString().length() - 1);
                map.put("sId", sid);
                map.put("dealStatus", dealStatus);
                map.put("handleRemark", handleRemark);
                map.put("conditionid", sixHandle.conditionid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
    }
}
