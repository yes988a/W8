package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fei on 2017/8/25.
 */
@Entity
public class Computer {

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
     * 对应的服务器ip。
     */
    @NotNull
    private String ipp;

    @Generated(hash = 2036043288)
    public Computer(Long _id, @NotNull String acc, @NotNull String ipp) {
        this._id = _id;
        this.acc = acc;
        this.ipp = ipp;
    }

    @Generated(hash = 1238779503)
    public Computer() {
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
