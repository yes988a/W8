package com.w8.base.pcurl;

/**
 * rat有app发送请求等。端应该有相应的所有ser更新。
 */
public class MineUtilA {

    //查询我的个人设置
    public final static int url_app_findMysetting = 4063;

    //修改我的昵称，对应的通知其他用户更新   @return rt_8用户不存在或者其他错误
    public final static int url_app_updateMyNickname = 6792;

    //修改，声音通知
    public final static int url_app_updateMysound = 2633;

    //修改，签名
    public final static int url_app_updateMyautograph = 3202;

    //声音
    public final static String para_sound = "2D3d";

    //昵称
    public final static String para_nickname = "1n9i";

    //手机号是否可用。0可用，-1不可用
    public final static String para_isphone = "iEo";

    //签名
    public final static String para_autograph = "a3h7";

    /**
     * 密码
     */
    public final static String para_pas = "p9a";

    /**
     * 用户的ID
     */
    public final static String para_uid = "U0D";

    /**
     * 查询个人设置，返回的我的简单信息。
     */
    public final static String para_m_simple_json = "usR1D";

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

    /**
     * 是的
     */
    public final static String para_yes = "y";

    /**
     * 否定
     */
    public final static String para_no = "n";

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
     * 验证我的昵称，至少一位，最多18
     */
    public final static boolean testUserNickname(String name) {
        if (name == null) {
            return false;
        } else {
            String ss = name.trim();
            if (ss == null || ss.length() < 2 || ss.length() > 36) {
                return false;
            } else {
                return true;
            }
        }
    }

}
