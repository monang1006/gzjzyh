package com.strongit.oa.vote;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVoteAnswer;
import com.strongit.oa.bo.TOaVoteQuestion;
import com.strongit.oa.vote.util.VoteConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author piyu
 * Jun 11, 2010
 *
 */
@Service
@Transactional
public class QuestionManager {
	private GenericDAOHibernate<TOaVoteQuestion, String> questionDao;
	protected static final Log logger = LogFactory.getLog(QuestionManager.class);
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.questionDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteQuestion.class);
	}
	/**
	 * 新建问题
	 */
	public synchronized  void addQuestion(TOaVoteQuestion question){
		try {
			question.setShowno(getCount(question.getVote().getVid())+1);//设置顺序号
			if(question.getMaxRow()<1){
				question.setMaxRow(1);
			}
			if(question.getIsRequired()==null){
				question.setIsRequired("N");
			}
			questionDao.insert(question);
		} catch (Exception e) {
			logger.error("新建问题",e);
			throw new ServiceException("find.error", new Object[] { "新建问题" });
		}
	}
	/**
	 * 获取问卷下的问题数量
	 */
	@Transactional(readOnly=true)
	public int getCount(String vid){
		String hql="select max(qt.showno) from TOaVoteQuestion qt where qt.vote.vid=?";
		Object []params=new Object[]{vid};
		List result=questionDao.createQuery(hql,params).list();
		int count=1;
		if(result.get(0)!=null){
			count=Integer.parseInt(result.get(0).toString());
		}
		
		return count;
	}
	@Transactional(readOnly=true)
	public TOaVoteQuestion getQuestion(String qid){
		TOaVoteQuestion question=null;
		try {
			question=questionDao.get(qid);
		} catch (Exception e) {
			logger.error("获取问题",e);
			throw new ServiceException("find.error", new Object[] { "获取问题" });
		}
		return question ;
	}
	/**
	 * 更新问题
	 */
	public void updateQuestion(TOaVoteQuestion question){
		try {
			StringBuffer hql=new StringBuffer("update TOaVoteQuestion qt set ");
			List<Object> params=new ArrayList<Object>();
			if(question.getIsRequired()!=null)
			{
			  hql.append(" qt.isRequired=? ,");
			  params.add(question.getIsRequired());
			}
			if(question.getMaxRow()>0)
			{
			  hql.append(" qt.maxRow=? ,");
			  params.add(question.getMaxRow());
			}
			if(question.getPicSize()!=null)
			{
			  hql.append(" qt.picSize=? ,");
			  params.add(question.getPicSize());
			}
			if(question.getShowno()>0)
			{
			  hql.append(" qt.showno=? ,");
			  params.add(question.getShowno());
			}
			if(question.getTableHeader()!=null&&question.getTableHeader().length()>0)
			{
			  int tablehead_num=question.getTableHeader().split(VoteConst.table_separator).length;
			  int num=1;
			  int start=0;
			  while(question.getTableHeader().indexOf(VoteConst.table_separator, start)>-1){
				  //计算=的数量
				  start=question.getTableHeader().indexOf(VoteConst.table_separator, start)+1;
				  num++;
			  }
			  if(num!=tablehead_num){
				  //存在：表头1=表头2==表头4，数据非法，则不保存
				  return ;
			  }
			 
			  hql.append(" qt.tableHeader=? ,");
			  params.add(question.getTableHeader());
			}
			if(question.getTableisonly()!=null&&question.getTableisonly().length()==1)
			{
			  hql.append(" qt.tableisonly=? ,");
			  params.add(question.getTableisonly());
			}
			if(question.getTitle()!=null)
			{
			  hql.append(" qt.title=? ,");
			  params.add(question.getTitle());
			}
			
			if(params.size()==0){
				return ;
			}else{
				hql=hql.delete(hql.lastIndexOf(","),hql.lastIndexOf(",")+1);
			}
			hql.append(" where qt.qid=? ");	
			params.add(question.getQid());;
			
			Query query=questionDao.createQuery(hql.toString(),params.toArray(new Object[params.size()]));
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("更新问题",e);
			throw new ServiceException("find.error", new Object[] { "更新问题" });
		}
	}
		
	/**
	 * 删除问题
	 */
	public void delQuestion(String qid){
		try {
			String hql="delete from TOaVoteAnswer answer where answer.question.qid=?";
	        Object[]params=new Object[]{qid}; 
			questionDao.createQuery(hql,params).executeUpdate();
			
			questionDao.delete(qid);
		} catch (Exception e) {
			logger.error("删除问题",e);
			throw new ServiceException("find.error", new Object[] { "删除问题" });
		}
	}
	/**
	 * 分页查询调查问题
	 */
	@Transactional(readOnly=true)
	public Page<TOaVoteQuestion> getQuestionPage(Page<TOaVoteQuestion> page, String hql ,Object[]params)  throws SystemException,ServiceException { 
		try {
			page=questionDao.find(page, hql, params);
		} catch (Exception e) {
			logger.error("问题查询",e);
			throw new ServiceException("find.error", new Object[] { "问题查询" });
		}
		return page;
	}
	/**
	 *  查询问卷下的所有问题,编辑问卷内容使用
	 */
	@Transactional(readOnly=true)
	public List<TOaVoteQuestion> loadQuestion(String vid,String order){
		//showno越大，显示顺序越前。
		//编辑问卷内容时，页面JS采用插入法加载问题，所以此处要asc
		String hql=String.format("from TOaVoteQuestion qt where qt.vote.vid=? order by qt.showno %s",order);
		Object[]params=new Object[]{vid};
		return questionDao.find(hql, params);
	}
	
	/**
	 *  查询客观题问题下的所有答案,按照显示顺序加载
	 */
	@Transactional(readOnly=true)
	public List<TOaVoteAnswer> loadAnswer(String qid){
		//showno越大，显示顺序越前
		String hql="from TOaVoteAnswer answer where answer.question.qid=? order by answer.showno asc";
		Object[]params=new Object[]{qid};
		return questionDao.find(hql, params);
	}
	
	/**
	 * 重新计算问卷下的问题顺序号
	 */
	public void updateSort(String ques_sort){
		String []ques=ques_sort.split(",");
		TOaVoteQuestion question=new TOaVoteQuestion();
		try{
			for(int i=0;i<ques.length;i++){
				question.setQid(ques[i]);
				question.setShowno(ques.length-i);
				updateQuestion(question);
			}
		}catch (Exception e) {
			logger.error("更新问题顺序号",e);
			throw new ServiceException("find.error", new Object[] { "更新问题顺序号" });
		}
	}
	
}
