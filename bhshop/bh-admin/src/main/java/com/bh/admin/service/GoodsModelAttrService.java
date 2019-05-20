package com.bh.admin.service;
import com.bh.admin.pojo.goods.GoodsModelAttr;
import com.bh.utils.PageBean;

public interface GoodsModelAttrService {
	
	/*平台后台模型属性值列表*/
	PageBean<GoodsModelAttr> selectPageBymId(String mId, String currentPage, String pageSize) throws Exception;
	
	/*模型属性值的添加*/
	int insertModelAttr(String mId, String name, String search, String value) throws Exception;
	
	/*商品模型值的删除*/
	int deleteModelAttr(String id) throws Exception;
	
	/*商品模型值的修改*/
	int editModelAttr(String id, String name, String search, String value) throws Exception;
	
	
}
