package com.w8.base.entity;

public class Ugroup {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 群id
     */
    private String gid;

    /**
     * 我在群中的备注名(昵称)
     */
    private String nickname;

    /**
     * 人对群的备注名
     */
    private String gremark;

    /**
     * 屏蔽
     */
    private Integer shie;

    private String bid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGremark() {
        return gremark;
    }

    public void setGremark(String gremark) {
        this.gremark = gremark;
    }

    public Integer getShie() {
        return shie;
    }

    public void setShie(Integer shie) {
        this.shie = shie;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
