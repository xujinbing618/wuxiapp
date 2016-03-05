package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AlarmProblemType;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务中心问题留言
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.my
 * 2015-15/5/7-下午3:13.
 */
public class ServiceLeaveMessageActivity extends SwipeBaseActivity  {

    private CustomActionBar mActionBar;
    private EditText et_content;//反馈内容
    private EditText et_contact;//联系方式
    private Button btn_submit;//提交按钮
    private ListView et_type_lv;//问题类型列表
    private ProgressDialog progressDialog;//进度条
    List<AlarmProblemType> listType = new ArrayList<AlarmProblemType>();
    private String selectTypeId="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_leave_message);
        initActionBar();
        initView();
        sendRequestTyep();
    }
    private final void initView(){
        progressDialog = UIUtil.initDialog(ServiceLeaveMessageActivity.this, "正在加载，请稍后...");
        et_content=(EditText)findViewById(R.id.et_content);
        et_contact=(EditText)findViewById(R.id.et_content);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        et_type_lv=(ListView)findViewById(R.id.et_type_lv);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAlarmType();
            }
        });



    }
    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(ServiceLeaveMessageActivity.this);
    }

    /**
     * 提交信息
     */
    private void submitAlarmType() {
        if("".equals(et_content.getText().toString().trim())){
            Toast.makeText(ServiceLeaveMessageActivity.this, "请填写留言内容", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.show();
            String url = URLConstant.HEAD_URL+URLConstant.URL_SAVE_PROBLEM_MESSAGE;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject o = new JSONObject(response);
                                if (o.has("resultCode")) {
                                    if (o.getString("resultCode").equals("true")) {
                                        Toast.makeText(ServiceLeaveMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }else{
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(ServiceLeaveMessageActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(ServiceLeaveMessageActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(ServiceLeaveMessageActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //在这里设置需要post的参数
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("typeId",selectTypeId);
                    map.put("problem",et_content.getText().toString());
                    map.put("contact", et_contact.getText().toString());
                    return map;
                }
            };
            MyApplication.getRequestQueue().add(stringRequest);
        }

    }

    /**
     * 问题类型列表
     */
    private void sendRequestTyep() {
        String url = URLConstant.HEAD_URL+URLConstant.URL_GET_PROBLEM_TYPE;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponseSource(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponseSource(String response) {
        AlarmProblemType.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                listType = (List<AlarmProblemType>) list;
                List<String> typename = new ArrayList<>();
                for (int i = 0; i < listType.size(); i++) {
                    typename.add(listType.get(i).getDic_name());
                }
                et_type_lv.setAdapter(new ArrayAdapter<String>(ServiceLeaveMessageActivity.this, android.R.layout.simple_list_item_single_choice, typename));
                et_type_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//如果不使用这个设置，选项中的radiobutton无法响应选中事件
                et_type_lv.setItemChecked(0, true);
                selectTypeId =listType.get(et_type_lv.getCheckedItemPosition()).getDictionary_id();
                et_type_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectTypeId =listType.get(et_type_lv.getCheckedItemPosition()).getDictionary_id();
                    }
                });
            }
            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, this);
    }
}
