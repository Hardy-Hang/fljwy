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

import com.hiynn.fl.jingwuyun.constants.LuceneConstants;
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

	public List<Map<String, String>> getSearchResults(String key, String index) throws Exception {
		NRTManager nrtManager = LuceneIndexUtil.getInstance().getNRTManager();
		IKAnalyzer analyzer = LuceneIndexUtil.getInstance().getAnalyzer();
		IndexSearcher searcher = nrtManager.acquire();
		try {
			BooleanQuery query = new BooleanQuery();
			query.add(new QueryParser(Version.LUCENE_40, index, analyzer).parse(key), BooleanClause.Occur.MUST);
			float limitScore = Float.parseFloat(LuceneConstants.getScoreLimit());
			// 限制返回的匹配分数
			log.info("----------begin to search : [ " + key + " ]----------");
			TopDocs tds = searcher.search(query, 20);
			log.info("----------found " + tds.scoreDocs.length + " hits ， and meet score records as:");

			List<Map<String, String>> docs = new ArrayList<Map<String, String>>();
			int i = 0;
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				Map<String, String> map = new HashMap<String, String>();
				map.put("INDUSTRY_NAME", doc.get("INDUSTRY_NAME"));
				map.put("TOWN_NAME", doc.get("TOWN_NAME"));
				map.put("ADDRESS", doc.get("ADDRESS"));
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
