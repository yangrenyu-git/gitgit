
package com.javaxxz.core.base.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.javaxxz.core.constant.Const;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.constant.ConstCacheKey;
import com.javaxxz.core.constant.ConstCurd;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.exception.NoPermissionException;
import com.javaxxz.core.exception.NoUserException;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.file.BladeFile;
import com.javaxxz.core.toolbox.grid.GridManager;
import com.javaxxz.core.toolbox.kit.CharsetKit;
import com.javaxxz.core.toolbox.kit.LogKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.kit.URLKit;
import com.javaxxz.core.toolbox.log.BladeLogManager;
import com.javaxxz.core.toolbox.support.BeanInjector;
import com.javaxxz.core.toolbox.support.Convert;
import com.javaxxz.core.toolbox.support.WafRequestWrapper;


public class BladeController implements ConstCurd, ConstCache, ConstCacheKey {
	
	private static final Logger log = LoggerFactory.getLogger(BladeController.class);
	


	@Resource
	private HttpServletRequest request;
	
	protected HttpServletRequest getRequest() {
		return new WafRequestWrapper(this.request);
	}
	
	public boolean isAjax(){
		String header = getRequest().getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
		return isAjax;
	}

	public String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	public String getParameter(String name, String defaultValue) {
		String result = getRequest().getParameter(name);
		return StrKit.notBlank(result) ? result : defaultValue;
	}

	public Integer getParameterToInt(String name) {
		return Convert.toInt(getRequest().getParameter(name));
	}

	public Integer getParameterToInt(String name, Integer defaultValue) {
		return Convert.toInt(getRequest().getParameter(name), defaultValue);
	}

	public Long getParameterToLong(String name) {
		return Convert.toLong(getRequest().getParameter(name));
	}

	public Long getParameterToLong(String name, Long defaultValue) {
		return Convert.toLong(getRequest().getParameter(name), defaultValue);
	}

	public String getParameterToEncode(String para) {
		return URLKit.encode(getRequest().getParameter(para), CharsetKit.UTF_8);
	}

	public String getParameterToDecode(String para) {
		return URLKit.decode(getRequest().getParameter(para), CharsetKit.UTF_8);
	}

	public String getContextPath() {
		return getRequest().getContextPath();
	}
	

	

	public <T> T mapping(Class<T> beanClass) {
		return (T) BeanInjector.inject(beanClass, getRequest());
	}


	public <T> T mapping(String paraPrefix, Class<T> beanClass) {
		return (T) BeanInjector.inject(beanClass, paraPrefix, getRequest());
	}


	public Paras getParas() {
		return BeanInjector.injectMaps(getRequest());
	}


	public Paras getParas(String paraPrefix) {
		return BeanInjector.injectMaps(paraPrefix, getRequest());
	}
	

	

	public BladeFile getFile(MultipartFile file){
		return getFile(file, "image", null, null);
	}
	

	public BladeFile getFile(MultipartFile file, String dir){
		return getFile(file, dir, null, null);
	}
	

	public BladeFile getFile(MultipartFile file, String dir, String path, String virtualPath){
		return new BladeFile(file, dir, path, virtualPath);
	}
	

	public List<BladeFile> getFiles(List<MultipartFile> files){
		return getFiles(files, "image", null, null);
	}
	

	public List<BladeFile> getFiles(List<MultipartFile> files, String dir){
		return getFiles(files, dir, null, null);
	}
	

	public List<BladeFile> getFiles(List<MultipartFile> files, String dir, String path, String virtualPath){
		List<BladeFile> list = new ArrayList<>();
		for (MultipartFile file : files){
			list.add(new BladeFile(file, dir, path, virtualPath));
		}
		return list;
	}



	

	public AjaxResult json(Object data) {
		return new AjaxResult().success(data);
	}
	

	public AjaxResult json(Object data, String message) {
		return json(data).setMessage(message);
	}
	

	public AjaxResult json(Object data, String message, int code) {
		return json(data, message).setCode(code);
	}
	

	public AjaxResult success(String message) {
		return new AjaxResult().addSuccess(message);
	}
	

	public AjaxResult error(String message) {
		return new AjaxResult().addError(message);
	}
	

	public AjaxResult warn(String message) {
		return new AjaxResult().addWarn(message);
	}
	

	public AjaxResult fail(String message) {
		return new AjaxResult().addFail(message);
	}
	
	

	
	private Object basepage(String slaveName, String source, IQuery intercept){
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
		Object grid = GridManager.paginate(slaveName, page, rows, source, where, sort, order, intercept, this);
		return grid;
	}
	

	protected Object paginate(String source){
		return basepage(null, source, Cst.me().getDefaultPageFactory());
	}
	

	protected Object paginate(String source, IQuery intercept){
		return basepage(null, source, intercept);
	}
	

	protected Object paginate(String slaveName, String source){
		return basepage(slaveName, source, Cst.me().getDefaultPageFactory());
	}
	

	protected Object paginate(String slaveName, String source, IQuery intercept){
		return basepage(slaveName, source, intercept);
	}
	
	


	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Object exceptionHandler(Exception ex, HttpServletResponse response, HttpServletRequest request) throws IOException {
		AjaxResult result = new AjaxResult();
		String url = Const.ERROR_500;
		String msg = ex.getMessage();
		Object resultModel = null;
		try {
			if (ex.getClass() == HttpRequestMethodNotSupportedException.class) {
				url = Const.ERROR_500;// 请求方式不允许抛出的异常,后面可自定义页面
			} else if (ex.getClass() == NoPermissionException.class) {
				url = Const.NOPERMISSION_PATH;// 无权限抛出的异常
				msg = ConstShiro.NO_PERMISSION;
			} else if (ex.getClass() == NoUserException.class) {
				url = Const.LOGIN_REALPATH;// session过期抛出的异常
				msg = ConstShiro.NO_USER;
			}
			if (isAjax()) {
				result.addFail(msg);
				resultModel = result;
			} else {
				ModelAndView view = new ModelAndView(url);
				view.addObject("error", msg);
				view.addObject("class", ex.getClass());
				view.addObject("method", request.getRequestURI());
				resultModel = view;
			}
			try {
				if(StrKit.notBlank(msg)){
					BladeLogManager.doLog("异常日志", msg, false);
				}
			} catch (Exception logex) {
				LogKit.logNothing(logex);
			}
			return resultModel;
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			return resultModel;
		} finally {
			log.error(msg, ex);
		}
	}

}
