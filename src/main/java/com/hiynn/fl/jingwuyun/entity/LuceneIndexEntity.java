package com.hiynn.fl.jingwuyun.entity;

/**
 * <p>Title: LuceneIndexEntity </p>
 * <p>Description: TODO </p>
 * Date: 2016年8月9日 下午2:10:09
 * @author admin@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date               Author           Content
 * ==========================================================
 * 2016年8月9日         admin         创建文件,实现基本功能
 * 
 * ==========================================================
 */
public class LuceneIndexEntity {
	private int id;
	private String operation;
	private String primaryKey;
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	/**
	 * <p>Title: LuceneIndexEntity </p>
	 * <p>Description: Constructor </p> 
	 * @param id
	 * @param operation
	 * @param primaryKey 
	 */
	public LuceneIndexEntity(int id, String operation, String primaryKey) {
		super();
		this.id = id;
		this.operation = operation;
		this.primaryKey = primaryKey;
	}
	/**
	 * <p>Title: LuceneIndexEntity </p>
	 * <p>Description: Constructor </p>  
	 */
	public LuceneIndexEntity() {
		super();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LuceneIndexEntity [id=" + id + ", operation=" + operation + ", primaryKey=" + primaryKey + "]";
	}
	/**
	 * LuceneIndexEntity.java
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * LuceneIndexEntity.java
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * LuceneIndexEntity.java
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * LuceneIndexEntity.java
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
