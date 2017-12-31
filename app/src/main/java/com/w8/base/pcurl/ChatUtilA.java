package com.w8.base.pcurl;

public class ChatUtilA {

    //请求者ID
    public final static String para_req = "r74qm";

    //回复者ID
    public final static String para_res = "s92m";

    //tim集合，删除已读消息使用
    public final static String para_del_tims_json = "di6h";

    //新消息，时间，传给接受者服务器使用
    public final static String para_tim_to_res_json = "8Ko0I";

    //chat内容集合
    public final static String para_list_msg_json = "lm39x";

    //等待发送：   发送给app，发送给其他ser
    public final static int stat_ab = 412;

    //发送成功：
    public final static int stat_abc = 4123;

    //发送失败，重试吧。
    public final static int stat_err = 455;

    public final static int typ_des_txt = 725;//文本
    public final static int typ_des_img = 185;//图片
    public final static int typ_des_file = 326;//附件
    public final static int typ_des_url = 486;//链接

    // 内容类型，参考方法testDesTyp
    public final static String para_chat_des_tif = "i5sv";

    //txt具体内容
    public final static String para_chat_txt = "3tx5";

    //图片 或者 文件 内容
    public final static String para_f_str = "f2s0";

    //撤回，和发送聊天，由url来决定，不适用typ判断。(暂时取消撤回信息操作。。。。)
    //单聊，群聊，也通过url判定。
    //各种请求的发送和返回，url分开。返回以 url_ret_ 开头。

    //app发送信息。。。
    public final static int url_app_addChatsingle = 4956;

    //app请求，查询聊天信息，各种通知等。
    public final static int url_app_findChatsingle = 3087;
    public static long url_app_findChatsingle_tim_old = 0L; // 上次时间 ，每次不管成功失败都修改此时间（时间为本地时间，不使用getRealTime）
    public static int url_app_findChatsingle_tim_interval = 15 * 1000; // 每次间隔，15秒
    public static int url_app_findChatsingle_tim_out = 26 * 1000; // 请求超时，30秒
    public static boolean url_app_findChatsingle_webing = false; // true表示正在请求中，不允许再请求

    // 删除已读信息。
    public final static int url_app_delChatsingleByTims = 3417;

    //app发送group聊天。
    public final static int url_app_chat_group = 6492;   // 接受app过来的请求。
    public final static int url_ser_chat_group_getFromReq = 5219;//接受分发。
    public final static int url_ret_chat_group = 16378;

    //判断类型是否合法
    public static boolean testDesTyp(Integer des_typ) {
        if (des_typ == null) {
            return false;
        } else {
            if (typ_des_txt == des_typ || typ_des_img == des_typ || typ_des_file == des_typ || typ_des_url == des_typ) {
                return true;
            } else {
                return false;
            }
        }
    }

}
