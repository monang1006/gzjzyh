package com.strongit.oa.vote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.TOaVoteAnswer;
import com.strongit.oa.bo.TOaVoteAnswerLog;
import com.strongit.oa.bo.TOaVoteLog;
import com.strongit.oa.bo.TOaVoteQuestion;
import com.strongit.oa.bo.TOaVoteSMS;
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
public class AnswerManager {
	private GenericDAOHibernate<TOaVoteAnswer, String> answerDao;
	private GenericDAOHibernate<TOaVoteLog, String> logDao;
	private GenericDAOHibernate<TOaVoteAnswerLog, String> answerlogDao;
	@Autowired private TargetManager targetmgr;
	@Autowired private VoteLogManager votelogmgr ;
	@Autowired private SMSCodeManager smscodemg;//问题编号管理器
	protected static final Log logger = LogFactory.getLog(AnswerManager.class);
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.answerDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteAnswer.class);
		this.logDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteLog.class);
		this.answerlogDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteAnswerLog.class);
	}
	public TOaVoteAnswer getAnswer(String aid){
		TOaVoteAnswer answer=answerDao.get(aid);
		return answer;
	}
		
	/**
	 * 短信提交调查问卷
	 * @param mobile:手机号
	 * @param sms_submit:手机回复的五位码
	 * return 是否成功
	 */
	public boolean submitVoteBySMS(String mobile,String username, String sms_submit){
		try{
		String msg=null;
		if(sms_submit==null||sms_submit.length()<5){
			msg="答案格式错误";
			logger.info(String.format("短信参与调查%s:",msg));
			return false;
		}
		String code=sms_submit.substring(1,4).trim();//问题编号
		String qid=null;
		TOaVoteSMS  smscode=smscodemg.getByCode(code);
		if(smscode!=null){
			qid=smscode.getQid();
		}else{
			msg="问题编号不存在";
			logger.info(String.format("短信参与调查%s:",msg));
			return false;
		}
		
		boolean isRepeated=false;//限制重复参与
		TOaVote vote=new TOaVote();
		String hql="select vote.vid,vote.isRepeated from TOaVoteQuestion question ,TOaVote vote where vote.vid=question.vote.vid and question.qid=?";
		Object []params=new Object[]{qid};
		List<Object[]> result=answerDao.createQuery(hql,params).list();
		if(result!=null&&result.size()>0){
			vote.setVid(result.get(0)[0].toString());
			isRepeated="Y".equals(result.get(0)[1].toString());
		}
//		if(!targetmgr.isTarget(vote.getVid(), null, mobile)){
//			//不是调查对象，返回
//			return false;
//		}
		if(isRepeated){
			//限制重复
			if(votelogmgr.existLog(vote.getVid(), null, mobile)){
				//已经参与过投票，返回
				return false;
			}
		}
		
		String showno=sms_submit.substring(4,5);
		TOaVoteLog log=new TOaVoteLog();
		log.setMobile(mobile);
		log.setVote_date(new Date());
		log.setVote(vote);
		log.setUsername(username);
		this.submitAnswerBySMS(qid,Integer.parseInt(showno),log);
		}catch(Exception e){
			logger.error("手机短信参与投票",e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 手机短信参与问题
	 */
	public void submitAnswerBySMS(String qid,int showno,TOaVoteLog log){
		String hql="update TOaVoteAnswer answer set answer.count=answer.count+1 where answer.question.qid=? and answer.showno=?";
		Object[]params=new Object[]{qid,showno};
		try{
			answerDao.createQuery(hql,params).executeUpdate();
			logDao.insert(log);
		}catch (Exception e) {
			logger.error("短信参与调查",e);
			throw new ServiceException("find.error", new Object[] { "问题回答" });
		}
	}
	
	/**
	 * 网页参与问题,并记录问卷参与信息和回答详情
	 * isRealname:如果true，记录用户回答的详情
	 */
	public void submitAnswer(HttpServletRequest request,TOaVoteLog log,boolean isRealname){
		try{
		List<TOaVoteAnswer> list_answer=new ArrayList<TOaVoteAnswer>();
		TOaVoteAnswer answer=null;
		Map map=request.getParameterMap();
		Set<String> qids = map.keySet();
		String []content=null;
		for (String qid : qids) { 
            if(qid.equals("vote.vid")){
            	continue;
            }
			content = (String[])map.get(qid); 
			if(content.length>1){
				//多选题
				for(int i=0;i<content.length;i++){
					if(content[i].length()<1){
						continue;
					}
					answer=new TOaVoteAnswer();
					answer.setAid(content[i]);
					list_answer.add(answer);
				}
			}else{
				answer=new TOaVoteAnswer();
				if(qid.startsWith("text_")){
					//文本框问题
					String text=content[0].trim();
					if(text.length()<1){
						continue;
					}
					TOaVoteQuestion question=new TOaVoteQuestion();
					question.setQid(qid.substring(5));
				    answer.setQuestion(question) ;	
				    answer.setContent(text);
				}else if(qid.startsWith("textarea_")){
					//文本域问题
					String textarea=content[0].trim();
					if(textarea.length()<1){
						continue;
					}
					TOaVoteQuestion question=new TOaVoteQuestion();
					question.setQid(qid.substring(9));
				    answer.setQuestion(question) ;	
				    answer.setContent(textarea);
				}else{
					//单选题
					if(content[0].length()<1){
						continue;
					}
					answer.setAid(content[0]);
				}
				list_answer.add(answer);
			}			
		}
			logDao.insert(log);//记录问卷参与信息
			for(TOaVoteAnswer item:list_answer){
				this.addAnswer(item);
				if(isRealname){
					//如果是实名，记录用户回答详情
					TOaVoteAnswerLog answerlog=new TOaVoteAnswerLog();
					answerlog.setAid(item.getAid());
					if(log.getUserid()==null){//匿名参与，记录IP
						answerlog.setUserid(log.getIP());
					}else{
						answerlog.setUserid(log.getUserid());
					}
					answerlogDao.insert(answerlog);
				}
			}
		}catch (ServiceException e) {
			logger.error("页面参与调查",e);
			throw new ServiceException("find.error", new Object[] { "参与问卷" });
		}
	}
	
	/**
	 * 创建问题答案选项或参与问题
	 * 必须同步，防止顺序号混乱
	 */
	public synchronized void addAnswer(TOaVoteAnswer answer){
		try {
	        if(answer.getAid()!=null&&answer.getAid().length()>0){
	        	String hql="update TOaVoteAnswer answer set answer.count=answer.count+1 where answer.aid=? ";
	        	Object[]params=new Object[]{answer.getAid()};
	        	
	        	answerDao.createQuery(hql,params).executeUpdate();   		        	
	        }else{
	        	answer.setContent(answer.getContent().trim());//清楚空格
	        	answer.setShowno(getCount(answer.getQuestion().getQid())+1);//设置顺序号
	        	answerDao.insert(answer);
	        }
		} catch (Exception e) {
			logger.error("创建答案",e);
			throw new ServiceException("find.error", new Object[] { "问题回答" });
		}
	}
	/**
	 * 获取问题下的答案数量
	 */
	@Transactional(readOnly=true)
	public int getCount(String qid){
		String hql="select count(*) from TOaVoteAnswer answer where answer.question.qid=?";
		Object []params=new Object[]{qid};
		List result=answerDao.createQuery(hql,params).list();
		int count=1;
		if(result.get(0)!=null){
			count=Integer.parseInt(result.get(0).toString());
		}
		return count;
	}
	
	/**
	 * 删除答案,并且更新答案的顺序号
	 */
	public void delAnswer(TOaVoteAnswer answer){
		try {
			answer=answerDao.get(answer.getAid());
			answerDao.delete(answer.getAid());
			
			String hql="update TOaVoteAnswer answer set answer.showno=answer.showno-1 where answer.showno>? and answer.question.qid=? ";
			Object[]params=new Object[]{answer.getShowno(),answer.getQuestion().getQid()};
			answerDao.createQuery(hql, params).executeUpdate();
		} catch (Exception e) {
			logger.error("删除答案",e);
			throw new ServiceException("find.error", new Object[] { "删除答案" });
		}
	}
	/**
	 * 修改图片路径
	 */
	public void updatePicPath(TOaVoteAnswer answer){
		try {
			StringBuffer hql=new StringBuffer("update TOaVoteAnswer answer set answer.picPath=? where answer.aid=?");
			Object[]params=new Object[]{answer.getPicPath(),answer.getAid()};
			Query query=answerDao.createQuery(hql.toString(),params);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("修改答案图片",e);
			throw new ServiceException("find.error", new Object[] { "修改答案图片" });
		}
	}
	/**
	 * 修改详情链接
	 */
	public void updateUrl(TOaVoteAnswer answer){
		try {
			StringBuffer hql=new StringBuffer("update TOaVoteAnswer answer set answer.url=? where answer.aid=?");
			Object[]params=new Object[]{answer.getUrl(),answer.getAid()};
			Query query=answerDao.createQuery(hql.toString(),params);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("修改答案图片",e);
			throw new ServiceException("find.error", new Object[] { "修改答案详情" });
		}
	}
	/**
	 * 修改答案选项内容
	 */	
	public void updateAnswer(TOaVoteAnswer answer){
		try {
			StringBuffer hql=new StringBuffer("update TOaVoteAnswer answer set ");
			List<Object> params=new ArrayList<Object>();
			
			if(answer.getContent()!=null){
				hql.append(" answer.content=?");
				params.add(answer.getContent());
			}
			
			if(params.size()==0){
				return ;
			}
			
			hql.append(" where answer.aid=? ");	
			params.add(answer.getAid());
			
			Query query=answerDao.createQuery(hql.toString(),params.toArray(new Object[params.size()]));
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("修改答案选项内容",e);
			throw new ServiceException("find.error", new Object[] { "修改答案选项内容" });
		}
	}
	/**
	 *  查询客观题问题下的所有答案
	 */
	@Transactional(readOnly=true)
	public List<TOaVoteAnswer> loadAnswer(String qid){
		//showno越大，显示顺序越前
		String hql="from TOaVoteAnswer answer where answer.question.qid=? order by answer.showno asc";
		Object[]params=new Object[]{qid};
		return answerDao.find(hql, params);
	}
	
	/**
	 * 分页查询主观题的答案
	 */
	@Transactional(readOnly=true)
	public Page<TOaVoteAnswer> getAnswerPage(Page<TOaVoteAnswer> page, String hql ,Object[]params)  throws SystemException,ServiceException { 
		try {
			page=answerDao.find(page, hql, params);
		} catch (Exception e) {
			logger.error("问题查询",e);
			throw new ServiceException("find.error", new Object[] { "问题查询" });
		}
		return page;
	}
	
}
