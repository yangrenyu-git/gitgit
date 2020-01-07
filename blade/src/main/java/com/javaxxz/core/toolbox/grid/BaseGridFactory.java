
package com.javaxxz.core.toolbox.grid;

import java.util.HashMap;
import java.util.Map;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.constant.Const;
import com.javaxxz.core.interfaces.IGrid;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.plugins.dao.Md;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.kit.JsonKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.support.SqlKeyword;


public abstract class BaseGridFactory implements IGrid{


	protected Object basePaginate(String slaveName, Integer page, Integer rows, String source, String para, String sort, String order, IQuery intercept, BladeController ctrl) {
		if (source.toLowerCase().indexOf("select") == -1) {
			return paginateById(slaveName, page, rows, source, para, sort, order, intercept, ctrl);
		} else {
			return paginateBySql(slaveName, page, rows, source, para, sort, order, intercept, ctrl);
		}
	}
	
	private Object paginateById(String slaveName, Integer page, Integer rows, String sqlId, String para, String sort, String order, IQuery intercept, BladeController ctrl) {	
		String sqlTemplate = Md.getSql(sqlId);
		return paginateBySql(slaveName, page, rows, sqlTemplate, para, sort, order, intercept, ctrl);
	}

	private Object paginateBySql(String slaveName, Integer page, Integer rows, String sqlTemplate, String para, String sort, String order, IQuery intercept, BladeController ctrl) {
		String sqlex = SqlKeyword.getWhere(para);
		Map<String, Object> map = getSqlMap(para, sort, order);	
		String statement = "select * from (" + sqlTemplate + ") blade_statement";
		
		// 查询前拦截
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setSql(statement + sqlex);
			ac.setCondition("");
			ac.setParam(map);
			intercept.queryBefore(ac);
			statement = statement + (StrKit.isBlank(ac.getWhere()) ? (sqlex + ac.getCondition()) : ac.getWhere());
		} else {
			statement = statement + sqlex;
		}

		Object list = null;
		String orderBy = (Func.isEmpty(map.get(Const.ORDER_BY_STR))) ? " " : (" order by " + Func.toStr(map.get(Const.ORDER_BY_STR)));
		if(StrKit.notBlank(slaveName)){
			list = Db.init(slaveName).paginate(statement + orderBy, Map.class, map, page, rows);			
		} else {
			list = Db.paginate(statement + orderBy, Map.class, map, page, rows);
		}

		// 查询后拦截
		if (null != intercept) {
			ac.setObject(list);
			intercept.queryAfter(ac);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getSqlMap(String para, String sort, String order){
		Map<String, Object> map = JsonKit.parse(Func.isEmpty(Func.decodeUrl(para)) ? null : Func.decodeUrl(para), HashMap.class);
		if (Func.isEmpty(map)) {
			map = new HashMap<>();
		}
		map.put(Const.ORDER_BY_STR, Func.isAllEmpty(sort, order) ? "" : (sort + " " + order));
		return map;
	}
}
