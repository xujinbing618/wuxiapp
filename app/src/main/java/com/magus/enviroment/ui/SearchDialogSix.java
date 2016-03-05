package com.magus.enviroment.ui;

import android.app.DatePickerDialog;
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

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.AlarmSixSpinnerAdapter;
import com.magus.enviroment.ep.adapter.AlarmSixSpinnerTypeAdapter;
import com.magus.enviroment.ep.bean.AlarmTypeInfo;
import com.magus.enviroment.ep.bean.AttentionEnterprise;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dao.AlarmTypeDao;
import com.magus.enviroment.ep.dao.EnterpriseManagerDao;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/10.
 */
public class SearchDialogSix extends Dialog {
    private EditText startEdit;//开始时间

    private EditText stopEdit;//结束时间
    public Button submit;//确定按钮
    private Spinner attention_detail_name_spinne;//单位列表下拉列表
    private Spinner attention_detail_type_spinner;//异常类型下拉列表
    private Spinner attention_detail_state_spinner;//异常处理状态下拉列表
    private Context context;
    private AlarmTypeDao alarmTypeDao;
    EnterpriseManagerDao enterpriseManagerDao;
    List<AttentionEnterprise> listFactory = new ArrayList<>();
    List<AlarmTypeInfo> mAlarmTypeList = new ArrayList<>();

    public SearchDialogSix(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search6);
        initView();
        initData();
    }

    public void initView() {
        enterpriseManagerDao = new EnterpriseManagerDao(context);
        alarmTypeDao = AlarmTypeDao.getInstance(context);
        startEdit = (EditText) findViewById(R.id.attention_detail_start_time);
        startEdit.setOnClickListener(new onclick());
        startEdit.setOnFocusChangeListener(focusChangeListener);
        startEdit.setInputType(InputType.TYPE_NULL);
        stopEdit = (EditText) findViewById(R.id.attention_detail_stop_time);
        stopEdit.setInputType(InputType.TYPE_NULL);
        stopEdit.setOnClickListener(new onclick());
        stopEdit.setOnFocusChangeListener(focusChangeListener);
        submit = (Button) findViewById(R.id.attention_detail_submit_btn);
        attention_detail_name_spinne = (Spinner) findViewById(R.id.attention_detail_name_spinne);
        attention_detail_type_spinner = (Spinner) findViewById(R.id.attention_detail_type_spinner);
        attention_detail_state_spinner = (Spinner) findViewById(R.id.attention_detail_state_spinner);
    }

    class onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Calendar d = Calendar.getInstance(Locale.CHINA);
                Date myDate = new Date();
                //创建一个Date实例
                d.setTime(myDate);
                //设置日历的时间，把一个新建Date实例myDate传入
                int year = d.get(Calendar.YEAR);
                int month = d.get(Calendar.MONTH);
                int day = d.get(Calendar.DAY_OF_MONTH);
                //获得日历中的 year month day
                DatePickerDialog dlg = new DatePickerDialog(context, DatePickerListener, year, month, day);
                //新建一个DatePickerDialog 构造方法中
                //     （设备上下文，OnDateSetListener时间设置监听器，默认年，默认月，默认日）
                dlg.show();
        }
    }
    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Calendar d = Calendar.getInstance(Locale.CHINA);
                Date myDate = new Date();
                //创建一个Date实例
                d.setTime(myDate);
                //设置日历的时间，把一个新建Date实例myDate传入
                int year = d.get(Calendar.YEAR);
                int month = d.get(Calendar.MONTH);
                int day = d.get(Calendar.DAY_OF_MONTH);
                //获得日历中的 year month day
                DatePickerDialog dlg = new DatePickerDialog(context, DatePickerListener, year, month, day);
                //新建一个DatePickerDialog 构造方法中
                //     （设备上下文，OnDateSetListener时间设置监听器，默认年，默认月，默认日）
                dlg.show();
            }
        }
    };

    private DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String month = String.valueOf(monthOfYear);
            String day = String.valueOf(dayOfMonth);
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            }
            if (dayOfMonth < 10) {
                day = "0" + day;
            }
            if (startEdit.hasFocus()) {
                startEdit.setText(year + "-" + month + "-" + day);
            }
            if (stopEdit.hasFocus()) {
                stopEdit.setText(year + "-" + month + "-" + day);
            }


        }
    };


    public void initData() {
        mAlarmTypeList = alarmTypeDao.findCheckAlarmType();
        AlarmTypeInfo alarmSixSpinner = new AlarmTypeInfo();
        alarmSixSpinner.setAlarmTypeName("全部");
        mAlarmTypeList.add(0, alarmSixSpinner);
        AlarmSixSpinnerTypeAdapter alarmSixSpinnerAdapter = new AlarmSixSpinnerTypeAdapter(context, mAlarmTypeList, 0);
        attention_detail_type_spinner.setAdapter(alarmSixSpinnerAdapter);

        if (enterpriseManagerDao.getAllFactory().size() <= 0) {
            sendRequestSource();
        } else {
            listFactory = enterpriseManagerDao.getAllFactory();
            AttentionEnterprise alarmSix = new AttentionEnterprise();
            alarmSix.setPollName("全部");
            listFactory.add(0, alarmSix);
            AlarmSixSpinnerAdapter alarmSixSpinne = new AlarmSixSpinnerAdapter(context, listFactory, 0);
            attention_detail_name_spinne.setAdapter(alarmSixSpinne);
        }
        List<String> stateList = new ArrayList();
        stateList.add("全部");
        stateList.add("待处理");
        stateList.add("逾期未处理");
        stateList.add("已处理");
        stateList.add("第三方处理");
        stateList.add("延期处理");
        stateList.add("延期已处理");
        attention_detail_state_spinner.setAdapter(new ArrayAdapter(context, android.R.layout.simple_spinner_item, stateList));
    }

    //获取开始时间
    public String getStartTime() {
        return startEdit.getText().toString();
    }

    //获取结束时间
    public String getStopTime() {
        return stopEdit.getText().toString();
    }

    //获取企业ID
    public String getName() {
        if (attention_detail_name_spinne.getSelectedItemPosition() <= 0) {
            return "";
        }
        return listFactory.get(attention_detail_name_spinne.getSelectedItemPosition()).getPollId();
    }

    //获取异常类型id
    public String getType() {
        if (attention_detail_type_spinner.getSelectedItemPosition() <= 0) {
            return "";
        }
        return mAlarmTypeList.get(attention_detail_type_spinner.getSelectedItemPosition()).getAlarmCode();
    }

    public String getState() {
        if (attention_detail_state_spinner.getSelectedItemPosition() <= 0) {
            return "";
        }
        return attention_detail_state_spinner.getSelectedItemPosition() - 1 + "";
    }

    /**
     * 请求我的关注企业
     */
    private void sendRequestSource() {

        String url = URLConstant.HEAD_URL + URLConstant.URL_ALARM_SOURCE + "?userId=" + MyApplication.mUid;
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
                attention_detail_name_spinne.setAdapter(alarmSixSpinnerAdapter);
            }

            @Override
            public void onFailed() {
                super.onFailed();

            }
        }, context);
    }
}
