
package com.javaxxz.system.service;

import com.javaxxz.core.base.service.IService;
import com.javaxxz.system.model.Menu;

public interface MenuService extends IService<Menu> {
	int findLastNum(String code);

	boolean isExistCode(String code);

	boolean updateStatus(String col, Object status);
}
