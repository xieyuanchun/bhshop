package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsModelAttrMapper;
import com.bh.goods.mapper.GoodsModelMapper;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsModel;
import com.bh.goods.pojo.GoodsModelAttr;
import com.bh.product.api.service.GoodsModelService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;

@Service
@Transactional
public class GoodsModelImpl implements GoodsModelService{
	@Autowired
	private GoodsModelMapper mapper;
	
	@Autowired
	private GoodsModelAttrMapper attrMapper;
	
	@Autowired
	private GoodsCategoryMapper categoryMapper;
	
	/**
	 * 用户端查询条件标签
	 */
	@Override
	public List<GoodsModelAttr> selectBycatId(String catid) throws Exception {
		List<GoodsModelAttr> alist = attrMapper.getBymId(1);
		alist.clear();
		
		List<GoodsModel> list = mapper.selectBycatId(catid);
		for(GoodsModel goodsModel : list){
			List<GoodsModelAttr> AttrList = attrMapper.getBymId(goodsModel.getId());
			if(AttrList.size()>0){
				for(GoodsModelAttr attr : AttrList){
					if(attr.getValue()!=null){
						String[] str = attr.getValue().split(",");
						attr.setAttrValue(str);
					}
					alist.add(attr);
				}
			}
			//goodsModel.setValue(AttrList);
		}
		return alist;
	}
	/**
	 * 商家后台获取所有商品属性
	 */
	@Override
	public List<GoodsModel> selectAllModel(String catid) throws Exception {
		List<GoodsModel> list = mapper.selectBycatId(catid);
		if(list.size()>0){
			for(GoodsModel model : list){
				List<GoodsModelAttr> AttrList = attrMapper.getBymId(model.getId());
				if(AttrList.size()>0){
					for(GoodsModelAttr attr : AttrList){
						if(attr.getValue()!=null){
							String[] str = attr.getValue().split(",");
							attr.setAttrValue(str);
						}
					}
				}
				model.setValue(AttrList);
			}
		}
		return list;
	}
	
