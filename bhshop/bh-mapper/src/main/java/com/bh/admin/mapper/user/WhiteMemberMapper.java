package com.bh.admin.mapper.user;

import java.util.List;
import com.bh.admin.pojo.user.WhiteMember;

public interface WhiteMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WhiteMember record);

    int insertSelective(WhiteMember record);

    WhiteMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteMember record);

    int updateByPrimaryKey(WhiteMember record);
    
    List<WhiteMember> pageList(WhiteMember g);
    
    List<WhiteMember> getByMid(WhiteMember w);
    
    String getAll();
}