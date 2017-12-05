package com.w8.base;

import java.util.*;

/**
 * 公用的存放。。。如果，有专属某个模块，就当如到相应的模块下面。
 * <p>
 * 各参数前加固定前缀。
 * <p>
 * 网络访问时间：tim_web_
 * <p>
 * redis存储时间：tim_redis_
 * <p>
 * redis存储前缀： redis_
 * <p>
 * 路径前缀： file_
 * <p>
 * url业务标识前缀：url_login  作为para_url的value存在。    ，，，URL是A发送给B的路上(线)上的标识（线标识。）
 * <p>
 * 参数前缀： para_
 * <p>
 * 固定值标识：val_
 * <p>
 * type类型区分：typ_
 * <p>
 * <p>
 * <p>
 * 即时通讯，如果，没有及时通知到对方，发送方应该有知情权，知道对方没有收到，否则还在等待对方回复。。。  故以对方服务器实际收到信息为发送成功。
 * <p>
 * 因为好多操作是异步，没办法及时返回结果，所以，当接收者服务收到信息后，返回成功信号。
 */
public class WxUtil {

    /**
     * 访问状态，0成功，1token过期
     */
    public final static String para_r = "r";

    /**
     * 具体url地址
     */
    public final static String para_url = "u1r";

    /**
     * 二维码
     */
    public final static String para_qrcode = "qr3c";

    /**
     * 网络访问时间，8秒。 单位秒
     */
    public final static int tim_eight = 8;

    /**
     * 网络访问时间，11秒。 单位秒
     */
    public final static int tim_eleven = 11;

    /**
     * 网络访问时间，20秒，用于关键操作长时间。 单位秒
     */
    public final static int tim_twenty = 20;

    /**
     * 网络访问时间，30秒，用于异步访问时间。 单位秒
     */
    public final static int tim_thirty = 30;

    /**
     * 积极地，喜欢的）
     * 添加好友:同意
     * 消息通知:不屏蔽消息，不免打扰。控件关闭
     * 我的手机号：有效
     * 接受图片:是
     * 我的设置，声音通知
     */
    public final static int val_positive = 20;

    /**
     * 消极的，厌倦的）
     * 添加好友:拒绝
     * 消息通知:屏蔽消息，免打扰。控件打开
     * 我的手机号：失效
     * 接受图片:否
     * 我的设置，取消声音通知
     */
    public final static int val_nagative = 21;

    //等待发送：   发送给app，发送给其他ser
    public final static int stat_ab = 412;

    //发送成功：
    public final static int stat_abc = 4123;

    //发送失败，重试吧。
    public final static int stat_err = 455;

    /**
     * 登录后随机ID
     */
    public final static String para_ranid = "r4D";

    /**
     * json
     */
    public final static String para_json = "JoN";

    /**
     * 32位随机主键
     */
    public final static String para_uuid = "uU0";

    /**
     * computer id。
     */
    public final static String para_cid = "C6d";

    /**
     * 是的
     */
    public final static String para_yes = "y";

    /**
     * 否定
     */
    public final static String para_no = "n";

    /**
     * 时间
     */
    public final static String para_tim = "tIm";

    public final static boolean testPass(String pass) {
        if (pass == null || pass.length() > 32 || pass.length() < 5) {
            return false;
        } else {
            return true;
        }
    }

    public final static boolean testEmail(String email) {
        String format = "\\w{1,}[@]\\w{1,}[.]\\p{Lower}{1,}";
        if (email.matches(format)) {
            return true;// 邮箱名合法，返回true
        } else {
            return false;// 邮箱名不合法，返回false
        }
    }

    public final static String getU32() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public final static long getTim() {
        return System.currentTimeMillis();
    }

    /**
     * 警告日志（应该使用往java日志文件中写入报错的形式）
     */
    public final static void logw(String uid, String local, String param, long tim) {

    }

    /**
     * 随机一个角标。
     *
     * @return
     */
    public final static int getRandomId(int size) {
        if (size == 1) {
            return 0;
        } else {
            return new Random().nextInt(size);
        }
    }
}
