package com.yoga.demo.common;

import com.alibaba.fastjson.JSON;

public class JsonMsgBean extends JSON {

    private Boolean success;// 是否成功
    private String message;// 消息
    private byte code;// 状态码
    private Object data;// 返回对象

    public JsonMsgBean() {
    }

    public JsonMsgBean(Boolean success, String message ,byte code) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public JsonMsgBean(Boolean success, String msg, byte code, Object data) {
        this.success = success;
        this.message = msg;
        this.code = code;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
        return data;
    }

    public void setObject(Object data) {
        this.data = data;
    }
    
    public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "JsonMsgBean [success=" + success + ", message=" + message + ", code=" + code + ", data=" + data
				+ "]";
	}
	
}
