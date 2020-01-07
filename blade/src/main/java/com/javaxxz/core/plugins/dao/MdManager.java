
package com.javaxxz.core.plugins.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLResult;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;

import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.plugins.connection.ConnectionPlugin;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.support.BladePage;

public class MdManager {
	private static Map<String, MdManager> pool = new ConcurrentHashMap<String, MdManager>();
	
	private volatile SQLManager sql = null;
	
	public static MdManager init() {
		return init(ConnectionPlugin.init().MASTER);
	}

	public static MdManager init(String name) {
		MdManager db = pool.get(name);
		if (null == db) {
			synchronized (MdManager.class) {
				db = pool.get(name);
				if (null == db) {
					db = new MdManager(name);
					pool.put(name, db);
				}
			}
		}
		return db;
	}
	
	private MdManager(String dbName) {
		this.sql = ConnectionPlugin.init().getPool().get(dbName);
	}

	private MdManager() {}
	
	private SQLManager getSqlManager() {
		if (null == sql) {
			synchronized (MdManager.class) {
				sql = ConnectionPlugin.init().getPool().get(ConnectionPlugin.init().MASTER);
			}
		}
		return sql;
	}
	
	

	

	public <T> T getMapper(Class<T> mapperInterface){
		return getSqlManager().getMapper(mapperInterface);
	}
	
	

	

	public <T> T selectOne(String sqlId, Object paras, Class<T> clazz){
		return getSqlManager().selectSingle(sqlId, paras, clazz);
	}
	

	public <T> T selectUnique(String sqlId, Object paras, Class<T> clazz){
		return getSqlManager().selectUnique(sqlId, paras, clazz);
	}
	

	public <T> List<T> selectList(String sqlId, Object paras, Class<T> clazz){
		return getSqlManager().select(sqlId, clazz, paras);
	}
	

	public String queryStr(String sqlId, Object paras){
		return selectOne(sqlId, paras, String.class);
	}


	public Integer queryInt(String sqlId, Object paras){
		return selectOne(sqlId, paras, Integer.class);
	}
	

	public Long queryLong(String sqlId, Object paras){
		return selectOne(sqlId, paras, Long.class);
	}
	

	public BigDecimal queryDecimal(String sqlId, Object paras){
		return selectOne(sqlId, paras, BigDecimal.class);
	}
	

	public <T> T selectOneByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		final String _sqlId = sqlId;
		final Object _paras = paras;
		final Class<T> _clazz = clazz;
		return CacheKit.get(cacheName, key, new ILoader() {
			@Override
			public Object load() {
				return getSqlManager().selectSingle(_sqlId, _paras, _clazz);
			}
		});
	}
	

	public <T> T selectUniqueByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		final String _sqlId = sqlId;
		final Object _paras = paras;
		final Class<T> _clazz = clazz;
		return CacheKit.get(cacheName, key, new ILoader() {
			@Override
			public Object load() {
				return getSqlManager().selectUnique(_sqlId, _paras, _clazz);
			}
		});
	}
	

	public <T> List<T> selectListByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		final String _sqlId = sqlId;
		final Object _paras = paras;
		final Class<T> _clazz = clazz;
		return CacheKit.get(cacheName, key, new ILoader() {
			@Override
			public Object load() {
				return getSqlManager().select(_sqlId, _clazz, _paras);
			}
		});
	}
	

	public <T> BladePage<T> paginate(String sqlId, Class<T> clazz, Object paras, int pageNum, int pageSize){
		return paginate(sqlId, clazz, paras, pageNum, pageSize, null);
	}
	
	

	@SuppressWarnings("unchecked")
	public <T> BladePage<T> paginate(String sqlId, Class<T> clazz, Object paras, int pageNum, int pageSize, String orderBy){
		PageQuery query = new PageQuery();
		query.setPageNumber(pageNum);
		query.setPageSize(pageSize);
		if(StrKit.notBlank(orderBy)){
			query.setOrderBy(orderBy);
		}
		getSqlManager().pageQuery(sqlId, clazz, query);
		BladePage<T> page = new BladePage<>(query.getList(), pageNum, pageSize, query.getTotalRow());
		return page;
	}
	

	public int insert(String sqlId, Object paras){
		return getSqlManager().insert(sqlId, paras, null, null);
	}
	

	public KeyHolder insert(String sqlId, Object paras, String keyName) {
		if (StrKit.isBlank(keyName))
			return null;
		KeyHolder holder = new KeyHolder();
		getSqlManager().insert(sqlId, paras, holder, keyName);
		return (getSqlManager().insert(sqlId, paras, holder, keyName) > 0) ? holder : null;
	}
	

	public int insert(String sqlId, Object paras, KeyHolder holder, String keyName) {
		return getSqlManager().insert(sqlId, paras, holder, keyName);
	}
	

	public int update(String sqlId, Object paras){
		return getSqlManager().update(sqlId, paras);
	}
	

	public int[] updateBatch(String sqlId, List<?> list){
		return getSqlManager().updateBatch(sqlId, list);
	}
	

	public int delete(String sqlId, Object paras){
		return getSqlManager().update(sqlId, paras);
	}
	

	public String getSql(String sqlId) {
		return getSqlManager().getScript(sqlId).getSql();
	}
	

	public SQLResult getSQLResult(String sqlId, Object paras){
		return getSqlManager().getSQLResult(sqlId, paras);
	}
}
