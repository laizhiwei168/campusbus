package com.zhiwei.campusbus.vo;

import java.io.Serializable;

public class UserInVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3662750765614398720L;
	private Integer userId;
	private String nickname;
	private String password;
	private String username;
	private String phone;
	private Integer status;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
