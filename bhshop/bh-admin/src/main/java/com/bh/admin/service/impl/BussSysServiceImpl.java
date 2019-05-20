package com.bh.admin.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderPaymentMapper;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderPayment;
import com.bh.result.BhResult;
import com.bh.admin.service.BussSysService;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberUserAddressMapper;
import com.bh.admin.mapper.user.MemberUserMapper;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.pojo.user.MemberUserDetail;
import com.bh.admin.pojo.user.ShowTiaoZheng;
import com.bh.utils.MD5Util1;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class BussSysServiceImpl implements BussSysService {

	@Value(value = "${pageSize}")
	private String pageSize;

	@Autowired
	private OrderPaymentMapper orderPaymentMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private MemberMapper memberMpper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;

	public PageBean<OrderPayment> selectOrderPayment(OrderPayment orderPayment, Integer page, Integer size)
			throws Exception {
		List<OrderPayment> list = new ArrayList<>();
		// 启用分页查询
		PageHelper.startPage(page, size, true);
		list = orderPaymentMapper.selectAllOrderPayments(orderPayment);

		PageInfo<OrderPayment> info = new PageInfo<>(list);

		PageBean<OrderPayment> pageBean = null;
		pageBean = new PageBean<>(list);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());
		return pageBean;
	}

	// 新增支付方式
	public int insertOrderPayment(OrderPayment orderPayment) throws Exception {
		int row = 0;
		orderPayment.setType(0);
		orderPaymentMapper.insertSelective(orderPayment);
		OrderPayment orderPayment2 = new OrderPayment();
		orderPayment2.setId(orderPayment.getId());
		orderPayment2.setType(orderPayment.getId());
		row = orderPaymentMapper.updateByPrimaryKeySelective(orderPayment2);
		return row;
	}

	public OrderPayment selectOrderPayment(OrderPayment orderPayment) throws Exception {
		OrderPayment orderPayment2 = new OrderPayment();
		orderPayment2 = orderPaymentMapper.selectByPrimaryKey(orderPayment.getId());
		return orderPayment2;
	}

	public int updateOrderPayment(OrderPayment orderPayment) throws Exception {
		int row = 0;
		row = orderPaymentMapper.updateByPrimaryKeySelective(orderPayment);
		return row;
	}

	public int deleteOrderPayment(OrderPayment orderPayment) throws Exception {
		int row = 0;
		row = orderPaymentMapper.updateOrderPaymentStatus(orderPayment);
		return row;
	}

	/*************************************** 以下是会员 **********************************************************/
	public PageBean<MemberUser> memberUserList(MemberUser entity) throws Exception {
		if (entity.getCurrentPage() == null) {
			entity.setCurrentPage("1");
		}
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<MemberUser> list = memberUserMapper.selectMemberUserByParams(entity);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Member member = memberMpper.selectByPrimaryKey(list.get(i).getmId());
				//由于头像可能是表情
				if (member!=null) {
					if (StringUtils.isNotEmpty(member.getUsername())) {
				        member.setUsername(URLDecoder.decode(member.getUsername(),"utf-8"));
				      }
				}
				list.get(i).setMember(member);
			}
		}
		PageBean<MemberUser> pageBean = new PageBean<>(list);
		return pageBean;
	}

	// 会员详情
	public MemberUserDetail selectMemberUserDetail(MemberUser memberUser) throws Exception {
		MemberUserDetail detail = new MemberUserDetail();
		MemberUser memberUser2 = memberUserMapper.selectByPrimaryKey(memberUser.getmId());
		Member member = memberMpper.selectByPrimaryKey(memberUser.getmId());
		detail.setEmail(memberUser2.getEmail());
		detail.setFullName(memberUser2.getFullName());
		detail.setPhone(member.getPhone());
		detail.setStatus(memberUser2.getStatus());
		//2018.5.24 zlk 
		if(org.apache.commons.lang.StringUtils.isNotBlank(member.getUsername())) {
		    detail.setUsername(URLDecoder.decode(member.getUsername(), "utf-8"));
		}
		//end
		detail.setSex(memberUser2.getSex());
		
		// 总计订单(笔)
		Order o = new Order();
		o.setmId(memberUser.getmId());
		List<Order> list = getMemberUserOrder(o);
		if (list.size() > 0) {
			detail.setOrderNum(list.size());
		}else{
			detail.setOrderNum(0);
		}
		detail.setOrder(list);
		// 用户的消费总金额
		Order order = new Order();
		order.setmId(memberUser.getmId());
		Integer totalAbility = orderMapper.selectTotalAbilityByUser(order);
		if (totalAbility == null) {
			totalAbility = 0;
		}
		double price = (double) totalAbility / 100;
		detail.setTotalAbility(RegExpValidatorUtils.formatdouble(price));

		// 用户本月订单
		Order thisOrder = new Order();
		thisOrder.setmId(memberUser.getmId());
		thisOrder.setAddtime(new Date());
		List<Order> list1 = getMemberUserOrder(thisOrder);
		if (list1.size() > 0) {
			int total1 = (int) list1.size();
			detail.setThisMonthOrderNum(total1);
		}else{
			detail.setThisMonthOrderNum(0);
		}

		// 用户本月消费金额
		Order order1 = new Order();
		order1.setmId(memberUser.getmId());
		order1.setAddtime(new Date());
		Integer thisMonthTotalAbility = orderMapper.selectTotalAbilityByUser(order1);
		if (thisMonthTotalAbility == null) {
			thisMonthTotalAbility = 0;
		}
		double thisMAmount =(double) thisMonthTotalAbility / 100;
		detail.setThisMonthTotalAbility(RegExpValidatorUtils.formatdouble(thisMAmount));
		MemberUserAddress memberUserAddress = new MemberUserAddress();
		memberUserAddress.setmId(memberUser.getmId());
		List<MemberUserAddress> address= getMemberUserAddress(memberUserAddress);
		detail.setMemberUserAddressList(address);
		return detail;
	}

	public List<Order> getMemberUserOrder(Order order) {
		List<Order> list = orderMapper.selectOrderByUser(order);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				double price = (double) list.get(i).getOrderPrice()/100;
				Integer goodsPay = list.get(i).getOrderPrice()-list.get(i).getDeliveryPrice();
				//商品价格
				double gPay = (double) goodsPay/100;
				list.get(i).setRealPrice(RegExpValidatorUtils.formatdouble(price));
				list.get(i).setRealDeliveryPrice(RegExpValidatorUtils.formatdouble(gPay));
				if (list.get(i).getmAddrId() != null) {
					MemberUserAddress address = memberUserAddressMapper.selectByPrimaryKey(list.get(i).getmAddrId());
					if (address != null) {
						list.get(i).setMemberUserAddress(address);
					}
				}
			}
		}
		
		return list;
	}
	
	public List<MemberUserAddress> getMemberUserAddress(MemberUserAddress address) {
		List<MemberUserAddress> list = new ArrayList<>();
		list = memberUserAddressMapper.selectAddressByOrderMain(address);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Order order = new Order();
				order.setmAddrId(list.get(i).getId());
				order.setmId(address.getmId());
				List<Order> orders = orderMapper.selectOrderBymAddressId(order);
				if (orders.size() > 0) {
					list.get(i).setEasybuy(orders.size());
				}else{
					list.get(i).setEasybuy(0);
				}
			}
		}
		
		return list;
	}

	@Transactional
	public BhResult addMember(MemberUser memberUser) throws Exception {
		BhResult bhResult = null;
		String username = memberUser.getUsername();
		String pwd = memberUser.getPassword();
		String phone = memberUser.getPhone();

		// 1为普通会员,2为商家
		// String type = map.get("type")
		String salt = "";
		// String im = map.get("im");

		Member member = new Member();

		if (StringUtils.isEmpty(username)) {
			bhResult = new BhResult(400, "用户名不能为空", null);
		} else {
			member.setUsername(username);
			
		}
		if (StringUtils.isEmpty(pwd)) {
			bhResult = new BhResult(400, "密码不能为空", null);
		} else {
			String first = MD5Util1.encode(pwd);
			salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			member.setPassword(second);
			member.setSalt(salt);
		}
		if (StringUtils.isEmpty(phone)) {
			bhResult = new BhResult(400, "手机号不能为空", null);
		} else {
			Member member2 = new Member();
			member2.setPhone(phone);
			List<Member> member3 = memberMpper.selecMemberByPhone(member2);
			if (member3.size() < 1) {
				member.setPhone(phone);
				member.setIm("0");
				member.setType(1);

				memberMpper.insertSelective(member);

				memberUser.setmId(member.getId());
				if (memberUser.getAddtime() !=null) {
					memberUser.setAddtime(new Date());
				}
				memberUser.setNote("这是平台添加用户");
				int row = memberUserMapper.insertSelective(memberUser);
				if (row == 1) {
					bhResult = new BhResult(200, "新增会员成功", null);
				} else {
					bhResult = new BhResult(400, "新增会员失败了", null);
				}
			} else {
				bhResult = new BhResult(400, "该手机号已被注册过", null);
			}
		}
		return bhResult;
	}

	public BhResult updateMember(MemberUser memberUser) throws Exception {
		BhResult bhResult = null;
		Member member = new Member();
		member.setId(memberUser.getmId());
		if ((memberUser.getUsername() !=null ) && (memberUser.getUsername() !="")) {
			member.setUsername(memberUser.getUsername());
		}
		if ((memberUser.getPassword() !=null ) && (memberUser.getPassword() !="")) {
			String first = MD5Util1.encode(memberUser.getPassword());
			String salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			member.setPassword(second);
			member.setSalt(salt);
		}if ((memberUser.getPhone() !=null ) &&(memberUser.getPhone() !="")) {
			member.setPhone(memberUser.getPhone());
			Member member2 = new Member();
			member2.setPhone(memberUser.getPhone());
			List<Member> member3 = memberMpper.selecMemberByPhone(member2);
			if (member3.size() < 1) {
				member.setPhone(memberUser.getPhone());
				int row1 = memberMpper.updateByPrimaryKeySelective(member);
				memberUserMapper.updateByPrimaryKeySelective(memberUser);
				if (row1 == 1) {
					bhResult = new BhResult(200, "编辑成功", null);
				} else {
					bhResult = new BhResult(400, "编辑失败", null);
				}
			} else {
				Member m = new Member();
				m.setId(memberUser.getmId());
				m.setPhone(memberUser.getPhone());
				List<Member> list = memberMpper.selectByParam(m);
				if (list.size() > 0) {
					member.setPhone(memberUser.getPhone());
					int row1 = memberMpper.updateByPrimaryKeySelective(member);
					memberUserMapper.updateByPrimaryKeySelective(memberUser);
					if (row1 == 1) {
						bhResult = new BhResult(200, "编辑成功", null);
					} else {
						bhResult = new BhResult(400, "编辑失败", null);
					}
				}else{
					bhResult = new BhResult(400, "该手机号已被注册过", null);
				}
			
			}
		}else{
			if (StringUtils.isEmpty(memberUser.getEmail())) {
				memberUser.setEmail(null);
			}if (StringUtils.isEmpty(memberUser.getWxCode())) {
				memberUser.setWxCode(null);
			}
			 memberUserMapper.updateByPrimaryKeySelective(memberUser);
			 memberMpper.updateByPrimaryKeySelective(member);
			 bhResult = new BhResult(200, "编辑成功", null);
		}
		
		
		
		
		return bhResult;
	}
	
	
	public List<MemberUser> selectMemberUserByFullName(MemberUser memberUser) throws Exception{
		List<MemberUser> list = new ArrayList<>();
		list = memberUserMapper.selectByFullName(memberUser);
		return list;
	}
	
	public List<Member> selectMember(Member member) throws Exception{
		List<Member> list = memberMpper.selectByParam(member);
		return list;
	}
	
	public ShowTiaoZheng selecttiaozheng(MemberUser memberUser) throws Exception{
		ShowTiaoZheng showTiaoZheng = new ShowTiaoZheng();
		showTiaoZheng.setmId(memberUser.getmId());
		MemberUser memberUser2 = memberUserMapper.selectByPrimaryKey(memberUser.getmId());
		if (memberUser2 !=null) {
			showTiaoZheng.setBeanNum(memberUser2.getPoint());
			double balance = (double) memberUser2.getBalance()/100;
			showTiaoZheng.setCurrentBalance(RegExpValidatorUtils.formatdouble(balance));
			showTiaoZheng.setNote(memberUser2.getNote());
			Member member = memberMpper.selectByPrimaryKey(memberUser.getmId());
			showTiaoZheng.setUsername(URLDecoder.decode(member.getUsername(), "utf-8"));
		}
		return showTiaoZheng;
	}
	
	
	public int updatetiaozheng(ShowTiaoZheng showTiaoZheng) throws Exception{
		int row =0;
		
		MemberUser memberUser = new MemberUser();
		memberUser.setmId(showTiaoZheng.getmId());
		if (showTiaoZheng.getCurrentBalance() !=null) {
			Double balance = showTiaoZheng.getCurrentBalance()*100;
			Long balance1 =(Long)balance.longValue();
			memberUser.setBalance(balance1);
		}
		Integer point = showTiaoZheng.getBeanNum();
		if (point !=null) {
			memberUser.setPoint(point);
		}
		if (showTiaoZheng.getNote() !=null) {
			memberUser.setNote(showTiaoZheng.getNote());
		}
		row = memberUserMapper.updateByParams(memberUser);
		return row;
	}
}
