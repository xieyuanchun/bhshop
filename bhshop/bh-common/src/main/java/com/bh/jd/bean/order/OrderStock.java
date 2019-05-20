package com.bh.jd.bean.order;

import java.util.List;

import net.sf.json.JSONObject;

public class OrderStock {

	//京东配送信息
	
	
	private String jdOrderId; //京东Id
	
	private List<Track> orderTrack; //京东物流信息
	
	private String orderNo; //订单号
	
	private String address; //收货地址
	
	private String imgeUrl; //图片URL
	
	private String sentTo; //0 已送达,1配送中
	
	private JSONObject logistics;    //其他物流配送信息
	
	private String jd;    //是否是京东的，0速达、商家自配, 1其他，2是京东物流
	
	private String express_name; //快递公司
	
	private String express_no; //快递单号
	
	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public List<Track> getOrderTrack() {
		return orderTrack;
	}

	public void setOrderTrack(List<Track> orderTrack) {
		this.orderTrack = orderTrack;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImgeUrl() {
		return imgeUrl;
	}

	public void setImgeUrl(String imgeUrl) {
		this.imgeUrl = imgeUrl;
	}



	public JSONObject getLogistics() {
		return logistics;
	}

	public void setLogistics(JSONObject logistics) {
		this.logistics = logistics;
	}

	public String getJd() {
		return jd;
	}

	public void setJd(String jd) {
		this.jd = jd;
	}

	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public String getExpress_name() {
		return express_name;
	}

	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}

	public String getExpress_no() {
		return express_no;
	}

	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}

	
 
	

    
	
}
