package com.w8.base.pcurl;

public class PhoneUtilA {

    //（登录后的用户修改自己的手机号）修改，电话。。。。。可能以后要单独放服务器。如放到cat。
    public final static int url_app_updateMyphone = 3691;

    // 获取验证码，用于移除手机号绑定。
    public final static int url_app_gettestnum_rp = 3547;

    /**
     * 是否已经阅读同意废弃其他手机号绑定。  值：MineUtilAA.para_yes + phone。代表已经知道要解绑的手机号。
     */
    public final static String para_know_del_phone = "W4d";

    //电话
    public final static String para_phone = "p0h";

    //，随机UUID，解除电话号码绑定。返回给app
    public final static String para_re_phone_random_uuid = "R37p";

    public final static boolean testPhone(String phone) {
        String regg = "^1\\d{10}";// 手机号简单验证
        boolean matches = phone.matches(regg);
        return matches;
    }

}
