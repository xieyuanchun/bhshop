package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.Menu;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKeyWithBLOBs(Menu record);

    int updateByPrimaryKey(Menu record);
    
    
    
    
    List<Menu> selectAll();
    
    int countAll();
    
    /*批量删除*/
    int batchDelete(List<String> list);
    
    List<Menu> selectByAdid(Integer adId);
    
    /*后台根据父类id获取下级菜单*/
    List<Menu> getListByReid(Integer reid);
    
    /*后台获取一级菜单*/
    List<Menu> getFirstLevel();
    
    /*一级菜单*/
    List<Menu> selectFirstLevel();
    
    /*二级菜单*/
    List<Menu> selectTwoLevel(Integer reid);
}