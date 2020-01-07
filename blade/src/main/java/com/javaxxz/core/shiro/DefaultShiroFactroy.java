
package com.javaxxz.core.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

import com.javaxxz.common.vo.ShiroUser;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.interfaces.IShiro;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.system.model.User;

public class DefaultShiroFactroy implements IShiro{
	
	public User user(String account) {
		User user = Blade.create(User.class).findFirstBy("account = #{account}", Paras.create().set("account", account));
		// 账号不存在
		if (null == user) {
			throw new UnknownAccountException();
		}
		// 账号未审核
		if (user.getStatus() == 3 || user.getStatus() == 4) {
			throw new DisabledAccountException();
		}
		// 账号被冻结
		if (user.getStatus() == 2 || user.getStatus() == 5) {
			throw new DisabledAccountException();
		}
		return user;
	}

	public ShiroUser shiroUser(User user) {
		List<String> roleList = new ArrayList<>();
		String[] roles = user.getRoleid().split(",");
		for (int i = 0; i < roles.length; i++) {
			roleList.add(roles[i]);
		}
		return new ShiroUser(user.getId(), user.getDeptid(), user.getAccount(), user.getName(), roleList);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> findPermissionsByRoleId(final Object userId, String roleId) {
		Map<String, Object> userRole =  Db.selectOneByCache(ConstCache.MENU_CACHE, 
															"role_ext_" + userId, 
															"select * from TFW_ROLE_EXT where USERID=#{userId}", 
															Paras.create().set("userId", userId));

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			Paras rd = Paras.parse(userRole);
			roleIn = rd.getStr("ROLEIN");
			roleOut = rd.getStr("ROLEOUT");
		}
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append("select ID,CODE,URL from TFW_MENU  ");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (url is not null) ");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (#{join(roleId)})) or id in (#{join(roleIn)}))");
		sql.append("	 and id not in(#{join(roleOut)})");
		sql.append("	)");
		sql.append(" order by levels,pCode,num");

		List<Map> permissions = Db.selectListByCache(ConstCache.MENU_CACHE, "permissions_" + userId, sql.toString(), Paras.create()
				.set("roleId", roleId.split(",")).set("roleIn", roleIn.split(",")).set("roleOut", roleOut.split(","))
				);
		
		return permissions;
	}

	@SuppressWarnings("unchecked")
	public String findRoleNameByRoleId(final String roleId) {
		Map<String, Object> map = Db.selectOneByCache(ConstCache.ROLE_CACHE, 
														"findRoleNameByRoleId" + roleId, 
														"select TIPS from tfw_role where id = #{id}", 
														Paras.create().set("id", roleId));
		return Func.toStr(map.get("TIPS"));
	}

	public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
		String credentials = user.getPassword();
		// 密码加盐处理
		String source = user.getSalt();
		ByteSource credentialsSalt = new Md5Hash(source);
		return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
	}

}
