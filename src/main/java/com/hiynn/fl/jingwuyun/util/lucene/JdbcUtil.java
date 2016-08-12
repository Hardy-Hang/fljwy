package com.hiynn.fl.jingwuyun.util.lucene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hiynn.fl.jingwuyun.util.PropertiesUtil;

/**
 * <p>Title: JdbcUtil </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 下午12:24:06
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class JdbcUtil {
	private static Connection conn;

	private JdbcUtil() {

	}

	static {
		String URL = PropertiesUtil.getJdbcProperty("datasource.jdbcUrl");
		String driver = PropertiesUtil.getJdbcProperty("datasource.driverClass");
		String username = PropertiesUtil.getJdbcProperty("datasource.user");
		String password = PropertiesUtil.getJdbcProperty("datasource.password");
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(URL, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return conn;
	}

	/** 
	 * <p>Title: excuteSql </p>
	 * <p>Description:  </p>
	 * @param sql
	 * @return 
	 * @throws SQLException 
	 */
	public static ResultSet excuteSql(String sql) throws SQLException {

		PreparedStatement ps = getConnection().prepareStatement(sql);
		return ps.executeQuery();

	}
}
