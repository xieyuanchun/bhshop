package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.SysCacheDb;

public interface SysCacheDbMapper {
    int deleteByPrimaryKey(Integer cacheDbId);

    int insert(SysCacheDb record);

    int insertSelective(SysCacheDb record);

    SysCacheDb selectByPrimaryKey(Integer cacheDbId);

    int updateByPrimaryKeySelective(SysCacheDb record);

    int updateByPrimaryKeyWithBLOBs(SysCacheDb record);

    int updateByPrimaryKey(SysCacheDb record);
    
    
    /**
     * <p>Description: 列表</p>
     *  @author scj  
     *  @date 2018年7月30日
     */
    List<SysCacheDb> listPage(SysCacheDb record);
    /**
     * <p>Description: 根据组名获取数据</p>
     *  @author scj  
     *  @date 2018年7月30日
     */
    List<SysCacheDb> getByDbGroup(SysCacheDb record);
    /**
     * <p>Description: 根据key获取数据</p>
     *  @author scj  
     *  @date 2018年7月30日
     */
    SysCacheDb  getByDbKey(SysCacheDb record);
}