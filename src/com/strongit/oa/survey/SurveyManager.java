package com.strongit.oa.survey;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSurveyUnrepeat;
import com.strongit.oa.bo.ToaSurveytableSurvey;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 调查管理 处理类
 * @Create Date: 2009-8-8
 * @version 1.0
 */
@Service
@Transactional
public class SurveyManager {
	
//	统一用户服务
	private IUserService user;

	private GenericDAOHibernate<ToaSurveytableSurvey, String> surveyDao;
	private GenericDAOHibernate<ToaSurveyUnrepeat, String> unrepeatDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.surveyDao = new GenericDAOHibernate<ToaSurveytableSurvey, String>(sessionFactory,
				ToaSurveytableSurvey.class);
		
		this.unrepeatDao = new GenericDAOHibernate<ToaSurveyUnrepeat, String>(sessionFactory,
				ToaSurveyUnrepeat.class);
	}

	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	/**
	 * 查询
	 * @param page
	 * @param surveyName
	 * @param surveyStartTime
	 * @param surveyEndTime
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSurveytableSurvey> getSelectSurveyPages(Page<ToaSurveytableSurvey> page, String surveyName,
			Date surveyStartTime,Date surveyEndTime)  throws SystemException,ServiceException {
		Object[] obj=new Object[4];
		int i = 0;
		String hql = "from ToaSurveytableSurvey t where 1=1 ";
		
		//名称
		if(surveyName!=null&&!"".equals(surveyName)&&!"null".equals(surveyName)){
		      hql+= " and t.surveyName like ?"; 
		   	  obj[i]="%"+surveyName+"%";
		   	  i++;
		}

		//时间
//		if(surveyStartTime!=null&&!"".equals(surveyStartTime)&&!"null".equals(surveyStartTime)){
//			hql+=" and t.surveyStartTime =?";
//		    obj[i]=surveyStartTime;
//			  i++;
//		}
//		if(surveyEndTime!=null&&!"".equals(surveyEndTime)&&!"null".equals(surveyEndTime)){
//			hql+=" and t.surveyEndTime =?";
//		    obj[i]=surveyEndTime;
//			  i++;
//		}
		
		if((surveyStartTime!=null&&!"".equals(surveyStartTime)&&!"null".equals(surveyStartTime))
				&&(surveyEndTime!=null&&!"".equals(surveyEndTime)&&!"null".equals(surveyEndTime))){//开始时间结束时间都不为空 则查找在这个时间段内的投票
			hql+=" and t.surveyStartTime > ?  and t.surveyEndTime <? ";
			obj[i]=surveyStartTime;
			i++;
			obj[i]=surveyEndTime;
			i++;
		}else if(surveyStartTime!=null&&!"".equals(surveyStartTime)&&!"null".equals(surveyStartTime)){
			hql+=" and t.surveyStartTime >?";
		    obj[i]=surveyStartTime;
			  i++;
		}else if(surveyEndTime!=null&&!"".equals(surveyEndTime)&&!"null".equals(surveyEndTime)){
			hql+=" and t.surveyEndTime <?";
		    obj[i]=surveyEndTime;
			  i++;
		}
		
		Object[] param=new Object[i];
		for(int k=0,t=0;k<obj.length;k++){
			if(obj[k]!=null){
				param[t]=obj[k];
				t++;
			}
		}
		
		hql=hql+"  order by t.surveyEndTime DESC "; 
		return page=surveyDao.find(page, hql, param);
	}
	
	public Page<ToaSurveytableSurvey> getSurveyPages(
			Page<ToaSurveytableSurvey> page) throws SystemException,
			ServiceException {
		try {
			String hql = "from ToaSurveytableSurvey t order by t.surveyId DESC";
			page = this.surveyDao.find(page, hql, new Object[0]);
			return page;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	/**
	 * 通过审核的投票结果集
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSurveytableSurvey> getSurveySuccPages(
			Page<ToaSurveytableSurvey> page) throws SystemException,
			ServiceException {
		try {
			String hql = "from ToaSurveytableSurvey t where t.state='1' order by t.surveyId DESC";
			page = this.surveyDao.find(page, hql, new Object[0]);
			return page;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
    /**
     * 读取调查对象
     * @param surveyId
     * @return
     * @throws SystemException
     * @throws ServiceException
     */
	public ToaSurveytableSurvey getSurvey(String surveyId)
			throws SystemException, ServiceException {
		try {
			return ((ToaSurveytableSurvey) this.surveyDao.get(surveyId));
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
    /**
     * 添加
     * @param survey
     * @throws SystemException
     * @throws ServiceException
     */
	public void addSurvey(ToaSurveytableSurvey survey) throws SystemException,
			ServiceException {
		try {
			this.surveyDao.save(survey);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
    
	/**
	 * 删除 并同时删除用户投票记录
	 * @param surveyId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delSurvey(String surveyId) throws SystemException,
			ServiceException {
		try {
			String[] ids = surveyId.split(",");

			for (int i = 0; i < ids.length; ++i) {
				this.surveyDao.delete(ids[i]);
				/*this.surveyDao
				.executeJdbcUpdate("delete  from T_OA_SURVEY_UNREPEAT t where t.UNR_SURVEY_ID='"
						+ ids[i]+"'");*/
				this.unrepeatDao.createQuery("delete   ToaSurveyUnrepeat t where t.unrSurveyId='"+ ids[i]+"'").executeUpdate();
			}
		} catch (ServiceException e) {
			throw new ServiceException("delete.error", new Object[] { "投票删除" });
		}
	}
    /**
     * 修改
     * @param survey
     * @throws SystemException
     * @throws ServiceException
     */
	public void editSurvey(ToaSurveytableSurvey survey) throws SystemException,
			ServiceException {
		try {
			this.surveyDao.update(survey);
			/*if("0".equals(survey.getSurveyUnRepeat())||"".equals(survey.getSurveyUnRepeat())||null==survey.getSurveyUnRepeat())
			{
				this.surveyDao
				.executeJdbcUpdate("delete  from T_OA_SURVEY_UNREPEAT t where t.UNR_SURVEY_ID='"
						+ survey.getSurveyId()+"'");
			}*/
			if("0".equals(survey.getSurveyUnRepeat())||"".equals(survey.getSurveyUnRepeat())||null==survey.getSurveyUnRepeat())
			{
				this.unrepeatDao
				.createQuery("delete  ToaSurveyUnrepeat t where t.unrSurveyId='"
						+ survey.getSurveyId()+"'").executeUpdate();
			}
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
	
	/**
	 * 问卷提交的次数
	 * @param surveyId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public synchronized void surveyCount(String  surveyId) throws SystemException,
			ServiceException {
		try {
			this.surveyDao.createQuery("update  ToaSurveytableSurvey t set t.surveyCount=surveyCount+1  where t.surveyId='"+surveyId+"'").executeUpdate();
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
	/**
	 * 添加该用户投票记录
	 * @param surveyId
	 * @param userId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public  void surveyUnrepeat(String surveyId)
			throws SystemException, ServiceException {
		try {
			String userId = user.getCurrentUser().getUserId();
			ToaSurveyUnrepeat vo = new ToaSurveyUnrepeat();
			vo.setUnrSurveyId(surveyId);
			vo.setUnrUserId(userId);
			ToaSurveytableSurvey oo = getSurvey(surveyId);
			if("1".equals(oo.getSurveyUnRepeat())){
			this.unrepeatDao.save(vo);
			}
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
	/**
	 * 检验用户是否以参与投票
	 * @param surveyId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String surveyUnrepeatCheck(String surveyId) throws SystemException,
			ServiceException {
		String flag = "false";
		try {

			String userId = user.getCurrentUser().getUserId();
			ToaSurveytableSurvey oo = getSurvey(surveyId);
			if ("1".equals(oo.getSurveyUnRepeat())) {
				List list = this.unrepeatDao
						.find("from ToaSurveyUnrepeat t where t.unrSurveyId='"
								+ surveyId
								+ "' and t.unrUserId='"
								+ userId
								+ "'");
					if (null!=list&&0!=list.size()) {
						flag = "true";
					}
			}
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
		return flag;
	}

	/**
	 * 获取当前用户
	 */
	public User getCurrentUser()throws SystemException{
		return user.getCurrentUser();
	}
	
	/**
	 * 读取问卷中问题的排序号
	 * 
	 * @param surveyId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getQusSort(String surveyId) throws SystemException,
			ServiceException {

		String qussortId = null;
		try {
			String hql = " select surveyQusSort from ToaSurveytableSurvey t where t.surveyId='"
					+ surveyId + "'";
			List list = this.surveyDao.find(hql);
			if (null!=list&&null != list.get(0)) {
				qussortId = list.get(0).toString();
			}
			return qussortId;
		} catch (Exception e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}

	}
}
