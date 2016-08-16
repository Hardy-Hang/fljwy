package com.hiynn.fl.jingwuyun.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * <p>Title: LucenEntity </p>
 * <p>Description:  </p>
 * Date: 2016年8月12日 上午11:25:01
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月12日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class LuceneConstants {
	private static String indexPath;
	private static String isInit;
	private static String initThreadNumber;
	private static String refreshInterval;
	private static String scoreLimit;
	static {
		InputStream inputStream = LuceneConstants.class.getResourceAsStream("/lucene.properties");
		try {
			Properties p = new Properties();
			p.load(inputStream);
			indexPath = p.getProperty("luceneIndex.path").trim();
			isInit = p.getProperty("luceneIndex.initFlag").trim();
			initThreadNumber = p.getProperty("luceneIndex.initThreadNumber").trim();
			refreshInterval = p.getProperty("luceneIndex.refreshInterval").trim();
			scoreLimit = p.getProperty("lucene.scoreLimit").trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * LuceneEntity.java
	 * @return the initThreadNumber
	 */
	public static String getInitThreadNumber() {
		return initThreadNumber;
	}

	/**
	 * LucenEntity.java
	 * @return the indexPath
	 */
	public static String getIndexPath() {
		return indexPath;
	}

	/**
	 * LucenEntity.java
	 * @return the isInit
	 */
	public static String getIsInit() {
		return isInit;
	}

	/**
	 * LucenEntity.java
	 * @return the refreshInterval
	 */
	public static String getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * LucenEntity.java
	 * @return the scoreLimit
	 */
	public static String getScoreLimit() {
		return scoreLimit;
	}
}
