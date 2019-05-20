package com.bh.product.api.service.impl;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsBrandMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.GoodsBrandService;
import com.bh.product.api.service.GoodsCommentService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberShopAdminMapper;
import com.bh.user.mapper.PromoteUserMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShopAdmin;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

import net.sf.json.JSONObject;

@Service
public class GoodsCommentImpl implements GoodsCommentService{
	@Autowired
	private GoodsCommentMapper mapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberShopAdminMapper shopMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private OrderSkuMapper skuMapper;
	@Autowired
	private PromoteUserMapper promoteMapper;
	
	
	/**
	 * 根据商品id查询所有评价
	 */
	@Override
	public PageBean<GoodsComment> selectByGoodsId(String goodsId, String shopId, String level, String isAddEvaluate, String fz, String currentPage, int pageSize) throws Exception {
		List<GoodsComment> list = null;
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		if(!StringUtils.isEmptyOrWhitespaceOnly(level)){ //评价等级查询
			list = mapper.getListByLevel(Integer.parseInt(goodsId), Integer.parseInt(shopId), level);
			
		}else if(!StringUtils.isEmptyOrWhitespaceOnly(isAddEvaluate)){//带追评查询
			list = mapper.getListByEvalute(Integer.parseInt(goodsId), Integer.parseInt(shopId));
			
		}else if(!StringUtils.isEmptyOrWhitespaceOnly(fz)){ //带评价图查询
			list = mapper.getListByImage(Integer.parseInt(goodsId), Integer.parseInt(shopId));
			
		}else{//全部列表
			list = mapper.getListByGoodsId(Integer.parseInt(goodsId), Integer.parseInt(shopId));
		}
		for(GoodsComment goodsComment : list){

			if (goodsComment.getDescription()!=null) {
				goodsComment.setDescription(URLDecoder.decode(goodsComment.getDescription(),"utf-8"));
			}

			//cheng修复时间的格式化
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			goodsComment.getAddtime();
			StringBuffer sb = new StringBuffer();
			String addtime = sdf.format(goodsComment.getAddtime());
			String [] a = addtime.split(" ");
			String[] a1 =a[0].toString().split("-");
			sb.append(a1[0].toString()).append(".").append(a1[1].toString().toString()).append(".").append(a1[2].toString().toString());
			goodsComment.setAddtime1(sb.toString());
			if(goodsComment.getSkuValue()!=null){
				JSONObject object = JSONObject.fromObject(goodsComment.getSkuValue()); //skuValue转义
				goodsComment.setSkuValueObj(object);
			}
			
			if(goodsComment.getImage()!=null){ //评价图片转数组
				List<String> bs = Arrays.asList(goodsComment.getImage().split(","));
				final int size = bs.size();
				String[] arr = (String[])bs.toArray(new String[size]);
				goodsComment.setImageStr(arr);
			}
			
			Member member =  memberMapper.selectByPrimaryKey(goodsComment.getmId());

			if(goodsComment.getNotname()==0){
				goodsComment.setmName("匿名");
				goodsComment.setmHead(Contants.headImage);
			}else if(member!=null){
				if(member.getUsername()!=null){
					//2018.5.24 zlk
					if(org.apache.commons.lang.StringUtils.isNotBlank(member.getUsername())) {
						member.setUsername(URLDecoder.decode(member.getUsername(), "utf-8"));
					}
					//end
					goodsComment.setmName(member.getUsername());
				}else{
					goodsComment.setmName("匿名");
				}
				if(member.getHeadimgurl()!=null){
					goodsComment.setmHead(member.getHeadimgurl());
				}else{
					goodsComment.setmHead(Contants.headImage);
				}
			}else{
				PromoteUser user = promoteMapper.selectByPrimaryKey(goodsComment.getmId());
				if(user!=null){
					//2018.5.24 zlk
					if(org.apache.commons.lang.StringUtils.isNotBlank(user.getName())) {
						user.setName(URLDecoder.decode(user.getName(), "utf-8"));
					}
					//end
					goodsComment.setmName(user.getName());
					goodsComment.setmHead(user.getHeadImg());
				}else{
					goodsComment.setmName("匿名");
					goodsComment.setmHead(Contants.headImage);
				}
			}
			
			GoodsComment comment = mapper.getListByReid(goodsComment.getId()); //追评
			if(comment!=null){
				if(comment.getImage()!=null){ //评价图片转数组
					List<String> bs = Arrays.asList(comment.getImage().split(","));
					final int size = bs.size();
					String[] arr = (String[])bs.toArray(new String[size]);
					comment.setImageStr(arr);
				}
				goodsComment.setAddEvaluate(comment);
			}
		}
		PageBean<GoodsComment> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 根据不同条件统计所有评论
	 */
	@Override
	public HashMap<Integer, String> selectCount(String goodsId, String shopId)
			throws Exception {
		//全部评价
		    int countAll = mapper.countByGoodsId(Integer.parseInt(goodsId), Integer.parseInt(shopId));
		//有评价图
		    int countImage = mapper.countByImage(Integer.parseInt(goodsId), Integer.parseInt(shopId));
		//有追评
		    int countEvaluate = mapper.countByEvalute(Integer.parseInt(goodsId), Integer.parseInt(shopId));
		//评价等级查询1为好评2为中评3为差评
			int countOne = mapper.countByLevel(Integer.parseInt(goodsId), Integer.parseInt(shopId), 1); 
			
			int countTwo = mapper.countByLevel(Integer.parseInt(goodsId), Integer.parseInt(shopId), 2);
			
			int countThree = mapper.countByLevel(Integer.parseInt(goodsId), Integer.parseInt(shopId), 3);
		//好评度
			String str = null;
			if(countOne>0 && countAll>0){
				DecimalFormat df = new DecimalFormat("0.00");
				String s = df.format((float)countOne / countAll);
				int degree = (int)(Double.parseDouble(s) * 100);
				str = degree + "%";
			}else{
				str = "0%";
			} 
			
			
			HashMap<Integer, String> map = new HashMap<>();
			map.put(1, Integer.toString(countAll));//全部评价
			map.put(2, Integer.toString(countImage));//有图评价
			map.put(3, Integer.toString(countEvaluate));//有追评
			map.put(4, Integer.toString(countOne));//好评
			map.put(5, Integer.toString(countTwo));//中评
			map.put(6, Integer.toString(countThree));//差评
			map.put(7, str);//好评度
			
			//list = Arrays.asList(buffer.toString().split(","));
		return map;
	}
	
	/**
	 * 后台商品评论管理
	 */
	@Override
	public PageBean<GoodsComment> pageByShopId(GoodsComment entity, int shopId)
			throws Exception {
		entity.setShopId(shopId);
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<GoodsComment> list = mapper.pageByShopId(entity);
		if(list.size()>0){
			for(GoodsComment comment : list){
				
				Member member =  memberMapper.selectByPrimaryKey(comment.getmId());
				if(member!=null){
					if(comment.getNotname()==0){
						comment.setmName("匿名");
					}else{
						comment.setmName(member.getUsername());
						comment.setmHead(member.getHeadimgurl());
					}
				}else{
					PromoteUser user = promoteMapper.selectByPrimaryKey(comment.getmId());
					if(user!=null){
						comment.setmName(user.getName());
						comment.setmHead(user.getHeadImg());
					}else{
						comment.setmName("匿名");
						comment.setmHead(Contants.headImage);
					}
				}
				Goods goods = goodsMapper.selectByPrimaryKey(comment.getGoodsId());
				comment.setGoodsName(goods.getName());
				
				/*//插入评论详情
				GoodsComment goodsComment = mapper.selectByPrimaryKey(comment.getId());
				
				JSONObject object = JSONObject.fromObject(goodsComment.getSkuValue()); //skuValue转义
				goodsComment.setSkuValueObj(object);
				
				if(goodsComment.getImage()!=null){ //评价图片转数组
					List<String> bs = Arrays.asList(goodsComment.getImage().split(","));
					final int size = bs.size();
					String[] arr = (String[])bs.toArray(new String[size]);
					goodsComment.setImageStr(arr);
				}
				
				GoodsComment addEvaluate = mapper.getListByReid(comment.getId()); //判断是否有追评
				if(addEvaluate!=null){
					if(addEvaluate.getImage()!=null){//评价图片转数组
						List<String> add = Arrays.asList(addEvaluate.getImage().split(","));
						final int sizes = add.size();
						String[] arrs = (String[])add.toArray(new String[sizes]);
						addEvaluate.setImageStr(arrs);
					}
					goodsComment.setAddEvaluate(addEvaluate);
				}
				
				Member mber =  memberMapper.selectByPrimaryKey(comment.getmId()); //设置评价用户信息
				if(member!=null){
					goodsComment.setmName(mber.getUsername());
					goodsComment.setmHead(mber.getHeadimgurl());
				}
				
				OrderSku sku = skuMapper.selectByPrimaryKey(comment.getOrderSkuId());
				if(sku!=null){
					goodsComment.setGoodsName(sku.getGoodsName());
					goodsComment.setGoodsImage(sku.getSkuImage());
				}
				
				comment.setDetails(goodsComment);*/
				
			}
		}
		PageBean<GoodsComment> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 后台商品评论详情
	 */
	@Override
	public GoodsComment getCommentDetails(String id) throws Exception {
		GoodsComment comment = mapper.selectByPrimaryKey(Integer.parseInt(id));
		
		JSONObject jsonObject = JSONObject.fromObject(comment.getSkuValue());
		comment.setSkuValueObj(jsonObject);
		
		GoodsComment addEvaluate = mapper.getListByReid(Integer.parseInt(id)); //判断是否有追评
		if(addEvaluate!=null){
			comment.setAddEvaluate(addEvaluate);
		}
		
		Member member =  memberMapper.selectByPrimaryKey(comment.getmId()); //设置评价用户信息
		if(member!=null){
			comment.setmName(member.getUsername());
			comment.setmHead(member.getHeadimgurl());
		}
		
		OrderSku sku = skuMapper.selectByPrimaryKey(comment.getOrderSkuId());
		if(sku!=null){
			comment.setGoodsName(sku.getGoodsName());
			comment.setGoodsImage(sku.getSkuImage());
		}
		return comment;
	}
	
	/**
	 * 后台新增评价
	 */
	@Override
	public int insert(GoodsComment entity) throws Exception {
		Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
		entity.setShopId(goods.getShopId());
		entity.setAddtime(new Date());		
		if(entity.getmId()==null){//添加随机用户id
			List<PromoteUser> list = promoteMapper.selectAllIds();
			StringBuffer buffer = new StringBuffer();
			if(list.size()>0){
				for(PromoteUser user : list){
					buffer.append(user.getId()+",");
				}
				String[] numbers = buffer.toString().substring(0, buffer.toString().length()-1).split(",");
				Random random = new Random();
			    int index = random.nextInt(numbers.length);
			    
			    entity.setmId(Integer.parseInt(numbers[index]));
			}
		}
		
		List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsId(entity.getGoodsId());
		if(skuList.size()>0){
			entity.setSkuValue(skuList.get(0).getValue());
		}
		
		Byte a = 1;
		Byte b = 2;
		Byte c = 3;
		if(entity.getStar()<3){ //设置评价等级
			entity.setLevel(c);
		}else if(entity.getStar()==5){
			entity.setLevel(a);
		}else{
			entity.setLevel(b);
		}
		return mapper.insertSelective(entity);
	}
	
	/**
	 * 评价的显示和隐藏
	 */
	@Override
	public int changeStatus(GoodsComment entity) throws Exception {
		return mapper.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 删除
	 */
	@Override
	public int delete(GoodsComment entity) throws Exception {
		int row = 0;
		GoodsComment comment = mapper.getByReid(entity.getId());
		//程凤云
		GoodsComment co = new GoodsComment();
		co.setId(entity.getId());
		co.setIsDel(1);
		if(comment!=null){
			//row = mapper.deleteByPrimaryKey(comment.getId());
			row = mapper.updateByPrimaryKeySelective(co);
		}
		//row = mapper.deleteByPrimaryKey(entity.getId());
		row = mapper.updateByPrimaryKeySelective(co);
		return row;
	}
	
	
	//批量修改排序
	public int updateCommentByBatch(List<GoodsComment> comments) throws Exception{
		int row = 0;
		if (comments.size() > 0) {
			for (GoodsComment goodsComment : comments) {
				mapper.updateByPrimaryKeySelective(goodsComment);
			}
		}else{
			row = 1;
		}
		return row;
	}
	
	
	public PageBean<PromoteUser> selectPromoteUser(PromoteUser entity) throws Exception{
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.SIZE, true);
		List<PromoteUser> list = promoteMapper.selectAllPUser(entity);
		PageBean<PromoteUser> pageBean = new PageBean<>(list);
		return pageBean;
	}
}
