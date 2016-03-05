package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuer on 2015/10/23.
 * 总量预警实体类
 */
public class AlarmTotal {

    private String facility_bas_id;//机组id
    private String num;//排放量
    private String total;//可排放量
    private String facility_name;//机组名称
    private String poll_source_name;//工厂名称
    private String zone_name;//城市名称
    private String quarter;//季度
    private String begin_time;//时间
    private String type;//类型
    private String warning;//预警值
    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }



    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBegin_time() {
        return begin_time;
    }
    public void setBegin_time(String beginTime) {
        begin_time = beginTime;
    }
    public String getFacility_bas_id() {
        return facility_bas_id;
    }
    public void setFacility_bas_id(String facilityBasId) {
        facility_bas_id = facilityBasId;
    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public String getFacility_name() {
        return facility_name;
    }
    public void setFacility_name(String facilityName) {
        facility_name = facilityName;
    }
    public String getPoll_source_name() {
        return poll_source_name;
    }
    public void setPoll_source_name(String pollSourceName) {
        poll_source_name = pollSourceName;
    }
    public String getZone_name() {
        return zone_name;
    }
    public void setZone_name(String zoneName) {
        zone_name = zoneName;
    }
    public String getQuarter() {
        return quarter;
    }
    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public static void parseTotalAlarmInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {

                    if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("{}")) {
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
                            List<AlarmTotal> list = new ArrayList<AlarmTotal>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmTotal typeInfo = new AlarmTotal();
                                if (object.has("facility_bas_id")) {
                                    typeInfo.setFacility_bas_id(object.getString("facility_bas_id"));
                                }
                                if (object.has("facility_name")) {
                                    typeInfo.setFacility_name(object.getString("facility_name"));
                                }
                                if (object.has("poll_source_name")) {
                                    typeInfo.setPoll_source_name(object.getString("poll_source_name"));
                                }
                                if (object.has("zone_name")) {
                                    typeInfo.setZone_name(object.getString("zone_name"));
                                }
                                if (object.has("quarter")) {
                                    typeInfo.setQuarter(object.getString("quarter"));
                                }
                                if (object.has("begin_time")) {
                                    typeInfo.setBegin_time(object.getString("begin_time"));
                                }
                                if (object.has("num")) {
                                    typeInfo.setNum(object.getString("num"));
                                }
                                if (object.has("type")) {
                                    typeInfo.setType(object.getString("type"));
                                }
                                if (object.has("total")) {
                                    typeInfo.setTotal(object.getString("total"));
                                }
                                if (object.has("warning")) {
                                    typeInfo.setWarning(object.getString("warning"));
                                }
                                list.add(typeInfo);
                            }
                            callBack.onSuccess(list, startRecord,totalRecord);
                        }else {
                            callBack.onFailed();
                        }
                    }  else {
                        callBack.onFailed();
                    }
                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }
}
