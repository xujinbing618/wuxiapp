package com.magus.enviroment.ep.bean;

import android.content.Context;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class AlarmGrid {
    private String count;//总数
    private String alarmtypename;//类型名称
    private String num;//处理数
    private String percent;//处理率
    private String grid_id;//网格id

    public String getGrid_name() {
        return grid_name;
    }

    public void setGrid_name(String grid_name) {
        this.grid_name = grid_name;
    }

    public String getGrid_id() {
        return grid_id;
    }

    public void setGrid_id(String grid_id) {
        this.grid_id = grid_id;
    }

    private String grid_name;//网格名称

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAlarmtypename() {
        return alarmtypename;
    }

    public void setAlarmtypename(String alarmtypename) {
        this.alarmtypename = alarmtypename;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
    public static void parseGridInfo(String response, RequestCallBack callBack, Context context) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AlarmGrid> list = new ArrayList<AlarmGrid>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AlarmGrid typeInfo = new AlarmGrid();
                            if (object.has("count")) {
                                typeInfo.setCount(object.getString("count"));
                            }
                            if (object.has("num")) {
                                typeInfo.setNum(object.getString("num"));
                            }
                            if (object.has("alarmtypename")) {
                                typeInfo.setAlarmtypename(object.getString("alarmtypename"));
                            }
                            if (object.has("percent")) {
                                typeInfo.setPercent(object.getString("percent"));
                            }

                            list.add(typeInfo);
                        }
                        callBack.onSuccess(list);
                    }
                }
            }
        } catch (Exception e) {
            callBack.onFailed();
        }
    }
}
