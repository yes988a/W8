package com.w8.base.entity;

public class Urelation {
    /**
     * 我的id
     */
    private String uid;

    /**
     * 好友的id 32
     */
    private String fid;

    /**
     * 好友IP
     */
    private String bid;

    /**
     * 好友账号
     */
    private String account;

    /**
     * 我对好友的备注 64
     */
    private String remark;

    /**
     * 完全屏蔽 1为true 0为false
     */
    private Integer shie;

    /**
     * 好友的电话。不允许添加多个。
     */
    private String phone;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getShie() {
        return shie;
    }

    public void setShie(Integer shie) {
        this.shie = shie;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
