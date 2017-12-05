package com.w8.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.NologinActivity;
import com.w8.base.OnlineActivity;
import com.w8.base.RetNumUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 总控制器。
 * <p>
 * app被后台关闭后，闹钟彻底停止
 * 用户退出登录后，闹钟彻底停止
 * <p>
 * app从后台销毁状态变成打开状态，在MyApp中的onCreate方法中被打开
 * 用户登录后，在登录成功时被打开。
 */
public class GuardService extends Service {

    public static String TAG = GuardService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 所有状态：
        // 一、未登录，tag为"NologinActivity.class"，不管打开还是关闭者activity。。。
        // 二、登录状态，并打开着activity，Tag有值为simpleName。。。
        // 三、登录状态，关闭activity后，tag为"OnlineActivity.class"。。。
        // 不应该有"" 的情况发生。如果有，也视为未登录吧。
        String tt = AppUtil.getTag();
        if (NologinActivity.class.getSimpleName().equals(tt)) {
            // 未登录，不应该有闹钟，应该判断到底是否登录。
        } else if (OnlineActivity.class.getSimpleName().equals(tt)) {
            if (MyApp.webSocket == null) {
                //登录，但是关闭着activity页面。
                long tim = AppUtil.getTimeReal();
                Calendar date = new GregorianCalendar();
                date.setTimeInMillis(tim);
                int hh = date.get(Calendar.HOUR_OF_DAY);
                if (RetNumUtil.n_1 < hh && hh < RetNumUtil.n_6) {//晚上,早1点到6点
                    AppUtil.startAlarmNight();
                } else {//白天
                    AppUtil.startAlarmDay();
                }
            }
        } else {
            //登录状态，并开着UI页面。
            if (MyApp.webSocket == null) {
                MyApp.mC.newWS();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
