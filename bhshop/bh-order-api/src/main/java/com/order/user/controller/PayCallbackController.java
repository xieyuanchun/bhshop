package com.order.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.bh.bean.Sms;
import com.bh.config.AlipayConfig;
import com.bh.config.Contants;
import com.bh.config.SwiftpassConfig;
import com.bh.enums.OrderStatusEnum;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.goods.pojo.TopicSeckillLog;
import com.bh.order.mapper.MsnApplyMapper;
import com.bh.order.mapper.RechargePhoneMapper;
import com.bh.order.pojo.MsnApply;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSeed;
import com.bh.order.pojo.RechargePhone;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.JedisUtil;
import com.bh.utils.MoneyUtil;
import com.bh.utils.SmsUtil;
import com.bh.utils.maths.MathsTool;
import com.bh.utils.recharge.MobileRecharge;
import com.order.shop.service.OrderTeamService;
import com.order.user.service.PayCallbackService;
import com.order.user.service.SeedService;
import com.order.user.service.ShopMService;
import com.order.user.service.UserOrderService;
import com.order.util.smallAppPay.PayCallbackNotify;
import com.order.util.smallAppPay.PayUtils;
import com.order.util.swiftpass.SignUtils;
import com.order.util.swiftpass.XmlUtils;
import com.wechat.service.WechatTemplateMsgService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 支付回调
 *
 * @author xxj
 */
@Controller
@RequestMapping("/payCallback")
public class PayCallbackController {
    private static final Logger logger = LoggerFactory.getLogger(PayCallbackController.class);
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private OrderTeamService orderTeamService;
    @Autowired
    private SeedService seedService;
    @Autowired
    private ShopMService shopMService;
    @Autowired
    private RechargePhoneMapper rpMapper;
    @Autowired
    private WalletLogMapper walletLogMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private WechatTemplateMsgService templateMsgService;
    @Autowired
    private PayCallbackService payCallbackService;
    @Autowired
	private MemberShopMapper memberShopMapper;
    @Autowired
	private MsnApplyMapper msnApplyMapper;

