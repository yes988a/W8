package com.w8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.data.Active;
import com.w8.base.data.ActiveDao;
import com.w8.base.event.Refresh_active;
import com.w8.base.pcurl.FriendUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动列表，包括聊天、消息通知等
 */
public class ActiveActivity extends OnlineActivity {

    private RecyclerView mRecyclerView_ac;
    private List<Active> datas_ac;
    private Adapter_ac adapter_ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        TAG = ActiveActivity.class.getSimpleName();
        datas_ac = new ArrayList<Active>();//解决初始化时，null
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        adapter_ac.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    private void initView() {
        mRecyclerView_ac = (RecyclerView) findViewById(R.id.mRecyclerView_ac);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_switch);
        setSupportActionBar(toolbar);
        adapter_ac = new Adapter_ac();
        mRecyclerView_ac.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView_ac.setAdapter(adapter_ac);
        mRecyclerView_ac.setHasFixedSize(true);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActiveActivity.this, FriendActivity.class);
                startActivity(intent);
                finish();
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
                        Intent intentAAA = new Intent(ActiveActivity.this, FriSearchActivity.class);//新朋友
                        startActivity(intentAAA);
                        break;
                    case R.id.fri_setting:
                        Intent intentBB = new Intent(ActiveActivity.this, MineSetingActivity.class);//个人详情和设置
                        startActivity(intentBB);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 正常状态下，有新消息后更新active列表
     */
    protected void initData() {
        datas_ac = MyApp.mC.getDS().getActiveDao().queryBuilder().orderDesc(ActiveDao.Properties.Tim).list();
    }

    class Adapter_ac extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == AppUtil.active_chatsingle || viewType == AppUtil.active_chatgroup) {
                return new MyViewHolder(LayoutInflater.from(ActiveActivity.this).inflate(R.layout.recycler_active, parent, false));
            } else if (viewType == AppUtil.n_typ_frireq) {//好友请求显示，当同意后，不在active显示啦就，直接是好友啦，忽略后也不现实啦
                return new MyViewHolder_friReqrequest(LayoutInflater.from(ActiveActivity.this).inflate(R.layout.recycler_frireq_request, parent, false));
            } else {
                //错误数据显示用户手册
                return new ViewHolderInstructions(LayoutInflater.from(ActiveActivity.this).inflate(R.layout.recycler_instructions, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
            int viewType = h.getItemViewType();
            if (viewType == AppUtil.active_chatsingle || viewType == AppUtil.active_chatgroup) {
                MyViewHolder ho = (MyViewHolder) h;
                Active f = datas_ac.get(position);
                final String rid = f.getUuid();
                ho.rid.setText(rid);
                ho.ac_uname.setText(f.getTitle());
                ho.ac_endtime.setText(f.getTimstr());
                ho.ac_enddes.setText(f.getDes());
                if (viewType == AppUtil.active_chatsingle) {
                    ho.ac_lineBig.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtil.toChat(rid, false);
                        }
                    });
                } else if (viewType == AppUtil.active_chatgroup) {
                    ho.ac_lineBig.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtil.toChat(rid, true);
                        }
                    });
                } else {
                    //错误信息待纠正
                }
            } else if (viewType == AppUtil.n_typ_frireq) {
                MyViewHolder_friReqrequest ho = (MyViewHolder_friReqrequest) h;
                Active f = datas_ac.get(position);
                final String rid = f.getUuid();
                ho.rid.setText(rid);
                ho.friReq_uname.setText(f.getTitle());
                ho.friReq_endtime.setText(f.getTimstr());
                ho.friReq_enddes.setText(f.getDes());
                ho.friReq_lineBig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ActiveActivity.this, FriResActivity.class);
                        intent.putExtra(FriendUtil.para_reqid, rid);
                        ActiveActivity.this.startActivity(intent);

                    }
                });
            } else {
                ViewHolderInstructions vh = (ViewHolderInstructions) h;
                vh.instructions_lineBig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "欢迎使用");
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return datas_ac.size();
        }

        @Override
        public int getItemViewType(int position) {
            int typ = datas_ac.get(position).getType();
            if (typ == AppUtil.n_typ_frireq || typ == AppUtil.active_chatgroup || typ == AppUtil.active_chatsingle) {
                return typ;
            } else {
                return AppUtil.nodata_erro;
            }
        }

        class MyViewHolder extends ViewHolderParent {
            ImageView ac_imghead;
            TextView ac_uname;
            TextView ac_endtime;
            TextView ac_enddes;
            LinearLayout ac_lineBig;

            public MyViewHolder(View view) {
                super(view);
                rid = (TextView) view.findViewById(R.id.ac_rid);
                ac_imghead = (ImageView) view.findViewById(R.id.ac_imghead);
                ac_uname = (TextView) view.findViewById(R.id.ac_uname);
                ac_endtime = (TextView) view.findViewById(R.id.ac_endtime);
                ac_enddes = (TextView) view.findViewById(R.id.ac_enddes);
                ac_lineBig = (LinearLayout) view.findViewById(R.id.ac_lineBig);
            }
        }

        class MyViewHolder_friReqrequest extends ViewHolderParent {
            ImageView friReq_imghead;
            TextView friReq_uname;
            TextView friReq_endtime;
            TextView friReq_enddes;
            LinearLayout friReq_lineBig;

            public MyViewHolder_friReqrequest(View view) {
                super(view);
                rid = (TextView) view.findViewById(R.id.friReq_rid);
                friReq_imghead = (ImageView) view.findViewById(R.id.friReq_imghead);
                friReq_uname = (TextView) view.findViewById(R.id.friReq_uname);
                friReq_endtime = (TextView) view.findViewById(R.id.friReq_endtime);
                friReq_enddes = (TextView) view.findViewById(R.id.friReq_enddes);
                friReq_lineBig = (LinearLayout) view.findViewById(R.id.friReq_lineBig);
            }
        }

        class ViewHolderParent extends RecyclerView.ViewHolder {
            TextView rid;

            public ViewHolderParent(View view) {
                super(view);
            }
        }

        class ViewHolderInstructions extends RecyclerView.ViewHolder {
            LinearLayout instructions_lineBig;

            public ViewHolderInstructions(View view) {
                super(view);
                instructions_lineBig = (LinearLayout) view.findViewById(R.id.instructions_lineBig);
            }
        }
    }

    //刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void c(Refresh_active r) {
        initData();
        adapter_ac.notifyDataSetChanged();
    }
}
