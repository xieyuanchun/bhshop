package com.bh.admin.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bh.utils.*;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.admin.mapper.goods.GoAddressAreaMapper;
import com.bh.admin.mapper.goods.GoodsAttrMapper;
import com.bh.admin.mapper.goods.GoodsBrandMapper;
import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.mapper.goods.GoodsCategroyJdMapper;
import com.bh.admin.mapper.goods.GoodsDescMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsModelAttrMapper;
import com.bh.admin.mapper.goods.GoodsModelMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.ItemModelValueMapper;
import com.bh.admin.mapper.goods.JdGoodsCopyMapper;
import com.bh.admin.mapper.goods.JdGoodsMapper;
import com.bh.admin.mapper.goods.JdGoodsNoticeMapper;
import com.bh.admin.mapper.goods.MessageQMapper;
import com.bh.admin.mapper.goods.MessageQSetMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberShopAdminMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.mapper.user.MemberUserMapper;
import com.bh.admin.pojo.goods.GoAddressArea;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsAttr;
import com.bh.admin.pojo.goods.GoodsBrand;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.GoodsDesc;
import com.bh.admin.pojo.goods.GoodsModel;
import com.bh.admin.pojo.goods.GoodsModelAttr;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.ItemModelValue;
import com.bh.admin.pojo.goods.JdGoods;
import com.bh.admin.pojo.goods.JdGoodsCopy;
import com.bh.admin.pojo.goods.JdGoodsNotice;
import com.bh.admin.pojo.goods.MessageQ;
import com.bh.admin.pojo.goods.MessageQSet;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.service.JDGoodsService;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.bean.goods.CategoryList;
import com.bh.jd.bean.goods.CategoryVo;
import com.bh.jd.bean.goods.Detail;
import com.bh.jd.bean.goods.HitResult;
import com.bh.jd.bean.goods.PageNum;
import com.bh.jd.bean.goods.SaleAttr;
import com.bh.jd.bean.goods.SearchResult;
import com.bh.jd.bean.goods.SellPriceResult;
import com.bh.jd.bean.goods.SimilarSku;
import com.bh.jd.bean.goods.SkuByPage;
import com.bh.jd.bean.goods.SkuImage;
import com.bh.result.BhResult;
import com.github.pagehelper.PageHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class JDGoodsServiceImpl implements JDGoodsService{
	private static final Logger logger = LoggerFactory.getLogger(JDGoodsServiceImpl.class);
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsBrandMapper goodsBrandMapper;
	@Autowired
	private JdGoodsNoticeMapper noticeMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsCategoryMapper categoryMapper;
	@Autowired
	private GoodsModelMapper modelMapper;
	@Autowired
	private GoodsModelAttrMapper modelAttrMapper;
	@Autowired
	private GoodsAttrMapper goodsAttrMapper;
	@Autowired
	private MessageQSetMapper messageQSetMapper;
	@Autowired
	private MessageQMapper messageQMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private MemberShopMapper shopMapper;
	@Autowired
	private GoodsDescMapper descMapper;
	@Autowired
	private JdGoodsCopyMapper jdGoodsCopyMapper;
	@Autowired
	private GoodsCategroyJdMapper catJdMapper;
	@Autowired
	private ItemModelValueMapper modelValueMapper;
	@Autowired
	private JdGoodsMapper jdGoodsMapper;
	@Autowired
	private GoAddressAreaMapper goAddressMapper;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
	
	
	public BhResult getSkuByPage(String pageNum,String pageNo) throws Exception{
		BhResult  re = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSkuByPage(pageNum, pageNo));
		return re;
		
	}
	public CategoryVo getCategory(String cid) throws Exception{
		CategoryVo vo = JDGoodsApi.getCategory(cid);
		return vo;
	}
	
	
	
	
	public CategoryList getCategorys(String pageNo, String pageSize, String parentId, String catClass) throws Exception{
		CategoryList map = JDGoodsApi.getCategorys(pageNo, pageSize, parentId, catClass);
		return map;
	}
	
	//同步京东分类
	public CategoryList synchroCategorys() throws Exception {
		 String pageNo = "1";
         String pageSize = "5000";
         String parentId = "";
         String catClass = "0";
		 CategoryList map1 = getCategorys(pageNo, pageSize, parentId, catClass);
		 List<GoodsCategory> goodsCategoryList=goodsCategoryMapper.selectByFirst();
         for (int i=0; i<map1.getCategorys().size() ; i++) {
         	boolean flag=false;
         	for (GoodsCategory goodsCategory1 : goodsCategoryList) {
         		if(map1.getCategorys().get(i).getName().trim().equals(goodsCategory1.getName().trim())) {
         			flag=true;
         		}
         	}
         	if(flag==false) {
         			GoodsCategory goodsCategory=new GoodsCategory();
     				goodsCategory.setId(map1.getCategorys().get(i).getCatId().longValue());
     				goodsCategory.setName(map1.getCategorys().get(i).getName());
     				goodsCategory.setReid(map1.getCategorys().get(i).getParentId().longValue());
     				goodsCategory.setSortnum((short) (i+1));
     				goodsCategory.setFlag(true);
     				goodsCategory.setIsLast(false);
     				goodsCategory.setSeries((short) 1);
     				goodsCategory.setAdId(0);
     				goodsCategory.setImage("");
     				goodsCategory.setIsJd(1);
     				goodsCategory.setIsNew(1);
     				goodsCategoryMapper.insert(goodsCategory);
         	}
         	
				String parentId1=map1.getCategorys().get(i).getCatId().toString();
				catClass = "1";
				CategoryList map2 = getCategorys(pageNo, pageSize, parentId1, catClass);
				List<GoodsCategory> goodsCategoryList1=goodsCategoryMapper.selectBySecond(parentId1);
				for (int j=0; j<map2.getCategorys().size() ; j++) {
					boolean flag1=false;
					for (GoodsCategory goodsCategory : goodsCategoryList1) {
	            	    if(map2.getCategorys().get(j).getName().trim().equals(goodsCategory.getName().trim())) {
	            			 flag1=true;
	            			 break;
	            		}
					}
	            	if(flag1==false) {
							GoodsCategory goodsCategory1=new GoodsCategory();
							goodsCategory1.setId(map2.getCategorys().get(j).getCatId().longValue());
							goodsCategory1.setName(map2.getCategorys().get(j).getName());
							goodsCategory1.setReid(map2.getCategorys().get(j).getParentId().longValue());
							goodsCategory1.setSortnum((short) (j+1));
							goodsCategory1.setFlag(true);
							goodsCategory1.setIsLast(false);
							goodsCategory1.setSeries((short) 2);
							goodsCategory1.setAdId(0);
							goodsCategory1.setImage("");
							goodsCategory1.setIsJd(1);
							goodsCategory1.setIsNew(1);
							goodsCategoryMapper.insert(goodsCategory1);
					}
	            	String	parentId2=map2.getCategorys().get(j).getCatId().toString();
					catClass = "2";
					CategoryList map3 =getCategorys(pageNo, pageSize, parentId2, catClass);
					List<GoodsCategory> goodsCategoryList2=goodsCategoryMapper.selectByThree(parentId2);
					for (int k=0; k<map3.getCategorys().size() ; k++) {
							boolean flag2=false;
							for (GoodsCategory goodsCategory : goodsCategoryList2) {
		            			if(map3.getCategorys().get(k).getName().trim().equals(goodsCategory.getName().trim())) {
		            				flag2=true;
		            				break;
		            			}
		            		}
		            		if(flag2==false) {
								GoodsCategory goodsCategory2=new GoodsCategory();
								goodsCategory2.setId(map3.getCategorys().get(k).getCatId().longValue());
								goodsCategory2.setName(map3.getCategorys().get(k).getName());
								goodsCategory2.setReid(map3.getCategorys().get(k).getParentId().longValue());
								goodsCategory2.setSortnum((short) (k+1));
								goodsCategory2.setFlag(true);
								goodsCategory2.setIsLast(true);
								goodsCategory2.setSeries((short) 3);
								goodsCategory2.setAdId(0);
								goodsCategory2.setImage("");
								goodsCategory2.setIsJd(1);
								goodsCategory2.setIsNew(1);
								goodsCategoryMapper.insert(goodsCategory2);
							}
					}
				}
			}
		
		return map1;
	}
	
	public SearchResult search(String keyword, String catId, String pageIndex, String pageSize, String min, String max, String brands) throws Exception{
		brands = ","+brands;
		SearchResult  s = JDGoodsApi.search(keyword, catId, pageIndex, pageSize, min, max, brands);
		if(s!=null){
			List<HitResult> result = s.getHitResult();
			if(result!=null){
				for(HitResult entity : result){
					List<GoodsSku> sku = goodsSkuMapper.selectByJdSkuNo(Long.valueOf(entity.getWareId()));
					if(sku.size()>0){ //判断是否入库
						entity.setIsGet(true);
					}else{
						entity.setIsGet(false);
					}
				}
			}
			s.setHitResult(result);
		}
		return s;
	}
	/**
	 * 根据分类id递归查询所有子分类
	 *//*
	@Override
	public List<CategoryVo> getCategorys1(String pageSize, String pageNo) throws Exception {
		// TODO Auto-generated method stub
		CategoryList list = JDGoodsApi.getCategorys("1", "10", "0", "");
		List<CategoryVo> vo = list.getCategorys();
		for(CategoryVo vo1 :vo){
			CategoryList list1 = JDGoodsApi.getCategorys("1", "10", String.valueOf(vo1.getParentId()), "");
			List<CategoryVo> v1 = list1.getCategorys();
			for(CategoryVo vo2 : v1){
				CategoryList list2 = JDGoodsApi.getCategorys("1", "10", String.valueOf(vo2.getParentId()), "");
				List<CategoryVo> v2 = list2.getCategorys();
				vo2.setList(v2);
			}
			vo1.setList(v1);
		}	
		return vo;
	}
*/
	
	/**
	 * 京东商品摘取
	 */ 
	@Override
	public List<Goods> getJdGoods(String sku) throws Exception {
		long thisTime = 0;
		List<Goods> goodsList = new ArrayList<Goods>();
		List<String> skus = getAllSku(sku);
		for(String entity : skus){
			List<GoodsSku> jdSku = goodsSkuMapper.selectByJdSkuNo(Long.valueOf(entity)); //判断京东商品是否已存在数据库
			if(jdSku.size()==0){
				Goods goods = new Goods();
				goods.setSaleType(1); //销售模式--默认拼团单卖皆可
				goods.setRefundDays(-1); //退货天数--默认-1不限时间退货
				goods.setTeamPrice(0); //拼团价--默认0
				goods.setTeamNum(2);  //拼团人数--默认2
				goods.setTeamEndTime(24); //拼团持续时间--默认24小时
				goods.setTimeUnit(2);  //时间单位--默认小时
				goods.setIsCreate(0); //是否支持系统发起拼单-1否 0是',
				goods.setIsPromote(0); //是否促成团(-1 否 0是',
				goods.setStoreNums(10000); //库存
				
				GoodsSku goodsSku = new GoodsSku();
				goodsSku.setStoreNums(10000); //库存--默认10000
				goodsSku.setJdSupport(1); //是否支持京东下单，0不支持，1支持'
				goodsSku.setScore(0);
				
				List<SellPriceResult> list = JDGoodsApi.getSellPrice(entity);
				if(list.size()>0){
					goodsSku.setRealJdPrice(list.get(0).getJdPrice()); //京东价
					goodsSku.setMarketRealPrice(list.get(0).getJdPrice());//市场价
					goodsSku.setRealJdBuyPrice(list.get(0).getPrice());//京东购买价
					goodsSku.setRealPrice(list.get(0).getPrice());//销售价
					goodsSku.setRealTeamPrice(list.get(0).getPrice());
				}
				
				List<SellPriceResult> protocolList = JDGoodsApi.getPrice(entity);
				if(protocolList.size()>0){
					goodsSku.setRealJdProtocolPrice(protocolList.get(0).getPrice()); //京东协议价
					goodsSku.setRealStockPrice(protocolList.get(0).getPrice());
					goodsSku.setRealJdOldBuyPrice(protocolList.get(0).getPrice()); //协议价（旧）
					
					if(protocolList.get(0).getPrice()>=99){ //设置物流价格
						goodsSku.setRealDeliveryPrice(0.0);
					}else if(protocolList.get(0).getPrice()<49){
						goodsSku.setRealDeliveryPrice(8.0);
					}else{
						goodsSku.setRealDeliveryPrice(6.0);
					}
				}
				long startTime = System.currentTimeMillis();
				StringBuffer buffer = new StringBuffer(); 
				Map<String, List<SkuImage>> map = JDGoodsApi.skuImage(entity);
				if(map!=null){
					List<SkuImage> imageList = map.get(entity);
					if(imageList.size()>0){
						for(SkuImage image : imageList){
							buffer.append("'"+Contants.jdImage+image.getPath()+"'"+",");
						}
						String str = buffer.toString().substring(0, buffer.toString().length()-1);
						List<SimilarSku> similarSkulist = JDGoodsApi.getSimilarSku(entity);
						String strValue = getSaleValeById(similarSkulist, entity);
						String value = "{'url':["+str+"],'value':'"+strValue+"'}";
						goodsSku.setValue(value);
						if(strValue.equals("普通规格")){
							goodsSku.setKeyOne("规格");
							goodsSku.setValueOne("普通规格");
						}
						setKeyValue(goodsSku, entity); //插入属性值
						
						JSONObject jsonObject = JSONObject.fromObject(value); //value转义
						goodsSku.setValueObj(jsonObject);
					}
				}
				long endTime = System.currentTimeMillis();
				thisTime = thisTime +(endTime-startTime);
				Detail detail = JDGoodsApi.getDetail(entity, "true");
				if(detail!=null){
					goods.setName(detail.getName()); //商品名称
					goods.setImage(Contants.jdImage + detail.getImagePath()); //商品图片
					goods.setDescription(detail.getIntroduction()); //详细介绍
					goods.setAppdescription(detail.getAppintroduce()); //移动端详细介绍
					goodsSku.setJdSkuNo(detail.getSku()); //商品编码
					goodsSku.setJdParam(detail.getParam()); //技术参数介绍
					goodsSku.setJdUpc(detail.getUpc()); //条形码
					goodsSku.setGoodsName(detail.getName());
				}
				goods.setIsJd(1);
				goods.setGoodsSku(goodsSku);
				goodsList.add(goods);
			}
		}
		System.out.println(thisTime);
		if(goodsList.size()>0){
			return goodsList;
		}else{
			return null;
		}
	}
	
	public List<GoodsCategory> getJdCategory(String cateGoryStr){
		List<GoodsCategory> list = categoryMapper.getFirstLevelList();
		list.clear();
		String[] str = cateGoryStr.split(";");
		for(int i=0; i<str.length; i++){
			CategoryVo jdCategory = JDGoodsApi.getCategory(str[i]);
			GoodsCategory myCategory = new GoodsCategory();
			
			myCategory.setFlag(true); //默认设置为推荐
			myCategory.setName(jdCategory.getName()); // 分类名称
			myCategory.setReid(jdCategory.getParentId().longValue()); //父类id
			myCategory.setId(jdCategory.getCatId().longValue());  //分类id
			myCategory.setIsJd(1);
			if(jdCategory.getCatClass()==0){
				myCategory.setSeries(Short.parseShort("1")); //层级
			}else if(jdCategory.getCatClass()==1){
				myCategory.setSeries(Short.parseShort("2"));
			}else if(jdCategory.getCatClass()==2){
				myCategory.setSeries(Short.parseShort("3"));
			}else if(jdCategory.getCatClass()==3){
				myCategory.setSeries(Short.parseShort("4"));
			}
			
			if(i+1==str.length){ //判断最后一级分类
				myCategory.setIsLast(true);
			}else{
				myCategory.setIsLast(false);
			}
			list.add(myCategory);
		}
		
		return list;
	}
	
	public JSONArray getJdCategoryArray(String cateGoryStr){
		JSONArray array = new JSONArray();
		String[] str = cateGoryStr.split(";");
		for(int i=0; i<str.length; i++){
			CategoryVo jdCategory = JDGoodsApi.getCategory(str[i]);
			GoodsCategory myCategory = new GoodsCategory();
			
			myCategory.setFlag(true); //默认设置为推荐
			myCategory.setName(jdCategory.getName()); // 分类名称
			myCategory.setReid(jdCategory.getParentId().longValue()); //父类id
			myCategory.setId(jdCategory.getCatId().longValue());  //分类id
			myCategory.setIsJd(1);
			if(jdCategory.getCatClass()==0){
				myCategory.setSeries(Short.parseShort("1")); //层级
			}else if(jdCategory.getCatClass()==1){
				myCategory.setSeries(Short.parseShort("2"));
			}else if(jdCategory.getCatClass()==2){
				myCategory.setSeries(Short.parseShort("3"));
			}else if(jdCategory.getCatClass()==3){
				myCategory.setSeries(Short.parseShort("4"));
			}
			if(i+1==str.length){ //判断最后一级分类
				myCategory.setIsLast(true);
			}else{
				myCategory.setIsLast(false);
			}
			array.add(myCategory);
		}
		return array;
	}
	
	public static List<String> getAllSku(String sku) throws Exception {
		List<String> str = new ArrayList<>();
		List<SimilarSku> list = JDGoodsApi.getSimilarSku(sku);
		String[] attrs = null;
		List<SaleAttr> attrList = null;
		if(list!=null && list.size()>0){
			for(SimilarSku entity : list){
				attrList = entity.getSaleAttrList();
				if(attrList.size()>0){
					for(SaleAttr attr : attrList){
						attrs = attr.getSkuIds();
						for(int i=0; i<attrs.length; i++){
							str.add(attrs[i]);
						}
					}
				}
				break;
			}
		}
		if(str.size()==0){//查询不到同类
			str.add(sku);
		}
		return str;
	}
	
	/**
	 * 京东商品价格变更
	 */
	@Override
	public List<Long> getPriceChange() throws Exception {
		int row = 0;
		List<Long> str = new ArrayList<>();
		StringBuffer buffer = new StringBuffer();
		List<GoodsSku> skuList = goodsSkuMapper.selectListByJdSkuNo();
		if(skuList.size()>0){
			for(GoodsSku sku : skuList){
				buffer.append(sku.getJdSkuNo()+",");
			}
			String jdSku = buffer.toString().substring(0, buffer.toString().length()-1);
			List<SellPriceResult> list = JDGoodsApi.getSellPrice(jdSku);//获取京东价和销售价
			for(SellPriceResult result : list){
				
				List<GoodsSku> goodsSku = goodsSkuMapper.selectByJdSkuNo(result.getSkuId());
				Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.get(0).getGoodsId());
				double realJdPrice = (double)goodsSku.get(0).getJdPrice()/100;
				double realJdBuyPrice = (double)goodsSku.get(0).getJdBuyPrice()/100;
				
				//京东价格--
				if(result.getJdPrice()>realJdPrice){ //涨价
					double price = result.getJdPrice() - realJdPrice; //差价
					row = insertJdGoodsNotice(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), 1, price, 1);
					row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
					str.add(result.getSkuId()); //返回变更的jd商品编码
					
				}else if(result.getJdPrice()<realJdPrice){//降价
					double price = realJdPrice - result.getJdPrice(); //差价
					row = insertJdGoodsNotice(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), 0, price, 1);
					row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
					str.add(result.getSkuId()); //返回变更的jd商品编码
				}
				
				//购买价格--
				if(result.getPrice()>realJdBuyPrice){ //涨价
					double price = result.getPrice() - realJdBuyPrice; //差价
					row = insertJdGoodsNotice(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), 1, price, 2);
					row = changePrice(goodsSku.get(0).getId(), result.getPrice(), 2);
					str.add(result.getSkuId()); //返回变更的jd商品编码
					
				}else if(result.getPrice()<realJdBuyPrice){//降价
					double price = realJdBuyPrice - result.getJdPrice(); //差价
					row = insertJdGoodsNotice(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), 0, price, 2);
					row = changePrice(goodsSku.get(0).getId(), result.getPrice(), 2);
					str.add(result.getSkuId()); //返回变更的jd商品编码
				}
			}
			
			List<SellPriceResult> otherList = JDGoodsApi.getPrice(jdSku);//获取京东价和销售价
			for(SellPriceResult otherResult : otherList){
				List<GoodsSku> goodsSku = goodsSkuMapper.selectByJdSkuNo(otherResult.getSkuId());
				Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.get(0).getGoodsId());
				double realJdProtocolPrice = (double)goodsSku.get(0).getJdProtocolPrice()/100;
				
				//协议价格--
				if(otherResult.getPrice()>realJdProtocolPrice){ //涨价
					double price = otherResult.getPrice() - realJdProtocolPrice; //差价
					row = insertJdGoodsNotice(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), 1, price, 3);
					row = changePrice(goodsSku.get(0).getId(), otherResult.getPrice(), 3);
					str.add(otherResult.getSkuId());
					
				}else if(otherResult.getPrice()<realJdProtocolPrice){//降价
					double price = realJdProtocolPrice - otherResult.getJdPrice(); //差价
					row = insertJdGoodsNotice(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), 0, price, 3);
					row = changePrice(goodsSku.get(0).getId(), otherResult.getPrice(), 3);
					str.add(otherResult.getSkuId());
				}
			}
			
		}
		if(row <= 0){
			str = null;
		}
		return str;
	}
	
	/**
	 * 价格变更消息提示
	 */
	@Override
	public String notice() throws Exception {
		String str = null;
		List<GoodsSku> list = goodsSkuMapper.selectListByJdSkuNo(); 
        if(list.size()>0){
        	int init = 90;// 每隔90条循环一次  
            int total = list.size();  
            int cycelTotal = total / init;  
            if (total % init != 0) {  
                cycelTotal += 1;  
                if (total < init) {  
                    init = list.size();  
                }  
            }  
            List<GoodsSku> list2 = new ArrayList<GoodsSku>();  
            for (int i = 0; i < cycelTotal; i++) {  
                for (int j = 0; j < init; j++) {  
                    if (list.get(j) == null) {  
                        break;  
                    }  
                    list2.add(list.get(j));  
                }  
                str = noticeNext(list2);
                list.removeAll(list2);//移出已经保存过的数据  
                list2.clear();//移出当前保存的数据  
                int t = list.size();  
                if (t < init) {  
                    init = t;  
                }  
            }
        }
        return str;
	}

	private String noticeNext(List<GoodsSku> skuList) throws Exception {
		String str = null;
		StringBuffer buffer = new StringBuffer();
		if(skuList.size()>0){
			for(GoodsSku sku : skuList){
				buffer.append(sku.getJdSkuNo()+",");
			}
			String jdSku = buffer.toString().substring(0, buffer.toString().length()-1);
			List<SellPriceResult> list = JDGoodsApi.getSellPrice(jdSku);//获取京东价和销售价
			if(list.size()>0){
				for(SellPriceResult result : list){
					
					List<GoodsSku> goodsSku = goodsSkuMapper.selectByJdSkuNo(result.getSkuId());
					if(goodsSku.size()>0){
						double realJdPrice = (double)goodsSku.get(0).getJdPrice()/100;
						double realJdBuyPrice = (double)goodsSku.get(0).getJdBuyPrice()/100;
						
						//京东价格--
						if(result.getJdPrice()>realJdPrice){ //涨价
							double price = result.getJdPrice() - realJdPrice; //差价
							str = sendNotice(goodsSku.get(0).getJdSkuNo(), 1, price, 1);
							
						}else if(result.getJdPrice()<realJdPrice){//降价
							double price = realJdPrice - result.getJdPrice(); //差价
							str = sendNotice(goodsSku.get(0).getJdSkuNo(), 0, price, 1);
						}
						
						//购买价格--
						if(result.getPrice()>realJdBuyPrice){ //涨价
							double price = result.getPrice() - realJdBuyPrice; //差价
							str = sendNotice(goodsSku.get(0).getJdSkuNo(), 1, price, 2);
							
						}else if(result.getPrice()<realJdBuyPrice){//降价
							double price = realJdBuyPrice - result.getJdPrice(); //差价
							str = sendNotice(goodsSku.get(0).getJdSkuNo(), 0, price, 2);
						}
					}
				}
			}
			
			List<SellPriceResult> otherList = JDGoodsApi.getPrice(jdSku);//获取京东价和销售价
			if(otherList.size()>0){
				for(SellPriceResult otherResult : otherList){
					List<GoodsSku> goodsSku = goodsSkuMapper.selectByJdSkuNo(otherResult.getSkuId());
					if(goodsSku.size()>0){
						double realJdProtocolPrice = (double)goodsSku.get(0).getJdProtocolPrice()/100;
						
						//协议价格--
						if(otherResult.getPrice()>realJdProtocolPrice){ //涨价
							double price = otherResult.getPrice() - realJdProtocolPrice; //差价
							str = sendNotice(goodsSku.get(0).getJdSkuNo(), 1, price, 3);
							
						}else if(otherResult.getPrice()<realJdProtocolPrice){//降价
							double price = realJdProtocolPrice - otherResult.getJdPrice(); //差价
							str = sendNotice(goodsSku.get(0).getJdSkuNo(), 0, price, 3);
						}
					}
				}
			}
			
		}
		return str;
	}
	
	/**
	 * 京价格变更发送短信及变更日志的插入
	 */
	@Override
	public int sendMessage() throws Exception {
		int row = 0;
		try {
			List<GoodsSku> list = goodsSkuMapper.selectListByJdSkuNo();
			if (list.size() > 0) {
				int size = list.size();
				int pageSize = 99;
				int pageSum = size / pageSize;
				for (int pageIndex = 0; pageIndex <= pageSum; pageIndex++) {
					int end = (pageIndex + 1) * pageSize;
					if (end > size - 1) {
						end = size - 1;
					}
					List<GoodsSku> listSub = list.subList(pageIndex * pageSize, end);
					row = sendMessageNext(listSub);
					LoggerUtil.getLogger().debug("sendMessage pageIndex=" + pageIndex);
				}
			}
		} catch (Exception e) {
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
        return row;
	}
	
	private int sendMessageNext(List<GoodsSku> skuList) throws Exception {
		int row = 0;
		StringBuffer buffer = new StringBuffer();
		MemberShop shop = null;
		int count = 0;
		double price = 0;
		double priceOne = 0;
		Goods goods = null;
		double realJdPrice = 0;
		double realJdProtocolPrice = 0;
		double realPrice = 0;
		double realTeamPrice = 0;
		int tPrice = 0;
		List<JdGoods> jdGoodsList = null;
		List<GoodsSku> goodsSku = null;
		long time = JedisUtil.getInstance().time();
		Date date = new Date(time);
		if(skuList.size()>0){
			for(GoodsSku sku : skuList){
				buffer.append(sku.getJdSkuNo()+",");
			}
			JdGoods jdGoods = new JdGoods();
			String jdSku = buffer.toString().substring(0, buffer.toString().length()-1);
			List<SellPriceResult> list = JDGoodsApi.getPrice(jdSku);//获取京东价和协议价
			for(SellPriceResult result : list){
				goodsSku = goodsSkuMapper.selectByJdSkuNo(result.getSkuId());
				if(goodsSku.size()>0){
					goods = goodsMapper.selectByPrimaryKey(goodsSku.get(0).getGoodsId());
					shop = shopMapper.selectByPrimaryKey(goods.getShopId());
					realJdPrice = (double)goodsSku.get(0).getJdPrice()/100;
					realJdProtocolPrice = (double)goodsSku.get(0).getJdProtocolPrice()/100;
					
					if(result.getJdPrice() > realJdPrice && result.getPrice() > realJdProtocolPrice){ //京东价up协议价up
						price = result.getJdPrice() - realJdPrice; //差价
						priceOne = result.getPrice() - realJdProtocolPrice;
						row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
						row = changeOtherPriceOne(goodsSku.get(0).getId(), price, priceOne, 1);
					}
					else if(result.getJdPrice() < realJdPrice && result.getPrice() < realJdProtocolPrice){//京东价down协议价down
						price = realJdPrice - result.getJdPrice(); //差价
						priceOne = realJdProtocolPrice - result.getPrice();
						row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
						row = changeOtherPriceOne(goodsSku.get(0).getId(), price, priceOne, 2);
						
					}
					else if(result.getJdPrice()>realJdPrice && result.getPrice()==realJdProtocolPrice){ //京东价up,协议价no
						price = result.getJdPrice() - realJdPrice; //差价
						row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
						row = changeOtherPrice(goodsSku.get(0).getId(), price, 1);
						
					}
					else if(result.getJdPrice()<realJdPrice && result.getPrice()==realJdProtocolPrice){//京东价down,协议价no
						price = realJdPrice - result.getJdPrice(); //差价
						row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
						if(realJdPrice < result.getPrice()){ //京东价降的比协议价还低
							priceOne = result.getPrice()-realJdPrice;
							row = insertNoticeOne(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), (double)(Math.round(price*100)/100.0), (double)(Math.round(priceOne*100)/100.0),MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0));
							count ++;
						}else{
							row = changeOtherPrice(goodsSku.get(0).getId(), price, 2);
						}
						
					}
					else if(result.getJdPrice()==realJdPrice && result.getPrice()>realJdProtocolPrice){//京东价no,协议价up
						price = result.getPrice() - realJdProtocolPrice; //差价
						row = changePrice(goodsSku.get(0).getId(), result.getPrice(), 3);
						row = changeOtherPrice(goodsSku.get(0).getId(), price, 1);
					}
					else if(result.getJdPrice()==realJdPrice && result.getPrice()<realJdProtocolPrice){//京东价no,协议价down
						price = realJdProtocolPrice - result.getPrice(); //差价
						row = changePrice(goodsSku.get(0).getId(), result.getPrice(), 3);
						row = changeOtherPrice(goodsSku.get(0).getId(), price, 2);
					}
					else if(result.getJdPrice()>realJdPrice && result.getPrice()<realJdProtocolPrice){//京东价up,协议价down
						price = result.getJdPrice() - realJdPrice; //差价
						priceOne = realJdProtocolPrice - result.getPrice();
						row = insertNoticeTwo(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), (double)(Math.round(price*100)/100.0), (double)(Math.round(priceOne*100)/100.0), 1,MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0));
						row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
						count ++;
					}
					else if(result.getJdPrice()<realJdPrice && result.getPrice()>realJdProtocolPrice){//京东价down,协议价up
						price = realJdPrice - result.getJdPrice(); //差价
						priceOne = result.getPrice() - realJdProtocolPrice;
						row = insertNoticeTwo(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), (double)(Math.round(price*100)/100.0), (double)(Math.round(priceOne*100)/100.0), 2,MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0));
						row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
						count ++;
					}
					GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(goodsSku.get(0).getId());
					if(sku!=null){
						realPrice = (double)sku.getSellPrice()/100;
						realTeamPrice = (double)sku.getTeamPrice()/100;
						if(realPrice > result.getJdPrice()){//单卖价高于京东价
							tPrice = sku.getSellPrice() - MoneyUtil.doubeToInt(result.getJdPrice());
							price = (double)tPrice/100;
							row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0), sku, 1);
							count ++;
						}
						if(realPrice <= result.getPrice()){ //单卖价低于协议价
							tPrice = MoneyUtil.doubeToInt(result.getPrice()) - sku.getSellPrice();
							price = (double)tPrice/100;
							row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0), sku, 2);
							if(goods.getStatus()==5){
								goods.setStatus(2); //下架
								goodsMapper.updateByPrimaryKeySelective(goods);
							}
							count ++;
						}
						if(goods.getSaleType()>1){//拼团模式
							if(realTeamPrice > result.getJdPrice()){//拼团价高于京东价
								tPrice = sku.getTeamPrice() - MoneyUtil.doubeToInt(result.getJdPrice());
								price = (double)tPrice/100;
								row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0), sku, 3);
								count ++;
							}
							if(realTeamPrice <= result.getPrice()){ //拼团价低于协议价
								tPrice = MoneyUtil.doubeToInt(result.getPrice()) - sku.getTeamPrice();
								price = (double)tPrice/100;
								row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,MoneyUtil.doubeToInt(result.getJdPrice()),MoneyUtil.doubeToInt(result.getPrice()), goodsSku.get(0), sku, 4);
								if(goods.getStatus()==5){
									goods.setStatus(2); //下架
									goodsMapper.updateByPrimaryKeySelective(goods);
								}
								count ++;
							}
						}
					}
				}
			}
			row = sendMsg(count, shop); //发短信、邮件
		}
		return row;
	}
	
	public int changePrice(int skuId, double price, int fz) {//fz--1京东价，2客户购买价，3协议价
		int row = 0;
		int realPrice = (int) Math.round(price*100);
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuId);
		if(fz==1){
			goodsSku.setJdPrice(realPrice);
			goodsSku.setMarketPrice(realPrice);
		}else if(fz==2){
			goodsSku.setJdBuyPrice(realPrice);
		}else if(fz==3){
			goodsSku.setJdProtocolPrice(realPrice);
			goodsSku.setStockPrice(realPrice);
		}
		goodsSku.setJdOldBuyPrice(goodsSku.getJdProtocolPrice());
		row = goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
		return row;
	}
	
	public int changePriceOne(int skuId, double jdPrice, double JdProtocolPrice) {
		int row = 0;
		int realjdPrice = MoneyUtil.doubeToInt(jdPrice);
		int realJdProtocolPrice = (int) Math.round(JdProtocolPrice*100);
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuId);
		goodsSku.setJdPrice(realjdPrice);
		goodsSku.setMarketPrice(realjdPrice);
		goodsSku.setJdProtocolPrice(realJdProtocolPrice);
		goodsSku.setStockPrice(realJdProtocolPrice);
		goodsSku.setJdOldBuyPrice(goodsSku.getJdProtocolPrice());
		row = goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
		return row;
	}
	
	private int changeOtherPrice(int skuId, double price, int fz){ //fz 1升高，2降低
		int row = 0;
		int realPrice = MoneyUtil.doubeToInt(price);
		GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(skuId);
		if(sku!=null){
			if(fz==1){
				sku.setSellPrice(sku.getSellPrice()+realPrice);
				sku.setTeamPrice(sku.getTeamPrice()+realPrice);
			}else{
				if(sku.getSellPrice()>realPrice){
					sku.setSellPrice(sku.getSellPrice()-realPrice);
				}
				if(sku.getTeamPrice()>realPrice){
					sku.setTeamPrice(sku.getTeamPrice()-realPrice);
				}
			}
			row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
		}
		return row ;
	}
	
	private int changeOtherPriceOne(int skuId, double price, double priceOne, int fz){ //fz 1升高，2降低
		int row = 0;
		int realPrice = MoneyUtil.doubeToInt(price);
		int realPriceOne = MoneyUtil.doubeToInt(priceOne);
		GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(skuId);
		if(sku!=null){
			if(fz==1){
				if(realPrice > realPriceOne){
					sku.setSellPrice(sku.getSellPrice()+realPrice);
					sku.setTeamPrice(sku.getTeamPrice()+realPrice);
				}else{
					sku.setSellPrice(sku.getSellPrice()+realPriceOne);
					sku.setTeamPrice(sku.getTeamPrice()+realPriceOne);
				}
			}else{
				if(realPrice > realPriceOne){
					sku.setSellPrice(sku.getSellPrice()-realPriceOne);
					sku.setTeamPrice(sku.getTeamPrice()-realPriceOne);
				}else{
					sku.setSellPrice(sku.getSellPrice()-realPrice);
					sku.setTeamPrice(sku.getTeamPrice()-realPrice);
				}
			}
			row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
		}
		return row ;
	}
	
	public int insertNoticeOne(Long jdSku, String name, int goodsId, double price, double priceOne, int newJdPrice, int newJdProtocolPrice, GoodsSku sku) {
		JdGoodsNotice jdNotice = new JdGoodsNotice();
		jdNotice.setJdSku(jdSku);
		jdNotice.setValid(1);
		List<JdGoodsNotice> oldList = noticeMapper.selectByJdSkuNoValid(jdNotice);
		if(oldList.size()>0){
			for(JdGoodsNotice jdGoodsNotice : oldList){
				jdGoodsNotice.setValid(0);
				noticeMapper.updateByPrimaryKeySelective(jdGoodsNotice); //将原先消息置为无效
			}
		}
		JdGoodsNotice notice = new JdGoodsNotice();
		notice.setAddTime(new Date()); //消息时间'
		notice.setJdSku(jdSku); //'京东sku'
		notice.setName(name); //'商品名称'
		notice.setGoodsId(goodsId); //商品id
		notice.setIsRead(0); //'是否已读(0未读 1已读)'
		notice.setOldJdPrice(sku.getJdPrice());
		notice.setOldJdProtocolPrice(sku.getJdProtocolPrice());
		notice.setOldSellPrice(sku.getSellPrice());
		notice.setOldTeamPrice(sku.getTeamPrice());
		notice.setNewJdPrice(newJdPrice);
		notice.setNewJdProtocolPrice(newJdProtocolPrice);
		notice.setNewSellPrice(sku.getSellPrice());
		notice.setNewTeamPrice(sku.getTeamPrice());
		notice.setValid(1);
		
		notice.setMsgType(0); //消息类型(0降价  1升价,2一升一降，3无)
		notice.setMsg("京东价格降低"+price+"元, 比协议价低了"+priceOne+"元,请手动审核数据");
		return noticeMapper.insertSelective(notice);
	}
	
	public int insertNoticeTwo(Long jdSku, String name, int goodsId, double price, double priceOne, int fz ,int newJdPrice, int newJdProtocolPrice, GoodsSku sku) {
		JdGoodsNotice jdNotice = new JdGoodsNotice();
		jdNotice.setJdSku(jdSku);
		jdNotice.setValid(1);
		List<JdGoodsNotice> oldList = noticeMapper.selectByJdSkuNoValid(jdNotice);
		if(oldList.size()>0){
			for(JdGoodsNotice jdGoodsNotice : oldList){
				jdGoodsNotice.setValid(0);
				noticeMapper.updateByPrimaryKeySelective(jdGoodsNotice); //将原先消息置为无效
			}
		}
		JdGoodsNotice notice = new JdGoodsNotice();
		notice.setAddTime(new Date()); //消息时间'
		notice.setJdSku(jdSku); //'京东sku'
		notice.setName(name); //'商品名称'
		notice.setGoodsId(goodsId); //商品id
		notice.setIsRead(0); //'是否已读(0未读 1已读)'
		notice.setMsgType(2); //消息类型(0降价  1升价)
		notice.setOldJdPrice(sku.getJdPrice());
		notice.setOldJdProtocolPrice(sku.getJdProtocolPrice());
		notice.setOldSellPrice(sku.getSellPrice());
		notice.setOldTeamPrice(sku.getTeamPrice());
		notice.setNewJdPrice(newJdPrice);
		notice.setNewJdProtocolPrice(newJdProtocolPrice);
		notice.setNewSellPrice(sku.getSellPrice());
		notice.setNewTeamPrice(sku.getTeamPrice());
		notice.setValid(1);
		if(fz==1){
			notice.setMsg("京东价格升高"+price+"元, 协议价降低"+priceOne+"元,请手动审核数据");
		}else if(fz==2){
			notice.setMsg("京东价格降低"+price+"元, 协议价升高"+priceOne+"元,请手动审核数据");
		}
		return noticeMapper.insertSelective(notice);
	}
	
	public int insertNoticeThree(Long jdSku, String name, int goodsId, double price,int newJdPrice, int newJdProtocolPrice, GoodsSku sku, GoodsSku realSku, int fz) { //fz:1-单卖高于京东价， 2-单卖低于协议价 3拼团价高于京东价，4拼团价低于京东价
		JdGoodsNotice jdNotice = new JdGoodsNotice();
		jdNotice.setJdSku(jdSku);
		jdNotice.setValid(1);
		List<JdGoodsNotice> oldList = noticeMapper.selectByJdSkuNoValid(jdNotice);
		if(oldList.size()>0){
			for(JdGoodsNotice jdGoodsNotice : oldList){
				jdGoodsNotice.setValid(0);
				noticeMapper.updateByPrimaryKeySelective(jdGoodsNotice); //将原先消息置为无效
			}
		}
		JdGoodsNotice notice = new JdGoodsNotice();
		notice.setAddTime(new Date()); //消息时间'
		notice.setJdSku(jdSku); //'京东sku'
		notice.setName(name); //'商品名称'
		notice.setGoodsId(goodsId); //商品id
		notice.setIsRead(0); //'是否已读(0未读 1已读)'
		notice.setMsgType(3); //消息类型(0降价  1升价,2一升一降，3无)
		if(fz==1){
			notice.setMsg("单卖价高于京东价"+price+"元,请手动审核数据");
		}else if(fz==2){
			if(price==0){
				notice.setMsg("单卖价等于协议价,请手动审核数据");
			}else{
				notice.setMsg("单卖价低于协议价"+price+"元,请手动审核数据");
			}
		}else if(fz==3){
			notice.setMsg("拼团价高于京东价"+price+"元,请手动审核数据");
		}else if(fz==4){
			if(price==0){
				notice.setMsg("拼团价等于协议价,请手动审核数据");
			}else{
				notice.setMsg("拼团价低于协议价"+price+"元,请手动审核数据");
			}
		}
		notice.setOldJdPrice(sku.getJdPrice());
		notice.setOldJdProtocolPrice(sku.getJdProtocolPrice());
		notice.setOldSellPrice(sku.getSellPrice());
		notice.setOldTeamPrice(sku.getTeamPrice());
		notice.setNewJdPrice(newJdPrice);
		notice.setNewJdProtocolPrice(newJdProtocolPrice);
		notice.setNewSellPrice(realSku.getSellPrice());
		notice.setNewTeamPrice(realSku.getTeamPrice());
		notice.setValid(1);
		return noticeMapper.insertSelective(notice);
	}
	
	public int sendMsg(int count, MemberShop memberShop) {
		int row = 0;
		if(count>0){
			String msgContent = count+"";
			List<MessageQSet> qSetList = messageQSetMapper.selectAll();
			if(qSetList.size()>0){
				for(MessageQSet entity: qSetList){
					String[] sendTo = entity.getSendTo().split(",");
					for(int i=0; i<sendTo.length; i++){
						if(Integer.parseInt(entity.getType())==1){ //邮箱
							BhResult result = sendMail(msgContent, sendTo[i], Contants.EMAIL_ACOUNT, Contants.EMAIL_PASSWORD);
							if(result.getStatus()==200){
								row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 1, memberShop.getmId(), 1);
							}else{
								row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 2, memberShop.getmId(), 1);
							}
						}else{//短信
							Sms sms = new Sms();
							sms.setPhoneNo(sendTo[i]);
							sms.setSmsContent(msgContent);
							BhResult result = SmsUtil.aliPushJDPrcieChange(sms);
							if(result.getStatus()==-1){//发送失败
								row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 2, memberShop.getmId(), 2);
							}else{//发送成功
								row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 1, memberShop.getmId(), 2);
							}
						}
					}
				}
			}
		}
		return row;
	}
	
	public int insertJdGoodsNotice(Long jdSku, String name, int goodsId, int msgType, double price, int fz) { // price--价格变动， fz--1京东价，2客户购买价，3协议价
		JdGoodsNotice notice = new JdGoodsNotice();
		notice.setAddTime(new Date()); //消息时间'
		notice.setJdSku(jdSku); //'京东sku'
		notice.setName(name); //'商品名称'
		notice.setGoodsId(goodsId); //商品id
		notice.setIsRead(0); //'是否已读(0未读 1已读)'
		
		notice.setMsgType(msgType); //消息类型(0降价  1升价)
		if(msgType==0){
			if(fz==1){
				notice.setMsg("京东价格降低"+price+"元, 请手动审核数据");
			}else if(fz==2){
				notice.setMsg("客户购买价格降低"+price+"元, 请手动审核数据");
			}else if(fz==3){
				notice.setMsg("协议价格降低"+price+"元, 请手动审核数据");
			}
		}
		if(msgType == 1){
			if(fz==1){
				notice.setMsg("京东价格升高"+price+"元, 请手动审核数据");
			}else if(fz==2){
				notice.setMsg("客户购买价格升高"+price+"元, 请手动审核数据");
			}else if(fz==3){
				notice.setMsg("协议价格升高"+price+"元, 请手动审核数据");
			}
		}
		return noticeMapper.insertSelective(notice);
	}
	
	public String sendNotice(Long jdSku, int msgType, double price, int fz) { // price--价格变动， fz--1京东价，2客户购买价，3协议价
		String str = null;
		if(msgType==0){
			if(fz==1){
				str = jdSku+"京东价格降低"+price+"元";
			}else if(fz==2){
				str = jdSku+"客户购买价格降低"+price+"元";
			}else if(fz==3){
				str = jdSku+"协议价格降低"+price+"元";
			}
		}
		if(msgType == 1){
			if(fz==1){
				str = jdSku+"京东价格升高"+price+"元";
			}else if(fz==2){
				str = jdSku+"客户购买价格升高"+price+"元";
			}else if(fz==3){
				str = jdSku+"协议价格升高"+price+"元";
			}
		}
		return str;
	}
	
	/**
	 * 价格变更发送短信邮件
	 * @param jdSku
	 * @param msgType
	 * @param price
	 * @param fz
	 * @param memberShop
	 * @return
	 */
	public int message(int jdSku, int msgType, double price, int fz, MemberShop memberShop) {
		int row = 0;
		String msgContent = null;
		if(msgType==0){
			if(fz==1){
				msgContent = "商品"+jdSku+"京东价格降低"+price+"元";
			}else if(fz==2){
				msgContent = "商品"+jdSku+"客户购买价格降低"+price+"元";
			}else if(fz==3){
				msgContent = "商品"+jdSku+"协议价格降低"+price+"元";
			}
		}
		if(msgType == 1){
			if(fz==1){
				msgContent = "商品"+jdSku+"京东价格升高"+price+"元";
			}else if(fz==2){
				msgContent = "商品"+jdSku+"客户购买价格升高"+price+"元";
			}else if(fz==3){
				msgContent = "商品"+jdSku+"协议价格升高"+price+"元";
			}
		}
		List<MessageQSet> qSetList = messageQSetMapper.selectAll();
		if(qSetList.size()>0){
			for(MessageQSet entity: qSetList){
				String[] sendTo = entity.getSendTo().split(",");
				for(int i=0; i<sendTo.length; i++){
					if(Integer.parseInt(entity.getType())==1){ //邮箱
						BhResult result = sendMail(msgContent, sendTo[i], Contants.EMAIL_ACOUNT, Contants.EMAIL_PASSWORD);
						if(result.getStatus()==200){
							row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 1, memberShop.getmId(), 1);
						}else{
							row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 2, memberShop.getmId(), 1);
						}
					}else{//短信
						Sms sms = new Sms();
						sms.setPhoneNo(sendTo[i]);
						sms.setSmsContent(msgContent);
						BhResult result = SmsUtil.aliPushJDPrcieChange(sms);
						if(result.getStatus()==-1){//发送失败
							row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 2, memberShop.getmId(), 2);
						}else{//发送成功
							row = insertMessageQ(msgContent, memberShop.getTel(), sendTo[i], 1, memberShop.getmId(), 2);
						}
					}
				}
			}
		}
		return row;
	}
	
	public BhResult sendMail(String content, String toEmail, final String fromEmail, final String fromPassword) {
		BhResult bhResult = null;
		try {
			if (RegExpValidatorUtils.isEmail(toEmail)) {
				// 1.创建Session对象：连接服务器
				// 参数一：代表连接服务器所需配置
				// 参数二：登录所需的用户名和密码
				Properties props = new Properties();
				// 服务器地址(端口默认为25)
				props.setProperty("mail.host", "smtp.mxhichina.com");

				// 是否为验证登录
				props.setProperty("mail.smtp.auth", "true");
				Session session = Session.getDefaultInstance(props, new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromEmail, fromPassword);
					}

				});
				session.setDebug(false);

				// 2.写邮件
				MimeMessage mail = new MimeMessage(session);

				// 发件人
				mail.setFrom(new InternetAddress(fromEmail));
				// 收件人
				// 参数一：收件类型
				// TO: 收件人
				// CC: 抄送人
				// BCC: 密送人
				// A->B(TO) C(CC) D(BCC)
				// 参数二：收件人地址
				mail.setRecipient(RecipientType.TO, new InternetAddress(toEmail));  
				//String path = request.getContextPath(); //得到当前工程的根路径，代码如下 
				//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";//得到登录的计算机域名，如果没有域名就得到IP
				StringBuffer sb=new StringBuffer("您有新的消息，请注意查收！</br>");  
				
				sb.append("尊敬的滨惠  用户：</br>"+"您好！此电子邮件地址用于测试京东商品价格变更消息提醒</br>");
				//String code = MixCodeUtil.getCode(6);
				sb.append("<strong>" +" <div style='text-align:center;font-size:30px'>【滨惠商城】检测到京东商品价格有变动，涉及商品数量有+"+content+"，请留言！</div>"+"</strong> </br>");
				sb.append("此致</br> " +
				"滨惠 开发团队敬上</br>");
				sb.append("此电子邮件地址无法接收回复。如需更多信息，请访问"+Contants.BIN_HUI_URL+"/binhui/home 帮助中心。");
				// 主题
				mail.setSubject("价格变更消息内容：");

				// 正文
				// 参数一：内容
				// 参数二：指定内容类型"<font color='red' size='+6'>这是邮件的正文3</font><br/>"
				mail.setContent(sb.toString(),"text/html;charset=utf-8");
				// 3.发送
				
				Transport.send(mail);
				bhResult = new BhResult(200, "邮件发送成功", null);
				
			}else{
				bhResult = new BhResult(400,"邮箱格式错误！",null);
			}
		} catch (Exception e) {
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}
	
	/**
	 * 发送短信邮箱日志插入
	 * @param content
	 * @param fromWho
	 * @param sendTo
	 * @param status
	 * @param fromId
	 * @param type
	 * @return
	 */
	public int insertMessageQ(String content, String fromWho, String sendTo, int status, int fromId, int type){
		int row = 0;
		MessageQ mq = new MessageQ();
		mq.setContent(content);
		mq.setFromWho(fromWho);
		mq.setSendTo(sendTo);
		mq.setStatus(status);
		mq.setFromId(fromId);
		mq.setType(type);
		mq.setAddTime(new Date());
		mq.setSendTime(new Date());
		
		if(type==1){ //邮箱
			List<MemberUser> memberUserList =  memberUserMapper.selectByEmail(sendTo);
			if(memberUserList.size()>0){
				mq.setToId(memberUserList.get(0).getmId());
			}
		}else{//短信
			List<Member> memberList = memberMapper.selectByPhone(sendTo);
			if(memberList.size()>0){
				mq.setToId(memberList.get(0).getId());
			}
		}
		row = messageQMapper.insertSelective(mq);
		
		return row;
	}
	
	public static String getSaleValeById(List<SimilarSku> list, String sku){
		String saleVale = null;
		String[] skuIds = null;
		StringBuffer buffer = new StringBuffer();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				 List<SaleAttr> saleAttrList=list.get(i).getSaleAttrList();
				 for(int j=0;j<saleAttrList.size();j++){
					 SaleAttr saleAttr=saleAttrList.get(j);
					 saleVale=saleAttr.getSaleValue();
					 skuIds=saleAttr.getSkuIds();
					 for(int k=0;k<skuIds.length;k++){
						 if(skuIds[k].equals(sku)){
							 buffer.append(saleVale); 
						 }
					 }
				 }
			 }
		}else{
			buffer.append("普通规格");
		}
		return buffer.toString();
	}
	
	private GoodsSku setKeyValue(GoodsSku entity, String sku) {
		List<SimilarSku> list = JDGoodsApi.getSimilarSku(sku);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					String saleName = list.get(i).getSaleName();
					entity.setKeyOne(saleName);
					List<SaleAttr> saleAttrList = list.get(i).getSaleAttrList();
					for (int j = 0; j < saleAttrList.size(); j++) {
						SaleAttr saleAttr = saleAttrList.get(j);
						String saleVale = saleAttr.getSaleValue();
						String[] skuIds = saleAttr.getSkuIds();
						for (int k = 0; k < skuIds.length; k++) {
							if (skuIds[k].equals(sku)) {
								entity.setValueOne(saleVale);
								break;
							}
						}
					}
					if (StringUtils.isBlank(entity.getValueOne())) {
						entity.setValueOne("*");
					}

				} else if (i == 1) {
					String saleName = list.get(i).getSaleName();
					entity.setKeyTwo(saleName);
					List<SaleAttr> saleAttrList = list.get(i).getSaleAttrList();
					for (int j = 0; j < saleAttrList.size(); j++) {
						SaleAttr saleAttr = saleAttrList.get(j);
						String saleVale = saleAttr.getSaleValue();
						String[] skuIds = saleAttr.getSkuIds();
						for (int k = 0; k < skuIds.length; k++) {
							if (skuIds[k].equals(sku)) {
								entity.setValueTwo(saleVale);
								break;
							}
						}

					}
					if (StringUtils.isBlank(entity.getValueTwo())) {
						entity.setValueTwo("*");
					}

				} else if (i == 2) {
					String saleName = list.get(i).getSaleName();
					entity.setKeyThree(saleName);
					List<SaleAttr> saleAttrList = list.get(i).getSaleAttrList();
					for (int j = 0; j < saleAttrList.size(); j++) {
						SaleAttr saleAttr = saleAttrList.get(j);
						String saleVale = saleAttr.getSaleValue();
						String[] skuIds = saleAttr.getSkuIds();
						for (int k = 0; k < skuIds.length; k++) {
							if (skuIds[k].equals(sku)) {
								entity.setValueThree(saleVale);
								break;
							}
						}
					}
					if (StringUtils.isBlank(entity.getValueThree())) {
						entity.setValueThree("*");
					}
				}
			}
		}
		return entity;
	}
	
	private Map<String, Object> setJdKeyValue(List<SimilarSku> list, Map<String, Object> entity, String sku){
		SaleAttr saleAttr = null;
		String saleVale = null;
		String[] skuIds = null; 
		List<SaleAttr> saleAttrList = null;
		String saleName = null;
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				if(i==0){
					saleName = list.get(i).getSaleName();
					entity.put("keyOne", saleName);
					saleAttrList=list.get(i).getSaleAttrList();
					for(int j=0;j<saleAttrList.size();j++){
						 saleAttr=saleAttrList.get(j);
						 saleVale=saleAttr.getSaleValue();
						 skuIds=saleAttr.getSkuIds();
						 for(int k=0;k<skuIds.length;k++){
							 if(skuIds[k].equals(sku)){
								entity.put("valueOne", saleVale);
							    break;
							 }
						 }
					}
					if (entity.get("valueOne")==null) {
						entity.put("valueOne", "*");
					}
				}else if(i==1){
					saleName = list.get(i).getSaleName();
					entity.put("keyTwo", saleName);
					saleAttrList=list.get(i).getSaleAttrList();
					for(int j=0;j<saleAttrList.size();j++){
						 saleAttr=saleAttrList.get(j);
						 saleVale=saleAttr.getSaleValue();
						 skuIds=saleAttr.getSkuIds();
						 for(int k=0;k<skuIds.length;k++){
							 if(skuIds[k].equals(sku)){
								entity.put("valueTwo", saleVale);
							    break;
							 }
						 }
					}
					if (entity.get("valueTwo")==null) {
						entity.put("valueTwo", "*");
					}
				}else if(i==2){
					saleName = list.get(i).getSaleName();
					entity.put("keyThree", saleName);
					saleAttrList=list.get(i).getSaleAttrList();
					for(int j=0;j<saleAttrList.size();j++){
						 saleAttr=saleAttrList.get(j);
						 saleVale=saleAttr.getSaleValue();
						 skuIds=saleAttr.getSkuIds();
						 for(int k=0;k<skuIds.length;k++){
							 if(skuIds[k].equals(sku)){
								entity.put("valueThree", saleVale);
							    break;
							 }
						 }
					}
					if (entity.get("valueThree")==null) {
						entity.put("valueThree", "*");
					}
				}else if(i==3){
					saleName = list.get(i).getSaleName();
					entity.put("keyFour", saleName);
					saleAttrList=list.get(i).getSaleAttrList();
					for(int j=0;j<saleAttrList.size();j++){
						 saleAttr=saleAttrList.get(j);
						 saleVale=saleAttr.getSaleValue();
						 skuIds=saleAttr.getSkuIds();
						 for(int k=0;k<skuIds.length;k++){
							 if(skuIds[k].equals(sku)){
								entity.put("valueFour", saleVale);
							    break;
							 }
						 }
					}
					if (entity.get("valueFour")==null) {
						entity.put("valueFour", "*");
					}
				}else if(i==4){
					saleName = list.get(i).getSaleName();
					entity.put("keyFive", saleName);
					saleAttrList=list.get(i).getSaleAttrList();
					for(int j=0;j<saleAttrList.size();j++){
						 saleAttr=saleAttrList.get(j);
						 saleVale=saleAttr.getSaleValue();
						 skuIds=saleAttr.getSkuIds();
						 for(int k=0;k<skuIds.length;k++){
							 if(skuIds[k].equals(sku)){
								entity.put("valueFive", saleVale);
							    break;
							 }
						 }
					}
					if (entity.get("valueFive")==null) {
						entity.put("valueFive", "*");
					}
				}
			}
		}
		return entity;
	}
	
	/**
	 * 获取所有的组合
	 * @param sku
	 * @return
	 */
	@Override
	public List<String> getSkuValue(String sku){
		List<SimilarSku> list = JDGoodsApi.getSimilarSku(sku);
		List<String> saleValeList=new ArrayList<String>();
		Set<String> saleIds=getSaleValueById(list);//获取所有的saleId
		for(String saleId:saleIds){
			String saleValue=getSaleValeById(saleId, list);//根据saleId获取saleValue
			if(saleValue!=null){
				saleValeList.add(saleValue);
			}
		}
		return saleValeList;
	}
	
	/**
	 * 通过skuId获取SaleVale
	 * @param id
	 * @param list
	 * @return
	 */
	public static String getSaleValeById(String id,List<SimilarSku> list){
		String str="";
		 for(int i=0;i<list.size();i++){
			 List<SaleAttr> saleAttrList=list.get(i).getSaleAttrList();
			 for(int j=0;j<saleAttrList.size();j++){
				 SaleAttr saleAttr=saleAttrList.get(j);
				 String saleVale=saleAttr.getSaleValue();
				 String [] skuIds=saleAttr.getSkuIds();
				 for(int k=0;k<skuIds.length;k++){
					 String skuId=skuIds[k];
					 if(skuId.equals(id)){
					    str+=saleVale+" ";	 
					 }
				 }
			 }
		 }
		return str;
	}
	
	/**
	 * 获取所有的skuIds
	 * @param list
	 * @return
	 */
	public static Set<String> getSaleValueById(List<SimilarSku> list){
		Set<String> idsSet = new HashSet<String>();  
		 for(int i=0;i<list.size();i++){
			 List<SaleAttr> saleAttrList=list.get(i).getSaleAttrList();
			 for(int j=0;j<saleAttrList.size();j++){
				 SaleAttr saleAttr=saleAttrList.get(j);
				 String [] skuIds=saleAttr.getSkuIds();
				 for(int k=0;k<skuIds.length;k++){
					 String skuId=skuIds[k];
					 idsSet.add(skuId);
				 }
			 }
		 }
		return idsSet;
	}
	
	/**
	 * 京东规格参数copy
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertJdParam(JSONArray list, String goodsId) throws Exception {
		int row = 0;
		boolean attrFlag = false;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
		    JSONObject ob = (JSONObject) it.next();
		    List<GoodsModel> modelList = modelMapper.selectByName(ob.getString("modelName"), ob.getString("catId")); //判断是否重名
		    if(modelList.size()>0){
				GoodsModelAttr modelAttr = insertAttr(ob.getString("attrName"), ob.getString("attrValue"), attrFlag, modelList.get(0));
				if(modelAttr!=null){
					row = insertGoodsAttr(Integer.parseInt(goodsId), modelAttr.getId(), ob.getString("attrValue"), modelList.get(0).getId());
				}else{
					row = 0;
				}
		    }else{
		    	    GoodsModel model = new GoodsModel();
				    model.setName(ob.getString("modelName")); //模型名称
				    model.setCatId(ob.getString("catId")); // 分类id
					model.setStatus(false);
					row = modelMapper.insertSelective(model);
					GoodsModelAttr attr = insertAttr(ob.getString("attrName"), ob.getString("attrValue"), attrFlag, model);
					if(attr!=null){
						row = insertGoodsAttr(Integer.parseInt(goodsId), attr.getId(), ob.getString("attrValue"), model.getId());
					}else{
						row = 0;
					}
		    }
		}
		return row;
	}
	/*public int insertJdParam(JSONArray list, String goodsId) throws Exception {
		int row = 0;
		boolean modelFlag = false;
		boolean attrFlag = false;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
		    JSONObject ob = (JSONObject) it.next();
		    List<GoodsModel> modelList = modelMapper.selectAllByName(ob.getString("modelName")); //判断是否重名
		    if(modelList.size()>0){
		    	String[] s1 = modelList.get(0).getCatId().split(",");
				for(int i=0; i<s1.length; i++){
					if(ob.getLong("catId")==Long.parseLong(s1[i])){
						modelFlag = true;
						row = 1;
						break;
					}
				}
				if(modelFlag==false){
					modelList.get(0).setCatId(modelList.get(0).getCatId() + "," + ob.getString("catId"));
					row = modelMapper.updateByPrimaryKeySelective(modelList.get(0));
				}
				GoodsModelAttr modelAttr = insertAttr(ob.getString("attrName"), ob.getString("attrValue"), attrFlag, modelList.get(0));
				if(modelAttr!=null){
					System.out.println(Integer.parseInt(goodsId));
					System.out.println(modelAttr.getId());
					System.out.println(ob.getString("attrValue"));
					System.out.println(modelList.get(0).getId());
					row = insertGoodsAttr(Integer.parseInt(goodsId), modelAttr.getId(), ob.getString("attrValue"), modelList.get(0).getId());
				}else{
					row = 0;
				}
		    }else{
		    	    GoodsModel model = new GoodsModel();
				    model.setName(ob.getString("modelName")); //模型名称
				    model.setCatId(ob.getString("catId")); // 分类id
					model.setStatus(false);
					row = modelMapper.insertSelective(model);
					
					System.out.println(model.getId());
					GoodsModelAttr attr = insertAttr(ob.getString("attrName"), ob.getString("attrValue"), attrFlag, model);
					if(attr!=null){
						System.out.println(Integer.parseInt(goodsId));
						System.out.println(attr.getId());
						System.out.println(ob.getString("attrValue"));
						System.out.println(model.getId());
						row = insertGoodsAttr(Integer.parseInt(goodsId), attr.getId(), ob.getString("attrValue"), model.getId());
					}else{
						row = 0;
					}
		    }
		}
		return row;
	}*/
	
	public GoodsModelAttr insertAttr(String attrName, String attrValue, boolean attrFlag, GoodsModel model){
		int row = 0; 
		GoodsModelAttr modelAttr = new GoodsModelAttr();
		List<GoodsModelAttr> attrList = modelAttrMapper.getByName(attrName, model.getId());//判断是否重名
		if(attrList.size()>0){
			String[] s2 = attrList.get(0).getValue().split(",");
			for(int i=0; i<s2.length; i++){;
				if(attrValue.equals(s2[i])){
					attrFlag = true;
					break;
				}
			}
			if(attrFlag==false){
				attrList.get(0).setValue(attrList.get(0).getValue()+"," + attrValue);
				row = modelAttrMapper.updateByPrimaryKeySelective(attrList.get(0));
			}
			return attrList.get(0);
		}else{
			modelAttr.setModelId(model.getId());
			modelAttr.setName(attrName); //属性名称
			modelAttr.setValue(attrValue); //属性值
			modelAttr.setSearch(true);
			row = modelAttrMapper.insertSelective(modelAttr);
		}
		if(row == 1){
			return modelAttr;
		}
		return null;
	}
	
	public int insertGoodsAttr(int goodsId, int attrId, String attrValue, int modelId){
		int row = 0;
		GoodsAttr attr = new GoodsAttr();
		attr.setAttrId(attrId);
		attr.setGoodsId(goodsId);
		attr.setAttrValue(attrValue);
		attr.setModelId(modelId);
		row = goodsAttrMapper.insertSelective(attr);
		return row;
	}
	@Transactional
	@Override
	public int batchAddJdGoods() throws Exception {
		int row = 0;
		List<JdGoodsCopy> list = jdGoodsCopyMapper.selectAll();
		int len = list.size();
		List threadList = new ArrayList();
		List<JdGoodsCopy> tempList = null;
		for(int j=0;j<len;j++){	
	        if(j%150==0){
	        	tempList = new ArrayList<JdGoodsCopy>();
	        	threadList.add(tempList);
	        }
	        tempList.add(list.get(j));
		}
		for(int i=0;i<threadList.size();i++){
			final List<JdGoodsCopy> dataList = (List<JdGoodsCopy>)threadList.get(i);
			Thread t = new Thread(){
				public void run(){
					try {
						if(dataList.size()>0){
							for(JdGoodsCopy entity : dataList){
								List<GoodsSku> jdSku = goodsSkuMapper.selectByJdSkuNo(entity.getJdSkuNo()); //判断京东商品是否已存在数据库
								if(jdSku.size()>0){
						    		jdSku.get(0).setGoodsName(entity.getGoodsName());
						    		jdSku.get(0).setMarketPrice(MoneyUtil.yuan2Fen(entity.getMarketPrice()));
						    		jdSku.get(0).setStockPrice(MoneyUtil.yuan2Fen(entity.getStockPrice()));
						    		jdSku.get(0).setSellPrice(MoneyUtil.yuan2Fen(entity.getSellPrice()));
						    		jdSku.get(0).setTeamPrice(MoneyUtil.yuan2Fen(entity.getTeamPrice()));
						    		jdSku.get(0).setStoreNums(10000);
						    		
						    		goodsSkuMapper.updateByPrimaryKeySelective(jdSku.get(0));
						    		
						    		Goods goods = goodsMapper.selectByPrimaryKey(jdSku.get(0).getGoodsId());
						    		if(goods!=null){
					    				goods.setName(entity.getGoodsName());
						    			//goods.setCatId(entity.getCategoryId());
						    			//goods.setCatName(entity.getCategoryName());
						    			goodsMapper.updateByPrimaryKey(goods);
						    		}
						    	}else{
						    		Goods goods = new Goods();
									goods.setSaleType(4); //销售模式--默认拼团单卖皆可
									goods.setRefundDays(-1); //退货天数--默认-1不限时间退货
									goods.setTeamPrice(0); //拼团价--默认0
									goods.setTeamNum(2);  //拼团人数--默认2
									goods.setTeamEndTime(24); //拼团持续时间--默认24小时
									goods.setTimeUnit(2);  //时间单位--默认小时
									goods.setIsCreate(0); //是否支持系统发起拼单-1否 0是',
									goods.setIsPromote(0); //是否促成团(-1 否 0是',
									goods.setStoreNums(10000); //库存
									goods.setName(entity.getGoodsName()); //商品名称
									goods.setTitle(entity.getGoodsName());
									//goods.setCatName(entity.getCategoryName());
									Timestamp now = new Timestamp(new Date().getTime());
									goods.setAddtime(now);
									goods.setEdittime(now);
									goods.setRefundDays(7);
									goods.setTagIds("35,37,36,38");
									
									GoodsDesc pcDesc = new GoodsDesc();
									GoodsDesc appDesc = new GoodsDesc();
									long brandId = 0;
									Detail detail = JDGoodsApi.getDetail(entity.getJdSkuNo().toString(), "true");
									if(detail!=null){
										goods.setImage(Contants.jdImage + detail.getImagePath()); //商品图片
										pcDesc.setDescription(detail.getIntroduction()); //详细介绍
										appDesc.setDescription(detail.getAppintroduce()); //移动端详细介绍
										
										String[] catIds = detail.getCategory().split(";");
										if(catIds.length>0){
											String catId = catIds[catIds.length-1];
											if(detail.getBrandName()!=null){
												brandId = insertBrand(detail.getBrandName(), catId);//插入品牌	
											}
											goods.setCatId(Long.parseLong(catId));
										}
										if(catIds.length==3){
											goods.setCatIdOne(Long.parseLong(catIds[0]));
											goods.setCatIdTwo(Long.parseLong(catIds[1]));
										}
										//JSONArray array = getJdCategoryArray(detail.getCategory()); //获取京东商品分类
										//insertJdCategory(array);

									}
									goods.setBrandId(brandId);
									goods.setIsJd(1);
									goods.setShopId(1);
									/*List<GoodsCategory> category = categoryMapper.getListByName(entity.getCategoryName());
									if(category.size()>0){
										goods.setCatId(category.get(0).getId());
									}*/
									goodsMapper.insertSelective(goods);
									
									pcDesc.setGoodsId(goods.getId());
									pcDesc.setIsPc(1);
									appDesc.setGoodsId(goods.getId());
									appDesc.setIsPc(0);
									descMapper.insertSelective(pcDesc);
									descMapper.insertSelective(appDesc);
									
									GoodsSku goodsSku = new GoodsSku();
									goodsSku.setSkuNo(MixCodeUtil.createOutTradeNo());
									goodsSku.setGoodsId(goods.getId());
									goodsSku.setGoodsName(entity.getGoodsName());
									goodsSku.setMarketPrice(MoneyUtil.yuan2Fen(entity.getMarketPrice()));
									goodsSku.setStockPrice(MoneyUtil.yuan2Fen(entity.getStockPrice()));
									goodsSku.setSellPrice(MoneyUtil.yuan2Fen(entity.getSellPrice()));
									goodsSku.setTeamPrice(MoneyUtil.yuan2Fen(entity.getTeamPrice()));
									goodsSku.setStoreNums(10000);
									
									List<SellPriceResult> resultList = JDGoodsApi.getSellPrice(entity.getJdSkuNo().toString());
									if(resultList.size()>0){
										Double jdPrice = resultList.get(0).getJdPrice()*100;
										goodsSku.setJdPrice(jdPrice.intValue()); //京东价
										Double jdBuyPrice = resultList.get(0).getPrice()*100;
										goodsSku.setJdBuyPrice(jdBuyPrice.intValue());//京东购买价
									}
									
									List<SellPriceResult> protocolList = JDGoodsApi.getPrice(entity.getJdSkuNo().toString());
									if(protocolList.size()>0){
										Double jdProtocolPrice = protocolList.get(0).getPrice()*100;
										goodsSku.setJdProtocolPrice(jdProtocolPrice.intValue()); //京东协议价
										goodsSku.setJdOldBuyPrice(jdProtocolPrice.intValue()); //协议价（旧）
										
										if(protocolList.get(0).getPrice()>=99){ //设置物流价格
											goodsSku.setDeliveryPrice(0);
										}else if(protocolList.get(0).getPrice()<49){
											goodsSku.setDeliveryPrice(1500);
										}else{
											goodsSku.setDeliveryPrice(600);
										}
									}
									
									StringBuffer buffer = new StringBuffer(); 
									Map<String, List<SkuImage>> map = JDGoodsApi.skuImage(entity.getJdSkuNo().toString());
									if(map!=null){
										List<SkuImage> imageList = map.get(entity.getJdSkuNo().toString());
										if(imageList.size()>0){
											for(SkuImage image : imageList){
												buffer.append("'"+Contants.jdImage+image.getPath()+"'"+",");
											}
											String str = buffer.toString().substring(0, buffer.toString().length()-1);
											//String value = "{'url':["+str+"],'value':'京东【"+entity+"】'}";
											List<SimilarSku> similarSkulist = JDGoodsApi.getSimilarSku(entity.getJdSkuNo().toString());
											String strValue = getSaleValeById(similarSkulist, entity.getJdSkuNo().toString());
											String value = "{'url':["+str+"],'value':'"+strValue+"'}";
											goodsSku.setValue(value);
											if(strValue.equals("普通规格")){
												goodsSku.setKeyOne("规格");
												goodsSku.setValueOne("普通规格");
											}
											
											setKeyValue(goodsSku, entity.getJdSkuNo().toString()); //插入属性值
											
											//JSONObject jsonObject = JSONObject.fromObject(value); //value转义
											//goodsSku.setValueObj(jsonObject);
										}
									}
									
									if(detail!=null){
										goodsSku.setJdSkuNo(detail.getSku()); //商品编码
										goodsSku.setJdParam(detail.getParam()); //技术参数介绍
										goodsSku.setJdUpc(detail.getUpc()); //条形码
										goodsSku.setGoodsName(detail.getName());
										
										/*String[] catIds = detail.getCategory().split(";");
										String categoryId = "1";
										if(catIds!=null){
											categoryId = catIds[catIds.length-1];
										}*/
										
										if(detail.getParam()!=null){
											insertItemModelVaue(detail.getParam(), goods.getId());
											//insertModelValue(detail.getParam(), goods.getId(), entity.getCategoryName(), categoryId);
										}
										/*GoodsCategroyJd catJd = new GoodsCategroyJd();
										catJd.setGoodsId(goods.getId());
										catJd.setJdCatId(Long.parseLong(categoryId));
										List<GoodsCategory> myCat = categoryMapper.getByName(entity.getCategoryName());
										if(myCat.size()>0){
											catJd.setMyCatId(myCat.get(0).getId());
										}
										catJdMapper.insertSelective(catJd);*/
										
									}
									goodsSkuMapper.insertSelective(goodsSku);
									
						    	}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					
				}
			};
			t.start();
		}
		
		return row;
	}
	
	@Override
	public int insertItemModelVaue(String htmlStr, Integer goodsId){	
		Document doc = Jsoup.parse(htmlStr);  
	    // 根据id获取table  
	    Elements table = doc.getElementsByClass("Ptable");  
	    //-----------
	    Elements tbody = table.select("tbody");
	    // 使用选择器选择该table内所有的<tr> <tr/>  
	    Elements trs = tbody.select("tr");  
	   
	    List<Map<String, Object>> listMap = new ArrayList<>();
	    List<Map<String, Object>> listMapOne = null;
	    //遍历该表格内的所有的<tr> <tr/>  
	    for (int i = 0; i < trs.size(); ++i) {
	        // 获取一个tr  
	        Element tr = trs.get(i);
	        //--------------
	        Elements ths = tr.select("th");
	        if(ths.size()>0){
	        	listMapOne = new ArrayList<>();
	        	Map<String, Object> mapOne = new HashMap<>();
	    		mapOne.put("group", ths.get(0).text());
	    		mapOne.put("params", listMapOne);
	    		listMap.add(mapOne);
	    		
	    		if(listMapOne.size()>0){
	        		listMapOne.clear();
	        	}
	        }
	        // 获取该行的所有td节点  
	        Elements tds = tr.select("td");  
	        if(tds.size()==2){
	        	Map<String, Object> mapTwo = new HashMap<>();
	    		mapTwo.put("key", tds.get(0).text());
	    		mapTwo.put("value", tds.get(1).text());
	    		listMapOne.add(mapTwo);
	        }

	    }  
	    JSONArray array = JSONArray.fromObject(listMap);
		ItemModelValue modelValue = new ItemModelValue();
		modelValue.setGoodsId(goodsId);
		modelValue.setParamData(array.toString());
		modelValue.setAddTime(new Date());
		return modelValueMapper.insertSelective(modelValue);
	}
	
	@Override
	public int insertModelValue(String htmlStr, Integer goodsId, String catName, String catId){
		int row = 0;
		JSONArray arr = new JSONArray();
		
		Document doc = Jsoup.parse(htmlStr);  
	    // 根据id获取table  
	    Elements table = doc.getElementsByClass("Ptable");  
	    //-----------
	    Elements tbody = table.select("tbody");
	    // 使用选择器选择该table内所有的<tr> <tr/>  
	    Elements trs = tbody.select("tr");  
	    //遍历该表格内的所有的<tr> <tr/>  
	    String modelName = null;
	    for (int i = 0; i < trs.size(); ++i) {
	    	JSONObject object = new JSONObject();
	    	
	        // 获取一个tr  
	        Element tr = trs.get(i);
	        //--------------
	        Elements ths = tr.select("th");
	        for (int j = 0; j < ths.size(); ++j) {
	        	Element th = ths.get(j);
	        	if(th.text()!=null){
	        		modelName = th.text();
		        }
	        }
	        // 获取该行的所有td节点  
	        Elements tds = tr.select("td");  
	        // 选择某一个td节点  
	        if(tds.size()==2){
	        	String attrValue = null;
	        	String attrName = null;
	        	attrName = tds.get(0).text();
	        	attrValue = tds.get(1).text();
	        	if(attrName !=null && attrValue!=null){
			        	object.put("modelName", modelName);
			        	object.put("attrName", attrName);
			        	object.put("attrValue", attrValue);
			        	object.put("catId", catId);
			        	arr.add(object);
			    }
	        }
	    }  
	    
	    try {
	    	copyJdParam(arr, goodsId.toString(), catName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
	
	public int copyJdParam(JSONArray list, String goodsId, String catName) throws Exception {
		int row = 0;
		boolean attrFlag = false;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
		    JSONObject ob = (JSONObject) it.next();
		    List<GoodsModel> modelList = modelMapper.selectByName(ob.getString("modelName"), ob.getString("catId")); //判断是否重名
		    if(modelList.size()>0){
				GoodsModelAttr modelAttr = insertAttr(ob.getString("attrName"), ob.getString("attrValue"), attrFlag, modelList.get(0));
				if(modelAttr!=null){
					row = insertGoodsAttr(Integer.parseInt(goodsId), modelAttr.getId(), ob.getString("attrValue"), modelList.get(0).getId());
				}else{
					row = 0;
				}
		    }else{
		    	    GoodsModel model = new GoodsModel();
				    model.setName(ob.getString("modelName")); //模型名称
				    model.setCatId(ob.getString("catId")); // 分类id
				    model.setCatName(catName);
					model.setStatus(false);
					row = modelMapper.insertSelective(model);
					GoodsModelAttr attr = insertAttr(ob.getString("attrName"), ob.getString("attrValue"), attrFlag, model);
					if(attr!=null){
						row = insertGoodsAttr(Integer.parseInt(goodsId), attr.getId(), ob.getString("attrValue"), model.getId());
					}else{
						row = 0;
					}
		    }
		}
		return row;
	}
	
	private long insertBrand(String name, String catId){
		GoodsBrand goodsBrand = new  GoodsBrand();
		GoodsBrand brand = goodsBrandMapper.selectByNameAndJd(name, 1);//判断品牌是否存在
		boolean isFlag = false;
		if(brand!=null){
			String[] s1 = brand.getCatId().split(",");
			for(int i=0; i<s1.length; i++){
				if(Integer.parseInt(catId)==Integer.parseInt(s1[i])){
					isFlag = true;
					break;
				}

			}
			if(isFlag==false){
				String catIdStr = brand.getCatId()+","+catId;
				brand.setCatId(catIdStr);
				goodsBrandMapper.updateByPrimaryKeySelective(brand);
			}
			return brand.getId();
		}else{
			goodsBrand.setId(Long.parseLong(MixCodeUtil.createOutTradeNo()));
			goodsBrand.setName(name);
			goodsBrand.setCatId(catId);
			goodsBrand.setIsJd(1);
			goodsBrandMapper.insertSelective(goodsBrand);
			return goodsBrand.getId();
		}
		
	}
	
	private int insertJdCategory(JSONArray list) throws Exception {
		int row = 0;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			GoodsCategory entity = categoryMapper.selectByPrimaryKey(ob.getLong("id"));
			if(entity==null){
				//GoodsCategory goodsCatgory = mapper.selectByNameAndSeries(ob.getString("name"), Short.parseShort(ob.getString("series")), ob.getLong("reid"), 1);
				GoodsCategory category = new GoodsCategory();
				category.setId(ob.getLong("id"));
				category.setName(ob.getString("name"));
				category.setReid(ob.getLong("reid"));
				category.setIsLast(ob.getBoolean("isLast"));
				category.setSeries(Short.parseShort(ob.getString("series")));
				category.setFlag(false);
				category.setIsJd(1);
				row = categoryMapper.insertSelective(category);
			}
		}
		return row;
	}
	
	/**
	 * 京东商品sku搜索
	 */
	@Override
	public SearchResult jdSkuNoSearch(String skuNo) throws Exception {
		SearchResult result = new SearchResult();
		if(JDGoodsApi.getSellPrice(skuNo)==null || JDGoodsApi.getSellPrice(skuNo).isEmpty()){ //判断是否在库中
			return result;
		}
		if(JDGoodsApi.skuState(skuNo).equals("0")){ //判断商品是否上架
			return result;
		}
		Detail detail = JDGoodsApi.getDetail(skuNo, "true");
		if(detail!=null){
			List<HitResult> hitList = new ArrayList<>();
			HitResult hit = new HitResult();
			if(detail.getBrandName()!=null){
				hit.setBrand(detail.getBrandName());
			}
			if(detail.getCategory()!=null){
				String[] str = detail.getCategory().split(";");
				hit.setCatId(str[2]);
				hit.setCid1(str[0]);
				hit.setCid2(str[1]);
			}
			hit.setImageUrl(detail.getImagePath());
			hit.setWareId(detail.getSku().toString());
			hit.setWareName(detail.getName());
			
			List<GoodsSku> sku = goodsSkuMapper.selectByJdSkuNo(detail.getSku());
			if(sku.size()>0){
				hit.setIsGet(true);
			}else{
				hit.setIsGet(false);
			}
			
			hitList.add(hit);
			
			result.setHitResult(hitList);
			result.setResultCount(1);
			result.setPageCount(1);
		}
		return result;
	}
	/**
	 * 复制sku判断是否已存在
	 */
	@Override
	public Map<String, Object> batchAddJdGoods(String goodsId, String jdSkuNo) throws Exception {
		boolean flag = false;
		Map<String, Object> map = new HashMap<>();
		List<String> strList = new ArrayList<>();
		List<GoodsSku> list = goodsSkuMapper.selectListByGidAndJdSkuNoAndStatus(Integer.parseInt(goodsId));
		if(list.size()>0){
			for(GoodsSku entity : list){
				strList.add(entity.getJdSkuNo().toString());
			}
		}
		if(strList.size()>0){
			for(String str : strList){
				if(jdSkuNo.equals(str)){
					flag = true;
				}
			}
		}
		String no = MixCodeUtil.createOutTradeNo(); //生成商品编码
		map.put("isExist", flag);
		map.put("skuNo", no);
		return map;
	}
	
	/**
	 * 京东商品池商品入库
	 */
	@Override
	public int exportJdGoods() throws Exception {
		int row = 0;
		String poolNum = null;
		JdGoods entity = new JdGoods();
		List<PageNum> list = JDGoodsApi.getPageNum();
		for(int m=0; m<list.size(); m++){
			poolNum = list.get(m).getPage_num();
			entity.setPoolNum(poolNum);
			SkuByPage skuByPage = JDGoodsApi.getSkuByPage(poolNum, "1");
			String[] skuStr = skuByPage.getSkuIds();
			for (int i = 0; i < skuStr.length; i++) {
				if(skuStr[i] != null && skuStr[i] != ""){
					entity.setJdSkuNo(Long.valueOf(skuStr[i]));
					List<JdGoods> jd = jdGoodsMapper.getByJdSkuNoAndPoolNum(entity);
					if(jd.size()==0){//判断是否已存在
						Detail detail = JDGoodsApi.getDetail(skuStr[i], "false");
						JdGoods jdGoods = new JdGoods();
						jdGoods.setPoolNum(poolNum);//所属商品池编号
						jdGoods.setJdSkuNo(Long.valueOf(skuStr[i]));//京东商品编码
						jdGoods.setGoodsImage(detail.getImagePath()); //商品主图
						jdGoods.setIsUp(detail.getState());//上架状态0下架，1上架
						jdGoods.setBrandName(detail.getBrandName()); //品牌名称
						jdGoods.setCatId(detail.getCategory()); //商品分类id
						jdGoods.setGoodsName(detail.getName());//商品名称
						jdGoods.setIsDelete(0); //是否删除0正常，1已删除
						jdGoods.setAddTime(new Date());//添加时间
						jdGoods.setEditTime(new Date());//编辑时间
						
						List<SellPriceResult> priceList = JDGoodsApi.getPrice(skuStr[i]);
						if(priceList.size()>0){
							jdGoods.setJdPrice((int)(priceList.get(0).getJdPrice()*100)); //京东价
							jdGoods.setStockPrice((int)(priceList.get(0).getPrice()*100)); //进货价
						}
						row = jdGoodsMapper.insertSelective(jdGoods);
					}
				}
			}
		}
		return row;
	}
	
	
	/**
	 * 京东商品列表
	 */
	@Override
	public PageBean<JdGoods> jdGoodsListPage(JdGoods entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<JdGoods> list = jdGoodsMapper.listPage(entity);
		PageBean<JdGoods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	@Override
	public ArrayList<ArrayList<String>> excelExport(Integer jdSkuNo, String poolNum, String goodsName, String brandName, String catId, Integer isUp,
			   Integer isDelete, Integer isGet, Double startJdPrice, Double endJdPrice, Double startStockPrice, Double endStockPrice) throws Exception {
		ArrayList<ArrayList<String>> arrayData = new ArrayList<>();
		List<JdGoods> goodsList = jdGoodsMapper.excelExportGoodsList(jdSkuNo, poolNum, goodsName, brandName, catId, isUp, isDelete, isGet, startJdPrice, endJdPrice, startStockPrice, endStockPrice);
		if(goodsList.size()>0){
			for(JdGoods g : goodsList){
				tradeData(arrayData, g);
			}
		} 
		return arrayData;
	}
	
	private ArrayList<ArrayList<String>> tradeData(ArrayList<ArrayList<String>> arrayData, JdGoods g){
		ArrayList<String> array = new ArrayList<>();
		array.add(0, g.getJdSkuNo().toString());  //商品编码
		array.add(1, g.getPoolNum());  //商品池编号
		array.add(2, g.getGoodsName());//商品名称
		array.add(3, g.getGoodsImage());//商品图片
		array.add(4, g.getBrandName());//品牌名称
		array.add(5, g.getCatId());//分类id
		array.add(6, (double)g.getJdPrice()/100+"");//京东价
		array.add(7, (double)g.getStockPrice()/100+""); //进货价
		arrayData.add(array);
		return arrayData;
	}
	
	@Override
	public int exportJdGoodsByNum(String poolNum) throws Exception {
		int row = 0;
		JdGoods entity = new JdGoods();
		entity.setPoolNum(poolNum);
		SkuByPage skuByPage = JDGoodsApi.getSkuByPage(poolNum, "1");
		String[] skuStr = skuByPage.getSkuIds();
		System.out.println(skuStr.length);
		for (int i = 0; i < skuStr.length; i++) {
			if(skuStr[i] !=null && skuStr[i] !=""){
				entity.setJdSkuNo(Long.valueOf(skuStr[i]));
				List<JdGoods> jd = jdGoodsMapper.getByJdSkuNoAndPoolNum(entity);
				if(jd.size()==0){
					Detail detail = JDGoodsApi.getDetail(skuStr[i], "false");
					JdGoods jdGoods = new JdGoods();
					jdGoods.setPoolNum(poolNum);//所属商品池编号
					jdGoods.setJdSkuNo(Long.valueOf(skuStr[i]));//京东商品编码
					jdGoods.setGoodsImage(detail.getImagePath()); //商品主图
					jdGoods.setIsUp(detail.getState());//上架状态0下架，1上架
					jdGoods.setBrandName(detail.getBrandName()); //品牌名称
					jdGoods.setCatId(detail.getCategory()); //商品分类id
					jdGoods.setGoodsName(detail.getName());//商品名称
					jdGoods.setIsDelete(0); //是否删除0正常，1已删除
					jdGoods.setAddTime(new Date());//添加时间
					jdGoods.setEditTime(new Date());//编辑时间
					
					List<SellPriceResult> priceList = JDGoodsApi.getPrice(skuStr[i]);
					if(priceList.size()>0){
						jdGoods.setJdPrice((int)(priceList.get(0).getJdPrice()*100)); //京东价
						jdGoods.setStockPrice((int)(priceList.get(0).getPrice()*100)); //进货价
					}
					List<GoodsSku> jdSku = goodsSkuMapper.selectByJdSkuNo(Long.valueOf(skuStr[i])); //判断京东商品是否已存在数据库
					if(jdSku.size()>0){
						jdGoods.setIsGet(1);//是否入库，0否，1是
					}else{
						jdGoods.setIsGet(0);
					}
					row = jdGoodsMapper.insertSelective(jdGoods);
				}
			}
		}
		return row;
	}
	
	
	@Override
	public int exportJdGoodsByNumAll(String poolNum) throws Exception {
		int row = 0;
		SkuByPage skuByPage = JDGoodsApi.getSkuByPage(poolNum, "1");
		String[] skuStr = skuByPage.getSkuIds();
		for (int i = 0; i < skuStr.length; i++) {
			Detail detail = JDGoodsApi.getDetail(skuStr[i], "false");
			JdGoods jdGoods = new JdGoods();
			jdGoods.setPoolNum(poolNum);//所属商品池编号
			jdGoods.setJdSkuNo(Long.valueOf(skuStr[i]));//京东商品编码
			jdGoods.setGoodsImage(detail.getImagePath()); //商品主图
			jdGoods.setIsUp(detail.getState());//上架状态0下架，1上架
			jdGoods.setBrandName(detail.getBrandName()); //品牌名称
			jdGoods.setCatId(detail.getCategory()); //商品分类id
			jdGoods.setGoodsName(detail.getName());//商品名称
			jdGoods.setIsDelete(0); //是否删除0正常，1已删除
			jdGoods.setAddTime(new Date());//添加时间
			jdGoods.setEditTime(new Date());//编辑时间
			
			List<SellPriceResult> priceList = JDGoodsApi.getPrice(skuStr[i]);
			if(priceList.size()>0){
				jdGoods.setJdPrice((int)(priceList.get(0).getJdPrice()*100)); //京东价
				jdGoods.setStockPrice((int)(priceList.get(0).getPrice()*100)); //进货价
			}
			
			List<GoodsSku> jdSku = goodsSkuMapper.selectByJdSkuNo(Long.valueOf(skuStr[i])); //判断京东商品是否已存在数据库
			if(jdSku.size()>0){
				jdGoods.setIsGet(1);//是否入库，0否，1是
			}else{
				jdGoods.setIsGet(0);
			}
			row = jdGoodsMapper.insertSelective(jdGoods);
		}
		return row;
	}
	
	/**
	 * 商品摘取（优）
	 */
	@Override
	public Map<String, Object> downJdGoods(String sku) throws Exception {
		Map<String, Object> goodsMap = new HashMap<>();
		boolean flag = false;
		List<Map<String, Object>> skuMapList = new ArrayList<>();
		List<SellPriceResult> list = null;
		Map<String, List<SkuImage>> map = null;
		List<SkuImage> imageList = null;
		String str = null;
		String strValue = null;
		Detail detail = null;
		List<GoodsSku> jdSku = null;
		List<SimilarSku> similarSkulist = null;
		List<String> skus = getAllSku(sku);
		for(String entity : skus){
			jdSku = goodsSkuMapper.isExistByJdSkuNo(Long.valueOf(entity)); //判断京东商品是否已存在数据库
			if(jdSku.size()==0){
				if(!flag){
					goodsMap.put("saleType", 1);
					goodsMap.put("refundDays", -1);
					goodsMap.put("teamPrice", 0);
					goodsMap.put("teamNum", 2);
					goodsMap.put("teamEndTime", 24);
					goodsMap.put("timeUnit", 2);
					goodsMap.put("isCreate", 0);
					goodsMap.put("isPromote", 0);
					goodsMap.put("storeNums", 10000);
				}
				Map<String, Object> skuMap = new HashMap<>();
				skuMap.put("storeNums", 10000);
				skuMap.put("jdSupport", 1);
				skuMap.put("score", 0);
				list = JDGoodsApi.getSellPrice(entity);
				if(list.size()>0){
					skuMap.put("realJdPrice", list.get(0).getJdPrice());
					skuMap.put("marketRealPrice", list.get(0).getJdPrice());
					skuMap.put("realJdBuyPrice", list.get(0).getPrice());
					skuMap.put("realPrice", list.get(0).getPrice());
					skuMap.put("realTeamPrice", list.get(0).getPrice());
					
					skuMap.put("realJdProtocolPrice", list.get(0).getPrice());
					skuMap.put("realStockPrice", list.get(0).getPrice());
					
					if(list.get(0).getPrice()>=99){ //设置物流价格
						skuMap.put("realDeliveryPrice", 0.0);
					}else if(list.get(0).getPrice()<49){
						skuMap.put("realDeliveryPrice", 8.0);
					}else{
						skuMap.put("realDeliveryPrice", 6.0);
					}
				}else{
					logger.error("getSellPrice 京东查询售卖价格获取不到！");
					goodsMap.put("code", 700);
					return goodsMap;
				}
				StringBuffer buffer = new StringBuffer();
				StringBuffer bufferStr = new StringBuffer();
				map = JDGoodsApi.skuImage(entity);
				if(map!=null){
					imageList = map.get(entity);
					if(imageList.size()>0){
						for(SkuImage image : imageList){
							buffer.append("'").append(Contants.jdImage+image.getPath()).append("'").append(",");
						}
						str = buffer.toString().substring(0, buffer.toString().length()-1);
						similarSkulist = JDGoodsApi.getSimilarSku(sku);
						strValue = getSaleValeById(similarSkulist, entity);
						bufferStr.append("{'url':[").append(str).append("],'value':'").append(strValue).append("'}");
						if(strValue.equals("普通规格")){
							skuMap.put("keyOne", "规格");
							skuMap.put("valueOne", "普通规格");
						}else{
							setJdKeyValue(similarSkulist, skuMap, entity); //插入属性值
						}
						skuMap.put("valueObj", JSONObject.fromObject(bufferStr.toString()));
					}
				}else{
					logger.error("skuImage京东商品图片获取不到！");
					goodsMap.put("code", 500);
					return goodsMap;
				}
				detail = JDGoodsApi.getDetail(entity, "true");
				if(detail!=null){
					if(!flag){
						goodsMap.put("name", detail.getName());
						goodsMap.put("image", Contants.jdImage + detail.getImagePath());
						goodsMap.put("description", detail.getIntroduction());
						goodsMap.put("appdescription", detail.getAppintroduce());
						goodsMap.put("jdParam", detail.getParam());
					}
					skuMap.put("jdSkuNo", detail.getSku());
					skuMap.put("jdUpc", detail.getUpc());
					skuMap.put("goodsName", detail.getName());
				}else{
					logger.error("getDetail 京东商品详情获取不到！");
					goodsMap.put("code", 600);
					return goodsMap;
				}
				skuMapList.add(skuMap);
				if(!flag){
					goodsMap.put("isJd", 1);
				}
				flag = true;
			}
		}
		goodsMap.put("skuList", skuMapList);
		if(skuMapList.size()>0 && skuMapList !=null){
			goodsMap.put("code", 0);
		}else{
			goodsMap.put("code", 400);
		}
		return goodsMap;
	}
	
	/**
	 * 京东商品池商品入库(skuNo先入)
	 */
	@Override
	public int exportJdSkuNo() throws Exception {
		Long startTime = System.currentTimeMillis();
		int row = 0;
		String poolNum = null;
		JdGoods entity = new JdGoods();
		List<PageNum> list = JDGoodsApi.getPageNum();
		for(int m=0; m<list.size(); m++){
			poolNum = list.get(m).getPage_num();
			entity.setPoolNum(poolNum);
			SkuByPage skuByPage = JDGoodsApi.getSkuByPage(poolNum, "1");
			if(skuByPage!=null){
				String[] skuStr = skuByPage.getSkuIds();
				for (int i = 0; i < skuStr.length; i++) {
					if(skuStr[i] != null && skuStr[i] != ""){
						entity.setJdSkuNo(Long.valueOf(skuStr[i]));
						List<JdGoods> jd = jdGoodsMapper.getByJdSkuNoAndPoolNum(entity);
						if(jd.size()==0){//判断是否已存在
							JdGoods jdGoods = new JdGoods();
							jdGoods.setPoolNum(poolNum);//所属商品池编号
							jdGoods.setJdSkuNo(Long.valueOf(skuStr[i]));//京东商品编码
							jdGoods.setAddTime(new Date());//添加时间
							row = jdGoodsMapper.insertSelective(jdGoods);
						}else{
							List<SellPriceResult> priceList = JDGoodsApi.getSellPrice(skuStr[i]);
							if(priceList.size()==0){//不在池中的商品，移除掉
								JdGoods g = new JdGoods();
								g.setJdSkuNo(Long.valueOf(skuStr[i]));
								List<JdGoods> isExitList = jdGoodsMapper.getByJdSkuNo(g);
								for(JdGoods goods : isExitList){
									jdGoodsMapper.deleteByPrimaryKey(goods.getId());
								}
							}
						}
					}
				}
				
				if(skuByPage.getPageCount()>1){ //该商品池总数超过10000条
					SkuByPage skuByPageTwo = JDGoodsApi.getSkuByPage(poolNum, "2");
					if(skuByPageTwo!=null){
						String[] skuStrTwo = skuByPageTwo.getSkuIds();
						for (int i = 0; i < skuStrTwo.length; i++) {
							if(skuStrTwo[i] != null && skuStrTwo[i] != ""){
								String skuStatus = JDGoodsApi.skuState(skuStrTwo[i]);//1为上架，0为下架
								if(skuStatus.equals("1")){
									entity.setJdSkuNo(Long.valueOf(skuStrTwo[i]));
									List<JdGoods> jd = jdGoodsMapper.getByJdSkuNoAndPoolNum(entity);
									if(jd.size()==0){//判断是否已存在
										JdGoods jdGoods = new JdGoods();
										jdGoods.setPoolNum(poolNum);//所属商品池编号
										jdGoods.setJdSkuNo(Long.valueOf(skuStrTwo[i]));//京东商品编码
										jdGoods.setAddTime(new Date());//添加时间
										row = jdGoodsMapper.insertSelective(jdGoods);
									}else{
										List<SellPriceResult> priceList = JDGoodsApi.getSellPrice(skuStrTwo[i]);
										if(priceList.size()==0){//不在池中的商品，移除掉
											JdGoods g = new JdGoods();
											g.setJdSkuNo(Long.valueOf(skuStrTwo[i]));
											List<JdGoods> isExitList = jdGoodsMapper.getByJdSkuNo(g);
											for(JdGoods goods : isExitList){
												jdGoodsMapper.deleteByPrimaryKey(goods.getId());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		Long endTime = System.currentTimeMillis();
		Long thisTime = endTime - startTime;
		System.out.println("总共用时：-----》"+thisTime+"s");
		return row;
	}
	
	/**
	 * 京东商品池商品详细信息入库(根据skuNo更新京东商品表)
	 */
	@Override
	public int detailsByJdSkuNo() throws Exception {
		Long startTime = System.currentTimeMillis();
		int row = 0;
		List<JdGoods> list = jdGoodsMapper.selectAllJdSkuNo();
		for(JdGoods entity : list){
			Detail detail = JDGoodsApi.getDetail(entity.getJdSkuNo().toString(), "false");
			entity.setGoodsImage(detail.getImagePath()); //商品主图
			entity.setIsUp(detail.getState());//上架状态0下架，1上架
			entity.setBrandName(detail.getBrandName()); //品牌名称
			entity.setCatId(detail.getCategory()); //商品分类id
			entity.setGoodsName(detail.getName());//商品名称
			entity.setIsDelete(0); //是否删除0正常，1已删除
			entity.setEditTime(new Date());//编辑时间
			List<SellPriceResult> priceList = JDGoodsApi.getPrice(entity.getJdSkuNo().toString());
			if(priceList.size()>0){
				if(entity.getJdPrice()==0){//防止上一个调度没有跑完异步更新
					Double jdPrice = priceList.get(0).getJdPrice()*100;
					Double price = priceList.get(0).getPrice()*100;
					entity.setJdPrice(jdPrice.intValue()); //京东价
					entity.setStockPrice(price.intValue()); //进货价
					row = jdGoodsMapper.updateByPrimaryKeySelective(entity);
				}
			}else{
				row = jdGoodsMapper.deleteByPrimaryKey(entity.getId()); //不在库中，清除掉
			}
		}
		Long endTime = System.currentTimeMillis();
		int alltime = (int)(endTime-startTime)/1000;
		System.out.println("总共用时：---》"+alltime+"s");
		return row;
	}
	
	
	@Override
	public String getJdStock(String jdSkuNo, String one, String two, String three, String four) throws Exception {
		String str = null;
		String fourId = null;
		GoAddressArea idOne = goAddressMapper.selectByName(one);
		GoAddressArea idTwo = goAddressMapper.selectByName(two);
		GoAddressArea idThree = goAddressMapper.selectByName(three);
		GoAddressArea idFour = goAddressMapper.selectByName(four);
		StringBuffer buffer = new StringBuffer();
		if(idOne!=null){
			buffer.append(idOne.getId());
		}else{
			buffer.append("0");
		}
		buffer.append("_");
		if(idTwo!=null){
			buffer.append(idTwo.getId());
		}else{
			buffer.append("0");
		}
		buffer.append("_");
		if(idThree!=null){
			buffer.append(idThree.getId());
		}else{
			buffer.append("0");
		}
		buffer.append("_");
		if(idFour!=null){
			buffer.append(idFour.getId());
			fourId = idFour.getId().toString();
		}else{
			buffer.append("0");
			fourId = "0";
		}
		System.out.println(buffer.toString());
		String Stock = JDGoodsApi.getStockById(jdSkuNo,buffer.toString());
	    JSONObject json = JSONObject.fromObject(Stock);
	    JSONArray array = JSONArray.fromObject(json.get("result"));
	    str = array.getJSONObject(0).get("state").toString();
	    
	    String areaLimit = JDGoodsApi.checkAreaLimit(jdSkuNo, idOne.getId().toString(), idTwo.getId().toString(), idThree.getId().toString(), fourId);
	    JSONObject jsonLimit = JSONObject.fromObject(areaLimit);
	    JSONArray arrayLimit = JSONArray.fromObject(jsonLimit.get("result"));
	    if(arrayLimit!=null){
	    	str = str+"_"+arrayLimit.getJSONObject(0).get("isAreaRestrict").toString();
	    }
		return str;
	}
	@Override
	public int testChange(String jdSku) throws Exception {
		int row = 0;
		int count = 0;
		double price = 0;
		double priceOne = 0;
		List<SellPriceResult> list = JDGoodsApi.getPrice(jdSku);//获取京东价和协议价
		for(SellPriceResult result : list){
			List<GoodsSku> goodsSku = goodsSkuMapper.selectByJdSkuNo(result.getSkuId());
			if(goodsSku.size()>0){
				Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.get(0).getGoodsId());
				double realJdPrice = (double)goodsSku.get(0).getJdPrice()/100;
				double realJdProtocolPrice = (double)goodsSku.get(0).getJdProtocolPrice()/100;
				double realSellPrice = (double)goodsSku.get(0).getSellPrice()/100;
				
				if(result.getJdPrice() > realJdPrice && result.getPrice() > realJdProtocolPrice){ //京东价up协议价up
					price = result.getJdPrice() - realJdPrice; //差价
					priceOne = result.getPrice() - realJdProtocolPrice;
					row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
					row = changeOtherPriceOne(goodsSku.get(0).getId(), price, priceOne, 1);
					/*double resultSellPrice = 0;
					if(price > priceOne){
						resultSellPrice = realSellPrice+price;
					}else{
						resultSellPrice = realSellPrice+priceOne;
					}
					if(resultSellPrice > result.getJdPrice()){//单卖价高于京东价
						price = resultSellPrice - result.getJdPrice(); //差价
						row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,(int) Math.round(result.getJdPrice()*100),(int) Math.round(result.getPrice()*100), goodsSku.get(0));
						count ++;
					}*/
				}
				else if(result.getJdPrice() < realJdPrice && result.getPrice() < realJdProtocolPrice){//京东价down协议价down
					price = realJdPrice - result.getJdPrice(); //差价
					priceOne = realJdProtocolPrice - result.getPrice();
					row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
					row = changeOtherPriceOne(goodsSku.get(0).getId(), price, priceOne, 2);
					
				}
				else if(result.getJdPrice()>realJdPrice && result.getPrice()==realJdProtocolPrice){ //京东价up,协议价no
					price = result.getJdPrice() - realJdPrice; //差价
					row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
					row = changeOtherPrice(goodsSku.get(0).getId(), price, 1);
					
				}
				else if(result.getJdPrice()<realJdPrice && result.getPrice()==realJdProtocolPrice){//京东价down,协议价no
					price = realJdPrice - result.getJdPrice(); //差价
					row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
					if(realJdPrice < result.getPrice()){ //京东价降的比协议价还低
						priceOne = result.getPrice()-realJdPrice;
						row = insertNoticeOne(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), (double)(Math.round(price*100)/100.0), (double)(Math.round(priceOne*100)/100.0),(int) Math.round(result.getJdPrice()*100),(int) Math.round(result.getPrice()*100), goodsSku.get(0));
						count ++;
					}else{
						row = changeOtherPrice(goodsSku.get(0).getId(), price, 2);
					}
					
				}
				else if(result.getJdPrice()==realJdPrice && result.getPrice()>realJdProtocolPrice){//京东价no,协议价up
					price = result.getPrice() - realJdProtocolPrice; //差价
					row = changePrice(goodsSku.get(0).getId(), result.getPrice(), 3);
					row = changeOtherPrice(goodsSku.get(0).getId(), price, 1);
					/*double realPrice = realSellPrice+price;
					if(realPrice > result.getJdPrice()){//单卖价高于京东价
						price = realPrice - result.getJdPrice(); //差价
						row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,(int) Math.round(result.getJdPrice()*100),(int) Math.round(result.getPrice()*100), goodsSku.get(0));
						count ++;
					}*/
				}
				else if(result.getJdPrice()==realJdPrice && result.getPrice()<realJdProtocolPrice){//京东价no,协议价down
					price = realJdProtocolPrice - result.getPrice(); //差价
					row = changePrice(goodsSku.get(0).getId(), result.getPrice(), 3);
					row = changeOtherPrice(goodsSku.get(0).getId(), price, 2);
				}
				else if(result.getJdPrice()>realJdPrice && result.getPrice()<realJdProtocolPrice){//京东价up,协议价down
					price = result.getJdPrice() - realJdPrice; //差价
					priceOne = realJdProtocolPrice - result.getPrice();
					row = insertNoticeTwo(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), (double)(Math.round(price*100)/100.0), (double)(Math.round(priceOne*100)/100.0), 1,(int) Math.round(result.getJdPrice()*100),(int) Math.round(result.getPrice()*100), goodsSku.get(0));
					row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
					count ++;
				}
				else if(result.getJdPrice()<realJdPrice && result.getPrice()>realJdProtocolPrice){//京东价down,协议价up
					price = realJdPrice - result.getJdPrice(); //差价
					priceOne = result.getPrice() - realJdProtocolPrice;
					row = insertNoticeTwo(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), (double)(Math.round(price*100)/100.0), (double)(Math.round(priceOne*100)/100.0), 2,(int) Math.round(result.getJdPrice()*100),(int) Math.round(result.getPrice()*100), goodsSku.get(0));
					row = changePriceOne(goodsSku.get(0).getId(), result.getJdPrice(), result.getPrice());
					count ++;
				}
				
				GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(goodsSku.get(0).getId());
				if(sku!=null){
					double realPrice = (double)sku.getSellPrice()/100;
					System.out.println("realPrice--->"+realPrice);
					System.out.println("result.getJdPrice()--->"+result.getJdPrice());
					if(realPrice > result.getJdPrice()){//单卖价高于京东价
						price = realPrice - result.getJdPrice(); //差价
						row = insertNoticeThree(goodsSku.get(0).getJdSkuNo(), goods.getName(), goodsSku.get(0).getGoodsId(), price,(int) Math.round(result.getJdPrice()*100),(int) Math.round(result.getPrice()*100), goodsSku.get(0), sku, 1);
						row = changePrice(goodsSku.get(0).getId(), result.getJdPrice(), 1);
						count ++;
					}
				}
			}
		}
		return 0;
	}
	@Override
	public int syncUpOrDownStatus(String jdSkuNo) throws Exception {
		int row = 0;
		JdGoods entity = new JdGoods();
		entity.setJdSkuNo(Long.valueOf(jdSkuNo));
		List<JdGoods> list = jdGoodsMapper.getByJdSkuNo(entity);
		if(list.size()>0){
			long time = JedisUtil.getInstance().time();
			Date date = new Date(time);
			for(JdGoods jdGoods : list){
				if(jdGoods.getIsUp()==1){
					jdGoods.setIsUp(0);
					jdGoods.setEditTime(date);
					row = jdGoodsMapper.updateByPrimaryKeySelective(jdGoods);
				}
			}
		}
		return row;
	}
	@Override
	public int syncDeleteStatus(String jdSkuNo) throws Exception {
		int row = 0;
		JdGoods entity = new JdGoods();
		entity.setJdSkuNo(Long.valueOf(jdSkuNo));
		List<JdGoods> list = jdGoodsMapper.getByJdSkuNo(entity);
		if(list.size()>0){
			long time = JedisUtil.getInstance().time();
			Date date = new Date(time);
			for(JdGoods jdGoods : list){
				if(jdGoods.getIsDelete()==0){
					jdGoods.setIsDelete(1);
					jdGoods.setEditTime(date);
					row = jdGoodsMapper.updateByPrimaryKeySelective(jdGoods);
				}
			}
		}
		return row;
	}
	@Override
	public int syncDifferStatus() throws Exception {
		int row = 0;
		long time = JedisUtil.getInstance().time();
		Date date = new Date(time);
		
		//同步goods表与jd_goods表的入库状态
		List<JdGoods> listOne = jdGoodsMapper.syncIsGetStatusOne();
		if(listOne.size()>0){
			for(JdGoods jdGoods : listOne){
				jdGoods.setIsGet(0);
				jdGoods.setEditTime(date);
				jdGoodsMapper.updateByPrimaryKeySelective(jdGoods);
			}
		}
		List<JdGoods> listTwo = jdGoodsMapper.syncIsGetStatusTwo();
		if(listTwo.size()>0){
			for(JdGoods jdGoods : listTwo){
				jdGoods.setIsGet(1);
				jdGoods.setEditTime(date);
				jdGoodsMapper.updateByPrimaryKeySelective(jdGoods);
			}
		}
		//goods表已删除，goods_sku表未删除
		List<GoodsSku> skuList = goodsSkuMapper.syncSkuDelete();
		if(skuList.size()>0){
			long jdSkuNo = 0;
			for(GoodsSku goodsSku : skuList){
				goodsSku.setStatus(1);
				goodsSku.setJdSkuNo(jdSkuNo);
				goodsSku.setJdSupport(0);
				goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
			}
		}
		//goods表 非京东，sku京东
		List<GoodsSku> skuListTwo = goodsSkuMapper.syncSkuDeleteTwo();
		if(skuListTwo.size()>0){
			long jdSkuNo = 0;
			for(GoodsSku goodsSku : skuListTwo){
				goodsSku.setJdSkuNo(jdSkuNo);
				goodsSku.setJdSupport(0);
				goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
			}
		}
		return row;
	}
	
	@Override
	public int syncJdGoodsPrice() throws Exception {
		String str = null;
		JdGoods entity = new JdGoods();
		List<JdGoods> list = jdGoodsMapper.syncJdGoodsPrice();
		if (list.size() > 0) {
			int size = list.size();
			int pageSize = 99;
			int pageSum = size / pageSize;
			for (int pageIndex = 0; pageIndex <= pageSum; pageIndex++) {
				int end = (pageIndex + 1) * pageSize;
				if (end > size - 1) {
					end = size - 1;
				}
				List<JdGoods> listSub = list.subList(pageIndex * pageSize, end);
				StringBuffer jdSkuNos = new StringBuffer();
				if(listSub.size()>0){
					for(JdGoods jdGoods : listSub){
						jdSkuNos.append(jdGoods.getJdSkuNo()).append(",");
					}
					str = jdSkuNos.toString().substring(0, jdSkuNos.toString().length()-1);
					List<SellPriceResult> sellList = JDGoodsApi.getSellPrice(str);
					if(sellList.size()>0){
						for(SellPriceResult result : sellList){
							entity.setJdSkuNo(result.getSkuId());
							entity.setJdPrice(MoneyUtil.doubeToInt(result.getJdPrice()));
							entity.setStockPrice(MoneyUtil.doubeToInt(result.getPrice()));
							entity.setEditTime(new Date());
							jdGoodsMapper.updateByJdSkuNo(entity);
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 
	 * @Description:根据jdSkuNo更新京东商品的属性值
	 * @author xieyc
	 * @date 2018年9月5日 上午10:45:12
	 * 
	 */
	public int updateGoodSkuByJdSkuNo(String jdskuNo) {
		List<GoodsSku> listGoodsSku = goodsSkuMapper.selectByJdSkuNo(Long.parseLong(jdskuNo));
		if (listGoodsSku.size() > 1) {
			return -1;// 存在多个jdSkuNo相同的商品
		} else if (listGoodsSku.size() == 0) {
			return -2;// 改jdSkuNo的商品不存在
		} else {		
			GoodsSku goodsSku = listGoodsSku.get(0);
			Goods goods=goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
			if(goods.getStatus()!=5){
				return -3;//商品已经被下架
			}
			List<SimilarSku> similarSkulist = JDGoodsApi.getSimilarSku(jdskuNo);
			SaleAttr saleAttr = null;
			String saleVale = null;
			String[] skuIds = null;
			List<SaleAttr> saleAttrList = null;
			String saleName = null;
			if (similarSkulist != null && similarSkulist.size() > 0) {
				for (int i = 0; i < similarSkulist.size(); i++) {
					if (i == 0) {
						saleName = similarSkulist.get(i).getSaleName();
						goodsSku.setKeyOne(saleName);
						saleAttrList = similarSkulist.get(i).getSaleAttrList();
						for (int j = 0; j < saleAttrList.size(); j++) {
							saleAttr = saleAttrList.get(j);
							saleVale = saleAttr.getSaleValue();
							skuIds = saleAttr.getSkuIds();
							for (int k = 0; k < skuIds.length; k++) {
								if (skuIds[k].equals(jdskuNo)) {
									goodsSku.setValueOne(saleVale);
									break;
								}
							}
						}
						if (goodsSku.getValueOne() == null) {
							goodsSku.setValueOne("*");
						}
					} else if (i == 1) {
						saleName = similarSkulist.get(i).getSaleName();
						goodsSku.setKeyTwo(saleName);
						saleAttrList = similarSkulist.get(i).getSaleAttrList();
						for (int j = 0; j < saleAttrList.size(); j++) {
							saleAttr = saleAttrList.get(j);
							saleVale = saleAttr.getSaleValue();
							skuIds = saleAttr.getSkuIds();
							for (int k = 0; k < skuIds.length; k++) {
								if (skuIds[k].equals(jdskuNo)) {
									goodsSku.setValueTwo(saleVale);
									break;
								}
							}
						}
						if (goodsSku.getValueTwo() == null) {
							goodsSku.setValueTwo("*");
						}
					} else if (i == 2) {
						saleName = similarSkulist.get(i).getSaleName();
						goodsSku.setKeyThree(saleName);
						saleAttrList = similarSkulist.get(i).getSaleAttrList();
						for (int j = 0; j < saleAttrList.size(); j++) {
							saleAttr = saleAttrList.get(j);
							saleVale = saleAttr.getSaleValue();
							skuIds = saleAttr.getSkuIds();
							for (int k = 0; k < skuIds.length; k++) {
								if (skuIds[k].equals(jdskuNo)) {
									goodsSku.setValueThree(saleVale);
									break;
								}
							}
						}
						if (goodsSku.getValueThree() == null) {
							goodsSku.setValueThree("*");
						}
					} else if (i == 3) {
						saleName = similarSkulist.get(i).getSaleName();
						goodsSku.setKeyFour(saleName);
						saleAttrList = similarSkulist.get(i).getSaleAttrList();
						for (int j = 0; j < saleAttrList.size(); j++) {
							saleAttr = saleAttrList.get(j);
							saleVale = saleAttr.getSaleValue();
							skuIds = saleAttr.getSkuIds();
							for (int k = 0; k < skuIds.length; k++) {
								if (skuIds[k].equals(jdskuNo)) {
									goodsSku.setValueFour(saleVale);
									break;
								}
							}
						}
						if (goodsSku.getValueFour() == null) {
							goodsSku.setValueFour("*");
						}
					} else if (i == 4) {
						saleName = similarSkulist.get(i).getSaleName();
						goodsSku.setKeyFive(saleName);
						saleAttrList = similarSkulist.get(i).getSaleAttrList();
						for (int j = 0; j < saleAttrList.size(); j++) {
							saleAttr = saleAttrList.get(j);
							saleVale = saleAttr.getSaleValue();
							skuIds = saleAttr.getSkuIds();
							for (int k = 0; k < skuIds.length; k++) {
								if (skuIds[k].equals(jdskuNo)) {
									goodsSku.setValueFive(saleVale);
									break;
								}
							}
						}
						if (goodsSku.getValueFive() == null) {
							goodsSku.setValueFive("*");
						}
					}
				}
			}
			return goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
		}
	}
}
