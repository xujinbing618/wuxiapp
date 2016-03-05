package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuer on 2015/10/20.
 * 超标预警实体类
 */
public class AlarmPic {
    private static final String TAG = "AlarmPic";
    private static final long serialVersionUID = 1L;
    private String alarm_address;//地址
    private String alarm_bas_id;//机组id
    private String alarm_basname;//机组名称
    private String alarm_city_id;//城市id
    private String alarm_cityname;//城市名称
    private String alarm_county_id;
    private String alarm_crtdatetime;
    private String alarm_desc;//详情描述
    private String alarm_emplyee;//-1
    private String alarm_fac_id;//工厂id
    private String alarm_facname;//工厂名称
    private String alarm_isvalid;
    private String alarm_path;//图片地址
    private String alarm_sid;//信息id
    private String alarm_time;//发生时间
    private String alarm_title;//自定义报警名称
    private String alarm_type_id;//报警类型id
    private String alarm_type_name;
    private String alarm_uptdatetime;
    private String alarm_uptemplyee;

    public String getAlarm_address() {
        return alarm_address;
    }

    public void setAlarm_address(String alarm_address) {
        this.alarm_address = alarm_address;
    }

    public String getAlarm_bas_id() {
        return alarm_bas_id;
    }

    public void setAlarm_bas_id(String alarm_bas_id) {
        this.alarm_bas_id = alarm_bas_id;
    }

    public String getAlarm_basname() {
        return alarm_basname;
    }

    public void setAlarm_basname(String alarm_basname) {
        this.alarm_basname = alarm_basname;
    }

    public String getAlarm_city_id() {
        return alarm_city_id;
    }

    public void setAlarm_city_id(String alarm_city_id) {
        this.alarm_city_id = alarm_city_id;
    }

    public String getAlarm_cityname() {
        return alarm_cityname;
    }

    public void setAlarm_cityname(String alarm_cityname) {
        this.alarm_cityname = alarm_cityname;
    }

    public String getAlarm_county_id() {
        return alarm_county_id;
    }

    public void setAlarm_county_id(String alarm_county_id) {
        this.alarm_county_id = alarm_county_id;
    }

    public String getAlarm_crtdatetime() {
        return alarm_crtdatetime;
    }

    public void setAlarm_crtdatetime(String alarm_crtdatetime) {
        this.alarm_crtdatetime = alarm_crtdatetime;
    }

    public String getAlarm_desc() {
        return alarm_desc;
    }

    public void setAlarm_desc(String alarm_desc) {
        this.alarm_desc = alarm_desc;
    }

    public String getAlarm_emplyee() {
        return alarm_emplyee;
    }

    public void setAlarm_emplyee(String alarm_emplyee) {
        this.alarm_emplyee = alarm_emplyee;
    }

    public String getAlarm_fac_id() {
        return alarm_fac_id;
    }

    public void setAlarm_fac_id(String alarm_fac_id) {
        this.alarm_fac_id = alarm_fac_id;
    }

    public String getAlarm_facname() {
        return alarm_facname;
    }

    public void setAlarm_facname(String alarm_facname) {
        this.alarm_facname = alarm_facname;
    }

    public String getAlarm_isvalid() {
        return alarm_isvalid;
    }

    public void setAlarm_isvalid(String alarm_isvalid) {
        this.alarm_isvalid = alarm_isvalid;
    }

    public String getAlarm_path() {
        return alarm_path;
    }

    public void setAlarm_path(String alarm_path) {
        this.alarm_path = alarm_path;
    }

    public String getAlarm_sid() {
        return alarm_sid;
    }

    public void setAlarm_sid(String alarm_sid) {
        this.alarm_sid = alarm_sid;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getAlarm_title() {
        return alarm_title;
    }

    public void setAlarm_title(String alarm_title) {
        this.alarm_title = alarm_title;
    }

    public String getAlarm_type_id() {
        return alarm_type_id;
    }

    public void setAlarm_type_id(String alarm_type_id) {
        this.alarm_type_id = alarm_type_id;
    }

    public String getAlarm_type_name() {
        return alarm_type_name;
    }

    public void setAlarm_type_name(String alarm_type_name) {
        this.alarm_type_name = alarm_type_name;
    }

    public String getAlarm_uptdatetime() {
        return alarm_uptdatetime;
    }

    public void setAlarm_uptdatetime(String alarm_uptdatetime) {
        this.alarm_uptdatetime = alarm_uptdatetime;
    }

    public String getAlarm_uptemplyee() {
        return alarm_uptemplyee;
    }

    public void setAlarm_uptemplyee(String alarm_uptemplyee) {
        this.alarm_uptemplyee = alarm_uptemplyee;
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
                            List<AlarmPic> list = new ArrayList<AlarmPic>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmPic typeInfo = new AlarmPic();
                                if (object.has("alarm_address")) {
                                    typeInfo.setAlarm_address(object.getString("alarm_address"));
                                }
                                if (object.has("alarm_bas_id")) {
                                    typeInfo.setAlarm_bas_id(object.getString("alarm_bas_id"));
                                }
                                if (object.has("alarm_basname")) {
                                    typeInfo.setAlarm_basname(object.getString("alarm_basname"));
                                }
                                if (object.has("alarm_city_id")) {
                                    typeInfo.setAlarm_city_id(object.getString("alarm_city_id"));
                                }
                                if (object.has("alarm_cityname")) {
                                    typeInfo.setAlarm_cityname(object.getString("alarm_cityname"));
                                }
                                if (object.has("alarm_county_id")) {
                                    typeInfo.setAlarm_county_id(object.getString("alarm_county_id"));
                                }
                                if (object.has("alarm_crtdatetime")) {
                                    typeInfo.setAlarm_crtdatetime(object.getString("alarm_crtdatetime"));
                                }
                                if (object.has("alarm_desc")) {
                                    typeInfo.setAlarm_desc(object.getString("alarm_desc"));
                                }
                                if (object.has("alarm_emplyee")) {
                                    typeInfo.setAlarm_emplyee(object.getString("alarm_emplyee"));
                                }
                                if (object.has("alarm_fac_id")) {
                                    typeInfo.setAlarm_fac_id(object.getString("alarm_fac_id"));
                                }
                                if (object.has("alarm_facname")) {
                                    typeInfo.setAlarm_facname(object.getString("alarm_facname"));
                                }
                                if (object.has("alarm_isvalid")) {
                                    typeInfo.setAlarm_isvalid(object.getString("alarm_isvalid"));
                                }
                                if (object.has("alarm_path")) {
                                    typeInfo.setAlarm_path(object.getString("alarm_path"));
                                }
                                if (object.has("alarm_sid")) {
                                    typeInfo.setAlarm_sid(object.getString("alarm_sid"));
                                }
                                if (object.has("alarm_time")) {
                                    typeInfo.setAlarm_time(object.getString("alarm_time"));
                                }
                                if (object.has("alarm_type_id")) {
                                    typeInfo.setAlarm_type_id(object.getString("alarm_type_id"));
                                }
                                if (object.has("alarm_type_name")) {
                                    typeInfo.setAlarm_type_name(object.getString("alarm_type_name"));
                                }
                                if (object.has("alarm_uptdatetime")) {
                                    typeInfo.setAlarm_uptdatetime(object.getString("alarm_uptdatetime"));
                                }
                                if (object.has("alarm_uptemplyee")) {
                                    typeInfo.setAlarm_uptemplyee(object.getString("alarm_uptemplyee"));
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
