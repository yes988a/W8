package com.w8;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.data.Friend;
import com.w8.base.data.FriendDao;
import com.w8.base.holder.FriList_head_holder;
import com.w8.base.holder.FriList_holder;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 通信好友列表
 */
public class FriendActivity extends OnlineActivity {

    public static int FRI_LIST_HEAD = 61;//显示abc头
    public static int FRI_LIST_NO_HEAD = 62;//不显示abc头

    private static int topnum = 2;//群聊，标签。个数

    private LinearLayoutManager lmfr;
    private RecyclerView mRecyclerView_fr;
    private Adapter_fr adapter_fr;
    private List<Friend> datas_fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        TAG = FriendActivity.class.getSimpleName();
        //必要个人信息判断
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    private void initData() {
        initData_fr();
        mRecyclerView_fr.setLayoutManager(lmfr);
        adapter_fr = new Adapter_fr();
        mRecyclerView_fr.setAdapter(adapter_fr);
        mRecyclerView_fr.setHasFixedSize(true);
    }

    protected void initData_fr() {
        QueryBuilder<Friend> builder_f = MyApp.mC.getDS().getFriendDao().queryBuilder();
        datas_fr = builder_f.orderAsc(FriendDao.Properties.Typ).orderAsc(FriendDao.Properties.Abc)
                .orderAsc(FriendDao.Properties.Remark).list();
    }

    class Adapter_fr extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (FRI_LIST_HEAD == viewType) {// 显示头：abcd
                return new FriList_head_holder(LayoutInflater.from(FriendActivity.this).inflate(R.layout.recycler_friend_group, parent, false));
            } else if (FRI_LIST_NO_HEAD == viewType) {//不显示头,即好友正文
                return new FriList_holder(LayoutInflater.from(FriendActivity.this).inflate(R.layout.recycler_friend, parent, false));
            } else if (AppUtil.FRI_LIST_SYS_GROUCHAT == viewType) {//42群聊
                return new SysFriGsViewHolder(LayoutInflater.from(FriendActivity.this).inflate(R.layout.recycler_friend_gs, parent, false));
            } else if (AppUtil.FRI_LIST_SYS_LABEL_ADD == viewType) {//43标签添加
                return new SysFriLaelNewViewHolder(LayoutInflater.from(FriendActivity.this).inflate(R.layout.recycler_friend_labelnew, parent, false));
            } else if (AppUtil.FRI_LIST_SYS_LABEL == viewType) {//50标签具体显示
                return new SysLabelViewHolder(LayoutInflater.from(FriendActivity.this).inflate(R.layout.recycler_friend_labels, parent, false));
            } else {
                //什么也不显示
                return new NullFriViewHolder(LayoutInflater.from(FriendActivity.this).inflate(R.layout.common_null, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            int viewType = holder.getItemViewType();

            if (FRI_LIST_HEAD == viewType) {// 显示头：abcd
                Friend f = datas_fr.get(position - topnum);
                FriList_head_holder mh = (FriList_head_holder) holder;
                mh.fr_head_abc.setText(f.getAbc());

                setCommonHolder(mh, f.getFid(), f.getRemark());

            } else if (FRI_LIST_NO_HEAD == viewType) {//不显示头,即好友正文
                Friend f = datas_fr.get(position - topnum);
                FriList_holder m = (FriList_holder) holder;

                setCommonHolder(m, f.getFid(), f.getRemark());

            } else if (AppUtil.FRI_LIST_SYS_GROUCHAT == viewType) {//42群聊
                SysFriGsViewHolder m = (SysFriGsViewHolder) holder;
                m.fri_gs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAct(null, GroupActivity.class);
                    }
                });
            } else if (AppUtil.FRI_LIST_SYS_LABEL_ADD == viewType) {//43标签添加
                SysFriLaelNewViewHolder m = (SysFriLaelNewViewHolder) holder;
                m.fri_laelNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "新标");
                    }
                });
            } else if (AppUtil.FRI_LIST_SYS_LABEL == viewType) {//50标签具体显示
                SysLabelViewHolder m = (SysLabelViewHolder) holder;
                m.fr_label_id.setText(datas_fr.get(position - topnum).getFid());
                m.fri_laelone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "（标签：朋友s）");
                    }
                });
            } else {
                //什么也不显示
            }
        }

        //适配器，公用设值。
        private void setCommonHolder(FriList_holder m, final String rid, String remark) {

//            Picasso.with(FriendActivity.this).load(FriendActivity.this.getFileStreamPath("")).into(m.fr_imagehead);

            Picasso.with(FriendActivity.this).load(FriendActivity.this.getFileStreamPath("")).into(m.fr_imagehead, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });

            m.fr_rid.setText(rid);
            m.fr_uname.setText(remark);
            m.fr_imagehead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.toFriDes(rid);
                }
            });
            m.fr_uname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.toChat(rid,true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas_fr.size() + topnum;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {//群聊
                return AppUtil.FRI_LIST_SYS_GROUCHAT;
            } else if (position == 1) {//标签
                return AppUtil.FRI_LIST_SYS_LABEL_ADD;
            } else if (AppUtil.FRI_LIST_TYPE == datas_fr.get(position - topnum).getTyp()) {
                if (1 == datas_fr.get(position - topnum).getShowabc()) {
                    return FRI_LIST_HEAD;
                } else {
                    return FRI_LIST_NO_HEAD;
                }
            } else {
                return datas_fr.get(position - topnum).getTyp();
            }
        }

        class SysLabelViewHolder extends RecyclerView.ViewHolder {
            TextView fr_label_id;
            LinearLayout fri_laelone;

            public SysLabelViewHolder(View view) {
                super(view);
                fr_label_id = (TextView) view.findViewById(R.id.fr_label_id);
                fri_laelone = (LinearLayout) view.findViewById(R.id.fri_laelone);

            }
        }

        class SysFriGsViewHolder extends RecyclerView.ViewHolder {
            LinearLayout fri_gs;

            public SysFriGsViewHolder(View view) {
                super(view);
                fri_gs = (LinearLayout) view.findViewById(R.id.fri_gs);
            }
        }


        class SysFriLaelNewViewHolder extends RecyclerView.ViewHolder {
            LinearLayout fri_laelNew;

            public SysFriLaelNewViewHolder(View view) {
                super(view);
                fri_laelNew = (LinearLayout) view.findViewById(R.id.fri_laelNew);
            }
        }

        class NullFriViewHolder extends RecyclerView.ViewHolder {

            public NullFriViewHolder(View view) {
                super(view);
            }
        }
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(FriendActivity.this, ActiveActivity.class);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.fri_scan:
                        //扫一扫
                        break;
                    case R.id.fri_addfri:
                        startAct(null, FriSearchActivity.class);//新朋友
                        break;
                    case R.id.fri_setting:
                        startAct(null, MineSetingActivity.class);//个人详情和设置
                        break;
                }
                return false;
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_switch);
        setSupportActionBar(toolbar);
        mRecyclerView_fr = (RecyclerView) findViewById(R.id.mRecyclerView_fr);
        lmfr = new LinearLayoutManager(this);
    }

}
