package com.bh.admin.mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.order.ExtFiles;
import com.bh.admin.pojo.order.MyExtFiles;



public interface ExtFilesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExtFiles record);

    int insertSelective(ExtFiles record);

    ExtFiles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExtFiles record);

    int updateByPrimaryKey(ExtFiles record);
    
    List<MyExtFiles> getExtFilesList(@Param("shopId")Integer shopId,@Param("name")String name);//分页查询图片  XIEYC
    
    List<MyExtFiles> getFilesList(@Param("shopId")Integer shopId,@Param("cateId")String cateId,@Param("startTime")String startTime,@Param("endTime")String endTime);//分页查询图片  XIEYC

	List<ExtFiles> getExtFilesByCateId(Integer cate_id);//根据cate_id查询文件 xieyc
}