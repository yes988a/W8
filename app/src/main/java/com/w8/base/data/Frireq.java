package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 好友请求和添加
 */
@Entity
public class Frireq {

    @Id(autoincrement = true)
    private Long _id;
    /** Not-null value. */
    /**
     * 请求方，主键
     */
    @NotNull
    private String requid;
    /** Not-null value. */
    /**
     * 接收方，主键，
     */
    @NotNull
    private String resuid;
    /** Not-null value. */
    /**
     * 请求方，账号
     */
    @NotNull
    private String reqaccount;
    /** Not-null value. */
    /**
     * 请求方，昵称
     */
    @NotNull
    private String reqnickname;
    /** Not-null value. */
    /**
     * 请求方，申请描述
     */
    @NotNull
    private String reqdes;
    /**
     * 时间，请求时间，当同意后更改为同意时间
     */
    @NotNull
    private long tim;
    /** Not-null value. */
    /**
     * 请求方，是否认识
     */
    @NotNull
    private String met;
    /**
     * 0未处理，，， 我接受后处理：positive
     */
    @NotNull
    private int typ;
    @Generated(hash = 1345105329)
    public Frireq(Long _id, @NotNull String requid, @NotNull String resuid,
            @NotNull String reqaccount, @NotNull String reqnickname,
            @NotNull String reqdes, long tim, @NotNull String met, int typ) {
        this._id = _id;
        this.requid = requid;
        this.resuid = resuid;
        this.reqaccount = reqaccount;
        this.reqnickname = reqnickname;
        this.reqdes = reqdes;
        this.tim = tim;
        this.met = met;
        this.typ = typ;
    }
    @Generated(hash = 566465387)
    public Frireq() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getRequid() {
        return this.requid;
    }
    public void setRequid(String requid) {
        this.requid = requid;
    }
    public String getResuid() {
        return this.resuid;
    }
    public void setResuid(String resuid) {
        this.resuid = resuid;
    }
    public String getReqaccount() {
        return this.reqaccount;
    }
    public void setReqaccount(String reqaccount) {
        this.reqaccount = reqaccount;
    }
    public String getReqnickname() {
        return this.reqnickname;
    }
    public void setReqnickname(String reqnickname) {
        this.reqnickname = reqnickname;
    }
    public String getReqdes() {
        return this.reqdes;
    }
    public void setReqdes(String reqdes) {
        this.reqdes = reqdes;
    }
    public long getTim() {
        return this.tim;
    }
    public void setTim(long tim) {
        this.tim = tim;
    }
    public String getMet() {
        return this.met;
    }
    public void setMet(String met) {
        this.met = met;
    }
    public int getTyp() {
        return this.typ;
    }
    public void setTyp(int typ) {
        this.typ = typ;
    }
}
