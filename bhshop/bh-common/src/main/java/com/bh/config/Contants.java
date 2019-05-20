package com.bh.config;
/**
 * 常量配置
 * @author xxj
 *
 */
public class Contants {
	//用户信息会话属性key
	public static final String USER_INFO_ATTR_KEY="userInfo";
	public static final String MUSER="muser";
	
	//用户默认头像
	public static final String headImage = Contants.bucketHttp+"goods/a55b480e-630b-4eea-bfeb-f628a6eb8637.png";
	
	//京东图片前缀 n0(最大图)、n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 为图片大小。
	public static final String jdImage = "http://img13.360buyimg.com/jgsq-productsoa/";
	
	//android系统App下载地址
	public static final String ANDROID_DOWNLOAD_URL = "https://www.baidu.com";
	
	//ios系统App下载地址
	public static final String IOS_DOWNLOAD_URL = "https://www.taobao.com";
	
	//邮箱发送方账号密码
	public static final String EMAIL_ACOUNT = "shucaijia@zhiyesoft.com";
	public static final String EMAIL_PASSWORD = "Scj1473874668";
	
	//配送员默认配送范围10km
	public static final String SEND_SCOPE = "10000"; 
	
	//分页，每页显示多少条
	public static final int PAGE_SIZE = 10;
	
	//图库分页，每页显示多少条
	public static final int PAGE_PICSIZE = 40;
	
	//最小配送时间
	public static final int MIN_TIME = 30; 
	
	//广告管理排序号范围: 1~50
	public static final int SORTNUM_FIFTY = 51;
	
	//平台订单总状态（发货状态：0未发货1已发货2为部分发货）
	public static final int DELIVERY_PART = 2;
	public static final int DELIVERY_ALL = 1;
	
	//商家后台订单状态：1待付，2待发货，3已发货，4已收货,5待评价、6已取消、7已评价、8已删除'
	public static final int IS_ALL = 0; //全部订单
	public static final int IS_WAIT_PAY = 1;
	public static final int IS_WAIT_SEND = 2;
	public static final int IS_SEND = 3;
	public static final int IS_WAIT_EVALUATE=5;
	public static final int IS_CANCEL = 6;
	public static final int IS_COMPLISH = 7;
	public static final int IS_DELETE = 8;
	
	public static final int IS_REFUND = 9; //退款单
	public static final int IS_FAIL = 10; //退款失败
	public static final int IS_SUCCESS = 11; //退款成功
	
	public static final int REFUN_STATUS = 2; //退款成功
	
	//OrderShop订单配送状态：配送状态0初始化，1待接单，2待发货，3配送中，4已送达，5已完成',
	public static final int CATE_TEST= -1;
	public static final int CATE_ZERO = 0;
	public static final int CATE_ONE = 1;
	public static final int CATE_TWO= 2;
	public static final int CATE_THREE= 3;
	public static final int CATE_FOUR = 4;
	public static final int CATE_FIVE= 5;
	
	//orderRefundDoc'退款状态，0:申请退款 1:退款失败 2:退款成功',
	public static final int REFUND_WAIT = 0;
	public static final int REFUND_FAIL = 1;
	public static final int REFUND_SUCCESS = 2;
	public static final int REFUND_REFUSE = 3;
	
	//OderSend'配送状态  0(初始状态)待发货   1发货中   2已签收',
	public static final int DELIVER_WAIT_SEND = 0;
	public static final int DELIVER_SEND = 1;
	public static final int DELIVER_ALREADY_SEND = 2;
	public static final int DELIVER_CANCEL = 3;
	public static final int DELIVER_SETTLE = 4;
	
	public static final String WAIT_SEND = "待取货";
	public static final String SEND = "配送中";
	public static final String ALREADY_SEND = "已配送";
	public static final String CANCEL = "已取消";
	public static final String ALREADY_SETTLE = "已完成";
	public static final String WAIT_SETTLE = "待结算";
	
	
	//广告类型：1:类目广告 2:菜单广告'
	public static final int adsType = 2;
	
	public static final int NORMAL_STATUS = 5; //商品状态5-上架
	
