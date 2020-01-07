
package com.javaxxz.core.aop;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.javaxxz.core.base.controller.BladeController;


public class AopContext {


	private BladeController ctrl;


	private ModelAndView view;


	private Object id;
	

	private Object object;


	private String sql;


	private String condition;


	private String where;
	

	private Map<String, Object> param;


	private String tips = "";

	public AopContext() {

	}

	public AopContext(String tips) {
		this.tips = tips;
	}

	public AopContext(BladeController ctrl) {
		this.ctrl = ctrl;
	}

	public AopContext(BladeController ctrl, Object object) {
		this(ctrl);
		this.object = object;
	}

	public AopContext(BladeController ctrl, ModelAndView view) {
		this(ctrl);
		this.view = view;
	}

	public AopContext(BladeController ctrl, Object object, ModelAndView view) {
		this(ctrl, object);
		this.view = view;
	}

	public BladeController getCtrl() {
		return ctrl;
	}

	public void setCtrl(BladeController ctrl) {
		this.ctrl = ctrl;
	}

	public ModelAndView getView() {
		return view;
	}

	public void setView(ModelAndView view) {
		this.view = view;
	}
	
	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

}
