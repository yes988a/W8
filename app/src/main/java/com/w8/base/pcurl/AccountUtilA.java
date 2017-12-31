package com.w8.base.pcurl;

/**
 * 账号是否存在。唯一性。
 */
public class AccountUtilA {

    /* 账号 */
    public final static String para_acc = "a75c";

    /**
     * 注册时，判断账号存是否在时，判断操作的唯一性。
     */
    public final static String para_uuid_existAccForReg = "oU74L";

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
