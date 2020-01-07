
package com.javaxxz.core.interfaces;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

import com.javaxxz.common.vo.ShiroUser;
import com.javaxxz.system.model.User;


public interface IShiro {
	User user(String account);

	ShiroUser shiroUser(User user);

	@SuppressWarnings("rawtypes")
	List<Map> findPermissionsByRoleId(Object userId, String roleId);

	String findRoleNameByRoleId(String roleId);
	
	SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName);
}
