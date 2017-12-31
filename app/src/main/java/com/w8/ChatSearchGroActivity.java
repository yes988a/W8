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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.data.Group;
import com.w8.base.data.GroupDao;

import java.util.List;

/**
 * 用于转发选择一个群
 * <p>
 * 样式和ChatSearchGroActivity一样
 */
public class ChatSearchGroActivity extends OnlineActivity {

    public static final String ret_gro_id = "l2l5v";

    private LinearLayoutManager lmfr;
    private RecyclerView mRecycler_group;
    private List<Group> datas_group;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search_gro);
        TAG = ChatSearchGroActivity.class.getSimpleName();
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
            result = intent.getIntExtra(AppUtil.RESULT, 0);
            if (result == 0) {
                finish();
            }
        }
        initData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        mRecycler_group = (RecyclerView) findViewById(R.id.mRecycler_group);
        lmfr = new LinearLayoutManager(this);
    }

    private void initData() {
        datas_group = MyApp.mC.getDS().getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Gname).list();
        mRecycler_group.setLayoutManager(lmfr);
        mRecycler_group.setAdapter(new Adapter_gr());
        mRecycler_group.setHasFixedSize(true);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatSearchGroActivity.this.finish();
            }
        });
    }

    class Adapter_gr extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChatSearchGroActivity.Adapter_gr.MyViewHolder(LayoutInflater.from(ChatSearchGroActivity.this).inflate(R.layout.recycler_singlegroup, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ChatSearchGroActivity.Adapter_gr.MyViewHolder myViewHolder = (ChatSearchGroActivity.Adapter_gr.MyViewHolder) holder;
            Group group = datas_group.get(position);
            final String gid = group.getGid();
            final String gremark = group.getGremark();
            myViewHolder.gr_rid.setText(gid);
            myViewHolder.gr_gremark.setText(gremark);
            myViewHolder.gr_biglinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ChatSearchGroActivity.this)
                            .setTitle(R.string.alert_choose_tittle)
                            .setMessage(gremark)
                            .setPositiveButton(R.string.alert_choose_true, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra(ret_gro_id, gid);
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