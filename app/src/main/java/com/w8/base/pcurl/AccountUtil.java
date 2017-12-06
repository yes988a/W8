package com.w8.base.pcurl;

/**
 * 账号是否存在。唯一性。
 */
public class AccountUtil {

    /* 账号 */
    public final static String para_acc = "a75c";

    // 注册，验证账号是否占用。
    public final static int url_ser_use_acc = 9062;

    // 账号占用。。
    public final static int url_ser_use_acc_exist = 3062;

    // acc暂时占用标识.  （怎么判断？是一次操作，如果网络断了，用uuid也不行，）
    public final static String redis_acc_tem = "_at)";
    //redis_acc_tem准确应该叫：被当前服务器占用。

    /**
     * 登录唯一标识，用户名验证
     */
    public static boolean testAcc(String acc) {
        if (acc == null || "".equals(acc)) {
            return false;
        } else {
            String uNameReg = "^[a-zA-Z][a-zA-Z0-9_.]{4,63}$";
            return acc.matches(uNameReg);
        }
    }
}
