package com.strongit.oa.worklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaUser;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;

import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
@org.springframework.stereotype.Service
@Transactional
public class WorkListManager implements IWorkListManager {
	protected @Autowired IUserService userService; //注入当前用户接口
	private @Autowired IWorkflowService manager;	//工作流；
	private GenericDAOHibernate<ToaUser, java.lang.String> workDao;
	/**
	 * 注册DAO
	 * @param sessionFactory
	 * @roseuid 49366EBF0203
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		workDao = new GenericDAOHibernate<ToaUser, java.lang.String>(
				sessionFactory, ToaUser.class);
	}
	public String getUserIDByUserName(String username)
	{
		User user = userService.getUserInfoByLoginName(username);
		String userId = user.getUserId();
		if(userId == null){
			user = userService.getUserInfoByLoginName(username.toLowerCase());
		}
		return user.getUserId();
	}
	/**
	 * 获取待办工作类型和数目
	 *	@author lee李俊勇
	 *	@date Apr 24, 2010 10:44:43 AM 
	 * 	@param page
	 * 	@return
	 */
	public Map<String, String> getProccessTypeNameAndCount(Page page) {
		
		Map<String,String> type = new HashMap<String,String>();
		List<Object[]> list=page.getResult();
		List<Object[]> typeList = manager.getAllProcessTypeList();
		Object[] typeObj;
		Object[] obj;
		int k;
		for(int i=0;i<typeList.size();i++){
			typeObj=typeList.get(i);
			k=0;
			for(int j=0;list!=null && j<list.size();j++){
				obj=list.get(j);
				if(String.valueOf(typeObj[0]).equals(String.valueOf(obj[8]))){
					k+=1;
				}
			}
			type.put(typeObj[1].toString(),String.valueOf(k));
		}
		return type;
	}
}
