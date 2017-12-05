package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 活动状态的通知
 */
@Entity
public class AccIp {

    @Id(autoincrement = true)
    private Long _id;
    /** Not-null value. */
    /**
     * 账号。
     */
    @NotNull
    @Unique
    private String acc;
    /**
     * ipp地址。如：127.0.0.1:9980 ，如果默认端口是80不需要写端口是127.0.01
     */
    @NotNull
    private String ipp;
    @Generated(hash = 313151982)
    public AccIp(Long _id, @NotNull String acc, @NotNull String ipp) {
        this._id = _id;
        this.acc = acc;
        this.ipp = ipp;
    }
    @Generated(hash = 153699009)
    public AccIp() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getAcc() {
        return this.acc;
    }
    public void setAcc(String acc) {
        this.acc = acc;
    }
    public String getIpp() {
        return this.ipp;
    }
    public void setIpp(String ipp) {
        this.ipp = ipp;
    }
}
