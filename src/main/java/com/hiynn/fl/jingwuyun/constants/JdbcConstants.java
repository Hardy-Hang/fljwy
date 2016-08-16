package com.hiynn.fl.jingwuyun.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Title: JdbcEntity </p>
 * <p>Description:  </p>
 * Date: 2016年8月12日 上午11:24:52
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月12日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class JdbcConstants {
	private static String URL;
	private static String driver;
	private static String username;
	private static String password;
	static {
		InputStream inputStream = JdbcConstants.class.getResourceAsStream("/jdbc.properties");
		try {
			Properties p = new Properties();
			p.load(inputStream);
			URL = p.getProperty("datasource.jdbcUrl").trim();
			driver = p.getProperty("datasource.driverClass").trim();
			username = p.getProperty("datasource.user").trim();
			password = p.getProperty("datasource.password").trim();
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
	 * JdbcEntity.java
	 * @return the uRL
	 */
	public static String getURL() {
		return URL;
	}

	/**
	 * JdbcEntity.java
	 * @return the driver
	 */
	public static String getDriver() {
		return driver;
	}

	/**
	 * JdbcEntity.java
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * JdbcEntity.java
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}
}
