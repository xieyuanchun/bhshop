package com.order.job;


import com.bh.jd.api.JDStockApi;
import com.bh.order.mapper.JdOrderMainMapper;
import com.bh.order.mapper.JdOrderSkuMapper;
import com.bh.order.pojo.JdOrderMain;
import com.bh.order.pojo.JdOrderSku;
import com.bh.utils.JedisUtil;
import com.bh.utils.MoneyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务Handler的一个Demo（Bean模式） 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解； 3、添加
 * “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，
 * 该名称对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xieyc
 * @Description:定时更新京东订单信息
 * @date 2018年7月17日 下午3:06:51
 */
@JobHander(value ="jdOrderInfoUpdateJob")
@Component
public class JdOrderInfoUpdateJob extends IJobHandler {
    private final static String ENCODE = "UTF-8";
    
    @Autowired
    private  JdOrderMainMapper jdOrderMainMapper;
    @Autowired
    private  JdOrderSkuMapper jdOrderSkuMapper;
    
	@Override
	public synchronized ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");
		try {
			// 物流状态:0是新建  订单状态:1-为有效订单 订单类型:2-是子订单
			List<JdOrderMain> jdOrderMainList = jdOrderMainMapper.getUpdateJdOrderMain();
			for (JdOrderMain jdOrderMain : jdOrderMainList) {
				String jdOrderStr = JDStockApi.SelectJdOrder(jdOrderMain.getJdOrderId());// 查询京东订单信息接口
				JSONObject jdOrder = JSONObject.fromObject(jdOrderStr);
				JSONObject jdOrderResult = (JSONObject) jdOrder.get("result");
				
				boolean falg=true;
				String s = jdOrderResult.getString("pOrder");
				try {
					JSONObject jsonObject = JSONObject.fromObject(s);
					falg=false;
				} catch(Exception e ) {
				}
				Object o = (Object) jdOrderResult.get("pOrder");
				if (falg) {// 更新该子订单
					JdOrderMain updateJdOrderMain = new JdOrderMain();
					updateJdOrderMain.setId(jdOrderMain.getId());
					updateJdOrderMain.setState(jdOrderResult.getInt("state"));// 物流状态:0-是新建,1-是妥投,2-是拒收
					updateJdOrderMain.setSubmitState(jdOrderResult.getInt("submitState"));// 是否确定下单：0-为未确认，1-为确认
					updateJdOrderMain.setOrderState(jdOrderResult.getInt("orderState"));// 订单状态: 0-为取消订单, 1-为有效
					updateJdOrderMain.setOrderPrice(MoneyUtil.doubeToInt(jdOrderResult.getDouble("orderPrice")));// 京东订单价格
					updateJdOrderMain.setFreight(jdOrderResult.getInt("freight"));// 运费（合同配置了才返回）
					updateJdOrderMain.setEditTime(new Date(JedisUtil.getInstance().time()));// 编辑时间
					jdOrderMainMapper.updateByPrimaryKeySelective(updateJdOrderMain);
				} else {// 父订单
					JdOrderMain updateJdOrderMain = new JdOrderMain();
					updateJdOrderMain.setId(jdOrderMain.getId());
					updateJdOrderMain.setType(1);//更新子订单为父订单
					jdOrderMainMapper.updateByPrimaryKeySelective(updateJdOrderMain);
					//生成该父订单的子订单
					JSONArray cOrderArray =JSONArray.fromObject(jdOrderResult.get("cOrder"));
					
					for (Object object : cOrderArray) {
						JSONObject ob =JSONObject.fromObject(object);//某个子订单
						JSONArray cOrderSkuArray =JSONArray.fromObject(ob.get("sku"));//某个子订单的sku
						
						JdOrderMain saveJdOrderMain=new JdOrderMain();
						saveJdOrderMain.setJdOrderId(ob.getLong("jdOrderId")+"");//京东订单编号
						saveJdOrderMain.setState(ob.getInt("state"));//物流状态:0-是新建,1-是妥投,2-是拒收
						saveJdOrderMain.setParentOrderId(ob.getString("pOrder"));//父订单号
						saveJdOrderMain.setType(ob.getInt("type"));//订单类型: 1-是父订单, 2-是子订单
						saveJdOrderMain.setSubmitState(ob.getInt("submitState"));//是否确定下单：0-为未确认，1-为确认
						saveJdOrderMain.setOrderState(ob.getInt("orderState"));//订单状态: 0-为取消订单 , 1-为有效
						saveJdOrderMain.setOrderPrice(MoneyUtil.doubeToInt(ob.getDouble("orderPrice")));//京东订单价格
						saveJdOrderMain.setFreight(ob.getInt("freight"));//运费（合同配置了才返回）
						saveJdOrderMain.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
						saveJdOrderMain.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
						saveJdOrderMain.setOrderShopId(jdOrderMain.getOrderShopId());//商家订单id
						saveJdOrderMain.setSendState(0);//京东配送状态
						jdOrderMainMapper.insertSelective(saveJdOrderMain);
						
						/*********** jd_order_sku 插入记录xieyc start **********/
						for (Object obsku : cOrderSkuArray) {
							JSONObject obSku = JSONObject.fromObject(obsku);
							JdOrderSku saveJdOrderSku = new JdOrderSku();
							saveJdOrderSku.setSkuId(obSku.getLong("skuId"));
							saveJdOrderSku.setCategory(obSku.getLong("category"));
							saveJdOrderSku.setNum(obSku.getInt("num"));
							//saveJdOrderSku.setName(obSku.getString("name"));
							saveJdOrderSku.setPrice(MoneyUtil.doubeToInt(obSku.getDouble("price")));
							saveJdOrderSku.setAddTime(new Date(JedisUtil.getInstance().time()));
							saveJdOrderSku.setEditTime(new Date(JedisUtil.getInstance().time()));
							saveJdOrderSku.setJdMainId(saveJdOrderMain.getId());
							JdOrderSku jdOrderSku=jdOrderSkuMapper.getByJdMainIdAndSkuId(jdOrderMain.getId(),obSku.getLong("skuId"));
							saveJdOrderSku.setOrderSkuId(jdOrderSku.getOrderSkuId());
							
							jdOrderSkuMapper.insertSelective(saveJdOrderSku);
						}
					}
				}
			}
		} catch (Exception e) {
			XxlJobLogger.log("定时更新京东订单信息  error=" + e.getMessage());
			e.printStackTrace();
		}
		return ReturnT.SUCCESS;
	}
}
