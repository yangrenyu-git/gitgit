
package com.javaxxz.core.toolbox.grid;

import java.util.List;
import java.util.Map;

import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.toolbox.support.BladePage;

@SuppressWarnings("unchecked")
public class LigerGridFactory extends BaseGridFactory {

	public LigerGrid<Map<String, Object>> paginate(String slaveName, Integer page, Integer rows,
			String source, String para, String sort, String order,
			IQuery intercept, BladeController ctrl) {
		BladePage<Map<String, Object>> list = (BladePage<Map<String, Object>>) super.basePaginate(slaveName, page, rows, source, para, sort, order, intercept, ctrl);
		
		int _pageNum = (int) list.getPage();
		int _pageSize = (int) list.getPageSize();
		int _totalPage = (int) list.getTotal();
		long _totalSize = list.getRecords();
		List<Map<String, Object>> _rows = list.getRows();
		
		LigerGrid<Map<String, Object>> grid = new LigerGrid<>(_pageNum, _pageSize, _totalPage, _totalSize, _rows);
		return grid;
	}

}
