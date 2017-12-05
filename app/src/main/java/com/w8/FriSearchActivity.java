package com.w8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.RetNumUtil;
import com.w8.base.WxUtil;
import com.w8.base.pcurl.FriendUtil;
import com.w8.base.pcurl.SearchUtil;
import com.w8.base.pcurl.MineUtil;

/**
 * 按手机号、小微号，搜索页面，添加好友搜索页面
 */
public class FriSearchActivity extends OnlineActivity implements View.OnClickListener {

    private AutoCompleteTextView sear_add_fri_uuid;//唯一，用户名或者电话号码。
    private Button sear_add_fri_start;
    private ProgressBar sear_add_fri_progres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri_search);
        TAG = FriSearchActivity.class.getSimpleName();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sear_add_fri_uuid.setText("yes988a");
        sear_add_fri_uuid.setFocusable(true);
        sear_add_fri_uuid.setFocusableInTouchMode(true);
        sear_add_fri_uuid.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchfriend, menu);
        return true;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);

        sear_add_fri_uuid = (AutoCompleteTextView) findViewById(R.id.sear_add_fri_uuid);
        sear_add_fri_start = (Button) findViewById(R.id.sear_add_fri_start);
        sear_add_fri_progres = (ProgressBar) findViewById(R.id.sear_add_fri_progres);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriSearchActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.searchfri_scan:
//扫一扫，添加群，跳转到扫一扫

                        break;
                }
                return false;
            }
        });
        sear_add_fri_uuid.setOnClickListener(this);
        sear_add_fri_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int idd = view.getId();
        if (R.id.sear_add_fri_uuid == idd) {
        } else if (R.id.sear_add_fri_start == idd) {
            final String text = sear_add_fri_uuid.getEditableText().toString();
            if ("".equals(text)) {
                return;
            } else {
                JsonObject into = new JsonObject();
                into.addProperty(WxUtil.para_uuid, sear_add_fri_uuid.getEditableText().toString());
                into.addProperty(WxUtil.para_url, SearchUtil.url_app_findUserByuu);
                StringRequest wj = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String msg) {
                        sear_add_fri_progres.setVisibility(View.GONE);
                        sear_add_fri_start.setEnabled(true);
                        try {

                            JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                            Integer r = jo.get(WxUtil.para_r).getAsInt();
                            if (RetNumUtil.n_0 == r) {

                                if (jo.get(MineUtil.para_uid) == null) {
                                    //不在当前服务器。
                                    if (jo.get(SearchUtil.para_ipp) == null) {
                                        alertDialogText(getString(R.string.search_des_no));
                                    } else {
                                        //到另一个服务器，请求数据。
                                    }
                                } else {
                                    String uid = jo.get(MineUtil.para_uid).getAsString();
                                    String nickname = jo.get(MineUtil.para_nickname).getAsString();
                                    String autograph = jo.get(MineUtil.para_autograph).getAsString();
                                    if (uid.equals(AppUtil.getUid())) {
                                        startAct(FriSearchActivity.this, MineSetingActivity.class);
                                    } else {
                                        //跳转到，请求好友列表。
                                        Intent intent = new Intent(FriSearchActivity.this, FriReqActivity.class);
                                        intent.putExtra(FriendUtil.para_fid, uid);
                                        intent.putExtra(MineUtil.para_nickname, nickname);
                                        intent.putExtra(MineUtil.para_autograph, autograph);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else if (RetNumUtil.n_8 == r) {//没有此用户
                                alertDialogText(getString(R.string.search_des_no));
                            } else {
                                alertDialogText(getString(R.string.fail));
                            }

                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertDialogText(getString(R.string.req_met_err));
                        sear_add_fri_progres.setVisibility(View.GONE);
                        sear_add_fri_start.setEnabled(true);
                    }
                });
                MyApp.mC.getVQ().add(wj);

                sear_add_fri_progres.setVisibility(View.VISIBLE);
                sear_add_fri_start.setEnabled(false);
            }
        }
    }
}
