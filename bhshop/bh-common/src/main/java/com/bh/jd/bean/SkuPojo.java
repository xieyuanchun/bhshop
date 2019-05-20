package com.bh.jd.bean;

public class SkuPojo {
	private String skuId;
	
	private Integer num;
	
	private Integer category;
	
	private String price;
	
	private String name;
	
	private String tax;
	
	private String taxPrice;
	
	private String nakedPrice;
	
	private Integer type;//type为0普通、1附件、2赠品
	
	private Integer oid;//oid为主商品skuid，如果本身是主商品，则oid为0

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}

	public String getNakedPrice() {
		return nakedPrice;
	}

	public void setNakedPrice(String nakedPrice) {
		this.nakedPrice = nakedPrice;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}
	
	
}
