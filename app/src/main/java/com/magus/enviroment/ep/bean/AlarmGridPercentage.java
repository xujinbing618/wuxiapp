package com.magus.enviroment.ep.bean;


import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 网格报警处理率
 *
 * @author Administrator
 */
public class AlarmGridPercentage implements Serializable {
    //工厂名称
    private String poll_source_name;
    //工厂id
    private String poll_source_id;
    //报警类型名称
    private String alarm_type_name;
    //报警类型ID
    private String alarm_type_code;
    //时间
    private String time;
    //总数
    private String count;
    //处理数
    private String num;
    //处理率
    private String percentage;

    public List<DealRate> getDealRates() {
        return dealRates;
    }

    public void setDealRates(List<DealRate> dealRates) {
        this.dealRates = dealRates;
    }

    private List<DealRate> dealRates; //处理率


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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * 解析企业率
     *
     * @param response
     * @param callBack
     */
    public static void parseInfo(String response, RequestCallBack callBack) {
        try {
            JSONObject o = new JSONObject(response);
            if (o.has("resultCode")) {
                if (o.getString("resultCode").equals("true")) {
                    if (o.has("resultEntity") && !"[]".equals(o.getString("resultEntity").trim())) {
                        JSONArray array = new JSONArray(o.getString("resultEntity"));
                        List<AlarmGridPercentage> list = new ArrayList<AlarmGridPercentage>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            JSONArray jsonArray = new JSONArray(obj.getString("data"));
                            AlarmGridPercentage alarmGridPercentage = new AlarmGridPercentage();
                            List<DealRate> listDeal = new ArrayList<DealRate>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                alarmGridPercentage.setPoll_source_name(jsonObject.getString("poll_source_name"));
                                DealRate dealRate = new DealRate();
                                dealRate.setRate(jsonObject.getString("percentage"));
                                dealRate.setAlarmName(jsonObject.getString("alarm_type_name"));
                                dealRate.setAlarmCode(jsonObject.getString("alarm_type_code"));
                                listDeal.add(dealRate);
                            }
                            alarmGridPercentage.setDealRates(listDeal);
                            list.add(alarmGridPercentage);
//							System.out.println(alarmGridPercentage.getPoll_source_name());
//							System.out.println(alarmGridPercentage.getDealRates().get(0).getRate());
                        }

                        callBack.onSuccess(list);
                    }else{
                        callBack.onFailed();
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed();
        }
    }
}
