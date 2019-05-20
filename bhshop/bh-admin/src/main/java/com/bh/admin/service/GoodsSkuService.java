package com.bh.admin.service;
import java.util.List;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.utils.PageBean;
import net.sf.json.JSONArray;

public interface GoodsSkuService {
	
	/*根据商品id查询所有sku*/
	List<GoodsSku> getListByGoodsId(String goodsId) throws Exception;
	
	/*goodsSku的添加*/
	int addGoodsSku(String goodsId, String value, String storeNums, String marketPrice, String sellPrice, String  weight, String minimum,String skuCode) throws Exception;
	
	/*goodsSku的修改*/
	int editGoodsSku(String id, String value, String storeNums, String marketPrice, String sellPrice, String  weight, String minimum, String teamPrice,
			String jdPrice, String jdBuyPrice, String jdOldBuyPrice, String jdProtocolPrice, String jdSupport, String jdSkuNo, String jdUpc,
			String goodsSkuName, String score, String keyOne, String valueOne, String keyTwo, String valueTwo, String keyThree, String valueThree,Integer userId) throws Exception;
	
	/*goodsSku的删除*/
	int deleteGoodsSku(String id) throws Exception;
	
	/*批量新增goodsSku*/
	int batchAddGoodsSku(String goodsId, JSONArray list) throws Exception;
	
	/*批量修改goodsSku*/
	int batchUpdateGoodsSku(JSONArray list) throws Exception;
	
	List<GoodsSku> bargainUse(int goodsId) throws Exception;
	
	//程凤云
	int batchUpdateGoodsSkuBySkuId(JSONArray list,String goodsId) throws Exception;
	
	/**
	 * <p>Description: sku单个删除</p>
	 *  @author scj  
	 *  @date 2018年7月18日
	 */
	int deleteSingle(GoodsSku entity) throws Exception;
	
	/**
	 * <p>Description: sku单个修改</p>
	 *  @author scj  
	 *  @date 2018年7月18日
	 */
	int updateSingle(GoodsSku entity) throws Exception;
	
	/**
	 * <p>Description: 置为非京东</p>
	 *  @author scj  
	 *  @date 2018年7月18日
	 */
	int updateJdStatus(GoodsSku entity) throws Exception;
	
	/**
	 * <p>Description: sku重构保存</p>
	 *  @author scj  
	 *  @date 2018年7月18日
	 */
	int updateBatch(GoodsSku entity) throws Exception;
} 
