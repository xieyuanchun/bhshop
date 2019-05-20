package com.bh.product.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.HollandDauctionMapper;
import com.bh.admin.pojo.goods.ActZone;
import com.bh.admin.pojo.goods.Address;
import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.goods.mapper.ActZoneMapper;
import com.bh.goods.mapper.ActZoneGoodsMapper;
import com.bh.goods.mapper.CashDepositMapper;
import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.mapper.GoodsAttrMapper;
import com.bh.goods.mapper.GoodsBrandMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsCategroyJdMapper;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsDescMapper;
import com.bh.goods.mapper.GoodsFavoriteMapper;
import com.bh.goods.mapper.GoodsImageMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsModelAttrMapper;
import com.bh.goods.mapper.GoodsModelMapper;
import com.bh.goods.mapper.GoodsMsgMapper;
import com.bh.goods.mapper.GoodsSalePMapper;
import com.bh.goods.mapper.GoodsShareLogMapper;
import com.bh.goods.mapper.GoodsShopCategoryMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.GoodsTagMapper;
import com.bh.goods.mapper.HollandDauctionLogMapper;
import com.bh.goods.mapper.InteractiveRecordMapper;
import com.bh.goods.mapper.ItemModelValueMapper;
import com.bh.goods.mapper.MemberUserAccessLogMapper;
import com.bh.goods.mapper.TopicDauctionMapper;
import com.bh.goods.mapper.TopicDauctionPriceMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.mapper.TopicTypeMapper;
import com.bh.goods.pojo.ActZoneGoods;
import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsAttr;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsDesc;
import com.bh.goods.pojo.GoodsFavorite;
import com.bh.goods.pojo.GoodsImage;
import com.bh.goods.pojo.GoodsList;
import com.bh.goods.pojo.GoodsMsg;
import com.bh.goods.pojo.GoodsSaleP;
import com.bh.goods.pojo.GoodsShareLog;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.GoodsTag;
import com.bh.goods.pojo.HollandDauction;
import com.bh.goods.pojo.HollandDauctionLog;
import com.bh.goods.pojo.InteractiveRecord;
import com.bh.goods.pojo.ItemModelValue;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.goods.pojo.MyGoods;
import com.bh.goods.pojo.MyGoodsDetail;
import com.bh.goods.pojo.MyGoodsPojo;
import com.bh.goods.pojo.MyGoodsSku;
import com.bh.goods.pojo.MyNewGoodsPojo;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicDauction;
import com.bh.goods.pojo.TopicDauctionPrice;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicType;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.bean.goods.SellPriceResult;
import com.bh.jd.enums.GoodsEnum;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.order.pojo.TeamLastOne;
import com.bh.product.api.controller.ShareResult;
import com.bh.product.api.service.ApiHollandDauctionLogService;
import com.bh.product.api.service.ApiHollandDauctionService;
import com.bh.product.api.service.GoodsService;
import com.bh.product.api.util.GoodsSaleAllot;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberShopAdminMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.PromoteUserMapper;
import com.bh.user.mapper.ScoreRuleExtMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.PromoteUser;
import com.bh.user.pojo.ScoreRuleExt;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.SysLog;
import com.bh.utils.GetLatitude;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.SmsUtil;
import com.bh.utils.StringUtil;
import com.bh.utils.pay.HttpService;
import com.control.file.upload;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class GoodsImpl implements GoodsService{
	@Autowired
	private GoodsMapper mapper;
	@Autowired
	private GoodsDescMapper descMapper;
	@Autowired
	private GoodsImageMapper imageMapper;
	@Autowired
	private GoodsCategoryMapper categoryMapper;
	@Autowired
	private GoodsAttrMapper attrMapper;
	@Autowired
	private GoodsModelMapper modelMapper;
	@Autowired
	private GoodsSkuMapper skuMapper;
	@Autowired
	private MemberUserAccessLogMapper accessLogMapper;
	@Autowired
	private MemberShopMapper shopMapper;
	@Autowired
	private MemberShopAdminMapper adminMapper;
	@Autowired
	private MemberUserAddressMapper addressMapper;
	@Autowired
	private GoodsShareLogMapper shareLogMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private PromoteUserMapper promoteUserMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private GoodsBrandMapper brandMapper;
	@Autowired
	private GoodsModelAttrMapper goodsModelAttrMapper;
	@Autowired
	private GoodsModelMapper goodsModelMapper;
	@Autowired
	private GoodsTagMapper tagMapper;
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	@Autowired
	private OrderSendInfoMapper  orderSendInfoMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private GoodsShopCategoryMapper goodsShopCategoryMapper;
	@Autowired
	private GoodsCategroyJdMapper catJdMapper;
	@Autowired
	private ItemModelValueMapper modelValueMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private GoodsMsgMapper goodsMsgMapper;
	@Autowired
	private InteractiveRecordMapper interactiveRecordMapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	@Autowired
	private GoodsSalePMapper goodsSalePMapper;
	@Autowired
	private TopicDauctionPriceMapper dauctionPriceMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private TopicTypeMapper topicTypeMapper;
	@Autowired
	private TopicDauctionMapper dauctionMapper;
	@Autowired
	private GoodsFavoriteMapper goodsFavoriteMapper;
	@Autowired
	private HollandDauctionLogMapper dauctionLogMapper;
	@Autowired
	private HollandDauctionMapper hdMapper;
	@Autowired
	private ApiHollandDauctionLogService hdLogService;
	@Autowired
	private ApiHollandDauctionService dauctionService;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private ActZoneMapper actZoneMapper;
    @Autowired
    private ActZoneGoodsMapper actZoneGoodsMapper;
    @Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private ScoreRuleExtMapper scoreRuleExtMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;
    
    
	@Override
	public int insertTest(String name, Integer sellPrice, Integer marktPrice) throws Exception {
		Goods goods = new Goods();
		goods.setName(name);
		goods.setSellPrice(sellPrice);
		goods.setMarketPrice(marktPrice);
		return mapper.insertTest(goods);
	}
	
	/**
	 * 商家后台商品列表
	 */
	@Override
	public PageBean<Goods> selectByShopid(int shopId, String name, String currentPage, int pageSize, String categoryId, String saleType, 
			String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<Goods> list = mapper.selectShopGoodsList(shopId, name, categoryId, saleType, status,startPrice,
				endPrice, topicType, id, skuNo, jdSkuNo);
		if(list.size()>0){
			for(Goods goods : list){
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
				if(skuList.size()>0){
					for(int i=0; i<skuList.size(); i++){
						double realSkuPrice = (double)skuList.get(i).getSellPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setRealPrice(realSkuPrice);
						double marketRealPrice = (double)skuList.get(i).getMarketPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setMarketRealPrice(marketRealPrice);
						
						double realJdPrice = (double)skuList.get(i).getJdPrice()/100; //京东价转化成“元”
						skuList.get(i).setRealJdPrice(realJdPrice);
						
						double realDeliveryPrice = (double)skuList.get(i).getDeliveryPrice()/100; //物流价转化成“元”
						skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);
						
						double realJdBuyPrice = (double)skuList.get(i).getJdBuyPrice()/100; //客户购买价“分”转化成“元”
						skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);
						
						double realJdOldBuyPrice = (double)skuList.get(i).getJdOldBuyPrice()/100; //客户购买价(旧)“分”转化成“元”
						skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);
						
						double realJdProtocolPrice = (double)skuList.get(i).getJdProtocolPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);
						
						double realStockPrice = (double)skuList.get(i).getStockPrice()/100; //进货价
						skuList.get(i).setRealStockPrice(realStockPrice);
						
						if(skuList.get(i).getTeamPrice()!=null){
							double realTPrice = (double)skuList.get(i).getTeamPrice()/100;//团购价格“分”转化成“元”
							skuList.get(i).setRealTeamPrice(realTPrice);
						}
						if(skuList.get(i).getAuctionPrice()!=null){
							double realAuctionPrice = (double)skuList.get(i).getAuctionPrice()/100;//拍卖价格“分”转化成“元”
							skuList.get(i).setRealAuctionPrice(realAuctionPrice);
						}
						JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); //value转义
						skuList.get(i).setValueObj(jsonObject);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyOne())){
						goods.setKeyOne(skuList.get(0).getKeyOne());
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyTwo())){
						goods.setKeyTwo(skuList.get(0).getKeyTwo());
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyThree())){
						goods.setKeyThree(skuList.get(0).getKeyThree());
					}
					goods.setSkuList(skuList);
				}
				double realPrice = (double)goods.getSellPrice()/100; //销售价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				double marketRealPrice = (double)goods.getMarketPrice()/100;//市场价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);
				
				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if(category!=null){
					goods.setCategory(category.getName());
				}
				
				//int sale = goods.getSale()+goods.getFixedSale();//销量
				//goods.setSale(sale);
				
				if(goods.getStatus()==0){
					goods.setGoodsStatus("正常");
				}
				if(goods.getStatus()==1){
					goods.setGoodsStatus("已删除");
				}
				if(goods.getStatus()==2){
					goods.setGoodsStatus("下架");
				}
				if(goods.getStatus()==3){
					goods.setGoodsStatus("申请上架");
				}
				if(goods.getStatus()==4){
					goods.setGoodsStatus("拒绝");
				}
			}
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 商品详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Goods selectBygoodsId(Integer id) throws Exception {
		Goods goods  = mapper.selectByPrimaryKey(id);
		double realPrice = (double)goods.getSellPrice()/100; //销售价格“分”转化成“元”
		goods.setRealPrice(realPrice);
		
		double realMarkPrice = (double)goods.getMarketPrice()/100; //市场价格“分”转化成“元”
		goods.setMarkRealPrice(realMarkPrice);
		
		if(goods.getTeamPrice()!=null){
			double realTeamPrice = (double)goods.getTeamPrice()/100; //团购价格“分”转化成“元”
			goods.setRealTeamPrice(realTeamPrice);
		}
		
		if(goods.getTopicType()==6){
			Map<String, Object> map = new HashMap<>();
			if(goods.getTopicGoodsId()!=null){
				List<TopicDauctionPrice> list = dauctionPriceMapper.selectByTgId(goods.getTopicGoodsId());
				if(list.size()>0){
					TopicDauction dauction = dauctionMapper.selectByPrimaryKey(list.get(0).getDauctionId());
					if(dauction!=null){
						map.put("realLowPrice", (double)dauction.getLowPrice()/100);
						map.put("realScopePrice", (double)dauction.getScopePrice()/100);
						map.put("realDauctionPrice", (double)dauction.getDauctionPrice()/100);
						map.put("timeSection", dauction.getTimeSection());
						TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(dauction.getTgId());
						if(topicGoods!=null){
							Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
							TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
							map.put("topicName", topic.getName());
							if(type.getName()!=null){
								map.put("topicType", type.getName());
							}else{
								map.put("topicType", null);
							}
							map.put("kuNums", topicGoods.getKuNums());
							map.put("actId", topicGoods.getActId());
						}else{
							map.put("topicType", null);
							map.put("topicType", null);
							map.put("kuNums", null);
							map.put("actId", null);
						}
					}else{
						map.put("realLowPrice", null);
						map.put("realScopePrice", null);
						map.put("realDauctionPrice", null);
						map.put("timeSection", null);
					}
				}
			}
			goods.setDauctionDetail(map);
		}
		
		double deliveryRealPrice = (double)goods.getDeliveryPrice()/100; //物流价格“分”转化成“元”
		goods.setDeliveryRealPrice(deliveryRealPrice);
		GoodsDesc goodsDesc = descMapper.selectByGoodsIdAndIsPc(id, 1);
		if(goodsDesc!=null){
			goods.setDescription(goodsDesc.getDescription());
		}
		
		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(id, 0);
		if(desc!=null){ //商品详情描述
			goods.setAppdescription(desc.getDescription());
		}
		
		GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
		if(category!=null){
			goods.setCategory(category.getName()); //分类名称
		}
		
		int sale = goods.getSale()+goods.getFixedSale();//销量
		goods.setSale(sale);
		
		GoodsBrand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
		if(brand!=null){
			goods.setBrandName(brand.getName()); //品牌名称
		}
		
		List<GoodsAttr> attrList = attrMapper.selectAllByGoodsId(id);
		goods.setAttrList(attrList);
		
		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(id);
		for(int i=0; i<skuList.size(); i++){
			double realSkuPrice = (double)skuList.get(i).getSellPrice()/100; //价格“分”转化成“元”
			skuList.get(i).setRealPrice(realSkuPrice);
			
			double realDeliveryPrice = (double)skuList.get(i).getDeliveryPrice()/100; //价格“分”转化成“元”
			skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);
			
			double marketRealPrice = (double)skuList.get(i).getMarketPrice()/100; //价格“分”转化成“元”
			skuList.get(i).setMarketRealPrice(marketRealPrice);
			
			double realJdPrice = (double)skuList.get(i).getJdPrice()/100; //京东价转化成“元”
			skuList.get(i).setRealJdPrice(realJdPrice);
			
			double realJdBuyPrice = (double)skuList.get(i).getJdBuyPrice()/100; //客户购买价“分”转化成“元”
			skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);
			
			double realJdOldBuyPrice = (double)skuList.get(i).getJdOldBuyPrice()/100; //客户购买价(旧)“分”转化成“元”
			skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);
			
			double realJdProtocolPrice = (double)skuList.get(i).getJdProtocolPrice()/100; //价格“分”转化成“元”
			skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);
			
			double realStockPrice = (double)skuList.get(i).getStockPrice()/100; //进货价
			skuList.get(i).setRealStockPrice(realStockPrice);
			
			if(skuList.get(i).getTeamPrice()!=null){
				double realTPrice = (double)skuList.get(i).getTeamPrice()/100;//团购价格“分”转化成“元”
				skuList.get(i).setRealTeamPrice(realTPrice);
			}
			if(skuList.get(i).getAuctionPrice()!=null){
				double realAuctionPrice = (double)skuList.get(i).getAuctionPrice()/100;//拍卖价格“分”转化成“元”
				skuList.get(i).setRealAuctionPrice(realAuctionPrice);
			}
			
			JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); //value转义
			skuList.get(i).setValueObj(jsonObject);
		}
		goods.setSkuList(skuList);
		
		ItemModelValue modelValue = new ItemModelValue(); //查询商品模型
		modelValue.setGoodsId(id);
		List<ItemModelValue> modelValueList = modelValueMapper.selectByGoodsId(modelValue);
		if(modelValueList.size()>0){
			JSONArray array = JSONArray.fromObject(modelValueList.get(0).getParamData()); //value转义
			goods.setModelValue(array);
		}
		
		//zlk 根据shop_cat_id 获取店铺分类名字
		String shopCategoryName=null;
		GoodsShopCategory goodsShopCategory	 = goodsShopCategoryMapper.selectByPrimaryKey(goods.getShopCatId());
		if(goodsShopCategory!=null){
			shopCategoryName=goodsShopCategory.getName();
		}
		goods.setShopCategoryName(shopCategoryName);   //店铺分类名字
		return goods;
	}

	@Override
	public int insertToModel(Goods goods) throws Exception {
		return mapper.insertSelective(goods);
	}
	
	/**
	 * 商品的新增
	 * @param name 商品名称
	 * @param title 商品标题
	 * @param modelId 模型id
	 * @param catId 分类id
	 * @param shopCatId 店铺分类
	 * @param brandId 品牌id
	 * @param sellPrice 销售价格单位分
	 * @param marketPrice 市场价格单位分
	 * @param storeNums 库存
	 * @param unit 单位
	 * @param description 详细内容
	 * @param url 图片地址
	 * @param image 主图
	 * @return
	 * @throws Exception
	 */
	@Override
	//@Transactional(rollbackFor=Exception.class)
	public int selectinsert(String name, String title, String modelId, String catId, String shopCatId, String brandId,
			String sellPrice, String marketPrice, String storeNums, String description, String image, JSONArray list, 
			String deliveryPrice, String isShopFlag, String isNew, String refundDays,String publicImage,
			String saleType, String teamNum, String teamEndTime, String isPromote, String timeUnit,
			String teamPrice, String isCreate, String isJd, String appintroduce, String topicType,
			int shopId, String tagIds,String isPopular,String catIdOne, String catIdTwo) throws Exception {
		
		Goods goods = new Goods();
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setAddtime(now);
		goods.setEdittime(now);
		goods.setShopId(shopId);
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){//商品名称
			goods.setName(name);
		}
		//218-3-15 是否拼人气， -1否 0是
		if (org.apache.commons.lang.StringUtils.isEmpty(isPopular)) {
			goods.setIsPopular(-1);
		}else{
			goods.setIsPopular(Integer.parseInt(isPopular));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(title)){//商品标题
			goods.setTitle(title);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(modelId)){//模型id
			goods.setModelId(Integer.parseInt(modelId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){//分类id
			goods.setCatId(Long.parseLong(catId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(shopCatId)){//所属店铺id
			goods.setShopCatId(Integer.parseInt(shopCatId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(brandId)){//品牌id
			goods.setBrandId(Long.parseLong(brandId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)){//销售价
			Integer realPrice =(int) (MoneyUtil.yuan2Fen(sellPrice));
			goods.setSellPrice(realPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)){//市场价
			Integer mPrice =(int) (MoneyUtil.yuan2Fen(marketPrice));
			goods.setMarketPrice(mPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(storeNums)){//库存
			goods.setStoreNums(Integer.parseInt(storeNums));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			goods.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(refundDays)){
			goods.setRefundDays(Integer.parseInt(refundDays));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(publicImage)){
			goods.setPublicimg(publicImage);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(saleType)){
			goods.setSaleType(Integer.parseInt(saleType));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamNum)){
			goods.setTeamNum(Integer.parseInt(teamNum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamEndTime)){
			goods.setTeamEndTime(Integer.parseInt(teamEndTime));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPromote)){
			goods.setIsPromote(Integer.parseInt(isPromote));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(timeUnit)){
			goods.setTimeUnit(Integer.parseInt(timeUnit));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamPrice)){//团购价
			Integer realTeamPrice =(int) (MoneyUtil.yuan2Fen(teamPrice));
			goods.setTeamPrice(realTeamPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isCreate)){
			goods.setIsCreate(Integer.parseInt(isCreate));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isJd)){
			goods.setIsJd(Integer.parseInt(isJd));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(catIdOne)){
			goods.setCatIdOne(Long.parseLong(catIdOne));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(catIdTwo)){
			goods.setCatIdTwo(Long.parseLong(catIdTwo));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isShopFlag)){
			if(Integer.parseInt(isShopFlag)==1){
				goods.setIsShopFlag(true);
			}else{
				goods.setIsShopFlag(false);
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isNew)){
			if(Integer.parseInt(isNew)==1){
				goods.setIsNew(true);
			}else{
				goods.setIsNew(false);
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(deliveryPrice)){ //物流价
			Integer realDeliveryPrice =(int) (MoneyUtil.yuan2Fen(deliveryPrice));
			goods.setDeliveryPrice(realDeliveryPrice);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(topicType)){
			goods.setTopicType(Integer.parseInt(topicType));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(tagIds)){
			goods.setTagIds(tagIds);
		}
		
		int row = mapper.insertSelective(goods); //插入商品
		
		GoodsDesc goodsDesc = new GoodsDesc(); //插入商品详细描述
		goodsDesc.setIsPc(1);
		goodsDesc.setGoodsId(goods.getId());
		if(!StringUtils.isEmptyOrWhitespaceOnly(description)){
			goodsDesc.setDescription(description);
		}
		int rowD = descMapper.insertGoodsDesc(goodsDesc);
		
		if(rowD==1){
			GoodsDesc desc = new GoodsDesc();
			desc.setIsPc(0);
			desc.setGoodsId(goods.getId());
			if(!StringUtils.isEmptyOrWhitespaceOnly(appintroduce)){
				desc.setDescription(appintroduce);
			}
			descMapper.insertGoodsDesc(desc);
		}
		
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
		    JSONObject ob = (JSONObject) it.next();
		    GoodsAttr attr = new GoodsAttr();
			attr.setGoodsId(goods.getId());
			attr.setAttrId(ob.getInt("attr_id"));
			attr.setAttrValue(ob.getString("attr_value"));
			attr.setModelId(ob.getInt("model_id"));
			attrMapper.insertSelective(attr);
		}
		
		/*Iterator<Object> is = url.iterator();
		while (is.hasNext()) {
			GoodsImage goodsImage = new GoodsImage();
		    JSONObject ob = (JSONObject) is.next();
		    goodsImage.setGoodsId(goods.getId());
		    goodsImage.setUrl(ob.getString("url"));
		    imageMapper.insertSelective(goodsImage);
		}*/
		if(goods.getId()!=null){
			return goods.getId();
		}else{
			return row;
		}
	}
	
	/**
	 * 商品的更新
	 * @param name 商品名称
	 * @param title 商品标题
	 * @param modelId 模型id
	 * @param catId 分类id
	 * @param shopCatId 店铺分类
	 * @param brandId 品牌id
	 * @param sellPrice 销售价格单位分
	 * @param marketPrice 市场价格单位分
	 * @param storeNums 库存
	 * @param description 详细内容
	 * @param url 图片地址
	 * @param image 主图
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int updateGoods(String id, String name, String title,String modelId, String catId, String shopCatId, String brandId,
			String sellPrice, String marketPrice, String storeNums, String description, String image,JSONArray list, 
			String deliveryPrice, String isHot, String isFlag, String sortnum, String isNew, String refundDays,
			String publicImage, String saleType, String teamNum, String teamEndTime, String isPromote,
			String timeUnit, String teamPrice, String isCreate, String appintroduce, String topicType, String tagIds,String catIdOne, String catIdTwo) throws Exception {
		
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			goods.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(title)){
			goods.setTitle(title);
		}
		if((org.apache.commons.lang.StringUtils.isNotEmpty(modelId)) &&(!modelId.equals("null"))){
			goods.setModelId(Integer.parseInt(modelId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){
			goods.setCatId(Long.parseLong(catId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(shopCatId) && !shopCatId.equals("null")){
			goods.setShopCatId(Integer.parseInt(shopCatId));
		}
		if((!StringUtils.isEmptyOrWhitespaceOnly(brandId)) && (!brandId.equals("null"))){
			goods.setBrandId(Long.parseLong(brandId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)){
			Integer realPrice =(int) (MoneyUtil.yuan2Fen(sellPrice));
			goods.setSellPrice(realPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)){
			Integer mPrice =(int) (MoneyUtil.yuan2Fen(marketPrice));
			goods.setMarketPrice(mPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(storeNums)){
			goods.setStoreNums(Integer.parseInt(storeNums));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			goods.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			goods.setSortnum(Short.parseShort(sortnum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(publicImage)){
			goods.setPublicimg(publicImage);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(saleType)){
			goods.setSaleType(Integer.parseInt(saleType));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamNum)){
			goods.setTeamNum(Integer.parseInt(teamNum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamEndTime)){
			goods.setTeamEndTime(Integer.parseInt(teamEndTime));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPromote)){
			goods.setIsPromote(Integer.parseInt(isPromote));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(timeUnit)){
			goods.setTimeUnit(Integer.parseInt(timeUnit));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamPrice)){//团购价
			Integer realTeamPrice =(int) (MoneyUtil.yuan2Fen(teamPrice));
			goods.setTeamPrice(realTeamPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isCreate)){
			goods.setIsCreate(Integer.parseInt(isCreate));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(catIdOne)){
			goods.setCatIdOne(Long.parseLong(catIdOne));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(catIdTwo)){
			goods.setCatIdTwo(Long.parseLong(catIdTwo));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isHot)){
			if(Integer.parseInt(isHot)==1){
				goods.setIsHot(true);
			}else{
				goods.setIsHot(false);
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(refundDays)){
			goods.setRefundDays(Integer.parseInt(refundDays));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isNew)){
			if(Integer.parseInt(isNew)==1){
				goods.setIsNew(true);
			}else{
				goods.setIsNew(false);
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isFlag)){
			if(Integer.parseInt(isFlag)==1){
				goods.setIsFlag(true);
			}else{
				goods.setIsFlag(false);
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(deliveryPrice)){ //物流价
			Integer realDeliveryPrice =(int) (MoneyUtil.yuan2Fen(deliveryPrice));
			goods.setDeliveryPrice(realDeliveryPrice);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(topicType)){
			goods.setTopicType(Integer.parseInt(topicType));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(tagIds)){
			goods.setTagIds(tagIds);
		}
		
		int row = mapper.updateByPrimaryKeySelective(goods); //更新商品
		
		/*if(goods.getCatId()!=Long.parseLong(catId)){
			GoodsCategroyJd catJd = catJdMapper.selectByPrimaryKey(Integer.parseInt(id));
			if(catJd!=null){
				catJd.setMyCatId(Long.parseLong(catId));
				catJdMapper.updateByPrimaryKeySelective(catJd);
			}
		}*/
		
		GoodsDesc goodsDesc = descMapper.selectByGoodsIdAndIsPc(Integer.parseInt(id), 1);
		if(goodsDesc!=null){
			if(!StringUtils.isEmptyOrWhitespaceOnly(description)){
				goodsDesc.setDescription(description);
				descMapper.updateByGoodsIdAndIsPc(goodsDesc);//更新商品详细描述
			}
		}else{
			GoodsDesc de = new GoodsDesc();
			de.setDescription(description);
			de.setGoodsId(Integer.parseInt(id));
			descMapper.insertGoodsDesc(de);//插入商品详细描述
		}
		
		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(Integer.parseInt(id), 0);
		if(desc!=null){
			if(!StringUtils.isEmptyOrWhitespaceOnly(appintroduce)){
				desc.setDescription(appintroduce);
			}
			descMapper.updateByGoodsIdAndIsPc(desc);//更新商品详细描述
		}else{
			GoodsDesc ds = new GoodsDesc();
			ds.setDescription(appintroduce);
			ds.setGoodsId(Integer.parseInt(id));
			descMapper.insertGoodsDesc(ds);//插入商品详细描述
		}
		
		if(list!=null){
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
			    JSONObject ob = (JSONObject) it.next();
			   if(ob.getString("id")!=null && !ob.getString("id").equals("undefined")){
				    GoodsAttr attr = attrMapper.selectByPrimaryKey(ob.getInt("id"));
					attr.setAttrId(ob.getInt("attr_id"));
					attr.setAttrValue(ob.getString("attr_value"));
					attr.setModelId(ob.getInt("model_id"));
					attrMapper.updateByPrimaryKeySelective(attr);
			   }else{
				    GoodsAttr attr = new GoodsAttr();
				    attr.setGoodsId(Integer.parseInt(id));
				    attr.setAttrId(ob.getInt("attr_id"));
					attr.setAttrValue(ob.getString("attr_value"));
					attr.setModelId(ob.getInt("model_id"));
					attrMapper.insertSelective(attr);
			   }
			}
		}
		return row;
	}
	
	/**
	 * 商品改变状态
	 * @param id 商品id
	 * @param status 商品状态
	 * @return
	 */
	@Override
	public int updateStatus(String id, Integer status) throws Exception{
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		goods.setStatus(status);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		return mapper.updateByPrimaryKeySelective(goods);
	}
	
	/**
	 * 商品申请上架
	 * @param id 商品id
	 * @param status 商品状态
	 * @return
	 */
	@Override
	public int goodsPutaway(String id, Integer status) throws Exception{
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		goods.setStatus(status);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setApplyTime(now);
		return mapper.updateByPrimaryKeySelective(goods);
	}
	
	
	/**
	 * 商品的下架
	 * @param id 商品id
	 * @param status 商品状态
	 * @return
	 */
	@Override
	public int goodsSoldOut(String id, Integer status,String outReason) throws Exception{
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		goods.setStatus(status);
		goods.setIsHot(false);
		goods.setIsFlag(false);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		goods.setDownTime(now);
		if(outReason!=null && outReason!=""){
			goods.setOutReason(outReason);
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}
	
	
	/**
	 * 平台后台审核商品上架
	 * @param id 商品id
	 * @param status 商品状态
	 * @return
	 */
	@Override
	public int checkGoodsPutaway(String id, Integer status, String reason,String fixedSale) throws Exception{
		int row = 0;
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(status==5){
			if(goods.getIsJd()==1){//判断京东商品是否在池中
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(Integer.parseInt(id));
				if(skuList.size() > 0){
					for(GoodsSku entity : skuList){
						List<SellPriceResult> list = JDGoodsApi.getSellPrice(entity.getJdSkuNo().toString());
						if(list.size()>0){
							
						}else{
							row = 10000;
							return row;
						}
					}
				}
			}
		}
		goods.setStatus(status);
		if(!StringUtils.isEmptyOrWhitespaceOnly(reason)){
			goods.setReason(reason);
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(fixedSale)) {
			goods.setFixedSale(Integer.parseInt(fixedSale));
		}
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		goods.setUpTime(now);
		row = mapper.updateByPrimaryKeySelective(goods);
		
		return row;
	}
	
	/**
	 * 平台后台商品列表
	 * @param name 查询条件
	 * @param currentPage 当前第几页
	 * @param pageSize 每页显示几条
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean<Goods> backgroundGoodsList(String name, String currentPage, int pageSize, String status, String isHot, String isFlag,
			String saleType,String startPrice,String endPrice,String topicType, String id, String skuNo, String jdSkuNo, String shopId) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<Goods> list = mapper.backgroundGoodsList(name, status, isHot, isFlag, saleType,startPrice,endPrice, topicType, id, skuNo, jdSkuNo, shopId);
		if(list.size()>0){
			for(Goods goods : list){
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
				if(skuList.size()>0){
					for(int i=0; i<skuList.size(); i++){
						double realSkuPrice = (double)skuList.get(i).getSellPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setRealPrice(realSkuPrice);
						double marketRealPrice = (double)skuList.get(i).getMarketPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setMarketRealPrice(marketRealPrice);
						
						double realJdPrice = (double)skuList.get(i).getJdPrice()/100; //京东价转化成“元”
						skuList.get(i).setRealJdPrice(realJdPrice);
						
						double realDeliveryPrice = (double)skuList.get(i).getDeliveryPrice()/100; //京东价转化成“元”
						skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);
						
						double realJdBuyPrice = (double)skuList.get(i).getJdBuyPrice()/100; //客户购买价“分”转化成“元”
						skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);
						
						double realJdOldBuyPrice = (double)skuList.get(i).getJdOldBuyPrice()/100; //客户购买价(旧)“分”转化成“元”
						skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);
						
						double realJdProtocolPrice = (double)skuList.get(i).getJdProtocolPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);
						
						double realStockPrice = (double)skuList.get(i).getStockPrice()/100; //进货价
						skuList.get(i).setRealStockPrice(realStockPrice);
						
						if(skuList.get(i).getTeamPrice()!=null){
							double realTPrice = (double)skuList.get(i).getTeamPrice()/100;//团购价格“分”转化成“元”
							skuList.get(i).setRealTeamPrice(realTPrice);
						}
						if(skuList.get(i).getAuctionPrice()!=null){
							double realAuctionPrice = (double)skuList.get(i).getAuctionPrice()/100;//拍卖价格“分”转化成“元”
							skuList.get(i).setRealAuctionPrice(realAuctionPrice);
						}
						JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); //value转义
						skuList.get(i).setValueObj(jsonObject);
					}
					goods.setSkuList(skuList);
				}
				if(goods.getSellPrice()!=null){
					double realPrice = (double)goods.getSellPrice()/100; //销售价格“分”转化成“元”
					goods.setRealPrice(realPrice);
				}
				if(goods.getMarketPrice()!=null){
					double marketRealPrice = (double)goods.getMarketPrice()/100;//市场价格“分”转化成“元”
					goods.setMarkRealPrice(marketRealPrice);
				}
				
				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if(category!=null){
					goods.setCategory(category.getName());
				}
				MemberShop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
				if(shop!=null){
					goods.setShopName(shop.getShopName());
				}
				
				//int sale = goods.getSale()+goods.getFixedSale();//销量
				//goods.setSale(sale);
				
				if(goods.getStatus()==0){
					goods.setGoodsStatus("正常");
				}
				if(goods.getStatus()==1){
					goods.setGoodsStatus("已删除");
				}
				if(goods.getStatus()==2){
					goods.setGoodsStatus("下架");
				}
				if(goods.getStatus()==3){
					goods.setGoodsStatus("申请上架");
				}
				if(goods.getStatus()==4){
					goods.setGoodsStatus("拒绝");
				}
			}	
		}
		PageBean<Goods> page = new PageBean<>(list);
		return page;
	}
	
	/**
	 * 客户端分类查询商品列表
	 * @param catId 分类id
	 * @param currentPage 当前第几页
	 * @param pageSize 每页显示几条
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean<Goods> clientCategoryGoodsList(String catId, String currentPage, int pageSize, String fz, String beginPrice, String endPrice, String brandId) throws Exception {
		int beginPrices = 0; //设置默认查询起始价格
		int endPrices = 10000000;
		Long brandIds = null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(beginPrice)){
			//Integer realPrice =(int) (Double.parseDouble(sellPrice) * 100);
			beginPrices = Integer.parseInt(beginPrice) * 100;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endPrice)){
			endPrices = Integer.parseInt(endPrice) * 100;
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(brandId)){
			brandIds = Long.parseLong(brandId);
		}
		
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = pageSize;//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		
		List<Goods> list = null;
		int fzs = 0;
		if(!StringUtils.isEmptyOrWhitespaceOnly(fz)){ //fz:1-按销量排序2-按价格3-时间
			fzs = Integer.parseInt(fz);
		}
		
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> categoryList = getAllCateList(Integer.parseInt(catId));
		for(GoodsCategory category : categoryList){
			buffer.append(category.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		list = mapper.clientCategoryGoodsList(catIdList, 5, pageStart, pageSizes, beginPrices,  endPrices, brandIds, fzs);
		setGetter(list);
		int total = mapper.clientCountByCategory(catIdList, 5, beginPrices,  endPrices, brandIds);//总条数
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<Goods> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);
		return page;
	}
	
	/**
	 * 根据分类id递归查询所有子分类
	 */
	public List<GoodsCategory> getAllCateList(long reid){
		GoodsCategory myGc = categoryMapper.selectByPrimaryKey(reid);
		List<GoodsCategory> catesList = getCatesByReid(reid);
		List<GoodsCategory> retList = new ArrayList<>();
		getCateTreeList(catesList,retList);
		retList.add(0, myGc);
		return retList;
	}

	private List<GoodsCategory> getCateTreeList(List<GoodsCategory> cateList,List<GoodsCategory> retList){
		List<GoodsCategory> subMenuList = new ArrayList<GoodsCategory>();
		for(GoodsCategory entity : cateList){
		  getCateTreeList(getCatesByReid(entity.getId()),retList);
		  retList.add(entity);  
		  subMenuList.add(entity);
		}
		return subMenuList;
	}
	private List<GoodsCategory> getCatesByReid(long reid){
		return categoryMapper.selectAllByReid(reid);
	}
	
	/**
	 * 客户端商品列表（关键字查询+分页+销量、价格、时间排序）
	 * @param name 查询条件
	 * @param currentPage 当前第几页
	 * @param pageSize 每页显示几条
	 * @param fz 1-按销量排序2-按价格3-时间
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean<Goods> clientGoodsList(String name, String currentPage, int pageSize, String fz, String beginPrice, String endPrice, String brandId) throws Exception {
		int beginPrices = 0; //设置默认查询起始价格
		int endPrices = 1000000;
		Long brandIds = null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(beginPrice)){
			beginPrices = Integer.parseInt(beginPrice) * 100;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endPrice)){
			endPrices = Integer.parseInt(endPrice) * 100;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(brandId)){
			brandIds = Long.parseLong(brandId);
		}
		List<Goods> list = null;
		int fzs = 0;
		if(!StringUtils.isEmptyOrWhitespaceOnly(fz)){ //fz:1-按销量排序2-按价格3-时间
			fzs = Integer.parseInt(fz);
		}
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		list = mapper.clientGoodsList(name, 5, beginPrices,  endPrices, brandIds, fzs);
		setGetter(list);
		PageBean<Goods> page = new PageBean<>(list);
		return page;
	}
	
	@Override
	public int uploadGoodsImage(String oss, String localFilePath, String key, Integer goodsId) throws Exception{
		upload myupload= new upload();
		boolean bl=myupload.singleupload(oss,localFilePath,key);
		StringBuffer url = new StringBuffer(Contants.bucketHttp);
		url.append(key);
		GoodsImage goodsImage = imageMapper.selectByGoodsId(goodsId);
		goodsImage.setUrl(url.toString());
		int row = imageMapper.updateByPrimaryKeySelective(goodsImage);
		return row;
	}
	
	/**
	 * 批量删除
	 */
	@Transactional
	@Override
	public int batchDelete(String id) throws Exception{
		int row = 0;
		List<String> result = Arrays.asList(id.split(",")); //string转list
		for(int i=0; i<result.size(); i++){ //判断所选商品是否已上架或者待审核
			Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(result.get(i)));
			if(goods.getStatus()==3 || goods.getStatus()==5){
				row = 999;
				return row ; 
			}
		}
		
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		row = mapper.batchDelete(list);
		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(Integer.parseInt(id));
		if(skuList.size()>0){
			for(GoodsSku sku : skuList){
				long jdSkuNo = 0;
				sku.setJdSkuNo(jdSkuNo);
				sku.setJdSupport(0);
				row = skuMapper.updateByPrimaryKeySelective(sku);
			}
		}
		return row;
	}
	
	/**
	 * 首页商品分类显示前6条
	 */
	@Override
	public List<Goods> selectTopSix(String catId) throws Exception{
		List<Goods> list = mapper.selectTopSix(Long.parseLong(catId));
		for(Goods goods : list){
			double realPrice = (double)goods.getSellPrice()/100; //价格“分”转化成“元”
			goods.setRealPrice(realPrice);
			GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
			if(category!=null){
				goods.setCategoryImage(category.getImage());
				goods.setCategory(category.getName());
			}
		}
		return list;
	}

	@Override
	public int selectInsert(GoodsImage goodsImage) throws Exception {
		return imageMapper.insertSelective(goodsImage);
	}
	
	/**
	 * 新增轮播图保存
	 */
	@Override
	public int insertGoodsImage(String goodsId, JSONArray url) throws Exception {
		int row = 0;
		List<GoodsImage> imageList = imageMapper.selectListByGoodsId(Integer.parseInt(goodsId));
		if(imageList.size()==0){
			
		}else{
			for(GoodsImage goodsImage : imageList){
				imageMapper.deleteByPrimaryKey(goodsImage.getId());
			}
		}
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(goodsId));
		Iterator<Object> is = url.iterator();
		while (is.hasNext()) {
			GoodsImage goodsImage = new GoodsImage();
		    JSONObject ob = (JSONObject) is.next();
		    goodsImage.setGoodsId(goods.getId());
		    goodsImage.setUrl(ob.getString("url"));
		    row = imageMapper.insertSelective(goodsImage);
		}
		return row;
	}
	
	/**
	 * 轮播图批量删除
	 */
	@Transactional
	@Override
	public int imageBatchDelete(String id) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		return imageMapper.batchDelete(list);
	}
	
	/**
	 * 移动端首页商品列表
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public PageBean<Goods> apiGoodsList(Goods entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<Goods> list = mapper.apiGoodsList(entity);
		if(list.size()>0){
			setGetter(list);
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 移动端首页商品列表 2018.5.16 zlk
	 */
	@Override
	public Map<String,Object> apiGoodsLists(Goods entity) throws Exception {
		// TODO Auto-generated method stub
		entity.setPageSize(20);//每页的数量
		entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage())-1)*entity.getPageSize());
		List<MyGoodsPojo> list = mapper.apiGoodsListByLimit1(entity);
		String name=entity.getName();
		Integer totle=mapper.selectTotleNum(name);
		Integer totlePage=totle % 100==0? totle/100:totle/100+1;
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
		returnMap.put("list", list);//当前数据
		returnMap.put("totle", totle);
		returnMap.put("totlePage", totlePage);
		return returnMap;
	}
	
	
	/**
	 * 移动端 惠拼单  商品列表 2018.5.23 zlk
	 */
	@Override
	public Map<String,Object> apiGoodsCollageList(Goods entity) throws Exception {
		// TODO Auto-generated method stub
		entity.setPageSize(10);//每页的数量
		entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage())-1)*entity.getPageSize());
		List<MyGoods> list = mapper.apiGoodsCollageList(entity);//当前页数
