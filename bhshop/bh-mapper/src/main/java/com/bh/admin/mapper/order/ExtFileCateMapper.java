package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.ExtFileCate;
import com.bh.utils.PageBean;



public interface ExtFileCateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExtFileCate record);

    int insertSelective(ExtFileCate record);

    ExtFileCate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExtFileCate record);

    int updateByPrimaryKey(ExtFileCate record);

	List<ExtFileCate> getExtFileCateList();//查询所有文件分类
}