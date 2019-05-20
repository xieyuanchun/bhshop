package com.bh.admin.mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.bh.admin.pojo.order.OrderTeam;

import com.bh.admin.pojo.order.OrderTeam;


public interface OrderTeamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderTeam record);

    int insertSelective(OrderTeam record);

    OrderTeam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderTeam record);

    int updateByPrimaryKey(OrderTeam record);
    
    List<OrderTeam> selectByGoodsIdList(Integer goodsId);
    
    
    List<OrderTeam> getByTeamNo(String teamNo);
    
    OrderTeam getByOrderNo(String orderNo);
    
    List<OrderTeam> getGroupingList(Integer goodsId);
    
    List<OrderTeam> pageList(OrderTeam record);
    
    List<OrderTeam> getMheadList(Integer goodsId);
    
    int getGroupCount(Integer goodsId);
    
    int groupCount(String teamNo);
    
    OrderTeam getByMidAndTeamNo(Integer mId, String teamNo);
    
    OrderTeam getByTeamNoAndOwner(String teamNo);
    
    OrderTeam getByMidAndOwner(Integer mId);
   
    int updateByTeamNo(String teamNo);
    
    List<OrderTeam> listPage(OrderTeam record);

    // cheng  通过orderNo查询orderTeam查看它是否存在
    List<OrderTeam> selectOrderTeanByOrderNo(String orderNo);
    List<OrderTeam> selectTeamTimeEndTask();
    List<OrderTeam> selectLastOne(String teamNo);
    List<OrderTeam> getTeamBySkuId(OrderTeam record);
    List<OrderTeam> getTeamList(OrderTeam record);
    List<OrderTeam> selectOrderTeanByOrderNoAndStatus(@Param("orderNo") String orderNo);
    List<OrderTeam> selectTeamMsg(@Param("goodsSkuId")Integer goodsSkuId);
    OrderTeam getTeamNoAndStatusByOrderNo(String orderNo);
    int countIdByTeamNoAndType(String teamNo, Integer type);
    
    
    
    int countByTeamNoAndType(String teamNo, Integer type);
 

    List<OrderTeam> listPageByLimit(OrderTeam record);
    int getAll(OrderTeam record);
}