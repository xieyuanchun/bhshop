package com.bh.admin.pojo.user;

import java.util.Date;
import java.util.List;

public class MemberShop {
    private Integer mId;

    private String shopName;//名称

    private String logo;//店铺图片

    private Integer status;//0正常1删除2锁定,3未审核,4审核

    private Date addtime;//注册时间

    private Date endtime;//最后登录时间

    private String tel;//公司电话

    private String email;//邮箱

    private String customerUrl;//web客服地址，在产品页面优先展示，没有时展示电话

    private String businessLicense;//营业执照(照片)

    private Integer prov;//省

    private Integer city;//市

    private Integer area;//区
    
    private Integer four;//第四级地址（如果有的话则四级地址的id,如果没有第四级则传0）

    private Double lon;//经度

    private Double lat;//纬度

    private String address;//地址

    private String description;//店铺介绍

    private Integer goodsComment;//好评数

    private Integer level;//店铺等级

    private Integer balance;//账户余额

    private Integer type;
    
    private Integer catId;//分类ID

    private String bannerUrl;//店铺banner图片
    
    private Integer adminId; //店铺管理员id
    
    private String linkmanName;//联系人姓名

    private String linkmanPhone;//联系人的手机号

    private String linkmanEmail;//联系人的电子邮

    private Integer isinvite;//是否接受其他商场邀请0否，1是

    private String code;//如果接受了其他商场的邀请则传推荐商家的id,如果没有则传0

    private String licenseTypeName;//营业执照类型

    private String emergencyContactPerson;//公司紧急联系人

    private String emergencyContactPhone;//公司紧急联系人手机

    private Integer step;//申请步骤

    private Integer cardType;//法定代表人证件类型：0身份证，1户口本，2军官证、3护照

    private String card;//法定代表人证件号

    private String beginTime;//营业期限（开始时间）

    private String endTime;//营业期限(结束时间)

    private String cardImage;//请按顺序依次上传身份证正面图，身份证背面图，手持身份证图(图片)

    private String licenseImage;//银行开户许可证图片

    private String organizationBeginTime;//组织机构代码证有效期（开始时间）

    private String organizationEndTime;//组织机构代码有效期（结束时间）

    private String organizationCode;//组织机构代码

    private String organizationImage;//组织机构代码证电子版（图片）

    private String taxCode;//纳税人识别号

    private Integer taxType;//纳税人类型：0代表一般纳税人；1代表小规模纳税人

    private String taxOtherCode;//纳税类型税码

    private String taxImage;//纳税资质图片上传（图片）
    
    
    
    //end
    
    
    private String licenseNumber;//营业执照注册号

    private String cardName;//法定代表人姓名

    private Integer licenseProv;//营业执照所在地的省

    private Integer licenseCity;

    private Integer licenseTown;
    
    private Integer licenseFour;//第四级地址（如果有的话则四级地址的id,如果没有第四级则传0）

    private String licenseAddress;//营业执照所在地的详细地址

    private String founddate;//公司成立日期

    private String regMoney;//公司的注册资本

    private String busScope;//经营范围

    private String cardBegintime;//法定代表人证件的有效期（开始时间）

    private String cardEndtime;//法定代表人证件的有效期（结束时间）

    private String orgBegintime;//组织机构证件的有效期(开始时间)

    private String orgEndtime;//组织机构证件的有效期(结束时间)

    private String token;

    private Integer isPc;//是否是pc端，0pc端，1移动端
    
    private Integer isAgree;//是否同意：0不同意，1同意
    
    private String note;//平台审核拒绝的理由
    
    private Integer existPos;//是否存在pos机，1有,2无，3无
    //用户存放pos机的信息,如果exist_pos=1话该字段存商户号，商户名称并以逗号隔开；
    //如果exit_pos=2的话存姓名，身份证，银行卡号，手机号，
    //如果exist_pos=3的话存姓名，联系号码
    private String posMsg;
    //处理状态handle_status(0未处理，1已处理)
    private Integer handleStatus;
   
    
    
    
    private List<BusBankCard> bankList;
    private String provName;
    private String cityName;//市
    private String areaName;//区
    private String fourName;//第四级地址（如果有的话则四级地址的id,如果没有第四级则传0）
    private String licenseProvName;//营业执照所在地的省
    private String licenseCityName;
    private String licenseTownName;
    private String licenseFourName;//第四级地址（如果有的话则四级地址的id,如果没有第四级则传0）
    
    private String md5Key;
    
    private String busiPayPre;//商家支付前缀
    
    private Integer isBhshop;//是否是滨惠自营店0不是，1是滨惠自营店,数据库如果不填的话默认是0
    
    private String tradeNo;//交易号

