package com.strongit.oa.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * 反射的Utils函数集合
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Uitls函数
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-9-28 上午10:36:40
 * @version  2.0.2.3
 * @classpath org.springside.modules.utils.ReflectionUtils
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class ReflectionUtils {

	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过gettter函数.
	 * @author:邓志城
	 * @date:2009-9-28 上午11:35:59
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(final Object object,final String fieldName){
		Field field = getDeclaredField(object, fieldName);
		
		if(field == null){
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		Object result = null ;
		try{
			result = field.get(object);
		}catch(IllegalAccessException e){
			logger.error("不可能抛出的异常{}",e.getMessage());
		}
		return result ;
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 * @author:邓志城
	 * @date:2009-9-28 上午11:40:53
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(final Object object,final String fieldName,final Object value) {
		Field field = getDeclaredField(object, fieldName);
		if(field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		
		makeAccessible(field);
		try{
			field.set(object, value);
		}catch(IllegalAccessException e){
			logger.error("不可能抛出的异常{}" + e.getMessage());
		}
	}
	
	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * @author:邓志城
	 * @date:2009-9-28 上午10:59:20
	 * @param object
	 * @param fieldName
	 * @return
	 */
	protected static Field getDeclaredField(final Object object,final String fieldName){
		Assert.notNull(object,"object不能为空！");
		Assert.hasText(fieldName);
		for(Class<?> superClass = object.getClass();superClass!=Object.class;superClass = superClass.getSuperclass()){
			try{
				return superClass.getDeclaredField(fieldName);
			}catch(NoSuchFieldException e){
				//Field不再当前类定义中,继续向上转型
			}
		}
		return null;
	}
	
	/**
	 * 循环向上转型，获取对象的DeclareMethod.
	 * @author:邓志城
	 * @date:2009-9-28 上午11:52:05
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	protected static Method getDeclareMethod(Object object,String methodName,Class<?>[] parameterTypes) {
		Assert.notNull(object,"object不能为空！");
		
		for(Class<?> superClass = object.getClass();superClass!=Object.class;superClass = superClass.getSuperclass()){
			try{
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			}catch(NoSuchMethodException e){
				//Method不再当前类定义中
			}
		}
		return null ;
	}
	
	/**
	 * 设置私有变量可用访问
	 * @author:邓志城
	 * @date:2009-9-28 上午10:41:56
	 * @param field
	 */
	protected static void makeAccessible(final Field field) {
		if(!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())){
			field.setAccessible(true);
		}
	}

	/**
	 * 直接调用对象方法,无视private/protected修饰符.
	 * @author:邓志城
	 * @date:2009-9-28 下午02:05:59
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(final Object object,
									  final String methodName,
									  final Class<?>[] parameterTypes,
									  final Object[] parameters) throws InvocationTargetException {
		Method method = getDeclareMethod(object, methodName, parameterTypes);
		
		if(method == null){
			throw new IllegalArgumentException("Could not find method [" + methodName + " ] on target [" + object + "]" );
		}
		method.setAccessible(true);
		try{
			return method.invoke(object, parameters);
		}catch(IllegalAccessException e){
			logger.error("不可能抛出的异常：{}",e.getMessage());
		}
		return null;
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * @author:邓志城
	 * @date:2009-9-28 下午03:51:01
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class getSuperClassGenricType(final Class clazz,final int index) {
		
		Type genType = clazz.getGenericSuperclass();
		
		if(!(genType instanceof ParameterizedType)){
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		
		Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		if(index >= params.length || index < 0){
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}
	
	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * @author:邓志城
	 * @date:2009-9-28 下午03:51:01
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成list.
	 * @author:邓志城
	 * @date:2009-9-28 下午04:09:15
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List fetchElementPropertyToList(final Collection collection,final String propertyName){
		
		List list = new ArrayList();
		try{
			for(Object obj : collection){
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		}catch(Exception e){
			convertToUncheckedException(e);
		}
		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertityName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	@SuppressWarnings("unchecked")
	public static String fetchElementPropertyToString(final Collection collection, final String propertyName,
			final String separator) {
		List list = fetchElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}
	
	/**
	 * 将反射时的checked exception转换成unchecked exception .
	 * @author:邓志城
	 * @date:2009-9-28 下午04:02:06
	 * @param e
	 * @throws IllegalArgumentException
	 */
	public static void convertToUncheckedException(Exception e) throws IllegalArgumentException {
		if(e instanceof IllegalAccessException || e instanceof IllegalArgumentException ||
			e instanceof NoSuchMethodException	){
			throw new IllegalArgumentException("Reflection Exception.",e);
		}else{
			throw new IllegalArgumentException(e);
		}
			
	}
}
