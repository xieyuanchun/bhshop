package com.order.vo;

import java.util.Date;

/**  
* 类说明   
*  
* @author homeway
* @email homeway.1984@163.com
* @date 2018年1月20日  新建  
*/
public class UnionPayVO {
	public String md5Key;
	public String msgType="WXPay.jsPay";//消息类型
	public String merOrderId;//商户订单号
	public String originalAmount;//订单原始金额，单位分，用于记录前端系统打折前的金额
	public String totalAmount;//支付总金额，单位分
	public String attachedData;
	public String openid;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/*public String isTeam="0";
	public String getIsTeam() {
		return isTeam;
	}
	public void setIsTeam(String isTeam) {
		this.isTeam = isTeam;
	}*/
	public String getAttachedData() {
		return attachedData;
	}
	public void setAttachedData(String attachedData) {
		this.attachedData = attachedData;
	}
	public String getMd5Key() {
		return md5Key;
	}
	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMerOrderId() {
		return merOrderId;
	}
	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}
	public String getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	
	
}
  