package com.bh.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bh.admin.pojo.goods.ActZoneGoods;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsImage;
import com.bh.admin.pojo.goods.GoodsMsg;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.goods.InteractiveRecord;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.vo.ShareResult;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

import net.sf.json.JSONArray;

public interface GoodsService {
	
	int insertGoods(String name, String title, String modelId, String catId,String shopCatId,String brandId,String sellPrice,String marketPrice,String storeNums,
			String desc, String image, JSONArray list, String deliveryPrice, String isShopFlag, String isNew, String refundDays, String publicImage, String saleType,
			String teamNum, String teamEndTime, String isPromote, String timeUnit, String teamPrice, String isCreate, String isJd, String appintroduce, String topicType,
			int shopId, String tagIds,String isPopular,String catIdOne, String catIdTwo,String goodBuyLimit,String sendArea,String userId,String deductibleRate) throws Exception;
	
	int insertGoodsImage(String goodsId, JSONArray url) throws Exception;
	
	List<GoodsImage> goodsImageDetails(String goodsId) throws Exception;
	
	PageBean<GoodsOperLog> selectAlloperation(String currentPage, Integer pageSize,String adminUser,String opType,String goodId,String orderId) throws Exception;
	
	int updateGoods(String id, String name,String title, String modelId,String catId,String shopCatId,String brandId,String sellPrice,String marketPrice,String storeNums,
			String desc, String image, JSONArray list, String deliveryPrice, String isHot, String isFlag, String sortnum, String isNew, String refundDays, String publicImage,
			String saleType, String teamNum, String teamEndTime, String isPromote, String timeUnit, String teamPrice, String isCreate, String appintroduce, String topicType, 
			String tagIds, String catIdOne, String catIdTwo,String goodBuyLimit,String sendArea,String deductibleRate) throws Exception;
	
	/*by scj 2017-08-31*/
	int insertToModel(Goods goods)throws Exception;
	
	/*2017-08-30*/
	int insertTest(String name, Integer sellPrice, Integer marktPrice)throws Exception;
	
	/*商品分页列表2017-08-31*/
	PageBean<Goods> selectByShopid(int shopId, String name, String currentPage, int pageSize, String catId, String saleType,
			String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo,String isJd,String isHotShop,String isNewShop) throws Exception;
	
	/*商品详情2017-08-31*/
	Goods selectBygoodsId(Integer id)throws Exception;
	
	/*改变商品状态2017-9-5*/
	int updateStatus(String id, Integer status) throws Exception;
	
	int goodsSoldOut(String id, Integer status, String outReason,String userId) throws Exception;
	
	int goodsPtSoldOut(String id, Integer status, String outReason,String userId) throws Exception;
	
	int goodsPutaway(String id, Integer status,String userId) throws Exception;
	
	int checkGoodsPutaway(String id, Integer status, String reason,String fixedSale) throws Exception;
	
	/*商品分页列表2017-09-6*/
	PageBean<Goods> backgroundGoodsList(String name, String currentPage, int pageSize, String status, String isHot,String isNew, String isFlag,
			String saleType,String startPrice,String endPrice,String topicType, String id, String skuNo, String jdSkuNo, String shopId,String tagName,String actZoneId) throws Exception;
	/*商品分页列表2018-07-23  优化*/
	Map<String, Object> backgroundGoodsLists(String name, String currentPage, int pageSize, String status, String isHot,String isNew, String isFlag,
			String saleType,String startPrice,String endPrice,String topicType, String id, String skuNo, String jdSkuNo, String shopId,String tagName,String actZoneId) throws Exception;
	
	
	/*客户端同类商品列表2017-09-7*/
	PageBean<Goods> clientCategoryGoodsList(String catId, String currentPage, int pageSize, String fz, String beginPrice, String endPrice, String brandId) throws Exception;
	
	/*客户端商品列表2017-09-7*/
	PageBean<Goods> clientGoodsList(String name, String currentPage, int pageSize, String fz, String beginPrice, String endPrice, String brandId) throws Exception;
	
	/*商品图片上传*/
	int uploadGoodsImage(String oss, String localFilePath, String key, Integer goodsId) throws Exception;
	
	/*批量删除*/
	int batchDelete(String id,String userId) throws Exception;
	
	/*轮播图批量删除*/
	int imageBatchDelete(String id) throws Exception;
	
	/*首页商品分类显示前6条*/
	List<Goods> selectTopSix(String catId) throws Exception;
	
	int selectInsert(GoodsImage goodsImage) throws Exception;
	
	
	/*移动端首页商品列表*/
	PageBean<Goods> apiGoodsList(Goods entity) throws Exception;
	
	/*移动端分类商品列表(手机通讯模块)*/
	PageBean<Goods> apiCategoryGoodsList(String catId, String currentPage, int pageSize) throws Exception;
	
	PageBean<Goods> apiCateGoodsList(Goods entity) throws Exception;
	
	/*移动端分类商品列表(美食模块)*/
	PageBean<Goods> apiCategoryEatList(String catId, String currentPage, int pageSize) throws Exception;
	
	/*移动端商品详情*/
	Goods apiGoodsDetails(Integer id, Member member, String tgId)throws Exception;
	
