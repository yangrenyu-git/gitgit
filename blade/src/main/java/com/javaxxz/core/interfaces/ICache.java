
package com.javaxxz.core.interfaces;

import java.util.List;
import java.util.Map;


public interface ICache {

	Map<String, Object> findOne(String cacheName, String key, String sql);

	Map<String, Object> findOne(String cacheName, String key, String sql, Object modelOrMap);

	Map<String, Object> findOneBySqlId(String cacheName, String key, String sqlId);

	Map<String, Object> findOneBySqlId(String cacheName, String key, String sqlId, Object modelOrMap);

	List<Map<String, Object>> find(String cacheName, String key, String sql);

	List<Map<String, Object>> find(String cacheName, String key, String sql, Object modelOrMap);

	List<Map<String, Object>> findBySqlId(String cacheName, String key, String sqlId);

	List<Map<String, Object>> findBySqlId(String cacheName, String key, String sqlId, Object modelOrMap);
}
