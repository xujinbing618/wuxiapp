package com.magus.enviroment.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmSixInfoHandleAdapter;
import com.magus.enviroment.ep.bean.AlarmSixInfoHandle;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 治污设备停运
 * Created by Administrator on 2015/11/26.
 */
public class SixHandle extends Dialog {
    Context context;
    private ListView dialog_six_handle_list;//处理原因选择列表
    public EditText dialog_six_handle_edit;//备注原因内容
    public Spinner dialog_six_handle_handle_type;//处理方式下拉列表
    public Button dialog_six_handle_handle_submit;//提交按钮
    AlarmSixInfoHandleAdapter adapter;
    List<AlarmSixInfoHandle> listFactory = new ArrayList<>();
    private String alarmTypeName;
    public String conditionid = "";//异常类型id


    public SixHandle(Context context, String alarmTypeName) {
        super(context);
        this.context = context;
        this.alarmTypeName = alarmTypeName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("异常处理");
        setContentView(R.layout.dialog_six_handle);
        initView();
        initData();
    }

    //初始化布局
    public void initView() {
        dialog_six_handle_list = (ListView) findViewById(R.id.dialog_six_handle_list);
        dialog_six_handle_edit = (EditText) findViewById(R.id.dialog_six_handle_edit);
        dialog_six_handle_handle_type = (Spinner) findViewById(R.id.dialog_six_handle_handle_type);
        dialog_six_handle_handle_submit = (Button) findViewById(R.id.dialog_six_handle_handle_submit);
    }

    //初始化数据
    public void initData() {
        sendRequestType();
        List<String> stateList = new ArrayList();
        stateList.add("已处理");
        stateList.add("第三方处理");
        stateList.add("延期处理");
        stateList.add("延期已处理");
        dialog_six_handle_handle_type.setAdapter(new ArrayAdapter(context, android.R.layout.simple_spinner_item, stateList));


    }
    /**
     * 请求我的关注企业
     */

    /**
     * 请求我的关注企业
     */
    private void sendRequestType() {
        String handle = "";
        if ("污水处理厂".equals(alarmTypeName)) {
            handle = "wsclssty";
        } else if ("so2".equals(alarmTypeName)) {
            handle = "so2";
        } else if ("nox".equals(alarmTypeName)) {
            handle = "nox";
        } else if ("烟尘".equals(alarmTypeName)) {
            handle = "smoke";
        } else if ("cod".equals(alarmTypeName)) {
            handle = "cod";
        } else if ("nh3-n".equals(alarmTypeName)) {
            handle = "nh3-n";
        } else {
            handle = "tlty";
        }
        String url = URLConstant.HEAD_URL + URLConstant.URL_ALARM_HANDLE + "?condition=" + handle;
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
        AlarmSixInfoHandle.parseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                listFactory = (List<AlarmSixInfoHandle>) list;
                List<String> handlename = new ArrayList<>();
                for (int i = 0; i < listFactory.size(); i++) {
                    handlename.add(listFactory.get(i).getDic_name());
                }
                dialog_six_handle_list.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice, handlename));
                dialog_six_handle_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//如果不使用这个设置，选项中的radiobutton无法响应选中事件
                dialog_six_handle_list.setItemChecked(0, true);
                conditionid=listFactory.get(dialog_six_handle_list.getCheckedItemPosition()).getDictionary_id();
                dialog_six_handle_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        conditionid=listFactory.get(dialog_six_handle_list.getCheckedItemPosition()).getDictionary_id();
                    }
                });
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, context);
    }


}