	//配送速度
	public static final int BIKE_SPEED = 10; //单车-10min/km
	public static final int TRICYCLE_SPEED = 8; //三轮车
	public static final int ELECTROMBILE_SPEED = 6; //电动车
	public static final int BIG_ELECTROMBILE_SPEED = 5; //大踏板电动车
	public static final int MOTORBIKE_SPEED = 4; // 摩托车
	public static final int SEDAN_CAR_SPEED = 3; // 小汽车
	public static final int TRUCKS_SPEED = 2; // 小货车
	public static final int WALK_SPEED = 20; //步行
	public static final int DRIVE_SPEED = 5; //驾车
	
	//交通工具
	public static final String BIKE = "单车"; 
	public static final String TRICYCLE = "三轮车"; 
	public static final String ELECTROMBILE = "电动车";
	public static final String BIG_ELECTROMBILE = "大踏板电动车"; 
	public static final String MOTORBIKE = "摩托车"; 
	public static final String SEDAN_CAR = "小汽车"; 
	public static final String TRUCKS = "小货车";
	
	//订单类型
	public static final String ALL_TYPE = "全部类型";
	public static final String SHORT_RANGE = "近距离"; 
	public static final String FAR_RANGE = "远距离"; 
	public static final String HIGH_PRICE = "高价单"; 
	public static final String LOW_PRICE = "低价单"; 
	
	//审核状态
	public static final String AUDIT_WAIT = "待审核";
	public static final String AUDIT_SUCCESS = "审核通过";
	public static final String AUDIT_FAIL = "审核不通过";
	
	//配送员在线状态'-1下线 0在线 1收工',
	public static final int ONLINE_OFF = 1;
	public static final int ONLINE_open = 0;
	
	
	/**chengfengyun 2017-10-17 当前页 1 */
	public static final Integer PAGE = 1;
	/**chengfengyun 2017-10-17 每页的大小 200*/
	public static final Integer SIZE = 10;
	
	/**chengfengyun 2017-10-17 当购物车的数量是100时提示 */
	//public static final Integer cartCount = 4;//如果是5的话，则list里面有6个然后提示不能添加
	public static final Integer cartCount = 500;
	
	/***chengfengyun收货地址的记录数 2017-10-17**/
	public static final Integer ADDRESS_COUNT=10;
	
	/*************cheng2017-11-07添加：orderMain的payment_status支付状态 :0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败******************/
	public static final String paymentStatus0="货到付款";
	public static final String paymentStatus1="待付款";
	public static final String paymentStatus2="已付款";
	public static final String paymentStatus3="待退款";
	public static final String paymentStatus4="退款成功";
	public static final String paymentStatus5="退款失败";
	/*************cheng2017-11-07添加：ordershop的status支付状态 :商家后台订单状态：1待付，2待发货，3已发货，4已收货,5待评价、6已取消、7已评价、8已删除'******************/
	public static final String orderShopStatu1="待付款";
	public static final String orderShopStatu2="待发货";
	public static final String orderShopStatu3="已发货";
	public static final String orderShopStatu4="已收货";
	public static final String orderShopStatu5="待评价";
	public static final String orderShopStatu6="已取消";
	public static final String orderShopStatu7="已评价";
	public static final String orderShopStatu8="已删除";
	public static final String orderShopStatu9="备货中";
	public static final Integer shopStatu1=1;
	public static final Integer shopStatu2=2;
	public static final Integer shopStatu3=3;
	public static final Integer shopStatu4=4;
	public static final Integer shopStatu5=5;
	public static final Integer shopStatu6=6;
	public static final Integer shopStatu7=7;
	public static final Integer shopStatu8=8;
	public static final Integer shopStatu9=9;
	
	
	
	/*************cheng2017-11-14添加：ordershop的is_refund：是否退款:0否，1是'******************/
	public static final Integer is_refund0=0;
	public static final Integer is_refund1=1;
	public static final String is_refund0Name="不退款";
	public static final String is_refund1Name="退款";
	public static final String refund0Name="不可以申请售后服务";
	public static final String refund1Name="退款";
	
	/*************cheng2017-11-16添加：order_refund_doc的reason'******************/
	public static final String refund0="可申请退款";
	public static final String refund1="既可以退货(换货)、也可以退款退货";
	public static final String refund4="可申请售后服务";
	public static final String refund5="不可以申请售后服务";
	public static final Integer refund41=4;
	public static final Integer refund51=5;
	
