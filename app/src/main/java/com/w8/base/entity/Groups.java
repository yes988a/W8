package com.w8.base.entity;

/**
 * @author 
 */
public class Groups {
    /**
     * 唯一标示
     */
    private String gid;

    /**
     * 群名称
     */
    private String gname;

    /**
     * 群公告
     */
    private String gotice;

    /**
     * 负均衡ID
     */
    private String bid;

    /**
     * 仅仅用于校验app端群人数是否正确
     */
    private Integer usernum;

    /**
     * 群主
     */
    private String masterid;

    private static final long serialVersionUID = 1L;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGotice() {
        return gotice;
    }

    public void setGotice(String gotice) {
        this.gotice = gotice;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public Integer getUsernum() {
        return usernum;
    }

    public void setUsernum(Integer usernum) {
        this.usernum = usernum;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }
}