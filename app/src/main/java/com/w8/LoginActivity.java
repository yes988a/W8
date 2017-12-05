package com.w8;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 登录页面
 */
public class LoginActivity extends LoginTestAcc {
    private TextInputEditText login_acc;
    private TextInputEditText login_pass;
    private Button login_submit;
    private TextView login_reg;
    private TextView login_forget;

    private ProgressBar progressBar;
    private boolean webing; //是否正在登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TAG = LoginActivity.class.getSimpleName();
        login_acc = (TextInputEditText) findViewById(R.id.login_acc);
        login_pass = (TextInputEditText) findViewById(R.id.login_pass);
        login_submit = (Button) findViewById(R.id.login_submit);
        progressBar = (ProgressBar) findViewById(R.id.login_bar);
        login_reg = (TextView) findViewById(R.id.login_reg);
        login_forget = (TextView) findViewById(R.id.login_forget);
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        webing = false;
    }

    /**
     * 初始化按钮事件。。。
     */
    private void initListener() {
        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!webing) {
                    testAndLogin(login_acc.getText().toString(), login_pass.getText().toString());
                }
            }
        });
        login_acc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    login_pass.setFocusable(true);
                    login_pass.setFocusableInTouchMode(true);
                    login_pass.requestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });
        login_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    testAndLogin(login_acc.getText().toString(), login_pass.getText().toString());
                    return true;
                } else {
                    return false;
                }
            }
        });
        login_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        login_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterAccPhoneActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void accPss() { // 账号密码不匹配
        alertDialogText(getString(R.string.login_usern_pass_err));
    }

    @Override
    public void accErr() { // 账号，不存在
        alertDialogText(getString(R.string.login_usern_null));
    }

    @Override
    public void startWeb() {
        webing = true;
        progressBar.setVisibility(View.VISIBLE);
        login_submit.setText(LoginActivity.this.getString(R.string.login_loading));
        login_submit.setEnabled(false);
    }

    @Override
    public void endWeb() {
        webing = false;
        progressBar.setVisibility(View.GONE);
        login_submit.setText(LoginActivity.this.getString(R.string.title_activity_login));
        login_submit.setEnabled(true);
    }
}