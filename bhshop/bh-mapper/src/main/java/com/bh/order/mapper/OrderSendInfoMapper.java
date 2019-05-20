package com.bh.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.order.pojo.OrderSendInfo;

public interface OrderSendInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSendInfo record);

    int insertSelective(OrderSendInfo record);

    OrderSendInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSendInfo record);

    int updateByPrimaryKey(OrderSendInfo record);
    
    /*********cheng2017-11-14通过mId和orderShopId查询*************/
    List<OrderSendInfo> selectOrderShopBymid(OrderSendInfo or);
    int selectSendMarkBymid(OrderSendInfo info);
    
    /*********scj************/
    List<OrderSendInfo> selectBySidAndStatus(Integer sId, @Param("status") String status);
    
    int countBySid(Integer sId);
    
    List<OrderSendInfo> selectAllByOrderShopId(@Param("list") List<String> list, String shopOrderNo, @Param("dState") String dState);
    
    int countByTimeSidAndLevel(Integer sId, String date, @Param("fz") Integer fz);
    
    List<OrderSendInfo> selectByTimeSidAndLevel(Integer sId, String date, @Param("fz") Integer fz);
    
    List<OrderSendInfo> selectByjdOrderId(OrderSendInfo record);
    
    int updateByjdOrderId(OrderSendInfo record);
    /**ZLK**/
    OrderSendInfo selectByOrderShopId(OrderSendInfo record);

	List<OrderSendInfo> getSendInfoByShopId(int shopId);
	List<OrderSendInfo> getSendInfo(OrderSendInfo info);
}