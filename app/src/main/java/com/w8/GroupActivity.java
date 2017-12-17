package com.w8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.WxUtil;
import com.w8.base.data.Group;
import com.w8.base.data.GroupDao;
import com.w8.base.pcurl.GroupUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 我的群列表
 */
public class GroupActivity extends OnlineActivity {

    private LinearLayoutManager lmfr;
    private RecyclerView mRecycler_group;
    private List<Group> datas_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        TAG = GroupActivity.class.getSimpleName();
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
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        mRecycler_group = (RecyclerView) findViewById(R.id.mRecycler_group);
        lmfr = new LinearLayoutManager(this);
    }

    private void initData() {
        QueryBuilder<Group> builder_f = MyApp.mC.getDS().getGroupDao().queryBuilder();
        datas_group = builder_f.orderAsc(GroupDao.Properties.Gname).list();
        mRecycler_group.setLayoutManager(lmfr);
        mRecycler_group.setAdapter(new Adapter_gr());
        mRecycler_group.setHasFixedSize(true);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.gro_add:
//新建群，跳转到好友选择页面
                        Intent intent = new Intent(GroupActivity.this, GroupSelectFriActivity.class);
                        intent.putExtra(AppUtil.tag_position, TAG);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    class Adapter_gr extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupActivity.Adapter_gr.MyViewHolder(LayoutInflater.from(GroupActivity.this).inflate(R.layout.recycler_singlegroup, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Group group = datas_group.get(position);
            final String gid = group.getGid();
            myViewHolder.gr_biglinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(GroupActivity.this, ChatallActivity.class);
                    intent.putExtra(ChatallActivity.para_uuid, gid);
                    intent.putExtra(AppUtil.para_boolean, false);
                    startActivity(intent);
                    GroupActivity.this.finish();
                }
            });
            myViewHolder.gr_rid.setText(group.getGid());
            myViewHolder.gr_gremark.setText(group.getGremark());
        }

        @Override
        public int getItemCount() {
            return datas_group.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout gr_biglinear;
            TextView gr_rid;
            ImageView gr_imagehead;
            TextView gr_gremark;

            public MyViewHolder(View view) {
                super(view);
                gr_biglinear = (LinearLayout) view.findViewById(R.id.gr_biglinear);
                gr_rid = (TextView) view.findViewById(R.id.gr_rid);
                gr_imagehead = (ImageView) view.findViewById(R.id.gr_imagehead);
                gr_gremark = (TextView) view.findViewById(R.id.gr_gremark);
            }
        }
    }
}
