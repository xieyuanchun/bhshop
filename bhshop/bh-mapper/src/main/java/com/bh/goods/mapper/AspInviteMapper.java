package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.AspInvite;

public interface AspInviteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AspInvite record);

    int insertSelective(AspInvite record);

    AspInvite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AspInvite record);

    int updateByPrimaryKey(AspInvite record);
    
    List<AspInvite> selectByReqUserId(Integer mId);

    AspInvite getByInvitedUserId(int invitedUserId);

	int getSuccessInvitedNum(int reqUserId);

}