package com.hiynn.fl.jingwuyun.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ExceptionUtil </p>
 * <p>Description:  </p>
 * Date: 2016年7月26日 下午4:22:52
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date        Author        Content
 * ==============================================
 * 2016年7月26日     hangzongguo   异常处理 
 * 
 * ==============================================
 */
public class ExceptionUtil {
	private static int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
	private static String className = Thread.currentThread().getStackTrace()[2].getClassName();
	private static final Logger log = LoggerFactory.getLogger(ExceptionUtil.class);

	/**
	 * 
	 * <p>Title: throwCommonException </p>
	 * <p>Description: 抛出异常 </p>
	 * @throws CommonException
	 */
	public static void throwCommonException() throws CommonException {
		CommonException ce = new CommonException();
		toLogFile(ce);
		throw ce;
	}

	public static void throwCommonException(String message) throws CommonException {
		CommonException ce = new CommonException(message);
		toLogFile(ce);
		throw ce;
	}

	public static void throwCommonException(String message, Throwable cause) throws CommonException {
		CommonException ce = new CommonException(message, cause);
		toLogFile(ce);
		throw ce;
	}

	public static void throwCommonException(Throwable cause) throws CommonException {
		CommonException ce = new CommonException(cause);
		toLogFile(ce);
		throw ce;
	}

	/** 
	 * <p>Title: toLogFile </p>
	 * <p>Description:  </p>
	 * @param e
	 */
	private static void toLogFile(CommonException e) {
		StringBuilder logContent = new StringBuilder(className + "(" + lineNumber + ") throw CmmonException: " + e.getMessage() + " \n");
		StackTraceElement[] stak = e.getStackTrace();
		for (int i = 1; i < stak.length; i++) {
			logContent.append("		at	" + stak[i] + "\n");
		}
		log.error(logContent.toString());
	}
}
