package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 报警原因
 * Created by pau on 15/6/15.
 */
public class AlarmCauseInfo {

    private static final long serialVersionUID = 1L;
    private String dicId;
    private String dicName;
    private String dicCode;
    private String dicValue;
    private String dicOrder;
    private String dicMemo;
    private String userId;
    private String parentDicId;
    private String dtCreate;
    private String isSys;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicOrder() {
        return dicOrder;
    }

    public void setDicOrder(String dicOrder) {
        this.dicOrder = dicOrder;
    }

    public String getDicMemo() {
        return dicMemo;
    }

    public void setDicMemo(String dicMemo) {
        this.dicMemo = dicMemo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentDicId() {
        return parentDicId;
    }

    public void setParentDicId(String parentDicId) {
        this.parentDicId = parentDicId;
    }

    public String getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(String dtCreate) {
        this.dtCreate = dtCreate;
    }

    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    public static void parseAlarmCauseInfo(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AlarmCauseInfo> list = new ArrayList<AlarmCauseInfo>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            AlarmCauseInfo causeInfo = new AlarmCauseInfo();
                            if (object.has("dicCode")) {
                                causeInfo.setDicCode(object.getString("dicCode"));
                            }
                            if (object.has("dicId")) {
                                causeInfo.setDicId(object.getString("dicId"));
                            }
                            if (object.has("dicMemo")) {
                                causeInfo.setDicMemo(object.getString("dicMemo"));
                            }
                            if (object.has("dicName")) {
                                causeInfo.setDicName(object.getString("dicName"));
                            }
                            if (object.has("dicOrder")) {
                                causeInfo.setDicOrder(object.getString("dicOrder"));
                            }
                            if (object.has("dicValue")) {
                                causeInfo.setDicValue(object.getString("dicValue"));
                            }
                            if (object.has("dtCreate")) {
                                causeInfo.setDtCreate(object.getString("dtCreate"));
                            }
                            if (object.has("isSys")) {
                                causeInfo.setIsSys(object.getString("isSys"));
                            }
                            if (object.has("userId")) {
                                causeInfo.setUserId(object.getString("userId"));
                            }
                            list.add(causeInfo);
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
