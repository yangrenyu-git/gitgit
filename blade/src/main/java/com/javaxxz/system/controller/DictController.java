
package com.javaxxz.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaxxz.common.base.BaseController;
import com.javaxxz.core.annotation.Before;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.JsonKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.meta.intercept.DictValidator;
import com.javaxxz.system.model.Dict;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController{
	private static String LIST_SOURCE = "dict.list";
	private static String BASE_PATH = "/system/dict/";
	private static String CODE = "dict";
	private static String PREFIX = "tfw_dict";
	
	@RequestMapping("/")
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "dict.html";
	}
	
	
	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object gird = paginate(LIST_SOURCE);
		return gird;
	}
	
	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "dict_add.html";
	}
	
	@RequestMapping(KEY_ADD + "/{id}")
	public String add(@PathVariable String id, ModelMap mm) {
		if (StrKit.notBlank(id)) {
			Dict dict = Blade.create(Dict.class).findById(id);
			mm.put("dictcode", dict.getCode());
			mm.put("pId", id);
			mm.put("num", findLastNum(dict.getCode()));
		}
		mm.put("code", CODE);
		return BASE_PATH + "dict_add.html";
	}
	
	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		Dict dict = Blade.create(Dict.class).findById(id);
		mm.put("model", JsonKit.toJson(dict));
		mm.put("code", CODE);
		return BASE_PATH + "dict_edit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Blade blade = Blade.create(Dict.class);
		Dict dict = blade.findById(id);
		Dict parent = blade.findById(dict.getPid());
		String pName = (null == parent) ? "" : parent.getName();
		Paras rd = Paras.parse(dict);
		rd.set("pName", pName);
		mm.put("model", JsonKit.toJson(rd));
		mm.put("code", CODE);
		return BASE_PATH + "dict_view.html";
	}
	
	@ResponseBody
	@Before(DictValidator.class)
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		Dict dict = mapping(PREFIX, Dict.class);
		boolean temp = Blade.create(Dict.class).save(dict);
		if (temp) {
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(DIY_CACHE);
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@Before(DictValidator.class)
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		Dict dict = mapping(PREFIX, Dict.class);
		boolean temp =  Blade.create(Dict.class).update(dict);
		if (temp) {
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(DIY_CACHE);
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove() {
		int cnt = Blade.create(Dict.class).deleteByIds(getParameter("ids"));
		if (cnt > 0) {
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(DIY_CACHE);
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}

	
	private int findLastNum(String code){
		try{
			Blade blade = Blade.create(Dict.class);
			Dict dict = blade.findFirstBy("code = #{code} order by num desc", Paras.create().set("code", code));
			return dict.getNum() + 1;
		}
		catch(Exception ex){
			return 1;
		}
	}
	
	
}
