package com.w8.base.entity;

/**
 * 消息类型封装。 所有参数不可以为null
 * <p>
 * 0是FALSE 1是TRUE
 * <p>
 * 不需要有下面两种概念，，，当前选择使用，建立连接和其他业务分开。
 * //是否第一次建立连接。（配合c使用。）
 * private Integer f;
 * <p>
 * //requestid请求者id（配合f使用。）
 * private String d;
 * <p>
 * <p>
 * //是否保持长连接。keep（保持1,短连接0）
 * private Integer k;  根据业务类型判断吧
 * <p>
 * //请求来源reqcome：user? 服务器？（0或者null为user，大于0是服务器请求。）
 * private Integer c; 根据业务类型判断吧
 * //acceptid接受者id
 * private String a; 根据业务类型判断吧
 */
public class Tabloid_feiqi {

    //业务类型businesstyp，什么业务？登录，注册，等等。
    private String bu;

    //具体内容,json。可以是jsonobject，可以是实体类。也可以仅仅是一个参数。
    private String j;

    /**
     * 业务类型businesstyp，什么业务？登录，注册，等等。
     */
    public String getBu() {
        return bu;
    }

    /**
     * 业务类型businesstyp，什么业务？登录，注册，等等。
     */
    public void setBu(String bu) {
        this.bu = bu;
    }

    /**
     * 具体内容,json
     */
    public String getJ() {
        return j;
    }

    /**
     * 具体内容,json
     */
    public void setJ(String j) {
        this.j = j;
    }
}
