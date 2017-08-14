package com.strongit.oa.suggestion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ApprovalSuggestionService implements IApprovalSuggestionService {
	
	private GenericDAOHibernate<ToaApprovalSuggestion, java.lang.String> appSuggestionDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		appSuggestionDao=new GenericDAOHibernate<ToaApprovalSuggestion, java.lang.String>(sessionFactory,ToaApprovalSuggestion.class);
	}

	public void delete(String suggestionCode, OALogInfo... loginfos)
			throws SystemException, ServiceException {
		try {
			appSuggestionDao.delete(suggestionCode);
		} catch (Exception e) {
			 throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"删除审批意见"});
		}

	}

	public List<ToaApprovalSuggestion> getAppSuggestionListByUserId(
			String userId, OALogInfo... loginfos) throws SystemException,
			ServiceException {
		try {
			List<Object> list =new ArrayList<Object>();
			String hql =" from ToaApprovalSuggestion t where 1=1";
			if (userId!=null&&!userId.equals("")) {
				hql=hql+" and suggestionUserid =?";
				list.add(userId);
			}
			hql=hql+" order by t.suggestionSeq asc,t.suggestionDate asc,t.suggestionContent desc";
			if(list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);
				}
				return appSuggestionDao.find(hql.toString(), obj);
			}else {
				return appSuggestionDao.find(hql.toString());
			}
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"获取当前用户所有审批意见"});
		}
	}

	public Page<ToaApprovalSuggestion> getAppSuggestionPage(Page<ToaApprovalSuggestion> page,String userId,Date startDate,Date endDate,ToaApprovalSuggestion model,OALogInfo ...logInfos)throws SystemException,ServiceException{
		try {
			List<Object> list =new ArrayList<Object>();
			StringBuffer hql = new StringBuffer("from ToaApprovalSuggestion t where t.suggestionUserid=?");
			list.add(userId);
			if(model.getSuggestionContent()!=null&&!model.getSuggestionContent().equals("")){
				hql.append(" and t.suggestionContent like ? ");
				list.add("%"+model.getSuggestionContent()+"%");
			}
			if(startDate!=null){
				hql.append(" and t.suggestionDate>= ?");
				list.add(startDate);
			}
			if(endDate!=null){
				hql.append(" and t.suggestionDate<= ?");
				list.add(endDate);
			}
			Object[] objs = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				objs[i] = list.get(i);
			}
			hql.append(" order by t.suggestionSeq asc,t.suggestionDate asc,t.suggestionContent desc");
			page=appSuggestionDao.find(page, hql.toString(),objs);
			return page;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "分页搜索审批意见" });
		}
	}
	
	
	public ToaApprovalSuggestion getModelById(String suggestionCode,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			return appSuggestionDao.get(suggestionCode);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据ID获取对象"});
		}
	}

	public void saveSuggestion(ToaApprovalSuggestion approvalSuggestion,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			appSuggestionDao.save(approvalSuggestion);
			appSuggestionDao.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"保存审批意见对象"});
		}

	}

	/**
	 * 保存
	 */
	public void saveSuggestion(String userId, String content,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			if(userId!=null&&!userId.equals("")&&content!=null&&!content.equals("")){
				int num=IsHasSuggestion(userId,content,"");
				if(num>0){
					
				}else {					
					ToaApprovalSuggestion model=new ToaApprovalSuggestion();
					model.setSuggestionContent(content);
					model.setSuggestionUserid(userId);
					model.setSuggestionDate(new Date());
					appSuggestionDao.save(model);
				}
				
			}
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"保存审批意见对象"});
		}

	}
	
	/**
	 * 判断审批意见是否存在
	 */
	public int IsHasSuggestion(String userId,String content,String suggestionCode)throws SystemException,ServiceException{
		try {
			Object[] objs = new Object[2];
			String hql="select count(*) from ToaApprovalSuggestion t where  t.suggestionUserid=? and t.suggestionContent=?";
			objs[0]=userId;
			objs[1]=content;
			if(suggestionCode!=null&&!suggestionCode.equals("")&&!suggestionCode.equals("null")){
				hql=hql+" and t.suggestionCode not in ('"+suggestionCode+"')";
			}
			
			int num=Integer.parseInt(appSuggestionDao.findUnique(hql,objs).toString());
			return num;			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查询审批意见是否存在"});
		}
	}

}
