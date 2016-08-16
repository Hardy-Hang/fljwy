package com.hiynn.fl.jingwuyun.util.lucene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hiynn.fl.jingwuyun.entity.LuceneIndexEntity;

/**
 * <p>Title: LuceneIndexRefresh </p>
 * <p>Description:  </p>
 * Date: 2016年8月15日 下午2:15:17
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月15日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class LuceneIndexRefresh {
	private static final Logger log = LoggerFactory.getLogger(LuceneIndexRefresh.class);
	private IndexWriter writer = LuceneIndexUtil.getInstance().getWriter();
	private TrackingIndexWriter trackWriter = LuceneIndexUtil.getInstance().getTrackWriter();

	private static LuceneIndexRefresh instance = new LuceneIndexRefresh();

	private LuceneIndexRefresh() {

	}

	public static LuceneIndexRefresh getInstance() {
		return instance;
	}

	/**
	 * <p>Title: refreshIndex </p>
	 * <p>Description:定时刷新索引方法 </p>
	 * 
	 * @throws Exception
	 */
	public void refreshIndex() throws Exception {
		String sql = "SELECT * FROM TD_FL_MNT_LUCENE_INDEX ORDER BY ID DESC";
		ResultSet rs = JdbcUtil.excuteSql(sql);

		List<Integer> ids = new ArrayList<Integer>();
		List<String> createList = new ArrayList<String>();
		List<String> updateList = new ArrayList<String>();
		List<String> deleteList = new ArrayList<String>();
		Map<String, List<LuceneIndexEntity>> repet = new HashMap<String, List<LuceneIndexEntity>>();

		while (rs.next()) {
			int id = rs.getInt("ID");
			String ope = rs.getString("OPERATION");
			String pk = rs.getString("AREA_ID");
			ids.add(id);

			LuceneIndexEntity li = new LuceneIndexEntity(id, ope, pk);
			List<LuceneIndexEntity> indexList = new ArrayList<LuceneIndexEntity>();
			indexList.add(li);

			if (repet.containsKey(pk)) {
				repet.get(pk).add(li);
			} else {
				repet.put(pk, indexList);
				if (ope.equals("c")) {
					createList.add(pk);
				} else if (ope.equals("u")) {
					updateList.add(pk);
				} else if (ope.equals("d")) {
					deleteList.add(pk);
				}
			}

		}
		JdbcUtil.closePs();

		// 处理多次修改过的记录：如果最后1次是u和d不需要处理，如果最后1次是c，需要放到updateList
		for (List<LuceneIndexEntity> indexs : repet.values()) {
			if (indexs.size() >= 2) {
				String operation = indexs.get(0).getOperation();
				String pk = indexs.get(0).getPrimaryKey();
				if (operation.equals("c")) {
					if (createList.contains(pk)) {
						createList.remove(pk);
						updateList.add(pk);
					}
				}
			}
		}

		if (!createList.isEmpty()) {
			createIndex(createList);
		}
		if (!updateList.isEmpty()) {
			updateIndex(updateList);
		}
		if (!deleteList.isEmpty()) {
			deleteIndex(deleteList);
		}

		writer.commit();
		if (!ids.isEmpty()) {
			clearDBRecords(ids);
			// 更新完索引删除数据库中记录
		}
	}

	/**
	 * <p>Title: clearDBRecords </p>
	 * <p>Description:清除索引表记录 </p>
	 * 
	 * @param ids
	 * @throws SQLException
	 */
	public void clearDBRecords(List<Integer> ids) throws SQLException {
		StringBuilder sql = null;
		if (null != ids && !ids.isEmpty()) {
			sql = new StringBuilder("delete from td_fl_mnt_lucene_index where id in (");
			for (int i = 0; i < ids.size() - 1; i++) {
				sql.append(ids.get(i) + ",");
			}
			sql.append(ids.get(ids.size() - 1) + ")");
		} else {
			sql = new StringBuilder("delete from td_fl_mnt_lucene_index");
		}
		JdbcUtil.excuteSql(sql.toString());
		JdbcUtil.closePs();
	}

	/**
	 * <p>Title: deleteIndex </p>
	 * <p>Description:删除数据库已删除部分的索引文件 </p>
	 * 
	 * @param deleteList
	 * @throws IOException
	 */
	private void deleteIndex(List<String> deleteList) throws IOException {
		for (String li : deleteList) {
			trackWriter.deleteDocuments(new Term("AREA_ID", li));
			log.info("delete Index " + li);
		}
	}

	/**
	 * <p>Title: updateIndex </p>
	 * <p>Description:更新数据库更新了的索引文件 </p>
	 * 
	 * @param updateList
	 * @throws SQLException
	 * @throws IOException
	 */
	private void updateIndex(List<String> updateList) throws IOException, SQLException {

		StringBuilder sb = new StringBuilder("SELECT DIS.AREA_ID,CAMERA_ID,X_COORDINATE,Y_COORDINATE,CATEGORY,INDUSTRY,"
				+ "INDUSTRY_NAME,INSTALL_SITE,STATUS,IS_NETWORK,TOWN_NAME,ADDRESS "
				+ "FROM TD_FL_MNT_CAMERA_DISTRIBUTE DIS LEFT JOIN TD_FL_MNT_AREA AREA ON DIS.AREA_ID=AREA.AREA_ID WHERE DIS.AREA_ID IN (");
		for (int i = 0; i < updateList.size() - 1; i++) {
			String area_id = updateList.get(i);
			sb.append(area_id + ",");
		}
		sb.append(updateList.get(updateList.size() - 1) + ")");
		ResultSet rs = JdbcUtil.excuteSql(sb.toString());

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

			trackWriter.updateDocument(new Term("AREA_ID", rs.getString("AREA_ID")), doc);

			log.info("update Index " + doc.toString());
		}
		JdbcUtil.closePs();
	}

	/**
	 * <p>Title: createIndex </p>
	 * <p>Description:增加数据库新增的索引文件 </p>
	 * 
	 * @param createList
	 * @throws Exception
	 */
	private void createIndex(List<String> createList) throws Exception {

		StringBuilder sb = new StringBuilder("SELECT DIS.AREA_ID,CAMERA_ID,X_COORDINATE,Y_COORDINATE,CATEGORY,INDUSTRY,"
				+ "INDUSTRY_NAME,INSTALL_SITE,STATUS,IS_NETWORK,TOWN_NAME,ADDRESS "
				+ "FROM TD_FL_MNT_CAMERA_DISTRIBUTE DIS LEFT JOIN TD_FL_MNT_AREA AREA ON DIS.AREA_ID=AREA.AREA_ID WHERE DIS.AREA_ID IN (");
		for (int i = 0; i < createList.size() - 1; i++) {
			String area_id = createList.get(i);
			sb.append(area_id + ",");
		}
		sb.append(createList.get(createList.size() - 1) + ")");
		ResultSet rs = JdbcUtil.excuteSql(sb.toString());

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

			log.info("create Index " + doc.toString());
		}
		JdbcUtil.closePs();
	}
}
