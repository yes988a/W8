package com.w8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.pcurl.MineUtilA;
import com.w8.base.pcurl.RetNumUtilA;
import com.w8.base.pcurl.TimUtilA;
import com.w8.base.data.Active;
import com.w8.base.data.ActiveDao;
import com.w8.base.data.FriendDao;
import com.w8.base.data.Frireq;
import com.w8.base.data.FrireqDao;
import com.w8.base.pcurl.ChatUtilA;
import com.w8.base.pcurl.FriendUtilA;

import java.util.List;

/**
 * 回复是否添加好友
 */
public class FriResActivity extends OnlineActivity {


    // 请求页面（搜到：昵称和签名）
    // 被请求页面：（账号，昵称，请求描述，是否认识）

    private TextView fri_res_acc;       //账号
    private TextView fri_res_nickname;  // 昵称
    private TextView fri_res_tim;  // 昵称
    private EditText fri_res_des;//请求描述

    private RadioGroup fri_res_radioGroup;

    private Button fri_res_agress;//同意
    private Button fri_res_ignore;//忽略

    private String reqid;//请求者id
    private Long tim;//时间

//    private String from; //从哪里过来？ 一、扫一扫。二、账号搜索。三、手机搜索。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri_res);
        TAG = FriReqActivity.class.getSimpleName();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fri_res_acc = (TextView) findViewById(R.id.fri_res_acc);
        fri_res_nickname = (TextView) findViewById(R.id.fri_res_nickname);
        fri_res_tim = (TextView) findViewById(R.id.fri_res_tim);
        fri_res_des = (EditText) findViewById(R.id.fri_res_des);
        fri_res_radioGroup = (RadioGroup) findViewById(R.id.fri_res_radioGroup);
        fri_res_agress = (Button) findViewById(R.id.fri_res_agress);
        fri_res_ignore = (Button) findViewById(R.id.fri_res_ignore);
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            finish();
        } else {
            reqid = bundle.getString(FriendUtilA.para_reqid);//请求者id。
            if (reqid == null) {
                finish();
            } else {
                //不需要删除或者修改Frireq。
                ActiveDao activeDao = MyApp.mC.getDS().getActiveDao();
                List<Active> actives = activeDao.queryBuilder().where(ActiveDao.Properties.Uuid.eq(reqid)).list();
                activeDao.deleteInTx(actives);

                FrireqDao frireqDao = MyApp.mC.getDS().getFrireqDao();
                List<Frireq> list = frireqDao.queryBuilder().where(FrireqDao.Properties.Requid.eq(reqid)).list();

                if (list.size() != 1) {
                    //错误数据验证
                    finish();
                } else {
                    Frireq frireq = list.get(0);
                    String yn = frireq.getMet();//是否认识
                    String reqacc = frireq.getReqaccount();//账号
                    String reqnickname = frireq.getReqnickname();//昵称
                    String reqdes = frireq.getReqdes();//请求描述
                    tim = frireq.getTim();//请求时间

                    if (reqacc == null || reqnickname == null || reqdes == null || reqid == null
                            || yn == null || (!yn.equals(MineUtilA.para_yes) && !yn.equals(MineUtilA.para_no))) {
                        finish();
                    } else {
                        try {
                            if (yn.equals(MineUtilA.para_yes)) {
                                fri_res_radioGroup.check(R.id.fri_res_radioyes);
                                fri_res_radioGroup.getChildAt(1).setEnabled(false);
                            } else {
                                fri_res_radioGroup.check(R.id.fri_res_radiono);
                                fri_res_radioGroup.getChildAt(0).setEnabled(false);
                            }
                            fri_res_acc.setText(reqacc);
                            fri_res_nickname.setText(reqnickname);
                            fri_res_tim.setText(TimUtilA.formatTimeToStr(tim));

                            //fri_res_des应该是不尅编辑但可以复制。
                            fri_res_des.setFocusable(false);
                            fri_res_des.setFocusableInTouchMode(false);
                            fri_res_des.requestFocus();

                        } catch (Exception e) {
                            finish();
                        }
                        //判断是否本来就是我的好友。
                        isFri();
                    }
                }
            }
        }
    }

    private void initListener() {

        fri_res_agress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 同意。

                JsonObject into = new JsonObject();

                into.addProperty(MineUtilA.para_url, FriendUtilA.url_app_agreeFri);

                into.addProperty(FriendUtilA.para_resid, AppUtil.getUid());
                into.addProperty(FriendUtilA.para_reqid, reqid);
                into.addProperty(FriendUtilA.para_reqnickname, fri_res_nickname.getText().toString());
                StringRequest wj = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String msg) {
                        fri_res_agress.setEnabled(true);

                        try {
                            JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                            Integer r = jo.get(MineUtilA.para_url).getAsInt();
                            if (RetNumUtilA.n_0 == r) {

                                TextView edi = new TextView(FriResActivity.this);//textAppearanceMedium
                                edi.setGravity(Gravity.CENTER);
                                edi.setText(getString(R.string.success));
                                new AlertDialog.Builder(FriResActivity.this)
                                        .setTitle(R.string.tittle_ok)
                                        .setView(edi)
                                        .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).show();

                                //更新好友，信息。
                            } else if (RetNumUtilA.n_10 == r) {
                                //不应该出现。
                                //日志
                                alertDialogText(getString(R.string.err_timeout));
                            } else {
                                alertDialogText(getString(R.string.fail));
                            }
                        } catch (Exception e) {
                            alertDialogText(getString(R.string.fail));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fri_res_agress.setEnabled(true);
                    }
                });
                MyApp.mC.getVQ().add(wj);
                fri_res_agress.setEnabled(false);
            }
        });

        fri_res_ignore.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleActive();
                        //忽略
                        finish();
                    }
                });
    }

    protected void deleActive() {
        if (reqid != null) {
            List<Active> al = MyApp.mC.getDS().getActiveDao().queryBuilder()
                    .where(ActiveDao.Properties.Uuid.eq(reqid)
                            , ActiveDao.Properties.Btyp.eq(ChatUtilA.url_app_findChatsingle)).list();
            MyApp.mC.getDS().getActiveDao().deleteInTx(al);
        }
    }


    // 是否我的好友，请求添加好友和回复使用
    private void isFri() {
        long num = MyApp.mC.getDS().getFriendDao().queryBuilder()
                .where(FriendDao.Properties.Fid.eq(reqid)).count();
        if (num == 1) {
            TextView edi = new TextView(FriResActivity.this);//textAppearanceMedium
            edi.setGravity(Gravity.CENTER);
            edi.setText(getString(R.string.myfriend_now));
            new AlertDialog.Builder(FriResActivity.this)
                    .setTitle(R.string.tittle_ok)
                    .setView(edi)
                    .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(FriResActivity.this, FriendDesActivity.class);
                            intent.putExtra(FriendUtilA.para_fid, reqid);
                            startActivity(intent);
                            finish();
                        }
                    }).show();
            deleActive();
        }
    }
}
