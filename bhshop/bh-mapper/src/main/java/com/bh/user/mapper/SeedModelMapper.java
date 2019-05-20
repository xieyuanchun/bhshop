package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.SeedModel;
import com.bh.user.pojo.SeedParam;

public interface SeedModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SeedModel record);

    int insertSelective(SeedModel record);

    SeedModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SeedModel record);

    int updateByPrimaryKey(SeedModel record);
    
    
    /*************cheng****************/
    //种子模型列表
    List<SeedModel> selectSeedModelByParams1(SeedModel seed);
    List<SeedModel> selectModelByParam(SeedParam seedParam);
    int selectSeedModelBySeedName(SeedModel seedModel);
    int selectCountBySeedName(SeedModel seedModel);
    List<SeedModel> selectUserLand(SeedModel seedModel);
    List<SeedModel> selectUnuserLand(SeedModel seedModel);
}