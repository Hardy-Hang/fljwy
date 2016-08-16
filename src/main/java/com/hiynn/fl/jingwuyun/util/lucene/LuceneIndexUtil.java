package com.hiynn.fl.jingwuyun.util.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.hiynn.fl.jingwuyun.constants.LuceneConstants;


/**
 * <p>Title: LuceneIndexUtil2 </p>
 * <p>Description:  </p>
 * Date: 2016年8月15日 下午1:52:48
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月15日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class LuceneIndexUtil {
	private IKAnalyzer analyzer = new IKAnalyzer(true);
	private IndexWriter writer;
	private Directory directory;
	private NRTManager nrtManager;
	private TrackingIndexWriter trackWriter;
	private NRTManagerReopenThread reopenThread;
	private static LuceneIndexUtil instance = new LuceneIndexUtil();

	private LuceneIndexUtil() {

	}

	public static LuceneIndexUtil getInstance() {
		return instance;
	}

	/**
	 * LuceneIndexUtil.java
	 * @return the writer
	 */
	public IndexWriter getWriter() {
		return writer;
	}

	/**
	 * LuceneIndexUtil.java
	 * @return the trackWriter
	 */
	public TrackingIndexWriter getTrackWriter() {
		return trackWriter;
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
			directory = FSDirectory.open(new File(LuceneConstants.getIndexPath()));
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_40, analyzer));
			trackWriter = new NRTManager.TrackingIndexWriter(writer);
			nrtManager = new NRTManager(trackWriter, null);

			reopenThread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			// 实现无需reopen索引的线程
			reopenThread.setName("NRT Reopen Thread");
			reopenThread.setDaemon(true);
			reopenThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
