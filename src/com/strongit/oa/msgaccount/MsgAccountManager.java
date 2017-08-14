package com.strongit.oa.msgaccount;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMessageAccount;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author yuhz
 * @description  查询信息类
 *
 */

@Service
@Transactional
public class MsgAccountManager {
	private GenericDAOHibernate<ToaMessageAccount,String> msgAccountDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		msgAccountDao = new GenericDAOHibernate<ToaMessageAccount,String>(sessionFactory,ToaMessageAccount.class);
	}
	
	public void saveAccountMsg(ToaMessageAccount accMsg){
		msgAccountDao.save(accMsg);
	}
	
	public Page<ToaMessageAccount> getMessageAccount(Page<ToaMessageAccount> page,String simNumber,String operNumber,String simContent,Date beginDate,Date endDate) throws UnsupportedEncodingException{
		//String hql="from ToaMessageAccount as msg order by msg.replyTime desc";
		String hql="from ToaMessageAccount as msg";
		Object[] obj=new Object[5];
		int i=0;
		boolean hasWhere=false;
		if(simNumber==null||"".equals(simNumber)||"请您输入SIM卡号码".equals(URLDecoder.decode(simNumber, "utf-8"))){
			obj[0]=null;
		}else{
			hql=hql+" where msg.senderNumber like ?";
			//obj[0]=URLDecoder.decode(simNumber, "utf-8");
			obj[0]="%"+URLDecoder.decode(simNumber, "utf-8")+"%";
			hasWhere=true;
			i++;
		}
		if(operNumber==null||"".equals(operNumber)||"请您输入运营商号码".equals(URLDecoder.decode(operNumber, "utf-8"))){
			obj[1]=null;
		}else{
			if(hasWhere){
				hql=hql+" and msg.replyNumber like ?";
			}else{
				hql=hql+" where msg.replyNumber like ?";
			}
			//obj[1]=URLDecoder.decode(operNumber, "utf-8");
			obj[1]="%"+URLDecoder.decode(operNumber, "utf-8")+"%";
			hasWhere=true;
			i++;
		}
		
		if(simContent==null||"".equals(simContent)||"请您输入短信内容".equals(URLDecoder.decode(simContent, "utf-8"))){
			obj[2]=null;
		}else{
			if(hasWhere){
				hql=hql+" and msg.replyContent like ?";
			}else{
				hql=hql+" where msg.replyContent like ?";
			}
			obj[2]="%"+URLDecoder.decode(simContent, "utf-8")+"%";
			hasWhere=true;
			i++;
		}
		
		if(beginDate==null){
			obj[3]=null;
		}else{
			if(hasWhere){
				hql=hql+" and msg.replyTime >=?";
			}else{
				hql=hql+" where msg.replyTime >=?";
			}
			obj[3]=beginDate;
			hasWhere=true;
			i++;
		}
		if(endDate==null){
			obj[4]=null;
		}else{
			if(hasWhere){
				hql=hql+" and msg.replyTime <=?";
			}else{
				hql=hql+" where msg.replyTime <=?";
			}
			obj[4]=endDate;
			hasWhere=true;
			i++;
		}
		Object[] param=new Object[i];
		for(int k=0,t=0;k<obj.length;k++){
			if(obj[k]!=null){
				param[t]=obj[k];
				t++;
			}
		}
		return msgAccountDao.find(page,hql+" order by msg.replyTime desc", param);
	}
	
	public ToaMessageAccount getObject(String ids){
		return msgAccountDao.get(ids);
	}
	
	public boolean deleteObj(String ids){
		if(ids.indexOf(",")!=-1){
			try{
				String[] id=ids.split(",");
				for(int i=0;i<id.length;i++){
					msgAccountDao.delete(id[i]);
				}
				return true;
			}catch(Exception e){
				return false;
			}
		}else{
			try{
				msgAccountDao.delete(ids);
				return true;
			}catch(Exception e){
				return false;
			}
		}
	}
}
