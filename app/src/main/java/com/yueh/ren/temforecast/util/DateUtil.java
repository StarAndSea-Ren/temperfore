package com.yueh.ren.temforecast.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chge on 2016/5/11.
 */
public class DateUtil {

    private static final String DATE_FORMAT_HOUR = "HH";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH/mm/ss";

    /**
     * 毫秒转换为小时
     * @param mills
     * @return
     */
    public static Double millis2Hour(Long mills){
        DateFormat format = new SimpleDateFormat(DATE_FORMAT_HOUR);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return Double.valueOf(format.format(calendar.getTime()));
    }

    /**
     * date转string
     * @param date
     * @return
     */
    public static String date2String(Date date){
        if (date == null){
            return null;
        }
        String dateStr = "";
        java.text.DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            dateStr = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * string转date
     * @param dateStr
     * @return
     */
    public static Date string2Date(String dateStr){
        if (dateStr == null){
            return null;
        }
        SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
        Date date= null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * X小时之后
     */
    public static String timeLater(String dateStr,Integer gapHour){
        if (dateStr == null || gapHour == null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(string2Date(dateStr));
        cal.add(Calendar.HOUR,gapHour);
        String laterDateStr = date2String(cal.getTime());
        return laterDateStr;
    }
}
