package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.JdOrderTrack;

public interface JdOrderTrackMapper {
    int insert(JdOrderTrack record);

    int insertSelective(JdOrderTrack record);
    //根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
    List<JdOrderTrack> getByOrderId(JdOrderTrack record);
}