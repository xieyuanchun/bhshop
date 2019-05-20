package com.bh.admin.pojo.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MemberUser implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6037371955672880516L;

	private Integer mId;

    private String fullName;//姓名

    private Integer groupId;//用户组


    private String email;//邮箱

    private Integer sex;//0保密，1为男2为女,默认0

    private Integer point;//积分

    private Integer status;//0正常1删除2锁定

    private Date addtime;//注册时间

    private Date endtime;//最后登录时间

    private Long balance;//账户余额单位分

    private Integer prov;//省

    private Integer city;//市

    private Integer area;//区

    private String address;//地址

    private String paycode;//支付密码

    private String year;

    private String month;

    private String day;

    private Integer career;//职业1.学生2.在职3.自由职业0.其他

    private Integer single;//个人情况：1.单身2恋爱中。3已婚，0保密
    
	private String categoryId;
	
	private Integer smId;//模型的id
	
	private String identity;//身份证号
	
    private String wxCode;//微信号

    private String inviteCode;//邀请人的id
    
    private String note;//备注
	

	private List<BankSimple> bankSimple;
	
	 private String currentPage;
	
	
	
	
	
	
	
	private Member member;
	private String key;
	private Integer term;//1.升序，2代表降序
	private String username;
	private String password;
	private String phone ;
	
	private String shopId;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

   

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

   
   

    public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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

	public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getCareer() {
		return career;
	}

	public void setCareer(Integer career) {
		this.career = career;
	}

	

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getSingle() {
		return single;
	}

	public void setSingle(Integer single) {
		this.single = single;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getSmId() {
		return smId;
	}

	public void setSmId(Integer smId) {
		this.smId = smId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public List<BankSimple> getBankSimple() {
		return bankSimple;
	}

	public void setBankSimple(List<BankSimple> bankSimple) {
		this.bankSimple = bankSimple;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getWxCode() {
		return wxCode;
	}

	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
    
	
	
    
    
}