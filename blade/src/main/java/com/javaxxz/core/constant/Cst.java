
package com.javaxxz.core.constant;

import com.javaxxz.core.intercept.CURDInterceptor;
import com.javaxxz.core.intercept.QueryInterceptor;
import com.javaxxz.core.intercept.SelectInterceptor;
import com.javaxxz.core.interfaces.ICURD;
import com.javaxxz.core.interfaces.ICheck;
import com.javaxxz.core.interfaces.IGrid;
import com.javaxxz.core.interfaces.ILog;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.interfaces.ISelect;
import com.javaxxz.core.interfaces.IShiro;
import com.javaxxz.core.listener.ConfigListener;
import com.javaxxz.core.shiro.DefaultShiroFactroy;
import com.javaxxz.core.toolbox.check.PermissionCheckFactory;
import com.javaxxz.core.toolbox.file.DefaultFileProxyFactory;
import com.javaxxz.core.toolbox.file.IFileProxy;
import com.javaxxz.core.toolbox.grid.JqGridFactory;
import com.javaxxz.core.toolbox.log.BladeLogFactory;

public class Cst {


	private boolean devMode = false;


	private boolean remoteMode = false;


	private boolean luceneIndex = false;


	private String remotePath = "D://blade";


	private String uploadPath = "/upload";


	private String downloadPath = "/download";


	private String realPath = ConfigListener.map.get("realPath");


	private String contextPath = ConfigListener.map.get("contextPath");


	private int passErrorCount = 6;


	private int passErrorHour = 6;
	

	private boolean optimisticLock = true;


	private IGrid defaultGridFactory = new JqGridFactory();


	private ILog defaultLogFactory = new BladeLogFactory();


	private ICheck defaultCheckFactory = new PermissionCheckFactory();


	private IShiro defaultShiroFactory = new DefaultShiroFactroy();
	

	private IFileProxy defaultFileProxyFactory = new DefaultFileProxyFactory();
	

	private ICURD defaultCURDFactory = new CURDInterceptor();
	

	private IQuery defaultPageFactory = new QueryInterceptor();
	

	private IQuery defaultQueryFactory = new QueryInterceptor();
	

	private ISelect defaultSelectFactory = new SelectInterceptor();

	private static final Cst me = new Cst();

	private Cst() {

	}

	public static Cst me() {
		return me;
	}

	public boolean isDevMode() {
		return devMode;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public boolean isRemoteMode() {
		return remoteMode;
	}

	public void setRemoteMode(boolean remoteMode) {
		this.remoteMode = remoteMode;
	}

	public boolean isLuceneIndex() {
		return luceneIndex;
	}

	public void setLuceneIndex(boolean luceneIndex) {
		this.luceneIndex = luceneIndex;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public int getPassErrorCount() {
		return passErrorCount;
	}

	public void setPassErrorCount(int passErrorCount) {
		this.passErrorCount = passErrorCount;
	}

	public int getPassErrorHour() {
		return passErrorHour;
	}

	public void setPassErrorHour(int passErrorHour) {
		this.passErrorHour = passErrorHour;
	}

	public String getUploadRealPath() {
		return (remoteMode ? remotePath : realPath) + uploadPath;
	}

	public String getUploadCtxPath() {
		return contextPath + uploadPath;
	}

	public String getRealPath() {
		return realPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public boolean isOptimisticLock() {
		return optimisticLock;
	}

	public void setOptimisticLock(boolean optimisticLock) {
		this.optimisticLock = optimisticLock;
	}

	public IGrid getDefaultGridFactory() {
		return defaultGridFactory;
	}

	public void setDefaultGridFactory(IGrid defaultGridFactory) {
		this.defaultGridFactory = defaultGridFactory;
	}
	
	public ILog getDefaultLogFactory() {
		return defaultLogFactory;
	}

	public void setDefaultLogFactory(ILog defaultLogFactory) {
		this.defaultLogFactory = defaultLogFactory;
	}

	public ICheck getDefaultCheckFactory() {
		return defaultCheckFactory;
	}

	public void setDefaultCheckFactory(ICheck defaultCheckFactory) {
		this.defaultCheckFactory = defaultCheckFactory;
	}

	public IShiro getDefaultShiroFactory() {
		return defaultShiroFactory;
	}

	public void setDefaultShiroFactory(IShiro defaultShiroFactory) {
		this.defaultShiroFactory = defaultShiroFactory;
	}

	public IFileProxy getDefaultFileProxyFactory() {
		return defaultFileProxyFactory;
	}

	public void setDefaultFileProxyFactory(IFileProxy defaultFileProxyFactory) {
		this.defaultFileProxyFactory = defaultFileProxyFactory;
	}

	public ICURD getDefaultCURDFactory() {
		return defaultCURDFactory;
	}

	public void setDefaultCURDFactory(ICURD defaultCURDFactory) {
		this.defaultCURDFactory = defaultCURDFactory;
	}

	public IQuery getDefaultPageFactory() {
		return defaultPageFactory;
	}

	public void setDefaultPageFactory(IQuery defaultPageFactory) {
		this.defaultPageFactory = defaultPageFactory;
	}

	public IQuery getDefaultQueryFactory() {
		return defaultQueryFactory;
	}

	public void setDefaultQueryFactory(IQuery defaultQueryFactory) {
		this.defaultQueryFactory = defaultQueryFactory;
	}

	public ISelect getDefaultSelectFactory() {
		return defaultSelectFactory;
	}

	public void setDefaultSelectFactory(ISelect defaultSelectFactory) {
		this.defaultSelectFactory = defaultSelectFactory;
	}
	
	
}
