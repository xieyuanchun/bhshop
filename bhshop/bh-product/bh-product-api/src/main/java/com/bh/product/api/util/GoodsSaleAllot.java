package com.bh.product.api.util;
import java.util.List;
import java.util.Random;
import com.bh.goods.pojo.Goods;


public class GoodsSaleAllot {
	public static void setGoodsFixedSale(List<Goods> list, int totalVal){
		if(list!=null&&list.size()>0&&totalVal>0){
			int size = list.size();
			Random rand = new Random();
			for(int i=0;i<totalVal;i++){
				int num = rand.nextInt(size);
				Goods goods = list.get(num);
				goods.setFixedSale(goods.getFixedSale()+1);
			}
		}
	}
	
}
