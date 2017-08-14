package com.strongit.oa.paramconfig;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSystemParamConfig;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 系统参数配置服务类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-8-3 上午11:51:31
 * @version  2.0.2.3
 * @classpath com.strongit.oa.paramconfig.ParamConfigService
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
public class ParamConfigService {

	GenericDAOHibernate<ToaSystemParamConfig, String> configDao ;			//定义DAO操作类
	
	/**
	 * 通过Spring上下文注入Session工厂.
	 * @author:邓志城
	 * @date:2010-8-3 上午11:50:52
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		configDao = new GenericDAOHibernate<ToaSystemParamConfig,String>(sessionFactory,ToaSystemParamConfig.class);
	}

	/**
	 * 保存配置信息.
	 * @author:邓志城
	 * @date:2010-8-3 下午02:10:45
	 * @param config
	 * @throws ServiceException
	 */
	public void save(ToaSystemParamConfig config) throws ServiceException {
		configDao.save(config);
	}

	/**
	 * 得到配置信息
	 * @author:邓志城
	 * @date:2010-8-9 上午10:18:47
	 * @param id	主键id
	 * @return		配置信息
	 * @throws ServiceException
	 */
	public ToaSystemParamConfig getToaSystemParamConfig(String id) throws ServiceException {
		return configDao.get(id);
	}
	
	/**
	 * 保存配置信息
	 * @author:邓志城
	 * @date:2010-8-3 下午02:13:26
	 * @param userId	用户id
	 * @param size		设置空间大小，单位为KB.
	 * @param module	配置对应模块.
	 */
	public void save(String userId,String size,ConfigModule module) {
		ToaSystemParamConfig config = this.getConfByModule(module,userId);
		config.setUserId(userId);
		config.setSize(size);
		config.setModule(module.getValue());
		save(config);
	}

	/**
	 * 保存配置信息
	 * @author:邓志城
	 * @date:2010-8-3 下午02:13:26
	 * @param userId	用户id
	 * @param size		设置空间大小，单位为KB.
	 * @param module	配置对应模块.
	 */
	public ToaSystemParamConfig getConfByModule(ConfigModule module , String userId) {
		StringBuilder hql = new StringBuilder(" from ToaSystemParamConfig t ");
		List<String> param = new ArrayList<String>(1);
		hql.append(" where t.module = ?");
		param.add(module.getValue());
		if(userId != null && !userId.equals("null") && userId.length() > 0 ){
			hql.append(" and t.userId = ?");
			param.add(userId);
		}else {
			hql.append(" and t.userId is null ");
		}
		List list = configDao.find(hql.toString(), param.toArray());
		if(list != null && !list.isEmpty()){
			ToaSystemParamConfig objs = (ToaSystemParamConfig)list.get(0);
			return objs;
		}else{
			return new ToaSystemParamConfig();
		}
	}
	
	/**
	 * 得到模块对应的空间大小.单位为KB.
	 * @author:邓志城
	 * @date:2010-8-3 下午02:24:23
	 * @param module	配置对应模块
	 * @param userId	用户id，不传入表示获取系统空间大小
	 * @return			分配的空间大小
	 */
	public String getConfigSize(ConfigModule module , String...userId) {
		StringBuilder hql = new StringBuilder("select t.size from ToaSystemParamConfig t ");
		List<String> param = new ArrayList<String>(1);
		hql.append(" where t.module = ?");
		param.add(module.getValue());
		if(userId != null && userId.length > 0){
			hql.append(" and t.userId = ?");
			param.add(userId[0]);
		}else {
			hql.append(" and t.userId is null ");
		}
		List list = configDao.find(hql.toString(), param.toArray());
		if(list != null && !list.isEmpty()){
			Object objs = list.get(0);
			if(objs != null)
				return objs.toString();
		}
		return null;
	}
}
