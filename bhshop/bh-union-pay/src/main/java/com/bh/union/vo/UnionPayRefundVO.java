package com.bh.union.vo;

import java.util.Date;

/**  
* 类说明   
*  
* @author homeway
* @email homeway.1984@163.com
* @date 2018年1月20日  新建  
*/
public class UnionPayRefundVO {
	
	public String md5Key;
	public String msgId;//消息ID，原样返回
	public String msgSrc =UnionPayVODef.msgSrc;//消息来源
	
	/**
	 *	
	 *  WXPay.jsPay:微信公众号支付
		trade.jsPay:支付宝
		qmf.jspay:全民付
		qmf.webPay:无卡
	 */
	public String msgType=UnionPayVODef.msgType;//消息类型
	public Date requestTimestamp;//报文请求时间，格式yyyy-MM-dd HH:mm:ss
	public String merOrderId;//商户订单号
	public String mid=UnionPayVODef.mid;//商户号     h5:898310060514010 web:898310060514001
	public String tid=UnionPayVODef.tid;//终端号
	public String instMid=UnionPayVODef.instMid;//业务类型 H5DEFAULT
	public String signType;//签名算法,值为：MD5或 SHA256；若不上送默认为MD5
	public String sign;//签名  fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR
	//退货金额
	public String refundAmount;
	
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getMsgSrc() {
		return msgSrc;
	}
	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Date getRequestTimestamp() {
		return requestTimestamp;
	}
	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	public String getMerOrderId() {
		return merOrderId;
	}
	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getInstMid() {
		return instMid;
	}
	public void setInstMid(String instMid) {
		this.instMid = instMid;
	}
	
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMd5Key() {
		return md5Key;
	}
	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	
}
  