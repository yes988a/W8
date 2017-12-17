package com.w8;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.RetNumUtil;
import com.w8.base.WxUtil;
import com.w8.base.data.Friend;
import com.w8.base.data.FriendDao;
import com.w8.base.holder.FriList_head_holder;
import com.w8.base.holder.FriList_holder;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天转发。选择一个好友或者群界面。返回可能是群id(ret_gro_id)，或者好友id(ret_fri_id)
 */
public class ChatSearchFriActivity extends OnlineActivity {

    public static final String ret_fri_id = "l2a1c";

    private static int search_des = 71;  //简单描述
    private static int friend = 77; //无标题好友描述
    private static int friend_group = 88;//有abc描述
    private static int resultSFCSa = 3621;//传递使用

    private LinearLayoutManager lmac;
    private RecyclerView mRecycler_sfcsa;
    private List<Friend> datas;
    private ChatSearchFriActivity.AdapterSa adapterSa;
    private int switchnum;
    private boolean sort;//初始化时true，默认活跃度排序。  false是好友列表选择
    private String textChooce;

    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search_fri);
        TAG = ChatSearchFriActivity.class.getSimpleName();
        datas = new ArrayList<Friend>();//解决初始化时，null
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            result = intent.getIntExtra(AppUtil.RESULT, RetNumUtil.n_0);
            if (result == RetNumUtil.n_0) {
                finish();
            }
        }
        initData();
        adapterSa.notifyDataSetChanged();
    }

    private void initData() {
        textChooce = getString(R.string.search_des_choosefriend);
        switchnum = 2;// 用于显示：0选择一个群，1选择一个好友，。怎么做才能好看呢555
        sort = true;
        notif();
    }

    private void notif() {
        if (sort) {
            datas = MyApp.mC.getDS().getFriendDao().queryBuilder().where(FriendDao.Properties.Typ.eq(AppUtil.FRI_LIST_TYPE)).orderDesc(FriendDao.Properties.Degree).list();
        } else {
            datas = MyApp.mC.getDS().getFriendDao().queryBuilder().where(FriendDao.Properties.Typ.eq(AppUtil.FRI_LIST_TYPE)).orderAsc(FriendDao.Properties.Typ).orderAsc(FriendDao.Properties.Abc).orderAsc(FriendDao.Properties.Remark).list();
        }
    }

    private void initView() {
        mRecycler_sfcsa = (RecyclerView) findViewById(R.id.mRecycler_sfcsa);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        lmac = new LinearLayoutManager(this);
        adapterSa = new ChatSearchFriActivity.AdapterSa();
        mRecycler_sfcsa.setLayoutManager(lmac);
        mRecycler_sfcsa.setAdapter(adapterSa);
        mRecycler_sfcsa.setHasFixedSize(true);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatSearchFriActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultSFCSa && resultCode == resultSFCSa) {
            String uid = data.getStringExtra(ChatSearchGroActivity.ret_gro_id);
            if (uid != null) {
                Intent intent = new Intent();
                intent.putExtra(ChatSearchGroActivity.ret_gro_id, uid);
                setResult(result, intent);
                finish();
            }
        }
    }

    class AdapterSa extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == search_des) {//选择一个群
                return new ChatSearchFriActivity.AdapterSa.ViewHolderSearcgdes(LayoutInflater.from(ChatSearchFriActivity.this).inflate(R.layout.recycler_search_des, parent, false));
            } else if (viewType == friend) {
                return new FriList_holder(LayoutInflater.from(ChatSearchFriActivity.this).inflate(R.layout.recycler_friend, parent, false));
            } else {//显示的是好友列表
                return new FriList_head_holder(LayoutInflater.from(ChatSearchFriActivity.this).inflate(R.layout.recycler_friend_group, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
            if (position == RetNumUtil.n_0) {//选择一个群
                ViewHolderSearcgdes bhs = (ViewHolderSearcgdes) vh;
                bhs.sea_des_az_text.setText(getString(R.string.search_des_choosegroup));
                bhs.sea_des_az_view.setVisibility(View.GONE);
                bhs.sea_des_az_biglinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转到群列表
                        Intent intent = new Intent(ChatSearchFriActivity.this, ChatSearchGroActivity.class);
                        intent.putExtra(AppUtil.RESULT, resultSFCSa);
                        startActivityForResult(intent, resultSFCSa);
                    }
                });
            } else if (position == RetNumUtil.n_1) {//选择一个好友
                ViewHolderSearcgdes bhs = (ViewHolderSearcgdes) vh;
                bhs.sea_des_az_text.setText(textChooce);
                bhs.sea_des_az_biglinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //字体
                        if (sort) {
                            sort = false;
                            textChooce = getString(R.string.search_des_chooseactive);
                        } else {
                            sort = true;
                            textChooce = getString(R.string.search_des_choosefriend);
                        }
                        //切换数据
                        notif();
                        //更新列表
                        adapterSa.notifyDataSetChanged();
                    }
                });
            } else {//好友
                Friend f = datas.get(position - switchnum);
                if (sort) {
                    FriList_holder mvh = (FriList_holder) vh;
                    setCommonHolder(mvh, f.getFid(), f.getRemark());
                } else {
                    if (RetNumUtil.n_1 == f.getShowabc()) {
                        FriList_head_holder mvh = (FriList_head_holder) vh;
                        mvh.fr_head_abc.setText(f.getAbc());
                        setCommonHolder(mvh, f.getFid(), f.getRemark());
                    } else {
                        FriList_holder mvh = (FriList_holder) vh;
                        setCommonHolder(mvh, f.getFid(), f.getRemark());
                    }
                }
            }
        }

        //适配器，公用设值。
        private void setCommonHolder(FriList_holder m, final String rid, final String remark) {

//            Picasso.with(FriendActivity.this).load(FriendActivity.this.getFileStreamPath("")).into(m.fr_imagehead);

            Picasso.with(ChatSearchFriActivity.this).load(ChatSearchFriActivity.this.getFileStreamPath("")).into(m.fr_imagehead, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
            m.fr_rid.setText(rid);
            m.fr_uname.setText(remark);
            m.fr_biglinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ChatSearchFriActivity.this)
                            .setTitle(R.string.alert_choose_tittle)
                            .setMessage(remark)
                            .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra(ret_fri_id, rid);
                                    setResult(result, intent);
                                    finish();
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
        }


        @Override
        public int getItemCount() {
            return datas.size() + switchnum;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == RetNumUtil.n_0 || position == RetNumUtil.n_1) {//选择一个群 或者 选择一个好友
                return search_des;
            } else if (sort || RetNumUtil.n_1 != datas.get(position - switchnum).getShowabc()) {
                return friend;
            } else {//显示的是好友列表
                return friend_group;
            }
        }

        class ViewHolderSearcgdes extends RecyclerView.ViewHolder {
            LinearLayout sea_des_az_biglinear;
            TextView sea_des_az_text;
            View sea_des_az_view;

            public ViewHolderSearcgdes(View view) {
                super(view);
                sea_des_az_biglinear = (LinearLayout) view.findViewById(R.id.sea_des_az_biglinear);
                sea_des_az_text = (TextView) view.findViewById(R.id.sea_des_az_text);
                sea_des_az_view = view.findViewById(R.id.sea_des_az_view);
            }
        }
    }
}