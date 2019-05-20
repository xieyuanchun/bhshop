package com.bh.union.vo;

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
	public String msgId;//消息ID，原样返回
	public String msgSrc = UnionPayVODef.msgSrc;//消息来源
	public String openid;
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
	public String srcReserve;//请求系统预留字段
	public String mid=UnionPayVODef.mid;//商户号     h5:898310060514010 web:898310060514001
	public String tid=UnionPayVODef.tid;//终端号
	public String instMid="YUEDANDEFAULT";//业务类型 H5DEFAULT
	public UnionPayGoodsVO goods;//商品
	public String attachedData;//商户附加数据
	public UnionPaySubOrdersVO subOrders;//子订单,仅当有合并支付情况时，上送
	public String orderDesc;//账单描述
	public String originalAmount;//订单原始金额，单位分，用于记录前端系统打折前的金额
	public String totalAmount;//支付总金额，单位分
	public String notifyUrl="";//支付结果通知地址
	public String returnUrl="";//网页跳转地址
	public String systemId;//系统ID
	public String signType;//签名算法,值为：MD5或 SHA256；若不上送默认为MD5
	public String subOpenId;//用户子标识,商户自己公众号appid下的用户openid，可以通过微信oauth接口获取。必填字段，否则交易可能失败。
	public String name;//实名认证姓名
	public String mobile;//实名认证手机号
	public String certType;//实名认证证件类型
	public String certNo;//实名认证证件号
	public String fixBuyer;//是否需要实名认证
	public String sign;//签名  fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR
	public String tradeType;
	
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
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
	public String getSrcReserve() {
		return srcReserve;
	}
	public void setSrcReserve(String srcReserve) {
		this.srcReserve = srcReserve;
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
	public UnionPayGoodsVO getGoods() {
		return goods;
	}
	public void setGoods(UnionPayGoodsVO goods) {
		this.goods = goods;
	}
	public String getAttachedData() {
		return attachedData;
	}
	public void setAttachedData(String attachedData) {
		this.attachedData = attachedData;
	}
	public UnionPaySubOrdersVO getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(UnionPaySubOrdersVO subOrders) {
		this.subOrders = subOrders;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
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
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSubOpenId() {
		return subOpenId;
	}
	public void setSubOpenId(String subOpenId) {
		this.subOpenId = subOpenId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getFixBuyer() {
		return fixBuyer;
	}
	public void setFixBuyer(String fixBuyer) {
		this.fixBuyer = fixBuyer;
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
  