package com.hiynn.fl.jingwuyun.util.lucene;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.hiynn.fl.jingwuyun.service.LuceneSearchService;

/**
 * <p>Title: TestSearch </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 下午3:49:31
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class TestLucene {
	@Test
	public void testSearch() throws Exception {
		String sql = "insert into test values(";
		for(int i=1;i<10000 ;i++){
		JdbcUtil.excuteSql(sql+i+")");
		JdbcUtil.closePs();
		}
		

	}
	
	@Test
	public void testInitIndex() throws Exception{
		long start = System.currentTimeMillis();
		LuceneIndexInit ls = LuceneIndexInit.getInstance();
		ls.initIndex();
		System.out.println("--------------------------"+(System.currentTimeMillis() - start));
	}
}
