package com.hiynn.fl.jingwuyun.util.lucene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import com.hiynn.fl.jingwuyun.util.CommonException;
import com.hiynn.fl.jingwuyun.util.ExceptionUtil;

/**
 * <p>Title: LuceneIndexInit </p>
 * <p>Description:  </p>
 * Date: 2016年8月15日 下午1:55:15
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月15日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class LuceneIndexInit {
	private IndexWriter writer = LuceneIndexUtil.getInstance().getWriter();
	private static LuceneIndexInit instance = new LuceneIndexInit();

	private LuceneIndexInit() {

	}

	public static LuceneIndexInit getInstance() {
		return instance;
	}

	/**
	 * 
	 * <p>Title: initIndex </p>
	 * <p>Description: 初始化所有索引 </p>
	 * @throws CommonException 
	 * @throws Exception
	 */
	public void initIndex() throws CommonException {
		try {
			writer.deleteAll();
		} catch (IOException e) {
			ExceptionUtil.throwCommonException("删除全部索引失败！");
			e.printStackTrace();
		}
		// 删除所有索引文件
		String sql = "SELECT DIS.AREA_ID,CAMERA_ID,X_COORDINATE,Y_COORDINATE,CATEGORY,INDUSTRY,"
				+ "INDUSTRY_NAME,INSTALL_SITE,STATUS,IS_NETWORK,TOWN_NAME,ADDRESS "
				+ "FROM TD_FL_MNT_CAMERA_DISTRIBUTE DIS LEFT JOIN TD_FL_MNT_AREA AREA ON DIS.AREA_ID=AREA.AREA_ID";
		ResultSet rs = JdbcUtil.excuteSql(sql);
		try {
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new StringField("AREA_ID", rs.getString("AREA_ID"), Field.Store.YES));
				doc.add(new StringField("CAMERA_ID", rs.getString("CAMERA_ID"), Field.Store.YES));
				doc.add(new StringField("X_COORDINATE", rs.getString("X_COORDINATE"), Field.Store.YES));
				doc.add(new StringField("Y_COORDINATE", rs.getString("Y_COORDINATE"), Field.Store.YES));
				doc.add(new StringField("CATEGORY", rs.getString("CATEGORY"), Field.Store.YES));
				doc.add(new StringField("INDUSTRY", rs.getString("INDUSTRY"), Field.Store.YES));
				doc.add(new TextField("INDUSTRY_NAME", rs.getString("INDUSTRY_NAME"), Field.Store.YES));
				doc.add(new StringField("INSTALL_SITE", rs.getString("INSTALL_SITE"), Field.Store.YES));
				doc.add(new StringField("STATUS", rs.getString("STATUS"), Field.Store.YES));
				doc.add(new StringField("IS_NETWORK", rs.getString("IS_NETWORK"), Field.Store.YES));
				doc.add(new TextField("TOWN_NAME", rs.getString("TOWN_NAME"), Field.Store.YES));
				doc.add(new TextField("ADDRESS", rs.getString("ADDRESS"), Field.Store.YES));
				writer.addDocument(doc);
			}
		} catch (SQLException | IOException e) {
			ExceptionUtil.throwCommonException("初始化索引失败！");
			e.printStackTrace();
		}
		JdbcUtil.closePs();
		try {
			writer.commit();
		} catch (IOException e) {
			ExceptionUtil.throwCommonException("初始化索引提交失败！");
			e.printStackTrace();
		}
	}
}
