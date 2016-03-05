package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuer on 2015/10/27.
 * 六类报警下拉列表实体类
 */
public class AlarmSixSpinner {
    private String alarm_code;//异常类型id
    private String alarm_name;//异常类型名称
    private String com_id;//企业id
    private String com_name;//企业名称

    public String getAlarm_code() {
        return alarm_code;
    }

    public void setAlarm_code(String alarm_code) {
        this.alarm_code = alarm_code;
    }

    public String getAlarm_name() {
        return alarm_name;
    }

    public void setAlarm_name(String alarm_name) {
        this.alarm_name = alarm_name;
    }

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }
    public static void parseAlarmSixSpinner(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {

                    if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("{}")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));


                        List<AlarmSixSpinner> list = new ArrayList<AlarmSixSpinner>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AlarmSixSpinner typeInfo = new AlarmSixSpinner();
                            if (object.has("alarm_code")) {
                                typeInfo.setAlarm_code(object.getString("alarm_code"));
                            }
                            if (object.has("alarm_name")) {
                                typeInfo.setAlarm_name(object.getString("alarm_name"));
                            }
                            if (object.has("com_id")) {
                                typeInfo.setCom_id(object.getString("com_id"));
                            }
                            if (object.has("com_name")) {
                                typeInfo.setCom_name(object.getString("com_name"));
                            }
                            list.add(typeInfo);
                        }
                        callBack.onSuccess(list);
                    }else {
                        callBack.onFailed();
                    }

                } else {//请求码为false

                    callBack.onFailed();
                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }


}