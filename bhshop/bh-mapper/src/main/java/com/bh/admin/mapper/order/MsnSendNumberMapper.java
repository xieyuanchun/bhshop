package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.MsnSendNumber;
import com.bh.admin.pojo.order.Phone;

public interface MsnSendNumberMapper {
    int deleteByPrimaryKey(Integer senmsnnumId);

    int insert(MsnSendNumber record);

    int insertSelective(MsnSendNumber record);

    MsnSendNumber selectByPrimaryKey(Integer senmsnnumId);

    int updateByPrimaryKeySelective(MsnSendNumber record);

    int updateByPrimaryKey(MsnSendNumber record);
    
    List<Phone> selectExportGoods(Integer apymsnId);
}