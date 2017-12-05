package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 我的群信息
 */
@Entity
public class Group {

    @Id(autoincrement = true)
    private Long _id;
    /**
     * Not-null value.
     */
    @NotNull
    @Index
    private String gid;
    /**
     * 群名字，可以为null，当点击群详情时查询群服务器
     */
    @NotNull
    private String gname;
    /** Not-null value. */
    /**
     * 我对群的备注名
     */
    @NotNull
    private String gremark;
    /** Not-null value. */
    /**
     * 我在群中的名称
     */
    @NotNull
    private String nickname;
    /**
     * 群公告，可以为null，当点击群详情时查询群服务器
     */
    @NotNull
    private String gotice;
    /**
     * 群主ID，可以为null，当点击群详情时查询群服务器，或者app在使用到时获取
     */
    @NotNull
    private String masterid;
    /**
     * 完全屏蔽 positive
     */
    @NotNull
    private int shie;
    /**
     * 用户数量，可以为null，当点击群详情时查询群服务器
     */
    @NotNull
    private Integer usernum;
    @Generated(hash = 730753866)
    public Group(Long _id, @NotNull String gid, @NotNull String gname,
            @NotNull String gremark, @NotNull String nickname,
            @NotNull String gotice, @NotNull String masterid, int shie,
            @NotNull Integer usernum) {
        this._id = _id;
        this.gid = gid;
        this.gname = gname;
        this.gremark = gremark;
        this.nickname = nickname;
        this.gotice = gotice;
        this.masterid = masterid;
        this.shie = shie;
        this.usernum = usernum;
    }
    @Generated(hash = 117982048)
    public Group() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getGid() {
        return this.gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getGname() {
        return this.gname;
    }
    public void setGname(String gname) {
        this.gname = gname;
    }
    public String getGremark() {
        return this.gremark;
    }
    public void setGremark(String gremark) {
        this.gremark = gremark;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getGotice() {
        return this.gotice;
    }
    public void setGotice(String gotice) {
        this.gotice = gotice;
    }
    public String getMasterid() {
        return this.masterid;
    }
    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }
    public int getShie() {
        return this.shie;
    }
    public void setShie(int shie) {
        this.shie = shie;
    }
    public Integer getUsernum() {
        return this.usernum;
    }
    public void setUsernum(Integer usernum) {
        this.usernum = usernum;
    }

}
