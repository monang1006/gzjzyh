package com.strongit.oa.formconfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TefTemplateConfig;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class ConfigManager{
	
	private GenericDAOHibernate<TefTemplateConfig, java.lang.String> configDao;

	private IWorkflowService workflow;	//	工作流服务

	private IEFormService eform;		//	电子表单服务
	
	private IUserService user;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		configDao = new GenericDAOHibernate<TefTemplateConfig, java.lang.String>(
				sessionFactory, TefTemplateConfig.class);
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Jan 18, 2010 4:40:18 PM
	 * Desc: 	获取当前用户
	 * param:
	 */
	public User getUser(){
		return user.getCurrentUser();
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Jan 18, 2010 12:04:30 PM
	 * Desc:	根据流程类型Id获取当前用户所拥有的所有启动表单
	 * param:
	 */
	public List<EForm> getRelativeFormByProcessType(String processTypeId) throws SystemException,ServiceException{
		try {
			List listObj = workflow.getRelativeFormByProcessType(processTypeId);
			List<EForm> formListbyWFType = new ArrayList<EForm>();
			List<EForm> formTemplateList = eform.getFormTemplateList(null);
			for(int i=0;i<listObj.size();i++){
				String formId = String.valueOf(listObj.get(i));
				for(Iterator<EForm> it=formTemplateList.iterator();it.hasNext();){
					EForm ef = it.next();
					String formTemplateId = ef.getId().toString();
					if(formTemplateId.equals(formId)){
						EForm tempEForm = new EForm();
						tempEForm.setId(Long.parseLong(formId));
						tempEForm.setTitle(ef.getTitle());
						formListbyWFType.add(tempEForm);
					}
				}
			}
			
			EForm[] EFormId = new EForm[formListbyWFType.size()];
			for(int j=0;j<formListbyWFType.size();j++){
				int n=0;
				int p = 0;
				EForm eformPrev = formListbyWFType.get(j);
				for(int k=formListbyWFType.size()-1;k>=0;k--){
					EForm eformNext = formListbyWFType.get(k);
					if(eformPrev.getId().intValue() == eformNext.getId().intValue()){
						n = n+1;
					}
				}
				if(n>1){
					EFormId[p] = eformPrev;
					p = p+1;
				}
			}
			for(int m=0;m<EFormId.length;m++){
				formListbyWFType.remove(EFormId[m]);
			}
			
			return formListbyWFType;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取表单"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Jan 18, 2010 4:36:12 PM
	 * Desc:
	 * param:
	 */
	public List<TefTemplateConfig> getConfigList(String userId,String processTypeId)throws SystemException,ServiceException{
		try{
			String hql="from TefTemplateConfig t where t.userId=? and t.formType=? order by t.senquenceNum";
			List<TefTemplateConfig> list=configDao.find(hql, userId,processTypeId);
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"删除配置"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Jan 18, 2010 4:02:42 PM
	 * Desc:	删除配置
	 * param:
	 */
	public void deleteConfig(String userId,String processTypeId)throws SystemException,ServiceException{
		try{
			List<TefTemplateConfig> list=this.getConfigList(userId, processTypeId);
			configDao.delete(list);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"删除配置"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Jan 18, 2010 4:11:10 PM
	 * Desc:	配置表单
	 * param:
	 */
	public String configForm(String sequenceStr,String processTypeId)throws SystemException,ServiceException{
		String retValue="0";
		try{
			String userId=this.getUser().getUserId();
			this.deleteConfig(userId, processTypeId);//删除原来的配置。
			String[] sequenceArr=sequenceStr.split(",");	
			String[] tempSeq;
			TefTemplateConfig config;
			for(int i=0;i<sequenceArr.length;i++){
				tempSeq=sequenceArr[i].split("_");
				config=new TefTemplateConfig();
				config.setFormId(new Long(tempSeq[0]));
				config.setFormType(processTypeId);
				config.setSenquenceNum(tempSeq[1]);
				config.setUserId(userId);
				configDao.save(config);
			}
			retValue="1";
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"配置表单"});
		}
		return retValue;
	}

	@Autowired
	public void setWorkflow(IWorkflowService workflow) {
		this.workflow = workflow;
	}

	@Autowired
	public void setEform(IEFormService eform) {
		this.eform = eform;
	}
	
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
}
