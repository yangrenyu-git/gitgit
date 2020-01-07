
package com.javaxxz.core.toolbox.grid;

import java.util.List;
import java.util.Map;

import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.toolbox.support.BladePage;

@SuppressWarnings("unchecked")
public class JqGridFactory extends BaseGridFactory {

	public JqGrid<Map<String, Object>> paginate(String slaveName, Integer page, Integer rows,
			String source, String para, String sort, String order,
			IQuery intercept, BladeController ctrl) {
		
		BladePage<Map<String, Object>> list = (BladePage<Map<String, Object>>) super.basePaginate(slaveName, page, rows, source, para, sort, order, intercept, ctrl);
		
		List<Map<String, Object>> _rows = list.getRows();
		long _total = list.getTotal();
		long _page = list.getPage();
		long _records = list.getRecords();
		
		JqGrid<Map<String, Object>> grid = new JqGrid<>(_rows, _total, _page, _records);
		return grid;
		
	}

}
