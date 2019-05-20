package com.bh.admin.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.order.TeamLastOne;

public class ShareResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer skuId; //商品skuid
	private Integer goodsId; //商品id
	private String name; // 商品名称
	private double teamPrice; //团购价
	private double bhPrice;//节省价格
	private String goodsImage; //商品图片
	private Integer teamNum; // 多少人正在拼团
	private Integer finishNum; //已拼多少件
	private Integer waitNum; //还差多少人成团
	private String headimgurl; //团主头像
	private String userName; //用户昵称
	private String waitTime; //结束时间
	private List<OrderTeam> teamList; //拼团列表
	private Goods goods; // 商品基本信息
	private TeamLastOne teamLastOne;
	
	
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public Integer getSkuId() {
		return skuId;
	}
	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTeamPrice() {
		return teamPrice;
	}
	public void setTeamPrice(double teamPrice) {
		this.teamPrice = teamPrice;
	}
	public double getBhPrice() {
		return bhPrice;
	}
	public void setBhPrice(double bhPrice) {
		this.bhPrice = bhPrice;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public Integer getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}
	public Integer getFinishNum() {
		return finishNum;
	}
	public void setFinishNum(Integer finishNum) {
		this.finishNum = finishNum;
	}
	public Integer getWaitNum() {
		return waitNum;
	}
	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}
	
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	public List<OrderTeam> getTeamList() {
		return teamList;
	}
	public void setTeamList(List<OrderTeam> teamList) {
		this.teamList = teamList;
	}
	public TeamLastOne getTeamLastOne() {
		return teamLastOne;
	}
	public void setTeamLastOne(TeamLastOne teamLastOne) {
		this.teamLastOne = teamLastOne;
	}
	
	
	
}