//		List<MyGoods> listAll = mapper.apiGoodsListAll(entity);
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
        if(list.size()>0){
		 for(int i=0; i<list.size(); i++){
			int groupCount = 0; //团购商品销量统计
			String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			if(countT!=null){
				groupCount = Integer.parseInt(countT)+list.get(i).getFixedSale();
			}else{
				groupCount = list.get(i).getFixedSale();
			}
			list.get(i).setGroupCount(groupCount);
			
			
			list.get(i).setImage(RegExpValidatorUtils.returnNewString(list.get(i).getValue()));
				   
				
			
			StringBuffer buffer = new StringBuffer(); //设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if(orderTeamList.size()>0){
				for(OrderTeam entitys : orderTeamList){
					if(entitys.getType()==0){//普通用户
						Member member = memberMapper.selectByPrimaryKey(entitys.getmId());
						if(member!=null){
							if(member.getHeadimgurl()!=null){
								buffer.append(member.getHeadimgurl()+",");
							}else{
								buffer.append(Contants.headImage+",");
							}
						}else{
							buffer.append(Contants.headImage+",");
						}
					}else{//虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if(promoteUser!=null){
							buffer.append(promoteUser.getHeadImg()+",");
						}else{
							buffer.append(Contants.headImage+",");
						}
					}
				}
			}
			if(buffer.toString().length()>0){
				String headStr = buffer.toString().substring(0, buffer.toString().length()-1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}
			
		 }
        }
