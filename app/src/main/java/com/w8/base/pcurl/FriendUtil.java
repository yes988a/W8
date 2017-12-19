package com.w8.base.pcurl;

//url中app请求，也作为回复app的标识。
public class FriendUtil {

    //查询单个好友设置
    public final static int url_app_findUserrelation = 9233;

    //修改friend设置
    public final static int url_app_updateRemark = 2510;

    //修改是否通知
    public final static int url_app_updateShie = 1634;

    //-------------              ------------------------------                     ----------               ----------

    //friend 申请 return n_9本来就是好友。
    public final static int url_app_requestFri = 4675;

    //申请 ，发送到不同服务器。
    public final static int url_ser_ratToFriend = 1940;

    //-------------              ------------------------------              ----------               ----------

    //同意 添加好友 ，回复方使用
    public final static int url_app_agreeFri = 5313;

    //同意 添加好友 ，回复方To请求方
    public final static int url_ser_resToreq = 2204;

    //-------------              ------------------------------             ----------               ----------

    //删除friend
    public final static int url_app_delFriFrom = 2257;

    public final static int url_ser_delFriToRat2 = 5637;

    //-------------              ------------------------------             ----------               ----------

    //好友信息变动。通知（需要配合，其他请求好友信息变化接口。）
    public final static int url_ret_knownFri = 3275;

    //-------------              ------------------------------             ----------               ----------

    //是否见过,y认识   ,,  n不认识
    public final static String para_met = "m0t";

    //请求方
    public final static String para_reqid = "2Id";

    //被请求方ID
    public final static String para_resid = "r6!D";

    //我对好友的备注。
    public final static String para_remark = "!2m";

    //我对好友的备注。
    public final static String para_shie = "s4e";

    //请求者账号
    public final static String para_reqacc = "R6c";

    //请求者昵称
    public final static String para_reqnickname = "Fn6i";

    //回复者账号
    public final static String para_resacc = "s8c";

    //回复者昵称
    public final static String para_resnickname = "Fn1s";

    //请求方，申请描述
    public final static String para_reqdes = "Q5L";

    //同意添加好友，时间
    public final static String para_tim_respone = "m10L";

    //添加好友请求。
    public final static int typ_add_fri = 571;

    //删除删除好友。
    public final static int typ_del_fri = 553;

    //好友ID集合，json格式.
    public final static String para_fids = "f0Ds";
    public final static String para_fid = ")3S";

}
