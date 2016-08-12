package com.hiynn.fl.jingwuyun.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Title: PropertiesUtil </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 下午12:39:02
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class PropertiesUtil {

	 public static String getLuceneProperty(String key) {
	        InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/lucene.properties");
	        Properties p = new Properties();
	        String value = null;
	        try {
	            p.load(inputStream);
	            inputStream.close();
	            value = p.getProperty(key).trim();
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }
	        return value;
	    }
	 
	 public static String getJdbcProperty(String key) {
	        InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/jdbc.properties");
	        Properties p = new Properties();
	        String value = null;
	        try {
	            p.load(inputStream);
	            inputStream.close();
	            value = p.getProperty(key).trim();
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }
	        return value;
	    }
}
