package com.strongit.oa.globalconfig;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaGlobalConfig;
import com.strongit.oa.globalconfig.util.GlobalConfigModule;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Repository
@Transactional
public class GlobalConfigService {

	private GenericDAOHibernate<ToaGlobalConfig, Serializable> DAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		DAO = new GenericDAOHibernate<ToaGlobalConfig, Serializable>(
				sessionFactory, ToaGlobalConfig.class);
	}
	
	public void save(ToaGlobalConfig entry) {
		DAO.save(entry);
	}
	
	public void delete(ToaGlobalConfig entry) {
		DAO.delete(entry);
	}

	private void deleteAll() {
		DAO.delete(DAO.findAll());
	}
	
	@Transactional(readOnly = true)
	public ToaGlobalConfig get(String id) {
		return DAO.get(id);
	}
	
	public void save(String name,String value,GlobalConfigModule module,String desc) {
		ToaGlobalConfig entry = new ToaGlobalConfig();
		entry.setGlobalDate(new Date());
		entry.setGlobalDesc(desc);
		entry.setGlobalModule(module == null ? "" : module.getValue());
		entry.setGlobalValue(value);
		entry.setGlobalName(name);
		DAO.save(entry);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaGlobalConfig> getPlugins() {
		StringBuilder hql = new StringBuilder("from ToaGlobalConfig t");
		return DAO.find(hql.toString());
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public ToaGlobalConfig getToaGlobalConfig(String name) {
		StringBuilder hql = new StringBuilder("from ToaGlobalConfig t where t.globalName = ?");
		List<ToaGlobalConfig> lst = DAO.find(hql.toString(), name);
		if(lst.size() > 1) {
			throw new SystemException("找到多于一条记录！");
		}
		if(!lst.isEmpty()) {
			return lst.get(0);			
		}
		return null;
	}
	
	/**
	 * 保存全局配置项
	 * @param pluginData		配置项数据 Json格式
	 */
	public void save(String pluginData) throws ServiceException, SystemException {
		if(pluginData == null || pluginData.length() == 0) {
			throw new SystemException("参数pluginData不可为空！");
		}
		JSONArray jsonArray = JSONArray.fromObject(pluginData);
		//删除所有配置项
		deleteAll();
		if(!jsonArray.isEmpty()) {
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String pluginName = jsonObject.getString("pluginName");
				String pluginValue = jsonObject.getString("pluginValue");
				String pluginDesc = jsonObject.getString("pluginDesc");
				String pluginModule = jsonObject.getString("pluginModule");
				this.save(pluginName, pluginValue, GlobalConfigModule.getEnumEntry(pluginModule), pluginDesc);
			}
		}
	}
}