//		int pages = listAll.size()/entity.getPageSize();
//		if(listAll.size()%entity.getPageSize()!=0) {
//			pages=pages+1;
//		}
//		returnMap.put("total", listAll.size());//当前数据
//		returnMap.put("pages", pages);//当前页条数
		returnMap.put("list", list);//当前数据
		returnMap.put("pageNum", entity.getCurrentPage());//当前页数
		returnMap.put("size", list.size());//当前页条数
		return returnMap;
	}
	
	
	
	/**
	 * 移动端商品详情
	 */
	@Override
	public Goods apiGoodsDetails(Integer id, Member member, String tgId) throws Exception { 
		Goods goods  = mapper.selectByPrimaryKey(id);
		double goodsPrice = (double)goods.getSellPrice()/100; //销售价格“分”转化成“元”
		goods.setRealPrice(goodsPrice);
		
		double marketRealPrice = (double)goods.getMarketPrice()/100;//市场价格“分”转化成“元”
		goods.setMarkRealPrice(marketRealPrice);
		
		if(goods.getTeamPrice()!=null){
			double realTeamPrice = (double)goods.getTeamPrice()/100;//团购价格“分”转化成“元”
			goods.setRealTeamPrice(realTeamPrice);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(tgId)){//活动商品明细id
			goods.setTgId(Integer.parseInt(tgId));
		}
		
		if(goods.getTopicType()==6){
			HollandDauction dauction = new HollandDauction();
			dauction.setGoodsId(goods.getId());
			HollandDauction hd = hdMapper.getByGoodsId(dauction);
			
			HollandDauctionLog log = new HollandDauctionLog();
			//检测是否有超过30秒未改状态的数据
			log.setGoodsId(hd.getGoodsId());
			List<HollandDauctionLog> logList = dauctionLogMapper.getListByGoodsIdAndStatus(log);
			if(logList.size()>0){
				HollandDauctionLog oldLog = logList.get(0);  
				Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
				Date date = new Date();
				Long curTimeStamp = date.getTime();
				if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){
					if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
						oldLog.setPayStatus(1); //待支付
						oldLog.setEndTime(date);
						//生成待支付订单
						Order order = hdLogService.rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), goods.getId());
						if(order!=null){
							oldLog.setOrderNo(order.getOrderNo());

						}
						dauctionLogMapper.updateByPrimaryKeySelective(oldLog);
						//未中标用户退金额
						dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
						
						if(hd.getStoreNums()>0){
							if(hd.getStoreNums()==1){
								
							}else{
								hd.setCurrentPeriods(hd.getCurrentPeriods()+1);
								Date loseTime = accountLoseTime(hd, date);//新一期流拍时间
								hd.setLoseTime(loseTime);
								hd.setStartTime(date);
							}
							hd.setStoreNums(hd.getStoreNums()-1);
							hdMapper.updateByPrimaryKeySelective(hd);
						}
					}
				}
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(goods.getTagIds())){
			StringBuffer buffer = new StringBuffer();
			String[] tagIds = goods.getTagIds().split(",");
			for(int i=0; i<tagIds.length; i++){
				GoodsTag tag = tagMapper.selectByPrimaryKey(Integer.parseInt(tagIds[i]));
				if(tag!=null){
					buffer.append(tag.getName()+",");
				}
			}
			if(buffer.toString().length()>0){
				String[] tagIdsValue = buffer.toString().substring(0, buffer.toString().length()-1).split(",");
				goods.setTagIdsValue(tagIdsValue);
			}
		}
		/*int sale = 0;//月销量
		String count = orderSkuMapper.getMonthSaleCount(id);
		if(count!=null){
			sale = Integer.parseInt(count);
		}
		goods.setSale(sale);*/
		
		int groupCount = 0; //团购商品销量统计
		if (goods.getSaleType()==1) {
			groupCount=goods.getSale()+goods.getFixedSale();
		}else{
			String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
			if(countT!=null){
				groupCount = Integer.parseInt(countT)+goods.getFixedSale();
			}else{
				groupCount = goods.getFixedSale();
			}
		}
		goods.setGroupCount(groupCount);
		
		Map<String, Object> shopMap = getShopInfo(goods.getShopId()); //店铺基本信息
		if(shopMap.size()>0){
			goods.setShopInfo(shopMap);
		}
		
		if(member!=null){
			List<MemberUserAccessLog> logList = accessLogMapper.getBymIdAndGoodsId(member.getId(), id);
			if(logList.size()==0){
				MemberUserAccessLog accessLog = new MemberUserAccessLog(); //新增浏览记录
				accessLog.setAddtime(new Date());
				accessLog.setGoodsId(id);
				accessLog.setmId(member.getId());
				accessLogMapper.insertSelective(accessLog);
			}else{
				//程凤云2018-4-8如果已经存在，则将时间改变
				MemberUserAccessLog log = new MemberUserAccessLog();
				log.setId(logList.get(0).getId());
				log.setAddtime(new Date());
				accessLogMapper.updateByPrimaryKeySelective(log);
			}
		}
		
		goods.setVisit(goods.getVisit()+1);//浏览次数+1
		mapper.updateByPrimaryKeySelective(goods);
		
		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(id, 0);
		if(desc!=null){ //商品详情描述
			goods.setAppdescription(desc.getDescription());
		}
		
		List<GoodsImage> imageList = imageMapper.selectListByGoodsId(id);
		if(imageList!=null){ //商品轮播图
			goods.setImageUrl(imageList);
		}
		List<GoodsAttr> attrList = attrMapper.getListByGoodsId(id); //商品模型属性值(此查询只获取模型id和模型名字)
		if(attrList.size()>0){
			for(GoodsAttr goodsAttr : attrList){
				//获取某个模型某个商品的所有属性
				List<GoodsAttr>  listGoodsAttr= attrMapper.getModelByGoodsIdAndModelId(id,goodsAttr.getModelId());
				goodsAttr.setListGoodsAttr(listGoodsAttr);
			}
			goods.setAttrList(attrList);
		}
		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(id); //商品规格属性
		if(skuList!=null){
			for(GoodsSku goodsSku : skuList){
				double skuPrice = (double)goodsSku.getSellPrice()/100; //销售价格“分”转化成“元”
				goodsSku.setRealPrice(skuPrice);
				
				double marketRPrice = (double)goodsSku.getMarketPrice()/100;//市场价格“分”转化成“元”
				goodsSku.setMarketRealPrice(marketRPrice);
				
				if(goodsSku.getTeamPrice()!=null){
					double realTPrice = (double)goodsSku.getTeamPrice()/100;//市场价格“分”转化成“元”
					goodsSku.setRealTeamPrice(realTPrice);
					goods.setSkuList(skuList);
				}
				
				JSONObject jsonObject = JSONObject.fromObject(goodsSku.getValue()); //value转义
				goodsSku.setValueObj(jsonObject);
			}
		}
		double deliveryRealPrice = (double)goods.getDeliveryPrice()/100; //销售价格“分”转化成“元”
		goods.setDeliveryRealPrice(deliveryRealPrice);
		
		ItemModelValue modelValue = new ItemModelValue(); //查询商品模型
		modelValue.setGoodsId(id);
		List<ItemModelValue> modelValueList = modelValueMapper.selectByGoodsId(modelValue);
		if(modelValueList.size()>0){
			JSONArray array = JSONArray.fromObject(modelValueList.get(0).getParamData()); //value转义
			goods.setModelValue(array);
		}
		String goods1=goods.getTagName();
		if(goods1!=null&&!"".equals(goods1) ){
			String[] goods2=goods1.split(",");
			for(int i=0;i<goods2.length;i++) {
				if(goods2[i].equals("3")) {
					goods.setTagName("3");
				}
			}
		}
		return goods;
	}
	
	private Date accountLoseTime(HollandDauction hd, Date endTime){
		//计算新一期流拍时间
		int middle = hd.getDauctionPrice() - hd.getLowPrice();
		int remainder = middle % hd.getScopePrice(); //余数
		int result = middle / hd.getScopePrice();  //去余数结果
		int num = remainder == 0 ? result+1:result+2;
		Long thisTimeStamp = (long) (num * hd.getTimeSection()*60000);
		Long reusltTimeStamp = endTime.getTime() + thisTimeStamp;
		Date date = new Date(reusltTimeStamp);			
		return date;
	}
	
	public Map<String, Object> getShopInfo(int shopId){
		Map<String, Object> map = new HashMap<>();
		MemberShop shop = shopMapper.selectByPrimaryKey(shopId);
		if(shop!=null){
			map.put("shopName", shop.getShopName()); //店铺名称
			map.put("shopLogo", shop.getLogo()); //店铺logo
			Goods good = new Goods();
			good.setShopId(shopId);
			good.setStatus(5);
			int goodsNum = mapper.countByShopId(good);
			map.put("goodsNum", goodsNum); //店铺商品数量
			
			int groupCount = 0; //店铺团购商品销量统计
			OrderSku entity = new OrderSku();
			entity.setShopId(shopId);
			String countT = orderSkuMapper.getShopGroupSale(entity);
			String shopFixedSale = mapper.getShopFixedSale(shopId);//店铺所有商品后台设置销量统计
			if(countT!=null){
				groupCount = Integer.parseInt(countT);
				if(shopFixedSale!=null){
					groupCount = Integer.parseInt(countT)+Integer.parseInt(shopFixedSale);
				}
			}else{
				if(shopFixedSale!=null){
					groupCount = Integer.parseInt(shopFixedSale);
				}
			}
			map.put("teamSale", groupCount);
			
		}
		return map;
	}
	
	/**
	 * 移动端分类商品列表(手机通讯模块)
	 */
	@Override
	public PageBean<Goods> apiCategoryGoodsList(String catId, String currentPage, int pageSize) throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = pageSize;//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(Long.parseLong(catId));
		for(GoodsCategory category : cateList){
			buffer.append(category.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		
		List<Goods> list = mapper.apiCategoryGoodsList(catIdList, 5, pageStart, pageSizes);
		skuSetter(list); //sku价格转化及复制
		
		int total = mapper.countApiCategoryGoodsList(catIdList, 5);//总条数
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<Goods> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);
		return page;
	}
	
	
	/**
	 * 移动端分类商品列表(活动专区模块)
	 */
	public Map<String, Object> apiCategoryActivityList(String currentPage, int pageSize,String tagName) throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = pageSize;//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		List<Goods> list=null;
		//int total=0;
		//1 奢侈品大折扣 2大牌好货 4节假日活动
		if(tagName.equals("1")||tagName.equals("2")||tagName.equals("4")) {
			  list = mapper.apiCategoryActivityList1(5,tagName, pageStart, pageSizes);
		}
		//3 周末滨惠日 5 周末大放送
		if(tagName.equals("3")||tagName.equals("5")) {
			  list = mapper.apiCategoryActivityList2(5,tagName, pageStart, pageSizes);
		}
	  
		/*//1 奢侈品大折扣 2大牌好货 4节假日活动
		if(tagName.equals("1")||tagName.equals("2")||tagName.equals("4")) {
			total = mapper.countApiCategoryActivityList1(5,tagName);//总条数
		}
		//3 周末滨惠日 5 周末大放送
		if(tagName.equals("3")||tagName.equals("5")) {
			total = mapper.countApiCategoryActivityList2(5,tagName);//总条数
		}
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<Goods> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);*/
		/*URL url = new URL("http://www.ntsc.ac.cn");
        URLConnection conn = url.openConnection();
        conn.connect();
        long dateL = conn.getDate();*/
        Date date = new Date();
        int week =date.getDay();
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
		returnMap.put("week", week);
		returnMap.put("list", list);//当前数据
		return returnMap;
	}
	
	
	/**
	 * 移动端分类商品列表(活动专区模块)
	 */
	/*public Map<String, Object> apiCategoryList(String currentPage, int pageSize,String tagId) throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = pageSize;//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		List<Goods> list=null;
		
	    list = mapper.apiCategoryList(5,tagId, pageStart, pageSizes);
        Date date = new Date();
        int week =date.getDay();
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
		returnMap.put("week", week);
		returnMap.put("list", list);//当前数据
		return returnMap;
	}*/
	
	
	/**
	 * 移动端分类商品列表(活动专区模块)
	 */
	public Map<String, Object> apiCategoryList(String uuid) throws Exception {
		com.bh.goods.pojo.ActZone actZone=actZoneMapper.selectByUuid(uuid);
		String id=actZone.getId().toString();
		List<com.bh.goods.pojo.ActZone> list1=actZoneMapper.apiCategoryName(id);
		List<com.bh.goods.pojo.ActZone> actZoneGoodsList1=null;
		if(list1.size()>0) {
			    actZoneGoodsList1=mapper.apiCategoryList(5,id);
				List<com.bh.goods.pojo.ActZone> actZoneList = actZoneMapper.selectListByReid(list1.get(0).getId());
				list1.get(0).setChildList(actZoneList);
				 //list = mapper.apiCategory1(5,tagId, pageStart, pageSizes);
				if(actZoneList.size()>0){
				   for(com.bh.goods.pojo.ActZone actZone1 : actZoneList){
					  List<com.bh.goods.pojo.ActZone> actZoneGoodsList = mapper.apiCategoryList(5,actZone1.getId().toString());
					  actZone1.setChildList(actZoneGoodsList);
				   }
			   }
			
		}
        Date date = new Date();
        int week =date.getDay();
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
		returnMap.put("week", week);
		returnMap.put("actZoneGoodsList", actZoneGoodsList1);
		//returnMap.put("actZone", actZone);
		returnMap.put("list", list1);//当前数据
		return returnMap;
	}
	
	
	
	
	
	/**
	 * 移动端分类名称(活动专区模块)
	 */
	/*public Map<String, Object> apiCategoryName(String tagId) throws Exception {
		List<com.bh.goods.pojo.ActZone> list=actZoneMapper.apiCategoryName(tagId);
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
		returnMap.put("list", list);
		return returnMap;
	}*/
	
	
	
	/**
	 * 移动端最新最热商品
	 */
	@Override
	public List<Goods> apiHotGoods() throws Exception {
		List<Goods> list = mapper.apiHotGoods();
		setGetter(list);
		return list;
	}

	/**
	 * 移动端分类商品热门推荐
	 */
	@Override
	public List<Goods> apiCategoryGoodsHot(String catId) throws Exception {
		
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(Integer.parseInt(catId));
		for(GoodsCategory category : cateList){
			buffer.append(category.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		
		List<Goods> list = mapper.apiCategoryGoodsHot(catIdList);
		setGetter(list); //sku价格转化及复制
		return list;
	}
	
	/**
	 * 商品轮播图详情
	 */
	@Override
	public List<GoodsImage> goodsImageDetails(String goodsId) throws Exception {
		List<GoodsImage> list = imageMapper.selectListByGoodsId(Integer.parseInt(goodsId));
		return list;
	}

	@Override
	public PageBean<Goods> getGoodsByCatId(String catId,String currentPage,int pageSize) {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = pageSize;//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		List<Goods> cateList = mapper.getCatesByCatId(pageStart,pageSize, Long.parseLong(catId));
		int total = mapper.countCatesByCatId(Long.parseLong(catId));//总条数
		
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = cateList.size() == pageSizes ?  pageSizes : cateList.size();
		PageBean<Goods> pageBean = new PageBean<>(cateList);
		pageBean.setPageNum(currentPages);
		pageBean.setList(cateList);
		pageBean.setTotal(total);
		pageBean.setPages(pages);
		pageBean.setPageSize(pageSizes);
		pageBean.setSize(size);
		return pageBean;
	}
	
	/**
	 * 价格的转化及sku与主表的赋值
	 * @param list
	 * @return
	 */
	public List<Goods> setGetter(List<Goods> list){
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;
		
		for(int i=0; i<list.size(); i++){
			//int avgStar=goodsCommentMapper.countStar(list.get(i).getShopId(),list.get(i).getId());
			int groupCount = 0; //团购商品销量统计
			Goods goods  = mapper.selectByPrimaryKey(list.get(i).getId());
			if (goods.getSaleType()==1) {
				groupCount=goods.getSale()+goods.getFixedSale();
			}else{
				String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
				if(countT!=null){
					groupCount = Integer.parseInt(countT)+goods.getFixedSale();
				}else{
					groupCount = goods.getFixedSale();
				}
			}
			
			list.get(i).setGroupCount(groupCount);
			
			if(list.get(i).getTopicType()==6){ //判断是否拍卖商品
				Map<String, Object> map = new HashMap<>();
				if(list.get(i).getTopicGoodsId()!=null){
					System.out.println("8888888888---->"+list.get(i).getTopicGoodsId());
					List<TopicDauctionPrice> dauctionList = dauctionPriceMapper.selectByTgId(list.get(i).getTopicGoodsId());
					if(dauctionList.size()>0){
						TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(dauctionList.get(0).getTgId());
						if(topicGoods!=null){
							Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
							map.put("startTime", topic.getStartTime());
							map.put("endTime", topic.getEndTime());
							//2018.5.11 zlk
//							map.put("startTime", topicGoods.getStartTime());
//							map.put("endTime", topicGoods.getEndTime());
							//end
							List<TopicDauction> dauction = dauctionMapper.selectByTgId(topicGoods.getId());
							if(dauction.size()>0){
								map.put("dauctionPrice", (double)dauction.get(0).getDauctionPrice()/100);
								map.put("lowPrice", (double)dauction.get(0).getLowPrice()/100);
								map.put("scopePrice", (double)dauction.get(0).getScopePrice()/100);
								map.put("timeSection", dauction.get(0).getTimeSection());
							}else{
								map.put("dauctionPrice", null);
								map.put("lowPrice", null);
								map.put("scopePrice", null);
								map.put("timeSection", null);
							}
						}else{
							map.put("startTime", null);
							map.put("endTime", null);
						}
						list.get(i).setKuNums(topicGoods.getKuNums());
					}
				}
				list.get(i).setDauctionDetail(map);
			}
			
			StringBuffer buffer = new StringBuffer(); //设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if(orderTeamList.size()>0){
				for(OrderTeam entity : orderTeamList){
					if(entity.getType()==0){//普通用户
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if(member!=null){
							if(member.getHeadimgurl()!=null){
								buffer.append(member.getHeadimgurl()+",");
							}else{
								buffer.append(Contants.headImage+",");
							}
						}else{
							buffer.append(Contants.headImage+",");
						}
					}else{//虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if(promoteUser!=null){
							buffer.append(promoteUser.getHeadImg()+",");
						}else{
							buffer.append(Contants.headImage+",");
						}
					}
				}
			}
			if(buffer.toString().length()>0){
				String headStr = buffer.toString().substring(0, buffer.toString().length()-1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}
			
			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if(skuList.size()>0){
				for(int j=0; j<skuList.size(); j++){
					List<String> imageList = new ArrayList<>();
					double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);
					
					double realTeamPrice = (double)skuList.get(0).getTeamPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);
					
					if(skuList.get(0).getAuctionPrice()!=null){
						double realAuctionPrice = (double)skuList.get(0).getAuctionPrice()/100;//拍卖价格“分”转化成“元”
						list.get(i).setRealAuctionPrice(realAuctionPrice);
					}
					
					double marketRealPrice = (double)skuList.get(0).getMarketPrice()/100; //价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());
					
					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if(j<1){
						for(int m = 0; m < personList.length(); m++){
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); //添加商品sku轮播图
					}
				}
				//list.get(i).setImage(url); //设置第一条sku图为商品主图
			}else{
				double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double)list.get(i).getMarketPrice()/100; //价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double)list.get(i).getTeamPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealTeamPrice(realTeamPrice);
			}
		}
		return list;
	}
	
	/**
	 * 排序、设为热门，设为推荐
	 */
	@Override
	public int changeValue(String id, String isHot, String isFlag, String sortnum) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(!StringUtils.isEmptyOrWhitespaceOnly(isHot)){
			if(Integer.parseInt(isHot)==1){
				goods.setIsHot(true);
			}else{
				goods.setIsHot(false);
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isFlag)){
			if(Integer.parseInt(isFlag)==1){
				goods.setIsFlag(true);
			}else{
				goods.setIsFlag(false);
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			goods.setSortnum(Short.parseShort(sortnum));
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}
	
	/**
	 * 商家后台商品销售排行列表
	 * @throws ParseException 
	 */
	@Override
	public PageBean<Goods> arrangeBySale(int shopId, String currentPage, int pageSize, String name)  {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		
		List<Goods> list = mapper.getListByShopId(shopId, name);
		for(int i=0; i<list.size(); i++){
			if(Integer.parseInt(currentPage)<2){
				list.get(i).setTop(i+1);
			}else{
				list.get(i).setTop(Integer.parseInt(currentPage)*10+i+1);
			}
		}
		
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	

	/**
	  * scj-pc端商品详情配送至获取用户默认地址
	  */
	@Override
	public String getAddressById(int userId) throws Exception {
		String attr = null;
		MemberUserAddress address = addressMapper.getByUserId(userId);
		if(address!=null){
			attr = address.getProvname()+address.getCityname()+address.getAreaname();
		}
		return attr;
	}
	
	/**
	 * 移动端分类商品列表(美食模块)
	 */
	@Override
	public PageBean<Goods> apiCategoryEatList(String catId, String currentPage, int pageSize) throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = pageSize;//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(Integer.parseInt(catId));
		for(GoodsCategory category : cateList){
			buffer.append(category.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		
		List<Goods> list = mapper.apiCategoryGoodsList(catIdList, 5, pageStart, pageSizes);
		if(list.size()>0){
			for(Goods goods : list){
				List<GoodsImage> imageList = imageMapper.selectListByGoodsId(goods.getId());
				MemberShop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
				goods.setImageUrl(imageList);
				goods.setShopName(shop.getShopName());
				goods.setShopLevel(shop.getLevel());
			}
		}
		
		int total = mapper.countApiCategoryGoodsList(catIdList, 5);//总条数
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<Goods> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);
		return page;
	}
	
	/**
	 * 获取后台首页店铺信息
	 */
	@Override
	public MemberShop getShopDetails(int shopId, int userId) throws Exception {
		MemberShop shop = shopMapper.selectByPrimaryKey(shopId);

		// 获取某个店铺星级的平均值
		int shopGrade = goodsCommentMapper.countStarAvg(shopId);

		// 查询某个商店所有已评论的订单的配送信息
		List<OrderSendInfo> sendInfoList = orderSendInfoMapper.getSendInfoByShopId(shopId);
		int speedLevel = 0;// 送货速度评级
		int sServiceLevel = 0;// 配送员服务评级
		int packLevel = 0;// 快递包装评级
		double speedLevelAvg = 0;// 平均送货速度评级
		double sServiceLevelAvg = 0;// 平均配送员服务评级
		double packLevelAvg = 0;// 平均快递包装评级
		double shopGradeAvg = 0;// 店铺平均评分
		if (sendInfoList.size() > 0) {
			int size = sendInfoList.size();
			for (OrderSendInfo orderSendInfo : sendInfoList) {
				speedLevel += orderSendInfo.getSpeedLevel();
				sServiceLevel += orderSendInfo.getsServiceLevel();
				packLevel += orderSendInfo.getPackLevel();
			}
			speedLevelAvg = (double) speedLevel / size;
			sServiceLevelAvg = (double) sServiceLevel / size;
			packLevelAvg = (double) packLevel / size;
			shopGradeAvg = (double) shopGrade / size;
		}
		DecimalFormat  df= new DecimalFormat("######0.0");//精确1位   
		shop.setSpeedLevelAvg(df.format(speedLevelAvg));
		shop.setsServiceLevelAvg(df.format(sServiceLevelAvg));
		shop.setPackLevelAvg(df.format(packLevelAvg));
		shop.setShopGradeAvg(df.format(shopGradeAvg));
		if (shop != null) {
			shop.setAdminId(userId);
		}
		return shop;
	}

	/**
	 * 获取移动端店铺信息
	 */
	@Override
	public MemberShop mgetShopDetails(int shopId, int userId) throws Exception {
		
		MemberShop shop = shopMapper.selectByPrimaryKey(shopId);
		// 获取某个店铺星级的平均值
		int shopGrade = goodsCommentMapper.countStarAvg(shopId);
		
		int orderShopNum=orderShopMapper.mCountorderShopNum(shopId);//商家订单总数
		
		List<OrderShop> orderShopList=orderShopMapper.mgetShopOrder(shopId);//商家订单
		double orderShopAmount=0;//交易总金额
		for (OrderShop orderShop : orderShopList) {
			double amount=(double)orderShop.getOrderPrice()/100;
			orderShopAmount+= amount;
		}
		
		// 查询某个商店所有已评论的订单的配送信息
		List<OrderSendInfo> sendInfoList = orderSendInfoMapper.getSendInfoByShopId(shopId);
		int speedLevel = 0;// 送货速度评级
		int sServiceLevel = 0;// 配送员服务评级
		int packLevel = 0;// 快递包装评级
		double speedLevelAvg = 0;// 平均送货速度评级
		double sServiceLevelAvg = 0;// 平均配送员服务评级
		double packLevelAvg = 0;// 平均快递包装评级
		double shopGradeAvg = 0;// 店铺平均评分
		double realOrderPrice=0;//押金
		if (sendInfoList.size() > 0) {
			int size = sendInfoList.size();
			for (OrderSendInfo orderSendInfo : sendInfoList) {
				speedLevel += orderSendInfo.getSpeedLevel();
				sServiceLevel += orderSendInfo.getsServiceLevel();
				packLevel += orderSendInfo.getPackLevel();
			}
			speedLevelAvg = (double) speedLevel / size;
			sServiceLevelAvg = (double) sServiceLevel / size;
			packLevelAvg = (double) packLevel / size;
			shopGradeAvg = (double) shopGrade / size;
		}
		shop.setOrderShopNum(orderShopNum);
		DecimalFormat  df1= new DecimalFormat("######0.0");//精确1位   
		shop.setSpeedLevelAvg(df1.format(speedLevelAvg));
		shop.setsServiceLevelAvg(df1.format(sServiceLevelAvg));
		shop.setPackLevelAvg(df1.format(packLevelAvg));
		shop.setShopGradeAvg(df1.format(shopGradeAvg));
		DecimalFormat  df2= new DecimalFormat("######0.00");//精确2位  
		shop.setRealPrice(df2.format(orderShopAmount));
		realOrderPrice=(double)shop.getOrderPrice()/100;
		shop.setRealOrderPrice(df2.format(realOrderPrice));
		if (shop != null) {
			shop.setAdminId(userId);
		}
		Member member =memberMapper.selectByPrimaryKey(userId);
		//查询上次登入时间
		List<SysLog> sysLogList=shopMapper.getUserLoginLog(member.getUsername());
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		int hours=0;
		if( sysLogList !=null && sysLogList.size()>0){
			String fromDate = simpleFormat.format(new Date());  
			String toDate = simpleFormat.format(sysLogList.get(0).getCreateDate());  
			long from = simpleFormat.parse(fromDate).getTime();  
			long to = simpleFormat.parse(toDate).getTime();  
			hours = (int) ((to - from)/(1000 * 60 * 60)); 
		}
		shop.setLoginTime(hours);
		return shop;
	}

	/**
	 * 获取店铺所有商品
	 */
	@Override
	public List<Goods> selectAllByShopId(int shopId) throws Exception {
		Goods good = new Goods();
		good.setShopId(shopId);
		List<Goods> list = mapper.getAllByShopId(good);
		return list;
	}
	
	/**
	 * 商家后台设置商品热门、新品、推荐
	 */
	@Override
	public int setHotNewAndFlag(String id, String isHotShop, String isShopFlag, String isNewShop, String publicImage) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(!StringUtils.isEmptyOrWhitespaceOnly(isHotShop)){
			goods.setIsHotShop(Integer.parseInt(isHotShop));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isShopFlag)){
			if(Integer.parseInt(isShopFlag)==1){
				goods.setIsShopFlag(true);
			}else{
				goods.setIsShopFlag(false);
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(isNewShop)){
			goods.setIsNewShop(Integer.parseInt(isNewShop));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(publicImage)){
			goods.setPublicimg(publicImage);
		}
		
		return mapper.updateByPrimaryKeySelective(goods);
	}
	
	/**
	 * 获取App下载地址
	 */
	@Override
	public String getDownloadUrl(String fz) throws Exception {
		String url = null;
		if(Integer.parseInt(fz)==1){ //android
			url = Contants.ANDROID_DOWNLOAD_URL;
		}
		if(Integer.parseInt(fz)==2){//ios
			url = Contants.IOS_DOWNLOAD_URL;
		}
		return url;
	}
	
	/**
	 * 获取分享商品详情
	 */
	@Override
	public ShareResult getShareGoodsDetails(String skuId, String teamNo, String userId, String orderId, String shareType,
			String shareUrl, Member member) throws Exception {
		
		//分享日志插入
		if(!StringUtils.isEmptyOrWhitespaceOnly(userId) && !StringUtils.isEmptyOrWhitespaceOnly(skuId) && member!=null){ //新增链接访问记录
			GoodsShareLog shareLog  = shareLogMapper.getBymIdAndSkuId(Integer.parseInt(userId), Integer.parseInt(skuId),teamNo);
			if(shareLog!=null){
				if(!shareLog.getrMId().equals(member.getId())){
					shareLog.setVisit(shareLog.getVisit()+1); //链接访问次数+1
					shareLog.setrMId(member.getId()); //当前访问人id
					shareLogMapper.updateByPrimaryKeySelective(shareLog);
				}
			}else{
				GoodsShareLog entity = new GoodsShareLog();
				entity.setmId(Integer.parseInt(userId)); //分享人
				entity.setrMId(member.getId()); //当前访问人
				
				Order order = orderMapper.selectByPrimaryKey(Integer.parseInt(orderId));
				entity.setOrderNo(order.getOrderNo()); //订单号
				
				if(order.getFz()==1){
					entity.setOrderType(1); //订单类型
				}else{
					entity.setOrderType(2);
				}
				
				OrderTeam orderTeam = orderTeamMapper.getByOrderNo(order.getOrderNo());
				if(orderTeam!=null){
					entity.setTeamNo(orderTeam.getTeamNo()); //团号
				}
				
				entity.setSkuId(Integer.parseInt(skuId)); //商品规格属性id
				if(!StringUtils.isEmptyOrWhitespaceOnly(shareType)){
					entity.setShareType(Integer.parseInt(shareType));
				}
				
				entity.setVisit(1); //访问量
				
				if(!StringUtils.isEmptyOrWhitespaceOnly(shareUrl)){
					entity.setShareUrl(shareUrl); //分享链接
				}
				//分享者增加积分
				try {
					addShareScore(Integer.parseInt(userId));
				} catch (Exception e) {
					e.printStackTrace();
				}
				shareLogMapper.insertSelective(entity);
			}
			
		}
		
		//分享页面详情
		ShareResult result = new ShareResult();
		
		GoodsSku sku = skuMapper.selectByPrimaryKey(Integer.parseInt(skuId));
		if(sku!=null){
			Goods goods  = mapper.selectByPrimaryKey(sku.getGoodsId());
			
			selectBygoodsId(goods.getId());
			result.setGoods(goods);//插入商品基本信息
			
			result.setSkuId(Integer.parseInt(skuId)); //商品skuId 
			
			OrderTeam orderTeam = orderTeamMapper.getByTeamNoAndOwner(teamNo);
			if(orderTeam!=null){
				//cheng
				List<OrderTeam> lastList = orderTeamMapper.selectLastOne(orderTeam.getTeamNo());
				if (lastList.size()>0) {
					TeamLastOne teamLastOne = new TeamLastOne();
					Member member1 = memberMapper.selectByPrimaryKey(lastList.get(0).getmId());
					if(member1!=null){
						if(member1.getHeadimgurl()!=null){
							teamLastOne.setHeadimgurl(member1.getHeadimgurl()); //主单用户头像
						}else{
							teamLastOne.setHeadimgurl(Contants.headImage); //默认头像
						}
						teamLastOne.setUsername(URLDecoder.decode(member1.getUsername(),"utf-8")); //主单用户昵称
					}else{
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(lastList.get(0).getmId());
						if(promoteUser!=null){
							teamLastOne.setHeadimgurl(promoteUser.getHeadImg()); //主单用户头像
							teamLastOne.setUsername(URLDecoder.decode(promoteUser.getName(),"utf-8")); //主单用户昵称
						}
					}
					result.setTeamLastOne(teamLastOne);
				}
				
				
				
				
				Member member1 = memberMapper.selectHeadAndName(orderTeam.getmId());
				if(member1!=null){
					if(org.apache.commons.lang.StringUtils.isNotEmpty(member1.getHeadimgurl())){
						result.setHeadimgurl(member1.getHeadimgurl()); //主单用户头像
					}else{
						result.setHeadimgurl(Contants.headImage); //默认头像
					}
					result.setUserName(URLDecoder.decode(member1.getUsername(),"utf-8")); //主单用户昵称
				}else{
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(orderTeam.getmId());
					if(promoteUser!=null){
						result.setHeadimgurl(promoteUser.getHeadImg()); //主单用户头像
						result.setUserName(URLDecoder.decode(promoteUser.getName(),"utf-8")); //主单用户昵称
					}
				}
				
				int count = orderTeamMapper.groupCount(orderTeam.getTeamNo());
				int waitNum = goods.getTeamNum()-count;
				result.setWaitNum(waitNum); //主单等待人数
				
				long a = new Date().getTime();
				long b = orderTeam.getEndTime().getTime();
				if(b>a){
					int c = (int)((b-a) / 1000);
					String hours = c / 3600+"";
					if(Integer.parseInt(hours)<10){
						hours = "0"+ hours;
					}
					String min = c % 3600 /60+"";
					if(Integer.parseInt(min)<10){
						min = "0"+ min;
					}
					String sencond = c % 3600 % 60+"";
					if(Integer.parseInt(sencond)<10){
						sencond = "0"+ sencond;
					}
					String waitTime = hours+":"+min+":"+sencond;
					result.setWaitTime(waitTime); //主单等待时间
				}
			}
			
			result.setGoodsId(goods.getId()); //商品id
			result.setName(goods.getName()); //商品名称
			
			double teamPrice = (double)sku.getTeamPrice()/100;
			result.setTeamPrice(teamPrice); //商品团购价
			
			double skuPrice = (double)sku.getSellPrice()/100;
			int chaPrice = sku.getSellPrice() - sku.getTeamPrice();
			double bhPrice = (double)chaPrice/100;
			result.setBhPrice(bhPrice); //商品差价
			
			org.json.JSONObject jsonObj = new org.json.JSONObject(sku.getValue()); //获取sku商品信息
			org.json.JSONArray personList = jsonObj.getJSONArray("url");
			String goodsImage = (String) personList.get(0);
			result.setGoodsImage(goodsImage); //商品图片
			
			//int num = orderTeamMapper.getGroupCount(goods.getId()); //多少人正在拼团
			result.setTeamNum(goods.getTeamNum());//几人团
			
			int finishNum = goods.getFixedSale(); 		
			String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
			if(countT!=null){
				finishNum = goods.getFixedSale()+Integer.parseInt(countT);
			}
			result.setFinishNum(finishNum); //已拼多少件
			
			
			List<OrderTeam> list = orderTeamMapper.getGroupingList(goods.getId()); //其他团购单列表
			int groupCount = 0;
			if(list.size()>0){
				for(OrderTeam team : list){
					List<OrderTeam> lastList = orderTeamMapper.selectLastOne(team.getTeamNo());
					if (lastList.size()>0) {
						TeamLastOne teamLastOne = new TeamLastOne();
						Member member1 = memberMapper.selectByPrimaryKey(lastList.get(0).getmId());
						if(member1!=null){
							if(org.apache.commons.lang.StringUtils.isNotEmpty(member1.getHeadimgurl())){
								teamLastOne.setHeadimgurl(member1.getHeadimgurl()); //主单用户头像
							}else{
								teamLastOne.setHeadimgurl(Contants.headImage); //默认头像
							}
							teamLastOne.setUsername(URLDecoder.decode(member1.getUsername(),"utf-8")); //主单用户昵称
						}else{
							PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(lastList.get(0).getmId());
							if(promoteUser!=null){
								teamLastOne.setHeadimgurl(promoteUser.getHeadImg()); //主单用户头像
								teamLastOne.setUsername(URLDecoder.decode(promoteUser.getName(),"utf-8")); //主单用户昵称
							}
						}
						team.setTeamLastOne(teamLastOne);
					}
					
					
					Member member2 = memberMapper.selectHeadAndName(team.getmId());
					if(member2!=null){
						if(org.apache.commons.lang.StringUtils.isNotEmpty(member2.getHeadimgurl())){
							//result.setHeadimgurl(member2.getHeadimgurl()); //主单用户头像
							team.setHeadimgurl(member2.getHeadimgurl());
						}else{
							//result.setHeadimgurl(Contants.headImage); //默认头像
							team.setHeadimgurl(Contants.headImage);
						}
						//result.setUserName(member2.getUsername()); //主单用户昵称
						team.setUsername(URLDecoder.decode(member2.getUsername(),"utf-8"));
					}else{
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
						if(promoteUser!=null){
							team.setHeadimgurl(promoteUser.getHeadImg()); //用户头像
							team.setUsername(URLDecoder.decode(promoteUser.getName(),"utf-8")); //用户昵称
						}
						
					}
					
					groupCount = orderTeamMapper.groupCount(team.getTeamNo());
					int waitNum = goods.getTeamNum()-groupCount;
					team.setWaitNum(waitNum);
					
					long a = new Date().getTime();
					long b = team.getEndTime().getTime();
					if(b>a){
						int c = (int)((b-a) / 1000);
						String hours = c / 3600+"";
						if(Integer.parseInt(hours)<10){
							hours = "0"+ hours;
						}
						String min = c % 3600 /60+"";
						if(Integer.parseInt(min)<10){
							min = "0"+ min;
						}
						String sencond = c % 3600 % 60+"";
						if(Integer.parseInt(sencond)<10){
							sencond = "0"+ sencond;
						}
						String waitTime = hours+":"+min+":"+sencond;
						team.setWaitTime(waitTime);
					}
				}
			}
			result.setTeamList(list);
		}
		
		return result;
	}

	/**
	 * 活动添加商品处获取商品列表
	 */
	@Override
	public List<Goods> selectAll(Goods entity) throws Exception {
		List<Goods> list = mapper.selectAll(entity);
		if(list.size()>0){
			for(Goods goods : list){
				double realPrice = (double)goods.getSellPrice()/100; //销售价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				double marketRealPrice = (double)goods.getMarketPrice()/100;//市场价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);
				
				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if(category!=null){
					goods.setCategory(category.getName());
				}
			}
		}
		return list;
	}

	@Override
	public PageBean<Goods> apiCateGoodsList(Goods entity) throws Exception {
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(entity.getCatId());
		for(GoodsCategory category : cateList){
			buffer.append(category.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		entity.setCatIdList(catIdList);
		
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<Goods> list = mapper.apiCateList(entity, catIdList);
		setGetter(list);
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	
	// cheng修改商品的排序2018-02-02
	public int updateGoodsSortnum(List<Goods> goods) throws Exception {
		int row = 0;
		for (Goods goods2 : goods) {
			row = mapper.updateByPrimaryKeySelective(goods2);
			if (goods2.getSortnum() != 30000) {
				List<Goods> goodsList = mapper.getGoodsBySortnum(goods2.getSortnum(), goods2.getId());// 查询比当前排序号大或等于的商品（30000除外，和本商品除外）
				for (Goods goods3 : goodsList) {
					List<Goods> goodList1 = mapper.getGoodsBySort(goods3.getSortnum(), goods3.getId());
					if (goodList1.size() == 0) {
						break;//
					}
					goods3.setSortnum((short) (goods3.getSortnum() + (short) 1));// 排序号+1
					mapper.updateByPrimaryKeySelective(goods3);
				}
			}
		}
		return row;
	}

	public int updateGoodsShopSortnum(List<Goods> goods) {
		int row = 0;
		for (Goods goods2 : goods) {
			row = mapper.updateByPrimaryKeySelective(goods2);
			if (goods2.getShopsortnum() != 30000) {
				List<Goods> goodsList = mapper.getGoodsByShopsortnum(goods2.getShopsortnum(), goods2.getId());// 查询比当前排序号大或等于的商品（30000除外，和本商品除外）
				for (Goods goods3 : goodsList) {
					List<Goods> goodList1 = mapper.getGoodsByShopSort(goods3.getShopsortnum(), goods3.getId());
					if (goodList1.size() == 0) {
						break;//
					}
					goods3.setShopsortnum((short) (goods3.getShopsortnum() + (short) 1));// 排序号+1
					mapper.updateByPrimaryKeySelective(goods3);
				}
			}
		}
		return row;
	}
	
    
    /*zlk 获取店铺 热门商品*/
	@Override
	public List<Goods> selectHotByShopId(int shopId) throws Exception {
		// TODO Auto-generated method stub
		Goods good = new Goods();
		good.setShopId(shopId);
		good.setIsHotShop(1);//1为热门
		return mapper.getAllByShopId(good);
	}

	/*zlk 移动端添加 商品*/
	@Override
	public int mInsert(String name, String title, String catId,String sellPrice, 
			String marketPrice,String description, String storeNums,
			String image, String saleType, String teamNum, String teamEndTime,
			String isPromote, String isCreate, String shopId,String isPopular) throws Exception {
		// TODO Auto-generated method stub
		Goods goods = new Goods();
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){//商品名称
			goods.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(title)){//商品标题
			goods.setTitle(title);
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(isPopular)) {
			goods.setIsPopular(-1);
		}else {
			goods.setIsPopular(Integer.parseInt(isPopular));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){//分类id
			goods.setCatId(Long.valueOf(catId));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)){//销售价格单位分
			Integer sPrice =(int) (Double.parseDouble(sellPrice) * 100);
			goods.setSellPrice(sPrice);
			
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)){//市场价格单位分
			Integer mPrice =(int) (Double.parseDouble(marketPrice) * 100);
			goods.setMarketPrice(mPrice);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(storeNums)){//库存
			goods.setStoreNums(Integer.valueOf(storeNums));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){//图片
			goods.setImage(image);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(saleType)){//销售模式 1单卖 2拼团单卖皆可
			goods.setSaleType(Integer.valueOf(saleType));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamNum)){//拼团人数
			goods.setTeamNum(Integer.valueOf(teamNum));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamEndTime)){//活动时间
			goods.setTeamEndTime(Integer.valueOf(teamEndTime));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPromote)){//是否促成团(-1 否 0是
			goods.setIsPromote(Integer.valueOf(isPromote));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(isCreate)){//是否支持系统发起拼单  -1否 0是
			goods.setIsCreate(Integer.valueOf(isCreate));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(shopId)){ //商家id
	    	goods.setShopId(Integer.valueOf(shopId));
		}
		
		
		Date date = new Date();
		goods.setAddtime(new java.sql.Timestamp(date.getTime())); //添加时间
		int row = mapper.insertSelective(goods); //插入商品
		
		GoodsDesc goodsDesc = new GoodsDesc(); //插入商品详细描述
		goodsDesc.setIsPc(0); //移动端
		goodsDesc.setGoodsId(goods.getId());
		if(!StringUtils.isEmptyOrWhitespaceOnly(description)){
			goodsDesc.setDescription(description);
		}
		int rowD = descMapper.insertGoodsDesc(goodsDesc);
		
		return row;
	}

	/**
	 * @Description: 更新商家店铺头像
	 * @author xieyc
	 * @date 2018年3月6日 下午2:28:29 
	 */
	public Integer updateShopLogo(MemberShop memberShop) {
		return shopMapper.updateByPrimaryKeySelective(memberShop);
	}

	/*zlk 获取店铺所有上架、下架 商品 分页*/
	@Override
	public Map<String, Object> selectByShopId(Goods good) throws Exception {
           
		   PageHelper.startPage(Integer.parseInt(good.getCurrentPage()), Contants.PAGE_SIZE, true);
		   Map<String,Object> map=new HashMap<String,Object>();
		   List<Goods> list = mapper.getByShopIdAndStatus(good);
		   //转换金额
		   for(int i=0;i<list.size();i++){
			   list.get(i).setRealPrice(list.get(i).getSellPrice()/100);
			   list.get(i).setMarkRealPrice(list.get(i).getMarketPrice()/100);
		   }
		   PageBean<Goods> pageBean = new PageBean<>(list);
		   
		    //上架数量
		   good.setStatus(5);
		   int count = mapper.countByShopId(good);
		   
		   //下架数量
		   good.setStatus(2);
		   int count2 = mapper.countByShopId(good);
		   
		   map.put("Good", pageBean);
		   map.put("onSale", count);
		   map.put("notOnSale", count2);
		   return map;
	}

	@Override
	public int mUpdateById(Goods good) throws Exception {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(good);
	}
	
	/**
	 * 修改商品活动类型
	 */
	@Override
	public int updateTopicType(Goods entity) throws Exception {
		if (entity.getTopicType()==6) {
			entity.setIsCreate(-1);
			entity.setIsPromote(-1);
			entity.setSaleType(1);
		}
		return mapper.updateByPrimaryKeySelective(entity);
	}
	
	
	//修改平台的新品
	public int updatePingTaiNew(Goods g) throws Exception{
		int row = 0;
		row = mapper.updateByPrimaryKeySelective(g);
		return row;
	}
	
	/**
	 * 程凤云新品列表
	 * @param list
	 * @return
	 */
	public List<Goods> changeParam(List<Goods> list){
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;
		
		for(int i=0; i<list.size(); i++){
			//int avgStar=goodsCommentMapper.countStar(list.get(i).getShopId(),list.get(i).getId());
			int groupCount = 0; //团购商品销量统计
			String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			if(countT!=null){
				groupCount = Integer.parseInt(countT)+list.get(i).getFixedSale();
			}else{
				groupCount = list.get(i).getFixedSale();
			}
			list.get(i).setGroupCount(groupCount);
			
			StringBuffer buffer = new StringBuffer(); //设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if(orderTeamList.size()>0){
				for(OrderTeam entity : orderTeamList){
					if(entity.getType()==0){//普通用户
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if(member!=null){
							if(member.getHeadimgurl()!=null){
								buffer.append(member.getHeadimgurl()+",");
							}else{
								buffer.append(Contants.headImage+",");
							}
						}else{
							buffer.append(Contants.headImage+",");
						}
					}else{//虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if(promoteUser!=null){
							buffer.append(promoteUser.getHeadImg()+",");
						}else{
							buffer.append(Contants.headImage+",");
						}
					}
				}
			}
			if(buffer.toString().length()>0){
				String headStr = buffer.toString().substring(0, buffer.toString().length()-1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}
			
			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if(skuList.size()>0){
				for(int j=0; j<skuList.size(); j++){
					List<String> imageList = new ArrayList<>();
					double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);
					
					double realTeamPrice = (double)skuList.get(0).getTeamPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);
					
					double marketRealPrice = (double)skuList.get(0).getMarketPrice()/100; //价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());
					
					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if(j<1){
						for(int m = 0; m < personList.length(); m++){
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); //添加商品sku轮播图
					}
				}
				list.get(i).setImage(url); //设置第一条sku图为商品主图
			}else{
				double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double)list.get(i).getMarketPrice()/100; //价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double)list.get(i).getTeamPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealTeamPrice(realTeamPrice);
			}
		}
		return list;
	}
	
	/**
	 * 移动端首页商品列表
	 */
	@Override
	public Map<String, Object> newGoodsList(Goods entity) throws Exception {
		entity.setPageSize(Contants.PAGE_SIZE);//每页的数量
		entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage())-1)*entity.getPageSize());
		List<MyNewGoodsPojo> list = mapper.apiNewGoodsListByLimit(entity);
		//List<Goods> list=mapper.apiGoodsList(entity);
		if (list.size()>0) {
			for (MyNewGoodsPojo myNewGoodsPojo : list) {
				String image = myNewGoodsPojo.getImage();
				if (org.apache.commons.lang.StringUtils.isNotEmpty(image)) {
					myNewGoodsPojo.setImage(RegExpValidatorUtils.returnNewString(image));
				}
			}
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		return map;
	}

	@Override
	public PageBean<Goods> mSelectByShopid(int shopId, String name, String currentPage, int pageSize, String catId,
			String saleType, String status, String startPrice, String endPrice, String topicType, String id,
			String skuNo, String jdSkuNo, String sort) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
