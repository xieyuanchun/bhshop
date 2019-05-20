package com.bh.enums;

public enum OrderStatusEnum {
	// 订单状态 1生成订单,2支付订单,3已经发货,4,待评价,5,完成订单,6退款,7部分退款8用户取消订单,9作废订单,10退款中
	BUILD_ORDER(1, "生成订单"),
	PAY_ORDER(2, "支付订单");
	private int status;
	private String desc;

	private OrderStatusEnum(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
