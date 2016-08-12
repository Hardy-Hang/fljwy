package com.hiynn.fl.jingwuyun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hiynn.fl.jingwuyun.entity.TempEntity;
import com.hiynn.fl.jingwuyun.service.TempService;

/**
 * <p> * Title: TempController * </p>
 * <p> * Description:  * </p>
 * Date: 2016年7月26日 下午2:22:33
 * 
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify： 
 * Date               Author               Content
 *========================================================== 
 *2016年7月26日            hangzongguo           用于测试事务
 * 
 *==========================================================
 */
@Controller
public class TempController {

	@Autowired
	private TempService tempService;

	/**
	 * 
	 * <p>Title: getAll </p>
	 * <p>Description: 读取所有 </p>
	 * @return
	 */
	@RequestMapping(value = "tempget", method = RequestMethod.GET)
	public @ResponseBody
	List<TempEntity> getAll() {

		return tempService.getAll();

	}

	/**
	 * 
	 * <p>Title: insertAll </p>
	 * <p>Description: 插入 </p>
	 * @param id
	 * @param name
	 * @throws Exception
	 */
	@RequestMapping(value = "tempinsert/id/{id}/name/{name}", method = RequestMethod.GET)
	public void insertAll(@PathVariable("id") int id, @PathVariable("name") String name) throws Exception {

		TempEntity te = new TempEntity();
		te.setId(id);
		te.setName(name);

		tempService.insertAll(te);

	}

	/**
	 * 
	 * <p>Title: getCxf </p>
	 * <p>Description: 按照cxf读取 </p>
	 * @param trans
	 * @return
	 */
	@RequestMapping(value = "tempcxf/trans/{trans}", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getCxf(@PathVariable("trans") String trans) {

		return tempService.getCxf(trans);

	}
}
