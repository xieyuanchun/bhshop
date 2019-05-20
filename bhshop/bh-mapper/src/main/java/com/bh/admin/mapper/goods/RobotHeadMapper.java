package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.RobotHead;

public interface RobotHeadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotHead record);

    int insertSelective(RobotHead record);

    RobotHead selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotHead record);

    int updateByPrimaryKey(RobotHead record);

	List<RobotHead> getALL();
}