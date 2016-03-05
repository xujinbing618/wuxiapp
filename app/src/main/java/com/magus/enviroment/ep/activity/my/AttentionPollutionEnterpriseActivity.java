package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.MyPollutionEnterpriseAdapter;
import com.magus.enviroment.ep.base.SwipeBaseActivity;
import com.magus.enviroment.ep.bean.AttentionEnterprise;
import com.magus.enviroment.ep.bean.AttentionZone;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.constant.URLConstant;
import com.magus.enviroment.ep.dao.EnterpriseManagerDao;
import com.magus.enviroment.ep.dao.ZoneManagerDao;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.UIUtil;
import com.magus.volley.Request;
import com.magus.volley.Response;
import com.magus.volley.VolleyError;
import com.magus.volley.toolbox.JsonObjectRequest;
import com.magus.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.my
 * 2015-15/5/8-下午2:40.
 */
public class AttentionPollutionEnterpriseActivity extends SwipeBaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "SourceActivity";
    public static String ENTERPRISE_TYPE = "ENTERPRISE_TYPE";
    private static int ID_SOLID = 0;//固态
    private static int ID_LIQUID = 1;//液态
    private static int ID_GAS = 2;//液态
    private ProgressDialog progressDialog;//进度条
    private CustomActionBar mActionBar;
    private ListView mEnterpriceListView;
    private MyPollutionEnterpriseAdapter mAdapter;

    private Button[] mSourceType;
    private int[] mSourceTypeIds = {R.id.me_source_solid, R.id.me_source_liquid, R.id.me_source_gas};


    private List<AttentionEnterprise> mSolidList = new ArrayList<AttentionEnterprise>();
    private List<AttentionEnterprise> mLiquidList = new ArrayList<AttentionEnterprise>();
    private List<AttentionEnterprise> mGasList = new ArrayList<AttentionEnterprise>();

    private FragmentManager mFragmentManager;

    public static final String KEY_ZONE_NAME = "ZONE_NAME";//传递城市名称
    public static final String KEY_ZONE_ID="ZONE_ID";//区域ID

    private String mZoneId = "";
    private String mZoneName = "";

    private TextView selectAll;
    private boolean isSelectAll = true;

    private EnterpriseManagerDao mEnterpriseManagerDao;
    private ZoneManagerDao mZoneManagerDao;

    private AttentionZone zone = new AttentionZone();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_pollution_source);
        initActionBar();
        initData();
        initView();
    }

    private void initData() {
        mEnterpriseManagerDao = EnterpriseManagerDao.getInstance(this);
        mZoneManagerDao = ZoneManagerDao.getInstance(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mZoneId = bundle.getString(KEY_ZONE_ID);
                mZoneName = bundle.getString(KEY_ZONE_NAME);
                zone.setZoneName(mZoneName);
                zone.setZoneId(mZoneId);
                Log.e(TAG, "mZoneId=" + mZoneId + ",mZoneName" + mZoneName);
            }
        }
    }

    private void initView() {
        progressDialog = UIUtil.initDialog(AttentionPollutionEnterpriseActivity.this, "正在加载，请稍后...");
        selectAll = (TextView) findViewById(R.id.my_select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = mZoneManagerDao.queryStatusByZoneId(mZoneId);
                if (status.equals("1")){//如果已经全选则取消 删除
                    mAdapter.setList(mSolidList, false, true, mZoneId, mZoneName);
                    zone.setStatus("0");
                    mEnterpriseManagerDao.deleteAllEnterpriseByZoneId(mZoneId);
                }else {
                    mAdapter.setList(mSolidList, true, true, mZoneId, mZoneName);
                    zone.setStatus("1");
                    for (int i=0;i<mSolidList.size();i++){
                        mEnterpriseManagerDao.insertEnterprise(mSolidList.get(i));
                    }

                }

                mZoneManagerDao.updateZoneStatus(zone);
                isSelectAll = !isSelectAll;
            }
        });
        mEnterpriceListView = (ListView) findViewById(R.id.enterprice_list);
        mAdapter = new MyPollutionEnterpriseAdapter(AttentionPollutionEnterpriseActivity.this, mSolidList);
        mEnterpriceListView.setAdapter(mAdapter);
        mSourceType = new Button[mSourceTypeIds.length];
        for (int i = 0; i < mSourceTypeIds.length; i++) {
            mSourceType[i] = (Button) findViewById(mSourceTypeIds[i]);
            mSourceType[i].setOnClickListener(onClickListener);
        }
        mSourceType[0].setSelected(true);

        sendRequest();

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.me_source_solid:
//                    changeFragment(ID_SOLID);
                    break;
                case R.id.me_source_liquid:
//                    changeFragment(ID_LIQUID);
                    break;
                case R.id.me_source_gas:
//                    changeFragment(ID_GAS);
                    break;
            }
        }
    };


    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionPollutionEnterpriseActivity.this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        submitPollInfo();
    }

    /**
     * 提交信息
     */
    private void submitPollInfo() {
        String loginUrl = URLConstant.HEAD_URL+URLConstant.URL_SUBMIT_ATTENTION;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("userZonePollList", getMessage());
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
    }

    private String getMessage() {
        List<AttentionZone> list2 = new ArrayList<AttentionZone>();
        List<AttentionZone> list1 = mZoneManagerDao.findZoneList();
        for (int i = 0; i < list1.size(); i++) {
            AttentionZone zone = list1.get(i);
            List<AttentionEnterprise> list = new ArrayList<AttentionEnterprise>();
            list = mEnterpriseManagerDao.findByZoneId(zone.getZoneId());
            zone.setPollList(list);
            list2.add(zone);
        }
        String str = "{userId:" + MyApplication.mUid + ",roleId:" + MyApplication.mRoleId + ",resultList:" + JSON.toJSONString(list2) + "}";
        return str;
    }


    /**
     * 请求企业列表
     */
    private void sendRequest() {
        progressDialog.show();
        String url = URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_ENTERPRISE + "?userId=" + MyApplication.mUid + "&roleId=" + MyApplication.mRoleId + "&zoneId=" + mZoneId;
        Log.e(TAG, url);
        //根据给定的URL新建一个请求
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(response.toString());
                        Log.e(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionPollutionEnterpriseActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getRequestQueue().add(request);
    }


    private void parseResponse(String response) {
        //获得请求数据之后先保存到数据库
        AttentionEnterprise.parseEnterpriseInfo(response, new RequestCallBack() {
            @Override
            public void onFailed(String errorMessage) {
                super.onFailed(errorMessage);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionPollutionEnterpriseActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                mSolidList = (List<AttentionEnterprise>) list;
                mAdapter.setList(mSolidList, false, false, mZoneId, mZoneName);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);
    }
}
