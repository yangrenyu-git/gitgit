
package com.javaxxz.core.base.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.javaxxz.common.base.BaseController;
import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.interfaces.IMeta;
import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.plugins.dao.Md;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.kit.ClassKit;
import com.javaxxz.core.toolbox.kit.JsonKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.service.CurdService;

public abstract class CurdController<M> extends BaseController {
	
	@Autowired
	CurdService service;

	private final BladeController ctrl = this;

	private MetaIntercept intercept = null;
	private Class<M> modelClass;
	private IMeta metaFactory;
	private String controllerKey;
	private String paraPrefix;
	private Map<String, Object> switchMap;
	private Map<String, String> renderMap;
	private Map<String, String> sourceMap;

	@SuppressWarnings("unchecked")
	private Class<M> getClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	private void init() {
		this.modelClass = getClazz();
		this.metaFactory = ClassKit.newInstance(metaFactoryClass());
		this.controllerKey = metaFactory.controllerKey();
		this.paraPrefix = metaFactory.paraPrefix();
		this.switchMap = metaFactory.switchMap();
		this.renderMap = metaFactory.renderMap();
		this.sourceMap = metaFactory.sourceMap();
		this.intercept = ClassKit.newInstance(metaFactory.intercept());
	}

	public CurdController() {
		this.init();
	}

	protected abstract Class<? extends IMeta> metaFactoryClass();


	protected Map<String, Object> reverseMap() {
		if (Func.isEmpty(switchMap)) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		for (String key : switchMap.keySet()) {
			map.put((String) switchMap.get(key), key);
		}
		return map;
	}

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView view = new ModelAndView(renderMap.get(KEY_INDEX));
		view.addObject("code", controllerKey);
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, view);
			intercept.renderIndexBefore(ac);
		}
		return view;
	}

	@RequestMapping(KEY_ADD)
	public ModelAndView add() {
		ModelAndView view = new ModelAndView(renderMap.get(KEY_ADD));
		view.addObject("code", controllerKey);
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, view);
			intercept.renderAddBefore(ac);
		}
		return view;
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public ModelAndView edit(@PathVariable String id) {
		ModelAndView view = new ModelAndView(renderMap.get(KEY_EDIT));
		Paras rd = Paras.create();
		if (StrKit.isBlank(sourceMap.get(KEY_EDIT))) {
			M model = Blade.create(modelClass).findById(id);
			rd.parseBean(model);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			Map<String, Object> model = this.find(sourceMap.get(KEY_EDIT), map);
			rd.parseMap(model);
		}
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, rd, view);
			intercept.renderEditBefore(ac);
		}
		view.addObject("code", controllerKey);
		view.addObject("model", JsonKit.toJson(rd));
		return view;
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public ModelAndView view(@PathVariable String id) {
		ModelAndView view = new ModelAndView(renderMap.get(KEY_VIEW));
		Paras rd = Paras.create();
		if (StrKit.isBlank(sourceMap.get(KEY_VIEW))) {
			M model = Blade.create(modelClass).findById(id);
			rd.parseBean(model);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			Map<String, Object> model = this.find(sourceMap.get(KEY_VIEW), map);
			rd.parseMap(model);
		}
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, rd, view);
			intercept.renderViewBefore(ac);
		}
		view.addObject("code", controllerKey);
		view.addObject("model", JsonKit.toJson(rd));
		return view;
	}

	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		M model = autoMapping();
		boolean temp = service.save(ctrl, model, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl, model);
				AjaxResult result = intercept.saveSucceed(ac);
				return result;
			}
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		M model = autoMapping();
		boolean temp = service.update(ctrl, model, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl, model);
				AjaxResult result = intercept.updateSucceed(ac);
				return result;
			}
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove() {
		String ids = getParameter("ids");
		boolean temp = service.deleteByIds(ctrl, ids, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl);
				ac.setId(ids);
				AjaxResult result = intercept.removeSucceed(ac);
				return result;
			}
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}
	
	@ResponseBody
	@RequestMapping(KEY_DEL)
	public AjaxResult del() {
		String ids = getParameter("ids");
		boolean temp = service.delByIds(ctrl, ids, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl);
				ac.setId(ids);
				AjaxResult result = intercept.delSucceed(ac);
				return result;
			}
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}
	
	@ResponseBody
	@RequestMapping(KEY_RESTORE)
	public AjaxResult restore() {
		String ids = getParameter("ids");
		boolean temp = service.restoreByIds(ctrl, ids, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl);
				ac.setId(ids);
				AjaxResult result = intercept.restoreSucceed(ac);
				return result;
			}
			return success(RESTORE_SUCCESS_MSG);
		} else {
			return error(RESTORE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Integer page = getParameterToInt("page", 1);
		Integer rows = getParameterToInt("rows", 10);
		String where = getParameter("where", StrKit.EMPTY);
		String sidx =  getParameter("sidx", StrKit.EMPTY);
		String sord =  getParameter("sord", StrKit.EMPTY);
		String sort =  getParameter("sort", StrKit.EMPTY);
		String order =  getParameter("order", StrKit.EMPTY);
		if (StrKit.notBlank(sidx)) {
			sort = sidx + " " + sord
					+ (StrKit.notBlank(sort) ? ("," + sort) : StrKit.EMPTY);
		}
		if (StrKit.isBlank(sourceMap.get(KEY_INDEX))) {
			throw new RuntimeException(modelClass.getName() + "没有配置数据源！");
		}
		Object grid = service.paginate(page, rows,
				sourceMap.get(KEY_INDEX), where, sort, order,
				intercept, ctrl);
		return grid;
	}


	protected M autoMapping() {
		if (Func.isAllEmpty(paraPrefix, switchMap)) {
			return mapping(modelClass);
		}else if (Func.isEmpty(switchMap) && !Func.isEmpty(paraPrefix)) {
			return mapping(paraPrefix, modelClass);
		} else {
			return null;
		}
	}

	private Map<String, Object> find(String source, Map<String, Object> map) {
		if (source.indexOf("select") == -1) {
			return findOneById(source, map);
		} else {
			return findOneBySql(source, map);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findOneBySql(String sql, Map<String, Object> map) {
		Map<String, Object> model = Db.selectOne(sql, map);
		return Func.caseInsensitiveMap(model);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findOneById(String sqlId, Map<String, Object> map) {
		Map<String, Object> model =  Md.selectOne(sqlId, map, Map.class); //Db.selectOneBySqlId(sqlId, map);
		return Func.caseInsensitiveMap(model);
	}

}
