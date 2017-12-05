package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 群成员
 */
@Entity
public class GroupFriend {

    @Id(autoincrement = true)
    private Long _id;
    /** Not-null value. */
    /**
     * 群ID
     */
    @NotNull
    private String gid;
    /** Not-null value. */
    /**
     * 用户id
     */
    @NotNull
    private String uid;
    /** Not-null value. */
    /**
     * 用户账号
     */
    @NotNull
    private String uaccount;
    /** Not-null value. */
    /**
     * 用户昵称
     */
    @NotNull
    private String uremark;
    @Generated(hash = 723159721)
    public GroupFriend(Long _id, @NotNull String gid, @NotNull String uid,
            @NotNull String uaccount, @NotNull String uremark) {
        this._id = _id;
        this.gid = gid;
        this.uid = uid;
        this.uaccount = uaccount;
        this.uremark = uremark;
    }
    @Generated(hash = 1780228753)
    public GroupFriend() {
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
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUaccount() {
        return this.uaccount;
    }
    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }
    public String getUremark() {
        return this.uremark;
    }
    public void setUremark(String uremark) {
        this.uremark = uremark;
    }

}
