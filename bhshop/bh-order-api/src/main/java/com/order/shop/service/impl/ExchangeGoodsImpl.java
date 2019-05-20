package com.order.shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.jd.bean.order.StockParams;
import com.bh.result.BhResult;
import com.bh.user.mapper.AreasMapper;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.Areas;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUserAddress;
import com.order.shop.service.ExchangeGoodsService;
import com.order.user.service.JDOrderService;

@Service
@Transactional
public class ExchangeGoodsImpl implements ExchangeGoodsService {

    @Autowired
    private MemberMapper menberMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private AreasMapper areasMapper;
	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsCartMapper goodsCartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private JDOrderService jDOrderService;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private CouponLogMapper couponLogMapper;
	
	@Override
	public int exchangeGoods(String phone) {
		// TODO Auto-generated method stub
		Member m = new Member();
		m.setUsername(phone);
		m.setPhone(phone);
		m.setIsNew(1);
		menberMapper.insertSelective(m);
		return m.getId();
	}

	@Override
	public Member getById(int mId) {
		// TODO Auto-generated method stub
		return menberMapper.selectByPrimaryKey(mId);
	}

	@Override
	public List<MemberUserAddress> selectAllAddressByisDel(MemberUserAddress address) throws Exception {
		List<MemberUserAddress> list = memberUserAddressMapper.selectAllAddressByisDel(address.getmId());
		return list;
	}

	@Override
	public int insertSelective(MemberUserAddress memberUserAddress) throws Exception {
		int row = 0;
		int r = memberUserAddressMapper.selectCountBymIdAndDefaultId(memberUserAddress.getmId());
		if (r == 1) {
			//memberUserAddressMapper.updateBymIdAndAndDefaultId(memberUserAddress.getmId());
			//是否是京东地址，0否，1是
			if (memberUserAddress.getIsJd() ==0) {
				if (memberUserAddress.getProv() != null) {
					Areas area = areasMapper.selectByPrimaryKey(memberUserAddress.getProv());
					String prvName = area.getAreaName();
					if (!StringUtils.isEmpty(prvName)) {
						memberUserAddress.setProvname(prvName);
						Areas area1 = areasMapper.selectByPrimaryKey(memberUserAddress.getCity());
						if (StringUtils.isNotEmpty(area1.getAreaName())) {
							memberUserAddress.setCityname(area1.getAreaName());
							Areas area2 = areasMapper.selectByPrimaryKey(memberUserAddress.getArea());
							if (area2 != null) {
								memberUserAddress.setAreaname(area2.getAreaName());
							}
						}
					}
				}
				row = memberUserAddressMapper.insertSelective(memberUserAddress);
			}else if (memberUserAddress.getIsJd() ==1) {
				row = insertAddressByIsJd(memberUserAddress);
			}
		} else {
			row = insertAddressByIsJd(memberUserAddress);
			
		}

		return row;
	}

	
	
	
	public int insertAddressByIsJd(MemberUserAddress memberUserAddress) throws Exception{
		int row =0;
		if (memberUserAddress.getProv() != null) {
			GoAddressArea area = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getProv());
			String prvName = area.getName();
			if (!StringUtils.isEmpty(prvName)) {
				memberUserAddress.setProvname(prvName);
				GoAddressArea area1 = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getCity());
				if (StringUtils.isNotEmpty(area1.getName())) {
					memberUserAddress.setCityname(area1.getName());
					GoAddressArea area2 = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getArea());
					if (area2 != null) {
						memberUserAddress.setAreaname(area2.getName());
						if (memberUserAddress.getFour() !=0) {
							GoAddressArea area3 = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getFour());
							if (area3.getName() !=null) {
								memberUserAddress.setFourname(area3.getName());
							}
						}else{
							memberUserAddress.setFourname("");
						}
					}
				}
			}
			if (memberUserAddress.getId() == null) {
				row = memberUserAddressMapper.insertSelective(memberUserAddress);
			}else{
				memberUserAddress.setIsJd(1);
				row = memberUserAddressMapper.updateByPrimaryKeySelective(memberUserAddress);
			}
		}
		return row;
	}

	@Override
	public GoodsSku getBySkuNo(Long skuNo) {
		// TODO Auto-generated method stub
		return goodsSkuMapper.selectByJdSkuNo(skuNo);
	}
	
	
	
	  //2018-6-6程凤云获得cartId
    public BhResult selectCartId(Long skuNo,Integer mId,String addresId)throws Exception{
    	BhResult bhResult=new BhResult(400, "", null);
    	List<GoodsSku> goodsSku=goodsSkuMapper.selectGoodsSkuByJDSku(skuNo);
    	if (goodsSku.size()<=0) {
			return bhResult=BhResult.build(400, "该兑换商品不存在");
		}
    	else{
			GoodsCart goodsCart = new GoodsCart();
			Goods goods=goodsMapper.selectByPrimaryKey(goodsSku.get(0).getGoodsId());
	    	if (goods ==null) {
				return bhResult=BhResult.build(400, "该兑换商品不存在");
			}else{
				goodsCart.setmId(mId);
		    	goodsCart.setShopId(goods.getShopId());
		    	goodsCart.setgId(goodsSku.get(0).getGoodsId());
		    	goodsCart.setNum(1);
		    	goodsCart.setAddtime(new Date());
		    	goodsCart.setIsDel(3);
		    	goodsCart.setGskuid(goodsSku.get(0).getId());
		    	goodsCartMapper.insertSelective(goodsCart);
		    	
		    	
		    	//库存的result
		    	/*BhResult result=new BhResult();
		    	//判断库存
		    	if (goodsSku.get(0).getJdSupport() == 0) {
		    		String provname = memberUserAddressMapper.selectProvName(Integer.parseInt(addresId));
					result = jDOrderService.getBHSto(goodsSku.get(0).getGoodsId(), provname, goodsSku.get(0).getStoreNums(),
							goodsCart.getNum());
				} else if (goodsSku.get(0).getJdSupport() == 1) {
					MemberUserAddress memberUserAddress = memberUserAddressMapper
							.selectByPrimaryKey(Integer.parseInt(addresId));
					List<StockParams> stockParams = new ArrayList<>();
					StockParams params = new StockParams();
					params.setNum(1);
					params.setSkuId(goodsSku.get(0).getJdSkuNo().toString());
					stockParams.add(params);
					result=jDOrderService.getJDStock(memberUserAddress.getProv().toString(), memberUserAddress.getCity().toString(), memberUserAddress.getArea().toString(),stockParams);
					if (result.getStatus() == 200) {
						if (!memberUserAddress.getFour().equals(0)) {
							result = jDOrderService.getAreaByJD(goodsSku.get(0).getJdSkuNo().toString(), memberUserAddress.getProv().toString(),
									memberUserAddress.getCity().toString(), memberUserAddress.getArea().toString(),
									memberUserAddress.getFour().toString());
						} else {
							result = jDOrderService.getAreaByJD(goodsSku.get(0).getJdSkuNo().toString(), memberUserAddress.getProv().toString(),
									memberUserAddress.getCity().toString(), memberUserAddress.getArea().toString(),
									"");
						}

					}
				}*/
		    	
		    	
		    	return bhResult=new BhResult(200, "操作成功", goodsCart.getId());
				
			}
	    	
	    	
		}
    }

	@Override
	public Coupon getById(Coupon c) {
		// TODO Auto-generated method stub
		return couponMapper.selectByPrimaryKey(c.getId());
	}

	@Override
	public int add(CouponLog c) {
		// TODO Auto-generated method stub
		return couponLogMapper.insertSelective(c);
	}


	
	
	
}
