package com.w8.base.data;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 表情存储
 */
@Entity
public class Smile {

    @Id(autoincrement = true)
    private Long _id;
    /**
     * 图片ID
     */
    @NotNull
    @Index
    private int fnum;

    /** Not-null value. */
    /**
     * 中文名字
     */
    @NotNull
    private String sname;

    @Generated(hash = 1306072665)
    public Smile(Long _id, int fnum, @NotNull String sname) {
        this._id = _id;
        this.fnum = fnum;
        this.sname = sname;
    }

    @Generated(hash = 1033585201)
    public Smile() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getFnum() {
        return this.fnum;
    }

    public void setFnum(int fnum) {
        this.fnum = fnum;
    }

    public String getSname() {
        return this.sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }


}
