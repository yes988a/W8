package com.w8;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.w8.base.AppUtil;
import com.w8.base.BadgeView;
import com.w8.base.MyApp;
import com.w8.base.NologinActivity;
import com.w8.base.data.DaoMaster;
import com.w8.base.pcurl.FriendUtilA;
import com.w8.services.TesttttttService;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WelcomeActivity extends NologinActivity {
    private ImageView welcome_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TAG = WelcomeActivity.class.getSimpleName();
        String key = "WZOoXwGv4OvxkeRu5op9sQ==";//测试使用
        AppUtil.setSafeAes_nolog(key);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String tt = AppUtil.getToken();
       /* if (tt.length() != 32) {
            startAct(this, LoginActivity.class);
        }*/
//        testImge();
        welcome_img = (ImageView) findViewById(R.id.welcome_img);
        BadgeView badgeVieweee = new BadgeView(this);
        badgeVieweee.setTargetView(welcome_img);
        badgeVieweee.setBadgeCount(2);


        BadgeView badgeView = new BadgeView(this);
//        badgeView.setTargetView(activeupbut);
        badgeView.setBadgeCount(120);

        welcome_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertDialogText("哈哈哈");
                return false;
            }
        });

    }

    public void clearData(View view) {

        MyApp.mC.getDM().dropAllTables(MyApp.mC.getDS().getDatabase(), true);
        MyApp.mC.getDM().createAllTables(MyApp.mC.getDS().getDatabase(), false);
        AppUtil.clearAll();
        AppUtil.clearFile();

    }

    public void selectImge(View view) {
        // 1. 使用默认风格，并指定选择数量：
        // 第一个参数Activity/Fragment； 第二个request_code； 第三个允许选择照片的数量，不填可以无限选择。
        // Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 9);

        // 2. 使用默认风格，不指定选择数量：
        // Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO); // 第三个参数不填的话，可以选择无数个。

        // 3. 指定风格，并指定选择数量，如果不想限制数量传入Integer.MAX_VALUE;

//        Album.album(this).start(11111);
/*
        Album.album(this)., 11666
                , 6                                                         // 指定选择数量。
                , ContextCompat.getColor(this, R.color.colorAccent)        // 指定Toolbar的颜色。
                , ContextCompat.getColor(this, R.color.colorPrimaryDark));  // 指定状态栏的颜色。
*/

        ArrayList<AlbumFile> mAlbumFiles = new ArrayList<AlbumFile>();


        Album.image(this) // Image selection.
                .multipleChoice()
                .requestCode(200)
                .camera(true)
                .columnCount(3)
                .selectCount(1)
                .checkedList(null)
                .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                    }

                    @Override
                    public void onAlbumCancel(int requestCode) {
                    }
                })
                .widget(Widget.newDarkBuilder(this)
                        .title("相册").build()) // 标题。
                .start();

    }

    public void registerAccPhoneActivity(View view) {
        Intent iii = new Intent(this, RegisterAccPhoneActivity.class);
        this.startActivity(iii);
    }

    public void DeletFiles(View view) {
        String[] s = this.fileList();
        for (int i = 0; i < s.length; i++) {
            this.deleteFile(s[i]);
        }
    }

    public void NewFriendActivity(View view) {
        Intent intent = new Intent(this, FriReqActivity.class);
        intent.putExtra(FriendUtilA.para_fids, "c913f1ee495e40a5bc9555c857a3366c");
        this.startActivity(intent);
    }

    public void ActiveActivity(View view) {
        Intent intent = new Intent(this, ActiveActivity.class);
        this.startActivity(intent);
    }

    public void LoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        AppUtil.clearAll();
        DaoMaster.dropAllTables(MyApp.mC.getDM().getDatabase(), true);
        DaoMaster.createAllTables(MyApp.mC.getDM().getDatabase(), true);
        this.startActivity(intent);
    }

    public void test_Okhttp_Ws(View view) {

//        OkHttpClient client = MyApp.mC.getWS();

        // client 一个对应多个newWebSocket，如果连接失败自动舍弃，如果成功，在WSListener记录到底有多少个连接，线程池executorService中没办法得到所有详情。

//        client.dispatcher().executorService().shutdown();  // 非阻塞。是的。 会运行所有和websocket有关的服务代码。
        //记录到待发送列表中。（不同业务，应该有不同的操作。）
        //在ui提示，网络尝试连接中。
    }

    public void testSer(View view) {
        Intent intent = new Intent(MyApp.mC, TesttttttService.class);
        intent.setAction(TesttttttService.class.getSimpleName());
        PendingIntent sender = PendingIntent.getService(MyApp.mC, 0, intent, 0);
        AlarmManager am = (AlarmManager) MyApp.mC.getSystemService(MyApp.mC.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), AppUtil.app_service_often, sender);
    }

    /*---------------------------       测试图片
             http://img3.duitang.com/uploads/item/201509/04/20150904123004_5RfsX.thumb.700_0.jpeg  -----------------------------------*/
    private void testImge() {
        ImageView imageView = (ImageView) findViewById(R.id.welcome_img);

        //加载网络图片
//        Picasso.with(WelcomeActivity.this).load("http://img3.duitang.com/uploads/item/201509/04/20150904123004_5RfsX.thumb.700_0.jpeg").error(R.drawable.mvcc1).into(imageView);

        //加载项目图片
//        Picasso.with(this).load(R.mipmap.ic_add).into(imageView);

        //加载私有图片  http://blog.csdn.net/roland_sun/article/details/46460063

        String imghead = "imghead_" + AppUtil.getUid();
        try {
            FileOutputStream fileOutputStream = this.openFileOutput(imghead, MODE_PRIVATE);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mvcc1);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
        }
        String[] ss = this.fileList();
        for (String s : ss) {
        }
        File ff = this.getFileStreamPath(imghead);
        if (ff.exists() && ff.isFile()) {
        }
        Picasso.with(this).load(ff).into(imageView);
    }


    private void alert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_failed)
                .setMessage(R.string.permission_camera_failed_hint)
                .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("会怎样", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("中立的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void PopupMenu(View view) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, view);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.menu_friend, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //弹出式菜单的单击事件处理
                alertDialogText("4444444444");
                return false;
            }
        });
        //显示(这一行代码不要忘记了)
        popup.show();
    }
}
