package com.w8.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.w8.ChatallActivity;
import com.w8.FriendDesActivity;
import com.w8.GroupDesActivity;
import com.w8.R;
import com.w8.base.data.AccIp;
import com.w8.base.data.AccIpDao;
import com.w8.base.data.ActiveDao;
import com.w8.base.data.Chat;
import com.w8.base.data.ChatDao;
import com.w8.base.data.EditeMsgDao;
import com.w8.base.data.Friend;
import com.w8.base.entity.UserrelationSimple;
import com.w8.base.pcurl.AccountUtil;
import com.w8.base.pcurl.ChatUtil;
import com.w8.base.pcurl.FriendUtil;
import com.w8.base.pcurl.GroupUtil;
import com.w8.base.pcurl.LoginUtil;
import com.w8.base.pcurl.PhoneUtil;
import com.w8.base.pcurl.MineUtil;
import com.w8.services.DayService;
import com.w8.services.GuardService;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.MODE_PRIVATE;
import static org.apache.commons.io.IOUtils.EOF;

/**
 * 所有，app独有。
 * <p>
 * 如果，某个属性，pc有对已经的工具类，但是没有对应的使用，那么放到这个工具类中来。
 */
public class AppUtil {

    private static String TAG = AppUtil.class.getSimpleName();

    public static String para_boolean = "b_";

    public static int c_a = 661;// app待接收

    //发送状态。
    public final static int stat_a = 41;

    private static String qr_prefix = "Qr2C3:";//二维码名称前缀,二维码内容前缀

    public static String img_path = "7pa";//内部图片地址

    public static String txt = "tX0";//文本内容

    public static String RESULT = "R1SL";//activity之间传递

    /**
     * 60好友
     */
    public static int FRI_LIST_TYPE = 60;

    /**
     * 50标签具体显示
     */
    public static int FRI_LIST_SYS_LABEL = 50;

    /**
     * 42群聊
     */
    public static int FRI_LIST_SYS_GROUCHAT = 42;
    /**
     * 43标签添加
     */
    public static int FRI_LIST_SYS_LABEL_ADD = 43;

    /**
     * 消息测回时间
     */
    public static int tim_back_chat = 1000 * 4 * 60;

    //无数据，时，显示没有内容和高度的view

    public static int c_err = 660;// 仅仅用于聊天，信息发送失败

    public static long  web_succ_tim_chat = 0L;// 获取最新信息，上次访问时间。

    //插入账号，关联，ipp
    public synchronized static void insertAccIp(String acc, String iipp) {
        AccIpDao accIpDao = MyApp.mC.getDS().getAccIpDao();
        List<AccIp> liii = accIpDao.
                queryBuilder().where(AccIpDao.Properties.Acc.eq(acc)).list();
        if (liii.size() == 1) {
            liii.get(0).setIpp(iipp);
            accIpDao.insertOrReplace(liii.get(0));
        } else {
            accIpDao.deleteInTx(liii);
            AccIp accIp = new AccIp();
            accIp.setIpp(iipp);
            accIp.setAcc(acc);
            accIpDao.insertOrReplace(accIp);
        }
    }

    //-----------------     chat  start    -------------------------------------------------------------------------------------------

