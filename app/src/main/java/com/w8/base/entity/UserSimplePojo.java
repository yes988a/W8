package com.w8.base.entity;

/**
 * 没有确定的需求，通用先,暂放
 *
 * @author syf
 */
public class UserSimplePojo {

    private String uid;

    private String account;

    private String nickname;

    private String bid;

    /**
     * 仅仅同意添加好友时使用
     */
    private String phone;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
