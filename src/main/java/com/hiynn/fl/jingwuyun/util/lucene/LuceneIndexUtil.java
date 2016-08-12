package com.hiynn.fl.jingwuyun.util.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.hiynn.fl.jingwuyun.entity.LuceneIndexEntity;
import com.hiynn.fl.jingwuyun.util.PropertiesUtil;

/**
 * <p>Title: LuceneIndexUtil </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 上午11:32:35
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   索引文件处理工具类
 * 
 * ============================================
 */
public class LuceneIndexUtil {
	private static final Logger log = LoggerFactory.getLogger(LuceneIndexUtil.class);
	private IKAnalyzer analyzer = new IKAnalyzer(true);
	private IndexWriter writer;
	private Directory directory = null;
	private NRTManager nrtManager;
	private TrackingIndexWriter trackWriter;
	private NRTManagerReopenThread reopenThread;
	private String tables = PropertiesUtil.getLuceneProperty("luceneIndex.initialTable");
	private String indexNames = PropertiesUtil.getLuceneProperty("luceneIndex.initialColumes");
	private static LuceneIndexUtil instance = new LuceneIndexUtil();

	private LuceneIndexUtil() {

	}

	public static LuceneIndexUtil getInstance() {
		return instance;
	}

	public NRTManager getNRTManager() {
		return this.nrtManager;
	}

	/**
	 * LuceneIndexUtil.java
	 * 
	 * @return the analyzer
	 */
	public IKAnalyzer getAnalyzer() {
		return analyzer;
	}

	{
		try {
			directory = FSDirectory.open(new File(PropertiesUtil.getLuceneProperty("luceneIndex.path")));
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_40, analyzer));
			trackWriter = new NRTManager.TrackingIndexWriter(writer);
			nrtManager = new NRTManager(trackWriter, null);

			reopenThread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			//实现无需reopen索引的线程
			reopenThread.setName("NRT Reopen Thread");
			reopenThread.setDaemon(true);
			reopenThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <p>Title: initIndex </p>
	 * <p>Description: 初始化所有索引 </p>
	 * @throws Exception
	 */
	public void initIndex() throws Exception {
		writer.deleteAll();
		//删除所有索引文件
		String[] allTables = tables.split(";");
		String[] allIndexNames = indexNames.split("/");

		String sql = "select * from ";
		for (int i = 0; i < allTables.length; i++) {
			String[] colums = allIndexNames[i].split(";");
			String tableName = allTables[i];
			ResultSet rs = JdbcUtil.excuteSql(sql + tableName);

			while (rs.next()) {
				Document doc = new Document();
				doc.add(new TextField("updateFlag", tableName + rs.getString(getPkbyTableName(tableName)), Field.Store.YES));
				//增加索引唯一标示
				for (String colum : colums) {
					doc.add(new TextField(colum.trim(), rs.getString(colum.trim()), Field.Store.YES));
				}
				writer.addDocument(doc);
			}
		}
		writer.commit();
		log.info("Initialized all Index...");
	}

