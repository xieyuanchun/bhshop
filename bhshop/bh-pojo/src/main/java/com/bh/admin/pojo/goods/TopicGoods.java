package com.bh.admin.pojo.goods;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


public class TopicGoods implements Serializable{
    private Integer id;

    private Integer actId;

    private Integer goodsId;

    private Long cid;

    private Date cTime;

    private Date uTime;

    private Byte status;

    private String remark;

    private Short listorder;

    private Integer sid;
    
    private Integer isDelete;
    
    private String currentPage;
    
    private int storeNums;//库存
    
    private int saleNums;//卖出数量
   
	private int surplusStoreNums;//剩余库存
    
	private String topicStartTime;//活动开始时间（例：18:30）开始
    
	private Boolean isJoin;//是否参加过该活动

	private Integer surplusNum;//剩余名额
    
    private Integer joinNum;//已经参加人数名额
    
	private String waitTime;//活动待定结束时间（还有多久结束）
	
	private double marketPrice;//市场价格
   
	private String goodsName; //商品名称
    
	private String goodsIds; //几个商品的id

	private String cName; //分类名称
    
    private String gIdStr; //商品id（多个）
    
    private String goodsImage; //商品图片
    
    private double price; //商品价格，砍价设置
    
    private String typeName; //活动类型名称
    
    private String topicName; //活动名称
    
    private Date startTime; //活动开始时间
    
    private Date endTime; //活动结束时间
    
    private Date applyTime;
	
    private Date actTime;
    
    private Integer fz; // 1升序，2降序
    
    private Integer sortOrPrice; //价格排序or排序号排序
    
    private Integer num; //人数，砍价设置
    
    private Double realPrice; //前端展示价格使用
    
    private Double realMarketPrice; //拍卖活动前端展示价格使用
    
    private Integer type; //活动类型 1竞价活动，2专题活动，3砍价活动',
    
    private String bargainNo; //砍价团号
    private Object valueObj;
    
    private Integer mId; //砍价发起人id
    
    private String openId; 
    
    private String myNo; //惠省钱团号
    
    private Integer shopId;
    
    private Boolean isBargain; //是否被当前用户砍过
     
    private Date applyStime;//报名开始时间

    private Date applyEtime;//报名结束时间
    
    private Integer goodsStatus;//商品状态
    
    private Date cStime;//报名开始时间

    private Date cEtime;//报名结束时间
    
    private Map<String, Object> dauctionDetail;
    
    private Integer kuNums;
    
    
    
    public Date getcStime() {
		return cStime;
	}

	public void setcStime(Date cStime) {
		this.cStime = cStime;
	}

	public Date getcEtime() {
		return cEtime;
	}

	public void setcEtime(Date cEtime) {
		this.cEtime = cEtime;
	}

	private Double realLowPrice;
    
    private Double realScopePrice;
    
    private Integer timeSection;
    
    private Integer countDown; //倒计时 ，秒
    
    public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Date getApplyStime() {
		return applyStime;
	}

	public void setApplyStime(Date applyStime) {
		this.applyStime = applyStime;
	}

	public Date getApplyEtime() {
		return applyEtime;
	}

	public void setApplyEtime(Date applyEtime) {
		this.applyEtime = applyEtime;
	}

	public Boolean getIsBargain() {
		return isBargain;
	}

	public void setIsBargain(Boolean isBargain) {
		this.isBargain = isBargain;
	}
    
  	public Integer getShopId() {
  		return shopId;
  	}

  	public void setShopId(Integer shopId) {
  		this.shopId = shopId;
  	}
    
    public String getGoodsIds() {

		return goodsIds;
	}

	public void setGoodsIds(String goodsIds) {
		this.goodsIds = goodsIds;
	}

	public int getStoreNums() {
		return storeNums;
	}

	public void setStoreNums(int storeNums) {
		this.storeNums = storeNums;
	}

	public int getSaleNums() {
		return saleNums;
	}

	public void setSaleNums(int saleNums) {
		this.saleNums = saleNums;
	}

	public int getSurplusStoreNums() {
		return surplusStoreNums;
	}

	public void setSurplusStoreNums(int surplusStoreNums) {
		this.surplusStoreNums = surplusStoreNums;
	}
    
    public String getTopicStartTime() {
		return topicStartTime;
	}

	public void setTopicStartTime(String topicStartTime) {
		this.topicStartTime = topicStartTime;
	}
    
    public double getMarketPrice() {
  		return marketPrice;
  	}

  	public void setMarketPrice(double marketPrice) {
  		this.marketPrice = marketPrice;
  	}

    
    public Boolean getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Boolean isJoin) {
		this.isJoin = isJoin;
	}
    
    public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
    
    public Integer getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(Integer joinNum) {
		this.joinNum = joinNum;
	}

    
    public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

    
    
    public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getBargainNo() {
		return bargainNo;
	}

	public void setBargainNo(String bargainNo) {
		this.bargainNo = bargainNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getSortOrPrice() {
		return sortOrPrice;
	}

	public void setSortOrPrice(Integer sortOrPrice) {
		this.sortOrPrice = sortOrPrice;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getFz() {
		return fz;
	}

	public void setFz(Integer fz) {
		this.fz = fz;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}


	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getActTime() {
		return actTime;
	}

	public void setActTime(Date actTime) {
		this.actTime = actTime;
	}

	public String getgIdStr() {
		return gIdStr;
	}

	public void setgIdStr(String gIdStr) {
		this.gIdStr = gIdStr;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

   
    public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getuTime() {
        return uTime;
    }

    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Short getListorder() {
        return listorder;
    }

    public void setListorder(Short listorder) {
        this.listorder = listorder;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

	public Object getValueObj() {
		return valueObj;
	}

	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}

	public String getMyNo() {
		return myNo;
	}

	public void setMyNo(String myNo) {
		this.myNo = myNo;
	}

	public Double getRealLowPrice() {
		return realLowPrice;
	}

	public void setRealLowPrice(Double realLowPrice) {
		this.realLowPrice = realLowPrice;
	}

	public Double getRealScopePrice() {
		return realScopePrice;
	}

	public void setRealScopePrice(Double realScopePrice) {
		this.realScopePrice = realScopePrice;
	}

	public Integer getTimeSection() {
		return timeSection;
	}

	public void setTimeSection(Integer timeSection) {
		this.timeSection = timeSection;
	}

	public Double getRealMarketPrice() {
		return realMarketPrice;
	}

	public void setRealMarketPrice(Double realMarketPrice) {
		this.realMarketPrice = realMarketPrice;
	}

	public Integer getCountDown() {
		return countDown;
	}

	public void setCountDown(Integer countDown) {
		this.countDown = countDown;
	}

	public Map<String, Object> getDauctionDetail() {
		return dauctionDetail;
	}

	public void setDauctionDetail(Map<String, Object> dauctionDetail) {
		this.dauctionDetail = dauctionDetail;
	}

	public Integer getKuNums() {
		return kuNums;
	}

	public void setKuNums(Integer kuNums) {
		this.kuNums = kuNums;
	}
	
}