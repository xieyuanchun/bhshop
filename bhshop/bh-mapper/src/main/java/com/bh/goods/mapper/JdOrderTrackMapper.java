package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.JdOrderTrack;

public interface JdOrderTrackMapper {
    int insert(JdOrderTrack record);

    int insertSelective(JdOrderTrack record);
    //根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
    List<JdOrderTrack> getByOrderId(JdOrderTrack record);
}