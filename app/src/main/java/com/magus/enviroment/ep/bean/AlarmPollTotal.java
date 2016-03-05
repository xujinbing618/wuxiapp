package com.magus.enviroment.ep.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 公司排放总量，预警量，排放量
 * @author Administrator
 *
 */
public class AlarmPollTotal {
    //排放量
    private String num;
    //当前季度许可总量
    private String total;
    //预警量
    private String warning;
    //剩余排放量
    private String surplus;

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
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
    public String getWarning() {
        return warning;
    }
    public void setWarning(String warning) {
        this.warning = warning;
    }


    public static AlarmPollTotal parseData(String response) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONObject object = new JSONObject(o.getString("resultEntity"));
                        AlarmPollTotal monitorData = new AlarmPollTotal();
                        if (object.has("num")) {
                            monitorData.setNum(object.getString("num"));//总量
                        }
                        if (object.has("total")) {
                            monitorData.setTotal(object.getString("total"));//剩余
                        }
                        if (object.has("warning")){
                            monitorData.setWarning(object.getString("warning"));//使用总量
                        }
                        if (object.has("surplus")){
                            monitorData.setSurplus(object.getString("surplus"));//使用总量
                        }
                        return monitorData;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }return null;
    }
}

