package com.bh.admin.mapper.goods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.MyGoodsCategory;
public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);
    
    List<GoodsCategory> serchById(long id);
    
    GoodsCategory selectByCatId(long catId);
    
    int insertParent(Map<String, Object> map);
    
	List<GoodsCategory> getListByFirstReid(String name, Integer currentPage, Integer pageSize, Long reid);
	
	int countAll(String name, Long reid);
	
	int delectCount(Long reid);
	
	GoodsCategory selectByNameAndSeries(String name, Short series, Long reid, Integer isJd);
	GoodsCategory insertselectByName(String name, Short series, Integer isJd);
	
	List<GoodsCategory> getByName(@Param("name")String name);
	
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
	
	List<GoodsCategory> selectTopSix();
	
	List<GoodsCategory> batchSelect(List<String> list);
	
	List<GoodsCategory> getCatesByCatId(Integer catId);
	
	List<GoodsCategory> selectAllByName();
	
	List<GoodsCategory> selectAllByReid(Long reid);
	
	List<GoodsCategory> selectAllByReid1(Long reid);
	
	List<GoodsCategory> selectAllByReid2(Long reid);
	
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
	
	List<MyGoodsCategory> selectById(long id);
	
	List<GoodsCategory> selectByCategoryName(String name);
	List<GoodsCategory> selectByNameAndSeries1(String name);
	List<GoodsCategory> selectByNameAndSeries2(String name);
	List<GoodsCategory> selectByNameAndSeries3(String name);
	
	//获取第一级分类
	List<GoodsCategory> selectByFirst();
	//获取二级分类
	List<GoodsCategory> selectBySecond(String parentId);
	//获取三级分类
	List<GoodsCategory> selectByThree(String parentId);
	
	GoodsCategory selectByReid(long catId);
}