package com.javaxxz.core.toolbox.kit;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.support.Convert;


public class BeanKit {
	

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException{
		return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
	}
	


	

	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass) {
		return fillBeanWithMap(map, ClassKit.newInstance(beanClass));
	}
	

	public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass) {
		return fillBeanWithMapIgnoreCase(map, ClassKit.newInstance(beanClass));
	}
	

	public static <T> T fillBeanWithMap(final Map<?, ?> map, T bean) {
		return fill(bean, new ValueProvider(){
			@Override
			public Object value(String name) {
				return map.get(name);
			}
		});
	}
	

	public static <T> T fillBeanWithMapIgnoreCase(Map<?, ?> map, T bean) {
		final Map<Object, Object> map2 = new HashMap<Object, Object>();
		for (Entry<?, ?> entry : map.entrySet()) {
			final Object key = entry.getKey();
			if(key instanceof String) {
				final String keyStr = (String)key;
				map2.put(keyStr.toLowerCase(), entry.getValue());
			}else{
				map2.put(key, entry.getValue());
			}
		}
		
		return fill(bean, new ValueProvider(){
			@Override
			public Object value(String name) {
				return map2.get(name.toLowerCase());
			}
		});
	}
	

	public static <T> T requestParamToBean(javax.servlet.ServletRequest request, Class<T> beanClass){
		return fillBeanWithRequestParam(request, ClassKit.newInstance(beanClass));
	}
	

	public static <T> T fillBeanWithRequestParam(final javax.servlet.ServletRequest request, T bean){
		final String beanName = StrKit.lowerFirst(bean.getClass().getSimpleName());
		return fill(bean, new ValueProvider(){
			@Override
			public Object value(String name) {
				String value = request.getParameter(name);
				if (StrKit.isBlank(value)) {
					//使用类名前缀尝试查找值
					value = request.getParameter(beanName + StrKit.DOT + name);
					if(StrKit.isBlank(value)){
						//此处取得的值为空时跳过，包括null和""
						value = null;
					}
				}
				return value;
			}
		});
	}
	

	public static <T> T toBean(Class<T> beanClass, ValueProvider valueProvider){
		return fill(ClassKit.newInstance(beanClass), valueProvider);
	}
	

	public static <T> T fill(T bean, ValueProvider valueProvider){
		if(null == valueProvider){
			return bean;
		}
		
		Class<?> beanClass = bean.getClass();
		try {
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(beanClass);
			String propertyName;
			Object value;
			for (PropertyDescriptor property : propertyDescriptors) {
				propertyName = property.getName();
				value = valueProvider.value(propertyName);
				if (null == value) {
					//此处取得的值为空时跳过，包括null和""
					continue;
				}
				
				try {
					property.getWriteMethod().invoke(bean, Convert.parse(property.getPropertyType(), value));
				} catch (Exception e) {
					throw new ToolBoxException(StrKit.format("Inject [{}] error!", property.getName()), e);
				}
			}
		} catch (Exception e) {
			throw new ToolBoxException(e);
		}
		return bean;
	}


	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> beanToMap(T bean) {

		if (bean == null) {
			return null;
		}
		if(bean.getClass().equals(Paras.class)){
			return (Map<String, Object>) bean;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(bean.getClass());
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (false == key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					if (null != value) {
						map.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			throw new ToolBoxException(e);
		}
		return map;
	}


	public static Map<String, Object> map2Map(Map<?, ?> oldMap,
			Map<String, Object> switchMap) {
		Map<String, Object> map = new HashMap<>();
		for (String key : switchMap.keySet()) {
			map.put(switchMap.get(key).toString().toLowerCase(),
					oldMap.get(key));
		}
		return map;
	}
	

	public static Map<String, Object> reverseMap2Map(Map<?, ?> oldMap, Map<String, Object> reverseMap) {
		Map<String, Object> map = new HashMap<>();
		for (String key : reverseMap.keySet()) {
			String reverseKey = ((String) reverseMap.get(key)).toUpperCase();
			map.put(key.toLowerCase(), oldMap.get(reverseKey));
		}
		return map;
	}
	

	public static Map<String, Object> reverse(Map<String, Object> reverseMap,
			Object model) {
		Map<String, Object> map = beanToMap(model);
		if (!Func.isEmpty(reverseMap)) {
			return map2Map(map, reverseMap);
		}
		return map;
	}
	

	public static interface ValueProvider{

		public Object value(String name);
	}
}
