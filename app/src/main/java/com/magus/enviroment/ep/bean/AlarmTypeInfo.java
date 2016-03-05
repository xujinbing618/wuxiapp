package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.MyApplication;
import com.magus.enviroment.ep.callback.RequestCallBack;
import com.magus.enviroment.ep.dao.AlarmTypeDao;
import com.magus.magusutils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 报警类型
 * Created by pau on 15/6/9.
 */
public class AlarmTypeInfo {
    private String alarmCode;//报警代码
    private String alarmTypeName;//类型名称
    private String checked;//是否选中

    private static AlarmTypeDao alarmTypeDao;

    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public static void parseAlarmTypeInfo(String response, RequestCallBack callBack, Context context) {
        alarmTypeDao = AlarmTypeDao.getInstance(context);
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONObject obj = new JSONObject(o.getString("resultEntity"));
                        if (obj.has("function")){
                            JSONArray array = new JSONArray(obj.getString("function"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                if (object.has("afr_id")) {
                                    if ("gn_0001".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0001", "true");
                                        //MyApplication.setGn_0001(true);
                                    }
                                    if ("gn_0002".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0002","true");
                                      //  MyApplication.setGn_0002(true);
                                    }
                                    if ("gn_0003".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0003", "true");
                                       // MyApplication.setGn_0003(true);
                                    }
                                    if ("gn_0004".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0004", "true");
                                       // MyApplication.setGn_0004(true);
                                    }
                                    if ("gn_0005".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0005", "true");
                                       // MyApplication.setGn_0005(true);
                                    }
                                    if ("gn_0006".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0006", "true");
                                       // MyApplication.setGn_0006(true);
                                    }
                                    if ("gn_0007".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0007", "true");
                                       // MyApplication.setGn_0007(true);
                                    }
                                    if ("gn_0008".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0008", "true");
                                        //MyApplication.setGn_0008(true);
                                    }
                                    if ("gn_0009".equals(object.getString("afr_id"))){
                                        SharedPreferenceUtil.save("gn_0009","true");
                                      //  MyApplication.setGn_0009(true);
                                    }
                                }
                            }
                        }
                        if (obj.has("select")) {
                            JSONArray array = new JSONArray(obj.getString("select"));
                            List<AlarmTypeInfo> list = new ArrayList<AlarmTypeInfo>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                AlarmTypeInfo typeInfo = new AlarmTypeInfo();
                                if (object.has("asr_id")) {
                                    typeInfo.setAlarmCode(object.getString("asr_id"));
                                }
                                if (object.has("asr_name")) {
                                    typeInfo.setAlarmTypeName(object.getString("asr_name"));
                                }
                                if (object.has("checked")) {
                                    typeInfo.setChecked(object.getString("checked"));
                                }
                                alarmTypeDao.insertAlarmType(typeInfo);

                                list.add(typeInfo);

                            }
                            callBack.onSuccess(list);
                        }

                    }
                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }

}
