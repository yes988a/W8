package com.w8.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.w8.LoginActivity;
import com.w8.R;
import com.w8.base.event.Common_close_all;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 常用工具类封装？
 */
public class WxActivity extends AppCompatActivity {

    protected boolean webing = false;//正在访问网络，其它按钮无效。

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//不可以多次注册。。。。。子类中@Subscribe也可以被扫描到。
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//可以多次解除注册
    }

    // 弹框提示。仅仅通知（此方法后面不可以直接跟finish()）
    protected void alertDialogText(String msg) {
        TextView edi = new TextView(WxActivity.this);//textAppearanceMedium
        edi.setGravity(Gravity.CENTER);
//        edi.setTextAppearance(R.attr.textAppearanceLargePopupMenu);//R.attr.textAppearanceLargePopupMenu
        edi.setText(msg);
        new AlertDialog.Builder(WxActivity.this)
                .setTitle(R.string.tittle_ok)
                .setView(edi)
                .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //可以多个方法。方法名字没有什么意思。
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void c(Common_close_all all) {
        //清空所有登录信息，(不需要做，因为所有到非登录页面都会清空所有登录信息。)
        //跳转到登录页面。
        startAct(this, LoginActivity.class);
    }

    protected void mwErr(String TAG, String where, Exception e) {
        Log.e(TAG, where + ":::::::::::" + e.toString());
    }

    /**
     * 跳转到activity，当前旧activity.finish
     */
    protected void startAct(WxActivity finshAct, Class<? extends WxActivity> t) {
        if (t != null) {
            Intent intent = new Intent(this, t);
            if (finshAct != null) {
                finshAct.startActivity(intent);
            } else {
                this.startActivity(intent);
            }
        }
        if (finshAct != null) {
            finshAct.finish();
        }
    }

    /**
     * 使TextView获取焦点，解决setError时提示内容不显示问题
     */
    protected void setErrorMy(TextView view, String s) {
        view.setError(s);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * 字符串转md5
     */
    protected String md5Str(String s) {
        if (s == null || "".equals(s)) {
            return "";
        } else {
            return md5(s);
        }
    }

    private String md5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}