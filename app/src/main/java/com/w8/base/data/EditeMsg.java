package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 群或者好友未发送的编辑内容存储
 */
@Entity
public class EditeMsg {

    @Id(autoincrement = true)
    private Long _id;
    /** Not-null value. */
    /**
     * 群id或者好友id
     */
    @NotNull@Index
    private String uuid;
    /** Not-null value. */
    /**
     * 我正在编辑状态的内容
     */
    @NotNull
    private String txt;
    /**
     * 请求时间
     */
    @NotNull
    private long tim;
    @Generated(hash = 1556200716)
    public EditeMsg(Long _id, @NotNull String uuid, @NotNull String txt, long tim) {
        this._id = _id;
        this.uuid = uuid;
        this.txt = txt;
        this.tim = tim;
    }
    @Generated(hash = 699839761)
    public EditeMsg() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getTxt() {
        return this.txt;
    }
    public void setTxt(String txt) {
        this.txt = txt;
    }
    public long getTim() {
        return this.tim;
    }
    public void setTim(long tim) {
        this.tim = tim;
    }


}
