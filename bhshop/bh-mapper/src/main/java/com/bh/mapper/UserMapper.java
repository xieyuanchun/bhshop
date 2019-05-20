package com.bh.mapper;

import java.util.List;
import java.util.Map;

import com.bh.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id) throws Exception;

    int insert(User record) throws Exception;

    int insertSelective(User record) throws Exception;

    User selectByPrimaryKey(Integer id) throws Exception;

    int updateByPrimaryKeySelective(User record) throws Exception;

    int updateByPrimaryKey(User record) throws Exception;
    
    List<Map<String, Object>> selectByPrimaryKey1(Integer id) throws Exception;

    int insertuser(Map<String, Object> map) throws Exception;
}