package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.ShopAuditLog;

public interface ShopAuditLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopAuditLog record);

    int insertSelective(ShopAuditLog record);

    ShopAuditLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopAuditLog record);

    int updateByPrimaryKey(ShopAuditLog record);

	List<ShopAuditLog> getByInfoId(Integer id);
}