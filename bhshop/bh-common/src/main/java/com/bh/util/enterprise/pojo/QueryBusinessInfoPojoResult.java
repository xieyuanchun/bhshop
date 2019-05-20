package com.bh.util.enterprise.pojo;

import java.io.Serializable;

public class QueryBusinessInfoPojoResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;//公司名称
	private String type;//公司类型
	private String regcapital;//注册资本
	private String scope;//经营范围
	private String startdate;//经营起始日期
	private String enddate;//营业终止日期
	private String regorgan;//登记机关
	private String legalperson;//法人
	private String approvaldate;//核准日期
	private String regdate;//注册日期
	private String canceldate;//注销日期
	private String status;//登记状态
	private String orgno;//组织结构代码
	private String creditno;//公司类型
	private String province;//省份
	private String city;//城市
	private String regno;//注册号
	private String regaddress;//注册地址
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRegcapital() {
		return regcapital;
	}
	public void setRegcapital(String regcapital) {
		this.regcapital = regcapital;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRegorgan() {
		return regorgan;
	}
	public void setRegorgan(String regorgan) {
		this.regorgan = regorgan;
	}
	public String getLegalperson() {
		return legalperson;
	}
	public void setLegalperson(String legalperson) {
		this.legalperson = legalperson;
	}
	public String getApprovaldate() {
		return approvaldate;
	}
	public void setApprovaldate(String approvaldate) {
		this.approvaldate = approvaldate;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getCanceldate() {
		return canceldate;
	}
	public void setCanceldate(String canceldate) {
		this.canceldate = canceldate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrgno() {
		return orgno;
	}
	public void setOrgno(String orgno) {
		this.orgno = orgno;
	}
	public String getCreditno() {
		return creditno;
	}
	public void setCreditno(String creditno) {
		this.creditno = creditno;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegno() {
		return regno;
	}
	public void setRegno(String regno) {
		this.regno = regno;
	}
	public String getRegaddress() {
		return regaddress;
	}
	public void setRegaddress(String regaddress) {
		this.regaddress = regaddress;
	}
	@Override
	public String toString() {
		return "QueryBusinessInfoPojoResult [name=" + name + ", type=" + type + ", regcapital=" + regcapital
				+ ", scope=" + scope + ", startdate=" + startdate + ", enddate=" + enddate + ", regorgan=" + regorgan
				+ ", legalperson=" + legalperson + ", approvaldate=" + approvaldate + ", regdate=" + regdate
				+ ", canceldate=" + canceldate + ", status=" + status + ", orgno=" + orgno + ", creditno=" + creditno
				+ ", province=" + province + ", city=" + city + ", regno=" + regno + ", regaddress=" + regaddress + "]";
	}
	
	
	
}
