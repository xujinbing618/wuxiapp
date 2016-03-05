package com.magus.enviroment.ep.bean;

import java.io.Serializable;

/**
 * 区域率
 * Created by pau on 15/6/18.
 */
public class ZoneDealRate implements Serializable {

    private String rate;
    private String time;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
