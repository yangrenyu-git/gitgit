
package com.javaxxz.core.plugins.dao;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.annotatoin.Table;
import org.beetl.sql.core.db.ClassDesc;
import org.beetl.sql.core.db.KeyHolder;

import com.javaxxz.core.annotation.BindID;
import com.javaxxz.core.annotation.DbName;
import com.javaxxz.core.constant.Const;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.plugins.connection.ConnectionPlugin;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.support.BladePage;


@SuppressWarnings({"unchecked","rawtypes"})
public class Blade {
	private static Map<Class<?>, Blade> pool = new ConcurrentHashMap<Class<?>, Blade>();
	private volatile SQLManager sql = null;
	private Class<?> modelClass;
	private String table;
	private String pk;
	private String dbName;

	private SQLManager getSqlManager() {
		if (null == sql) {
			synchronized (Blade.class) {
				if (null == sql) {
					DbName dbName = modelClass.getAnnotation(DbName.class);
					if (null == dbName){
						sql = dao();
						this.dbName = ConnectionPlugin.init().MASTER;
					} else {
						sql = dao(dbName.name());
						this.dbName = dbName.name();
					}
				}
			}
		}
		return sql;
	}
	

	public static SQLManager dao() {
		return dao(ConnectionPlugin.init().MASTER);
	}
	

	public static SQLManager dao(String name) {
		return ConnectionPlugin.init().getPool().get(name);
	}


	public static Blade create(Class<?> modelClass) {
		Blade blade = pool.get(modelClass);
		if (null == blade) {
			synchronized (Blade.class) {
				blade = pool.get(modelClass);
				if (null == blade) {
					DbName dbName = modelClass.getAnnotation(DbName.class);
					if (null == dbName){
						blade = new Blade(ConnectionPlugin.init().MASTER, modelClass);
					} else {
						blade = new Blade(dbName.name(), modelClass);
					}
					pool.put(modelClass, blade);
				}
			}
		}
		return blade;
	}

	private Blade() {
		
	}
	
	private Blade(String sourceName, Class<?> modelClass) {
		if(modelClass != Blade.class){
			setTable(modelClass);			
		}
		setSource(sourceName);
	}

	private void setTable(Class<?> modelClass) {
		this.modelClass = modelClass;
		Table Table = this.modelClass.getAnnotation(Table.class);
		if (null != Table) {
			this.table = Table.name();
		} else {
			throw new RuntimeException("未给 " + this.modelClass.getName() + " 绑定表名!");
		}
		BindID BindID = this.modelClass.getAnnotation(BindID.class);
		if (null != BindID) {
			this.pk = BindID.name();
		} else {
			throw new RuntimeException("未给 " + this.modelClass.getName() + " 绑定主键! ");
		}
	}
	
	private void setSource(String name) {
		if (null == sql) {
			synchronized (Blade.class) {
				if (null == sql) {
					sql = dao(name);
				}
			}
		}
		dbName = name;
	}
	

	public Map findOneColBy(String columns){
		List<Map> list = getSqlManager().execute(getSelectSql(columns) + getFromSql(), Map.class, Paras.create(), 1, 1);
		if(list.size() == 0){
			return null;
		} else {
			return list.get(0);
		}
	}
	

	public Map findOneColBy(String columns, String where, Object paras){
		List<Map> list = getSqlManager().execute(getSelectSql(columns) + getFromSql() + getWhere(where), Map.class, paras, 1, 1);
		if (list.size() == 0){
			return null;
		} else {
			return list.get(0);
		}
	}
	

	public List<Map> findColBy(String columns){
		List<Map> list = getSqlManager().execute(getSelectSql(columns) + getFromSql(), Map.class, Paras.create());
		return list;
	}
	

	public List<Map> findColBy(String columns, String where, Object paras){
		List<Map> list = getSqlManager().execute(getSelectSql(columns) + getFromSql() + getWhere(where), Map.class, paras);
		return list;
	}


	public <T> T findById(Object id) {
		try{
			return (T) getSqlManager().unique(this.modelClass, id);
		} catch (Exception ex){
			return null;
		}
	}


	public <T> List<T> find(String sqlTemplate, Object paras) {
		List<T> list = (List<T>) getSqlManager().execute(sqlTemplate, this.modelClass, paras);
		return list;
	}


	public <T> T findTopOne(Object model) {
		List<T> list = (List<T>) getSqlManager().template(model, 1, 1);
		if(list.size() == 0){
			return null;
		}
		return list.get(0);
	}


	public <T> List<T> findTop(int topNum, Object model) {
		List<T> list = (List<T>) getSqlManager().template(model, 1, topNum);
		return list;
	}
	

	public <T> List<T> findTop(int topNum, String sqlTemplate) {
		List<T> list = (List<T>) getSqlManager().execute(sqlTemplate, this.modelClass, Paras.create(), 1, topNum);
		return list;
	}
	

