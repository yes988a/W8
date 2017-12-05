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
public class Active {

    @Id(autoincrement = true)
    private Long _id;
    /** Not-null value. */
    /**
     * 通知唯一真实id
     */
    @NotNull@Unique
    private String uuid;
    /**
     * 标题
     */
    @NotNull
    private String title;
    /**
     * 正文描述
     */
    @NotNull
    private String des;
    /**
     * 参考:group_chat_disturb，nodata_erro等
     */
    @NotNull
    private Integer type;
    /**
     * 消息数量
     */
    @NotNull
    private int num;
    /** Not-null value. */
    /**
     * 请求时间字符串直接显示，如果没有时间，就存入''
     */
    @NotNull
    private String timstr;
    /**
     * 请求时间，可以为null，当我发起聊天请求但是没有聊天时
     */
    @NotNull
    private Long tim;
    @Generated(hash = 2125596085)
    public Active(Long _id, @NotNull String uuid, @NotNull String title,
            @NotNull String des, @NotNull Integer type, int num,
            @NotNull String timstr, @NotNull Long tim) {
        this._id = _id;
        this.uuid = uuid;
        this.title = title;
        this.des = des;
        this.type = type;
        this.num = num;
        this.timstr = timstr;
        this.tim = tim;
    }
    @Generated(hash = 1726193047)
    public Active() {
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
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDes() {
        return this.des;
    }
    public void setDes(String des) {
        this.des = des;
    }
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getTimstr() {
        return this.timstr;
    }
    public void setTimstr(String timstr) {
        this.timstr = timstr;
    }
    public Long getTim() {
        return this.tim;
    }
    public void setTim(Long tim) {
        this.tim = tim;
    }


}
