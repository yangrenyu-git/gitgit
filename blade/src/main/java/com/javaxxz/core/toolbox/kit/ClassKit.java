package com.javaxxz.core.toolbox.kit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.support.BasicType;
import com.javaxxz.core.toolbox.support.Singleton;


public class ClassKit {
	private final static Logger log = LogManager.getLogger(ClassKit.class);
	
	private ClassKit() {
		// 静态类不可实例化
	}
	

	public static Class<?>[] getClasses(Object... objects){
		Class<?>[] classes = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}
	

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricFirstType(Class clazz){
		return getSuperClassGenricType(clazz, 0);
	}
	

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(Class clazz, int index){
		Type t = clazz.getGenericSuperclass();
		if (!(t instanceof ParameterizedType)) {
			return null;
		}
		ParameterizedType parameterizedType = (ParameterizedType) t;
		Type[] params = parameterizedType.getActualTypeArguments();
		if (null == params) {
			return null;
		}
		if (index < 0 || index > params.length - 1) {
			return null;
		}
		Type param = params[index];
		if (param instanceof Class) {
			return (Class) param;
		}
		return null;
	}
	

	public static Set<Class<?>> scanPackage() {
		return scanPackage(StrKit.EMPTY, null);
	}
	

	public static Set<Class<?>> scanPackage(String packageName) {
		return scanPackage(packageName, null);
	}
	

