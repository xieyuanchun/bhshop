package com.bh.admin.pojo.order;

import java.io.Serializable;

public class SmallPayRefundPojo implements Serializable{
	private static final long serialVersionUID = 3347934397481321543L;
	//小程序ID:微信分配的小程序ID
	private String appid;
	//商户号:微信支付分配的商户号
	private String mch_id;
	//随机字符串:随机字符串，不长于32位。推荐随机数生成算法
	private String nonce_str;
	//签名:签名，详见签名生成算法
	private String sign;
	//商户订单号:商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
	private String out_trade_no;
	//商户退款单号:商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
	private String out_refund_no;
	//订单金额:订单总金额，单位为分，只能为整数，详见支付金额
	private String total_fee;
	//退款金额:退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
	private String refund_fee;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	
	
}
