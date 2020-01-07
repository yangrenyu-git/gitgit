
package com.javaxxz.core.toolbox.log;

import java.util.Date;
import java.util.Map;

import com.javaxxz.common.vo.ShiroUser;
import com.javaxxz.core.constant.Const;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.constant.ConstCacheKey;
import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.interfaces.ILog;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.system.model.OperationLog;


public class BladeLogFactory implements ILog {

	public String[] logPatten() {
		String[] patten = { "login", "logout", "grant", "save", "update", "remove", "del", "delete", "restore" };
		return patten;
	}

	public Paras logMaps() {
		Paras rd = Paras.create()
				.set("login", "登录")
				.set("logout", "登出")
				.set("grant", "授权")
				.set("save", "新增")
				.set("update", "修改")
				.set("remove", "删除")
				.set("del", "删除")
				.set("delete", "删除")
				.set("restore", "还原");
		return rd;
	}

	public boolean isDoLog() {
		@SuppressWarnings("rawtypes")
		Map map = CacheKit.get(ConstCache.SYS_CACHE, ConstCacheKey.PARAMETER_LOG, new ILoader() {
			@Override
			public Object load() {
				return Db.selectOne("select para from tfw_parameter where code = #{code}", Paras.create().set("code", Const.PARA_LOG_CODE));
			}
		}); 
		if(map.get("para").equals("1")){
			return true;
		}
		return false;
	}
	
	public boolean doLog(String logName, String msg, boolean succeed) {
		ShiroUser user = ShiroKit.getUser();
		if (null == user) {
			return true;
		}
		try {
			OperationLog log = new OperationLog();
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed((succeed)?"1":"0");
			log.setUserid(Func.toStr(user.getId()));
			log.setLogname(logName);
			boolean temp = Blade.create(OperationLog.class).save(log);
			return temp;
		} catch (Exception ex) {
			return false;
		}
	}

}
