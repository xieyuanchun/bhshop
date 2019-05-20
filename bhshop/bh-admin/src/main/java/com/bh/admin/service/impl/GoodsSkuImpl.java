package com.bh.admin.service.impl;

import java.util.Iterator;
import java.util.List;

import com.bh.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsCartMapper;
import com.bh.admin.mapper.goods.GoodsImageMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.goods.GoodsPriceApprovalMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.JdGoodsMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.goods.GoodsPriceApproval;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.JdGoods;
import com.bh.admin.service.GoodsPriceApprovalService;
import com.bh.admin.service.GoodsSkuService;
import com.bh.utils.LoggerUtil;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.MoneyUtil;
import com.mysql.jdbc.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class GoodsSkuImpl implements GoodsSkuService{
	@Autowired
	private GoodsSkuMapper mapper;
	@Autowired
	private GoodsImageMapper imageMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsCartMapper cartMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired 
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private GoodsPriceApprovalService goodsPriceApprovalService;
	@Autowired
	private JdGoodsMapper jdGoodsMapper;
	@Autowired
	private GoodsPriceApprovalMapper approvalMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
	
	/**
	 * 新增商品规格属性
	 */
	@Override
	public int addGoodsSku(String goodsId, String value, String storeNums, String marketPrice,
			String sellPrice, String weight, String minimum,String skuCode) throws Exception {
		
		GoodsSku goodsSku = new GoodsSku();
		
		String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
		goodsSku.setSkuNo(no);
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(goodsId)){
			goodsSku.setGoodsId(Integer.parseInt(goodsId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(value)){
			goodsSku.setValue(value);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(storeNums)){
			goodsSku.setStoreNums(Integer.parseInt(storeNums));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)){
			Integer mPrice =(int) (Double.parseDouble(marketPrice) * 100);
			goodsSku.setMarketPrice(mPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)){
			Integer realPrice =(int) (Double.parseDouble(sellPrice) * 100);
			goodsSku.setSellPrice(realPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(weight)){
			goodsSku.setWeight(Integer.parseInt(weight));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(minimum)){
			goodsSku.setMinimum(Integer.parseInt(minimum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(skuCode)) {
			goodsSku.setSkuCode(skuCode);
		}
		return mapper.insertSelective(goodsSku);
	}
	
	/**
	 * 根据商品id查询所有sku
	 */
	@Override
	public List<GoodsSku> getListByGoodsId(String goodsId) throws Exception {
		List<GoodsSku> list = mapper.selectListByGoodsIdAndStatus(Integer.parseInt(goodsId));
		if(list!=null){
			for(GoodsSku goodsSku : list){
				double skuPrice = (double)goodsSku.getSellPrice()/100; //销售价格“分”转化成“元”
				goodsSku.setRealPrice(skuPrice);
				double markRealPrice = (double)goodsSku.getMarketPrice()/100; //市场价格“分”转化成“元”
				goodsSku.setMarketRealPrice(markRealPrice);
				double realTeamPrice = (double)goodsSku.getTeamPrice()/100; //团购价格“分”转化成“元”
				goodsSku.setRealTeamPrice(realTeamPrice);
				
				double realStockPrice = (double)goodsSku.getStockPrice()/100;
				goodsSku.setRealStockPrice(realStockPrice);
				
				double realDeliveryPrice = (double)goodsSku.getDeliveryPrice()/100;
				goodsSku.setRealDeliveryPrice(realDeliveryPrice);
				
				double realJdPrice = (double)goodsSku.getJdPrice()/100; //京东价转化成“元”
				goodsSku.setRealJdPrice(realJdPrice);
				
				double realJdBuyPrice = (double)goodsSku.getJdBuyPrice()/100; //客户购买价“分”转化成“元”
				goodsSku.setRealJdBuyPrice(realJdBuyPrice);
				
				double realJdOldBuyPrice = (double)goodsSku.getJdOldBuyPrice()/100; //协议价(旧)“分”转化成“元”
				goodsSku.setRealJdOldBuyPrice(realJdOldBuyPrice);
				
				double realJdProtocolPrice = (double)goodsSku.getJdProtocolPrice()/100; //协议价价格“分”转化成“元”
				goodsSku.setRealJdProtocolPrice(realJdProtocolPrice);
				
				JSONObject jsonObject = JSONObject.fromObject(goodsSku.getValue());
				goodsSku.setValueObj(jsonObject);
			}
		}
		return list;
	}
	
	/**
	 * 编辑商品规格属性
	 */
	@Override
	public int editGoodsSku(String id, String value, String storeNums, String marketPrice, String sellPrice,
			String weight, String minimum, String teamPrice,String jdPrice, String jdBuyPrice, String jdOldBuyPrice,
			String jdProtocolPrice, String jdSupport, String jdSkuNo, String jdUpc, String goodsSkuName, String score,
			String keyOne, String valueOne, String keyTwo, String valueTwo, String keyThree, String valueThree,Integer userId) throws Exception {
		
		GoodsSku goodsSku = mapper.selectByPrimaryKey(Integer.parseInt(id));
		String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
		goodsSku.setSkuNo(no);
		if(!StringUtils.isEmptyOrWhitespaceOnly(value)){
			goodsSku.setValue(value);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(storeNums)){
			goodsSku.setStoreNums(Integer.parseInt(storeNums));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(marketPrice)){
			double d = Double.parseDouble(marketPrice);
			int mPrice =(int) (d*10 * 10);
			goodsSku.setMarketPrice(mPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sellPrice)){
			Integer realPrice =(int) (Double.parseDouble(sellPrice) * 10 *10);
			goodsSku.setSellPrice(realPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(weight)){
			goodsSku.setWeight(Integer.parseInt(weight));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(minimum)){
			goodsSku.setMinimum(Integer.parseInt(minimum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(teamPrice)){
			Integer tPrice =(int) (Double.parseDouble(teamPrice) * 10 * 10);
			goodsSku.setTeamPrice(tPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdOldBuyPrice)){
			Integer jOldBuyPrice =(int) (Double.parseDouble(jdOldBuyPrice) * 10 * 10);
			goodsSku.setJdOldBuyPrice(jOldBuyPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdPrice)){
			Integer jPrice =(int) (Double.parseDouble(jdPrice) * 10 * 10);
			goodsSku.setJdPrice(jPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdBuyPrice)){
			Integer jBuyPrice =(int) (Double.parseDouble(jdBuyPrice) * 10 * 10);
			goodsSku.setJdBuyPrice(jBuyPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdProtocolPrice)){
			Integer jProtocolPrice =(int) (Double.parseDouble(jdProtocolPrice) * 10 * 10);
			goodsSku.setJdProtocolPrice(jProtocolPrice);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdSkuNo)){
			goodsSku.setJdSkuNo(Long.valueOf(jdSkuNo));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdSupport)){
			goodsSku.setJdSupport(Integer.parseInt(jdSupport));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(jdUpc)){
			goodsSku.setJdUpc(jdUpc);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(goodsSkuName)){
			goodsSku.setGoodsName(goodsSkuName);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(score)){
			goodsSku.setScore(Integer.parseInt(score));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(keyOne)){
			goodsSku.setKeyOne(keyOne);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(valueOne)){
			goodsSku.setValueOne(valueOne);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(keyTwo)){
			goodsSku.setKeyTwo(keyTwo);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(valueTwo)){
			goodsSku.setValueTwo(valueTwo);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(keyThree)){
			goodsSku.setKeyThree(keyThree);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(valueThree)){
			goodsSku.setValueThree(valueThree);
		}
		
		return mapper.updateByPrimaryKeySelective(goodsSku);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteGoodsSku(String id) throws Exception {
		int row = 0;
		GoodsSku goodsSku =goodsSkuMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(goodsSku!=null){
			List<GoodsSku> list = mapper.selectListByGoodsIdAndStatus(goodsSku.getGoodsId());
			if(list.size()==1){
				return -1;
			}
			
			if(goodsSku.getJdSkuNo()>0){//如果是京东商品，更改京东商品列表入库状态;
				JdGoods jd = new JdGoods();
				jd.setJdSkuNo(goodsSku.getJdSkuNo());
				List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
				if(jdGoodsList.size()>0){
					for(JdGoods j : jdGoodsList){
						j.setIsGet(0);
						jdGoodsMapper.updateByPrimaryKeySelective(j);
					}
				}
			}
			goodsSku.setStatus(1);
			long jdSkuNo = 0;
			goodsSku.setJdSkuNo(jdSkuNo);
			goodsSku.setJdSupport(0);
			row = mapper.updateByPrimaryKeySelective(goodsSku);
		}
		return row;
	}
	/*public int deleteGoodsSku(String id) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		//查询该skuId对应的商品的goods_sku,如果size为1不允许删除
		GoodsSku goodsSku =goodsSkuMapper.selectByPrimaryKey(Integer.valueOf(str[0]));
		List<GoodsSku> goodsSkuList=goodsSkuMapper.selectListByGoodsId(goodsSku.getGoodsId());
		if(goodsSkuList!=null && goodsSkuList.size()==1){//最后一条记录，不允许删除
			return -1;
		}
		//根据skuId查询order_sku中的记录，如果order_sku有记录  那么不允许删除
		List<OrderSku> orderSkuList = orderSkuMapper.getListBySkuId(Integer.valueOf(str[0]));
		if(orderSkuList!=null && orderSkuList.size()>0){
			return 1000;
		}
		return mapper.batchDelete(list);
	}*/
	
	/**
	 * 批量新增goodsSku
	 */
	@Override
	public int batchAddGoodsSku(String goodsId, JSONArray list) throws Exception {
		int row = 0;
		if(list.size()!=0){
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
			    JSONObject ob = (JSONObject) it.next();
			
				GoodsSku goodsSku = new GoodsSku();
				String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
				goodsSku.setSkuNo(no);
				if(ob.get("stockPrice")!=null && ob.get("stockPrice")!=""){
					Integer realStockPrice =(int)(MoneyUtil.yuan2Fen(ob.getString("stockPrice")));
					goodsSku.setStockPrice(realStockPrice);
				}
				
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsId"))){
					goodsSku.setGoodsId(ob.getInt("goodsId"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("value"))){
					JSONObject jsonObject = JSONObject.fromObject(ob.getString("value"));
					goodsSku.setValue(jsonObject.toString());
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("storeNums"))){
					goodsSku.setStoreNums(ob.getInt("storeNums"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("marketPrice"))){
					Integer mPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("marketPrice")));
					goodsSku.setMarketPrice(mPrice);
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("sellPrice"))){
					Integer realPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("sellPrice")));
					goodsSku.setSellPrice(realPrice);
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("weight"))){
					goodsSku.setWeight(ob.getInt("weight"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("minimum"))){
					goodsSku.setMinimum(ob.getInt("minimum"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdParam"))){
					goodsSku.setJdParam(ob.getString("jdParam"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("teamPrice"))){
					Integer tPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("teamPrice")));
					goodsSku.setTeamPrice(tPrice);
				}
				/*if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("auctionPrice"))){
					Integer auctionPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("auctionPrice")));
					goodsSku.setAuctionPrice(auctionPrice);
				}*/
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSkuNo"))){
					goodsSku.setJdSkuNo(ob.getLong("jdSkuNo"));
				}
				
				/*if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("deliveryPrice"))){
					Integer deliveryPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("deliveryPrice")));
					goodsSku.setDeliveryPrice(deliveryPrice);
				}*/
				
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdPrice"))){
					Integer jdPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdPrice")));
					goodsSku.setJdPrice(jdPrice);
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdBuyPrice"))){
					Integer jdBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdBuyPrice")));
					goodsSku.setJdBuyPrice(jdBuyPrice);
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdOldBuyPrice"))){
					Integer jdOldBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdOldBuyPrice")));
					goodsSku.setJdOldBuyPrice(jdOldBuyPrice);
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdProtocolPrice"))){
					Integer jdProtocolPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdProtocolPrice")));
					goodsSku.setJdProtocolPrice(jdProtocolPrice);
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSupport"))){
					goodsSku.setJdSupport(ob.getInt("jdSupport"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdUpc"))){
					goodsSku.setJdUpc(ob.getString("jdUpc"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsSkuName"))){
					goodsSku.setGoodsName(ob.getString("goodsSkuName"));
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("score"))){
					goodsSku.setScore(ob.getInt("score"));
				}
				if(ob.get("keyOne")!=null && ob.get("keyOne")!=""){
					goodsSku.setKeyOne(ob.getString("keyOne"));
				}
				if(ob.get("valueOne")!=null && ob.get("valueOne")!=""){
					goodsSku.setValueOne(ob.getString("valueOne"));
				}
				if(ob.get("keyTwo")!=null && ob.get("keyTwo")!=""){
					goodsSku.setKeyTwo(ob.getString("keyTwo"));
				}
				if(ob.get("valueTwo")!=null && ob.get("valueTwo")!=""){
					goodsSku.setValueTwo(ob.getString("valueTwo"));
				}
				if(ob.get("keyThree")!=null && ob.get("keyThree")!=""){
					goodsSku.setKeyThree(ob.getString("keyThree"));
				}
				if(ob.get("valueThree")!=null && ob.get("valueThree")!=""){
					goodsSku.setValueThree(ob.getString("valueThree"));
				}
				
				if(ob.get("keyFour")!=null && ob.get("keyFour")!=""){
					goodsSku.setKeyFour(ob.getString("keyFour"));
				}
				if(ob.get("valueFour")!=null && ob.get("valueFour")!=""){
					goodsSku.setValueFour(ob.getString("valueFour"));
				}
				if(ob.get("keyFive")!=null && ob.get("keyFive")!=""){
					goodsSku.setKeyFive(ob.getString("keyFive"));
				}
				if(ob.get("valueFive")!=null && ob.get("valueFive")!=""){
					goodsSku.setValueFive(ob.getString("valueFive"));
				}
				if(ob.get("skuCode")!=null && ob.get("skuCode")!=""){
					goodsSku.setSkuCode(ob.get("skuCode").toString());
				}
				row = mapper.insertSelective(goodsSku);
				
				if(row==1){ //如果是京东商品，更改京东商品列表入库状态
					GoodsSku sku = mapper.selectByPrimaryKey(goodsSku.getId());
					if(sku!=null) {
						if(sku.getJdSkuNo()!=null) {
							if (sku.getJdSkuNo() > 0) {
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
						} else {
							LoggerUtil.getLogger().error("sku.getJdSkuNo()为null!");
						}
					} else {
						LoggerUtil.getLogger().error("sku为null!");
					}
				}
			}
		}else{ //sku为空时，默认设置sku
			GoodsSku sku = new GoodsSku();
			Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodsId)); 
			sku.setGoodsId(Integer.parseInt(goodsId));
			String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
			sku.setSkuNo(no);
			StringBuffer buffer = new StringBuffer();
			buffer.append(goods.getImage());
			sku.setValue("{'url':['"+buffer+"'],'value':'普通规格'}");
			sku.setStoreNums(goods.getStoreNums());
			sku.setMarketPrice(goods.getMarketPrice());
			sku.setSellPrice(goods.getSellPrice());
			if(goods.getTeamPrice()!=null){
				sku.setTeamPrice(goods.getTeamPrice());
			}
			sku.setGoodsName(goods.getName());
			row = mapper.insertSelective(sku);
		}
		return row;
	}
	
	@Override
	public List<GoodsSku> bargainUse(int id){
		List<GoodsSku> skuList = mapper.selectListByGoodsIdAndStatus(id); //商品规格属性
		if(skuList!=null){
			for(GoodsSku goodsSku : skuList){
				double skuPrice = (double)goodsSku.getSellPrice()/100; //销售价格“分”转化成“元”
				goodsSku.setRealPrice(skuPrice);
				
				double marketRPrice = (double)goodsSku.getMarketPrice()/100;//市场价格“分”转化成“元”
				goodsSku.setMarketRealPrice(marketRPrice);
				
				if(goodsSku.getTeamPrice()!=null){
					double realTPrice = (double)goodsSku.getTeamPrice()/100;//市场价格“分”转化成“元”
					goodsSku.setRealTeamPrice(realTPrice);
				}
				
				JSONObject jsonObject = JSONObject.fromObject(goodsSku.getValue()); //value转义
				goodsSku.setValueObj(jsonObject);
			}
		}
		return skuList;
	}

	@Override
	public int batchUpdateGoodsSku(JSONArray list) throws Exception {
		int row = 0;
		if(list.size()!=0){
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
			    JSONObject ob = (JSONObject) it.next();
			    System.out.println(ob.getString("value"));
			    System.out.println(ob.toString());
			    if(ob.getString("id")!=null && ob.getString("id")!=""){
			    	GoodsSku goodsSku = mapper.selectByPrimaryKey(ob.getInt("id"));
					if(goodsSku!=null){
	
						if(ob.get("stockPrice")!=null && ob.get("stockPrice")!=""){
							Integer realStockPrice =(int)(MoneyUtil.yuan2Fen(ob.getString("stockPrice")));
							goodsSku.setStockPrice(realStockPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsId"))){
							goodsSku.setGoodsId(ob.getInt("goodsId"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("value"))){
							JSONObject jsonObject = JSONObject.fromObject(ob.getString("value"));
							goodsSku.setValue(jsonObject.toString());
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("storeNums"))){
							goodsSku.setStoreNums(ob.getInt("storeNums"));
						}
						
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("deliveryPrice"))){
							Integer deliveryPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("deliveryPrice")));
							goodsSku.setDeliveryPrice(deliveryPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("marketPrice"))){
							Integer mPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("marketPrice")));
							goodsSku.setMarketPrice(mPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("sellPrice"))){
							Integer realPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("sellPrice")));
							goodsSku.setSellPrice(realPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("weight"))){
							goodsSku.setWeight(ob.getInt("weight"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("minimum"))){
							goodsSku.setMinimum(ob.getInt("minimum"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdParam"))){
							goodsSku.setJdParam(ob.getString("jdParam"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("teamPrice"))){
							Integer tPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("teamPrice")));
							goodsSku.setTeamPrice(tPrice);
						}
						/*if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("auctionPrice"))){
							Integer auctionPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("auctionPrice")));
							goodsSku.setAuctionPrice(auctionPrice);
						}*/
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSkuNo"))){
							if(!ob.getString("jdSkuNo").equals("null")){
								goodsSku.setJdSkuNo(ob.getLong("jdSkuNo"));
							}
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdPrice"))){
							Integer jdPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdPrice")));
							goodsSku.setJdPrice(jdPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdBuyPrice"))){
							Integer jdBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdBuyPrice")));
							goodsSku.setJdBuyPrice(jdBuyPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdOldBuyPrice"))){
							Integer jdOldBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdOldBuyPrice")));
							goodsSku.setJdOldBuyPrice(jdOldBuyPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdProtocolPrice"))){
							Integer jdProtocolPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdProtocolPrice")));
							goodsSku.setJdProtocolPrice(jdProtocolPrice);
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSupport"))){
							goodsSku.setJdSupport(ob.getInt("jdSupport"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdUpc"))){
							goodsSku.setJdUpc(ob.getString("jdUpc"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsSkuName"))){
							goodsSku.setGoodsName(ob.getString("goodsSkuName"));
						}
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("score"))){
							goodsSku.setScore(ob.getInt("score"));
						}
						if(ob.get("keyOne")!=null && ob.get("keyOne")!=""){
							goodsSku.setKeyOne(ob.getString("keyOne"));
						}
						if(ob.get("valueOne")!=null && ob.get("valueOne")!=""){
							goodsSku.setValueOne(ob.getString("valueOne"));
						}
						if(ob.get("keyTwo")!=null && ob.get("keyTwo")!=""){
							goodsSku.setKeyTwo(ob.getString("keyTwo"));
						}
						if(ob.get("valueTwo")!=null && ob.get("valueTwo")!=""){
							goodsSku.setValueTwo(ob.getString("valueTwo"));
						}
						if(ob.get("keyThree")!=null && ob.get("keyThree")!=""){
							goodsSku.setKeyThree(ob.getString("keyThree"));
						}
						if(ob.get("valueThree")!=null && ob.get("valueThree")!=""){
							goodsSku.setValueThree(ob.getString("valueThree"));
						}
						if(ob.get("keyFour")!=null && ob.get("keyFour")!=""){
							goodsSku.setKeyFour(ob.getString("keyFour"));
						}
						if(ob.get("valueFour")!=null && ob.get("valueFour")!=""){
							goodsSku.setValueFour(ob.getString("valueFour"));
						}
						if(ob.get("keyFive")!=null && ob.get("keyFive")!=""){
							goodsSku.setKeyFive(ob.getString("keyFive"));
						}
						if(ob.get("valueFive")!=null && ob.get("valueFive")!=""){
							goodsSku.setValueFive(ob.getString("valueFive"));
						}
						if(ob.get("skuCode")!=null && ob.get("skuCode")!=""){
							goodsSku.setSkuCode(ob.get("skuCode").toString());
						}
						row = mapper.updateByPrimaryKeySelective(goodsSku);
						if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSkuNo"))){
							if(!ob.getString("jdSkuNo").equals("null")){
								//程凤云 --开始
								try {
									if (goodsSku.getJdSkuNo().equals(0)) {
										Goods goods = new Goods();
										goods.setId(goodsSku.getGoodsId());
										goods.setIsJd(0);
										goodsMapper.updateByPrimaryKeySelective(goods);
										
										goodsSkuMapper.batchUpdateSkuByGoodsId(goodsSku.getGoodsId());
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								//程凤云 --结束
							}
							
						}
					}
			    }else{
					GoodsSku sku = new GoodsSku();
					String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
					sku.setSkuNo(no);
					if(ob.get("skuNo")!=null && ob.get("skuNo")!=""){
						sku.setSkuNo(ob.getString("skuNo"));
					}
					if(ob.get("stockPrice")!=null && ob.get("stockPrice")!=""){
						Integer realStockPrice =(int)(MoneyUtil.yuan2Fen(ob.getString("stockPrice")));
						sku.setStockPrice(realStockPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsId"))){
						sku.setGoodsId(ob.getInt("goodsId"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("value"))){
						JSONObject jsonObject = JSONObject.fromObject(ob.getString("value"));
						sku.setValue(jsonObject.toString());
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("storeNums"))){
						sku.setStoreNums(ob.getInt("storeNums"));
					}
					
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("deliveryPrice"))){
						Integer deliveryPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("deliveryPrice")));
						sku.setDeliveryPrice(deliveryPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("marketPrice"))){
						Integer mPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("marketPrice")));
						sku.setMarketPrice(mPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("sellPrice"))){
						Integer realPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("sellPrice")));
						sku.setSellPrice(realPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("weight"))){
						sku.setWeight(ob.getInt("weight"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("minimum"))){
						sku.setMinimum(ob.getInt("minimum"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdParam"))){
						sku.setJdParam(ob.getString("jdParam"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("teamPrice"))){
						Integer tPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("teamPrice")));
						sku.setTeamPrice(tPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSkuNo"))){
						if(!ob.getString("jdSkuNo").equals("null")){
							sku.setJdSkuNo(ob.getLong("jdSkuNo"));
						}
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdPrice"))){
						Integer jdPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdPrice")));
						sku.setJdPrice(jdPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdBuyPrice"))){
						Integer jdBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdBuyPrice")));
						sku.setJdBuyPrice(jdBuyPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdOldBuyPrice"))){
						Integer jdOldBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdOldBuyPrice")));
						sku.setJdOldBuyPrice(jdOldBuyPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdProtocolPrice"))){
						Integer jdProtocolPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdProtocolPrice")));
						sku.setJdProtocolPrice(jdProtocolPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSupport"))){
						sku.setJdSupport(ob.getInt("jdSupport"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdUpc"))){
						sku.setJdUpc(ob.getString("jdUpc"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsSkuName"))){
						sku.setGoodsName(ob.getString("goodsSkuName"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("score"))){
						sku.setScore(ob.getInt("score"));
					}
					if(ob.get("keyOne")!=null && ob.get("keyOne")!=""){
						sku.setKeyOne(ob.getString("keyOne"));
					}
					if(ob.get("valueOne")!=null && ob.get("valueOne")!=""){
						sku.setValueOne(ob.getString("valueOne"));
					}
					if(ob.get("keyTwo")!=null && ob.get("keyTwo")!=""){
						sku.setKeyTwo(ob.getString("keyTwo"));
					}
					if(ob.get("valueTwo")!=null && ob.get("valueTwo")!=""){
						sku.setValueTwo(ob.getString("valueTwo"));
					}
					if(ob.get("keyThree")!=null && ob.get("keyThree")!=""){
						sku.setKeyThree(ob.getString("keyThree"));
					}
					if(ob.get("valueThree")!=null && ob.get("valueThree")!=""){
						sku.setValueThree(ob.getString("valueThree"));
					}
					row = mapper.insertSelective(sku);
				}
				
			}
		}
		return row;
	}
	
	
	//程凤云
	public int batchUpdateGoodsSkuBySkuId(JSONArray list,String goodsId) throws Exception{
		int row = 0;
		if(list.size()!=0){
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
			    JSONObject ob = (JSONObject) it.next();
				GoodsSku goodsSku = mapper.selectByPrimaryKey(ob.getInt("id"));
				if(goodsSku!=null){
					
					if(ob.get("skuNo")!=null && ob.get("skuNo")!=""){
						goodsSku.setSkuNo(ob.getString("skuNo"));
					}
					if(ob.get("stockPrice")!=null && ob.get("stockPrice")!=""){
						Integer realStockPrice =(int)(MoneyUtil.yuan2Fen(ob.getString("stockPrice")));
						goodsSku.setStockPrice(realStockPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsId"))){
						goodsSku.setGoodsId(ob.getInt("goodsId"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("value"))){
						JSONObject jsonObject = JSONObject.fromObject(ob.getString("value"));
						goodsSku.setValue(jsonObject.toString());
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("storeNums"))){
						goodsSku.setStoreNums(ob.getInt("storeNums"));
					}
					
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("deliveryPrice"))){
						Integer deliveryPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("deliveryPrice")));
						goodsSku.setDeliveryPrice(deliveryPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("marketPrice"))){
						Integer mPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("marketPrice")));
						goodsSku.setMarketPrice(mPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("sellPrice"))){
						Integer realPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("sellPrice")));
						goodsSku.setSellPrice(realPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("weight"))){
						goodsSku.setWeight(ob.getInt("weight"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("minimum"))){
						goodsSku.setMinimum(ob.getInt("minimum"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdParam"))){
						goodsSku.setJdParam(ob.getString("jdParam"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("teamPrice"))){
						Integer tPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("teamPrice")));
						goodsSku.setTeamPrice(tPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSkuNo"))){
						goodsSku.setJdSkuNo(ob.getLong("jdSkuNo"));
					}
					
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdPrice"))){
						Integer jdPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdPrice")));
						goodsSku.setJdPrice(jdPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdBuyPrice"))){
						Integer jdBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdBuyPrice")));
						goodsSku.setJdBuyPrice(jdBuyPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdOldBuyPrice"))){
						Integer jdOldBuyPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdOldBuyPrice")));
						goodsSku.setJdOldBuyPrice(jdOldBuyPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdProtocolPrice"))){
						Integer jdProtocolPrice =(int) (MoneyUtil.yuan2Fen(ob.getString("jdProtocolPrice")));
						goodsSku.setJdProtocolPrice(jdProtocolPrice);
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdSupport"))){
						goodsSku.setJdSupport(ob.getInt("jdSupport"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("jdUpc"))){
						goodsSku.setJdUpc(ob.getString("jdUpc"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("goodsSkuName"))){
						goodsSku.setGoodsName(ob.getString("goodsSkuName"));
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(ob.getString("score"))){
						goodsSku.setScore(ob.getInt("score"));
					}
					if(ob.get("keyOne")!=null && ob.get("keyOne")!=""){
						goodsSku.setKeyOne(ob.getString("keyOne"));
					}
					if(ob.get("valueOne")!=null && ob.get("valueOne")!=""){
						goodsSku.setValueOne(ob.getString("valueOne"));
					}
					if(ob.get("keyTwo")!=null && ob.get("keyTwo")!=""){
						goodsSku.setKeyTwo(ob.getString("keyTwo"));
					}
					if(ob.get("valueTwo")!=null && ob.get("valueTwo")!=""){
						goodsSku.setValueTwo(ob.getString("valueTwo"));
					}
					if(ob.get("keyThree")!=null && ob.get("keyThree")!=""){
						goodsSku.setKeyThree(ob.getString("keyThree"));
					}
					if(ob.get("valueThree")!=null && ob.get("valueThree")!=""){
						goodsSku.setValueThree(ob.getString("valueThree"));
					}
					if (goodsSku.getSkuNo().equals("0")) {
						Goods goods = new Goods();
						goods.setId(Integer.parseInt(goodsId));
						//是否是京东商品，0否，1是
						goods.setIsJd(0);
						goodsMapper.updateByPrimaryKeySelective(goods);
						
					}
					row = mapper.updateByPrimaryKeySelective(goodsSku);
				}
			}
		}
		return row;
	}

	/**
	 * 单个删除
	 */
	@Override
	public int deleteSingle(GoodsSku entity) throws Exception {
		int row = 0;
		GoodsSku goodsSku =goodsSkuMapper.selectByPrimaryKey(entity.getId());
		if(goodsSku!=null){
			if(goodsSku.getJdSkuNo()>0){//如果是京东商品，更改京东商品列表入库状态;
				JdGoods jd = new JdGoods();
				jd.setJdSkuNo(goodsSku.getJdSkuNo());
				List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
				if(jdGoodsList.size()>0){
					for(JdGoods j : jdGoodsList){
						j.setIsGet(0);
						row = jdGoodsMapper.updateByPrimaryKeySelective(j);
					}
				}
			}
			goodsSku.setStatus(1);
			long jdSkuNo = 0;
			goodsSku.setJdSkuNo(jdSkuNo);
			goodsSku.setJdSupport(0);
			row = mapper.updateByPrimaryKeySelective(goodsSku);
			
			GoodsPriceApproval val = new GoodsPriceApproval(); //价格变更表置为已删除
			val.setGoodsSkuId(entity.getId());
			List<GoodsPriceApproval> approvalList = approvalMapper.getByGoodsSkuId(val);
			if(approvalList.size()>0){
				for(GoodsPriceApproval app : approvalList){
					app.setIsDel(1);
					row = approvalMapper.updateByPrimaryKeySelective(app);
				}
			}
			try {
				GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
				goodsOperLog.setOpType("商家删除了id为"+entity.getId()+"的sku");
				goodsOperLog.setUserId(entity.getUserId().toString());
				goodsOperLog.setOrderId("");
				String userName = memberShopMapper.selectUsernameBymId(entity.getUserId()); //查找操作人
				goodsOperLog.setAdminUser(userName);
				row = goodsOperLogMapper.insertGoodsById(entity.getId(), goodsOperLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}
	
	/**
	 * 单个更新
	 */
	@Override
	public int updateSingle(GoodsSku entity) throws Exception {
		int row = 0;
		entity.setDeliveryPrice(MoneyUtil.doubeToInt(entity.getRealDeliveryPrice()));//物流价
		entity.setSellPrice(MoneyUtil.doubeToInt(entity.getRealPrice()));//销售价
		entity.setMarketPrice(MoneyUtil.doubeToInt(entity.getMarketRealPrice()));//市场价
		entity.setTeamPrice(MoneyUtil.doubeToInt(entity.getRealTeamPrice()));//拼团价
		entity.setStockPrice(MoneyUtil.doubeToInt(entity.getRealStockPrice()));//进货价
		entity.setJdPrice(MoneyUtil.doubeToInt(entity.getRealJdPrice()));//京东价
		entity.setJdBuyPrice(MoneyUtil.doubeToInt(entity.getRealJdBuyPrice()));//京东购买价
		entity.setJdProtocolPrice(MoneyUtil.doubeToInt(entity.getRealJdProtocolPrice()));//京东协议价
		row = mapper.updateByPrimaryKeySelective(entity);
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家修改id为"+entity.getId()+"的sku");
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
	 * 置为非京东
	 */
	@Override
	public int updateJdStatus(GoodsSku entity) throws Exception {
		int row = 0;
		long jdSkuNo = 0;
		Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
		if(goods!=null){
			goods.setIsJd(0);
			row = goodsMapper.updateByPrimaryKeySelective(goods);
		}
		List<GoodsSku> skuList = mapper.selectListByGoodsIdAndStatus(entity.getGoodsId());
		if(skuList.size()>0){
			for(GoodsSku sku : skuList){
				if(sku.getJdSkuNo()>0){
					sku.setJdSkuNo(jdSkuNo);
					sku.setJdSupport(0);
					row = mapper.updateByPrimaryKeySelective(sku);
				}
			}
		}
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家将id为"+entity.getGoodsId()+"的商品置为了非京东");
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
	 * sku重构保存
	 */
	@Override
	public int updateBatch(GoodsSku entity) throws Exception {
		int row = 0;
		List<GoodsSku> oldList = mapper.selectListByGoodsIdAndStatus(entity.getGoodsId()); //清除旧sku
		if(oldList.size()>0){
			for(GoodsSku gSku : oldList){
				if(gSku.getJdSkuNo()>0){//如果是京东商品，更改京东商品列表入库状态;
					JdGoods jd = new JdGoods();
					jd.setJdSkuNo(gSku.getJdSkuNo());
					List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
					if(jdGoodsList.size()>0){
						for(JdGoods j : jdGoodsList){
							j.setIsGet(0);
							row = jdGoodsMapper.updateByPrimaryKeySelective(j);
						}
					}
				}
				long jdSkuNo = 0;
				gSku.setStatus(1);
				gSku.setJdSkuNo(jdSkuNo);
				gSku.setJdSupport(0);
				row = mapper.updateByPrimaryKeySelective(gSku);
			}
		}
		List<GoodsSku> skuList = entity.getSkuList(); //插入sku
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
				sku.setGoodsId(entity.getGoodsId());
				String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
				sku.setSkuNo(no);
				row = mapper.insertSelective(sku);
			}
		}
		try {
			GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
			goodsOperLog.setOpType("商家将id为"+entity.getGoodsId()+"的商品进行的sku的重组");
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
}
