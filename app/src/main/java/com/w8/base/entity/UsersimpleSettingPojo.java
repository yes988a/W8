package com.w8.base.entity;

/**
 * 获取和更新我的设置时使用
 * 
 * @author syf
 * 
 */
public class UsersimpleSettingPojo {

	private String uid;
	private String nickname;// 昵称
	private String phone;// 电话
	private Integer sound;// 声音通知
	private Integer shake;// 震动
	private String autograph;// 签名

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSound() {
		return sound;
	}

	public void setSound(Integer sound) {
		this.sound = sound;
	}

	public Integer getShake() {
		return shake;
	}

	public void setShake(Integer shake) {
		this.shake = shake;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}
}
