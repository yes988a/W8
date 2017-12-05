package com.w8;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.data.GroupFriend;
import com.w8.base.data.GroupFriendDao;
import com.w8.base.pcurl.GroupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 群用户列表
 */
public class GroupusersActivity extends OnlineActivity {
    private static int resultSele = 3637;//传递使用，跳转到选择好友。

    private boolean checkBoxShow = false;//编辑状态？
    private String gid;//群id
    private String gurl;//群地址
    private int gunum;//群人数
    private Boolean isMaster;//我是不是管理员，用于群人员的menu显示。


    private RecyclerView groupuser_recycler;
    private List<GroupFriend> datas;//所有成员集合
    private List<String> fids;//倍选择要剔除的人
    private GroupusersActivity.Adapter_gs adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupusers);
        TAG = GroupusersActivity.class.getSimpleName();
        datas = new ArrayList<GroupFriend>();//解决初始化时，null
        fids = new ArrayList<String>();//解决初始化时，null
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkBoxShow = false;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            this.finish();
        } else {
           /* gid = bundle.getString(WxUtil.gid);
            gurl = bundle.getString(WxUtil.url);
            gunum = bundle.getInt(WxUtil.num);
            isMaster = bundle.getBoolean(WxUtil.knowMaster);
            if (isMaster == null || gunum == 0 || gurl == null || "".equals(gurl) || gid == null || "".equals(gid)) {
                normalStart = false;
                this.finish();
            } else {
                initData();
            }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMaster) {
            getMenuInflater().inflate(R.menu.menu_groupuser_master, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_groupuser_user, menu);
        }
        return true;
    }

    private void initData() {
        fids = new ArrayList<String>();//解决初始化时，null
        datas = MyApp.mC.getDS().getGroupFriendDao().queryBuilder().where(GroupFriendDao.Properties.Gid.eq(gid)).list();
        if (datas.size() == gunum) {
            adapter.notifyDataSetChanged();
        } else {
            getWebGroupUser();
        }
    }

    private void initView() {
        groupuser_recycler = (RecyclerView) findViewById(R.id.groupuser_recycler);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        adapter = new GroupusersActivity.Adapter_gs();
        groupuser_recycler.setLayoutManager(new GridLayoutManager(this, 6));
        groupuser_recycler.setAdapter(adapter);
        groupuser_recycler.setHasFixedSize(true);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isMaster) {//管理员
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.groupuser_master_add://添加群员，邀请好友
                           /* Intent intent = new Intent(GroupusersActivity.this, GroupSelectFriActivity.class);
                            intent.putExtra(WxUtil.tag_position, TAG);
                            intent.putExtra(WxUtil.url, gurl);
                            intent.putExtra(WxUtil.gid, gid);
                            startActivity(intent);
                            finish();
                            break;*/
                        case R.id.groupuser_master_remove://移除群员view修改，管理员操作
                            toolbar.getMenu().findItem(R.id.groupuser_master_dele).setVisible(true);
                            toolbar.getMenu().findItem(R.id.groupuser_master_add).setVisible(false);
                            toolbar.getMenu().findItem(R.id.groupuser_master_remove).setVisible(false);
                            checkBoxShow = true;
                            adapter.notifyDataSetChanged();
                            break;
                        case R.id.groupuser_master_dele://完成移除
                            toolbar.getMenu().findItem(R.id.groupuser_master_dele).setVisible(false);
                            toolbar.getMenu().findItem(R.id.groupuser_master_add).setVisible(true);
                            toolbar.getMenu().findItem(R.id.groupuser_master_remove).setVisible(true);
                            completeDele();
                            checkBoxShow = false;
                            adapter.notifyDataSetChanged();
                            break;
                    }
                    return false;
                }
            });
        } else {//普通群员
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.groupuser_user_add://添加群员，邀请好友
                           /* Intent intent = new Intent(GroupusersActivity.this, GroupSelectFriActivity.class);
                            intent.putExtra(WxUtil.tag_position, TAG);
                            intent.putExtra(WxUtil.url, gurl);
                            intent.putExtra(WxUtil.gid, gid);
                            startActivity(intent);
                            finish();*/
                            break;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 通过网络获取，群用户信息，（群服务器使用)u_getgroupusers
     */
    private void getWebGroupUser() {

        JsonObject jo = new JsonObject();
        jo.addProperty(GroupUtil.para_gid, gid);
        /*WXStringRequest wxr = new WXStringRequest(GroupusersActivity.this, gurl + WxUtil.u_getgroupusers, jo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jjj = null;
                Integer r = null;
                try {
                    jjj = JSON.parseObject(response);
                    r = jjj.getInteger(WxUtil.r);
                } catch (Exception e) {
                    mwErr(TAG, "u_getgroupusers,onResponse", e);
                }
                if (r == null) {
// 日志
                } else {
                    if (RetNumUtil.n_0 == r) {
                        List<Guser> gus = null;
                        try {
                            gus = JSON.parseArray(jjj.getString(WxUtil.groupusers), Guser.class);
                        } catch (Exception e) {
                            mwErr(TAG, "u_getgroupusers,gus", e);
                        }
                        if (gus == null) {
// 日志
                            Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                        } else {
                            List<GroupFriend> list = MyApp.mC.getDS().getGroupFriendDao().queryBuilder().where(GroupFriendDao.Properties.Gid.eq(gid)).list();
                            List<GroupFriend> newlist = new ArrayList<GroupFriend>();
                            for (int i = gus.size() - 1; i >= 0; i--) {
                                Guser grr = gus.get(i);
                                if (grr.getBid() != null && grr.getUaccount() != null && grr.getUid() != null && grr.getUremark() != null) {
                                    GroupFriend gf = new GroupFriend();
                                    gf.setGid(gid);
                                    gf.setBid(grr.getBid());
                                    gf.setUaccount(grr.getUaccount());
                                    gf.setUid(grr.getUid());
                                    gf.setUremark(grr.getUremark());
                                    newlist.add(gf);
                                }
                            }
                            MyApp.mC.getDS().getGroupFriendDao().deleteInTx(list);
                            MyApp.mC.getDS().getGroupFriendDao().insertInTx(newlist);
                            if (newlist.size() != gunum) {
                                // 日志
                                Group ggg = MyApp.mC.getDS().getGroupDao().queryBuilder().where(GroupDao.Properties.Gid.eq(gid)).unique();
                                if (ggg == null) {
                                    // 日志
                                } else {
                                    ggg.setUsernum(newlist.size());
                                    MyApp.mC.getDS().getGroupDao().update(ggg);
                                }
                            }
                        }
                    } else if (RetNumUtil.n_1 == r) {
                        TokenUtil.updateTo(app);
                    } else {
                        Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                    }
                    datas = MyApp.mC.getDS().getGroupFriendDao().queryBuilder().where(GroupFriendDao.Properties.Gid.eq(gid)).list();
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
            }
        });
        app.getVolleyQeue().add(wxr);
*/
    }

    /**
     * 完成移除群成员(管理员操作)   u_gooutgroup
     */
    private void completeDele() {
        JsonObject jo = new JsonObject();
       /* jo.addProperty(WxUtil.gid, gid);
        jo.addProperty(WxUtil.fids, new Gson().toJson(fids));
        WXStringRequest wxr = new WXStringRequest(GroupusersActivity.this, gurl + WxUtil.u_gooutgroup, jo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jw = null;
                Integer r = null;
                try {//1token过期，0成功，2不是群管理员，3参数错误或者服务器错误
                    jw = JSON.parseObject(response);
                    r = jw.getInteger(WxUtil.r);
                } catch (Exception e) {
                    mwErr(TAG, "u_gooutgroup,onResponse", e);
                }

                if (r == null) {
                    Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                } else {
                    if (RetNumUtil.n_0 == r) {
                        //通知所有群员所有更新群信息
                    } else if (RetNumUtil.n_1 == r) {
                        TokenUtil.updateTo(app);
                        Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                    } else if (RetNumUtil.n_15 == r) {
                        // 优化，前面已经判断是否群主啦，出现此问题，防止攻击，和判断交换群主时是否正确。
                        Toast.makeText(GroupusersActivity.this, R.string.group_des_update_err_m, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                    }
                    datas = MyApp.mC.getDS().getGroupFriendDao().queryBuilder().where(GroupFriendDao.Properties.Gid.eq(gid)).list();
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupusersActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
            }
        });
        app.getVolleyQeue().add(wxr);*/
    }

    class Adapter_gs extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupusersActivity.Adapter_gs.VH_gs(LayoutInflater.from(GroupusersActivity.this).inflate(R.layout.recycler_groupuser, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
            GroupusersActivity.Adapter_gs.VH_gs vh = (GroupusersActivity.Adapter_gs.VH_gs) holder;
            GroupFriend sss = datas.get(pos);
            final String fid = sss.getUid();
            vh.groupuser_fid.setText(fid);
            vh.groupuser_nickname.setText(sss.getUremark());
            vh.groupuser_head_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            vh.groupuser_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fids.contains(fid)) {
                        fids.remove(fid);
                    } else {
                        fids.add(fid);
                    }
                }
            });
            if (checkBoxShow) {
                vh.groupuser_checkbox.setVisibility(View.VISIBLE);
            } else {
                vh.groupuser_checkbox.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class VH_gs extends RecyclerView.ViewHolder {
            CheckBox groupuser_checkbox;
            ImageView groupuser_head_img;
            TextView groupuser_nickname;
            TextView groupuser_fid;

            public VH_gs(View itemView) {
                super(itemView);
                groupuser_checkbox = (CheckBox) itemView.findViewById(R.id.groupuser_checkbox);
                groupuser_head_img = (ImageView) itemView.findViewById(R.id.groupuser_head_img);
                groupuser_nickname = (TextView) itemView.findViewById(R.id.groupuser_nickname);
                groupuser_fid = (TextView) itemView.findViewById(R.id.groupuser_fid);
            }
        }
    }
}
