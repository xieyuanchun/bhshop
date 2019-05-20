package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.AspInvite;

public interface AspInviteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AspInvite record);

    int insertSelective(AspInvite record);

    AspInvite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AspInvite record);

    int updateByPrimaryKey(AspInvite record);
}