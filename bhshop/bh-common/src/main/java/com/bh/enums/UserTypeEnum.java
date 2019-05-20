package com.bh.enums;

public enum UserTypeEnum {
	BUYER(1,"member", "购买用户"),
	SELLER(2,"member_shop_admin","商家用户"),
	SENDER(3,"member","配送用户"),
	DEFAULT(-1,"未知表","未知用户");
	private int type;
	private String userTable;
	private String desc;

	private UserTypeEnum(int type,String userTable,String desc) {
		this.type = type;
		this.userTable = userTable;
		this.desc = desc;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getUserTable() {
		return userTable;
	}
	public void setUserTable(String userTable) {
		this.userTable = userTable;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
