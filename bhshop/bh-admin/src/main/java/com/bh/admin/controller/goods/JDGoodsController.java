package com.bh.admin.controller.goods;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.JdGoods;
import com.bh.admin.service.JDGoodsService;
import com.bh.admin.util.ExcelFileGenerator;
import com.bh.enums.BhResultEnum;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.goods.CategoryList;
import com.bh.jd.bean.goods.CategoryVo;
import com.bh.jd.bean.goods.SearchResult;
import com.bh.jd.bean.goods.SellPriceResult;
import com.bh.jd.bean.goods.SimilarSku;
import com.bh.jd.bean.goods.Sku;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Controller
@RequestMapping("/api")
public class JDGoodsController {
	
	@Autowired
	private JDGoodsService jdGoodsService;
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;

    /**
     * 3.1 获取商品池编号接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getPageNum")
    @ResponseBody
    public BhResult getPageNum(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {

            result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getPageNum());
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 3.2 获取池内商品编号接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getSku")
    @ResponseBody
    public BhResult getSku(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String pageNum = map.get("pageNum");
            result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSku(pageNum));
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 3.3 获取池内商品编号接口-品类商品池
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getSkuByPage")
    @ResponseBody
    public BhResult getSkuByPage(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String pageNum = map.get("pageNum");
            String pageNo = map.get("pageNo");
            if (StringUtils.isEmpty(pageNo)) {
                result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
            }
            if (StringUtils.isEmpty(pageNum)) {
                result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
            } else {
                result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSkuByPage(pageNum, pageNo));
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 3.4 获取商品详细信息接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getDetail")
    @ResponseBody
    public BhResult getDetail(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String sku = map.get("sku");
            String isShow = map.get("isShow");
            result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getDetail(sku, isShow));
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 3.12 商品搜索接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/search/search")
    @ResponseBody
    public BhResult search(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String keyword = map.get("keyword");
            String catId = map.get("catId");
            String pageIndex = map.get("pageIndex");
            String pageSize = map.get("pageSize");
            String min = map.get("min");
            String max = map.get("max");
            String brands = map.get("brands");
            SearchResult s = jdGoodsService.search(keyword, catId, pageIndex, pageSize, min, max, brands);
            result = new BhResult(BhResultEnum.SUCCESS, s);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 3.15 查询分类信息接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getCategory")
    @ResponseBody
    public BhResult getCategory(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            //参数："1", "10", "670", "1"
            String cid = map.get("cid");
            result = new BhResult();
            CategoryVo vo = jdGoodsService.getCategory(cid);
            result = new BhResult(200, "查询成功", vo);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 3.16 查询分类信息接口
     *
     * @param map
     * @return
     */
   /* @RequestMapping("/product/getCategorys")
    @ResponseBody
    public BhResult getCategorys(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            //参数："1", "10", "670", "1"
            //String pageNo, String pageSize, String parentId, String catClass
            String pageNo = map.get("pageNo");
            String pageSize = map.get("pageSize");
            String parentId = map.get("parentId");
            String catClass = map.get("catClass");
            result = new BhResult();
            CategoryList map1 = jdGoodsService.getCategorys(pageNo, pageSize, parentId, catClass);
            result = new BhResult(200, "查询成功", map1);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }*/
    
