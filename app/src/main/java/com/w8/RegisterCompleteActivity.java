package com.w8;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w8.base.AppUtil;
import com.w8.base.AuthCodeUtil;
import com.w8.base.MyApp;
import com.w8.base.RetNumUtil;
import com.w8.base.WxUtil;
import com.w8.base.pcurl.AccountUtil;
import com.w8.base.pcurl.LoginUtil;
import com.w8.base.pcurl.PhoneUtil;
import com.w8.base.pcurl.RegisterUtil;
import com.w8.base.pcurl.MineUtil;
import com.w8.base.pcurl.TestnumUtil;

public class RegisterCompleteActivity extends RegisterTestNum implements View.OnClickListener {

    private TextView register_complete_phone;  // 转送过来的手机号。
    private TextInputEditText register_complete_testnum; //验证码。
    private TextInputEditText register_complete_pass; // 密码。
    private Button register_complete_submit; // 完成注册按钮。

    //-----------         下面变量仅仅RegisterComplete使用          -----------------
    protected String uuidTestNum = "";//验证码返回的UUID。
    protected Button register_complete_getagin;          //获取验证码。

    protected String phone = "";
    protected String acc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);
        TAG = RegisterCompleteActivity.class.getSimpleName();
        initView();
        register_complete_getagin = (Button) findViewById(R.id.register_complete_getagin);
        register_complete_phone = (TextView) findViewById(R.id.register_complete_phone);
        register_complete_testnum = (TextInputEditText) findViewById(R.id.register_complete_testnum);
        register_complete_pass = (TextInputEditText) findViewById(R.id.register_complete_pass);
        register_complete_pass.setText("123456");
        register_complete_submit = (Button) findViewById(R.id.register_complete_submit);
        initOnClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TAG = RegisterCompleteActivity.class.getSimpleName();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            this.finish();
            return;
        } else {
            phone = bundle.getString(PhoneUtil.para_phone);
            acc = bundle.getString(AccountUtil.para_acc);
            uuidTestNum = bundle.getString(TestnumUtil.para_testnum_random);
            delPhone = bundle.getString(PhoneUtil.para_know_del_phone);
            if (delPhone == null || phone == null || "".equals(phone) || acc == null || "".equals(acc)
                    || uuidTestNum == null || "".equals(uuidTestNum)) {
                Intent intent = new Intent(RegisterCompleteActivity.this, RegisterAccPhoneActivity.class);
                startActivity(intent);
                finish();
            } else {
                register_complete_phone.setText(phone);
                AuthCodeUtil authCodeUtil = new AuthCodeUtil(RegisterCompleteActivity.this, register_complete_getagin);
                authCodeUtil.start();
            }
        }
        webing = false;
    }

    //初始化点击事件。
    private void initOnClick() {
        register_complete_getagin.setOnClickListener(this);
        register_complete_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int idd = v.getId();
        if (idd == R.id.register_complete_getagin) {  //获取验证码
            String passssss = register_complete_pass.getText().toString().trim();
            http_testnum(acc, phone, false, passssss);
        } else if (idd == R.id.register_complete_submit) { //完成注册
            String passssss = register_complete_pass.getText().toString().trim();
            complete_submit(passssss);
        }
    }

    // 完成注册。
    private void complete_submit(final String passssss) {
        if (testAccPhone(acc, phone)) {
        } else if (passssss != null && !testPass(passssss)) {
            alertDialogText(getString(R.string.password_error));
            return;
        } else {
            boolean bb = testAccPhone(acc, phone);
            if (bb || webing) {
                return;
            } else {
                JsonObject jo = new JsonObject();
                String testNnnnnn = md5Str(uuidTestNum + register_complete_testnum.getText().toString().trim());
                jo.addProperty(TestnumUtil.para_testnum, "" + testNnnnnn);
                jo.addProperty(PhoneUtil.para_phone, "" + phone);
                jo.addProperty(AccountUtil.para_acc, "" + acc);
                jo.addProperty(MineUtil.para_pas, "" + passssss);
                jo.addProperty(WxUtil.para_url, RegisterUtil.url_app_complete);

                startWeb();

                StringRequest request = new StringRequest(
                        getString(R.string.httpHomeAddress) + jo.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JsonObject into = new JsonParser().parse(s).getAsJsonObject();
                            int rrr = into.get(WxUtil.para_r).getAsInt();
                            if (rrr == RetNumUtil.n_0) {
                                String iipp = into.get(LoginUtil.para_login_ip).getAsString();
                                AppUtil.insertAccIp(acc, iipp);
                                Intent iii = new Intent(RegisterCompleteActivity.this, LoginActivity.class);
                                startActivity(iii);
                                finish();
                            } else if (rrr == RetNumUtil.n_7) {//手机号
                                retAccPhone(acc, phone);
                            } else if (rrr == RetNumUtil.n_4) {//账号
                                retAccPhone(acc, phone);
                            } else if (rrr == RetNumUtil.n_26) {//密码
                                alertDialogText(getString(R.string.password_error));
                                endWeb();
                            } else if (rrr == RetNumUtil.n_5) {//账号已经被注册
                                //一、尝试登陆，是否已经注册完成。
                                //二、如果不是已经成功：返回RegsterAccPhone，并提示错误。
                                ttErr = RetNumUtil.n_5;
                                testAndLogin(acc, passssss);
                            } else if (rrr == RetNumUtil.n_6) {//验证码手机号匹配已过期。
                                ttErr = RetNumUtil.n_6;
                                testAndLogin(acc, passssss);
                            } else if (rrr == RetNumUtil.n_17) {//验证码操作太过频繁
                                alertDialogText(getString(R.string.err_test_register));
                                endWeb();
                            } else if (rrr == RetNumUtil.n_18) {//手机号已经注册，是否解除注册？
                                //一、尝试登陆，是否已经注册完成。
                                //二、如果不是已经成功：返回RegsterAccPhone，并提示错误。
                                ttErr = RetNumUtil.n_18;
                                testAndLogin(acc, passssss);
                                delPhone = WxUtil.para_yes + phone;
                            } else {//未知错误。
                                //日志
                                alertDialogText(getString(R.string.server_err));
                                endWeb();
                            }
                        } catch (Exception e) {
                            //日志
                            alertDialogText(getString(R.string.server_err));
                            endWeb();
                        }
                        Log.e(TAG, s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        endWeb();
                        alertDialogText(getString(R.string.reg_web_err));
                    }
                });
                MyApp.mC.getVQ().add(request);
            }
        }
    }

    @Override
    public void retCompleteTestnum() {
        webing = false;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void complete_getagin_ing(String uuidTestNum) {
        this.uuidTestNum = uuidTestNum;
        AuthCodeUtil authCodeUtil = new AuthCodeUtil(RegisterCompleteActivity.this, register_complete_getagin);
        authCodeUtil.start();
    }

    @Override
    public void accErr() {
        accPss();
    }

    @Override
    public void startWeb() {
        webing = true;
        progressBar.setVisibility(View.VISIBLE);
        register_complete_submit.setEnabled(false);
    }

    @Override
    public void endWeb() {
        webing = false;
        progressBar.setVisibility(View.GONE);
        register_complete_submit.setEnabled(true);
    }

    @Override
    public void accPss() { //尝试登陆后返回，账号密码不匹配。
        webing = false;
        progressBar.setVisibility(View.GONE);
        if (ttErr == RetNumUtil.n_5) {
            if (TAG.equals(RegisterCompleteActivity.class.getSimpleName())) {
                retAccPhone(acc, phone);
            }
            alertDialogText(getString(R.string.reg_two_username));
        } else if (ttErr == RetNumUtil.n_18) {
            delPhone = WxUtil.para_yes + phone;
            alertDialogText(getString(R.string.reg_del_phone));
        } else if (ttErr == RetNumUtil.n_6) {
            alertDialogText(getString(R.string.reg_phonetestnumerr));
        }
    }
}
