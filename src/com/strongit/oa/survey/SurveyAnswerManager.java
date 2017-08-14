package com.strongit.oa.survey;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSurveytableAnswer;
import com.strongit.oa.bo.ToaSurveytableQuestion;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 问卷调查内容  答案处理类
 * @Create Date: 2009-8-8
 * @version 1.0
 */
@Service
@Transactional
public class SurveyAnswerManager {

	private GenericDAOHibernate<ToaSurveytableAnswer, String> answerDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.answerDao = new GenericDAOHibernate(sessionFactory,
				ToaSurveytableAnswer.class);
	}

	public void addAnswer(ToaSurveytableAnswer answer) throws SystemException,
			ServiceException {

		try {
			this.answerDao.save(answer);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
    /**
     * 删除
     * @param answerNumber
     * @throws SystemException
     * @throws ServiceException
     */
	public void delAnswer(String answerNumber) throws SystemException,
			ServiceException {

		try {
			StringBuffer hql = new StringBuffer();
		/*	hql.append("delete from  T_OA_ANSWER t where t.ANSWER_NUMBER='")
					.append(answerNumber).append("'");*/
			hql.append(" delete  ToaSurveytableAnswer t where t.answerNumber='")
			.append(answerNumber).append("'");
			//this.answerDao.executeJdbcUpdate(hql.toString());
			this.answerDao.createQuery(hql.toString()).executeUpdate();

		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}
	
	public void delAnswers(List<ToaSurveytableAnswer> answer)throws SystemException,
    ServiceException {
try {
	 this.answerDao.delete(answer);
} catch (ServiceException e) {
	throw new ServiceException("find.error", new Object[] { "投票调查" });
}

}
    /**
     *  更新答案
     * @param answerNumber
     * @param answerValue
     * @param answerName
     * @throws SystemException
     * @throws ServiceException
     */
	public void updateAnswerName(String answerNumber, String answerValue,
			String answerName) throws SystemException, ServiceException {

		try {
			StringBuffer hql = new StringBuffer();
			/*hql.append("update  T_OA_ANSWER t set t.answer_name='").append(
					answerName).append("', t.ANSWER_VALUE='").append(
					answerValue).append("' where t.ANSWER_NUMBER='").append(
					answerNumber).append("'");
			this.answerDao.executeJdbcUpdate(hql.toString());*/
			hql.append("update  ToaSurveytableAnswer t set t.answerName='").append(
					answerName).append("', t.answerValue='").append(
					answerValue).append("' where t.answerNumber='").append(
					answerNumber).append("'");
			this.answerDao.createQuery(hql.toString()).executeUpdate();

		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	/**
	 * 更新统计数
	 * 
	 * @param answerNumber
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public synchronized void updateAnswerCount(String[] answerNumber)
			throws SystemException, ServiceException {

		try {
			for (int i = 0; i < answerNumber.length; i++) {
				this.answerDao
						.createQuery("update  ToaSurveytableAnswer t set t.answerCount=answerCount+1  where t.answerNumber='"
								+ answerNumber[i] + "'").executeUpdate();
			}

		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	public Page<ToaSurveytableAnswer> getAnswerPages(
			Page<ToaSurveytableAnswer> page, int answerQueNumber)
			throws SystemException, ServiceException {
		String hql = "from ToaSurveytableAnswer t where t.answerQueNumber=? ";
		page = this.answerDao.find(page, hql, new Object[] { answerQueNumber });
		return page;
	}

	/**
	 * 提供一个该问题所对应的答案的最大ID
	 * @param answerQueNumber --问题ID
	 * @return --返回该问题答案最大的ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getMaxNumber(int answerQueNumber) throws SystemException,
			ServiceException {

		int maxNumber = 1;
		try {
			String hql = " select max(t.answerSortid) from ToaSurveytableAnswer t where t.answerQueNumber=? ";
			List list = this.answerDao.find(hql,
					new Object[] { answerQueNumber });
			if (null != list.get(0)) {
				maxNumber = Integer.parseInt(list.get(0).toString()) + 1;
			}
			return maxNumber;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}

	}

	/*
	 * public String getQuestionId(int QuestionNumber )throws SystemException,
	 * ServiceException {
	 * 
	 * String questionId=null; try { String hql = " select question_id from
	 * t_oa_surveytable_question t where
	 * t.question_number='"+QuestionNumber+"'"; ResultSet res =
	 * this.answerDao.executeJdbcQuery(hql); if(res.next()) { questionId
	 * =res.getString("question_id"); } return questionId; } catch (Exception e) {
	 * throw new ServiceException("find.error", new Object[] { "投票调查" }); }
	 *  }
	 */

}
