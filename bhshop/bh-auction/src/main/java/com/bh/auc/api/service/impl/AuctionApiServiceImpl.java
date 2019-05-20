package com.bh.auc.api.service.impl;


import com.bh.auc.api.service.AuctionApiService;
import com.bh.auc.mapper.AuctionConfigMapper;
import com.bh.auc.mapper.AuctionHistoryMapper;
import com.bh.auc.mapper.AuctionRecordMapper;
import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.pojo.AuctionRecord;
import com.bh.auc.util.JedisUtil;
import com.bh.auc.vo.AuctionApiGoods;
import com.bh.auc.vo.BidMessage;
import com.bh.config.Contants;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class AuctionApiServiceImpl implements AuctionApiService {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    @Autowired
    private AuctionConfigMapper auctionConfigMapper;
    @Autowired
    private  AuctionRecordMapper auctionRecordMapper;
    @Autowired
    private AuctionHistoryMapper auctionHistoryMapper;
    /**
	 * @Description: 订单生成和退保证金测试
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	public void rendAucOrderAndRefundDeposit() {
		HttpResponse resp=null;
		
		try {
			HttpPost httpPost = new HttpPost("http://localhost:8080/bh-product-api/auction/rendAucOrderAndRefundDeposit.json");
			//HttpPost httpPost = new HttpPost(Contants.BIN_HUI_URL + "/bh-product-api/auction/rendAucOrderAndRefundDeposit.json");
			CloseableHttpClient client = HttpClients.createDefault();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("goodsId", "11778");
			jsonParam.put("mId", "14768");
			jsonParam.put("auctionPrice", "100");
			jsonParam.put("currentPeriods","589");// 当前期
			StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			resp = client.execute(httpPost);
		} catch (Exception e) {
			System.out.println("生成订单报错");
			e.printStackTrace();
		}
		String orderId =null;//订单id
		String orderNo =null;//订单号
		Integer statusCode=null;
		try {
			if (resp != null) {
				statusCode = resp.getStatusLine().getStatusCode();
				if (statusCode != null && statusCode == 200) {
					JSONObject jsonbject = JSONObject.fromObject(EntityUtils.toString(resp.getEntity()));
					JSONObject jsonbjectDate = JSONObject.fromObject(jsonbject.get("data"));
					orderId = String.valueOf(jsonbjectDate.get("id")); // 订单id
					orderNo = (String) jsonbjectDate.get("orderNo");// 订单号
				}
			}
		} catch (Exception e) {
			System.out.println("获取订单号和订单id报错");
			e.printStackTrace();
		}
		System.out.println(orderId+"  "+orderNo);
	}
	
	/**
	 * @Description: 退保证金测试
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	public void refundDeposit() {
		try {
			HttpPost httpPost = new HttpPost("http://localhost:8080/bh-product-api/auction/rendAuctionOrder.json");
			//HttpPost httpPost = new HttpPost(Contants.BIN_HUI_URL + "/bh-product-api/auction/refundDeposit.json");
			CloseableHttpClient client = HttpClients.createDefault();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("goodsId", "11778");
			jsonParam.put("mId", "14768");
			jsonParam.put("currentPeriods", "32");
			StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse resp = client.execute(httpPost);
			System.out.println(EntityUtils.toString(resp.getEntity()));
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
    /**
     * @Description: app拍卖商品列表
     * @author xieyc
     * @date 2018年7月6日 下午2:06:15 7
     */
    public List<AuctionApiGoods> auctionApiGoodList(AuctionConfig entity) {
        entity.setPageSize(Contants.PAGE_SIZE);//每页的数量
        entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage()) - 1) * entity.getPageSize());
        List<AuctionApiGoods> returnList = auctionConfigMapper.getAuctionApiGoodList(entity);//查询商家商家并且库存》0的拍卖商品
        return returnList;
    }

    /**
     * @throws Exception
     * @Description:api商品详情
     * @author xieyc
     * @date 2018年7月3日 下午5:41:31
     */
    public Map<String, Object> auctionApiGoodDetails(int goodsId,Integer mId,Integer currentPeriods) throws Exception {

        Map<String, Object> returnMap = new HashMap<String, Object>();
        AuctionConfig auctionConfig = auctionConfigMapper.getByGoodsId(goodsId);
        auctionConfig.setCurrentPage(currentPeriods);

        /*********************传入参数中获取*********************************/
        returnMap.put("mId", mId);//当前登入用户id
        returnMap.put("goodsId", auctionConfig.getGoodsId());//商品id
        returnMap.put("currentPeriods", currentPeriods);//当前期
        returnMap.put("sysCode", auctionConfig.getSysCode());//商品系统来源标识
        returnMap.put("goodsName", auctionConfig.getGoodsName());//商品名字
        returnMap.put("goodsImage", auctionConfig.getGoodsImage());//商品图片
        /*********************传入参数中获取*********************************/

        /**************************从公告板中获取的信息******************************/
        JedisUtil jedisUtil = JedisUtil.getInstance();
        final JedisUtil.Strings strings = jedisUtil.new Strings();
        BidMessage bidMessage = this.strToObeject(strings, auctionConfig);//获取当前公告板对象
        if(bidMessage!=null){
        	 returnMap.put("auctionPrice", (double) bidMessage.getAuctionPrice() / 100);//商品拍卖价格
             returnMap.put("headImg", bidMessage.getHeadImg());//用户头像
             returnMap.put("userName", bidMessage.getUserName());//用户昵号
             returnMap.put("curTime", bidMessage.getCurTime());//出价时间
             returnMap.put("binMId", bidMessage.getMid());//公告板用户id
             Date curTime = new Date(bidMessage.getCurTime());
             long countDown = (new Date().getTime() - curTime.getTime()) / 1000;
             if (countDown < 0) {
                 countDown = 0;
             }
             returnMap.put("countDown", countDown);//倒计时开始时间点
        }else{
        	 returnMap.put("auctionPrice","");//商品拍卖价格
             returnMap.put("headImg","");//用户头像
             returnMap.put("userName","");//用户昵号
             returnMap.put("curTime", "");//出价时间
             returnMap.put("binMId", "");//用户id
             returnMap.put("countDown", null);//倒计时开始时间点
        }
        /**************************从公告板中获取的信息******************************/
        
        AuctionRecord findAuctionRecord=new AuctionRecord();
        findAuctionRecord.setCurrentPeriods(currentPeriods);
        findAuctionRecord.setGoodsId(goodsId);
        List<AuctionRecord> bidsList=auctionRecordMapper.listPage(findAuctionRecord);
        for (AuctionRecord auctionRecord : bidsList) {
            auctionRecord.setRealAucPrice((double)auctionRecord.getAucPrice()/100);
        }
        returnMap.put("bidsList", bidsList);//出价记录列表
        
        return returnMap;
    }
    /**
     * @Description: 获取公告板 BidMessage对象
     * @author xieyc
     * @date 2018年7月6日 下午4:35:17
     */
    private BidMessage strToObeject(JedisUtil.Strings strings, AuctionConfig entity) {
        /*BidMessage bidMessage1 = new BidMessage();
		bidMessage1.setMid(14768);
		bidMessage1.setUserName("谢元春");
		bidMessage1.setHeadImg("http://bhs-oss.bh2015.com/base64images/D258ABD5CE8545D09559B629CCEB38F8.jpg");
		bidMessage1.setAuctionPrice(100);
		bidMessage1.setSysCode(entity.getSysCode());
		bidMessage1.setGoodsId(entity.getGoodsId());
		bidMessage1.setCurrentPeriods(entity.getCurrentPeriods());
		Date date=new Date();
		date.setSeconds(date.getSeconds() - 5); 
		bidMessage1.setSysTime(df.format(new Date())); // 为获取当前系统时间
		bidMessage1.setCurTime(df.format(date)); // 为获取当前系统时间
		bidMessage1.setType(1);
		String bidMsgJson1 = JsonUtils.objectToJson(bidMessage1);
		String periodTag1=entity.getSysCode()+"-"+entity.getGoodsId()+"-"+entity.getCurrentPeriods();//bhshop-15794-1
		strings.set(periodTag1, bidMsgJson1);*/

        String periodTag = entity.getSysCode() + "-" + entity.getGoodsId() + "-" + entity.getCurrentPeriods();//bhshop-15794-1
        String bidMsgJson = strings.get(periodTag);//获取当前公告板的信息
        JSONObject jsonObject = JSONObject.fromObject(bidMsgJson);
        BidMessage bidMessage = (BidMessage) JSONObject.toBean(jsonObject, BidMessage.class);//公告板转化为对象

        return bidMessage;
    }

    /**
     * @Description: api用户拍卖历史记录
     * @author xieyc
     * @date 2018/7/23 10:49
     */
    public List< Map<String, Object> > apiUserAuctionRecord(AuctionRecord entity) {
        List <Map<String, Object>> retrunList=new ArrayList<Map<String, Object>>();
        if(entity.getmId()!=null){//没有登入的时候 mId是null
            entity.setPageSize(Contants.PAGE_SIZE);//每页的数量
            entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage()) - 1) * entity.getPageSize());
            List<AuctionRecord> listAuctionRecord = auctionRecordMapper.apiUserAuctionRecord(entity);
            for (AuctionRecord auctionRecord : listAuctionRecord) {
                Map<String, Object> retrunMap=new HashMap<String, Object>();
                AuctionConfig auctionConfig=auctionConfigMapper.getByGoodsId(auctionRecord.getGoodsId());//获取配置详情
                retrunMap.put("goodsName",auctionConfig.getGoodsName());//商品名字
                retrunMap.put("goodsImage",auctionConfig.getGoodsImage());//商品名字
                retrunMap.put("currentPeriods",auctionRecord.getCurrentPeriods());//哪一期
                //这个用户是否在某个商品的某一期拍卖成功
                AuctionHistory auctionHistory=auctionHistoryMapper.getByMidAndCurrentPeriods(auctionConfig.getId(),auctionRecord.getCurrentPeriods(),entity.getmId());
                boolean isSuccess=false;//这期拍卖失败
                if(auctionHistory!=null){
                    retrunMap.put("bargainPrice",(double)auctionHistory.getBargainPrice()/100);//哪一期
                    isSuccess=true;//这期拍卖成功
                }
                retrunMap.put("isSuccess",isSuccess);//是否拍卖成功

                retrunList.add(retrunMap);
            }
        }
        return retrunList;
    }
}
