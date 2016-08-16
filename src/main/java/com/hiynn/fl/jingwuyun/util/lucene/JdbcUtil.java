package com.hiynn.fl.jingwuyun.util.lucene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hiynn.fl.jingwuyun.constants.JdbcConstants;


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
	private static String URL = JdbcConstants.getURL();
	private static String driver = JdbcConstants.getDriver();
	private static String username = JdbcConstants.getUsername();
	private static String password = JdbcConstants.getPassword();
	private static Connection conn;
	private static PreparedStatement ps;

	private JdbcUtil() {

	}

	static {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(URL, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * <p>Title: excuteSql </p>
	 * <p>Description:  </p>
	 * @param sql
	 * @return 
	 * @throws SQLException 
	 */
	public static ResultSet excuteSql(String sql) {
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/** 
	 * <p>Title: closeConn </p>
	 * <p>Description:  </p>
	 */
	public static void closePs() {
		try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
