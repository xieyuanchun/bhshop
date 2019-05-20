package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsShopCategoryMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.product.api.service.GoodsShopCategoryService;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class GoodsShopCategoryImpl implements GoodsShopCategoryService {
	@Autowired
	private GoodsShopCategoryMapper mapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	/**
	 * 新增分类
	 */
	@Override
	public int selectParentInsert(String name, String reid, String sortnum, String image, int shopId, String series, String isLast) throws Exception {
		int row = 0;
		List<GoodsShopCategory> list = mapper.selectByName(name);
		if(list.size()==0){
			GoodsShopCategory shopCategory = new GoodsShopCategory();
			if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
				shopCategory.setImage(image);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				shopCategory.setSortnum(Short.parseShort(sortnum));
			}
			shopCategory.setName(name);
			shopCategory.setReid(Integer.parseInt(reid));
			shopCategory.setSeries(Short.parseShort(series));
			
			GoodsShopCategory goodsShopCateGory = mapper.selectByPrimaryKey(Integer.parseInt(reid));
			if(Short.parseShort(series)==1){
				shopCategory.setIsLast(true);
			}else{
				shopCategory.setIsLast(true);
				goodsShopCateGory.setIsLast(false);
				mapper.updateByPrimaryKeySelective(goodsShopCateGory);
			}
			
			shopCategory.setShopId(shopId);
			row =  mapper.insertSelective(shopCategory);
		}else{
			row = 1000;
		}
		return row;	
	}
	
	/**
	 * 删除分类
	 */
	@Override
	public int selectdelete(String id) throws Exception {
		int row = 0;
		int count = mapper.delectCount(Integer.parseInt(id));//判断是否有下级分类
		if(count>0){
			row = 999;
			return row;
		}
		
		List<Goods> goodsList = goodsMapper.getListByShopCatId(Integer.parseInt(id)); //判断当前分类是否被商品绑定
		if(goodsList.size()>0){
			row = 998;
			return row;
		}
		row = mapper.deleteByPrimaryKey(Integer.parseInt(id));
		return row;
	}
	
	/**
	 * 分类详情
	 */
	@Override
	public GoodsShopCategory selectById(Integer id) throws Exception {
		GoodsShopCategory shopCategory = mapper.selectByPrimaryKey(id);
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(shopCategory.getShopId());
		GoodsShopCategory parentCategory = mapper.selectByPrimaryKey(shopCategory.getReid());
		if(parentCategory!=null){
			shopCategory.setParentName(parentCategory.getName());
		}
		shopCategory.setShopName(memberShop.getShopName());
		return shopCategory;
	}
	
	/**
	 * 查询分类列表
	 */
	@Override
	public PageBean<GoodsShopCategory> selectByFirstReid(String name, String currentPage, String pageSize, String reId)
			throws Exception {
		Integer currentPages = Integer.parseInt(currentPage);//当前第几页
		Integer pageSizes = Integer.parseInt(pageSize);//每页显示几条
		Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
		List<GoodsShopCategory> list = mapper.getListByFirstReid(name, pageStart, pageSizes, Integer.parseInt(reId));
		for(GoodsShopCategory category : list){
			GoodsShopCategory parentCategory = mapper.selectByPrimaryKey(category.getReid());
			if(parentCategory!=null){
				category.setParentName(parentCategory.getName());
			}
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(category.getShopId());
			category.setShopName(memberShop.getShopName());
		}
		int total = mapper.countAll(name, Integer.parseInt(reId));//总条数
		int pages = total / pageSizes;//总页数
		pages = total % pageSizes > 0 ? (pages+1) : pages;
		int size = list.size() == pageSizes ?  pageSizes : list.size();
		PageBean<GoodsShopCategory> page = new PageBean<>(list);
		page.setPageNum(currentPages);
		page.setList(list);
		page.setTotal(total);
		page.setPages(pages);
		page.setPageSize(pageSizes);
		page.setSize(size);
		return page;
	}
	
	/**
	 * 修改分类
	 */
	@Override
	public int updateCategory(String id, String name, String sortnum, String image ,String reid) throws Exception {
		int row = 0;
		GoodsShopCategory shopCategory = mapper.selectByPrimaryKey(Integer.parseInt(id));
		GoodsShopCategory entity = new GoodsShopCategory();
		entity.setName(name);
		entity.setId(Integer.parseInt(id));
		entity.setSeries(shopCategory.getSeries());
		List<GoodsShopCategory> list = mapper.selectUpdateByName(entity);
		if(list.size()==0){
			if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
				shopCategory.setImage(image);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				shopCategory.setSortnum(Short.parseShort(sortnum));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
				shopCategory.setName(name);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(reid)){
				shopCategory.setReid(Integer.parseInt(reid));
			}
			row = mapper.updateByPrimaryKeySelective(shopCategory);
		}else{
			row = 1000;
		}
		return row;
	}
	
	/**
	 * 根据父类id获取分类
	 */
	@Override
	public List<GoodsShopCategory> selectByParent(String reid) throws Exception {
		List<GoodsShopCategory> list = mapper.selectByParent(Integer.parseInt(reid));
		return list;
	}

	/**
	 * 获取所有分类
	 */
	@Override
	public List<GoodsShopCategory> selectAll() throws Exception {
		List<GoodsShopCategory> list = mapper.selectAll();
		for(GoodsShopCategory shopCategory : list){
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(shopCategory.getShopId());
			shopCategory.setShopName(memberShop.getShopName());
			GoodsShopCategory parentCategory = mapper.selectByPrimaryKey(shopCategory.getReid());
			if(parentCategory!=null){
				shopCategory.setParentName(parentCategory.getName());
			}
		}
		return list;
	}
	
	/**
	 * 获取三级分类
	 */
	@Override
	public List<GoodsShopCategory> selectThreeLevel() throws Exception {
		return mapper.selectThreeLevel();
	}
	
	/**
	 * 获取最后一级分类
	 */
	@Override
	public List<GoodsShopCategory> selectLastLevel() throws Exception {
		return mapper.selectLastLevel();
	}
	
	/**
	 * 获取所有分类列表
	 */
	@Override
	public List<GoodsShopCategory> selectAllList() throws Exception {
		List<GoodsShopCategory> cateGoryList = new ArrayList<>();
		GoodsShopCategory shopCateGory = new GoodsShopCategory();
		shopCateGory.setName("一级分类");
		shopCateGory.setId(0);
		shopCateGory.setIsLast(false);
		shopCateGory.setReid(-1);
		cateGoryList.add(shopCateGory);
		List<GoodsShopCategory> list = mapper.selectAllByName();
		for(GoodsShopCategory categroy : list ){
			if(categroy.getReid()>0){
				GoodsShopCategory parentCategory = mapper.selectByPrimaryKey(categroy.getReid());
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
	public List<GoodsShopCategory> selectLinkedList(int shopId) throws Exception {
		Short series = 1;
		GoodsShopCategory entity = new GoodsShopCategory();
		entity.setSeries(series);
		entity.setShopId(shopId);
		List<GoodsShopCategory> list = mapper.getBySeries(entity); //返回第一层分类
		if(list.size()>0){
			list = nextList(list);
		}
		return list;
	}
	
	/**
	 * 获取所有分类列表(树形结构)
	 */
	@Override
	public PageBean<GoodsShopCategory> selectLinkedPage(String currentPage, int pageSize, String name, int shopId) throws Exception {
		PageParams<GoodsShopCategory> params = new PageParams<GoodsShopCategory>();
		PageBean<GoodsShopCategory> pageBean = new PageBean<GoodsShopCategory>();
		params.setCurPage(Integer.parseInt(currentPage));
		params.setPageSize(Contants.PAGE_SIZE);
		
		Short series = 1;
		List<GoodsShopCategory> list = null;
		GoodsShopCategory entity = new GoodsShopCategory();
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			entity.setName(name);
			entity.setSeries(series);
			entity.setShopId(shopId);
			list = mapper.getBySeries(entity);
			if(list.size()==0){
				GoodsShopCategory entityTwo = new GoodsShopCategory();
				entityTwo.setName(name);
				series = 2;
				entityTwo.setSeries(series);
				entityTwo.setShopId(shopId);
				list = mapper.getBySeries(entityTwo);
				if(list.size()==0){
					GoodsShopCategory entityThree = new GoodsShopCategory();
					entityThree.setName(name);
					series = 3;
					entityThree.setSeries(series);
					entityThree.setShopId(shopId);
					list = mapper.getBySeries(entityThree);
				}else{
					list = nextList(list);
				}
			}else{
				list = nextList(list);
			}
		}else{
			entity.setSeries(series);
			entity.setShopId(shopId);
			list = mapper.getBySeries(entity); //返回第一层分类
			if(list!=null && list.size()>0){
				list = nextList(list);
			}
		}
		params.setResult(list);
		PageBean<GoodsShopCategory> retBean = pageBean.getPageResult(params);
		return retBean;
	}
	/*public PageBean<GoodsShopCategory> selectLinkedPage(String currentPage, int pageSize, String name, int shopId) throws Exception {
		Short series = 1;
		GoodsShopCategory entity = new GoodsShopCategory();
		entity.setSeries(series);
		entity.setName(name);
		entity.setShopId(shopId);
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<GoodsShopCategory> list = mapper.getBySeries(entity); //返回第一层分类
		if(list.size()>0){
			list = nextList(list);
		}
		PageBean<GoodsShopCategory> pageBean = new PageBean<>(list);
		return pageBean;
	}*/
	
	public List<GoodsShopCategory> nextList(List<GoodsShopCategory> list){
		for(GoodsShopCategory category : list){
			if(category.getReid()>0){
				GoodsShopCategory parentCategory = mapper.selectByPrimaryKey(category.getReid());
				category.setParentName(parentCategory.getName());
			}
			if(category.getIsLast()==false){
				List<GoodsShopCategory> childList = mapper.selectAllByReid(category.getId()); //根据父类id查询下一级列表
				category.setChildList(childList);
				if(childList.size()>0){
					nextList(childList);
				}
			}
		}
		return list;
	}

}
