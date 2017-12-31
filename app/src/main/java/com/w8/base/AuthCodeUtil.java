package com.w8.base;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

/**
 * 获取验证码按钮实现
 */
public class AuthCodeUtil extends CountDownTimer {
    private static int millisInFuture = 60000;
    private static int countDownInterval = 1000;
    private AppCompatActivity mActivity;
    private Button btn;//按钮
    private int widthBtn;

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public AuthCodeUtil(AppCompatActivity mActivity, Button btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
        this.widthBtn = btn.getWidth();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / countDownInterval + "秒后获取");//设置倒计时时间
        btn.setWidth(widthBtn);
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);
    }

    @Override
    public void onFinish() {
        btn.setText("获取验证码");
        btn.setClickable(true);//重新获得点击
    }

}