    @RequestMapping("/product/getCategorys")
    @ResponseBody
    public BhResult getCategorys(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            //参数："1", "10", "670", "1"
            //String pageNo, String pageSize, String parentId, String catClass
            String pageNo = map.get("pageNo");
            String pageSize = map.get("pageSize");
            String parentId = map.get("parentId");
            String catClass = map.get("catClass");
            result = new BhResult();
            CategoryList map1 = jdGoodsService.getCategorys(pageNo, pageSize, parentId, catClass);
            result = new BhResult(200, "查询成功", map1);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    //获取京东所有分类列表
    @RequestMapping("/product/getCategorys2")
    @ResponseBody
    public BhResult getCategorys2() {
        BhResult result = null;
        try {
            //参数："1", "10", "670", "1"
            //String pageNo, String pageSize, String parentId, String catClass
            String pageNo = "1";
            String pageSize = "5000";
            String parentId = "";
            String catClass = "0";
            result = new BhResult();
            CategoryList map1 = jdGoodsService.getCategorys(pageNo, pageSize, parentId, catClass);
            for (int i=0; i<map1.getCategorys().size() ; i++) {
				GoodsCategory goodsCategory=new GoodsCategory();
				goodsCategory.setId(map1.getCategorys().get(i).getCatId().longValue());
				goodsCategory.setName(map1.getCategorys().get(i).getName());
				goodsCategory.setReid(map1.getCategorys().get(i).getParentId().longValue());
				goodsCategory.setSortnum((short) (i+1));
				goodsCategory.setFlag(true);
				goodsCategory.setIsLast(false);
				goodsCategory.setSeries((short) 1);
				goodsCategory.setAdId(0);
				goodsCategory.setImage("");
				goodsCategory.setIsJd(1);
				goodsCategoryMapper.insert(goodsCategory);
				parentId=map1.getCategorys().get(i).getCatId().toString();
				catClass = "1";
				CategoryList map2 = jdGoodsService.getCategorys(pageNo, pageSize, parentId, catClass);
				for (int j=0; j<map2.getCategorys().size() ; j++) {
					GoodsCategory goodsCategory1=new GoodsCategory();
					goodsCategory1.setId(map2.getCategorys().get(j).getCatId().longValue());
					goodsCategory1.setName(map2.getCategorys().get(j).getName());
					goodsCategory1.setReid(map2.getCategorys().get(j).getParentId().longValue());
					goodsCategory1.setSortnum((short) (j+1));
					goodsCategory1.setFlag(true);
					goodsCategory1.setIsLast(false);
					goodsCategory1.setSeries((short) 2);
					goodsCategory1.setAdId(0);
					goodsCategory1.setImage("");
					goodsCategory1.setIsJd(1);
					goodsCategoryMapper.insert(goodsCategory1);
					parentId=map2.getCategorys().get(j).getCatId().toString();
					catClass = "2";
					CategoryList map3 = jdGoodsService.getCategorys(pageNo, pageSize, parentId, catClass);
					for (int k=0; k<map3.getCategorys().size() ; k++) {
						GoodsCategory goodsCategory2=new GoodsCategory();
						goodsCategory2.setId(map3.getCategorys().get(k).getCatId().longValue());
						goodsCategory2.setName(map3.getCategorys().get(k).getName());
						goodsCategory2.setReid(map3.getCategorys().get(k).getParentId().longValue());
						goodsCategory2.setSortnum((short) (k+1));
						goodsCategory2.setFlag(true);
						goodsCategory2.setIsLast(true);
						goodsCategory2.setSeries((short) 3);
						goodsCategory2.setAdId(0);
						goodsCategory2.setImage("");
						goodsCategory2.setIsJd(1);
						goodsCategoryMapper.insert(goodsCategory2);
					}
				}
			}
            result = new BhResult(200, "添加成功", map1);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 3.16 查询分类信息接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getCategorys1")
    @ResponseBody
    public BhResult getCategorys1(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            //参数："1", "10", "670", "1"
            //String pageNo, String pageSize, String parentId, String catClass
            String pageNo = map.get("pageNo");
            String pageSize = map.get("pageSize");
            result = new BhResult();
            List<CategoryVo> list = null; //=jdGoodsService.getCategorys1(pageNo,pageSize);

            result = new BhResult(200, "11", list);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 3.17 同类商品查询
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getSimilarSku")
    @ResponseBody
    public BhResult getSimilarSku(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String sku = map.get("sku");
            result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSimilarSku(sku));
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SCJ-20171220-01
     * 京东商品摘取
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getJdGoods")
    @ResponseBody
    public BhResult getJdGoods(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String sku = map.get("sku");
            if (JDGoodsApi.getSellPrice(sku) == null || JDGoodsApi.getSellPrice(sku).isEmpty()) {
                result = new BhResult(BhResultEnum.GOODS_NOT_EXIT, null);
                return result;
            }
            if (JDGoodsApi.skuState(sku).equals("0")) {
                result = new BhResult(BhResultEnum.GOODS_IS_DOWN, null);
                return result;
            }
            List<Goods> goods = jdGoodsService.getJdGoods(sku);
            if (goods != null) {
                result = new BhResult(BhResultEnum.SUCCESS, goods);
            } else {
                result = new BhResult(BhResultEnum.IS_GET, null);
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <p>Description: 商品摘取（优）</p>
     *
     * @author scj
     * @date 2018年6月4日
     */
    @RequestMapping("/product/downJdGoods")
    @ResponseBody
    public BhResult downJdGoods(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String sku = map.get("sku");
            if (JDGoodsApi.getSellPrice(sku) == null || JDGoodsApi.getSellPrice(sku).isEmpty()) {
            	jdGoodsService.syncDeleteStatus(sku);
                result = new BhResult(BhResultEnum.GOODS_NOT_EXIT, null);
                return result;
            }
            if (JDGoodsApi.skuState(sku).equals("0")) {
            	jdGoodsService.syncUpOrDownStatus(sku);
                result = new BhResult(BhResultEnum.GOODS_IS_DOWN, null);
                return result;
            }
            Map<String, Object> data = jdGoodsService.downJdGoods(sku);
            if (Integer.valueOf(data.get("code").toString()) == 0) {
                result = new BhResult(BhResultEnum.SUCCESS, data);
            } else if(Integer.valueOf(data.get("code").toString()) == 400){
                result = new BhResult(BhResultEnum.IS_GET, null);
            }else if(Integer.valueOf(data.get("code").toString()) == 500){
            	result = new BhResult(BhResultEnum.JD_IMAGE, null);
            }else if(Integer.valueOf(data.get("code").toString()) == 600){
            	result = new BhResult(BhResultEnum.JD_DETAILS, null);
            }else if(Integer.valueOf(data.get("code").toString()) == 700){
            	result = new BhResult(BhResultEnum.JD_SELLPRICE, null);
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SCJ-20171225-01
     * 京东商品价格变更
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getPriceChange")
    @ResponseBody
    public BhResult getPriceChange() {
        BhResult result = null;
        try {
            result = new BhResult(BhResultEnum.SUCCESS, jdGoodsService.getPriceChange());
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SCJ-20180109-01
     * 京东商品价格变更消息提示
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/notice")
    @ResponseBody
    public BhResult notice() {
        BhResult result = null;
        try {
            result = new BhResult(BhResultEnum.SUCCESS, jdGoodsService.notice());
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SCJ-20180109-02
     * 价格变更短息发送
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/sendMessage")
    @ResponseBody
    public BhResult sendMessage() {
        BhResult result = null;
        try {
            jdGoodsService.sendMessage();
            result = new BhResult(BhResultEnum.SUCCESS, null);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SCJ-20171228-01
     * 获取商品组合sku属性值
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getSkuValue")
    @ResponseBody
    public BhResult getSkuValue(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            String sku = map.get("sku");
            List<String> str = jdGoodsService.getSkuValue(sku);
            if (str != null) {
                result = new BhResult(BhResultEnum.SUCCESS, str);
            } else {
                result = new BhResult(BhResultEnum.GAIN_FAIL, str);
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SCJ-20180102-01
     * 京东规格参数copy
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getParam")
    @ResponseBody
    public BhResult getParam(@RequestBody Map<String, Object> map) {
        BhResult result = null;
        try {
            JSONArray list = JSONArray.fromObject(map.get("list"));
            String goodsId = (String) map.get("goodsId");
            int row = jdGoodsService.insertJdParam(list, goodsId);
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
     * 3.4 根据skuId获取商品详细信息接口(分类id/品牌id/品牌名字)
     *
     * @param map
     * @return
     */
    @RequestMapping("/product/getDetailBySkuId")
    @ResponseBody
    public BhResult getDetailBySkuId(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
            //调用京东获取商品详细信息接口
            String sku = map.get("sku");
            String isShow = map.get("isShow");
            JdResult<Sku> result2 = JDGoodsApi.getDetailBySkuID(sku, isShow);
            if (result2 == null) {
            }
//	        String  retJsonStr2 = null;
//			try {
//				retJsonStr2 = new String(result2.getResult().getBrandName().getBytes("GBK"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            //调用京东商品搜索接口
            if (result2.getSuccess()) {
                String keyword = "";
                String catId = "";
                String pageIndex = map.get("pageIndex");
                String pageSize = "30";
                String min = "";
                String max = "";
                String brands = result2.getResult().getBrandName();
                SearchResult s = jdGoodsService.search(keyword, catId, pageIndex, pageSize, min, max, brands);
                result = new BhResult(BhResultEnum.SUCCESS, s);
            } else {
                result = new BhResult(400, result2.getResultMessage(), null);
            }
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * SCJ-20180316-03
     * jd数据导入
     *
     * @param map
     * @return
     */
    @RequestMapping("/batchAddJdGoods")
    @ResponseBody
    public BhResult batchAddJdGoods() {
        BhResult r = null;
        try {
            jdGoodsService.batchAddJdGoods();
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            return r;
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            e.printStackTrace();
            return r;
        }
    }

    /**
     * scj
     * 测试京东param数据迁移
     */
    @RequestMapping("/insertModelValue")
    @ResponseBody
    public BhResult insertModelValue() {
        BhResult r = null;
        try {
            String htmlStr = "<table width='100%' cellspacing='1' cellpadding='0' border='0' class='Ptable'><tbody><tr><th colspan='2' class='tdTitle'>主体</th></tr><tr></tr><tr><td class='tdTitle'>品牌</td><td>雷蛇 Razer</td></tr><tr><td class='tdTitle'>类型</td><td>有线鼠标</td></tr><tr><td class='tdTitle'>型号</td><td>地狱狂蛇镜面特别版</td></tr><tr><td class='tdTitle'>颜色</td><td>黑色</td></tr><tr><th colspan='2' class='tdTitle'>规格</th></tr><tr></tr><tr><td class='tdTitle'>传输方式</td><td>线缆</td></tr><tr><td class='tdTitle'>人体工学</td><td>否</td></tr><tr><td class='tdTitle'>鼠标工作方式</td><td>光电</td></tr><tr><td class='tdTitle'>鼠标分辨率</td><td>3500dpi</td></tr><tr><td class='tdTitle'>接口</td><td>USB</td></tr><tr><td class='tdTitle'>鼠标尺寸</td><td>115(L) x 63(W) x 40(H) mm</td></tr><tr><td class='tdTitle'>重量</td><td>0.09kg</td></tr></tbody></table>";
            jdGoodsService.insertModelValue(htmlStr, 1, "京东分类名称", "1");
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
     * scj
     * 测试京东param数据迁移
     */
    @RequestMapping("/insertItemModelValue")
    @ResponseBody
    public BhResult insertItemModelValue() {
        BhResult r = null;
        try {
            String htmlStr = "<table width='100%' cellspacing='1' cellpadding='0' border='0' class='Ptable'><tbody><tr><th colspan='2' class='tdTitle'>主体</th></tr><tr></tr><tr><td class='tdTitle'>品牌</td><td>雷蛇 Razer</td></tr><tr><td class='tdTitle'>类型</td><td>有线鼠标</td></tr><tr><td class='tdTitle'>型号</td><td>地狱狂蛇镜面特别版</td></tr><tr><td class='tdTitle'>颜色</td><td>黑色</td></tr><tr><th colspan='2' class='tdTitle'>规格</th></tr><tr></tr><tr><td class='tdTitle'>传输方式</td><td>线缆</td></tr><tr><td class='tdTitle'>人体工学</td><td>否</td></tr><tr><td class='tdTitle'>鼠标工作方式</td><td>光电</td></tr><tr><td class='tdTitle'>鼠标分辨率</td><td>3500dpi</td></tr><tr><td class='tdTitle'>接口</td><td>USB</td></tr><tr><td class='tdTitle'>鼠标尺寸</td><td>115(L) x 63(W) x 40(H) mm</td></tr><tr><td class='tdTitle'>重量</td><td>0.09kg</td></tr></tbody></table>";
            int goodsId = 7217;
            jdGoodsService.insertItemModelVaue(htmlStr, goodsId);
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
     * SCJ-20180327-01
     * 京东商品sku搜索
     *
     * @param map
     * @return
     */
    @RequestMapping("/jdSkuNoSearch")
    @ResponseBody
    public BhResult jdSkuNoSearch(@RequestBody Map<String, String> map) {
        BhResult r = null;
        try {
            String skuNo = map.get("skuNo");
            SearchResult s = jdGoodsService.jdSkuNoSearch(skuNo);
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            r.setData(s);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            r = BhResult.build(500, "操作异常!");
            return r;
        }
    }

    /**
     * SCJ-20180329-01
     * 复制sku判断是否已存在
     *
     * @param map
     * @return
     */
    @RequestMapping("/checkSkuNoExist")
    @ResponseBody
    public BhResult checkSkuNoExist(@RequestBody Map<String, String> map) {
        BhResult r = null;
        try {
            String goodsId = map.get("goodsId");
            String jdSkuNo = map.get("jdSkuNo");
            Map<String, Object> b = jdGoodsService.batchAddJdGoods(goodsId, jdSkuNo);
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            r.setData(b);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            r = BhResult.build(500, "操作异常!");
            return r;
        }
    }

    /**
     * <p>Description: 京东商品池商品入库</p>
     *
     * @author scj
     * @date 2018年5月30日
     */
    @RequestMapping("/exportJdGoods")
    @ResponseBody
    public BhResult exportJdGoods() {
        BhResult r = null;
        try {
            jdGoodsService.exportJdGoods();
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
     * <p>Description: 根据京东商品池编号入库(补全)</p>
     *
     * @author scj
     * @date 2018年5月30日
     */
    @RequestMapping("/exportJdGoodsByNum")
    @ResponseBody
    public BhResult exportJdGoodsByNum(@RequestBody Map<String, String> map) {
        BhResult r = null;
        try {
            String poolNum = map.get("poolNum");
            jdGoodsService.exportJdGoodsByNum(poolNum);
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
     * <p>Description: 根据京东商品池编号入库</p>
     *
     * @author scj
     * @date 2018年5月30日
     */
    @RequestMapping("/exportJdGoodsByNumAll")
    @ResponseBody
    public BhResult exportJdGoodsByNumAll(@RequestBody Map<String, String> map) {
        BhResult r = null;
        try {
            String poolNum = map.get("poolNum");
            jdGoodsService.exportJdGoodsByNumAll(poolNum);
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
     * <p>Description: 京东商品列表</p>
     *
     * @author scj
     * @date 2018年5月30日
     */
    @RequestMapping("/jdGoodsListPage")
    @ResponseBody
    public BhResult jdGoodsListPage(@RequestBody JdGoods entity) {
        BhResult r = null;
        try {
            PageBean<JdGoods> data = jdGoodsService.jdGoodsListPage(entity);
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            r.setData(data);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            r = BhResult.build(500, "操作异常!");
            return r;
        }
    }

    /**
     * <p>Description: 导出excel</p>
     *
     * @author scj
     * @date 2018年5月31日
     */
    @RequestMapping("/excelExport")
    public void excelExport(Integer jdSkuNo, String poolNum, String goodsName, String brandName, String catId, Integer isUp,
                            Integer isDelete, Integer isGet, Double startJdPrice, Double endJdPrice, Double startStockPrice, Double endStockPrice,
                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArrayList<String> fieldName = new ArrayList<>();
        fieldName.add("商品编码");
        fieldName.add("商品池编号");
        fieldName.add("商品名称");
        fieldName.add("商品图片");
        fieldName.add("品牌名称");
        fieldName.add("分类id");
        fieldName.add("京东价");
        fieldName.add("进货价");
        ArrayList<ArrayList<String>> fieldData = jdGoodsService.excelExport(jdSkuNo, poolNum, goodsName, brandName, catId, isUp, isDelete, isGet, startJdPrice, endJdPrice, startStockPrice, endStockPrice);
        ExcelFileGenerator generator = new ExcelFileGenerator(fieldName, fieldData);
        OutputStream os = response.getOutputStream();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filename = "商品池excel（" + df.format(date).replace(":", "_") + "）.xls";
        filename = ExcelFileGenerator.processFileName(request, filename);
        //可以不加，但是保证response缓冲区没有任何数据，开发时建议加上
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setBufferSize(1024);
        /**将生成的excel报表，写到os中*/
        generator.expordExcel(os);
    }

    /**
     * <p>Description: 京东商品池商品入库(skuNo先入)</p>
     *
     * @author scj
     * @date 2018年6月25日
     */
    @RequestMapping("/exportJdSkuNo")
    @ResponseBody
    public BhResult exportJdSkuNo() {
        BhResult r = null;
        try {
            jdGoodsService.exportJdSkuNo();
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            return r;
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            e.printStackTrace();
            return r;
        }
    }

    /**
     * <p>Description: 京东商品池商品详细信息入库(根据skuNo更新京东商品表)</p>
     *
     * @author scj
     * @date 2018年6月25日
     */
    @RequestMapping("/detailsByJdSkuNo")
    @ResponseBody
    public BhResult detailsByJdSkuNo() {
        BhResult r = null;
        try {
            jdGoodsService.detailsByJdSkuNo();
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            return r;
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            e.printStackTrace();
            return r;
        }
    }


    @RequestMapping("/getJdStock")
    @ResponseBody
    public BhResult getJdStock(@RequestBody Map<String, String> map) {
        BhResult r = null;
        try {
            String jdSkuNo = map.get("jdSkuNo");
            String one = map.get("one");
            String two = map.get("two");
            String three = map.get("three");
            String four = map.get("four");
            String data = jdGoodsService.getJdStock(jdSkuNo, one, two, three, four);
            r = new BhResult();
            r.setStatus(BhResultEnum.SUCCESS.getStatus());
            r.setMsg(BhResultEnum.SUCCESS.getMsg());
            r.setData(data);
            return r;
        } catch (Exception e) {
            r = BhResult.build(500, "操作异常!");
            e.printStackTrace();
            return r;
        }
    }
    
    
    /**
     * <p>Description: testjdChange</p>
     *  @author scj  
     *  @date 2018年7月9日
     */
    @RequestMapping("/product/testChange")
    @ResponseBody
    public BhResult testChange(@RequestBody Map<String, String> map) {
        BhResult result = null;
        try {
        	String jdSkuNo = map.get("jdSkuNo");
            jdGoodsService.testChange(jdSkuNo);
            result = new BhResult(BhResultEnum.SUCCESS, null);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * <p>Description: 定时同步京东表价格</p>
     *  @author scj  
     *  @date 2018年8月1日
     */
    @RequestMapping("/product/syncJdGoodsPrice")
    @ResponseBody
    public BhResult syncJdGoodsPrice() {
        BhResult result = null;
        try {
            jdGoodsService.syncJdGoodsPrice();
            result = new BhResult(BhResultEnum.SUCCESS, null);
        } catch (Exception e) {
            result = new BhResult(BhResultEnum.VISIT_FAIL, null);
            LoggerUtil.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
	/**
	 * @Description:根据jdSkuNo更新京东商品的属性值
	 * @author xieyc
	 * @date 2018年9月5日 上午10:45:12
	 */
	@RequestMapping("/product/updateGoodSkuByJdSkuNo")
	@ResponseBody
	public BhResult updateGoodSkuByJdSkuNo(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String jdskuNo = map.get("jdSkuNo");
			int row=jdGoodsService.updateGoodSkuByJdSkuNo(jdskuNo);
			if(row==-1){
				result = BhResult.build(400, "存在多个jdSkuNo相同的商品!");
			}else if(row==-2){
				result = BhResult.build(400, "该jdSkuNo的商品不存在!");
			}else if(row==-3){
				result = BhResult.build(400, "商品已经被下架!");
			}else{
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
    
}
