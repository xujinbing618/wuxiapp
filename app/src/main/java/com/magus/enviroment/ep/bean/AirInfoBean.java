package com.magus.enviroment.ep.bean;

import com.magus.enviroment.ep.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 空气信息实体类
 * Created by pau on 15/5/25.
 */
public class AirInfoBean {
    private String aqi;
    private String cityCode; //城市编码
    private String area; //城市名称
    private String co;
    private String id;
    private String no2;
    private String o3;
    private String pm10;
    private String pm25;
    private String primaryPollutant; //首要污染物
    private String quality; //等级
    private String siteCode; //站点编码

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    private String position_name; //站点名字
    private String so2;
    private String time; //时间
    private String dateTime;
    private String hourTime;
    private String advice;//建议


    public AirInfoBean() {
    }

    public AirInfoBean(String aqi, String cityCode, String area, String co, String id, String no2, String o3, String pm10, String pm25, String primaryPollutant, String quality, String siteCode, String position_name, String so2, String time, String dateTime, String hourTime, String advice) {
        this.aqi = aqi;
        this.cityCode = cityCode;
        this.area = area;
        this.co = co;
        this.id = id;
        this.no2 = no2;
        this.o3 = o3;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.primaryPollutant = primaryPollutant;
        this.quality = quality;
        this.siteCode = siteCode;
        this.position_name = position_name;
        this.so2 = so2;
        this.time = time;
        this.dateTime = dateTime;
        this.hourTime = hourTime;
        this.advice = advice;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPrimaryPollutant() {
        return primaryPollutant;
    }

    public void setPrimaryPollutant(String primaryPollutant) {
        this.primaryPollutant = primaryPollutant;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }


    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getHourTime() {
        return hourTime;
    }

    public void setHourTime(String hourTime) {
        this.hourTime = hourTime;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public static void parseAirInfo(JSONObject object, RequestCallBack callBack) {
        List<AirInfoBean> list = new ArrayList<AirInfoBean>();
        try {
            if (object.has("resultCode")) {  //先判断是否有这个值
                if (object.getString("resultCode").equals("true")) {
                    if (object.has("resultEntity") && !object.getString("resultEntity").trim().equals("[]")) {
                        JSONArray array = new JSONArray(object.getString("resultEntity"));
                        AirInfoBean infoBean0 = new AirInfoBean();
                        for (int i = 0; i < array.length(); i++){
                            AirInfoBean infoBean = new AirInfoBean();
                            JSONObject o = (JSONObject) array.get(i);
                            if (o.has("aqi")) {
                                infoBean.setAqi(o.getString("aqi").trim());
                            }
                            if (o.has("area")) {
                                infoBean.setArea(o.getString("area").trim());
                            }
                            if (o.has("cityCode")) {
                                infoBean.setCityCode(o.getString("cityCode").trim());
                            }
                            if (o.has("co")) {
                                infoBean.setCo(o.getString("co").trim());
                            }
                            if (o.has("no2")) {
                                infoBean.setNo2(o.getString("no2").trim());
                            }
                            if (o.has("o3")) {
                                infoBean.setO3(o.getString("o3").trim());
                            }
                            if (o.has("pm10")) {
                                infoBean.setPm10(o.getString("pm10").trim());
                            }
                            if (o.has("pm2_5")) {
                                infoBean.setPm25(o.getString("pm2_5").trim());
                            }
                            if (o.has("primaryPollutant")) {
                                infoBean.setPrimaryPollutant(o.getString("primaryPollutant").trim());
                            }
                            if (o.has("quality")) {
                                infoBean.setQuality(o.getString("quality").trim());
                            }
                            if (o.has("siteCode")) {
                                infoBean.setSiteCode(o.getString("siteCode").trim());
                            }
                            if (o.has("position_name")) {
                                infoBean.setPosition_name(o.getString("position_name").trim());
                            }
                            if (o.has("so2")) {
                                infoBean.setSo2(o.getString("so2").trim());
                            }
                            if (o.has("time")) {
                                infoBean.setTime(o.getString("time").trim());
                            }
                            if (o.has("dayTime")) {
                                infoBean.setDateTime(o.getString("dayTime").trim());
                            }
                            if (o.has("time_point")) {
                                infoBean.setHourTime(o.getString("time_point").trim());
                            }
                            int aqiIndex = Integer.parseInt(o.getString("aqi").trim());
                            if (aqiIndex > 0 && aqiIndex <= 50) {
                                infoBean.setAdvice("各类人群可正常活动");
                            } else if (aqiIndex > 50 && aqiIndex <= 100) {
                                infoBean.setAdvice("极少数异常敏感人群应减少户外活动");
                            } else if (aqiIndex > 100 && aqiIndex <= 150) {
                                infoBean.setAdvice("儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼");
                            } else if (aqiIndex > 150 && aqiIndex <= 200) {
                                infoBean.setAdvice("疾病患者避免长时间、高强度的户外锻练，一般人群适量减少户外运动");
                            } else if (aqiIndex > 200 && aqiIndex <= 300) {
                                infoBean.setAdvice("儿童、老年人和心脏病、肺病患者应停留在室内，停止户外运动，一般人群减少户外运动");
                            } else {
                                infoBean.setAdvice("儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动");
                            }
                            if (!"null".equals(infoBean.getPosition_name())){
                                list.add(infoBean);
                            } else {
                                infoBean0 = infoBean;
                            }
                        }
                        list.add(0, infoBean0);
                        callBack.onSuccess(list);//成功
                    } else {
                        callBack.onFailed();
                    }

                } else {//请求码为false

                    callBack.onFailed();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onFailed();
        }

    }

}
