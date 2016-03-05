package com.magus.enviroment.ep.bean;

import java.io.Serializable;

/**
 * 处理率
 * Created by pau on 15/6/17.
 */
public class DealRate implements Serializable{

    private String alarmCode;//报警代码
    private String alarmName;//报警名称
    private String dealCount;//处理数量
    private String rate;//处理率
    private String sumCount;//总数量

    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getDealCount() {
        return dealCount;
    }

    public void setDealCount(String dealCount) {
        this.dealCount = dealCount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSumCount() {
        return sumCount;
    }

    public void setSumCount(String sumCount) {
        this.sumCount = sumCount;
    }
}
