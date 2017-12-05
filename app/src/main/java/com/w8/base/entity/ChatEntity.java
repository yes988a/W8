package com.w8.base.entity;

/**
 * 和pc端的chat一样。
 */
public class ChatEntity {
    /**
     * 用户ID
     */
    private String uid;

    private Long tim;

    /**
     * 请求者id：  好友id和群id
     */
    private String reqid;

    /**
     * 添加好友请求：typ_add_fri。
     */
    private Integer typ;

    /**
     * 是否发送给app的状态，typ_stat_ab  ，  typ_stat_abc
     */
    private Integer stat;

    /**
     * chat单聊Json：群聊消息发起者id，txt长度640，typ int(2): 0 文本 1图片 3附件 4链接。
     * （不管是群聊还是单聊，requid是：请求者ID(群id或人id)，长度只能是32位）
     * <p>
     * Chat群聊Json：群聊消息发起者id，txt长度640，typ int(2): 0 文本 1图片 3附件 4链接。
     * （不管是群聊还是单聊，requid是：请求者ID(群id或人id)，长度只能是32位）
     * <p>
     * delefri:fid，typ为删除类型标识 。。。。。。
     * <p>
     * frireq：(主键requid，请求方) ，(des为:reqaccount ，reqnickname 、reqdes、met），typ为类型标识。 。。。。。
     */
    private String des;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTim() {
        return tim;
    }

    public void setTim(Long tim) {
        this.tim = tim;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public Integer getTyp() {
        return typ;
    }

    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
