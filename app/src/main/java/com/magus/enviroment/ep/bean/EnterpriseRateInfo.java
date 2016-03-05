package com.magus.enviroment.ep.bean;


import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业率
 * Created by pau on 15/6/17.
 */
public class EnterpriseRateInfo implements Serializable {
    private static final String TAG = "EnterpriseRateInfo";
    private String alarmTime;//报警时间
    private List<DealRate> dealRates; //处理率
    private String dealStatus; //处理状态
    private String dealTime; //处理实际那
    private String pollSourceId; //企业id
    private String pollSourceName; //企业名称
    private String userId; //用户id
    private String userName; //用户名
    private String zoneId; //区域id
    private String zoneName; //区域名称
    private String shortName;//企业简称

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public List<DealRate> getDealRates() {
        return dealRates;
    }

    public void setDealRates(List<DealRate> dealRates) {
        this.dealRates = dealRates;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    /**
     * 解析企业率
     *
     * @param response
     * @param callBack
     */
    public static void parseEnterpriseRateInfo(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<EnterpriseRateInfo> list = new ArrayList<EnterpriseRateInfo>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            EnterpriseRateInfo rateInfo = new EnterpriseRateInfo();
                            if (object.has("alarmTime")) {
                                rateInfo.setAlarmTime(object.getString("alarmTime"));
                            }
                            if (object.has("dealStatus")) {
                                rateInfo.setDealStatus(object.getString("dealStatus"));
                            }
                            if (object.has("dealTime")) {
                                rateInfo.setDealTime(object.getString("dealTime"));
                            }
                            if (object.has("pollSourceId")) {
                                rateInfo.setPollSourceId(object.getString("pollSourceId"));
                            }
                            if (object.has("pollSourceName")) {
                                rateInfo.setPollSourceName(object.getString("pollSourceName"));
                            }
                            if (object.has("userId")) {
                                rateInfo.setUserId(object.getString("userId"));
                            }
                            if (object.has("userName")) {
                                rateInfo.setUserName(object.getString("userName"));
                            }
                            if (object.has("zoneId")) {
                                rateInfo.setZoneId(object.getString("zoneId"));
                            }
                            if (object.has("zoneName")) {
                                rateInfo.setZoneName(object.getString("zoneName"));
                            }
                            if (object.has("shortName")) {
                                rateInfo.setShortName(object.getString("shortName"));
                            }
                            if (object.has("alarmTypeDealRate")) {
                                JSONArray array2 = new JSONArray(object.getString("alarmTypeDealRate"));
//                                Log.e(TAG,"array2.length()"+array2.length());
                                List<DealRate> dealRateList = new ArrayList<DealRate>();
                                for (int j = 0; j < array2.length(); j++) {
                                    JSONObject rateObject = array2.getJSONObject(j);
                                    DealRate rate = new DealRate();
                                    if (rateObject.has("alarmCode")) {
                                        rate.setAlarmCode(rateObject.getString("alarmCode"));
                                    }
                                    if (rateObject.has("alarmName")) {
                                        rate.setAlarmName(rateObject.getString("alarmName"));
                                    }
                                    if (rateObject.has("dealCount")) {
                                        rate.setDealCount(rateObject.getInt("dealCount") + "");
                                    }
                                    if (rateObject.has("rate")) {
                                        rate.setRate(rateObject.getString("rate"));
                                    }
                                    if (rateObject.has("sumCount")) {
                                        rate.setSumCount(rateObject.getInt("sumCount") + "");
                                    }
                                    dealRateList.add(rate);
                                }
                                rateInfo.setDealRates(dealRateList);
                            }
                            list.add(rateInfo);
                        }
                        callBack.onSuccess(list);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed();
        }
    }
}
