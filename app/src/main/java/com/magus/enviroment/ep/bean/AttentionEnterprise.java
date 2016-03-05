package com.magus.enviroment.ep.bean;

import android.content.Context;
import android.util.Log;

import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.dao.EnterpriseManagerDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注的企业
 * Created by pau on 15/6/8.
 */
public class AttentionEnterprise {
    private static final String TAG = "AttentionEnterprise";

    public boolean checked;//是否选中
    public String pollId;//企业id
    public String pollName;//企业名称
    public String zoneId;

    public static EnterpriseManagerDao managerDao;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }


    public static void parseEnterpriseInfo(String response, RequestCallBack callBack, Context context) {
        managerDao = EnterpriseManagerDao.getInstance(context);
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AttentionEnterprise> list = new ArrayList<AttentionEnterprise>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AttentionEnterprise enterprise = new AttentionEnterprise();
                            if (object.has("checked")) {
                                enterprise.setChecked(object.getBoolean("checked"));
                            }
                            if (object.has("pollId")) {
                                enterprise.setPollId(object.getInt("pollId") + "");
                            }
                            if (object.has("zoneId")) {
                                enterprise.setZoneId(object.getInt("zoneId") + "");
                            }
                            if (object.has("pollName")) {
                                enterprise.setPollName(object.getString("pollName"));
                            }
                            if (enterprise.isChecked()) {
                                managerDao.insertEnterprise(enterprise);
                            }else {
                                Log.e(TAG,"未选择");
                            }
                            list.add(enterprise);
                        }
                        callBack.onSuccess(list);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed("请求失败");
        }
    }



    public static void parseEnterpriseForMonitor(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AttentionEnterprise> list = new ArrayList<AttentionEnterprise>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AttentionEnterprise enterprise = new AttentionEnterprise();
                            if (object.has("checked")) {
                                enterprise.setChecked(object.getBoolean("checked"));
                            }
                            if (object.has("pollId")) {
                                enterprise.setPollId(object.getInt("pollId") + "");
                            }
                            if (object.has("zoneId")) {
                                enterprise.setZoneId(object.getInt("zoneId") + "");
                            }
                            if (object.has("pollName")) {
                                enterprise.setPollName(object.getString("pollName"));
                            }
                            list.add(enterprise);
                        }
                        callBack.onSuccess(list);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed("请求失败");
        }
    }
}
