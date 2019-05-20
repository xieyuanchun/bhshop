package com.print.controller;

import java.util.List;


public class PrintUtil {

	/**
	 * 物流打印工具类
	 */
	
	private List<String> url;  //物流模板地址
	
	private List<String> cp_code; //模板代号
	
	private List<String> standard_template_id; //模板id
	
	private List<String> standard_template_name; //模板名称

	private List<String> urlId;  //物流模板地址+id
	
	private List<String> city;  //市名称（二级地址）
	
	private List<String> detail;  //详细地址
	
	private List<String> district;  //区名称（三级地址）
	
	private List<String> province;  //省名称（一级地址）

	
	
	
	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public List<String> getCp_code() {
		return cp_code;
	}

	public void setCp_code(List<String> cp_code) {
		this.cp_code = cp_code;
	}

	public List<String> getStandard_template_id() {
		return standard_template_id;
	}

	public void setStandard_template_id(List<String> standard_template_id) {
		this.standard_template_id = standard_template_id;
	}

	public List<String> getStandard_template_name() {
		return standard_template_name;
	}

	public void setStandard_template_name(List<String> standard_template_name) {
		this.standard_template_name = standard_template_name;
	}

	public List<String> getUrlId() {
		return urlId;
	}

	public void setUrlId(List<String> urlId) {
		this.urlId = urlId;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	public List<String> getDetail() {
		return detail;
	}

	public void setDetail(List<String> detail) {
		this.detail = detail;
	}

	public List<String> getDistrict() {
		return district;
	}

	public void setDistrict(List<String> district) {
		this.district = district;
	}

	public List<String> getProvince() {
		return province;
	}

	public void setProvince(List<String> province) {
		this.province = province;
	}
	
	
}
