package com.bh.product.api.job.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时器测试
 * @author scj
 *
 */
@Component("myBean")
public class TestTimeJob {
	private static final Logger logger = LoggerFactory.getLogger(TestTimeJob.class);
	public void run() {   
		System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();  
      
        //执行数据库操作了哦...  
        System.out.println("执行数据库操作了哦...  ");
      
        long end = System.currentTimeMillis();  
        System.out.println("定时任务结束，共耗时："+(end-begin)+"ms");
	}
}
