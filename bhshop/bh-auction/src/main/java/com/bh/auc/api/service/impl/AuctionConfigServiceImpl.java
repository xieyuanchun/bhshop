package com.bh.auc.api.service.impl;

import com.bh.auc.api.service.AuctionConfigService;
import com.bh.auc.mapper.AuctionConfigMapper;
import com.bh.auc.mapper.AuctionHistoryMapper;
import com.bh.auc.mapper.AuctionRecordMapper;
import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.pojo.AuctionRecord;
import com.bh.auc.pojo.BhShopGoodsInfo;
import com.bh.auc.util.JedisUtil;
import com.bh.auc.vo.AuctionHistoryVo;
import com.bh.config.Contants;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuctionConfigServiceImpl implements AuctionConfigService{
	
	@Autowired
	private AuctionConfigMapper mapper;
	@Autowired
	private AuctionHistoryMapper auctionHistoryMapper;
	@Autowired
	private AuctionRecordMapper auctionRecordMapper;

	/**
	 * @Description: 测试
	 * @author xieyc
	 * @date 2018/7/27 16:03
	 */
	public void test() {
		List<AuctionRecord> listWaitInsert = new ArrayList<AuctionRecord>();
		for(int i = 0; i < 2; i++) {
			AuctionRecord saveAuctionRecord = new AuctionRecord();
			saveAuctionRecord.setSysCode("bhshop");//商品系统来源
			saveAuctionRecord.setGoodsId(123);//商品id
			saveAuctionRecord.setGoodsSkuId(456);//商品skuid
			saveAuctionRecord.setmId(14768);//出价用户id
			saveAuctionRecord.setHeadImg("666");//出价用户头像
			saveAuctionRecord.setUserName("谢元春");//出价用户名字
			saveAuctionRecord.setAucId(1);//配置id
			saveAuctionRecord.setCurrentPeriods(1);//当前期
			saveAuctionRecord.setAucPrice(100);//出价价格
			saveAuctionRecord.setAddTime(new Date());//出价时间
			listWaitInsert.add(saveAuctionRecord);
		}
		auctionRecordMapper.insertBatch(listWaitInsert);

	}

	/**
	 * @Description: 获取滨惠商城商品信息
	 * @author xieyc
	 * @date 2018年7月11日 下午2:57:02 
	 */
	public BhShopGoodsInfo getBhShopGoodsInfo(Integer goodsId){
		BhShopGoodsInfo bhShopGoodsInfo=new  BhShopGoodsInfo();
		try {
			//HttpPost httpPost = new HttpPost("http://localhost:8080/bh-admin/hollandDauction/getBhShopGoodsInfo.json");
			HttpPost httpPost = new HttpPost(Contants.BIN_HUI_URL+"/bh-admin/hollandDauction/getBhShopGoodsInfo.json");
			CloseableHttpClient client = HttpClients.createDefault();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("goodsId", goodsId);
			StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse resp = client.execute(httpPost);
			JSONObject jsonObject = JSONObject.fromObject(EntityUtils.toString(resp.getEntity()));
			bhShopGoodsInfo = (BhShopGoodsInfo) JSONObject.toBean(jsonObject, BhShopGoodsInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bhShopGoodsInfo;
	}



	/**
	 * @Description: 去拍卖接口（将商品设置成拍卖商品）
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37 
	 */
	public int goAuction(String strGoodsId, String strStoreNum, String sysCode) {
		int  row =0;
		int  goodsId=Integer.valueOf(strGoodsId);//商品id
		int  storeNum=Integer.valueOf(strStoreNum);//拍卖库存
		
		if(storeNum<=0){
			return -2;//拍卖库存要设置成大于0
		}
		BhShopGoodsInfo bhShopGoodsInfo=this.getBhShopGoodsInfo(goodsId);
		
		if(bhShopGoodsInfo.getStoreNums()<storeNum){//判断设置的库存是否大于商品的库存
			return -1;//商品库存不足
		}
		
		AuctionConfig saveOrUpdateACfig=new AuctionConfig();//更新或报存对象
		JSONObject jsonObject = JSONObject.fromObject(bhShopGoodsInfo.getValue()); //value转义
		JSONArray jsonArray=jsonObject.getJSONArray("url");
		saveOrUpdateACfig.setGoodsImage((String)jsonArray.get(0));//商品图片
		saveOrUpdateACfig.setGoodsSkuId(bhShopGoodsInfo.getSkuId());//商品skuId
		saveOrUpdateACfig.setGoodsName(bhShopGoodsInfo.getGoodsName());//商品名字
		saveOrUpdateACfig.setGoodsId(goodsId);//商品id
		saveOrUpdateACfig.setSysCode(sysCode);//商品系统来源标识
		
		AuctionConfig auctionConfig=mapper.getByGoodsId(goodsId);
		if(auctionConfig!=null){//不为null更新配置表
			if(auctionConfig.getUpDownStatus()==1){
				return -3;
			}
			int oldStoreNums=auctionConfig.getStoreNum();//原来库存剩余量
			if(oldStoreNums==0){//修改的时候原来库存为0的时候，当前期数要+1
				saveOrUpdateACfig.setCurrentPeriods(auctionConfig.getCurrentPeriods()+1);
			}
			saveOrUpdateACfig.setStoreNum(auctionConfig.getStoreNum()+storeNum);//增加拍卖库存
			saveOrUpdateACfig.setId(auctionConfig.getId());
			row=mapper.updateByPrimaryKeySelective(saveOrUpdateACfig);
		}else{//为空插入配置表
			saveOrUpdateACfig.setStoreNum(storeNum);//设置拍卖库存
			saveOrUpdateACfig.setAddTime(new Date());//添加时间
			row= mapper.insertSelective(saveOrUpdateACfig);
		}
		return row;
	}
	/**
	 * @Description: 设置拍卖配置
	 * @author xieyc
	 * @date 2018年5月21日 下午4:04:24  
	 */
	public int setAuctionConfig(AuctionConfig entity) {
		int row =0;
		if (entity.getActPrice() == 0 || entity.getLowPrice() == 0 || entity.getTimeSection() == 0
				|| entity.getScopePrice() == 0) {
			return -1;// 参数配置不正确（降价值、拍卖价格、最低价格、降价时间区间 要大于0）
		}
		AuctionConfig auctionConfig=mapper.getByGoodsId(entity.getGoodsId());
		entity.setId(auctionConfig.getId());//配置id
		row=mapper.updateByPrimaryKeySelective(entity);
		return row;
	}
	/**
	 * @Description: 拍卖配置详情
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37 
	 */
	public AuctionConfig auctionConfigDetail(int goodsId) {
		AuctionConfig auctionConfig=mapper.getByGoodsId(goodsId);
		if(auctionConfig!=null&& auctionConfig.getLowPrice() !=0 &&auctionConfig.getActPrice()!=0 ){
			double realLowPrice=(double)auctionConfig.getLowPrice()/100;//最低价
			double realActPrice=(double)auctionConfig.getActPrice()/100;//拍卖价
			double realScopePrice=(double)auctionConfig.getScopePrice()/100;//将价值
			double realCashDeposit=(double)auctionConfig.getCashDeposit()/100;//保证金
			double realHighPrice=(double)auctionConfig.getHighPrice()/100;//最高价
			
			auctionConfig.setRealActPrice(realActPrice);
			auctionConfig.setRealLowPrice(realLowPrice);
			auctionConfig.setRealScopePrice(realScopePrice);
			auctionConfig.setRealCashDeposit(realCashDeposit);
			auctionConfig.setRealHighPrice(realHighPrice);
		}
		return auctionConfig;
	}
	/**
	 * @Description: 拍卖商品列表
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40 
	 */
	public PageBean<AuctionConfig> auctionGoodList(AuctionConfig entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize(), true);
		List<AuctionConfig> list = mapper.listPage(entity);
		
		for (AuctionConfig auctionConfig : list) {
			double realLowPrice=(double)auctionConfig.getLowPrice()/100;//最低价
			double realActPrice=(double)auctionConfig.getActPrice()/100;//拍卖价
			double realScopePrice=(double)auctionConfig.getScopePrice()/100;//将价值
			double realCashDeposit=(double)auctionConfig.getCashDeposit()/100;//保证金
			double realHighPrice=(double)auctionConfig.getHighPrice()/100;//最高价
			
			auctionConfig.setRealActPrice(realActPrice);
			auctionConfig.setRealLowPrice(realLowPrice);
			auctionConfig.setRealScopePrice(realScopePrice);
			auctionConfig.setRealCashDeposit(realCashDeposit);
			auctionConfig.setRealHighPrice(realHighPrice);
		}
		PageBean<AuctionConfig> page = new PageBean<>(list);
		return page;
	}
	
	/**
	 * @Description: 上架或下降拍卖商品
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37 
	 */
	public int upDownAuctionGoods(int id,int upDownStatus) {
		int  row=0;
		AuctionConfig auctionConfig= mapper.selectByPrimaryKey(id);
		auctionConfig.setUpDownStatus(upDownStatus);
		if(upDownStatus==1){//上架的时候
			if (auctionConfig.getLowPrice() == 0 || auctionConfig.getActPrice() == 0
					|| auctionConfig.getTimeSection() == 0 || auctionConfig.getScopePrice() == 0) {
				return -1;//请在设置完拍卖参数或设置正确的参数后再上架(降价值、拍卖价格、最低价格、降价时间区间 要大于0才允许上架)
			}
		}
		row=mapper.updateByPrimaryKeySelective(auctionConfig);
		return  row;
	}

	/**
	 * @Description: 每期的拍卖记录列表
	 * @author xieyc
	 * @date 2018/7/23 10:49
	 */
	public PageBean<AuctionHistoryVo> auctionHistoryList(AuctionHistory entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize(), true);
		List<AuctionHistoryVo> list = auctionHistoryMapper.listPage(entity);
		PageBean<AuctionHistoryVo> page = new PageBean<>(list);
		return page;
	}


	/**
	 * 拍卖师进程
	 */
	@Override
	public int auctioneerProcess() throws Exception {
		JedisUtil jedisUtil= JedisUtil.getInstance();
		final JedisUtil.Strings strings=jedisUtil.new Strings();
		List<AuctionConfig> list = mapper.getAllWaitAcu();
		if(list.size()>0){
			for(final AuctionConfig entity : list){
				//codeNum:系统来源标识-商品id
				final String codeNum = entity.getSysCode()+"-"+entity.getGoodsId();
				//codeValue:用户id_商品id_当前价格_期数
				final String codeValue = "0_"+entity.getGoodsId()+"_"+(double)entity.getActPrice()/100+"_"+entity.getCurrentPeriods();
				strings.setnx(codeNum, codeValue);
				String json = strings.get(codeNum);
				if(StringUtils.isBlank(json)){
					strings.set(codeNum, codeValue);
				}
				final Long timeSecond = (long)entity.getTimeSection()*60*1000;
				//定义一个拍卖师
				Thread thread = new Thread(){
				   public void run(){
					   addAuctioneer(timeSecond, strings, codeNum, codeValue, entity.getLowPrice()/100);
				   }
				};
				thread.start();
			}
		}
		return 0;
	}

	public int addAuctioneer(Long time,final JedisUtil.Strings strings, final String codeNum, final String codeValue, final Integer lowPrice){
		int currentDwonPrice = 0;
		Boolean waitMq = false;
		String json = strings.get(codeValue);
		if(StringUtils.isNotBlank(json)){
			String[] params = json.split("_");
			if(params[0].equals(0)){
				int chaPrice = MoneyUtil.yuan2Fen(params[2])- lowPrice*2;//计算正常下降的价格
				if(chaPrice>0){
					currentDwonPrice = MoneyUtil.yuan2Fen(params[2]) - lowPrice;
				}else{
					currentDwonPrice =  lowPrice;
				}
				final String nextValue = "0_"+params[1]+"_"+(double)currentDwonPrice/100+"_"+params[3];
				Timer timer = new Timer();
		        timer.schedule(new TimerTask() {
			        public void run() {
			        	//1、更新公告板
			        	strings.set(codeNum, nextValue);
			        	//2、发布消息到Mq
			        }
		        }, time, time);// 设定指定的时间time,此处为毫秒

		        if(waitMq){//收到mq的消息--表明有人出价
		        	//1、从mq订阅出价

		        	//2、如果mq的价格大于公告板的价格，更新公告板


	        		//3、发布消息到Mq

		        	//4、跳出定时器
		        	timer.cancel();
		        }
			}else{
				final Timer timer = new Timer();
		        timer.schedule(new TimerTask() {
			        public void run() {
			        	//1、下锤操作

			        	//2、发布消息到Mq

			        }
		        }, 30000);// 设定30秒倒计时

		        if(waitMq){//收到mq的消息--表明有人出价

		        	//2、如果mq的价格大于公告板的价格，更新公告板

	        		//3、发布消息到Mq

		        	//4、跳出定时器
		        	timer.cancel();
		        }
			}
		}
        return 0;
	}





}
