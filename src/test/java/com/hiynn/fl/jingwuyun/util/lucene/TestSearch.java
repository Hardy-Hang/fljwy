package com.hiynn.fl.jingwuyun.util.lucene;

import java.util.List;
import java.util.Map;

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
public class TestSearch {
	@Test
	public void testSearch() throws Exception {
		LuceneSearchService su = new LuceneSearchService();
		List<Map<String, String>> searchFile = su.getSearchResults("特种行业2628");
		System.out.println(searchFile);

	}
}
