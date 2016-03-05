package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 * 推送以及我的消息实体类
 */
public class PushBean {
    private String sid;//id
    private String alarm_detal;//报警详情
    private String alarm_type_name;//报警类型

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAlarm_detal() {
        return alarm_detal;
    }

    public void setAlarm_detal(String alarm_detal) {
        this.alarm_detal = alarm_detal;
    }

    public String getAlarm_type_name() {
        return alarm_type_name;
    }

    public void setAlarm_type_name(String alarm_type_name) {
        this.alarm_type_name = alarm_type_name;
    }

    public static void parseInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("[]")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<PushBean> list = new ArrayList<PushBean>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            PushBean typeInfo = new PushBean();
                            if (object.has("sid")) {
                                typeInfo.setSid(object.getString("sid"));
                            }
                            if (object.has("alarm_detal")) {
                                typeInfo.setAlarm_detal(object.getString("alarm_detal"));
                            }
                            if (object.has("alarm_type_name")) {
                                typeInfo.setAlarm_type_name(object.getString("alarm_type_name"));
                            }
                            list.add(typeInfo);
                        }
                        callBack.onSuccess(list);
                    } else {
                        callBack.onFailed();
                    }
                } else {
                    callBack.onFailed();
                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }

}
