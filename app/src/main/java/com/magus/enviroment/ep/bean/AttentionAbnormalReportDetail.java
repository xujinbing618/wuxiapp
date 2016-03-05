package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.dao.ZoneManagerDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 工况异常报告bean
 * Created by ern on 2015/6/19.
 */
public class AttentionAbnormalReportDetail {
    private static final String TAG = "AttentionAbnormalReportDetail";

    private String pollSourceName; //企业名称
    private String alarmTypeName; //异常名称
    private String facilityName; //机组名称
    private int alarmLogId; //报警ID
    private String facilityBasId; //机组编号
    private String alarmTime; //时间
    private String alarmCauses;// 异常原因
    private String alarmExplain;// 异常描述
    private String alarmImage;//异常图片

    public String getAlarmImage() {
        return alarmImage;
    }

    public void setAlarmImage(String alarmImage) {
        this.alarmImage = alarmImage;
    }

    public String getAlarmExplain() {
        return alarmExplain;
    }

    public void setAlarmExplain(String alarmExplain) {
        this.alarmExplain = alarmExplain;
    }

    public String getAlarmCauses() {
        return alarmCauses;
    }

    public void setAlarmCauses(String alarmCauses) {
        this.alarmCauses = alarmCauses;
    }

    public static String getTAG() {
        return TAG;
    }

    private boolean checked; //是否选中
    private List<AttentionEnterprise> pollList = new ArrayList<AttentionEnterprise>(); //关注的企业

    private static ZoneManagerDao managerDao;

    public AttentionAbnormalReportDetail() {
    }

    public int getAlarmLogId() {
        return alarmLogId;
    }

    public void setAlarmLogId(int alarmLogId) {
        this.alarmLogId = alarmLogId;
    }

    public String getfacilityBasId() {
        return facilityBasId;
    }

    public void setfacilityBasId(String facilityBasId) {
        this.facilityBasId = facilityBasId;
    }

    public String getpollSourceName() {
        return pollSourceName;
    }

    public void setpollSourceName(String pollSourceName) {
        this.pollSourceName = pollSourceName;
    }

    public String getalarmTypeName() {
        return alarmTypeName;
    }

    public void setalarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getfacilityName() {
        return facilityName;
    }

    public void setfacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getalarmTime() {
        return alarmTime;
    }

    public void setalarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public List<AttentionEnterprise> getPollList() {
        return pollList;
    }

    public void setPollList(List<AttentionEnterprise> pollList) {
        this.pollList = pollList;
    }

    public static void parseAbnormalInfo(String response, RequestCallBack callBack, Context context) {
//        managerDao = ZoneManagerDao.getInstance(context);
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONObject object = new JSONObject(o.getString("resultEntity"));
                        AttentionAbnormalReportDetail zone = new AttentionAbnormalReportDetail();
                        if (object.has("checked")) {
                            zone.setChecked(object.getBoolean("checked"));
                        }
                        if (object.has("alarmTypeName")) {
                            zone.setalarmTypeName(object.getString("alarmTypeName"));
                        }
                        if (object.has("facilityName")) {
                            zone.setfacilityName(object.getString("facilityName") + "");
                        }
                        if (object.has("alarmLogId")) {
                            zone.setAlarmLogId(object.getInt("alarmLogId"));
                        }
                        if (object.has("alarmTime")) {
                            zone.setalarmTime(object.getString("alarmTime") + "");
                        }
                        if (object.has("alarmCauses")) {
                            zone.setAlarmCauses(object.getString("alarmCauses") + "");
                        }
                        if (object.has("alarmExplain")) {
                            zone.setAlarmExplain(object.getString("alarmExplain") + "");
                        }
                        if (object.has("facilityBasId")) {
                            zone.setfacilityBasId(object.getString("facilityBasId") + "");
                        }
                        if (object.has("pollSourceName")) {
                            zone.setpollSourceName(object.getString("pollSourceName"));
                        }
                        if(object.has("path")){
                            zone.setAlarmImage(object.getString("path"));
                        }
//                            managerDao.updateZonealarmTypeName(zone);
                        callBack.onSuccess(zone);
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed("请求失败");
        }
    }
}