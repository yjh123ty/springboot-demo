package com.yoga.demo.domain.shiro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户信息")
public class UserInfo implements Serializable{
	private static final long serialVersionUID = -4671554168993353373L;
	
	public UserInfo() {
		super();
	}

	public UserInfo(String username, String password, Integer state, Boolean isDelete) {
		super();
		this.username = username;
		this.password = password;
		this.state = state;
		this.isDelete = isDelete;
	}

	public interface UserState{
		Integer DISABLE = 1;
		Integer UNCKECK = 2;
		Integer CHECKED = 3;
	}
	
	@ApiModelProperty(value = "ID")
	private Integer uid;
	
	@ApiModelProperty(value = "用户登录账号", required = true)
    private String username;//帐号
	
	@ApiModelProperty(value = "用户昵称", required = true)
    private String name;//名称（昵称或者真实姓名，不同系统不同定义）
	
	@ApiModelProperty(value = "密码", required = true)
    private String password; //密码
	
    private String idNo;	//身份证号
    private String email;	//邮箱
    private String mobile;	//手机号
    private String salt;//加密密码的盐
    private Integer state;//用户状态 ：  1：用户被停用,2:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 3:正常状态.
    private String headIcon;	//头像
    
    private Boolean isDelete;
    
    @ApiModelProperty(value = "记录创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private List<SysRole> roleList;// 一个用户具有多个角色
    private List<Integer> roleIds;// 一个用户具有多个角色
    
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<SysRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}
	
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}
	
	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	

	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.username+this.salt;
    }

	@Override
	public String toString() {
		return "UserInfo [uid=" + uid + ", username=" + username + ", name=" + name + ", password=" + password
				+ ", idNo=" + idNo + ", salt=" + salt + ", state=" + state + ", roleList=" + roleList + ", roleIds="
				+ roleIds + "]";
	}

	
    
}
