package com.bh.admin.controller.order;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bh.bean.IDCardEntity;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.AliMarketUtil;
import com.bh.utils.ImageBase64Util;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.StringUtil;
import com.bh.utils.WxJsApiUtil;
import com.control.file.upload;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberSend;
import com.bh.admin.service.SendService;
import com.bh.admin.vo.WangEditorInfo;;

@Controller
/* @RequestMapping("/send") */
public class SendController {

	@Autowired
	private SendService sendService;

	@Value(value = "${pageSize}")
	private String pageSize;

	// 完善配送员的基本信息(pc端)
	@RequestMapping(value = "/insertSendMsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertSendMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			String phone = map.get("phone");
			String fullName = map.get("fullName");
			String idcard_img_url = map.get("idcard_img_url"); // 身份证
			String idcard_hand_img_url = map.get("idcard_hand_img_url");
			String address = map.get("address"); // 住址
			// String time = map.get("time"); // 接单时间
			String scope = map.get("scope");// 接单的范围
			String income = map.get("income"); // 预计收入
			String tool = map.get("tool"); // 工具
			String lon = map.get("lon"); //
			String lat = map.get("lat"); //
			// String status = map.get("status");//
			String sex = map.get("sex");
			String age = map.get("age");

			Member member = new Member();

			MemberSend memberSend = new MemberSend();
			if (StringUtils.isNotEmpty(phone)) {
				memberSend.setPhone(phone);
			}
			if (StringUtils.isNotEmpty(fullName)) {
				memberSend.setFullName(fullName);
			}
			if (StringUtils.isNotEmpty(idcard_img_url)) {
				memberSend.setIdcardImgUrl(idcard_img_url);
			}
			if (StringUtils.isNotEmpty(idcard_hand_img_url)) {
				memberSend.setIdcardHandImgUrl(idcard_hand_img_url);
			}
			if (StringUtils.isNotEmpty(address)) {
				memberSend.setAddress(address);
			}
			if (StringUtils.isNotEmpty(scope)) {
				Integer scope1 = Integer.parseInt(scope);
				Integer scope2 = scope1 * 1000;
				memberSend.setScope(String.valueOf(scope2));
			}
			if (StringUtils.isNotEmpty(income)) {
				memberSend.setIncome(income);
			}
			if (StringUtils.isNotEmpty(tool)) {
				memberSend.setTool(Integer.parseInt(tool));
			}
			if (StringUtils.isNotEmpty(lon)) {
				memberSend.setLon(Double.parseDouble(lon));
			}
			if (StringUtils.isNotEmpty(lat)) {
				memberSend.setLat(Double.parseDouble(lat));
			}
			if (StringUtils.isNotEmpty(sex)) {
				memberSend.setSex(Integer.parseInt(sex));
			}
			if (StringUtils.isNotEmpty(age)) {
				memberSend.setAge(Integer.parseInt(age));
			}
			memberSend.setStatus(0);
			memberSend.setTime(new Date());

			// memberSend.setStatus(Integer.parseInt(status));

			String faceImgBase64Str = ImageBase64Util.getImageBase64StrByUrl(idcard_img_url);
			IDCardEntity idEntity = AliMarketUtil.verifyIDCardFace(faceImgBase64Str);
			if (idEntity == null) {
				result = new BhResult(500, "身份证正面照验证失败", null);
				return result;
			}
			memberSend.setAddress(idEntity.getAddress());
			String sex1 = idEntity.getSex();
			if (sex1.equals("女")) {// 1男2女
				memberSend.setSex(2);
			} else if (sex1.equals("男")) {
				memberSend.setSex(1);
			}
			memberSend.setIdentity(idEntity.getNum());
			memberSend.setFullName(idEntity.getName());

			int row = sendService.insertSendMsg(memberSend);
			if (row == 1) {
				result = new BhResult(200, "添加成功", null);
			} else {
				result = new BhResult(400, "添加失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	// pc端
	@RequestMapping(value = "/getMsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String phone = map.get("phone");
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (StringUtils.isEmpty(phone)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			} else {
				if (member == null) {
					MemberSend send = sendService.getMsg(phone);// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
					if ("0".equals(send.getFullName())) {
						result = new BhResult(201, "该用户不存在，请先成为普通用户才能注册成配送员", null);
					} else if ("1".equals(send.getFullName())) {
						result = new BhResult(BhResultEnum.SUCCESS, "该用户可以注册成为配送员");
					} else {
						if (send.getStatus().equals("0")) {
							result = new BhResult(202, "该用户已提交注册信息，待管理员审核", send);
						} else if (send.getStatus().equals("1")) {
							result = new BhResult(203, "该用户已成为配送员，不可以再次提交", null);
						} else if (send.getStatus().equals("2")) {
							result = new BhResult(204, "该用户提交的信息审核失败，可再次提交", send);
						}

					}

				} else {
					List<Member> member2 = sendService.getMsg1(phone,member.getId());// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
					if (member2.size() > 0) {
						if (member2.get(0).getId().equals(member.getId())) {
							MemberSend send = sendService.getMsg(phone);// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
							if ("0".equals(send.getFullName())) {
								result = new BhResult(201, "该用户不存在，请先成为普通用户才能注册成配送员", null);
							} else if ("1".equals(send.getFullName())) {
								result = new BhResult(BhResultEnum.SUCCESS, "该用户可以注册成为配送员");
							} else if ("2".equals(send.getFullName())) {
								System.out.print("fullname" + send.getFullName());
								if (send.getStatus().equals(0)) {
									result = new BhResult(202, "该用户已提交注册信息，待管理员审核", send);
								} else if (send.getStatus().equals(1)) {
									result = new BhResult(203, "该用户已成为配送员，不可以再次提交", null);
								} else if (send.getStatus().equals(2)) {
									result = new BhResult(204, "该用户提交的信息审核失败，可再次提交", send);
								}

							}
						} else {
							result = new BhResult(400, "该用户的手机号不匹配", null);
						}
					} else {
						result = new BhResult(205, "该手机号在系统中不存在", null);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	// 移动端实名认证(移动端)
	@RequestMapping(value = "/send/autoByMove", method = RequestMethod.POST)
	@ResponseBody
	public BhResult autoByMove(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			String idcard_img_url = map.get("idcard_img_url"); // 身份证
			String idcard_hand_img_url = map.get("idcard_hand_img_url");
			String scope = "10";// 接单的范围
			String income = "0"; // 预计收入
			String tool = "1"; // 工具

			MemberSend memberSend = new MemberSend();
			if (StringUtils.isNotEmpty(member.getPhone())) {
				memberSend.setPhone(member.getPhone());
			}
			if (StringUtils.isNotEmpty(idcard_img_url)) {
				memberSend.setIdcardImgUrl(idcard_img_url);
			}
			if (StringUtils.isNotEmpty(idcard_hand_img_url)) {
				memberSend.setIdcardHandImgUrl(idcard_hand_img_url);
			}
			if (StringUtils.isNotEmpty(scope)) {
				Integer scope1 = Integer.parseInt(scope);
				Integer scope2 = scope1 * 1000;
				memberSend.setScope(String.valueOf(scope2));
			}
			if (StringUtils.isNotEmpty(income)) {
				memberSend.setIncome(income);
			}
			if (StringUtils.isNotEmpty(tool)) {
				memberSend.setTool(Integer.parseInt(tool));
			}
			memberSend.setStatus(0);
			memberSend.setTime(new Date());

			String faceImgBase64Str = ImageBase64Util.getImageBase64StrByUrl(idcard_img_url);
			IDCardEntity idEntity = AliMarketUtil.verifyIDCardFace(faceImgBase64Str);
			if (idEntity == null) {
				result = new BhResult(500, "身份证正面照验证失败", null);
				return result;
			}
			memberSend.setAddress(idEntity.getAddress());
			String sex1 = idEntity.getSex();
			if (sex1.equals("女")) {// 1男2女
				memberSend.setSex(2);
			} else if (sex1.equals("男")) {
				memberSend.setSex(1);
			}
			memberSend.setIdentity(idEntity.getNum());
			memberSend.setFullName(idEntity.getName());

			int row = sendService.insertSendMsg(memberSend);
			if (row == 1) {
				result = new BhResult(200, "添加成功", null);
			} else {
				result = new BhResult(400, "添加失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	// 移动端端
	@RequestMapping(value = "/send/getMsgByMove", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getMsgByMove(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);

			if (member.getPhone() == null) {
				result = new BhResult(206, "该手机号在系统中不存在,请绑定手机", null);
			}else{
				List<Member> member2 = sendService.getMsg1(member.getPhone(),member.getId());// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
				if (member2.size() > 0) {
					if (member2.get(0).getId().equals(member.getId())) {
						MemberSend send = sendService.getMsg(member.getPhone());// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
						if ("0".equals(send.getFullName())) {
							result = new BhResult(201, "该用户不存在，请先成为普通用户才能注册成配送员", null);
						} else if ("1".equals(send.getFullName())) {
							result = new BhResult(BhResultEnum.SUCCESS, "该用户可以注册成为配送员");
						} else if ("2".equals(send.getFullName())) {
							System.out.print("fullname" + send.getFullName());
							if (send.getStatus().equals(0)) {
								result = new BhResult(202, "该用户已提交注册信息，待管理员审核", send);
							} else if (send.getStatus().equals(1)) {
								result = new BhResult(203, "该用户已成为配送员，不可以再次提交", null);
							} else if (send.getStatus().equals(2)) {
								result = new BhResult(204, "该用户提交的信息审核失败，可再次提交", send);
							}
						}
					} else {
						result = new BhResult(206, "该手机号在系统中不存在,请绑定手机", null);
					}
				} else {
					result = new BhResult(206, "该手机号在系统中不存在,请绑定手机", null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	@Value(value = "${oss}")
	private String oss;

	@RequestMapping("/upload")
	@ResponseBody
	public WangEditorInfo uploadImg(MultipartFile files[], HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");// 获得files目录的绝对路径
		WangEditorInfo w = new WangEditorInfo();
		w.setErrno(0);
		ArrayList<String> list = new ArrayList<>();
		for (MultipartFile file : files) {
			StringBuffer realPath = new StringBuffer(Contants.bucketHttp);
			StringBuffer key = new StringBuffer();

			String fileName = file.getOriginalFilename();// 获得上传文件的实际名称
			String[] types = fileName.split("\\.");
			String type = types[types.length - 1];
			type = "." + type;// 获得文件的后缀名
			String targetFileName = StringUtil.genUuId() + type;
			File targetFile = new File(path, targetFileName);

			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);// 保存文件

				key.append("sendimages/");
				key.append(targetFileName);
				upload myupload = new upload();
				String localFilePath = path + targetFileName;
				boolean bl = myupload.singleupload(oss, localFilePath, key.toString());

				realPath.append("sendimages/");
				realPath.append(targetFileName);

				if (bl) {
					list.add(realPath.toString());
				} else {
					w.setErrno(400);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		w.setData(list);
		return w;
	}

	/**
	 * CHENG-20171124-01 速达-个人中心
	 * 
	 * @param map(goodsId-商品的id,skuId-sku的id)
	 * @return
	 */
	@RequestMapping("/send/sendcenter")
	@ResponseBody
	public BhResult showGoodsFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			MemberSend memberSend = new MemberSend();
			memberSend.setmId(member.getId());

			MemberSend memberSend2 = sendService.selectMemberSendCenter(memberSend);

			result = new BhResult(BhResultEnum.SUCCESS, memberSend2);

			return result;

		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * scj-20171128-01 后台配送员管理列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/memberSend/pageList")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			String type = map.get("type"); // 查询条件-接单类型1全部，2近距离，3远距离，4高价，5低价',
			String name = map.get("name"); // 查询条件-配送员真实姓名或身份证
			String status = map.get("status"); // 状态：0待审核，1审核成功，2审核失败',
			PageBean<MemberSend> page = sendService.pageList(pageSize, currentPage, status, name, type);
			if (page != null) {
				result = new BhResult(BhResultEnum.SUCCESS, page);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * scj-20171128-02 配送员审核
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/memberSend/auditSend")
	@ResponseBody
	public BhResult auditSend(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			String status = map.get("status"); // 状态：0待审核，1审核成功，2审核失败',
			int row = sendService.auditSend(mId, status);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, null);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * scj-20171128-03 配送员详情
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/memberSend/sendDetails")
	@ResponseBody
	public BhResult sendDetails(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			MemberSend send = sendService.sendDetails(mId);
			if (send != null) {
				result = new BhResult(BhResultEnum.SUCCESS, send);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * scj-20171128-04 获取配送员身份证
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/memberSend/getIdentity")
	@ResponseBody
	public BhResult getIdentity(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			String str = sendService.getIdentity(mId);
			result = new BhResult(BhResultEnum.SUCCESS, str);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 *  * 获取媒体文件  *   * @param accessToken  *      接口访问凭证  * @param media_id
	 *  *      媒体文件id  *
	 */
	  
	  @RequestMapping("/baseimg")
	@ResponseBody
	public static WangEditorInfo downloadMedia(@RequestBody Map<String, String> map,HttpServletRequest request) {
		//String serverId = "P8lLdFpw8gP-NSQdKnCSDpUjCoNjtVIUycoK6zXnTy0iSWP1GZzvP_xKN9d6H-SD,5ZZlVYxINtNAQgB8oA2X4BUzrOqyaRR16LSyROsB38NYSB5TguOP7J-EqpSoNzeW,7vfErr0Ge-M986bNPc2xH3ODJz1PAVEnTio9nynPzv-BTmz_EqPQ6P5sgWOKrT-W";
		String serverId = map.get("serverId");
		String accessToken = WxJsApiUtil.getAccessToken(Contants.appId, Contants.appSecret);
		
		HttpURLConnection conn = null;
		WangEditorInfo w = new WangEditorInfo();
		ArrayList<String> list = new ArrayList<>();
		try {
			List<String> singleServerId = JsonUtils.stringToList(serverId);
			for (String string : singleServerId) {
				String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
				
			    requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", string);
				URL url = new URL(requestUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
				byte[] buff = new byte[100];
				int rc = 0;
				
				while ((rc = bis.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				String path = request.getSession(true).getServletContext().getRealPath("/");// 获得files目录的绝对路径
				byte[] filebyte = swapStream.toByteArray();//字节数组
				String key1 = "base64images/";//主键识别
				
				String returnString = RegExpValidatorUtils.downloadMedia(path, filebyte, key1);
				if (returnString.equals("0")) {
					
				}else{
					list.add(returnString);
				}
			}
			/*String targetFileName = StringUtil.genUuId() + getFileExtendName(filebyte);
			File targetFile = new File(path, targetFileName);
			byte2image(filebyte,path+targetFileName);
			
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}		
			StringBuffer key = new StringBuffer();
			
			key.append(targetFileName);
			upload myupload = new upload();
			String localFilePath = path + targetFileName;
			boolean bl = myupload.singleupload("aliyun", localFilePath, key.toString());
			StringBuffer realPath = new StringBuffer("http://bhshop.oss-cn-shenzhen.aliyuncs.com/");
			realPath.append("headimages/");
			realPath.append(targetFileName);
*//*
			if (bl) {
					list.add(realPath.toString());
			} else {
					w.setErrno(400);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		w.setData(list);
		return w;
	}

}