    //清空，某个聊天记录，包括图片。（如果，速度很慢应该放到后台server中处理。）
    public static void clearOne(String uuid) {
        //清空活动内容。
        String myid = getUid();
        ActiveDao ac = MyApp.mC.getDS().getActiveDao();
        ac.deleteInTx(ac.queryBuilder().where(ActiveDao.Properties.Uuid.eq(uuid)).list());

        //清空编辑中的内容
        EditeMsgDao egof = MyApp.mC.getDS().getEditeMsgDao();
        egof.deleteInTx(egof.queryBuilder().where(EditeMsgDao.Properties.Uuid.eq(uuid)).list());

        //清空聊天内容
        List<Chat> delChat = new ArrayList<Chat>();
        ChatDao cd = MyApp.mC.getDS().getChatDao();

        QueryBuilder<Chat> qb = cd.queryBuilder();

        // 查询单聊，（缺少，接受者是好友，下面查询群时包含啦，如果UUID是人id的话。）
        QueryBuilder<Chat> qb1 = qb.where(ChatDao.Properties.Requid.eq(uuid), ChatDao.Properties.Resuid.eq(myid));

        delChat.addAll(qb1.list());

        // 查询群聊
        QueryBuilder<Chat> qb3 = qb.where(ChatDao.Properties.Resuid.eq(uuid));

        delChat.addAll(qb3.list());

        for (int i = delChat.size() - 1; i >= 0; i--) {
            if (ChatUtil.typ_des_img == delChat.get(i).getDtyp()) {
                AppUtil.deleFile(delChat.get(i).getTxt());
            }
            //缺少删除附件。
        }
        cd.deleteInTx(delChat);
    }

    //转到聊天,不关闭上一个activity

    /**
     *
     * @param uuid  好友id或者群id。
     * @param isFri  用于判断是我的好友还是群。
     */
    public static void toChat(String uuid, int isFri) {
        Intent intent = new Intent(MyApp.mC, ChatallActivity.class);
        intent.putExtra(ChatallActivity.para_uuid, uuid);
        intent.putExtra(AppUtil.para_boolean, isFri);
        MyApp.mC.startActivity(intent);
    }

    //跳转到好友详情,不关闭上一个activity
    public static void toFriDes(String fid) {
        Intent intent = new Intent(MyApp.mC, FriendDesActivity.class);
        intent.putExtra(FriendUtil.para_fid, fid);
        MyApp.mC.startActivity(intent);
    }

    //跳转到群详,不关闭上一个activity
    public static void toGroDes(String gid) {
        Intent intent = new Intent(MyApp.mC, GroupDesActivity.class);
        intent.putExtra(GroupUtil.para_gid, gid);
        MyApp.mC.startActivity(intent);
    }

    //插入好友。（清除以前旧数据，插入传入的list）
    public static void insertFrind(List<UserrelationSimple> userList) {
        //插入好友数据。
//                            frilist  需要排序和添加abc搜索。
        List<Friend> newfrilist = new ArrayList<Friend>();
        List<String> abcList = new ArrayList<String>();
        for (int x = userList.size() - 1; x >= 0; x--) {
            UserrelationSimple ue = userList.get(x);
            Friend ff = new Friend();
            ff.setFid(ue.getFid());
            ff.setShie(ue.getShie());
            ff.setRemark(ue.getRemark());
            ff.setPhone("");//ue.getPhone());
            ff.setAccount("");
            ff.setDegree(RetNumUtil.n_0);
            ff.setTyp(AppUtil.FRI_LIST_TYPE);
            ff.setNickname("");//ue.getNickname());
            ff.setAbc("");//ue.getAbc());

            String first_abc = "";//ue.getAbc().substring(1);

            ff.setFirst_abc(first_abc);
            if (abcList.contains(first_abc)) {
                ff.setShowabc(WxUtil.val_positive);
            } else {
                abcList.add(first_abc);
                ff.setShowabc(1);
            }
            newfrilist.add(ff);
        }
        MyApp.mC.getDS().getFriendDao().deleteAll();
        MyApp.mC.getDS().getFriendDao().insertInTx(newfrilist);
    }

    //-----------------          闹钟   start    -----------------------------------------------------------------------------------------------

    /**
     * 常驻定时器，（1分钟）,,,检测各种状态，切换各种状态。
     * app打开状态。尝试建立长连接，如果建立失败，那么此service中检测一下是否有新消息。
     */
    public static int app_service_often = 60 * 1000;

    /**
     * app关闭状态（白天），长连接已经断开状态。3分钟
     */
    public static int app_service_close_day = 180 * 1000;

