package com.company.project.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyyMMdd)
     */
    public final static String DATE_PATTERN = "yyyyMMdd";

    /**
     * 通过字符串转成日期
     * @param data
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String data, String pattern)  {
        Date date = null;
        if(data != null && data != ""){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                date = df.parse(data);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 格式化日期，默认是以yyyy-MM-dd HH:mm:ss格式
     * @param date
     * @return
     */
    public static String format(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            return df.format(date);
        }
        return null;
    }


    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }

    /**
     * 获取某日期的前一天
     * @param date
     * @return
     * @throws ParseException
     */
    public static String subDay(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String reStr = "";
        try {
            Date dt = sdf.parse(date);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DAY_OF_MONTH, -1);
            Date dt1 = rightNow.getTime();
            reStr = sdf.format(dt1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return reStr;
    }


    /**
     * 获取相对与当前时间的第dayNum天的日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDay(Date date, Integer dayNum) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_MONTH, dayNum);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

}
