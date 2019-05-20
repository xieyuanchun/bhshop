package com.bh.admin.service.impl;


import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.AspUserGuessMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.AspUserGuess;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.AspGuessService;
import com.bh.admin.service.impl.AspGuessImpl;
import com.bh.admin.util.ExcelFileGenerator;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.pojo.OrderShop;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;



@Service
public class AspGuessImpl implements AspGuessService{
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = LoggerFactory.getLogger(AspGuessImpl.class);
	@Autowired
	private AspUserGuessMapper aspUserGuessMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberMapper memberMapper;
	
	/**
	 * @Description: 亚运会中奖记录导出
	 * @author xieyc
	 * @throws ParseException 
	 * @throws Exception 
	 * @date 2018年8月15日 上午10:53:34
	 */
	public void excelExport(AspUserGuess aspUserGuess1, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<String> fieldName = new ArrayList<>();
		fieldName.add("用户id");
		fieldName.add("申请时间");
		fieldName.add("第一次竞猜金牌数");
		fieldName.add("第二次竞猜金牌数");
		fieldName.add("消费金额");
		fieldName.add("退还金额");
		fieldName.add("开户行");
		fieldName.add("银行卡号");
		fieldName.add("持卡人");
		fieldName.add("银行手机预留号码");
		fieldName.add("审核状态");
		fieldName.add("是否转过账");
		fieldName.add("审核人");
		fieldName.add("备注");
		
		
		ArrayList<ArrayList<String>> fieldData = new ArrayList<>();
		List<AspUserGuess> list = aspUserGuessMapper.selectAll(aspUserGuess1);

		for (AspUserGuess aspUserGuess : list) {
			ArrayList<String> array = new ArrayList<>();
			array.add(0, aspUserGuess.getmId() + ""); // 用户id
			if(aspUserGuess.getApplyTime()!=null) {
				array.add(1, sf.format(aspUserGuess.getApplyTime()) + ""); // 申请时间
			}else {
				array.add(1,""); // 申请时间
			}
			array.add(2, aspUserGuess.getGuessOne() + ""); // 第一次竞猜金牌数
			array.add(3, aspUserGuess.getGuessTwo() + ""); // 第二次竞猜金牌数
			array.add(4, MoneyUtil.IntToDouble(aspUserGuess.getRetPrice()) + ""); // 消费金额
			array.add(5, MoneyUtil.IntToDouble(aspUserGuess.getBackPrice()) + ""); // 退还金额
			array.add(6, aspUserGuess.getBankName() + ""); // 开户行
			array.add(7, aspUserGuess.getBankCardNo() + ""); // 银行卡号
			array.add(8, aspUserGuess.getBankCardOwner() + ""); //公司名字
			array.add(9, aspUserGuess.getPhone() + ""); // 银行手机预留号码
			if (aspUserGuess.getState() == 0) {
				array.add(10, "审核中"); // 审核状态
			} else if (aspUserGuess.getState() == 1) {
				array.add(10, "审核成功"); // 审核状态
			} else {
				array.add(10, "审核失败"); // 审核状态
			}
			if (aspUserGuess.getIsTransfer() == 0) {
				array.add(11, "未转账");// 是否转账
			} else {
				array.add(11, "已转账");// 是否转账
			}
			array.add(12, aspUserGuess.getAuditor() + ""); // 审核人
			array.add(13, aspUserGuess.getNote() + ""); // 备注
			fieldData.add(array);
		}
		ExcelFileGenerator generator = new ExcelFileGenerator(fieldName, fieldData);
		OutputStream os = response.getOutputStream();
		Date date = new Date();
		String filename = " 亚运会中奖记录excel（" + sf.format(date).replace(":", "_") + "）.xls";
		filename = ExcelFileGenerator.processFileName(request, filename);
		// 可以不加，但是保证response缓冲区没有任何数据，开发时建议加上
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		response.setBufferSize(1024);
		/** 将生成的excel报表，写到os中 */
		generator.expordExcel(os);

	}
	
	/**
	 * 
	 * @Description: 获取提现列表的信息
	 * @author fanjh
	 * @date 2018年8月24日 上午10:28:40
	 */
	public PageBean<AspUserGuess> getWinUser(AspUserGuess aspUserGuess){
		 List<AspUserGuess> aspUserGuessList=null;
		 PageHelper.startPage(Integer.parseInt(aspUserGuess.getCurePage()),Integer.parseInt(aspUserGuess.getPageSize()) , true);
		 aspUserGuessList = aspUserGuessMapper.selectAll(aspUserGuess);
		 
		 if(aspUserGuessList.size()>0) {
			 for (AspUserGuess aspUserGuess2 : aspUserGuessList) {
				Member member=memberMapper.selectByPrimaryKey(aspUserGuess2.getmId());
				try {
					aspUserGuess2.setUserName(URLDecoder.decode(member.getUsername(), "UTF-8"));
					aspUserGuess2.setRetMoney(MoneyUtil.IntToDouble(aspUserGuess2.getRetPrice()));
					aspUserGuess2.setBackMoney(MoneyUtil.IntToDouble(aspUserGuess2.getBackPrice()));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }
		 PageBean<AspUserGuess> pageBean = new PageBean<>(aspUserGuessList);
		 return pageBean;
	}
	
	
	/**
	 * 
	 * @Description: 获取中奖信息
	 * @author fanjh
	 * @date 2018年8月24日 上午10:28:40
	 */
	public PageBean<AspUserGuess> getWin(AspUserGuess aspUserGuess){
		 List<AspUserGuess> aspUserGuessList=null;
		 PageHelper.startPage(Integer.parseInt(aspUserGuess.getCurePage()),Integer.parseInt(aspUserGuess.getPageSize()) , true);
		 aspUserGuessList = aspUserGuessMapper.selectAll1(aspUserGuess);
		 if(aspUserGuessList.size()>0) {
			 for (AspUserGuess aspUserGuess2 : aspUserGuessList) {
				Member member=memberMapper.selectByPrimaryKey(aspUserGuess2.getmId());
				try {
					aspUserGuess2.setUserName(URLDecoder.decode(member.getUsername(), "UTF-8"));
					aspUserGuess2.setRetMoney(MoneyUtil.IntToDouble(aspUserGuess2.getRetPrice()));
					aspUserGuess2.setBackMoney(MoneyUtil.IntToDouble(aspUserGuess2.getBackPrice()));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }
		 PageBean<AspUserGuess> pageBean = new PageBean<>(aspUserGuessList);
		 return pageBean;
	}
	
	
	/**
	 * @Description: 审核
	 * @author fanjh
	 * @date 2018年8月24日 上午10:28:40
	 */
	
	public int updateStatus(AspUserGuess aspUserGuess, Integer userId) {
		String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
		if (aspUserGuess.getState()!=null) {//审核
			aspUserGuess.setAuditTime(new Date(JedisUtil.getInstance().time()));
			aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));
			aspUserGuess.setAuditor(userName);
		}
		if(aspUserGuess.getIsTransfer()!=null) {
			aspUserGuess.setConfirmTime(new Date(JedisUtil.getInstance().time()));
			aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));
			aspUserGuess.setTransferPeople(userName);
		}
		return aspUserGuessMapper.updateByPrimaryKeySelective(aspUserGuess);
	}
	
	

	
}
