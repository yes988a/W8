package com.w8.base;

import com.w8.base.pcurl.RetNumUtilA;

public class OnlineActivity extends WxActivity {
    protected String TAG = OnlineActivity.class.getSimpleName();//其他继承者，会改变它

    @Override
    protected void onStart() {
        super.onStart();
        String tt = AppUtil.getToken();
        if (tt.length() != RetNumUtilA.n_32) {
            finish();
        } else {
            AppUtil.startAlarmGuard();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtil.setTag(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtil.setTag(OnlineActivity.class.getSimpleName());
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    //初始化，视图。。。 子类可以继承调用此方法
    protected void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_switch);
        setSupportActionBar(toolbar);
    }

    //初始化点击事件。。。 子类可以继承调用此方法
    protected void initListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (toolbar != null) {
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
    }*/
}
