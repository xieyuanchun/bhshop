package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.PrizeSet;

public interface PrizeSetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PrizeSet record);

    int insertSelective(PrizeSet record);

    PrizeSet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PrizeSet record);

    int updateByPrimaryKeyWithBLOBs(PrizeSet record);

    int updateByPrimaryKey(PrizeSet record);
    
    
    List<PrizeSet> listPage(PrizeSet record);
    
    List<PrizeSet> selectAll();
    
    List<PrizeSet> selectByIsDefault();
    
    //zlk 中奖设置和当前的商品列表
    List<PrizeSet> getGoodAndPrize(PrizeSet record);
}