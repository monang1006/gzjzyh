package com.strongit.oa.im.config;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaImConfig;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 即时通讯配置服务类
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-10-13 下午02:52:22
 * @version  2.0.7
 * @classpath com.strongit.oa.im.config.IMConfigManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Transactional
@Service("iMConfigManager")
public class IMConfigManager {

	private GenericDAOHibernate<ToaImConfig,java.lang.String> dao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new GenericDAOHibernate<ToaImConfig,java.lang.String>(sessionFactory,ToaImConfig.class);
	}
	
	/**
	 * 得到即时通讯软件配置.
	 * @author:邓志城
	 * @date:2010-6-1 下午03:16:43
	 * @return
	 */
	public ToaImConfig getConfig() {
		List<ToaImConfig> list = dao.findAll();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新配置信息.
	 * @author:邓志城
	 * @date:2010-6-1 下午04:01:57
	 * @param config
	 */
	public void updateConfig(ToaImConfig config){
		if("".equals(config.getId())){
			config.setId(null);
		}
		if(config.getImconfigUrl() == null || "".equals(config.getImconfigUrl())){
			config.setImconfigUrl("rtx.php");
		}
		dao.save(config);
	}
	
}
