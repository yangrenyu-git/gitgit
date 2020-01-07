
package com.javaxxz.core.plugins.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.plugins.connection.ConnectionPlugin;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.support.BladePage;

@SuppressWarnings({"unchecked","rawtypes"})
public class DbManager {
	private static Map<String, DbManager> pool = new ConcurrentHashMap<String, DbManager>();
	
	private volatile SQLManager sql = null;
	
	public static DbManager init() {
		return init(ConnectionPlugin.init().MASTER);
	}

	public static DbManager init(String name) {
		DbManager db = pool.get(name);
		if (null == db) {
			synchronized (DbManager.class) {
				db = pool.get(name);
				if (null == db) {
					db = new DbManager(name);
					pool.put(name, db);
				}
			}
		}
		return db;
	}
	
	private DbManager(String dbName) {
		this.sql = ConnectionPlugin.init().getPool().get(dbName);
	}

	private DbManager() {}
	
	private SQLManager getSqlManager() {
		if (null == sql) {
			synchronized (DbManager.class) {
				sql = ConnectionPlugin.init().getPool().get(ConnectionPlugin.init().MASTER);
			}
		}
		return sql;
	}
	
	

	

	public <T> List<T> execute(SQLReady p, Class<T> clazz){
		return getSqlManager().execute(p, clazz);
	}
	

	public int executeUpdate(SQLReady p){
		return getSqlManager().executeUpdate(p);
	}
	

	public int insert(String sqlTemplate, Object paras){
		return getSqlManager().executeUpdate(sqlTemplate, paras);
	}
	

	public int update(String sqlTemplate, Object paras){
		return getSqlManager().executeUpdate(sqlTemplate, paras);
	}
	

	public int delete(String sqlTemplate, Object paras){
		return getSqlManager().executeUpdate(sqlTemplate, paras);
	}
	

	public Map selectOne(String sqlTemplate){
		return queryMap(sqlTemplate, Paras.create());
	}
	

	public Map selectOne(String sqlTemplate, Object paras){
		return queryMap(sqlTemplate, paras);
	}
	

	private Map queryMap(String sqlTemplate, Object paras){
		List<Map> list = getSqlManager().execute(sqlTemplate, Map.class, paras, 1, 1);
		if(list.size() == 0){
			return null;
		} else {
			return list.get(0);
		}
	}	
	

	public List<Map> selectList(String sqlTemplate){	
		return queryListMap(sqlTemplate, Paras.create());
	}
	

	public List<Map> selectList(String sqlTemplate, Object paras){	
		return queryListMap(sqlTemplate, paras);
	}
	

	public List<Map> selectTop(String sqlTemplate, Object paras, Integer topNum){	
		return getSqlManager().execute(sqlTemplate, Map.class, paras, topNum, 1);
	}
	

	private List<Map> queryListMap(String sqlTemplate, Object paras){
		List<Map> list = getSqlManager().execute(sqlTemplate, Map.class, paras);
		return list;
	}
	

	public Map findById(String tableName, String pkValue) {
		return selectOneBy(tableName, "id = #{id}", Paras.create().set("id", pkValue));
	}
	

	public Map findById(String tableName, String pk, String pkValue) {
		return selectOneBy(tableName, pk + " = #{id}", Paras.create().set("id", pkValue));
	}
	

	private Map selectOneBy(String tableName, String where, Object paras){
		String sqlTemplate = Func.format("select * from {} where {} ", tableName, where);
		return selectOne(sqlTemplate, paras);
	}
	

	public Integer queryInt(String sqlTemplate, Object paras){
		List<Integer> list = getSqlManager().execute(sqlTemplate, Integer.class, paras, 1, 1);
		if(list.size() == 0){
			return 0;
		} else {
			return list.get(0);
		}
	}
	

	public List<Integer> queryListInt(String sqlTemplate, Object paras){
		List<Integer> list = getSqlManager().execute(sqlTemplate, Integer.class, paras);
		return list;
	}
	

	public String queryStr(String sqlTemplate, Object paras){
		List<String> list = getSqlManager().execute(sqlTemplate, String.class, paras, 1, 1);
		if(list.size() == 0){
			return "";
		} else {
			return list.get(0);
		}
	}
	

	public List<String> queryListStr(String sqlTemplate, Object paras){
		List<String> list = getSqlManager().execute(sqlTemplate, String.class, paras);
		return list;
	}
	

	public Map selectOne(String sqlTemplate, Map<String, Object> param, AopContext ac) {
		return selectOne(sqlTemplate, param, ac, Cst.me().getDefaultQueryFactory());
	}
	

	public List<Map> selectList(String sqlTemplate, Map<String, Object> param, AopContext ac) {
		return selectList(sqlTemplate, param, ac, Cst.me().getDefaultQueryFactory());
	}
	

	public Map selectOne(String sqlTemplate, Map<String, Object> param, AopContext ac, IQuery intercept) {
		ac.setSql(sqlTemplate);
		ac.setCondition("");
		ac.setParam(param);
		if (null != intercept) {
			intercept.queryBefore(ac);
			sqlTemplate = (StrKit.notBlank(ac.getWhere())) ? ac.getWhere() : (sqlTemplate + " " + ac.getCondition());
		}
		Map rst = selectOne(sqlTemplate, param);
		if (null != intercept) {
			ac.setObject(rst);
			intercept.queryAfter(ac);
		}
		return rst;
	}
	

