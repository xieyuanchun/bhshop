package com.bh.jd.bean.goods;

public class Detail {
	
	private String saleUnit;
	
	private String weight;
	
	private String productArea;
	
	private String wareQD;
	
	private String imagePath;
	
	private String param;
	
	private Integer state;
	
	private Long sku;
	
	private String brandName;
	
	private String upc;
	
	private String category;
	
	private String name;
	
	private String introduction;
	
	private String appintroduce;

	
	public String getAppintroduce() {
		return appintroduce;
	}

	public void setAppintroduce(String appintroduce) {
		this.appintroduce = appintroduce;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getProductArea() {
		return productArea;
	}

	public void setProductArea(String productArea) {
		this.productArea = productArea;
	}

	public String getWareQD() {
		return wareQD;
	}

	public void setWareQD(String wareQD) {
		this.wareQD = wareQD;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getSku() {
		return sku;
	}

	public void setSku(Long sku) {
		this.sku = sku;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Override
	public String toString() {
		return "Detail [saleUnit=" + saleUnit + ", weight=" + weight + ", productArea=" + productArea + ", wareQD="
				+ wareQD + ", imagePath=" + imagePath + ", param=" + param + ", state=" + state + ", sku=" + sku
				+ ", brandName=" + brandName + ", upc=" + upc + ", category=" + category + ", name=" + name
				+ ", introduction=" + introduction + "]";
	}

	
	
	
	
	
	
	
	
}
