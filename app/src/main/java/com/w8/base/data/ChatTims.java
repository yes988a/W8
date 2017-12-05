package com.w8.base.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 删除过期chat信息，失败记录
 */
@Entity
public class ChatTims {

    @Id(autoincrement = true)
    private Long _id;

    /**
     * 需要删除的时间。
     */
    @NotNull
    private long oldTim;

    @Generated(hash = 411368401)
    public ChatTims(Long _id, long oldTim) {
        this._id = _id;
        this.oldTim = oldTim;
    }

    @Generated(hash = 711545320)
    public ChatTims() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getOldTim() {
        return this.oldTim;
    }

    public void setOldTim(long oldTim) {
        this.oldTim = oldTim;
    }

}
