package com.yoga.demo.domain.dto;

public class UserLoginDTO {
	private String loginName;
 	private String password;
 	private String orgCode;
 	/**二维码*/
	private String code;
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "UserLoginDTO [loginName=" + loginName + ", password=" + password + ", orgCode=" + orgCode + ", code="
				+ code + "]";
	}
	
	
}