	public List<Map> selectList(String sqlTemplate, Map<String, Object> param, AopContext ac, IQuery intercept) {
		ac.setSql(sqlTemplate);
		ac.setCondition("");
		ac.setParam(param);
		if (null != intercept) {
			intercept.queryBefore(ac);
			sqlTemplate = (StrKit.notBlank(ac.getWhere())) ? ac.getWhere() : (sqlTemplate + " " + ac.getCondition());
		}
		List<Map> rst = selectList(sqlTemplate, param);
		if (null != intercept) {
			ac.setObject(rst);
			intercept.queryAfter(ac);
		}
		return rst;
	}
	

	public Map selectOneByCache(String cacheName, String key, String sqlTemplate, Object paras){
		final String _sqlTemplate = sqlTemplate;
		final Object _paras = paras;
		return CacheKit.get(cacheName, key, new ILoader() {
			public Object load() {
				return selectOne(_sqlTemplate, _paras);
			}
		});
	} 
	

	public List<Map> selectListByCache(String cacheName, String key, String sqlTemplate, Object paras){
		final String _sqlTemplate = sqlTemplate;
		final Object _paras = paras;
		return CacheKit.get(cacheName, key, new ILoader() {
			public Object load() {
				return selectList(_sqlTemplate, _paras);
			}
		});
	} 
	

	

	public int save(String tableName, String pk, Paras paras) {
		if(Func.isOneEmpty(tableName, pk)){
			throw new RuntimeException("表名或主键不能为空!");
		}
		String mainSql = " insert into {} ({}) values ({})";
		pk = (String) Func.getValue(pk, "ID");
		if(Func.isOracle()){
			String pkValue = paras.getStr(pk);
			if(pkValue.indexOf(".nextval") > 0){
				Map<String, Object> map = selectOne("select " + pkValue + " as PK from dual");
				Object val = map.get("PK");
				paras.set(pk, val);
			}
		}
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for(String key : paras.keySet()){
			fields.append(key + ",");
			values.append("#{" + key + "},");
		}
		String sqlTemplate = Func.format(mainSql, tableName, StrKit.removeSuffix(fields.toString(), ","), StrKit.removeSuffix(values.toString(), ","));
		int cnt = insert(sqlTemplate, paras);
		if(cnt > 0 && Func.isMySql()){
			Object pkValue = paras.get(pk);
			if(Func.isEmpty(pkValue)){
				Map<String, Object> map = selectOne(" select LAST_INSERT_ID() as PK ");
				Object val = map.get("PK");
				paras.set(pk, val);
			}
		}
		return cnt;
	}
	

	public int update(String tableName, String pk, Paras paras) {
		if(Func.isOneEmpty(tableName, pk)){
			throw new RuntimeException("表名或主键不能为空!");
		}
		pk = (String) Func.getValue(pk, "ID");
		String mainSql = " update {} set {} where {} = #{" + pk + "}";
		StringBuilder fields = new StringBuilder();
		for(String key : paras.keySet()){
			if(!key.equals(pk)){
				fields.append(key + " = #{" + key + "},");
			}
		}
		String sqlTemplate = Func.format(mainSql, tableName, StrKit.removeSuffix(fields.toString(), ","), pk);
		return update(sqlTemplate, paras);
	}
	

	public int deleteByIds(String table, String col, String ids) {
		String sqlTemplate = " DELETE FROM " + table + " WHERE " + col + " IN (#{join(ids)}) ";
		Paras paras = Paras.create().set("ids", ids.split(","));
		int result = getSqlManager().executeUpdate(sqlTemplate, paras);
		return result;
	}
	
	

	public <T> List<T> getList(Object model, int pageNum, int pageSize) {
		List<T> all = (List<T>) getSqlManager().template(model, (pageNum - 1) * pageSize + 1, pageSize);
		return all;
	}
	


	public <T> List<T> getList(String sqlTemplate, Class<T> clazz, Object paras, int pageNum, int pageSize) {
		List<T> all = (List<T>) getSqlManager().execute(sqlTemplate, clazz, paras, (pageNum - 1) * pageSize + 1, pageSize);
		return all;
	}
	

	public BladePage<Map> paginate(String sqlTemplate, Object paras, int pageNum, int pageSize){
		return paginate(sqlTemplate, Map.class, paras, pageNum, pageSize);
	}
	

	public <T> BladePage<T> paginate(String sqlTemplate, Class<T> clazz, Object paras, int pageNum, int pageSize){
		List<T> rows = getList(sqlTemplate, clazz, paras, pageNum, pageSize);
		long count = queryInt(" SELECT COUNT(*) CNT FROM (" + sqlTemplate + ") a", paras).longValue();
		BladePage<T> page = new BladePage<>(rows, pageNum, pageSize, count);
		return page;
	}
	

	public boolean isExist(String sqlTemplate, Object paras) {
		int count = getSqlManager().execute(sqlTemplate, Map.class, paras).size();
		if (count != 0) {
			return true;
		}
		return false;
	}
}
