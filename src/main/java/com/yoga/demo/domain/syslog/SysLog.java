package com.yoga.demo.domain.syslog;

import com.yoga.demo.domain.BaseDO;

/**
 *  系统日志
 * 
 * @author yoga
 */
public class SysLog extends BaseDO{

	private static final long serialVersionUID = -6476573532832459957L;

	private String username;
	
	private String action;
	
	private String ip;
	
	private String description;
	
	private String uri;
	
	private String requestBody;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	@Override
	public String toString() {
		return "SysLog [username=" + username + ", action=" + action + ", ip=" + ip + ", description=" + description
				+ ", uri=" + uri + ", requestBody=" + requestBody + "]";
	}

	
	
	
}