	public <T> List<T> findTop(int topNum, String sqlTemplate, Object paras) {
		List<T> list = (List<T>) getSqlManager().execute(sqlTemplate, this.modelClass, paras, 1, topNum);
		return list;
	}


	public <T> List<T> findAll() {
		List<T> all = (List<T>) getSqlManager().all(this.modelClass);
		return all;
	}


	public <T> List<T> findBy(String where, Object paras) {
		List<T> models = (List<T>) getSqlManager().execute(getSelectSql() + getFromSql() + getWhere(where), this.modelClass, paras);
		return models;
	}


	public <T> List<T> findBy(String columns, String where, Object paras) {
		List<T> models = (List<T>) getSqlManager().execute(getSelectSql(columns) + getFromSql() + getWhere(where), this.modelClass, paras);
		return models;
	}


	public <T> List<T> findByTemplate(Object model) {
		return (List<T>) getSqlManager().template(model);
	}


	public <T> T findFirst(String sqlTemplate, Object paras) {
		List<T> list = this.findTop(1, sqlTemplate, paras);
		if (list.size() == 0){
			return null;
		} else {
			return list.get(0);
		}
	}


	public <T> T findFirstBy(String where, Object paras) {
		List<T> list = this.findTop(1, getSelectSql() + getFromSql() + getWhere(where), paras);
		if (list.size() == 0){
			return null;
		} else {
			return list.get(0);
		}
	}


	public boolean save(Object model) {
		return getSqlManager().insert(this.modelClass, model, false) > 0;
	}


	public int saveRtId(Object model) {
		KeyHolder key = new KeyHolder();
		int n = getSqlManager().insert(this.modelClass, model, key);
		if (n > 0) {
			return Integer.parseInt(key.getKey().toString());
		} else {
			return 0;
		}
	}


	public String saveRtStrId(Object model) {
		KeyHolder key = new KeyHolder();
		int n = getSqlManager().insert(this.modelClass, model, key);
		if (n > 0) {
			return key.getKey().toString();
		} else {
			return "";
		}
	}
	

	public boolean saveAndSetKey(Object model){
		return getSqlManager().insert(this.modelClass, model, true) > 0;
	}


	public boolean update(Object model) {
		return baseUpdate(model, false);
	}


	public boolean updateEveryCol(Object model) {
		return baseUpdate(model, true);
	}
	

	private boolean baseUpdate(Object model, boolean flag) {
		Object idValue = this.getIdValue(model);
		
		if(Func.isEmpty(idValue)){
			throw new RuntimeException("未取到ID的值,无法修改!");
		}
		
		if(Cst.me().isOptimisticLock()){
			// 1.数据是否还存在
			String sqlExist = new StringBuffer("select * from ").append(table).append(" where ").append(pk).append(" = #{idValue} ").toString();
			Map modelOld = Db.init(dbName).selectOne(sqlExist, Paras.create().set("idValue", idValue));
			// 2.数据已经被删除
			if (null == modelOld) { 
				throw new RuntimeException("数据库中此数据不存在，可能数据已经被删除，请刷新数据后再操作");
			}
			// 3.乐观锁控制
			Paras modelForm = Paras.parse(model);
			if (modelForm.get(Const.OPTIMISTIC_LOCK.toLowerCase()) != null) { // 是否需要乐观锁控制
				int versionDB = Func.toInt(modelOld.get(Const.OPTIMISTIC_LOCK.toLowerCase()), 0); // 数据库中的版本号
				int versionForm = Func.toInt(modelForm.get(Const.OPTIMISTIC_LOCK.toLowerCase()), 1); // 表单中的版本号
				if (!(versionForm > versionDB)) {
					throw new RuntimeException("表单数据版本号和数据库数据版本号不一致，可能数据已经被其他人修改，请重新编辑");
				}
			}
		}
		if (flag) {
			return sql.updateById(model) > 0;
		} else {
			return sql.updateTemplateById(model) > 0;
		}
	}


	public int updateAllRecords(Object model) {
		return getSqlManager().updateAll(this.modelClass, model);
	}


	public boolean updateBy(String set, String where, Object paras) {
		int n = getSqlManager().executeUpdate(getUpdateSql() + getSet(set) + getWhere(where), paras);
		return n > 0;
	}


	public int[] updateBathById(List<?> list) {
		int[] n = getSqlManager().updateByIdBatch(list);
		return n;
	}


	public int delete(Object id) {
		int cnt = getSqlManager().deleteById(this.modelClass, id);
		return cnt;
	}


	public int deleteBy(String sqlTemplate) {
		int result = getSqlManager().executeUpdate(sqlTemplate, null);
		return result;
	}


	public int deleteBy(String where, Object paras) {
		int result = getSqlManager().executeUpdate(getDeleteSql(where), paras);
		return result;
	}