	/*************cheng2017-11-17添加：order_refund_doc的reason'******************/
	public static final String statusName="退款状态:";
	public static final String stautName0="买家申请退款";
	public static final String statuName1="退款失败";
	public static final String statuName2="退款成功";
	public static final String statuName3="已拒绝";
	/*************cheng2017-11-17添加：评论表的状态'******************/
	public static final String levelName1="已好评";
	public static final String levelName2="中评";
	public static final String levelName3="差评";
	
	/*************cheng2017-11-24配送员的等级称号'******************/
	public static final String sendLevelName1="见习骑士";
	public static final String sendLevelName2="铜牌骑士";
	public static final String sendLevelName3="银牌骑士";
	public static final String sendLevelName4="金牌骑士";
	public static final String sendLevelName5="钻石骑士";
	public static final Integer sendLevel1=100;
	public static final Integer sendLevel2=400;
	public static final Integer sendLevel3=900;
	public static final Integer sendLevel4=1700;
	
	/*************cheng2017-12-05种子积分规则'******************/
	public static final Integer rule1=1;
	public static final Integer rule2=2;
	public static final Integer rule3=3;
	public static final Integer rule4=4;
	public static final Integer rule5=5;
	public static final String rule1Name="签到";
	public static final String rule2Name="浇水";
	public static final String rule3Name="拼单";
	public static final String rule4Name="单买";
	public static final String rule5Name="分享";
	
	public static final Integer storeNums=0;
	public static final String storeNumsYes="有货";
	public static final Integer storeNumsYes1=1;
	public static final Integer storeNumsNo0=0;
	public static final String storeNumsNo="无货";

	public static final Integer GOODS_STATUES_PUTAWASY = 5;//xieyc  商品状态上架

	public static final Integer SHOP_STEP_CHECK_PASS =7;// xieyc  商店状态审核通过
	
	/*************xieyc payment_status支付状态 :0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败******************/
	public static final Integer PAY_STATUS0=0;
	public static final Integer PAY_STATUS1=1;
	public static final Integer PAY_STATUS2=2;
	public static final Integer PAY_STATUS3=3;
	public static final Integer PAY_STATUS4=4;
	public static final Integer PAY_STATUS5=5;
	
	
	
	/*活动类型*/
	public static final Integer BIDDING=1;
	public static final Integer TOPIC=2;
	public static final Integer BARGAIN=3;
	

	//一个滨惠豆抵扣的钱
	public static final double saveMoney = 0.01;
	/**是否中奖（topic_prize_log）  xieyc**/
	public static final Integer WIN_PRIZE_INITIALIZE =0 ;//初始化
	public static final Integer NOT_WIN_PRIZE=1;//没有中奖
	public static final Integer WIN_PRIZE=2;//中奖
	public static final Integer CANCEL_PRIZE=3;//活动取消

	/**是否可以提现（wallet）  xieyc**/
	public static final Integer WITHDRAW=1;//可以提现
	public static final Integer NOT_WITHDRAW=0;//不可以提现
	
	/**多少天后确认收货**/
	public static final Integer CONFIGGOODS=15;
	
	/**默认的银行卡图片**/
	public static final String DEFAULT_BANK_IMG=Contants.bucketHttp+"goods/C78EE7B2164546D3BD84961BB90ED3DF.png";
	
	//钱包图片
    public static final String bondImage="https://bhs-oss.bh2015.com/goods/ff401ff4-e6b1-47cf-b565-5a7cfc7a86ee.png";
	public static final String rechargeImage="https://bhs-oss.bh2015.com/goods/353cdb78-546f-48e2-a465-8b6aaf8d26bd.png";
	     
	
	
	/**等级 rank  每级升级所需经验为：（N+1）*（200点）；**/
	public static final Integer RANK_NUM =20;
	
