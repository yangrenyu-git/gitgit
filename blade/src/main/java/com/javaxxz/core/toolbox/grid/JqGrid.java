
package com.javaxxz.core.toolbox.grid;

import java.util.List;


public class JqGrid<E> {

	private List<E> rows;


	private long total;


	private long page;


	private long records;

	public JqGrid() {

	}
	
	public JqGrid(List<E> rows, long total, long page, long records) {
		super();
		this.rows = rows;
		this.total = total;
		this.page = page;
		this.records = records;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

}
