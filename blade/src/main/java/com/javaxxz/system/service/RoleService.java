
package com.javaxxz.system.service;

import com.javaxxz.core.annotation.DoLog;
import com.javaxxz.core.base.service.IService;
import com.javaxxz.system.model.Role;

public interface RoleService extends IService<Role> {
	int findLastNum(String id);

	@DoLog(name = "设置权限")
	boolean authority(String ids, String roleId);

	int getParentCnt(String id);
}
