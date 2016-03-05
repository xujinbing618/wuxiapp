package com.magus.enviroment.ep.activity.attention;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.MySpinnerAdapter;
import com.magus.enviroment.ep.bean.AlarmPollTotal;
import com.magus.enviroment.ep.bean.AttentionEnterprise;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dao.EnterpriseManagerDao;
import com.magus.enviroment.ep.fragment.attention.TotalMonitoringLinechartFragment;
import com.magus.enviroment.ep.fragment.attention.ui.DialChartView;
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

/**
 * 总量监控
 * Created by pau on 15/7/21.
 */
public class OverMonitoringActivity extends SwipeBackActivity {
    private TotalMonitoringLinechartFragment linechartFragment;//折线图
    private FragmentManager mFragmentManager;
    private static String TAG = "OverMonitoring";
    private CustomActionBar mActionBar;
    private DialChartView chartView;//饼图
    private Spinner spinner;
    private MySpinnerAdapter mAdapter;
    private List<AttentionEnterprise> mList = new ArrayList<AttentionEnterprise>();
    private Button[] btns;
    private int[] btnIds = {R.id.btn_so2, R.id.btn_nox, R.id.btn_dust, R.id.btn_cod, R.id.btn_nh3n};
    private String pollSourceId = "";
    private String pollutantCode = "";//污染源id
    private TextView titleText;
    EnterpriseManagerDao enterpriseManagerDao;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_monitoring);
        initActionBar();
        initView();

    }

    private void initView() {
        enterpriseManagerDao = new EnterpriseManagerDao(this);
        mFragmentManager = getSupportFragmentManager();
        progressDialog = UIUtil.initDialog(OverMonitoringActivity.this, "正在加载，请稍后...");
        btns = new Button[btnIds.length];
        for (int i = 0; i < btnIds.length; i++) {
            btns[i] = (Button) findViewById(btnIds[i]);
            btns[i].setOnClickListener(onClickListener);
        }
        btns[0].setSelected(true);
        titleText = (TextView) findViewById(R.id.title_2);
        chartView = (DialChartView) findViewById(R.id.circle_view);
        spinner = (Spinner) findViewById(R.id.spinner);


        mList = enterpriseManagerDao.getAllFactory();
        if (mList.size() < 1) {
            sendRequestSource();
        } else {
            mAdapter = new MySpinnerAdapter(this, mList);
            spinner.setAdapter(mAdapter);
            spinner.setOnItemSelectedListener(selectedListener);
        }


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_so2:
                    setBtnSelected(0);
                    break;
                case R.id.btn_nox:
                    setBtnSelected(1);
                    break;
                case R.id.btn_dust:
                    setBtnSelected(2);
                    break;
                case R.id.btn_cod:
                    setBtnSelected(3);
                    break;
                case R.id.btn_nh3n:
                    setBtnSelected(4);
                    break;

            }
        }
    };

    //初始化折线图
    private void initButtonFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        linechartFragment = new TotalMonitoringLinechartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pollSourceId", pollSourceId);
        bundle.putString("pollutantCode", pollutantCode);
        linechartFragment.setArguments(bundle);


        transaction.replace(R.id.fg_total_monitoring_linechart, linechartFragment);
        transaction.commitAllowingStateLoss();
    }

    private void setBtnSelected(int id) {


        progressDialog.show();

        for (int i = 0; i < btnIds.length; i++) {
            btns[i].setSelected(false);
        }
        String code = "";
        String title = "";
        btns[id].setSelected(true);
        if (id == 0) {
            code = "so2";
            title = "SO2";
        } else if (id == 1) {
            code = "nox";
            title = "NOX";
        } else if (id == 2) {
            title = "烟尘";
            code = "smoke";
        } else if (id == 3) {
            code = "cod";
            title = "COD";
        } else if (id == 4) {
            code = "nh3-n";
            title = "NH3-N";
        }
        pollutantCode = code;
        titleText.setText(title + "排放量(/t)");
        initButtonFragment();
        sendForYearCount();
    }

    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e(TAG, "position=" + position + ",text=" + mList.get(position).getPollName());
            pollSourceId = mList.get(position).getPollId();
            setBtnSelected(0);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
                mList = (List<AttentionEnterprise>) list;
                mAdapter = new MySpinnerAdapter(OverMonitoringActivity.this, mList);
                spinner.setAdapter(mAdapter);
                spinner.setOnItemSelectedListener(selectedListener);
            }

            @Override
            public void onFailed() {
                super.onFailed();
            }
        }, this);
    }


    /**
     * 请求年度总量
     */
    private void sendForYearCount() {
        String url = URLConstant.HEAD_URL + URLConstant.URL_MONITOR_POLLUTION_COUNT + "?pollId=" + pollSourceId + "&type=" + pollutantCode;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("总量预警数据" + response);
                AlarmPollTotal data = AlarmPollTotal.parseData(response);
                    String total = "0";
                    String warning = "0f";
                    String num = "0f";
                    String surplus = "0f";
                if (data != null) {
                    if (!"".equals(data.getTotal())) {
                        total = data.getTotal();
                    }
                    if (!"".equals(data.getSurplus())) {
                        surplus = data.getSurplus();
                    }
                    if (!"".equals(data.getNum())) {
                        num = data.getNum();
                    }

                    if (!"".equals(data.getWarning())) {
                        warning = data.getWarning();
                    }
                }
                    chartView.setNewData(total, surplus, num, Float.parseFloat(warning));

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 60 * 1000, 1, 1.0f));
        MyApplication.getRequestQueue().add(request);
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(OverMonitoringActivity.this);

        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }
}
