package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 机组详情
 * Created by pau on 15/6/9.
 */
public class AlarmDetailInfo implements Serializable {
    private static final String TAG="AlarmDetailInfo";
    private static final long serialVersionUID = 1L;

    private String alarmCauses = "";//报警原因
    private String alarmCode = ""; // 报警类型代码
    private String alarmExplain = ""; //解释
    private String alarmTypeName = ""; //类型名称
    private String beginDate = ""; //开始时间
    private String dealStatus = ""; //处理状态
    private String endDate = "";  //结束时间
    private String facilitiesTypeId = "";//机组类型id
    private String facilitiesTypeName = "";//机组类型名称
    private String facilityBasId = ""; //机组id
    private String facilityName = ""; //机组名

    private String isRead = ""; //阅读状态
    private int num;//排放值
    private String pollSourceName = "";//企业名称
    private int timeCou;//次数
    private String zoneName = "";//区域名称
    private String alarmTime="";//报警时间
    private int startRecord;//起始位置

    private String facility_name="";//机组名称

    private String dealDetail="";//处理详情

    private String dealTime="";//处理时间
    private String userId="";
    private String userName="";
    private String alarmLogId="";//报警日志id

    private boolean isChecked=false;//污染源详情列表是否被选中

    private String overTimes="";//超标倍数
    private String pollutantName="";//超标污染物
    private String begin_time;//预警时间
    private String fact_out_nd;//预警值
    public String getFact_out_nd() {
        return fact_out_nd;
    }