	public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
		return scanPackage(packageName, new ClassFilter() {
			@Override
			public boolean accept(Class<?> clazz) {
				return clazz.isAnnotationPresent(annotationClass);
			}
		});
	}
	

	public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
		return scanPackage(packageName, new ClassFilter() {
			@Override
			public boolean accept(Class<?> clazz) {
				return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
			}
		});
	}
	

	public static Set<Class<?>> scanPackage(String packageName, ClassFilter classFilter) {
		if(StrKit.isBlank(packageName)) {
			packageName = StrKit.EMPTY;
		}
		log.debug("Scan classes from package ["+packageName+"]...");
		packageName = getWellFormedPackageName(packageName);
		
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		for (String classPath : getClassPaths(packageName)) {
			//bug修复，由于路径中空格和中文导致的Jar找不到
			classPath = URLKit.decode(classPath, CharsetKit.systemCharset());
			log.debug("Scan classpath: ["+classPath+"]");
			// 填充 classes
			fillClasses(classPath, packageName, classFilter, classes);
		}
		
		//如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
		if(classes.isEmpty()) {
			for (String classPath : getJavaClassPaths()) {
				//bug修复，由于路径中空格和中文导致的Jar找不到
				classPath = URLKit.decode(classPath, CharsetKit.systemCharset());
				
				log.debug("Scan java classpath: ["+classPath+"]");
				// 填充 classes
				fillClasses(classPath, new File(classPath), packageName, classFilter, classes);
			}
		}
		return classes;
	}


	public final static Set<String> getMethods(Class<?> clazz) {
		HashSet<String> methodSet = new HashSet<String>();
		Method[] methodArray = clazz.getMethods();
		for (Method method : methodArray) {
			String methodName = method.getName();
			methodSet.add(methodName);
		}
		return methodSet;
	}
	

	public static String[] getJavaClassPaths() {
		String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		return classPaths;
	}
	

	public static Class<?> castToPrimitive(Class<?> clazz) {
		if(null == clazz || clazz.isPrimitive()) {
			return clazz;
		}
		
		BasicType basicType;
		try {
			basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
		}catch(Exception e) {
			return clazz;
		}
		
		//基本类型
		switch (basicType) {
			case BYTE:
				return byte.class;
			case SHORT:
				return short.class;
			case INTEGER:
				return int.class;
			case LONG:
				return long.class;
			case DOUBLE:
				return double.class;
			case FLOAT:
				return float.class;
			case BOOLEAN:
				return boolean.class;
			case CHAR:
				return char.class;
			default:
				return clazz;
		}
	}
	

	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	

	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if(classLoader == null) {
			classLoader = ClassKit.class.getClassLoader();
		}
		return classLoader;
	}


	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (Exception e) {
			throw new ToolBoxException(StrKit.format("Instance class [{}] error!", clazz), e);
		}
	}
	

	public static <T> T newInstance(Class<T> clazz) {
		if (null == clazz)
			return null;
		try {
			return (T) clazz.newInstance();
		} catch (Exception e) {
			throw new ToolBoxException(StrKit.format("Instance class [{}] error!", clazz), e);
		}
	}
	

	public static <T> T newInstance(Class<T> clazz, Object... params) {
		if(CollectionKit.isEmpty(params)){
			return newInstance(clazz);
		}
		
		try {
			return clazz.getDeclaredConstructor(getClasses(params)).newInstance(params);
		} catch (Exception e) {
			throw new ToolBoxException(StrKit.format("Instance class [{}] error!", clazz), e);
		}
	}


	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T cloneObj(T obj) {
		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); 
		
		try {
			final ObjectOutputStream out = new ObjectOutputStream(byteOut); 
			out.writeObject(obj); 
			final ObjectInputStream in =new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
			return (T) in.readObject();
		} catch (Exception e) {
			throw new ToolBoxException(e);
		}
	}
	

	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String className, boolean isInitialized) {
		Class<T> clazz;
		try {
			clazz = (Class<T>) Class.forName(className, isInitialized, getClassLoader());
		}catch (ClassNotFoundException e) {
			throw new ToolBoxException(e);
		}
		return clazz;
	}
	

	public static <T> Class<T> loadClass(String className) {
		return loadClass(className, true);
	}
	

	public static Set<String> getClassPathResources(){
		return getClassPaths(StrKit.EMPTY);
	}


	public static Set<String> getClassPaths(String packageName) {
		String packagePath = packageName.replace(StrKit.DOT, StrKit.SLASH);
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(packagePath);
		} catch (IOException e) {
			throw new ToolBoxException(StrKit.format("Loading classPath [{}] error!", packagePath), e);
		}
		Set<String> paths = new HashSet<String>();
		while (resources.hasMoreElements()) {
			paths.add(resources.nextElement().getPath());
		}
		return paths;
	}


	public static String getClassPath() {
		return getClassPathURL().getPath();
	}


	public static URL getClassPathURL() {
		return getURL(StrKit.EMPTY);
	}


	public static URL getURL(String resource) {
		return ClassKit.getClassLoader().getResource(resource);
	}
	

	public static <T> T invoke(String classNameDotMethodName, Object... args) {
		return invoke(classNameDotMethodName, false, args);
	}


	public static <T> T invoke(String classNameDotMethodName, boolean isSingleton, Object... args) {
		if (StrKit.isBlank(classNameDotMethodName)) {
			throw new ToolBoxException("Blank classNameDotMethodName!");
		}
		final int dotIndex = classNameDotMethodName.lastIndexOf('.');
		if (dotIndex <= 0) {
			throw new ToolBoxException("Invalid classNameDotMethodName [{}]!", classNameDotMethodName);
		}

		final String className = classNameDotMethodName.substring(0, dotIndex);
		final String methodName = classNameDotMethodName.substring(dotIndex + 1);

		return invoke(className, methodName, isSingleton, args);
	}


	public static <T> T invoke(String className, String methodName, Object... args) {
		return invoke(className, methodName, false, args);
	}


	public static <T> T invoke(String className, String methodName, boolean isSingleton, Object... args) {
		Class<Object> clazz = loadClass(className);
		try {
			return invoke(isSingleton ? Singleton.create(clazz) : clazz.newInstance(), methodName, args);
		} catch (Exception e) {
			throw new ToolBoxException(e);
		}
	}


	public static <T> T invoke(Object obj, String methodName, Object... args) {
		try {
			final Method method = getDeclaredMethod(obj, methodName, args);
			return invoke(obj, method, args);
		} catch (Exception e) {
			throw new ToolBoxException(e);
		}
	}
	

	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object obj, Method method, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (false == method.isAccessible()) {
			method.setAccessible(true);
		}
		return (T) method.invoke(isStatic(method) ? null : obj, args);
	}
	

	public static Method getDeclaredMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, SecurityException {
		return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
	}


	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Method method = null;
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (NoSuchMethodException e) {
				//继续向上寻找
			}
		}
		return Object.class.getDeclaredMethod(methodName, parameterTypes);
	}
	

	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(Class<T> interfaceClass, InvocationHandler invocationHandler){
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), interfaceClass.getInterfaces(), invocationHandler);
	}
	

	@SuppressWarnings("unchecked")
	public static <T> T newProxyCglibFactory(Class<T> clazz, MethodInterceptor methodInterceptor){
		try {
			Class<?> superClass = Class.forName(clazz.getName());
			Enhancer hancer = new Enhancer();
			hancer.setSuperclass(superClass);
			hancer.setCallback(methodInterceptor);
			return (T) hancer.create();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.wrapperPrimitiveMap.containsKey(clazz);
	}


	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}


	public static boolean isSimpleTypeOrArray(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}


	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) || clazz.isEnum() || CharSequence.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || clazz
				.equals(URI.class) || clazz.equals(URL.class) || clazz.equals(Locale.class) || clazz.equals(Class.class);
	}


	public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}
		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}
		// 基本类型
		if (targetType.isPrimitive()) {
			// 原始类型
			Class<?> resolvedPrimitive = BasicType.wrapperPrimitiveMap.get(sourceType);
			if (resolvedPrimitive != null && targetType.equals(resolvedPrimitive)) {
				return true;
			}
		} else {
			// 包装类型
			Class<?> resolvedWrapper = BasicType.primitiveWrapperMap.get(sourceType);
			if (resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}


	public static boolean isPublic(Class<?> clazz) {
		if (null == clazz) {
			throw new NullPointerException("Class to provided is null.");
		}
		return Modifier.isPublic(clazz.getModifiers());
	}


	public static boolean isPublic(Method method) {
		if (null == method) {
			throw new NullPointerException("Method to provided is null.");
		}
		return isPublic(method.getDeclaringClass());
	}


	public static boolean isNotPublic(Class<?> clazz) {
		return false == isPublic(clazz);
	}


	public static boolean isNotPublic(Method method) {
		return false == isPublic(method);
	}
	

	public static boolean isStatic(Method method){
		return Modifier.isStatic(method.getModifiers());
	}


	public static Method setAccessible(Method method) {
		if (null != method && isNotPublic(method)) {
			method.setAccessible(true);
		}
		return method;
	}
	
	//--------------------------------------------------------------------------------------------------- Private method start

	private static FileFilter fileFilter = new FileFilter(){
		@Override
		public boolean accept(File pathname) {
			return isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);
		}
	};


	private static String getWellFormedPackageName(String packageName) {
		return packageName.lastIndexOf(StrKit.DOT) != packageName.length() - 1 ? packageName + StrKit.DOT : packageName;
	}


	private static void fillClasses(String path, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		//判定给定的路径是否为Jar
		int index = path.lastIndexOf(FileKit.JAR_PATH_EXT);
		if(index != -1) {
			//Jar文件
			path = path.substring(0, index + FileKit.JAR_FILE_EXT.length());	//截取jar路径
			path = StrKit.removePrefix(path, FileKit.PATH_FILE_PRE);	//去掉文件前缀
			processJarFile(new File(path), packageName, classFilter, classes);
		}else {
			fillClasses(path, new File(path), packageName, classFilter, classes);
		}
	}
	

	private static void fillClasses(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		if (file.isDirectory()) {
			processDirectory(classPath, file, packageName, classFilter, classes);
		} else if (isClassFile(file)) {
			processClassFile(classPath, file, packageName, classFilter, classes);
		} else if (isJarFile(file)) {
			processJarFile(file, packageName, classFilter, classes);
		}
	}


	private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		for (File file : directory.listFiles(fileFilter)) {
			fillClasses(classPath, file, packageName, classFilter, classes);
		}
	}


	private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		if(false == classPath.endsWith(File.separator)) {
			classPath += File.separator;
		}
		String path = file.getAbsolutePath();
		if(StrKit.isBlank(packageName)) {
			path = StrKit.removePrefix(path, classPath);
		}
		final String filePathWithDot = path.replace(File.separator, StrKit.DOT);
		
		int subIndex = -1;
		if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
			final int endIndex = filePathWithDot.lastIndexOf(FileKit.CLASS_EXT);
			
			final String className = filePathWithDot.substring(subIndex, endIndex);
			fillClass(className, packageName, classes, classFilter);
		}
	}


	private static void processJarFile(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		try {
			for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
				if (isClass(entry.getName())) {
					final String className = entry.getName().replace(StrKit.SLASH, StrKit.DOT).replace(FileKit.CLASS_EXT, StrKit.EMPTY);
					fillClass(className, packageName, classes, classFilter);
				}
			}
		} catch (Throwable ex) {
			log.error(ex.getMessage(), ex);
		}
	}


	private static void fillClass(String className, String packageName, Set<Class<?>> classes, ClassFilter classFilter) {
		if (className.startsWith(packageName)) {
			try {
				final Class<?> clazz = Class.forName(className, false, getClassLoader());
				if (classFilter == null || classFilter.accept(clazz)) {
					classes.add(clazz);
				}
			} catch (Throwable ex) {
				//Log.error(log, ex, "Load class [{}] error!", className);
				//Pass Load Error.
			}
		}
	}


	private static boolean isClassFile(File file) {
		return isClass(file.getName());
	}
	

	private static boolean isClass(String fileName) {
		return fileName.endsWith(FileKit.CLASS_EXT);
	}


	private static boolean isJarFile(File file) {
		return file.getName().endsWith(FileKit.JAR_FILE_EXT);
	}
	//--------------------------------------------------------------------------------------------------- Private method end
	

	public interface ClassFilter {
		boolean accept(Class<?> clazz);
	}
}
