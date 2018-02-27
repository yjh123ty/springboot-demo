package com.yoga.demo.common;

import java.io.Serializable;
import java.util.Collection;

/**
 * <pre>
 *  用来在分页查询，其作用是将当前查询的某一页的信息
 *  另外，也提供了转载用户序列化对象的把手
 * </pre>
 * @author vencent.lu
 *
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 3200069927678126482L;
	
	private int pageNumber = 1;

	private int pageSize = 0;

	private int total = 0;
	
	private Collection<T> rows;
	
	private int pageNumCount = -1;
	
	private String actionUrl;
	
	/**
	 *  bootgrid 分页
	 */
	private Integer current;
	private Integer rowCount;
	
	

	public Page(){}
	
	/**
	 * @param offset
	 * @param pageSize
	 */
	public Page(int pageNumber, int pageSize) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	/**
	 * @param offset
	 * @param pageSize
	 */
	public Page(int pageNumber, int pageSize, int total) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.total = total;
	}

	/**
	 * @param offset
	 *            偏移
	 * @param pageSize
	 *            每页大小
	 * @param total
	 *            总共大小
	 * @param userObjects
	 *            取得的当页的对象
	 */
//	public Page(int pageNumber, int pageSize, int total, Collection<T> results) {
//		this.pageNumber = pageNumber;
//		this.pageSize = pageSize;
//		this.total = total;
//		this.results = results;
//	}

	/** 返回总页数， 如果是0条数据， 总页数也是1页 */
	public int getTotalPage() {
		if (pageSize <= 0)
			return 1;
		int totalPage = total / pageSize + (total%pageSize==0?0:1);
		totalPage = total == 0 ? 1 : totalPage;
		return totalPage;
	}

	/**
	 * 返回偏移量，如果越界，则返回最后一页的偏移量
	 * 
	 * @return
	 */
	public int getOffset() {
		if (total == 0) {
			return 0; //(pageNumber - 1) * pageSize;
		}
		if ((pageNumber - 1) * pageSize >= total) {
			pageNumber = getTotalPage();
		}
		return (pageNumber - 1) * pageSize;
	}

	public int getPageNumber() {
		if(pageNumber>this.getTotalPage()){
			return this.getTotalPage();
		}else if(pageNumber<1){
			return 1;
		}
		return pageNumber;
	}
		
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the pageNumCount
	 */
	public int getPageNumCount() {
		return pageNumCount;
	}

	/**
	 * @param pageNumCount the pageNumCount to set
	 */
	public void setPageNumCount(int pageNumCount) {
		this.pageNumCount = pageNumCount;
	}

	/**
	 * @return the actionUrl
	 */
	public String getActionUrl() {
		return actionUrl;
	}

	/**
	 * @param actionUrl the actionUrl to set
	 */
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public Collection<T> getRows() {
		return rows;
	}

	public void setRows(Collection<T> rows) {
		this.rows = rows;
	}
	
	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	

}
