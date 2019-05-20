package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.JdGoodsNotice;

public interface JdGoodsNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdGoodsNotice record);

    int insertSelective(JdGoodsNotice record);

    JdGoodsNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdGoodsNotice record);

    int updateByPrimaryKey(JdGoodsNotice record);
    
    
    
    List<JdGoodsNotice> pageList(JdGoodsNotice record);
    
    List<JdGoodsNotice> selectByJdSkuNoValid(JdGoodsNotice record);
}