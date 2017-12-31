package com.w8.base.pcurl;

//url中app请求，也作为回复app的标识。
public class FriendUtilA {

    //查询单个好友设置
    public final static int url_app_findUserrelation = 9233;

    //修改friend设置
    public final static int url_app_updateRemark = 2510;

    //修改是否通知
    public final static int url_app_updateShie = 1634;

    //-------------              ------------------------------                     ----------               ----------

    //friend 申请 return n_9本来就是好友。
    public final static int url_app_requestFri = 4675;

    //-------------              ------------------------------              ----------               ----------

    //同意 添加好友 ，回复方使用
    public final static int url_app_agreeFri = 5313;

    //-------------              ------------------------------             ----------               ----------

    //删除friend
    public final static int url_app_delFriFrom = 2257;

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

    //添加成功，回复给请求者服务器。时间
    public final static String para_tim_succ_add = "T85S";

    //删除请求，接受到APP的时间。
    public final static String para_tim_req_del = "Bd3y";

    //请求方，申请描述
    public final static String para_reqdes = "Q5L";

    //同意添加好友，时间
    public final static String para_tim_respone = "m10L";

    //查询好友简单信息时返回。。。
    public final static String para_user_simple_json = "oj48";

    //好友请求，服务器间传递的好友简单信息json
    public final static String para_fri_req_json = "sdf2";

    //同意添加好友，chat内容。ws返回给app
    public final static String para_fri_respone_json = "sked";

    //添加好友请求。
    public final static int typ_add_fri = 571;

    //删除删除好友。
    public final static int typ_del_fri = 553;

    //好友ID集合，json格式.
    public final static String para_fids = "f0Ds";

    //好友id，单个的
    public final static String para_fid = ")3S";

}
