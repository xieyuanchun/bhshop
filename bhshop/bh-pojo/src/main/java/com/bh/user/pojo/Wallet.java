package com.bh.user.pojo;

import java.io.Serializable;
import java.util.List;

import com.bh.jd.bean.order.Track;

public class Wallet implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer uid;

    private String name;

    private String type;

    private String des;

    private Integer money;

    private String salt;

    private String payPassword;

    private Integer withdrawCash;
    
	private String currentPage;
    
	private String orderNo;
	
	private String money2; //zlk
	
	//zlk 银行卡信息

	private List<MemberBankCard> memberBankCard;

	private String cardNum; //0没有，1有
	
	
	
    public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des == null ? null : des.trim();
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public Integer getWithdrawCash() {
        return withdrawCash;
    }

    public void setWithdrawCash(Integer withdrawCash) {
        this.withdrawCash = withdrawCash;
    }

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMoney2() {
		return money2;
	}

	public void setMoney2(String money2) {
		this.money2 = money2;
	}

	public List<MemberBankCard> getMemberBankCard() {
		return memberBankCard;
	}

	public void setMemberBankCard(List<MemberBankCard> memberBankCard) {
		this.memberBankCard = memberBankCard;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

    
    
	
    
}