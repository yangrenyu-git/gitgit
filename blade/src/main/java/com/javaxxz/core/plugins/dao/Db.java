
package com.javaxxz.core.plugins.dao;

import java.util.List;
import java.util.Map;

import org.beetl.sql.core.SQLReady;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.support.BladePage;


public class Db {

	private static DbManager dbManager = null;

	public static DbManager init(String name) {
		return DbManager.init(name);
	}

	private Db() {}
	
	private static DbManager getDbManager() {
		if (null == dbManager) {
			synchronized (Db.class) {
				dbManager = DbManager.init();
			}
		}
		return dbManager;
	}
	

	

	public static <T> List<T> execute(SQLReady p, Class<T> clazz){
		return getDbManager().execute(p, clazz);
	}
	

	public static int executeUpdate(SQLReady p){
		return getDbManager().executeUpdate(p);
	}
	

	public static int insert(String sqlTemplate, Object paras){
		return getDbManager().insert(sqlTemplate, paras);
	}
	

	public static int update(String sqlTemplate, Object paras){
		return getDbManager().update(sqlTemplate, paras);
	}
	

	public static int delete(String sqlTemplate, Object paras){
		return getDbManager().delete(sqlTemplate, paras);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map selectOne(String sqlTemplate){
		return getDbManager().selectOne(sqlTemplate);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map selectOne(String sqlTemplate, Object paras){
		return getDbManager().selectOne(sqlTemplate, paras);
	}
	

	@SuppressWarnings("rawtypes")
	public static List<Map> selectList(String sqlTemplate){	
		return getDbManager().selectList(sqlTemplate);
	}
	

	@SuppressWarnings("rawtypes")
	public static List<Map> selectList(String sqlTemplate, Object paras){	
		return getDbManager().selectList(sqlTemplate, paras);
	}
	

	@SuppressWarnings("rawtypes")
	public static List<Map> selectTop(String sqlTemplate, Object paras, Integer topNum){	
		return getDbManager().selectTop(sqlTemplate, paras, topNum);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map findById(String tableName, String pkValue) {
		return getDbManager().findById(tableName, pkValue);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map findById(String tableName, String pk, String pkValue) {
		return getDbManager().findById(tableName, pk, pkValue);
	}
	

	public static Integer queryInt(String sqlTemplate, Object paras){
		return getDbManager().queryInt(sqlTemplate, paras);
	}
	

	public static List<Integer> queryListInt(String sqlTemplate, Object paras){
		return getDbManager().queryListInt(sqlTemplate, paras);
	}
	

	public static String queryStr(String sqlTemplate, Object paras){
		return getDbManager().queryStr(sqlTemplate, paras);
	}
	

	public static List<String> queryListStr(String sqlTemplate, Object paras){
		return getDbManager().queryListStr(sqlTemplate, paras);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map selectOne(String sqlTemplate, Map<String, Object> param, AopContext ac) {
		return getDbManager().selectOne(sqlTemplate, param, ac);
	}
	

	@SuppressWarnings("rawtypes")
	public static List<Map> selectList(String sqlTemplate, Map<String, Object> param, AopContext ac) {
		return getDbManager().selectList(sqlTemplate, param, ac);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map selectOne(String sqlTemplate, Map<String, Object> param, AopContext ac, IQuery intercept) {
		return getDbManager().selectOne(sqlTemplate, param, ac, intercept);
	}
	

	@SuppressWarnings("rawtypes")
	public static List<Map> selectList(String sqlTemplate, Map<String, Object> param, AopContext ac, IQuery intercept) {
		return getDbManager().selectList(sqlTemplate, param, ac, intercept);
	}
	

	@SuppressWarnings("rawtypes")
	public static Map selectOneByCache(String cacheName, String key, String sqlTemplate, Object paras){
		return getDbManager().selectOneByCache(cacheName, key, sqlTemplate, paras);
	}
	

	@SuppressWarnings("rawtypes")
	public static List<Map> selectListByCache(String cacheName, String key, String sqlTemplate, Object paras){
		return getDbManager().selectListByCache(cacheName, key, sqlTemplate, paras);
	} 
	

	

	public static int save(String tableName, String pk, Paras paras) {
		return getDbManager().save(tableName, pk, paras);
	}
	

	public static int update(String tableName, String pk, Paras paras) {
		return getDbManager().update(tableName, pk, paras);
	}
	

	public static int deleteByIds(String table, String col, String ids) {
		return getDbManager().deleteByIds(table, col, ids);
	}
	
	

	public static <T> List<T> getList(Object model, int pageNum, int pageSize) {
		return getDbManager().getList(model, pageNum, pageSize);
	}
	


	public static <T> List<T> getList(String sqlTemplate, Class<T> clazz, Object paras, int pageNum, int pageSize) {
		return getDbManager().getList(sqlTemplate, clazz, paras, pageNum, pageSize);
	}
	

	@SuppressWarnings("rawtypes")
	public static BladePage<Map> paginate(String sqlTemplate, Object paras, int pageNum, int pageSize){
		return getDbManager().paginate(sqlTemplate, paras, pageNum, pageSize);
	}
	

	public static <T> BladePage<T> paginate(String sqlTemplate, Class<T> clazz, Object paras, int pageNum, int pageSize){
		return getDbManager().paginate(sqlTemplate, clazz, paras, pageNum, pageSize);
	}


	public static boolean isExist(String sqlTemplate, Object paras) {
		return getDbManager().isExist(sqlTemplate, paras);
	}
}
