package com.bh.jd.bean.order;

public class SubmitOrderParam {
	private String thirdOrder;//必须    第三方的订单单号
	
	private String sku;//[{"skuId":商品编号, "num":商品数量,"bNeedAnnex":true, "bNeedGift":true,"price":100,"yanbao":[{"skuId":商品编号}]}]    (最高支持50种商品)
	                      //bNeedAnnex表示是否需要附件，默认每个订单都给附件，默认值为：true，如果客户实在不需要附件bNeedAnnex可以给false，该参数配置为false时请谨慎，真的不会给客户发附件的;
	                      //bNeedGift表示是否需要增品，默认不给增品，默认值为：false，如果需要增品bNeedGift请给true,建议该参数都给true,但如果实在不需要增品可以给false;
	                      //price 表示透传价格，需要合同权限，接受价格权限，否则不允许传该值；
	private String name;//必须	收货人
	private Integer province;// 必须	一级地址
	private Integer city;//	int	必须	二级地址
	private Integer county;//int 必须	三级地址
	private Integer town;//	int	必须	四级地址  (如果该地区有四级地址，则必须传递四级地址，没有四级地址则传0)
	private String address;//Stirng	必须	详细地址
	private String zip;//Stirng	非必须  邮编
	private String mobile;//Stirng	必须	手机号 
	private String email;//	Stirng	必须	邮箱
	private Integer invoiceState;//	int	必须	开票方式(1为随货开票，0为订单预借，2为集中开票 )
	private Integer invoiceType;//int	必须	1普通发票2增值税发票
	private Integer selectedInvoiceTitle;//int	必须	发票类型：4个人，5单位
	private String companyName;//	String	必须	发票抬头  (如果selectedInvoiceTitle=5则此字段必须)
	private Integer invoiceContent;//	int	必须	1:明细，3：电脑配件，19:耗材，22：办公用品  (备注:若增值发票则只能选1 明细)
	private Integer paymentType;//	int	必须	支付方式 (1：货到付款，2：邮局付款，4：在线支付，5：公司转账，6：银行转账，7：网银钱包，101：金采支付)
	private Integer isUseBalance;//	int	必须	使用余额paymentType=4时，此值固定是1   ，  其他支付方式0
	private Integer submitState;//	Int	必须	是否预占库存，0是预占库存（需要调用确认订单接口），1是不预占库存     金融支付必须预占库存传0;

