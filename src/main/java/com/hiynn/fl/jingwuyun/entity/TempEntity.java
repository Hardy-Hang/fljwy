package com.hiynn.fl.jingwuyun.entity;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * <p>Title: TempEntity </p>
 * <p>Description: TODO </p>
 * Date: 2016年7月26日 下午1:48:30
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date               Author           Content
 * ==========================================================
 * 2016年7月26日         hangzongguo         创建文件,实现基本功能
 * 
 * ==========================================================
 */
public class TempEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用于打印TempEntity实例对象
	 */
	private static Gson gson = new Gson();
	private int id;
	private String name;
	/**
	 * TempEntity.java
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * TempEntity.java
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * TempEntity.java
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * TempEntity.java
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
}
