package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsBrandMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsModelMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.ItemModelMapper;
import com.bh.goods.mapper.TopicDauctionMapper;
import com.bh.goods.mapper.TopicDauctionPriceMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsModel;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MyGoods;
import com.bh.goods.pojo.MyGoodsCategoryPojo;
import com.bh.goods.pojo.MyGoodsPojo;
import com.bh.goods.pojo.MyPingtaiGoodsPojo;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicDauction;
import com.bh.goods.pojo.TopicDauctionPrice;
import com.bh.goods.pojo.TopicGoods;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.product.api.service.GoodsCategoryService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.PromoteUserMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class GoodsCategoryImpl implements GoodsCategoryService{
	@Autowired
	private GoodsCategoryMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper skuMapper;
	@Autowired
	private GoodsBrandMapper brandMapper;
	@Autowired
	private GoodsModelMapper modelMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private PromoteUserMapper promoteUserMapper;
	@Autowired
	private ItemModelMapper itemModelMapper;
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
		
		int count = mapper.delectCount(Long.parseLong(id));
		if (count > 0){ //判断是有下级分类
			str = "请先删除子分类";
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
					if( Long.parseLong(arr1[i]) == Long.parseLong(arr2[j])){
						str = "该分类已被品牌所绑定";
						return str;
					}
				}
			}
		}
		row = mapper.deleteByPrimaryKey(Long.parseLong(id));
		if(row>0){
			str="删除成功";
		}else{
			str="删除失败";
		}
		return str;
	}

	@Override
	public int selectupdate(GoodsCategory categorymap) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 根据分类id查询分类
	 */
	@Override
	public GoodsCategory selectById(String id) throws Exception {
		GoodsCategory category = mapper.selectByPrimaryKey(Long.parseLong(id));
		return category;
	}
	
	/**
	 * 添加分类 
	 */
	@Override
	public int selectParentInsert(String name, String reid, String sortnum, String image, String flag, String isLast, String series) throws Exception {
		int row = 0;
		//List<GoodsCategory> list = mapper.selectByName(name);
		GoodsCategory goodsCatgory = mapper.insertselectByName(name, Short.parseShort(series),0);
		if(goodsCatgory==null){
			GoodsCategory category = new GoodsCategory();
			category.setId(Long.parseLong(MixCodeUtil.createOutTradeNo()));
			if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
				category.setImage(image);
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(series)){
				category.setSeries(Short.parseShort(series));
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				category.setSortnum(Short.parseShort(sortnum));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(flag)){
				if(Integer.parseInt(flag)==1){
					category.setFlag(true);
				}else{
					category.setFlag(false);
				}
			}
			
			GoodsCategory goodsCateGory = mapper.selectByPrimaryKey(Long.parseLong(reid));
			if(Short.parseShort(series)==1){
				category.setIsLast(true);
			}else{
				category.setIsLast(true);
				goodsCateGory.setIsLast(false);
				mapper.updateByPrimaryKeySelective(goodsCateGory);
			}
			category.setName(name.replaceAll(",", "/").trim());
			category.setReid(Long.parseLong(reid));
			row = mapper.insertSelective(category);
		}else{
			row = 1000;
		}
		
		return row;
	}

	@Override
	public PageBean<GoodsCategory> selectByFirstReid(String name, String currentPage, String pageSize, String reId)
			throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = Integer.parseInt(pageSize);//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		List<GoodsCategory> list = mapper.getListByFirstReid(name, pageStart, pageSizes, Long.parseLong(reId));
		int total = mapper.countAll(name, Long.parseLong(reId));//总条数
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<GoodsCategory> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);
		return page;
	}

	/**
	 * 分类修改
	 */
	public int updateCategory(String id, String name, String sortnum, String image, String flag) throws Exception{
		int row = 0;
		GoodsCategory category = mapper.selectByPrimaryKey(Long.parseLong(id));
		GoodsCategory entity = new GoodsCategory();
		entity.setName(name);
		entity.setId(Long.parseLong(id));
		entity.setSeries(category.getSeries());
		List<GoodsCategory> list = mapper.selectUpdateByName(entity);
		if(list.size()==0){
			if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
				category.setImage(image);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				category.setSortnum(Short.parseShort(sortnum));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
				category.setName(name);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(flag)){
				if(Integer.parseInt(flag)==1){
					category.setFlag(true);
				}else{
					category.setFlag(false);
				}
			}
			
			row = mapper.updateByPrimaryKeySelective(category);
		}else{
			row = 1000;
		}
		return row;
	}

	@Override
	public List<GoodsCategory> selectByParent(String reid) throws Exception{
		List<GoodsCategory> list = mapper.selectByParent(Long.parseLong(reid));
		return list;
	}
	
	/**
	 * PC首页导航分类
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<GoodsCategory> homePageList() throws Exception {
		long catid = 0;
		List<GoodsCategory> list = mapper.homeZeroList(catid);
		for(GoodsCategory category : list){
			StringBuffer str = new StringBuffer();
			List<Goods> goodsList = goodsMapper.selectHostTopThree(category.getId());		
			for(Goods goods : goodsList){
				str.append(goods.getName());
				str.append(",");
			}
			String [] stringArr= str.toString().split(","); 
			category.setKeyword(stringArr);
			
			
			List<GoodsCategory> listOne = mapper.homeOneList(category.getId()); //显示前4条
			
			for(GoodsCategory oneCateGory : listOne){
				List<GoodsCategory> listTwo = mapper.homeTwoList(oneCateGory.getId()); //显示前10条
				oneCateGory.setArry(listTwo);
			}
			category.setArry(listOne);
		}
		return list;
	}

	/**
	 * 获取所有分类
	 */
	@Override
	public List<GoodsCategory> selectAll() throws Exception {
		List<GoodsCategory> list = mapper.selectAll();
		return list;
	}
	
	/**
	 * 分类排序
	 */
	@Override
	public int changeSortNum(String id, String sortnum) throws Exception {
		GoodsCategory goodsCategory = mapper.selectByPrimaryKey(Long.parseLong(id));
		goodsCategory.setSortnum(Short.parseShort(sortnum));
		return mapper.updateByPrimaryKeySelective(goodsCategory);
	}
	
	/**
	 * 获取三级分类
	 */
	@Override
	public List<GoodsCategory> selectThreeLevel() throws Exception {
		List<GoodsCategory> list = mapper.selectThreeLevel();
		if(list.size()>0){
			for(GoodsCategory entity : list){
				GoodsCategory parent = mapper.selectByPrimaryKey(entity.getReid());
				if(parent!=null){
					entity.setParentName(parent.getName());
					GoodsCategory godParent = mapper.selectByPrimaryKey(parent.getReid());
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
	public List<MyGoodsCategoryPojo> selectTopSixGoodsBycatId() throws Exception {
		List<MyGoodsCategoryPojo> list = mapper.selectTopSix();
		if (list.size()>0) {
			for(MyGoodsCategoryPojo goodsCategory : list){
				List<MyPingtaiGoodsPojo> goodsList = goodsMapper.selectIdByTopSix(goodsCategory.getId().toString());
				goodsCategory.setGoodsList(goodsList);
			}
		}
		return list;
	}
	
	/**
	 * 获取最后一级分类
	 */
	@Override
	public List<GoodsCategory> selectLastLevel() throws Exception {
		return mapper.selectLastLevel();
	}
	
	/**
	 * 商品添加时获取所有分类
	 */
	@Override
	public List<GoodsCategory> selectAllByCatId(String catId) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = catId.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		List<GoodsCategory> categoryList = mapper.batchSelect(list);
		return categoryList;
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
	public List<GoodsCategory> selectCategoryAndNumByParent(String reid) throws Exception {
		int count = 0;
		
		StringBuffer buffer = new StringBuffer();
		List<String> catIdList = new ArrayList<>();
		List<GoodsCategory> cateList = getAllCateList(Integer.parseInt(reid));
		for(GoodsCategory goodsCategory : cateList){
			buffer.append(goodsCategory.getId()+",");
		}
		if(buffer.toString().length()>0){
			String catIdStr = buffer.toString().substring(0,buffer.toString().length()-1);
			String[] str = catIdStr.split(",");
			for (int i = 0; i < str.length; i++) {
				catIdList.add(str[i]);
			}
		}
		
		List<GoodsCategory> list = mapper.selectAllByParent(Long.parseLong(reid));
		if(list.size()>0){
			for(GoodsCategory category: list){
				
				StringBuffer bf = new StringBuffer();
				List<String> idslist = new ArrayList<>();
				List<GoodsCategory> goryList = getAllCateList(category.getId());
				for(GoodsCategory goodsCategory : goryList){
					bf.append(goodsCategory.getId()+",");
				}
				if(bf.toString().length()>0){
					String catIdStr = bf.toString().substring(0,bf.toString().length()-1);
					String[] str = catIdStr.split(",");
					for (int i = 0; i < str.length; i++) {
						idslist.add(str[i]);
					}
				}
				
				count = goodsMapper.countApiCategoryGoodsList(idslist, Contants.NORMAL_STATUS);
				category.setCount(count);
			}
		}
		int total = goodsMapper.countApiCategoryGoodsList(catIdList, Contants.NORMAL_STATUS);
		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setName("全部");
		goodsCategory.setCount(total);
		list.add(0, goodsCategory);
		return list;
	}
	
	/**
	 * 获取所有分类列表
	 */
	@Override
	public List<GoodsCategory> selectAllList() throws Exception {
		List<GoodsCategory> cateGoryList = new ArrayList<>();
		GoodsCategory goodsCateGory = new GoodsCategory();
		goodsCateGory.setName("一级分类");
		goodsCateGory.setId(Long.parseLong("0"));
		goodsCateGory.setIsLast(false);
		goodsCateGory.setReid(Long.parseLong("-1"));
		cateGoryList.add(goodsCateGory);
		List<GoodsCategory> list = mapper.selectAllByName();
		for(GoodsCategory categroy : list ){
			if(categroy.getReid()>0){
				GoodsCategory parentCategory = mapper.selectByPrimaryKey(categroy.getReid());
				categroy.setParentName(parentCategory.getName());
			}
			cateGoryList.add(categroy);
		}
		return cateGoryList;
	}
	
	
	
	/**
	 * 获取所有分类列表(树形结构)
	 */
	@Override
	public PageBean<GoodsCategory> selectLinkedPage(String currentPage, int pageSize, String name) throws Exception {
		PageParams<GoodsCategory> params = new PageParams<GoodsCategory>();
		PageBean<GoodsCategory> pageBean = new PageBean<GoodsCategory>();
		params.setCurPage(Integer.parseInt(currentPage));
		params.setPageSize(Contants.PAGE_SIZE);
		
		Short series = 1;
		List<GoodsCategory> list = null;
		GoodsCategory entity = new GoodsCategory();
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			entity.setName(name);
			entity.setSeries(series);
			list = mapper.getBySeries(entity);
			if(list.size()==0){
				GoodsCategory entityTwo = new GoodsCategory();
				entityTwo.setName(name);
				series = 2;
				entityTwo.setSeries(series);
				list = mapper.getBySeries(entityTwo);
				if(list.size()==0){
					GoodsCategory entityThree = new GoodsCategory();
					entityThree.setName(name);
					series = 3;
					entityThree.setSeries(series);
					list = mapper.getBySeries(entityThree);
				}else{
					list = nextList(list);
				}
			}else{
				list = nextList(list);
			}
		}else{
			entity.setSeries(series);
			list = mapper.getBySeries(entity); //返回第一层分类
			if(list!=null && list.size()>0){
				list = nextList(list);
			}
		}
		params.setResult(list);
		PageBean<GoodsCategory> retBean = pageBean.getPageResult(params);
		return retBean;
	}
	/*public PageBean<GoodsCategory> selectLinkedPage(String currentPage, int pageSize, String name) throws Exception {
		Short series = 1;
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		GoodsCategory entity = new GoodsCategory();
		entity.setSeries(series);
		entity.setName(name);
		List<GoodsCategory> list = mapper.getBySeries(entity); //返回第一层分类
		if(list!=null && list.size()>0){
			list = nextList(list);
		}
		PageBean<GoodsCategory> pageBean = new PageBean<>(list);
		return pageBean;
	}*/
	
	
	
	/**
	 * 根据分类id递归查询所有子分类
	 */
	@Override
	public List<GoodsCategory> getAllCateList(long reid){
		GoodsCategory myGc = mapper.selectByPrimaryKey(reid);
		List<GoodsCategory> catesList = getCatesByReid(reid);
		List<GoodsCategory> retList = new ArrayList<>();
		getCateTreeList(catesList,retList);
		retList.add(0, myGc);
		return retList;
	}

	private List<GoodsCategory> getCateTreeList(List<GoodsCategory> cateList,List<GoodsCategory> retList){
		List<GoodsCategory> subMenuList = new ArrayList<GoodsCategory>();
		for(GoodsCategory entity : cateList){
		  getCateTreeList(getCatesByReid(entity.getId()),retList);
		  retList.add(entity);  
		  subMenuList.add(entity);
		}
		return subMenuList;
	}
	private List<GoodsCategory> getCatesByReid(long reid){
		return mapper.selectAllByReid(reid);
	}
	
	/**
	 * 移动端获取所有一级菜单
	 */
	@Override
	public List<GoodsCategory> getFirstLevelList() {
		long catId = 0;
		List<GoodsCategory> list = mapper.getFirstLevelList();
		GoodsCategory entity = new GoodsCategory();
		entity.setId(catId);
		entity.setName("全部");
		list.add(0, entity);
		if(list.size()>0){
			for(GoodsCategory category : list){ //"手机/电脑/运营商"--去斜杠取第一个值"手机"
				String temp[] = category.getName().replaceAll("\\\\","/").split("/");
				category.setName(temp[0]);
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
		if(Long.parseLong(catId)==0){
			Goods entity = new Goods();
			List<GoodsCategory> catList = new ArrayList<>();
			PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
			List<Goods> goodsList = goodsMapper.apiGoodsList(entity);
			if(goodsList.size()>0){
				setGetter(goodsList);
			}
			PageBean<Goods> bean = new PageBean<>(goodsList);
			map.put("cateGoryList", catList);
			map.put("goodsList", bean);
		}else{
			List<GoodsCategory> categoryList = mapper.getNextByReid(Long.parseLong(catId));
			
			StringBuffer buffer = new StringBuffer();
			List<String> catIdList = new ArrayList<>();
			List<GoodsCategory> cateList = getAllCateList(Long.parseLong(catId));
			if(cateList.size()>0){
				for(GoodsCategory category : cateList){
					buffer.append(category.getId()+",");
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
			
			map.put("cateGoryList", categoryList);
			map.put("goodsList", pageBean);
		}
		return map;
	}
	
	/**
	 * 2018.5.30  zlk 移动端分类模块根据分类获取下级分类及商品
	 * @param pageSize
	 * @param currentPage
	 * @param catId
	 * @return
	 */
	@Override
	public Map<String, Object> getCateListAndGood(int pageSize, String currentPage, String catId) {
		Map<String, Object> map = new LinkedHashMap<>();
		if(Long.parseLong(catId)==0){
			Goods entity = new Goods();
			List<GoodsCategory> catList = new ArrayList<>();
			entity.setPageSize(pageSize);//每页的数量
			entity.setCurrentPageIndex((Integer.valueOf(currentPage)-1)*pageSize);
			List<MyGoodsPojo> goodsList = goodsMapper.apiGoodsListByLimit1(entity);	
			if(goodsList.size()>0) {
			   for(int i=0;i<goodsList.size();i++) {
				   goodsList.get(i).setImage(RegExpValidatorUtils.returnNewString(goodsList.get(i).getValue()));
			   }
			}
			Map<String, Object> returnMap = new HashMap<String, Object>();//返回的Map
			returnMap.put("list", goodsList);//当前数据
			map.put("cateGoryList", catList);
			map.put("goodsList", returnMap);
		}else{
			List<GoodsCategory> categoryList = mapper.getNextByReid(Long.parseLong(catId));
			
			StringBuffer buffer = new StringBuffer();
			List<String> catIdList = new ArrayList<>();
			List<GoodsCategory> cateList = getAllCateList(Long.parseLong(catId));
			if(cateList.size()>0){
				for(GoodsCategory category : cateList){
					buffer.append(category.getId()+",");
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
			List<MyGoods> list = goodsMapper.selectNextAlls(catIdList);
			
			if(list.size()>0){
				for(int i=0;i<list.size();i++) {
				 
//				  int groupCount = 0; //团购商品销量统计
//				  
//				  if(list.get(i).getSaleType()==1) {
//					  groupCount=list.get(i).getSale()+list.get(i).getFixedSale();
//				  }else {
//					  String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getId());
//					  if(countT!=null){
//						  groupCount = Integer.parseInt(countT)+list.get(i).getFixedSale();
//					  }else{
//						  groupCount = list.get(i).getFixedSale();
//					  }
//				  }
				 
//				  list.get(i).setGroupCount(groupCount);
			    
			      list.get(i).setImage(RegExpValidatorUtils.returnNewString(list.get(i).getValue()));
			      
//			      double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
//			      list.get(i).setRealPrice(realPrice);
			    
//				  List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(list.get(i).getId());
//				  if(skuList.size()>0) {
//				     list.get(i).setImage(RegExpValidatorUtils.returnNewString(skuList.get(0).getValue()));
//				     double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
//				     list.get(i).setRealPrice(realPrice);
//				  }else {
//					 double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
//					 list.get(i).setRealPrice(realPrice);
//				  }
				}
			}
			PageBean<MyGoods> pageBean = new PageBean<>(list);
			
			map.put("cateGoryList", categoryList);
			map.put("goodsList", pageBean);
		}
		return map;
	}
	
	
	public List<GoodsCategory> nextList(List<GoodsCategory> list) {
		for (GoodsCategory goodsCategory : list) {
			if (goodsCategory.getReid() > 0) {
				GoodsCategory parentCategory = mapper.selectByPrimaryKey(goodsCategory.getReid());
				goodsCategory.setParentName(parentCategory.getName());
			}
			if (goodsCategory.getIsLast() == false) {
				List<GoodsCategory> childList = mapper.selectAllByReid(goodsCategory.getId()); // 根据父类id查询下一级列表
				goodsCategory.setChildList(childList);
				if (childList.size() > 0) {
					nextList(childList);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 京东商品分类摘取
	 */
	@Transactional
	@Override
	public int insertJdCategory(JSONArray list) throws Exception {
		int row = 0;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			GoodsCategory entity = mapper.selectByPrimaryKey(ob.getLong("id"));
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
				row = mapper.insertSelective(category);
			}
		}
		return row;
	}

	@Override
	public List<GoodsBrand> getBrandByCategory(Long catId) {
		// TODO Auto-generated method stub
		return brandMapper.getByCatid(catId);
	}

	@Override
	public List<GoodsCategory> getAllCategory() {
		// TODO Auto-generated method stub
		return mapper.selectAllCategory();
	}

	@Override
	public List<GoodsCategory> getByLevel(GoodsCategory entity) {
		List<GoodsCategory> list = mapper.getByLevel(entity);
		return list;
	}
	
	@Override
	public List<GoodsCategory> getByReid(GoodsCategory entity) {
		List<GoodsCategory> list = mapper.getByReid(entity);
		return list;
	}
	/**
	 * 
	 * @Description: 获取3级分类下的所有商品
	 * @author xieyc
	 * @date 2018年8月10日 上午11:20:20
	 */
	public PageBean<Goods> getGoodsByCatId(String currentPage, String catId) {
		PageHelper.startPage(Integer.parseInt(currentPage), 10, true);
		List<Goods> list = goodsMapper.getGoodsByCatId(Long.parseLong(catId));
		if(list.size()>0){
			setGetter(list);
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	/**
	 * @Description: 获取所有某个catone下的所有级
	 * @author xieyc
	 * @date 2018年8月10日 下午5:01:53 
	 * @param   
	 * @return  
	 */
	public List<GoodsCategory> selectLinkedByCatIdOne(String catIdOne) {
		List<GoodsCategory> list = mapper.selectAllByReid(Long.parseLong(catIdOne)); // 根据父类id查询下一级列表
		if(list.size()>0){
			selectNextList(list);
		}
		return list;
	}
	public List<GoodsCategory> selectNextList(List<GoodsCategory> list) {
		for (GoodsCategory goodsCategory : list) {
			if (goodsCategory.getIsLast() == false) {
				List<GoodsCategory> childList = mapper.selectAllByReid(goodsCategory.getId()); //根据父类id查询下一级列表
				goodsCategory.setChildList(childList);
				if (childList.size() > 0) {
					selectNextList(childList);
				}
			}
		}
		return list;
	}
	/**
	 * 获取所有一级分类列表(树形结构)
	 */
	@Override
	public List<GoodsCategory> selectLinkedList() throws Exception {
		List<GoodsCategory> list = mapper.getFirstLevelList(); //返回第一层分类
		/*List<GoodsCategory> listTwo = mapper.selectAllByReid(list.get(0).getId()); // 根据父类id查询下一级列表
		list.get(0).setChildList(listTwo);
		for (GoodsCategory goodsCategory : listTwo) {
			List<GoodsCategory> listThree = mapper.selectAllByReid(goodsCategory.getId()); //根据父类id查询下一级列表
			goodsCategory.setChildList(listThree);
		}*/
		return list;
	}
}
