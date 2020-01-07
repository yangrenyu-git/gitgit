
package com.javaxxz.core.plugins.dao;

import java.math.BigDecimal;
import java.util.List;

import org.beetl.sql.core.SQLResult;
import org.beetl.sql.core.db.KeyHolder;

import com.javaxxz.core.toolbox.support.BladePage;


public class Md {

	private static MdManager mdManager = null;

	public static MdManager init(String name) {
		return MdManager.init(name);
	}

	private Md() {}
	
	private static MdManager getMdManager() {
		if (null == mdManager) {
			synchronized (Md.class) {
				mdManager = MdManager.init();
			}
		}
		return mdManager;
	}
	
	

	

	public static <T> T getMapper(Class<T> mapperInterface){
		return getMdManager().getMapper(mapperInterface);
	}
	
	

	

	public static <T> T selectOne(String sqlId, Object paras, Class<T> clazz){
		return getMdManager().selectOne(sqlId, paras, clazz);
	}
	

	public static <T> T selectUnique(String sqlId, Object paras, Class<T> clazz){
		return getMdManager().selectUnique(sqlId, paras, clazz);
	}
	

	public static <T> List<T> selectList(String sqlId, Object paras, Class<T> clazz){
		return getMdManager().selectList(sqlId, paras, clazz);
	}
	

	public static String queryStr(String sqlId, Object paras){
		return getMdManager().queryStr(sqlId, paras);
	}


	public static Integer queryInt(String sqlId, Object paras){
		return getMdManager().queryInt(sqlId, paras);
	}
	

	public static Long queryLong(String sqlId, Object paras){
		return getMdManager().queryLong(sqlId, paras);
	}
	

	public static BigDecimal queryDecimal(String sqlId, Object paras){
		return getMdManager().queryDecimal(sqlId, paras);
	}
	

	public static <T> T selectOneByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		return getMdManager().selectOneByCache(cacheName, key, sqlId, paras, clazz);
	}
	

	public static <T> T selectUniqueByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		return getMdManager().selectUniqueByCache(cacheName, key, sqlId, paras, clazz);
	}
	

	public static <T> List<T> selectListByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		return getMdManager().selectListByCache(cacheName, key, sqlId, paras, clazz);
	}
	

	public static <T> BladePage<T> paginate(String sqlId, Class<T> clazz, Object paras, int pageNum, int pageSize){
		return getMdManager().paginate(sqlId, clazz, paras, pageNum, pageSize);
	}
	
	

	public static <T> BladePage<T> paginate(String sqlId, Class<T> clazz, Object paras, int pageNum, int pageSize, String orderBy){
		return getMdManager().paginate(sqlId, clazz, paras, pageNum, pageSize, orderBy);
	}
	

	public static int insert(String sqlId, Object paras){
		return getMdManager().insert(sqlId, paras);
	}
	

	public static KeyHolder insert(String sqlId, Object paras, String keyName) {
		return getMdManager().insert(sqlId, paras, keyName);
	}
	

	public static int insert(String sqlId, Object paras, KeyHolder holder, String keyName) {
		return getMdManager().insert(sqlId, paras, holder, keyName);
	}
	

	public static int update(String sqlId, Object paras){
		return getMdManager().update(sqlId, paras);
	}
	

	public static int[] updateBatch(String sqlId, List<?> list){
		return getMdManager().updateBatch(sqlId, list);
	}
	

	public static int delete(String sqlId, Object paras){
		return getMdManager().delete(sqlId, paras);
	}
	

	public static String getSql(String sqlId) {
		return getMdManager().getSql(sqlId);
	}
	

	public static SQLResult getSQLResult(String sqlId, Object paras){
		return getMdManager().getSQLResult(sqlId, paras);
	}
	

}
