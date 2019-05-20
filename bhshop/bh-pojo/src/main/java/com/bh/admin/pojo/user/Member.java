package com.bh.admin.pojo.user;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3123702822876920814L;

	private Integer id;

    private String username;

    private String password;

    private String salt;

    private String headimgurl;

    private Integer flagUser;

    private Integer type;

    private String im;
    
    private int isNew;
    
    private String openid;
    
    private String phone ;//小程添加2017.8.30(曾工提出的)
    private String email ;//2017-9-12((曾工提出的)
    
    
    private String top; //排名
    
    private int orderNum; //接单数量
    
    private int waitNum; //开团差多少人
    
    private String waitTime; //倒计时
    
    private String teamNo; //团号
    
    
    
    
    
    

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public int getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(int waitNum) {
		this.waitNum = waitNum;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public Integer getFlagUser() {
        return flagUser;
    }

    public void setFlagUser(Integer flagUser) {
        this.flagUser = flagUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im == null ? null : im.trim();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
    
    
    
    
}