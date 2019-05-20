package com.bh.goods.pojo;

public class TopicNav {
    private Integer id;

    private String name;

    private String iconUrl;

    private String url;

    private Integer pos;
    
    private Integer currentPag;
    
    public Integer getCurrentPag() {
		return currentPag;
	}

	public void setCurrentPag(Integer currentPag) {
		this.currentPag = currentPag;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }
}