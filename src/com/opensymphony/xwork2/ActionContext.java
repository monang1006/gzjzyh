package com.opensymphony.xwork2;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ActionContext
  implements Serializable
{
  static ThreadLocal<ActionContext> actionContext = new ThreadLocal();
  public static final String ACTION_NAME = "com.opensymphony.xwork2.ActionContext.name";
  public static final String VALUE_STACK = "com.opensymphony.xwork2.util.ValueStack.ValueStack";
  public static final String SESSION = "com.opensymphony.xwork2.ActionContext.session";
  public static final String APPLICATION = "com.opensymphony.xwork2.ActionContext.application";
  public static final String PARAMETERS = "com.opensymphony.xwork2.ActionContext.parameters";
  public static final String LOCALE = "com.opensymphony.xwork2.ActionContext.locale";
  public static final String TYPE_CONVERTER = "com.opensymphony.xwork2.ActionContext.typeConverter";
  public static final String ACTION_INVOCATION = "com.opensymphony.xwork2.ActionContext.actionInvocation";
  public static final String CONVERSION_ERRORS = "com.opensymphony.xwork2.ActionContext.conversionErrors";
  public static final String CONTAINER = "com.opensymphony.xwork2.ActionContext.container";
  private Map<String, Object> context;

  public ActionContext(Map<String, Object> context)
  {
    this.context = context;
  }

  public void setActionInvocation(ActionInvocation actionInvocation)
  {
    put("com.opensymphony.xwork2.ActionContext.actionInvocation", actionInvocation);
  }

  public ActionInvocation getActionInvocation()
  {
    return ((ActionInvocation)get("com.opensymphony.xwork2.ActionContext.actionInvocation"));
  }

  public void setApplication(Map<String, Object> application)
  {
    put("com.opensymphony.xwork2.ActionContext.application", application);
  }

  public Map<String, Object> getApplication()
  {
    return ((Map)get("com.opensymphony.xwork2.ActionContext.application"));
  }

  public static void setContext(ActionContext context)
  {
    actionContext.set(context);
  }

  public static ActionContext getContext()
  {
    return (ActionContext)actionContext.get();
  }

  public void setContextMap(Map<String, Object> contextMap)
  {
    getContext().context = contextMap;
  }

  public Map<String, Object> getContextMap()
  {
    return this.context;
  }

  public void setConversionErrors(Map<String, Object> conversionErrors)
  {
    put("com.opensymphony.xwork2.ActionContext.conversionErrors", conversionErrors);
  }

  public Map<String, Object> getConversionErrors()
  {
    Map errors = (Map)get("com.opensymphony.xwork2.ActionContext.conversionErrors");

    if (errors == null) {
      errors = new HashMap();
      setConversionErrors(errors);
    }

    return errors;
  }

  public void setLocale(Locale locale)
  {
    put("com.opensymphony.xwork2.ActionContext.locale", locale);
  }

  public Locale getLocale()
  {
    Locale locale = (Locale)get("com.opensymphony.xwork2.ActionContext.locale");

    if (locale == null) {
      locale = Locale.getDefault();
      setLocale(locale);
    }

    return locale;
  }

  public void setName(String name)
  {
    put("com.opensymphony.xwork2.ActionContext.name", name);
  }

  public String getName()
  {
    return ((String)get("com.opensymphony.xwork2.ActionContext.name"));
  }

  public void setParameters(Map<String, Object> parameters)
  {
    put("com.opensymphony.xwork2.ActionContext.parameters", parameters);
  }

  public Map<String, Object> getParameters()
  {
    return ((Map)get("com.opensymphony.xwork2.ActionContext.parameters"));
  }

  public void setSession(Map<String, Object> session)
  {
    put("com.opensymphony.xwork2.ActionContext.session", session);
  }

  public Map<String, Object> getSession()
  {
    return ((Map)get("com.opensymphony.xwork2.ActionContext.session"));
  }

  public void setValueStack(ValueStack stack)
  {
    put("com.opensymphony.xwork2.util.ValueStack.ValueStack", stack);
  }

  public ValueStack getValueStack()
  {
    return ((ValueStack)get("com.opensymphony.xwork2.util.ValueStack.ValueStack"));
  }

  public void setContainer(Container cont)
  {
    put("com.opensymphony.xwork2.ActionContext.container", cont);
  }

  public Container getContainer()
  {
    return ((Container)get("com.opensymphony.xwork2.ActionContext.container"));
  }

  public <T> T getInstance(Class<T> type) {
    Container cont = getContainer();
    if (cont != null) {
      return cont.getInstance(type);
    }
    throw new XWorkException("Cannot find an initialized container for this request.");
  }

  public Object get(String key)
  {
    return this.getContextMap().get(key);
  }

  public void put(String key, Object value)
  {
    this.context.put(key, value);
  }
}