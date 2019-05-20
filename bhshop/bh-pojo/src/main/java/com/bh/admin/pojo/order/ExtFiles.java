package com.bh.admin.pojo.order;

import java.util.Date;

public class ExtFiles {
    private Integer id;

    private Integer cateId;
    
    //文件名称
    private String name;
    
    //文件后辍名
    private String ext;
    
    //文件路径
    private String fileUrl;
    
    //文件类型 0图片 1视频
    private Integer fileType;
    
    //文件大小filelength
    private String size;

    private Date addTime;
    
    //图片尺寸
    private String picSize;
    
    //商家的id
    private Integer shopId;
    
    //是否删除,0不删除,1删除
    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

   

    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
    
}