package com.w8.base.pcurl;

public class GroupUtilA {

    //查询我的群列表简单信息.。
    public final static int url_app_findGroupSimples = 9456;

    //查询群成员简单信息。
    public final static int url_app_findGroupUsersSimple = 4142;

    //修改，群备注。
    public final static int url_app_updateGroupRemark = 5425;

    //修改，我在群中的备注。
    public final static int url_app_updateUserRemark = 5514;

    //修改，我在群中的备注。
    public final static int url_app_updateShie = 1613;

    //(新增群)仅仅添加一条，我对群的设置信息。
    public final static int url_app_addGroup = 2621;

    // 邀请好友加入群（通知其他服务器。发送信息时每次都带群成员数目，来验证群成员是否正确，如果不正确？对比缺少谁？然后到相应人员服务器为准。（入校验系统。）。）
    public final static int url_app_invitation = 8290;

    //自己退出群，没有被群主删除使用此方法 (删除群某条信息)
    public final static int url_app_quitGroup = 4545;

    //修改，群名称。（群主权限）（等同于发送啦一条特殊聊天记录。具体更新由定时任务来做）
    public final static int url_app_updateGname = 1282;

    //保存群公告。（群主权限）（等同于发送啦一条特殊聊天记录。具体更新由定时任务来做）
    public final static int url_app_updateGotice = 1623;

    //群ID
    public final static String para_gid = "gid";

    //用户对群名称备注。
    public final static String para_gropremark = "Ewg";

    //用户在群中的对自己的备注。
    public final static String para_userremark = "3gU";

    //设置群的通知情况
    public final static String para_groupshie = "G9E";

    //设置群的名称
    public final static String para_groupname = "sn0a";

    //群公告
    public final static String para_groupnotice = "g3nNe";

    //群在摸个服务器的人数.
    public final static String para_cid_usernum = "U35n";

    /**
     * 验证群名称，至少一位，最多16
     */
    public final static boolean testGroupNickname(String name) {
        if(name==null){
            return false;
        }else {
            String ss = name.trim();
            if (ss == null || ss.length() < 2 || ss.length() > 32) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 验证群公告，至少三位，最多160
     */
    public final static boolean testGroupNotice(String name) {
        if(name==null){
            return false;
        }else {
            String ss = name.trim();
            if (ss == null || ss.length() < 2 || ss.length() > 160) {
                return false;
            } else {
                return true;
            }
        }
    }

}
