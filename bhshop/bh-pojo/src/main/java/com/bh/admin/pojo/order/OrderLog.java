package com.bh.admin.pojo.order;

import java.io.Serializable;
import java.util.Date;
/**
 * 订单日志实体类
 * @author xxj
 *
 */
public class OrderLog implements Serializable{
	private static final long serialVersionUID = 1L;
	//id
    private Integer id;
    //订单ID
    private Integer orderId;
    //订单号
    private String orderNo;
    //订单状态
    private Integer orderStatus;
    //操作用户名称
    private String adminUser;
    //用户类型  ：商家用户，购买用户，配送员
    private String userType;
    //用户ID
    private Integer userId;
    //用户表，普通用户，商家用户数据分别保存在不同表
    private String userTable;
    //操作行为
    private String action;
    //添加时间
    private Date addtime;
    //备注
    private String note;
    //商家id
    private Integer shopId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser == null ? null : adminUser.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserTable() {
        return userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable == null ? null : userTable.trim();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
    
    
}