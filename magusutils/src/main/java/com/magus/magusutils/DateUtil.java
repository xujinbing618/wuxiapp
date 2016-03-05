package com.magus.magusutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期管理
 * Created by pau on 15/6/17.
 */
public class DateUtil {


    /**
     * 获得当前日期
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDay() {// 可以用new
        String currentDay;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDay = sdf.format(date);
        return currentDay;
    }
    /**
     * 获得当前时刻
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentSecond() {// 可以用new
        String currentDay;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        currentDay = sdf.format(date);
        return currentDay;
    }
    /**
     * 获得当前日期年-月
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentMon() {
        String currentDay;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        currentDay = sdf.format(date);
        return currentDay;
    }

    /**
     * 获取当前日期的前一个月
     * @return
     */
    public static String getBeforeMon(){
        String currentDay;
        Date date = new Date();
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        currentDay = sdf.format(ca.getTime());


        return currentDay;
    }
    /**
     * 获得指定日期的前一个月
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedMonBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int mon = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, mon - 1);

        String monBefore = new SimpleDateFormat("yyyy-MM").format(c
                .getTime());
        return monBefore;
    }

    /**
     * 获得指定日期的后一个月
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedMonAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int mon = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, mon + 1);

        String monAfter = new SimpleDateFormat("yyyy-MM")
                .format(c.getTime());
        return monAfter;
    }
    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }

    /**
     * 获得一个月最后一天的日
     * @return
     */
    public static int getLastDayOfMonth() {
        Date sDate1 = new Date();
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(sDate1);
        final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Date lastDate = cDay1.getTime();
//        lastDate.setDate(lastDay);
        return lastDay;
    }



    /**
     * 某一个月第一天和最后一天
     *
     * @param date
     * @return
     */
    private static Map<String, String> getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date theDate = calendar.getTime();

        //上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }

    /**
     * 当月第一天
     *
     * @return
     */
    private static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }

    /**
     * 当月最后一天
     *
     * @return
     */
    private static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        return s.toString();

    }


    /**
     * 获得上一个月
     * @return
     */
    public static String getLastMonth(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);

        return new SimpleDateFormat("yyyy年MM月").format(c.getTime());

    }
}
