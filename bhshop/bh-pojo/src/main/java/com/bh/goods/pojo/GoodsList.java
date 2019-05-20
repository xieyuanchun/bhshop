package com.bh.goods.pojo;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class GoodsList implements Serializable{
	private Integer id;

    private String name;
    
    private String image;
    
    private double realPrice;
    
	private Integer score; 
    
    private Integer groupCount;
    
    private double markRealPrice;
    
    private String deductibleRate;
    
    private double bhdPrice;
    
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
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}
	
	
	
	 public Integer getGroupCount() {
			return groupCount;
		}

		public void setGroupCount(Integer groupCount) {
			this.groupCount = groupCount;
		}

		public double getMarkRealPrice() {
			return markRealPrice;
		}

		public void setMarkRealPrice(double markRealPrice) {
			this.markRealPrice = markRealPrice;
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

		public double getBhdPrice() {
			return bhdPrice;
		}

		public void setBhdPrice(double bhdPrice) {
			this.bhdPrice = bhdPrice;
		}


}