	//播种经验2
	public static final Integer SOW_EXP=2;
	//铲除(翻土)的经验3
	public static final Integer UPROOT_EXP=3;
	//除草的经验2
	public static final Integer WEED_EXP = 2;
	//除虫的经验2
	public static final Integer PYRE_EXP = 2;
	//浇水的经验2
	public static final Integer WATERING_EXP = 2;
	//用户的金币(初始化为500)
	public static final Integer GOLD = 500;
	//浇水频率=3小时/次
	public static final Integer WATERING_RATE = 3;
	
	
	//商家入驻的押金金额(元)
	public static final String MARGIN="0.02";
	//商家商品免审核的保证金金额
	public static final String PROMISE_MONEY="200000";
	//订单金额＜49元，收取基础运费8元，不收续重运费(分为单位)
	public static final Integer price1=8;
	//49元≤订单金额＜99元，收取基础运费6元，不收续重运费；
	public static final Integer price2=6;
	//无货的提示
	public static final String notStockMsg="所选地区暂时无货,非常抱歉!";
	//区域限制的提示
	public static final String limitAreaMsg="部分商品在该地区暂不支持销售";
	//百度api的开发密匙
	public static final String tengxunMapKey="DYXBZ-5XZLQ-UKJ5Z-GHRRX-UOGHJ-TSBGB";
	//商品详情的区域无货提示
	public static final String goodsDetailLimitAreaMSg="该商品在该地区暂不支持销售";
	//商品详情的区域无货提示
	public static final String GOODS_NUM="商品的数量必须大于或者等于1";
	
	//需要滨惠豆最低门槛
	public static final Integer MIN_DEDUCTIBLE=1;
	
	 //测试环境 
	 //公众号
	 public static String appId="wxd3441d36a44400de";
	 public static String appSecret="23a6a9cf524478f239dc48ebefdcfb72";
	 public static final String BIN_HUI_URL="https://bhmall.zhiyesoft.cn";//xieyc add  URL前缀
	 //平台md5key
	 public static final String PLAT_MD5_KEY="2b8JBaFTEn6i26AASw3iswBySjw46pJWiW2dEh8wMsXiNPXK";
	 public static final String PLAT_ORDER_NO_PRE="3769";
	 public static final String bucketHttp="http://bhshoptest3.oss-cn-shenzhen.aliyuncs.com/";
	 public static final String bucketHttps="https://bhshoptest3.oss-cn-shenzhen.aliyuncs.com/";
	 public static final String orderNoPre="3769";
	 public static final String printOpenId = "oj9MyxAHaJMMaL8b5MfexVNyPHPU";
	 public static final boolean isPro=false;
	 
	 
	//正式环境
	//公众号
	 /*public static String appId="wxe96accb07b947e01";
	 public static String appSecret="3fb7f510985fbbde530c3ce868ab6757";
     public static final String BIN_HUI_URL="https://bh2015.com";//xieyc add  URL前缀
	//平台订单前辍
	 public static final String PLAT_ORDER_NO_PRE="3134";
	 public static final String PLAT_MD5_KEY="Bt77Kwb6mp2CSdK6DwhnBczMZTB2N63KTXwp3bn25MC7naDw";
	 public static final String bucketHttp="http://bhshop.oss-cn-shenzhen.aliyuncs.com/";
	 public static final String bucketHttps="https://bhshop.oss-cn-shenzhen.aliyuncs.com/";
	 
	 public static final String bucketHttp="http://bhs-oss.bh2015.com/";
	 public static final String bucketHttps="https://bhs-oss.bh2015.com/";
	 public static final String orderNoPre="3134";
	 public static final String printOpenId = "oUx0u0YawhVduzZgZcD4CuPobPsM";
	 //是否生产环境
	 public static final boolean isPro=true;*/
	 
	 
	 //小程序测试环境
	 public static final String sAppId="wxa4b6bd7f420e9fb2";
	 public static final String sAppSecret="bb831a4aa207431c2486b850ad09dd93";
	 //小程序密钥
	 public static final String sKey="BH8888808com1yygbhcombh888812345";
	 //小程序支付成功回调地址
	 public static final String s_notify_url=BIN_HUI_URL+"/bh-order-api/payCallback/smallAppPayCallback";
	 //小程序商户号 
	 public static final String s_mch_id="1456013002";
	 
	 public static final String NEWUSERCOUPONGIFTNAME = "用户大礼包";//xieyc
	 
	 
	
	 //获取redis key
	 public static String getRedisKey(String key){
		 if(!isPro){
			return "test_"+key; 
		 }else{
			return key;
		 }
	 }
	
	
	 //24小时*60
	 public static final Integer oneDayLen=1440;
	 
	//24小时*60*5
     public static final Integer fiveDayLen=7200;
   //24小时*60*10
     public static final Integer tenDayLen=14400;
     
     
	 public static final Integer semih=30;
	
}
