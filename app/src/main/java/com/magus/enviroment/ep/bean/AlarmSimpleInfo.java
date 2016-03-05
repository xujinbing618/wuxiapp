package com.magus.enviroment.ep.bean;

import java.io.Serializable;

/**
 * 机组详情
 * Created by pau on 15/6/9.
 */
public class AlarmSimpleInfo implements Serializable {
    private static final String TAG="AlarmSimpleInfo";
    private static final long serialVersionUID = 1L;


    private String alarmLogId="";//报警日志id

    public String getAlarmLogId() {
        return alarmLogId;
    }

    public void setAlarmLogId(String alarmLogId) {
        this.alarmLogId = alarmLogId;
    }
}
