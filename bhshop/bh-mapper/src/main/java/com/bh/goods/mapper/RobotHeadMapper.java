package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.RobotHead;

public interface RobotHeadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotHead record);

    int insertSelective(RobotHead record);

    RobotHead selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotHead record);

    int updateByPrimaryKey(RobotHead record);

	List<RobotHead> getALL();
}