	private String phone;//	Stirng	非必须	座机号 
	private String remark;//	Stirng	非必须	备注（少于100字）
	private String invoiceName;//String	非必须	增值票收票人姓名     (备注：当invoiceType=2 且invoiceState=1时则此字段必填)
	private String invoicePhone;//String	非必须	增值票收票人电话   (备注：当invoiceType=2 且invoiceState=1时则此字段必填)
	private Integer invoiceProvice;//nt	非必须	增值票收票人所在省(京东地址编码)(备注：当invoiceType=2 且invoiceState=1时则此字段必填)
    private Integer invoiceCity;//int	非必须	增值票收票人所在市(京东地址编码)   (备注：当invoiceType=2 且invoiceState=1时则此字段必填)
    private Integer invoiceCounty;//	int	非必须	增值票收票人所在区/县(京东地址编码)   备注：当invoiceType=2 且invoiceState=1时则此字段必填
    private String invoiceAddress;//	String	非必须	增值票收票人所在地址   备注：当invoiceType=2 且invoiceState=1时则此字段必填
    private Integer doOrderPriceMode;//	int	下单价格模式  1:必需验证客户端订单价格快照，如果快照与京东价格不一致返回下单失败，需要更新商品价格后，重新下单;
    private String orderPriceSnap;//	String	客户端订单价格快照	Json格式的数据，格式为:   [{  "price":21.30, "skuId":123123},{"price":99.55, "skuId":22222}]
    	                                                                                //商品价格,类型：BigDecimal,商品编号,类型：long
    private Integer reservingDate;//int	大家电配送日期	默认值为-1，0表示当天，1表示明天，2：表示后天; 如果为-1表示不使用大家电预约日历	   
    private Integer installDate;//	int	大家电安装日期	不支持默认按-1处理，0表示当天，1表示明天，2：表示后天
    private boolean needInstall;//	是否选择了安装，默认为true，选择了“暂缓安装”，此为必填项，必填值为false。
    private String promiseDate;//	String	中小件配送预约日期	格式：yyyy-MM-dd
    private String promiseTimeRange;//String	中小件配送预约时间段	时间段如： 9:00-15:00
    private Integer promiseTimeRangeCode;//	Integer	中小件预约时间段的标记	
	public String getThirdOrder() {
		return thirdOrder;
	}
	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getCounty() {
		return county;
	}
	public void setCounty(Integer county) {
		this.county = county;
	}
	public Integer getTown() {
		return town;
	}
	public void setTown(Integer town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getInvoiceState() {
		return invoiceState;
	}
	public void setInvoiceState(Integer invoiceState) {
		this.invoiceState = invoiceState;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Integer getSelectedInvoiceTitle() {
		return selectedInvoiceTitle;
	}
	public void setSelectedInvoiceTitle(Integer selectedInvoiceTitle) {
		this.selectedInvoiceTitle = selectedInvoiceTitle;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getInvoiceContent() {
		return invoiceContent;
	}
	public void setInvoiceContent(Integer invoiceContent) {
		this.invoiceContent = invoiceContent;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public Integer getIsUseBalance() {
		return isUseBalance;
	}
	public void setIsUseBalance(Integer isUseBalance) {
		this.isUseBalance = isUseBalance;
	}
	public Integer getSubmitState() {
		return submitState;
	}
	public void setSubmitState(Integer submitState) {
		this.submitState = submitState;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInvoiceName() {
		return invoiceName;
	}
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	public String getInvoicePhone() {
		return invoicePhone;
	}
	public void setInvoicePhone(String invoicePhone) {
		this.invoicePhone = invoicePhone;
	}
	public Integer getInvoiceProvice() {
		return invoiceProvice;
	}
	public void setInvoiceProvice(Integer invoiceProvice) {
		this.invoiceProvice = invoiceProvice;
	}
	public Integer getInvoiceCity() {
		return invoiceCity;
	}
	public void setInvoiceCity(Integer invoiceCity) {
		this.invoiceCity = invoiceCity;
	}
	public Integer getInvoiceCounty() {
		return invoiceCounty;
	}
	public void setInvoiceCounty(Integer invoiceCounty) {
		this.invoiceCounty = invoiceCounty;
	}
	public String getInvoiceAddress() {
		return invoiceAddress;
	}
	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}
	public Integer getDoOrderPriceMode() {
		return doOrderPriceMode;
	}
	public void setDoOrderPriceMode(Integer doOrderPriceMode) {
		this.doOrderPriceMode = doOrderPriceMode;
	}
	public String getOrderPriceSnap() {
		return orderPriceSnap;
	}
	public void setOrderPriceSnap(String orderPriceSnap) {
		this.orderPriceSnap = orderPriceSnap;
	}
	public Integer getReservingDate() {
		return reservingDate;
	}
	public void setReservingDate(Integer reservingDate) {
		this.reservingDate = reservingDate;
	}
	public Integer getInstallDate() {
		return installDate;
	}
	public void setInstallDate(Integer installDate) {
		this.installDate = installDate;
	}
	public boolean isNeedInstall() {
		return needInstall;
	}
	public void setNeedInstall(boolean needInstall) {
		this.needInstall = needInstall;
	}
	public String getPromiseDate() {
		return promiseDate;
	}
	public void setPromiseDate(String promiseDate) {
		this.promiseDate = promiseDate;
	}
	public String getPromiseTimeRange() {
		return promiseTimeRange;
	}
	public void setPromiseTimeRange(String promiseTimeRange) {
		this.promiseTimeRange = promiseTimeRange;
	}
	public Integer getPromiseTimeRangeCode() {
		return promiseTimeRangeCode;
	}
	public void setPromiseTimeRangeCode(Integer promiseTimeRangeCode) {
		this.promiseTimeRangeCode = promiseTimeRangeCode;
	}
    
    
    


}