    @RequestMapping(value = "/alipayCallback")
     @ResponseBody
     public String alipayCallback(HttpServletRequest request){
      try {
              Map<String,String> params = new HashMap<String,String>();
              Map<String,String[]> requestParams = request.getParameterMap();
              for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                  String name = (String) iter.next();
                  String[] values = (String[]) requestParams.get(name);
                  String valueStr = "";
                  for (int i = 0; i < values.length; i++) {
                      valueStr = (i == values.length - 1) ? valueStr + values[i]
                              : valueStr + values[i] + ",";
                  }
                  params.put(name, valueStr);
              }

              boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipayPublicKey, AlipayConfig.charset, AlipayConfig.signType);
              if(signVerified) {//验证成功
                  //商户订单号
                  String outTradeNo =request.getParameter("out_trade_no");
                  //支付宝交易号
                  String tradeNo = request.getParameter("trade_no");
                  //交易状态
                  String tradeStatus = request.getParameter("trade_status");
                  String attachedData=request.getParameter("body");
                   //交易完成 订单已关闭
                  if(tradeStatus.equals("TRADE_FINISHED")){

                  }
                  //交易成功
                  else if (tradeStatus.equals("TRADE_SUCCESS")){
                	  callbackProcess(outTradeNo,attachedData,tradeNo);
                  }
                  return "success";

              }else {//验证失败
                  return "fail";
                  //调试用，写文本函数记录程序运行情况是否正常

              }

      } catch (Exception e) {
          return "error";
      }


     }
    
    /*
     @RequestMapping(value = "/weixinCallback")
      @ResponseBody
      public String weixinCallback(HttpServletRequest request) {
          System.out.println("###########weixinCallback#############");
          try {
                  PayCallbackNotify notifyBean = PayUtils.payCallbackNotify(request.getInputStream());
                  if(notifyBean!=null){
                  System.out.println("###########weixinCallback sucess#############");
                  String out_trade_no = notifyBean.getOut_trade_no();
                  String attach =notifyBean.getAttach();
                  String transaction_id =notifyBean.getTransaction_id();

                  System.out.println("#####attach#######--->"+attach);
                  String[] strs = attach.split(",");
                  //种子支付
                  if(strs[1].equals("4")&&strs[0].equals("null")){
                      OrderSeed seed = new OrderSeed();
                      seed.setOrderNo(out_trade_no);
                      seed.setTradeNo(transaction_id);
                      seed.setStatus(2);
                      seedService.updateByStatus(seed);
                  }else{
                      Order retOrder = userOrderService.getOrderByOrderNo(out_trade_no);
                      if (retOrder != null && retOrder.getStatus().intValue() == OrderStatusEnum.BUILD_ORDER.getStatus()) {
                          if(strs[1].equals("2") || strs[1].equals("3")){ //团模式
                              String teamNo = strs[0];
                              if(teamNo.equals("null")){
                                  teamNo = null;
                              }
                              orderTeamService.insertGroupOrder(out_trade_no, teamNo);

                          }else if(strs[1].equals("5")){ //秒杀
                              String tgId = strs[2];
                              TopicSeckillLog entity = new TopicSeckillLog();
                              entity.setOrderNo(out_trade_no);
                              entity.setTgId(Integer.parseInt(tgId));
                              orderTeamService.addSeckillLog(entity);

                          }else if(strs[1].equals("7") || strs[1].equals("8")){ //抽奖
                              String topicNo = strs[0];
                              String tgId = strs[2];
                              if(topicNo.equals("null")){
                                  topicNo = null;
                              }
                              TopicPrizeLog entity = new TopicPrizeLog();
                              entity.setTopicNo(topicNo); //活动商品团号
                              entity.setOrderNo(out_trade_no); //订单号
                              entity.setTgId(Integer.parseInt(tgId)); //活动商品明细id
                              orderTeamService.add(entity);

                          }else if(strs[1].equals("9") || strs[1].equals("10")){ //惠省钱
                              String topicNo = strs[0];
                              String tgId = strs[2];
                              String actNo = strs[3];
                              if(topicNo.equals("null")){
                                  topicNo = null;
                              }
                              if(actNo.equals("null")){
                                  actNo = null;
                              }
                              TopicSavemoneyLog entity = new TopicSavemoneyLog();
                              entity.setMyNo(topicNo);//活动商品团号
                              entity.setOrderNo(out_trade_no);//订单号
                              entity.setTgId(Integer.parseInt(tgId));//活动商品明细id
                              entity.setActNo(actNo);//活动编号
                              orderTeamService.addTopicSaveMoney(entity);
                          }
                          paySucess(out_trade_no,transaction_id,retOrder);
                      }
              }
                  }

          } catch (Exception e) {
              e.printStackTrace();
              // TODO: handle exception
          }

          return "";
      }
  */
    @RequestMapping(value = "/smallAppPayCallback")
    public String smallAppPayCallback(HttpServletRequest request) {
        System.out.println("###########smallAppPayCallback#############");
        try {
        /*	String status = request.getParameter("status");
            String out_trade_no= request.getParameter("merOrderId");
			String attach  = request.getParameter("attachedData");
			String transaction_id  = request.getParameter("seqId");*/
            PayCallbackNotify noticeResult = PayUtils.payCallbackNotify(request.getInputStream());
            if (noticeResult != null && "SUCCESS".equals(noticeResult.getResult_code())) {

                System.out.println("###########smallAppPayCallback sucess#############");
                String out_trade_no = noticeResult.getOut_trade_no();
                String attach = noticeResult.getAttach();
                String transaction_id = noticeResult.getTransaction_id();


                System.out.println("#####smallAppPayCallback attach#######--->" + attach);
                String[] strs = attach.split(",");
                //充值成功
                if (strs[1].equals("0000") && strs[0].equals("null")) {

                    //更改WalletLog 的当前记录充值状态
                    WalletLog walletLog = new WalletLog();
                    walletLog.setStatus(1);  //状态1成功
                    walletLog.setOrderNo(out_trade_no); //订单号

                    //获取WalletLog 当前的充值金额
                    WalletLog walletLog2 = walletLogMapper.getByOrderNo(walletLog);
                    //判断状态是否改变过
                    if (walletLog2.getStatus() < 1) {

                        int row = walletLogMapper.updateByOrderNo(walletLog);
                        if (row > 0) {
                            System.out.println("==========================更改WalletLog 状态成功");
                        } else {
                            System.out.println("==========================更改WalletLog 状态失败");
                        }

                        //获取Wallet 的余额
                        List<Wallet> walletList = walletMapper.getWalletByUid(Integer.valueOf(strs[2]));
                        //根据使用者id更改Wallet表金额
                        Wallet wallet = new Wallet();
                        wallet.setUid(Integer.valueOf(strs[2]));
                        wallet.setMoney(walletList.get(0).getMoney() + walletLog2.getAmount());
                        int row2 = walletMapper.updateByUid(wallet);
                        if (row2 > 0) {
                            System.out.println("==========================更改Wallet 金额成功");
                        } else {
                            System.out.println("==========================更改Wallet 金额失败");
                        }
                    }
                } else if (strs[1].equals("0003") && strs[0].equals("null")) {
                    RechargePhone p = rpMapper.selectByPrimaryKey(Integer.valueOf(strs[2]));
                    MoneyUtil m = new MoneyUtil();
                    String am = m.fen2Yuan(String.valueOf(p.getAmount()));
                    if (am.equals("29.97")) {
                        am = "30";
                    } else if (am.equals("49.95")) {
                        am = "50";
                    } else if (am.equals("99.9")) {
                        am = "100";
                    } else if (am.equals("199.8")) {
                        am = "200";
                    } else if (am.equals("299.7")) {
                        am = "300";
                    } else if (am.equals("499.5")) {
                        am = "500";
                    } else {
                        am = "0";
                    }
                    if (p.getStatus() == 0) {
                        String row = MobileRecharge.recharge(am, p.getPhone(), p.getOrderNo());
                        p.setPaytime(new Date());
                        p.setStatus(1);
                        p.setPaystatus(Integer.valueOf(row));
                        rpMapper.updateByPrimaryKeySelective(p);
                    }

                }
                //押金支付2018-3-9
                else if (strs[1].equals("0001") && strs[0].equals("null")) {

                    MemberShop memberShop = new MemberShop();
                    memberShop.setOrderNo(out_trade_no);
                    memberShop.setTradeNo(transaction_id);
                    memberShop.setPayStatus(2);
                    shopMService.updateMemberShopByOrderNo(memberShop);
                }

                //程凤云免审核支付2018-3-22
                else if (strs[1].equals("0002") && strs[0].equals("null")) {

                    MemberShop memberShop = new MemberShop();
                    memberShop.setDepositNo(out_trade_no);
                    memberShop.setDepositTradeNo(transaction_id);
                    memberShop.setDepositStatus(2);
                    shopMService.updateMemberShopByDescNo(memberShop);
                }

                //种子支付
                else if (strs[1].equals("4") && strs[0].equals("null")) {
                    OrderSeed seed = new OrderSeed();
                    seed.setOrderNo(out_trade_no);
                    seed.setTradeNo(transaction_id);
                    seed.setStatus(2);
                    seedService.updateByStatus(seed);
                } else {
                    Order retOrder = userOrderService.getOrderByOrderNo(out_trade_no);
                    if (retOrder != null && retOrder.getStatus().intValue() == OrderStatusEnum.BUILD_ORDER.getStatus()) {
                        if (strs[1].equals("2") || strs[1].equals("3")) { //团模式
                            String teamNo = strs[0];
                            if (teamNo.equals("null")) {
                                teamNo = null;
                            }
                            orderTeamService.insertGroupOrder(out_trade_no, teamNo);

                            if (strs[0].equals("null")) {
                                templateMsgService.sendStartGroupTemplate(out_trade_no); //给团主发送发起团成功的消息
                            } else {
                                templateMsgService.sendJoinGroupTemplate(out_trade_no); //给团主发送他人参团的消息
                                templateMsgService.sendJoinedGroupTemplate(out_trade_no); //给参团的人发送参团成功的消息
                            }

                        } else if (strs[1].equals("5")) { //秒杀
                            String tgId = strs[2];
                            TopicSeckillLog entity = new TopicSeckillLog();
                            entity.setOrderNo(out_trade_no);
                            entity.setTgId(Integer.parseInt(tgId));
                            orderTeamService.addSeckillLog(entity);

                        } else if (strs[1].equals("7") || strs[1].equals("8")) { //抽奖
                            String topicNo = strs[0];
                            String tgId = strs[2];
                            if (topicNo.equals("null")) {
                                topicNo = null;
                            }
                            TopicPrizeLog entity = new TopicPrizeLog();
                            entity.setTopicNo(topicNo); //活动商品团号
                            entity.setOrderNo(out_trade_no); //订单号
                            entity.setTgId(Integer.parseInt(tgId)); //活动商品明细id
                            orderTeamService.add(entity);

                        } else if (strs[1].equals("9") || strs[1].equals("10")) { //惠省钱
                            String topicNo = strs[0];
                            String tgId = strs[2];
                            String actNo = strs[3];
                            if (topicNo.equals("null")) {
                                topicNo = null;
                            }
                            if (actNo.equals("null")) {
                                actNo = null;
                            }
                            TopicSavemoneyLog entity = new TopicSavemoneyLog();
                            entity.setMyNo(topicNo);//活动商品团号
                            entity.setOrderNo(out_trade_no);//订单号
                            entity.setTgId(Integer.parseInt(tgId));//活动商品明细id
                            entity.setActNo(actNo);//活动编号
                            orderTeamService.addTopicSaveMoney(entity);
                        } else if (strs[1].equals("11") && strs[0].equals("null")) { //荷兰式拍卖
                            orderTeamService.updateAuctionStatus(out_trade_no);// 荷兰式拍卖记录状态修改并且退回拍卖成功者的保证金
                        }
                        paySucess(out_trade_no, transaction_id, retOrder);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#######smallAppPayCallback#######" + e);
            return "FAILED";
            // TODO: handle exception
        }

        return "SUCCESS";
    }

    private void paySucess(String outTradeNo, String tradeNo, Order retOrder) {
        Order reqOrder = new Order();
        reqOrder.setStatus(OrderStatusEnum.PAY_ORDER.getStatus());
        reqOrder.setPaymentNo(tradeNo);
        reqOrder.setOrderNo(outTradeNo);
        reqOrder.setPaymentStatus(OrderStatusEnum.PAY_ORDER.getStatus());
        reqOrder.setPaymentId(retOrder.getPaymentId());
        reqOrder.setOrderPrice(retOrder.getOrderPrice());
        reqOrder.setId(retOrder.getId());
        reqOrder.setmId(retOrder.getmId());
        userOrderService.updateStatusByOrderNo(reqOrder);

    }

    @RequestMapping(value = "/unionCallback")
    public String unionCallback(HttpServletRequest request) {
        try {
            String status = request.getParameter("status");
            String out_trade_no = request.getParameter("merOrderId");
            String attachedData = request.getParameter("attachedData");
            String transaction_id = request.getParameter("seqId");
            if ("TRADE_SUCCESS".equals(status)) {
            	this.callbackProcess(out_trade_no,attachedData,transaction_id);
                logger.debug("unionCallback success,out_trade_no=" + out_trade_no + ",attachedData=" + attachedData);
               
            } else {
                logger.debug("unionCallback fail! out_trade_no=" + out_trade_no + ",attachedData = " + attachedData + "status=" + status);
            }
        } catch (Exception e) {
            logger.error("unionCallback error=" + e.getMessage());
            e.printStackTrace();
            return "FAILED";
        }
        return "SUCCESS";
    }
    
    /**
     * 
     * @Description: 回调处理
     * @author xieyc
     * @date 2018年8月14日 下午3:50:40 
     */
    public void callbackProcess(String out_trade_no,String attachedData,String transaction_id){
    	try {
    		 String[] strs = attachedData.split(",");
             //充值成功
             if (strs[1].equals("0000") && strs[0].equals("null")) {

                 //更改WalletLog 的当前记录充值状态
                 WalletLog walletLog = new WalletLog();
                 walletLog.setStatus(1);  //状态1成功
                 walletLog.setOrderNo(out_trade_no); //订单号

                 //获取WalletLog 当前的充值金额
                 WalletLog walletLog2 = walletLogMapper.getByOrderNo(walletLog);
                 //判断状态是否改变过
                 if (walletLog2.getStatus() < 1) {

                     int row = walletLogMapper.updateByOrderNo(walletLog);
                     if (row > 0) {
                         logger.debug("unionCallback 更改WalletLog 状态成功");
                     } else {
                         logger.debug("unionCallback 更改WalletLog 状态失败");
                     }

                     //获取Wallet 的余额
                     List<Wallet> walletList = walletMapper.getWalletByUid(Integer.valueOf(strs[2]));
                     //根据使用者id更改Wallet表金额
                     Wallet wallet = new Wallet();
                     wallet.setUid(Integer.valueOf(strs[2]));
                     wallet.setMoney(walletList.get(0).getMoney() + walletLog2.getAmount());
                     int row2 = walletMapper.updateByUid(wallet);
                     if (row2 > 0) {
                         logger.debug("unionCallback 更改WalletLog 金额成功");
                     } else {
                         logger.debug("unionCallback 更改WalletLog 金额失败");
                     }
                 }
             } else if (strs[1].equals("0003") && strs[0].equals("null")) {
                 RechargePhone p = rpMapper.selectByPrimaryKey(Integer.valueOf(strs[2]));
                 MoneyUtil m = new MoneyUtil();
                 String am = m.fen2Yuan(String.valueOf(p.getAmount()));
                 if (am.equals("29.97")) {
                     am = "30";
                 } else if (am.equals("49.95")) {
                     am = "50";
                 } else if (am.equals("99.9")) {
                     am = "100";
                 } else if (am.equals("199.8")) {
                     am = "200";
                 } else if (am.equals("299.7")) {
                     am = "300";
                 } else if (am.equals("499.5")) {
                     am = "500";
                 } else {
                     am = "0";
                 }
                 if (p.getStatus() == 0) {
                     String row = MobileRecharge.recharge(am, p.getPhone(), p.getOrderNo());
                     p.setPaytime(new Date());
                     p.setStatus(1);
                     p.setPaystatus(Integer.valueOf(row));
                     rpMapper.updateByPrimaryKeySelective(p);
                 }

             }
             //短信群发2018-9-11
             else if (strs[1].equals("0004") && strs[0].equals("null")) {
                 MsnApply msnApply = new MsnApply();
                 msnApply.setPayTime(new Date(JedisUtil.getInstance().time()));
                 msnApply.setEditTime(new Date(JedisUtil.getInstance().time()));
                 msnApply.setApymsnId(Integer.valueOf(strs[2]));
                 msnApply.setPayStatus(1);
                 msnApplyMapper.updateByPrimaryKeySelective(msnApply);
             }
             //押金支付2018-3-9
             else if (strs[1].equals("0001") && strs[0].equals("null")) {
                 MemberShop memberShop = new MemberShop();
                 memberShop.setOrderNo(out_trade_no);
                 memberShop.setTradeNo(transaction_id);
                 memberShop.setPayStatus(2);
                 shopMService.updateMemberShopByOrderNo(memberShop);//更新
                 MemberShop memberShop1=memberShopMapper.getMemberShopByOrderNo(memberShop.getOrderNo());                	
         		shopMService.insertAdminAndMemberSend(memberShop1,memberShop1.getmId());//插入sys_user与配送员
         		Sms sms = new Sms();
    				sms.setPhoneNo(memberShop.getLinkmanPhone());
    				sms.setSmsContent(memberShop.getLinkmanPhone());
    				SmsUtil.aliPushShopReg(sms);// 发送短信 
             }
             //程凤云免审核支付2018-3-22
             else if (strs[1].equals("0002") && strs[0].equals("null")) {
                 MemberShop memberShop = new MemberShop();
                 memberShop.setDepositNo(out_trade_no);
                 memberShop.setDepositTradeNo(transaction_id);
                 memberShop.setDepositStatus(2);
                 shopMService.updateMemberShopByDescNo(memberShop);
             }

             //种子支付
             else if (strs[1].equals("4") && strs[0].equals("null")) {
                 OrderSeed seed = new OrderSeed();
                 seed.setOrderNo(out_trade_no);
                 seed.setTradeNo(transaction_id);
                 seed.setStatus(2);
                 seedService.updateByStatus(seed);
             } else {
                 payCallbackService.paySesessUnion(out_trade_no, transaction_id, strs);
             }
		} catch (Exception e) {
			logger.error("回调处理 error=" + e.getMessage());
	        e.printStackTrace();
		}
    }
    
    /**
     * @Description: 支付方式为 9的时候的回调函数
     * @author xieyc
     * @date 2018年8月14日 下午4:17:52 
     */
    @RequestMapping(value = "/afterSucceedPay")
    public String afterSucceedPay(HttpServletRequest request) {
        try {
            String status = request.getParameter("status");
            String out_trade_no = request.getParameter("merchantOrderNo");
            String attachedData = request.getParameter("attachedData");
            String transaction_id = request.getParameter("payOrderId");
            if (StringUtils.isNotBlank(status) && Integer.valueOf(status)==1) {
            	this.callbackProcess(out_trade_no,attachedData,transaction_id);
                logger.debug("afterSucceedPay success,out_trade_no=" + out_trade_no + ",attachedData=" + attachedData);
            } else {
                logger.debug("afterSucceedPay fail! out_trade_no=" + out_trade_no + ",attachedData = " + attachedData + "status=" + status);
            }
        } catch (Exception e) {
            logger.error("afterSucceedPay error=" + e.getMessage());
            e.printStackTrace();
            return "FAILED";
        }
        return "SUCCESS";
    }
    
    
    
    
    @RequestMapping(value = "/swiftpassCallback")
    public String swiftpassCallback(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("#######swiftpassCallback##########");
        swiftpassNotify(req, resp);
        return "SUCCESS";
    }

    protected String swiftpassNotify(HttpServletRequest req, HttpServletResponse resp) {
        String respString = "error";

        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = XmlUtils.parseRequst(req);
            System.out.println("#######swiftpassCallback########## resString--->" + resString);
            if (resString != null && !"".equals(resString)) {
                Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
                String res = XmlUtils.toXml(map);

                if (map.containsKey("sign")) {
                    if (!SignUtils.checkParam(map, SwiftpassConfig.key)) {
                        System.out.println("#######swiftpassCallback########## sign--->不通过");
                        res = "验证签名不通过";
                        respString = "error";
                    } else {
                        String status = map.get("status");
                        if (status != null && "0".equals(status)) {
                            String result_code = map.get("result_code");
                            if (result_code != null && "0".equals(result_code)) {
                                String out_trade_no = map.get("out_trade_no");
                                String attach = map.get("attach");
                                String transaction_id = map.get("transaction_id");
                                String[] strs = attach.split(",");
                                //充值成功
                                if (strs[1].equals("0000") && strs[0].equals("null")) {

                                    //更改WalletLog 的当前记录充值状态
                                    WalletLog walletLog = new WalletLog();
                                    walletLog.setStatus(1);  //状态1成功
                                    walletLog.setOrderNo(out_trade_no); //订单号

                                    //获取WalletLog 当前的充值金额
                                    WalletLog walletLog2 = walletLogMapper.getByOrderNo(walletLog);
                                    //判断状态是否改变过
                                    if (walletLog2.getStatus() < 1) {

                                        int row = walletLogMapper.updateByOrderNo(walletLog);
                                        if (row > 0) {
                                            System.out.println("==========================更改WalletLog 状态成功");
                                        } else {
                                            System.out.println("==========================更改WalletLog 状态失败");
                                        }

                                        //获取Wallet 的余额
                                        List<Wallet> walletList = walletMapper.getWalletByUid(Integer.valueOf(strs[2]));
                                        //根据使用者id更改Wallet表金额
                                        Wallet wallet = new Wallet();
                                        wallet.setUid(Integer.valueOf(strs[2]));
                                        wallet.setMoney(walletList.get(0).getMoney() + walletLog2.getAmount());
                                        int row2 = walletMapper.updateByUid(wallet);
                                        if (row2 > 0) {
                                            System.out.println("==========================更改Wallet 金额成功");
                                        } else {
                                            System.out.println("==========================更改Wallet 金额失败");
                                        }
                                    }
                                }

                                //押金支付2018-3-9
                                else if (strs[1].equals("0001") && strs[0].equals("null")) {
                                    System.out.println("==========================订单号:" + out_trade_no);
                                    System.out.println("==========================第三方交易号:" + transaction_id);
                                    MemberShop memberShop = new MemberShop();
                                    memberShop.setOrderNo(out_trade_no);
                                    memberShop.setTradeNo(transaction_id);
                                    memberShop.setPayStatus(2);
                                    shopMService.updateMemberShopByOrderNo(memberShop);
                                }

                                //种子支付
                                else if (strs[1].equals("4") && strs[0].equals("null")) {
                                    OrderSeed seed = new OrderSeed();
                                    seed.setOrderNo(out_trade_no);
                                    seed.setTradeNo(transaction_id);
                                    seed.setStatus(2);
                                    seedService.updateByStatus(seed);
                                } else {
                                    Order retOrder = userOrderService.getOrderByOrderNo(out_trade_no);
                                    if (retOrder != null && retOrder.getStatus().intValue() == OrderStatusEnum.BUILD_ORDER.getStatus()) {
                                        if (strs[1].equals("2") || strs[1].equals("3")) { //团模式
                                            String teamNo = strs[0];
                                            if (teamNo.equals("null")) {
                                                teamNo = null;
                                            }
                                            orderTeamService.insertGroupOrder(out_trade_no, teamNo);

                                        } else if (strs[1].equals("5")) { //秒杀
                                            String tgId = strs[2];
                                            TopicSeckillLog entity = new TopicSeckillLog();
                                            entity.setOrderNo(out_trade_no);
                                            entity.setTgId(Integer.parseInt(tgId));
                                            orderTeamService.addSeckillLog(entity);

                                        } else if (strs[1].equals("7") || strs[1].equals("8")) { //抽奖
                                            String topicNo = strs[0];
                                            String tgId = strs[2];
                                            if (topicNo.equals("null")) {
                                                topicNo = null;
                                            }
                                            TopicPrizeLog entity = new TopicPrizeLog();
                                            entity.setTopicNo(topicNo); //活动商品团号
                                            entity.setOrderNo(out_trade_no); //订单号
                                            entity.setTgId(Integer.parseInt(tgId)); //活动商品明细id
                                            orderTeamService.add(entity);

                                        } else if (strs[1].equals("9") || strs[1].equals("10")) { //惠省钱
                                            String topicNo = strs[0];
                                            String tgId = strs[2];
                                            String actNo = strs[3];
                                            if (topicNo.equals("null")) {
                                                topicNo = null;
                                            }
                                            if (actNo.equals("null")) {
                                                actNo = null;
                                            }
                                            TopicSavemoneyLog entity = new TopicSavemoneyLog();
                                            entity.setMyNo(topicNo);//活动商品团号
                                            entity.setOrderNo(out_trade_no);//订单号
                                            entity.setTgId(Integer.parseInt(tgId));//活动商品明细id
                                            entity.setActNo(actNo);//活动编号
                                            orderTeamService.addTopicSaveMoney(entity);
                                        }
                                        paySucess(out_trade_no, transaction_id, retOrder);
                                    }
                                }
                                respString = "success";
                            }
                        }
                    }
                }
            }
            resp.getWriter().write(respString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respString;
    }
    
    @RequestMapping(value = "/unionRediret")
    public void unionRediret(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
         
    }
    
    
    private void unionRediretByFlag(String isFlag,HttpServletRequest request, HttpServletResponse response) throws IOException {
    	 String status = request.getParameter("status");
         String merOrderId = request.getParameter("merOrderId");
         String totalAmount = request.getParameter("totalAmount");
         String openid = request.getParameter("openid");
         if (StringUtils.isBlank(totalAmount)) {
             totalAmount = "0";
         }
         logger.debug("isFlag"+isFlag+"--"+openid);
         String baseUrl = Contants.BIN_HUI_URL + "/binhuiApp/";
         StringBuffer sb = new StringBuffer();
         sb.append(baseUrl);
         try {
             String fourRandom = MathsTool.getRandomString();  //获取随机数
             //支付成功
             if (StringUtils.isNotBlank(status) && "TRADE_SUCCESS".equals(status)) {
                 if (StringUtils.isNotBlank(isFlag)) {
                     if (isFlag.equals("0")) {
                         //公众号支付
                         sb.append("paySuccess?");
                         double realPrice = 0d;
                         try {
                             realPrice = Double.valueOf(totalAmount) / 100;
                         } catch (Exception e) {
                             logger.error("unionRediret convert error" + e.getMessage());
                             e.printStackTrace();
                         }
                         sb.append("orderFee=" + realPrice + "");
                         sb.append("&orderSign=" + merOrderId);
                         sb.append("&v=" + fourRandom);
                     } else if (isFlag.equals("1")) {
                         //充值支付
                         sb.append("mycard?");
                         sb.append("v=" + fourRandom);
                     } else if (isFlag.equals("2")) {
                         //商家支付
                         sb.append("registerssuccess?");
                         sb.append("v=" + fourRandom);
                         sb.append("&openid=" + openid);
                     } else if (isFlag.equals("11")) {
                         sb.append("rechargeSuccess?merOrderId=");
                         sb.append(merOrderId);
                         sb.append("&");
                         sb.append("v=" + fourRandom);
                     }
                 }
             } else {
                 //支付失败
                 if (StringUtils.isNotBlank(isFlag)) {
                     if (isFlag.equals("0")) {
                         //公众号支付
                         sb.append("orderlist?");
                         sb.append("orderStatus=1");
                         sb.append("&v=" + fourRandom);
                     } else if (isFlag.equals("1")) {
                         //充值支付
                         sb.append("mycard?");
                         sb.append("v=" + fourRandom);
                     } else if (isFlag.equals("2")) {
                         //商家支付
                         sb.append("regisiterdepositdeposit?");
                         sb.append("v=" + fourRandom);
                         sb.append("&openid=" + openid);
                     } else if (isFlag.equals("11")) {
                         //商家支付
                         sb.append("recharge?");
                         sb.append("v=" + fourRandom);
                     }
                 }
             }
         } catch (Exception e) {
             logger.error("unionRediret error=" + e.getMessage());
             e.printStackTrace();
         }
         logger.info("unionRediret=" + sb.toString());
         response.sendRedirect(sb.toString());
    }
    /**
     * 
     * @Description: 公众号支付
     * @author xieyc
     * @date 2018年9月20日 下午3:09:46 
     * @param   
     * @return  
     *
     */
	@RequestMapping(value = "/unionRediret0")
	public void unionRediret0(HttpServletRequest request, HttpServletResponse response) throws Exception {
		unionRediretByFlag("0",request,response);
	}
	   /**
     * 
     * @Description: 充值支付
     * @author xieyc
     * @date 2018年9月20日 下午3:09:46 
     * @param   
     * @return  
     *
     */
	@RequestMapping(value = "/unionRediret1")
	public void unionRediret1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		unionRediretByFlag("1",request,response); 
	}
	/**
     * 
     * @Description: 商家支付
     * @author xieyc
     * @date 2018年9月20日 下午3:09:46 
     * @param   
     * @return  
     *
     */
	@RequestMapping(value = "/unionRediret2")
	public void unionRediret2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		unionRediretByFlag("2",request,response);
		 
	}
	/**
     * 
     * @Description: 
     * @author xieyc
     * @date 2018年9月20日 下午3:09:46 
     * @param   
     * @return  
     *
     */

	@RequestMapping(value = "/unionRediret11")
	public void unionRediret11(HttpServletRequest request, HttpServletResponse response) throws Exception {
		unionRediretByFlag("11",request,response);
	}
    
    
  
    

    public static void main(String[] args) {
        //	String json="{\"charset\":\"UTF-8\",\"nonce_str\":\"1521617672308\",\"pay_info\":\"{\\\"appId\\\":\\\"wxe00fda5293a2fa25\\\",\\\"timeStamp\\\":\\\"1521617672617\\\",\\\"status\\\":\\\"0\\\",\\\"signType\\\":\\\"MD5\\\",\\\"package\\\":\\\"prepay_id\\u003dwx2018032115343208c5005bd00826275495\\\",\\\"callback_url\\\":null,\\\"nonceStr\\\":\\\"1521617672617\\\",\\\"paySign\\\":\\\"FC6125CBC58DF7F4871F623EA7672DF8\\\"}\",\"token_id\":\"1044df11e091871ef07d117b9ef331aef\",\"appid\":\"wxe00fda5293a2fa25\",\"result_code\":\"0\",\"mch_id\":\"400590008629\",\"sign_type\":\"MD5\",\"version\":\"2.0\",\"status\":\"0\"}"
        String json = "{\"appId\":\"wxe00fda5293a2fa25\",\"timeStamp\":\"1521619474960\",\"status\":\"0\",\"signType\":\"MD5\",\"package\":\"prepay_id=wx2018032116043456c25209330732379036\",\"callback_url\":null,\"nonceStr\":\"1521619474960\",\"paySign\":\"4F452D7862E4905BC58EC8215B839444\"}";
        JSONObject payInfoObj = JSONObject.parseObject(json);
        String nonceStr = (String) payInfoObj.get("nonceStr");
        String appid = (String) payInfoObj.get("appId");
        String sign = (String) payInfoObj.get("paySign");
        //	String prepayId = (String) josnObj.get("package");
        String prepayId = (String) payInfoObj.get("package");
        String timeStamp = (String) payInfoObj.get("timeStamp");
        System.out.println("prepayId--->" + prepayId);

    }
}