    /**
     * app关闭状态（晚上），长连接已经断开状态。15分钟
     */
    public static int app_service_close_night = 900 * 1000;

    /**
     * 中控制器，控制其他alarm状态
     */
    public static void startAlarmGuard() {
        Intent intent = new Intent(MyApp.mC, GuardService.class);
        intent.setAction(GuardService.class.getSimpleName());
        PendingIntent sender = PendingIntent.getService(MyApp.mC, 0, intent, 0);
        AlarmManager am = (AlarmManager) MyApp.mC.getSystemService(MyApp.mC.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+2, app_service_often, sender);
    }

    public static void stopAlarmGuard() {
        stopAlarm(GuardService.class);
    }

    /**
     * app关闭状态（白天）http轮询。
     */
    public static void startAlarmDay() {
        Intent intent = new Intent(MyApp.mC, DayService.class);
        intent.setAction(DayService.class.getSimpleName());
        PendingIntent sender = PendingIntent.getService(MyApp.mC, 0, intent, 0);
        AlarmManager am = (AlarmManager) MyApp.mC.getSystemService(MyApp.mC.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), app_service_close_day, sender);
    }

    public static void stopAlarmDay() {
        stopAlarm(DayService.class);
    }

    /**
     * app关闭状态（晚上空闲）http轮询。
     */
    public static void startAlarmNight() {
        Intent intent = new Intent(MyApp.mC, DayService.class);
        intent.setAction(DayService.class.getSimpleName());
        PendingIntent sender = PendingIntent.getService(MyApp.mC, 0, intent, 0);
        AlarmManager am = (AlarmManager) MyApp.mC.getSystemService(MyApp.mC.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), app_service_close_night, sender);
    }

    public static void stopAlarmNight() {
        stopAlarm(DayService.class);
    }


    private static void stopAlarm(Class clazz) {
        Intent intent = new Intent(MyApp.mC, clazz);
        intent.setAction(clazz.getSimpleName());
        PendingIntent sender = PendingIntent.getService(MyApp.mC, 0, intent, 0);
        AlarmManager am = (AlarmManager) MyApp.mC.getSystemService(MyApp.mC.ALARM_SERVICE);
        am.cancel(sender);
    }
