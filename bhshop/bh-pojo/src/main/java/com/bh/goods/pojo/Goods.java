package com.bh.goods.pojo;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class Goods implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3279394001747875611L;
	
	private Integer actId;//对于topic_goods表中的act_id字段  xieyc

	private Integer id;

    private String name;
    
    private String title;

    private Integer modelId;

    private Long catId;

    private Integer shopCatId;

    private Long brandId;

    private Integer sellPrice;

    private Integer marketPrice;

    private Date upTime;

    private Date downTime;

    private Timestamp addtime;

    private Timestamp edittime;

    private String image;

    private Integer storeNums;

    private String unit;

    private Integer status;

    private Integer visit;

    private Integer favorite;

    private Short sortnum;

    private Integer comments;

    private Integer sale;

    private Integer shopId;

    private Boolean isHot;

    private Boolean isNew;//true=1
    
    private Integer isHotShop;//如果=1则该商品的最热商品

    private Integer isNewShop;

    private Boolean isFlag;

    private Boolean isShopFlag;
    
    private Integer deliveryPrice;
    
    private Integer refundDays;
    
    private String reason;
    private String publicimg;
    
    private Integer saleType;

    private Integer teamNum;

    private Integer teamEndTime;
    
    private Integer timeUnit;
    
    private Integer isPromote;
    
    private Integer teamPrice;
    
    private Integer isCreate;
    
    private Integer isJd;
    
    private Integer topicType;
    
    private String tagIds;
    
    private Short shopsortnum;
    
    private Integer fixedSale;//已售数量
    
    private Integer isPopular;//218-3-15 是否拼人气， -1否 0是
    
    private String catName; //分类名称
    
    private String goodStatus; //商品所属状态0热门上架、1上架 zlk
    
    private Long catIdOne; //一级分类
    
    private Long catIdTwo; //二级分类
    
    private Timestamp applyTime;
    
    private String outReason;
    
    private Integer auctionPrice; //拍卖价
    
    private Double realAuctionPrice;
    
    private Integer topicGoodsId;
    
    private Integer goodBuyLimit;
    
    private Integer visible; //是否显示
    
    private Integer kuNums; //拍卖库存
    
    private String tagName; //商品标签
   
    private String sendArea; //配送区域
    
    private String deductibleRate;//抵扣率
    
	private String[] tagIdsValue;
    private double realTeamPrice;
    private double deliveryRealPrice; //配送费转化的价格
    
    private String shopName; //所属店铺名称
    
    private Integer shopLevel;//店铺等级
    
    private double realPrice; //商品销售价展示
    
    private double markRealPrice; //商品市场价展示
    
    private String description; //添加属性---商品描述
    
    private String category; //分类名称
    
    private String categoryImage; //分类图片
    
    private String goodsStatus;//商品状态
    
    private List<GoodsSku> skuList; //商品规格属性
    
    private List<GoodsImage> imageUrl; //商品轮播图
    
    private List<String> imageList; //商品sku轮播图
    
    private List<GoodsAttr> attrList;// 商品属性值列表
    
    private Integer skuId;//商品skuId
    
    private Integer top; //商品销售排行
    
    private Integer groupCount; //已拼多少单
    
    private String[] userGroupHead; //最近拼单人头像
    
    private GoodsSku goodsSku; //
    
    private List<GoodsCategory> categoryList; //京东商品摘取使用
    
    private String brandName; //品牌名称
    
    private String appdescription; //app商品描述
    
    private Map<String, Object> shopInfo; //店铺信息
    
    private String currentPage; //起始页
    
    private int pageSize; //每页取多少条数据 2018.5.16 zlk
    
    private int currentPageIndex; //第几条开始取 2018.5.16 zlk
    
    private Integer tgId; //活动商品明细id
    private Integer startPrice;//开始的价格-cheng
    private Integer endPrice;//结束的价格-cheng
    
    private String shopCategoryName; //店铺分类名称 zlk 2018.319
    
    private String sort; //zlk  按 时间(1降序、2升序)、销量(3降序、5升序)、成交额(6降序、7升序)、库存 排序(8降序、9升序)
    
    private Integer onSaleNum; //zlk 上架数量
    
    private Integer notOnSaleNum; //zlk下架数量
    
    private Integer mId; //2018.5.7 zlk 登录id
    
    private String collect;//2018.5.7 zlk 收藏  0是，1否
    
    private Integer min;

    private Integer max;
    
    
    private Integer score;//滨惠豆
    
    private String bhdPrice;//滨惠豆价格

    
    private List<String> catIdList;
    
    private String keyOne;
    
    private String keyTwo;
    
    private String keyThree;
    
    private JSONArray modelValue;
    
    private List childList;
    
    private Map<String, Object> dauctionDetail;
    
    public Short getShopsortnum() {
		return shopsortnum;
	}

	public void setShopsortnum(Short shopsortnum) {
		this.shopsortnum = shopsortnum;
	}
    
	public String getKeyOne() {
		return keyOne;
	}

	public void setKeyOne(String keyOne) {
		this.keyOne = keyOne;
	}

	public String getKeyTwo() {
		return keyTwo;
	}

	public void setKeyTwo(String keyTwo) {
		this.keyTwo = keyTwo;
	}

	public String getKeyThree() {
		return keyThree;
	}

	public void setKeyThree(String keyThree) {
		this.keyThree = keyThree;
	}

	public String[] getTagIdsValue() {
		return tagIdsValue;
	}

	public void setTagIdsValue(String[] tagIdsValue) {
		this.tagIdsValue = tagIdsValue;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public List<String> getCatIdList() {
		return catIdList;
	}

	public void setCatIdList(List<String> catIdList) {
		this.catIdList = catIdList;
	}

	public Integer getTgId() {
		return tgId;
	}

	public void setTgId(Integer tgId) {
		this.tgId = tgId;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Map<String, Object> getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(Map<String, Object> shopInfo) {
		this.shopInfo = shopInfo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<GoodsCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<GoodsCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public Integer getIsJd() {
		return isJd;
	}

	public void setIsJd(Integer isJd) {
		this.isJd = isJd;
	}

	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	public String[] getUserGroupHead() {
		return userGroupHead;
	}

	public void setUserGroupHead(String[] userGroupHead) {
		this.userGroupHead = userGroupHead;
	}

	public Integer getIsCreate() {
		return isCreate;
	}

	public void setIsCreate(Integer isCreate) {
		this.isCreate = isCreate;
	}

	public double getRealTeamPrice() {
		return realTeamPrice;
	}

	public void setRealTeamPrice(double realTeamPrice) {
		this.realTeamPrice = realTeamPrice;
	}

	public Integer getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(Integer teamPrice) {
		this.teamPrice = teamPrice;
	}

	public Integer getShopLevel() {
		return shopLevel;
	}

	public void setShopLevel(Integer shopLevel) {
		this.shopLevel = shopLevel;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getDeliveryRealPrice() {
		return deliveryRealPrice;
	}

	public void setDeliveryRealPrice(double deliveryRealPrice) {
		this.deliveryRealPrice = deliveryRealPrice;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getMarkRealPrice() {
		return markRealPrice;
	}

	public void setMarkRealPrice(double markRealPrice) {
		this.markRealPrice = markRealPrice;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

	public List<GoodsSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<GoodsSku> skuList) {
		this.skuList = skuList;
	}

	public List<GoodsAttr> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<GoodsAttr> attrList) {
		this.attrList = attrList;
	}

	public String getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}

	public List<GoodsImage> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(List<GoodsImage> imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

	public Integer getShopCatId() {
        return shopCatId;
    }

    public void setShopCatId(Integer shopCatId) {
        this.shopCatId = shopCatId;
    }


    public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public Date getDownTime() {
        return downTime;
    }

    public void setDownTime(Date downTime) {
        this.downTime = downTime;
    }

    public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

    public Timestamp getEdittime() {
		return edittime;
	}

	public void setEdittime(Timestamp edittime) {
		this.edittime = edittime;
	}

	public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getStoreNums() {
        return storeNums;
    }

    public void setStoreNums(Integer storeNums) {
        this.storeNums = storeNums;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVisit() {
        return visit;
    }

    public void setVisit(Integer visit) {
        this.visit = visit;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Short getSortnum() {
        return sortnum;
    }

    public void setSortnum(Short sortnum) {
        this.sortnum = sortnum;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(Boolean isFlag) {
        this.isFlag = isFlag;
    }

    public Boolean getIsShopFlag() {
        return isShopFlag;
    }

    public void setIsShopFlag(Boolean isShopFlag) {
        this.isShopFlag = isShopFlag;
    }

	public Integer getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Integer deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public Integer getRefundDays() {
		return refundDays;
	}

	public void setRefundDays(Integer refundDays) {
		this.refundDays = refundDays;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getIsHotShop() {
		return isHotShop;
	}

	public void setIsHotShop(Integer isHotShop) {
		this.isHotShop = isHotShop;
	}

	public Integer getIsNewShop() {
		return isNewShop;
	}

	public void setIsNewShop(Integer isNewShop) {
		this.isNewShop = isNewShop;
	}

	public String getPublicimg() {
		return publicimg;
	}

	public void setPublicimg(String publicimg) {
		this.publicimg = publicimg;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	public Integer getTeamEndTime() {
		return teamEndTime;
	}

	public void setTeamEndTime(Integer teamEndTime) {
		this.teamEndTime = teamEndTime;
	}

	public Integer getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(Integer timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Integer getIsPromote() {
		return isPromote;
	}

	public void setIsPromote(Integer isPromote) {
		this.isPromote = isPromote;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	public String getAppdescription() {
		return appdescription;
	}

	public void setAppdescription(String appdescription) {
		this.appdescription = appdescription;
	}

	public Integer getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Integer startPrice) {
		this.startPrice = startPrice;
	}

	public Integer getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(Integer endPrice) {
		this.endPrice = endPrice;
	}
	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public Integer getFixedSale() {
		return fixedSale;
	}

	public void setFixedSale(Integer fixedSale) {
		this.fixedSale = fixedSale;
	}

	public Integer getIsPopular() {
		return isPopular;
	}

	public void setIsPopular(Integer isPopular) {
		this.isPopular = isPopular;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getGoodStatus() {
		return goodStatus;
	}

	public void setGoodStatus(String goodStatus) {
		this.goodStatus = goodStatus;
	}

	public String getShopCategoryName() {
		return shopCategoryName;
	}

	public void setShopCategoryName(String shopCategoryName) {
		this.shopCategoryName = shopCategoryName;
	}

	public Long getCatIdOne() {
		return catIdOne;
	}

	public void setCatIdOne(Long catIdOne) {
		this.catIdOne = catIdOne;
	}

	public Long getCatIdTwo() {
		return catIdTwo;
	}

	public void setCatIdTwo(Long catIdTwo) {
		this.catIdTwo = catIdTwo;
	}

	public JSONArray getModelValue() {
		return modelValue;
	}

	public void setModelValue(JSONArray modelValue) {
		this.modelValue = modelValue;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getOnSaleNum() {
		return onSaleNum;
	}

	public void setOnSaleNum(Integer onSaleNum) {
		this.onSaleNum = onSaleNum;
	}

	public Integer getNotOnSaleNum() {
		return notOnSaleNum;
	}

	public void setNotOnSaleNum(Integer notOnSaleNum) {
		this.notOnSaleNum = notOnSaleNum;
	}

	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public String getOutReason() {
		return outReason;
	}

	public void setOutReason(String outReason) {
		this.outReason = outReason;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getAuctionPrice() {
		return auctionPrice;
	}

	public void setAuctionPrice(Integer auctionPrice) {
		this.auctionPrice = auctionPrice;
	}

	public Double getRealAuctionPrice() {
		return realAuctionPrice;
	}

	public void setRealAuctionPrice(Double realAuctionPrice) {
		this.realAuctionPrice = realAuctionPrice;
	}

	public Map<String, Object> getDauctionDetail() {
		return dauctionDetail;
	}

	public void setDauctionDetail(Map<String, Object> dauctionDetail) {
		this.dauctionDetail = dauctionDetail;
	}

	public Integer getTopicGoodsId() {
		return topicGoodsId;
	}

	public void setTopicGoodsId(Integer topicGoodsId) {
		this.topicGoodsId = topicGoodsId;
	}

	public Integer getKuNums() {
		return kuNums;
	}

	public void setKuNums(Integer kuNums) {
		this.kuNums = kuNums;
	}

	public Integer getGoodBuyLimit() {
		return goodBuyLimit;
	}

	public void setGoodBuyLimit(Integer goodBuyLimit) {
		this.goodBuyLimit = goodBuyLimit;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public String getSendArea() {
		return sendArea;
	}

	public void setSendArea(String sendArea) {
		this.sendArea = sendArea;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public List getChildList() {
		return childList;
	}

	public void setChildList(List childList) {
		this.childList = childList;
	}

	public String getDeductibleRate() {
		return deductibleRate;
	}

	public void setDeductibleRate(String deductibleRate) {
		this.deductibleRate = deductibleRate;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getBhdPrice() {
		return bhdPrice;
	}

	public void setBhdPrice(String bhdPrice) {
		this.bhdPrice = bhdPrice;
	}
	
	

}