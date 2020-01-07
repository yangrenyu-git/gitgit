
package com.javaxxz.core.toolbox.check;

import javax.servlet.http.HttpServletRequest;

import com.javaxxz.common.vo.ShiroUser;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.ICheck;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.kit.CollectionKit;
import com.javaxxz.core.toolbox.kit.HttpKit;


public class PermissionCheckFactory implements ICheck {

	@Override
	public boolean check(Object[] permissions) {
		ShiroUser user = ShiroKit.getUser();
		if (null == user) {
			return false;
		}
		String join = CollectionKit.join(permissions, ",");
		if(ShiroKit.hasAnyRoles(join)){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkAll() {
		HttpServletRequest request = HttpKit.getRequest();
		ShiroUser user = ShiroKit.getUser();
		if (null == user) {
			return false;
		}
		String requestURI = request.getRequestURI().replace(Cst.me().getContextPath(), "");
		String[] str = requestURI.split("/");
		if (str.length > 3) {
			requestURI = str[1] + "/" + str[2];
		}
		if(ShiroKit.hasPermission(requestURI)){
			return true;
		}
		return false;
	}

}
