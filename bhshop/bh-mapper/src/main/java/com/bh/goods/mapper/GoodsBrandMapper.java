package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.GoodsBrand;

public interface GoodsBrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsBrand record);

    int insertSelective(GoodsBrand record);

    GoodsBrand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsBrand record);

    int updateByPrimaryKey(GoodsBrand record);
    
    
    List<GoodsBrand> getByCatid(Long catId);
    
    List<GoodsBrand> selectByCatid(Long catId);
    
    List<GoodsBrand> selectPageList(String name);
    
    int countAll(String name);
    
    /*批量删除*/
    int batchDelete(List<String> list);
    
    List<GoodsBrand> selectAll();
    
    GoodsBrand selectByNameAndJd(String name, Integer isJd);
    
    /*新增查重*/
    List<GoodsBrand> selectByName(String name);
    
    /*修改查重*/
    List<GoodsBrand> selectByNameNotMy(String name, Long id);
}