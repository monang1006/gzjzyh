/**
 * 
 */
package com.strongit.oa.componentMap;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaComponentMap;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

/**
 * @description 列表控件映射服务类
 * @className ComponentMapManager
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2012-2-9 上午09:17:13
 * @version 3.0
 */
@Service
@Transactional
public class ComponentMapManager{
	
	IGenericDAO<ToaComponentMap, java.lang.String> componentMapDao = null; //定义Dao操作类
	
	IGenericDAO<Object, java.lang.String>Dao = null;                     //通用DAO操作类
	
	/**
	* @method setSessionFactory
	* @author 申仪玲
	* @created 2012-2-9 上午09:21:25
	* @description 注入SESSION工厂
	* @return void 返回类型
	*/
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		componentMapDao =  new GenericDAOHibernate<ToaComponentMap, java.lang.String>(
				sessionFactory, ToaComponentMap.class);
		Dao =  new GenericDAOHibernate<Object, java.lang.String>(
				sessionFactory, Object.class);
	}
	
	/**
	* @method getComponentMapList
	* @author 申仪玲
	* @created 2012-2-14 上午09:30:21
	* @description 得到列表控件分页对象
	* @return Page<ToaComponentMap> 返回类型
	*/
	public Page<ToaComponentMap> getComponentMapList(Page<ToaComponentMap> page ,ToaComponentMap model){		
		StringBuilder hql = new StringBuilder("from ToaComponentMap t where 1=1 ");
		if(model != null){
			if(model.getMapPrivil() != null){
				
				if(model.getMapPrivil().getPrivilName() != null && !"".equals(model.getMapPrivil().getPrivilName())){
					hql.append(" and t.mapPrivil.privilName like '%" + model.getMapPrivil().getPrivilName() + "%'"); 
				}
			}
			if(model.getMapProcess() != null){
				
				if(model.getMapProcess().getPfName()!= null && !"".equals(model.getMapProcess().getPfName())){
					hql.append(" and t.mapProcess.pfName like '%" + model.getMapProcess().getPfName() + "%'"); 
				}
			}
			if(model.getMapForm() != null){
				
				if(model.getMapForm().getTitle()!= null && !"".equals(model.getMapForm().getTitle())){
					hql.append(" and t.mapForm.title like '%" + model.getMapForm().getTitle() + "%'"); 
				}
			}
			if(model.getMapType() != null && !"".equals(model.getMapType())){
				hql.append(" and t.mapType = '" + model.getMapType() + "'"); 
			}
		}

		return componentMapDao.find(page, hql.toString());
		
		
	}
	
	/**
	* @method save
	* @author 申仪玲
	* @created 2012-2-17 上午09:24:39
	* @description 保存
	* @return void 返回类型
	*/
	public void save(ToaComponentMap model){
		
		componentMapDao.save(model);
	}
	
	public void delete(String mapIds){
		String[] ids = mapIds.split(",");
		componentMapDao.delete(ids);
	}
	
	/**
	* @method getMapById
	* @author 申仪玲
	* @created 2012-2-17 下午04:18:32
	* @description 根据主键ID获取一条记录
	* @return ToaComponentMap 返回类型
	*/
	public ToaComponentMap getMapById(String mapId){
		
		return componentMapDao.get(mapId);
		
	}
	
	/**
	* @method getPrivilList
	* @author 申仪玲
	* @created 2012-2-14 上午09:30:43
	* @description 得到使用列表控件的资源模块 
	* @return List<Object> 返回类型
	*/
	public List getPrivilList(){
		StringBuilder hql = new StringBuilder("from TUumsBasePrivil t where t.privilDescription like '%列表控件%'");
		return Dao.find(hql.toString());
		
	}
	
	/**
	* @method getEformTemplate
	* @author 申仪玲
	* @created 2012-2-14 上午09:36:43
	* @description 得到列表控件
	* @return List<Object> 返回类型
	*/
	public List getEformTemplateList(){
		StringBuilder hql = new StringBuilder("from TEFormTemplate t where t.type = 'VF'");
		return Dao.find(hql.toString());
	}
	
	/**
	* @method getProcessList
	* @author 申仪玲
	* @created 2012-2-14 上午10:00:16
	* @description 得到流程
	* @return List<Object> 返回类型
	*/
	public List getProcessList(){
		StringBuilder hql = new StringBuilder("from TwfBaseProcessfile t where 1=1");
		return Dao.find(hql.toString());
		
	}

}
