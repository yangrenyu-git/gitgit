
package com.javaxxz.system.meta.intercept;

import com.javaxxz.common.tool.SysCache;
import com.javaxxz.core.aop.Invocation;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.intercept.BladeValidator;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.CollectionKit;
import com.javaxxz.core.toolbox.kit.StrKit;

public class RoleValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		validateRole("roleId", "ids", "超级管理员不能去掉角色管理的权限!");
	}

	protected void validateRole(String field1, String field2, String errorMessage) {
		String ids = request.getParameter(field2);
		if (StrKit.isBlank(ids)) {
			addError("请选择权限!");
		} 
		String roleId = request.getParameter(field1);
		String roleAlias = SysCache.getRoleAlias(roleId);
		if(roleAlias.equals(ConstShiro.ADMINISTRATOR)){
			String[] id = ids.split(",");
			String authority = Db.queryStr("select id from tfw_menu where code = #{code}", Paras.create().set("code", "role_authority"));
			if(!CollectionKit.contains(id, authority)){
				//超管不包含权限配置则报错
				addError(errorMessage);
			}
		}
	}

}
