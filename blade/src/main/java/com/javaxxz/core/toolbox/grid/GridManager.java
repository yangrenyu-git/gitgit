
package com.javaxxz.core.toolbox.grid;

import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.IGrid;
import com.javaxxz.core.interfaces.IQuery;

public class GridManager {
	private final static GridManager me = new GridManager();

	private IGrid defaultGridFactory = Cst.me().getDefaultGridFactory();

	public static GridManager me() {
		return me;
	}

	private GridManager() {}

	public void setDefaultGridFactory(IGrid defaultGridFactory) {
		this.defaultGridFactory = defaultGridFactory;
	}
	
	public static Object paginate(String slaveName, Integer page, Integer rows, String source, String para, String sort, String order, IQuery intercept, BladeController ctrl) {
		return me.defaultGridFactory.paginate(slaveName, page, rows, source, para, sort, order, intercept, ctrl);
	}

}
