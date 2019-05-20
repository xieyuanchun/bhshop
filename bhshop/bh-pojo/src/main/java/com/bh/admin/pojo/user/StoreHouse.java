package com.bh.admin.pojo.user;

public class StoreHouse {
	//memberSeed表的id
	private Integer id;
	
	//种子的名称
	private String seedName;
	
	//种子的图片
	private String image;
	
	//等级
	private Integer rand;
	
	//数量
	private Integer num;
	
    private double realSalePrice;//购买价格
    
    private double realBonus;//卖出去的价格

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
		this.seedName = seedName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getRand() {
		return rand;
	}

	public void setRand(Integer rand) {
		this.rand = rand;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
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
    
    
	
	
}
