/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22
 * Autour: lanlc
 * Version: V1.0
 * Description：公文字号历史记录manager
 */
package com.strongit.oa.docnumber;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDocnumberHistory;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class DocNumberHistoryManager {
	 private GenericDAOHibernate<ToaDocnumberHistory,java.lang.String> docNumberHistoryDao;
	 @Autowired
	 public void setSessionFactory(SessionFactory sessionFactory) {
		 docNumberHistoryDao = new GenericDAOHibernate<ToaDocnumberHistory,String>(sessionFactory,ToaDocnumberHistory.class);
	 }
	 
	public DocNumberHistoryManager(){
	}
	
	/**
	 * 查询公文文号记录分页列表
	 * @param page
	 * @return
	 */
	public Page<ToaDocnumberHistory> getdocNumberHistoryList(Page<ToaDocnumberHistory> page,ToaDocnumberHistory model,String searchdocnhword)throws SystemException,ServiceException{
		try{
			Object[] obj = new Object[2];//创建数组,接收搜索参数
			StringBuffer hql = new StringBuffer("from ToaDocnumberHistory t where 1=1");
			int i = 0;  //统计接收参数数量，调用具体的查询方法
			if(searchdocnhword!=null && !"".equals(searchdocnhword)){
				hql = hql.append(" and (t.docnumberHistoryword like  ? or t.docnumberHistoryyear like ? or t.docnumberHistorynumber)");
				obj[i]= searchdocnhword;
				i++;
			}
			if(model.getDocnumberHistoryflag()!=null && !"".equals(model.getDocnumberHistoryflag())){
				hql = hql.append(" and t.docnumberHistoryflag = ?");
				obj[i] = model.getDocnumberHistoryflag();
				i++;
			}
			if(i==0){
				page = docNumberHistoryDao.findAll(page);
			}
			else if(i==1){
				page = docNumberHistoryDao.find(page, hql.toString(), obj[0]);
			}
			else if(i==2){
				page = docNumberHistoryDao.find(page, hql.toString(), obj[0],obj[1]);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"公文文号记录页"});
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param docnumberHistoryid
	 * @return
	 */
	public ToaDocnumberHistory getDocnumberHistory(String docnumberHistoryid)throws SystemException,ServiceException{
		try{
			return docNumberHistoryDao.get(docnumberHistoryid);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文文号"});
		}
	}
	
	/**
	 * 根据ID删除实体
	 * @param docnumberHistoryid
	 * @throws ServiceException
	 */
	public void deleteDocnumberHistory(String docnumberHistoryid)throws SystemException,ServiceException{
		try{
			docNumberHistoryDao.delete(docnumberHistoryid);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除公文文号"});
		}
	}
	
	/**
	 * 保存实体
	 * @param model
	 */
	public void saveDocnumberHistory(ToaDocnumberHistory model)throws SystemException,ServiceException{
		try{
			docNumberHistoryDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存公文文号"});
		}
	}
	
	/**
	 * 判断公文字号历史记录中是否已存在
	 * @param docnumber
	 * @return
	 */
	public boolean isExist(String docnumber)throws SystemException,ServiceException{
		try{
			boolean isexist=true;
			//传递过来的以,号分隔的组合公文字号
			String[] dochistory=docnumber.split(",");
			String hql="from ToaDocnumberHistory t where t.docnumberHistoryword=? and t.docnumberHistoryyear=? and t.docnumberHistorynumber=?";
			Object[] values=dochistory;
			List docNumberHistoty=docNumberHistoryDao.find(hql,values);
			if(!docNumberHistoty.isEmpty()){
				isexist=false;
			}else{
				isexist=true;
			}
			return isexist;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"是否已存在文号"});
		}
		
	}
}
