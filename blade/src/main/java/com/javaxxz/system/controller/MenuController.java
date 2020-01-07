
package com.javaxxz.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaxxz.common.base.BaseController;
import com.javaxxz.core.annotation.Before;
import com.javaxxz.core.annotation.Permission;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.JsonKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.meta.intercept.MenuValidator;
import com.javaxxz.system.model.Menu;
import com.javaxxz.system.service.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController implements ConstShiro{
	private static String LIST_SOURCE = "menu.list";
	private static String BASE_PATH = "/system/menu/";
	private static String CODE = "menu";
	private static String PREFIX = "TFW_MENU";

	@Autowired
	MenuService service;

	@RequestMapping("/")
	@Permission(ADMINISTRATOR)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "menu.html";
	}

	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		if(ShiroKit.lacksRole(ADMINISTRATOR)){
			return REDIRECT_UNAUTH;
		}
		mm.put("code", CODE);
		return BASE_PATH + "menu_add.html";
	}
	
	@RequestMapping(KEY_ADD + "/{id}")
	@Permission(ADMINISTRATOR)
	public String add(@PathVariable String id, ModelMap mm) {
		if (StrKit.notBlank(id)) {
			Menu menu = service.findById(id);
			mm.put("PCODE", menu.getCode());
			mm.put("LEVELS", menu.getLevels() + 1);
			mm.put("NUM", service.findLastNum(menu.getCode()));
		}
		mm.put("code", CODE);
		return BASE_PATH + "menu_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	@Permission(ADMINISTRATOR)
	public String edit(@PathVariable String id, ModelMap mm) {
		Menu menu = service.findById(id);
		mm.put("model", JsonKit.toJson(menu));
		mm.put("code", CODE);
		return BASE_PATH + "menu_edit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	@Permission(ADMINISTRATOR)
	public String view(@PathVariable String id, ModelMap mm) {
		Menu menu = service.findById(id);
		mm.put("model", JsonKit.toJson(menu));
		mm.put("code", CODE);
		return BASE_PATH + "menu_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	@Permission(ADMINISTRATOR)
	public Object list() {
		Object gird = paginate(LIST_SOURCE);
		return gird;
	}

	@ResponseBody
	@Before(MenuValidator.class)
	@RequestMapping(KEY_SAVE)
	@Permission(ADMINISTRATOR)
	public AjaxResult save() {
		Menu menu = mapping(PREFIX, Menu.class);
		menu.setStatus(1);
		boolean temp = service.save(menu);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@Before(MenuValidator.class)
	@RequestMapping(KEY_UPDATE)
	@Permission(ADMINISTRATOR)
	public AjaxResult update() {
		Menu menu = mapping(PREFIX, Menu.class);
		menu.setVersion(getParameterToInt("VERSION", 0) + 1);
		boolean temp = service.update(menu);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_DEL)
	@Permission(ADMINISTRATOR)
	public AjaxResult del() {
		boolean temp = service.updateStatus(getParameter("ids"), 2);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_RESTORE)
	@Permission(ADMINISTRATOR)
	public AjaxResult restore(@RequestParam String ids) {
		boolean temp = service.updateStatus(ids, 1);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			return success(RESTORE_SUCCESS_MSG);
		} else {
			return error(RESTORE_FAIL_MSG);
		}

	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	@Permission(ADMINISTRATOR)
	public AjaxResult remove(@RequestParam String ids) {
		int cnt = service.deleteByIds(ids);
		if (cnt > 0) {
			CacheKit.removeAll(MENU_CACHE);
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping("/getMenu")
	public List<Map<String, Object>> getMenu(){
		final Object userId = getParameter("userId");
		final Object roleId = getParameter("roleId");

		Map<String, Object> userRole = CacheKit.get(MENU_CACHE, ROLE_EXT + userId, new ILoader() {
			@Override
			public Object load() {
				return Db.selectOne("select * from TFW_ROLE_EXT where USERID=#{userId}", Paras.create().set("userId", userId));
			}
		}); 


		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			Paras rd = Paras.parse(userRole);
			roleIn = rd.getStr("ROLEIN");
			roleOut = rd.getStr("ROLEOUT");
		}
		final StringBuilder sql = new StringBuilder();
		sql.append("select * from TFW_MENU  ");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and icon not LIKE '%btn%' and icon not LIKE '%icon%' ) ");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (" + roleId + ")) or id in (" + roleIn + "))");
		sql.append("	 and id not in(" + roleOut + ")");
		sql.append("	)");
		sql.append(" order by levels,pCode,num");

		List<Map<String, Object>> sideBar = CacheKit.get(MENU_CACHE, SIDEBAR + userId, new ILoader() {
			@Override
			public Object load() {
				return Db.selectList(sql.toString());
			}
		}); 
		return sideBar;
	}

}
