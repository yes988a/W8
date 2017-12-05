package com.w8;

import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.w8.base.AppUtil;
import com.w8.base.OnlineActivity;

import java.io.File;

/**
 * 我的二维码页面，将来此页面要美化，和其他图片activity不一样
 */
public class MineQRCodeActivity extends OnlineActivity {

    private ImageView imageView;
    private String uid;
    private String qrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_q_r_code);
        TAG = MineQRCodeActivity.class.getSimpleName();
        uid = AppUtil.getUid();
        qrid = AppUtil.getQrcode();
        initView();
        initListener();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.qrcode_imageView);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.seting_scan:
                        //扫一扫
                        break;
                }
                return false;
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu(v);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        uid = AppUtil.getUid();
        qrid = AppUtil.getQrcode() + "测试";
        if (uid == null || qrid == null) {
            finish();
            return;
        } else {
            File file = AppUtil.getQr(uid);
            if (file == null) {
                boolean bb = AppUtil.createQrToFile(uid, qrid);
                if (bb) {
                    File file22 = AppUtil.getQr(uid);
                    if (file22 == null) {
                        finish();
                        return;
                    } else {
                        succStart(file22);
                    }
                } else {
                    finish();
                    return;
                }
            } else {
                succStart(file);
            }
        }
    }

    private void succStart(File file) {
        if (imageView == null) {
            finish();
        } else {
            Picasso.with(this).load(file).error(R.drawable.img_err).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    finish();
                }
            });
        }
    }

    public void PopupMenu(View view) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, view);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.menu_qr_popup, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //弹出式菜单的单击事件处理
                int id = item.getItemId();
                switch (id) {
                    case R.id.qr_popup_save:
                        //保存到相册

                        break;
                    case R.id.qr_popup_reset:
                        //重置二维码。

                        break;
                }
                return false;
            }
        });
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtil.setTag(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtil.setTag("");
    }
}
