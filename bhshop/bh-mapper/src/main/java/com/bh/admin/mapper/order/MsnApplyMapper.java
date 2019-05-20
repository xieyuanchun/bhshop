package com.bh.admin.mapper.order;

import com.bh.admin.pojo.order.MsnApply;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.order.MyMsnApply;
import com.bh.admin.pojo.order.Phone;

import java.util.Date;
import java.util.List;

public interface MsnApplyMapper {
    int deleteByPrimaryKey(Integer apymsnId);

    int insert(MsnApply record);

    int insertSelective(MsnApply record);

    MsnApply selectByPrimaryKey(Integer apymsnId);

    int updateByPrimaryKeySelective(MsnApply record);

    int updateByPrimaryKey(MsnApply record);



    List<MsnApply> selectByShopId(Integer shopid);

    List<MsnApply> selectMsnList(@Param("shopId")Integer shopId,@Param("payStatus")Integer payStatus,@Param("addTime")Date addTime);

    
    MsnApply checkIsPaySeccuss(String orderNo);
    
    MyMsnApply selectByApymsnId(Integer apymsnId);
    
    
    List<MyMsnApply> selectByShopMessageList(@Param("shopId") Integer shopId, @Param("startTimes") Date startTimes,@Param("endTimes") Date endTimes,@Param("payStatus") String payStatus,@Param("reviewResult") String reviewResult);
    
    List<MyMsnApply> selectByMessageList(@Param("startTimes") Date startTimes,@Param("endTimes") Date endTimes,@Param("payStatus") String payStatus,@Param("reviewResult") String reviewResult);
    
    List<MyMsnApply> mselectByShopMessageList(Integer shopId);
    
    int selectCustCount(@Param("shopId")Integer shopId, @Param("memberType")String memberType);

    List<Phone> selectCustList(@Param("shopId")Integer shopId,  @Param("memberType")String memberType);
    
    List<MsnApply> selectNoPayList();
    
    


}