	public int deleteByIds(String ids) {
		String sqlTemplate = getDeleteSql(this.table, this.pk);
		Paras paras = Paras.create().set("ids", ids.split(","));
		int result = getSqlManager().executeUpdate(sqlTemplate, paras);
		return result;
	}


	public int deleteByCols(String col, String ids) {
		String sqlTemplate = getDeleteSql(this.table, col);
		Paras paras = Paras.create().set("ids", ids.split(","));
		int result = getSqlManager().executeUpdate(sqlTemplate, paras);
		return result;
	}


	public int deleteTableByCols(String table, String col, String ids) {
		String sqlTemplate = getDeleteSql(table, col);
		Paras paras = Paras.create().set("ids", ids.split(","));
		int result = getSqlManager().executeUpdate(sqlTemplate, paras);
		return result;
	}


	public long total() {
		long n = getSqlManager().allCount(this.modelClass);
		return n;
	}


	public long count(Object model) {
		long total = getSqlManager().templateCount(model);
		return total;
	}


	public int countBy(String sqlTemplate, Object paras) {
		int n = getSqlManager().execute(sqlTemplate, this.modelClass, paras).size();
		return n;
	}


	public int count(String where, Object paras) {
		int n = getSqlManager().execute(getCountSql() + getWhere(where), this.modelClass, paras).size();
		return n;
	}


	public <T> List<T> getList(int start, int size) {
		List<T> all = (List<T>) getSqlManager().all(this.modelClass, (start - 1) * size + 1, size);
		return all;
	}


	public <T> List<T> getList(Object model, int start, int size) {
		List<T> all = (List<T>) getSqlManager().template(model, (start - 1) * size + 1, size);
		return all;
	}
	


	public <T> List<T> getList(String sqlTemplate, Object paras, int start, int size) {
		List<T> all = (List<T>) getSqlManager().execute(sqlTemplate, this.modelClass, paras, (start - 1) * size + 1, size);
		return all;
	}
	


	public <T> List<T> getList(String sqlTemplate, Class<?> clazz, Object paras, int start, int size) {
		List<T> all = (List<T>) getSqlManager().execute(sqlTemplate, clazz, paras, (start - 1) * size + 1, size);
		return all;
	}
	

	public <T> BladePage<T> paginate(String sqlTemplate, Object paras, int start, int size){
		List<T> rows = getList(sqlTemplate, paras, start, size);
		long count = Db.init(dbName).queryInt(getCountSql(sqlTemplate), paras).longValue();
		BladePage<T> page = new BladePage<>(rows, start, size, count);
		return page;
	}
	

	public <T> BladePage<T> paginate(String sqlTemplate, Class<?> clazz, Object paras, int start, int size){
		List<T> rows = getList(sqlTemplate, clazz, paras, start, size);
		long count = Db.init(dbName).queryInt(getCountSql(sqlTemplate), paras).longValue();
		BladePage<T> page = new BladePage<>(rows, start, size, count);
		return page;
	}
	


	public boolean isExist(String sqlTemplate, Object paras) {
		int count = getSqlManager().execute(sqlTemplate, this.modelClass, paras).size();
		if (count != 0) {
			return true;
		}
		return false;
	}
	

	public Object getIdValue(Object model){
		SQLManager sql = getSqlManager();
		String table = sql.getNc().getTableName(this.modelClass);
		ClassDesc desc = sql.getMetaDataManager().getTable(table).getClassDesc(this.modelClass, sql.getNc());
		Method getterMethod = (Method) desc.getIdMethods().get(desc.getIdCols().get(0));
		Object idValue = null;
		try {
			idValue = getterMethod.invoke(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idValue;
	}
	


	private String getSet(String set) {
		if (set != null && !set.isEmpty() && !set.trim().toUpperCase().startsWith("SET")) {
			set = " SET " + set + " ";
		}
		return set;
	}

	private String getWhere(String where) {
		if (where != null && !where.isEmpty() && !where.trim().toUpperCase().startsWith("WHERE")) {
			where = " WHERE " + where + " ";
		}
		return where;
	}

	private String getSelectSql() {
		return " SELECT * ";
	}

	private String getSelectSql(String columns) {
		return " SELECT " + columns + " ";
	}

	private String getFromSql() {
		return " FROM " + this.table + " ";
	}

	private String getUpdateSql() {
		return " UPDATE " + this.table + " ";
	}

	private String getDeleteSql(String where) {
		return " DELETE FROM " + this.table + " WHERE " + where + " ";
	}

	private String getDeleteSql(String table, String col) {
		return " DELETE FROM " + table + " WHERE " + col + " IN (#{join(ids)}) ";
	}

	private String getCountSql() {
		return " SELECT " + this.pk + " FROM " + this.table + " ";
	}
	
	private String getCountSql(String sqlTemplate) {
		return " SELECT COUNT(*) CNT FROM (" + sqlTemplate + ") a";
	}
}

