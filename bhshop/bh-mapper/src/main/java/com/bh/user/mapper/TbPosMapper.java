package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.POSParam;
import com.bh.user.pojo.TbPos;

public interface TbPosMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbPos record);

    int insertSelective(TbPos record);

    TbPos selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbPos record);

    int updateByPrimaryKey(TbPos record);
    
    //程凤云条件查询pos
    List<TbPos> selectTbPosListByP(TbPos pos);
    int updateTbPos(TbPos pos);
    List<TbPos> selectPosList(POSParam pos);
    int updateNameAndPhone(TbPos pos);
}