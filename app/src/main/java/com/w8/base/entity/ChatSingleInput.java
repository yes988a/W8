package com.w8.base.entity;

public class ChatSingleInput {

    private String auid; // app端标记型id
    private String resuid;// 好友id，接受者
    private Integer typ;// 消息类型
    private String txt;// 消息内容


    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public String getResuid() {
        return resuid;
    }

    public void setResuid(String resuid) {
        this.resuid = resuid;
    }

    public Integer getTyp() {
        return typ;
    }

    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
