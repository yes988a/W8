package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 单聊信息
 */
@Entity
public class Chat {

    @Id(autoincrement = true)
    private Long _id;

    /** Not-null value. */
    /**
     * 请求者ID（群聊时：指群成员id、包括我自己）（单聊时，指发送者id、也包含我和好友。）
     */
    @NotNull
    private String requid;
    /** Not-null value. */
    /**
     * 接收者ID（群聊时：群id。）（单聊时：我的id，或者，好友id。）
     *
     * 显示： 群聊时，resuid既是群的所有信息。
     * 显示： 单聊时，好友id为requid我的id是resuid，，或者，，好友id是resuid我的id是requid即为某好友的单聊信息。
     */
    @NotNull
    private String resuid;

    /**
     * 时间（删除某条信息时：requid和Tim确定唯一一条数据。）
     */
    @NotNull
    private long tim;

    /**
     * 类型 0 文本 1图片 3附件 4链接 其它
     */
    @NotNull
    private int typ;
    /** Not-null value. */
    /**
     * 文本 内容
     */
    @NotNull
    private String txt;

    /**
     * a1-->p2-->b3-->p2 0end
     */
    @NotNull
    private int stat;

    @Generated(hash = 1054477464)
    public Chat(Long _id, @NotNull String requid, @NotNull String resuid, long tim,
            int typ, @NotNull String txt, int stat) {
        this._id = _id;
        this.requid = requid;
        this.resuid = resuid;
        this.tim = tim;
        this.typ = typ;
        this.txt = txt;
        this.stat = stat;
    }

    @Generated(hash = 519536279)
    public Chat() {
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

    public long getTim() {
        return this.tim;
    }

    public void setTim(long tim) {
        this.tim = tim;
    }

    public int getTyp() {
        return this.typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public String getTxt() {
        return this.txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getStat() {
        return this.stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

}
