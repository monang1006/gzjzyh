package com.strongit.oa.survey;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSurveytableQuestion;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 问卷调查内容  问题处理类
 * @Create Date: 2009-8-8
 * @version 1.0
 */
@Service
@Transactional
public class SurveyVoteManager {

	private GenericDAOHibernate<ToaSurveytableQuestion, String> questionDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.questionDao = new GenericDAOHibernate(sessionFactory,
				ToaSurveytableQuestion.class);
	}

	/*
	 * public Page<ToaSurveytableQuestion> getQuestionPages(Page<ToaSurveytableQuestion>
	 * page,String questionSurveyId) throws SystemException, ServiceException {
	 * String hql = "from ToaSurveytableQuestion t where t.questionSurveyId=? ";
	 * page =this.questionDao.find(page, hql, new Object[]{questionSurveyId});
	 * 
	 * return page; }
	 */
	public void addQuestion(ToaSurveytableQuestion question)
			throws SystemException, ServiceException {

		try {
			this.questionDao.save(question);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

public void updateQuestion(ToaSurveytableQuestion question)
	throws SystemException, ServiceException {

   try {
	this.questionDao.update(question);
} catch (ServiceException e) {
	throw new ServiceException("find.error", new Object[] { "投票调查" });
}
}
	public void delQuestion(String questionId) throws SystemException,
			ServiceException {

		try {
			this.questionDao.delete(questionId);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
	
  public void delQuestions(List<ToaSurveytableQuestion> question)throws SystemException,
	        ServiceException {
		try {
			this.questionDao.delete(question);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
		
	}

	public ToaSurveytableQuestion getQuestion(String questionId)
			throws SystemException, ServiceException {
		try {
			return ((ToaSurveytableQuestion) this.questionDao.get(questionId));
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	public synchronized int getMaxNumber() throws SystemException, ServiceException {

		int maxNumber = 1;
		try {
			String hql = " select max(t.questionNumber) from ToaSurveytableQuestion t ";
			List list = this.questionDao.find(hql);
			if (null != list.get(0)) {
				maxNumber = Integer.parseInt(list.get(0).toString()) + 1;
			}
			return maxNumber;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}

	}

	public String getQuestionId(int QuestionNumber) throws SystemException,
			ServiceException {

		String questionId = null;
		try {
			String hql = " select t.questionId from ToaSurveytableQuestion t where t.questionNumber='"
					+ QuestionNumber + "'";
			List list = this.questionDao.find(hql);
			if (null != list.get(0)) {
				questionId = list.get(0).toString();
			}
			return questionId;
		} catch (Exception e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}

	}
    /**
     * 
     * @param questionNumber
     * @param questionName
     * @throws SystemException
     * @throws ServiceException
     */
	public void updateQuestionName(int questionNumber, String questionName)
			throws SystemException, ServiceException {
		try {
			String hql = "update   ToaSurveytableQuestion t set t.questionName ='"
					+ questionName
					+ "' where t.questionNumber='"
					+ questionNumber + "'";

			this.questionDao.createQuery(hql).executeUpdate();

		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	public void updateQuestionTrue(int questionNumber, String questionTrue)
			throws SystemException, ServiceException {
		try {
			String hql = "update   ToaSurveytableQuestion t set t.questionTrue ='"
					+ questionTrue
					+ "' where t.questionNumber='"
					+ questionNumber + "'";

			this.questionDao.createQuery(hql).executeUpdate();

		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
    /**
     * 读取问题集合,并排序
     * @param page
     * @param sort
     * @param questionSurveyId
     * @return
     * @throws SystemException
     * @throws ServiceException
     */
	public Page<ToaSurveytableQuestion> getQuestionPages(
			Page<ToaSurveytableQuestion> page, String[] sort,
			String questionSurveyId) throws SystemException, ServiceException {
		String hql = "from ToaSurveytableQuestion t where t.questionSurveyId=? ";
		page = this.questionDao.find(page, hql,
				new Object[] { questionSurveyId });
		if (sort != null) {
			List<ToaSurveytableQuestion> list = page.getResult();
			List<ToaSurveytableQuestion> list2 = new ArrayList<ToaSurveytableQuestion>();
			for (int i = 0; i < sort.length; i++) {
				for (ToaSurveytableQuestion model : list) {
					if (Integer.parseInt(sort[i]) == model.getQuestionNumber()) {
						list2.add(model);
						// list.iterator().remove(model);
					}
				}
			}
			page.setResult(list2);
		}
		return page;
	}


	
	/**
	 *  数组倒序操作
	 * @param val
	 * @return
	 */
	public String[] reverse(String[] val) {
		int size = 0;
		int half = 0;
		String temp = null;
		if (null != val) {
			size = val.length;
			half = size / 2;
			for (int i = 0, j = size - 1; i < half; i++, j--) {
				temp = val[i];
				val[i] = val[j];
				val[j] = temp;
			}
		}
		return val;
	}

}
