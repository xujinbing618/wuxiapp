package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域率信息
 * Created by pau on 15/6/18.
 */
public class ZoneRateInfo implements Serializable {

    private static final String TAG = "ZoneRateInfo";
    private String alarmCode;
    private List<ZoneDealRate> dealRateList;
    private String zoneId;


    public static void parseZoneRateInfo(String response, RequestCallBack callBack) {

        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity")) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<ZoneDealRate> overList = new ArrayList<ZoneDealRate>();
                        List<ZoneDealRate> stopList = new ArrayList<ZoneDealRate>();
                        List<ZoneDealRate> lostList = new ArrayList<ZoneDealRate>();
                        List<ZoneDealRate> stop2List = new ArrayList<ZoneDealRate>();
                        List<ZoneDealRate> mOver2List = new ArrayList<ZoneDealRate>();
                        List<ZoneDealRate> mConstantList = new ArrayList<ZoneDealRate>();
                        for (int i = 0; i < array.length(); i++) {//超标处理率
                            JSONObject object = array.getJSONObject(i);
                            if (object.has("code")) {
                                if ("14".equals(object.getString("code"))) {
                                    getList(overList, object);
                                } else if ("5".equals(object.getString("code"))) {//机组停运处理率
                                    getList(stopList, object);
                                } else if ("7".equals(object.getString("code"))) {//数据缺失处理率
                                    getList(lostList, object);
                                } else if ("9".equals(object.getString("code"))) {//治污设施处理率
                                    getList(stop2List, object);
                                } else if ("1".equals(object.getString("code"))) {//治污设施处理率
                                    getList(mOver2List, object);
                                } else if ("12".equals(object.getString("code"))) {//治污设施处理率
                                    getList(mConstantList, object);
                                }
                            }
                        }
                        callBack.onSuccess(overList, stopList, lostList, stop2List, mOver2List, mConstantList);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed();
        }

    }

    public static void getList(List<ZoneDealRate> list, JSONObject object) {
        try {
            if (object.has("list")) {
                JSONArray array1 = new JSONArray(object.getString("list"));
                for (int j = 0; j < array1.length(); j++) {
                    ZoneDealRate rate = new ZoneDealRate();
                    JSONObject rateObject = array1.getJSONObject(j);
                    if (rateObject.has("percent")) {
                        rate.setRate(rateObject.getString("percent"));
                    }
                    list.add(rate);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
