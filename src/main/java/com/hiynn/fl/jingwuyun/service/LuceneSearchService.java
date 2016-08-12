package com.hiynn.fl.jingwuyun.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.hiynn.fl.jingwuyun.util.PropertiesUtil;
import com.hiynn.fl.jingwuyun.util.lucene.LuceneIndexUtil;

/**
 * <p>Title: LuceneSearchUtil </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 下午2:21:35
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   搜索工具类
 * 
 * ============================================
 */
@Service
public class LuceneSearchService {
	private static final Logger log = LoggerFactory.getLogger(LuceneSearchService.class);
	private String indexNames = PropertiesUtil.getLuceneProperty("luceneIndex.initialColumes");

	public List<Map<String, String>> getSearchResults(String key) throws Exception {
		NRTManager nrtManager = LuceneIndexUtil.getInstance().getNRTManager();
		IKAnalyzer analyzer = LuceneIndexUtil.getInstance().getAnalyzer();
		IndexSearcher searcher = nrtManager.acquire();
		String[] allInds = indexNames.split("/");
		//取得所有索引名
		try {
			BooleanQuery query = new BooleanQuery();
			for (String inds : allInds) {
				String[] indexs = inds.split(";");
				for (String index : indexs) {
					query.add(new QueryParser(Version.LUCENE_40, index, analyzer).parse(key), BooleanClause.Occur.SHOULD);
					//在所有索引名中搜索
				}
			}
			float limitScore = Float.parseFloat(PropertiesUtil.getLuceneProperty("lucene.scoreLimit"));
			//限制返回的匹配分数
			log.info("----------begin to search : [ " + key + " ]----------");
			TopDocs tds = searcher.search(query, 20);
			log.info("----------found " + tds.scoreDocs.length + " hits ， and meet score records as:");
			
			List<Map<String, String>> docs = new ArrayList<Map<String, String>>();
			int i = 0;
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				Map<String, String> map = new HashMap<String, String>();
				for (String inds : allInds) {
					String[] indexs = inds.split(";");
					for (String index : indexs) {
						map.put(index, doc.get(index));
					}
				}
				if (sd.score >= limitScore) {
					docs.add(map);
					log.info((i + 1) + ". " + map + " \t " + sd.score);
				}
			}
			return docs;
		} finally {
			nrtManager.release(searcher);
			searcher = null;
		}
	}
}
