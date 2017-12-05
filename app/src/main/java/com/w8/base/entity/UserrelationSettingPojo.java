package com.w8.base.entity;

/**
 * 更新我对好友设置，使用
 * 
 * @author syf
 * 
 */
public class UserrelationSettingPojo {
	private String uid;
	private String fid;
	private String remark;// 我对好友的备注
	private Integer shie;

	// ---------- 仅仅获取使用，更新时没有----------------
	private String phone;// 好友的电话号码

	private String bid;// 好友的电话号码

	private String nickname;// 好友昵称

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getShie() {
		return shie;
	}

	public void setShie(Integer shie) {
		this.shie = shie;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