    private String orderNo;//订单号

    private Integer payStatus;//支付状态1未支付，2已支付
    
    private Integer deposit;//免押金的钱

    private String depositTradeNo;//免审核的订单(第三方订单号)

    private String depositNo;//免审核的订单

    private Integer depositStatus;//支付状态
    
    private Date depositTime;//交免审核支付成功的时间

    private Integer orderPrice;//支付金额
    
    private String realPrice;//交易金额
    
    private String realOrderPrice;//交易金额
    
    private int orderShopNum;//
    
   
    private String speedLevelAvg;//平均送货速度评级
    private String sServiceLevelAvg;//平均配送员服务评级
    private String packLevelAvg;//平均快递包装评级
	
    private String shopGradeAvg;
	
    private int loginTime;
    
    private String currentPage;//当前页
    private String size;//每页的大小
    
    private String returnAddress;
    
    private Integer freeNum;
    private Integer surplusNum;
    
	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public int getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}

	public String getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public String getRealOrderPrice() {
		return realOrderPrice;
	}

	public void setRealOrderPrice(String realOrderPrice) {
		this.realOrderPrice = realOrderPrice;
	}

	public int getOrderShopNum() {
		return orderShopNum;
	}

	public void setOrderShopNum(int orderShopNum) {
		this.orderShopNum = orderShopNum;
	}

	public String getSpeedLevelAvg() {
		return speedLevelAvg;
	}

	public void setSpeedLevelAvg(String speedLevelAvg) {
		this.speedLevelAvg = speedLevelAvg;
	}

	public String getsServiceLevelAvg() {
		return sServiceLevelAvg;
	}

	public void setsServiceLevelAvg(String sServiceLevelAvg) {
		this.sServiceLevelAvg = sServiceLevelAvg;
	}

	public String getPackLevelAvg() {
		return packLevelAvg;
	}

	public void setPackLevelAvg(String packLevelAvg) {
		this.packLevelAvg = packLevelAvg;
	}

	public String getShopGradeAvg() {
		return shopGradeAvg;
	}

	public void setShopGradeAvg(String shopGradeAvg) {
		this.shopGradeAvg = shopGradeAvg;
	}

	public Integer getIsBhshop() {
		return isBhshop;
	}

	public void setIsBhshop(Integer isBhshop) {
		this.isBhshop = isBhshop;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getCustomerUrl() {
        return customerUrl;
    }

    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl == null ? null : customerUrl.trim();
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense == null ? null : businessLicense.trim();
    }

    public Integer getProv() {
        return prov;
    }

    public void setProv(Integer prov) {
        this.prov = prov;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getGoodsComment() {
        return goodsComment;
    }

    public void setGoodsComment(Integer goodsComment) {
        this.goodsComment = goodsComment;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl == null ? null : bannerUrl.trim();
    }

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getLinkmanName() {
		return linkmanName;
	}

	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}

	public String getLinkmanPhone() {
		return linkmanPhone;
	}

	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}

	public String getLinkmanEmail() {
		return linkmanEmail;
	}

	public void setLinkmanEmail(String linkmanEmail) {
		this.linkmanEmail = linkmanEmail;
	}

	public Integer getIsinvite() {
		return isinvite;
	}

	public void setIsinvite(Integer isinvite) {
		this.isinvite = isinvite;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLicenseTypeName() {
		return licenseTypeName;
	}

	public void setLicenseTypeName(String licenseTypeName) {
		this.licenseTypeName = licenseTypeName;
	}

	public String getEmergencyContactPerson() {
		return emergencyContactPerson;
	}

	public void setEmergencyContactPerson(String emergencyContactPerson) {
		this.emergencyContactPerson = emergencyContactPerson;
	}

	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCardImage() {
		return cardImage;
	}

	public void setCardImage(String cardImage) {
		this.cardImage = cardImage;
	}

	public String getLicenseImage() {
		return licenseImage;
	}

	public void setLicenseImage(String licenseImage) {
		this.licenseImage = licenseImage;
	}

	public String getOrganizationBeginTime() {
		return organizationBeginTime;
	}

	public void setOrganizationBeginTime(String organizationBeginTime) {
		this.organizationBeginTime = organizationBeginTime;
	}

	public String getOrganizationEndTime() {
		return organizationEndTime;
	}

	public void setOrganizationEndTime(String organizationEndTime) {
		this.organizationEndTime = organizationEndTime;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationImage() {
		return organizationImage;
	}

	public void setOrganizationImage(String organizationImage) {
		this.organizationImage = organizationImage;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Integer getTaxType() {
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
	}

	public String getTaxOtherCode() {
		return taxOtherCode;
	}

	public void setTaxOtherCode(String taxOtherCode) {
		this.taxOtherCode = taxOtherCode;
	}

	public String getTaxImage() {
		return taxImage;
	}

	public void setTaxImage(String taxImage) {
		this.taxImage = taxImage;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public Integer getLicenseProv() {
		return licenseProv;
	}

	public void setLicenseProv(Integer licenseProv) {
		this.licenseProv = licenseProv;
	}

	public Integer getLicenseCity() {
		return licenseCity;
	}

	public void setLicenseCity(Integer licenseCity) {
		this.licenseCity = licenseCity;
	}

	public Integer getLicenseTown() {
		return licenseTown;
	}

	public void setLicenseTown(Integer licenseTown) {
		this.licenseTown = licenseTown;
	}

	public String getLicenseAddress() {
		return licenseAddress;
	}

	public void setLicenseAddress(String licenseAddress) {
		this.licenseAddress = licenseAddress;
	}

	public String getFounddate() {
		return founddate;
	}

	public void setFounddate(String founddate) {
		this.founddate = founddate;
	}

	public String getRegMoney() {
		return regMoney;
	}

	public void setRegMoney(String regMoney) {
		this.regMoney = regMoney;
	}

	public String getBusScope() {
		return busScope;
	}

	public void setBusScope(String busScope) {
		this.busScope = busScope;
	}

	public String getCardBegintime() {
		return cardBegintime;
	}

	public void setCardBegintime(String cardBegintime) {
		this.cardBegintime = cardBegintime;
	}

	public String getCardEndtime() {
		return cardEndtime;
	}

	public void setCardEndtime(String cardEndtime) {
		this.cardEndtime = cardEndtime;
	}

	public String getOrgBegintime() {
		return orgBegintime;
	}

	public void setOrgBegintime(String orgBegintime) {
		this.orgBegintime = orgBegintime;
	}

	public String getOrgEndtime() {
		return orgEndtime;
	}

	public void setOrgEndtime(String orgEndtime) {
		this.orgEndtime = orgEndtime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getIsPc() {
		return isPc;
	}

	public void setIsPc(Integer isPc) {
		this.isPc = isPc;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public Integer getFour() {
		return four;
	}

	public void setFour(Integer four) {
		this.four = four;
	}

	public Integer getLicenseFour() {
		return licenseFour;
	}

	public void setLicenseFour(Integer licenseFour) {
		this.licenseFour = licenseFour;
	}

	public List<BusBankCard> getBankList() {
		return bankList;
	}

	public void setBankList(List<BusBankCard> bankList) {
		this.bankList = bankList;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getFourName() {
		return fourName;
	}

	public void setFourName(String fourName) {
		this.fourName = fourName;
	}

	public String getLicenseProvName() {
		return licenseProvName;
	}

	public void setLicenseProvName(String licenseProvName) {
		this.licenseProvName = licenseProvName;
	}

	public String getLicenseCityName() {
		return licenseCityName;
	}

	public void setLicenseCityName(String licenseCityName) {
		this.licenseCityName = licenseCityName;
	}

	public String getLicenseTownName() {
		return licenseTownName;
	}

	public void setLicenseTownName(String licenseTownName) {
		this.licenseTownName = licenseTownName;
	}

	public String getLicenseFourName() {
		return licenseFourName;
	}

	public void setLicenseFourName(String licenseFourName) {
		this.licenseFourName = licenseFourName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBusiPayPre() {
		return busiPayPre;
	}

	public void setBusiPayPre(String busiPayPre) {
		this.busiPayPre = busiPayPre;
	}

	public Integer getExistPos() {
		return existPos;
	}

	public void setExistPos(Integer existPos) {
		this.existPos = existPos;
	}

	public String getPosMsg() {
		return posMsg;
	}

	public void setPosMsg(String posMsg) {
		this.posMsg = posMsg;
	}

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getDeposit() {
		return deposit;
	}

	public void setDeposit(Integer deposit) {
		this.deposit = deposit;
	}

	public String getDepositTradeNo() {
		return depositTradeNo;
	}

	public void setDepositTradeNo(String depositTradeNo) {
		this.depositTradeNo = depositTradeNo;
	}

	public String getDepositNo() {
		return depositNo;
	}

	public void setDepositNo(String depositNo) {
		this.depositNo = depositNo;
	}

	public Integer getDepositStatus() {
		return depositStatus;
	}

	public void setDepositStatus(Integer depositStatus) {
		this.depositStatus = depositStatus;
	}

	public Date getDepositTime() {
		return depositTime;
	}

	public void setDepositTime(Date depositTime) {
		this.depositTime = depositTime;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(Integer freeNum) {
		this.freeNum = freeNum;
	}

	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}
	
	
	
	
    
}