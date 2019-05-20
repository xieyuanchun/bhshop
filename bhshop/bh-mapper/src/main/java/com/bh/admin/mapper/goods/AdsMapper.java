package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.Ads;

public interface AdsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ads record);

    int insertSelective(Ads record);

    Ads selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Ads record);

    int updateByPrimaryKeyWithBLOBs(Ads record);

    int updateByPrimaryKey(Ads record);
    
    
    
    List<Ads> selectListByIsMain(Integer isPc);
    
    List<Ads> selectListBySortNum(Integer sortnum);
    
    List<Ads> pageList(@Param("isPc") String isPc, @Param("isMain") String isMain, String name, @Param("fz") String fz);
    
    /*获取所有广告，按排序号递减*/
    List<Ads> selectAll();
    
    int countAll();
    
    List<Ads> getList();
    
    List<Ads> getListOrderbySortNum();
    
    /*批量删除*/
    int batchDelete(List<String> list);
    
    List<Ads> batchSelect(List<String> list);
}