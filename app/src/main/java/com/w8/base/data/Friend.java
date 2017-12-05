package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 好友列表
 */
@Entity
public class Friend {

    @Id(autoincrement = true)
    private Long _id;
    /**
     * 好友id
     */
    @NotNull
    @Index
    private String fid;
    /** Not-null value. */
    /**
     * 好友账号
     */
    @NotNull
    private String account;
    /**
     * 好友电话号码
     */
    @NotNull
    private String phone;
    /** Not-null value. */
    /**
     * 昵称
     */
    @NotNull
    private String nickname;
    /** Not-null value. */
    /**
     * 备注
     */
    @NotNull
    private String remark;
    /** Not-null value. */
    /**
     * remark转英文。
     */
    @NotNull
    private String abc;
    /** Not-null value. */
    /**
     * 首字母是abc？
     */
    @NotNull
    private String first_abc;
    /**
     * val_positive
     * 是否显示abc
     */
    @NotNull
    private int showabc;
    /**
     * 完全屏蔽 positive
     */
    @NotNull
    private int shie;
    /**
     * 第一个位是大类型，第二位是排序:60好友  50标签  41新朋友  42群聊   43标签添加   44我的
     */
    @NotNull
    private int typ;
    /**
     * 活跃程度
     */
    @NotNull
    private int degree;
    @Generated(hash = 904704184)
    public Friend(Long _id, @NotNull String fid, @NotNull String account,
            @NotNull String phone, @NotNull String nickname, @NotNull String remark,
            @NotNull String abc, @NotNull String first_abc, int showabc, int shie,
            int typ, int degree) {
        this._id = _id;
        this.fid = fid;
        this.account = account;
        this.phone = phone;
        this.nickname = nickname;
        this.remark = remark;
        this.abc = abc;
        this.first_abc = first_abc;
        this.showabc = showabc;
        this.shie = shie;
        this.typ = typ;
        this.degree = degree;
    }
    @Generated(hash = 287143722)
    public Friend() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getFid() {
        return this.fid;
    }
    public void setFid(String fid) {
        this.fid = fid;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getAbc() {
        return this.abc;
    }
    public void setAbc(String abc) {
        this.abc = abc;
    }
    public String getFirst_abc() {
        return this.first_abc;
    }
    public void setFirst_abc(String first_abc) {
        this.first_abc = first_abc;
    }
    public int getShowabc() {
        return this.showabc;
    }
    public void setShowabc(int showabc) {
        this.showabc = showabc;
    }
    public int getShie() {
        return this.shie;
    }
    public void setShie(int shie) {
        this.shie = shie;
    }
    public int getTyp() {
        return this.typ;
    }
    public void setTyp(int typ) {
        this.typ = typ;
    }
    public int getDegree() {
        return this.degree;
    }
    public void setDegree(int degree) {
        this.degree = degree;
    }

    
}
