package com.bh.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.user.pojo.MemberSeed;

public interface MemberSeedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberSeed record);

    int insertSelective(MemberSeed record);

    MemberSeed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberSeed record);

    int updateByPrimaryKey(MemberSeed record);
    
    
    
    //程凤云
    //选择最大的列值
    int selectScore(Integer mId);
    int selectCount(Integer mId);
    List<MemberSeed> selectMemberSeedByParams(MemberSeed memberSeed);
    List<MemberSeed> selectStoreHouseList(MemberSeed memberSeed);
    int countStoreNum(MemberSeed memberSeed);
    int selectMemberSeedByOrderSeedId(@Param("orderseedId")Integer orderseedId);
    //查询用户购买的土地
    List<MemberSeed> selectUserLand(Integer mId);
    
    
}