package com.w8;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w8.base.MyApp;
import com.w8.base.RetNumUtil;
import com.w8.base.WxUtil;
import com.w8.base.pcurl.AccountUtil;
import com.w8.base.pcurl.PhoneUtil;
import com.w8.base.pcurl.RegisterUtil;
import com.w8.base.pcurl.TestnumUtil;

/**
 * 注册，验证码公用。  正好有其他可以公用的。顺便公用吧。如：马上登陆。
 */
public abstract class RegisterTestNum extends LoginTestAcc {

    public abstract void retCompleteTestnum(); //完成验证码请求。

    public abstract void complete_getagin_ing(String uuidTestNum);//获取验证码标记为倒计时

    protected Toolbar toolbar;
    protected TextView reg_login; //已有账号，马上登陆。
    protected String delPhone = "";//是否解绑其他账号。

    protected ProgressBar progressBar; //正在请求，小圆圈。

    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        delPhone = "";
        progressBar = (ProgressBar) findViewById(R.id.reg_progressBar);
        reg_login = (TextView) findViewById(R.id.reg_login);
        reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//已有账号，马上登陆
                Intent intent = new Intent(RegisterTestNum.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //验证用户名，手机号的合法性
    protected boolean testAccPhone(String acc, String phone) {
        if (!PhoneUtil.testPhone(phone)) {
            errPhone(acc, phone);
            return true;
        } else if (!AccountUtil.testAcc(acc)) {
            errAcc(acc, phone);
            return true;
        } else {
            return false;
        }
    }

    public int ttErr = 0;

    /**
     * 验证账号和手机号是否被注册，并，获取验证码。
     *
     * @param acc
     * @param phone
     * @param comp     仅仅标记，是哪个Activity调用的。
     * @param passtext 可以为null和""。
     */
    protected void http_testnum(final String acc, final String phone, final boolean comp, final String passtext) {
        boolean bb = testAccPhone(acc, phone);
        if (bb || webing) {
            return;
        } else {
            JsonObject jo = new JsonObject();
            jo.addProperty(PhoneUtil.para_know_del_phone, "" + delPhone);
            jo.addProperty(AccountUtil.para_acc, "" + acc);
            jo.addProperty(PhoneUtil.para_phone, "" + phone);
            jo.addProperty(WxUtil.para_url, RegisterUtil.url_app_testnum);
//                String mi = WxUtil.aesEncrypt(AppUtil.getSafeAes_nolog(), jo.toString());
            progressBar.setVisibility(View.VISIBLE);
            webing = true;
            StringRequest request = new StringRequest(
                    getString(R.string.httpHomeAddress) + jo.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        retCompleteTestnum();
                        JsonObject into = new JsonParser().parse(s).getAsJsonObject();
                        int rrr = into.get(WxUtil.para_r).getAsInt();
                        if (rrr == RetNumUtil.n_0) {
                            String uuidTestNum = into.get(TestnumUtil.para_testnum_random).getAsString();
                            if (comp) {
                                Intent iii = new Intent(RegisterTestNum.this, RegisterCompleteActivity.class);
                                iii.putExtra(PhoneUtil.para_know_del_phone, delPhone);
                                iii.putExtra(PhoneUtil.para_phone, phone);
                                iii.putExtra(AccountUtil.para_acc, acc);
                                iii.putExtra(TestnumUtil.para_testnum_random, uuidTestNum);
                                startActivity(iii);
                                finish();
                            } else {
                                complete_getagin_ing(uuidTestNum);
                            }
                        } else if (rrr == RetNumUtil.n_7) {//手机号不正确
                            errPhone(acc, phone);
                        } else if (rrr == RetNumUtil.n_4) {//账号格式不正确
                            errAcc(acc, phone);
                        } else if (rrr == RetNumUtil.n_5) {//账号已经被注册
                            if (comp) {
                                alertDialogText(getString(R.string.reg_two_username));
                            } else {
                                //一、尝试登陆，是否已经注册完成。
                                //二、如果不是已经成功：返回RegsterAccPhone，并提示错误。
                                ttErr = RetNumUtil.n_5;
                                testAndLogin(acc, passtext);
                            }
                        } else if (rrr == RetNumUtil.n_17) {//验证码操作太过频繁
                            alertDialogText(getString(R.string.err_test_register));
                        } else if (rrr == RetNumUtil.n_18) {//手机号已经注册，是否解除注册？
                            if (comp) {
                                alertDialogText(getString(R.string.reg_del_phone));
                                delPhone = WxUtil.para_yes + phone;
                            } else {
                                //一、尝试登陆，是否已经注册完成。
                                //二、如果不是已经成功：返回RegsterAccPhone，并提示错误。
                                ttErr = RetNumUtil.n_18;
                                testAndLogin(acc, passtext);
                            }
                        } else {//未知错误。
                            alertDialogText(getString(R.string.server_err));
                        }
                    } catch (Exception e) {
                        alertDialogText(getString(R.string.server_err));
                    }
                    Log.e(TAG, s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    retCompleteTestnum();
                    alertDialogText(getString(R.string.fail_web));
                }
            });
            MyApp.mC.getVQ().add(request);
        }
    }

    protected void errAcc(String acc, String phone) {
        if (TAG.equals(RegisterAccPhoneActivity.class.getSimpleName())) {
            alertDialogText(getString(R.string.acc_error));
        } else if (TAG.equals(RegisterCompleteActivity.class.getSimpleName())) {
            retAccPhone(acc, phone);
        }
    }

    protected void errPhone(String acc, String phone) {
        if (TAG.equals(RegisterAccPhoneActivity.class.getSimpleName())) {
            alertDialogText(getString(R.string.err_phone));
        } else if (TAG.equals(RegisterCompleteActivity.class.getSimpleName())) {
            retAccPhone(acc, phone);
        }
    }

    //带参数返回到RegisterAccPhone。
    protected void retAccPhone(String acc, String phone) {
        Intent iii = new Intent(RegisterTestNum.this, RegisterAccPhoneActivity.class);
        iii.putExtra(PhoneUtil.para_phone, phone);
        iii.putExtra(AccountUtil.para_acc, acc);
        startActivity(iii);
        finish();
    }
}
