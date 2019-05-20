package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.Navigation;

public interface NavigationMapper {
  
    int insertSelective(Navigation record);

    int updateByPrimaryKeySelective(Navigation record);

    List<Navigation> selectList(@Param("usingObject")Integer usingObject);
}