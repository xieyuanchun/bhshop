package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.SysCacheConf;

public interface SysCacheConfMapper {
    int deleteByPrimaryKey(Integer cacheConfId);

    int insert(SysCacheConf record);

    int insertSelective(SysCacheConf record);

    SysCacheConf selectByPrimaryKey(Integer cacheConfId);

    int updateByPrimaryKeySelective(SysCacheConf record);

    int updateByPrimaryKeyWithBLOBs(SysCacheConf record);

    int updateByPrimaryKey(SysCacheConf record);
    
    
    /**
     * <p>Description: 列表</p>
     *  @author scj  
     *  @date 2018年7月24日
     */
    List<SysCacheConf> listPage(SysCacheConf record);
    /**
     * <p>Description: 根据组名获取数据</p>
     *  @author scj  
     *  @date 2018年7月24日
     */
    List<SysCacheConf> getByConfigGroup(SysCacheConf record);
    /**
     * <p>Description: 根据key获取数据</p>
     *  @author scj  
     *  @date 2018年7月27日
     */
    SysCacheConf  getByConfigKey(SysCacheConf record);
}