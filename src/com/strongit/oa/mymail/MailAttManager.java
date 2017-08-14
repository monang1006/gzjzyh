package com.strongit.oa.mymail;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMail;
import com.strongit.oa.bo.ToaMailAttach;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 邮件附件管理
 * @author yuhz
 * @version 1.0
 */
@Service
@Transactional
public class MailAttManager {
	
	private GenericDAOHibernate<ToaMailAttach,String> mailAttDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mailAttDao = new GenericDAOHibernate<ToaMailAttach,String>(sessionFactory,ToaMailAttach.class);
	}
	
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:21:03 AM
	 * @desc: 进行附件的保存
	 * @param mailAtt
	 * @return boolean
	 */
	
	public boolean saveObj(ToaMailAttach mailAtt){
		try{
			mailAttDao.save(mailAtt);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:20:29 AM
	 * @desc: 进行附件的保存
	 * @param id
	 * @param toaMail
	 * @return boolean
	 */
	public boolean saveObj(String id,ToaMail toaMail){
		ToaMailAttach obj=new ToaMailAttach();
		obj.setAttachId6(id);
		obj.setToaMail(toaMail);
		try{
			mailAttDao.save(obj);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:20:06 AM
	 * @desc: 根据附件ID找到附件
	 * @param id 附件ID
	 * @return ToaMailAttach
	 */
	public ToaMailAttach findByAttId(String id){
		String[] param={id};
		List<ToaMailAttach> list=mailAttDao.find("from ToaMailAttach as att where att.attachId6=?", param);
		if(list.size()==0||list.size()>1){
			return null;
		}else{
			return list.get(0);
		}
		
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:19:46 AM
	 * @desc: 删除邮件的附件
	 * @param toaMailAttach
	 * @return boolean
	 */
	public boolean deleteObj(ToaMailAttach toaMailAttach){
		try{
			mailAttDao.delete(toaMailAttach);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
