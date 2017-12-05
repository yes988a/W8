package com.w8.base.entity;

/**
 * 群消息，app传入参数
 * 
 * @author syf
 * 
 */
public class ChatGroupInput {

	/**
	 * 消息接收者
	 */
	private String gid;

	/**
	 * 类型 0 文本 1图片 3附件 4链接 其它
	 */
	private int typ;
	/**
	 * 文本 内容
	 */
	private String txt;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public int getTyp() {
		return typ;
	}

	public void setTyp(int typ) {
		this.typ = typ;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}
}
