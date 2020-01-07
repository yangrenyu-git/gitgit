package com.javaxxz.common.tool;

import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.constant.ConstCacheKey;
import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.model.Dept;
import com.javaxxz.system.model.Dict;
import com.javaxxz.system.model.Parameter;
import com.javaxxz.system.model.Role;
import com.javaxxz.system.model.User;

public class SysCache implements ConstCache, ConstCacheKey{

	public static String getDictName(final Object code, final Object num) {
		Dict dict = CacheKit.get(DICT_CACHE, GET_DICT_NAME + code + "_" + num, new ILoader() {
			@Override
			public Object load() {
				return Blade.create(Dict.class).findFirstBy("code=#{code} and num=#{num}", Paras.create().set("code", code).set("num", num));
			}
		});
		if(null == dict){
			return "";
		}
		return dict.getName();
	}


	public static String getRoleName(final Object roleIds) {
		if(Func.isEmpty(roleIds)){
			return "";
		}
		final String [] roleIdArr = roleIds.toString().split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < roleIdArr.length; i++){
			final String roleId = roleIdArr[i];
			Role role = CacheKit.get(ROLE_CACHE, GET_ROLE_NAME + roleId, new ILoader() {
				@Override
				public Object load() {
					return Blade.create(Role.class).findById(roleId);
				}
			});
			sb.append(role.getName()).append(",");
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}


	public static String getRoleAlias(final Object roleIds) {
		if(Func.isEmpty(roleIds)){
			return "";
		}
		final String [] roleIdArr = roleIds.toString().split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < roleIdArr.length; i++){
			final String roleId = roleIdArr[i];
			Role role = CacheKit.get(ROLE_CACHE, GET_ROLE_ALIAS + roleId, new ILoader() {
				@Override
				public Object load() {
					return Blade.create(Role.class).findById(roleId);
				}
			});
			sb.append(role.getTips()).append(",");
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}


	public static String getUserName(final Object userId) {
		User user = CacheKit.get(USER_CACHE, GET_USER_NAME + userId, new ILoader() {
			@Override
			public Object load() {
				return Blade.create(User.class).findById(userId);
			}
		});
		if(null == user){
			return "";
		}
		return user.getName();
	}


	public static String getDeptName(final Object deptIds) {
		if(Func.isEmpty(deptIds)){
			return "";
		}
		final String [] deptIdArr = deptIds.toString().split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < deptIdArr.length; i++){
			final String deptId = deptIdArr[i];
			Dept dept = CacheKit.get(DEPT_CACHE, GET_DEPT_NAME + deptId, new ILoader() {
				@Override
				public Object load() {
					return Blade.create(Dept.class).findById(deptId);
				}
			});
			sb.append(dept.getSimplename()).append(",");
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}
	

	public static String getParamByCode(String code){
		Parameter param = Blade.create(Parameter.class).findFirstBy("code = #{code} and status = 1", Paras.create().set("code", code));
		return param.getPara();
	}
}
