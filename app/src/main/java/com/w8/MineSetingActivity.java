package com.w8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.w8.base.pcurl.MineUtilA;

/**
 * 个人详情
 */
public class MineSetingActivity extends OnlineActivity implements View.OnClickListener {

    private TextView set_acc;
    private Switch sound_two;
    private LinearLayout details_head;
    private LinearLayout details_QRcode;
    private LinearLayout set_n_linear;
    private TextView set_nickname;
    private LinearLayout set_safe;
    private LinearLayout set_more;
    private Button sign_out;
    private TextView seting_auto_edit;
    private TextView seting_auto_txt;

    private String uid;
    private String ccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_seting);
        TAG = MineSetingActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        uid = AppUtil.getUid();
        ccount = AppUtil.getAccount();
        if (uid == null || "".equals(uid) || ccount == null || "".equals(ccount)) {
            this.finish();
            return;
        } else {
            initData();
        }
    }

    private void initData() {
        set_acc.setText(ccount);
        set_nickname.setText(AppUtil.getNickname());
        String auto = AppUtil.getAutograph();
        if ("".equals(auto)) {
            seting_auto_txt.setText(R.string.seting_auto);
        } else {
            seting_auto_txt.setText(auto);
        }
        switchUtils(sound_two, AppUtil.getSound());
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        set_n_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_nickname.setError(null);
                final EditText edi = new EditText(MineSetingActivity.this);
                edi.setText(AppUtil.getNickname());
                new AlertDialog.Builder(MineSetingActivity.this)
                        .setTitle(R.string.namedes_update)
                        .setView(edi)
                        .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newdes = edi.getText().toString();
                                if (MineUtilA.testUserNickname(newdes)) {
                                    upd(MineUtilA.url_app_updateMyNickname, newdes);
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    alertDialogText(getString(R.string.nickname_error));
                                }
                            }
                        })
                        .setNegativeButton(R.string.alert_choose_false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        sound_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                upd(MineUtilA.url_app_updateMysound, tt(isChecked) + "");
            }
        });
        details_head.setOnClickListener(this);
        details_QRcode.setOnClickListener(this);
        set_safe.setOnClickListener(this);
        set_more.setOnClickListener(this);
        sign_out.setOnClickListener(this);
        seting_auto_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.seting_auto_edit) {
            final EditText edi = new EditText(MineSetingActivity.this);
            if (seting_auto_txt.getText().equals(getString(R.string.seting_auto))) {
                edi.setText("");
            } else {
                edi.setText(seting_auto_txt.getText());
            }
            new AlertDialog.Builder(MineSetingActivity.this)
                    .setTitle(R.string.seting_update)
                    .setView(edi)
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upd(MineUtilA.url_app_updateMyautograph, edi.getText().toString());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.alert_choose_false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else if (id == R.id.sign_out) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.sign_out));
            new AlertDialog.Builder(MineSetingActivity.this)
                    .setTitle(R.string.tittle_ok)
                    .setView(textView)
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppUtil.quitSafe();
                            finish();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.alert_choose_false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else if (id == R.id.set_more) {

        } else if (id == R.id.set_safe) {

        } else if (id == R.id.details_QRcode) {
            Intent intent = new Intent(this, MineQRCodeActivity.class);
            startActivity(intent);
        } else if (id == R.id.details_head) {
//            Intent intent = new Intent(MineSetingActivity.this, ChatBigimgActivity.class);
//       头像问题         intent.putExtra(WxUtil.URL, AppUtil.getHeadportrait(MyDetailsActivity.this));
//            startActivity(intent);
        }
    }

    private void switchUtils(Switch switchs, int ii) {
        if (ii == MineUtilA.val_positive) {
            switchs.setChecked(true);
        } else {
            switchs.setChecked(false);//不接受和99都会成为这个
        }
    }

    private int tt(boolean b) {
        if (b) {
            return MineUtilA.val_positive;
        } else {
            return MineUtilA.val_nagative;
        }
    }

    //统一使用，更新我的设置，如：昵称，签名。
    private void upd(final int url, final String des) {
        JsonObject into = new JsonObject();
        into.addProperty(MineUtilA.para_url, url);
        boolean bb = false;
        try {
            into.addProperty(MineUtilA.para_uid, AppUtil.getUid());
            if (MineUtilA.url_app_updateMyautograph == url) {
                if (AppUtil.getAutograph().equals(des)) {
                    bb = false;
                } else {
                    into.addProperty(MineUtilA.para_autograph, des);
                    bb = true;
                }
            } else if (MineUtilA.url_app_updateMyNickname == url) {
                if (AppUtil.getNickname().equals(des)) {
                    bb = false;
                } else {
                    into.addProperty(MineUtilA.para_nickname, des);
                    bb = true;
                }
            } else if (MineUtilA.url_app_updateMysound == url) {
                if (AppUtil.getSound() == Integer.valueOf(des)) {
                    bb = false;
                } else {
                    into.addProperty(MineUtilA.para_sound, des);
                    bb = true;
                }
            }
        } catch (Exception e) {
            bb = false;
            Log.e(TAG, e.getMessage());
        }
        if (bb) {
            StringRequest wxr = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String msg) {
                            try {
                                JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                                Integer r = jo.get(MineUtilA.para_url).getAsInt();
                                if (RetNumUtilA.n_0 == r) {
                                    if (MineUtilA.url_app_updateMyautograph == url) {
                                        seting_auto_txt.setText(des);
                                        AppUtil.setAutograph(des);
                                    } else if (MineUtilA.url_app_updateMyNickname == url) {
                                        set_nickname.setText(des);
                                        AppUtil.setNickname(des);
                                    } else if (MineUtilA.url_app_updateMysound == url) {
                                        int sound = Integer.valueOf(des);
                                        switchUtils(sound_two, sound);
                                        AppUtil.setSound(sound);
                                    }
                                } else {
                                    setErrorMy(set_nickname, MineSetingActivity.this.getString(R.string.fail));
                                }
                            } catch (Exception e) {
                                alertDialogText(getString(R.string.fail));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    alertDialogText(getString(R.string.reg_web_err));
                }
            });
            MyApp.mC.getVQ().add(wxr);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        set_acc = (TextView) findViewById(R.id.set_acc);//账号
        set_n_linear = (LinearLayout) findViewById(R.id.set_n_linear);//昵称
        set_nickname = (TextView) findViewById(R.id.set_nickname);//昵称内容
        details_head = (LinearLayout) findViewById(R.id.details_head);//头像
        details_QRcode = (LinearLayout) findViewById(R.id.details_QRcode);//二维码
        sound_two = (Switch) findViewById(R.id.sound_two);//消息通知：声音通知
        set_safe = (LinearLayout) findViewById(R.id.set_safe);//安全：手机，邮箱，密保,黑名单等
        set_more = (LinearLayout) findViewById(R.id.set_more);//更多
        sign_out = (Button) findViewById(R.id.sign_out);//安全退出
        seting_auto_edit = (TextView) findViewById(R.id.seting_auto_edit);//签名编辑按钮
        seting_auto_txt = (TextView) findViewById(R.id.seting_auto_txt);//签名内容
    }
}
