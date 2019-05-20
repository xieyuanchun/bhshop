package com.bh.auc.job;

import com.bh.auc.mapper.AuctionHistoryMapper;
import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.util.JedisUtil;
import com.bh.config.Contants;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 任务Handler的一个Demo（Bean模式） 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解； 3、添加
 * “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，
 * 该名称对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xieyc
 * @Description:定时检查拍卖订单是否生成
 * @date 2018年7月17日 下午3:06:51
 */
@JobHander(value = "rendAucOrderJob")
@Component
public class RendAucOrderJob extends IJobHandler {
    private final static String ENCODE = "UTF-8";
    @Autowired
    private AuctionHistoryMapper auctionHistoryMapper;

    @Override
    public synchronized ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("XXL-JOB, Hello World.");
        try {
            List<AuctionHistory> listHistory = auctionHistoryMapper.getNoRendOrderBargainRecord();// 获取没有生成订单的的成交记录
            for (AuctionHistory auctionHistory : listHistory) {
                if (auctionHistory.getSchedulNum() <= 3) {//检查4次以下的
                    //HttpPost httpPost = new HttpPost("http://localhost:8080/bh-product-api/auction/rendAucOrderAndRefundDeposit.json");
                    HttpPost httpPost = new HttpPost(Contants.BIN_HUI_URL + "/bh-product-api/auction/rendAucOrderAndRefundDeposit.json");
                    CloseableHttpClient client = HttpClients.createDefault();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("goodsId", auctionHistory.getGoodsId());// 商品id
                    jsonParam.put("mId", auctionHistory.getBargainMid());// 拍中的用户id
                    jsonParam.put("auctionPrice", auctionHistory.getBargainPrice());// 成交价格
                    jsonParam.put("currentPeriods", auctionHistory.getCurrentPeriods());// 当前期
                    StringEntity entity = new StringEntity(jsonParam.toString(), ENCODE);// 解决中文乱码问题
                    entity.setContentEncoding(ENCODE);
                    entity.setContentType("application/json");
                    httpPost.setEntity(entity);
                    HttpResponse resp = client.execute(httpPost);

                    String orderId = null;// 订单id
                    String orderNo = null;// 订单号
                    Integer statusCode = null;//响应状态码（200成功）
                    try {
                        if (resp != null) {
                            statusCode = resp.getStatusLine().getStatusCode();//获取请求状态码
                            if (statusCode != null && statusCode == 200) {
                                JSONObject jsonbject = JSONObject.fromObject(EntityUtils.toString(resp.getEntity()));
                                JSONObject jsonbjectDate = JSONObject.fromObject(jsonbject.get("data"));
                                orderId = String.valueOf(jsonbjectDate.get("id")); // 订单id
                                orderNo = (String) jsonbjectDate.get("orderNo");// 订单号
                            }
                        }
                    } catch (Exception e) {
                        XxlJobLogger.log("获取订单id和订单号  error=" + e.getMessage());
                        e.printStackTrace();
                    }
                    if (orderId != null) {
                        auctionHistory.setOrderId(Integer.valueOf(orderId));//订单id
                        auctionHistory.setOrderNo(orderNo);//订单号
                    }
                    auctionHistory.setUpdateTime(new Date(JedisUtil.getInstance().time()));
                    auctionHistory.setSchedulNum(auctionHistory.getSchedulNum() + 1);//调度次数+1
                    auctionHistoryMapper.updateByPrimaryKeySelective(auctionHistory);
                }
            }
        } catch (Exception e) {
            XxlJobLogger.log("RendAucOrderJob  error=" + e.getMessage());
            e.printStackTrace();
        }
        return ReturnT.SUCCESS;
    }
}
