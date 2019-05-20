package com.bh.admin.pojo.user;

import java.util.List;

public class MemberUserSimple {
    private String fullName;//姓名
    
	private String identity;//身份证号
	
	private double balance;//账户余额
	
	private double sudabalance;//速达账户余额
	
	private List<BankSimple> bankSimple;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<BankSimple> getBankSimple() {
		return bankSimple;
	}

	public void setBankSimple(List<BankSimple> bankSimple) {
		this.bankSimple = bankSimple;
	}

	public double getSudabalance() {
		return sudabalance;
	}

	public void setSudabalance(double sudabalance) {
		this.sudabalance = sudabalance;
	}

	
	
	
	
	
	
}