    public void setFact_out_nd(String fact_out_nd) {
        this.fact_out_nd = fact_out_nd;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }
    public AlarmDetailInfo() {
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getAlarmCauses() {
        return alarmCauses;
    }

    public void setAlarmCauses(String alarmCauses) {
        this.alarmCauses = alarmCauses;
    }

    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmExplain() {
        return alarmExplain;
    }

    public void setAlarmExplain(String alarmExplain) {
        this.alarmExplain = alarmExplain;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFacilitiesTypeId() {
        return facilitiesTypeId;
    }

    public void setFacilitiesTypeId(String facilitiesTypeId) {
        this.facilitiesTypeId = facilitiesTypeId;
    }
    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
    public String getFacilitiesTypeName() {
        return facilitiesTypeName;
    }

    public void setFacilitiesTypeName(String facilitiesTypeName) {
        this.facilitiesTypeName = facilitiesTypeName;
    }

    public String getFacilityBasId() {
        return facilityBasId;
    }

    public void setFacilityBasId(String facilityBasId) {
        this.facilityBasId = facilityBasId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPollSourceName() {
        return pollSourceName;
    }

    public void setPollSourceName(String pollSourceName) {
        this.pollSourceName = pollSourceName;
    }

    public int getTimeCou() {
        return timeCou;
    }

    public void setTimeCou(int timeCou) {
        this.timeCou = timeCou;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    public String getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(String dealDetail) {
        this.dealDetail = dealDetail;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
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

    public String getAlarmLogId() {
        return alarmLogId;
    }

    public void setAlarmLogId(String alarmLogId) {
        this.alarmLogId = alarmLogId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getOverTimes() {
        return overTimes;
    }

    public void setOverTimes(String overTimes) {
        this.overTimes = overTimes;
    }

    public String getPollutantName() {
        return pollutantName;
    }

    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public static void parseAlarmDetailInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")&& !o.getString("resultEntity").trim().equals("{}")) {
                        JSONObject jsonObject = new JSONObject(o.getString("resultEntity"));
                        int startRecord=0;

                        if (jsonObject.has("startRecord")){
                            startRecord = jsonObject.getInt("startRecord");
                        }
                        int totalRecord=0;
                        if(jsonObject.has("totalRecord")){
                            totalRecord = jsonObject.getInt("totalRecord");
                        }
                        if (jsonObject.has("data") && !jsonObject.getString("data").trim().equals("[]")) {
                            JSONArray array = new JSONArray(jsonObject.getString("data"));
                            List<AlarmDetailInfo> list = new ArrayList<AlarmDetailInfo>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmDetailInfo typeInfo = new AlarmDetailInfo();
                                if (object.has("alarmCauses")) {
                                    typeInfo.setAlarmCauses(object.getString("alarmCauses"));
                                }
                                if (object.has("alarmCode")) {
                                    typeInfo.setAlarmCode(object.getString("alarmCode"));
                                }
                                if (object.has("is_read")) {
                                    typeInfo.setIsRead(object.getString("is_read"));
                                }
                                if (object.has("alarmExplain")) {
                                    typeInfo.setAlarmExplain(object.getString("alarmExplain"));
                                }
                                if (object.has("alarmTypeName")) {
                                    typeInfo.setAlarmTypeName(object.getString("alarmTypeName"));
                                }
                                if (object.has("dealStatus")) {
                                    typeInfo.setDealStatus(object.getString("dealStatus"));
                                }
                                if (object.has("beginDate")) {
                                    typeInfo.setBeginDate(object.getString("beginDate"));
                                }
                                if (object.has("endDate")) {
                                    typeInfo.setEndDate(object.getString("endDate"));
                                }
                                if (object.has("facilitiesTypeId")) {
                                    typeInfo.setFacilitiesTypeId(object.getString("facilitiesTypeId"));
                                }
                                if (object.has("facilitiesTypeName")) {
                                    typeInfo.setFacilitiesTypeName(object.getString("facilitiesTypeName"));
                                }
                                if (object.has("facilityName")){
                                    typeInfo.setFacilityName(object.getString("facilityName"));
                                }
                                if (object.has("facility_name")){
                                    typeInfo.setFacility_name(object.getString("facility_name"));
                                }
                                if (object.has("facilityBasId")){
                                    typeInfo.setFacilityBasId(object.getString("facilityBasId"));
                                }
                                if (object.has("num")) {
                                    typeInfo.setNum(object.getInt("num"));
                                }
                                if (object.has("pollSourceName")) {
                                    typeInfo.setPollSourceName(object.getString("pollSourceName"));
                                }
                                if (object.has("timeCou")) {
                                    typeInfo.setTimeCou(object.getInt("timeCou"));
                                }
                                if (object.has("zoneName")) {
                                    typeInfo.setZoneName(object.getString("zoneName"));
                                }
                                if (object.has("alarmTime")) {
                                    typeInfo.setAlarmTime(object.getString("alarmTime"));
                                }
                                if (object.has("dealDetail")){
                                    typeInfo.setDealDetail(object.getString("dealDetail"));
                                }
                                if (object.has("dealTime")){
                                    typeInfo.setDealTime(object.getString("dealTime"));
                                }
                                if (object.has("userId")){
                                    typeInfo.setUserId(object.getString("userId"));
                                }
                                if (object.has("userName")){
                                    typeInfo.setUserName(object.getString("userName"));
                                }
                                if (object.has("alarmLogId")){
                                    typeInfo.setAlarmLogId(object.getString("alarmLogId"));
                                }
                                if (object.has("pollutantName")){
                                    typeInfo.setPollutantName(object.getString("pollutantName"));
                                }
                                if (object.has("begin_time")) {
                                    typeInfo.setBegin_time(object.getString("begin_time"));
                                }
                                if (object.has("fact_out_nd")) {
                                    typeInfo.setFact_out_nd(object.getString("fact_out_nd"));
                                }
                                list.add(typeInfo);
                            }
                            callBack.onSuccess(list,startRecord,totalRecord);
                        }else {
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

    public static void parseOverAlarmInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {

                    if (o.has("resultEntity")) {
                        JSONObject jsonObject = new JSONObject(o.getString("resultEntity"));
                        int startRecord = 0;
                        if (jsonObject.has("startRecord")) {
                            startRecord = jsonObject.getInt("startRecord");
                        }
                        int totalRecord = 0;
                        if (jsonObject.has("totalRecord")) {
                            totalRecord = jsonObject.getInt("totalRecord");
                        }
                        if (jsonObject.has("data") && !jsonObject.getString("data").trim().equals("[]")) {
                            JSONArray array = new JSONArray(jsonObject.getString("data"));
                            List<AlarmDetailInfo> list = new ArrayList<AlarmDetailInfo>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmDetailInfo typeInfo = new AlarmDetailInfo();
                                if (object.has("alarmCauses")) {
                                    typeInfo.setAlarmCauses(object.getString("alarmCauses"));
                                }
                                if (object.has("alarmCode")) {
                                    typeInfo.setAlarmCode(object.getString("alarmCode"));
                                }
                                if (object.has("alarmExplain")) {
                                    typeInfo.setAlarmExplain(object.getString("alarmExplain"));
                                }
                                if (object.has("alarmTypeName")) {
                                    typeInfo.setAlarmTypeName(object.getString("alarmTypeName"));
                                }
                                if (object.has("dealStatus")) {
                                    typeInfo.setDealStatus(object.getString("dealStatus"));
                                }
                                if (object.has("beginDate")) {
                                    typeInfo.setBeginDate(object.getString("beginDate"));
                                }
                                if (object.has("endDate")) {
                                    typeInfo.setEndDate(object.getString("endDate"));
                                }
                                if (object.has("facilitiesTypeId")) {
                                    typeInfo.setFacilitiesTypeId(object.getString("facilitiesTypeId"));
                                }
                                if (object.has("facilitiesTypeName")) {
                                    typeInfo.setFacilitiesTypeName(object.getString("facilitiesTypeName"));
                                }
                                if (object.has("facilityName")) {
                                    typeInfo.setFacilityName(object.getString("facilityName"));
                                }
                                if (object.has("facilityBasId")) {
                                    typeInfo.setFacilityBasId(object.getString("facilityBasId"));
                                }
                                if (object.has("num")) {
                                    typeInfo.setNum(object.getInt("num"));
                                }
                                if (object.has("pollSourceName")) {
                                    typeInfo.setPollSourceName(object.getString("pollSourceName"));
                                }
                                if (object.has("timeCou")) {
                                    typeInfo.setTimeCou(object.getInt("timeCou"));
                                }
                                if (object.has("zoneName")) {
                                    typeInfo.setZoneName(object.getString("zoneName"));
                                }
                                if (object.has("alarmTime")) {
                                    typeInfo.setAlarmTime(object.getString("alarmTime"));
                                }
                                if (object.has("dealDetail")) {
                                    typeInfo.setDealDetail(object.getString("dealDetail"));
                                }
                                if (object.has("dealTime")) {
                                    typeInfo.setDealTime(object.getString("dealTime"));
                                }
                                if (object.has("userId")) {
                                    typeInfo.setUserId(object.getString("userId"));
                                }
                                if (object.has("userName")) {
                                    typeInfo.setUserName(object.getString("userName"));
                                }
                                if (object.has("alarmLogId")) {
                                    typeInfo.setAlarmLogId(object.getString("alarmLogId"));
                                }
                                if (object.has("overTimes")) {
                                    typeInfo.setOverTimes(object.getString("overTimes"));
                                }
                                if (object.has("pollutantName")) {
                                    typeInfo.setPollutantName(object.getString("pollutantName"));
                                }
                                if (object.has("begin_time")) {
                                    typeInfo.setBegin_time(object.getString("begin_time"));
                                }
                                if (object.has("fact_out_nd")) {
                                    typeInfo.setFact_out_nd(object.getString("fact_out_nd"));
                                }
                                list.add(typeInfo);
                            }
                            callBack.onSuccess(list,startRecord,totalRecord);
                        }else{
                            callBack.onFailed();
                        }

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
