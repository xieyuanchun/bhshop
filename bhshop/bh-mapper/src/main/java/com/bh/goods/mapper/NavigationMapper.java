package com.bh.goods.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.bh.user.vo.NavigationVo;

public interface NavigationMapper {
    List<NavigationVo> selectNavigationMsg(@Param("usingObject")Integer usingObject);
   
}