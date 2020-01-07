
package com.javaxxz.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaxxz.common.vo.ShiroUser;
import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.StrKit;

@Controller
@RequestMapping("/cache")
public class CacheController extends BladeController {

	public void index() {

	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/getBtn")
	public AjaxResult getBtn() {
		final String code = getParameter("code");
		ShiroUser user = ShiroKit.getUser();
		final String userId = user.getId().toString();
		final String roleId = user.getRoles().toString();

		Map<String, Object> userRole = Db.selectOneByCache(MENU_CACHE, "roleExt_" + userId, "select * from TFW_ROLE_EXT where userId= #{id}", Paras.create().set("id", userId));

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			roleIn = Func.toStr(userRole.get("ROLEIN"));
			roleOut = Func.toStr(userRole.get("ROLEOUT"));
		}
		final StringBuilder sql = new StringBuilder();
		sql.append("select TFW_MENU.* ,(select name from TFW_MENU where code=#{code}) as PNAME  from TFW_MENU");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and (icon like '%btn%' or icon like '%icon%' ) ) ");
		sql.append("	 and (pCode=#{code})");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (#{join(roleId)})) or id in (#{join(roleIn)}))");
		sql.append("	 and id not in(#{join(roleOut)})");
		sql.append("	)");
		sql.append(" order by num");


		List<Map> btnList = Db.selectListByCache(MENU_CACHE, "btnList_" + code + "_" + userId, sql.toString(), 
				Paras.create()
				.set("code", code)
				.set("roleId", roleId.toString().split(","))
				.set("roleIn", roleIn.split(","))
				.set("roleOut", roleOut.split(",")));
		
		
		
		return json(btnList);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/getChildBtn")
	public AjaxResult getChildBtn() {
		final String code = getParameter("code");
		ShiroUser user = ShiroKit.getUser();
		final String userId = user.getId().toString();
		final String roleId = user.getRoles().toString();

		Map<String, Object> userRole = Db.selectOneByCache(MENU_CACHE, "roleExt_" + userId, "select * from TFW_ROLE_EXT where userId= #{id}", Paras.create().set("id", userId));

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			roleIn = Func.toStr(userRole.get("ROLEIN"));
			roleOut = Func.toStr(userRole.get("ROLEOUT"));
		}
		final StringBuilder sql = new StringBuilder();
		sql.append("select TFW_MENU.* ,(select name from TFW_MENU where code=#{code}) as PNAME  from TFW_MENU");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and (icon like '%btn%' or icon like '%icon%' ) ) ");
		sql.append("	 and (pCode=#{code})");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (#{join(roleId)})) or id in (#{join(roleIn)}))");
		sql.append("	 and id not in(#{join(roleOut)})");
		sql.append("	)");
		sql.append(" order by num");

		List<Map> btnList = Db.selectListByCache(MENU_CACHE, "childBtnList_" + code + "_" + userId, sql.toString(), 
				Paras.create()
				.set("code", code)
				.set("roleId", roleId.toString().split(","))
				.set("roleIn", roleIn.split(","))
				.set("roleOut", roleOut.split(",")));
		
		return json(btnList);
	}


