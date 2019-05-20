package com.bh.goods.pojo;

import java.util.Date;

public class GoodsComment {
	private Integer id;

	private Integer goodsId;

	private Integer shopId;

	private Integer mId;

	private Integer orderId;

	private Integer orderSkuId;

	private Byte level;

	private Date addtime;
	
	private String mName; //用户名称
	
	private String mHead; //用户头像
	
	private String image;
	
	private String[] imageStr; //评价图转数组

    private Integer star;

    private Integer isAddEvaluate;

    private Integer reid;
    
    private Integer notname;//是否匿名0匿名  1不匿名
    
    private String skuValue;

    private String description;
    
    private GoodsComment addEvaluate; //追评 
    
    private String goodsName; //商品名称
    
    private String goodsImage; //商品图片
    
    private Integer sortnum; //排序
    
    private Integer status; //状态 1显示 0隐藏
    
    private Integer isDel;//是否删除0表示未删除，1表示已删除
    
    private GoodsComment details; //评论详情
    private Object skuValueObj;//2017-11-10cheng添加
    private String levelName;//cheng
    private double skuPrice;
    private double totalPrice;
    private Integer num;
    
    private String currentPage;
    
    private String pageSize;
    private String addtime1;
    
	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getSortnum() {
		return sortnum;
	}

	public void setSortnum(Integer sortnum) {
		this.sortnum = sortnum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public double getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(double skuPrice) {
		this.skuPrice = skuPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String[] getImageStr() {
		return imageStr;
	}

	public void setImageStr(String[] imageStr) {
		this.imageStr = imageStr;
	}

	public GoodsComment getDetails() {
		return details;
	}

	public void setDetails(GoodsComment details) {
		this.details = details;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getmHead() {
		return mHead;
	}

	public void setmHead(String mHead) {
		this.mHead = mHead;
	}

	public GoodsComment getAddEvaluate() {
		return addEvaluate;
	}

	public void setAddEvaluate(GoodsComment addEvaluate) {
		this.addEvaluate = addEvaluate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Integer getIsAddEvaluate() {
		return isAddEvaluate;
	}

	public void setIsAddEvaluate(Integer isAddEvaluate) {
		this.isAddEvaluate = isAddEvaluate;
	}

	public Integer getReid() {
		return reid;
	}

	public void setReid(Integer reid) {
		this.reid = reid;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(Integer orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}
	
	public String getSkuValue() {
	    return skuValue;
	}
	
    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue == null ? null : skuValue.trim();
    }
	
    public String getDescription() {
        return description;
    }
	
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Object getSkuValueObj() {
		return skuValueObj;
	}

	public void setSkuValueObj(Object skuValueObj) {
		this.skuValueObj = skuValueObj;
	}

	public Integer getNotname() {
		return notname;
	}

	public void setNotname(Integer notname) {
		this.notname = notname;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getAddtime1() {
		return addtime1;
	}

	public void setAddtime1(String addtime1) {
		this.addtime1 = addtime1;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
    

}