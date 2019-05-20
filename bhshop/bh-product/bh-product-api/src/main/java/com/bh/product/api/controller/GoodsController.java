package com.bh.product.api.controller;

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.Address;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.pojo.*;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.pojo.Order;
import com.bh.product.api.service.*;
import com.bh.product.api.util.JedisUtil;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.MBusEntity;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.AddressUtils;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.control.file.upload;
import net.sf.json.JSONArray;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
    @Autowired
	private GoodsSkuService service;
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private GoodsPropertyService goodsPropertyService;
	@Autowired
	private GoodsModelService goodsModelService;
	@Autowired
	private GoodsShopCategoryService goodsShopCategoryService;
	@Autowired
	private GoodsTagService goodsTagService;
	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private OrderMapper orderMapper;
	/**
	 * SCJ-20170912-02 批量删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public BhResult batchDelete(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			int row = goodsService.batchDelete(id);
			if (row == 0) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else if (row == 999) {
				result = new BhResult(BhResultEnum.BATCH_DELETE_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.DELETE_SUCCESS, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	// 单文件上传
	/*
	 * @Value(value = "${oss}") private String oss;
	 * 
	 * @RequestMapping("/upload")
	 * 
	 * @ResponseBody public BhResult upload(String localFilePath,String key){
	 * //oss BhResult res = null; upload myupload= new upload(); boolean
	 * bl=myupload.singleupload(oss,localFilePath,key); if(bl){ res = new
	 * BhResult(200, "操作成功 ",null); }else{ res = new BhResult(400, "操作失败 ",
	 * null); } return res;
	 * 
	 * }
	 */

	/**
	 * 测试修改文件名
	 * 
	 * @param localFilePath
	 * @return
	 */
	@RequestMapping(value = "/changeFileName", method = RequestMethod.GET)
	@ResponseBody
	public BhResult changeFileName(String localFilePath) {
		BhResult result = null;
		String folderPath = localFilePath.substring(0, localFilePath.lastIndexOf("/"));
		String fileJpg = localFilePath.substring(localFilePath.lastIndexOf("/") + 1, localFilePath.length());
		File file = new File(folderPath);
		String dirPath = file.getAbsolutePath();// 目录路径
		if (file.isDirectory()) {
			File[] files = file.listFiles();// 获取此目录下的文件列表
			for (File fileFrom : files) {
				String fromFile = fileFrom.getName();// 得到单个文件名
				if (fromFile.endsWith(".jpg") || fromFile.endsWith(".png")) {
					// fromFile = fromFile.substring(0,
					// fromFile.lastIndexOf(".jpg"));
					if (fromFile.equals(fileJpg)) {
						fromFile = "haha.jpg";
						String toFileName = dirPath + "/" + fromFile;
						File toFile = new File(toFileName);
						if (fileFrom.exists() && !toFile.exists()) {
							// 开始改名
							fileFrom.renameTo(toFile);
							result = new BhResult(200, "更改成功", toFileName);
						}
					}
				}
			}

		}
		return result;
	}

	/**
	 * 根据id获取商品详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/selectBygoodsId")
	@ResponseBody
	public BhResult selectBygoodsId(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Goods goods = goodsService.selectBygoodsId(Integer.parseInt(id));
			if (goods != null) {
				result = new BhResult(200, "成功", goods);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171102-01 根据客户端ip地址定位
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getAddressByIp")
	@ResponseBody
	public BhResult getAddressByIp(HttpServletRequest request) {
		BhResult result = null;
		try {
			String address = "";
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (member != null) { // 登录情况下
				address = goodsService.getAddressById(member.getId());
				if (address == null) { // 当前用户无地址
					AddressUtils addressUtils = new AddressUtils();
					String ip = "219.136.134.157";
					//String ip = addressUtils.getIpAddr(request);
					address = addressUtils.getAddresses("ip=" + ip, "utf-8");
				}
			} else {
				AddressUtils addressUtils = new AddressUtils();
				// 测试ip广东省广州市
				String ip = "219.136.134.157";
				// String ip = addressUtils.getIpAddr(request);
				address = addressUtils.getAddresses("ip=" + ip, "utf-8");
			}
			if (address != null) {
				result = new BhResult(BhResultEnum.GAIN_SUCCESS, address);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}

		} catch (Exception e) {
			result = new BhResult(BhResultEnum.LOCATION_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 商家后台商品列表(按商家Id+条件查询+分页)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/selectByShopid")
	@ResponseBody
	public BhResult selectByShopid(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String name = map.get("name"); // 查询条件
			String catId = map.get("catid");// 分类查询id
			String status = map.get("status");// '商品状态 0正常 1已删除 2下架
												// 3申请上架4拒绝5上架',
			String saleType = map.get("saleType"); // '销售模式 1普通模式 2只拼团 3单卖
													// 4拼团单卖皆可'
			String currentPage = map.get("currentPage");// 当前第几页

			// 价格查询
			String startPrice = map.get("startPrice");
			String endPrice = map.get("endPrice");
			String topicType = map.get("topicType");
			String id = map.get("id");
			String skuNo = map.get("skuNo");
			String jdSkuNo = map.get("jdSkuNo");

			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			PageBean<Goods> msg = goodsService.selectByShopid(shopId, name, currentPage, Contants.PAGE_SIZE, catId,
					saleType, status, startPrice, endPrice, topicType, id, skuNo, jdSkuNo);
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 图片的上传
	 */
	/*
	 * @Value(value = "${oss}") private String oss;
	 * 
	 * @RequestMapping("/uploadGoodsImage")
	 * 
	 * @ResponseBody public BhResult uploadGoodsImage(String
	 * localFilePath,String key, int goodsId) { //oss BhResult res = null; try {
	 * int row = goodsService.uploadGoodsImage(oss, localFilePath, key,
	 * goodsId); if(row == 0 ){ res = new BhResult(400, "操作失败 ",null); }else{
	 * res = new BhResult(200, "操作成功 ", null); } } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return res; }
	 */

	/**
	 * SCJ-20170929-01 多图片上传
	 */
	@Value(value = "${oss}")
	private String oss;

	@RequestMapping("/uploadImage")
	@ResponseBody
	public BhResult uploadImage(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult result = null;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isEmpty()) {
				String realName = null;
				String uuidName = null;
				StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
				StringBuffer key = new StringBuffer();

				try {

					// 文件原来的名称
					realName = files[i].getOriginalFilename();
					// 得到这个文件的uuidname
					uuidName = this.getUUIDFileName(files[i].getOriginalFilename());

					String roolPath = request.getSession().getServletContext().getRealPath("/");
					// 得到文件的输入流
					InputStream in = new BufferedInputStream(files[i].getInputStream());
					// 获得文件的输出流
					OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(roolPath, uuidName)));

					IOUtils.copy(in, out);
					in.close();
					out.close();

					key.append("goods/");
					key.append(uuidName);

					upload myupload = new upload();
					String localFilePath = roolPath + uuidName;
					boolean bl = myupload.singleupload(oss, localFilePath, key.toString());
					realPath.append("goods/");
					realPath.append(uuidName);

					if (bl) {
						result = new BhResult(200, "上传成功", realPath.toString());
					} else {
						result = new BhResult(400, "上传失败", null);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库操作失败!");
					LoggerUtil.getLogger().error(e.getMessage());

					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * 机器人头像上传 xieyc
	 */
	@RequestMapping("/uploadRobotHead")
	@ResponseBody
	public BhResult uploadRobotHead(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult result = null;
		for (int i = 0; i < files.length; i++) {

			if (!files[i].isEmpty()) {
				String realName = null;
				String uuidName = null;
				StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
				StringBuffer key = new StringBuffer();

				try {

					// 文件原来的名称
					realName = files[i].getOriginalFilename();
					// 得到这个文件的uuidname
					uuidName = this.getUUIDFileName(files[i].getOriginalFilename());

					String roolPath = request.getSession(false).getServletContext().getRealPath("/");
					// 得到文件的输入流
					InputStream in = new BufferedInputStream(files[i].getInputStream());
					// 获得文件的输出流
					OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(roolPath, uuidName)));

					IOUtils.copy(in, out);
					in.close();
					out.close();

					key.append("robotHead/");
					key.append(uuidName);

					upload myupload = new upload();
					String localFilePath = roolPath + uuidName;
					boolean bl = myupload.singleupload(oss, localFilePath, key.toString());
					realPath.append("robotHead/");
					realPath.append(uuidName);

					if (bl) {
						result = new BhResult(200, "上传成功", realPath.toString());
					} else {
						result = new BhResult(400, "上传失败", null);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库操作失败!");
					LoggerUtil.getLogger().error(e.getMessage());

					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * SCJ-20180322-01 多图片上传
	 */
	@RequestMapping("/uploadImageSort")
	@ResponseBody
	public BhResult uploadImageSort(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult result = null;
		List<String> list = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isEmpty()) {
				String realName = null;
				String uuidName = null;
				StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
				StringBuffer key = new StringBuffer();

				try {

					// 文件原来的名称
					realName = files[i].getOriginalFilename();
					// 得到这个文件的uuidname
					uuidName = this.getUUIDFileName(files[i].getOriginalFilename());

					String roolPath = request.getSession(false).getServletContext().getRealPath("/");
					// 得到文件的输入流
					InputStream in = new BufferedInputStream(files[i].getInputStream());
					// 获得文件的输出流
					OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(roolPath, uuidName)));

					IOUtils.copy(in, out);
					in.close();
					out.close();

					key.append("goods/");
					key.append(uuidName);

					upload myupload = new upload();
					String localFilePath = roolPath + uuidName;
					boolean bl = myupload.singleupload(oss, localFilePath, key.toString());
					realPath.append("goods/");
					realPath.append(uuidName);

					if (bl) {
						//result = new BhResult(200, "上传成功", realPath.toString());
						list.add(realPath.toString());
					} else {
						result = new BhResult(400, "上传失败", null);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库操作失败!");
					LoggerUtil.getLogger().error(e.getMessage());

					e.printStackTrace();
				}
			}
		}
		result = new BhResult(200, "上传成功", list);
		return result;
	}

	/**
	 * SCJ-20171026-01 商品详细描述图片上传回调
	 * 
	 * @param files
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/uploadDescImage")
	@ResponseBody
	public BhResult uploadDescImage(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult result = null;
		boolean bl = false;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isEmpty()) {
				String realName = null;
				String uuidName = null;
				StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
				StringBuffer key = new StringBuffer();
				try {

					// 文件原来的名称
					realName = files[i].getOriginalFilename();
					// 得到这个文件的uuidname
					uuidName = this.getUUIDFileName(files[i].getOriginalFilename());

					String roolPath = request.getSession(false).getServletContext().getRealPath("/");
					// 得到文件的输入流
					InputStream in = new BufferedInputStream(files[i].getInputStream());
					// 获得文件的输出流
					OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(roolPath, uuidName)));

					IOUtils.copy(in, out);
					in.close();
					out.close();

					key.append("goods/");
					key.append(uuidName);

					upload myupload = new upload();
					String localFilePath = roolPath + uuidName;
					bl = myupload.singleupload(oss, localFilePath, key.toString());
					realPath.append("goods/");
					realPath.append(uuidName);

					buffer.append(realPath.toString() + ",");
				} catch (Exception e) {
					result = BhResult.build(500, "数据库操作失败!");
					LoggerUtil.getLogger().error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		String[] str = buffer.toString().substring(0, buffer.toString().length() - 1).split(",");
		if (bl) {
			result = new BhResult(200, "上传成功", str);
		} else {
			result = new BhResult(400, "上传失败", null);
		}
		return result;
	}

	/**
	 * SCJ-20170929-02 设置商品图片轮播图保存
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/insertGoodsImage", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertGoodsImage(@RequestBody Map<String, Object> map) {
		BhResult result = null;
		try {
			String goodsId = (String) map.get("goodsId");
			JSONArray url = JSONArray.fromObject(map.get("url"));
			// 图片保存到数据库
			int row = goodsService.insertGoodsImage(goodsId, url);
			if (row == 0) {
				result = new BhResult(400, "请求失败", null);
			} else {
				result = new BhResult(200, "添加成功", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171010-07 商品图片轮播图的删除
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/deleteGoodsImage", method = RequestMethod.POST)
	@ResponseBody
	public BhResult deleteGoodsImage(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {

			String id = map.get("id");
			int row = goodsService.imageBatchDelete(id);
			if (row == 0) {
				result = BhResult.build(400, "请求失败！", null);
			} else {
				result = BhResult.build(200, "删除成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171010-05 商品图片轮播图详情
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/goodsImageDetails", method = RequestMethod.POST)
	@ResponseBody
	public BhResult goodsImageDetails(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId");
			List<GoodsImage> goodsImage = goodsService.goodsImageDetails(goodsId);
			if (goodsImage != null) {
				result = new BhResult(200, "获取成功", goodsImage);
			} else {
				result = new BhResult(400, "获取失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 新增商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/insertGoods", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertGoods(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}

			String name = (String) map.get("name");
			String title = (String) map.get("title");
			String modelId = (String) map.get("modelId");
			String catId = (String) map.get("catId");
			String shopCatId = (String) map.get("shopCatId");
			String brandId = (String) map.get("brandId");
			String sellPrice = (String) map.get("sellPrice");
			String marketPrice = (String) map.get("marketPrice");
			String storeNums = (String) map.get("storeNums");
			String image = (String) map.get("image");
			String deliveryPrice = (String) map.get("deliveryPrice");
			String isShopFlag = (String) map.get("isShopFlag");// 店铺内是否推荐0否，1是
			String isNew = (String) map.get("isNew");// 是否最新产品0否，1是
			String refundDays = (String) map.get("refundDays");// -1不限时间退货
																// 0不接受退货
																// 其它表示接受退货天数'
			String publicImage = (String) map.get("publicImage");// 宣传图
			String saleType = (String) map.get("saleType");// '销售模式 1普通模式 2只拼团
															// 3单卖 4拼团单卖皆可'
			String teamNum = (String) map.get("teamNum");// ''拼团人数',
			String teamEndTime = (String) map.get("teamEndTime");// '开团截止时间'
			String isPromote = (String) map.get("isPromote");// '是否促成团(-1 否 0是',
			String timeUnit = (String) map.get("timeUnit");// '持续时间单位：1-分钟，2-小时，3-天',
			String teamPrice = (String) map.get("teamPrice"); // 拼团价
			String isCreate = (String) map.get("isCreate"); // '是否支持系统发起拼单 -1否
															// 0是',
			String isJd = (String) map.get("isJd"); // 是否京东商品
			String desc = "";
			String topicType = (String) map.get("topicType");
			String tagIds = (String) map.get("tagIds");
			String catIdOne = (String) map.get("catIdOne");
			String catIdTwo = (String) map.get("catIdTwo");
			Object descs = map.get("desc");
			if (descs != null && descs != "") {
				desc = (String) descs;
			}
			String isPopular = (String) map.get("isPopular");
			String appintroduce = "";
			Object appintroduces = map.get("appintroduce");
			if (appintroduces != null && appintroduces != "") {
				appintroduce = (String) appintroduces;
			}
			JSONArray list = JSONArray.fromObject(map.get("list"));
			// JSONArray url = JSONArray.fromObject(map.get("url"));
			// 图片保存到数据库
			int row = goodsService.selectinsert(name, title, modelId, catId, shopCatId, brandId, sellPrice, marketPrice,
					storeNums, desc, image, list, deliveryPrice, isShopFlag, isNew, refundDays, publicImage, saleType,
					teamNum, teamEndTime, isPromote, timeUnit, teamPrice, isCreate, isJd, appintroduce, topicType,
					shopId, tagIds,isPopular, catIdOne, catIdTwo);
			if (row == 0) {
				result = new BhResult(400, "商品添加失败", null);
			} else {
				result = new BhResult(200, "商品添加成功", row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/*
	 * 测试单图片上传
	 * 
	 * @Value(value = "${oss}") private String oss;
	 * 
	 * @RequestMapping("/uploadImage")
	 * 
	 * @ResponseBody public BhResult uploadImage(@RequestParam("file")
	 * MultipartFile file, HttpServletRequest request,HttpServletResponse
	 * response) { BhResult result = null; String realName = null; String
	 * uuidName = null; StringBuffer realPath = new
	 * StringBuffer("http://bhshop.oss-cn-shenzhen.aliyuncs.com/"); StringBuffer
	 * key = new StringBuffer();
	 * 
	 * try {
	 * 
	 * //文件原来的名称 realName = file.getOriginalFilename(); //得到这个文件的uuidname
	 * uuidName = this.getUUIDFileName(file.getOriginalFilename());
	 * 
	 * String roolPath =
	 * request.getSession(false).getServletContext().getRealPath("/"); //得到文件的输入流
	 * InputStream in = new BufferedInputStream(file.getInputStream());
	 * //获得文件的输出流 OutputStream out = new BufferedOutputStream(new
	 * FileOutputStream(new File(roolPath,uuidName)));
	 * 
	 * IOUtils.copy(in, out); in.close(); out.close();
	 * 
	 * key.append("goods/"); key.append(uuidName);
	 * 
	 * upload myupload= new upload(); String localFilePath = roolPath +
	 * uuidName; boolean
	 * bl=myupload.singleupload(oss,localFilePath,key.toString());
	 * System.out.println("上传情况----->"+bl); //真实路径 //String roolPath =
	 * request.getSession(false).getServletContext().getRealPath("/");
	 * 
	 * //图片保存到数据库 realPath.append("goods/");
	 * //"http://bhshop.oss-cn-shenzhen.aliyuncs.com/"
	 * realPath.append(uuidName); GoodsImage image = new GoodsImage();
	 * image.setGoodsId(1); image.setUrl(realPath.toString()); int flag =
	 * goodsService.selectInsert(image);
	 * 
	 * if(flag!=0){ result = new BhResult(200, "添加成功", image); }else{ result =
	 * new BhResult(400, "添加失败", null); } } catch (Exception e) { result =
	 * BhResult.build(500, "数据库搜索失败!");
	 * LoggerUtil.getLogger().error(e.getMessage());
	 * 
	 * e.printStackTrace(); } return result; }
	 */

	/**
	 * 修改商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/updateGoods", method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateGoods(@RequestBody Map<String, Object> map) {
		BhResult result = null;
		try {
			String id = (String) map.get("id");// 商品id
			String name = (String) map.get("name");// 名称
			String title = (String) map.get("title");// 副标题
			String modelId = (String) map.get("modelId");// 模型id
			String catId = (String) map.get("catId");// 分类id
			String shopCatId = (String) map.get("shopCatId");// 商品店铺分类id
			String brandId = (String) map.get("brandId"); // 品牌id
			String sellPrice = (String) map.get("sellPrice");// 慧滨价
			String marketPrice = (String) map.get("marketPrice");
			String storeNums = (String) map.get("storeNums");// 库存
			String image = (String) map.get("image");// 主图
			String deliveryPrice = (String) map.get("deliveryPrice");
			String isHot = (String) map.get("isHot"); // 是否热门0否，1是
			String isFlag = (String) map.get("isFlag");// 是否推荐0否，1是
			String sortnum = (String) map.get("sortnum");// 排序
			String isNew = (String) map.get("isNew");// 是否最新产品0否，1是
			String refundDays = (String) map.get("refundDays");// -1不限时间退货
																// 0不接受退货
																// 其它表示接受退货天数'
			String publicImage = (String) map.get("publicImage");// 宣传图
			String saleType = (String) map.get("saleType");// '销售模式 1普通模式 2只拼团
															// 3单卖 4拼团单卖皆可'
			String teamNum = (String) map.get("teamNum");// ''拼团人数',
			String teamEndTime = (String) map.get("teamEndTime");// '开团持续时间，单位分钟',
			String isPromote = (String) map.get("isPromote");// '是否促成团(-1 否 0是',
			String timeUnit = (String) map.get("timeUnit");// '持续时间单位：1-分钟，2-小时，3-天',
			String teamPrice = (String) map.get("teamPrice"); // 拼团价
			String isCreate = (String) map.get("isCreate"); // '是否支持系统发起拼单 -1否
															// 0是',
			String desc = "";
			Object descs = map.get("desc");// 描述
			String topicType = (String) map.get("topicType");
			String tagIds = (String) map.get("tagIds");
			String catIdOne = (String) map.get("catIdOne");
			String catIdTwo = (String) map.get("catIdTwo");
			if (descs != null && descs != "") {
				desc = (String) descs;
			}
			String appintroduce = "";
			Object appintroduces = map.get("appintroduce");
			if (appintroduces != null && appintroduces != "") {
				appintroduce = (String) appintroduces;
			}
			JSONArray list = JSONArray.fromObject(map.get("list"));// 模型属性值
			// JSONArray url = JSONArray.fromObject(map.get("url"));
			// 图片保存到数据库
			int row = goodsService.updateGoods(id, name, title, modelId, catId, shopCatId, brandId, sellPrice,
					marketPrice, storeNums, desc, image, list, deliveryPrice, isHot, isFlag, sortnum, isNew, refundDays,
					publicImage, saleType, teamNum, teamEndTime, isPromote, timeUnit, teamPrice, isCreate, appintroduce,
					topicType, tagIds, catIdOne, catIdTwo);
			if (row == 0) {
				result = new BhResult(400, "请求失败", null);
			} else {
				result = new BhResult(200, "修改成功", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171027-02 平台商品排序、设为热门，设为推荐
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/changeValue")
	@ResponseBody
	public BhResult changeValue(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String isHot = map.get("isHot"); // 是否热门0否，1是
			String isFlag = map.get("isFlag");// 是否推荐0否，1是
			String sortnum = map.get("sortnum");// 排序
			int row = goodsService.changeValue(id, isHot, isFlag, sortnum);
			if (row == 0) {
				result = new BhResult(BhResultEnum.FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 商品的删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delectById")
	@ResponseBody
	public BhResult delectById(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			int status = 1;
			int row = goodsService.updateStatus(id, status);
			if (row == 0) {
				result = BhResult.build(400, "请求失败！", null);
			} else {
				result = BhResult.build(200, "删除成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 申请商品上架
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/goodsPutaway")
	@ResponseBody
	public BhResult goodsPutaway(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			int status = 3;
			
			//程凤云获的当前登陆的商家，如果是已经交了免押金的商家，则直接将商品上架
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			MemberShop memberShop = goodsService.selectMemberShopBymId(shopId);
			if (memberShop.getDepositStatus().equals(2)) {
				int row = goodsService.goodsPutaway(id, 5);
				result = BhResult.build(200, "上架成功", null);
			}else{
				//如果该商家未有免审核的记录，则照原来的方法走
				int row = goodsService.goodsPutaway(id, status);
				if (row == 0) {
					result = BhResult.build(400, "请求失败！", null);
				} else {
					result = BhResult.build(200, "上架成功", null);
				}
			}
			
			
			
			
		} catch (Exception e) {
			result = BhResult.build(500, "数据库上架失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 商品的下架
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/goodsSoldOut")
	@ResponseBody
	public BhResult goodsSoldOut(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String msg = map.get("outReason");
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			int status = 2;
			int row = goodsService.goodsSoldOut(id, status, msg);
			if (row == 1) {
				boolean falg=goodsService.isPtSoldOut(shopId,id);//判断是否是平台下架商家商品
				//System.out.println("谢元春"+falg);
				if(falg){
					BhResult r=goodsService.sendMsgToShop(id,msg,request);//发送短信通知商家商品下架原因
					if(r.getStatus()==200){
						//保存数据到数据库
						int rowMsg=goodsService.saveGoodsMsg(r.getMsg(),id);
						result = BhResult.build(200, "成功", rowMsg);
					}else{
						result = BhResult.build(400, "失败", "短信发送异常，但商品已经被下架！！");
					}
				}else{
					result = BhResult.build(200, "成功", row);
				}
			} else {
				result = BhResult.build(400, "请求失败！", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库下架失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 平台后台审核商品上架
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/checkGoodsPutaway")
	@ResponseBody
	public BhResult checkGoodsPutaway(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String status = map.get("status");// 4拒绝5同意上架
			String reason = map.get("reason"); // 拒绝上架理由
			String fixedSale = map.get("fixedSale");// 已售数量
			int row = goodsService.checkGoodsPutaway(id, Integer.parseInt(status), reason, fixedSale);
			if (row == 0) {
				result = BhResult.build(400, "请求失败！", null);
			} else if (row == 10000) {
				result = new BhResult(BhResultEnum.GOODS_NOT_EXIT, null);
			} else {
				result = BhResult.build(200, "审核成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库审核失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20170907-01 平台后台商品列表(所有商品+条件查询+分页)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/backgroundGoodsList")
	@ResponseBody
	public BhResult backgroundGoodsList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String name = map.get("name"); // 查询条件
			String status = map.get("status"); // 商品状态--3待审核，4拒绝上架，5已上架
			String currentPage = map.get("currentPage");// 当前第几页
			String isHot = (String) map.get("isHot"); // 是否热门0否，1是
			String isFlag = (String) map.get("isFlag");// 是否推荐0否，1是
			String saleType = map.get("saleType"); // '销售模式 1普通模式 2只拼团 3单卖
													// 4拼团单卖皆可'
			String topicType = map.get("topicType");
			String id = map.get("id");
			String skuNo = map.get("skuNo");
			String jdSkuNo = map.get("jdSkuNo");

			// 价格查询
			String startPrice = map.get("startPrice");
			String endPrice = map.get("endPrice");
			String shopId = map.get("shopId");

			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> msg = goodsService.backgroundGoodsList(name, currentPage, Contants.PAGE_SIZE, status, isHot,
					isFlag, saleType, startPrice, endPrice, topicType, id, skuNo, jdSkuNo, shopId);
			result = new BhResult(BhResultEnum.GAIN_SUCCESS, msg);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * SCJ-20170907-02 PC端同类商品列表（按状态、上架+分页+标签查询+销量、价格、上架时间排序）
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/clientCategoryGoodsList")
	@ResponseBody
	public BhResult clientCategoryGoodsList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String catId = map.get("catId");
			String fz = map.get("fz"); // fz:1-按销量排序2-按价格3-时间
			String brandId = map.get("brandId"); // 按品牌查询
			String currentPage = map.get("currentPage");// 当前第几页
			String beginPrice = map.get("beginPrice");// 价格区间起始点
			String endPrice = map.get("endPrice");// 价格区间结束点
			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> msg = goodsService.clientCategoryGoodsList(catId, currentPage, Contants.PAGE_SIZE, fz,
					beginPrice, endPrice, brandId);
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20170907-03 PC端商品详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/clientGoodsDetials")
	@ResponseBody
	public BhResult clientGoodsDetials(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Goods goods = goodsService.selectBygoodsId(Integer.parseInt(id));
			if (goods != null) {
				result = new BhResult(200, "成功", goods);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20170907-04 PC端商品列表（按状态、上架+分页+条件查询+销量、价格、上架时间排序）
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/clientGoodsList")
	@ResponseBody
	public BhResult clientGoodsList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String name = map.get("name");// 查询条件
			String fz = map.get("fz"); // fz:1-按销量排序2-按价格3-时间
			String brandId = map.get("brandId"); // 按品牌查询
			String beginPrice = map.get("beginPrice");// 价格区间起始点
			String endPrice = map.get("endPrice");// 价格区间结束点
			String currentPage = map.get("currentPage");// 当前第几页
			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> msg = goodsService.clientGoodsList(name, currentPage, Contants.PAGE_SIZE, fz, beginPrice,
					endPrice, brandId);
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-201709015-02 PC端首页商品分类显示前6条
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectTopSix")
	@ResponseBody
	public BhResult selectTopSix(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String catId = map.get("catId");
			List<Goods> list = goodsService.selectTopSix(catId);
			if (list != null) {
				result = new BhResult(200, "成功", list);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-201709030-03 移动端首页商品列表
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/apiGoodsList")
	@ResponseBody
	public BhResult apiGoodsList(@RequestBody Goods entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			if (StringUtils.isEmpty(entity.getCurrentPage())) {
				entity.setCurrentPage(Contants.PAGE+"");
			}
		    Map<String,Object> page=goodsService.apiGoodsLists(entity);
			r = new BhResult();
			r.setData(page);
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			e.printStackTrace();
			return r;
		}
		return r;
	}

	/**
	 * zlk-2018- 05- 23 移动端 惠拼单商品列表
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/apiGoodsCollageList")
	@ResponseBody
	public BhResult apiGoodsCollageList(@RequestBody Goods entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			
			//2018.5.23 zlk 白名单
			JedisUtil jedisUtil= JedisUtil.getInstance();  
			JedisUtil.Strings strings=jedisUtil.new Strings();
			if(StringUtils.isNotBlank(strings.get("whiteMember"))) {//开启
				HttpSession session = request.getSession(false);
				if(session!=null) {
				   Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			       if(member!=null&&!member.equals("")) {
				      if(strings.get("whiteMember").equals(",")) {
				    	   return r = new BhResult(400,"暂未开放","暂未开放");
				      }else {
				    	   String whiteMember = ","+strings.get("whiteMember")+",";
					       if(!whiteMember.contains(","+member.getId()+",")) {
					    	   return r = new BhResult(400,"暂未开放","暂未开放");
					       }
				      }
			       }else {
			    	   return r = new BhResult(400,"请登录","请登录");
			       }
				}else {
					return r = new BhResult(400,"请登录","请登录");
				}
			}
            
			//2018.5.23 zlk 
			Map<String,Object> map=goodsService.apiGoodsCollageList(entity);
			//end
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	/**
	 * SCJ-201709030-04 移动端按分类显示商品列表(手机通讯模块)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/apiCategoryGoodsList")
	@ResponseBody
	public BhResult apiCategoryGoodsList(String catId, String currentPage) {
		BhResult result = null;
		try {
			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> msg = goodsService.apiCategoryGoodsList(catId, currentPage, Contants.PAGE_SIZE);
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20180125-04 移动端按分类显示商品列表(手机通讯模块)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/apiCateGoodsList")
	@ResponseBody
	public BhResult apiCateGoodsList(@RequestBody Goods entity) {
		BhResult r = null;
		try {
			PageBean<Goods> page = goodsService.apiCateGoodsList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}

	/**
	 * SCJ-201709030-05 移动端商品详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/apiGoodsDetails")
	@ResponseBody
	public BhResult apiGoodsDetails(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String tgId = map.get("tgId");
			if (StringUtils.isBlank(id)) {
				return result = BhResult.build(400, "参数不能为空");
			}
			//Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
			//2018.6.1 zlk
			HttpSession session = request.getSession(false);
		    Member member = new Member();
			if(session!=null) {
			     member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			//end				
			Goods goods = goodsService.apiGoodsDetails(Integer.parseInt(id), member, tgId);
			if (goods != null) {
				result = new BhResult(200, "成功", goods);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-201709030-06 移动端首页最新最热产品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/apiHotGoods")
	@ResponseBody
	public BhResult apiHotGoods() {
		BhResult result = null;
		try {
			List<Goods> msg = goodsService.apiHotGoods();
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-201709030-07 移动端分类商品热门推荐
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/apiCategoryGoodsHot")
	@ResponseBody
	public BhResult apiCategoryGoodsHot(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String catId = map.get("catId");
			List<Goods> msg = goodsService.apiCategoryGoodsHot(catId);
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取分类下的最后一级所有商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getGoodsByCatId")
	@ResponseBody
	public BhResult getGoodsByCatId(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String catId = map.get("catid");
			String currentPage = map.get("currentPage");
			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> goods = goodsService.getGoodsByCatId(catId, currentPage, Contants.PAGE_SIZE);
			if (goods != null) {
				result = new BhResult(200, "成功", goods);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171101-04 商家后台商品销售排行列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/arrangeBySale")
	@ResponseBody
	public BhResult arrangeBySale(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String name = map.get("name");
			String currentPage = map.get("currentPage");
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> goods = goodsService.arrangeBySale(shopId, currentPage, Contants.PAGE_SIZE, name);
			if (goods != null) {
				result = new BhResult(BhResultEnum.GAIN_SUCCESS, goods);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * @Description: 更新商家店铺头像
	 * @author xieyc
	 * @date 2018年3月6日 下午2:28:29
	 */
	@RequestMapping("/updateShopLogo")
	@ResponseBody
	public BhResult updateShopLogo(HttpServletRequest request, @RequestBody MemberShop memberShop) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			memberShop.setmId(shopId);
			Integer row = goodsService.updateShopLogo(memberShop);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * @Description: 移动端更新商家店铺头像
	 * @author xieyc
	 * @date 2018年3月6日 下午2:28:29
	 */
	@RequestMapping("/mupdateShopLogo")
	@ResponseBody
	public BhResult mupdateShopLogo(HttpServletRequest request, @RequestBody MemberShop memberShop) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				memberShop.setmId(shopId.intValue());
				Integer row = goodsService.updateShopLogo(memberShop);
				if (row == 1) {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} else {
				result = new BhResult();
				result.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				result.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return result;
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * @Description: 移动端更新商家店铺（退押金）
	 * @author xieyc
	 * @date 2018年3月6日 下午2:28:29
	 */
	@RequestMapping("/mupdateShopOrderPrice")
	@ResponseBody
	public BhResult mupdateShopOrderPrice(HttpServletRequest request) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null) {
				MemberShop memberShop = new MemberShop();
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				memberShop.setmId(shopId.intValue());
				memberShop.setOrderPrice(0);// 退押金
				Integer row = goodsService.updateShopLogo(memberShop);
				if (row == 1) {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} else {
				result = new BhResult();
				result.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				result.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return result;
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171109-02 商家后台首页店铺信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/shopDetails")
	@ResponseBody
	public BhResult shopDetails(HttpServletRequest request) {
		BhResult r = null;
		try {

			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Integer userId = (Integer) mapOne.get("userId");
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			MemberShop shop = goodsService.getShopDetails(shopId,userId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(shop);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}

	/**
	 * @Description: 移动端商家后台店铺信息
	 * @author xieyc
	 * @date 2018年3月5日 下午2:48:39
	 */
	@RequestMapping("/mshopDetails")
	@ResponseBody
	public BhResult mshopDetails(HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				Long userId = user.getUserId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				MemberShop shop = goodsService.mgetShopDetails(shopId.intValue(), userId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(shop);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}

	/**
	 * SCJ-201709030-04 移动端按分类显示商品列表(美食模块)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/apiCategoryEatList")
	@ResponseBody
	public BhResult apiCategoryEatList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String catId = map.get("catId"); // 分类id
			String currentPage = map.get("currentPage");// 当前第几页
			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			PageBean<Goods> msg = goodsService.apiCategoryGoodsList(catId, currentPage, Contants.PAGE_SIZE);
			if (msg != null) {
				result = new BhResult(200, "成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171129-07 获取店铺所有商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectAllByShopId")
	@ResponseBody
	public BhResult selectAllByShopId(HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			List<Goods> list = goodsService.selectAllByShopId(shopId);
			if (list != null) {
				result = new BhResult(BhResultEnum.SUCCESS, list);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171130-01 商家后台设置商品热门、新品、推荐
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/setHotNewAndFlag")
	@ResponseBody
	public BhResult setHotNewAndFlag(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String isHotShop = map.get("isHotShop"); // 是否店铺内热门0否，1是
			String isShopFlag = map.get("isShopFlag");// 是否店铺内推荐0否，1是
			String isNewShop = map.get("isNewShop");// 是否店铺内最新0否，1是
			String publicImage = (String) map.get("publicImage");// 宣传图
			int row = goodsService.setHotNewAndFlag(id, isHotShop, isShopFlag, isNewShop, publicImage);
			if (row == 0) {
				result = new BhResult(BhResultEnum.FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171201-01 获取App下载地址
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getDownloadUrl")
	@ResponseBody
	public BhResult getDownloadUrl(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String fz = map.get("fz"); // 1-android, 2-ios
			String url = goodsService.getDownloadUrl(fz);
			if (url != null) {
				result = new BhResult(BhResultEnum.SUCCESS, url);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * SCJ-20171219-01 获取分享商品详情
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getShareGoodsDetails")
	@ResponseBody
	public BhResult getShareGoodsDetails(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String userId = map.get("userId"); // 分享人id
			String skuId = map.get("skuId"); // 分享链接skuId
			String orderId = map.get("orderId"); // 分享链接订单id
			String shareType = map.get("shareType"); // '1、朋友圈 2、朋友',
			String shareUrl = map.get("shareUrl");// 分享链接
			String teamNo = map.get("teamNo"); // 团号
			Member member = (Member) request.getSession(true).getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
			ShareResult share = goodsService.getShareGoodsDetails(skuId, teamNo, userId, orderId, shareType, shareUrl,
					member);
			if (share != null) {
				result = new BhResult(BhResultEnum.SUCCESS, share);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * SCJ-20180110-11 活动添加商品处获取商品列表
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping("/selectAll")
	@ResponseBody
	public BhResult selectAll(@RequestBody Goods entity) {
		BhResult b = null;
		try {
			if (entity.getTopicType() == null) {
				/// 商品类型，0-普通商品，1砍价，2秒杀，3抽奖，4超级滨惠豆，5惠省钱
				b = new BhResult(400, "商品类型不能为空", null);
			} else {
				List<Goods> page = goodsService.selectAll(entity);
				b = new BhResult();
				b.setMsg(BhResultEnum.SUCCESS.getMsg());
				b.setStatus(BhResultEnum.SUCCESS.getStatus());
				b.setData(page);
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			b = BhResult.build(500, "操作异常！");
			return b;
		}

	}

	/**
	 * cheng-20180202-1 后台-平台(或商家)-商品管理 增加商品排序功能--->平台时参数为 id: 591, sortnum: "1"
	 * --->商家时参数为 id: 591, shopsortnum: "1"
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping("/updateGoodsSortnum")
	@ResponseBody
	public BhResult updateGoodsSortnum(@RequestBody Map<String, Object> map) {
		BhResult r = null;
		try {
			String json = JSONArray.fromObject(map.get("entity")).toString();// 模型属性值
			// 判断是否为空
			List<Goods> goods = JsonUtils.jsonToList(json, Goods.class);
			goodsService.updateGoodsSortnum(goods);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常！");
			return r;
		}
	}
	
	
	@RequestMapping("/updateGoodsShopSortnum")
	@ResponseBody
	public BhResult updateGoodsShopSortnum(@RequestBody Map<String, Object> map) {
		BhResult r = null;
		try {
			String json = JSONArray.fromObject(map.get("entity")).toString();// 模型属性值
			// 判断是否为空
			List<Goods> goods = JsonUtils.jsonToList(json, Goods.class);
			goodsService.updateGoodsShopSortnum(goods);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常！");
			return r;
		}
	}
	

	private String getExtName(String s, char split) {
		int i = s.lastIndexOf(split);
		int leg = s.length();
		return i > 0 ? (i + 1) == leg ? " " : s.substring(i + 1, s.length()) : " ";
	}

	private String getUUIDFileName(String fileName) {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder(100);
		sb.append(uuid.toString()).append(".").append(this.getExtName(fileName, '.'));
		return sb.toString();
	}

	/**
	 * zlk-2018.3.5 移动端 获取店铺热门商品 (商家主页)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/mSelectAllByShopId")
	@ResponseBody
	public BhResult mSelectAllByShopId(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null && !user.equals("") && user.getUserId() != null) {
				if (user.getShopId() == null) {
					long shopId = 1;// 平台
					user.setShopId(shopId);
				}
				// 热门上架商品
				List<Goods> list = goodsService.selectHotByShopId(user.getShopId().intValue());

				if (list != null && list.size() > 0) {
					
					for(int i =0;i<list.size();i++){
						list.get(i).setGoodStatus("0");
					}
					result = new BhResult(BhResultEnum.SUCCESS, list);
					
				} else {// 如果热门商品没有，则返回所有的已上架商品
					List<Goods> list2 = goodsService.selectAllByShopId(user.getShopId().intValue());
					if (list2.size() > 0) {
						for(int i =0;i<list2.size();i++){
							list2.get(i).setGoodStatus("1");
						}
						result = new BhResult(BhResultEnum.SUCCESS, list2);
					} else {
						result = new BhResult(BhResultEnum.GAIN_FAIL, null);
					}
				}

			} else {
				return result = new BhResult(100, "用户没有登录", null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 移动端 新增商品时加载设置sku所有的属性
	 *zlk-201802012-12
	 * @param entity
	 * @return
	 */
	@RequestMapping("/mSelectAllPro")
	@ResponseBody
	public BhResult mSelectAllPro(@RequestBody GoodsProperty entity) {
		BhResult r = null;
		try {
			List<GoodsProperty> row = goodsPropertyService.selectAllPro(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * zlk-20180315
	 * 商品添加时获取，设置规格
	 * @return
	 */
	@RequestMapping("/mSelectAllModel")
	@ResponseBody
	public BhResult mSelectAllModel(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String catid= map.get("catid");
			List<GoodsModel> list = goodsModelService.selectAllModel(catid);
			   if (list !=null) {
				   result = new BhResult(200, "查询成功", list);
				} else {
				   result = BhResult.build(400, "暂无数据！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * zlk-20183.15
	 * 移动端 获取 店铺商品分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/mSelectLinkedList")
	@ResponseBody
	public BhResult mSelectLinkedList(HttpServletRequest request) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null && !user.equals("") && user.getUserId() != null) {
				if (user.getShopId() == null) {
					long shopId = 1;// 平台
					user.setShopId(shopId);
				}
			    List<GoodsShopCategory> msg = goodsShopCategoryService.selectLinkedList(user.getShopId().intValue());
			    if(msg!=null){
				    result = new BhResult(200, "成功", msg);
			    }else{
				   result = BhResult.build(400, "暂无数据！");
			    }
		  }else{
			  return result = new BhResult(100, "用户没有登录", null);
		  }
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170927-02
	 * 获取商品分类
	 * @return
	 */
	@RequestMapping("/mSelectThreeLevel")
	@ResponseBody
	public BhResult mSelectThreeLevel() {
		BhResult result = null;
		try {
			List<GoodsCategory> goodsCategory = goodsCategoryService.selectThreeLevel();
			if(goodsCategory!=null){
				result = new BhResult(200, "成功", goodsCategory);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}	

	/**
	 * 根据分类id查品牌
	 * zlk
	 */
	@RequestMapping("/mGetBrandByCategory")
	@ResponseBody
	public BhResult mGetBrandByCategory(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			Long catId = Long.parseLong(id);
			List<GoodsBrand> list = goodsCategoryService.getBrandByCategory(catId);
			if(list.size()>0){
				result = new BhResult(200,"查询成功！",list);
			}else{
				result = new BhResult(200,"当前分类下没有对应的品牌！",null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}	
	

	/**
	 * 商品新增时获取，商品标签
	 * zlk-20180202-06
	 * @param entity
	 * @return
	 */
	@RequestMapping("/mSelectAll")
	@ResponseBody
	public BhResult mSelectAll() {
		BhResult r = null;
		try {
			List<GoodsTag> row = goodsTagService.selectAll();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	
	/**
	 * zlk-2018.3.7 移动端 获取店铺上架商品、已下架商品 (点击 添加商品 跳转进来的 商品管理页面)已经不用
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/mSelectByShopIdAndStatus")
	@ResponseBody
	public BhResult mSelectByShopIdAndStatus(@RequestBody Goods goods, HttpServletRequest request) {
		BhResult result = null;
		try {

			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user.getShopId() == null) {
				user.setShopId((long) 1);
			}
			goods.setShopId(user.getShopId().intValue());

			if (goods.getStatus() == 5) {
				// 所有的已上架商品,默认时间降序
				// 获取ordershop 的相关信息，把order_price 累加 ，这就是成交额
				Map<String, Object> map2 = goodsService.selectByShopId(goods);
				if (map2.size() > 0) {
					result = new BhResult(BhResultEnum.SUCCESS, map2);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} else {
				// 商家下架的商品

				Map<String, Object> map2 = goodsService.selectByShopId(goods);
				if (map2.size() > 0) {
					result = new BhResult(BhResultEnum.SUCCESS, map2);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * zlk 2018.3.5 移动端的商家的商品的添加 操作 goods 表
	 */
	@RequestMapping(value = "/mInsertGoods", method = RequestMethod.POST)
	@ResponseBody
	public BhResult mInsertGoods(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null && !user.equals("") && user.getUserId() != null) {
				if (user.getShopId() == null) {
					long shopId = 1;// 平台
					user.setShopId(shopId);
				}
				String name = (String) map.get("name");
				String title = (String) map.get("title");
				String modelId = (String) map.get("modelId");
				String catId = (String) map.get("catId");
				String shopCatId = (String) map.get("shopCatId");
				String brandId = (String) map.get("brandId");
				String sellPrice = (String) map.get("sellPrice");
				String marketPrice = (String) map.get("marketPrice");
				String storeNums = (String) map.get("storeNums");
				String image = (String) map.get("image");
				String deliveryPrice = (String) map.get("deliveryPrice");
				String isShopFlag = (String) map.get("isShopFlag");// 店铺内是否推荐0否，1是
				String isNew = (String) map.get("isNew");// 是否最新产品0否，1是
				String refundDays = (String) map.get("refundDays");// -1不限时间退货
																	// 0不接受退货
																	// 其它表示接受退货天数'
				String publicImage = (String) map.get("publicImage");// 宣传图
				String saleType = (String) map.get("saleType");// '销售模式 1普通模式 2只拼团
																// 3单卖 4拼团单卖皆可'
				String teamNum = (String) map.get("teamNum");// ''拼团人数',
				String teamEndTime = (String) map.get("teamEndTime");// '开团截止时间'
				String isPromote = (String) map.get("isPromote");// '是否促成团(-1 否 0是',
				String timeUnit = (String) map.get("timeUnit");// '持续时间单位：1-分钟，2-小时，3-天',
				String teamPrice = (String) map.get("teamPrice"); // 拼团价
				String isCreate = (String) map.get("isCreate"); // '是否支持系统发起拼单 -1否
																// 0是',
				String isJd = (String) map.get("isJd"); // 是否京东商品
				String desc = "";
				String topicType = (String) map.get("topicType");
				String tagIds = (String) map.get("tagIds");
				String catIdOne = (String) map.get("catIdOne");
				String catIdTwo = (String) map.get("catIdTwo");
				Object descs = map.get("desc");
				if (descs != null && descs != "") {
					desc = (String) descs;
				}
				String isPopular = (String) map.get("isPopular");
				String appintroduce = "";
				Object appintroduces = map.get("appintroduce");
				if (appintroduces != null && appintroduces != "") {
					appintroduce = (String) appintroduces;
				}
				JSONArray list = JSONArray.fromObject(map.get("list"));
				// JSONArray url = JSONArray.fromObject(map.get("url"));
				// 图片保存到数据库
				int row = goodsService.selectinsert(name, title, modelId, catId, shopCatId, brandId, sellPrice, marketPrice,
						storeNums, desc, image, list, deliveryPrice, isShopFlag, isNew, refundDays, publicImage, saleType,
						teamNum, teamEndTime, isPromote, timeUnit, teamPrice, isCreate, isJd, appintroduce, topicType,
						user.getShopId().intValue(), tagIds,isPopular, catIdOne, catIdTwo);
				if (row == 0) {
					result = new BhResult(400, "商品添加失败", null);
				} else {
					result = new BhResult(200, "商品添加成功", row);
				}
			  }else{
				  return result = new BhResult(100, "用户没有登录", null);
			  }
			} catch (Exception e) {
				e.printStackTrace();
				result = BhResult.build(500, "数据库添加失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
			return result;
		}
	
	/**
	 * zlk-20173.16
	 * 移动端 批量新增goodsSku
	 * @param map
	 * @return
	 */
	@RequestMapping("/mBatchAddGoodsSku")
	@ResponseBody
	public BhResult mBatchAddGoodsSku(@RequestBody Map<String, Object> map){
		BhResult result = null;
		try {
			String goodsId = (String) map.get("goodsId");
			JSONArray list = JSONArray.fromObject(map.get("list"));
			int row = service.batchAddGoodsSku(goodsId, list);
			if(row==0){
				result = BhResult.build(400, "sku新增失败！", null);
			}else{
				result = BhResult.build(200, "sku新增成功！", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 移动端 根据id获取商品详情 zlk
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/mSelectBygoodsId")
	@ResponseBody
	public BhResult mSelectBygoodsId(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Goods goods = goodsService.selectBygoodsId(Integer.parseInt(id));
			if (goods != null) {
				result = new BhResult(200, "成功", goods);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 移动端 zlk 商家后台  商品管理 列表 (按商家Id+条件查询+分页)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/mSelectByShopid")
	@ResponseBody
	public BhResult mSelectByShopid(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String name = map.get("name"); // 查询条件
			String catId = map.get("catid");// 分类查询id
			String status = map.get("status");// '商品状态 0正常 1已删除 2下架
												// 3申请上架4拒绝5上架',
			String saleType = map.get("saleType"); // '销售模式 1普通模式 2只拼团 3单卖
													// 4拼团单卖皆可'
			String currentPage = map.get("currentPage");// 当前第几页

			String sort =  map.get("sort"); //按 时间(1降序、2升序)、销量(3降序、5升序)、成交额(6降序、7升序)、库存 排序(8降序、9升序)
			if(StringUtils.isBlank(sort)){
				sort= "1";
			}
			// 价格查询
			String startPrice = map.get("startPrice");
			String endPrice = map.get("endPrice");
			String topicType = map.get("topicType");
			String id = map.get("id");
			String skuNo = map.get("skuNo");
			String jdSkuNo = map.get("jdSkuNo");

			if (StringUtils.isBlank(currentPage)) {
				currentPage = "1";
			}
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user != null && !user.equals("") && user.getUserId() != null) {
				    if (user.getShopId() == null) {
					     long shopId = 1;// 平台
					     user.setShopId(shopId);
			     	}
//			        PageBean<Goods> msg1 = goodsService.selectByShopid(user.getShopId().intValue(), name, currentPage, Contants.PAGE_SIZE, catId,
//					saleType, status, startPrice, endPrice, topicType, id, skuNo, jdSkuNo);
			        PageBean<Goods> msg = goodsService.mSelectByShopid(user.getShopId().intValue(), name, currentPage, Contants.PAGE_SIZE, catId,
					saleType, status, startPrice, endPrice, topicType, id, skuNo, jdSkuNo, sort);
		        	if (msg != null) {
				         result = new BhResult(200, "成功", msg);
			       } else {
				        result = BhResult.build(400, "暂无数据！");
			        }
			}else{
				    result = new BhResult(100, "暂无用户登录", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * zlk-2018.3.7 移动端 修改 店铺 商品 (发货页面 的设置商品下架、上架、新品、推荐、热销、删除 )
	 * 商品所属状态goodsStatus  上架1、下架2 。 设为新品 3，取消新品5。 设为推荐 6，取消推荐7，设为热销8，取消热销9。删除 10，取消删除 11
	 * @param map
	 * @return
	 */
	@RequestMapping("/mUpdateById")
	@ResponseBody
	public BhResult mUpdateById(@RequestBody Goods goods) {
		BhResult result = null;
		try {

			if (goods.getGoodsStatus().equals("1")||goods.getGoodsStatus().equals("2")) {
				// 设置商品的上下架
				if(goods.getGoodsStatus().equals("1")){
					     //上架
				        Goods goods2 = new Goods();
				        goods2.setStatus(3); //申请上架
			     	    goods2.setId(goods.getId());
			     	    goods2.setUpTime(new Date());
			     	    
			     	    int row = goodsService.mUpdateById(goods2);
				}else{
					    //下架
			            Goods goods2 = new Goods();
			            goods2.setStatus(2);  //下架
		     	        goods2.setId(goods.getId());
		     	       goods2.setDownTime(new Date());
					    int row = goodsService.mUpdateById(goods2);
				}
			} else if (goods.getGoodsStatus().equals("3")||goods.getGoodsStatus().equals("5")) {
			        	
				if(goods.getGoodsStatus().equals("3")){
					// 设置商品的新品
				        Goods goods2 = new Goods();
			         	goods2.setIsNewShop(1);  //店铺展示新品
				        goods2.setId(goods.getId());
				        int row = goodsService.mUpdateById(goods2);
				} else{
				      //取消新品
					    Goods goods2 = new Goods();
			         	goods2.setIsNewShop(0);
				        goods2.setId(goods.getId());
				        int row = goodsService.mUpdateById(goods2);
				 }
			} else if (goods.getGoodsStatus().equals("6")||goods.getGoodsStatus().equals("7")) {
			    if(goods.getGoodsStatus().equals("6")){
			        	// 设置商品的推荐
				       Goods goods2 = new Goods();
				       goods2.setIsShopFlag(true);
				       goods2.setId(goods.getId());
				       int row = goodsService.mUpdateById(goods2);
			    }else{
			    	   // 取消商品的推荐
			    	   Goods goods2 = new Goods();
				       goods2.setIsShopFlag(false);
				       goods2.setId(goods.getId());
				       int row = goodsService.mUpdateById(goods2);
			    }
			} else if (goods.getGoodsStatus().equals("8")||goods.getGoodsStatus().equals("9")) {
				if(goods.getGoodsStatus().equals("8")){
				      // 设置商品的热门
				      Goods goods2 = new Goods();
				      goods2.setIsHotShop(1);
				      goods2.setId(goods.getId());
				      int row = goodsService.mUpdateById(goods2);
				}else{
					  // 取消商品的热门
					 Goods goods2 = new Goods();
				      goods2.setIsHotShop(0);
				      goods2.setId(goods.getId());
				      int row = goodsService.mUpdateById(goods2);
				}
			}else if(goods.getGoodsStatus().equals("10")||goods.getGoodsStatus().equals("11")){
				if(goods.getGoodsStatus().equals("10")){
				      // 删除商品
				      Goods goods2 = new Goods();
				      goods2.setStatus(1);
				      goods2.setId(goods.getId());
				      int row = goodsService.mUpdateById(goods2);
				}else{
					  //取消删除 
				      Goods goods2 = new Goods();
				      goods2.setStatus(0);
				      goods2.setId(goods.getId());
				      int row = goodsService.mUpdateById(goods2);
				}
			}
			result = new BhResult(200,"修改成功",null);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * zlk-20173.16
	 * 移动端 修改订单地址
	 * @param map
	 * @return
	 */
	@RequestMapping("/mUpdateOrder")
	@ResponseBody
	public BhResult mUpdateOrder(@RequestBody Map<String, Object> map){
		BhResult result = null;
		try {
			String name = (String) map.get("name");
			String phone = (String) map.get("phone");
			String address = (String) map.get("address");
			String order_no = (String) map.get("order_no"); //平台订单号
			String prov = (String) map.get("prov");  //省份
			String city = (String) map.get("city");   //城市
			String area = (String) map.get("area");   //地区
			String four = (String) map.get("four");   //第四级地址
			
			Order order = orderMapper.getOrderByOrderNo(order_no);
			
			//根据京东编码获取地址信息
			GoAddressArea p = new GoAddressArea();
			p.setId(Integer.valueOf(prov));
			List<GoAddressArea> go1 =   goAddressAreaMapper.selectById(p);
			
			GoAddressArea c = new GoAddressArea();
		    c.setId(Integer.valueOf(city));
			List<GoAddressArea> go3 =   goAddressAreaMapper.selectById(c);
		
			GoAddressArea a = new GoAddressArea();
			a.setId(Integer.valueOf(area));
			List<GoAddressArea> go5 =   goAddressAreaMapper.selectById(a);
			
			GoAddressArea f = new GoAddressArea();
			f.setId(Integer.valueOf(four));
			List<GoAddressArea> go7 =   goAddressAreaMapper.selectById(f);
		
			MemberUserAddress ma = new MemberUserAddress();
			ma.setAddress(address);
			ma.setmId(order.getmId());
			ma.setFullName(name);
			ma.setTel(phone);
			ma.setArea(Integer.valueOf(area));
			ma.setCity(Integer.valueOf(city));
			ma.setProv(Integer.valueOf(prov));
			ma.setFour(Integer.valueOf(four));
			ma.setAreaname(go5.get(0).getName());
			ma.setCityname(go3.get(0).getName());
			ma.setProvname(go1.get(0).getName());
			if(go7.size()>0){
			      ma.setFourname(go7.get(0).getName());
			}
			int row= memberUserAddressMapper.insertSelective(ma);
			if(row==0){
				result = BhResult.build(400, "修改失败！", null);
			}else{
				result = BhResult.build(200, "修改成功！", null);
				order.setmAddrId(ma.getId());
				orderMapper.updateByPrimaryKeySelective(order);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 移动端后台 zlk 2018.3.19 修改商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/mUpdateGoods", method = RequestMethod.POST)
	@ResponseBody
	public BhResult mUpdateGoods(@RequestBody Map<String, Object> map) {
		BhResult result = null;
		try {
			String id = (String) map.get("id");// 商品id
			String name = (String) map.get("name");// 名称
			String title = (String) map.get("title");// 副标题
			String modelId = (String) map.get("modelId");// 模型id
			String catId = (String) map.get("catId");// 分类id
			String shopCatId = (String) map.get("shopCatId");// 商品店铺分类id
			String brandId = (String) map.get("brandId"); // 品牌id
			String sellPrice = (String) map.get("sellPrice");// 慧滨价
			String marketPrice = (String) map.get("marketPrice");
			String storeNums = (String) map.get("storeNums");// 库存
			String image = (String) map.get("image");// 主图
			String deliveryPrice = (String) map.get("deliveryPrice");
			String isHot = (String) map.get("isHot"); // 是否热门0否，1是
			String isFlag = (String) map.get("isFlag");// 是否推荐0否，1是
			String sortnum = (String) map.get("sortnum");// 排序
			String isNew = (String) map.get("isNew");// 是否最新产品0否，1是
			String refundDays = (String) map.get("refundDays");// -1不限时间退货
																// 0不接受退货
																// 其它表示接受退货天数'
			String publicImage = (String) map.get("publicImage");// 宣传图
			String saleType = (String) map.get("saleType");// '销售模式 1普通模式 2只拼团
															// 3单卖 4拼团单卖皆可'
			String teamNum = (String) map.get("teamNum");// ''拼团人数',
			String teamEndTime = (String) map.get("teamEndTime");// '开团持续时间，单位分钟',
			String isPromote = (String) map.get("isPromote");// '是否促成团(-1 否 0是',
			String timeUnit = (String) map.get("timeUnit");// '持续时间单位：1-分钟，2-小时，3-天',
			String teamPrice = (String) map.get("teamPrice"); // 拼团价
			String isCreate = (String) map.get("isCreate"); // '是否支持系统发起拼单 -1否
															// 0是',
			String desc = "";
			Object descs = map.get("desc");// 描述
			String topicType = (String) map.get("topicType");
			String tagIds = (String) map.get("tagIds");
			String catIdOne = (String) map.get("catIdOne");
			String catIdTwo = (String) map.get("catIdTwo");
			if (descs != null && descs != "") {
				desc = (String) descs;
			}
			String appintroduce = "";
			Object appintroduces = map.get("appintroduce");
			if (appintroduces != null && appintroduces != "") {
				appintroduce = (String) appintroduces;
			}
			JSONArray list = JSONArray.fromObject(map.get("list"));// 模型属性值
			// JSONArray url = JSONArray.fromObject(map.get("url"));
			// 图片保存到数据库
			int row = goodsService.updateGoods(id, name, title, modelId, catId, shopCatId, brandId, sellPrice,
					marketPrice, storeNums, desc, image, list, deliveryPrice, isHot, isFlag, sortnum, isNew, refundDays,
					publicImage, saleType, teamNum, teamEndTime, isPromote, timeUnit, teamPrice, isCreate, appintroduce,
					topicType, tagIds, catIdOne, catIdTwo);
			if (row == 0) {
				result = new BhResult(400, "请求失败", null);
			} else {
				result = new BhResult(200, "修改成功", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * zlk 20180301-19
	 * 移动端 批量修改goodsSku 
	 * @param map
	 * @return
	 */
	@RequestMapping("/mBatchUpdateGoodsSku")
	@ResponseBody
	public BhResult mBatchUpdateGoodsSku(@RequestBody Map<String, Object> map){
		BhResult r = null;
		try {
			JSONArray list = JSONArray.fromObject(map.get("list"));
			service.batchUpdateGoodsSku(list);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * zlk 20180323
	 * 移动端 获取后台登录的用户shopId
	 * @param map
	 * @return
	 */
	@RequestMapping("/mGetShopId")
	@ResponseBody
	public BhResult mGetShopId(HttpServletRequest request){
		BhResult r = null;
		try {
			   //获取当期的移动端登录的用户信息 
			   MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   if(user==null){
				  return r = new BhResult(100, "您还没有登录，请重新登录", null);
			   }else if(user.getUsername().equals("admin")){
				  return r = new BhResult(200, "获取成功",0);
			   }else if(user.getShopId() == null) {
				  return r = new BhResult(200, "获取成功",0);
			   }else{
				  r = new BhResult(200,"获取成功",user.getShopId());
			   }
			  
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
		return r;
	}
	
	
	/**
	 * scj-20180313-01 修改商品活动类型
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping("/updateTopicType")
	@ResponseBody
	public BhResult get(@RequestBody Goods entity) {
		BhResult r = null;
		try {
			goodsService.updateTopicType(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
	
	/**
	 *设为平台新品
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping("/updatePingTaiNew")
	@ResponseBody
	public BhResult updatePingTaiNew(@RequestBody Goods entity) {
		BhResult r = null;
		try {
			goodsService.updatePingTaiNew(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
	/**
	 * 移动端新品列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/newGoodsList")
	@ResponseBody
	public BhResult newGoodsList(@RequestBody Goods entity) {
		BhResult r = null;
		try {
			
			Map<String, Object> page = goodsService.newGoodsList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	/**
	 * @Description: 商品下架短信列表
	 * @author xieyc
	 * @date 2018年3月27日 下午4:49:51 
	 */
	@RequestMapping("/goodsMsgList")
	@ResponseBody
	public BhResult goodsMsgList(@RequestBody GoodsMsg goodsMsg,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			goodsMsg.setShopId(shopId);
			PageBean<GoodsMsg> page  = goodsService.goodsMsgList(goodsMsg);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	/**
	 * @Description: 商家与平台互动信息保存
	 * @author xieyc
	 * @date 2018年3月27日 下午4:50:23 
	 */
	@RequestMapping("/saveChatRecord")
	@ResponseBody
	public BhResult saveChatRecord( @RequestBody InteractiveRecord record,HttpServletRequest request){
		BhResult result = null;
		try {
			int row=goodsService.saveChatRecord(record);
			if(row==1){
				result = new BhResult(200, "成功",row);
			}else{
				result = BhResult.build(400, "操作失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/**
	 * @Description: 更新回话状态
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53 
	 */
	@RequestMapping("/updateMsgstate")
	@ResponseBody
	public BhResult updateMsgstate(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			String id=(String)map.get("id");
			int row=goodsService.updateMsgstate(id,shopId);
			r = new BhResult(200, "成功",row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * @Description: 更新短信状态
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53 
	 */
	@RequestMapping("/updateMsg")
	@ResponseBody
	public BhResult updateGoodsMsg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			int row=goodsService.updateGoodsMsg(shopId);
			r = new BhResult(200, "成功",row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	/**
	 * @Description: 获取没有读的信息条数
	 * @author xieyc
	 * @date 2018年3月27日 下午4:26:53 
	 */
	@RequestMapping("/getUnreadNum")
	@ResponseBody
	public BhResult getUnreadNum(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			int num=goodsService.getUnreadNum(shopId);
			r = new BhResult(200, "成功",num);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * @Description: zlk 
	 * @author 金额 区间 设置
	 * @date 2018年3月29日 下午4:26:53 
	 */
	@RequestMapping("/updateFixedSale")
	@ResponseBody
	public BhResult updateFixedSale(@RequestBody Goods good) {
		BhResult r = null;
		try {

			goodsService.updateFixedSale();
			r = new BhResult(200, "成功",null);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}

	}
		
/**
 * 移动端按分类显示商品列表(活动专区模块)
 * 
 * @param map
 * @return
 */
@RequestMapping("/apiCategoryDiscountList")
@ResponseBody
public BhResult apiCategoryActivityList(@RequestBody Map<String, String> map) {
	BhResult result = null;
	 Map<String,Object> msg=null;
	try {
		String tagName=map.get("tagName");
		String currentPage = map.get("currentPage");// 当前第几页
		
		if (StringUtils.isBlank(currentPage)) {
			currentPage = "1";
		}
		msg = goodsService.apiCategoryActivityList(currentPage, Contants.PAGE_SIZE,tagName);
		
		if (msg != null) {
			result = new BhResult(200, "成功", msg);
		} else {
			result = BhResult.build(400, "暂无数据！");
		}
	} catch (Exception e) {
		result = BhResult.build(500, "数据库搜索失败!");
		LoggerUtil.getLogger().error(e.getMessage());
	}
	return result;
}	


/**
 * 移动端按分类显示商品列表(活动专区模块)
 * 
 * @param map
 * @return
 */
/*@RequestMapping("/apiCategoryList")
@ResponseBody
public BhResult apiCategoryList(@RequestBody Map<String, String> map) {
	BhResult result = null;
	 Map<String,Object> msg=null;
	try {
		String tagId=map.get("tagId");
		String currentPage = map.get("currentPage");// 当前第几页
		
		if (StringUtils.isBlank(currentPage)) {
			currentPage = "1";
		}
		msg = goodsService.apiCategoryList(currentPage, Contants.PAGE_SIZE,tagId);
		
		if (msg != null) {
			result = new BhResult(200, "成功", msg);
		} else {
			result = BhResult.build(400, "暂无数据！");
		}
	} catch (Exception e) {
		result = BhResult.build(500, "数据库搜索失败!");
		LoggerUtil.getLogger().error(e.getMessage());
	}
	return result;
}	*/	


/**
 * 获取活动专区信息
 */

/*@RequestMapping("/apiCategoryName")
@ResponseBody
public BhResult apiCategoryName(@RequestBody Map<String, String> map) {
	BhResult result = null;
	 Map<String,Object> msg=null;
	try {
		String tagId=map.get("tagId");
		msg = goodsService.apiCategoryName(tagId);
		if (msg != null) {
			result = new BhResult(200, "成功", msg);
		} else {
			result = BhResult.build(400, "暂无数据！");
		}
	} catch (Exception e) {
		result = BhResult.build(500, "数据库搜索失败!");
		LoggerUtil.getLogger().error(e.getMessage());
	}
	return result;
}	*/


/**
 * 移动端按分类显示商品列表(活动专区模块)
 * 
 * @param map
 * @return
 */
@RequestMapping("/apiCategoryList")
@ResponseBody
public BhResult apiCategoryList(@RequestBody Map<String, String> map) {
	BhResult result = null;
	 Map<String,Object> msg=null;
	try {
		String uuid=map.get("uuid");
		//String currentPage = map.get("currentPage");// 当前第几页
		
		/*if (StringUtils.isBlank(currentPage)) {
			currentPage = "1";
		}*/
		msg = goodsService.apiCategoryList(uuid);
		
		if (msg != null) {
			result = new BhResult(200, "成功", msg);
		} else {
			result = BhResult.build(400, "暂无数据！");
		}
	} catch (Exception e) {
		result = BhResult.build(500, "数据库搜索失败!");
		LoggerUtil.getLogger().error(e.getMessage());
	}
	return result;
}	


	/**
	 * <p>Description: 拍卖详情页</p>
	 *  @author scj  
	 *  @date 2018年5月22日
	 */
	@RequestMapping("/dauctionGoodsDetails")
	@ResponseBody
	public BhResult apiHomeList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");
			String currentPeriods = map.get("currentPeriods");
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if(obj!=null){
					member = (Member)obj; //获取当前登录用户信息
				}
			}
			if(member==null){
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			Goods data = goodsService.dauctionGoodsDetails(goodsId, currentPeriods, member);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	//根据经纬度获取地址
	@RequestMapping("/apiAddress")
	@ResponseBody
	public BhResult apiAddress(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String lat = map.get("lat"); //纬度
			String log = map.get("log"); //经度
			
			Address address=goodsService.apiAddress(lat,log);
			if (address != null) {
				result = new BhResult(200, "获取成功", address);
			} else {
				result = BhResult.build(400, "暂无地址！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "定位失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 优化商品详情接口 移动端商品详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/apiGoodsDetailsOptimize")
	@ResponseBody
	public BhResult apiGoodsDetailsOptimize(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String tgId = map.get("tgId");
			if (StringUtils.isBlank(id)) {
				return result = BhResult.build(400, "参数不能为空");
			}
			//Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
			//2018.6.1 zlk
			HttpSession session = request.getSession(false);
		    Member member = new Member();
			if(session!=null) {
			     member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			//end	
			
			//Goods goods = goodsService.apiGoodsDetails(Integer.parseInt(id), member, tgId);
			MyGoodsDetail goods=goodsService.apiGoodsDetailOptimize(Integer.parseInt(id), member, tgId);
			if (goods != null) {
				result = new BhResult(200, "操作成功", goods);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	
	
	
}
