package com.hiynn.fl.jingwuyun.dao;

import java.util.List;

import com.hiynn.fl.jingwuyun.entity.TempEntity;

/**
 * <p>Title: TemMapper </p>
 * <p>Description: TODO </p>
 * Date: 2016年7月26日 下午1:59:39
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date               Author           Content
 * ==========================================================
 * 2016年7月26日           hangzongguo       用于测试事务
 * 
 * ==========================================================
 */
public interface TempMapper {
	/**
	 * 
	 * <p>Title: getAll </p>
	 * <p>Description: 取出所有 </p>
	 * @return
	 */
	List<TempEntity> getAll();

	/** 
	 * <p>Title: insertAll </p>
	 * <p>Description: 插入一条 </p>
	 * @param te
	 */
	void insertAll(TempEntity te);

}
