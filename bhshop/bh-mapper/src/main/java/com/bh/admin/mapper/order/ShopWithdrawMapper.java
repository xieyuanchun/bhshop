package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.ShopWithdraw;
import com.bh.admin.pojo.order.ShopWithdrawVo;

public interface ShopWithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopWithdraw record);

    int insertSelective(ShopWithdraw record);

    ShopWithdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopWithdraw record);

    int updateByPrimaryKey(ShopWithdraw record);

	ShopWithdraw getByShopIAndApplyTime(Integer shopId);

	List<ShopWithdrawVo> getList(ShopWithdraw entity);

	List<ShopWithdraw> getListByTypeAndShopId(ShopWithdraw entity);

	int countWithdraAmounting(Integer shopId);

	int getAlreadyWithdraAmount(Integer shopId);

	int lockWithdraAmount(Integer shopId);
}