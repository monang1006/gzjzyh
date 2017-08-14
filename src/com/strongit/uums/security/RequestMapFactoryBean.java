package com.strongit.uums.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.SecurityConfig;
import org.springframework.security.intercept.web.RequestKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.oa.common.user.util.PrivilHelper;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;

@Service("requestMap")
@Transactional
public class RequestMapFactoryBean implements FactoryBean {
	private IGenericDAO<TUumsBasePrivil, String> TUumsBasePrivilDao;
	private LinkedHashMap<RequestKey, ConfigAttributeDefinition> requestMap;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		TUumsBasePrivilDao = new GenericDAOHibernate<TUumsBasePrivil, String>(sessionFactory,
				TUumsBasePrivil.class);
		/*System.out.println("############################# 0"
				+ sessionFactory.toString());*/
	}

	@SuppressWarnings("unchecked")
	public void init() {
		requestMap = new LinkedHashMap<RequestKey, ConfigAttributeDefinition>();
		/*System.out.println("############################# 1");*/
		List<TUumsBasePrivil> TUumsBasePrivils = TUumsBasePrivilDao.findAll();
		
		StringBuffer privilMapKey;
		/*System.out.println("############################# 2");*/
		
		Map<String, List<ConfigAttribute>> tmpRequestMap = new HashMap<String, List<ConfigAttribute>>();
		
		for (TUumsBasePrivil TUumsBasePrivil : TUumsBasePrivils) {
			privilMapKey = new StringBuffer("");
			/**
			 * 唯一确定一个权限的编码为：权限所属系统编码-权限编码
			 * 系统启动时将基权限信息保存到内存中
			 */
			privilMapKey.append(TUumsBasePrivil.getBasePrivilType().getBaseSystem().getSysSyscode())
				.append("-").append(TUumsBasePrivil.getPrivilSyscode());
			
			if(!PrivilHelper.checkHas(privilMapKey.toString())){
				PrivilHelper.addPrivil(privilMapKey.toString(), TUumsBasePrivil);
			}
			
			/**
			 * 判断是否具有权限属性，权限属性为空则不加入基权限中，但会存在在登录用户具有的权限中
			 */
			if(TUumsBasePrivil.getPrivilCode() != null
					&& !"".equals(TUumsBasePrivil.getPrivilCode())){
				String[] urls = TUumsBasePrivil.getPrivilCode().split(";");//支持一个资源权限中设置多个url，以“;”分隔
				for(String url : urls){
					ConfigAttribute attribute = new SecurityConfig(privilMapKey.toString());
					
					if(tmpRequestMap.containsKey(url)){
						tmpRequestMap.get(url).add(attribute);
					}else{
						List<ConfigAttribute> tmpArray = new ArrayList<ConfigAttribute>(1);
						tmpArray.add(attribute);
						tmpRequestMap.put(url, tmpArray);
					}
				}
			}
		}
		
		if(!tmpRequestMap.isEmpty()){
			for(Map.Entry<String, List<ConfigAttribute>> entry : tmpRequestMap.entrySet()){
				RequestKey key = new RequestKey(entry.getKey().trim());
				ConfigAttributeDefinition definition = new ConfigAttributeDefinition(entry.getValue());
				requestMap.put(key, definition);
			}
		}
	}

	public Object getObject() throws Exception {
		if (requestMap == null) {
			init();
		}
		return requestMap;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LinkedHashMap.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
