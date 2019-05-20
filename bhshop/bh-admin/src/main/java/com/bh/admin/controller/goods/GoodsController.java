package com.bh.admin.controller.goods;

import com.alibaba.fastjson.JSON;
import com.bh.admin.mapper.goods.*;
import com.bh.admin.mapper.order.ExtFilesMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.mapper.user.MemberUserAddressMapper;
import com.bh.admin.pojo.goods.*;
import com.bh.admin.pojo.order.ExtFiles;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.service.*;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.vo.ShareResult;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.result.BhResult;
import com.bh.utils.AddressUtils;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.control.file.upload;
import net.sf.json.JSONArray;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;


@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
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
    @Autowired
    private GoodsOperLogMapper goodsOperLogMapper;
    @Autowired
    private MemberShopMapper memberShopMapper;
    @Autowired
    private ExtFilesMapper extFilesMapper;//文件
    @Autowired
    private GoodsMapper mapper;
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    @Autowired
    private JdGoodsMapper jdGoodsMapper;


    //将京东商品分类
    @RequestMapping("/jdfl")
    @ResponseBody
    public BhResult jdfl() {
        BhResult result = null;
        try {
            List<GoodsSku> goodsSkuList = goodsSkuMapper.selectByJdGoods();
            for (GoodsSku goodsSku : goodsSkuList) {
                List<JdGoods> jdGoodsList = jdGoodsMapper.selectByJdSkuNo(goodsSku.getJdSkuNo());
                if (jdGoodsList != null && jdGoodsList.size() > 0) {
                    Goods goods = new Goods();
                    goods.setId(goodsSku.getGoodsId());
                    String[] catId = jdGoodsList.get(0).getCatId().split(";");
                    if (catId !=  null && catId.length>=3) {
                        goods.setCatIdOne(Integer.valueOf(catId[0]).longValue());
                        goods.setCatIdTwo(Integer.valueOf(catId[1]).longValue());
                        goods.setCatId(Integer.valueOf(catId[2]).longValue());
                        mapper.updateByPrimaryKeySelective(goods);
                    }
                }

            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/bhfl")
    @ResponseBody
    public BhResult bhfl() {
        BhResult result = null;
        try {
            List<Goods> goodsList = mapper.selectByBhGoods();
            if (goodsList != null && goodsList.size() > 0) {
                for (Goods goods : goodsList) {
                    Goods goods1 = new Goods();
                    goods1.setId(goods.getId());
                    goods1.setCatId(Integer.valueOf(15762).longValue());
                    goods1.setCatIdOne(Integer.valueOf(9259).longValue());
                    goods1.setCatIdTwo(Integer.valueOf(9401).longValue());
                    mapper.updateByPrimaryKeySelective(goods1);
                }
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            e.printStackTrace();
        }
        return result;
    }


    /**
     * SCJ-20170912-02 批量删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/batchDelete")
    @ResponseBody
    public BhResult batchDelete(@RequestBody Map<String, String> map, HttpServletRequest request) {
        BhResult result = null;
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            String id = map.get("id");
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Goods entity = new Goods();
            entity.setSortnum((short) 29999);
            entity.setId(Integer.parseInt(id));
            map1.put("entity", entity);
            updateGoodsSortnum(map1);
            int row = goodsService.batchDelete(id, userId.toString());
            if (row == 0) {
                result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
            } else if (row == 999) {
                result = new BhResult(BhResultEnum.BATCH_DELETE_FAIL, null);
            } else {
                result = new BhResult(BhResultEnum.DELETE_SUCCESS, null);
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            e.printStackTrace();
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
            Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
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
        String isHotShop = null;
        String isNewShop = null;
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
            String isJd = map.get("isJd");
            String tagName = map.get("tagName"); //商品标签
            if (tagName != "" && tagName != null) {
                if (tagName.equals("1")) {
                    isNewShop = "1";
                }
                if (tagName.equals("2")) {
                    isHotShop = "1";
                }
            }
            if (StringUtils.isBlank(currentPage)) {
                currentPage = "1";
            }


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

            PageBean<Goods> msg = goodsService.selectByShopid(shopId, name, currentPage, Contants.PAGE_SIZE, catId,
                    saleType, status, startPrice, endPrice, topicType, id, skuNo, jdSkuNo, isJd, isHotShop, isNewShop);
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

                    String roolPath = request.getSession(true).getServletContext().getRealPath("/");
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
     * SCJ-20170929-01 多图片上传
     */

    @RequestMapping("/uploadGoodsImage")
    @ResponseBody
    public BhResult uploadGoodsImage(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
                                     HttpServletResponse response) {
        BhResult result = null;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isEmpty()) {
                String realName = null;
                String uuidName = null;
                StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
                StringBuffer key = new StringBuffer();
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
                try {

                    // 文件原来的名称
                    realName = files[i].getOriginalFilename();
                    // 得到这个文件的uuidname
                    uuidName = this.getUUIDFileName(files[i].getOriginalFilename());

                    String roolPath = request.getSession(true).getServletContext().getRealPath("/");
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
                        ExtFiles extFiles = new ExtFiles();
                        extFiles.setCateId(1);
                        extFiles.setFileUrl(realPath.toString());
                        extFiles.setFileType(0);
                        extFiles.setAddTime(new Date());
                        extFiles.setShopId(shopId);
                        extFiles.setIsDel(0);
                        extFilesMapper.insertSelective(extFiles);
                        result = new BhResult(200, "上传成功", realPath.toString());
                    } else {
                        result = new BhResult(400, "上传失败", null);
                    }
                } catch (Exception e) {
                    result = BhResult.build(500, "数据库操作失败!");

                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * SCJ-20170929-01 多图片上传
     */
    @RequestMapping("/uploadAdsImage")
    @ResponseBody
    public BhResult uploadAdsImage(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
                                   HttpServletResponse response) {
        BhResult result = null;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isEmpty()) {
                String realName = null;
                String uuidName = null;
                StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
                StringBuffer key = new StringBuffer();
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
                try {

                    // 文件原来的名称
                    realName = files[i].getOriginalFilename();
                    // 得到这个文件的uuidname
                    uuidName = this.getUUIDFileName(files[i].getOriginalFilename());

                    String roolPath = request.getSession(true).getServletContext().getRealPath("/");
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
                        ExtFiles extFiles = new ExtFiles();
                        extFiles.setCateId(3);
                        extFiles.setFileUrl(realPath.toString());
                        extFiles.setFileType(0);
                        extFiles.setAddTime(new Date());
                        extFiles.setShopId(shopId);
                        extFiles.setIsDel(0);
                        extFilesMapper.insertSelective(extFiles);
                        result = new BhResult(200, "上传成功", realPath.toString());
                    } else {
                        result = new BhResult(400, "上传失败", null);
                    }
                } catch (Exception e) {
                    result = BhResult.build(500, "数据库操作失败!");

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

                    String roolPath = request.getSession().getServletContext().getRealPath("/");
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
                        //result = new BhResult(200, "上传成功", realPath.toString());
                        ExtFiles extFiles = new ExtFiles();
                        extFiles.setCateId(2);
                        extFiles.setFileUrl(realPath.toString());
                        extFiles.setFileType(0);
                        extFiles.setAddTime(new Date());
                        extFiles.setIsDel(0);
                        extFiles.setShopId(shopId);
                        extFilesMapper.insertSelective(extFiles);
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
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Object sId = mapOne.get("shopId");
            Integer shopId = 1;
            if (sId != null) {
                shopId = (Integer) sId;
            }
            String goodBuyLimit = (String) map.get("goodBuyLimit"); //zlk
            String sendArea = (String) map.get("sendArea"); //2018.5.23  zlk
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
            String deductibleRate = (String) map.get("deductibleRate");//折扣率
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
            int row = goodsService.insertGoods(name, title, modelId, catId, shopCatId, brandId, sellPrice, marketPrice,
                    storeNums, desc, image, list, deliveryPrice, isShopFlag, isNew, refundDays, publicImage, saleType,
                    teamNum, teamEndTime, isPromote, timeUnit, teamPrice, isCreate, isJd, appintroduce, topicType,
                    shopId, tagIds, isPopular, catIdOne, catIdTwo, goodBuyLimit, sendArea, userId.toString(), deductibleRate);

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
	 * request.getSession().getServletContext().getRealPath("/"); //得到文件的输入流
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
	 * request.getSession().getServletContext().getRealPath("/");
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
    public BhResult updateGoods(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        BhResult result = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            String goodBuyLimit = (String) map.get("goodBuyLimit"); //商品限制数量
            String sendArea = (String) map.get("sendArea"); //限制区域
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
            String deductibleRate = (String) map.get("deductibleRate"); // 折扣率
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
                    topicType, tagIds, catIdOne, catIdTwo, goodBuyLimit, sendArea,deductibleRate);

            if (row == 0) {
                result = new BhResult(400, "请求失败", null);
            } else {
                GoodsOperLog goodsOperLog = new GoodsOperLog();
                goodsOperLog.setOpType("商家修改商品");
                goodsOperLog.setUserId(userId.toString());
                goodsOperLog.setOrderId("");
                String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
                goodsOperLog.setAdminUser(userName);
                goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
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
            String tagName = map.get("tagName");
            String isHot = map.get("isHot"); // 是否热门0否，1是
            String isFlag = map.get("isFlag");// 是否推荐0否，1是
            String sortnum = map.get("sortnum");// 排序
            int row = goodsService.changeValue(id, isHot, isFlag, sortnum, tagName);
            if (row == 0) {
                result = new BhResult(BhResultEnum.FAIL, null);
            } else {
                result = new BhResult(BhResultEnum.SUCCESS, null);
            }

			/*Goods goods=goodsService.selectBygoodsId(Integer.parseInt(id));
			goods.setTagName(tagName);
			goodsService.update(goods);*/
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
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            String id = map.get("id");
            int status = 1;
            int row = goodsService.updateStatus(id, status);
            if (row == 0) {
                result = BhResult.build(400, "请求失败！", null);
            } else {
                Goods entity = new Goods();
                entity.setSortnum((short) 29999);
                entity.setId(Integer.parseInt(id));
                map1.put("entity", entity);
                updateGoodsSortnum(map1);
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
    public BhResult goodsPutaway(@RequestBody Map<String, String> map, HttpServletRequest request) {
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
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Object sId = mapOne.get("shopId");
            Integer shopId = 1;
            if (sId != null) {
                shopId = (Integer) sId;
            }
            List<GoodsSku>	goodsSkuList=goodsSkuMapper.selectListByGoodsIdAndStatus(Integer.valueOf(id));
    		if(goodsSkuList.size()==0){
    			return result = BhResult.build(400, "该商品无sku，不允许提交申请！", null);
    		}
            MemberShop memberShop = goodsService.selectMemberShopBymId(shopId);
            if (memberShop.getDepositStatus().equals(2)) {
                int row = goodsService.goodsPutaway(id, 5, userId.toString());
                result = BhResult.build(200, "上架成功", null);
            } else {
                //如果该商家未有免审核的记录，则照原来的方法走
                int row = goodsService.goodsPutaway(id, status, userId.toString());
                if (row == 0) {
                    result = BhResult.build(400, "请求失败！", null);
                } else {
                    result = BhResult.build(200, "上架成功", null);
                }
            }
            //2018.7.18 zlk 加编辑时间
            Goods g = new Goods();
            g.setId(Integer.valueOf(id));
            g.setEdittime(new Timestamp(new Date().getTime()));
            mapper.updateByPrimaryKeySelective(g);
            //end


        } catch (Exception e) {
            result = BhResult.build(500, "数据库上架失败!");
            e.printStackTrace();
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
    public BhResult goodsSoldOut(@RequestBody Map<String, String> map, HttpServletRequest request) {
        BhResult result = null;
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            String id = map.get("id");
            String msg = map.get("outReason");
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Object sId = mapOne.get("shopId");
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Integer shopId = 0;
            if (sId != null) {
                shopId = (Integer) sId;
            }
            int status = 2;
            int row = goodsService.goodsSoldOut(id, status, msg, userId.toString());
            if (row == 1) {
                boolean falg = goodsService.isPtSoldOut(shopId, id);//判断是否是平台下架商家商品
                //System.out.println("谢元春"+falg);
                if (falg) {
                    BhResult r = goodsService.sendMsgToShop(id, msg, request);//发送短信通知商家商品下架原因
                    if (r.getStatus() == 200) {
                        //保存数据到数据库
                        int rowMsg = goodsService.saveGoodsMsg(r.getMsg(), id);
                        Goods entity = new Goods();
                        //Goods entity=goodsService.selectBygoodsId(Integer.parseInt(id));
                        entity.setSortnum((short) 29999);
                        entity.setId(Integer.parseInt(id));
                        map1.put("entity", entity);
                        updateGoodsSortnum(map1);
                        result = BhResult.build(200, "成功", rowMsg);
                    } else {
                        result = BhResult.build(400, "失败", "短信发送异常，但商品已经被下架！！");
                    }
                } else {
                    Goods entity = new Goods();
                    //Goods entity=goodsService.selectBygoodsId(Integer.parseInt(id));
                    entity.setSortnum((short) 29999);
                    entity.setId(Integer.parseInt(id));
                    map1.put("entity", entity);
                    updateGoodsSortnum(map1);
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
     * 平台商品的下架
     *
     * @param id
     * @return
     */
    @RequestMapping("/goodsPtSoldOut")
    @ResponseBody
    public BhResult goodsPtSoldOut(@RequestBody Map<String, String> map, HttpServletRequest request) {
        BhResult result = null;
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            String id = map.get("id");
            String msg = map.get("outReason");
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Object sId = mapOne.get("shopId");
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Integer shopId = 0;
            if (sId != null) {
                shopId = (Integer) sId;
            }
            int status = 2;
            int row = goodsService.goodsPtSoldOut(id, status, msg, userId.toString());
            if (row == 1) {
                boolean falg = goodsService.isPtSoldOut(shopId, id);//判断是否是平台下架商家商品
                //System.out.println("谢元春"+falg);
                if (falg) {
                    BhResult r = goodsService.sendMsgToShop(id, msg, request);//发送短信通知商家商品下架原因
                    if (r.getStatus() == 200) {
                        //保存数据到数据库
                        int rowMsg = goodsService.saveGoodsMsg(r.getMsg(), id);
                        Goods entity = new Goods();
                        //Goods entity=goodsService.selectBygoodsId(Integer.parseInt(id));
                        entity.setSortnum((short) 29999);
                        entity.setId(Integer.parseInt(id));
                        map1.put("entity", entity);
                        updateGoodsSortnum(map1);
                        result = BhResult.build(200, "成功", rowMsg);
                    } else {
                        result = BhResult.build(400, "失败", "短信发送异常，但商品已经被下架！！");
                    }
                } else {
                    Goods entity = new Goods();
                    //Goods entity=goodsService.selectBygoodsId(Integer.parseInt(id));
                    entity.setSortnum((short) 29999);
                    entity.setId(Integer.parseInt(id));
                    map1.put("entity", entity);
                    updateGoodsSortnum(map1);
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
    public BhResult checkGoodsPutaway(@RequestBody Map<String, String> map, HttpServletRequest request) {
        BhResult result = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            String id = map.get("id");
            String status = map.get("status");// 4拒绝5同意上架
            String reason = map.get("reason"); // 拒绝上架理由
            String fixedSale = map.get("fixedSale");// 已售数量
            int row = goodsService.checkGoodsPutaway(id, Integer.parseInt(status), reason, fixedSale);
            if (row == 0) {
                result = BhResult.build(400, "请求失败！", null);
            }else if (row == -1) {
                result =  BhResult.build(400, "该商品无sku！不允许上架！", null);
            } else if (row == 10000) {
                result = new BhResult(BhResultEnum.GOODS_NOT_EXIT, null);
            } else {
                GoodsOperLog goodsOperLog = new GoodsOperLog();
                goodsOperLog.setOpType("平台商品审核是否上架");
                goodsOperLog.setUserId(userId.toString());
                goodsOperLog.setOrderId("");
                String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
                goodsOperLog.setAdminUser(userName);
                goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
                result = BhResult.build(200, "审核成功", null);
            }
        } catch (Exception e) {
            result = BhResult.build(500, "数据库审核失败!");
            e.printStackTrace();
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
        String isHot = null;
        String isNew = null;
        try {
            String actZoneId = map.get("actZoneId");
            String name = map.get("name"); // 查询条件
            String status = map.get("status"); // 商品状态--3待审核，4拒绝上架，5已上架
            String currentPage = map.get("currentPage");// 当前第几页
            //String isHot = (String) map.get("isHot"); // 是否热门0否，1是
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
            String tagName = map.get("tagName"); //商品标签
            if (tagName.equals("1")) {
                isNew = "1";
                tagName = "";
            }
            if (tagName.equals("2")) {
                isHot = "1";
                tagName = "";
            }
            if (tagName.equals("3")) {
                tagName = "1";
            }
            if (tagName.equals("4")) {
                tagName = "2";
            }
            if (tagName.equals("5")) {
                tagName = "3";
            }
            if (tagName.equals("6")) {
                tagName = "4";
            }
            if (tagName.equals("7")) {
                tagName = "5";
            }
            if (StringUtils.isBlank(currentPage)) {
                currentPage = "1";
            }
//			PageBean<Goods> msg = goodsService.backgroundGoodsList(name, currentPage, Contants.PAGE_SIZE, status, isHot,isNew,
//					isFlag, saleType, startPrice, endPrice, topicType, id, skuNo, jdSkuNo, shopId,tagName,actZoneId);

            Map<String, Object> msg = goodsService.backgroundGoodsLists(name, currentPage, Contants.PAGE_SIZE, status, isHot, isNew,
                    isFlag, saleType, startPrice, endPrice, topicType, id, skuNo, jdSkuNo, shopId, tagName, actZoneId);


            result = new BhResult(BhResultEnum.GAIN_SUCCESS, msg);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);

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
    @RequestMapping("/apiGoodsList")
    @ResponseBody
    public BhResult apiGoodsList(@RequestBody Goods entity) {
        BhResult r = null;
        try {
            PageBean<Goods> page = goodsService.apiGoodsList(entity);
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
            Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
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
            Integer shopId = 1;
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
     * @Description: 更新商家名称
     * @author xieyc
     * @date 2018年3月6日 下午2:28:29
     */
    @RequestMapping("/updateShopName")
    @ResponseBody
    public BhResult updateShopName(HttpServletRequest request, @RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            MemberShop memberShop=new MemberShop();
            String mId=map.get("m_id");
            String shopName=map.get("shop_name");
            if(shopName.length()>20){
            	return  result = new BhResult(400,"名称太长,不能超过20个字符",null);
            }
            memberShop.setmId(Integer.valueOf(mId));
            memberShop.setShopName(shopName);
            Integer row = goodsService.updateShopName(memberShop);
            if (row == 1) {
                result = new BhResult(BhResultEnum.SUCCESS, row);
            }else  if(row==999){
            	result = new BhResult(400,"名称已存在",null);
            } 
            else {
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
            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
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
     * @Description: 移动端更新商家名称
     * @author xieyc
     * @date 2018年3月6日 下午2:28:29
     */
    @RequestMapping("/mupdateShopName")
    @ResponseBody
    public BhResult mupdateShopName(HttpServletRequest request, @RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
        	MemberShop memberShop=new MemberShop();
            String mId=map.get("m_id");
            String shopName=map.get("shop_name");
            if(shopName.length()>20){
            	return  result = new BhResult(400,"名称太长,不能超过20个字符",null);
            }
            memberShop.setmId(Integer.valueOf(mId));
            memberShop.setShopName(shopName);
                Integer row = goodsService.updateShopName(memberShop);
                if (row == 1) {
                    result = new BhResult(BhResultEnum.SUCCESS, row);
                } else  if(row==999){
                	result = new BhResult(400,"名称已存在",null);
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
     * @Description: 移动端更新商家店铺（退押金）
     * @author xieyc
     * @date 2018年3月6日 下午2:28:29
     */
    @RequestMapping("/mupdateShopOrderPrice")
    @ResponseBody
    public BhResult mupdateShopOrderPrice(HttpServletRequest request) {
        BhResult result = null;
        try {
            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
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
    public BhResult shopDetails(HttpServletRequest request ) {
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
            MemberShop shop = goodsService.getShopDetails(shopId, userId);
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
            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            if (user != null) {
                Long shopId = user.getShopId();
                Long userId = user.getUserId();
                if (shopId == null) {
                    shopId = (long) 1;
                }
                HashMap<String,Object> shop = goodsService.mgetShopDetails(shopId.intValue(), userId.intValue());
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
	/*@RequestMapping("/setHotNewAndFlag")
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
	}*/

    /**
     * 平台标签修改
     *
     * @param map
     * @return
     */
    @RequestMapping("/setPtHotNewAndFlag")
    @ResponseBody
    public BhResult setPtHotNewAndFlag(@RequestBody Map<String, String> map, HttpServletRequest request) {
        BhResult result = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            String id = map.get("id");
            String name = map.get("tagName");

            int row = goodsService.setPtHotNewAndFlag(id, name);

            if (row == 0) {
                result = new BhResult(BhResultEnum.FAIL, null);
            } else {
                GoodsOperLog goodsOperLog = new GoodsOperLog();
                goodsOperLog.setOpType("平台修改商品标签");
                goodsOperLog.setUserId(userId.toString());
                goodsOperLog.setOrderId("");
                String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
                goodsOperLog.setAdminUser(userName);
                goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
                result = new BhResult(200, "成功", null);
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
        }
        return result;
    }

    /**
     * 商家标签修改
     *
     * @param map
     * @return
     */
    @RequestMapping("/setHotNewAndFlag")
    @ResponseBody
    public BhResult setHotNewAndFlag(@RequestBody Map<String, String> map, HttpServletRequest request) {
        BhResult result = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            String id = map.get("id");
            String name = map.get("tagName");
            String publicImage = (String) map.get("image");// 宣传图
            int row = goodsService.setHotNewAndFlag(id, name, publicImage);

            if (row == 0) {
                result = new BhResult(BhResultEnum.FAIL, null);
            } else {
                GoodsOperLog goodsOperLog = new GoodsOperLog();
                goodsOperLog.setOpType("商家修改商品标签");
                goodsOperLog.setUserId(userId.toString());
                goodsOperLog.setOrderId("");
                String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
                goodsOperLog.setAdminUser(userName);
                goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
                result = new BhResult(200, "成功", null);
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
            Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
            ShareResult share = goodsService.getShareGoodsDetails(skuId, teamNo, userId, orderId, shareType, shareUrl,
                    member);
            if (share != null) {
                result = new BhResult(BhResultEnum.SUCCESS, share);
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

    //将不连续的连在一起,只调用一次
    @RequestMapping("/updateGoodsSortnum1")
    @ResponseBody
    public BhResult updateGoodsSortnum1() {
        BhResult r = null;
        try {
            goodsService.updateGoodsSortnum1();
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

            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            if (user != null && !user.equals("") && user.getUserId() != null) {
                if (user.getShopId() == null) {
                    long shopId = 1;// 平台
                    user.setShopId(shopId);
                }
                // 热门上架商品
                List<Goods> list = goodsService.selectHotByShopId(user.getShopId().intValue());

                if (list != null && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setGoodStatus("0");
                    }
                    result = new BhResult(BhResultEnum.SUCCESS, list);

                } else {// 如果热门商品没有，则返回所有的已上架商品
                    List<Goods> list2 = goodsService.selectAllByShopId(user.getShopId().intValue());
                    if (list2.size() > 0) {
                        for (int i = 0; i < list2.size(); i++) {
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
     * zlk-201802012-12
     *
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
     *
     * @return
     */
    @RequestMapping("/mSelectAllModel")
    @ResponseBody
    public BhResult mSelectAllModel(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String catid = map.get("catid");
            List<GoodsModel> list = goodsModelService.selectAllModel(catid);
            if (list != null) {
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
     *
     * @param map
     * @return
     */
    @RequestMapping("/mSelectLinkedList")
    @ResponseBody
    public BhResult mSelectLinkedList(HttpServletRequest request) {
        BhResult result = null;
        try {
            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            if (user != null && !user.equals("") && user.getUserId() != null) {
                if (user.getShopId() == null) {
                    long shopId = 1;// 平台
                    user.setShopId(shopId);
                }
                List<GoodsShopCategory> msg = goodsShopCategoryService.selectLinkedList(user.getShopId().intValue());
                if (msg != null) {
                    result = new BhResult(200, "成功", msg);
                } else {
                    result = BhResult.build(400, "暂无数据！");
                }
            } else {
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
     *
     * @return
     */
    @RequestMapping("/mSelectThreeLevel")
    @ResponseBody
    public BhResult mSelectThreeLevel() {
        BhResult result = null;
        try {
            List<GoodsCategory> goodsCategory = goodsCategoryService.selectThreeLevel();
            if (goodsCategory != null) {
                result = new BhResult(200, "成功", goodsCategory);
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
     * 根据分类id查品牌
     * zlk
     */
    @RequestMapping("/mGetBrandByCategory")
    @ResponseBody
    public BhResult mGetBrandByCategory(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String id = map.get("id");
            Long catId = Long.parseLong(id);
            List<GoodsBrand> list = goodsCategoryService.getBrandByCategory(catId);
            if (list.size() > 0) {
                result = new BhResult(200, "查询成功！", list);
            } else {
                result = new BhResult(200, "当前分类下没有对应的品牌！", null);
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
     *
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

            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
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
            /*String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }*/
            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            Integer userId=1;
            if (user != null && !user.equals("") && user.getUserId() != null) {
                if (user.getShopId() == null) {
                    long shopId = 1;// 平台
                    user.setShopId(shopId);
                    userId=user.getUserId().intValue();
                }
                String goodBuyLimit = (String) map.get("goodBuyLimit"); //zlk
                String sendArea = (String) map.get("sendArea"); //zlk
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
                String deductibleRate = (String) map.get("deductibleRate");//折扣率
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
                int row = goodsService.insertGoods(name, title, modelId, catId, shopCatId, brandId, sellPrice, marketPrice,
                        storeNums, desc, image, list, deliveryPrice, isShopFlag, isNew, refundDays, publicImage, saleType,
                        teamNum, teamEndTime, isPromote, timeUnit, teamPrice, isCreate, isJd, appintroduce, topicType,
                        user.getShopId().intValue(), tagIds, isPopular, catIdOne, catIdTwo, goodBuyLimit, sendArea, userId.toString(), deductibleRate);
                if (row == 0) {
                    result = new BhResult(400, "商品添加失败", null);
                } else {
                    result = new BhResult(200, "商品添加成功", row);
                }
            } else {
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
     *
     * @param map
     * @return
     */
    @RequestMapping("/mBatchAddGoodsSku")
    @ResponseBody
    public BhResult mBatchAddGoodsSku(@RequestBody Map<String, Object> map) {
        BhResult result = null;
        try {
            String goodsId = (String) map.get("goodsId");
            JSONArray list = JSONArray.fromObject(map.get("list"));
            int row = service.batchAddGoodsSku(goodsId, list);
            if (row == 0) {
                result = BhResult.build(400, "sku新增失败！", null);
            } else {
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

            String sort = map.get("sort"); //按 时间(1降序、2升序)、销量(3降序、5升序)、库存(6降序、7升序)、上下架 排序(8上架、9下架)
            if (StringUtils.isBlank(sort)) {
                sort = "1";
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
            MBusEntity user =(MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            if (user != null && !user.equals("") && user.getUserId() != null) {
                if (user.getShopId() == null) {
                    long shopId = 1;// 平台
                    user.setShopId(shopId);
                }
//			      PageBean<Goods> msg1 = goodsService.selectByShopid(user.getShopId().intValue(), name, currentPage, Contants.PAGE_SIZE, catId,
//					saleType, status, startPrice, endPrice, topicType, id, skuNo, jdSkuNo);
                PageBean<Goods> msg = goodsService.mSelectByShopid(user.getShopId().intValue(), name, currentPage, Contants.PAGE_SIZE, catId,
                        saleType, status, startPrice, endPrice, topicType, id, skuNo, jdSkuNo, sort);
                if (msg != null) {
                    result = new BhResult(200, "成功", msg);
                } else {
                    result = BhResult.build(400, "暂无数据！");
                }
          } else {
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
     *
     * @param map
     * @return
     */
    @RequestMapping("/mUpdateById")
    @ResponseBody
    public BhResult mUpdateById(@RequestBody Goods goods,HttpServletRequest request) {
        BhResult result = null;

        try {
            //从session中取当前操作者的信息
            MBusEntity entity = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            if (goods.getGoodsStatus().equals("1") || goods.getGoodsStatus().equals("2")) {
                // 设置商品的上下架
                if (goods.getGoodsStatus().equals("1")) {
                    //上架
                    Goods goods2 = new Goods();
                    goods2.setStatus(3); //申请上架
                    goods2.setId(goods.getId());
                    goods2.setUpTime(new Date());
                    try {
                        GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
                        goodsOperLog.setOpType("商家商品申请上架");
                        goodsOperLog.setOrderId("");
                        if(entity != null) {
                            if(entity.getUserId() != null) {
                                goodsOperLog.setUserId(entity.getUserId().toString());
                                String userName = memberShopMapper.selectUsernameBymId(entity.getUserId().intValue()); //查找操作人
                                goodsOperLog.setAdminUser(userName);
                            }
                        }
                        int operLogRow = goodsOperLogMapper.insertGoodsById(goods.getId(), goodsOperLog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int row = goodsService.mUpdateById(goods2);
                } else {
                    //下架
                    Goods goods2 = new Goods();
                    goods2.setStatus(2);  //下架
                    goods2.setId(goods.getId());
                    goods2.setDownTime(new Date());
                    try {
                        GoodsOperLog goodsOperLog = new GoodsOperLog();//插入操作人记录
                        goodsOperLog.setOpType("商家商品下架");
                        goodsOperLog.setOrderId("");
                        if(entity != null) {
                            if(entity.getUserId() != null) {
                                goodsOperLog.setUserId(entity.getUserId().toString());
                                String userName = memberShopMapper.selectUsernameBymId(entity.getUserId().intValue()); //查找操作人
                                goodsOperLog.setAdminUser(userName);
                            }
                        }
                        int operLogRow = goodsOperLogMapper.insertGoodsById(goods.getId(), goodsOperLog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int row = goodsService.mUpdateById(goods2);
                }


            } else if (goods.getGoodsStatus().equals("3") || goods.getGoodsStatus().equals("5")) {

                if (goods.getGoodsStatus().equals("3")) {
                    // 设置商品的新品
                    Goods goods2 = new Goods();
                    goods2.setIsNewShop(1);  //店铺展示新品
                    goods2.setId(goods.getId());
                    if (org.apache.commons.lang.StringUtils.isNotBlank(goods.getPublicimg())) {
                        goods2.setPublicimg(goods.getPublicimg());
                    }
                    int row = goodsService.mUpdateById(goods2);
                } else {
                    //取消新品
                    Goods goods2 = new Goods();
                    goods2.setIsNewShop(0);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                }
            } else if (goods.getGoodsStatus().equals("6") || goods.getGoodsStatus().equals("7")) {
                if (goods.getGoodsStatus().equals("6")) {
                    // 设置商品的推荐
                    Goods goods2 = new Goods();
                    goods2.setIsShopFlag(true);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                } else {
                    // 取消商品的推荐
                    Goods goods2 = new Goods();
                    goods2.setIsShopFlag(false);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                }
            } else if (goods.getGoodsStatus().equals("8") || goods.getGoodsStatus().equals("9")) {
                if (goods.getGoodsStatus().equals("8")) {
                    // 设置商品的热门
                    Goods goods2 = new Goods();
                    goods2.setIsHotShop(1);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                } else {
                    // 取消商品的热门
                    Goods goods2 = new Goods();
                    goods2.setIsHotShop(0);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                }
            } else if (goods.getGoodsStatus().equals("10") || goods.getGoodsStatus().equals("11")) {
                if (goods.getGoodsStatus().equals("10")) {
                    // 删除商品
                    Goods goods2 = new Goods();
                    goods2.setStatus(1);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                    //删除商品 日志
                    GoodsOperLog goodsOperLog = new GoodsOperLog();
                    goodsOperLog.setOpType("删除商品");
                    goodsOperLog.setGoodId(goods.getId().toString());
                    goodsOperLog.setUserId(entity.getUserId().toString());
                    goodsOperLog.setOrderId("");
                    goodsOperLog.setOpTime(new Date());
                    String userName = memberShopMapper.selectUsernameBymId(entity.getUserId().intValue()); //查找操作人
                    goodsOperLog.setAdminUser(userName);
//                    goodsOperLogMapper.insertSelective(goodsOperLog);
                    goodsOperLogMapper.insertGoodsById(goods.getId(), goodsOperLog);
                } else {
                    //取消删除
                    Goods goods2 = new Goods();
                    goods2.setStatus(0);
                    goods2.setId(goods.getId());
                    int row = goodsService.mUpdateById(goods2);
                }
            }
            result = new BhResult(200, "修改成功", null);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
        }
        return result;
    }

    /**
     * zlk-20173.16
     * 移动端 修改订单地址
     *
     * @param map
     * @return
     */
    @RequestMapping("/mUpdateOrder")
    @ResponseBody
    public BhResult mUpdateOrder(@RequestBody Map<String, Object> map) {
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
            List<GoAddressArea> go1 = goAddressAreaMapper.selectById(p);

            GoAddressArea c = new GoAddressArea();
            c.setId(Integer.valueOf(city));
            List<GoAddressArea> go3 = goAddressAreaMapper.selectById(c);

            GoAddressArea a = new GoAddressArea();
            a.setId(Integer.valueOf(area));
            List<GoAddressArea> go5 = goAddressAreaMapper.selectById(a);

            GoAddressArea f = new GoAddressArea();
            f.setId(Integer.valueOf(four));
            List<GoAddressArea> go7 = goAddressAreaMapper.selectById(f);

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
            if (go7.size() > 0) {
                ma.setFourname(go7.get(0).getName());
            }
            int row = memberUserAddressMapper.insertSelective(ma);
            if (row == 0) {
                result = BhResult.build(400, "修改失败！", null);
            } else {
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
            String goodBuyLimit = (String) map.get("goodBuyLimit");//商品限购数量
            String sendArea = (String) map.get("sendArea");//2018.5.23 zlk
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
            String deductibleRate = (String) map.get("deductibleRate"); // 折扣率
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
                    topicType, tagIds, catIdOne, catIdTwo, goodBuyLimit, sendArea,deductibleRate);
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
     *
     * @param map
     * @return
     */
    @RequestMapping("/mBatchUpdateGoodsSku")
    @ResponseBody
    public BhResult mBatchUpdateGoodsSku(@RequestBody Map<String, Object> map) {
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
     *
     * @param map
     * @return
     */
    @RequestMapping("/mGetShopId")
    @ResponseBody
    public BhResult mGetShopId(HttpServletRequest request) {
        BhResult r = null;
        try {
            //获取当期的移动端登录的用户信息
            MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
            if (user == null) {
                return r = new BhResult(100, "您还没有登录，请重新登录", null);
            } else if (user.getUsername().equals("admin")) {
                return r = new BhResult(200, "获取成功", 0);
            } else if (user.getShopId() == null) {
                return r = new BhResult(200, "获取成功", 0);
            } else {
                r = new BhResult(200, "获取成功", user.getShopId());
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
     * 设为平台新品
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
            PageBean<Goods> page = goodsService.newGoodsList(entity);
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
    public BhResult goodsMsgList(@RequestBody GoodsMsg goodsMsg, HttpServletRequest request) {
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
            PageBean<GoodsMsg> page = goodsService.goodsMsgList(goodsMsg);
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
    public BhResult saveChatRecord(@RequestBody InteractiveRecord record, HttpServletRequest request) {
        BhResult result = null;
        try {
            int row = goodsService.saveChatRecord(record);
            if (row == 1) {
                result = new BhResult(200, "成功", row);
            } else {
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
    public BhResult updateMsgstate(@RequestBody Map<String, Object> map, HttpServletRequest request) {
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
            String id = (String) map.get("id");
            int row = goodsService.updateMsgstate(id, shopId);
            r = new BhResult(200, "成功", row);
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
    public BhResult updateGoodsMsg(@RequestBody Map<String, Object> map, HttpServletRequest request) {
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
            int row = goodsService.updateGoodsMsg(shopId);
            r = new BhResult(200, "成功", row);
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
    public BhResult getUnreadNum(@RequestBody Map<String, Object> map, HttpServletRequest request) {
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
            int num = goodsService.getUnreadNum(shopId);
            r = new BhResult(200, "成功", num);
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
            r = new BhResult(200, "成功", null);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            r = BhResult.build(500, "操作异常!");
            return r;
        }
    }

    /**
     * @Description: zlk
     * @author 商品限购数量修改
     * @date 2018年4月28日 下午4:26:53
     */
    @RequestMapping("/updateGoodBuyLimit")
    @ResponseBody
    public BhResult updateGoodBuyLimit(@RequestBody Goods good) {
        BhResult r = null;
        try {
            Goods g = new Goods();
            g.setId(good.getId());
            g.setGoodBuyLimit(good.getGoodBuyLimit());
            goodsService.update(g);
            r = new BhResult(200, "成功", null);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            r = BhResult.build(500, "操作异常!");
            return r;
        }
    }


    /**
     * @Description: cheng
     * @author 商品修改是否可见或者隐藏
     * @date 2018年4月28日 下午4:26:53
     */
    @RequestMapping("/updateGoodVisible")
    @ResponseBody
    public BhResult updateGoodVisible(@RequestBody Goods good, HttpServletRequest request) {
        BhResult r = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            if (good != null && good.getId() != null && good.getVisible() != null) {
                Goods g = new Goods();
                g.setId(good.getId());
                g.setVisible(good.getVisible());
                goodsService.update(g);
                GoodsOperLog goodsOperLog = new GoodsOperLog();
                goodsOperLog.setOpType("平台修改商品是否可见");
                goodsOperLog.setUserId(userId.toString());
                goodsOperLog.setOrderId("");
                String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
                goodsOperLog.setAdminUser(userName);
                goodsOperLogMapper.insertGoodsById(good.getId(), goodsOperLog);
                r = new BhResult(200, "操作成功", null);
            } else {
                r = new BhResult(400, "参数不能为空", null);
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            r = BhResult.build(500, "操作异常!");
            return r;
        }
    }

    /**
     * 将商品添加到分类列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateActZone", method = RequestMethod.POST)
    @ResponseBody
    public BhResult updateActZone(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        BhResult result = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Integer goodsId = (Integer) map.get("goodsId");
            String id = goodsId.toString();
            String zoneId = (String) map.get("zoneId");
            Integer skuId = (Integer) map.get("skuId");
            int row = goodsService.updateActZone(zoneId, goodsId, skuId);
            if (row == 0) {
                result = new BhResult(400, "操作失败", null);
            } else {
                GoodsOperLog goodsOperLog = new GoodsOperLog();
                goodsOperLog.setOpType("平台商品添加到专区");
                goodsOperLog.setUserId(userId.toString());
                goodsOperLog.setOrderId("");
                String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
                goodsOperLog.setAdminUser(userName);
                goodsOperLogMapper.insertGoodsById(Integer.parseInt(id), goodsOperLog);
                result = new BhResult(200, "操作成功", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = BhResult.build(500, "数据库操作失败!");
            LoggerUtil.getLogger().error(e.getMessage());
        }
        return result;
    }

    /**
     * 获取所有操作日志
     */
    @RequestMapping(value = "/selectAlloperation", method = RequestMethod.POST)
    @ResponseBody
    public BhResult selectAlloperation(@RequestBody Map<String, Object> map) {
        BhResult result = null;
        try {

            Integer currentPage = (Integer) map.get("currentPage");
            String adminUser = (String) map.get("adminUser");
            String opType = (String) map.get("opType");
            String goodId = (String) map.get("goodId");
            String orderId = (String) map.get("orderId");

            PageBean<GoodsOperLog> msg = goodsService.selectAlloperation(currentPage.toString(), Contants.PAGE_SIZE, adminUser, opType, goodId, orderId);
            if (msg == null) {
                result = new BhResult(400, "操作失败", null);
            } else {
                result = new BhResult(200, "操作成功", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = BhResult.build(500, "数据库操作失败!");
            LoggerUtil.getLogger().error(e.getMessage());
        }
        return result;
    }

    @RequestMapping("/testInsertDesc")
    @ResponseBody
    public BhResult testInsertDesc(@RequestBody Goods entity) {
        BhResult r = null;
        try {
            String desc = "<div align='center'><img src='http://img20.360buyimg.com/vc/jfs/t3502/334/678886442/169138/ae6c2d34/5810a2b4Nbd8649c4.jpg' alt='' /><br /><img src='http://img20.360buyimg.com/vc/jfs/t3715/197/656715487/87378/43368a7/5810a2b5N69f79551.jpg' alt='' /><br /><img src='http://img20.360buyimg.com/vc/jfs/t3541/314/673136631/118363/8cbc4ade/58116668Na1b908e0.jpg' alt='' /><br /><img src='http://img20.360buyimg.com/vc/jfs/t3574/144/676675592/152918/1e5964a8/5810a2b5N2f0ce31a.jpg' alt='' /><img src='data:image/png;base64,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…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' alt='' /><br /></div>";
            entity.setDescription(desc);
            goodsService.testInsertDesc(entity);
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            logger.error(e.getMessage());
            e.printStackTrace();
            return r;
        }
        return r;
    }

    /**
     * <p>Description: 后台商品新增（new）</p>
     *
     * @author scj
     * @date 2018年7月10日
     */
    @RequestMapping("/addGoods")
    @ResponseBody
    public BhResult add(@RequestBody Goods entity, HttpServletRequest request) {
        BhResult r = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Object sId = mapOne.get("shopId");
            Integer shopId = 1;
            if (sId != null) {
                shopId = (Integer) sId;
            }
            entity.setUserId(userId);
            entity.setShopId(shopId);
            int data = goodsService.addGoods(entity);
            if (data == 666) {
                r = new BhResult();
                r.setStatus(BhResultEnum.JDSKUNO_ISEXICT.getStatus());
                r.setMsg(BhResultEnum.JDSKUNO_ISEXICT.getMsg());
                r.setData(data);
                return r;
            }
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            logger.error(e.getMessage());
            e.printStackTrace();
            return r;
        }
        return r;
    }

    /**
     * <p>Description: 后台商品修改（new）</p>
     *
     * @author scj
     * @date 2018年7月10日
     */
    @RequestMapping("/editGoods")
    @ResponseBody
    public BhResult editGoods(@RequestBody Goods entity, HttpServletRequest request) {
        BhResult r = null;
        try {
            String token = request.getHeader("token");
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Strings strings = jedisUtil.new Strings();
            String userJson = strings.get(token);
            Map mapOne = JSON.parseObject(userJson, Map.class);
            Integer userId = (Integer) mapOne.get("userId");
            if (StringUtils.isBlank(String.valueOf(userId))) {
                userId = 1;
            }
            Object sId = mapOne.get("shopId");
            ;
            Integer shopId = 1;
            if (sId != null) {
                shopId = (Integer) sId;
            }
            entity.setUserId(userId);
            entity.setShopId(shopId);
            goodsService.editGoods(entity);
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            logger.error(e.getMessage());
            e.printStackTrace();
            return r;
        }
        return r;
    }      
	/**
	 * <p>
	 * Description: 移动端商品新增（new）
	 * </p>
	 *
	 * @author scj
	 * @date 2018年7月10日
	 */
	@RequestMapping("/addGoodsM")
	@ResponseBody
	public BhResult addGoodsM(@RequestBody Goods entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user =(MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				Long userId = user.getUserId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				if(userId==null){
					userId = (long) 1;
				}
				entity.setUserId(userId.intValue());
				entity.setShopId(shopId.intValue());
				int data = goodsService.addGoodsM(entity);
				if (data == 666) {
					r = new BhResult();
					r.setStatus(BhResultEnum.JDSKUNO_ISEXICT.getStatus());
					r.setMsg(BhResultEnum.JDSKUNO_ISEXICT.getMsg());
					r.setData(data);
				}else{
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * <p>
	 * Description: 移动端商品修改（new）
	 * </p>
	 *
	 * @author scj
	 * @date 2018年7月10日
	 */
	@RequestMapping("/mEditGoods")
	@ResponseBody
	public BhResult mEditGoods(@RequestBody Goods entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				Long userId = user.getUserId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				if(userId==null){
					userId = (long) 1;
				}
				entity.setUserId(userId.intValue());
				entity.setShopId(shopId.intValue());
				goodsService.mEditGoods(entity);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}      
	
	
	 /**
     * @Description: 修改商家退货地址
     * @author fanjh
     * @date 2018年9月3日 下午2:28:29
     */
    @RequestMapping("/updateShopAddress")
    @ResponseBody
    public BhResult updateShopAddress(HttpServletRequest request, @RequestBody Map<String, Object> map) {
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
            String address=(String) map.get("address");
            MemberShop memberShop=new MemberShop();
            memberShop.setmId(shopId);
            memberShop.setReturnAddress(address);
            Integer row = goodsService.updateShopAddress(memberShop);
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
	
	
}
