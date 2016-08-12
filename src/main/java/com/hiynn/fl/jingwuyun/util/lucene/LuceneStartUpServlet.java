package com.hiynn.fl.jingwuyun.util.lucene;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hiynn.fl.jingwuyun.util.PropertiesUtil;

/**
 * <p>Title: LuceneStartUpServlet </p>
 * <p>Description:  </p>
 * Date: 2016年8月10日 上午11:32:35
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月10日    hangzongguo   项目启动时初始化索引
 * 
 * ============================================
 */
public class LuceneStartUpServlet extends HttpServlet {
	/**
	 * Fields serialVersionUID: ( ) 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(LuceneStartUpServlet.class);
	private LuceneIndexUtil luceneIndexUtil = LuceneIndexUtil.getInstance();

	private Thread refreshCacheThread = new Thread(new Runnable() { //后台线程刷新索引

				public void run() {
					while (true) {
						try {
							int time = Integer.parseInt(PropertiesUtil.getLuceneProperty("luceneIndex.refreshInterval"));
							Thread.sleep(time);
							log.debug("begin to refresh Index : " + PropertiesUtil.getLuceneProperty("luceneIndex.path"));
							luceneIndexUtil.refreshIndex();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			});

	/**
	 * 初始化Lucene索引
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		String initFlag = PropertiesUtil.getLuceneProperty("luceneIndex.initFlag");
		if (null != initFlag && initFlag.equals("1")) {
			log.info("Lucene Score is limited to " + PropertiesUtil.getLuceneProperty("lucene.scoreLimit"));
			log.info("LuceneIndexPath : " + PropertiesUtil.getLuceneProperty("luceneIndex.path"));
			log.info("Initializing Lucene Index , Please wait... ");

			long start = System.currentTimeMillis();
			try {
				luceneIndexUtil.clearDBRecords(null);
				luceneIndexUtil.initIndex();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Initialize Lucene Index failed ,due to " + e.getMessage());
			}
			log.info("Initialize Lucene Index over ，cost " + (System.currentTimeMillis() - start) + " ms");
		}
		refreshCacheThread.setDaemon(true);
		refreshCacheThread.start(); //启动后台线程
	}
}
