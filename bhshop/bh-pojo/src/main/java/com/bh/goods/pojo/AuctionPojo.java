package com.bh.goods.pojo;
/**
 * 
 * @Description: 荷兰式拍卖pojo
 * @author xieyc
 * @date 2018年5月30日 上午10:03:18 
 *
 */
public class AuctionPojo {
	
    private double depositPrice;//荷兰式拍卖的交纳金额 xieyc
    
    private double deductionPrice;//抵扣金额 xieyc
    
    private boolean flag;//是否调用支付接口 （true 调用  fasle调用 http://localhost:8080/bh-product-api/api/dauction/depositJsp.json这个接口支付）
    
    private int fz;//xieyc
    
    private double  realPayPrice;//实际支付金额（订单总价-保证金）

	public double getDepositPrice() {
		return depositPrice;
	}

	public void setDepositPrice(double depositPrice) {
		this.depositPrice = depositPrice;
	}

	public double getDeductionPrice() {
		return deductionPrice;
	}

	public void setDeductionPrice(double deductionPrice) {
		this.deductionPrice = deductionPrice;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getFz() {
		return fz;
	}

	public void setFz(int fz) {
		this.fz = fz;
	}

	public double getRealPayPrice() {
		return realPayPrice;
	}

	public void setRealPayPrice(double realPayPrice) {
		this.realPayPrice = realPayPrice;
	}

	

}
