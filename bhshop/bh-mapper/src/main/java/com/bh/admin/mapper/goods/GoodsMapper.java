package com.bh.admin.mapper.goods;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.AuctionConfig;
import com.bh.admin.pojo.goods.ExportGoods;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.order.SysTeam;


public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);
    
    int insertTest(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
    
    int updateBySortnum(Integer id ,Short sortNum);
    
    int updateByPrimaryKeyBySelf(Goods record);
    
    int insertToMap(Map<String, String> map);
    
    List<Goods> goodsAll();
    
    List<Goods> selectByBhGoods();
    
    int updateSort(String id,int sortnum);
    
    List<Goods> selectShopGoodsList(@Param("shopId")Integer shopId, String name, @Param("catId") String catId, @Param("saleType") String saleType, @Param("status") String status, @Param("startPrice") String startPrice, @Param("endPrice") String endPrice,
    		@Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo,@Param("isJd")String isJd,@Param("isHotShop")String isHotShop,@Param("isNewShop")String isNewShop);
    
    int countAll(Integer id, String name, @Param("catId") Long catId);
    
    /*平台后台商品列表*/
    List<Goods> backgroundGoodsList(String name,  @Param("status") String status, @Param("isHot") String isHot, @Param("isNew") String isNew,@Param("isFlag") String isFlag, @Param("saleType") String saleType,@Param("startPrice") String startPrice,
    		@Param("endPrice") String endPrice, @Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo, @Param("shopId")String shopId, @Param("tagName")String tagName, @Param("actZoneId")String actZoneId);
    
    /*平台后台商品列表(优化版)*/
    List<Goods> backgroundGoodsLists(String name,  @Param("status") String status, @Param("isHot") String isHot, @Param("isNew") String isNew,@Param("isFlag") String isFlag, @Param("saleType") String saleType,@Param("startPrice") String startPrice,
    		@Param("endPrice") String endPrice, @Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo, @Param("shopId")String shopId, @Param("tagName")String tagName,
    		@Param("actZoneId")String actZoneId,@Param("page") int currentPage,@Param("pageSize")int pageSize);
    
    
    /*分类入口-默认排序*/
    List<Goods> clientCategoryGoodsList(@Param("list") List<String> list, Integer status, Integer pageStart, Integer pageSize, Integer beginPrice, Integer endPrice,  @Param("brandId") Long brandId, @Param("fz") Integer fz);
    
    int clientCountByCategory(@Param("list") List<String> list, Integer status, Integer beginPrice, Integer endPrice, @Param("brandId") Long brandId);
    
    /*查询入口-默认排序*/
    List<Goods> clientGoodsList(String name, Integer status, Integer beginPrice, Integer endPrice, @Param("brandId") Long brandId, @Param("fz") Integer fz);
    //List<Goods> clientGoodsList(String name, Integer status, Integer pageStart, Integer pageSize, Integer beginPrice, Integer endPrice, @Param("brandId") Integer brandId, @Param("fz") Integer fz);
    
    int clientCountAll(String name, Integer status, Integer beginPrice, Integer endPrice, @Param("brandId") Long brandId);
    
    /*批量删除*/
    int batchDelete(List<String> list);
    
    /*首页商品分类显示前6条*/
    List<Goods> selectTopSix(Long catId);
    
    List<Goods> selectHostTopThree(Long catId);
    
    
    /*移动端首页商品列表*/
    List<Goods> apiGoodsList(Goods entity);
    
    /*移动端分类商品列表*/
    List<Goods> apiCategoryGoodsList(@Param("list") List<String> list, Integer status, Integer pageStart, Integer pageSize);
    
    /*移动端分类商品列表*/
    List<Goods> apiCategoryDiscountList(Integer status, Integer pageStart, Integer pageSize);
    
    List<Goods> apiCateList(Goods entity, @Param("list") List<String> list);
    
    
    
    int countApiCategoryGoodsList(@Param("list") List<String> list, Integer status);
    
    /*移动端最新最热商品*/
    List<Goods> apiHotGoods();
    
    /*移动端分类商品热门推荐*/
    List<Goods> apiCategoryGoodsHot(List<String> list) ;
    
    /*由一级分类查询所有下级商品*/
    List<Goods> selectIdByTopSix(List<String> list);
    
    List<Goods> selectNextAll(List<String> list);
    
    /*首页分类商品不足6条时拼接商品*/
    List<Goods> selectIdByTopSale(List<String> list);
    
    int countByTopSix(Long catId);
    
    /*由二级分类查询所有下级商品*/
    List<Goods> selectSeriesTwo(Long catId);
    
    int countSeriesTwo(Long catId);
    
    List<Goods> getCatesByCatId(Integer pageNum, Integer pageSize, @Param("catId") Long catId);
    
    int countCatesByCatId(@Param("catId") Long catId);
    
    /*商品列表浏览次数排序*/
    List<Goods> getListByVisit();
    
    /*批量查询*/
    List<Goods> batchSelect(List<String> list);
    
    List<Goods> getListByShopId(@Param("shopId")Integer shopId, String name);
    
    int counTgetByShopId(Integer shopId, String name);
    
    List<Goods> getListByShopCatId(Integer shopCatId);
    
    List<Goods> getByShopId(Integer shopId);
    
    List<Goods> getAllByShopId(Goods good);
    
  
    /*zlk 移动端 商家 上架、下架商品*/
    List<Goods> getByShopIdAndStatus(Goods good);
  
    
    int countByShopId(Goods good);
    
    String getShopFixedSale(Integer shopId);
    
    /*活动添加商品处获取商品列表*/
    List<Goods> selectAll(Goods record);
    
    List<Goods> selectByBrandId(Goods record);
    
    List<Goods> selectByTagId(Goods record);
    
    
    
    /***********************************cheng 2017-9-18星期一*******************************/
   List<Goods> selectByPrimaryKeys(List<String> list);
   int selectGoodsDeliByIds(List<Integer> list);
   List<Goods> selectGoodsByShopId(Goods g);
   List<Goods> selectGoodsByShopId1(Goods g);
   List<Goods> selectGoodsByShopId2(Goods g);
   List<Goods> selectGoodsByShopId3(Integer page, Integer size,@Param("shopId") Integer shopId, @Param("cartIds") List<String>shopCatId,@Param("name")String name,@Param("fz")String fz);
   int selectCountGoodsByShopId3(@Param("shopId") Integer shopId, @Param("cartIds") List<String> cartIds,@Param("name")String name);
   List<Goods> selectSimilarity(Goods g);
   List<Goods> selectGoodsByParams(Goods g);
   List<Goods> selectGoodsByCart(@Param("cartIds") List<String> cartIds,@Param("mId")Integer mId);
   
   
   
   List<SysTeam> selectCreateTeamTask();
   List<SysTeam> selectPromoteTeamTask();
   List<SysTeam> selectPromoteTeamTaskByOrderNo(String orderNo);
   List<SysTeam> selectPromoteTeamTaskByGoodsId(Integer goodsId);
   //统计商品总数
   int countNormalGoods();
   //统计平台商品数
   int countShopGoods(Integer shopId);
   
   /*zlk  移动端 zlk 商家后台  商品管理 列表 (按商家Id+条件查询+分页)*/
   List<Goods> mSelectShopGoodsList(Integer shopId, @Param("name")String name, @Param("catId") String catId, @Param("saleType") String saleType, @Param("status") String status, @Param("startPrice") String startPrice, @Param("endPrice") String endPrice,
   		@Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo,@Param("sort")String sort);

   List<Goods> getGoodsBySortnum(Short sortnum, Integer integer);

   List<Goods> getGoodsBySort(short s, Integer integer);

   List<Goods> getGoodsByShopsortnum(Short shopsortnum, Integer id);
   
   List<Goods> getGoodsByShopsortnum1(Short shopsortnum, Integer id, int sortnum);
   
   List<Goods> getGoodsByShopsortnum2(Short shopsortnum, Integer id, int sortnum);
   
   List<Goods> getGoodsBySortnum1(Short shopsortnum, Integer id, int sortnum);
   
   List<Goods> getGoodsBySortnum2(Short shopsortnum, Integer id, int sortnum);


   List<Goods> getGoodsByShopSort(Short shopsortnum, Integer id);
   
   //zlk 
   List<Goods> getGoodsBySale(Goods g);
   
   int updateFixedSale(Goods record);
   
   List<ExportGoods> selectExportGoods(@Param("shopId")Integer shopId, @Param("name")String name, @Param("catId") String catId, @Param("saleType") String saleType, @Param("status") String status, @Param("startPrice") String startPrice, @Param("endPrice") String endPrice,
   		@Param("topicType") String topicType, @Param("id")List<String> id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo,@Param("isJd")String isJd,@Param("isHotShop") String isHotShop, @Param("isNewShop") String isNewShop);

   
   List<Goods> selectByCatIdAndNotDelete(Goods record);

   AuctionConfig getAuctionConfigByGoodsId(Integer id);

   /*平台后台商品列表(优化版) 获取所有数量*/
   int getAll(String name,  @Param("status") String status, @Param("isHot") String isHot, @Param("isNew") String isNew,@Param("isFlag") String isFlag, @Param("saleType") String saleType,@Param("startPrice") String startPrice,
   		@Param("endPrice") String endPrice, @Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo, @Param("shopId")String shopId, @Param("tagName")String tagName,
   		@Param("actZoneId")String actZoneId);

   List<Goods> querySql(String sql);

List<GoodsCategory> selectAllByReid(Long id);


}