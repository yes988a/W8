package com.w8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.pcurl.RetNumUtilA;
import com.w8.base.data.Group;
import com.w8.base.data.GroupDao;
import com.w8.base.pcurl.GroupUtilA;
import com.w8.base.pcurl.MineUtilA;

import java.util.List;

/**
 * 群详情描述，包括我对群的设置
 */
public class GroupDesActivity extends OnlineActivity implements View.OnClickListener {

    //必须知道是不是管理员。
    private boolean knowMaster;//是不是管理员，应该在初始化数据时就做判断。但是由于第一次时没有groups详情，所以，，，应该在跳转到这个activity前就去获取群详情???，，

    private String gid;//群ID
    private String gurl;//群地址

    private Group g;//群详情

    private int group_des_shield_old = RetNumUtilA.n_b1;//屏蔽
    private int group_des_shield_new = RetNumUtilA.n_b1;

    private ImageView group_des_head;
    private TextView group_des_gname;
    private LinearLayout group_des_gremark_linear;
    private TextView group_des_gremark_txt;
    private LinearLayout group_des_usernum_linear;
    private TextView group_des_usernum_txt;
    private LinearLayout group_des_notice_linear;
    private TextView group_des_notice_txt;
    private LinearLayout group_des_nickname_linear;
    private TextView group_des_nickname_txt;
    private Switch group_des_shield_switch;
    private Button group_des_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_des);
        TAG = GroupDesActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        knowMaster = false;
        Bundle bundle = this.getIntent().getExtras();
        String uid = AppUtil.getUid();
        if (uid == null) {
            AppUtil.setToken("");
            this.finish();
        } else if (bundle == null) {
            this.finish();
        } else {
            gid = bundle.getString(GroupUtilA.para_gid);
            if (gid == null || "".equals(gid)) {
                this.finish();
            } else {
                List<Group> list = MyApp.mC.getDS().getGroupDao().queryBuilder().where(GroupDao.Properties.Gid.eq(gid)).list();
                if (list == null || list.size() != 1) {
                    this.finish();
                } else {
                    g = list.get(0);
                    //提前获取bid对应的URL------start--
                    /*List<Balanc> lbbb = MyApp.mC.getDS().getBalancDao().queryBuilder().where(BalancDao.Properties.Bid.eq(g.getBid())).list();
                    //根据bid没有查找到url会出现获取不到群详情的错误。不影响我对群的设置.。所以不是特别重要的事情。
                    if (lbbb.size() == RetNumUtilA.n_0) {
                        updateBidWeb(g.getBid());
                        normalStart = false;
                        this.finish();
                    } else if (lbbb.size() == RetNumUtilA.n_1) {
                        gurl = lbbb.get(0).getBurl();
                        if (uid.equals(g.getMasterid())) {
                            knowMaster = true;
                        }
                        getGrouSimpleBygid();
                    } else {
                        MyApp.mC.getDS().getBalancDao().deleteInTx(lbbb);
                        updateBidWeb(g.getBid());
                        normalStart = false;
                        this.finish();
                    }*/
                    //提前获取bid对应的URL------------end--
                    initData();
                }
            }
        }
    }

    private void initData() {

        JsonObject jv = new JsonObject();
        jv.addProperty(GroupUtilA.para_gid, gid);
        //查询user服务器
       /* WXStringRequest getset = new WXStringRequest(GroupDesActivity.this, AppUtil.getBurl(GroupDesActivity.this) + WxUtil.u_getgroupseting, jv, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                JsonObject jreq = null;
                Integer r = null;
                try {
                    jreq = JSON.parseObject(res);
                    r = jreq.getInteger(WxUtil.r);
                } catch (Exception e) {
                    mwErr(TAG, "查询user服务器,wehgna", e);
                }
                if (r == null) {
                    // 日志
                    normalStart = false;
                    GroupDesActivity.this.finish();
                } else if (RetNumUtilA.n_0 == r) {
                    Ugroup sue = null;
                    try {
                        sue = JSON.parseObject(jreq.getString("fo"), Ugroup.class);
                    } catch (Exception e) {
                        mwErr(TAG, "查询user服务器,fo", e);
                    }
                    if (sue == null) {
                        normalStart = false;
                        GroupDesActivity.this.finish();
                    } else {
                        boolean chan = false;
                        if (sue.getNickname() != null && !sue.getNickname().equals(g.getNickname())) {
                            chan = true;
                            g.setNickname(sue.getNickname());
                            group_des_nickname_txt.setText(sue.getNickname());
                        }

                        if (sue.getGremark() != null && !sue.getGremark().equals(g.getGremark())) {
                            chan = true;
                            g.setGremark(sue.getGremark());
                            group_des_gremark_txt.setText(sue.getGremark());
                        }

                        if (sue.getShie() != null && !sue.getShie().equals(g.getShie())) {
                            chan = true;
                            g.setShie(sue.getShie());
                            switchUtils(group_des_shield_switch, sue.getShie());
                        }

                        if (sue.getBid() != null && !sue.getBid().equals(g.getBid())) {
                            chan = true;
                            g.setBid(sue.getBid());
                        }
                        if (chan) {
                            MyApp.mC.getDS().update(g);
                        }
                    }
                } else if (RetNumUtilA.n_1 == r) {
                    TokenUtil.updateTo(app);
                } else {
                    // 日志
                    normalStart = false;
                    GroupDesActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
// 日志
            }
        });
        app.getVolleyQeue().add(getset);*/

        group_des_shield_old = g.getShie();
        group_des_shield_new = g.getShie();
        group_des_gname.setText(g.getGname());
        group_des_gremark_txt.setText(g.getGremark());
        group_des_usernum_txt.setText(g.getUsernum() + getString(R.string.userstring));
        if (g.getGotice() != null && g.getGotice().length() > 0) {
            group_des_notice_txt.setText(g.getGotice());
        } else {
            group_des_notice_txt.setText(R.string.group_notice_no);
        }
        group_des_nickname_txt.setText(g.getNickname());
        switchUtils(group_des_shield_switch, group_des_shield_old);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groupdes, menu);
        return true;
    }

    /**
     * 投诉
     */
    private void complaint() {
    }

    /**
     * 删除并退出
     */
    private void deleGroup() {

        if (g.getMasterid() == null) {
            GroupDesActivity.this.finish();
        } else if (knowMaster) {
            new AlertDialog.Builder(GroupDesActivity.this)
                    .setTitle(R.string.group_des_quitgroup)
                    .setMessage(R.string.group_des_groupmasterquit)
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //解散群
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.alert_choose_false, new DialogInterface.OnClickListener() {
                        //取消操作
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton(R.string.group_des_give, new DialogInterface.OnClickListener() {
                        //群主身份转让
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            quitGroup();
        }
    }

    private void switchUtils(Switch switchs, int ii) {
        if (ii == MineUtilA.val_nagative) {
            switchs.setChecked(true);
        } else {
            switchs.setChecked(false);//不接受和99都会成为这个
        }
    }

    private int tt(boolean b) {
        if (b) {
            return MineUtilA.val_nagative;
        } else {
            return MineUtilA.val_positive;
        }
    }

    /**
     * 根据群主键获取群信息。单独写此方法是因为，如果bid不存在需要请求服务器获取，需要广播
     * <p>
     * 广播后使用，
     * 或者gurl不为空时使用
     * <p>
     * 如果url为null，等待用户刷新也可吧。
     */
    private void getGrouSimpleBygid() {
        if (gurl == null) {
           /* List<Balanc> lbbb = MyApp.mC.getDS().getBalancDao().queryBuilder().where(BalancDao.Properties.Bid.eq(g.getBid())).list();
            if (lbbb.size() == 1) {
                gurl = lbbb.get(0).getBurl();
            }*/
        } else {
            JsonObject jv = new JsonObject();
            jv.addProperty(GroupUtilA.para_gid, gid);
            //查询群服务器
            /*WXStringRequest srr = new WXStringRequest(GroupDesActivity.this, gurl + WxUtil.u_getonegroupsimple, jv, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {
                    JsonObject jreq = null;
                    Integer r = null;
                    try {
                        jreq = JSON.parseObject(res);
                        r = jreq.getInteger(WxUtil.r);
                    } catch (Exception e) {
                        mwErr(TAG, "u_getonegroupsimple,onResponse", e);
                    }
                    if (r == null) {
                        normalStart = false;
                        // 日志
                        GroupDesActivity.this.finish();
                    } else if (RetNumUtilA.n_0 == r) {
                        Groups sue = null;
                        try {
                            sue = JSON.parseObject(jreq.getString(WxUtil.groupdes), Groups.class);
                        } catch (Exception e) {
                            mwErr(TAG, "u_getonegroupsimple,gsm", e);
                        }
                        if (sue == null) {
                            normalStart = false;
                            // 日志
                            GroupDesActivity.this.finish();
                        } else {
                            boolean chan = false;
                            if (sue.getGname() != null && !sue.getGname().equals(g.getGname())) {
                                chan = true;
                                g.setGname(sue.getGname());
                                group_des_gname.setText(sue.getGname());
                            }
                            if (sue.getGotice() != null && !sue.getGotice().equals(g.getGotice())) {
                                chan = true;
                                g.setGotice(sue.getGotice());
                                if ("".equals(sue.getGotice())) {
                                    group_des_notice_txt.setText(R.string.group_notice_no);
                                } else {
                                    group_des_notice_txt.setText(sue.getGotice());
                                }
                            }
                            if (sue.getMasterid() != null && !sue.getMasterid().equals(g.getMasterid())) {
                                chan = true;
                                g.setMasterid(sue.getMasterid());
                            }
                            if (sue.getUsernum() != null && sue.getUsernum() != g.getUsernum()) {
                                chan = true;
                                g.setUsernum(sue.getUsernum());
                                group_des_usernum_txt.setText(sue.getUsernum() + getString(R.string.userstring));
                                //验证人数是否正确，并从服务器获取正确人数
                            }
                            if (chan) {
                                MyApp.mC.getDS().update(g);
                            }
                        }
                    } else if (RetNumUtilA.n_1 == r) {
                        TokenUtil.updateTo(app);
                    } else {
                        normalStart = false;
                        // 日志
                        GroupDesActivity.this.finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // 日志
                }
            });
            app.getVolleyQeue().add(srr);*/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSeting();
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);

        group_des_head = (ImageView) findViewById(R.id.group_des_head);
        group_des_gname = (TextView) findViewById(R.id.group_des_gname);
        group_des_gremark_linear = (LinearLayout) findViewById(R.id.group_des_gremark_linear);
        group_des_gremark_txt = (TextView) findViewById(R.id.group_des_gremark_txt);
        group_des_usernum_linear = (LinearLayout) findViewById(R.id.group_des_usernum_linear);
        group_des_usernum_txt = (TextView) findViewById(R.id.group_des_usernum_txt);
        group_des_notice_linear = (LinearLayout) findViewById(R.id.group_des_notice_linear);
        group_des_notice_txt = (TextView) findViewById(R.id.group_des_notice_txt);
        group_des_nickname_linear = (LinearLayout) findViewById(R.id.group_des_nickname_linear);
        group_des_nickname_txt = (TextView) findViewById(R.id.group_des_nickname_txt);
        group_des_shield_switch = (Switch) findViewById(R.id.group_des_shield_switch);
        group_des_clear = (Button) findViewById(R.id.group_des_clear);

    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupDesActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (R.id.frienddes_complaint == id) {//投诉
                    complaint();
                } else if (R.id.frienddes_dele == id) {//删除
                    deleGroup();
                }
                return false;
            }
        });

        group_des_shield_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group_des_shield_new = tt(isChecked);
            }
        });
        group_des_head.setOnClickListener(this);
        group_des_gname.setOnClickListener(this);
        group_des_gremark_linear.setOnClickListener(this);
        group_des_usernum_linear.setOnClickListener(this);
        group_des_notice_linear.setOnClickListener(this);
        group_des_nickname_linear.setOnClickListener(this);
        group_des_clear.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int idds = view.getId();
        if (idds == R.id.group_des_usernum_linear) {//群成员
            Intent iii = new Intent(this, GroupusersActivity.class);
            /*iii.putExtra(WxUtil.gid, gid);
            iii.putExtra(WxUtil.url, gurl);
            iii.putExtra(WxUtil.num, g.getUsernum());
            iii.putExtra(WxUtil.knowMaster, knowMaster);*/
            startActivity(iii);
        } else if (idds == R.id.group_des_head) {//群头像，单击上传。群主操作

        } else if (idds == R.id.group_des_gname) {//群真正的名称。群主操作

            if (g.getMasterid() == null || AppUtil.getUid().equals(g.getMasterid())) {
                final EditText edi = new EditText(GroupDesActivity.this);
                edi.setText(group_des_gname.getText());
                new AlertDialog.Builder(GroupDesActivity.this)
                        .setTitle(R.string.group_des_name_update)
                        .setView(edi)
                        .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (GroupUtilA.testGroupNickname(edi.getText().toString())) {
                                    if (!edi.getText().toString().equals(g.getGname())) {
                                        updateGname(edi.getText().toString());
                                    }
                                } else {
                                    Toast.makeText(GroupDesActivity.this, R.string.fail_format, Toast.LENGTH_LONG).show();
                                }
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
            } else {
                new AlertDialog.Builder(GroupDesActivity.this)
                        .setTitle(R.string.group_des_name_update)
                        .setMessage(R.string.group_des_update_err_m)
                        .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        } else if (idds == R.id.group_des_notice_linear) {//群公告
            if (g.getMasterid() == null || AppUtil.getUid().equals(g.getMasterid())) {
                final EditText edi = new EditText(GroupDesActivity.this);
                if (group_des_notice_txt.getText().equals(getString(R.string.group_notice_no))) {
                    edi.setText("");
                } else {
                    edi.setText(group_des_notice_txt.getText());
                }
                new AlertDialog.Builder(GroupDesActivity.this)
                        .setTitle(R.string.group_des_notice_update)
                        .setView(edi)
                        .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!edi.getText().toString().equals(g.getGotice())) {
                                    updateGnotice(edi.getText().toString());
                                }
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
            } else {
                new AlertDialog.Builder(GroupDesActivity.this)
                        .setTitle(R.string.group_des_name_update)
                        .setMessage(R.string.group_des_update_err_m)
                        .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        } else if (idds == R.id.group_des_gremark_linear) {//群备注名
            final EditText edi = new EditText(GroupDesActivity.this);
            edi.setText(group_des_gremark_txt.getText());
            new AlertDialog.Builder(GroupDesActivity.this)
                    .setTitle(R.string.group_des_gname_update)
                    .setView(edi)
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (GroupUtilA.testGroupNickname(edi.getText().toString())) {
                                group_des_gremark_txt.setText(edi.getText().toString());
                            } else {
                                Toast.makeText(GroupDesActivity.this, R.string.fail_format, Toast.LENGTH_SHORT).show();
                            }
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

        } else if (idds == R.id.group_des_nickname_linear) {//我在群中的备注
            final EditText edi = new EditText(GroupDesActivity.this);
            edi.setText(group_des_nickname_txt.getText());
            new AlertDialog.Builder(GroupDesActivity.this)
                    .setTitle(R.string.group_des_myname_update)
                    .setView(edi)
                    .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (MineUtilA.testUserNickname(edi.getText().toString())) {
                                group_des_nickname_txt.setText(edi.getText().toString());
                            } else {
                                Toast.makeText(GroupDesActivity.this, R.string.fail_format, Toast.LENGTH_SHORT).show();
                            }
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
        } else if (idds == R.id.group_des_clear) {//清空
            AppUtil.clearOne(gid);
            finish();
        }
    }

    /**
     * 保存对群设置
     */
    private void saveSeting() {
        boolean change = false;
        JsonObject jo = new JsonObject();
/*
        jo.addProperty(WxUtil.gid, gid);
        if (!group_des_gremark_txt.getText().toString().equals(g.getGremark())) {
            change = true;
            jo.addProperty(WxUtil.gremark, group_des_gremark_txt.getText().toString());
            g.setGremark(group_des_gremark_txt.getText().toString());
        }
        if (!group_des_nickname_txt.getText().toString().equals(g.getNickname())) {
            change = true;
            jo.addProperty(WxUtil.nickname, group_des_nickname_txt.getText().toString());
            g.setGremark(group_des_nickname_txt.getText().toString());
        }
        if (group_des_shield_new != group_des_shield_old) {
            change = true;
            jo.addProperty(WxUtil.shie, group_des_shield_new);
            g.setShie(group_des_shield_new);
        }*/
        if (change) {
            MyApp.mC.getDS().getGroupDao().update(g);
            /*WXStringRequest wxsr = new WXStringRequest(this, AppUtil.getBurl(this) + WxUtil.u_usergroupsetting, jo, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JsonObject jo = null;
                    Integer r = null;
                    try {
                        jo = JSON.parseObject(response);
                        r = jo.getInteger(WxUtil.r);
                    } catch (Exception e) {
                        mwErr(TAG, "u_usergroupsetting,onResponse", e);
                    }

                    if (r == null) {
                        Toast.makeText(GroupDesActivity.this, R.string.group_save_err, Toast.LENGTH_LONG).show();
                    } else if (RetNumUtilA.n_0 == r) {
                        try {
                            AppUtil.setTimeReal(GroupDesActivity.this, jo.getLong(WxUtil.tim));
                        } catch (Exception e) {
                            mwErr(TAG, "u_usergroupsetting,                                mwErr(TAG,\"u_usergroupsetting,onRespo AppUtil.setTimeReal(GroupDnse\",e);\n", e);
                        }
                    } else if (RetNumUtilA.n_1 == r) {
                        TokenUtil.updateTo(app);
                        Toast.makeText(GroupDesActivity.this, R.string.group_save_err, Toast.LENGTH_LONG).show();
                    } else {
                        // 日志
                        Toast.makeText(GroupDesActivity.this, R.string.group_save_err, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_LONG).show();
                }
            });
            app.getVolleyQeue().add(wxsr);*/
        }
    }

    /**
     * 修改群名称
     */
    private void updateGname(final String gname) {
        if (gurl == null) {
            this.finish();
        } else {
            JsonObject jv = new JsonObject();
            /*jv.addProperty(WxUtil.gid, gid);
            jv.addProperty(WxUtil.gname, gname);
            //查询群服务器
            final WXStringRequest srr = new WXStringRequest(GroupDesActivity.this, gurl + WxUtil.u_savegnamebymaster, jv, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {
                    JsonObject jo = null;
                    Integer r = null;
                    try {
                        jo = JSON.parseObject(res);
                        r = jo.getInteger(WxUtil.r);
                    } catch (Exception e) {
                    }

                    if (r == null) {
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                    } else if (RetNumUtilA.n_0 == r) {
                        group_des_gname.setText(gname);
                        g.setGname(gname);
                        MyApp.mC.getDS().getGroupDao().update(g);
                    } else if (RetNumUtilA.n_1 == r) {
                        TokenUtil.updateTo(app);
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                    } else if (RetNumUtilA.n_2 == r) {
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (RetNumUtilA.n_15 == r) {
                        Toast.makeText(GroupDesActivity.this, R.string.group_des_update_err_m, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_SHORT).show();
                }
            });
            app.getVolleyQeue().add(srr);*/
        }
    }

    /**
     * 修改群公告
     */
    private void updateGnotice(final String gnotice) {
        if (gurl == null) {
            this.finish();
        } else {
            JsonObject jv = new JsonObject();
            /*jv.addProperty(WxUtil.gid, gid);
            jv.addProperty(WxUtil.gnotice, gnotice);
            //查询群服务器
            WXStringRequest srr = new WXStringRequest(GroupDesActivity.this, gurl + WxUtil.u_savegnoticebymaster, jv, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {
                    JsonObject jo = null;
                    Integer r = null;
                    try {
                        jo = JSON.parseObject(res);
                        r = jo.getInteger(WxUtil.r);
                    } catch (Exception e) {
                    }

                    if (r == null) {
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                    } else if (RetNumUtilA.n_0 == r) {
                        if (gnotice == null || "".equals(gnotice)) {
                            group_des_notice_txt.setText(R.string.group_notice_no);
                        } else {
                            group_des_notice_txt.setText(gnotice);
                        }
                        g.setGotice(gnotice);
                        MyApp.mC.getDS().getGroupDao().update(g);
                    } else if (RetNumUtilA.n_1 == r) {
                        TokenUtil.updateTo(app);
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                    } else if (RetNumUtilA.n_15 == r) {
                        Toast.makeText(GroupDesActivity.this, R.string.group_des_update_err_m, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GroupDesActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_SHORT).show();
                }
            });
            app.getVolleyQeue().add(srr);*/
        }
    }

    /**
     * 普通群员退群
     */
    private void quitGroup() {
        JsonObject jv = new JsonObject();
         /*jv.addProperty(WxUtil.gid, gid);
        jv.addProperty(WxUtil.knowMaster, knowMaster);
        //查询群服务器
       WXStringRequest srr = new WXStringRequest(GroupDesActivity.this, gurl + WxUtil.u_quitgroup, jv, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                JsonObject jo = null;
                Integer r = null;
                try {
                    jo = JSON.parseObject(res);
                    r = jo.getInteger(WxUtil.r);
                } catch (Exception e) {
                }

                if (r == null) {
                    Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_SHORT).show();
                } else if (RetNumUtilA.n_0 == r) {
                    Toast.makeText(GroupDesActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                } else if (RetNumUtilA.n_1 == r) {
                    TokenUtil.updateTo(app);
                    Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_SHORT).show();
                } else {//参数不正确
                    Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupDesActivity.this, R.string.fail_web, Toast.LENGTH_SHORT).show();
            }
        });
        app.getVolleyQeue().add(srr);*/
    }
}
