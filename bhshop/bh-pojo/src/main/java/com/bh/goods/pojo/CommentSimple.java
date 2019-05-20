package com.bh.goods.pojo;

import java.io.Serializable;
import java.util.Arrays;

public class CommentSimple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1548932579618222245L;

	private Integer id;

	private String remark;
	private Integer level;

	private String images[];

	private Integer star;
	
	private Integer add ;//这是第几次评价
	
    private Integer speedLevel;//送货速度评级  0默认未评级 1一星级 2二星级 3三星级 4四星级 5五星级

    private Integer sServiceLevel;//配送员服务评级 0默认未评级 1一星级 2二星级 3三星级 4四星级 5五星级

    private Integer packLevel;//快递包装评级 0默认未评级 1一星级 2二星级 3三星级 4四星级 5五星级'
    private Integer notname;//是否匿名0匿名  1不匿名
	
	

	@Override
	public String toString() {
		return "CommentSimple [id=" + id + ", remark=" + remark + ", level=" + level + ", images="
				+ Arrays.toString(images) + ", star=" + star + ", add=" + add + ", speedLevel=" + speedLevel
				+ ", sServiceLevel=" + sServiceLevel + ", packLevel=" + packLevel + ", notname=" + notname + "]";
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	
	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Integer getAdd() {
		return add;
	}

	public void setAdd(Integer add) {
		this.add = add;
	}

	public Integer getSpeedLevel() {
		return speedLevel;
	}

	public void setSpeedLevel(Integer speedLevel) {
		this.speedLevel = speedLevel;
	}

	public Integer getsServiceLevel() {
		return sServiceLevel;
	}

	public void setsServiceLevel(Integer sServiceLevel) {
		this.sServiceLevel = sServiceLevel;
	}

	public Integer getPackLevel() {
		return packLevel;
	}

	public void setPackLevel(Integer packLevel) {
		this.packLevel = packLevel;
	}

	public Integer getNotname() {
		return notname;
	}

	public void setNotname(Integer notname) {
		this.notname = notname;
	}

	
	
}
