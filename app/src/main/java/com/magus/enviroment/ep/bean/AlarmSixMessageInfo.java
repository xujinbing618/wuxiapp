package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 六类报警详情页实体类
 * Created by Administrator on 2015/11/24.
 */
public class AlarmSixMessageInfo {
    private String sid;// 'id';
    private String alarm_log_id;// '预警id';
    private String poll_source_name;// '工厂名称';
    private String poll_source_id;// '工厂id';
    private String facility_name;// '机组名称';
    private String facility_bas_id;// '机组id';
    private String alarm_type_name;// '报警名称';
    private String alarm_type_code;// '报警编码';
    private String cou;// '报警次数';
    private String alarmtime;// '报警时间';7
    private String timecou;// '报警持续时间';
    private String deal_status;// '处理状态';
    private String is_read;// '1为已读,null 为未读';
    private String create_time;// '创建时间';
    private String handle_people_name;// '处理人姓名';
    private String handle_people_id;// '处理人id';
    private String handle_status;// '处理状态';
    private String handle_remark;// '处理描述';
    private String handle_time;// '处理时间';
    private String over_times;// '超标倍数';
    private String over_chroma;// '超标浓度';
    private String alarm_detal;// '异常详情';
    private String electric_poll_source_type;//工厂类型
    private String overtypename;//超标类型


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAlarm_log_id() {
        return alarm_log_id;
    }

    public void setAlarm_log_id(String alarmLogId) {
        alarm_log_id = alarmLogId;
    }

    public String getPoll_source_name() {
        return poll_source_name;
    }

    public void setPoll_source_name(String pollSourceName) {
        poll_source_name = pollSourceName;
    }

    public String getPoll_source_id() {
        return poll_source_id;
    }

    public void setPoll_source_id(String pollSourceId) {
        poll_source_id = pollSourceId;
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

    public String getCou() {
        return cou;
    }

    public void setCou(String cou) {
        this.cou = cou;
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

    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String dealStatus) {
        deal_status = dealStatus;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String isRead) {
        is_read = isRead;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String createTime) {
        create_time = createTime;
    }

    public String getHandle_people_name() {
        return handle_people_name;
    }

    public void setHandle_people_name(String handlePeopleName) {
        handle_people_name = handlePeopleName;
    }

    public String getHandle_people_id() {
        return handle_people_id;
    }

    public void setHandle_people_id(String handlePeopleId) {
        handle_people_id = handlePeopleId;
    }

    public String getHandle_status() {
        return handle_status;
    }

    public void setHandle_status(String handleStatus) {
        handle_status = handleStatus;
    }

    public String getHandle_remark() {
        return handle_remark;
    }

    public void setHandle_remark(String handleRemark) {
        handle_remark = handleRemark;
    }

    public String getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(String handleTime) {
        handle_time = handleTime;
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
    public String getElectric_poll_source_type() {
        return electric_poll_source_type;
    }

    public void setElectric_poll_source_type(String electric_poll_source_type) {
        this.electric_poll_source_type = electric_poll_source_type;
    }

    public String getOvertypename() {
        return overtypename;
    }

    public void setOvertypename(String overtypename) {
        this.overtypename = overtypename;
    }

    public static void parseInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity") && !o.getString("resultEntity").trim().equals("[]")) {
                            JSONArray array = new JSONArray(o.getString("resultEntity"));
                            List<AlarmSixMessageInfo> list = new ArrayList<AlarmSixMessageInfo>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmSixMessageInfo typeInfo = new AlarmSixMessageInfo();
                                if (object.has("sid")) {
                                    typeInfo.setSid(object.getString("sid"));
                                }
                                if (object.has("alarm_log_id")) {
                                    typeInfo.setAlarm_log_id(object.getString("alarm_log_id"));
                                }
                                if (object.has("poll_source_name")) {
                                    typeInfo.setPoll_source_name(object.getString("poll_source_name"));
                                }
                                if (object.has("poll_source_id")) {
                                    typeInfo.setPoll_source_id(object.getString("poll_source_id"));
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
                                if (object.has("cou")) {
                                    typeInfo.setCou(object.getString("cou"));
                                }
                                if (object.has("alarmtime")) {
                                    typeInfo.setAlarmtime(object.getString("alarmtime"));
                                }
                                if (object.has("timecou")) {
                                    typeInfo.setTimecou(object.getString("timecou"));
                                }
                                if (object.has("deal_status")) {
                                    typeInfo.setDeal_status(object.getString("deal_status"));
                                }
                                if (object.has("is_read")) {
                                    typeInfo.setIs_read(object.getString("is_read"));
                                }
                                if (object.has("create_time")) {
                                    typeInfo.setCreate_time(object.getString("create_time"));
                                }
                                if (object.has("handle_people_name")) {
                                    typeInfo.setHandle_people_name(object.getString("handle_people_name"));
                                }
                                if (object.has("handle_people_id")) {
                                    typeInfo.setHandle_people_id(object.getString("handle_people_id"));
                                }
                                if (object.has("handle_status")) {
                                    typeInfo.setHandle_status(object.getString("handle_status"));
                                }
                                if (object.has("handle_remark")) {
                                    typeInfo.setHandle_remark(object.getString("handle_remark"));
                                }
                                if (object.has("handle_time")) {
                                    typeInfo.setHandle_time(object.getString("handle_time"));
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
                                if (object.has("electric_poll_source_type")) {
                                    typeInfo.setElectric_poll_source_type(object.getString("electric_poll_source_type"));
                                }
                                if (object.has("overtypename")) {
                                    typeInfo.setOvertypename(object.getString("overtypename"));
                                }
                                list.add(typeInfo);
                            }
                            callBack.onSuccess(list);

                    } else {
                        callBack.onFailed();
                    }

                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }
}
