package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2015/11/23.
 */
public class AlarmOverReport {
    private String begin_time;//开始时间
    private String facility_name;//机组名称
    private String poll_source_name;//电厂名称
    private String pollutant_name;//污染物名称
    private String overtimes;//超标倍数
    private String overchroma;//超标浓度

    public String getFacility_bas_id() {
        return facility_bas_id;
    }

    public void setFacility_bas_id(String facility_bas_id) {
        this.facility_bas_id = facility_bas_id;
    }

    private String facility_bas_id;//机组id

    public String getOverchroma() {
        return overchroma;
    }
    public void setOverchroma(String overchroma) {
        this.overchroma = overchroma;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getPoll_source_name() {
        return poll_source_name;
    }

    public void setPoll_source_name(String poll_source_name) {
        this.poll_source_name = poll_source_name;
    }

    public String getPollutant_name() {
        return pollutant_name;
    }

    public void setPollutant_name(String pollutant_name) {
        this.pollutant_name = pollutant_name;
    }

    public String getOvertimes() {
        return overtimes;
    }

    public void setOvertimes(String overtimes) {
        this.overtimes = overtimes;
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
                        List<AlarmOverReport> list = new ArrayList<AlarmOverReport>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AlarmOverReport typeInfo = new AlarmOverReport();
                            if (object.has("begin_time")) {
                                typeInfo.setBegin_time(object.getString("begin_time"));
                            }
                            if (object.has("facility_name")) {
                                typeInfo.setFacility_name(object.getString("facility_name"));
                            }
                            if (object.has("poll_source_name")) {
                                typeInfo.setPoll_source_name(object.getString("poll_source_name"));
                            }
                            if (object.has("pollutant_name")) {
                                typeInfo.setPollutant_name(object.getString("pollutant_name"));
                            }
                            if (object.has("overtimes")) {
                                typeInfo.setOvertimes(object.getString("overtimes"));
                            }
                            if (object.has("overchroma")) {
                                typeInfo.setOverchroma(object.getString("overchroma"));
                            }
                            if(object.has("facility_bas_id")){
                                typeInfo.setFacility_bas_id(object.getString("facility_bas_id"));
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

            }
        }
    } catch (Exception e) {
        callBack.onFailed();
    }
}
}
