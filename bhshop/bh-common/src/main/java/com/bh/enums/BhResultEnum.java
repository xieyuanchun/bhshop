package com.bh.enums;

public enum BhResultEnum {
	SUCCESS(200,"操作成功"),
	GAIN_SUCCESS(200, "获取成功"),
	DELETE_SUCCESS(200, "删除成功"),
	LOGIN_GAIN_FAIL(200, "已登录，获取失败"),
	DELETE_FAIL(400, "删除失败"),
	GAIN_FAIL(400, "暂无数据！"),
	REQUEST_FAIL(400, "请求失败"),
	NAME_EXIST(400, "名称已存在"),
	ERROR_EMAIL(400, "邮箱格式不正确"),
	ERROR_MOBILE(400, "手机格式不正确"),
	TOPIC_GOING(400, "活动进行中，不可操作"),
	GOODS_EXCIT(400, "商品已存在，请勿重复报名"),
	GOODS_PRIZE_ZERO(400, "商品拍卖价不能为0"),
	GOODS_EXCIT_NOT_DOWN(400, "该商品所在活动未下架"),
	IS_GET(400, "该商品已入库，请勿重复摘取"),
	JD_IMAGE(400, "京东图片接口异常"),
	JD_DETAILS(400, "京东详情接口异常"),
	JD_SELLPRICE(400, "京东价格接口异常"),
	BATCH_DELETE_FAIL(400, "不允许删除，该sku有对应的订单记录"),
	DELETE_BATCH_FAIL(400, "删除失败，请先删除子类"),
	DELETE_EXIT(400, "删除失败，已被引用"),
	OTHER_FAIL(1000, "该广告已被绑定，不能修改为主页轮播"),
	VISIT_FAIL(500, "操作失败"),
	NULL_FAIL(500, "用户信息为空"),
	LOGIN_FAIL(100, "未登录"),
	WX_LOGIN_FAIL(100, "微信授权信息不能为空"),
	TOPIC_END(400, "砍价已完成"),
	TOPIC_GET(400, "您已参与过，请勿重复砍价"),
	TOPIC_OUT(400, "今天的帮砍次数已用完"),
	ILLEGALITY_FAIL(300, "非法操作"),
	ORDER_FAIL(600, "该单已被接"),
	BRAND_EXIT(1000,"该品牌已存在"),
	BRAND_CHOICED(999,"该品牌已被引用"),
	GOODS_NOT_EXIT(10000,"该商品不在池中"),
	GOODS_IS_DOWN(666,"该商品已下架"),
	GOODS_NOT_UP(400,"该商品未上架"),
	LOCATION_FAIL(500,"定位失败"),
	PRIZE_GET(400, "您已参与过，请勿重复参加此次抽奖活动"),
	FAIL(400,"操作失败"),
	SAVEMONEY(400,"你已经参加过活动，不能重复参加"), 
	DELETE_SUBCATEGORY(400,"请先删除子分类"),
	BRAND_BINDING(400,"该分类已被品牌所绑定"),
	MODEL_BINDING(400,"该分类已被模型所绑定"),
	GOODS_BINDING(400,"该分类已与商品绑定"),
	JDSKUNO_ISEXICT(400, "该京东商品已存在"),;
	
	private BhResultEnum(Integer status,String msg){
		this.status = status;
		this.msg = msg;
	}
	private Integer status;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private String msg;
	
}
