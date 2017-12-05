package com.w8;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.w8.base.NologinActivity;
import com.w8.base.RetNumUtil;
import com.w8.base.pcurl.PhoneUtil;

//解除手机绑定。
//可能发生在多个地方。登录前，登录后，网页上等。
public class RemovephoneActivity extends NologinActivity implements View.OnClickListener {

    private String phone;//电话号码

    private Toolbar toolbar;
    private ProgressBar progressBar_removephone;
    private TextView removephonenum;
    private TextInputEditText remove_test;
    private Button getagin;
    private Button removephone_butt;
    private boolean webing;//正在访问网络，其它按钮无效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removephone);
        TAG = RemovephoneActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        webing = false;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null || bundle.getString(PhoneUtil.para_phone) == null) {
            this.finish();
        } else {
            phone = bundle.getString(PhoneUtil.para_phone);
            if (PhoneUtil.testPhone(phone)) {
                removephonenum.setText(phone);
            } else {
                this.finish();
            }
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        progressBar_removephone = (ProgressBar) findViewById(R.id.progressBar_removephone);
        removephonenum = (TextView) findViewById(R.id.removephonenum);
        remove_test = (TextInputEditText) findViewById(R.id.remove_test);
        getagin = (Button) findViewById(R.id.getagin);
        removephone_butt = (Button) findViewById(R.id.removephone_butt);
    }

    private void initListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemovephoneActivity.this.finish();
            }
        });
        getagin.setOnClickListener(this);//获取验证码
        removephone_butt.setOnClickListener(this);//提交解除绑定
    }

    @Override
    public void onClick(View v) {
        int idd = v.getId();
        if (idd == R.id.getagin) {
            if (!webing) {
                getVerification();
            }
        } else if (idd == R.id.removephone_butt) {
            if (!webing) {
                if (remove_test.getText().toString().length() > RetNumUtil.n_0) {
                    removePhone();
                } else {
                    setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_testnum_err));
                }
            }
        }
    }

    /**
     * 发送手机号获取验证码
     */
    private void getVerification() {

        setErrorMy(remove_test, null);
        /*StringRequest sr = new StringRequest(Request.Method.POST, getString(R.string.urlBase) + WxUtil.u_regis_veri, new Response.Listener<String>() {
            @Override
            public void onResponse(String ss) {
                progressBar_removephone.setVisibility(View.GONE);
                JSONObject jw = null;
                Integer rr = null;
                try {
                    jw = JSON.parseObject(ss);
                    rr = jw.getInteger(WxUtil.r);
                    tim = jw.getLong(WxUtil.tim);
                } catch (Exception e) {
                    mwErr(TAG, "RemovephoneActivity.getVerification.R.string.urlBase.onResponse", e);
                }
                if (jw == null || tim == null || rr == null || RetNumUtil.n_0 != rr) {
                    // 日志
                    setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_gettesterr));
                } else {
                    AuthCodeUtil authCodeUtil = new AuthCodeUtil(RemovephoneActivity.this, getagin);
                    authCodeUtil.start();
                    Toast.makeText(RemovephoneActivity.this, R.string.reg_testnum_send, Toast.LENGTH_LONG);
                }
                getagin.setClickable(true);
                webing = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_removephone.setVisibility(View.GONE);
                setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_gettesterr));
                getagin.setClickable(true);
                webing = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject jo = new JSONObject();
                jo.put(WxUtil.phone, phone);
                String mi = AESUtils.encrypt(AppUtil.getSafeAes_nolog(RemovephoneActivity.this), jo.toString());
                Map<String, String> mm = new HashMap<String, String>();
                mm.put(WxUtil.w, mi);
                return mm;
            }
        };
        RequestQueue ve = app.getVolleyQeue();
        sr.setRetryPolicy(new DefaultRetryPolicy(RetNumUtil.n_26 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ve.add(sr);*/

        webing = true;
        getagin.setClickable(false);
        progressBar_removephone.setVisibility(View.VISIBLE);
    }


    private void removePhone() {

        /*StringRequest sr = new StringRequest(Request.Method.POST, getString(R.string.urlBase) + WxUtil.u_removephone, new Response.Listener<String>() {
            @Override
            public void onResponse(String ss) {
                progressBar_removephone.setVisibility(View.GONE);
                JSONObject jw = null;
                Integer rr = null;
                try {
                    jw = JSON.parseObject(ss);
                    rr = jw.getInteger(WxUtil.r);
                } catch (Exception e) {
                    mwErr(TAG, "RemovephoneActivity.getVerification.R.string.urlBase.onResponse", e);
                }

                if (rr == null || rr == null) {
                    //日志

                } else {

                    if (RetNumUtil.n_7 == rr) {
                        //日志
                        setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.err_phone));
                        RemovephoneActivity.this.finish();
                    } else if (RetNumUtil.n_6 == rr) {
                        setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_phonetestnumerr));
                    } else if (RetNumUtil.n_20 == rr) {
                        setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_phonetesttimeout));
                    } else if (RetNumUtil.n_0 == rr) {
                        new AlertDialog.Builder(RemovephoneActivity.this)
                                .setTitle(R.string.removephonebutton)
                                .setMessage(R.string.success)
                                .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RemovephoneActivity.this.finish();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_web_err));
                    }
                }


                if (rr != null && RetNumUtil.n_0 == rr) {
                    Toast.makeText(RemovephoneActivity.this, R.string.success, Toast.LENGTH_LONG);

                    RemovephoneActivity.this.finish();
                } else {
                    Toast.makeText(RemovephoneActivity.this, R.string.req_met_err, Toast.LENGTH_LONG);
                }
                getagin.setClickable(true);
                removephone_butt.setClickable(true);
                webing = false;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_removephone.setVisibility(View.GONE);
                setErrorMy(remove_test, RemovephoneActivity.this.getString(R.string.reg_web_err));
                getagin.setClickable(true);
                removephone_butt.setClickable(true);
                webing = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject jo = new JSONObject();
                jo.put(WxUtil.phone, phone);
                jo.put(WxUtil.tim, tim);
                jo.put(WxUtil.testnum, remove_test.getText().toString());
                String mi = AESUtils.encrypt(AppUtil.getSafeAes_nolog(RemovephoneActivity.this), jo.toString());
                Map<String, String> mm = new HashMap<String, String>();
                mm.put(WxUtil.w, mi);
                return mm;
            }
        };
        RequestQueue ve = app.getVolleyQeue();
        sr.setRetryPolicy(new DefaultRetryPolicy(RetNumUtil.n_28 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        ve.add(sr);*/

        webing = true;
        getagin.setClickable(false);
        removephone_butt.setClickable(false);
        progressBar_removephone.setVisibility(View.VISIBLE);
    }
}
