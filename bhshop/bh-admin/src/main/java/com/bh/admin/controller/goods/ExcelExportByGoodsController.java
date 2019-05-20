package com.bh.admin.controller.goods;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.ExportGoods;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.service.ExcelExportByGoodsService;
import com.bh.admin.util.ExcelFileGenerator;
import com.bh.admin.util.GoodsExcelUtil;
import com.bh.admin.util.JedisUtil;
import com.mysql.jdbc.StringUtils;

@Controller
public class ExcelExportByGoodsController {
	@Autowired
	private ExcelExportByGoodsService excelExportByGoodsService;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
	
	   
	   
	   /**
	    * <p>Description: 导出excel</p>
	    *  @author scj  
	    *  @date 2018年5月2日
	    */
	   @RequestMapping(value="/excelExport1",method = RequestMethod.GET)  
	   public void excelExport1(String tagName,String name,String catid,String status,String saleType,String startPrice,
			   
			   String endPrice,String topicType,String id,String skuNo,String jdSkuNo,String isJd,String token, HttpServletRequest request,  
	                             HttpServletResponse response) throws Exception{  
		   try {
			 	
				
			    JedisUtil jedisUtil = JedisUtil.getInstance();
				JedisUtil.Strings strings = jedisUtil.new Strings();
				String userJson = strings.get(token);
				Map mapOne = JSON.parseObject(userJson, Map.class);
			    Integer userId = (Integer)mapOne.get("userId");
			    if(org.apache.commons.lang3.StringUtils.isBlank(String.valueOf(userId))){
			       userId  = 1;
				}
			   
				Object sId = mapOne.get("shopId");
				Integer shopId = 1;
				if (sId != null) {
					shopId = (Integer) sId;
				}
			    GoodsOperLog goodsOperLog = new GoodsOperLog();
				goodsOperLog.setOpType("商家商品导出");
				goodsOperLog.setUserId(userId.toString());
				goodsOperLog.setOpTime(new Date());
				String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
				goodsOperLog.setAdminUser(userName);
				goodsOperLogMapper.insertSelective(goodsOperLog);
		       ArrayList<String> fieldName =new ArrayList<>();  
		       fieldName.add("商品ID"); //0商品ID
		       fieldName.add("父级商品ID");  //1父级商品ID
		       fieldName.add("商品名称");  //2商品名称
		       
		       fieldName.add("skuNo");//3
		       fieldName.add("商品(sku)名称");//4
		       fieldName.add("属性一");//5
		       fieldName.add("属性二");//6
		       fieldName.add("属性三");//7
		       fieldName.add("属性四");//8
		       fieldName.add("属性五");//9
		       fieldName.add("商品分类");//10
		       fieldName.add("市场价");//11
		       fieldName.add("单买价");//12
		       fieldName.add("协议价");//13
		       fieldName.add("团购价");//14
		       fieldName.add("进货价");//15
		       fieldName.add("用户购买价");//16
		       fieldName.add("库存");//17
		       fieldName.add("已售数量");//18
		       fieldName.add("销量");//19
		       fieldName.add("京东SKU编码");//20
		        String isHotShop=null;
				String isNewShop=null;
				if(!StringUtils.isNullOrEmpty(tagName)) {
					 if(tagName.equals("1")) {
							isNewShop="1";
						   }
						if(tagName.equals("2")) {
							isHotShop="1";
						}
				}
				String startPrices=null;
				String endPrices=null;
				if(!StringUtils.isNullOrEmpty(startPrice)) {
					Integer startPrice1=Integer.valueOf(startPrice)*100;
					startPrices=startPrice1.toString();
				}
			   if(!StringUtils.isNullOrEmpty(endPrice)) {
				   Integer endPrice1=Integer.valueOf(endPrice)*100;
				   endPrices=endPrice1.toString();
			   }
			   
		       ArrayList<ArrayList<String>> fieldData = 
		    		   excelExportByGoodsService.getData(shopId, name, catid,isHotShop,isNewShop,
						saleType, status, startPrices, endPrices, topicType, id, skuNo, jdSkuNo,isJd);
		       ExcelFileGenerator generator = new ExcelFileGenerator(fieldName,fieldData);  
		       OutputStream os = response.getOutputStream();
		       Date date = new Date();
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       String filename = "商品数据excel（"+ df.format(date).replace(":", "_") +"）.xls";  
		       filename = ExcelFileGenerator.processFileName(request,filename);  
		       //可以不加，但是保证response缓冲区没有任何数据，开发时建议加上  
		       response.reset();  
		       response.setContentType("application/vnd.ms-excel;charset=utf-8");  
		       response.setHeader("Content-disposition", "attachment;filename="+filename);  
		       response.setBufferSize(1024);  
		       /**将生成的excel报表，写到os中*/  
		      
				
		       generator.expordExcel(os);  
		  } catch (Exception e) {
			  e.printStackTrace();
			// TODO: handle exception
		  }
		  
		  
	   }
	   
	   
	   /**
	    * <p>Description: 导出excel</p>
	    *  @author scj  
	    *  @date 2018年5月2日
	    */
	   @RequestMapping(value="/excelExportOne",method = RequestMethod.GET)  
	   public void excelExportOne(String tagName,String name,String catid,String status,String saleType,String startPrice,
			   String endPrice,String topicType,String id,String skuNo,String jdSkuNo,String isJd,String token, HttpServletRequest request,  
	           HttpServletResponse response) throws Exception{  
		   try {
			    JedisUtil jedisUtil = JedisUtil.getInstance();
				JedisUtil.Strings strings = jedisUtil.new Strings();
				String userJson = strings.get(token);
				Map mapOne = JSON.parseObject(userJson, Map.class);
			    Integer userId = (Integer)mapOne.get("userId");
			    if(org.apache.commons.lang3.StringUtils.isBlank(String.valueOf(userId))){
			       userId  = 1;
				}
				Object sId = mapOne.get("shopId");
				Integer shopId = 1;
				if (sId != null) {
					shopId = (Integer) sId;
				}
			    GoodsOperLog goodsOperLog = new GoodsOperLog();
				goodsOperLog.setOpType("商家商品导出");
				goodsOperLog.setUserId(userId.toString());
				goodsOperLog.setOpTime(new Date());
				String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
				goodsOperLog.setAdminUser(userName);
				goodsOperLogMapper.insertSelective(goodsOperLog);
				
		        String isHotShop=null;
				String isNewShop=null;
				if(!StringUtils.isNullOrEmpty(tagName)) {
					 if(tagName.equals("1")) {
							isNewShop="1";
						   }
						if(tagName.equals("2")) {
							isHotShop="1";
						}
				}
				String startPrices=null;
				String endPrices=null;
				if(!StringUtils.isNullOrEmpty(startPrice)) {
					Integer startPrice1=Integer.valueOf(startPrice)*100;
					startPrices=startPrice1.toString();
				}
			   if(!StringUtils.isNullOrEmpty(endPrice)) {
				   Integer endPrice1=Integer.valueOf(endPrice)*100;
				   endPrices=endPrice1.toString();
			   }
			   
			   List<ExportGoods> list = excelExportByGoodsService.exportListNew(shopId, name, catid,isHotShop,isNewShop,
						saleType, status, startPrices, endPrices, topicType, id, skuNo, jdSkuNo,isJd);
			   ServletOutputStream out = response.getOutputStream();
			   
               //这里设置的文件格式是application/x-excel
			   Date date = new Date();
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   String filename = "商品数据excel（"+ df.format(date).replace(":", "_") +"）.xls";  
               response.setContentType("application/x-excel");
               response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));
               ServletOutputStream outputStream = response.getOutputStream();
			   GoodsExcelUtil.exportGoodsExcel(list, out);
			   if(outputStream != null){
				   outputStream.close();
			   }
			   System.out.println("导出完毕！");
		  } catch (Exception e) {
			  e.printStackTrace();
			// TODO: handle exception
		  }
	   }
	   
	   /**
	    * <p>Description: 导出excel</p>
	    *  @author scj  
	    *  @date 2018年5月2日
	    */
	   @RequestMapping(value="/excelExportTwo",method = RequestMethod.GET)  
	   public void excelExportTwo(String tagName,String name,String catid,String status,String saleType,String startPrice,
			   String endPrice,String topicType,String id,String skuNo,String jdSkuNo,String isJd,String token, HttpServletRequest request,  
	           HttpServletResponse response) throws Exception{  
		   try {
			    /*JedisUtil jedisUtil = JedisUtil.getInstance();
				JedisUtil.Strings strings = jedisUtil.new Strings();
				String userJson = strings.get(token);
				Map mapOne = JSON.parseObject(userJson, Map.class);
			    Integer userId = (Integer)mapOne.get("userId");
			    if(org.apache.commons.lang3.StringUtils.isBlank(String.valueOf(userId))){
			       userId  = 1;
				}
				Object sId = mapOne.get("shopId");*/
			   	Integer userId  = 1;
				Integer shopId = 1;
				/*if (sId != null) {
					shopId = (Integer) sId;
				}*/
			    GoodsOperLog goodsOperLog = new GoodsOperLog();
				goodsOperLog.setOpType("商家商品导出");
				goodsOperLog.setUserId(userId.toString());
				goodsOperLog.setOpTime(new Date());
				String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
				goodsOperLog.setAdminUser(userName);
				goodsOperLogMapper.insertSelective(goodsOperLog);
				
		        String isHotShop=null;
				String isNewShop=null;
				if(!StringUtils.isNullOrEmpty(tagName)) {
					 if(tagName.equals("1")) {
							isNewShop="1";
						   }
						if(tagName.equals("2")) {
							isHotShop="1";
						}
				}
				String startPrices=null;
				String endPrices=null;
				if(!StringUtils.isNullOrEmpty(startPrice)) {
					Integer startPrice1=Integer.valueOf(startPrice)*100;
					startPrices=startPrice1.toString();
				}
			   if(!StringUtils.isNullOrEmpty(endPrice)) {
				   Integer endPrice1=Integer.valueOf(endPrice)*100;
				   endPrices=endPrice1.toString();
			   }
			   
			   List<ExportGoods> list = excelExportByGoodsService.exportListNew(shopId, name, catid,isHotShop,isNewShop,
						saleType, status, startPrices, endPrices, topicType, id, skuNo, jdSkuNo,isJd);
			   Date date = new Date();
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       String fileName = "商品数据excel（"+ df.format(date).replace(":", "_") +"）";  
		       //fileName = ExcelFileGenerator.processFileName(request,fileName);  
			   String[] colTitle = GoodsExcelUtil.TdTitle();
			   String pageTitle = "滨惠商品列表";
			   String path = "C:/Users/pc/Downloads";
			   GoodsExcelUtil.writer(path, fileName, "xlsx", list, pageTitle, colTitle);
			   System.out.println("导出完毕！");
		  } catch (Exception e) {
			  e.printStackTrace();
			// TODO: handle exception
		  }
	   }
}
