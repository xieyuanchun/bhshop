package com.bh.admin.pojo.user;

public class SeedModel {
	/******种子模型********/
    private Integer id;

    private String seedName;//植物名称

    private Integer gainPeriod;//初成熟时间    小时为单位

    private Integer score;//积分

    private Integer salePrice;//分为单位(购买价格)
    
    private Integer bonus;//分为单位(总卖出价格）

    private Integer status;//-3已删除 -2审核未通过  -1下架 0创建 1未审核 2上架
    
    private String image;//种子模型的图片
    
    private String description;//对该种子的描述
    
    private Integer type;//模型的类型，0代表种子，1代表土地，2代表鱼苗,3代表工具
    
    private String smallImage;//发芽、小叶子、大叶子、开花、成熟结果五个阶段，大致每个阶段对应一张小图
    
    private Integer rand;//购买等级

    private Integer gainPeriodAgain;//再次成熟的时间(小时为单位)

    private Integer seasonNum;//季数,默认第1季

    private Integer experienceEverySeason;//每一季收获的经验
    
    private double realSalePrice;//购买价格
    
    private double realBonus;//总卖出价格
    
    private Integer orderSeedId;//OrderSeed的id
    
    private String page;
    
    private Integer totalGainPeriod;//总时间=gainPeriod+gainPeriodAgain*(seasonNum-1)
    private double eachHourExperience;//每小时经验=(experienceEverySeason+播种、铲除的5点经验)/totalGainPeriod
    private double allProfit ;//总利润=realBonus-realSalePrice
    private double eachProfit;//每小时金币=总利润/总时间
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName == null ? null : seedName.trim();
    }

    public Integer getGainPeriod() {
        return gainPeriod;
    }

    public void setGainPeriod(Integer gainPeriod) {
        this.gainPeriod = gainPeriod;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

	public Integer getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public double getRealSalePrice() {
		return realSalePrice;
	}

	public void setRealSalePrice(double realSalePrice) {
		this.realSalePrice = realSalePrice;
	}

	public double getRealBonus() {
		return realBonus;
	}

	public void setRealBonus(double realBonus) {
		this.realBonus = realBonus;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrderSeedId() {
		return orderSeedId;
	}

	public void setOrderSeedId(Integer orderSeedId) {
		this.orderSeedId = orderSeedId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public Integer getRand() {
		return rand;
	}

	public void setRand(Integer rand) {
		this.rand = rand;
	}

	public Integer getGainPeriodAgain() {
		return gainPeriodAgain;
	}

	public void setGainPeriodAgain(Integer gainPeriodAgain) {
		this.gainPeriodAgain = gainPeriodAgain;
	}

	public Integer getSeasonNum() {
		return seasonNum;
	}

	public void setSeasonNum(Integer seasonNum) {
		this.seasonNum = seasonNum;
	}

	public Integer getExperienceEverySeason() {
		return experienceEverySeason;
	}

	public void setExperienceEverySeason(Integer experienceEverySeason) {
		this.experienceEverySeason = experienceEverySeason;
	}

	public Integer getTotalGainPeriod() {
		return totalGainPeriod;
	}

	public void setTotalGainPeriod(Integer totalGainPeriod) {
		this.totalGainPeriod = totalGainPeriod;
	}

	public double getEachHourExperience() {
		return eachHourExperience;
	}

	public void setEachHourExperience(double eachHourExperience) {
		this.eachHourExperience = eachHourExperience;
	}

	public double getAllProfit() {
		return allProfit;
	}

	public void setAllProfit(double allProfit) {
		this.allProfit = allProfit;
	}

	public double getEachProfit() {
		return eachProfit;
	}

	public void setEachProfit(double eachProfit) {
		this.eachProfit = eachProfit;
	}
	
	
    
    
}