package com.bh.admin.pojo.user;

public class MemberShopStep {
	
	private StepOne stepOne;
	
	private MemberShopStepTwo memberShopStepTwo;
	
	private MemberShopStepThree memberShopStepThree;
	
	private MemberShopStepFour memberShopStepFour;
	
	private Integer step;//当前步

	public StepOne getStepOne() {
		return stepOne;
	}

	public void setStepOne(StepOne stepOne) {
		this.stepOne = stepOne;
	}

	public MemberShopStepTwo getMemberShopStepTwo() {
		return memberShopStepTwo;
	}

	public void setMemberShopStepTwo(MemberShopStepTwo memberShopStepTwo) {
		this.memberShopStepTwo = memberShopStepTwo;
	}

	public MemberShopStepThree getMemberShopStepThree() {
		return memberShopStepThree;
	}

	public void setMemberShopStepThree(MemberShopStepThree memberShopStepThree) {
		this.memberShopStepThree = memberShopStepThree;
	}

	public MemberShopStepFour getMemberShopStepFour() {
		return memberShopStepFour;
	}

	public void setMemberShopStepFour(MemberShopStepFour memberShopStepFour) {
		this.memberShopStepFour = memberShopStepFour;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
	
	
	
	
	
}
