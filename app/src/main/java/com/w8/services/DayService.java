package com.w8.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.w8.base.MyApp;

/**
 * http轮询，是否有新信息。白天晚上都用一个service即可。仅仅更新时间不同。
 */
public class DayService extends Service {

    public int onStartCommand(Intent intent, final int flags, int startId) {
        MyApp.mC.getChat();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
