package com.bh.admin.mapper.order;

import com.bh.admin.pojo.order.MsnTemplate;

import java.util.List;

public interface MsnTemplateMapper {
    int deleteByPrimaryKey(Integer tempId);

    int insert(MsnTemplate record);

    int insertSelective(MsnTemplate record);

    MsnTemplate selectByPrimaryKey(Integer tempId);

    int updateByPrimaryKeySelective(MsnTemplate record);

    int updateByPrimaryKey(MsnTemplate record);


    List<MsnTemplate> selectByShopIdAndApyId(Integer shopId, Integer apymsnId);

    int updateByApymsnId(MsnTemplate msnTemplate);

}