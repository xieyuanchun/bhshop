package com.bh.admin.mapper.goods;

import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.goods.ActZone;


public interface ActZoneMapper {
	 int deleteByPrimaryKey(Integer id);

	    int insert(ActZone record);

	    int insertSelective(ActZone record);

	    ActZone selectByPrimaryKey(long id);

	    int updateByPrimaryKeySelective(ActZone record);

	    int updateByPrimaryKey(ActZone record);
    
     int insertParent(Map<String, Object> map);
     
     int deleteById(Integer id);
   
      ActZone selectById(Integer id);
      
      int deleteByZoneId(Integer id);
      
      List<ActZone> selectByReids(Integer id);
    
	List<ActZone> getListByFirstReid(String name, Integer currentPage, Integer pageSize, Integer reid);
	
	int countAll(String name, Integer reid);
	
	int delectCount(Integer reid);
	
	int deleteReid(Integer reid);

	ActZone selectByNameAndlevelNum(String name, String levelnum, Integer reid);
	
	ActZone insertselectByName(String name, String levelnum,Integer reid);
	
	ActZone insertselectByName1(String name, String levelnum,Integer reid,Integer sortnum);
	
	List<ActZone> getByName(String name);
	
	List<ActZone> selecteByReid(Integer reid);
	
	List<ActZone> selectByParent(Integer reid);
	
	List<ActZone> selectAllByParent(Integer reid);
	
	List<ActZone> homeZeroList(Integer reid);
	
	List<ActZone> homeOneList(Integer reid);
	
	List<ActZone> homeTwoList(Integer reid);
	
	List<ActZone> selectAll();
	
	List<ActZone> selectThreeLevel();
	
	List<ActZone> selectLastLevel();
	
	List<ActZone> selectByName(String name);
	
	List<ActZone> selectUpdateByName(ActZone record);
	
	List<ActZone> selectTopSix();
	
	List<ActZone> batchSelect(List<String> list);
	
	List<ActZone> getCatesByCatId(Integer catId);
	
	List<ActZone> selectAllByName();
	
	List<ActZone> selectAllByReid(long reid);
	
	List<ActZone> getFirstLevelList();
	
	List<ActZone> getNextByReid(Integer reid);
	
	List<ActZone> getListByName(String name);
	
	List<ActZone> getByLevel(ActZone record);
	
	List<ActZone> getByReid(ActZone record);
	
	//cheng
	List<ActZone> getByLevelNum(ActZone record);
	List<ActZone> selectLastLevel1(ActZone g);
	List<ActZone> selectActZoneById(ActZone g);
	List<ActZone> selectHistoryCategory(ActZone g);
	List<ActZone> selectCategoryByreid();
	
	//zlk
	List<ActZone> selectAllCategory();
	
	String getUuid();
	List<ActZone> selectActZoneList();
	
}