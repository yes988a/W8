package com.w8.base.pcurl;

/**
 * rat有app发送请求等。端应该有相应的所有ser更新。
 */
public class MineUtil {

    //查询我的个人设置
    public final static int url_app_findMysetting = 4063;

    //修改我的昵称，对应的通知其他用户更新   @return rt_8用户不存在或者其他错误
    public final static int url_app_updateMyNickname = 6792;

    //修改，声音通知
    public final static int url_app_updateMysound = 2633;

    //修改，签名
    public final static int url_app_updateMyautograph = 3202;

    //更新我的昵称。 （关于时间冲突问题：
    // 如果这次，带时间过期，当更新成功后，时间改成最新，但其实，以前的一个其他修改，没有成功同步到其他服务器。
    // 所有，要么不依赖时间，要么，每个更新都对应一个时间，但是如果每个都对应时间，又何必要时间，直接对比值即可
    // 方法二、时间不一样，每次不仅仅更新如昵称，要更新，同级别的所有属性如sound、autograph、nickname
    // 三、pas应该为“”，因为不是自己的服务器。也可以其他
    // 四、手机号更改单独有个时间，因为，需要同时更改其他人员的手机操作。
    // 所以，不需要有url_ser_updateMyNickname这样的url，而是url_ser_update_mine）
    public final static int url_ser_update_mine = 7642;

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