	/*移动端最新最热商品*/
	List<Goods> apiHotGoods() throws Exception;
	
	int changeValue(String id, String isHot, String isFlag, String sortnum,String tagNames) throws Exception;
	
	/*移动端分类商品热门推荐*/
	List<Goods> apiCategoryGoodsHot(String catId) throws Exception;
	
	/*商家后台商品销售排行列表*/
	PageBean<Goods> arrangeBySale(int shopId, String currentPage,int pageSize, String name) ;
	
	/*scj-pc端商品详情配送至。。。获取用户默认地址*/
	 String getAddressById(int userId) throws Exception;
	 
	 /*获取后台首页店铺信息*/
	 MemberShop getShopDetails(int shopId, int userId) throws Exception;
	 /*获取店铺信息(移动端)*/
	 HashMap<String,Object> mgetShopDetails(int shopId, int userId) throws Exception;
	 
	 /*获取店铺所有商品*/
	 List<Goods> selectAllByShopId(int shopId) throws Exception;
	 
	 /*平台后台设置商品热门、新品、推荐*/
	 int setHotNewAndFlag(String id, String isHotShop, String isShopFlag, String isNewShop, String publicImage) throws Exception;
	 
	 
	 
	 int setHotNewAndFlag(String id, String name,String publicImage) throws Exception;
	 int setPtHotNewAndFlag(String id, String name) throws Exception;
	 
	 /*获取下载地址*/
	 String getDownloadUrl(String fz) throws Exception;
	 
	 
	 /*获取分享商品详情*/
	 ShareResult getShareGoodsDetails(String skuId, String teamNo, String userId, String orderId, String shareType, String shareUrl, Member member) throws Exception;
	 
	 /*活动添加商品处获取商品列表*/
	 List<Goods> selectAll(Goods entity) throws Exception;
		
	/**
	 * 根据cateId数组查询商品
	 * @param cateIds
	 * @return
	 */
	PageBean<Goods> getGoodsByCatId(String cateId, String currentPage,int pageSize);
	
	//cheng修改商品的排序2018-02-02
	int updateGoodsSortnum(List<Goods> goods) throws Exception;
	
	void updateGoodsSortnum1() throws Exception;
	 /*zlk 获取店铺 热门商品*/
	 List<Goods> selectHotByShopId(int shopId) throws Exception;
	 
	 /*zlk 移动端添加 商品*/
	 int mInsert(String name, String title,  String catId,String sellPrice,String marketPrice,String description,
			     String storeNums, String image, String saleType,String teamNum, String teamEndTime, 
			     String isPromote, String isCreate,String shopId,String isPopular) throws Exception;

	 /*zlk 获取店铺所有上架商品 分页*/
	 Map<String,Object> selectByShopId(Goods entity) throws Exception;
	 
     /*zlk  移动端 zlk 商家后台  商品管理 列表 (按商家Id+条件查询+分页)*/
	PageBean<Goods> mSelectByShopid(int shopId, String name, String currentPage, int pageSize, String catId, String saleType,
				String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo,String sort) throws Exception;	 
	 
	 /*zlk 移动端 更改商品 信息*/
	 int mUpdateById(Goods good) throws Exception;
	 
	Integer updateShopLogo(MemberShop memberShop);
	
	Integer updateShopName(MemberShop memberShop);
	
	int updateTopicType(Goods entity)throws Exception;
	
	//修改平台的新品
	int updatePingTaiNew(Goods g) throws Exception; 
	
	//程凤云新品列表
	PageBean<Goods> newGoodsList(Goods g) throws Exception;
	MemberShop selectMemberShopBymId(Integer mId) throws Exception;

	BhResult sendMsgToShop(String id, String msg, HttpServletRequest request);

	int saveGoodsMsg(String msg, String id);


	PageBean<GoodsMsg> goodsMsgList(GoodsMsg goodsMsg);

	boolean isPtSoldOut(Integer shopId, String id);

	int saveChatRecord(InteractiveRecord record);



	int updateMsgstate(String id, int shopId);

	int updateGoodsMsg(Integer shopId);

	int getUnreadNum(Integer shopId);
	 
	//累加已售数量 zlk 2018.3.27
	void updateFixedSale();

	int updateGoodsShopSortnum(List<Goods> goods);
	//修改 zlk 
	int update(Goods good);
	
	int updateActZone(String zoneId, Integer goodsId,Integer skuId) throws Exception;
	
	//List<ActZoneGoods> getActZoneList(String goodsId);
	
	int testInsertDesc(Goods good)throws Exception;
	
	/**
	 * <p>Description: 后台商品新增（new）</p>
	 *  @author scj  
	 *  @date 2018年7月10日
	 */
	int addGoods(Goods entity)throws Exception;
	
	/**
	 * <p>Description: 后台商品编辑（new）</p>
	 *  @author scj  
	 *  @date 2018年7月10日
	 */
	int editGoods(Goods entity)throws Exception;

	int addGoodsM(Goods entity);

	int mEditGoods(Goods entity);
	
	int updateShopAddress(MemberShop memberShop);
	
} 
