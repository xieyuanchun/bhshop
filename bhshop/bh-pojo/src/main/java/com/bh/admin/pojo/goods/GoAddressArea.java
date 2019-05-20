package com.bh.admin.pojo.goods;

import java.util.List;

public class GoAddressArea {
    private Integer id;

    private Byte level;

    private Integer parentId;

    private String name;
    
    private List<GoAddressArea> areas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

	public List<GoAddressArea> getAreas() {
		return areas;
	}

	public void setAreas(List<GoAddressArea> areas) {
		this.areas = areas;
	}

	
	
    
}