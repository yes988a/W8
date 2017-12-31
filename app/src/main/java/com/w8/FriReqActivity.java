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
import com.w8.base.pcurl.RetNumUtilA;
import com.w8.base.data.FriendDao;
import com.w8.base.pcurl.FriendUtilA;
import com.w8.base.pcurl.MineUtilA;

/**
 * 添加好友请求页面，扫一扫后跳转到这里
 */
public class FriReqActivity extends OnlineActivity {

    // 请求页面（搜到：昵称，签名）
    // 被请求页面：（账号，昵称，请求描述，是否认识,from从哪里来）

    private TextView fri_req_nickname;  // 昵称
    private TextView fri_req_autograph;// 签名
    private EditText fri_req_des;//请求描述

    private RadioGroup fri_req_radioGroup;

    private Button fri_req_add;//添加好友。

    private String resid;

//    private String from; //从哪里过来？ 一、扫一扫。二、账号搜索。三、手机搜索。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri_req);
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
        fri_req_nickname = (TextView) findViewById(R.id.fri_req_nickname);
        fri_req_autograph = (TextView) findViewById(R.id.fri_req_autograph);
        fri_req_des = (EditText) findViewById(R.id.fri_req_des);
        fri_req_radioGroup = (RadioGroup) findViewById(R.id.fri_req_radioGroup);
        fri_req_add = (Button) findViewById(R.id.fri_req_add);
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        webing = false;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            finish();
        } else {
            fri_req_radioGroup.check(R.id.fri_req_radiono);
            resid = bundle.getString(FriendUtilA.para_fid);//用户ID
            String nickname = bundle.getString(MineUtilA.para_nickname);//昵称
            String autograph = bundle.getString(MineUtilA.para_autograph);//签名

            if (nickname == null || autograph == null || resid == null) {
                finish();
            } else {
                if (!"".equals(nickname)) {
                    fri_req_nickname.setText(nickname);
                }
                if (!"".equals(autograph)) {
                    fri_req_autograph.setText(autograph);
                }
                isFri();
            }
        }
    }

    private void initListener() {
        fri_req_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webing) {
                    // 请求中，
                } else {
//发送添加请求
                    JsonObject into = new JsonObject();

                    into.addProperty(MineUtilA.para_url, FriendUtilA.url_app_requestFri);

                    into.addProperty(FriendUtilA.para_resid, resid);
                    into.addProperty(FriendUtilA.para_reqid, AppUtil.getUid());

                    if (R.id.fri_req_radioyes == fri_req_radioGroup.getCheckedRadioButtonId()) {
                        into.addProperty(FriendUtilA.para_met, MineUtilA.para_yes);
                    } else {
                        into.addProperty(FriendUtilA.para_met, MineUtilA.para_no);
                    }

                    into.addProperty(FriendUtilA.para_reqdes, fri_req_des.getText().toString());

                    StringRequest wj = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String msg) {
                            fri_req_add.setEnabled(true);
                            try {
                                JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                                Integer r = jo.get(MineUtilA.para_url).getAsInt();
                                if (RetNumUtilA.n_0 == r) {
                                    TextView edi = new TextView(FriReqActivity.this);//textAppearanceMedium
                                    edi.setGravity(Gravity.CENTER);
                                    edi.setText(getString(R.string.success));
                                    new AlertDialog.Builder(FriReqActivity.this)
                                            .setTitle(R.string.tittle_ok)
                                            .setView(edi)
                                            .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            }).show();
                                } else if (RetNumUtilA.n_8 == r) {
                                    //不应该出现。
                                    //日志
                                    alertDialogText(getString(R.string.search_des_no));
                                } else if (RetNumUtilA.n_9 == r) {
                                    isFri();
                                } else {
                                    alertDialogText(getString(R.string.fail));
                                }
                            } catch (Exception e) {
                                mwErr(TAG, "u_reqFriReq,sfawehjf", e);
                                alertDialogText(getString(R.string.fail));
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            fri_req_add.setEnabled(true);
                        }
                    });
                    MyApp.mC.getVQ().add(wj);
                    fri_req_add.setEnabled(false);
                }
            }
        });
    }

    // 是否我的好友，请求添加好友和回复使用
    private void isFri() {
        long num = MyApp.mC.getDS().getFriendDao().queryBuilder()
                .where(FriendDao.Properties.Fid.eq(resid)).count();
        if (num == 1) {

            TextView edi = new TextView(FriReqActivity.this);//textAppearanceMedium
            edi.setGravity(Gravity.CENTER);
            edi.setText(getString(R.string.myfriend_now));
            new AlertDialog.Builder(FriReqActivity.this)
                    .setTitle(R.string.tittle_ok)
                    .setView(edi)
                    .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(FriReqActivity.this, FriendDesActivity.class);
                            intent.putExtra(FriendUtilA.para_fid, resid);
                            startActivity(intent);
                            finish();
                        }
                    }).show();

        } else {
            //更新我的好友列表。web
            // 优化
        }
    }
}
