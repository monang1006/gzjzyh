package com.strongit.oa.message;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMsgStateMean;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class MessageStateMeanManager {

	private GenericDAOHibernate<ToaMsgStateMean,String> msgStateMeanDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		msgStateMeanDao = new GenericDAOHibernate<ToaMsgStateMean,String>(sessionFactory,ToaMsgStateMean.class);
	}
	
	public void saveMsgStateMean(ToaMsgStateMean msm){
		msgStateMeanDao.save(msm);
	}

	@Transactional(readOnly=true)
	public List getMeanListByMsgId(String msgId){
		return msgStateMeanDao.find("from ToaMsgStateMean t where t.toaMessage.msgId=? ",msgId);
	}
	
	/**
	 * @author:luosy
	 * @description: 获取短信回复的答案列表
	 * @date : 2010-11-12
	 * @modifyer:
	 * @description:
	 * @param modelCode	模块ID
	 * @param increaseCode 自增长
	 * @return
	 */
	public List getMeanListByModelCode(String modelCode,String increaseCode) {
		return msgStateMeanDao.find("from ToaMsgStateMean t where t.modelCode=? and t.increaseCode=?",modelCode,increaseCode);
	}
}

