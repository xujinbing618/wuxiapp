package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 六类报警列表页实体类
 * Created by Administrator on 2015/11/24.
 */
public class AlarmSixListInfo {
    //工厂名称
    private String poll_source_name;
    //机组名称
    private String facility_name;
    //机组ID
    private String facility_bas_id;
    //报警类型
    private String alarm_type_name;
    //报警id
    private String alarm_type_code;
    //时间
    private String alarmtime;
    //时长
    private String timecou;
    //超标倍数
    private String over_times;
    //超标浓度
    private String over_chroma;
    //异常详情
    private String alarm_detal;


    public String getPoll_source_name() {
        return poll_source_name;
    }
    public void setPoll_source_name(String pollSourceName) {
        poll_source_name = pollSourceName;
    }
    public String getFacility_name() {
        return facility_name;
    }
    public void setFacility_name(String facilityName) {
        facility_name = facilityName;
    }
    public String getFacility_bas_id() {
        return facility_bas_id;
    }
    public void setFacility_bas_id(String facilityBasId) {
        facility_bas_id = facilityBasId;
    }
    public String getAlarm_type_name() {
        return alarm_type_name;
    }
    public void setAlarm_type_name(String alarmTypeName) {
        alarm_type_name = alarmTypeName;
    }
    public String getAlarm_type_code() {
        return alarm_type_code;
    }
    public void setAlarm_type_code(String alarmTypeCode) {
        alarm_type_code = alarmTypeCode;
    }
    public String getAlarmtime() {
        return alarmtime;
    }
    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }
    public String getTimecou() {
        return timecou;
    }
    public void setTimecou(String timecou) {
        this.timecou = timecou;
    }
    public String getOver_times() {
        return over_times;
    }
    public void setOver_times(String overTimes) {
        over_times = overTimes;
    }
    public String getOver_chroma() {
        return over_chroma;
    }
    public void setOver_chroma(String overChroma) {
        over_chroma = overChroma;
    }
    public String getAlarm_detal() {
        return alarm_detal;
    }
    public void setAlarm_detal(String alarmDetal) {
        alarm_detal = alarmDetal;
    }

    public static void parseInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")&& !o.getString("resultEntity").trim().equals("{}")) {
                        JSONObject jsonObject = new JSONObject(o.getString("resultEntity"));
                        int startRecord = 0;
                        if (jsonObject.has("startRecord")) {
                            startRecord = jsonObject.getInt("startRecord");
                        }
                        int totalRecord=0;
                        if(jsonObject.has("totalRecord")){
                            totalRecord = jsonObject.getInt("totalRecord");
                        }

                        if (jsonObject.has("data") && !jsonObject.getString("data").trim().equals("[]")) {
                            JSONArray array = new JSONArray(jsonObject.getString("data"));
                            List<AlarmSixListInfo> list = new ArrayList<AlarmSixListInfo>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmSixListInfo typeInfo = new AlarmSixListInfo();
                                if (object.has("poll_source_name")) {
                                    typeInfo.setPoll_source_name(object.getString("poll_source_name"));
                                }
                                if (object.has("facility_name")) {
                                    typeInfo.setFacility_name(object.getString("facility_name"));
                                }
                                if (object.has("facility_bas_id")) {
                                    typeInfo.setFacility_bas_id(object.getString("facility_bas_id"));
                                }
                                if (object.has("alarm_type_name")) {
                                    typeInfo.setAlarm_type_name(object.getString("alarm_type_name"));
                                }
                                if (object.has("alarm_type_code")) {
                                    typeInfo.setAlarm_type_code(object.getString("alarm_type_code"));
                                }
                                if (object.has("alarmtime")) {
                                    typeInfo.setAlarmtime(object.getString("alarmtime"));
                                }
                                if (object.has("timecou")) {
                                    typeInfo.setTimecou(object.getString("timecou"));
                                }
                                if (object.has("over_times")) {
                                    typeInfo.setOver_times(object.getString("over_times"));
                                }
                                if (object.has("over_chroma")) {
                                    typeInfo.setOver_chroma(object.getString("over_chroma"));
                                }
                                if (object.has("alarm_detal")) {
                                    typeInfo.setAlarm_detal(object.getString("alarm_detal"));
                                }
                                list.add(typeInfo);
                            }
                            callBack.onSuccess(list,startRecord,totalRecord);
                        }else{
                            callBack.onFailed();
                        }
                    }else{
                        callBack.onFailed();
                    }

                }else{
                    callBack.onFailed();
                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }
}
