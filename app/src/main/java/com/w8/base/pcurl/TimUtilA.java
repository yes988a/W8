package com.w8.base.pcurl;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 所有时间标识。其他工具类不存时间。
 * <p>
 * 所有时间常量和公用方法。
 */
public class TimUtilA {

    public static String week_one = "星期一";
    public static String week_two = "星期二";
    public static String week_three = "星期三";
    public static String week_four = "星期四";
    public static String week_five = "星期五";
    public static String week_six = "星期六";
    public static String week_seven = "星期日";

    //--------------------- 时间格式。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。

    /**
     * 时间转换成星期和年月日
     * @param time  需要转换的时间。
     * @param sysTime 当前系统时间。
     * @return
     */
    public static String formatTime(long time,long sysTime) {

        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(sysTime);
        int yearCurr = date.get(Calendar.YEAR);
        int yeardayCurr = date.get(Calendar.DAY_OF_YEAR);

        // 上面是当前时间。。。

        List<String> weeks = new ArrayList<String>();
        weeks.add(0, week_one);
        weeks.add(1, week_one);
        weeks.add(2, week_two);
        weeks.add(3, week_three);
        weeks.add(4, week_four);
        weeks.add(5, week_five);
        weeks.add(6, week_six);

        //变换成出入进来的时间。
        date.setTimeInMillis(time);
        int year = date.get(Calendar.YEAR);
        int yearday = date.get(Calendar.DAY_OF_YEAR);

        // 对比年份，如果是同一年，查询时间变换成星期几。
        if (year == yearCurr) {
            if (yearday == yeardayCurr) {//如果是今天
                return date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
            } else if (yearday + 1 == yeardayCurr) {//如果是昨天
                return "昨天";
            } else if (yearday + 5 > yeardayCurr) {
                return weeks.get(date.get(Calendar.DAY_OF_WEEK) - 1);
            } else {
                return (1 + date.get(Calendar.MONTH)) + "月" + (date.get(Calendar.DAY_OF_MONTH));
            }
        } else {
            //如果不是同一年，直接显示年月日。
            return (year + "年" + 1 + date.get(Calendar.MONTH)) + "月" + (date.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * 将long时间转换成年月日时分秒
     *
     * @return
     */
    public static String formatTimeToStr(long time) {
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(time);
        int YY = date.get(Calendar.YEAR);
        int MM = date.get(Calendar.MONTH) + 1;
        int DD = date.get(Calendar.DATE);
        int HH = date.get(Calendar.HOUR_OF_DAY);
        int NN = date.get(Calendar.MINUTE);
        int SS = date.get(Calendar.SECOND);
        return YY + "/" + MM + "/" + DD + "  " + HH + ":" + NN + ":" + SS;
    }
    /**
     * new date转换成2016-02-25 11:30:09
     */
    protected String toStr(Date nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(nowTime);
    }

    /**
     * 构造一个最早的时间
     */
    protected long getOlderTim() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1600);
        return c.getTimeInMillis();
    }
}
