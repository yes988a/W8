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
     * 请求者id：  好友id、群id、其他组织id
     */
    private String reqid;

    /**
     * 请求类型：type_app.md。
     */
    private Integer btyp;

    /**
     * 请求内容类型：type_app.md。
     */
    private Integer dtyp;

    /**
     * 单聊Json：txt长度640，。
     * （不管是群聊还是单聊，requid是：请求者ID(群id或人id)，长度只能是32位）
     * <p>
     * 群聊Json：群聊消息发起者id，txt长度640，。
     * （不管是群聊还是单聊，requid是：请求者ID(群id或人id)，长度只能是32位）
     * <p>
     * delefri:fid，：：：des不需要有内容。
     * <p>
     * frireq：(des为:reqaccount ，reqnickname 、reqdes、met），
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

    public Integer getBtyp() {
        return btyp;
    }

    public void setBtyp(Integer btyp) {
        this.btyp = btyp;
    }

    public Integer getDtyp() {
        return dtyp;
    }

    public void setDtyp(Integer dtyp) {
        this.dtyp = dtyp;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