	/**
	 * 平台后台商品属性列表
	 */
	@Override
	public PageBean<GoodsModel> selectAllList(String currentPage, String pageSize, String name, String catid) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<GoodsModel> list = mapper.selectPage(name, catid);
		for(GoodsModel goodsModel : list){
			String str = goodsModel.getCatId();
			List<String> result = Arrays.asList(str.split(",")); //string转list
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i<result.size(); i++){
				GoodsCategory category = categoryMapper.selectByPrimaryKey(Long.parseLong(result.get(i)));
				if(category!=null){
					buffer.append(category.getName()+",");
				}
			}
			String gategoryName=null;
			if(buffer.length()>0){
				gategoryName=buffer.toString().substring(0,buffer.toString().length()-1);
			}
			goodsModel.setCategoryName(gategoryName);
		}	
		PageBean<GoodsModel> pageBean = new PageBean<>(list);;
		return pageBean;
	}
	
	
	/**
	 * 商品模型的添加
	 */
	@Override
	public String insertModel(String name, String catId) throws Exception {
		int row = 0;
		String back = null;
		//查询是否重名
		List<GoodsModel> modelList = mapper.selectByName(name, catId);
		if(modelList.size()>0){
			back = "isExcit";
		}else{
			GoodsModel goodsModel = new GoodsModel();
			goodsModel.setName(name);
			if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){
				goodsModel.setCatId(catId);
			}
			row =  mapper.insertSelective(goodsModel);
			if(row >0){
				back = "yes";
			}else{
				back = "no";
			}
		}
		return back;
	}
	
	/*public String insertModel(String name, String catId) throws Exception {
		int row = 0;
		String back = null;
		StringBuffer buffer = new StringBuffer();
		StringBuffer catIds = new StringBuffer();
		String[] str= catId.toString().split(","); 
		//查询是否重名
		List<GoodsModel> modelList = mapper.selectAllByName(name);//拼接重名分类id
		if(modelList.size()>0){
			for(GoodsModel goodsModel : modelList){
				buffer.append(goodsModel.getCatId()+",");
				catIds.append(goodsModel.getCatId()+",");
			}
			
			String catAttr = catIds.toString().substring(0, catIds.toString().length()-1);
			List<String> listString = new ArrayList<String>();
			String[] strs = catAttr.split(",");
			for (int i = 0; i < strs.length; i++) {
				listString.add(strs[i]);
			}
			
			for(int i=0; i<str.length; i++){
				for (int j = 0; j < strs.length; j++) {
					if( Integer.parseInt(str[i]) == Integer.parseInt(strs[j])){
						return buffer.toString().substring(0, buffer.toString().length()-1);
					}
				}
			}
		}
		GoodsModel goodsModel = new GoodsModel();
		goodsModel.setName(name);
		if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){
			goodsModel.setCatId(catId);
		}
		row =  mapper.insertSelective(goodsModel);
		if(row >0){
			back = "yes";
		}else{
			back = "no";
		}
		return back;
	}*/
	
	/**
	 * 商品模型的修改
	 */
	@Override
	public String editModel(String id, String name, String catId) throws Exception {
		GoodsModel model = mapper.selectByPrimaryKey(Integer.parseInt(id));
		List<GoodsModel> modelList = mapper.selectAllByName(name, catId, Integer.parseInt(id));
		String back = null;
		if(modelList.size()>0){
			back = "isExcit";
		}else{
			back = change(model, name, catId);
		}
		return back;
	}
	
	/*public String editModel(String id, String name, String catId) throws Exception {
		GoodsModel model = mapper.selectByPrimaryKey(Integer.parseInt(id));
		List<GoodsModel> mList = mapper.selectAllByName(name);//查询所用同名的模型列表
		String back = null;
		if(mList.size()>0){
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i<mList.size(); i++){
				buffer.append(mList.get(i).getCatId()+",");
			}
			String strTwo = buffer.toString().substring(0, buffer.toString().length()-1); //获取所有同名分类id
			List<GoodsModel> list = mapper.selectUpdateByName(name, Integer.parseInt(id));
			if(list.size()>0){ //判断是否有重名
				if(catId.equals(model.getCatId())){ //判断修改的分类是否不变
					back = change(model, name, catId);
				}else if(model.getCatId().contains(catId)){ //判断修改的分类是否包含在原分类里
					back = change(model, name, catId);
				}else{
					String strOne = beyond(model.getCatId(), catId); //取出不同部分
					boolean flag = compare(strOne, strTwo);
					if(flag){
						back = change(model, name, catId);
					}else{
						return strTwo;
					}
				}
			}else{ //不重名时正常修改
				back = change(model, name, catId);
			}
		}else{
			back = change(model, name, catId);
		}
		return back;
	}*/
	
	public String change(GoodsModel model, String name, String catId){
		String back = null;
		int row = 0;
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			model.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){
			model.setCatId(catId);
		}
		row = mapper.updateByPrimaryKeySelective(model);
		if(row == 1){
			back = "yes";
		}else{
			back = "no";
		}
		return back;
	}
	
	public String beyond(String oldStr, String newStr){ //获取两个字符串表不相同部分
		String[] arr1 = newStr.split(",") ;
        String arr2[] = oldStr.split(",") ; 
        
        for (int i = 0; i < arr2.length; i++){
            for (int j = 0; j < arr1.length; j++){
                if (arr1[j].equals(arr2[i])){
                    arr1[j] = "" ;
                }
            }
        }
        StringBuffer sb = new StringBuffer() ;
        for (int j = 0; j < arr1.length; j++){
            if (!"".equals(arr1[j]) ){
                sb.append(arr1[j] + ",") ;
            }
        }
        String end = sb.toString().substring(0, sb.toString().length()-1);
        return end;
	}
	
	public boolean compare(String strOne, String strTwo){ //比较两个字符串是否完全不同
		String[] arr1 = strOne.split(",") ;
        String[] arr2 = strTwo.split(",") ; 	
		for(int i=0; i<arr1.length; i++){
			for (int j = 0; j < arr2.length; j++) {
				if( Integer.parseInt(arr1[i]) == Integer.parseInt(arr2[j])){
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * 商品模型的删除
	 */
	@Override
	public int deleteModel(String id) throws Exception {
		int row = 0;
		GoodsModel model = mapper.selectByPrimaryKey(Integer.parseInt(id));
		List<GoodsModelAttr> list = attrMapper.selectAllBymId(Integer.parseInt(id));
		if(list.size()==0){
			model.setStatus(true);
			row = mapper.updateByPrimaryKeySelective(model);
		}else{
			row = 2;
		}
		return row;
	}
	
		
}
