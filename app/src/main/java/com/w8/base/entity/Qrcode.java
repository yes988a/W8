package com.w8.base.entity;

/**
 * @author 
 */
public class Qrcode {
    /**
     * 二维码主键，也是二维码内容
     */
    private String qrid;

    /**
     * 对应的用户、或者群、或者广告的主键
     */
    private String relatid;

    /**
     * qrcode_常量前缀
     */
    private Integer typ;

    /**
     * 负载均衡主键。
     */
    private String bid;

    private static final long serialVersionUID = 1L;

    public String getQrid() {
        return qrid;
    }

    public void setQrid(String qrid) {
        this.qrid = qrid;
    }

    public String getRelatid() {
        return relatid;
    }

    public void setRelatid(String relatid) {
        this.relatid = relatid;
    }

    public Integer getTyp() {
        return typ;
    }

    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}