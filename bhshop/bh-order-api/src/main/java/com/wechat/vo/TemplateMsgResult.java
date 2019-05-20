package com.wechat.vo;

public class TemplateMsgResult {

	//板消息 返回的结果 
	
    private String msgid; // 消息id(发送模板消息)  
	 
	private String errmsg; 
	
	private String errcode;

	 
	 
	 
	 
	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
}
