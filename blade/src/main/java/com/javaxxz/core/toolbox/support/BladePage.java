
package com.javaxxz.core.toolbox.support;

import java.util.List;

import com.javaxxz.core.toolbox.Func;

public class BladePage<T> {


	private List<T> rows;


	private long total;


	private long page;


	private long pageSize;


	private long records;


	private long rowBegin;


	private long rowEnd;

	public BladePage(List<T> rows, long page, long pageSize, long records) {
		setRows(rows);
		setPage(page);
		setPageSize(pageSize);
		setRecords(records);
		onInit();
	}

	private void onInit() {
		this.rowBegin = (page - 1) * pageSize + 1;
		this.rowEnd = page * pageSize;
		long pages = Func.toLong(records / pageSize, 0);
		this.total = (records % this.pageSize == 0L) ? pages : (pages + 1);
	}
	
	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
		if (page < 1)
			this.page = 1;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
		if (pageSize < 1)
			this.pageSize = 5;
	}

	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records < 0 ? 0 : records;
	}

	public long getRowBegin() {
		return rowBegin;
	}

	public long getRowEnd() {
		return rowEnd;
	}

}
