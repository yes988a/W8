package com.w8.base.entity;

/**
 * 更新我对好友设置，使用
 *
 * @author syf
 *
 */
public class UserrelationSimple {

	// 没必要，知道好友的电话号码和昵称？ 其他方式获取吧。
	// remark对应的英文名，app自己生成并存储吧。

	private String fid;

	private String remark;// 我对好友的备注

	private Integer shie;

	private Integer del;//是否已经被好友删除。0是正常，负数是已经被删除-1

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

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}
}
