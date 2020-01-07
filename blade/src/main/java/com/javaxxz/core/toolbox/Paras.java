package com.javaxxz.core.toolbox;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.javaxxz.core.toolbox.kit.BeanKit;
import com.javaxxz.core.toolbox.support.Convert;


@SuppressWarnings("serial")
public class Paras extends HashMap<String, Object> {


	public static Paras create() {
		return new Paras();
	}

	private Paras(){
		
	}
	

	public static HashMap<String, Object> createHashMap() {
		return new HashMap<>();
	}
	

	public static <T> Paras parse(T bean) {
		return create().parseBean(bean);
	}


	public static <T> Paras parse(Map<String, Object> map) {
		return create().parseMap(map);
	}

	

	public <T> T toBean(T bean) {
		BeanKit.fillBeanWithMap(this, bean);
		return bean;
	}
	

	public <T> T toBean(Class<T> clazz) {
		return BeanKit.mapToBean(this, clazz);
	}
	

	public <T> T toBeanIgnoreCase(Class<T> clazz) {
		return BeanKit.mapToBeanIgnoreCase(this, clazz);
	}
	

	public <T> Paras parseBean(T bean) {
		this.putAll(BeanKit.beanToMap(bean));
		return this;
	}
	

	public <T> Paras parseMap(Map<String, Object> map) {
		this.putAll(map);
		return this;
	}


	public <T extends Paras> void removeEqual(T map, String... withoutNames) {
		HashSet<String> withoutSet = new HashSet<String>();
		for (String name : withoutNames) {
			withoutSet.add(name);
		}
		
		for(Entry<String, Object> entry : map.entrySet()) {
			if(withoutSet.contains(entry.getKey())) {
				continue;
			}
			
			final Object value = this.get(entry.getKey());
			if(null != value && value.equals(entry.getValue())) {
				this.remove(entry.getKey());
			}
		}
	}

	//-------------------------------------------------------------------- 特定类型值

	public Paras set(String attr, Object value) {
		return this.put(attr, value);
	}
	

	@Override
	public Paras put(String attr, Object value) {
		super.put(attr, value);
		return this;
	}
	
	

	public Paras setIgnoreNull(String attr, Object value) {
		if(null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}
	

	@SuppressWarnings("unchecked")
	public <T> T get(String attr, T defaultValue) {
		final Object result = get(attr);
		return (T)(result != null ? result : defaultValue);
	}
	

	public String getStr(String attr) {
		return Convert.toStr(get(attr), "");
	}
	

	public Integer getInt(String attr) {
		return Convert.toInt(get(attr), 0);
	}
	

	public Long getLong(String attr) {
		return Convert.toLong(get(attr), null);
	}
	

	public Float getFloat(String attr) {
		return Convert.toFloat(get(attr), null);
	}
	

	public Boolean getBool(String attr) {
		return Convert.toBool(get(attr), null);
	}
	

	public byte[] getBytes(String attr) {
		return get(attr, null);
	}
	

	public Date getDate(String attr) {
		return get(attr, null);
	}
	

	public Time getTime(String attr) {
		return get(attr, null);
	}
	

	public Timestamp getTimestamp(String attr) {
		return get(attr, null);
	}
	

	public Number getNumber(String attr) {
		return get(attr, null);
	}
	

	public BigDecimal getBigDecimal(String attr) {
		return get(attr, null);
	}
	

	public BigInteger getBigInteger(String attr) {
		return get(attr, null);
	}
	
	//-------------------------------------------------------------------- 特定类型值
	
	@Override
	public Paras clone() {
		return (Paras) super.clone();
	}
	
	//-------------------------------------------------------------------- 特定类型值
}
