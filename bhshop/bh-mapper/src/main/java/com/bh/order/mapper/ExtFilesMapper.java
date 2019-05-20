package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.ExtFiles;

public interface ExtFilesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExtFiles record);

    int insertSelective(ExtFiles record);

    ExtFiles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExtFiles record);

    int updateByPrimaryKey(ExtFiles record);
    
    List<ExtFiles> getExtFilesList(Integer file_type);//分页查询图片  XIEYC

	List<ExtFiles> getExtFilesByCateId(Integer cate_id);//根据cate_id查询文件 xieyc
}