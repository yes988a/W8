package com.w8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.w8.base.OnlineActivity;
import com.w8.base.pcurl.RetNumUtilA;
import com.w8.base.pcurl.FriendUtilA;

import java.util.List;

/**
 * 完成新增群。选择群头像，填写群名称，配置群初始化设置
 */
public class GroupCompleteActivity extends OnlineActivity {

    private List<String> ids;
    private Button addgroup_submit;
    private TextView addgroup_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_complete);
        TAG = GroupCompleteActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        addgroup_submit = (Button) findViewById(R.id.addgroup_submit);
        addgroup_name = (TextView) findViewById(R.id.addgroup_name);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null || intent.getStringExtra(FriendUtilA.para_fids) == null) {
            // 日志
            GroupCompleteActivity.this.finish();
        } else {
            ids = null;
            try {
                ids = new Gson().fromJson(intent.getStringExtra(FriendUtilA.para_fids),new TypeToken<List<String>>() {
                }.getType());
            } catch (Exception e) {
                mwErr(TAG, "initData,", e);
            }
            if (ids == null || ids.size() == RetNumUtilA.n_0) {
                // 日志
                GroupCompleteActivity.this.finish();
            }
        }
    }




    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupCompleteActivity.this.finish();
            }
        });


        /*

        addgroup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gname = addgroup_name.getText().toString();
                Jsono jo = new JSONObject();
                jo.put(WxUtil.gname, gname);
                jo.put(WxUtil.fids, JSON.toJSONString(ids));
                WXStringRequest wxr = new WXStringRequest(GroupCompleteActivity.this, AppUtil.getBurl(GroupCompleteActivity.this) + WxUtil.u_addgroup, jo, new Downloader.Response.Listener<String>() {
                    @Override
                    public void onResponse(String ress) {
                        JSONObject jjj = null;
                        Integer rr = null;
                        try {
                            jjj = JSON.parseObject(ress);
                            rr = jjj.getInteger(WxUtil.r);
                        } catch (Exception e) {
                            mwErr(TAG, "u_addgroup,", e);
                        }
                        if (rr == null) {
                            // 错误，优化，日志
                        } else {
                            if (rr == RetNumUtilA.n_0) {
                                Ugroup sue = null;
                                try {
                                    sue = JSON.parseObject(jjj.getString("ug"), Ugroup.class);
                                } catch (Exception e) {
                                    mwErr(TAG, "egrdgnhtj,", e);
                                }
                                // 日志// 优化
                                if (sue == null) {//启动service更新群列表吧
                                    Intent iser = new Intent(GroupCompleteActivity.this, UpdateGroupService.class);
                                    startService(iser);
                                    Toast.makeText(GroupCompleteActivity.this, R.string.fail, Toast.LENGTH_SHORT);
                                    GroupCompleteActivity.this.finish();
                                } else {
                                    Group gg = new Group();
                                    gg.setGid(sue.getGid());
                                    gg.setShie(sue.getShie());
                                    gg.setUsernum(0);
                                    gg.setBid(sue.getBid());
                                    gg.setNickname(sue.getNickname());
                                    gg.setGremark(sue.getGremark());
                                    app.getDaoSession().getGroupDao().insert(gg);
                                    Toast.makeText(GroupCompleteActivity.this, R.string.success, Toast.LENGTH_SHORT);
                                    GroupCompleteActivity.this.finish();
                                }
                            } else if (rr == RetNumUtilA.n_19) {
//每个人只能创建。xx个群。
                                Intent inn = new Intent(GroupCompleteActivity.this, ErrcheckService.class);
                                startService(inn);

                                new AlertDialog.Builder(GroupCompleteActivity.this)
                                        .setTitle(R.string.group_num_err_tittle)
                                        .setMessage(R.string.group_num_err_des)
                                        .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                //日志，优化，错误
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GroupCompleteActivity.this, R.string.fail, Toast.LENGTH_SHORT);
                    }
                });
                app.getVolleyQeue().add(wxr);
            }
        });
*/

    }
}
