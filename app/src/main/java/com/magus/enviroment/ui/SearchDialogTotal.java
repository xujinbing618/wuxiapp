package com.magus.enviroment.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmSixSpinnerAdapter;
import com.magus.enviroment.ep.bean.AttentionEnterprise;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dao.EnterpriseManagerDao;
import com.magus.magusutils.SharedPreferenceUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/11/10.
 */
public class SearchDialogTotal extends Dialog {
    private EditText startEdit;//开始时间
    private EditText stopEdit;//结束时间
    public Button submit;//确定按钮
    private Spinner dialog_source_spinner;//污染源下拉列表
    private Spinner dialog_name_spinne;//单位列表下拉列表
    private TextView tv_start;
    private TextView tv_stop;
    private Context context;
    List<AttentionEnterprise> listFactory = new ArrayList<>();
    List<String> srouce;//污染源列表
    EnterpriseManagerDao enterpriseManagerDao;
    public SearchDialogTotal(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search);
        initView();
        initData();
    }
    /**
     * 初始化控件
     */
    public void initView() {
        tv_start=(TextView)findViewById(R.id.dialog_start_time_tv);
        tv_start.setVisibility(View.GONE);
        tv_stop=(TextView)findViewById(R.id.dialog_stop_time_tv);
        tv_stop.setVisibility(View.GONE);
        enterpriseManagerDao=new EnterpriseManagerDao(context);
        startEdit = (EditText) findViewById(R.id.dialog_start_time);
        startEdit.setVisibility(View.GONE);
        startEdit.setInputType(InputType.TYPE_NULL);
        stopEdit = (EditText) findViewById(R.id.dialog_stop_time);
        stopEdit.setVisibility(View.GONE);
        stopEdit.setInputType(InputType.TYPE_NULL);
        submit = (Button) findViewById(R.id.dialog_submit_btn);
        dialog_source_spinner = (Spinner) findViewById(R.id.dialog_source_spinner);
        dialog_name_spinne = (Spinner) findViewById(R.id.dialog_name_spinne);

    }
    /*
    *初始化数据
     */
    public void initData() {
        srouce = new ArrayList<String>();
        Set<String> s=SharedPreferenceUtil.getSet("source");
        if (s==null||s.size()<=0){
            sendRequest();
        }else{
            Iterator it=s.iterator();
            while(it.hasNext()){
                srouce.add(it.next().toString());
            }
            srouce.add(0,"全部");
            dialog_source_spinner.setAdapter(new ArrayAdapter(context, android.R.layout.simple_spinner_item, srouce));
        }


        if (enterpriseManagerDao.getAllFactory().size()<=0){
            sendRequestSource();
        }else{
            listFactory=enterpriseManagerDao.getAllFactory();
            AttentionEnterprise alarmSixSpinner = new AttentionEnterprise();
            alarmSixSpinner.setPollName("全部");
            listFactory.add(0, alarmSixSpinner);
            AlarmSixSpinnerAdapter alarmSixSpinnerAdapter = new AlarmSixSpinnerAdapter(context, listFactory, 0);
            dialog_name_spinne.setAdapter(alarmSixSpinnerAdapter);
        }


    }

    //获取开始时间
    public String getStartTime() {
        return startEdit.getText().toString();
    }

    //获取结束时间
    public String getStopTime() {
        return stopEdit.getText().toString();
    }

    //获取污染源
    public String getSource() {
        if (dialog_source_spinner.getSelectedItemPosition()<=0){
            return "";
        }
        return dialog_source_spinner.getSelectedItem().toString();
    }

    //获取企业ID
    public String getName() {
        if (dialog_name_spinne.getSelectedItemPosition()<=0){
            return "";
        }
        return listFactory.get(dialog_name_spinne.getSelectedItemPosition()).getPollId();
    }

    /**
     * 请求我的关注企业
     */
    private void sendRequestSource() {

        String url = URLConstant.HEAD_URL+URLConstant.URL_ALARM_SOURCE + "?userId=" + MyApplication.mUid;
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
        AttentionEnterprise.parseEnterpriseInfo(response, new RequestCallBack() {
            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                listFactory = (List<AttentionEnterprise>) list;
                AttentionEnterprise alarmSixSpinner = new AttentionEnterprise();
                alarmSixSpinner.setPollName("全部");
                listFactory.add(0, alarmSixSpinner);
                AlarmSixSpinnerAdapter alarmSixSpinnerAdapter = new AlarmSixSpinnerAdapter(context, listFactory, 0);
                dialog_name_spinne.setAdapter(alarmSixSpinnerAdapter);
            }
            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, context);
    }

    private void sendRequest() {
        String url = URLConstant.HEAD_URL+URLConstant.URL_ALARM_SOURCEVALUE;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);
                    if (o.has("resultCode")) {
                        if (o.getString("resultCode").equals("true")) {
                            if (o.has("resultEntity")) {
                                JSONArray array = new JSONArray(o.getString("resultEntity"));
                                Set<String> so=new HashSet<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (object.has("pollutantValue")) {
                                        so.add(object.getString("pollutantValue"));
                                        srouce.add(object.getString("pollutantValue"));
                                    }
                                }

                                SharedPreferenceUtil.saveSet("source",so);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                srouce.add(0,"全部");
                dialog_source_spinner.setAdapter(new ArrayAdapter(context,android.R.layout.simple_spinner_item,srouce));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getRequestQueue().add(request);

    }

}
