package com.w8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.pcurl.RetNumUtilA;
import com.w8.base.data.Friend;
import com.w8.base.data.FriendDao;
import com.w8.base.data.GroupDao;
import com.w8.base.pcurl.FriendUtilA;
import com.w8.base.pcurl.GroupUtilA;

import java.util.ArrayList;
import java.util.List;

/**
 * 新建群聊，选择好友
 */
public class GroupSelectFriActivity extends OnlineActivity {
    public static int FRI_LIST_HEAD = 61;//显示abc头
    public static int FRI_LIST_NO_HEAD = 62;//不显示abc头
    private RecyclerView mRecycler_selectfri;
    private LinearLayoutManager lmac;
    private List<Friend> datas;
    private GroupSelectFriActivity.AdapterSf adapter;
    private List<String> ids;//别选择的人员ID
    private String gid;//群添加人员时的群真正地址
    private String tagfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select_fri);
        TAG = GroupSelectFriActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent == null || intent.getStringExtra(AppUtil.tag_position) == null) {
        } else {
            tagfrom = intent.getStringExtra(AppUtil.tag_position);
            if (GroupusersActivity.class.getSimpleName().equals(tagfrom)) {//邀请好友入群
                gid = intent.getStringExtra(GroupUtilA.para_gid);
                if (gid == null) {
                    // 日志
                }
            } else { //新建群。
                long gnum = MyApp.mC.getDS().getGroupDao().queryBuilder().where(GroupDao.Properties.Masterid.eq(AppUtil.getUid())).count();
                if (gnum > RetNumUtilA.n_7) {
                    new AlertDialog.Builder(GroupSelectFriActivity.this)
                            .setTitle(R.string.group_num_err_tittle)
                            .setMessage(R.string.group_num_err_des)
                            .setPositiveButton(R.string.alert_choose_know, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GroupSelectFriActivity.this.finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selectfriend, menu);
        return true;
    }

    private void initView() {
        mRecycler_selectfri = (RecyclerView) findViewById(R.id.mRecycler_selectfri);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        lmac = new LinearLayoutManager(this);
        adapter = new GroupSelectFriActivity.AdapterSf();
        mRecycler_selectfri.setLayoutManager(lmac);
        mRecycler_selectfri.setAdapter(adapter);
        mRecycler_selectfri.setHasFixedSize(true);
    }

    private void initData() {
        ids = new ArrayList<String>();
        datas = MyApp.mC.getDS().getFriendDao().queryBuilder().where(FriendDao.Properties.Typ.eq(AppUtil.FRI_LIST_TYPE)).orderAsc(FriendDao.Properties.Typ).orderAsc(FriendDao.Properties.Abc).orderAsc(FriendDao.Properties.Remark).list();
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupSelectFriActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.sfri_complete:

                        if (ids == null || ids.size() == 0) {  //新政群选择人员
                            Toast.makeText(GroupSelectFriActivity.this, R.string.group_select_null, Toast.LENGTH_LONG).show();
                        } else {
                            if (tagfrom.equals(GroupActivity.class.getSimpleName())) {
                                Intent intent = new Intent(GroupSelectFriActivity.this, GroupCompleteActivity.class);
                                intent.putExtra(FriendUtilA.para_fids, new Gson().toJson(ids));
                                startActivity(intent);
                                GroupSelectFriActivity.this.finish();
                            } else if (tagfrom.equals(GroupusersActivity.class.getSimpleName())) { //群添加人员，邀请人员
//                                addPersonToGroup();//发送邀请好友请求
                            } else {//意外情况

                            }
                        }
                }
                return false;
            }
        });
    }

    class AdapterSf extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (FRI_LIST_HEAD == viewType) {// 显示头：abcd
                return new MyViewHolder_head(LayoutInflater.from(GroupSelectFriActivity.this).inflate(R.layout.recycler_friend_group_select, parent, false));
            } else {//不显示头,即好友正文
                return new MyViewHolder(LayoutInflater.from(GroupSelectFriActivity.this).inflate(R.layout.recycler_friend_select, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Friend f = datas.get(position);
            if (1 == f.getShowabc()) {// 显示头：abcd
                MyViewHolder_head mh = (MyViewHolder_head) holder;
                final String rid = f.getFid();
                mh.fr_head_sele_rid.setText(rid);
                mh.fr_head_sele_abc.setText(f.getAbc());
                mh.fr_head_sele_uname.setText(f.getRemark());
                Picasso.with(GroupSelectFriActivity.this).load(GroupSelectFriActivity.this.getFileStreamPath("")).into(mh.fr_head_sele_imagehead);
                mh.fr_head_sele_checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ids.contains(rid)) {
                            ids.remove(rid);
                        } else {
                            ids.add(rid);
                        }
                    }
                });
            } else {//不显示头,即好友正文
                MyViewHolder m = (MyViewHolder) holder;
                final String rid = f.getFid();
                m.fr_sele_rid.setText(rid);
                m.fr_sele_uname.setText(f.getRemark());
                Picasso.with(GroupSelectFriActivity.this).load(GroupSelectFriActivity.this.getFileStreamPath("")).into(m.fr_sele_imagehead, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
                m.fr_sele_checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ids.contains(rid)) {
                            ids.remove(rid);
                        } else {
                            ids.add(rid);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (1 == datas.get(position).getShowabc()) {
                return FRI_LIST_HEAD;
            } else {
                return FRI_LIST_NO_HEAD;
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder_head extends RecyclerView.ViewHolder {
            LinearLayout fr_head_sele_biglinear;
            TextView fr_head_sele_abc;
            TextView fr_head_sele_rid;
            ImageView fr_head_sele_imagehead;
            TextView fr_head_sele_uname;
            CheckBox fr_head_sele_checkbox;

            public MyViewHolder_head(View view) {
                super(view);
                fr_head_sele_biglinear = (LinearLayout) view.findViewById(R.id.fr_head_sele_biglinear);
                fr_head_sele_abc = (TextView) view.findViewById(R.id.fr_head_sele_abc);
                fr_head_sele_rid = (TextView) view.findViewById(R.id.fr_head_sele_rid);
                fr_head_sele_imagehead = (ImageView) view.findViewById(R.id.fr_head_sele_imagehead);
                fr_head_sele_uname = (TextView) view.findViewById(R.id.fr_head_sele_uname);
                fr_head_sele_checkbox = (CheckBox) view.findViewById(R.id.fr_head_sele_checkbox);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout fr_sele_biglinear;
            TextView fr_sele_rid;
            ImageView fr_sele_imagehead;
            TextView fr_sele_uname;
            CheckBox fr_sele_checkbox;

            public MyViewHolder(View view) {
                super(view);
                fr_sele_biglinear = (LinearLayout) view.findViewById(R.id.fr_sele_biglinear);
                fr_sele_rid = (TextView) view.findViewById(R.id.fr_sele_rid);
                fr_sele_imagehead = (ImageView) view.findViewById(R.id.fr_sele_imagehead);
                fr_sele_uname = (TextView) view.findViewById(R.id.fr_sele_uname);
                fr_sele_checkbox = (CheckBox) view.findViewById(R.id.fr_sele_checkbox);
            }
        }
    }

  /*  //发送,好友入群请求
    private void addPersonToGroup() {
        JSONObject jo = new JSONObject();
        jo.put(WxUtil.gid, gid);
        jo.put(WxUtil.fids, JSON.toJSONString(ids));
        WXStringRequest wxr = new WXStringRequest(GroupSelectFriActivity.this, gurl + WxUtil.u_invitegroupuser, jo, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                JSONObject jjj = null;
                try {
                    jjj = JSON.parseObject(res);
                } catch (Exception e) {
                    mwErr(TAG, "u_invitegroupuser,", e);
                }
                if (jjj == null || jjj.getInteger(WxUtil.r) == null) {

                } else {
                    Integer rrr = jjj.getInteger(WxUtil.r);

                    if (RetNumUtilA.n_0 == rrr) {//0成功
                        Toast.makeText(GroupSelectFriActivity.this, R.string.success, Toast.LENGTH_SHORT);
                        GroupSelectFriActivity.this.finish();
                    } else if (RetNumUtilA.n_1 == rrr) {//11token过期
                        TokenUtil.updateTo(app);
                        Toast.makeText(GroupSelectFriActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                    } else {//2参数错误或者服务器
                        Toast.makeText(GroupSelectFriActivity.this, R.string.fail_web, Toast.LENGTH_SHORT);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        app.getVolleyQeue().add(wxr);
    }
*/
}
