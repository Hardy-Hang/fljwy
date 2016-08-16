package com.hiynn.fl.jingwuyun.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hiynn.fl.jingwuyun.service.LuceneSearchService;

/**
 * <p>Title: SearchController </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 下午3:11:13
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   实现搜索
 * 
 * ============================================
 */
@Controller
public class SearchController {
	@Autowired
	private LuceneSearchService luceneSearchService;

	@RequestMapping(value = "searchindustry/{key}", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, String>> searchIndustry(@PathVariable("key") String key) throws Exception {

		key = new String(key.getBytes("ISO-8859-1"), "UTF-8");

		List<Map<String, String>> searResults = luceneSearchService.getSearchResults(key, "INDUSTRY_NAME");
		return searResults;

	}

	@RequestMapping(value = "searchtown/{key}", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, String>> searchTown(@PathVariable("key") String key) throws Exception {

		key = new String(key.getBytes("ISO-8859-1"), "UTF-8");

		List<Map<String, String>> searResults = luceneSearchService.getSearchResults(key, "TOWN_NAME");
		return searResults;

	}

	@RequestMapping(value = "searchaddress/{key}", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, String>> searchAddress(@PathVariable("key") String key) throws Exception {

		key = new String(key.getBytes("ISO-8859-1"), "UTF-8");

		List<Map<String, String>> searResults = luceneSearchService.getSearchResults(key, "ADDRESS");
		return searResults;

	}
}