//		List<Goods> list = mapper.selectShopGoodsList(shopId, name, catId, saleType, status,startPrice,
//				endPrice, topicType, id, skuNo, jdSkuNo);
		List<Goods> list = mapper.mSelectShopGoodsList(shopId, name, catId, saleType, status,startPrice,
				endPrice, topicType, id, skuNo, jdSkuNo,sort);
		Goods good = new Goods();
		for(int i=0;i<list.size();i++){
			
			good.setStatus(5);
			good.setShopId(shopId);
			int count = mapper.countByShopId(good);
			list.get(i).setOnSaleNum(count);
			//下架数量
			 good.setStatus(2);
			int count2 = mapper.countByShopId(good);
			list.get(i).setNotOnSaleNum(count2);
		}
		if(list.size()>0){
			for(Goods goods : list){
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
				if(skuList.size()>0){
					for(int i=0; i<skuList.size(); i++){
						double realSkuPrice = (double)skuList.get(i).getSellPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setRealPrice(realSkuPrice);
						double marketRealPrice = (double)skuList.get(i).getMarketPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setMarketRealPrice(marketRealPrice);
						
						double realJdPrice = (double)skuList.get(i).getJdPrice()/100; //京东价转化成“元”
						skuList.get(i).setRealJdPrice(realJdPrice);
						
						double realDeliveryPrice = (double)skuList.get(i).getDeliveryPrice()/100; //物流价转化成“元”
						skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);
						
						double realJdBuyPrice = (double)skuList.get(i).getJdBuyPrice()/100; //客户购买价“分”转化成“元”
						skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);
						
						double realJdOldBuyPrice = (double)skuList.get(i).getJdOldBuyPrice()/100; //客户购买价(旧)“分”转化成“元”
						skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);
						
						double realJdProtocolPrice = (double)skuList.get(i).getJdProtocolPrice()/100; //价格“分”转化成“元”
						skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);
						
						double realStockPrice = (double)skuList.get(i).getStockPrice()/100; //进货价
						skuList.get(i).setRealStockPrice(realStockPrice);
						
						if(skuList.get(i).getTeamPrice()!=null){
							double realTPrice = (double)skuList.get(i).getTeamPrice()/100;//团购价格“分”转化成“元”
							skuList.get(i).setRealTeamPrice(realTPrice);
						}
						JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); //value转义
						skuList.get(i).setValueObj(jsonObject);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyOne())){
						goods.setKeyOne(skuList.get(0).getKeyOne());
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyTwo())){
						goods.setKeyTwo(skuList.get(0).getKeyTwo());
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyThree())){
						goods.setKeyThree(skuList.get(0).getKeyThree());
					}
					goods.setSkuList(skuList);
				}
				double realPrice = (double)goods.getSellPrice()/100; //销售价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				double marketRealPrice = (double)goods.getMarketPrice()/100;//市场价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);
				
				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if(category!=null){
					goods.setCategory(category.getName());
				}
				
				int sale = goods.getSale()+goods.getFixedSale();//销量
				goods.setSale(sale);
				
				if(goods.getStatus()==0){
					goods.setGoodsStatus("正常");
				}
				if(goods.getStatus()==1){
					goods.setGoodsStatus("已删除");
				}
				if(goods.getStatus()==2){
					goods.setGoodsStatus("下架");
				}
				if(goods.getStatus()==3){
					goods.setGoodsStatus("申请上架");
				}
				if(goods.getStatus()==4){
					goods.setGoodsStatus("拒绝");
				}
			}
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	
	/**
	 * 价格的转化及sku与主表的赋值
	 * @param list
	 * @return
	 */
	public List<Goods> skuSetter(List<Goods> list){
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;
		
		for(int i=0; i<list.size(); i++){
			//int avgStar=goodsCommentMapper.countStar(list.get(i).getShopId(),list.get(i).getId());
			
			int groupCount = 0; //团购商品销量统计
			Goods goods  = mapper.selectByPrimaryKey(list.get(i).getId());
			if (goods.getSaleType()==1) {
				groupCount=goods.getSale()+goods.getFixedSale();
			}else{
				String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
				if(countT!=null){
					groupCount = Integer.parseInt(countT)+goods.getFixedSale();
				}else{
					groupCount = goods.getFixedSale();
				}
			}
			
			list.get(i).setGroupCount(groupCount);
			
			StringBuffer buffer = new StringBuffer(); //设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if(orderTeamList.size()>0){
				for(OrderTeam entity : orderTeamList){
					if(entity.getType()==0){//普通用户
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if(member!=null){
							if(member.getHeadimgurl()!=null){
								buffer.append(member.getHeadimgurl()+",");
							}else{
								buffer.append(Contants.headImage+",");
							}
						}else{
							buffer.append(Contants.headImage+",");
						}
					}else{//虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if(promoteUser!=null){
							buffer.append(promoteUser.getHeadImg()+",");
						}else{
							buffer.append(Contants.headImage+",");
						}
					}
				}
			}
			if(buffer.toString().length()>0){
				String headStr = buffer.toString().substring(0, buffer.toString().length()-1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}
			
			
			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if(skuList.size()>0){
				for(int j=0; j<skuList.size(); j++){
					List<String> imageList = new ArrayList<>();
					double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);
					
					double realTeamPrice = (double)skuList.get(0).getTeamPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);
					
					double marketRealPrice = (double)skuList.get(0).getMarketPrice()/100; //价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());
					
					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if(j<1){
						for(int m = 0; m < personList.length(); m++){
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); //添加商品sku轮播图
					}
				}
				list.get(i).setImage(url); //设置第一条sku图为商品主图
			}else{
				double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double)list.get(i).getMarketPrice()/100; //价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double)list.get(i).getTeamPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealTeamPrice(realTeamPrice);
			}
		}
		return list;
	}
	
	
	
	
	
	
	/**
	 * @Description: 判断是否是平台下架商家商品操作
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51 
	 */
	public boolean isPtSoldOut(Integer shopId,String id) {
		Goods goods=mapper.selectByPrimaryKey(Integer.valueOf(id));
		if(goods.getShopId().intValue()!=shopId.intValue()){
			if(goods.getShopId()!=1){//admin下架自己自营的商品也不发送短信
				return true;
			}
		}
		return false;
	}
	public MemberShop selectMemberShopBymId(Integer mId) throws Exception{
		return memberShopMapper.selectByPrimaryKey(mId);
	}
	/**
	 * @Description: 发送短信通知商家商品下架原因
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51 
	 */
	public BhResult sendMsgToShop(String id, String msg,HttpServletRequest request) {
		Goods good=mapper.selectByPrimaryKey(Integer.valueOf(id));
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(good.getShopId());	
		Sms sms = new Sms();
		sms.setPhoneNo(memberShop.getLinkmanPhone());
		sms.setShopName(memberShop.getShopName());
		sms.setGoodsName(good.getName());
		sms.setMsg(msg);
		BhResult r=SmsUtil.aliPushGoodsSoldOutMsg(sms);
		if(r.getStatus()==200){
			r.setMsg("亲爱的"+memberShop.getShopName()+"会员,你店铺的"+good.getName()+"已被下架，原因："+msg+"。如有疑问请联系客服！");
		}
		return r;
	}
	/**
	 * @Description: 保存短信内容到数据库
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51 
	 */
	public int saveGoodsMsg(String msg, String id) {
		Goods goods=mapper.selectByPrimaryKey(Integer.valueOf(id));
		GoodsMsg goodsMsg=new GoodsMsg();
		goodsMsg.setMsg(msg);
		goodsMsg.setShopId(goods.getShopId());
		goodsMsg.setCreateTime(new Date());
		goodsMsg.setUpdateTime(new Date());
		goodsMsg.setIsfalgbypt(0);//平台没读
		goodsMsg.setIsfalgbyshop(0);//商家没读
		goodsMsg.setMsgtype(0);//下架短信通知
		return goodsMsgMapper.insert(goodsMsg);
	}
	
	/**
	 * @Description: 商品下架短信列表
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51 
	 */
	public PageBean<GoodsMsg> goodsMsgList(GoodsMsg goodsMsg) {
		PageHelper.startPage(goodsMsg.getCurrentPage(), goodsMsg.getPageSize(), true);
		List<GoodsMsg> list = goodsMsgMapper.getAll(goodsMsg.getShopId());
		int isShop=0;
		if(goodsMsg.getShopId()==1){//为1平台登入
			isShop=0;//如果是平台登入那么查询是否有商家发送的消息没读
		}else{//是其他的为商家登入
			isShop=1;//如果是商家登入那么查询是否平台发送的消息没读
		}
		for (GoodsMsg msg : list) {
			//查询商家或平台是否有回话消息没读
			List<InteractiveRecord>	listChat =interactiveRecordMapper.getList(msg.getId(),isShop);
			boolean isRead=false;
			if(listChat.size()==0){
				isRead=true;
			}
			msg.setRead(isRead);
			List<InteractiveRecord>	interactiveRecordList=interactiveRecordMapper.getListByMsgId(msg.getId());//查询所有回话列表
			msg.setListRecord(interactiveRecordList);
		}
		PageBean<GoodsMsg> page = new PageBean<>(list);
		return page;
	}
	
	/**
	 * @Description: 商家与平台互动信息保存
	 * @author xieyc
	 * @date 2018年3月27日 下午4:50:23 
	 */
	public int saveChatRecord(InteractiveRecord record) {
		record.setCreateTime(new Date());
		record.setIsFlag(0);//未读
		return interactiveRecordMapper.insert(record);
	}
	/**
	 * @Description: 更新短信状态
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53 
	 */
	public int updateMsgstate(String id,int shopId) {
		int row=0;
		int isShop=0;
		if(shopId==1){//为1平台登入
			isShop=0;//如果是平台登入那么查询是否有商家发送的消息没读
		}else{//是其他的为商家登入
			isShop=1;//如果是商家登入那么查询是否平台发送的消息没读
		}
		List<InteractiveRecord>	interactiveRecordList =interactiveRecordMapper.getList(Integer.valueOf(id),isShop);
		for (InteractiveRecord interactiveRecord : interactiveRecordList) {
			if(interactiveRecord.getIsFlag()==0){//将没有读的信息改成已读
				interactiveRecord.setIsFlag(1);
				row=interactiveRecordMapper.updateByPrimaryKey(interactiveRecord);
			}
		}
		return  row;
	}

	/**
	 * @Description: 更新短信状态
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53 
	 */
	public int updateGoodsMsg(Integer shopId) {
		int row=0;
		List<GoodsMsg> list = goodsMsgMapper.getAll(shopId);
		if(shopId==1){//为1平台登入
			for (GoodsMsg goodsMsg : list) {
				if(goodsMsg.getIsfalgbypt()==0){
					goodsMsg.setIsfalgbypt(1);
					goodsMsg.setUpdateTime(new Date());
					row=goodsMsgMapper.updateByPrimaryKeySelective(goodsMsg);
				}
			}
		}else{//是其他的为商家登入
			for (GoodsMsg goodsMsg : list) {
				if(goodsMsg.getIsfalgbyshop()==0){
					goodsMsg.setIsfalgbyshop(1);
					goodsMsg.setUpdateTime(new Date());
					row=goodsMsgMapper.updateByPrimaryKeySelective(goodsMsg);
				}
			}
		}
		return row;
	}

	/**
	 * @Description: 获取没有读的信息条数
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53 
	 */
	public int getUnreadNum(Integer shopId) {
		List<GoodsMsg> list = goodsMsgMapper.getAll(shopId);
		int isShop=0;
		int returnNum=0;
		if(shopId==1){//为1平台登入
			isShop=0;//如果是平台登入那么查询是否有商家发送的消息没读
		}else{//是其他的为商家登入
			isShop=1;//如果是商家登入那么查询是否平台发送的消息没读
		}
		for (GoodsMsg msg : list) {
			//查询商家或平台是否有回话消息没读
			List<InteractiveRecord>	listChat =interactiveRecordMapper.getList(msg.getId(),isShop);
			boolean isRead=true;
			if(listChat.size()>0){
				isRead=false;
			}
			msg.setRead(isRead);//设置该短信是否已读
		}
		for (GoodsMsg goodsMsg : list) {//跟上isFalg和isRead同时判断短信是否为已读
			if(shopId==1){//平台
				if(goodsMsg.getIsfalgbypt()==0){
					returnNum++;
				}else{
					if(!goodsMsg.isRead()){
						returnNum++;
					}
				}
			}else{//商家
				if(goodsMsg.getIsfalgbyshop()==0){
					returnNum++;
				}else{
					if(!goodsMsg.isRead()){
						returnNum++;
					}
				}
			}
		}
		return returnNum;
	}
	/**
	 * zlk 按商品的价格修改商品的当前已售数量
	 * 2018.3.27
	 */
	@Override
	public void updateFixedSale() {
		
		GoodsSaleP g = new GoodsSaleP();
		List<GoodsSaleP> goodsSaleP = goodsSalePMapper.selectAll(g);//获取所有商品价格区间
		System.out.println("goodsSaleP size-->"+goodsSaleP.size());
	
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<goodsSaleP.size();i++){ //遍历商品区间
			 GoodsSaleP p = goodsSaleP.get(i); 
			 Goods goods = new Goods();
			 goods.setMin(p.getMin());
			 goods.setMax(p.getMax());
			 
			 List<Goods> goodsList = mapper.getGoodsBySale(goods);//获取当前区间的商品
		 	 System.out.println("goodsList size-->"+goodsList.size());
			 int num=0;
			 GoodsSaleAllot.setGoodsFixedSale(goodsList, p.getTotalNum());
			 for(int j=0;j<goodsList.size();j++){
				 Goods good = new Goods();
				 good.setId(goodsList.get(j).getId());
				 good.setFixedSale(goodsList.get(j).getFixedSale());
				 mapper.updateFixedSale(good); 
				 num+=goodsList.get(j).getFixedSale();
			 }
			 list.add(num);
			 list.add(p.getTotalNum());
		}
		for(Integer num2:list){
			 System.out.println(num2);
		}
