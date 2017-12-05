package com.w8.base.pcurl;

public class PhoneUtil {

    //（登录后的用户修改自己的手机号）修改，电话。。。。。可能以后要单独放服务器。如放到cat。
    public final static int url_app_updateMyphone = 3691;

    //废弃 某个手机号的归属，电话（发送给所有server）
    //手机号，解除(仅仅解绑，因为是注册，，不做其他绑定。)（服务器间通信使用）
    public final static int url_ser_del_phone = 4678;

    //更新某个手机号的归属，到某个人员.，电话（发送给所有server）
    public final static int url_ser_upda_phone = 8428;

    // 获取验证码，用于移除手机号绑定。
    public final static int url_app_gettestnum_rp = 3547;

    /**
     * 是否已经阅读同意废弃其他手机号绑定。  值：WxUtil.para_yes + phone。代表已经知道要解绑的手机号。
     */
    public final static String para_know_del_phone = "W4d";

    //电话
    public final static String para_phone = "p0h";

    public final static boolean testPhone(String phone) {
        String regg = "^1\\d{10}";// 手机号简单验证
        boolean matches = phone.matches(regg);
        return matches;
    }
}
