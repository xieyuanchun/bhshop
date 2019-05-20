package com.bh.utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bh.bean.Alipay;
import com.github.pagehelper.Page;
/**
 * 分页实体类  
 * @author xxj
 *
 * @param <T>
 */
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 8656597559014685635L;
    private long total;        //总记录数
    private List<T> list;    //结果集
    private int pageNum;    // 第几页
    private int pageSize;    // 每页记录数
    private int pages;        // 总页数
    private int size;        // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
    
    /**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     * @param list          page结果
     * @param navigatePages 页码数量
     */
    public PageBean() {
    
    }
    public PageBean(List<T> list) {
    	  
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
        }
    }
    public static void main(String args[]){
    	PageParams<Alipay> p = new PageParams<>();
    	List<Alipay> list = new ArrayList<>();
    	for(int i=0;i<18;i++){
    		Alipay a= new Alipay();
    		a.setBody("body"+i);
    		list.add(a);
    	}
    	p.setCurPage(3);
    	p.setPageSize(10);
    	p.setResult(list);
    	PageBean<Alipay> page = new PageBean<>();
        page.getPageResult(p);
        System.out.println("pageNum"+page.getPageNum());
        System.out.println("pagesize"+page.getPageSize());
        System.out.println("pages"+page.getPages());
        System.out.println("size"+page.getSize());
        for(Alipay a:page.getList()){
        	System.out.println(a.getBody());
        }
    }
    //获取一页结果集
    public  PageBean<T> getPageResult(PageParams<T> params){
    	List<T> result = params.getResult();
    	this.pageSize = params.getPageSize();
    	this.total = (long)result.size();
    	this.pages = (int)this.total%this.pageSize==0 ? (int)this.total/this.pageSize:(int)this.total/this.pageSize+1;
    	
    	if(params.getCurPage()<1){
    	//	this.pageNum = 1;
    		this.list = new ArrayList<>();
    		this.size = 0;
    		this.pageNum = params.getCurPage();
    		return this;
    	}else if(params.getCurPage()>this.pages){
    		this.pageNum = params.getCurPage();
    	//	this.pageNum = this.pages;
    		this.list = new ArrayList<>();
    		this.size = 0;
    		return this;
    	}else{
    		this.pageNum = params.getCurPage();
    		this.size   = this.pageSize;
    	}
    	
    	if(result!=null){
    		if(result.size()<=this.pageSize*this.pageNum){
    			List<T> tempList = result.subList((this.pageNum-1)*pageSize, result.size());
    			this.size =  result.size()-(this.pageNum-1)*pageSize;
    			this.list = tempList;
    		}else{
    			List<T> tempList = result.subList((this.pageNum-1)*pageSize, this.pageNum*pageSize);
    			this.list = tempList;
    			this.size =  this.pageNum*pageSize-(this.pageNum-1)*pageSize;
    		}
    	}
    	return this;
    }
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
}