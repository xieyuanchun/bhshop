package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsPrice;
import com.bh.admin.pojo.goods.GoodsPriceApproval;
import com.bh.admin.pojo.goods.SimplePriceApproval;


public interface GoodsPriceApprovalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPriceApproval record);

    int insertSelective(GoodsPriceApproval record);

    GoodsPriceApproval selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPriceApproval record);

    int updateByPrimaryKey(GoodsPriceApproval record);
    
    //cheng
    List<GoodsPriceApproval> selectGoodsP(GoodsPriceApproval record);
    List<GoodsPriceApproval> listPage(GoodsPriceApproval val);
    List<GoodsPriceApproval> selectByStatus(GoodsPriceApproval val);
    
    
    //2018-4-16
    List<SimplePriceApproval> listPage1(SimplePriceApproval val);
    List<GoodsPrice> selectPrice(GoodsPrice prive);
    List<GoodsPriceApproval> selectPriceByGoodsSkuId(GoodsPriceApproval record );
    
    List<GoodsPriceApproval> getByGoodsSkuId(GoodsPriceApproval record );
    
    
    
}