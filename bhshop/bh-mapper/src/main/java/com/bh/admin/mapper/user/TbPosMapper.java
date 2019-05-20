package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.POSParam;
import com.bh.admin.pojo.user.TbPos;


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