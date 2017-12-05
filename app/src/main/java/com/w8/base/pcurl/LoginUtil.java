package com.w8.base.pcurl;

/**
 * login，工具和常量。
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
 */
public class LoginUtil {

    //app，请求cat，获取自己的服务器
    public final static int url_app_testAcc = 9150;

    //app，请求rat，获取token等首次登录信息断开连接。
    public final static int url_app_LoginCompelete = 2762;

    //好友列表
    public final static String para_fri_list = "E3K5f";

    //前缀：token的ID
    public final static String para_login_tid = "89I2";

    //前缀：加密信息aes
    public final static String para_login_aes = "A2eS";

    //前缀：公网ip
    public final static String para_login_ip = "PI7s";

    //前缀：用户创建时间。
    public final static String para_login_ctim = "C8_M";


    //前缀：加密后的安全验证信息（用户验证是否真正的登录用户使用，，，不同的业务有不同的内容传送。）
    public final static String para_login_aes_safedes = "s_ad";
}
