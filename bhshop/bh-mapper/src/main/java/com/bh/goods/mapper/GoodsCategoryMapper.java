package com.bh.goods.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.goods.pojo.MyGoodsCategoryPojo;

public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);
    
    
    
    
    
    int insertParent(Map<String, Object> map);
    
	List<GoodsCategory> getListByFirstReid(String name, Integer currentPage, Integer pageSize, Long reid);
	
	int countAll(String name, Long reid);
	
	int delectCount(Long reid);
	
	GoodsCategory selectByNameAndSeries(String name, Short series, Long reid, Integer isJd);
	GoodsCategory insertselectByName(String name, Short series, Integer isJd);
	
	List<GoodsCategory> getByName(String name);
	
	List<GoodsCategory> selectByParent(Long reid);
	
	List<GoodsCategory> selectAllByParent(Long reid);
	
	List<GoodsCategory> homeZeroList(Long reid);
	
	List<GoodsCategory> homeOneList(Long reid);
	
	List<GoodsCategory> homeTwoList(Long reid);
	
	List<GoodsCategory> selectAll();
	
	List<GoodsCategory> selectThreeLevel();
	
	List<GoodsCategory> selectLastLevel();
	
	List<GoodsCategory> selectByName(String name);
	
	List<GoodsCategory> selectUpdateByName(GoodsCategory record);
	
	List<MyGoodsCategoryPojo> selectTopSix();
	
	List<GoodsCategory> batchSelect(List<String> list);
	
	List<GoodsCategory> getCatesByCatId(Integer catId);
	
	List<GoodsCategory> selectAllByName();
	
	List<GoodsCategory> selectAllByReid(Long reid);
	
	List<GoodsCategory> getFirstLevelList();
	
	List<GoodsCategory> getNextByReid(Long reid);
	
	List<GoodsCategory> getListByName(String name);
	
	List<GoodsCategory> getByLevel(GoodsCategory record);
	
	List<GoodsCategory> getByReid(GoodsCategory record);
	
	//cheng
	List<GoodsCategory> getBySeries(GoodsCategory record);
	List<GoodsCategory> selectLastLevel1(GoodsCategory g);
	List<GoodsCategory> selectGoodsCategoryById(GoodsCategory g);
	List<GoodsCategory> selectHistoryCategory(GoodsCategory g);
	List<GoodsCategory> selectCategoryByreid();
	
	//zlk
	List<GoodsCategory> selectAllCategory();
}