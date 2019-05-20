package com.bh.user.pojo;

public class MemberUserAddress {
    private Integer id;

    private Integer mId;

    private String fullName;

    private Integer prov;

    private Integer city;

    private Integer area;
    
    private Integer four;//如果没有第四级就传0

    private String address;

    private String tel;

    private Boolean isDefault;//当为true时1默认收货地址，false0

    private String provname;

    private String cityname;

    private String areaname;
    
    private String fourname;//第四级地址
    
    private Integer isDel;//0未删除，1已删除,m默认0 2017-10-31chengfengyun添加
    
    private Integer easybuy;//该地地址是否用于一键购的地址，0用户，1不用，默认1   2017 -10 - 30cheng添加
    

    private String label;//2017-10-31chengfengyun添加,标签
    
    private String telephone;//2017-10-31chengfengyun添加,固定电话
    
    private Integer isJd;//是否是京东地址，0否，1是

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

	public String getProvname() {
		return provname;
	}

	public void setProvname(String provname) {
		this.provname = provname;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	

	public Integer getEasybuy() {
		return easybuy;
	}

	public void setEasybuy(Integer easybuy) {
		this.easybuy = easybuy;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getIsJd() {
		return isJd;
	}

	public void setIsJd(Integer isJd) {
		this.isJd = isJd;
	}

	public Integer getFour() {
		return four;
	}

	public void setFour(Integer four) {
		this.four = four;
	}

	public String getFourname() {
		return fourname;
	}

	public void setFourname(String fourname) {
		this.fourname = fourname;
	}

	
	
    
    
}