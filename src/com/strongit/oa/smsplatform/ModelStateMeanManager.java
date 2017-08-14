package com.strongit.oa.smsplatform;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaModuleStateMean;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class ModelStateMeanManager {
	private GenericDAOHibernate<ToaModuleStateMean,String> modelStateMeanDao;
	
	private Log logger = LogFactory.getLog(ModelStateMeanManager.class);
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		modelStateMeanDao = new GenericDAOHibernate<ToaModuleStateMean,String>(sessionFactory,ToaModuleStateMean.class);
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 9, 20099:49:52 AM
	 * @desc: 进行批量插入系统默认含义表
	 * @param parent	模块对象
	 * @param means		含义
	 * @param state		对应状态字段
	 * @return boolean	是否成功
	 */
	public boolean saveObj(ToaBussinessModulePara parent,String[] means ,String[] state){
		try{
			for(int i=0;i<means.length;i++){
				ToaModuleStateMean obj=new ToaModuleStateMean();
				obj.setModuleStateFlag(state[i]);
				obj.setModuleStateMean(means[i]);
				obj.setToaBussinessModulePara(parent);
				save(obj);
			}
			return true;
		}catch(Exception e){
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 9, 20099:56:44 AM
	 * @desc: 对状态位含义对象进行保存
	 * @param obj	所需要保存对象
	 * @return boolean
	 */
	public boolean save(ToaModuleStateMean obj){
		try{
			modelStateMeanDao.save(obj);
			return true;
		}catch(Exception e){
			logger.error(e);
			return false;
		}
	}
	
	/**
	 * @author：于宏洲
	 * @time：Apr 9, 20099:10:30 PM
	 * @desc: 根据父ID进行批量删除
	 * @param parentId
	 * @return boolean
	 */
	public boolean deleteObjByPar(String parentId){
		try{
			List<ToaModuleStateMean> list=modelStateMeanDao.find("from ToaModuleStateMean as mean where mean.toaBussinessModulePara.bussinessModuleId=?",parentId);
			modelStateMeanDao.delete(list);
			return true;
		}catch(Exception e){ 
			return false;
		}
	}
	/**
	 * @author：于宏洲
	 * @time：Apr 9, 20099:11:52 PM
	 * @desc: 根据父ID进行查询
	 * @param parentId
	 * @return List<ToaModuleStateMean>
	 */
	public List<ToaModuleStateMean> getObjsByPar(String parentId){
		List<ToaModuleStateMean> list=modelStateMeanDao.find("from ToaModuleStateMean as mean where mean.toaBussinessModulePara.bussinessModuleCode=?",parentId);
		return list;
	}
}
