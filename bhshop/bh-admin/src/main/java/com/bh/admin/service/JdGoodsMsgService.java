package com.bh.admin.service;

import com.bh.admin.pojo.goods.JdGoodsMsg;
import com.bh.utils.PageBean;

public interface JdGoodsMsgService {
	//上下架消息
	int receiveUpOrDownMsg() throws Exception;
	//删除新增消息
	int receiveDeleteOrAddMsg() throws Exception;
	//价格变更消息
	int receivePriceChangeMsg() throws Exception;
	//参数及介绍变更消息
	int introduceChangeMsg() throws Exception;
	//已阅
	int isLook(JdGoodsMsg entity) throws Exception;
	//删除
	int delete(JdGoodsMsg entity) throws Exception;
	//列表管理
	PageBean<JdGoodsMsg> listPage(JdGoodsMsg entity)throws Exception;
	//京东商品下架删除逻辑处理Test 
	int jdDownDeleteTest(JdGoodsMsg entity) throws Exception;
}
