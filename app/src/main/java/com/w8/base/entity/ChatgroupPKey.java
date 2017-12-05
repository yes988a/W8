package com.w8.base.entity;

/**
 * 群消息分发使用（跨服务器he服务组)
 * 
 * @author syf
 * 
 */
public class ChatgroupPKey {

	/**
	 * 群ID
	 */
	private String gid;

	/**
	 * 时间
	 */
	private long tim;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public long getTim() {
		return tim;
	}

	public void setTim(long tim) {
		this.tim = tim;
	}
}
