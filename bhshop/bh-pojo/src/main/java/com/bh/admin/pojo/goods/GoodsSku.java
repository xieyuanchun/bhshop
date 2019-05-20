package com.bh.admin.pojo.goods;

import java.io.Serializable;
import java.util.List;

public class GoodsSku implements Serializable{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 2023059577934010996L;

	private Integer id;

    private Integer goodsId;

    private String skuNo;

    private Integer storeNums;

    private Integer marketPrice;

    private Integer sellPrice;

    private Integer weight;

    private Integer minimum;

    private String value;
    
    private Object valueObj;
    
    private Integer teamPrice;
    
    private Integer jdPrice; //京东价格单位分
    
    private Integer jdBuyPrice;//客户购买价格单位分
    
    private Integer jdOldBuyPrice;//客户购买价格(旧)单位分
    
    private Integer jdProtocolPrice; //协议价格单位分
    
    private Long jdSkuNo; //京东商品编码
    
    private String jdParam; //京东技术参数
    
    private Integer jdSupport; //是否支持京东下单
    
    private String jdUpc; //条形码
    
    private String goodsName; //商品sku名称
    
    private String keyOne;
    
    private String valueOne;
    
    private String keyTwo;
    
    private String valueTwo;
    
    private String keyThree;
    
    private String valueThree;
    
    private Integer status;
    
    private Integer stockPrice;
    
    private Integer deliveryPrice;
    
    private String skuCode; //zlk 新增字段
    
    private double realDeliveryPrice;
    
    private double realPrice; //销售价展示
    
    private double marketRealPrice; //市场价展示
    
    private double realTeamPrice;
    
    private String image;//取value的一张图片
    
    private double realJdPrice; //京东价格单位元
    
    private double realJdBuyPrice;//客户购买价格单位元
    
    private double realJdOldBuyPrice;//客户购买价格(旧)单位元
    
    private double realJdProtocolPrice; //协议价格单位元
    
    private double realStockPrice; //进货价
    
    private Integer score;//换购所需积分(滨惠豆)-1是不允许换购，0-正数允许换购的滨惠豆
    private Integer sale;//对应goods的groupCount字段，已拼多少单
    private double discounted;//滨惠豆抵扣的钱
    private String currentPage;
    
    private Integer auctionPrice; //拍卖价
    private Double realAuctionPrice;
    
    private ActZoneGoods actZoneGoods;
    
    private String newMarketPrice; //2018.6.29 zlk 审核中的 京东价
    
    private String newSellPrice; //2018.6.29 zlk 审核中的 单卖价

    private String newTeamPrice; //2018.6.29 zlk 审核中的 拼团价
   
    private String newStockPrice; //2018.6.29 zlk 审核中的 进货价
    
    private String newDeliveryPrice; //2018.6.29 zlk 审核中的 物流（邮费）价
    
    private String keyFour;
    
    private String valueFour;
    
    private String keyFive;
    
    private String valueFive;
    
    private List<GoodsSku> skuList; //重组的skuList（用户批量保存）
    
    private Integer userId; //当前后台登陆用户id
    
    
	public String getKeyFour() {
		return keyFour;
	}

	public void setKeyFour(String keyFour) {
		this.keyFour = keyFour;
	}

	public String getValueFour() {
		return valueFour;
	}

	public void setValueFour(String valueFour) {
		this.valueFour = valueFour;
	}

	public String getKeyFive() {
		return keyFive;
	}

	public void setKeyFive(String keyFive) {
		this.keyFive = keyFive;
	}

	public String getValueFive() {
		return valueFive;
	}

	public void setValueFive(String valueFive) {
		this.valueFive = valueFive;
	}

	public double getRealDeliveryPrice() {
		return realDeliveryPrice;
	}

	public void setRealDeliveryPrice(double realDeliveryPrice) {
		this.realDeliveryPrice = realDeliveryPrice;
	}

	public Integer getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Integer deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public double getRealStockPrice() {
		return realStockPrice;
	}

	public void setRealStockPrice(double realStockPrice) {
		this.realStockPrice = realStockPrice;
	}

