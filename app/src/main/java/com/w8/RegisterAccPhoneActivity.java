package com.w8;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.w8.base.pcurl.AccountUtil;
import com.w8.base.pcurl.PhoneUtil;

/**
 * 手机号，账号，填写。。。
 */
public class RegisterAccPhoneActivity extends RegisterTestNum implements View.OnClickListener {

    private TextInputEditText reg_one_phone; //手机号。
    private TextInputEditText reg_one_acc; //用户名。
    private Button reg_one_submit; //下一步（完成账号验证和发送验证码。），

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acc_phone);
        TAG = RegisterAccPhoneActivity.class.getSimpleName();
        initView();
        reg_one_phone = (TextInputEditText) findViewById(R.id.reg_one_phone);
        reg_one_phone.setText("1734651314");
        reg_one_acc = (TextInputEditText) findViewById(R.id.reg_one_acc);
        reg_one_acc.setText("yes988a");
        reg_one_submit = (Button) findViewById(R.id.reg_one_submit);
        initOnClick();
    }

    //初始化点击事件。
    private void initOnClick() {
        reg_one_submit.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TAG = RegisterAccPhoneActivity.class.getSimpleName();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            return;
        } else {
            String phone = bundle.getString(PhoneUtil.para_phone);
            String acc = bundle.getString(AccountUtil.para_acc);
            if (PhoneUtil.testPhone(phone)) {
                reg_one_phone.setText(phone);
            }
            if (AccountUtil.testAcc(acc)) {
                reg_one_acc.setText(acc);
            }
        }
        webing = false;
    }

    @Override
    public void onClick(View v) {
        int idd = v.getId();
        if (idd == R.id.reg_one_submit) {
            String phone = reg_one_phone.getText().toString().trim();
            String acc = reg_one_acc.getText().toString().trim();
            //http请求
            http_testnum(acc, phone, true, "");
        }
    }

    @Override
    public void retCompleteTestnum() {
        webing = false;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void complete_getagin_ing(String uuidTestNum) {
    }

    @Override
    public void accPss() { //尝试登陆，账号密码不匹配。
        // 获取验证码第一步，不做任何操作。
    }

    @Override
    public void accErr() {
        accPss();
    }

    @Override
    public void startWeb() {
        webing = true;
        progressBar.setVisibility(View.VISIBLE);
        reg_one_submit.setEnabled(false);
    }

    @Override
    public void endWeb() {
        webing = false;
        progressBar.setVisibility(View.GONE);
        reg_one_submit.setEnabled(true);
    }
}
