package com.bh.product.api.service.impl;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.config.Contants;
import com.bh.goods.mapper.CashDepositMapper;
import com.bh.goods.mapper.GoodsFavoriteMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.HollandDauctionLogMapper;
import com.bh.goods.mapper.HollandDauctionMapper;
import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsFavorite;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.HollandDauction;
import com.bh.goods.pojo.HollandDauctionLog;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.ApiHollandDauctionLogService;
import com.bh.product.api.service.ApiHollandDauctionService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.IDUtils;
import com.bh.utils.PageBean;
import com.bh.utils.StringUtil;
import com.bh.utils.pay.HttpService;
import com.github.pagehelper.PageHelper;
@Service
public class ApiHollandDauctionServiceImpl implements ApiHollandDauctionService{
	@Autowired
	private HollandDauctionMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private HollandDauctionLogMapper logMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private GoodsFavoriteMapper favoriteMapper;
	@Autowired
	private ApiHollandDauctionLogService hdLogService;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Autowired
	private ApiHollandDauctionService dauctionService;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private ApiHollandDauctionLogService dauctionLogService;
	
	
	@Override
	public PageBean<HollandDauction> apiDauctionList(HollandDauction entity, Member member) throws Exception {
		int isLogin = 0;
		double realDauctionPrice = 0;
		double realLowPrice = 0;
		double realScopePrice = 0;
		List<HollandDauctionLog> logList = null;
		if(member!=null){
			isLogin=1;
		}
		HollandDauctionLog log = new HollandDauctionLog();
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), 10, true);
		List<HollandDauction> list = mapper.apiDauctionList();
		if(list.size()>0){
			for(HollandDauction hollDauction : list){
				realDauctionPrice = (double)hollDauction.getDauctionPrice()/100;
				hollDauction.setRealDauctionPrice(realDauctionPrice);
				
				realLowPrice = (double)hollDauction.getLowPrice()/100;
				hollDauction.setRealLowPrice(realLowPrice);
				
				realScopePrice = (double)hollDauction.getScopePrice()/100;
				hollDauction.setRealScopePrice(realScopePrice);
				
				Goods goods = goodsMapper.selectByPrimaryKey(hollDauction.getGoodsId());
				Map<String, Object> goodsDetails = new LinkedHashMap<>();
				goodsDetails.put("goodsName", goods.getName());
				goodsDetails.put("goodsImage", goods.getImage());
				
				if(isLogin==1){
					GoodsFavorite favorite = favoriteMapper.findByGoodsIdAndMid(goods.getId(), member.getId());
					if(favorite!=null){
						goodsDetails.put("collect", 0);
					}else{
						goodsDetails.put("collect", 1);
					}
				}else{
					goodsDetails.put("collect", 1);
				}
				
				//检测是否有超过30秒未改状态的数据
				log.setGoodsId(hollDauction.getGoodsId());
				logList = logMapper.getListByGoodsIdAndStatus(log);
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
							Order order = hdLogService.rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), oldLog.getGoodsId());
							if(order!=null){
								oldLog.setOrderNo(order.getOrderNo());
							}
							logMapper.updateByPrimaryKeySelective(oldLog);
							//未中标用户退押金
							dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
							
							if(hollDauction.getStoreNums()>0){
								if(hollDauction.getStoreNums()==1){
									
								}else{
									hollDauction.setCurrentPeriods(hollDauction.getCurrentPeriods()+1);
									Date loseTime = accountLoseTime(hollDauction, date);//新一期流拍时间
									hollDauction.setLoseTime(loseTime);
									hollDauction.setStartTime(date);
								}
								hollDauction.setStoreNums(hollDauction.getStoreNums()-1);
								mapper.updateByPrimaryKeySelective(hollDauction);
							}
						}
					}
					//检测结束
					
					if(oldLog.getPayStatus()==1 || oldLog.getPayStatus()==2){ //最新出价为待支付或者已支付状态
						goodsDetails.put("price", null);
					}else{
						if(logList.get(0).getdStatus()==4){ //流拍
							goodsDetails.put("price", null);
						}else{
							goodsDetails.put("price", (double)logList.get(logList.size()-1).getPrice()/100);
						}
					}
				}else{
					goodsDetails.put("price", null);
				}
				hollDauction.setGoodsDetails(goodsDetails);
			}
		}
		PageBean<HollandDauction> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/*public PageBean<HollandDauction> apiDauctionList(HollandDauction entity, Member member) throws Exception {
		int isLogin = 0;
		double realDauctionPrice = 0;
		double realLowPrice = 0;
		double realScopePrice = 0;
		List<HollandDauctionLog> logList = null;
		if(member!=null){
			isLogin=1;
		}
		HollandDauctionLog log = new HollandDauctionLog();
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), 10, true);
		List<HollandDauction> list = mapper.apiDauctionList();
		if(list.size()>0){
			for(HollandDauction hollDauction : list){
				realDauctionPrice = (double)hollDauction.getDauctionPrice()/100;
				hollDauction.setRealDauctionPrice(realDauctionPrice);
				
				realLowPrice = (double)hollDauction.getLowPrice()/100;
				hollDauction.setRealLowPrice(realLowPrice);
				
				realScopePrice = (double)hollDauction.getScopePrice()/100;
				hollDauction.setRealScopePrice(realScopePrice);
				
				Goods goods = goodsMapper.selectByPrimaryKey(hollDauction.getGoodsId());
				Map<String, Object> goodsDetails = new LinkedHashMap<>();
				goodsDetails.put("goodsName", goods.getName());
				goodsDetails.put("goodsImage", goods.getImage());
				
				if(isLogin==1){
					GoodsFavorite favorite = favoriteMapper.findByGoodsIdAndMid(goods.getId(), member.getId());
					if(favorite!=null){
						goodsDetails.put("collect", 0);
					}else{
						goodsDetails.put("collect", 1);
					}
				}else{
					goodsDetails.put("collect", 1);
				}
				
				//检测是否有超过30秒未改状态的数据
				log.setGoodsId(hollDauction.getGoodsId());
				logList = logMapper.getListByGoodsIdAndStatus(log);
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
							Order order = hdLogService.rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), oldLog.getGoodsId());
							if(order!=null){
								oldLog.setOrderNo(order.getOrderNo());
							}
							logMapper.updateByPrimaryKeySelective(oldLog);
							//未中标用户退押金
							dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
							
							if(hollDauction.getStoreNums()>0){
								if(hollDauction.getStoreNums()==1){
									
								}else{
									hollDauction.setCurrentPeriods(hollDauction.getCurrentPeriods()+1);
									Date loseTime = accountLoseTime(hollDauction, date);//新一期流拍时间
									hollDauction.setLoseTime(loseTime);
								}
								hollDauction.setStoreNums(hollDauction.getStoreNums()-1);
								mapper.updateByPrimaryKeySelective(hollDauction);
							}
						}
					}
					//检测结束
					
					if(oldLog.getPayStatus()==1 || oldLog.getPayStatus()==2){ //最新出价为待支付或者已支付状态
						goodsDetails.put("price", null);
					}else{
						if(logList.get(0).getdStatus()==4){ //流拍
							goodsDetails.put("price", null);
						}else{
							goodsDetails.put("price", (double)logList.get(logList.size()-1).getPrice()/100);
						}
					}
				}else{
					goodsDetails.put("price", null);
				}
				
				if(hollDauction.getCurrentPeriods()==1){
					hollDauction.setStartTime(goods.getUpTime());
				}else{
					log.setCurrentPeriods(hollDauction.getCurrentPeriods()-1);
					logList = logMapper.getListByGoodsIdAndCurrentPeriods(log);
					if(logList.size()>0){
						if(logList.get(0).getEndTime()!=null){
							hollDauction.setStartTime(logList.get(0).getEndTime());
						}else{
							hollDauction.setStartTime(logList.get(1).getEndTime());
						}
					}
				}
				hollDauction.setGoodsDetails(goodsDetails);
			}
		}
		PageBean<HollandDauction> pageBean = new PageBean<>(list);
		return pageBean;
	}*/
	
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
	
	/**
	 * 
	 * @param mId 用户  ID
	 * @param userName 用户名
	 * @param headImg  用户头像
	 * @param auctionPrice 当前用户拍卖价格
	 * @return
	 */
	@Override
	public boolean dauctionNotice(Integer mId, String auctionPrice, String goodsId, String currentPeriods) {
		try {
			int price = 0;
			Double realPrice = null;
			Date date = new Date();
			HollandDauction dauction = new HollandDauction();
			dauction.setGoodsId(Integer.parseInt(goodsId));
			HollandDauction hd = mapper.getByGoodsId(dauction);
			
			HollandDauctionLog log = new HollandDauctionLog();
			log.setGoodsId(Integer.parseInt(goodsId));
			List<HollandDauctionLog> list = logMapper.getListByGoodsIdAndStatus(log);
			if(list.size()>0){
				HollandDauctionLog oldLog = list.get(0);  
				Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
				Long curTimeStamp = date.getTime();
				if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){ //拍卖中
					if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
						Long thisTimeStamp = oldLog.getEndTime().getTime(); //上期结束时间等于本期的开始时间
						Long cTime = date.getTime()-thisTimeStamp;
						int second = (int)(cTime/1000/60);
						int db = second /hd.getTimeSection();
						price = hd.getDauctionPrice()-db*hd.getScopePrice();
						if(price<hd.getLowPrice()){
							price = hd.getLowPrice();
						}
					}else{
						if(list.size()>1){
							price = list.get(1).getPrice()+(int)(Double.parseDouble(auctionPrice)*100);
						}else{
							price = list.get(0).getPrice()+(int)(Double.parseDouble(auctionPrice)*100);
						}
					}
				}else{
					Long thisTimeStamp = hd.getStartTime().getTime();
					Long cTime = date.getTime()-thisTimeStamp;
					int second = (int)(cTime/1000/60);
					int db = second /hd.getTimeSection();
					price = hd.getDauctionPrice()-db*hd.getScopePrice();
					if(price<hd.getLowPrice()){
						price = hd.getLowPrice();
					}
				}
			}else{
				Long thisTimeStamp = hd.getStartTime().getTime();
				Long cTime = date.getTime()-thisTimeStamp;
				int second = (int)(cTime/1000/60);
				int db = second /hd.getTimeSection();
				price = hd.getDauctionPrice()-db*hd.getScopePrice();
				if(price<hd.getLowPrice()){
					price = hd.getLowPrice();
				}
			}
			realPrice = (double)price/100;
			System.out.println("####################dauctionNotice pushAuctionMsgggggggg################");
			Member member = memberMapper.selectByPrimaryKey(mId);
			String baseUrl = Contants.BIN_HUI_URL+"/bh-webserver/webPush/pushAuctionMsg";
			StringBuffer sb = new StringBuffer();
			sb.append(baseUrl);
			sb.append("?mId="+mId);
			sb.append("&currentPeriods="+currentPeriods);
			sb.append("&goodsId="+goodsId);
			//sb.append("&userName="+member.getUsername());
			//zlk
			if(member.getUsername()!=null) {
				   member.setUsername(URLDecoder.decode(member.getUsername(),"utf-8"));
				   sb.append("&userName="+member.getUsername());
			}
			//end
			sb.append("&headImg="+member.getHeadimgurl());
			sb.append("&auctionPrice="+realPrice.toString());
			//System.out.println(sb.toString());
			System.out.println("####################dauctionNotice pushAuctionMsgggggggg end ################");
			String ret = HttpService.doGet(sb.toString());
			if(ret.equals("true")){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		
	}

	/*public boolean dauctionNotice(Integer mId, String auctionPrice, String goodsId, String currentPeriods) {
		try {
			int price = 0;
			Double realPrice = null;
			Date date = new Date();
			Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodsId));
			HollandDauction dauction = new HollandDauction();
			dauction.setGoodsId(Integer.parseInt(goodsId));
			HollandDauction hd = mapper.getByGoodsId(dauction);
			
			HollandDauctionLog log = new HollandDauctionLog();
			log.setGoodsId(Integer.parseInt(goodsId));
			List<HollandDauctionLog> list = logMapper.getListByGoodsIdAndStatus(log);
			if(list.size()>0){
				HollandDauctionLog oldLog = list.get(0);  
				Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
				Long curTimeStamp = date.getTime();
				if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){ //拍卖中
					if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
						Long thisTimeStamp = oldLog.getEndTime().getTime(); //上期结束时间等于本期的开始时间
						Long cTime = date.getTime()-thisTimeStamp;
						int second = (int)(cTime/1000/60);
						int db = second /hd.getTimeSection();
						price = hd.getDauctionPrice()-db*hd.getScopePrice();
						if(price<hd.getLowPrice()){
							price = hd.getLowPrice();
						}
					}else{
						if(list.size()>1){
							price = list.get(1).getPrice()+(int)(Double.parseDouble(auctionPrice)*100);
						}else{
							price = list.get(0).getPrice()+(int)(Double.parseDouble(auctionPrice)*100);
						}
					}
				}else{
					Long thisTimeStamp = oldLog.getEndTime().getTime();
					Long cTime = date.getTime()-thisTimeStamp;
					int second = (int)(cTime/1000/60);
					int db = second /hd.getTimeSection();
					price = hd.getDauctionPrice()-db*hd.getScopePrice();
					if(price<hd.getLowPrice()){
						price = hd.getLowPrice();
					}
				}
			}else{
				Long thisTimeStamp = goods.getUpTime().getTime();
				Long cTime = date.getTime()-thisTimeStamp;
				int second = (int)(cTime/1000/60);
				int db = second /hd.getTimeSection();
				price = hd.getDauctionPrice()-db*hd.getScopePrice();
				if(price<hd.getLowPrice()){
					price = hd.getLowPrice();
				}
			}
			realPrice = (double)price/100;
			System.out.println("####################dauctionNotice pushAuctionMsgggggggg################");
			Member member = memberMapper.selectByPrimaryKey(mId);
			String baseUrl = Contants.BIN_HUI_URL+"/bh-webserver/webPush/pushAuctionMsg";
			StringBuffer sb = new StringBuffer();
			sb.append(baseUrl);
			sb.append("?mId="+mId);
			sb.append("&currentPeriods="+currentPeriods);
			sb.append("&goodsId="+goodsId);
			sb.append("&userName="+member.getUsername());
			sb.append("&headImg="+member.getHeadimgurl());
			sb.append("&auctionPrice="+realPrice.toString());
			//System.out.println(sb.toString());
			System.out.println("####################dauctionNotice pushAuctionMsgggggggg end ################");
			String ret = HttpService.doGet(sb.toString());
			if(ret.equals("true")){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		
	}*/

	/**
	 * <p>Description: 交保证金接口</p>
	 *  @author scj  
	 *  @date 2018年5月25日
	 */
	public int payDeposit(CashDeposit entity) {
		int row = 0;
		int price = 200;
		Wallet wallet =walletMapper.getWalletByUid(entity.getmId()).get(0);
		if(wallet!=null){
			wallet.setMoney(wallet.getMoney()-price);
			row = walletMapper.updateByPrimaryKey(wallet);
			row = insertWalletLog(entity.getmId(), price);
			row = insertCashDeposit(entity.getGoodsId(), entity.getmId(), price);
		}else{//钱包不存在
			return 666;
		}
		HollandDauctionLog log = new HollandDauctionLog();
		log.setGoodsId(entity.getGoodsId());
		log.setmId(entity.getmId());
		log.setCurrentPeriods(entity.getCurrentPeriods());
		try {
			dauctionLogService.insertFirst(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	
	/**
	 * <p>Description: 插入出账记录</p>
	 *  @author scj  
	 *  @date 2018年5月25日
	 */
	private int insertWalletLog(int mId, int amount){
		WalletLog entity = new WalletLog();
		entity.setmId(mId);
		entity.setAmount(amount);
		entity.setInOut(1); //出账
		entity.setAddTime(new Date());
		entity.setRemark("拍卖支付保证金");
		entity.setOrderNo(IDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
		entity.setStatus(1);
		entity.setType(0);
		return walletLogMapper.insertSelective(entity);
	}
	
	/**
	 * <p>Description: 插入押金支付记录</p>
	 *  @author scj  
	 *  @date 2018年5月29日
	 */
	private int insertCashDeposit(int goodsId, int mId, int price){
		CashDeposit entity = new CashDeposit();
		entity.setmId(mId);
		entity.setGoodsId(goodsId);
		entity.setDepositPrice(price);
		entity.setIsrefund(0);
		entity.setPayTime(new Date());
		HollandDauction hollandDauction=mapper.getListByGoodsId(goodsId).get(0);
		if(hollandDauction!=null){
			entity.sethId(hollandDauction.getId());
			entity.setCurrentPeriods(hollandDauction.getCurrentPeriods());
		}
		return cashDepositMapper.insertSelective(entity);
	}

	/**
	 * @Description: 退保证金
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54
	 */
	public void refundDeposit(int goodsId, int currentPeriods, int mId) {// mid该期成功的用户id
		// 获取某个用户某个商品的某一期交纳的保证金是多少
		CashDeposit findCashDeposit = new CashDeposit();// 查询条件
		findCashDeposit.setGoodsId(goodsId);
		findCashDeposit.setCurrentPeriods(currentPeriods);
		List<CashDeposit> cashDepositList = cashDepositMapper.getCashDeposit(findCashDeposit);
		for (CashDeposit cashDeposit : cashDepositList) {
			if (cashDeposit.getmId().intValue() != mId) {// 改期成功竞拍的人在这里不退
				Wallet wallet = walletMapper.getWalletByUid(cashDeposit.getmId()).get(0);
				wallet.setMoney(wallet.getMoney() + cashDeposit.getDepositPrice());
				walletMapper.updateByPrimaryKeySelective(wallet); // 更新钱包金额+

				cashDeposit.setIsrefund(1);// 已经退保证金
				cashDeposit.setRefundTime(new Date());// 退保证金时间
				cashDepositMapper.updateByPrimaryKeySelective(cashDeposit);

				this.insertRefundWalletLog(cashDeposit.getmId(), cashDeposit.getDepositPrice());// 插入钱包记录
			}
		}
	}

	/**
	 * @Description: 当商品价格小于保证金的时候,点击去结算调用此接口（不调用wxjsp接口）
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	public void depositJsp(String orderId,String addressId ) {
		Order order = orderMapper.selectByPrimaryKey(Integer.valueOf(orderId));//订单
		if(StringUtils.isNotEmpty(addressId)){
			order.setmAddrId(Integer.valueOf(addressId));//荷兰式订单允许修改地址
		}
		order.setPaymentStatus(2);
		order.setStatus(2);
		order.setPaytime(new Date());
		order.setPaymentId(4); //钱包支付
		orderMapper.updateByPrimaryKeySelective(order);
		OrderShop entity = new OrderShop();
		entity.setOrderNo(order.getOrderNo());
		OrderShop orderShop = orderShopMapper.selectByOrderNo(entity);
		orderShop.setStatus(2);
		orderShopMapper.updateByPrimaryKeySelective(orderShop);
		List<OrderSku> skuList = orderSkuMapper.getSkuListByOrderId(order.getId());
		Goods goods = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
		goods.setSale(goods.getSale()+1);
		goodsMapper.updateByPrimaryKeySelective(goods);
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuList.get(0).getSkuId());
		goodsSku.setStoreNums(goodsSku.getStoreNums()-1);
		goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
		
		HollandDauctionLog hollandDauctionLog = logMapper.getLogByOrderNo(order.getOrderNo());//拍卖记录
		
		/********************更新记录状态  start**************************/
		hollandDauctionLog.setPayTime(new Date());//支付时间
		hollandDauctionLog.setPayStatus(2);//'支付状态，0初始化，1待支付，2已支付'
		hollandDauctionLog.setdStatus(2);//拍卖状态，0有效，1无效，2拍卖成功，3拍卖失败
		logMapper.updateByPrimaryKeySelective(hollandDauctionLog);//更新记录
		/********************更新记录状态  end**************************/
		

		// 获取某个用户某个商品的某一期交纳的保证金是多少
		CashDeposit findCashDeposit = new CashDeposit();// 查询条件
		findCashDeposit.setmId(hollandDauctionLog.getmId());
		findCashDeposit.setGoodsId(hollandDauctionLog.getGoodsId());
		findCashDeposit.setCurrentPeriods(hollandDauctionLog.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		CashDeposit cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit).get(0);
		
		cashDeposit.setIsrefund(1);// 已经退保证金
		cashDeposit.setRefundTime(new Date());// 退保证金时间
		cashDepositMapper.updateByPrimaryKeySelective(cashDeposit);//更新保证金表
		
		int refundMonny=cashDeposit.getDepositPrice()-order.getOrderPrice();//退还的保证金
		if(refundMonny>0){
			/*********** 退还保证金 start ****************/	
			Wallet wallet = walletMapper.getWalletByUid(hollandDauctionLog.getmId()).get(0);
			wallet.setMoney(wallet.getMoney() + refundMonny);
			walletMapper.updateByPrimaryKeySelective(wallet); // 更新钱包金额
			/*********** 退还保证金 end ****************/	
			this.insertRefundWalletLog(hollandDauctionLog.getmId(),refundMonny);//插入钱包记录
		}
	}

	/**
	 * <p>Description: 插入退保证金出账记录</p>
	 *  @author xieyc 
	 *  @date 2018年5月25日
	 */
	private int insertRefundWalletLog(int mId, int amount){
		WalletLog saveWalletLog = new WalletLog();
		saveWalletLog.setmId(mId);
		saveWalletLog.setAmount(amount);
		saveWalletLog.setInOut(0); //进账
		saveWalletLog.setAddTime(new Date());
		saveWalletLog.setRemark("拍卖退保证金");
		saveWalletLog.setOrderNo(IDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
		saveWalletLog.setStatus(1);
		saveWalletLog.setType(0);
		return walletLogMapper.insertSelective(saveWalletLog);
	}

	/**
	 * 最新一期拍卖配置信息
	 */
	@Override
	public Map<String, Object> dauctionDetail(HollandDauction entity, Member member) {
		Map<String, Object> map = new LinkedHashMap<>();
		HollandDauction  d = new HollandDauction();
		d.setGoodsId(entity.getGoodsId());
		HollandDauction hd = mapper.getByGoodsId(d);
		if(hd!=null){
			map.put("currentPeriods", hd.getCurrentPeriods());
			map.put("dauctionPrice", (double)hd.getDauctionPrice()/100);
			map.put("loseTime", hd.getLoseTime());
			map.put("lowPrice", (double)hd.getLowPrice()/100);
			map.put("scopePrice", (double)hd.getScopePrice()/100);
			map.put("storeNums", hd.getStoreNums());
			map.put("timeSection", hd.getTimeSection());
			map.put("startTime", hd.getStartTime());
			
			if(member != null){
				//判断是否交押金
				CashDeposit findCashDeposit = new CashDeposit();
				findCashDeposit.setmId(member.getId());
				findCashDeposit.setGoodsId(entity.getGoodsId());
				findCashDeposit.setCurrentPeriods(hd.getCurrentPeriods());
				findCashDeposit.setIsrefund(0);
				List<CashDeposit> cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit);
				if(cashDeposit.size()>0){
					map.put("payDeposit", true);
				}else{
					map.put("payDeposit", false);
				}
			}else{
				map.put("payDeposit", false);
			}
			
			HollandDauctionLog log = new HollandDauctionLog();
			log.setGoodsId(entity.getGoodsId());
			List<HollandDauctionLog> logList = logMapper.getListByGoodsIdAndStatus(log);
			if(logList.size()>0){
				HollandDauctionLog oldLog = logList.get(0);
				if(oldLog.getPayStatus()==1 || oldLog.getPayStatus()==2){ //最新出价为待支付或者已支付状态
					map.put("price", null);
				}else{
					if(oldLog.getdStatus()==4){
						map.put("price", null);
					}else{
						map.put("price", (double)logList.get(logList.size()-1).getPrice()/100);
					}
				}
			}else{
				map.put("price", null);
			}
		}
		HollandDauction h = mapper.getCurrentTime();
		map.put("systemTime", h.getNowTime());
		return map;
	}
	/*public Map<String, Object> dauctionDetail(HollandDauction entity, Member member) {
		Map<String, Object> map = new LinkedHashMap<>();
		HollandDauction  d = new HollandDauction();
		d.setGoodsId(entity.getGoodsId());
		HollandDauction hd = mapper.getByGoodsId(d);
		if(hd!=null){
			Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
			map.put("currentPeriods", hd.getCurrentPeriods());
			map.put("dauctionPrice", (double)hd.getDauctionPrice()/100);
			map.put("loseTime", hd.getLoseTime());
			map.put("lowPrice", (double)hd.getLowPrice()/100);
			map.put("scopePrice", (double)hd.getScopePrice()/100);
			map.put("storeNums", hd.getStoreNums());
			map.put("timeSection", hd.getTimeSection());
			
			if(member != null){
				//判断是否交押金
				CashDeposit findCashDeposit = new CashDeposit();
				findCashDeposit.setmId(member.getId());
				findCashDeposit.setGoodsId(entity.getGoodsId());
				findCashDeposit.setCurrentPeriods(hd.getCurrentPeriods());
				findCashDeposit.setIsrefund(0);
				List<CashDeposit> cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit);
				if(cashDeposit.size()>0){
					map.put("payDeposit", true);
				}else{
					map.put("payDeposit", false);
				}
			}else{
				map.put("payDeposit", false);
			}
			
			HollandDauctionLog log = new HollandDauctionLog();
			log.setGoodsId(entity.getGoodsId());
			List<HollandDauctionLog> logList = logMapper.getListByGoodsIdAndStatus(log);
			if(logList.size()>0){
				HollandDauctionLog oldLog = logList.get(0);
				if(oldLog.getPayStatus()==1 || oldLog.getPayStatus()==2){ //最新出价为待支付或者已支付状态
					map.put("price", null);
				}else{
					if(oldLog.getdStatus()==4){
						map.put("price", null);
					}else{
						map.put("price", (double)logList.get(logList.size()-1).getPrice()/100);
					}
				}
			}else{
				map.put("price", null);
			}
			
			if(hd.getCurrentPeriods()==1){
				map.put("startTime", goods.getUpTime());
			}else{
				HollandDauctionLog lg = new HollandDauctionLog();
				lg.setGoodsId(goods.getId());
				lg.setCurrentPeriods(hd.getCurrentPeriods()-1);
				List<HollandDauctionLog> lgList = logMapper.getListByGoodsIdAndCurrentPeriods(lg);
				if(lgList.size()>0){
					map.put("startTime", lgList.get(0).getEndTime());
				}else{
					lg.setCurrentPeriods(hd.getCurrentPeriods()-2);
					List<HollandDauctionLog> hdlog = logMapper.getListByGoodsIdAndCurrentPeriods(lg);
					map.put("startTime", hdlog.get(0).getEndTime());
				}
			}
		}
		HollandDauction h = mapper.getCurrentTime();
		map.put("systemTime", h.getNowTime());
		return map;
	}*/

	@Override
	public Map<String, Object> nextDauctionDetail(HollandDauction entity) {
		Map<String, Object> map = new LinkedHashMap<>();
		HollandDauction  d = new HollandDauction();
		d.setGoodsId(entity.getGoodsId());
		HollandDauction hd = mapper.getByGoodsId(d);
		if(hd!=null){
			map.put("currentPeriods", entity.getCurrentPeriods()+1);
			map.put("dauctionPrice", (double)hd.getDauctionPrice()/100);
			map.put("lowPrice", (double)hd.getLowPrice()/100);
			map.put("scopePrice", (double)hd.getScopePrice()/100);
			map.put("storeNums", entity.getStoreNums());
			map.put("timeSection", hd.getTimeSection());
			map.put("payDeposit", false);
			map.put("price", null);
			map.put("startTime", hd.getLoseTime());
			
			//计算新一期流拍时间
			int middle = hd.getDauctionPrice() - hd.getLowPrice();
			int remainder = middle % hd.getScopePrice(); //余数
			int result = middle / hd.getScopePrice();  //去余数结果
			int num = remainder == 0 ? result+1:result+2;
			Long thisTimeStamp = (long) (num * hd.getTimeSection()*60000);
			Long reusltTimeStamp = hd.getLoseTime().getTime() + thisTimeStamp;
			Date date = new Date(reusltTimeStamp);			
			map.put("loseTime", date);
		}
		HollandDauction h = mapper.getCurrentTime();
		map.put("systemTime", h.getNowTime());
		return map;
	}

}
