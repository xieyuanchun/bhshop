package com.bh.admin.pojo.order;


public class OrderInfoPojo {
	private  long watipayNumber ;//1待付款（未支付）的数量
	
	private long watimerchanNumber;// 2待发货
	
	private long sendmerchanNumber;//3已发货，待收货
	
	private long waticommentNumber;//5待评价
	
	private long refund;//DEFAULT '0'  '是否退款:0否，1是',
	
	private long share;//待分享的数量
	

	public long getRefund() {
		return refund;
	}

	public void setRefund(long refund) {
		this.refund = refund;
	}

	public long getWatipayNumber() {
		return watipayNumber;
	}

	public void setWatipayNumber(long watipayNumber) {
		this.watipayNumber = watipayNumber;
	}

	public long getWatimerchanNumber() {
		return watimerchanNumber;
	}

	public void setWatimerchanNumber(long watimerchanNumber) {
		this.watimerchanNumber = watimerchanNumber;
	}

	public long getSendmerchanNumber() {
		return sendmerchanNumber;
	}

	public void setSendmerchanNumber(long sendmerchanNumber) {
		this.sendmerchanNumber = sendmerchanNumber;
	}

	public long getWaticommentNumber() {
		return waticommentNumber;
	}

	public void setWaticommentNumber(long waticommentNumber) {
		this.waticommentNumber = waticommentNumber;
	}

	public long getShare() {
		return share;
	}

	public void setShare(long share) {
		this.share = share;
	}

	
	
	
}
