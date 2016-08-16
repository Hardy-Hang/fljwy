package com.hiynn.fl.jingwuyun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.webxml.ArrayOfString;
import cn.com.webxml.EnglishChineseSoap;

import com.hiynn.fl.jingwuyun.dao.TempMapper;
import com.hiynn.fl.jingwuyun.entity.TempEntity;
import com.hiynn.fl.jingwuyun.util.CommonException;

/**
 * <p>Title: TempService </p>
 * <p>Description:  </p>
 * Date: 2016年7月26日 下午2:18:53
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date               Author           Content
 * ==========================================================
 * 2016年7月26日           hangzongguo       用于测试事务
 * 
 * ==========================================================
 */
@Service
public class TempService {

	@Autowired
	private TempMapper tempMapper;
	@Autowired
	private EnglishChineseSoap cxfClient;

	/**
	 * 
	 * <p>Title: getAll </p>
	 * <p>Description: 取出所有 </p>
	 * @return
	 */
	public List<TempEntity> getAll() {

		return tempMapper.getAll();

	}

	/**
	 * 
	 * <p>Title: insertAll </p>
	 * <p>Description: 插入 </p>
	 * @param te2 
	 * @throws Exception 
	 */
	public void insertAll(TempEntity te) throws CommonException {

		tempMapper.insertAll(te);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		ExceptionUtil.throwCommonException("测试事务");
	}

	/** 
	 * <p>Title: getCxf </p>
	 * <p>Description:  </p>
	 * @return
	 */
	public List<String> getCxf(String trans) {

		ArrayOfString translator = cxfClient.translatorString(trans);

		return translator.getString();
	}
}
