package com.bh.admin.pojo.goods;

import java.util.List;

public class MyGoodsCategory {

  private Long id;
	
  private Long childId;
  
  private Long reid;
  
  private String name;
  
  private String parentName;
  
  private List childList;


public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Long getChildId() {
	return childId;
}

public void setChildId(Long childId) {
	this.childId = childId;
}

public Long getReid() {
	return reid;
}

public void setReid(Long reid) {
	this.reid = reid;
}

public String getParentName() {
	return parentName;
}

public void setParentName(String parentName) {
	this.parentName = parentName;
}

public List getChildList() {
	return childList;
}

public void setChildList(List childList) {
	this.childList = childList;
}


public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}
  
  
   
	
    
    
}