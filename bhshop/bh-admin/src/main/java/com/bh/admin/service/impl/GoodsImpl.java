package com.bh.admin.service.impl;

import com.bh.admin.mapper.goods.*;
import com.bh.admin.mapper.order.*;
import com.bh.admin.mapper.user.*;
import com.bh.admin.pojo.goods.*;
import com.bh.admin.pojo.order.*;
import com.bh.admin.pojo.user.*;
import com.bh.admin.service.GoodsService;
import com.bh.admin.util.GoodsSaleAllot;
import com.bh.admin.vo.ShareResult;
import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.bean.goods.SellPriceResult;
import com.bh.result.BhResult;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.SmsUtil;
import com.control.file.upload;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class GoodsImpl implements GoodsService {
	private static final Logger logger = LoggerFactory.getLogger(GoodsImpl.class);
	@Autowired
	private GoodsMapper mapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsDescMapper descMapper;
	@Autowired
	private GoodsImageMapper imageMapper;
	@Autowired
	private GoodsCategoryMapper categoryMapper;
	@Autowired
	private GoodsAttrMapper attrMapper;
	@Autowired
	private GoodsSkuMapper skuMapper;
	@Autowired
	private MemberUserAccessLogMapper accessLogMapper;
	@Autowired
	private MemberShopMapper shopMapper;
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
	private OrderSendInfoMapper orderSendInfoMapper;
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
	private HollandDauctionMapper hollandDauctionMapper;
	@Autowired
	private JdGoodsMapper jdGoodsMapper;
	@Autowired
	private ActZoneGoodsMapper actZoneGoodsMapper;
	@Autowired
	private GoodsPriceApprovalMapper goodsPriceApprovalMapper;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;


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
	public PageBean<Goods> selectByShopid(int shopId, String name, String currentPage, int pageSize, String categoryId,
			String saleType, String status, String startPrice, String endPrice, String topicType, String id,
			String skuNo, String jdSkuNo, String isJd, String isHotShop, String isNewShop) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<Goods> list = mapper.selectShopGoodsList(shopId, name, categoryId, saleType, status, startPrice, endPrice,
				topicType, id, skuNo, jdSkuNo, isJd, isHotShop, isNewShop);
		if (list.size() > 0) {
			for (Goods goods : list) {
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
				if (skuList.size() > 0) {
					for (int i = 0; i < skuList.size(); i++) {
						// zlk 审核的价格显示
						GoodsPriceApproval gpa = new GoodsPriceApproval();
						gpa.setGoodsSkuId(skuList.get(i).getId());
						List<GoodsPriceApproval> gp = goodsPriceApprovalMapper.getByGoodsSkuId(gpa);
						for (int j = 0; j < gp.size(); j++) {
							if (gp.get(j).getReplyNo().equals("1")) {
								skuList.get(i).setNewMarketPrice(MoneyUtil.fen2Yuan(gp.get(j).getNewVal()));// 价格“分”转化成“元”
							} else if (gp.get(j).getReplyNo().equals("2")) {
								skuList.get(i).setNewSellPrice(MoneyUtil.fen2Yuan(gp.get(j).getNewVal()));// 价格“分”转化成“元”
							} else if (gp.get(j).getReplyNo().equals("3")) {
								skuList.get(i).setNewTeamPrice(MoneyUtil.fen2Yuan(gp.get(j).getNewVal()));// 价格“分”转化成“元”
							} else if (gp.get(j).getReplyNo().equals("4")) {
								skuList.get(i).setNewStockPrice(MoneyUtil.fen2Yuan(gp.get(j).getNewVal()));// 价格“分”转化成“元”
							} else if (gp.get(j).getReplyNo().equals("5")) {
								skuList.get(i).setNewDeliveryPrice(MoneyUtil.fen2Yuan(gp.get(j).getNewVal()));// 价格“分”转化成“元”
							}
						}

						double realSkuPrice = (double) skuList.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
						skuList.get(i).setRealPrice(realSkuPrice);
						double marketRealPrice = (double) skuList.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
						skuList.get(i).setMarketRealPrice(marketRealPrice);

						double realJdPrice = (double) skuList.get(i).getJdPrice() / 100; // 京东价转化成“元”
						skuList.get(i).setRealJdPrice(realJdPrice);

						double realDeliveryPrice = (double) skuList.get(i).getDeliveryPrice() / 100; // 物流价转化成“元”
						skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);

						double realJdBuyPrice = (double) skuList.get(i).getJdBuyPrice() / 100; // 客户购买价“分”转化成“元”
						skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);

						double realJdOldBuyPrice = (double) skuList.get(i).getJdOldBuyPrice() / 100; // 客户购买价(旧)“分”转化成“元”
						skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);

						double realJdProtocolPrice = (double) skuList.get(i).getJdProtocolPrice() / 100; // 价格“分”转化成“元”
						skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);

						double realStockPrice = (double) skuList.get(i).getStockPrice() / 100; // 进货价
						skuList.get(i).setRealStockPrice(realStockPrice);

						if (skuList.get(i).getTeamPrice() != null) {
							double realTPrice = (double) skuList.get(i).getTeamPrice() / 100;// 团购价格“分”转化成“元”
							skuList.get(i).setRealTeamPrice(realTPrice);
						}
						if (skuList.get(i).getAuctionPrice() != null) {
							double realAuctionPrice = (double) skuList.get(i).getAuctionPrice() / 100;// 拍卖价格“分”转化成“元”
							skuList.get(i).setRealAuctionPrice(realAuctionPrice);
						}
						JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); // value转义
						skuList.get(i).setValueObj(jsonObject);
					}
					if (!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyOne())) {
						goods.setKeyOne(skuList.get(0).getKeyOne());
					}
					if (!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyTwo())) {
						goods.setKeyTwo(skuList.get(0).getKeyTwo());
					}
					if (!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyThree())) {
						goods.setKeyThree(skuList.get(0).getKeyThree());
					}
					goods.setSkuList(skuList);
				}
				double realPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				double marketRealPrice = (double) goods.getMarketPrice() / 100;// 市场价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);

				if (goods.getSaleType() == 1) {

				} else {
					String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
					if (countT != null) {
						goods.setSale(Integer.parseInt(countT));
					} else {
						goods.setSale(0);
					}
				}

				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if (category != null) {
					goods.setCategory(category.getName());
				}

				// int sale = goods.getSale()+goods.getFixedSale();//销量
				// goods.setSale(sale);

				if (goods.getStatus() == 0) {
					goods.setGoodsStatus("正常");
				}
				if (goods.getStatus() == 1) {
					goods.setGoodsStatus("已删除");
				}
				if (goods.getStatus() == 2) {
					goods.setGoodsStatus("下架");
				}
				if (goods.getStatus() == 3) {
					goods.setGoodsStatus("申请上架");
				}
				if (goods.getStatus() == 4) {
					goods.setGoodsStatus("拒绝");
				}
			}
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}

	/**
	 * 商品详情
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Goods selectBygoodsId(Integer id) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(id);
		double realPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
		goods.setRealPrice(realPrice);

		double realMarkPrice = (double) goods.getMarketPrice() / 100; // 市场价格“分”转化成“元”
		goods.setMarkRealPrice(realMarkPrice);

		if (goods.getTeamPrice() != null) {
			double realTeamPrice = (double) goods.getTeamPrice() / 100; // 团购价格“分”转化成“元”
			goods.setRealTeamPrice(realTeamPrice);
		}
		
		double deliveryRealPrice = (double) goods.getDeliveryPrice() / 100; // 物流价格“分”转化成“元”
		goods.setDeliveryRealPrice(deliveryRealPrice);
		GoodsDesc goodsDesc = descMapper.selectByGoodsIdAndIsPc(id, 1);
		if (goodsDesc != null) {
			goods.setDescription(goodsDesc.getDescription());
		}

		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(id, 0);
		if (desc != null) { // 商品详情描述
			goods.setAppdescription(desc.getDescription());
		}

		GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				
		if (category!= null) {
			goods.setCategory(category.getName()); // 分类名称
			GoodsCategory categoryOne = categoryMapper.selectByPrimaryKey(goods.getCatIdOne());
			GoodsCategory categoryTwo = categoryMapper.selectByPrimaryKey(goods.getCatIdTwo());
			List<GoodsCategory> categoryThereList = new ArrayList<GoodsCategory>();
			categoryThereList.add(category);
			List<GoodsCategory> categoryTwoList = new ArrayList<GoodsCategory>();
			categoryTwoList.add(categoryTwo);
			List<GoodsCategory> categoryOneList = new ArrayList<GoodsCategory>();
			categoryOneList.add(categoryOne);
			categoryTwo.setChildList(categoryThereList);
			categoryOne.setChildList(categoryTwoList);
			goods.setCategoryList(categoryOneList);
		}
		int sale = goods.getSale() + goods.getFixedSale();// 销量
		goods.setSale(sale);

		GoodsBrand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
		if (brand != null) {
			goods.setBrandName(brand.getName()); // 品牌名称
		}

		List<GoodsAttr> attrList = attrMapper.selectAllByGoodsId(id);
		goods.setAttrList(attrList);

		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(id);
		for (int i = 0; i < skuList.size(); i++) {
			double realSkuPrice = (double) skuList.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
			skuList.get(i).setRealPrice(realSkuPrice);

			double realDeliveryPrice = (double) skuList.get(i).getDeliveryPrice() / 100; // 价格“分”转化成“元”
			skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);

			double marketRealPrice = (double) skuList.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
			skuList.get(i).setMarketRealPrice(marketRealPrice);

			double realJdPrice = (double) skuList.get(i).getJdPrice() / 100; // 京东价转化成“元”
			skuList.get(i).setRealJdPrice(realJdPrice);

			double realJdBuyPrice = (double) skuList.get(i).getJdBuyPrice() / 100; // 客户购买价“分”转化成“元”
			skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);

			double realJdOldBuyPrice = (double) skuList.get(i).getJdOldBuyPrice() / 100; // 客户购买价(旧)“分”转化成“元”
			skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);

			double realJdProtocolPrice = (double) skuList.get(i).getJdProtocolPrice() / 100; // 价格“分”转化成“元”
			skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);

			double realStockPrice = (double) skuList.get(i).getStockPrice() / 100; // 进货价
			skuList.get(i).setRealStockPrice(realStockPrice);

			if (skuList.get(i).getTeamPrice() != null) {
				double realTPrice = (double) skuList.get(i).getTeamPrice() / 100;// 团购价格“分”转化成“元”
				skuList.get(i).setRealTeamPrice(realTPrice);
			}
			if (skuList.get(i).getAuctionPrice() != null) {
				double realAuctionPrice = (double) skuList.get(i).getAuctionPrice() / 100;// 拍卖价格“分”转化成“元”
				skuList.get(i).setRealAuctionPrice(realAuctionPrice);
			}

			JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); // value转义
			skuList.get(i).setValueObj(jsonObject);
		}
		goods.setSkuList(skuList);

		ItemModelValue modelValue = new ItemModelValue(); // 查询商品模型
		modelValue.setGoodsId(id);
		List<ItemModelValue> modelValueList = modelValueMapper.selectByGoodsId(modelValue);
		if (modelValueList.size() > 0) {
			if(org.apache.commons.lang.StringUtils.isNotBlank(modelValueList.get(0).getParamData())){
				JSONArray array = JSONArray.fromObject(modelValueList.get(0).getParamData()); // value转义
				goods.setModelValue(array);
			}
		}

		// zlk 根据shop_cat_id 获取店铺分类名字
		String shopCategoryName = null;
		GoodsShopCategory goodsShopCategory = goodsShopCategoryMapper.selectByPrimaryKey(goods.getShopCatId());
		if (goodsShopCategory != null) {
			shopCategoryName = goodsShopCategory.getName();
		}
		goods.setShopCategoryName(shopCategoryName); // 店铺分类名字
		return goods;
	}


	@Override
	public int insertToModel(Goods goods) throws Exception {
		return mapper.insertSelective(goods);
	}

	/**
	 * 商品的新增
	 *
	 * @param name
	 *            商品名称
	 * @param title
	 *            商品标题
	 * @param modelId
	 *            模型id
	 * @param catId
	 *            分类id
	 * @param shopCatId
	 *            店铺分类
	 * @param brandId
	 *            品牌id
	 * @param sellPrice
	 *            销售价格单位分
	 * @param marketPrice
	 *            市场价格单位分
	 * @param storeNums
	 *            库存
	 * @param unit
	 *            单位
	 * @param description
	 *            详细内容
	 * @param url
	 *            图片地址
	 * @param image
	 *            主图
	 * @return
	 * @throws Exception
	 */
	@Override
	// @Transactional(rollbackFor=Exception.class)
	public int insertGoods(String name, String title, String modelId, String catId, String shopCatId, String brandId,
			String sellPrice, String marketPrice, String storeNums, String description, String image, JSONArray list,
			String deliveryPrice, String isShopFlag, String isNew, String refundDays, String publicImage,
			String saleType, String teamNum, String teamEndTime, String isPromote, String timeUnit, String teamPrice,
			String isCreate, String isJd, String appintroduce, String topicType, int shopId, String tagIds,
			String isPopular, String catIdOne, String catIdTwo, String goodBuyLimit, String sendArea,String userId,String deductibleRate) throws Exception {

		Goods goods = new Goods();
		Timestamp now = new Timestamp(new Date().getTime());
		
		goods.setAddtime(now);
		goods.setEdittime(now);
		goods.setShopId(shopId);
		if (!StringUtils.isEmptyOrWhitespaceOnly(deductibleRate)) {//折扣率
			goods.setDeductibleRate(deductibleRate);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(name)) {// 商品名称
			goods.setName(name);
		}
		// 218-3-15 是否拼人气， -1否 0是
		if (org.apache.commons.lang.StringUtils.isEmpty(isPopular)) {
			goods.setIsPopular(-1);
		} else {
			goods.setIsPopular(Integer.parseInt(isPopular));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(title)) {// 商品标题
			goods.setTitle(title);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(modelId)) {// 模型id
			goods.setModelId(Integer.parseInt(modelId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(catId)) {// 分类id
			goods.setCatId(Long.parseLong(catId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(shopCatId)) {// 所属店铺id
			goods.setShopCatId(Integer.parseInt(shopCatId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(brandId)) {// 品牌id
			goods.setBrandId(Long.parseLong(brandId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)) {// 销售价
			Integer realPrice = (int) (MoneyUtil.yuan2Fen(sellPrice));
			goods.setSellPrice(realPrice);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)) {// 市场价
			Integer mPrice = (int) (MoneyUtil.yuan2Fen(marketPrice));
			goods.setMarketPrice(mPrice);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(storeNums)) {// 库存
			goods.setStoreNums(Integer.parseInt(storeNums));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(image)) {
			goods.setImage(image);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(refundDays)) {
			goods.setRefundDays(Integer.parseInt(refundDays));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(publicImage)) {
			goods.setPublicimg(publicImage);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(saleType)) {
			goods.setSaleType(Integer.parseInt(saleType));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(teamNum)) {
			goods.setTeamNum(Integer.parseInt(teamNum));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(teamEndTime)) {
			goods.setTeamEndTime(Integer.parseInt(teamEndTime));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isPromote)) {
			goods.setIsPromote(Integer.parseInt(isPromote));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(timeUnit)) {
			goods.setTimeUnit(Integer.parseInt(timeUnit));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(teamPrice)) {// 团购价
			Integer realTeamPrice = (int) (MoneyUtil.yuan2Fen(teamPrice));
			goods.setTeamPrice(realTeamPrice);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isCreate)) {
			goods.setIsCreate(Integer.parseInt(isCreate));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isJd)) {
			goods.setIsJd(Integer.parseInt(isJd));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(catIdOne)) {
			goods.setCatIdOne(Long.parseLong(catIdOne));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(catIdTwo)) {
			goods.setCatIdTwo(Long.parseLong(catIdTwo));
		}
		
		if(catId!=null&&!"".equals(catId)) {
			GoodsCategory goodsCategory=null;
			goodsCategory=goodsCategoryMapper.selectByReid(Long.parseLong(catId));
			goods.setCatIdTwo(goodsCategory.getReid());
			goodsCategory=goodsCategoryMapper.selectByReid(goodsCategory.getReid());
			goods.setCatIdOne(goodsCategory.getReid());
		}
			
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(isShopFlag)) {
			if (Integer.parseInt(isShopFlag) == 1) {
				goods.setIsShopFlag(true);
			} else {
				goods.setIsShopFlag(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isNew)) {
			if (Integer.parseInt(isNew) == 1) {
				goods.setIsNew(true);
			} else {
				goods.setIsNew(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(deliveryPrice)) { // 物流价
			Integer realDeliveryPrice = (int) (MoneyUtil.yuan2Fen(deliveryPrice));
			goods.setDeliveryPrice(realDeliveryPrice);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(topicType)) {
			goods.setTopicType(Integer.parseInt(topicType));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(tagIds)) {
			goods.setTagIds(tagIds);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(goodBuyLimit)) {
			goods.setGoodBuyLimit(Integer.valueOf(goodBuyLimit)); // 商品限购数量
		}
		if (org.apache.commons.lang.StringUtils.isNotBlank(sendArea)) {
			goods.setSendArea(sendArea); // zlk 配送区域
		} else {
			goods.setSendArea("0"); // zlk 配送区域
		}
		int row = mapper.insertSelective(goods); // 插入商品
		Integer id=goods.getId();
		GoodsOperLog goodsOperLog = new GoodsOperLog();
		goodsOperLog.setOpType("商家新增商品");
		goodsOperLog.setUserId(userId.toString());
		goodsOperLog.setOrderId("");
		String userName = memberShopMapper.selectUsernameBymId(Integer.parseInt(userId)); //查找操作人
		goodsOperLog.setAdminUser(userName);
		goodsOperLogMapper.insertGoodsById(id, goodsOperLog);
		GoodsDesc goodsDesc = new GoodsDesc(); // 插入商品详细描述
		goodsDesc.setIsPc(1);
		goodsDesc.setGoodsId(goods.getId());
		if (!StringUtils.isEmptyOrWhitespaceOnly(description)) {
			goodsDesc.setDescription(description);
		}
		int rowD = descMapper.insertGoodsDesc(goodsDesc);

		if (rowD == 1) {
			GoodsDesc desc = new GoodsDesc();
			desc.setIsPc(0);
			desc.setGoodsId(goods.getId());
			if (!StringUtils.isEmptyOrWhitespaceOnly(appintroduce)) {
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

		/*
		 * Iterator<Object> is = url.iterator(); while (is.hasNext()) { GoodsImage
		 * goodsImage = new GoodsImage(); JSONObject ob = (JSONObject) is.next();
		 * goodsImage.setGoodsId(goods.getId()); goodsImage.setUrl(ob.getString("url"));
		 * imageMapper.insertSelective(goodsImage); }
		 */
		if (goods.getId() != null) {
			return goods.getId();
		} else {
			return row;
		}
	}

	/**
	 * 商品的更新
	 *
	 * @param name
	 *            商品名称
	 * @param title
	 *            商品标题
	 * @param modelId
	 *            模型id
	 * @param catId
	 *            分类id
	 * @param shopCatId
	 *            店铺分类
	 * @param brandId
	 *            品牌id
	 * @param sellPrice
	 *            销售价格单位分
	 * @param marketPrice
	 *            市场价格单位分
	 * @param storeNums
	 *            库存
	 * @param description
	 *            详细内容
	 * @param url
	 *            图片地址
	 * @param image
	 *            主图
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateGoods(String id, String name, String title, String modelId, String catId, String shopCatId,
			String brandId, String sellPrice, String marketPrice, String storeNums, String description, String image,
			JSONArray list, String deliveryPrice, String isHot, String isFlag, String sortnum, String isNew,
			String refundDays, String publicImage, String saleType, String teamNum, String teamEndTime,
			String isPromote, String timeUnit, String teamPrice, String isCreate, String appintroduce, String topicType,
			String tagIds, String catIdOne, String catIdTwo, String goodBuyLimit, String sendArea,String deductibleRate) throws Exception {

		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		if (!StringUtils.isEmptyOrWhitespaceOnly(name)) {
			goods.setName(name);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(title)) {
			goods.setTitle(title);
		}
		if ((org.apache.commons.lang.StringUtils.isNotEmpty(modelId)) && (!modelId.equals("null"))) {
			goods.setModelId(Integer.parseInt(modelId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(catId)) {
			goods.setCatId(Long.parseLong(catId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(shopCatId) && !shopCatId.equals("null")) {
			goods.setShopCatId(Integer.parseInt(shopCatId));
		}
		if ((!StringUtils.isEmptyOrWhitespaceOnly(brandId)) && (!brandId.equals("null"))) {
			goods.setBrandId(Long.parseLong(brandId));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)) {
			Integer realPrice = (int) (MoneyUtil.yuan2Fen(sellPrice));
			goods.setSellPrice(realPrice);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)) {
			Integer mPrice = (int) (MoneyUtil.yuan2Fen(marketPrice));
			goods.setMarketPrice(mPrice);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(storeNums)) {
			goods.setStoreNums(Integer.parseInt(storeNums));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(image)) {
			goods.setImage(image);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(sortnum)) {
			goods.setSortnum(Short.parseShort(sortnum));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(publicImage)) {
			goods.setPublicimg(publicImage);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(saleType)) {
			goods.setSaleType(Integer.parseInt(saleType));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(teamNum)) {
			goods.setTeamNum(Integer.parseInt(teamNum));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(teamEndTime)) {
			goods.setTeamEndTime(Integer.parseInt(teamEndTime));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isPromote)) {
			goods.setIsPromote(Integer.parseInt(isPromote));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(timeUnit)) {
			goods.setTimeUnit(Integer.parseInt(timeUnit));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(teamPrice)) {// 团购价
			Integer realTeamPrice = (int) (MoneyUtil.yuan2Fen(teamPrice));
			goods.setTeamPrice(realTeamPrice);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isCreate)) {
			goods.setIsCreate(Integer.parseInt(isCreate));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(catIdOne)) {
			goods.setCatIdOne(Long.parseLong(catIdOne));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(catIdTwo)) {
			goods.setCatIdTwo(Long.parseLong(catIdTwo));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isHot)) {
			if (Integer.parseInt(isHot) == 1) {
				goods.setIsHot(true);
			} else {
				goods.setIsHot(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(refundDays)) {
			goods.setRefundDays(Integer.parseInt(refundDays));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isNew)) {
			if (Integer.parseInt(isNew) == 1) {
				goods.setIsNew(true);
			} else {
				goods.setIsNew(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isFlag)) {
			if (Integer.parseInt(isFlag) == 1) {
				goods.setIsFlag(true);
			} else {
				goods.setIsFlag(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(deliveryPrice)) { // 物流价
			Integer realDeliveryPrice = (int) (MoneyUtil.yuan2Fen(deliveryPrice));
			goods.setDeliveryPrice(realDeliveryPrice);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(topicType)) {
			goods.setTopicType(Integer.parseInt(topicType));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(deductibleRate)) {
			goods.setDeductibleRate(deductibleRate);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(tagIds)) {
			goods.setTagIds(tagIds);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(goodBuyLimit)) {
			goods.setGoodBuyLimit(Integer.valueOf(goodBuyLimit));
		}
		// 2018.23 zlk 配送区域
		if (org.apache.commons.lang.StringUtils.isNotBlank(sendArea)) {
			goods.setSendArea(sendArea);
		} else {
			goods.setSendArea("0");
		}
		// end
		int row = mapper.updateByPrimaryKeySelective(goods); // 更新商品

		/*
		 * if(goods.getCatId()!=Long.parseLong(catId)){ GoodsCategroyJd catJd =
		 * catJdMapper.selectByPrimaryKey(Integer.parseInt(id)); if(catJd!=null){
		 * catJd.setMyCatId(Long.parseLong(catId));
		 * catJdMapper.updateByPrimaryKeySelective(catJd); } }
		 */

		GoodsDesc goodsDesc = descMapper.selectByGoodsIdAndIsPc(Integer.parseInt(id), 1);
		if (goodsDesc != null) {
			if (!StringUtils.isEmptyOrWhitespaceOnly(description)) {
				goodsDesc.setDescription(description);
				descMapper.updateByGoodsIdAndIsPc(goodsDesc);// 更新商品详细描述
			}
		} else {
			GoodsDesc de = new GoodsDesc();
			de.setDescription(description);
			de.setGoodsId(Integer.parseInt(id));
			descMapper.insertGoodsDesc(de);// 插入商品详细描述
		}

		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(Integer.parseInt(id), 0);
		if (desc != null) {
			if (!StringUtils.isEmptyOrWhitespaceOnly(appintroduce)) {
				desc.setDescription(appintroduce);
			}
			descMapper.updateByGoodsIdAndIsPc(desc);// 更新商品详细描述
		} else {
			GoodsDesc ds = new GoodsDesc();
			ds.setDescription(appintroduce);
			ds.setGoodsId(Integer.parseInt(id));
			descMapper.insertGoodsDesc(ds);// 插入商品详细描述
		}

		if (list != null) {
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
				JSONObject ob = (JSONObject) it.next();
				if (ob.getString("id") != null && !ob.getString("id").equals("undefined")) {
					GoodsAttr attr = attrMapper.selectByPrimaryKey(ob.getInt("id"));
					attr.setAttrId(ob.getInt("attr_id"));
					attr.setAttrValue(ob.getString("attr_value"));
					attr.setModelId(ob.getInt("model_id"));
					attrMapper.updateByPrimaryKeySelective(attr);
				} else {
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
	 *
	 * @param id
	 *            商品id
	 * @param status
	 *            商品状态
	 * @return
	 */
	@Override
	public int updateStatus(String id, Integer status) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		goods.setStatus(status);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		return mapper.updateByPrimaryKeySelective(goods);
	}

	/**
	 * 商品申请上架
	 *
	 * @param id
	 *            商品id
	 * @param status
	 *            商品状态
	 * @return
	 */
	@Override
	public int goodsPutaway(String id, Integer status,String userId) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		GoodsOperLog goodsOperLog = new GoodsOperLog();
		goodsOperLog.setOpType("商家商品申请上架");
		goodsOperLog.setUserId(userId);
		goodsOperLog.setOrderId("");
		String userName = memberShopMapper.selectUsernameBymId(Integer.parseInt(userId)); //查找操作人
		goodsOperLog.setAdminUser(userName);
		goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
		goods.setStatus(status);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setApplyTime(now);
		if (status == 5) {// 直接上架跳过审核
			goods.setUpTime(now);
			if (goods.getTopicType() == 6) { // 荷兰拍卖商品，上架设置当前期流拍时间
				HollandDauction entity = new HollandDauction();
				entity.setGoodsId(goods.getId());
				HollandDauction hd = hollandDauctionMapper.getListByGoodsId(goods.getId());
				if (hd != null) {
					int middle = hd.getDauctionPrice() - hd.getLowPrice();
					int remainder = middle % hd.getScopePrice(); // 余数
					int result = middle / hd.getScopePrice(); // 去余数结果
					int num = remainder == 0 ? result + 1 : result + 2;
					Long thisTimeStamp = (long) (num * hd.getTimeSection() * 60000);
					Long reusltTimeStamp = now.getTime() + thisTimeStamp;
					Date date = new Date(reusltTimeStamp);
					hd.setLoseTime(date);
					hd.setStartTime(now);
					hollandDauctionMapper.updateByPrimaryKeySelective(hd);
				}
			}
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}

	/**
	 * 商品的下架
	 *
	 * @param id
	 *            商品id
	 * @param status
	 *            商品状态
	 * @return
	 */
	@Override
	public int goodsSoldOut(String id, Integer status, String outReason,String userId) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		// goodsOperLogMapper.insertGoodsById();
		GoodsOperLog goodsOperLog = new GoodsOperLog();
		goodsOperLog.setOpType("商家商品下架");
		goodsOperLog.setUserId(userId);
		goodsOperLog.setOrderId("");
		String userName = memberShopMapper.selectUsernameBymId(Integer.parseInt(userId)); //查找操作人
		goodsOperLog.setAdminUser(userName);
		goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
		goods.setStatus(status);
		goods.setIsHot(false);
		goods.setIsFlag(false);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		goods.setDownTime(now);
		if (outReason != null && outReason != "") {
			goods.setOutReason(outReason);
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}
	
	/**
	 * 平台商品的下架
	 *
	 * @param id
	 *            商品id
	 * @param status
	 *            商品状态
	 * @return
	 */
	@Override
	public int goodsPtSoldOut(String id, Integer status, String outReason,String userId) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		GoodsOperLog goodsOperLog = new GoodsOperLog();
		goodsOperLog.setOpType("平台商品下架");
		goodsOperLog.setUserId(userId);
		goodsOperLog.setOrderId("");
		String userName = memberShopMapper.selectUsernameBymId(Integer.parseInt(userId)); //查找操作人
		goodsOperLog.setAdminUser(userName);
		goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
		goods.setStatus(status);
		goods.setIsHot(false);
		goods.setIsFlag(false);
		Timestamp now = new Timestamp(new Date().getTime());
		goods.setEdittime(now);
		goods.setDownTime(now);
		if (outReason != null && outReason != "") {
			goods.setOutReason(outReason);
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}


	/**
	 * 平台后台审核商品上架
	 *
	 * @param id
	 *            商品id
	 * @param status
	 *            商品状态
	 * @return
	 */
	@Override
	public int checkGoodsPutaway(String id, Integer status, String reason, String fixedSale) throws Exception {
		int row = 0;
		

		Timestamp now = new Timestamp(new Date().getTime());
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		//查询该审核的商品是否正常（有没正常的goods_sku）
		List<GoodsSku>	goodsSkuList=goodsSkuMapper.selectListByGoodsIdAndStatus(goods.getId());
		if(goodsSkuList.size()==0){
			return -1;
		}
		if (status == 5) {
			goods.setUpTime(now);
			if (goods.getIsJd() == 1) {// 判断京东商品是否在池中
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(Integer.parseInt(id));
				if (skuList.size() > 0) {
					for (GoodsSku entity : skuList) {
						if(entity.getJdSkuNo()>0){
							List<SellPriceResult> list = JDGoodsApi.getSellPrice(entity.getJdSkuNo().toString());
							if (list!=null && list.size() > 0) {

							} else {
								row = 10000;
								return row;
							}
						}
					}
				}
			}
		}
		goods.setStatus(status);
		if (!StringUtils.isEmptyOrWhitespaceOnly(reason)) {
			goods.setReason(reason);
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(fixedSale)) {
			goods.setFixedSale(Integer.parseInt(fixedSale));
		}
		goods.setEdittime(now);
		row = mapper.updateByPrimaryKeySelective(goods);

		if (status == 5) {// 同意上架
			if (goods.getTopicType() == 6) { // 荷兰拍卖商品，上架设置当前期流拍时间
				HollandDauction entity = new HollandDauction();
				entity.setGoodsId(goods.getId());
				HollandDauction hd = hollandDauctionMapper.getListByGoodsId(goods.getId());
				if (hd != null) {
					int middle = hd.getDauctionPrice() - hd.getLowPrice();
					int remainder = middle % hd.getScopePrice(); // 余数
					int result = middle / hd.getScopePrice(); // 去余数结果
					int num = remainder == 0 ? result + 1 : result + 2;
					Long thisTimeStamp = (long) (num * hd.getTimeSection() * 60000);
					Long reusltTimeStamp = goods.getUpTime().getTime() + thisTimeStamp;
					Date date = new Date(reusltTimeStamp);
					hd.setLoseTime(date);
					hd.setStartTime(now);
					hollandDauctionMapper.updateByPrimaryKeySelective(hd);
				}
			}
		}

		return row;
	}

	/**
     * 平台后台商品列表
     *
     * @param name        查询条件
     * @param currentPage 当前第几页
     * @param pageSize    每页显示几条
     * @return
     * @throws Exception
     */
    @Override
    public PageBean<Goods> backgroundGoodsList(String name, String currentPage, int pageSize, String status,
                                               String isHot, String isNew, String isFlag, String saleType, String startPrice, String endPrice,
                                               String topicType, String id, String skuNo, String jdSkuNo, String shopId, String tagName, String actZoneId)
            throws Exception {
        int intCurrentPage = Integer.parseInt(currentPage);
        PageHelper.startPage(intCurrentPage, pageSize, true);
        List<Goods> list = mapper.backgroundGoodsList(name, status, isHot, isNew, isFlag, saleType, startPrice,
                endPrice, topicType, id, skuNo, jdSkuNo, shopId, tagName, actZoneId);
        if (list != null && list.size() > 0) {
             for (Goods goods : list) {
            	     String zoneIds=actZoneGoodsMapper.selectGoodsActZone(goods.getId());
            	     goods.setZoneId(zoneIds);
                    List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
                    if (skuList.size() > 0) {
                        for (int i = 0; i < skuList.size(); i++) {
                            double realSkuPrice = (double) skuList.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
                            skuList.get(i).setRealPrice(realSkuPrice);
                            double marketRealPrice = (double) skuList.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
                            skuList.get(i).setMarketRealPrice(marketRealPrice);

                            double realJdPrice = (double) skuList.get(i).getJdPrice() / 100; // 京东价转化成“元”
                            skuList.get(i).setRealJdPrice(realJdPrice);

                            double realDeliveryPrice = (double) skuList.get(i).getDeliveryPrice() / 100; // 京东价转化成“元”
                            skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);

                            double realJdBuyPrice = (double) skuList.get(i).getJdBuyPrice() / 100; // 客户购买价“分”转化成“元”
                            skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);

                            double realJdOldBuyPrice = (double) skuList.get(i).getJdOldBuyPrice() / 100; // 客户购买价(旧)“分”转化成“元”
                            skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);

                            double realJdProtocolPrice = (double) skuList.get(i).getJdProtocolPrice() / 100; // 价格“分”转化成“元”
                            skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);

                            double realStockPrice = (double) skuList.get(i).getStockPrice() / 100; // 进货价
                            skuList.get(i).setRealStockPrice(realStockPrice);

                            if (skuList.get(i).getTeamPrice() != null) {
                                double realTPrice = (double) skuList.get(i).getTeamPrice() / 100;// 团购价格“分”转化成“元”
                                skuList.get(i).setRealTeamPrice(realTPrice);
                            }
                            if (skuList.get(i).getAuctionPrice() != null) {
                                double realAuctionPrice = (double) skuList.get(i).getAuctionPrice() / 100;// 拍卖价格“分”转化成“元”
                                skuList.get(i).setRealAuctionPrice(realAuctionPrice);
                            }
                            JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); // value转义
                            skuList.get(i).setValueObj(jsonObject);
                        }
                        goods.setSkuList(skuList);
                    }
                    if (goods.getSellPrice() != null) {
                        double realPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
                        goods.setRealPrice(realPrice);
                    }
                    if (goods.getMarketPrice() != null) {
                        double marketRealPrice = (double) goods.getMarketPrice() / 100;// 市场价格“分”转化成“元”
                        goods.setMarkRealPrice(marketRealPrice);
                    }

                    GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
                    if (category != null) {
                        goods.setCategory(category.getName());
                    }
                    MemberShop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
                    if (shop != null) {
                        goods.setShopName(shop.getShopName());
                    }
                    if (goods.getStatus() == 0) {
                        goods.setGoodsStatus("正常");
                    }
                    if (goods.getStatus() == 1) {
                        goods.setGoodsStatus("已删除");
                    }
                    if (goods.getStatus() == 2) {
                        goods.setGoodsStatus("下架");
                    }
                    if (goods.getStatus() == 3) {
                        goods.setGoodsStatus("申请上架");
                    }
                    if (goods.getStatus() == 4) {
                        goods.setGoodsStatus("拒绝");
                    }
                    int groupCount = 0; // 团购商品销量统计
                    if (goods.getSaleType() == 1) {
                        groupCount = goods.getSale() + goods.getFixedSale();
                    } else {
                        String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
                        if (countT != null) {
                            groupCount = Integer.parseInt(countT) + goods.getFixedSale();
                            goods.setSale(Integer.parseInt(countT));
                        } else {
                            goods.setSale(0);
                            groupCount = goods.getFixedSale();
                        }
                    }
                    goods.setFixedSale(groupCount);
            }
        }
        PageBean<Goods> page = new PageBean<>(list);
        return page;
    }

	/**
	 * 客户端分类查询商品列表
	 *
	 * @param catId
	 *            分类id
	 * @param currentPage
	 *            当前第几页
	 * @param pageSize
	 *            每页显示几条
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean<Goods> clientCategoryGoodsList(String catId, String currentPage, int pageSize, String fz,
			String beginPrice, String endPrice, String brandId) throws Exception {
		int beginPrices = 0; // 设置默认查询起始价格
		int endPrices = 10000000;
		Long brandIds = null;
		if (!StringUtils.isEmptyOrWhitespaceOnly(beginPrice)) {
			// Integer realPrice =(int) (Double.parseDouble(sellPrice) * 100);
			beginPrices = Integer.parseInt(beginPrice) * 100;
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(endPrice)) {
			endPrices = Integer.parseInt(endPrice) * 100;
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(brandId)) {
			brandIds = Long.parseLong(brandId);
		}

		Integer currentPages = Integer.parseInt(currentPage);// 当前第几页
		Integer pageSizes = pageSize;// 每页显示几条
		Integer pageStart = (currentPages - 1) * pageSizes;// 从第几条开始

		List<Goods> list = null;
		int fzs = 0;
		if (!StringUtils.isEmptyOrWhitespaceOnly(fz)) { // fz:1-按销量排序2-按价格3-时间
			fzs = Integer.parseInt(fz);
		}

		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> categoryList = getAllCateList(Integer.parseInt(catId));
		for (GoodsCategory category : categoryList) {
			buffer.append(category.getId() + ",");
		}
		if (buffer.toString().length() > 0) {
			String catIdStr = buffer.toString().substring(0, buffer.toString().length() - 1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		list = mapper.clientCategoryGoodsList(catIdList, 5, pageStart, pageSizes, beginPrices, endPrices, brandIds,
				fzs);
		setGetter(list);
		int total = mapper.clientCountByCategory(catIdList, 5, beginPrices, endPrices, brandIds);// 总条数
		int pages = total / pageSizes;// 总页数
		pages = total % pageSizes > 0 ? (pages + 1) : pages;
		int size = list.size() == pageSizes ? pageSizes : list.size();
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
	public List<GoodsCategory> getAllCateList(long reid) {
		GoodsCategory myGc = categoryMapper.selectByPrimaryKey(reid);
		List<GoodsCategory> catesList = getCatesByReid(reid);
		List<GoodsCategory> retList = new ArrayList<>();
		getCateTreeList(catesList, retList);
		retList.add(0, myGc);
		return retList;
	}

	private List<GoodsCategory> getCateTreeList(List<GoodsCategory> cateList, List<GoodsCategory> retList) {
		List<GoodsCategory> subMenuList = new ArrayList<GoodsCategory>();
		for (GoodsCategory entity : cateList) {
			getCateTreeList(getCatesByReid(entity.getId()), retList);
			retList.add(entity);
			subMenuList.add(entity);
		}
		return subMenuList;
	}

	private List<GoodsCategory> getCatesByReid(long reid) {
		return categoryMapper.selectAllByReid(reid);
	}

	/**
	 * 客户端商品列表（关键字查询+分页+销量、价格、时间排序）
	 *
	 * @param name
	 *            查询条件
	 * @param currentPage
	 *            当前第几页
	 * @param pageSize
	 *            每页显示几条
	 * @param fz
	 *            1-按销量排序2-按价格3-时间
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean<Goods> clientGoodsList(String name, String currentPage, int pageSize, String fz, String beginPrice,
			String endPrice, String brandId) throws Exception {
		int beginPrices = 0; // 设置默认查询起始价格
		int endPrices = 1000000;
		Long brandIds = null;
		if (!StringUtils.isEmptyOrWhitespaceOnly(beginPrice)) {
			beginPrices = Integer.parseInt(beginPrice) * 100;
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(endPrice)) {
			endPrices = Integer.parseInt(endPrice) * 100;
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(brandId)) {
			brandIds = Long.parseLong(brandId);
		}
		List<Goods> list = null;
		int fzs = 0;
		if (!StringUtils.isEmptyOrWhitespaceOnly(fz)) { // fz:1-按销量排序2-按价格3-时间
			fzs = Integer.parseInt(fz);
		}
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		list = mapper.clientGoodsList(name, 5, beginPrices, endPrices, brandIds, fzs);
		setGetter(list);
		PageBean<Goods> page = new PageBean<>(list);
		return page;
	}

	@Override
	public int uploadGoodsImage(String oss, String localFilePath, String key, Integer goodsId) throws Exception {
		upload myupload = new upload();
		boolean bl = myupload.singleupload(oss, localFilePath, key);
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
	public int batchDelete(String id, String userId) throws Exception {
		int row = 0;
		GoodsOperLog goodsOperLog = new GoodsOperLog();
		goodsOperLog.setOpType("商家商品删除");
		goodsOperLog.setUserId(userId);
		goodsOperLog.setOrderId("");
		String userName = memberShopMapper.selectUsernameBymId(Integer.parseInt(userId)); //查找操作人
		goodsOperLog.setAdminUser(userName);
		goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
		List<String> result = Arrays.asList(id.split(",")); // string转list
		for (int i = 0; i < result.size(); i++) { // 判断所选商品是否已上架或者待审核
			Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(result.get(i)));
			if (goods.getStatus() == 3 || goods.getStatus() == 5) {
				row = 999;
				return row;
			}
		}

		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		row = mapper.batchDelete(list);
		//2018.7.18 zlk 加编辑时间
		Goods g=new Goods();
		g.setId(Integer.valueOf(id));
		g.setEdittime(new Timestamp(new Date().getTime()));
		mapper.updateByPrimaryKeySelective(g);
		//end
		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(Integer.parseInt(id));
		if (skuList.size() > 0) {
			for (GoodsSku sku : skuList) {
				if (sku.getJdSkuNo() > 0) {// 如果是京东商品，修改京东商品列表入库状态
					JdGoods jd = new JdGoods();
					jd.setJdSkuNo(sku.getJdSkuNo());
					List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
					if (jdGoodsList.size() > 0) {
						for (JdGoods j : jdGoodsList) {
							j.setIsGet(0);
							jdGoodsMapper.updateByPrimaryKeySelective(j);
						}
					}
				}
				long jdSkuNo = 0;
				sku.setJdSkuNo(jdSkuNo);
				sku.setJdSupport(0);
				sku.setStatus(1);
				row = skuMapper.updateByPrimaryKeySelective(sku);
			}
		}
		return row;
	}

	/**
	 * 首页商品分类显示前6条
	 */
	@Override
	public List<Goods> selectTopSix(String catId) throws Exception {
		List<Goods> list = mapper.selectTopSix(Long.parseLong(catId));
		for (Goods goods : list) {
			double realPrice = (double) goods.getSellPrice() / 100; // 价格“分”转化成“元”
			goods.setRealPrice(realPrice);
			GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
			if (category != null) {
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
		if (imageList.size() == 0) {

		} else {
			for (GoodsImage goodsImage : imageList) {
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
	@Override
	public PageBean<Goods> apiGoodsList(Goods entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<Goods> list = mapper.apiGoodsList(entity);
		if (list.size() > 0) {
			setGetter(list);
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}

	/**
	 * 移动端商品详情
	 */
	@Override
	public Goods apiGoodsDetails(Integer id, Member member, String tgId) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(id);
		double goodsPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
		goods.setRealPrice(goodsPrice);

		double marketRealPrice = (double) goods.getMarketPrice() / 100;// 市场价格“分”转化成“元”
		goods.setMarkRealPrice(marketRealPrice);

		if (goods.getTeamPrice() != null) {
			double realTeamPrice = (double) goods.getTeamPrice() / 100;// 团购价格“分”转化成“元”
			goods.setRealTeamPrice(realTeamPrice);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(tgId)) {// 活动商品明细id
			goods.setTgId(Integer.parseInt(tgId));
		}

		if (goods.getTopicType() == 6) {
			Map<String, Object> map = new HashMap<>();
			if (goods.getTopicGoodsId() != null) {
				System.out.println("8888888888---->" + goods.getTopicGoodsId());
				List<TopicDauctionPrice> dauctionList = dauctionPriceMapper.selectByTgId(goods.getTopicGoodsId());
				if (dauctionList.size() > 0) {
					TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(dauctionList.get(0).getTgId());
					if (topicGoods != null) {
						goods.setTgId(topicGoods.getId());

						Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
						map.put("startTime", topic.getStartTime());
						map.put("endTime", topic.getEndTime());

						List<TopicDauction> dauction = dauctionMapper.selectByTgId(topicGoods.getId());
						if (dauction.size() > 0) {
							map.put("dauctionPrice", (double) dauction.get(0).getDauctionPrice() / 100);
							map.put("lowPrice", (double) dauction.get(0).getLowPrice() / 100);
							map.put("scopePrice", (double) dauction.get(0).getScopePrice() / 100);
							map.put("timeSection", dauction.get(0).getTimeSection());
						} else {
							map.put("dauctionPrice", null);
							map.put("lowPrice", null);
							map.put("scopePrice", null);
							map.put("timeSection", null);
						}
					} else {
						map.put("startTime", null);
						map.put("endTime", null);
					}
					goods.setKuNums(topicGoods.getKuNums());
				}
			}
			goods.setDauctionDetail(map);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(goods.getTagIds())) {
			StringBuffer buffer = new StringBuffer();
			String[] tagIds = goods.getTagIds().split(",");
			for (int i = 0; i < tagIds.length; i++) {
				GoodsTag tag = tagMapper.selectByPrimaryKey(Integer.parseInt(tagIds[i]));
				if (tag != null) {
					buffer.append(tag.getName() + ",");
				}
			}
			if (buffer.toString().length() > 0) {
				String[] tagIdsValue = buffer.toString().substring(0, buffer.toString().length() - 1).split(",");
				goods.setTagIdsValue(tagIdsValue);
			}
		}
		/*
		 * int sale = 0;//月销量 String count = orderSkuMapper.getMonthSaleCount(id);
		 * if(count!=null){ sale = Integer.parseInt(count); } goods.setSale(sale);
		 */

		int groupCount = 0; // 团购商品销量统计
		String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
		if (countT != null) {
			groupCount = Integer.parseInt(countT) + goods.getFixedSale();
		} else {
			groupCount = goods.getFixedSale();
		}
		goods.setGroupCount(groupCount);

		Map<String, Object> map = getShopInfo(goods.getShopId()); // 店铺基本信息
		if (map.size() > 0) {
			goods.setShopInfo(map);
		}

		if (member != null) {
			List<MemberUserAccessLog> logList = accessLogMapper.getBymIdAndGoodsId(member.getId(), id);
			if (logList.size() == 0) {
				MemberUserAccessLog accessLog = new MemberUserAccessLog(); // 新增浏览记录
				accessLog.setAddtime(new Date());
				accessLog.setGoodsId(id);
				accessLog.setmId(member.getId());
				accessLogMapper.insertSelective(accessLog);
			} else {
				// 程凤云2018-4-8如果已经存在，则将时间改变
				MemberUserAccessLog log = new MemberUserAccessLog();
				log.setId(logList.get(0).getId());
				log.setAddtime(new Date());
				accessLogMapper.updateByPrimaryKeySelective(log);
			}
		}

		goods.setVisit(goods.getVisit() + 1);// 浏览次数+1
		mapper.updateByPrimaryKeySelective(goods);

		GoodsDesc desc = descMapper.selectByGoodsIdAndIsPc(id, 0);
		if (desc != null) { // 商品详情描述
			goods.setAppdescription(desc.getDescription());
		}

		List<GoodsImage> imageList = imageMapper.selectListByGoodsId(id);
		if (imageList != null) { // 商品轮播图
			goods.setImageUrl(imageList);
		}
		List<GoodsAttr> attrList = attrMapper.getListByGoodsId(id); // 商品模型属性值(此查询只获取模型id和模型名字)
		if (attrList.size() > 0) {
			for (GoodsAttr goodsAttr : attrList) {
				// 获取某个模型某个商品的所有属性
				List<GoodsAttr> listGoodsAttr = attrMapper.getModelByGoodsIdAndModelId(id, goodsAttr.getModelId());
				goodsAttr.setListGoodsAttr(listGoodsAttr);
			}
			goods.setAttrList(attrList);
		}
		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(id); // 商品规格属性
		if (skuList != null) {
			for (GoodsSku goodsSku : skuList) {
				double skuPrice = (double) goodsSku.getSellPrice() / 100; // 销售价格“分”转化成“元”
				goodsSku.setRealPrice(skuPrice);

				double marketRPrice = (double) goodsSku.getMarketPrice() / 100;// 市场价格“分”转化成“元”
				goodsSku.setMarketRealPrice(marketRPrice);

				if (goodsSku.getTeamPrice() != null) {
					double realTPrice = (double) goodsSku.getTeamPrice() / 100;// 市场价格“分”转化成“元”
					goodsSku.setRealTeamPrice(realTPrice);
					goods.setSkuList(skuList);
				}

				JSONObject jsonObject = JSONObject.fromObject(goodsSku.getValue()); // value转义
				goodsSku.setValueObj(jsonObject);
			}
		}
		double deliveryRealPrice = (double) goods.getDeliveryPrice() / 100; // 销售价格“分”转化成“元”
		goods.setDeliveryRealPrice(deliveryRealPrice);

		ItemModelValue modelValue = new ItemModelValue(); // 查询商品模型
		modelValue.setGoodsId(id);
		List<ItemModelValue> modelValueList = modelValueMapper.selectByGoodsId(modelValue);
		if (modelValueList.size() > 0) {
			JSONArray array = JSONArray.fromObject(modelValueList.get(0).getParamData()); // value转义
			goods.setModelValue(array);
		}
		return goods;
	}

	public Map<String, Object> getShopInfo(int shopId) {
		Map<String, Object> map = new HashMap<>();
		MemberShop shop = shopMapper.selectByPrimaryKey(shopId);
		if (shop != null) {
			map.put("shopName", shop.getShopName()); // 店铺名称
			map.put("shopLogo", shop.getLogo()); // 店铺logo
			Goods good = new Goods();
			good.setShopId(shopId);
			good.setStatus(5);
			int goodsNum = mapper.countByShopId(good);
			map.put("goodsNum", goodsNum); // 店铺商品数量

			int groupCount = 0; // 店铺团购商品销量统计
			OrderSku entity = new OrderSku();
			entity.setShopId(shopId);
			String countT = orderSkuMapper.getShopGroupSale(entity);
			String shopFixedSale = mapper.getShopFixedSale(shopId);// 店铺所有商品后台设置销量统计
			if (countT != null) {
				groupCount = Integer.parseInt(countT);
				if (shopFixedSale != null) {
					groupCount = Integer.parseInt(countT) + Integer.parseInt(shopFixedSale);
				}
			} else {
				if (shopFixedSale != null) {
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
		Integer currentPages = Integer.parseInt(currentPage);// 当前第几页
		Integer pageSizes = pageSize;// 每页显示几条
		Integer pageStart = (currentPages - 1) * pageSizes;// 从第几条开始

		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(Long.parseLong(catId));
		for (GoodsCategory category : cateList) {
			buffer.append(category.getId() + ",");
		}
		if (buffer.toString().length() > 0) {
			String catIdStr = buffer.toString().substring(0, buffer.toString().length() - 1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}

		List<Goods> list = mapper.apiCategoryGoodsList(catIdList, 5, pageStart, pageSizes);
		skuSetter(list); // sku价格转化及复制

		int total = mapper.countApiCategoryGoodsList(catIdList, 5);// 总条数
		int pages = total / pageSizes;// 总页数
		pages = total % pageSizes > 0 ? (pages + 1) : pages;
		int size = list.size() == pageSizes ? pageSizes : list.size();
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
		for (GoodsCategory category : cateList) {
			buffer.append(category.getId() + ",");
		}
		if (buffer.toString().length() > 0) {
			String catIdStr = buffer.toString().substring(0, buffer.toString().length() - 1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}

		List<Goods> list = mapper.apiCategoryGoodsHot(catIdList);
		setGetter(list); // sku价格转化及复制
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
	public PageBean<Goods> getGoodsByCatId(String catId, String currentPage, int pageSize) {
		Integer currentPages = Integer.parseInt(currentPage);// 当前第几页
		Integer pageSizes = pageSize;// 每页显示几条
		Integer pageStart = (currentPages - 1) * pageSizes;// 从第几条开始
		List<Goods> cateList = mapper.getCatesByCatId(pageStart, pageSize, Long.parseLong(catId));
		int total = mapper.countCatesByCatId(Long.parseLong(catId));// 总条数

		int pages = total / pageSizes;// 总页数
		pages = total % pageSizes > 0 ? (pages + 1) : pages;
		int size = cateList.size() == pageSizes ? pageSizes : cateList.size();
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
	 *
	 * @param list
	 * @return
	 */
	public List<Goods> setGetter(List<Goods> list) {
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;

		for (int i = 0; i < list.size(); i++) {
			// int
			// avgStar=goodsCommentMapper.countStar(list.get(i).getShopId(),list.get(i).getId());
			int groupCount = 0; // 团购商品销量统计
			String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			if (countT != null) {
				groupCount = Integer.parseInt(countT) + list.get(i).getFixedSale();
			} else {
				groupCount = list.get(i).getFixedSale();
			}
			list.get(i).setGroupCount(groupCount);

			if (list.get(i).getTopicType() == 6) { // 判断是否拍卖商品
				Map<String, Object> map = new HashMap<>();
				if (list.get(i).getTopicGoodsId() != null) {
					System.out.println("8888888888---->" + list.get(i).getTopicGoodsId());
					List<TopicDauctionPrice> dauctionList = dauctionPriceMapper
							.selectByTgId(list.get(i).getTopicGoodsId());
					if (dauctionList.size() > 0) {
						TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(dauctionList.get(0).getTgId());
						if (topicGoods != null) {
							Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
							map.put("startTime", topic.getStartTime());
							map.put("endTime", topic.getEndTime());

							List<TopicDauction> dauction = dauctionMapper.selectByTgId(topicGoods.getId());
							if (dauction.size() > 0) {
								map.put("dauctionPrice", (double) dauction.get(0).getDauctionPrice() / 100);
								map.put("lowPrice", (double) dauction.get(0).getLowPrice() / 100);
								map.put("scopePrice", (double) dauction.get(0).getScopePrice() / 100);
								map.put("timeSection", dauction.get(0).getTimeSection());
							} else {
								map.put("dauctionPrice", null);
								map.put("lowPrice", null);
								map.put("scopePrice", null);
								map.put("timeSection", null);
							}
						} else {
							map.put("startTime", null);
							map.put("endTime", null);
						}
						list.get(i).setKuNums(topicGoods.getKuNums());
					}
				}
				list.get(i).setDauctionDetail(map);
			}

			StringBuffer buffer = new StringBuffer(); // 设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if (orderTeamList.size() > 0) {
				for (OrderTeam entity : orderTeamList) {
					if (entity.getType() == 0) {// 普通用户
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if (member != null) {
							if (member.getHeadimgurl() != null) {
								buffer.append(member.getHeadimgurl() + ",");
							} else {
								buffer.append(Contants.headImage + ",");
							}
						} else {
							buffer.append(Contants.headImage + ",");
						}
					} else {// 虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if (promoteUser != null) {
							buffer.append(promoteUser.getHeadImg() + ",");
						} else {
							buffer.append(Contants.headImage + ",");
						}
					}
				}
			}
			if (buffer.toString().length() > 0) {
				String headStr = buffer.toString().substring(0, buffer.toString().length() - 1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}

			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if (skuList.size() > 0) {
				for (int j = 0; j < skuList.size(); j++) {
					List<String> imageList = new ArrayList<>();
					double realPrice = (double) skuList.get(0).getSellPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);

					double realTeamPrice = (double) skuList.get(0).getTeamPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);

					if (skuList.get(0).getAuctionPrice() != null) {
						double realAuctionPrice = (double) skuList.get(0).getAuctionPrice() / 100;// 拍卖价格“分”转化成“元”
						list.get(i).setRealAuctionPrice(realAuctionPrice);
					}

					double marketRealPrice = (double) skuList.get(0).getMarketPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());

					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); // 获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if (j < 1) {
						for (int m = 0; m < personList.length(); m++) {
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); // 添加商品sku轮播图
					}
				}
				// list.get(i).setImage(url); //设置第一条sku图为商品主图
			} else {
				double realPrice = (double) list.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double) list.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double) list.get(i).getTeamPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setRealTeamPrice(realTeamPrice);
			}
		}
		return list;
	}

	/**
	 * 排序、设为热门，设为推荐
	 */
	@Override
	public int changeValue(String id, String isHot, String isFlag, String sortnum, String tagName) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if (!StringUtils.isEmptyOrWhitespaceOnly(isHot)) {
			if (Integer.parseInt(isHot) == 1) {
				goods.setIsHot(true);
			} else {
				goods.setIsHot(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isFlag)) {
			if (Integer.parseInt(isFlag) == 1) {
				goods.setIsFlag(true);
			} else {
				goods.setIsFlag(false);
			}
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(tagName)) {
			goods.setTagName(tagName);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(sortnum)) {
			goods.setSortnum(Short.parseShort(sortnum));
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}

	/**
	 * 商家后台商品销售排行列表
	 *
	 * @throws ParseException
	 */
	@Override
	public PageBean<Goods> arrangeBySale(int shopId, String currentPage, int pageSize, String name) {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);

		List<Goods> list = mapper.getListByShopId(shopId, name);
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(currentPage) < 2) {
				list.get(i).setTop(i + 1);
			} else {
				list.get(i).setTop(Integer.parseInt(currentPage) * 10 + i + 1);
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
		if (address != null) {
			attr = address.getProvname() + address.getCityname() + address.getAreaname();
		}
		return attr;
	}

	/**
	 * 移动端分类商品列表(美食模块)
	 */
	@Override
	public PageBean<Goods> apiCategoryEatList(String catId, String currentPage, int pageSize) throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);// 当前第几页
		Integer pageSizes = pageSize;// 每页显示几条
		Integer pageStart = (currentPages - 1) * pageSizes;// 从第几条开始

		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(Integer.parseInt(catId));
		for (GoodsCategory category : cateList) {
			buffer.append(category.getId() + ",");
		}
		if (buffer.toString().length() > 0) {
			String catIdStr = buffer.toString().substring(0, buffer.toString().length() - 1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}

		List<Goods> list = mapper.apiCategoryGoodsList(catIdList, 5, pageStart, pageSizes);
		if (list.size() > 0) {
			for (Goods goods : list) {
				List<GoodsImage> imageList = imageMapper.selectListByGoodsId(goods.getId());
				MemberShop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
				goods.setImageUrl(imageList);
				goods.setShopName(shop.getShopName());
				goods.setShopLevel(shop.getLevel());
			}
		}

		int total = mapper.countApiCategoryGoodsList(catIdList, 5);// 总条数
		int pages = total / pageSizes;// 总页数
		pages = total % pageSizes > 0 ? (pages + 1) : pages;
		int size = list.size() == pageSizes ? pageSizes : list.size();
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
		DecimalFormat df = new DecimalFormat("######0.0");// 精确1位
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
	public HashMap<String,Object> mgetShopDetails(int shopId, int userId) throws Exception {
		//使用hashmap把关键字段封装好
		HashMap<String,Object> hashMap=new HashMap<>();
		MemberShop shop = shopMapper.selectByPrimaryKey(shopId);

		//订单总条数
		int count = orderShopMapper.orderShopCount(0,shopId);
		hashMap.put("orderShopNum",count);

		// 获取某个店铺星级的平均值
		int shopGrade = goodsCommentMapper.countStarAvg(shopId);
		// 查询某个商店所有已评论的订单的配送信息
		List<OrderSendInfo> sendInfoList = orderSendInfoMapper.getSendInfoByShopId(shopId);
		double shopGradeAvg = 0;// 店铺平均评分
		if (sendInfoList.size() > 0) {
			int size = sendInfoList.size();
			shopGradeAvg = (double) shopGrade / size;
		}
		DecimalFormat df = new DecimalFormat("######0.0");// 精确1位
		hashMap.put("level",df.format(shopGradeAvg));


		double realOrderPrice = 0;// 押金
		realOrderPrice = (double) shop.getOrderPrice() / 100;
		hashMap.put("realOrderPrice",realOrderPrice);
		hashMap.put("logo",shop.getLogo());
		hashMap.put("mId",shop.getmId());
		hashMap.put("shopName",shop.getShopName());
		return hashMap;
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
	public int setHotNewAndFlag(String id, String isHotShop, String isShopFlag, String isNewShop, String publicImage)
			throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if (!StringUtils.isEmptyOrWhitespaceOnly(isHotShop)) {
			goods.setIsHotShop(Integer.parseInt(isHotShop));
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(isShopFlag)) {
			if (Integer.parseInt(isShopFlag) == 1) {
				goods.setIsShopFlag(true);
			} else {
				goods.setIsShopFlag(false);
			}
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(isNewShop)) {
			goods.setIsNewShop(Integer.parseInt(isNewShop));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(publicImage)) {
			goods.setPublicimg(publicImage);
		}

		return mapper.updateByPrimaryKeySelective(goods);
	}

	/**
	 * 平台标签修改
	 *
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Override
	public int setPtHotNewAndFlag(String id, String name) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		String[] names = name.split(",");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < names.length; i++) {
			if (!names[i].equals("1")) {
				goods.setIsHot(false);
			}
			if (!names[i].equals("2")) {
				goods.setIsNew(false);
			}
		}
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals("1")) {
				goods.setIsHot(true);
			}
			if (names[i].equals("2")) {
				goods.setIsNew(true);
			}
			if (names[i].equals("3")) {
				sb.append("1" + ",");
			}
			if (names[i].equals("4")) {
				sb.append("2" + ",");
			}
			if (names[i].equals("5")) {
				sb.append("3" + ",");
			}
			if (names[i].equals("6")) {
				sb.append("4" + ",");
			}
			if (names[i].equals("7")) {
				sb.append("5" + ",");
			}
		}
		if (sb.toString() != null && sb.toString().length() > 0) {
			String headStr = sb.toString().substring(0, sb.toString().length() - 1);
			goods.setTagName(headStr);
		} else {
			goods.setTagName("");
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}

	/**
	 * 商家标签修改
	 *
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Override
	public int setHotNewAndFlag(String id, String name, String publicImage) throws Exception {
		Goods goods = mapper.selectByPrimaryKey(Integer.parseInt(id));
		String[] names = name.split(",");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < names.length; i++) {
			if (!names[i].equals("1")) {
				goods.setIsHotShop(0);
			}
			if (!names[i].equals("2")) {
				goods.setIsNewShop(0);
			}
		}
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals("1")) {
				goods.setIsHotShop(1);
			}
			if (names[i].equals("2")) {
				goods.setIsNewShop(1);
			}
			if (names[i].equals("3")) {
				sb.append("1" + ",");
			}
			if (names[i].equals("4")) {
				sb.append("2" + ",");
			}
			if (names[i].equals("5")) {
				sb.append("3" + ",");
			}
			if (names[i].equals("6")) {
				sb.append("4" + ",");
			}
			if (names[i].equals("7")) {
				sb.append("5" + ",");
			}
		}
		if (sb.toString() != null && sb.toString().length() > 0) {
			String headStr = sb.toString().substring(0, sb.toString().length() - 1);
			goods.setTagName(headStr);
		} else {
			goods.setTagName("");
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(publicImage)) {
			goods.setPublicimg(publicImage);
		} else {
			goods.setPublicimg("");
		}
		return mapper.updateByPrimaryKeySelective(goods);
	}

	/**
	 * 获取App下载地址
	 */
	@Override
	public String getDownloadUrl(String fz) throws Exception {
		String url = null;
		if (Integer.parseInt(fz) == 1) { // android
			url = Contants.ANDROID_DOWNLOAD_URL;
		}
		if (Integer.parseInt(fz) == 2) {// ios
			url = Contants.IOS_DOWNLOAD_URL;
		}
		return url;
	}

	/**
	 * 获取分享商品详情
	 */
	@Override
	public ShareResult getShareGoodsDetails(String skuId, String teamNo, String userId, String orderId,
			String shareType, String shareUrl, Member member) throws Exception {

		// 分享日志插入
		if (!StringUtils.isEmptyOrWhitespaceOnly(userId) && !StringUtils.isEmptyOrWhitespaceOnly(skuId)
				&& member != null) { // 新增链接访问记录
			GoodsShareLog shareLog = shareLogMapper.getBymIdAndSkuId(Integer.parseInt(userId), Integer.parseInt(skuId));
			if (shareLog != null) {
				if (!shareLog.getrMId().equals(member.getId())) {
					shareLog.setVisit(shareLog.getVisit() + 1); // 链接访问次数+1
					shareLog.setrMId(member.getId()); // 当前访问人id
					shareLogMapper.updateByPrimaryKeySelective(shareLog);
				}
			} else {
				GoodsShareLog entity = new GoodsShareLog();
				entity.setmId(Integer.parseInt(userId)); // 分享人
				entity.setrMId(member.getId()); // 当前访问人

				Order order = orderMapper.selectByPrimaryKey(Integer.parseInt(orderId));
				entity.setOrderNo(order.getOrderNo()); // 订单号

				if (order.getFz() == 1) {
					entity.setOrderType(1); // 订单类型
				} else {
					entity.setOrderType(2);
				}

				OrderTeam orderTeam = orderTeamMapper.getByOrderNo(order.getOrderNo());
				if (orderTeam != null) {
					entity.setTeamNo(orderTeam.getTeamNo()); // 团号
				}

				entity.setSkuId(Integer.parseInt(skuId)); // 商品规格属性id
				if (!StringUtils.isEmptyOrWhitespaceOnly(shareType)) {
					entity.setShareType(Integer.parseInt(shareType));
				}

				entity.setVisit(1); // 访问量

				if (!StringUtils.isEmptyOrWhitespaceOnly(shareUrl)) {
					entity.setShareUrl(shareUrl); // 分享链接
				}
				shareLogMapper.insertSelective(entity);
			}
		}

		// 分享页面详情
		ShareResult result = new ShareResult();

		GoodsSku sku = skuMapper.selectByPrimaryKey(Integer.parseInt(skuId));
		if (sku != null) {
			Goods goods = mapper.selectByPrimaryKey(sku.getGoodsId());

			selectBygoodsId(goods.getId());
			result.setGoods(goods);// 插入商品基本信息

			result.setSkuId(Integer.parseInt(skuId)); // 商品skuId

			OrderTeam orderTeam = orderTeamMapper.getByTeamNoAndOwner(teamNo);
			if (orderTeam != null) {
				// cheng
				List<OrderTeam> lastList = orderTeamMapper.selectLastOne(orderTeam.getTeamNo());
				if (lastList.size() > 0) {
					TeamLastOne teamLastOne = new TeamLastOne();
					Member member1 = memberMapper.selectByPrimaryKey(lastList.get(0).getmId());
					if (member1 != null) {
						if (member1.getHeadimgurl() != null) {
							teamLastOne.setHeadimgurl(member1.getHeadimgurl()); // 主单用户头像
						} else {
							teamLastOne.setHeadimgurl(Contants.headImage); // 默认头像
						}
						teamLastOne.setUsername(member1.getUsername()); // 主单用户昵称
					} else {
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(lastList.get(0).getmId());
						if (promoteUser != null) {
							teamLastOne.setHeadimgurl(promoteUser.getHeadImg()); // 主单用户头像
							teamLastOne.setUsername(promoteUser.getName()); // 主单用户昵称
						}
					}
					result.setTeamLastOne(teamLastOne);
				}

				Member member1 = memberMapper.selectHeadAndName(orderTeam.getmId());
				if (member1 != null) {
					if (org.apache.commons.lang.StringUtils.isNotEmpty(member1.getHeadimgurl())) {
						result.setHeadimgurl(member1.getHeadimgurl()); // 主单用户头像
					} else {
						result.setHeadimgurl(Contants.headImage); // 默认头像
					}
					result.setUserName(member1.getUsername()); // 主单用户昵称
				} else {
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(orderTeam.getmId());
					if (promoteUser != null) {
						result.setHeadimgurl(promoteUser.getHeadImg()); // 主单用户头像
						result.setUserName(promoteUser.getName()); // 主单用户昵称
					}
				}

				int count = orderTeamMapper.groupCount(orderTeam.getTeamNo());
				int waitNum = goods.getTeamNum() - count;
				result.setWaitNum(waitNum); // 主单等待人数

				long a = new Date().getTime();
				long b = orderTeam.getEndTime().getTime();
				if (b > a) {
					int c = (int) ((b - a) / 1000);
					String hours = c / 3600 + "";
					if (Integer.parseInt(hours) < 10) {
						hours = "0" + hours;
					}
					String min = c % 3600 / 60 + "";
					if (Integer.parseInt(min) < 10) {
						min = "0" + min;
					}
					String sencond = c % 3600 % 60 + "";
					if (Integer.parseInt(sencond) < 10) {
						sencond = "0" + sencond;
					}
					String waitTime = hours + ":" + min + ":" + sencond;
					result.setWaitTime(waitTime); // 主单等待时间
				}
			}

			result.setGoodsId(goods.getId()); // 商品id
			result.setName(goods.getName()); // 商品名称

			double teamPrice = (double) sku.getTeamPrice() / 100;
			result.setTeamPrice(teamPrice); // 商品团购价

			double skuPrice = (double) sku.getSellPrice() / 100;
			int chaPrice = sku.getSellPrice() - sku.getTeamPrice();
			double bhPrice = (double) chaPrice / 100;
			result.setBhPrice(bhPrice); // 商品差价

			org.json.JSONObject jsonObj = new org.json.JSONObject(sku.getValue()); // 获取sku商品信息
			org.json.JSONArray personList = jsonObj.getJSONArray("url");
			String goodsImage = (String) personList.get(0);
			result.setGoodsImage(goodsImage); // 商品图片

			// int num = orderTeamMapper.getGroupCount(goods.getId()); //多少人正在拼团
			result.setTeamNum(goods.getTeamNum());// 几人团

			int finishNum = goods.getFixedSale();
			String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
			if (countT != null) {
				finishNum = goods.getFixedSale() + Integer.parseInt(countT);
			}
			result.setFinishNum(finishNum); // 已拼多少件

			List<OrderTeam> list = orderTeamMapper.getGroupingList(goods.getId()); // 其他团购单列表
			int groupCount = 0;
			if (list.size() > 0) {
				for (OrderTeam team : list) {
					List<OrderTeam> lastList = orderTeamMapper.selectLastOne(team.getTeamNo());
					if (lastList.size() > 0) {
						TeamLastOne teamLastOne = new TeamLastOne();
						Member member1 = memberMapper.selectByPrimaryKey(lastList.get(0).getmId());
						if (member1 != null) {
							if (org.apache.commons.lang.StringUtils.isNotEmpty(member1.getHeadimgurl())) {
								teamLastOne.setHeadimgurl(member1.getHeadimgurl()); // 主单用户头像
							} else {
								teamLastOne.setHeadimgurl(Contants.headImage); // 默认头像
							}
							teamLastOne.setUsername(member1.getUsername()); // 主单用户昵称
						} else {
							PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(lastList.get(0).getmId());
							if (promoteUser != null) {
								teamLastOne.setHeadimgurl(promoteUser.getHeadImg()); // 主单用户头像
								teamLastOne.setUsername(promoteUser.getName()); // 主单用户昵称
							}
						}
						team.setTeamLastOne(teamLastOne);
					}

					Member member2 = memberMapper.selectHeadAndName(team.getmId());
					if (member2 != null) {
						if (org.apache.commons.lang.StringUtils.isNotEmpty(member2.getHeadimgurl())) {
							// result.setHeadimgurl(member2.getHeadimgurl()); //主单用户头像
							team.setHeadimgurl(member2.getHeadimgurl());
						} else {
							// result.setHeadimgurl(Contants.headImage); //默认头像
							team.setHeadimgurl(Contants.headImage);
						}
						// result.setUserName(member2.getUsername()); //主单用户昵称
						team.setUsername(member2.getUsername());
					} else {
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
						if (promoteUser != null) {
							team.setHeadimgurl(promoteUser.getHeadImg()); // 用户头像
							team.setUsername(promoteUser.getName()); // 用户昵称
						}

					}

					groupCount = orderTeamMapper.groupCount(team.getTeamNo());
					int waitNum = goods.getTeamNum() - groupCount;
					team.setWaitNum(waitNum);

					long a = new Date().getTime();
					long b = team.getEndTime().getTime();
					if (b > a) {
						int c = (int) ((b - a) / 1000);
						String hours = c / 3600 + "";
						if (Integer.parseInt(hours) < 10) {
							hours = "0" + hours;
						}
						String min = c % 3600 / 60 + "";
						if (Integer.parseInt(min) < 10) {
							min = "0" + min;
						}
						String sencond = c % 3600 % 60 + "";
						if (Integer.parseInt(sencond) < 10) {
							sencond = "0" + sencond;
						}
						String waitTime = hours + ":" + min + ":" + sencond;
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
		if (list.size() > 0) {
			for (Goods goods : list) {
				double realPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				double marketRealPrice = (double) goods.getMarketPrice() / 100;// 市场价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);

				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if (category != null) {
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
		for (GoodsCategory category : cateList) {
			buffer.append(category.getId() + ",");
		}
		if (buffer.toString().length() > 0) {
			String catIdStr = buffer.toString().substring(0, buffer.toString().length() - 1);
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

	public void updateGoodsSortnum1() throws Exception {
		List<Goods> goodsList = mapper.goodsAll();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < goodsList.size(); i++) {
			sb.append(goodsList.get(i).getId() + ",");
		}
		String headStr = sb.toString().substring(0, sb.toString().length() - 1);
		// List goodsStrId = Arrays.asList(headStr.split(","));
		String[] goodsStrId = headStr.split(",");
		for (int j = 0; j < goodsStrId.length; j++) {
			String id = goodsStrId[j];
			int sortnum = (int) (j + 1);
			mapper.updateSort(id, sortnum);
		}
	}

	public int updateGoodsSortnum(List<Goods> goods) throws Exception {

		int row = 0;
		for (Goods goods2 : goods) {
			Goods good = mapper.selectByPrimaryKey(goods2.getId());
			int sortnum = good.getSortnum();
			row = mapper.updateBySortnum(goods2.getId(), goods2.getSortnum());

			if (sortnum < goods2.getSortnum()) {
				List<Goods> goodsList2 = mapper.getGoodsBySortnum1(goods2.getSortnum(), goods2.getId(), sortnum);
				for (Goods goods4 : goodsList2) {
					if (sortnum < goods4.getSortnum() && goods4.getSortnum() <= goods2.getSortnum()) {
						List<Goods> goodList1 = mapper.getGoodsBySort(goods4.getSortnum(), goods4.getId());
						if (goodList1.size() == 0) {
							break;//
						}
						goods4.setSortnum((short) (goods4.getSortnum() - (short) 1));// 排序号-1
						mapper.updateByPrimaryKeySelective(goods4);
					}
				}
			} else {
				List<Goods> goodsList2 = mapper.getGoodsBySortnum2(goods2.getSortnum(), goods2.getId(), sortnum);
				for (Goods goods5 : goodsList2) {
					if (goods5.getSortnum() < sortnum && goods5.getSortnum() >= goods2.getSortnum()) {
						List<Goods> goodList1 = mapper.getGoodsBySort(goods5.getSortnum(), goods5.getId());
						if (goodList1.size() == 0) {
							break;//
						}
						goods5.setSortnum((short) (goods5.getSortnum() + (short) 1));// 排序号+1
						mapper.updateByPrimaryKeySelective(goods5);
					}
				}
			}
		}

		return row;
	}

	public int updateGoodsShopSortnum(List<Goods> goods) {
		int row = 0;
		for (Goods goods2 : goods) {
			Goods good = mapper.selectByPrimaryKey(goods2.getId());
			int sortnum = good.getShopsortnum();
			row = mapper.updateByPrimaryKeySelective(goods2);
			if (goods2.getShopsortnum() != 30000) {
				/*
				 * List<Goods> goodsList = mapper.getGoodsByShopsortnum(goods2.getShopsortnum(),
				 * goods2.getId());// 查询比当前排序号大或等于的商品（30000除外，和本商品除外） for (Goods goods3 :
				 * goodsList) { List<Goods> goodList1 =
				 * mapper.getGoodsByShopSort(goods3.getShopsortnum(), goods3.getId()); if
				 * (goodList1.size() == 0) { break;// } goods3.setShopsortnum((short)
				 * (goods3.getShopsortnum() + (short) 1));// 排序号+1
				 * mapper.updateByPrimaryKeySelective(goods3); }
				 */
				if (sortnum < goods2.getShopsortnum()) {
					List<Goods> goodsList2 = mapper.getGoodsByShopsortnum1(goods2.getShopsortnum(), goods2.getId(),
							sortnum);
					for (Goods goods4 : goodsList2) {
						if (sortnum < goods4.getShopsortnum() && goods4.getShopsortnum() <= goods2.getShopsortnum()) {
							List<Goods> goodList1 = mapper.getGoodsByShopSort(goods4.getShopsortnum(), goods4.getId());
							if (goodList1.size() == 0) {
								break;//
							}
							goods4.setShopsortnum((short) (goods4.getShopsortnum() - (short) 1));// 排序号-1
							mapper.updateByPrimaryKeySelective(goods4);
						}
					}
				} else {
					List<Goods> goodsList2 = mapper.getGoodsByShopsortnum2(goods2.getShopsortnum(), goods2.getId(),
							sortnum);
					for (Goods goods5 : goodsList2) {
						if (goods5.getShopsortnum() < sortnum && goods5.getShopsortnum() >= goods2.getShopsortnum()) {
							List<Goods> goodList1 = mapper.getGoodsByShopSort(goods5.getShopsortnum(), goods5.getId());
							if (goodList1.size() == 0) {
								break;//
							}
							goods5.setShopsortnum((short) (goods5.getShopsortnum() + (short) 1));// 排序号+1
							mapper.updateByPrimaryKeySelective(goods5);
						}
					}
				}
			}
		}
		return row;
	}

	/* zlk 获取店铺 热门商品 */
	@Override
	public List<Goods> selectHotByShopId(int shopId) throws Exception {
		// TODO Auto-generated method stub
		Goods good = new Goods();
		good.setShopId(shopId);
		good.setIsHotShop(1);// 1为热门
		return mapper.getAllByShopId(good);
	}

	/* zlk 移动端添加 商品 */
	@Override
	public int mInsert(String name, String title, String catId, String sellPrice, String marketPrice,
			String description, String storeNums, String image, String saleType, String teamNum, String teamEndTime,
			String isPromote, String isCreate, String shopId, String isPopular) throws Exception {
		// TODO Auto-generated method stub
		Goods goods = new Goods();
		if (!StringUtils.isEmptyOrWhitespaceOnly(name)) {// 商品名称
			goods.setName(name);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(title)) {// 商品标题
			goods.setTitle(title);
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(isPopular)) {
			goods.setIsPopular(-1);
		} else {
			goods.setIsPopular(Integer.parseInt(isPopular));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(catId)) {// 分类id
			goods.setCatId(Long.valueOf(catId));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)) {// 销售价格单位分
			Integer sPrice = (int) (Double.parseDouble(sellPrice) * 100);
			goods.setSellPrice(sPrice);

		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)) {// 市场价格单位分
			Integer mPrice = (int) (Double.parseDouble(marketPrice) * 100);
			goods.setMarketPrice(mPrice);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(storeNums)) {// 库存
			goods.setStoreNums(Integer.valueOf(storeNums));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(image)) {// 图片
			goods.setImage(image);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(saleType)) {// 销售模式 1单卖 2拼团单卖皆可
			goods.setSaleType(Integer.valueOf(saleType));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(teamNum)) {// 拼团人数
			goods.setTeamNum(Integer.valueOf(teamNum));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(teamEndTime)) {// 活动时间
			goods.setTeamEndTime(Integer.valueOf(teamEndTime));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(isPromote)) {// 是否促成团(-1 否 0是
			goods.setIsPromote(Integer.valueOf(isPromote));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(isCreate)) {// 是否支持系统发起拼单 -1否 0是
			goods.setIsCreate(Integer.valueOf(isCreate));
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(shopId)) { // 商家id
			goods.setShopId(Integer.valueOf(shopId));
		}

		Date date = new Date();
		goods.setAddtime(new Timestamp(date.getTime())); // 添加时间
		int row = mapper.insertSelective(goods); // 插入商品

		GoodsDesc goodsDesc = new GoodsDesc(); // 插入商品详细描述
		goodsDesc.setIsPc(0); // 移动端
		goodsDesc.setGoodsId(goods.getId());
		if (!StringUtils.isEmptyOrWhitespaceOnly(description)) {
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
	
	
	 /**
     * @Description: 修改商家退货地址
     * @author fanjh
     * @date 2018年9月3日 下午2:28:29
     */
	public int updateShopAddress(MemberShop memberShop) {
		return shopMapper.updateByPrimaryKeySelective(memberShop);
	}
	
	/**
	 * @Description: 更新商家名称
	 * @author xieyc
	 * @date 2018年3月6日 下午2:28:29
	 */
	public Integer updateShopName(MemberShop memberShop) {
		int row=0;
		String shopName=memberShop.getShopName();
		List<MemberShop> memberShopList=shopMapper.selectByShopName(shopName);
		if(memberShopList.size()>0) {
			row=999;
		}else {
			row=shopMapper.updateByPrimaryKeySelective(memberShop);
		}
		return row;
	}

	/* zlk 获取店铺所有上架、下架 商品 分页 */
	@Override
	public Map<String, Object> selectByShopId(Goods good) throws Exception {

		PageHelper.startPage(Integer.parseInt(good.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Goods> list = mapper.getByShopIdAndStatus(good);
		// 转换金额
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRealPrice(list.get(i).getSellPrice() / 100);
			list.get(i).setMarkRealPrice(list.get(i).getMarketPrice() / 100);
		}
		PageBean<Goods> pageBean = new PageBean<>(list);

		// 上架数量
		good.setStatus(5);
		int count = mapper.countByShopId(good);

		// 下架数量
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
		if (entity.getTopicType() == 6) {
			entity.setIsCreate(-1);
			entity.setIsPromote(-1);
			entity.setSaleType(1);
		}
		return mapper.updateByPrimaryKeySelective(entity);
	}

	// 修改平台的新品
	public int updatePingTaiNew(Goods g) throws Exception {
		int row = 0;
		row = mapper.updateByPrimaryKeySelective(g);
		return row;
	}

	/**
	 * 程凤云新品列表
	 *
	 * @param list
	 * @return
	 */
	public List<Goods> changeParam(List<Goods> list) {
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;

		for (int i = 0; i < list.size(); i++) {
			// int
			// avgStar=goodsCommentMapper.countStar(list.get(i).getShopId(),list.get(i).getId());
			int groupCount = 0; // 团购商品销量统计
			String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			if (countT != null) {
				groupCount = Integer.parseInt(countT) + list.get(i).getFixedSale();
			} else {
				groupCount = list.get(i).getFixedSale();
			}
			list.get(i).setGroupCount(groupCount);

			StringBuffer buffer = new StringBuffer(); // 设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if (orderTeamList.size() > 0) {
				for (OrderTeam entity : orderTeamList) {
					if (entity.getType() == 0) {// 普通用户
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if (member != null) {
							if (member.getHeadimgurl() != null) {
								buffer.append(member.getHeadimgurl() + ",");
							} else {
								buffer.append(Contants.headImage + ",");
							}
						} else {
							buffer.append(Contants.headImage + ",");
						}
					} else {// 虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if (promoteUser != null) {
							buffer.append(promoteUser.getHeadImg() + ",");
						} else {
							buffer.append(Contants.headImage + ",");
						}
					}
				}
			}
			if (buffer.toString().length() > 0) {
				String headStr = buffer.toString().substring(0, buffer.toString().length() - 1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}

			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if (skuList.size() > 0) {
				for (int j = 0; j < skuList.size(); j++) {
					List<String> imageList = new ArrayList<>();
					double realPrice = (double) skuList.get(0).getSellPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);

					double realTeamPrice = (double) skuList.get(0).getTeamPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);

					double marketRealPrice = (double) skuList.get(0).getMarketPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());

					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); // 获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if (j < 1) {
						for (int m = 0; m < personList.length(); m++) {
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); // 添加商品sku轮播图
					}
				}
				list.get(i).setImage(url); // 设置第一条sku图为商品主图
			} else {
				double realPrice = (double) list.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double) list.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double) list.get(i).getTeamPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setRealTeamPrice(realTeamPrice);
			}
		}
		return list;
	}

	/**
	 * 移动端首页商品列表
	 */
	@Override
	public PageBean<Goods> newGoodsList(Goods entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<Goods> list = mapper.apiGoodsList(entity);
		changeParam(list);
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public PageBean<Goods> mSelectByShopid(int shopId, String name, String currentPage, int pageSize, String catId,
			String saleType, String status, String startPrice, String endPrice, String topicType, String id,
			String skuNo, String jdSkuNo, String sort) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		// List<Goods> list = mapper.selectShopGoodsList(shopId, name, catId, saleType,
		// status,startPrice,
		// endPrice, topicType, id, skuNo, jdSkuNo);
		List<Goods> list = mapper.mSelectShopGoodsList(shopId, name, catId, saleType, status, startPrice, endPrice,
				topicType, id, skuNo, jdSkuNo, sort);
		
		for (int i = 0; i < list.size(); i++) {
			Goods good = new Goods();
			good.setStatus(5);
			good.setShopId(shopId);
			int count = mapper.countByShopId(good);
			list.get(i).setOnSaleNum(count);
			// 下架数量
			good.setStatus(2);
			int count2 = mapper.countByShopId(good);
			list.get(i).setNotOnSaleNum(count2);
		}
		for (Goods goods : list) {
			int storeNums=skuMapper.selectByGoodsId1(goods.getId());
			goods.setStoreNums(storeNums);
		}
		if (list.size() > 0) {
			for (Goods goods : list) {
				List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
				if (skuList.size() > 0) {
					for (int i = 0; i < skuList.size(); i++) {
						double realSkuPrice = (double) skuList.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
						skuList.get(i).setRealPrice(realSkuPrice);
						double marketRealPrice = (double) skuList.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
						skuList.get(i).setMarketRealPrice(marketRealPrice);

						double realJdPrice = (double) skuList.get(i).getJdPrice() / 100; // 京东价转化成“元”
						skuList.get(i).setRealJdPrice(realJdPrice);

						double realDeliveryPrice = (double) skuList.get(i).getDeliveryPrice() / 100; // 物流价转化成“元”
						skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);

						double realJdBuyPrice = (double) skuList.get(i).getJdBuyPrice() / 100; // 客户购买价“分”转化成“元”
						skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);

						double realJdOldBuyPrice = (double) skuList.get(i).getJdOldBuyPrice() / 100; // 客户购买价(旧)“分”转化成“元”
						skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);

						double realJdProtocolPrice = (double) skuList.get(i).getJdProtocolPrice() / 100; // 价格“分”转化成“元”
						skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);

						double realStockPrice = (double) skuList.get(i).getStockPrice() / 100; // 进货价
						skuList.get(i).setRealStockPrice(realStockPrice);

						if (skuList.get(i).getTeamPrice() != null) {
							double realTPrice = (double) skuList.get(i).getTeamPrice() / 100;// 团购价格“分”转化成“元”
							skuList.get(i).setRealTeamPrice(realTPrice);
						}
						if(skuList.get(i).getValue()!=null) {
							JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); // value转义
							skuList.get(i).setValueObj(jsonObject);
						}
						
					}
					if (!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyOne())) {
						goods.setKeyOne(skuList.get(0).getKeyOne());
					}
					if (!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyTwo())) {
						goods.setKeyTwo(skuList.get(0).getKeyTwo());
					}
					if (!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getKeyThree())) {
						goods.setKeyThree(skuList.get(0).getKeyThree());
					}
					goods.setSkuList(skuList);
				}
				double realPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				double marketRealPrice = (double) goods.getMarketPrice() / 100;// 市场价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);

				GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
				if (category != null) {
					goods.setCategory(category.getName());
				}

				int sale = goods.getSale() + goods.getFixedSale();// 销量
				goods.setSale(sale);

				if (goods.getStatus() == 0) {
					goods.setGoodsStatus("正常");
				}
				if (goods.getStatus() == 1) {
					goods.setGoodsStatus("已删除");
				}
				if (goods.getStatus() == 2) {
					goods.setGoodsStatus("下架");
				}
				if (goods.getStatus() == 3) {
					goods.setGoodsStatus("申请上架");
				}
				if (goods.getStatus() == 4) {
					goods.setGoodsStatus("拒绝");
				}
			}
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}

	/**
	 * 价格的转化及sku与主表的赋值
	 *
	 * @param list
	 * @return
	 */
	public List<Goods> skuSetter(List<Goods> list) {
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;

		for (int i = 0; i < list.size(); i++) {
			// int
			// avgStar=goodsCommentMapper.countStar(list.get(i).getShopId(),list.get(i).getId());
			int groupCount = 0; // 团购商品销量统计
			String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			if (countT != null) {
				groupCount = Integer.parseInt(countT) + list.get(i).getFixedSale();
			} else {
				groupCount = list.get(i).getFixedSale();
			}
			list.get(i).setGroupCount(groupCount);

			StringBuffer buffer = new StringBuffer(); // 设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if (orderTeamList.size() > 0) {
				for (OrderTeam entity : orderTeamList) {
					if (entity.getType() == 0) {// 普通用户
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if (member != null) {
							if (member.getHeadimgurl() != null) {
								buffer.append(member.getHeadimgurl() + ",");
							} else {
								buffer.append(Contants.headImage + ",");
							}
						} else {
							buffer.append(Contants.headImage + ",");
						}
					} else {// 虚拟用户
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if (promoteUser != null) {
							buffer.append(promoteUser.getHeadImg() + ",");
						} else {
							buffer.append(Contants.headImage + ",");
						}
					}
				}
			}
			if (buffer.toString().length() > 0) {
				String headStr = buffer.toString().substring(0, buffer.toString().length() - 1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}

			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if (skuList.size() > 0) {
				for (int j = 0; j < skuList.size(); j++) {
					List<String> imageList = new ArrayList<>();
					double realPrice = (double) skuList.get(0).getSellPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);

					double realTeamPrice = (double) skuList.get(0).getTeamPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);

					double marketRealPrice = (double) skuList.get(0).getMarketPrice() / 100; // 价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());

					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); // 获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if (j < 1) {
						for (int m = 0; m < personList.length(); m++) {
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); // 添加商品sku轮播图
					}
				}
				list.get(i).setImage(url); // 设置第一条sku图为商品主图
			} else {
				double realPrice = (double) list.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double) list.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double) list.get(i).getTeamPrice() / 100; // 价格“分”转化成“元”
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
	public boolean isPtSoldOut(Integer shopId, String id) {
		Goods goods = mapper.selectByPrimaryKey(Integer.valueOf(id));
		if (goods.getShopId().intValue() != shopId.intValue()) {
			if (goods.getShopId() != 1) {// admin下架自己自营的商品也不发送短信
				return true;
			}
		}
		return false;
	}

	public MemberShop selectMemberShopBymId(Integer mId) throws Exception {
		return memberShopMapper.selectByPrimaryKey(mId);
	}

	/**
	 * @Description: 发送短信通知商家商品下架原因
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51
	 */
	public BhResult sendMsgToShop(String id, String msg, HttpServletRequest request) {
		Goods good = mapper.selectByPrimaryKey(Integer.valueOf(id));
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(good.getShopId());
		Sms sms = new Sms();
		sms.setPhoneNo(memberShop.getLinkmanPhone());
		sms.setShopName(memberShop.getShopName());
		sms.setGoodsName(good.getName());
		sms.setMsg(msg);
		BhResult r = SmsUtil.aliPushGoodsSoldOutMsg(sms);
		if (r.getStatus() == 200) {
			r.setMsg("亲爱的" + memberShop.getShopName() + "会员,你店铺的" + good.getName() + "已被下架，原因：" + msg + "。如有疑问请联系客服！");
		}
		return r;
	}

	/**
	 * @Description: 保存短信内容到数据库
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51
	 */
	public int saveGoodsMsg(String msg, String id) {
		Goods goods = mapper.selectByPrimaryKey(Integer.valueOf(id));
		GoodsMsg goodsMsg = new GoodsMsg();
		goodsMsg.setMsg(msg);
		goodsMsg.setShopId(goods.getShopId());
		goodsMsg.setCreateTime(new Date());
		goodsMsg.setUpdateTime(new Date());
		goodsMsg.setIsfalgbypt(0);// 平台没读
		goodsMsg.setIsfalgbyshop(0);// 商家没读
		goodsMsg.setMsgtype(0);// 下架短信通知
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
		int isShop = 0;
		if (goodsMsg.getShopId() == 1) {// 为1平台登入
			isShop = 0;// 如果是平台登入那么查询是否有商家发送的消息没读
		} else {// 是其他的为商家登入
			isShop = 1;// 如果是商家登入那么查询是否平台发送的消息没读
		}
		for (GoodsMsg msg : list) {
			// 查询商家或平台是否有回话消息没读
			List<InteractiveRecord> listChat = interactiveRecordMapper.getList(msg.getId(), isShop);
			boolean isRead = false;
			if (listChat.size() == 0) {
				isRead = true;
			}
			msg.setRead(isRead);
			List<InteractiveRecord> interactiveRecordList = interactiveRecordMapper.getListByMsgId(msg.getId());// 查询所有回话列表
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
		record.setIsFlag(0);// 未读
		return interactiveRecordMapper.insert(record);
	}

	/**
	 * @Description: 更新短信状态
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53
	 */
	public int updateMsgstate(String id, int shopId) {
		int row = 0;
		int isShop = 0;
		if (shopId == 1) {// 为1平台登入
			isShop = 0;// 如果是平台登入那么查询是否有商家发送的消息没读
		} else {// 是其他的为商家登入
			isShop = 1;// 如果是商家登入那么查询是否平台发送的消息没读
		}
		List<InteractiveRecord> interactiveRecordList = interactiveRecordMapper.getList(Integer.valueOf(id), isShop);
		for (InteractiveRecord interactiveRecord : interactiveRecordList) {
			if (interactiveRecord.getIsFlag() == 0) {// 将没有读的信息改成已读
				interactiveRecord.setIsFlag(1);
				row = interactiveRecordMapper.updateByPrimaryKey(interactiveRecord);
			}
		}
		return row;
	}

	/**
	 * @Description: 更新短信状态
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53
	 */
	public int updateGoodsMsg(Integer shopId) {
		int row = 0;
		List<GoodsMsg> list = goodsMsgMapper.getAll(shopId);
		if (shopId == 1) {// 为1平台登入
			for (GoodsMsg goodsMsg : list) {
				if (goodsMsg.getIsfalgbypt() == 0) {
					goodsMsg.setIsfalgbypt(1);
					goodsMsg.setUpdateTime(new Date());
					row = goodsMsgMapper.updateByPrimaryKeySelective(goodsMsg);
				}
			}
		} else {// 是其他的为商家登入
			for (GoodsMsg goodsMsg : list) {
				if (goodsMsg.getIsfalgbyshop() == 0) {
					goodsMsg.setIsfalgbyshop(1);
					goodsMsg.setUpdateTime(new Date());
					row = goodsMsgMapper.updateByPrimaryKeySelective(goodsMsg);
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
		int isShop = 0;
		int returnNum = 0;
		if (shopId == 1) {// 为1平台登入
			isShop = 0;// 如果是平台登入那么查询是否有商家发送的消息没读
		} else {// 是其他的为商家登入
			isShop = 1;// 如果是商家登入那么查询是否平台发送的消息没读
		}
		for (GoodsMsg msg : list) {
			// 查询商家或平台是否有回话消息没读
			List<InteractiveRecord> listChat = interactiveRecordMapper.getList(msg.getId(), isShop);
			boolean isRead = true;
			if (listChat.size() > 0) {
				isRead = false;
			}
			msg.setRead(isRead);// 设置该短信是否已读
		}
		for (GoodsMsg goodsMsg : list) {// 跟上isFalg和isRead同时判断短信是否为已读
			if (shopId == 1) {// 平台
				if (goodsMsg.getIsfalgbypt() == 0) {
					returnNum++;
				} else {
					if (!goodsMsg.isRead()) {
						returnNum++;
					}
				}
			} else {// 商家
				if (goodsMsg.getIsfalgbyshop() == 0) {
					returnNum++;
				} else {
					if (!goodsMsg.isRead()) {
						returnNum++;
					}
				}
			}
		}
		return returnNum;
	}

	/**
	 * zlk 按商品的价格修改商品的当前已售数量 2018.3.27
	 */
	@Override
	public void updateFixedSale() {

		GoodsSaleP g = new GoodsSaleP();
		List<GoodsSaleP> goodsSaleP = goodsSalePMapper.selectAll(g);// 获取所有商品价格区间
		System.out.println("goodsSaleP size-->" + goodsSaleP.size());

		for (int i = 0; i < goodsSaleP.size(); i++) { // 遍历商品区间
			GoodsSaleP p = goodsSaleP.get(i);
			Goods goods = new Goods();
			goods.setMin(p.getMin());
			goods.setMax(p.getMax());

			List<Goods> goodsList = mapper.getGoodsBySale(goods);// 获取当前区间的商品
			System.out.println("goodsList size-->" + goodsList.size());

			GoodsSaleAllot.setGoodsFixedSale(goodsList, p.getTotalNum());
			for (int j = 0; j < goodsList.size(); j++) {
				Goods good = new Goods();
				good.setId(goodsList.get(j).getId());
				good.setFixedSale(goodsList.get(j).getFixedSale());
				mapper.updateFixedSale(good);
			}
		}
		// for(int l=0;l<goodsList.size();l++){
		// Goods gg = goodsList.get(l);
		// int fixedSale = gg.getFixedSale();//当前商品的已售数量
		// int totalNum = p.getTotalNum(); //当前区间的分发已售数量总数
		// System.out.println("totalNum---->"+totalNum);
		//
		// int randNum = 0;
		// if(goodsList.size()-1==l){//最后一个此区间商品，也就此区间只有一个商品符合
		// randNum = totalNum;
		// }else{
		// randNum = StringUtil.getMaxVal(totalNum,goodsList.size()-l);//把总数分成随机数
		// int totalRecord =goodsList.size()-l;//当前的剩余商品
		// System.out.println("totalRecord---->"+totalRecord);
		// if(totalNum>0){
		// p.setTotalNum(totalNum-randNum);
		// }
		//
		// }
		// System.out.println("randNum---->"+randNum);
		// fixedSale = fixedSale+randNum; //当前商品的已售数量赋值
		//
		// System.out.println("fixedSale---->"+fixedSale);
		// Goods req = new Goods();
		// System.out.println("gg.getId()---->"+gg.getId());
		// req.setId(gg.getId());
		// req.setFixedSale(fixedSale);
		// mapper.updateByPrimaryKeySelective(req);
		// }

	}

	@Override
	public int update(Goods good) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(good);
	}

	/**
	 * 将商品添加到分类专区
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateActZone(String zoneId, Integer goodsId, Integer skuId) throws Exception {
		int row = 0;
		Date date = new Date();
		if (zoneId == null || zoneId == "") {
			row = actZoneGoodsMapper.deleteByGoodsAndSkuId(goodsId, skuId);
		} else {
			String[] zoneIds = zoneId.split(",");
			List<ActZoneGoods> actZoneGoodsList = actZoneGoodsMapper.selectByGoodsIdList(goodsId);
			if (actZoneGoodsList != null && actZoneGoodsList.size() > 0) {
				actZoneGoodsMapper.deleteByGoodsId(goodsId);
			}
			for (int i = 0; i < zoneIds.length; i++) {
				ActZoneGoods actGoods = new ActZoneGoods();
				if (!StringUtils.isEmptyOrWhitespaceOnly(zoneIds[i])) {
					actGoods.setZoneId(Integer.parseInt(zoneIds[i]));
				}
				if (!StringUtils.isEmptyOrWhitespaceOnly(goodsId.toString())) {
					actGoods.setGoodsId(goodsId);
				}
				if (!StringUtils.isEmptyOrWhitespaceOnly(skuId.toString())) {
					actGoods.setSkuId(skuId);
				}
				actGoods.setAddtime(date);
				actGoods.setEdittime(date);
				row = actZoneGoodsMapper.insertSelective(actGoods);
			}
		}
		return row;
	}

	 /**
	 * 获取所有操作
	 */
	  public PageBean<GoodsOperLog> selectAlloperation(String currentPage, Integer pageSize,String adminUser,String opType,String goodId,String orderId)throws Exception  {
		  Integer currentPages = Integer.parseInt(currentPage);// 当前第几页
		  Integer pageSizes = pageSize;// 每页显示几条
		  Integer pageStart = (currentPages - 1) * pageSizes;// 从第几条开始
	      List<GoodsOperLog> list=goodsOperLogMapper.selectAlloperation(pageStart,pageSizes,adminUser,opType,goodId,orderId);
	      int total = goodsOperLogMapper.selectAlloperationCount(adminUser,opType,goodId,orderId);// 总条数
			int pages = total / pageSizes;// 总页数
			pages = total % pageSizes > 0 ? (pages + 1) : pages;
			int size = list.size() == pageSizes ? pageSizes : list.size();
			PageBean<GoodsOperLog> page = new PageBean<>(list);
			page.setPageNum(currentPages);
			page.setList(list);
			page.setTotal(total);
			page.setPages(pages);
			page.setPageSize(pageSizes);
			page.setSize(size);
	     return page;
	   }

	@Override
	public int testInsertDesc(Goods goods) throws Exception {
		GoodsDesc desc = new GoodsDesc();
		desc.setDescription(goods.getDescription());
		desc.setGoodsId(goods.getId());
		desc.setIsPc(1);
		return descMapper.insertGoodsDesc(desc);
	}
	
	/**
	 * 后台商品新增（new）
	 */
	@Override
	public int addGoods(Goods entity) throws Exception {
		int row = 0;
		Timestamp now = new Timestamp(new Date().getTime());
		List<GoodsSku> skuByJdskuNoList = null;
		List<GoodsSku> skuList = entity.getSkuList(); //判断jdSku是否已存在
		if(skuList.size()>0 && skuList !=null){
			for(GoodsSku sku : skuList){
				if(sku.getJdSkuNo()>0){
					skuByJdskuNoList = skuMapper.isExistByJdSkuNo(sku.getJdSkuNo());
					if(skuByJdskuNoList.size()>0){
						return 666;
					}
				}
			}
		}
		entity.setAddtime(now);
		entity.setEdittime(now);
		row = mapper.insertSelective(entity); //插入商品
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getAppDescription())){
			GoodsDesc appDesc = new GoodsDesc(); //插入移动端商品详细描述
			appDesc.setIsPc(0);
			appDesc.setGoodsId(entity.getId());
			appDesc.setDescription(entity.getAppDescription());
			row = descMapper.insertSelective(appDesc);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getWebDescription())){
			GoodsDesc webDesc = new GoodsDesc(); //插入web端商品详细描述
			webDesc.setIsPc(1);
			webDesc.setGoodsId(entity.getId());
			webDesc.setDescription(entity.getWebDescription());
			row = descMapper.insertSelective(webDesc);
		}
		
		if(skuList.size()>0 && skuList !=null){
			for(GoodsSku sku : skuList){
				sku.setDeliveryPrice(MoneyUtil.doubeToInt(sku.getRealDeliveryPrice()));//物流价
				sku.setSellPrice(MoneyUtil.doubeToInt(sku.getRealPrice()));//销售价
				sku.setMarketPrice(MoneyUtil.doubeToInt(sku.getMarketRealPrice()));//市场价
				sku.setTeamPrice(MoneyUtil.doubeToInt(sku.getRealTeamPrice()));//拼团价
				sku.setStockPrice(MoneyUtil.doubeToInt(sku.getRealStockPrice()));//进货价
				sku.setJdPrice(MoneyUtil.doubeToInt(sku.getRealJdPrice()));//京东价
				sku.setJdBuyPrice(MoneyUtil.doubeToInt(sku.getRealJdBuyPrice()));//京东购买价
				sku.setJdProtocolPrice(MoneyUtil.doubeToInt(sku.getRealJdProtocolPrice()));//京东协议价
				String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
				sku.setSkuNo(no);
				sku.setGoodsId(entity.getId());
				row = skuMapper.insertSelective(sku);
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					if(sku.getJdSkuNo()>0) {
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(sku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								j.setIsGet(1);
								jdGoodsMapper.updateByPrimaryKeySelective(j);
							}
						}
					}
				}
			}
		}
		ItemModelValue value = new ItemModelValue(); //插入模型值
		String paramData="";
		if(org.apache.commons.lang.StringUtils.isNotBlank(entity.getParamData())){
			paramData=entity.getParamData();
		}else{
			paramData="[]";
		}
		value.setGoodsId(entity.getId());
		value.setParamData(paramData);
		value.setAddTime(new Date());
		row = modelValueMapper.insertSelective(value);
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家新增商品");
			goodsOperLog.setUserId(entity.getUserId().toString());
			goodsOperLog.setOrderId("");
			String userName = memberShopMapper.selectUsernameBymId(entity.getUserId()); //查找操作人
			goodsOperLog.setAdminUser(userName);
			row = goodsOperLogMapper.insertGoodsById(entity.getId(), goodsOperLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	
	/**
	 * 后台商品修改（new）
	 */
	@Override
	public int editGoods(Goods entity) throws Exception {
		int row = 0;
		Timestamp now = new Timestamp(new Date().getTime());
		entity.setEdittime(now);
		row = mapper.updateByPrimaryKeySelective(entity); //更新商品
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getAppDescription())){//更新移动端商品详细描述
			GoodsDesc appDesc = descMapper.selectByGoodsIdAndIsPc(entity.getId(), 0);
			if(appDesc!=null){
				appDesc.setDescription(entity.getAppDescription());
				row = descMapper.updateByGoodsIdAndIsPc(appDesc);
			}else{
				GoodsDesc desc = new GoodsDesc(); 
				desc.setIsPc(0);
				desc.setGoodsId(entity.getId());
				desc.setDescription(entity.getAppDescription());
				row = descMapper.insertSelective(desc);
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getWebDescription())){//更新web端商品详细描述
			GoodsDesc webDesc = descMapper.selectByGoodsIdAndIsPc(entity.getId(), 1);
			if(webDesc!=null){
				webDesc.setDescription(entity.getWebDescription());
				row = descMapper.updateByGoodsIdAndIsPc(webDesc);
			}else{
				GoodsDesc desc = new GoodsDesc(); 
				desc.setIsPc(1);
				desc.setGoodsId(entity.getId());
				desc.setDescription(entity.getWebDescription());
				row = descMapper.insertSelective(desc);
			}
		}
		String paramData="";
		if(org.apache.commons.lang.StringUtils.isNotBlank(entity.getParamData())){
			paramData=entity.getParamData();
		}else{
			paramData="[]";
		}
		ItemModelValue value = new ItemModelValue();
		value.setGoodsId(entity.getId());
		List<ItemModelValue> list = modelValueMapper.selectByGoodsId(value);
		if(list.size()>0){ //更新模型值
			ItemModelValue modelValue = modelValueMapper.selectByPrimaryKey(list.get(0).getId());
			modelValue.setParamData(paramData);
			row = modelValueMapper.updateByPrimaryKeySelective(modelValue);
		}else{
			ItemModelValue mValue = new ItemModelValue(); //插入模型值
			mValue.setGoodsId(entity.getId());
			mValue.setParamData(paramData);
			mValue.setAddTime(new Date());
			row = modelValueMapper.insertSelective(mValue);
		}
		
		List<GoodsSku> skuList = entity.getSkuList(); //修改的sku
		if(skuList.size()>0 && skuList !=null){
			GoodsSku oldSku = null;
			for(GoodsSku sku : skuList){
				oldSku = skuMapper.selectByPrimaryKey(sku.getId());
				sku.setDeliveryPrice(MoneyUtil.doubeToInt(sku.getRealDeliveryPrice()));//物流价
				sku.setSellPrice(MoneyUtil.doubeToInt(sku.getRealPrice()));//销售价
				sku.setMarketPrice(MoneyUtil.doubeToInt(sku.getMarketRealPrice()));//市场价
				sku.setTeamPrice(MoneyUtil.doubeToInt(sku.getRealTeamPrice()));//拼团价
				sku.setStockPrice(MoneyUtil.doubeToInt(sku.getRealStockPrice()));//进货价
				sku.setJdPrice(MoneyUtil.doubeToInt(sku.getRealJdPrice()));//京东价
				sku.setJdBuyPrice(MoneyUtil.doubeToInt(sku.getRealJdBuyPrice()));//京东购买价
				sku.setJdProtocolPrice(MoneyUtil.doubeToInt(sku.getRealJdProtocolPrice()));//京东协议价
				row = skuMapper.updateByPrimaryKeySelective(sku);
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					if(sku.getJdSkuNo()>0) {//摘取京东商品
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(sku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==0){
									j.setIsGet(1);
									row = jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}else if(sku.getJdSkuNo()==0 && oldSku.getJdSkuNo()>0){//置为非京东
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(oldSku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==1){
									j.setIsGet(0);
									row = jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}
				}
			}
		}
		
		List<GoodsSku> addSkuList = entity.getAddSkuList(); //新增的sku
		if(addSkuList.size()>0 && addSkuList !=null){
			for(GoodsSku sku : addSkuList){
				sku.setDeliveryPrice(MoneyUtil.doubeToInt(sku.getRealDeliveryPrice()));//物流价
				sku.setSellPrice(MoneyUtil.doubeToInt(sku.getRealPrice()));//销售价
				sku.setMarketPrice(MoneyUtil.doubeToInt(sku.getMarketRealPrice()));//市场价
				sku.setTeamPrice(MoneyUtil.doubeToInt(sku.getRealTeamPrice()));//拼团价
				sku.setStockPrice(MoneyUtil.doubeToInt(sku.getRealStockPrice()));//进货价
				sku.setJdPrice(MoneyUtil.doubeToInt(sku.getRealJdPrice()));//京东价
				sku.setJdBuyPrice(MoneyUtil.doubeToInt(sku.getRealJdBuyPrice()));//京东购买价
				sku.setJdProtocolPrice(MoneyUtil.doubeToInt(sku.getRealJdProtocolPrice()));//京东协议价
				String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
				sku.setSkuNo(no);
				sku.setGoodsId(entity.getId());
				row = skuMapper.insertSelective(sku);
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					if(sku.getJdSkuNo()>0) {
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(sku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==0){
									j.setIsGet(1);
									jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}
				}
			}
		}
		String[] deleteSkuList = entity.getDeleteSkuList();//删除的sku
		if(deleteSkuList.length>0 && deleteSkuList !=null){
			GoodsSku goodsSku = null;
			long jdSkuNo = 0;
			for (int i = 0; i < deleteSkuList.length; i++) {
				goodsSku = skuMapper.selectByPrimaryKey(Integer.valueOf(deleteSkuList[i]));
				if(goodsSku!=null){
					if(goodsSku.getJdSkuNo()>0){//如果是删除京东商品，更改京东列表状态
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(goodsSku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==1){
									j.setIsGet(0);
									jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}
					goodsSku.setStatus(1);
					goodsSku.setJdSkuNo(jdSkuNo);
					goodsSku.setJdSupport(0);
					skuMapper.updateByPrimaryKeySelective(goodsSku);
				}else{
					logger.info("sku不存在删除失败");
				}
			}
		}
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家修改商品");
			goodsOperLog.setUserId(entity.getUserId().toString());
			goodsOperLog.setOrderId("");
			String userName = memberShopMapper.selectUsernameBymId(entity.getUserId()); //查找操作人
			goodsOperLog.setAdminUser(userName);
			row = goodsOperLogMapper.insertGoodsById(entity.getId(), goodsOperLog);
		} catch (Exception e) {
			logger.info("操作日志插入失败");
			e.printStackTrace();
		}
		return row;
	}

	
	/**
     * 平台后台商品列表(优化版)
     *
     * @param name        查询条件
     * @param currentPage 当前第几页
     * @param pageSize    每页显示几条
     * @return
     * @throws Exception
     */
	@Override
	public Map<String, Object> backgroundGoodsLists(String name, String currentPage, int pageSize, String status,
			String isHot, String isNew, String isFlag, String saleType, String startPrice, String endPrice,
			String topicType, String id, String skuNo, String jdSkuNo, String shopId, String tagName, String actZoneId)
			throws Exception {
		 Map<String, Object> map = new HashMap<>();
		 int listAll = mapper.getAll(name, status, isHot, isNew, isFlag, saleType, startPrice,
				 endPrice, topicType, id, skuNo, jdSkuNo, shopId, tagName, actZoneId);
		 int page = (Integer.valueOf(currentPage)-1) * pageSize; // 从第几条开始获取
		 List<Goods> list  = mapper.backgroundGoodsLists(name, status, isHot, isNew, isFlag, saleType, startPrice,
				 endPrice, topicType, id, skuNo, jdSkuNo, shopId, tagName, actZoneId,page,pageSize);
		 if (list != null && list.size() > 0) {
             for (Goods goods : list) {
            	    String zoneIds=actZoneGoodsMapper.selectGoodsActZone(goods.getId());
            	    goods.setZoneId(zoneIds);
                    List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
                    if (skuList.size() > 0) {
                        for (int i = 0; i < skuList.size(); i++) {
                            double realSkuPrice = (double) skuList.get(i).getSellPrice() / 100; // 价格“分”转化成“元”
                            skuList.get(i).setRealPrice(realSkuPrice);
                            double marketRealPrice = (double) skuList.get(i).getMarketPrice() / 100; // 价格“分”转化成“元”
                            skuList.get(i).setMarketRealPrice(marketRealPrice);

                            double realJdPrice = (double) skuList.get(i).getJdPrice() / 100; // 京东价转化成“元”
                            skuList.get(i).setRealJdPrice(realJdPrice);

                            double realDeliveryPrice = (double) skuList.get(i).getDeliveryPrice() / 100; // 京东价转化成“元”
                            skuList.get(i).setRealDeliveryPrice(realDeliveryPrice);

                            double realJdBuyPrice = (double) skuList.get(i).getJdBuyPrice() / 100; // 客户购买价“分”转化成“元”
                            skuList.get(i).setRealJdBuyPrice(realJdBuyPrice);

                            double realJdOldBuyPrice = (double) skuList.get(i).getJdOldBuyPrice() / 100; // 客户购买价(旧)“分”转化成“元”
                            skuList.get(i).setRealJdOldBuyPrice(realJdOldBuyPrice);

                            double realJdProtocolPrice = (double) skuList.get(i).getJdProtocolPrice() / 100; // 价格“分”转化成“元”
                            skuList.get(i).setRealJdProtocolPrice(realJdProtocolPrice);

                            double realStockPrice = (double) skuList.get(i).getStockPrice() / 100; // 进货价
                            skuList.get(i).setRealStockPrice(realStockPrice);

                            if (skuList.get(i).getTeamPrice() != null) {
                                double realTPrice = (double) skuList.get(i).getTeamPrice() / 100;// 团购价格“分”转化成“元”
                                skuList.get(i).setRealTeamPrice(realTPrice);
                            }
                            if (skuList.get(i).getAuctionPrice() != null) {
                                double realAuctionPrice = (double) skuList.get(i).getAuctionPrice() / 100;// 拍卖价格“分”转化成“元”
                                skuList.get(i).setRealAuctionPrice(realAuctionPrice);
                            }
                            JSONObject jsonObject = JSONObject.fromObject(skuList.get(i).getValue()); // value转义
                            skuList.get(i).setValueObj(jsonObject);
                        }
                        goods.setSkuList(skuList);
                    }
                    if (goods.getSellPrice() != null) {
                        double realPrice = (double) goods.getSellPrice() / 100; // 销售价格“分”转化成“元”
                        goods.setRealPrice(realPrice);
                    }
                    if (goods.getMarketPrice() != null) {
                        double marketRealPrice = (double) goods.getMarketPrice() / 100;// 市场价格“分”转化成“元”
                        goods.setMarkRealPrice(marketRealPrice);
                    }

                    GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCatId());
                    if (category != null) {
                        goods.setCategory(category.getName());
                    }
                    MemberShop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
                    if (shop != null) {
                        goods.setShopName(shop.getShopName());
                    }
                    if (goods.getStatus() == 0) {
                        goods.setGoodsStatus("正常");
                    }
                    if (goods.getStatus() == 1) {
                        goods.setGoodsStatus("已删除");
                    }
                    if (goods.getStatus() == 2) {
                        goods.setGoodsStatus("下架");
                    }
                    if (goods.getStatus() == 3) {
                        goods.setGoodsStatus("申请上架");
                    }
                    if (goods.getStatus() == 4) {
                        goods.setGoodsStatus("拒绝");
                    }
                    int groupCount = 0; // 团购商品销量统计
                    if (goods.getSaleType() == 1) {
                        groupCount = goods.getSale() + goods.getFixedSale();
                    } else {
                        String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
                        if (countT != null) {
                            groupCount = Integer.parseInt(countT) + goods.getFixedSale();
                            goods.setSale(Integer.parseInt(countT));
                        } else {
                            goods.setSale(0);
                            groupCount = goods.getFixedSale();
                        }
                    }
                    goods.setFixedSale(groupCount);
            }
        }
		map.put("list", list);// 当前页数据
	    int pages = listAll / pageSize; 
		if (listAll % pageSize != 0) {
			pages = pages + 1;
		}
		map.put("total", listAll);//总数据
		map.put("pages", pages);//总页数
		map.put("size", list.size());//当前页的数据（几条）
		map.put("pageNum", currentPage);//当前页数
		map.put("pageSize", pageSize);//当前页条数（默认十条）
		return map;
	}

	
	/**
	 * 移动端商品新增（new）
	 */
	public int addGoodsM(Goods entity) {
		int row = 0;
		GoodsCategory goodsCategoryThree=categoryMapper.selectByPrimaryKey(entity.getCatId());
		if(goodsCategoryThree!=null){
			GoodsCategory goodsCategoryTwo=categoryMapper.selectByPrimaryKey(goodsCategoryThree.getReid());
			GoodsCategory goodsCategoryOne=categoryMapper.selectByPrimaryKey(goodsCategoryTwo.getReid());
			entity.setCatIdTwo(goodsCategoryTwo.getId());
			entity.setCatIdOne(goodsCategoryOne.getId());
		}
		Timestamp now = new Timestamp(new Date().getTime());
		List<GoodsSku> skuByJdskuNoList = null;
		List<GoodsSku> skuList = entity.getSkuList(); //判断jdSku是否已存在
		if(skuList.size()>0 && skuList !=null){
			for(GoodsSku sku : skuList){
				if(sku.getJdSkuNo()>0){
					skuByJdskuNoList = skuMapper.isExistByJdSkuNo(sku.getJdSkuNo());
					if(skuByJdskuNoList.size()>0){
						return 666;
					}
				}
			}
		}
		entity.setAddtime(now);
		entity.setEdittime(now);
		row = mapper.insertSelective(entity); //插入商品
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getAppDescription())){
			GoodsDesc appDesc = new GoodsDesc(); //插入移动端商品详细描述
			appDesc.setIsPc(0);
			appDesc.setGoodsId(entity.getId());
			appDesc.setDescription(entity.getAppDescription());
			row = descMapper.insertSelective(appDesc);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getWebDescription())){
			GoodsDesc webDesc = new GoodsDesc(); //插入web端商品详细描述
			webDesc.setIsPc(1);
			webDesc.setGoodsId(entity.getId());
			webDesc.setDescription(entity.getWebDescription());
			row = descMapper.insertSelective(webDesc);
		}
		
		if(skuList.size()>0 && skuList !=null){
			for(GoodsSku sku : skuList){
				sku.setDeliveryPrice(MoneyUtil.doubeToInt(sku.getRealDeliveryPrice()));//物流价
				sku.setSellPrice(MoneyUtil.doubeToInt(sku.getRealPrice()));//销售价
				sku.setMarketPrice(MoneyUtil.doubeToInt(sku.getMarketRealPrice()));//市场价
				sku.setTeamPrice(MoneyUtil.doubeToInt(sku.getRealTeamPrice()));//拼团价
				sku.setStockPrice(MoneyUtil.doubeToInt(sku.getRealStockPrice()));//进货价
				sku.setJdPrice(MoneyUtil.doubeToInt(sku.getRealJdPrice()));//京东价
				sku.setJdBuyPrice(MoneyUtil.doubeToInt(sku.getRealJdBuyPrice()));//京东购买价
				sku.setJdProtocolPrice(MoneyUtil.doubeToInt(sku.getRealJdProtocolPrice()));//京东协议价
				String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
				sku.setSkuNo(no);
				sku.setGoodsId(entity.getId());
				row = skuMapper.insertSelective(sku);
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					if(sku.getJdSkuNo()>0) {
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(sku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								j.setIsGet(1);
								jdGoodsMapper.updateByPrimaryKeySelective(j);
							}
						}
					}
				}
			}
		}
		ItemModelValue value = new ItemModelValue(); //插入模型值
		String paramData="";
		if(org.apache.commons.lang.StringUtils.isNotBlank(entity.getParamData())){
			paramData=entity.getParamData();
		}else{
			paramData="[]";
		}
		value.setGoodsId(entity.getId());
		value.setParamData(paramData);
		value.setAddTime(new Date());
		row = modelValueMapper.insertSelective(value);
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家新增商品");
			goodsOperLog.setUserId(entity.getUserId().toString());
			goodsOperLog.setOrderId("");
			String userName = memberShopMapper.selectUsernameBymId(entity.getUserId()); //查找操作人
			goodsOperLog.setAdminUser(userName);
			row = goodsOperLogMapper.insertGoodsById(entity.getId(), goodsOperLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	/**
	 * 移动端商品修改（new）
	 */
	public int mEditGoods(Goods entity) {
		int row = 0;
		GoodsCategory goodsCategoryThree=categoryMapper.selectByPrimaryKey(entity.getCatId());
		if(goodsCategoryThree!=null){
			GoodsCategory goodsCategoryTwo=categoryMapper.selectByPrimaryKey(goodsCategoryThree.getReid());
			GoodsCategory goodsCategoryOne=categoryMapper.selectByPrimaryKey(goodsCategoryTwo.getReid());
			entity.setCatIdTwo(goodsCategoryTwo.getId());
			entity.setCatIdOne(goodsCategoryOne.getId());
		}
		
		Timestamp now = new Timestamp(new Date().getTime());
		entity.setEdittime(now);
		row = mapper.updateByPrimaryKeySelective(entity); //更新商品
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getAppDescription())){//更新移动端商品详细描述
			GoodsDesc appDesc = descMapper.selectByGoodsIdAndIsPc(entity.getId(), 0);
			if(appDesc!=null){
				appDesc.setDescription(entity.getAppDescription());
				row = descMapper.updateByGoodsIdAndIsPc(appDesc);
			}else{
				GoodsDesc desc = new GoodsDesc(); 
				desc.setIsPc(0);
				desc.setGoodsId(entity.getId());
				desc.setDescription(entity.getAppDescription());
				row = descMapper.insertSelective(desc);
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getWebDescription())){//更新web端商品详细描述
			GoodsDesc webDesc = descMapper.selectByGoodsIdAndIsPc(entity.getId(), 1);
			if(webDesc!=null){
				webDesc.setDescription(entity.getWebDescription());
				row = descMapper.updateByGoodsIdAndIsPc(webDesc);
			}else{
				GoodsDesc desc = new GoodsDesc(); 
				desc.setIsPc(1);
				desc.setGoodsId(entity.getId());
				desc.setDescription(entity.getWebDescription());
				row = descMapper.insertSelective(desc);
			}
		}
		
		ItemModelValue value = new ItemModelValue();
		value.setGoodsId(entity.getId());
		List<ItemModelValue> list = modelValueMapper.selectByGoodsId(value);
		String paramData="";
		if(org.apache.commons.lang.StringUtils.isNotBlank(entity.getParamData())){
			paramData=entity.getParamData();
		}else{
			paramData="[]";
		}
		if(list.size()>0){ //更新模型值
			ItemModelValue modelValue = modelValueMapper.selectByPrimaryKey(list.get(0).getId());
			modelValue.setParamData(paramData);
			row = modelValueMapper.updateByPrimaryKeySelective(modelValue);
		}else{
			ItemModelValue mValue = new ItemModelValue(); //插入模型值
			mValue.setGoodsId(entity.getId());
			mValue.setParamData(paramData);
			mValue.setAddTime(new Date());
			row = modelValueMapper.insertSelective(mValue);
		}
		
		List<GoodsSku> skuList = entity.getSkuList(); //修改的sku
		if(skuList.size()>0 && skuList !=null){
			GoodsSku oldSku = null;
			for(GoodsSku sku : skuList){
				oldSku = skuMapper.selectByPrimaryKey(sku.getId());
				sku.setDeliveryPrice(MoneyUtil.doubeToInt(sku.getRealDeliveryPrice()));//物流价
				sku.setSellPrice(MoneyUtil.doubeToInt(sku.getRealPrice()));//销售价
				sku.setMarketPrice(MoneyUtil.doubeToInt(sku.getMarketRealPrice()));//市场价
				sku.setTeamPrice(MoneyUtil.doubeToInt(sku.getRealTeamPrice()));//拼团价
				sku.setStockPrice(MoneyUtil.doubeToInt(sku.getRealStockPrice()));//进货价
				sku.setJdPrice(MoneyUtil.doubeToInt(sku.getRealJdPrice()));//京东价
				sku.setJdBuyPrice(MoneyUtil.doubeToInt(sku.getRealJdBuyPrice()));//京东购买价
				sku.setJdProtocolPrice(MoneyUtil.doubeToInt(sku.getRealJdProtocolPrice()));//京东协议价
				row = skuMapper.updateByPrimaryKeySelective(sku);
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					if(sku.getJdSkuNo()>0) {//摘取京东商品
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(sku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==0){
									j.setIsGet(1);
									row = jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}else if(sku.getJdSkuNo()==0 && oldSku.getJdSkuNo()>0){//置为非京东
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(oldSku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==1){
									j.setIsGet(0);
									row = jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}
				}
			}
		}
		
		List<GoodsSku> addSkuList = entity.getAddSkuList(); //新增的sku
		if(addSkuList.size()>0 && addSkuList !=null){
			for(GoodsSku sku : addSkuList){
				sku.setDeliveryPrice(MoneyUtil.doubeToInt(sku.getRealDeliveryPrice()));//物流价
				sku.setSellPrice(MoneyUtil.doubeToInt(sku.getRealPrice()));//销售价
				sku.setMarketPrice(MoneyUtil.doubeToInt(sku.getMarketRealPrice()));//市场价
				sku.setTeamPrice(MoneyUtil.doubeToInt(sku.getRealTeamPrice()));//拼团价
				sku.setStockPrice(MoneyUtil.doubeToInt(sku.getRealStockPrice()));//进货价
				sku.setJdPrice(MoneyUtil.doubeToInt(sku.getRealJdPrice()));//京东价
				sku.setJdBuyPrice(MoneyUtil.doubeToInt(sku.getRealJdBuyPrice()));//京东购买价
				sku.setJdProtocolPrice(MoneyUtil.doubeToInt(sku.getRealJdProtocolPrice()));//京东协议价
				String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
				sku.setSkuNo(no);
				sku.setGoodsId(entity.getId());
				row = skuMapper.insertSelective(sku);
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					if(sku.getJdSkuNo()>0) {
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(sku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==0){
									j.setIsGet(1);
									jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}
				}
			}
		}
		String[] deleteSkuList = entity.getDeleteSkuList();//删除的sku
		if(deleteSkuList.length>0 && deleteSkuList !=null){
			GoodsSku goodsSku = null;
			long jdSkuNo = 0;
			for (int i = 0; i < deleteSkuList.length; i++) {
				goodsSku = skuMapper.selectByPrimaryKey(Integer.valueOf(deleteSkuList[i]));
				if(goodsSku!=null){
					if(goodsSku.getJdSkuNo()>0){//如果是删除京东商品，更改京东列表状态
						JdGoods jd = new JdGoods();
						jd.setJdSkuNo(goodsSku.getJdSkuNo());
						List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
						if (jdGoodsList.size() > 0) {
							for (JdGoods j : jdGoodsList) {
								if(j.getIsGet()==1){
									j.setIsGet(0);
									jdGoodsMapper.updateByPrimaryKeySelective(j);
								}
							}
						}
					}
					goodsSku.setStatus(1);
					goodsSku.setJdSkuNo(jdSkuNo);
					goodsSku.setJdSupport(0);
					skuMapper.updateByPrimaryKeySelective(goodsSku);
				}else{
					logger.info("sku不存在删除失败");
				}
			}
		}
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家修改商品");
			goodsOperLog.setUserId(entity.getUserId().toString());
			goodsOperLog.setOrderId("");
			String userName = memberShopMapper.selectUsernameBymId(entity.getUserId()); //查找操作人
			goodsOperLog.setAdminUser(userName);
			row = goodsOperLogMapper.insertGoodsById(entity.getId(), goodsOperLog);
		} catch (Exception e) {
			logger.info("操作日志插入失败");
			e.printStackTrace();
		}
		return row;
		
	}
	
	
	
}
