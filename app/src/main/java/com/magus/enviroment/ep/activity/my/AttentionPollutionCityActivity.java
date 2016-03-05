package com.magus.enviroment.ep.activity.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.magus.enviroment.R;
import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.adapter.MyPollutionCityAdapter;
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
 * 污染源城市列表
 * Created by pau
 * Packagename com.magus.enviroment.ep.activity.my
 * 2015-15/5/8-下午2:40.
 */
public class AttentionPollutionCityActivity extends SwipeBaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "PollutionCityActivity";
    private CustomActionBar mActionBar;
    private ListView mCityListView;
    private MyPollutionCityAdapter mAdapter;
    private ProgressDialog progressDialog;//进度条
    private List<AttentionZone> mZoneNameList = new ArrayList<AttentionZone>();

   // private TextView selectAll;
    private boolean isSelectAll = false;


    private EnterpriseManagerDao mEnterpriseManagerDao;
    private ZoneManagerDao mZoneManagerDao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_pollution_city);
        initActionBar();
        initView();

    }


    @Override
    protected void onPause() {
        super.onPause();
        submitPollInfo();
    }

    private void initView() {
        progressDialog = UIUtil.initDialog(AttentionPollutionCityActivity.this, "正在加载，请稍后...");
        mEnterpriseManagerDao = EnterpriseManagerDao.getInstance(this);
        mZoneManagerDao = ZoneManagerDao.getInstance(this);
       // selectAll = (TextView) findViewById(R.id.my_select_all);
        mCityListView = (ListView) findViewById(R.id.me_pollution_city_list);
        mAdapter = new MyPollutionCityAdapter(this, mZoneNameList);
        mCityListView.setAdapter(mAdapter);
        mCityListView.setOnItemClickListener(this);
      //  selectAll = (TextView) findViewById(R.id.my_select_all);
//        selectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAdapter.setList(mZoneNameList, !isSelectAll, true);
//                isSelectAll = !isSelectAll;
//            }
//        });
        sendRequest();
    }

    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(AttentionPollutionCityActivity.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(AttentionPollutionCityActivity.this, AttentionPollutionEnterpriseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AttentionPollutionEnterpriseActivity.KEY_ZONE_NAME, mZoneNameList.get(position).getZoneName());
        bundle.putString(AttentionPollutionEnterpriseActivity.KEY_ZONE_ID, mZoneNameList.get(position).getZoneId()+"");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //请求城市
    private void sendRequest() {
        progressDialog.show();
        String url = URLConstant.HEAD_URL+URLConstant.URL_ATTENTION_ZONE +"?userId="+MyApplication.mUid+"&roleId="+MyApplication.mRoleId;
        Log.e(TAG, url);
        //根据给定的URL新建一个请求
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(AttentionPollutionCityActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getRequestQueue().add(request);
    }

    private void parseResponse(String response) {
        //获得请求数据之后先保存到数据库
        AttentionZone.parseZoneInfo(response, new RequestCallBack() {
            @Override
            public void onFailed(String errorMessage) {
                super.onFailed(errorMessage);
                Toast.makeText(AttentionPollutionCityActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(List<?> list) {
                super.onSuccess(list);
                mZoneNameList = (List<AttentionZone>) list;
                mAdapter.setList(mZoneNameList, false, false);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, this);

    }

    /**
     * 提交信息
     */
    private void submitPollInfo() {
        String loginUrl =URLConstant.HEAD_URL+ URLConstant.URL_SUBMIT_ATTENTION;
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

    private String getMessage2() {
        List<AttentionZone> list2 = new ArrayList<AttentionZone>();
        List<AttentionZone> list1 = mZoneManagerDao.findZoneList();
        if (list1.size() > 0) {
            AttentionZone zone = list1.get(0);
            String status = mZoneManagerDao.queryStatusByZoneId(zone.getZoneId());
            if (!"0".equals(status)) {
                for (int i = 0; i < list1.size(); i++) {
                    AttentionZone zone2 = list1.get(i);
                    List<AttentionEnterprise> list = new ArrayList<AttentionEnterprise>();
                    list = mEnterpriseManagerDao.findByZoneId(zone2.getZoneId());
                    zone2.setPollList(list);
                    list2.add(zone2);
                }
            }
        }

        String str = "{userId:" + MyApplication.mUid + ",roleId:" + MyApplication.mRoleId + ",resultList:" + JSON.toJSONString(list2) + "}";
        return str;
    }
}
