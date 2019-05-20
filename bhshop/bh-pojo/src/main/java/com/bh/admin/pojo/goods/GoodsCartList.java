package com.bh.admin.pojo.goods;

import java.io.Serializable;
import java.util.List;

public class GoodsCartList implements Serializable {
    
    /**
	 * 2017-10-10 chengfengyun
	 */
	private static final long serialVersionUID = -7055124226940418019L;
	private List<GoodsCartListShopIdList> list ;
    private List<GoodsCart> removeList;

    
  

	public List<GoodsCartListShopIdList> getList() {
		return list;
	}

	public void setList(List<GoodsCartListShopIdList> list) {
		this.list = list;
	}

	public List<GoodsCart> getRemoveList() {
		return removeList;
	}

	public void setRemoveList(List<GoodsCart> removeList) {
		this.removeList = removeList;
	}

	
	
	
}
