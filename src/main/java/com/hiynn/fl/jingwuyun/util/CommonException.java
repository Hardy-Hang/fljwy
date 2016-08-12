package com.hiynn.fl.jingwuyun.util;

/**
 * <p>
 * Title: CommonException
 * </p>
 * <p>
 * Description: 自定义异常
 * </p>
 * Date: 2016年7月26日 下午4:11:23
 * 
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> Significant Modify： Date Author Content
 *========================================================== 
 *2016年7月26日 hangzongguo 自定义异常
 * 
 *==========================================================
 */
public class CommonException extends Exception {

	/**
	 * Fields serialVersionUID: (序列化时使用)
	 */
	private static final long serialVersionUID = 1598331190963372024L;
	/**
	 * 
	 * <p>Title: CommonException </p>
	 * <p>Description: Constructor </p>
	 */
	public CommonException() {
	}
	/**
	 * 
	 * <p>Title: CommonException </p>
	 * <p>Description: Constructor </p> 
	 * @param message
	 */
	public CommonException(String message) {
		super(message);
	}
	
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}
}
