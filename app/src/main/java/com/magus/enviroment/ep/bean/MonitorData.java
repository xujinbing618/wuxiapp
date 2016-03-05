package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 监控曲线数据
 * Created by pau on 15/7/29.
 */
public class MonitorData {
    private String countTime;//时间
    private String pollSourceId;
    private String pollSourceName;
    private String pollutantCode;//污染物code
    private String preAlarmTotalValue;//预警总量
    private String rtAvgValue;
    private String rtTotalDayValue;
    private String rtTotalQuarterValue;
    private String rtTotalValue;//污染物实时排放总量
    private String standAvgValue;
    private String standardTotalValue;//污染物标准排放总量
    private String totalRtId;

    public MonitorData() {
    }

    public String getCountTime() {
        return countTime;
    }

    public void setCountTime(String countTime) {
        this.countTime = countTime;
    }

    public String getPollSourceId() {
        return pollSourceId;
    }

    public void setPollSourceId(String pollSourceId) {
        this.pollSourceId = pollSourceId;
    }

    public String getPollSourceName() {
        return pollSourceName;
    }

    public void setPollSourceName(String pollSourceName) {
        this.pollSourceName = pollSourceName;
    }

    public String getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(String pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public String getPreAlarmTotalValue() {
        return preAlarmTotalValue;
    }

    public void setPreAlarmTotalValue(String preAlarmTotalValue) {
        this.preAlarmTotalValue = preAlarmTotalValue;
    }

    public String getRtAvgValue() {
        return rtAvgValue;
    }

    public void setRtAvgValue(String rtAvgValue) {
        this.rtAvgValue = rtAvgValue;
    }

    public String getRtTotalDayValue() {
        return rtTotalDayValue;
    }

    public void setRtTotalDayValue(String rtTotalDayValue) {
        this.rtTotalDayValue = rtTotalDayValue;
    }

    public String getRtTotalQuarterValue() {
        return rtTotalQuarterValue;
    }

    public void setRtTotalQuarterValue(String rtTotalQuarterValue) {
        this.rtTotalQuarterValue = rtTotalQuarterValue;
    }

    public String getRtTotalValue() {
        return rtTotalValue;
    }

    public void setRtTotalValue(String rtTotalValue) {
        this.rtTotalValue = rtTotalValue;
    }

    public String getStandAvgValue() {
        return standAvgValue;
    }

    public void setStandAvgValue(String standAvgValue) {
        this.standAvgValue = standAvgValue;
    }

    public String getStandardTotalValue() {
        return standardTotalValue;
    }

    public void setStandardTotalValue(String standardTotalValue) {
        this.standardTotalValue = standardTotalValue;
    }

    public String getTotalRtId() {
        return totalRtId;
    }

    public void setTotalRtId(String totalRtId) {
        this.totalRtId = totalRtId;
    }


    public static void parseMonitorData(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")&&!o.getString("resultEntity").trim().equals("[null]")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<MonitorData> list = new ArrayList<MonitorData>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            MonitorData monitorData = new MonitorData();
                            if (object.has("countTime")) {
                                monitorData.setCountTime(object.getString("countTime"));
                            }
                            if (object.has("rtTotalDayValue")) {
                                monitorData.setRtTotalDayValue(object.getString("rtTotalDayValue"));
                            }
                            if (object.has("pollSourceId")) {
                                monitorData.setPollSourceId(object.getString("pollSourceId"));
                            }
                            if (object.has("pollSourceName")) {
                                monitorData.setPollSourceName(object.getString("pollSourceName"));
                            }
                            if (object.has("rtAvgValue")) {
                                monitorData.setRtAvgValue(object.getString("rtAvgValue"));
                            }
                            list.add(monitorData);
                        }
                        callBack.onSuccess(list);
                    }else{
                        callBack.onFailed();
                    }
                }else{
                    callBack.onFailed();
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed();
        }
    }

//    public static MonitorData parseData(String response) {
//        try {
//            JSONObject o = new JSONObject(response);
//            if (o.has("resultCode")) {
//                if (o.getString("resultCode").equals("true")) {
//                    if (o.has("resultEntity")) {
//                        JSONArray array = new JSONArray(o.getString("resultEntity"));
//                        JSONObject object = array.getJSONObject(0);
//                        MonitorData monitorData = new MonitorData();
//                        if (object.has("standardTotalValue")) {
//                            monitorData.setStandardTotalValue(object.getString("standardTotalValue"));//总量
//                        }
//                        if (object.has("rtTotalQuarterValue")) {
//                            monitorData.setRtTotalQuarterValue(object.getString("rtTotalQuarterValue"));//剩余
//                        }
//                        if (object.has("rtTotalValue")){
//                            monitorData.setRtTotalValue(object.getString("rtTotalValue"));//使用总量
//                        }
//                        return monitorData;
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
