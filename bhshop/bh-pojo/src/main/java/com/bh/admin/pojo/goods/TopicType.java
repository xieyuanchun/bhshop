package com.bh.admin.pojo.goods;

public class TopicType {
    private Integer id;

    private Integer typeid;

    private String name;

    private Byte status;
    
    private String desc;

    private String thumb;
    
    private Integer shopId;
    
  	public Integer getShopId() {
  		return shopId;
  	}

  	public void setShopId(Integer shopId) {
  		this.shopId = shopId;
  	}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getTypeid() {
		return typeid;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@Override
	public String toString() {
		return "TopicType [id=" + id + ", typeid=" + typeid + ", name=" + name + ", status=" + status + ", desc=" + desc
				+ ", thumb=" + thumb + "]";
	}
    
	
	
}