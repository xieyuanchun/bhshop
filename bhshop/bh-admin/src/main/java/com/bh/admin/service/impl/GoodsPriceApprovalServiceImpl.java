package com.bh.admin.service.impl;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.goods.GoodsPriceApprovalMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.goods.GoodsPrice;
import com.bh.admin.pojo.goods.GoodsPriceApproval;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.SimplePriceApproval;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.service.GoodsPriceApprovalService;
import com.bh.utils.JsonUtils;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class GoodsPriceApprovalServiceImpl implements GoodsPriceApprovalService{
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsPriceApprovalMapper goodsPriceApprovalMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	
	
	
	//如果价格改变，则插入价格改变表
	public	void insertGoodsPriceApproval(GoodsSku goodsSku,Integer replyId) throws Exception{
		
		GoodsSku goodsSku2 = goodsSkuMapper.selectByPrimaryKey(goodsSku.getId());
		GoodsPriceApproval approval = new GoodsPriceApproval();
		approval.setGoodsSkuId(goodsSku.getId());
		approval.setGoodsId(goodsSku2.getGoodsId());
		approval.setStatus(0);
		//申请时间
		approval.setReplyTime(new Date());
		approval.setReplyId(replyId);
		approval.setType(0);
		approval.setIsDel(0);
		
		//市场价(市场价单位分)
		if (goodsSku.getMarketPrice() != null) {
			approval.setReplyNo("1");
			//新值
			approval.setNewVal(goodsSku.getMarketPrice()+"");
			//旧值
			approval.setOldVal(goodsSku2.getMarketPrice()+"");
		
			List<GoodsPriceApproval> list = goodsPriceApprovalMapper.selectGoodsP(approval);
			//如果表中没有数据存在/全部已经审核
			if (list.size() < 1) {
				if (!goodsSku.getMarketPrice().equals(goodsSku2.getMarketPrice())) {
					approval.setId(null);
					goodsPriceApprovalMapper.insertSelective(approval);
				}
			}else{
				approval.setId(list.get(0).getId());
				goodsPriceApprovalMapper.updateByPrimaryKeySelective(approval);
			}
				
			
		}
		
		if (goodsSku.getSellPrice() !=null) {
			//单卖价
			approval.setReplyNo("2");
			approval.setNewVal(goodsSku.getSellPrice()+"");
			approval.setOldVal(goodsSku2.getSellPrice()+"");
			List<GoodsPriceApproval> list2 = goodsPriceApprovalMapper.selectGoodsP(approval);
			//如果表中没有数据存在/全部已经审核
			if (list2.size() < 1) {
				if (!goodsSku.getSellPrice().equals(goodsSku2.getSellPrice())) {
					approval.setId(null);
					goodsPriceApprovalMapper.insertSelective(approval);
				}
			}else{
				approval.setId(list2.get(0).getId());
				if (! list2.get(0).getNewVal().equals(goodsSku.getSellPrice())) {
					goodsPriceApprovalMapper.updateByPrimaryKeySelective(approval);
				}
			}
		}
		if (goodsSku.getTeamPrice() !=null) {
			//拼团价
			approval.setReplyNo("3");
			approval.setNewVal(goodsSku.getTeamPrice()+"");
			approval.setOldVal(goodsSku2.getTeamPrice()+"");
			List<GoodsPriceApproval> list3 = goodsPriceApprovalMapper.selectGoodsP(approval);
			//如果表中没有数据存在/全部已经审核
			if (list3.size() < 1) {
				if (! goodsSku.getTeamPrice().equals(goodsSku2.getTeamPrice())) {
					approval.setId(null);
					goodsPriceApprovalMapper.insertSelective(approval);
				}
			}else{
				approval.setId(list3.get(0).getId());
				goodsPriceApprovalMapper.updateByPrimaryKeySelective(approval);
			}
		}
		
		//进货价
		if (goodsSku.getStockPrice() !=null) {
			approval.setReplyNo("4");
			approval.setNewVal(goodsSku.getStockPrice()+"");
			approval.setOldVal(goodsSku2.getStockPrice()+"");
			List<GoodsPriceApproval> list4 = goodsPriceApprovalMapper.selectGoodsP(approval);
			if (list4.size() < 1) {
				if (! goodsSku.getStockPrice().equals(goodsSku2.getStockPrice())) {
					approval.setId(null);
					goodsPriceApprovalMapper.insertSelective(approval);
				}
			}else{
				approval.setId(list4.get(0).getId());
				goodsPriceApprovalMapper.updateByPrimaryKeySelective(approval);
			}
		}
		//修改运费
		if (goodsSku.getDeliveryPrice() !=null) {
			approval.setReplyNo("5");
			approval.setNewVal(goodsSku.getDeliveryPrice()+"");
			approval.setOldVal(goodsSku2.getDeliveryPrice()+"");
			List<GoodsPriceApproval> list4 = goodsPriceApprovalMapper.selectGoodsP(approval);
			if (list4.size() < 1) {
				if (! goodsSku.getDeliveryPrice().equals(goodsSku2.getDeliveryPrice())) {
					approval.setId(null);
					goodsPriceApprovalMapper.insertSelective(approval);
				}
			}else{
				approval.setId(list4.get(0).getId());
				goodsPriceApprovalMapper.updateByPrimaryKeySelective(approval);
			}
		}
		
	}
	
	public int checkVelifyStatus(GoodsPriceApproval vApproval) throws Exception{
		int row =0;
		//参数id,status
		GoodsPriceApproval val = goodsPriceApprovalMapper.selectByPrimaryKey(vApproval.getId());
		//状态:0未审核  1通过  2未通过, 3价格已变更
		if (vApproval.getStatus().equals(1)) {
			//如果是审核通过,则需要判断该商品是否在拼团中(真人发起团)
			List<OrderTeam> teamList = selectTeamMsg(val.getGoodsSkuId());
			//如果团列表的长度小于1，则该商品不再团中
			if (teamList.size() < 1) {
				vApproval.setStatus(3);
				//改变价格和状态
				row = velifyStatus(vApproval);
			}else{
				vApproval.setStatus(1);
				//仅改变状态:设置状态为1
				row = velifyStatus2(vApproval);
			}
		}else if (vApproval.getStatus().equals(2)) {
			//仅改变状态:设置状态为1
			row = velifyStatus2(vApproval);
		}
		return row;
	}
	
	//价格的改变
    public	int velifyStatus(GoodsPriceApproval val) throws Exception{
		int row = 0;
		//审核时间
		val.setApprovalTime(new Date());
		
		goodsPriceApprovalMapper.updateByPrimaryKeySelective(val);
		GoodsPriceApproval approval = goodsPriceApprovalMapper.selectByPrimaryKey(val.getId());
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(approval.getGoodsSkuId());
		//审请编号1市场价，2单卖价，3拼团价，4进货价
		if (goodsSku != null) {
			String replyNo = approval.getReplyNo();
			GoodsSku goodsSku2 = new GoodsSku();
			goodsSku2.setId(goodsSku.getId());
			switch (replyNo) {
			case "1":
				goodsSku2.setMarketPrice(Integer.parseInt(approval.getNewVal()));
			    break;
			case "2":
				goodsSku2.setSellPrice(Integer.parseInt(approval.getNewVal()));
				break;
		   case "3":
				goodsSku2.setTeamPrice(Integer.parseInt(approval.getNewVal()));
			    break;
			case "4":
				goodsSku2.setStockPrice(Integer.parseInt(approval.getNewVal()));
				break;
			case "5":
				goodsSku2.setDeliveryPrice(Integer.parseInt(approval.getNewVal()));
				break;	
			default:
				break;
			}
			row = goodsSkuMapper.updateByPrimaryKeySelective(goodsSku2);
		}
		return row;
	}
    
    
    public PageBean<GoodsPriceApproval> selectPriceApprovalList(GoodsPriceApproval val) throws Exception{
    	
    	PageHelper.startPage(Integer.parseInt(val.getCurrentPage()), Integer.parseInt(val.getSize()), true);
		List<GoodsPriceApproval> list = goodsPriceApprovalMapper.listPage(val);
		if (list.size()>0) {
			for (GoodsPriceApproval goodsPriceApproval : list) {
				Goods goods = goodsMapper.selectByPrimaryKey(goodsPriceApproval.getGoodsId());
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(goodsPriceApproval.getGoodsSkuId());
				goodsPriceApproval.setGoodsName(goods.getName());
				goodsPriceApproval.setGoodsSkuNo(goodsSku.getSkuNo());
				 String value = goodsSku.getValue();
				 goodsPriceApproval.setSkuValue(JsonUtils.stringToObject(value));
				 org.json.JSONObject jsonObj = new org.json.JSONObject(value);
			     org.json.JSONArray personList = jsonObj.getJSONArray("url");
			     goodsPriceApproval.setGoodsImage((String) personList.get(0));
			     //replyId申请人的id
			     if (goodsPriceApproval.getReplyId() !=null) {
			    	 String userName = memberShopMapper.selectUsernameBymId(goodsPriceApproval.getReplyId());
			    	 if (userName !=null) {
						goodsPriceApproval.setReplyName(userName);
					}
				}
			     //approvalId审批人的id
			     if (goodsPriceApproval.getApprovalId() !=null) {
			    	 String userName = memberShopMapper.selectUsernameBymId(goodsPriceApproval.getApprovalId());
			    	 if (userName !=null) {
						goodsPriceApproval.setApprovalName(userName);
					}
				}
			    
			}
		}
			
		PageBean<GoodsPriceApproval> pageBean = new PageBean<>(list);
		return pageBean;
	}
    
    
    public int velifyStatus2(GoodsPriceApproval val) throws Exception{
    	int row = 0;
    	row = goodsPriceApprovalMapper.updateByPrimaryKeySelective(val);
    	return row;
    }
    
    //判断该商品是否在拼团列表中
    public List<OrderTeam> selectTeamMsg(Integer goodsSkuId) throws Exception{
    	//查询该商品拼团的信息
    	List<OrderTeam> listTeam = orderTeamMapper.selectTeamMsg(goodsSkuId);
    	return listTeam;
    }


	public List<GoodsPriceApproval> selectByStatus(GoodsPriceApproval val) throws Exception {
		// TODO Auto-generated method stub
		return goodsPriceApprovalMapper.selectByStatus(val);
	}

    
    
    public int deletePriceApproval(List<String> ids) throws Exception{
    	int row = 1;
    	for (String string : ids) {
    		GoodsPriceApproval val = new GoodsPriceApproval();
    		val.setId(Integer.parseInt(string));
    		val.setIsDel(1);
			goodsPriceApprovalMapper.updateByPrimaryKeySelective(val);
		}
    	return row;
    }
    
    
    
    //2018-4-16程 凤云
 public PageBean<SimplePriceApproval> selectPriceApprovalList1(SimplePriceApproval val) throws Exception{
    	PageHelper.startPage(Integer.parseInt(val.getCurrentPage()), Integer.parseInt(val.getSize()), true);
		List<SimplePriceApproval> list = goodsPriceApprovalMapper.listPage1(val);
		if (list.size()>0) {
			for (SimplePriceApproval goodsPriceApproval : list) {
				Goods goods = goodsMapper.selectByPrimaryKey(goodsPriceApproval.getGoodsId());
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(goodsPriceApproval.getGoodsSkuId());
				goodsPriceApproval.setGoodsName(goods.getName());
				goodsPriceApproval.setGoodsSkuNo(goodsSku.getSkuNo());
				 String value = goodsSku.getValue();
				 goodsPriceApproval.setSkuValue(JsonUtils.stringToObject(value));
				 org.json.JSONObject jsonObj = new org.json.JSONObject(value);
			     org.json.JSONArray personList = jsonObj.getJSONArray("url");
			     goodsPriceApproval.setGoodsImage((String) personList.get(0));
			     //replyId申请人的id
			     if (goodsPriceApproval.getReplyId() !=null) {
			    	 String userName = memberShopMapper.selectUsernameBymId(goodsPriceApproval.getReplyId());
			    	 if (userName !=null) {
						goodsPriceApproval.setReplyName(userName);
					}
				}
			     //approvalId审批人的id
			     if (goodsPriceApproval.getApprovalId() !=null) {
			    	 String userName = memberShopMapper.selectUsernameBymId(goodsPriceApproval.getApprovalId());
			    	 if (userName !=null) {
						goodsPriceApproval.setApprovalName(userName);
					}
				}
			    GoodsPrice goodsPrice = new GoodsPrice();
			    goodsPrice.setGoodsSkuId(goodsPriceApproval.getGoodsSkuId());
			    goodsPrice.setShopId(val.getShopId());
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    goodsPrice.setNewVal(formatter.format(goodsPriceApproval.getReplyTime()));
			    List<GoodsPrice> priceList = goodsPriceApprovalMapper.selectPrice(goodsPrice);
			    goodsPriceApproval.setGoodsPrice(priceList);
			}
		}
			
		PageBean<SimplePriceApproval> pageBean = new PageBean<>(list);
		return pageBean;
	}

}