//     ----------------       smile 开始         --------------------------------------------------------------------------------------------

    public static String reg = "(\\[[\u4e00-\u9fa5]{1,3}\\])";

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param spannableString
     * @param patten
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static void dealExpression(SpannableString spannableString, Pattern patten, Map<String, Integer> mapSmile) {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String namekey = matcher.group();
            Integer fnummm = mapSmile.get(namekey);
            Integer dd = null;
            if (fnummm != null) {
                try {
                    dd = Integer.parseInt(R.drawable.class.getDeclaredField("f" + fnummm).get(null).toString());
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage());
                } catch (NoSuchFieldException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            if (dd != null) {
                Bitmap bitmap = BitmapFactory.decodeResource(MyApp.mC.getResources(), dd);
                ImageSpan imageSpan = new ImageSpan(MyApp.mC, bitmap);                //通过图片资源id来得到bitmap，用一个ImageSpan来包装
                int end = matcher.start() + namekey.length();                    //计算该图片名字的长度，也就是要替换的字符串的长度
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);    //将该图片替换字符串中规定的位置中
            }
        }
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     * <p>
     * 用于已经存储到sql里面的数据显示使用
     *
     * @param str
     * @return
     */
    public static SpannableString getExpressionString(String str, Map<String, Integer> mapSmile) {
        SpannableString spannableString = new SpannableString(str);
        if (mapSmile != null) {
            Pattern sinaPatten = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);        //通过传入的正则表达式来生成一个pattern
            dealExpression(spannableString, sinaPatten, mapSmile);
        }
        return spannableString;
    }


    //-----------------------        SharedPreferences  start                 ------------------------------------------------------------------------

    public static byte[] getSafeAes() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);

        return Base64.decode(sp.getString(LoginUtil.para_login_aes, ""), Base64.DEFAULT);
    }

    /**
     * @param rawKeyB64 处理过：AppUtil.getRawKey(aesUUID + Long.toString(ctim))
     */
    public static void setSafeAes(String rawKeyB64) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LoginUtil.para_login_aes, rawKeyB64);
        editor.commit();
    }

    /**
     * 获取未登录时，加密操作.
     *
     * @return 返回null标示没有加密信息。
     */
    public static byte[] getSafeAes_nolog() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(START, MyApp.mC.MODE_PRIVATE);
        String ss = sp.getString(aes_nolog, "");
        if ("".equals(ss)) {
            return null;
        } else {
            return Base64.decode(ss, Base64.DEFAULT);
        }
    }

    public static void setSafeAes_nolog(String aes) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(START, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(aes_nolog, aes);
        editor.commit();
    }

    public static void setToken(String token) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LoginUtil.para_login_tid, token);
        editor.commit();
    }

    public static String getToken() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(LoginUtil.para_login_tid, "");
    }

    public static String getUid() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP_SAVE, MyApp.mC.MODE_PRIVATE);
        return sp.getString(MineUtil.para_uid, "");
    }

    public static void setUrl(String url) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(WxUtil.para_url, url);
        editor.commit();
    }

    //获取，登录者的服务器url::::ws://127.0.0.1:9981
    public static String getUrl() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(WxUtil.para_url, "");
    }

    //我的账号
    public static String getAccount() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(AccountUtil.para_acc, "");
    }

    //我的昵称
    public static String getNickname() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(MineUtil.para_nickname, "");
    }

    //我的昵称
    public static void setNickname(String nick) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MineUtil.para_nickname, nick);
        editor.commit();
    }

    //手机
    public static String getPhone() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(PhoneUtil.para_phone, "");
    }

    //手机
    public static void setPhone(String phone) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PhoneUtil.para_phone, phone);
        editor.commit();
    }

    //签名
    public static String getAutograph() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(MineUtil.para_autograph, "");
    }

    //签名
    public static void setAutograph(String autograph) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MineUtil.para_autograph, autograph);
        editor.commit();
    }

    public static int getSound() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getInt(MineUtil.para_sound, RetNumUtil.n_b1);
    }

    public static void setSound(int sound) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(MineUtil.para_sound, sound);
        editor.commit();
    }

    public static String getQrcode() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(WxUtil.para_qrcode, "");
    }

    /**
     * 更新真实时间，(真实时间)服务器时间 = time_di ++ app时间
     *
     * @param td 服务器返回的真实时间
     */
    public static void setTimeReal(long td) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(time_di, td - System.currentTimeMillis());
        editor.commit();
    }

    /**
     * 获取真实时间，(真实时间)服务器时间 = time_di ++ app时间
     *
     * @return
     */
    public static long getTimeReal() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return System.currentTimeMillis() + sp.getLong(time_di, RetNumUtil.n_0);
    }

    /**
     * 查询数据库存储个人的登录名
     */

    public static void setLogin(String uid, String randomid, String token, String rawKeyB64, Integer sound, String acc, String nickname, long crateTime) {

        /*
         * @param uid  用户id
         * @param randomid 每次登录的随机id。
         * @param token
         * @param rawKey 密钥。
         * @param sound 声音。
         * @param acc 账号。
         * @param nickname   昵称。
         */

        if (uid != null) {
            SharedPreferences sp_save = MyApp.mC.getSharedPreferences(APP_SAVE, MyApp.mC.MODE_PRIVATE);
            SharedPreferences.Editor editor_save = sp_save.edit();
            editor_save.putString(MineUtil.para_uid, uid);
            editor_save.commit();
        }

        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // 判断为null然后删除的方法暂时没有用到。
        if (uid == null || randomid == null || token == null
                || rawKeyB64 == null || acc == null || nickname == null || sound == null) {
            //用于安全退出，清除个人信息（安全）
//            editor.remove(MineUtil.ranid);
            editor.remove(AccountUtil.para_acc);
            editor.remove(MineUtil.para_nickname);
            editor.remove(PhoneUtil.para_phone);
            editor.remove(MineUtil.para_autograph);
            editor.remove(WxUtil.para_qrcode);
            editor.remove(LoginUtil.para_login_tid);
            editor.remove(LoginUtil.para_login_aes);
            editor.remove(LoginUtil.para_login_ctim);
        } else {

            editor.putString(LoginUtil.para_ranid, randomid);
            editor.putString(AccountUtil.para_acc, acc);
            editor.putString(MineUtil.para_nickname, nickname);
            editor.putInt(MineUtil.para_sound, sound);
            editor.putString(LoginUtil.para_login_aes, rawKeyB64);
            editor.putString(LoginUtil.para_login_tid, token);
            editor.putLong(LoginUtil.para_login_ctim, crateTime);
        }
        editor.commit();
    }

    /**
     * @param tag activity标识，末节点
     */
    public static void setTag(String tag) {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag_position, tag);
        editor.commit();
    }

    /**
     * @return
     */
    public static String getTag() {
        SharedPreferences sp = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        return sp.getString(tag_position, NologinActivity.class.getSimpleName());
    }

    public static void clearAll() {
        SharedPreferences sp4 = MyApp.mC.getSharedPreferences(APP, MyApp.mC.MODE_PRIVATE);
        SharedPreferences.Editor ed4 = sp4.edit();
        ed4.clear();
        ed4.commit();
    }

    /*-----------不变信息存储,为了性能  结束-<<<---------------------------*/

    /**
     * activity当前位置。每个activity在onResume和onPause中注册和更改tag位置标识
     */
    public static String tag_position = "_tp_";

    /**
     * 时间差，毫米值，(真实时间)服务器时间 = time_di ++ app时间
     */
    public static String time_di = "te_i";

    /**
     * des普通聊天
     */
    public static String aes_nolog = "as_og";

    /**
     * 如屏幕变化等，app本地改变信息存在
     */
    public static String APP = "WX";
    //退出时保留，判断是否同一个账号使用
    public static String APP_SAVE = "WsXa";

    /**
     * 未登录时初始化的安全信息
     */
    public static String START = "SNL";

    //--------------------- 安全退出。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。

    /**
     * 安全退出，然后finish();当前activity即可，其它打开的App就会跟着判断token然后关闭。
     */
    public static void quitSafe() {
        //清除保密信息
        AppUtil.stopAlarmGuard();
        AppUtil.stopAlarmDay();
        AppUtil.stopAlarmNight();
//        AppUtil.setLogin(null);
        //关闭所有的后台更新通知
        AppUtil.clearAll();
        if(MyApp.webSocket!=null){
            MyApp.webSocket.close(7900, MyApp.mC.getString(R.string.sign_out));
        }
    }

    //---------------------------   文件操作     ---------------------------------------------------------------------------------

    /**
     * 得到文件  私有文件夹操作
     */
    public static File getFile(String fileName) {
        File file = MyApp.mC.getFileStreamPath(fileName);
        if (file.exists() && file.isFile() && file.canRead()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 得到文件流（私有文件）  私有文件夹操作
     */
    public static FileInputStream getFileStream(String fileName) {
        try {
            FileInputStream inputStream = MyApp.mC.openFileInput(fileName);
            return inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * file转base64   私有文件夹操作
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data == null) {
            return "";
        } else {
            return Base64.encodeToString(data, Base64.DEFAULT);
        }
    }

    /**
     * 文件存储到私有文件夹，并设置名字 私有文件夹操作
     *
     * @param file
     * @param fileName 有后缀名
     */
    public static int setFile(File file, String fileName) {
        try {
            if (file.exists()) {
                FileOutputStream output = MyApp.mC.openFileOutput(fileName, MODE_PRIVATE);
                FileInputStream inputStream = new FileInputStream(file);
                int nnn = copy(inputStream, output);
                inputStream.close();
                return nnn;
            } else {
                return -1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除文件 私有文件夹操作
     *
     * @param fileName 有后缀名
     */
    public static boolean deleFile(String fileName) {
        return MyApp.mC.deleteFile(fileName);
    }

    /**
     * 清空所有私有文件。
     */
    public static void clearFile() {
        String[] ns = MyApp.mC.fileList();
        for (int i = 0; i < ns.length; i++) {
            deleFile(ns[i]);
        }
    }

    /**
     * 文件流复制，记得关闭input
     *
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    private static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        return copyLarge(input, output, new byte[1024]);
    }

    private static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    //--------------     生成二维码    ----------------------------------------------------------------------------------------------------------------------------

    private final static String qr_name = ".PNG";

    /**
     * 获取二维码文件。
     *
     * @param uid
     * @return
     */
    public static File getQr(String uid) {
        return getFile(qr_prefix + uid + qr_name);
    }

    /**
     * 生成二维码。保持成私有图片。
     *
     * @param uid  人员id。
     * @param qrid 二维码内容。
     */
    public static boolean createQrToFile(String uid, String qrid) {
        try {
            Bitmap bitmap = createQrToBitmap(qr_prefix + qrid);
            if (bitmap == null) {
                return false;
            } else {
                FileOutputStream fileOutputStream = MyApp.mC.
                        openFileOutput(qr_prefix + uid + qr_name, MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream);
                fileOutputStream.close();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成QRCode，返回bitmap
     *
     * @param qrid ，需要带前缀
     * @return
     */
    public static Bitmap createQrToBitmap(String qrid) {

        try {
            if (qrid == null || qrid.equals("")) {
                return null;
            } else {

                //配置参数
                Map hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                //容错级别
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

                // 图像数据转换，使用了矩阵转换
                BitMatrix bitMatrix = new QRCodeWriter().encode(qrid, BarcodeFormat.QR_CODE, 900, 901, hints);

                int widthPix = bitMatrix.getWidth();
                int heightPix = bitMatrix.getHeight();

                int[] pixels = new int[widthPix * heightPix];
                // 下面这里按照二维码的算法，逐个生成二维码的图片，
                // 两个for循环是图片横列扫描的结果
                for (int y = 0; y < heightPix; y++) {
                    for (int x = 0; x < widthPix; x++) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * widthPix + x] = 0xff000000;
                        } else {
                            pixels[y * widthPix + x] = 0xffffffff;
                        }
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
                        Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
                return bitmap;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------------------------     aes加密解密        ---------------------------------------------------------------------

    /**
     * 获取秘钥，string，base64，用于存储。便于使用。
     *
     * @param seed 种子数据
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getRawKey(String seed) {
        try {
            byte[] rawKey = getRawKey_Byte(seed);
            return Base64.encodeToString(rawKey, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取秘钥， byte
     *
     * @param seed 种子数据
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] getRawKey_Byte(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes("UTF-8"));
        // AES加密数据块分组长度必须为128比特，密钥长度可以是128比特、192比特、256比特中的任意一个
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 加密
     *
     * @param rawKey   密钥
     * @param clearPwd 明文字符串
     * @return 密文字节数组
     */
    public static String aesEncrypt(byte[] rawKey, String clearPwd) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encypted = cipher.doFinal(clearPwd.getBytes("UTF-8"));
            return Base64.encodeToString(encypted, Base64.DEFAULT);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解密
     *
     * @param encrypted 密文字节数组
     * @param rawKey    密钥
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String encrypted, byte[] rawKey) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
}
