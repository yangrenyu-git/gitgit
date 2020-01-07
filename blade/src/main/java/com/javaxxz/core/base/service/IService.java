
package com.javaxxz.core.base.service;

import java.util.List;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.interfaces.ICURD;
import com.javaxxz.core.toolbox.support.BladePage;

public interface IService<M> {


	public M findById(Object id);


	public List<M> find(String sql, Object modelOrMap);


	public List<M> findTop(int topNum, M model);
	

	public List<M> findTop(int topNum, String sqlTemplate);
	

	public List<M> findTop(int topNum, String sqlTemplate, Object modelOrMap);


	public List<M> findAll();


	public List<M> findBy(String where, Object modelOrMap);


	public List<M> findBy(String columns, String where, Object modelOrMap);


	public List<M> findByTemplate(M model);


	public M findFirst(String sql, Object modelOrMap);


	public M findFirstBy(String where, Object modelOrMap);


	public boolean save(M model);
	

	public int saveRtId(M model);


	public String saveRtStrId(M model);


	public boolean saveAndSetKey(M model);


	public boolean update(M model);


	public boolean updateEveryCol(M model);


	public int updateAllRecords(M model);


	public boolean updateBy(String set, String where, Object modelOrMap);
	

	public int[] updateBathById(List<M> list);


	public int delete(Object id);


	public int deleteByIds(String ids);


	public int deleteByCols(String col, String ids);



	public int deleteTableByCols(String table, String col, String ids);
	

	public int deleteBy(String sqlTemplate);


	public int deleteBy(String where, Object modelOrMap);


	public long total();


	public long count(M model);


	public int count(String where, Object modelOrMap);


	public int countBy(String sqlTemplate, Object modelOrMap);
	

	public List<M> getList(int start, int size);


	public List<M> getList(M model, int start, int size);
	

	public List<M> getList(String sqlTemplate, Object modelOrMap, int start, int size);
	

	public BladePage<M> paginate(String sqlTemplate, Object paras, int start, int size);
	

	public boolean isExist(String sqlTemplate, Object modelOrMap);


	public Object getIdValue(Object model);
	

	public boolean save(M model, AopContext ac);


	public boolean update(M model, AopContext ac);


	public boolean removeByIds(String ids, AopContext ac);


	public boolean delByIds(String ids, AopContext ac);


	public boolean restoreByIds(String ids, AopContext ac);
	

	public boolean save(M model, AopContext ac, ICURD intercept);


	public boolean update(M model, AopContext ac, ICURD intercept);


	public boolean removeByIds(String ids, AopContext ac, ICURD intercept);


	public boolean delByIds(String ids, AopContext ac, ICURD intercept);


	public boolean restoreByIds(String ids, AopContext ac, ICURD intercept);
	
}
