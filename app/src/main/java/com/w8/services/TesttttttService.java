package com.w8.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.w8.base.pcurl.TimUtilA;

/**
 * 测试稳定性。  会被管理程序杀死。
 */
public class TesttttttService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("--------ffffffffff-", TimUtilA.formatTimeToStr(System.currentTimeMillis()));
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