	@ResponseBody
	@RequestMapping("/getSelect")
	public AjaxResult getSelect() {
		final String code = getParameter("code");
		final String num = getParameter("num");
		List<Map<String, Object>> dict = CacheKit.get(DICT_CACHE, DICT_SELECT + code,
				new ILoader() {
					public Object load() {
						return Db.selectList("select num as ID,pId as PID,name as TEXT from  TFW_DICT where code=#{code} and num>0", Paras.create().set("code", code));
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> dic : dict) {
			sb.append("<option value=\"" + dic.get("ID") + "\">" + dic.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/getCombo")
	public AjaxResult getCombo() {
		final String code = getParameter("code");
		List<Map<String, Object>> dict = CacheKit.get(DICT_CACHE, DICT_COMBO + code,
				new ILoader() {
					public Object load() {
						return Db.selectList("select num as \"id\",name as \"text\" from  TFW_DICT where code=#{code} and num>0", Paras.create().set("code", code), new AopContext("ztree"));
					}
				});

		return json(dict);
	}

	@ResponseBody
	@RequestMapping("/getDeptSelect")
	public AjaxResult getDeptSelect() {
		final String num = getParameter("num");
		List<Map<String, Object>> dept = CacheKit.get(DEPT_CACHE, DEPT_ALL_LIST,
				new ILoader() {
					public Object load() {
						return Db.selectList("select ID,PID,simpleName as TEXT from  TFW_DEPT order by pId,num asc", Paras.create(), new AopContext(), Cst.me().getDefaultSelectFactory().deptIntercept());
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _dept : dept) {
			sb.append("<option value=\"" + _dept.get("ID") + "\">" + _dept.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/getUserSelect")
	public AjaxResult getUserSelect() {
		final String num = getParameter("num");
		List<Map<String, Object>> dept = CacheKit.get(DEPT_CACHE, DICT_SELECT_USER,
				new ILoader() {
					public Object load() {
						return Db.selectList("select ID,name TEXT from TFW_USER where status=1 and name is not null order by name ", Paras.create(), new AopContext(), Cst.me().getDefaultSelectFactory().userIntercept());
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _dept : dept) {
			sb.append("<option value=\"" + _dept.get("ID") + "\">" + _dept.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/getRoleSelect")
	public AjaxResult getRoleSelect() {
		final String num = getParameter("num");
		List<Map<String, Object>> dept = CacheKit.get(ROLE_CACHE, ROLE_ALL_LIST,
				new ILoader() {
					public Object load() {
						return Db.selectList("select ID,name TEXT from TFW_Role where  name is not null order by name ", Paras.create(), new AopContext("ztree"));
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _dept : dept) {
			sb.append("<option value=\"" + _dept.get("ID") + "\">" + _dept.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/dicTreeList")
	public AjaxResult dicTreeList() {
		List<Map<String, Object>> dic = CacheKit.get(DICT_CACHE, DICT_TREE_ALL,
				new ILoader() {
					public Object load() {
						return Db.selectList("select code \"code\",id \"id\",pId \"pId\",name \"name\",num \"num\",'false' \"open\" from TFW_DICT order by code asc,num asc", Paras.create());
					}
				});

		return json(dic);
	}

	@ResponseBody
	@RequestMapping("/deptTreeList")
	public AjaxResult deptTreeList() {
		List<Map<String, Object>> dept = CacheKit.get(DEPT_CACHE, DEPT_TREE_ALL + "_" + ShiroKit.getUser().getId(),
				new ILoader() {
					public Object load() {
						return Db.selectList("select id \"id\",pId \"pId\",simpleName as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DEPT ", Paras.create(), new AopContext("ztree"), Cst.me().getDefaultSelectFactory().deptIntercept());
					}
				});

		return json(dept);
	}

	@ResponseBody
	@RequestMapping("/roleTreeList")
	public AjaxResult roleTreeList() {
		List<Map<String, Object>> dept = CacheKit.get(ROLE_CACHE, ROLE_TREE_ALL + "_" + ShiroKit.getUser().getId(),
				new ILoader() {
					public Object load() {
						return Db.selectList("select id \"id\",pId \"pId\",name as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_ROLE ", Paras.create(), new AopContext("ztree"), Cst.me().getDefaultSelectFactory().roleIntercept());
					}
				});

		return json(dept);
	}

	@ResponseBody
	@RequestMapping("/getDicById")
	public AjaxResult getDicById() {
		final int id = getParameterToInt("id");
		List<Map<String, Object>> dict = CacheKit.get(DICT_CACHE, DICT + id,
				new ILoader() {
					public Object load() {
						return Db.selectList("select CODE from TFW_DICT where id=#{id}",Paras.create().set("id", id), new AopContext("ztree"));
					}
				});
		return json(dict);
	}

	@ResponseBody
	@RequestMapping("/menuTreeList")
	public AjaxResult menuTreeList() {
		List<Map<String, Object>> menu = CacheKit.get(MENU_CACHE, MENU_TREE_ALL,
				new ILoader() {
					public Object load() {
						return Db.selectList("select code \"id\",pCode \"pId\",name \"name\",(case when levels=1 then 'true' else 'false' end) \"open\" from TFW_MENU where status=1 order by levels asc,num asc");
					}
				});

		return json(menu);
	}

	@ResponseBody
	@RequestMapping("/menuTreeListByRoleId")
	public AjaxResult menuTreeListByRoleId() {
		final String roleId = getParameter("roleId", "0");
		List<Map<String, Object>> menu = CacheKit.get(MENU_CACHE, MENU_TREE + roleId,
				new ILoader() {
					@SuppressWarnings("rawtypes")
					public Object load() {
						String table = "TFW_MENU";
						String pid = "";
						List<Map> record = Db.selectList("select PID from TFW_ROLE where id in (" + roleId + ")"); 
						for (Map p : record) {
							if (!Func.isEmpty(p.get("PID"))) {
								pid += p.get("PID").toString() + ",";
							}
						}
						if (!Func.isEmpty(pid)) {
							pid = StrKit.removeSuffix(pid, ",");
							table = "(select * from TFW_MENU where id in( select MENUID from TFW_RELATION where roleId in ("
									+ pid + ") ))";
						}
						StringBuilder sb = new StringBuilder();
						sb.append("select m.id \"id\",(select id from TFW_MENU  where code=m.pCode) \"pId\",name \"name\",(case when m.levels=1 then 'true' else 'false' end) \"open\",(case when r.menuId is not null then 'true' else 'false' end) \"checked\"");
						sb.append(" from ");
						sb.append(table);
						sb.append(" m left join (select MENUID from TFW_RELATION where roleId in ("
								+ roleId
								+ ") GROUP BY MENUID) r on m.id=r.menuId where m.status=1 order by m.levels,m.num asc");
						return Db.selectList(sb.toString());
					}
				});

		return json(menu);
	}

	@ResponseBody
	@RequestMapping("/roleTreeListById")
	public AjaxResult roleTreeListById() {
		final String Id = getParameter("id");
		final String roleId = getParameter("roleId", "0");
		List<Map<String, Object>> menu = CacheKit.get(ROLE_CACHE, ROLE_TREE + Id,
				new ILoader() {
					public Object load() {
						String sql = "select id \"id\",pId \"pId\",name as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_ROLE order by pId,num asc";
						if (Id.indexOf(",") == -1) {
							sql = "select r.id \"id\",pId \"pId\",name as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\",(case when (r1.ID=0 or r1.ID is null) then 'false' else 'true' end) \"checked\" from  TFW_ROLE r left join (select ID  from TFW_ROLE where ID in ("
									+ roleId
									+ ")) r1 on r.ID=r1.ID order by pId,num asc";
						}
						return Db.selectList(sql);
					}
				});

		return json(menu);
	}
}