	public Integer getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Integer stockPrice) {
		this.stockPrice = stockPrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getValueOne() {
		return valueOne;
	}

	public void setValueOne(String valueOne) {
		this.valueOne = valueOne;
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

	public String getValueTwo() {
		return valueTwo;
	}

	public void setValueTwo(String valueTwo) {
		this.valueTwo = valueTwo;
	}

	public String getValueThree() {
		return valueThree;
	}

	public void setValueThree(String valueThree) {
		this.valueThree = valueThree;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getJdUpc() {
		return jdUpc;
	}

	public void setJdUpc(String jdUpc) {
		this.jdUpc = jdUpc;
	}

	public Integer getJdSupport() {
		return jdSupport;
	}

	public void setJdSupport(Integer jdSupport) {
		this.jdSupport = jdSupport;
	}

	public String getJdParam() {
		return jdParam;
	}

	public void setJdParam(String jdParam) {
		this.jdParam = jdParam;
	}

	public double getRealJdPrice() {
		return realJdPrice;
	}

	public void setRealJdPrice(double realJdPrice) {
		this.realJdPrice = realJdPrice;
	}

	public double getRealJdBuyPrice() {
		return realJdBuyPrice;
	}

	public void setRealJdBuyPrice(double realJdBuyPrice) {
		this.realJdBuyPrice = realJdBuyPrice;
	}

	public double getRealJdOldBuyPrice() {
		return realJdOldBuyPrice;
	}

	public void setRealJdOldBuyPrice(double realJdOldBuyPrice) {
		this.realJdOldBuyPrice = realJdOldBuyPrice;
	}

	public double getRealJdProtocolPrice() {
		return realJdProtocolPrice;
	}

	public void setRealJdProtocolPrice(double realJdProtocolPrice) {
		this.realJdProtocolPrice = realJdProtocolPrice;
	}

	public Integer getJdOldBuyPrice() {
		return jdOldBuyPrice;
	}

	public void setJdOldBuyPrice(Integer jdOldBuyPrice) {
		this.jdOldBuyPrice = jdOldBuyPrice;
	}

	public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}

	public Integer getJdPrice() {
		return jdPrice;
	}

	public void setJdPrice(Integer jdPrice) {
		this.jdPrice = jdPrice;
	}

	public Integer getJdBuyPrice() {
		return jdBuyPrice;
	}

	public void setJdBuyPrice(Integer jdBuyPrice) {
		this.jdBuyPrice = jdBuyPrice;
	}

	public Integer getJdProtocolPrice() {
		return jdProtocolPrice;
	}

	public void setJdProtocolPrice(Integer jdProtocolPrice) {
		this.jdProtocolPrice = jdProtocolPrice;
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

	public Object getValueObj() {
		return valueObj;
	}

	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

	public double getMarketRealPrice() {
		return marketRealPrice;
	}

	public void setMarketRealPrice(double marketRealPrice) {
		this.marketRealPrice = marketRealPrice;
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

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo == null ? null : skuNo.trim();
    }

    public Integer getStoreNums() {
        return storeNums;
    }

    public void setStoreNums(Integer storeNums) {
        this.storeNums = storeNums;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

   

   

	public Integer getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getSale() {
		return sale;
	}

	public void setSale(Integer sale) {
		this.sale = sale;
	}

	public double getDiscounted() {
		return discounted;
	}

	public void setDiscounted(double discounted) {
		this.discounted = discounted;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
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

	public ActZoneGoods getActZoneGoods() {
		return actZoneGoods;
	}

	public void setActZoneGoods(ActZoneGoods actZoneGoods) {
		this.actZoneGoods = actZoneGoods;
	}

	public String getNewMarketPrice() {
		return newMarketPrice;
	}

	public void setNewMarketPrice(String newMarketPrice) {
		this.newMarketPrice = newMarketPrice;
	}

	public String getNewSellPrice() {
		return newSellPrice;
	}

	public void setNewSellPrice(String newSellPrice) {
		this.newSellPrice = newSellPrice;
	}

	public String getNewTeamPrice() {
		return newTeamPrice;
	}

	public void setNewTeamPrice(String newTeamPrice) {
		this.newTeamPrice = newTeamPrice;
	}

	public String getNewStockPrice() {
		return newStockPrice;
	}

	public void setNewStockPrice(String newStockPrice) {
		this.newStockPrice = newStockPrice;
	}

	public String getNewDeliveryPrice() {
		return newDeliveryPrice;
	}

	public void setNewDeliveryPrice(String newDeliveryPrice) {
		this.newDeliveryPrice = newDeliveryPrice;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public List<GoodsSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<GoodsSku> skuList) {
		this.skuList = skuList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}