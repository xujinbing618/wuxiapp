package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.dao.ZoneManagerDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注区域
 * Created by pau on 15/6/8.
 */
public class AttentionZone {
    private static final String TAG = "AttentionZone";

    private String zoneId; //区域id
    private String zoneName; //区域名称
    private String status; //状态
    private boolean checked; //是否选中
    private String userId; //用户id
    private String userZoneId; //忽略
    private List<AttentionEnterprise> pollList = new ArrayList<AttentionEnterprise>(); //关注的企业

    private static ZoneManagerDao managerDao;

    public AttentionZone() {
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserZoneId() {
        return userZoneId;
    }

    public void setUserZoneId(String userZoneId) {
        this.userZoneId = userZoneId;
    }

    public List<AttentionEnterprise> getPollList() {
        return pollList;
    }

    public void setPollList(List<AttentionEnterprise> pollList) {
        this.pollList = pollList;
    }

    public static void parseZoneInfo(String response, RequestCallBack callBack, Context context) {
        managerDao = ZoneManagerDao.getInstance(context);
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AttentionZone> list = new ArrayList<AttentionZone>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AttentionZone zone = new AttentionZone();
                            if (object.has("checked")) {
                                zone.setChecked(object.getBoolean("checked"));
                            }
                            if (object.has("status")) {
                                zone.setStatus(object.getString("status"));
                            }
                            if (object.has("userId")) {
                                zone.setUserId(object.getInt("userId") + "");
                            }
                            if (object.has("userZoneId")) {
                                zone.setUserZoneId(object.getInt("userZoneId") + "");
                            }
                            if (object.has("zoneId")) {
                                zone.setZoneId(object.getInt("zoneId") + "");
                            }
                            if (object.has("zoneName")) {
                                zone.setZoneName(object.getString("zoneName"));
                            }
                            managerDao.updateZoneStatus(zone);
                            list.add(zone);
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
