package com.w8.base.pcurl;

/**
 * 验证码
 */
public class TestnumUtil {

    /**
     * 验证码，类型：注册时，手机或者邮箱验证码。
     */
    public final static String para_testnum = "T04n";

    //同步验证码发送次数到其他服务器。
    public final static int url_ser_syn_testSfe = 9412;

    /**
     * 手机验证码次数限定，（主键：手机号）
     * <p>
     * value： 时间戳&发送次数。
     */
    public final static String redis_test_num = "n3M";
    public final static String redis_reg_temp = "T7R";//验证码和手机号匹配临时存储。

    public final static String redis_remove_phone_temp = "w*j";//移除手机号账号关联，临时存储。
}
