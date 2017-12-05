package com.w8.base.entity;

public class Guser {
    /**
     * 群ID
     */
    private String gid;

    /**
     * 用户主键
     */
    private String uid;

    /**
     * 对应的IP
     */
    private String bid;

    /**
     * 用户登录名，如yes988a
     */
    private String uaccount;

    /**
     * 用户在群中的名字
     */
    private String uremark;


    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUaccount() {
        return uaccount;
    }

    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }

    public String getUremark() {
        return uremark;
    }

    public void setUremark(String uremark) {
        this.uremark = uremark;
    }
}
