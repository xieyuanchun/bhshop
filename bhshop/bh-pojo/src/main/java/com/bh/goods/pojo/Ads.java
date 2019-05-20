package com.bh.goods.pojo;

import java.util.Date;

public class Ads {
    private Integer id;

    private Date createtime;

    private String name;

    private String image;
    
    private String content;
    
    private String link;

    private String sLink;
    
    private Integer sortnum;

    private Byte isMain;
    
    private Integer isPc;
    
    private Integer status; //广告的状态0，正常，1使用中，2禁止
    
    
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsPc() {
		return isPc;
	}

	public void setIsPc(Integer isPc) {
		this.isPc = isPc;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Byte getIsMain() {
        return isMain;
    }

    public void setIsMain(Byte isMain) {
        this.isMain = isMain;
    }

	public String getsLink() {
		return sLink;
	}

	public void setsLink(String sLink) {
		this.sLink = sLink;
	}
    
    
}