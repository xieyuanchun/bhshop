package com.order.bean;

import java.io.Serializable;
import java.util.Date;

public class UnionNoticeResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mid;
	private String tid;
	private String instMid;
	private String attachedData;
	private String bankCardNo;
	private String bankInfo;
	private String billFunds;
	private String billFundsDesc;
	private String buyerId;
	private String buyerUsername;
	private Integer couponAmount;
	private Integer buyerPayAmount;
	private Integer totalAmount;
	private Integer invoiceAmount;
	
	private String merOrderId;
	private Date payTime;
	private Integer receiptAmount;
	private String refId;
	private Integer refundAmount;
	private String refundDesc;
	private String seqId;
	
	private Date settleDate;
	private String status;
	private String subBuyerId;
	private String targetOrderId;
	private String targetSys;
	private String notifyId;
	private String sign;
	
	
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
	public String getAttachedData() {
		return attachedData;
	}
	public void setAttachedData(String attachedData) {
		this.attachedData = attachedData;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getBankInfo() {
		return bankInfo;
	}
	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}
	public String getBillFunds() {
		return billFunds;
	}
	public void setBillFunds(String billFunds) {
		this.billFunds = billFunds;
	}
	public String getBillFundsDesc() {
		return billFundsDesc;
	}
	public void setBillFundsDesc(String billFundsDesc) {
		this.billFundsDesc = billFundsDesc;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerUsername() {
		return buyerUsername;
	}
	public void setBuyerUsername(String buyerUsername) {
		this.buyerUsername = buyerUsername;
	}
	public Integer getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(Integer couponAmount) {
		this.couponAmount = couponAmount;
	}
	public Integer getBuyerPayAmount() {
		return buyerPayAmount;
	}
	public void setBuyerPayAmount(Integer buyerPayAmount) {
		this.buyerPayAmount = buyerPayAmount;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(Integer invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getMerOrderId() {
		return merOrderId;
	}
	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Integer getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(Integer receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public Integer getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(Integer refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundDesc() {
		return refundDesc;
	}
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	public Date getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubBuyerId() {
		return subBuyerId;
	}
	public void setSubBuyerId(String subBuyerId) {
		this.subBuyerId = subBuyerId;
	}
	public String getTargetOrderId() {
		return targetOrderId;
	}
	public void setTargetOrderId(String targetOrderId) {
		this.targetOrderId = targetOrderId;
	}
	public String getTargetSys() {
		return targetSys;
	}
	public void setTargetSys(String targetSys) {
		this.targetSys = targetSys;
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
