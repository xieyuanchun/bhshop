package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.SeedModel;
import com.bh.admin.pojo.user.SeedParam;


public interface SeedModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SeedModel record);

    int insertSelective(SeedModel record);

    SeedModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SeedModel record);

    int updateByPrimaryKey(SeedModel record);
    
    
    /*************cheng****************/
    //种子模型列表
    List<SeedModel> selectSeedModelByParams(SeedModel seed);
    List<SeedModel> selectSeedModelByParams1(SeedModel seed);
    List<SeedModel> selectModelByParam(SeedParam seedParam);
    int selectSeedModelBySeedName(SeedModel seedModel);
    int selectCountBySeedName(SeedModel seedModel);
    List<SeedModel> selectUserLand(SeedModel seedModel);
    List<SeedModel> selectUnuserLand(SeedModel seedModel);
}