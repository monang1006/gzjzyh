/**
 * 
 */
package com.strongit.tag.web.util;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public final class RequestUtils {

            private static Log log = null;
            private static final Locale DEFAULTLOCALE = Locale.getDefault();

            private RequestUtils() {
            }

            public static Class applicationClass(String className) throws ClassNotFoundException {
            	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            	if(classLoader == null)
            		classLoader = com.strongit.tag.web.util.RequestUtils.class.getClassLoader();
            	return classLoader.loadClass(className);
            }

            public static Object applicationInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            	return applicationClass(className).newInstance();
            }

            public static String getActionMappingName(String action) {
            	String value = action;
            	int question = action.indexOf("?");
            	if(question >= 0)
            		value = value.substring(0, question);
            	int slash = value.lastIndexOf("/");
            	int period = value.lastIndexOf(".");
            	if(period >= 0 && period > slash)
            		value = value.substring(0, period);
            	if(value.startsWith("/"))
            		return value;
            	else
            		return "/" + value;
            }

            public static Object lookup(PageContext pageContext, String name, String scope) throws JspException {
/* 136*/        Object bean = null;
/* 137*/        if(scope == null)
/* 138*/            bean = pageContext.findAttribute(name);
/* 139*/        else
/* 139*/        if(scope.equalsIgnoreCase("page"))
/* 140*/            bean = pageContext.getAttribute(name, 1);
/* 141*/        else
/* 141*/        if(scope.equalsIgnoreCase("request"))
/* 142*/            bean = pageContext.getAttribute(name, 2);
/* 143*/        else
/* 143*/        if(scope.equalsIgnoreCase("session"))
/* 144*/            bean = pageContext.getAttribute(name, 3);
/* 145*/        else
/* 145*/        if(scope.equalsIgnoreCase("application")) {
/* 147*/            bean = pageContext.getAttribute(name, 4);
                } else {
/* 150*/           //记录出错日志信息
                	JspException  e= new JspException("");
                	throw e;
                }
/* 154*/        return bean;
            }

            public static Object lookup(PageContext pageContext, String name, String property, String scope) throws JspException {
            	Object bean = lookup(pageContext, name, scope);
            	if(bean == null) {
            		JspException e = null;
            		if(scope == null)
            			e = new JspException("");
            		else
            			e = new JspException("");
            		throw e;
                }
/* 179*/        if(property == null)
/* 180*/            return bean;
/* 185*/        try {
/* 185*/            return PropertyUtils.getProperty(bean, property);
                }
/* 186*/        catch(IllegalAccessException e) {
/* 188*/            throw new JspException("");
                }
/* 190*/        catch(InvocationTargetException e) {
/* 191*/            Throwable t = e.getTargetException();
/* 192*/            if(t == null)
/* 193*/                t = ((Throwable) (e));
/* 196*/            throw new JspException("");
                }
/* 198*/        catch(NoSuchMethodException e) {
/* 200*/            throw new JspException("");
                }
            }

            public static String getMessage(String key, String desc, Object args[]) {
/* 216*/        String formatString = desc;
/* 217*/        if(formatString == null)
/* 218*/            return null;
/* 220*/        if(args == null) {
/* 221*/            return desc;
                } else {
/* 224*/            MessageFormat format = new MessageFormat(escape(formatString));
/* 225*/            return format.format(((Object) (args)));
                }
            }

            protected static String escape(String string) {
/* 235*/        if(string == null || string.indexOf('\'') < 0)
/* 236*/            return string;
/* 238*/        int n = string.length();
/* 239*/        StringBuffer sb = new StringBuffer(n);
/* 240*/        for(int i = 0; i < n; i++) {
/* 241*/            char ch = string.charAt(i);
/* 242*/            if(ch == '\'')
/* 243*/                sb.append('\'');
/* 245*/            sb.append(ch);
                }

/* 247*/        return sb.toString();
            }

            public static Object getCollection(PageContext pageContext, String property, String defaultProperty) throws JspException {
/* 260*/        String beanName = property;
/* 261*/        if(beanName == null)
/* 262*/            beanName = defaultProperty;
/* 264*/        if(log.isDebugEnabled())
/* 265*/            log.debug(((Object) ("\u8981\u83B7\u53D6\u96C6\u5408\u7684beanName\u4E3A:" + beanName)));
/* 267*/        Object bean = lookup(pageContext, beanName, ((String) (null)));
/* 269*/        if(bean == null) {
/* 271*/            JspException e = new JspException("");
/* 272*/            log.error(((Object) ("getter.bean:" + e.toString())));
/* 273*/            throw e;
                } else {
/* 275*/            return bean;
                }
            }

            public static Iterator getIterator(PageContext pageContext, String name, String defaultName) throws JspException {
/* 288*/        String beanName = name;
/* 289*/        if(beanName == null)
/* 290*/            beanName = defaultName;
/* 292*/        if(log.isTraceEnabled())
/* 293*/            log.trace(((Object) ("\u8981\u83B7\u53D6\u96C6\u5408\u7684beanName\u4E3A:" + beanName)));
/* 295*/        Object bean = lookup(pageContext, beanName, ((String) (null)));
/* 297*/        if(bean == null) {
/* 299*/            JspException e = new JspException("");
/* 300*/            log.error(((Object) ("getter.bean:" + e.toString())));
/* 301*/            throw e;
                } else {
/* 303*/            return (Iterator)(bean);
                }
            }

            public static Iterator getIterator(PageContext pageContext, String name, String defaultName, String property) throws JspException {
/* 318*/        String beanName = name;
/* 319*/        if(beanName == null)
/* 320*/            beanName = defaultName;
/* 322*/        if(log.isDebugEnabled())
/* 323*/            log.debug(((Object) ("\u8981\u83B7\u53D6\u96C6\u5408\u7684beanName\u4E3A:" + beanName)));
/* 325*/        Object bean = lookup(pageContext, beanName, ((String) (null)));
/* 327*/        if(bean == null) {
/* 329*/            JspException e = new JspException("");
/* 330*/            log.error(((Object) ("getter.bean:" + e.toString())));
/* 331*/            throw e;
                }
/* 333*/        Object collection = bean;
/* 334*/        if(property != null)
/* 336*/            try {
/* 336*/                collection = PropertyUtils.getProperty(bean, property);
/* 337*/                if(collection == null)
/* 338*/                    throw new JspException("");
                    }
/* 341*/            catch(IllegalAccessException e) {
/* 342*/                throw new JspException("");
                    }
/* 344*/            catch(InvocationTargetException e) {
/* 345*/                Throwable t = e.getTargetException();
/* 346*/                throw new JspException("");
                    }
/* 351*/            catch(NoSuchMethodException e) {
/* 352*/                throw new JspException("");
                    }
/* 357*/        return (Iterator)(bean);
            }

            static  {
/*  26*/        log = LogFactory.getLog(com.strongit.tag.web.util.RequestUtils.class);
            }
}