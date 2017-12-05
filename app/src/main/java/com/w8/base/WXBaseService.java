package com.w8.base;

import android.app.Service;
import android.util.Log;

import com.w8.base.MyApp;

/**
 * 公用方法抽取，使用
 */
public abstract class WXBaseService extends Service {
    protected MyApp app;

    public WXBaseService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (app == null) {
            app = (MyApp) getApplication();
        }
    }

    protected void mwErr(String TAG, String where, Exception e) {
        Log.e(TAG, where + ":::::::::::" + e.toString());
    }
}
