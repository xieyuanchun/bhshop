package com.bh.goods.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.ActZone;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsSaleP;
import com.bh.goods.pojo.MyGoods;
import com.bh.goods.pojo.MyGoodsPojo;
import com.bh.goods.pojo.MyHotGoodsShopPojo;
import com.bh.goods.pojo.MyNewGoodsPojo;
import com.bh.goods.pojo.MyNewGoodsShopPojo;
import com.bh.goods.pojo.MyPingtaiGoodsPojo;
import com.bh.order.pojo.SysTeam;
import com.bh.user.pojo.ShopMainPage;
import com.bh.utils.PageBean;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);
    
    int insertTest(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);
    

    int updateByPrimaryKey(Goods record);
    
    
    
    
    int updateByPrimaryKeyBySelf(Goods record);
    
    int insertToMap(Map<String, String> map);
    
    List<Goods> selectShopGoodsList(@Param("shopId")Integer shopId, String name, @Param("catId") String catId, @Param("saleType") String saleType, @Param("status") String status, @Param("startPrice") String startPrice, @Param("endPrice") String endPrice,
    		@Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo);
    
    int countAll(Integer id, String name, @Param("catId") Long catId);
    
    /*平台后台商品列表*/
    List<Goods> backgroundGoodsList(String name,  @Param("status") String status, @Param("isHot") String isHot, @Param("isFlag") String isFlag, @Param("saleType") String saleType,@Param("startPrice") String startPrice,
    		@Param("endPrice") String endPrice, @Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo, @Param("shopId")String shopId);
    
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
    
    /*移动端首页商品列表 2018.5.16 zlk */
    List<Goods> apiGoodsListByLimit(Goods entity);
    
    /*移动端惠拼单 商品列表 2018.5.23 zlk */
    List<MyGoods> apiGoodsCollageList(Goods entity);
    
    /*移动端首页商品列表 2018.5.16 zlk */
    List<MyGoods> apiGoodsListAll(Goods entity);
    
    /*移动端分类商品列表*/
    List<Goods> apiCategoryGoodsList(@Param("list") List<String> list, Integer status, Integer pageStart, Integer pageSize);
   
    
    List<Goods> apiCateList(Goods entity, @Param("list") List<String> list);
    
    int countApiCategoryGoodsList(@Param("list") List<String> list, Integer status);
    
    
    /*移动端奢侈品牌大折扣，大牌好货，节假日活动模块*/
    List<Goods> apiCategoryActivityList1(Integer status,String tagName, Integer pageStart, Integer pageSize);
    
    /*移动超级滨惠豆,周末大放送模块*/
    List<Goods> apiCategoryActivityList2(Integer status,String tagName, Integer pageStart, Integer pageSize);
    
    /*移动端奢侈品牌大折扣，大牌好货，节假日活动模块*/
    int countApiCategoryActivityList1(Integer status,String tagName);
    
    /*移动超级滨惠豆,周末大放送模块*/
    int countApiCategoryActivityList2(Integer status,String tagName);
    
   
    /*分类专区*/
    //List<Goods> apiCategoryList(Integer status,String tagId, Integer pageStart, Integer pageSize);
    
    /*分类专区*/
    List<ActZone> apiNoCategoryList(Integer status,String id);
    List<ActZone> apiCategoryList(Integer status,String id);
    
    /*移动端最新最热商品*/
    List<Goods> apiHotGoods();
    
    /*移动端分类商品热门推荐*/
    List<Goods> apiCategoryGoodsHot(List<String> list) ;
    
    /*由一级分类查询所有下级商品*/
    List<MyPingtaiGoodsPojo> selectIdByTopSix(@Param("catIdOne")String catIdOne);
    
    List<Goods> selectNextAll(List<String> list);
    /*2018.5.30 zlk*/
    List<MyGoods> selectNextAlls(List<String> list);
    
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
   List<MyHotGoodsShopPojo> selectGoodsByShopId(@Param("shopId")Integer shopId);
   List<ShopMainPage> selectGoodsByShopId1(Goods g);
   List<MyNewGoodsShopPojo> selectGoodsByShopId2(@Param("shopId")Integer shopId);
   List<Goods> selectGoodsByShopId3(Integer page, Integer size,@Param("shopId") Integer shopId, @Param("cartIds") List<String>shopCatId,@Param("name")String name,@Param("fz")String fz);
   int selectCountGoodsByShopId3(@Param("shopId") Integer shopId, @Param("cartIds") List<String> cartIds,@Param("name")String name);
   List<Goods> selectSimilarity(Goods g);
   List<Goods> selectGoodsByParams(Goods g);
   List<Goods> selectGoodsByCart(@Param("cartIds") List<String> cartIds,@Param("mId")Integer mId);
   List<Goods> selectGoodsAreaByCart(@Param("list")List<String> list,@Param("mId")Integer mId);
   String selectArea(@Param("id")Integer id);
   
   
   List<SysTeam> selectCreateTeamTask();
   List<SysTeam> selectPromoteTeamTask();
   
   List<SysTeam> selectPromoteTeamTaskByOrderNo(String orderNo);
   List<SysTeam> selectPromoteTeamTaskByGoodsId(Integer goodsId);
   
   //统计商品总数
   int countNormalGoods(Integer goodsStatus);
   //统计平台商品数
   int countShopGoods(Integer goodsStatus, Integer shopId);
   
   /*zlk  移动端 zlk 商家后台  商品管理 列表 (按商家Id+条件查询+分页)*/
   List<Goods> mSelectShopGoodsList(Integer shopId, String name, @Param("catId") String catId, @Param("saleType") String saleType, @Param("status") String status, @Param("startPrice") String startPrice, @Param("endPrice") String endPrice,
   		@Param("topicType") String topicType, @Param("id")String id,  @Param("skuNo")String skuNo,  @Param("jdSkuNo")String jdSkuNo,@Param("sort")String sort);

   List<Goods> getGoodsBySortnum(Short sortnum, Integer integer);

   List<Goods> getGoodsBySort(short s, Integer integer);

   List<Goods> getGoodsByShopsortnum(Short shopsortnum, Integer id);
  
   List<Goods> getGoodsByShopSort(Short shopsortnum, Integer id);
   //zlk 
   List<Goods> getGoodsBySale(Goods g);
   //zlk 4.11
   int updateFixedSale(Goods record);

   List<MyGoodsPojo> apiGoodsListByLimit1(Goods goods);
   List<MyNewGoodsPojo> apiNewGoodsListByLimit(Goods goods);

   List<Goods> getGoodByIdsList(List<Integer> list);
   
   Goods selectGoodsDetail(Integer id);
   
   Integer selectTotleNum(@Param("name")String  name);

List<Goods> getGoodsByCatId(long parseLong);
   
}