package com.bh.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.service.ActZoneService;
import com.bh.config.Contants;
import com.bh.admin.mapper.goods.GoodsBrandMapper;
import com.bh.admin.mapper.goods.ActZoneMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicDauctionMapper;
import com.bh.admin.mapper.goods.TopicDauctionPriceMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.PromoteUserMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsBrand;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.ActZone;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicDauction;
import com.bh.admin.pojo.goods.TopicDauctionPrice;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.PromoteUser;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ActZoneImpl implements ActZoneService{
	@Autowired
	private ActZoneMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper skuMapper;
	@Autowired
	private GoodsBrandMapper brandMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private PromoteUserMapper promoteUserMapper;
	@Autowired
	private TopicDauctionPriceMapper dauctionPriceMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private TopicDauctionMapper dauctionMapper;
	/**
	 * 分类的删除
	 */
	@Override
	public String selectdelete(String id) throws Exception {
		int row = 0;
		String str = null;
		
		int count = mapper.delectCount(Integer.parseInt(id));
		if (count > 0){ //判断是有下级分类
			str = "请先删除子分类";
			return str;
		}
		/*Goods g = new Goods();
		g.setCatId(Long.parseLong(id));
		List<Goods> goodsList = goodsMapper.selectByCatIdAndNotDelete(g);
		if(goodsList.size()>0){
			str = "该分类已与商品绑定";
			return str;
		}
		
		String[] arr1 = id.split(",");
		StringBuffer buffer = new StringBuffer(); //判断是否与品牌所绑定
		List<GoodsBrand> brandList = brandMapper.selectAll(); 
		if(brandList.size()>0){
			for(GoodsBrand brand : brandList){
				buffer.append(brand.getCatId()+",");
			}
			String s2 = buffer.toString().substring(0,buffer.toString().length()-1);
	        String[] arr2 = s2.split(","); 
	        	
			for(int i=0; i<arr1.length; i++){
				for (int j = 0; j < arr2.length; j++) {
					if( Integer.parseInt(arr1[i]) == Integer.parseInt(arr2[j])){
						str = "该分类已被品牌所绑定";
						return str;
					}
				}
			}
		}*/
		mapper.deleteByZoneId(Integer.parseInt(id));
		row = mapper.deleteById(Integer.parseInt(id));
		if(row>0){
			str="删除成功";
		}else{
			str="删除失败";
		}
		return str;
	}

	
	/**
	 * 根据分类id查询分类
	 */
	@Override
	public ActZone selectById(String id) throws Exception {
		ActZone actZone = mapper.selectByPrimaryKey(Integer.parseInt(id));
		return actZone;
	}
	
	/**
	 * 添加专区名
	 */
	@Override
	public int selectParentInsert(String isNormalShow,String name, String reid,  String image , String isLast, String levelnum, String sortnum,String freePostage,String isCart,String isCoupon,String isRefund,String isLockScore) throws Exception {
		int row = 0;
		//List<ActZone> list = mapper.selectByName(name);
		ActZone actZone = mapper.insertselectByName(name, levelnum,Integer.parseInt(reid));
		String uuid=mapper.getUuid();
		Date date=new Date();
		/*List<ActZone> actZoneList=mapper.selectActZoneList();
		for (int i = 0; i < actZoneList.size(); i++) {
			int tagIds=actZoneList.get(i).getTagId();
			if(tagIds==Integer.parseInt(tagId)) {
				return row=999;
			}
		}*/
		if(actZone==null){
			ActZone actZone1 = new ActZone();
			if(!StringUtils.isEmptyOrWhitespaceOnly(isNormalShow)){
				actZone1.setIsNormalShow(Integer.parseInt(isNormalShow));
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
				actZone1.setImageUrl(image);
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(levelnum)){
				actZone1.setLevelNum(Integer.parseInt(levelnum));
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				actZone1.setSortNum(Integer.parseInt(sortnum));
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(freePostage)){
				actZone1.setFreePostage(Integer.parseInt(freePostage));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isCart)){
				actZone1.setIsCart(Integer.parseInt(isCart));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isCoupon)){
				actZone1.setIsCoupon(Integer.parseInt(isCoupon));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isRefund)){
				actZone1.setIsRefund(Integer.parseInt(isRefund));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isLockScore)){
				actZone1.setIsLockScore(Integer.parseInt(isLockScore));
			}
			actZone1.setActUuid(uuid);
			actZone1.setAddtime(date);
			actZone1.setEdittime(date);
			//ActZone actZone2 = mapper.selectByTagId(Integer.parseInt(reid));
			if(Integer.parseInt(levelnum)>1){
				actZone1.setIsLast(1);
			}else{
				actZone1.setIsLast(0);
				/*actZone2.setIsLast(1);
				mapper.updateByPrimaryKeySelective(actZone2);*/
			}
			actZone1.setName(name.replaceAll(",", "/").trim());
			actZone1.setReid(Integer.parseInt(reid));
			row = mapper.insertSelective(actZone1);
		}else{
			row = 1000;
		}
		
		return row;
	}
	
	
	/**
	 * 添加分类
	 */
	@Override
	public int selectTaxonomyInsert(String name, String reid,  String isLast, String levelnum, String sortnum) throws Exception {
		int row = 0;
		/*String[] names=name.split(",");
		String[] sortnums=sortnum.split(",");
		for (int i = 0; i < names.length; i++) {*/
		ActZone actZone = mapper.insertselectByName(name, levelnum,Integer.parseInt(reid));
		String uuid=mapper.getUuid();
		Date date=new Date();
		if(actZone==null){
			ActZone actZone1 = new ActZone();
			if(!StringUtils.isEmptyOrWhitespaceOnly(levelnum)){
				actZone1.setLevelNum(Integer.parseInt(levelnum));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				actZone1.setSortNum(Integer.parseInt(sortnum));
			}
			ActZone actZone2=mapper.selectByPrimaryKey(Long.parseLong(reid));
			actZone1.setFreePostage(actZone2.getFreePostage());
			actZone1.setIsNormalShow(actZone2.getIsNormalShow());
			actZone1.setIsCart(actZone2.getIsCart());
			actZone1.setIsCoupon(actZone2.getIsCoupon());
			actZone1.setIsRefund(actZone2.getIsRefund());
			actZone1.setIsLockScore(actZone2.getIsLockScore());
			actZone1.setActUuid(uuid);
			actZone1.setAddtime(date);
			actZone1.setEdittime(date);
			if(Integer.parseInt(levelnum)>1){
				actZone1.setIsLast(1);
			}else{
				actZone1.setIsLast(0);
			}
			actZone1.setName(name.replaceAll(",", "/").trim());
			actZone1.setReid(Integer.parseInt(reid));
			row = mapper.insertSelective(actZone1);
		 
		}else{
			row = 1000;
	  }
		return row;
	}
	
	//删除分类
		@Override
		public int selectTaxonomyDelete(Integer id) throws Exception {
			int row = 0;
			row=mapper.deleteByPrimaryKey(id);
			return row;
		}
	
	//修改分类
	@Override
	public int selectTaxonomyUpdate(Integer id,String name, String levelnum, String sortnum,Integer reid) throws Exception {
		int row = 0;
		ActZone actZone = mapper.insertselectByName1(name, levelnum,reid,Integer.parseInt(sortnum));
		Date date=new Date();
		if(actZone==null){
			ActZone actZone1 = new ActZone();
			actZone1.setId(id);
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				actZone1.setSortNum(Integer.parseInt(sortnum));
			}
			actZone1.setEdittime(date);
			actZone1.setName(name.replaceAll(",", "/").trim());
			row = mapper.updateByPrimaryKeySelective(actZone1);
		 
		}else{
			row = 1000;
		}
		return row;
	}
	

	@Override
	public PageBean<ActZone> selectByFirstReid(String name, String currentPage, String pageSize, String reId)
			throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = Integer.parseInt(pageSize);//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		List<ActZone> list = mapper.getListByFirstReid(name, pageStart, pageSizes, Integer.parseInt(reId));
		int total = mapper.countAll(name, Integer.parseInt(reId));//总条数
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<ActZone> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);
		return page;
	}

	/**
	 * 修改专区名
	 */
	public int updateCategory(String id, String name, String sortnum, String image,String freePostage,String isNormalShow,String isCart,String isCoupon,String isRefund,String isLockScore,String failuretime) throws Exception{
		int row = 0;
		ActZone actZone = mapper.selectById(Integer.parseInt(id));
		ActZone entity = new ActZone();
		Date date=new Date();
		entity.setName(name);
		entity.setEdittime(date);
		entity.setLevelNum(actZone.getLevelNum());
		//ActZone actZone1=mapper.selectByPrimaryKey(Long.parseLong(id));
		List<ActZone> list = mapper.selectUpdateByName(entity);
		
		if(list.size()==0){
			if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
				actZone.setImageUrl(image);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				actZone.setSortNum(Integer.parseInt(sortnum));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
				actZone.setName(name);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(freePostage)){
				actZone.setFreePostage(Integer.parseInt(freePostage));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isNormalShow)){
				actZone.setIsNormalShow(Integer.parseInt(isNormalShow));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isCart)){
				actZone.setIsCart(Integer.parseInt(isCart));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isCoupon)){
				actZone.setIsCoupon(Integer.parseInt(isCoupon));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isRefund)){
				actZone.setIsRefund(Integer.parseInt(isRefund));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(isLockScore)){
				actZone.setIsLockScore(Integer.parseInt(isLockScore));
			}
			//失效时间
			actZone.setFailuretime(failuretime);
			
			row = mapper.updateByPrimaryKeySelective(actZone);
			ActZone actZone1=mapper.selectByPrimaryKey(Long.parseLong(id));
			List<ActZone> actZoneList=mapper.selectByReids(Integer.parseInt(id));
			for (ActZone actZone2 : actZoneList) {
				actZone2.setFreePostage(actZone1.getFreePostage());
				actZone2.setIsNormalShow(actZone1.getIsNormalShow());
				actZone2.setIsCart(actZone1.getIsCart());
				actZone2.setIsCoupon(actZone1.getIsCoupon());
				actZone2.setIsRefund(actZone1.getIsRefund());
				actZone2.setIsLockScore(actZone1.getIsLockScore());
				actZone2.setFailuretime(actZone1.getFailuretime());//失效时间
				mapper.updateByPrimaryKey(actZone2);
			}
		}else{
			row = 1000;
		}
		return row;
	}

	@Override
	public List<ActZone> selectByParent(String reid) throws Exception{
		List<ActZone> list = mapper.selectByParent(Integer.parseInt(reid));
		return list;
	}
	
	/**
	 * PC首页导航分类
	 * @return
	 * @throws Exception
	 */
	//@Override
	/*public List<ActZone> homePageList() throws Exception {
		Integer catid = 0;
		List<ActZone> list = mapper.homeZeroList(catid);
		for(ActZone actZone : list){
			StringBuffer str = new StringBuffer();
			List<Goods> goodsList = goodsMapper.selectHostTopThree(actZone.getId());		
			for(Goods goods : goodsList){
				str.append(goods.getName());
				str.append(",");
			}
			String [] stringArr= str.toString().split(","); 
			actZone.setKeyword(stringArr);
			
			
			List<ActZone> listOne = mapper.homeOneList(actZone.getId()); //显示前4条
			
			for(ActZone oneCateGory : listOne){
				List<ActZone> listTwo = mapper.homeTwoList(oneCateGory.getId()); //显示前10条
				oneCateGory.setArry(listTwo);
			}
			actZone.setArry(listOne);
		}
		return list;
	}
*/
	/**
	 * 获取所有分类
	 */
	@Override
	public List<ActZone> selectAll() throws Exception {
		List<ActZone> list = mapper.selectAll();
		return list;
	}
	
	/**
	 * 分类排序
	 */
	@Override
	public int changeSortNum(String id, String sortnum) throws Exception {
		ActZone actZone = mapper.selectByPrimaryKey(Integer.parseInt(id));
		actZone.setSortNum(Integer.parseInt(sortnum));
		return mapper.updateByPrimaryKeySelective(actZone);
	}
	
	/**
	 * 获取三级分类
	 */
	@Override
	public List<ActZone> selectThreeLevel() throws Exception {
		List<ActZone> list = mapper.selectThreeLevel();
		if(list.size()>0){
			for(ActZone entity : list){
				ActZone parent = mapper.selectByPrimaryKey(entity.getReid());
				if(parent!=null){
					entity.setParentName(parent.getName());
					ActZone godParent = mapper.selectByPrimaryKey(parent.getReid());
					if(godParent!=null){
						entity.setGodParentName(godParent.getName());
					}
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 首页分类显示前6条商品
	 */
	public List<ActZone> selectTopSixGoodsBycatId() throws Exception {
		List<ActZone> list = mapper.selectTopSix();
		for(ActZone ActZone : list){
			
			StringBuffer buffer = new StringBuffer();
			List<String> catIdList = new ArrayList<>();
			List<ActZone> cateList = getAllCateList(ActZone.getId());
			for(ActZone category : cateList){
				buffer.append(category.getId()+",");
			}
			if(buffer.toString().length()>0){
				String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
				String[] str = catIdStr.split(",");
				for (int i = 0; i < str.length; i++) {
					catIdList.add(str[i]);
				}
			}
			
			List<Goods> goodsList = goodsMapper.selectIdByTopSix(catIdList);
			
			if(goodsList.size()<6){
				List<Goods> listSale = goodsMapper.selectIdByTopSale(catIdList);
				for(Goods sale : listSale){
					goodsList.add(sale);
				}
			}
			setGetter(goodsList);
			
			ActZone.setGoodsList(goodsList);
		}
		return list;
	}
	
	/**
	 * 获取最后一级分类
	 */
	@Override
	public List<ActZone> selectLastLevel() throws Exception {
		return mapper.selectLastLevel();
	}
	
	/**
	 * 商品添加时获取所有分类
	 */
	@Override
	public List<ActZone> selectAllByCatId(String catId) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = catId.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		List<ActZone> actZone = mapper.batchSelect(list);
		return actZone;
	}
	
	
	/**
	 * 价格的转化及sku与主表价格的赋值
	 * @param list
	 * @return
	 */
	public List<Goods> setGetter(List<Goods> list){
		List<GoodsSku> skuList = null;
		String url = null;
		for(int i=0; i<list.size(); i++){
			
			//int groupCount = 0; //团购商品销量统计
			//String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			//if(countT!=null){
			//	groupCount = Integer.parseInt(countT);
			//}
			//程凤云2018-3-29 下午14:48修改销量
			int groupCount = 0; //团购商品销量统计
			String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
			if(countT!=null){
				groupCount = Integer.parseInt(countT)+list.get(i).getFixedSale();
			}else{
				groupCount = list.get(i).getFixedSale();
			}
			list.get(i).setGroupCount(groupCount);
			
			
			if(list.get(i).getTopicType()==6){ //判断是否拍卖商品
				Map<String, Object> map = new HashMap<>();
				if(list.get(i).getTopicGoodsId()!=null){
					List<TopicDauctionPrice> dauctionList = dauctionPriceMapper.selectByTgId(list.get(i).getTopicGoodsId());
					if(dauctionList.size()>0){
						TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(dauctionList.get(0).getTgId());
						if(topicGoods!=null){
							Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
							map.put("startTime", topic.getStartTime());
							map.put("endTime", topic.getEndTime());
							
							List<TopicDauction> dauction = dauctionMapper.selectByTgId(topicGoods.getId());
							if(dauction.size()>0){
								map.put("dauctionPrice", (double)dauction.get(0).getDauctionPrice()/100);
								map.put("lowPrice", (double)dauction.get(0).getLowPrice()/100);
								map.put("scopePrice", (double)dauction.get(0).getScopePrice()/100);
								map.put("timeSection", dauction.get(0).getTimeSection());
							}else{
								map.put("dauctionPrice", null);
								map.put("lowPrice", null);
								map.put("scopePrice", null);
								map.put("timeSection", null);
							}
						}else{
							map.put("startTime", null);
							map.put("endTime", null);
						}
					}
				}
				list.get(i).setDauctionDetail(map);
			}
			
			StringBuffer buffer = new StringBuffer(); //设置前三团购用户头像
			List<OrderTeam> orderTeamList = orderTeamMapper.getMheadList(list.get(i).getId());
			if(orderTeamList.size()>0){
				for(OrderTeam entity : orderTeamList){
					if(entity.getType()==0){
						Member member = memberMapper.selectByPrimaryKey(entity.getmId());
						if(member!=null){
							if(member.getHeadimgurl()!=null){
								buffer.append(member.getHeadimgurl()+",");
							}else{
								buffer.append(Contants.headImage+",");
							}
						}
					}else{
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(entity.getmId());
						if(promoteUser!=null){
							buffer.append(promoteUser.getHeadImg()+",");
						}
					}
				}
			}
			if(buffer.toString().length()>0){
				String headStr = buffer.toString().substring(0, buffer.toString().length()-1);
				String[] imageStr = headStr.split(",");
				list.get(i).setUserGroupHead(imageStr);
			}
			
			skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
			if(skuList.size()>0){
				for(int j=0; j<skuList.size(); j++){
					double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);
					
					double marketRealPrice = (double)skuList.get(0).getMarketPrice()/100; //价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					
					double realTeamPrice = (double)skuList.get(0).getTeamPrice()/100; //团购价“分”转化成“元”
					list.get(i).setRealTeamPrice(realTeamPrice);
					
					if(skuList.get(0).getAuctionPrice()!=null){
						double realAuctionPrice = (double)skuList.get(0).getAuctionPrice()/100;//拍卖价格“分”转化成“元”
						list.get(i).setRealAuctionPrice(realAuctionPrice);
					}
					
					org.json.JSONObject jsonObj = new org.json.JSONObject(skuList.get(0).getValue());
					org.json.JSONArray personList = jsonObj.getJSONArray("url");
					for(int m = 0; m < personList.length(); m++){
						url = (String) personList.get(0);
					}
				}
				//cheng设置分类的商品图片
				try {
					String image = "";
					image = RegExpValidatorUtils.returnNewString(skuList.get(0).getValue());
					list.get(i).setImage(image);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double)list.get(i).getMarketPrice()/100; //价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
				double realTeamPrice = (double)list.get(i).getTeamPrice()/100; //团购价“分”转化成“元”
				list.get(i).setRealTeamPrice(realTeamPrice);
			}
			
			
			
		}
		return list;
	}
	
	/**
	 * 移动端根据父类id获取下级分类及商品数量
	 */
	@Override
	public List<ActZone> selectCategoryAndNumByParent(String reid) throws Exception {
		int count = 0;
		
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<ActZone> cateList = getAllCateList(Integer.parseInt(reid));
		for(ActZone ActZone : cateList){
			buffer.append(ActZone.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		
		List<ActZone> list = mapper.selectAllByParent(Integer.parseInt(reid));
		if(list.size()>0){
			for(ActZone actZone: list){
				
				StringBuffer bf = new StringBuffer();
				List<String> idslist = new ArrayList<>();
				List<ActZone> goryList = getAllCateList(actZone.getId());
				for(ActZone actZone1 : goryList){
					bf.append(actZone1.getId()+",");
				}
				if(bf.toString().length()>0){
					String catIdStr = bf.toString().substring(0,bf.toString().length()-1);
					String[] str = catIdStr.split(",");
					for (int i = 0; i < str.length; i++) {
						idslist.add(str[i]);
					}
				}
				
				count = goodsMapper.countApiCategoryGoodsList(idslist, Contants.NORMAL_STATUS);
				actZone.setCount(count);
			}
		}
		int total = goodsMapper.countApiCategoryGoodsList(catIdList, Contants.NORMAL_STATUS);
		ActZone actZone = new ActZone();
		actZone.setName("全部");
		actZone.setCount(total);
		list.add(0, actZone);
		return list;
	}
	
	/**
	 * 获取所有分类列表
	 */
	@Override
	public List<ActZone> selectAllList() throws Exception {
		List<ActZone> actZoneList = new ArrayList<>();
		ActZone actZone = new ActZone();
		actZone.setName("一级分类");
		actZone.setId(Integer.parseInt("0"));
		//actZone.setIsLast(0);
		actZone.setReid(Integer.parseInt("-1"));
		actZoneList.add(actZone);
		List<ActZone> list = mapper.selectAllByName();
		for(ActZone actZone1 : list ){
			if(actZone1.getReid()>0){
				ActZone parentActZone = mapper.selectByPrimaryKey(actZone.getReid());
				actZone1.setParentName(parentActZone.getName());
			}
			actZoneList.add(actZone1);
		}
		return actZoneList;
	}
	
	
	/**
	 * 获取所有分类列表(树形结构)
	 */
	@Override
	public List<ActZone> selectLinkedList() throws Exception {
		Integer levelNum = 1;
		ActZone entity = new ActZone();
		entity.setLevelNum(levelNum);
		List<ActZone> list = mapper.getFirstLevelList(); //返回第一层分类
		if(list.size()>0){
			list = nextList(list);
		}
		return list;
	}
	
	/**
	 * 获取所有分类列表(树形结构)
	 */
	@Override
	public PageBean<ActZone> selectLinkedPage(String currentPage, int pageSize, String name) throws Exception {
		PageParams<ActZone> params = new PageParams<ActZone>();
		PageBean<ActZone> pageBean = new PageBean<ActZone>();
		params.setCurPage(Integer.parseInt(currentPage));
		params.setPageSize(Contants.PAGE_SIZE);
		
		Integer levelNum = 1;
		List<ActZone> list = null;
		ActZone entity = new ActZone();
		entity.setLevelNum(levelNum);
		entity.setName(name);
		list = mapper.getByLevelNum(entity);
		nextList(list);
		params.setResult(list);
		PageBean<ActZone> retBean = pageBean.getPageResult(params);
		return retBean;
	}
	/*public PageBean<ActZone> selectLinkedPage(String currentPage, int pageSize, String name) throws Exception {
		Short series = 1;
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		ActZone entity = new ActZone();
		entity.setSeries(series);
		entity.setName(name);
		List<ActZone> list = mapper.getBySeries(entity); //返回第一层分类
		if(list!=null && list.size()>0){
			list = nextList(list);
		}
		PageBean<ActZone> pageBean = new PageBean<>(list);
		return pageBean;
	}*/
	
	public List<ActZone> nextList(List<ActZone> list){
		for(ActZone actZone : list){
			if(actZone.getReid()>0){
				ActZone parentActZone = mapper.selectById(actZone.getReid());
				actZone.setParentName(parentActZone.getName());
			}
			if(actZone.getIsLast()==0){
				List<ActZone> childList = mapper.selectAllByReid(actZone.getId()); //根据父类id查询下一级列表
				actZone.setChildList(childList);
				if(childList.size()>0){
					nextList(childList);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据分类id递归查询所有子分类
	 */
	@Override
	public List<ActZone> getAllCateList(long reid){
		ActZone myGc = mapper.selectByPrimaryKey(reid);
		List<ActZone> catesList = getCatesByReid(reid);
		List<ActZone> retList = new ArrayList<>();
		getCateTreeList(catesList,retList);
		retList.add(0, myGc);
		return retList;
	}

	private List<ActZone> getCateTreeList(List<ActZone> cateList,List<ActZone> retList){
		List<ActZone> subMenuList = new ArrayList<ActZone>();
		for(ActZone entity : cateList){
		  getCateTreeList(getCatesByReid(entity.getId()),retList);
		  retList.add(entity);  
		  subMenuList.add(entity);
		}
		return subMenuList;
	}
	private List<ActZone> getCatesByReid(long reid){
		return mapper.selectAllByReid(reid);
	}
	
	/**
	 * 移动端获取所有一级菜单
	 */
	@Override
	public List<ActZone> getFirstLevelList() {
		Integer catId = 0;
		List<ActZone> list = mapper.getFirstLevelList();
		ActZone entity = new ActZone();
		entity.setId(catId);
		entity.setName("全部");
		list.add(0, entity);
		if(list.size()>0){
			for(ActZone actZone : list){ //"手机/电脑/运营商"--去斜杠取第一个值"手机"
				String temp[] = actZone.getName().replaceAll("\\\\","/").split("/");
				actZone.setName(temp[0]);
			}
		}
		return list;
	}
	
	/**
	 * 移动端分类模块根据分类获取下级分类及商品
	 * @param pageSize
	 * @param currentPage
	 * @param catId
	 * @return
	 */
	@Override
	public Map<String, Object> getCateListAndGoods(int pageSize, String currentPage, String catId) {
		Map<String, Object> map = new LinkedHashMap<>();
		if(Integer.parseInt(catId)==0){
			Goods entity = new Goods();
			List<ActZone> catList = new ArrayList<>();
			PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
			List<Goods> goodsList = goodsMapper.apiGoodsList(entity);
			if(goodsList.size()>0){
				setGetter(goodsList);
			}
			PageBean<Goods> bean = new PageBean<>(goodsList);
			map.put("cateGoryList", catList);
			map.put("goodsList", bean);
		}else{
			List<ActZone> actZoneList = mapper.getNextByReid(Integer.parseInt(catId));
			
			StringBuffer buffer = new StringBuffer();
			List<String> catIdList = new ArrayList<>();
			List<ActZone> cateList = getAllCateList(Integer.parseInt(catId));
			if(cateList.size()>0){
				for(ActZone actZone : cateList){
					buffer.append(actZone.getId()+",");
				}
			}
			if(buffer.toString().length()>0){
				String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
				String[] str = catIdStr.split(",");
				for (int i = 0; i < str.length; i++) {
					catIdList.add(str[i]);
				}
			}
			
			PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
			List<Goods> list = goodsMapper.selectNextAll(catIdList);
			if(list.size()>0){
				setGetter(list);
			}
			PageBean<Goods> pageBean = new PageBean<>(list);
			
			map.put("actZoneList", actZoneList);
			map.put("goodsList", pageBean);
		}
		return map;
	}
	
	/**
	 * 京东商品分类摘取
	 */
/*	@Transactional
	@Override
	public int insertJdCategory(JSONArray list) throws Exception {
		int row = 0;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			ActZone entity = mapper.selectByPrimaryKey(ob.getLong("id"));
			if(entity==null){
				//ActZone goodsCatgory = mapper.selectByNameAndlevelNum(ob.getString("name"), Short.parseShort(ob.getString("series")), ob.getLong("reid"), 1);
				ActZone actZone = new ActZone();
				actZone.setId(ob.getLong("id"));
				actZone.setName(ob.getString("name"));
				actZone.setReid(ob.getLong("reid"));
				actZone.setIsLast(ob.getBoolean("isLast"));
				actZone.setSeries(Short.parseShort(ob.getString("series")));
				actZone.setFlag(false);
				actZone.setIsJd(1);
				row = mapper.insertSelective(actZone);
			}
		}
		return row;
	}*/

	@Override
	public List<GoodsBrand> getBrandByCategory(Long catId) {
		// TODO Auto-generated method stub
		return brandMapper.getByCatid(catId);
	}

	@Override
	public List<ActZone> getAllCategory() {
		// TODO Auto-generated method stub
		return mapper.selectAllCategory();
	}

	public List<ActZone> getByLevel(ActZone entity) {
		List<ActZone> list = mapper.getByLevel(entity);
		return list;
	}
	
	public List<ActZone> getByReid(ActZone entity) {
		List<ActZone> list = mapper.getByReid(entity);
		return list;
	}


}