//			 for(int l=0;l<goodsList.size();l++){ 
//				 Goods gg = goodsList.get(l);
//				 int fixedSale = gg.getFixedSale();//当前商品的已售数量
//				 int totalNum = p.getTotalNum(); //当前区间的分发已售数量总数
//				 System.out.println("totalNum---->"+totalNum);
//				 
//				 int randNum  = 0;
//				 if(goodsList.size()-1==l){//最后一个此区间商品，也就此区间只有一个商品符合
//					 randNum = totalNum; 
//				 }else{ 
//					 randNum = StringUtil.getMaxVal(totalNum,goodsList.size()-l);//把总数分成随机数
//					 int totalRecord =goodsList.size()-l;//当前的剩余商品
//					 System.out.println("totalRecord---->"+totalRecord);
//					 if(totalNum>0){
//					     p.setTotalNum(totalNum-randNum); 
//					 }
//					 
//				 }
//				 System.out.println("randNum---->"+randNum);
//				 fixedSale = fixedSale+randNum; //当前商品的已售数量赋值
//
//				 System.out.println("fixedSale---->"+fixedSale);
//				 Goods req = new Goods();
//				 System.out.println("gg.getId()---->"+gg.getId());
//				 req.setId(gg.getId());
//				 req.setFixedSale(fixedSale);
//				 mapper.updateByPrimaryKeySelective(req); 
//			 }

		
	}

	
	
	/**
	 * 拍卖详情页
	 */
	@Override
	public Goods dauctionGoodsDetails(String goodsId , String currentPeriods, Member member) {
		Member m = null;
		long thisTime = 0;
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(goodsId));
		goods.setmId(member.getId());
		
		HollandDauctionLog log = new HollandDauctionLog();
		log.setGoodsId(goods.getId());
		log.setCurrentPeriods(Integer.parseInt(currentPeriods));
		List<HollandDauctionLog> logList = dauctionLogMapper.getListByGoodsIdAndCurrentPeriods(log);
		
		Map<String, Object> map = new LinkedHashMap<>();
		if(logList.size()>0){
			map.put("mId", logList.get(0).getmId());
			m = memberMapper.selectByPrimaryKey(logList.get(0).getmId());
			if(m!=null){
				//2018.5.29 zlk
				try {
					map.put("mNick", URLDecoder.decode(m.getUsername(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//end
				if(m.getHeadimgurl()==null || m.getHeadimgurl()==""){
					map.put("mHead", Contants.headImage);
				}else{
					map.put("mHead", m.getHeadimgurl());
				}
			}else{
				map.put("mNick", null);
				map.put("mHead", null);
			}
			map.put("price", (double)logList.get(0).getPrice()/100);
			map.put("addTime", logList.get(0).getAddTime());
			
			Date date = new Date();
			Long timeStamp = logList.get(0).getAddTime().getTime()+30*1000;
			Long curTimeStamp = date.getTime();
			if(timeStamp>curTimeStamp){
				thisTime = timeStamp - curTimeStamp;
			}
			int second = (int)thisTime/1000;
			map.put("countDown", second);
			
			if(logList.get(0).getmId().equals(member.getId())){
				if(timeStamp>curTimeStamp){
					map.put("isGet", 0); //未中标
					map.put("payStatus", 0); //未支付
				}else{
					map.put("isGet", 1); //中标
					if(logList.get(0).getPayStatus()==2){
						map.put("payStatus", 1);//已支付
					}else{
						map.put("payStatus", 0);
					}
				}
			}else{
				map.put("isGet", 0); 
				map.put("payStatus", 0);
			}
			
			if(logList.get(0).getOrderNo()!=null){
				Order order = orderMapper.getOrderByOrderNo(logList.get(0).getOrderNo());
				if(order!=null){
					map.put("orderId", order.getId());
				}else{
					map.put("orderId", null);
				}
			}else{
				map.put("orderId", null);
			}
			
		}else{
			map.put("mId", null);
			map.put("mNick", null);
			map.put("mHead", null);
			map.put("price", null);
			map.put("addTime", null);
			map.put("countDown", null);
			map.put("isGet", 0); 
			map.put("payStatus", 0);
			map.put("orderId", null);
		}
		goods.setDauctionDetail(map);
		return goods;
	}

	//经纬度获取地址
	public Address apiAddress(String lat,String log)throws Exception{
		String address=GetLatitude.getAdd(log, lat); //根据经纬度获取地址
		Address address1=new Address();
		JSONObject jsonObject = JSONObject.fromObject(address);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		String allAdd = j_2.getString("admName");
		String arr[] = allAdd.split(",");
		
		if(arr[0].substring(0, 2).equals("北京")||arr[0].substring(0, 2).equals("天津")||arr[0].substring(0, 2).equals("上海")||arr[0].substring(0, 2).equals("重庆")) {
			GoAddressArea goAddressArea=goAddressAreaMapper.selectProv(arr[1].substring(0, 2));
			
			String name1=arr[2].substring(0, 2);
			Integer parentId1=goAddressArea.getId();
			GoAddressArea goAddressArea1=goAddressAreaMapper.selectArea(name1,parentId1);
			String address2=goAddressArea.getName()+" "+goAddressArea1.getName();
			address1.setAddress(address2);   
			address1.setProv(0);
			address1.setCity(goAddressArea.getId());
			address1.setArea(goAddressArea1.getId());
		}else {
			GoAddressArea goAddressArea=goAddressAreaMapper.selectProv(arr[0].substring(0, 2));

			String name=arr[1].substring(0, 2);
			Integer parentId=goAddressArea.getId();
			GoAddressArea goAddressArea1=goAddressAreaMapper.selectCity(name,parentId);
			
			String name1=arr[2].substring(0, 2);
			Integer parentId1=goAddressArea1.getId();
			GoAddressArea goAddressArea2=goAddressAreaMapper.selectArea(name1,parentId1);
			String address2=goAddressArea.getName()+" "+goAddressArea1.getName()+" "+goAddressArea2.getName();
			address1.setAddress(address2);   
			address1.setProv(goAddressArea.getId());
			address1.setCity(goAddressArea1.getId());
			address1.setArea(goAddressArea2.getId());
		}
		return address1;
	}
	
	/*(优化后的商品详情)移动端商品详情*/
	public MyGoodsDetail apiGoodsDetailOptimize(Integer id, Member member, String tgId)throws Exception{
		
		MyGoodsDetail myGoodsDetail=new MyGoodsDetail();
		//Goods goods=mapper.selectByPrimary(id);
		Goods goods  = mapper.selectGoodsDetail(id);
		List<com.bh.goods.pojo.ActZone> actZoneList=actZoneMapper.selectByGoodsId(id);
		if(actZoneList!=null&&actZoneList.size()>0&&Integer.parseInt(goods.getDeductibleRate())>0){
			myGoodsDetail.setIsAct("1");
		}else if(actZoneList.size()==0&&Integer.parseInt(goods.getDeductibleRate())>0){
			myGoodsDetail.setIsAct("0");
		}else {
			myGoodsDetail.setIsAct("2");
		}
		myGoodsDetail.setBhdPrice(MoneyUtil.fen2Yuan(goods.getBhdPrice())+"");
		myGoodsDetail.setScore(goods.getScore());
		//折扣率
		myGoodsDetail.setDeductibleRate(goods.getDeductibleRate());
		//商品的id
		myGoodsDetail.setId(goods.getId());
		//该商品是否是京东商品
		myGoodsDetail.setIsJd(goods.getIsJd());
		//商品类型，0-普通商品，1砍价，2秒杀，3抽奖，4超级滨惠豆，5惠省钱，6荷兰拍卖
		myGoodsDetail.setTopicType(goods.getTopicType());
		//销售模式 1单卖 2拼团单卖皆可 3只拼团
		myGoodsDetail.setSaleType(goods.getSaleType());
		//商品的名称
		myGoodsDetail.setName(goods.getName());
		//拼团人数
		myGoodsDetail.setTeamNum(goods.getTeamNum());
		//商品的图片
		myGoodsDetail.setImage(goods.getImage());
		//店铺的id
		myGoodsDetail.setShopId(goods.getShopId());
		//商品状态 0正常 1已删除 2下架 3申请上架4拒绝5上架
		myGoodsDetail.setStatus(goods.getStatus());
		
		//商品标签 1奢侈品牌大折扣 2大牌好货 3超级滨惠豆 4节假日活动 5周末大放送
		myGoodsDetail.setTagName(goods.getTagName());
		
		
		try {
            Set<Integer> goodsIdSet = new HashSet<>();
            goodsIdSet.add(goods.getId());
            List<Integer> isList = actZoneGoodsMapper.selectIsCart(goodsIdSet);
            if (isList != null && isList.size() > 0) {
            	StringBuffer sb=new StringBuffer();
        		boolean first = true;
        		///第一个前面不拼接","
    			for (Integer string : isList) {
    				if(first) {
    			         first=false;
    			      }else{
    			    	  sb.append(",");
    			      }
    				sb.append(string);
    			}
    			String str=sb.toString();
    			//只要有一个不能加入购物车,整个就不能加入购物车
    			if (str.contains("0")) {
    				myGoodsDetail.setTagName("3");
				}
            }
        } catch (Exception e) {
			e.printStackTrace();
		}
		//商品的标签(比如假一赔十,正品保证,极速发货,七天可退等)
		if(!StringUtils.isEmptyOrWhitespaceOnly(goods.getTagIds())){
			StringBuffer buffer = new StringBuffer();
			String[] tagIds = goods.getTagIds().split(",");
			for(int i=0; i<tagIds.length; i++){
				GoodsTag tag = tagMapper.selectByPrimaryKey(Integer.parseInt(tagIds[i]));
				if(tag!=null){
					buffer.append(tag.getName()+",");
				}
			}
			if(buffer.toString().length()>0){
				String[] tagIdsValue = buffer.toString().substring(0, buffer.toString().length()-1).split(",");
				myGoodsDetail.setTagIdsValue(tagIdsValue);
			}
		}
		
		//单卖或者团购商品的销量统计
		int groupCount = 0; 
		if (goods.getSaleType()==1) {
			groupCount=goods.getSale()+goods.getFixedSale();
		}else{
			String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
			if(countT!=null){
				groupCount = Integer.parseInt(countT)+goods.getFixedSale();
			}else{
				groupCount = goods.getFixedSale();
			}
		}
		myGoodsDetail.setGroupCount(groupCount);
		
		//商品详情描述
		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(id, 0);
		if(desc!=null){
			myGoodsDetail.setAppdescription(desc.getDescription());
		}
		
		//店铺的基本信息(包含店铺名称,商品总数,已拼多少件,店铺logo等)
		Map<String, Object> shopMap = getShopInfo(goods.getShopId()); //店铺基本信息
		if(shopMap.size()>0){
			myGoodsDetail.setShopInfo(shopMap);
		}
		
		//商品模型
		ItemModelValue modelValue = new ItemModelValue(); 
		modelValue.setGoodsId(id);
		List<ItemModelValue> modelValueList = modelValueMapper.selectByGoodsId(modelValue);
		if(modelValueList.size()>0){
			if(modelValueList.get(0)!=null){
				JSONArray array = JSONArray.fromObject(modelValueList.get(0).getParamData()); //value转义
				myGoodsDetail.setModelValue(array);
			}
		}
		//获取商品的Sku信息
		//List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(id); //商品规格属性
		List<GoodsSku> skuList=skuMapper.selectGoodsSkuList(id);
		List<MyGoodsSku> myGoodsSkuList=new ArrayList<>();
		if(skuList.size()>0){
			for(GoodsSku goodsSku : skuList){
				MyGoodsSku myGoodsSku=new MyGoodsSku();
				myGoodsSku.setId(goodsSku.getId());
				myGoodsSku.setRealPrice(MoneyUtil.fen2Yuan(goodsSku.getSellPrice()+""));
				myGoodsSku.setMarketRealPrice(MoneyUtil.fen2Yuan(goodsSku.getMarketPrice()+""));
				myGoodsSku.setStoreNums(goodsSku.getStoreNums());
				if(goodsSku.getTeamPrice()!=null){
					myGoodsSku.setRealTeamPrice(MoneyUtil.fen2Yuan(goodsSku.getTeamPrice()+""));
				}
				JSONObject jsonObject = JSONObject.fromObject(goodsSku.getValue()); //value转义
				myGoodsSku.setValueObj(jsonObject);
				myGoodsSku.setKeyOne(goodsSku.getKeyOne());
				myGoodsSku.setValueOne(goodsSku.getValueOne());
				myGoodsSku.setKeyTwo(goodsSku.getKeyTwo());
				myGoodsSku.setValueTwo(goodsSku.getValueTwo());
				myGoodsSku.setKeyThree(goodsSku.getKeyThree());
				myGoodsSku.setValueThree(goodsSku.getValueThree());
				myGoodsSku.setKeyFour(goodsSku.getKeyFour());
				myGoodsSku.setValueFour(goodsSku.getValueFour());
				myGoodsSku.setKeyFive(goodsSku.getKeyFive());
				myGoodsSku.setValueFive(goodsSku.getValueFive());
				myGoodsSkuList.add(myGoodsSku);
			}
		}
		myGoodsDetail.setSkuList(myGoodsSkuList);
		
		
		if(member!=null){
			List<MemberUserAccessLog> logList = accessLogMapper.getBymIdAndGoodsId(member.getId(), id);
			if(logList.size()==0){
				MemberUserAccessLog accessLog = new MemberUserAccessLog(); //新增浏览记录
				accessLog.setAddtime(new Date());
				accessLog.setGoodsId(id);
				accessLog.setmId(member.getId());
				accessLogMapper.insertSelective(accessLog);
			}else{
				//程凤云2018-4-8如果已经存在，则将时间改变
				MemberUserAccessLog log1 = new MemberUserAccessLog();
				log1.setId(logList.get(0).getId());
				log1.setAddtime(new Date());
				accessLogMapper.updateByPrimaryKeySelective(log1);
			}
		}
		
		
		
		goods.setVisit(goods.getVisit()+1);//浏览次数+1
		mapper.updateByPrimaryKeySelective(goods);
		
		return myGoodsDetail;
	}

	
	
	
	public void addShareScore(Integer mId) throws Exception{
		//获取签到规则： 1，签到 2,浇水 3,拼单 4单买 5,分享,6注册积分，7购物积分
		SeedScoreRule se = new SeedScoreRule();
		se.setScoreAction(5);
		List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
		if (rule.size() > 0) {
			//状态 0关 1开
			if (rule.get(0).getStatus().equals(1)) {
				//分享人Id
				MemberUser memberUser1 = memberUserMapper.selectByPrimaryKey(mId);
				if (memberUser1 !=null) {
					MemberUser u1 = new MemberUser();
					u1.setmId(memberUser1.getmId());
					Integer point = memberUser1.getPoint() + rule.get(0).getScore();
					u1.setPoint(point);
					memberUserMapper.updatePointBymId(u1);
					
					MemerScoreLog  log=new MemerScoreLog();
					log.setCreateTime(new Date());
					log.setmId(memberUser1.getmId());
					log.setIsDel(0);
					log.setSmId(-3);
					log.setSsrId(1);
					log.setScore(rule.get(0).getScore());
					log.setOrderseedId(0);
					memerScoreLogMapper.insertSelective(log);
				}
			}
		}
	}
	

	
	

	

}