	/**
	 * <p>Title: refreshIndex </p>
	 * <p>Description:定时刷新索引方法 </p>
	 * 
	 * @throws Exception
	 */
	public void refreshIndex() throws Exception {
		String sql = "select * from td_fl_mnt_lucene_index order by id desc";
		ResultSet rs = JdbcUtil.excuteSql(sql);

		String[] allTables = tables.split(";");

		List<Integer> ids = new ArrayList<Integer>();
		List<LuceneIndexEntity> records = new ArrayList<LuceneIndexEntity>();
		// 将所有的数据放到records中
		while (rs.next()) {
			int id = rs.getInt("ID");
			ids.add(id);
			String ope = rs.getString("OPERATION");
			String pk = null;
			for (String tableName : allTables) {
				pk = rs.getString(tableName);
				if (null != pk && !pk.isEmpty()) {
					records.add(new LuceneIndexEntity(id, ope, pk, tableName));
					break;
				}
			}
		}

		for (String tableName : allTables) {
			List<String> createList = new ArrayList<String>();
			List<String> updateList = new ArrayList<String>();
			List<String> deleteList = new ArrayList<String>();
			Map<String, List<LuceneIndexEntity>> repet = new HashMap<String, List<LuceneIndexEntity>>();
			//存放索引表中同一条记录被多次更改的数据
			for (LuceneIndexEntity li : records) {
				if (li.getTableName().equals(tableName)) {
					// 按照不同表分开操作
					List<LuceneIndexEntity> indexList = new ArrayList<LuceneIndexEntity>();
					indexList.add(li);
					String pk = li.getPrimaryKey();
					String ope = li.getOperation();
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
			}
			//处理多次修改过的记录：如果最后1次是u和d不需要处理，如果最后1次是c，需要放到updateList
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
				createIndex(createList, tableName);
			}
			if (!updateList.isEmpty()) {
				updateIndex(updateList, tableName);
			}
			if (!deleteList.isEmpty()) {
				deleteIndex(deleteList, tableName);
			}
		}

		writer.commit();
		if (!ids.isEmpty()) {
			clearDBRecords(ids);
			//更新完索引删除数据库中记录
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
	}

	/**
	 * <p>Title: deleteIndex </p>
	 * <p>Description:删除数据库已删除部分的索引文件 </p>
	 * 
	 * @param deleteList
	 * @param tableName
	 * @throws IOException
	 */
	private void deleteIndex(List<String> deleteList, String tableName) throws IOException {
		for (String li : deleteList) {
			trackWriter.deleteDocuments(new Term("updateFlag", tableName + li));
			log.info("delete Index " + tableName + li);
		}
	}

	/**
	 * <p>Title: updateIndex </p>
	 * <p>Description:更新数据库更新了的索引文件 </p>
	 * 
	 * @param updateList
	 * @param tableName
	 * @throws SQLException
	 * @throws IOException
	 */
	private void updateIndex(List<String> updateList, String tableName) throws IOException, SQLException {

		StringBuilder sb = new StringBuilder("select * from ").append(tableName).append(" where ").append(getPkbyTableName(tableName))
				.append(" in (");
		for (int i = 0; i < updateList.size() - 1; i++) {
			String area_id = updateList.get(i);
			sb.append(area_id + ",");
		}
		sb.append(updateList.get(updateList.size() - 1) + ")");
		ResultSet rs = JdbcUtil.excuteSql(sb.toString());

		String tableIndexNames = getIndexNamesbyTableName(tableName);
		String[] indNames = tableIndexNames.split(";");
		while (rs.next()) {
			Document doc = new Document();
			doc.add(new TextField("updateFlag", tableName + rs.getString(getPkbyTableName(tableName)), Field.Store.YES));
			for (String ind : indNames) {
				doc.add(new TextField(ind.trim(), rs.getString(ind.trim()), Field.Store.YES));
			}

			trackWriter.updateDocument(new Term("updateFlag", tableName + rs.getString(getPkbyTableName(tableName))), doc);
			log.info("update Index " + doc.toString());
		}
	}

	/**
	 * <p>Title: createIndex </p>
	 * <p>Description:增加数据库新增的索引文件 </p>
	 * 
	 * @param createList
	 * @param tableName
	 * @throws Exception
	 */
	private void createIndex(List<String> createList, String tableName) throws Exception {

		StringBuilder sb = new StringBuilder("select * from ").append(tableName).append(" where ").append(getPkbyTableName(tableName))
				.append(" in (");
		for (int i = 0; i < createList.size() - 1; i++) {
			String area_id = createList.get(i);
			sb.append(area_id + ",");
		}
		sb.append(createList.get(createList.size() - 1) + ")");
		ResultSet rs = JdbcUtil.excuteSql(sb.toString());

		String tableIndexNames = getIndexNamesbyTableName(tableName);
		String[] indNames = tableIndexNames.split(";");

		while (rs.next()) {
			Document doc = new Document();
			doc.add(new TextField("updateFlag", tableName + rs.getString(getPkbyTableName(tableName)), Field.Store.YES));
			for (String ind : indNames) {
				doc.add(new TextField(ind.trim(), rs.getString(ind.trim()), Field.Store.YES));
			}
			writer.addDocument(doc);
			log.info("create Index " + doc.toString());
		}
	}

	/**
	 * 
	 * <p>Title: getIndexNamesbyTableName </p>
	 * <p>Description:  根据表名取得对应的所有索引名</p>
	 * @param tableName
	 * @return
	 */
	private String getIndexNamesbyTableName(String tableName) {
		String[] allIndexNames = indexNames.split("/");
		String[] allTables = tables.split(";");
		String tableIndexNames = null;
		for (int i = 0; i < allTables.length; i++) {
			if (null != tableName && tableName.equals(allTables[i])) {
				tableIndexNames = allIndexNames[i];
				break;
			}
		}
		return tableIndexNames;
	}

	/**
	 * 
	 * <p>Title: getPkbyTableName </p>
	 * <p>Description:  根据表名找到主键名</p>
	 * @param tableName
	 * @return
	 */
	private String getPkbyTableName(String tableName) {
		String tablePks = PropertiesUtil.getLuceneProperty("luceneIndex.initialTablePk");
		String[] allPks = tablePks.split(";");
		String[] allTables = tables.split(";");
		String pk = null;
		for (int i = 0; i < allTables.length; i++) {
			if (null != tableName && tableName.equals(allTables[i])) {
				pk = allPks[i];
				break;
			}
		}
		return pk;
	}
}
