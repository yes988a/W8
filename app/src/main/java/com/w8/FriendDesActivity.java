package com.w8;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.WxUtil;
import com.w8.base.data.Friend;
import com.w8.base.data.FriendDao;
import com.w8.base.pcurl.FriendUtil;
import com.w8.base.pcurl.MineUtil;

import java.util.List;

public class FriendDesActivity extends OnlineActivity implements View.OnClickListener {
    protected final int PERMISSION_CALL = 6721;//打电话权限标示
    protected final int PERMISSION_SEND_SMS = 34751;//发送短信权限标示
    private String userRid;//好友ID

    private Friend f;//好友详情

    private TextView friend_des_bzname;//好友备注名
    private TextView friend_des_rname;//好友登录名
    private TextView friend_des_phonenum;//好友电话号
    private TextView friend_des_phoneaddress;//电话地址
    private ImageView friend_des_cell;// 打电话
    private ImageView friend_des_sendmess;//发短信
    private ImageView friend_des_head;//头像
    private TextView friend_des_nowtime;//生活印记

    private Button friend_des_sendidcard;//发送名片
    private Button friend_des_clear;//清空聊天信息

    private Switch friend_des_shield_switch;//屏蔽好友

    /*开关的新旧值记录*/
    private int shield_new;
    private int shield_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_des);
        TAG = FriendDesActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            this.finish();
            return;
        } else {
            userRid = bundle.getString(FriendUtil.para_fid);
            if (userRid == null || "".equals(userRid)) {
                this.finish();
                return;
            } else {
                List<Friend> list = MyApp.mC.getDS().getFriendDao().queryBuilder().where(FriendDao.Properties.Fid.eq(userRid)).list();
                if (list == null || list.size() != 1) {
                    this.finish();
                    return;
                } else {
                    f = list.get(0);
                    initData();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_frienddes, menu);
        return true;
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendDesActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (R.id.frienddes_complaint == id) {//投诉
                    complaint();
                } else if (R.id.frienddes_blacklist == id) {//黑名单
                    blacklist();
                } else if (R.id.frienddes_dele == id) {//删除好友
                    deleFriend();
                }
                return false;
            }
        });
        friend_des_cell.setOnClickListener(this);//打电话
        friend_des_sendmess.setOnClickListener(this);//发消息
        friend_des_head.setOnClickListener(this);//头像
        friend_des_nowtime.setOnClickListener(this);//生活印记
        friend_des_sendidcard.setOnClickListener(this);//发送名片
        friend_des_clear.setOnClickListener(this);//清空聊天内容
        friend_des_bzname.setOnClickListener(this);//备注名修改
        friend_des_shield_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shield_new = tt(isChecked);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int idd = view.getId();
        if (idd == R.id.friend_des_cell) {//打电话
            new AlertDialog.Builder(this)
                    .setTitle(R.string.call)
                    .setMessage(f.getPhone())
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                int permissionResult = ContextCompat.checkSelfPermission(FriendDesActivity.this, Manifest.permission.CALL_PHONE);
                                if (permissionResult == PackageManager.PERMISSION_GRANTED) {//已授权
                                    cellPhone();
                                } else if (permissionResult == PackageManager.PERMISSION_DENIED) {//未授权
                                    ActivityCompat.requestPermissions(FriendDesActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CALL);//请求权限
                                }
                            } else {
                                cellPhone();
                            }
                        }
                    })
                    .setNegativeButton(R.string.alert_choose_false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else if (idd == R.id.friend_des_sendmess) {//发消息
            if (Build.VERSION.SDK_INT >= 23) {
                int permissionResult = ContextCompat.checkSelfPermission(FriendDesActivity.this, Manifest.permission.SEND_SMS);
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {//已授权
                    sms();
                } else if (permissionResult == PackageManager.PERMISSION_DENIED) {//未授权
                    ActivityCompat.requestPermissions(FriendDesActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_SEND_SMS);//请求权限
                }
            } else {
                sms();
            }
        } else if (idd == R.id.friend_des_head) {//头像
           /* Intent intent = new Intent(this, ChatBigimgActivity.class);
//            intent.putExtra(WxUtil.img_path, f.getFid());
            startActivity(intent);*/
        } else if (idd == R.id.friend_des_nowtime) {//生活印记

        } else if (idd == R.id.friend_des_sendidcard) {//发送名片

        } else if (idd == R.id.friend_des_clear) {//清空聊天内容
            String fid = f.getFid();
            AppUtil.clearOne(fid);
        } else if (idd == R.id.friend_des_bzname) {//备注名修改
            final EditText edi = new EditText(FriendDesActivity.this);
            edi.setText(friend_des_bzname.getText().toString());
            new AlertDialog.Builder(FriendDesActivity.this)
                    .setTitle(R.string.update_remarks)
                    .setView(edi)
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String newdes = edi.getText().toString();
                            if (MineUtil.testUserNickname(newdes)) {
                                friend_des_bzname.setText(newdes);
                                dialog.dismiss();
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
    }

    private void initData() {
/*
        List<Balanc> lbbb = MyApp.mC.getDS().getBalancDao().queryBuilder().where(BalancDao.Properties.Bid.eq(f.getBid())).list();
        if (lbbb.size() != 1) {  //仅仅用于检测用户对应的IP是否有效，此activity中无用，聊天时才有用
            MyApp.mC.getDS().getBalancDao().deleteInTx(lbbb);
            updateBidWeb(f.getBid());
        }*/
        friend_des_bzname.setText(f.getRemark());
        friend_des_rname.setText(f.getAccount());
        friend_des_phonenum.setText(f.getPhone().toString());
//        friend_des_phoneaddress.setText(friend.getPhoneaddress() + " " + friend.getPhonetype());   暂时去掉啦吧
        shield_old = f.getShie();
        shield_new = f.getShie();
        switchUtils(friend_des_shield_switch, shield_old);
        webSwitch();//网络请求验证我对好友的设置
    }

    /**
     * 拨打电话
     */
    private void cellPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + f.getPhone()));
        startActivity(intent);
    }

    /**
     * 发送短信
     * <p>
     * 待完善，不能调用系统发短信界面，因为有些模拟器没有此界面
     */
    private void sms() {
        if (PhoneNumberUtils.isGlobalPhoneNumber(f.getPhone())) {
            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("smsto:" + f.getPhone()));
            startActivity(intent);
        }
    }

    /**
     * 请求授权返回值
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    cellPhone();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permission_failed)
                            .setMessage(R.string.permission_callphone_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
            case PERMISSION_SEND_SMS: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    sms();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permission_failed)
                            .setMessage(R.string.permission_sms_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 网络，请求，我对好友的设置和备注和电话号码。。。
     */
    private void webSwitch() {
        if (userRid != null && !"".equals(userRid)) {//正常情况
            JsonObject jo = new JsonObject();
            jo.addProperty(FriendUtil.para_fid, userRid);//哪个好友？
            /*WXStringRequest wxsr = new WXStringRequest(this, AppUtil.getBurl(this) + WxUtil.u_getfriseting, jo, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jreq = null;
                    try {
                        jreq = JSON.parseObject(response);
                    } catch (Exception e) {
                        mwErr(TAG, "u_getfriseting,onResponse", e);
                    }
                    if (jreq == null) {
                        // 日志
                    } else {
                        Integer r = jreq.getInteger(WxUtil.r);
                        if (RetNumUtil.n_0 == r) {
                            UserrelationSettingPojo se = null;
                            try {
                                se = JSON.parseObject(jreq.getString("fo"), UserrelationSettingPojo.class);
                            } catch (Exception e) {
                                mwErr(TAG, "u_getfriseting,fo", e);
                            }
                            boolean beq = false;
                            if (se == null) {
                                // 日志
                            } else {
                                //一个一个判值是否相等
                                String friendid = se.getFid();
                                if (friendid != null && friendid.equals(f.getFid())) {
                                    if (se.getPhone() != null && !se.getPhone().equals(f.getPhone())) {
                                        beq = true;
                                        f.setPhone(se.getPhone());
                                        friend_des_phonenum.setText(se.getPhone());
                                    }
                                    if (se.getRemark() != null && !se.getRemark().equals(f.getRemark())) {
                                        beq = true;
                                        f.setRemark(se.getRemark());
                                        friend_des_bzname.setText(se.getRemark());
                                    }
                                    if (se.getBid() != null && !se.getBid().equals(f.getBid())) {
                                        beq = true;
                                        f.setBid(se.getBid());
                                    }
                                    if (se.getShie() != null && se.getShie() != f.getShie()) {
                                        beq = true;
                                        shield_old = se.getShie();
                                        f.setShie(se.getShie());
                                        switchUtils(friend_des_shield_switch, shield_old);
                                    }

                                } else {
                                    // 日志
                                    FriendDesActivity.this.finish();
                                }
                            }
                            if (beq) {//app和pc不一样，更新sqlite
                                MyApp.mC.getDS().getFriendDao().updateInTx(f);
                            }
                        } else if (RetNumUtil.n_1 == r) {
                            TokenUtil.updateTo(app);
                        } else {
                            normalStart = false;
                            // 日志
                            FriendDesActivity.this.finish();
                        }
                    }
                }
            });
            app.getVolleyQeue().add(wxsr);*/
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        friend_des_bzname = (TextView) findViewById(R.id.friend_des_bzname);
        friend_des_rname = (TextView) findViewById(R.id.friend_des_rname);
        friend_des_phonenum = (TextView) findViewById(R.id.friend_des_phonenum);
        friend_des_phoneaddress = (TextView) findViewById(R.id.friend_des_phoneaddress);
        friend_des_cell = (ImageView) findViewById(R.id.friend_des_cell);
        friend_des_sendmess = (ImageView) findViewById(R.id.friend_des_sendmess);
        friend_des_head = (ImageView) findViewById(R.id.friend_des_head);
        friend_des_nowtime = (TextView) findViewById(R.id.friend_des_nowtime);
        friend_des_clear = (Button) findViewById(R.id.friend_des_clear);
        friend_des_sendidcard = (Button) findViewById(R.id.friend_des_sendidcard);
        friend_des_shield_switch = (Switch) findViewById(R.id.friend_des_shield_switch);
    }

    private void switchUtils(Switch switchs, int ii) {
        if (ii == WxUtil.val_nagative) {
            switchs.setChecked(true);
        } else {
            switchs.setChecked(false);//不接受和99都会成为这个
        }
    }

    private int tt(boolean b) {
        if (b) {
            return WxUtil.val_nagative;
        } else {
            return WxUtil.val_positive;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (f != null) {
            boolean change = false;
            JsonObject jo = new JsonObject();
            jo.addProperty(FriendUtil.para_fid, userRid);
            if (!friend_des_bzname.getText().toString().equals(f.getRemark())) {
                change = true;
                jo.addProperty(FriendUtil.para_remark, friend_des_bzname.getText().toString());
                f.setRemark(friend_des_bzname.getText().toString());
            }

            if (shield_old != shield_new) {
                change = true;
                jo.addProperty(FriendUtil.para_shie, shield_new);
                f.setShie(shield_new);
            }
            // 优化，如果失败怎么办？不能让用户返回操作
            if (change) {
                MyApp.mC.getDS().getFriendDao().updateInTx(f);
               /* WXStringRequest wxsr = new WXStringRequest(this, AppUtil.getBurl(this) + WxUtil.u_updatefriset, jo, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jo = null;
                        try {
                            jo = JSON.parseObject(response);
                        } catch (Exception e) {
                            mwErr(TAG, "u_updatefriset,onResponse", e);
                        }
                        if (jo == null) {
// 日志
                        } else {
                            Integer r = jo.getInteger(WxUtil.r);
                            if (r == null) {
                                Toast.makeText(FriendDesActivity.this, R.string.friend_save_err, Toast.LENGTH_SHORT).show();
                            } else if (RetNumUtil.n_0 == r) {

                            } else if (RetNumUtil.n_1 == r) {
                                TokenUtil.updateTo(app);
                                Toast.makeText(FriendDesActivity.this, R.string.friend_save_err, Toast.LENGTH_SHORT).show();
                            } else {
                                // 日志
                                Toast.makeText(FriendDesActivity.this, R.string.friend_save_err, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                app.getVolleyQeue().add(wxsr);*/
            }
        }
    }

    /**
     * 投诉
     */
    private void complaint() {
    }

    /**
     * 黑名单
     */
    private void blacklist() {
    }

    /**
     * 删除好友
     */
    private void deleFriend() {
    }
}
