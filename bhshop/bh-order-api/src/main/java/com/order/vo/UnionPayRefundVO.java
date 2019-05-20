package com.order.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author xxj
 *
 */
public class UnionPayRefundVO implements Serializable{
	
	public String md5Key;
	public String msgId;//消息ID，原样返回
	public String msgSrc = "WWW.LONGXINMY.COM";//消息来源
	
	/**
	 *	
	 */
	public String msgType="WXPay.jsPay";//消息类型
	public Date requestTimestamp;//报文请求时间，格式yyyy-MM-dd HH:mm:ss
	public String merOrderId;//商户订单号
	public String mid="898445148160105";//商户号     h5:898310060514010 web:898310060514001
	public String tid="67105510";//终端号
	public String instMid="YUEDANDEFAULT";//业务类型 H5DEFAULT
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
	@Override
	public String toString() {
		return "UnionPayRefundVO [md5Key=" + md5Key + ", msgId=" + msgId + ", msgSrc=" + msgSrc + ", msgType=" + msgType
				+ ", requestTimestamp=" + requestTimestamp + ", merOrderId=" + merOrderId + ", mid=" + mid + ", tid="
				+ tid + ", instMid=" + instMid + ", signType=" + signType + ", sign=" + sign + ", refundAmount="
				+ refundAmount + "]";
	}
   
	
}
  