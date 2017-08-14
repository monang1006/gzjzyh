package com.strongit.oa.mymail;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMailBox;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
/**
 * 对邮箱进行管理操作
 * @author yuhz
 * @version 1.0
 */
@Service
@Transactional
public class MailBoxManager {
	private GenericDAOHibernate<ToaMailBox,String> mailBoxDao;
	
	private IUserService userService;
	
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mailBoxDao = new GenericDAOHibernate<ToaMailBox,String>(sessionFactory,ToaMailBox.class);
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:02:39 AM
	 * @desc: 获得当前的用户对象
	 * @return User
	 */
	@Transactional(readOnly=true)
	public User getCurrentUser(){
		return userService.getCurrentUser();
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:05:49 AM
	 * @desc: 根据当前的用户获得其所配置的邮箱信息列表
	 * @return List<ToaMailBox>
	 */
	@Transactional(readOnly=true)
	public List<ToaMailBox> getAllList(){
		User user=getCurrentUser();
		String[] par={user.getUserId()};
		return mailBoxDao.find("from ToaMailBox as box where box.userId=? order by box.mailboxUserName asc ",par);
		//return mailBoxDao.findAll();
	}
	/**
	 * @author  于宏洲
	 * @date    2009-10-14 19:53:54
	 * @des     
	 * @return  boolean
	 */
	public boolean chargeName(String name){
		User user=getCurrentUser();
		String[] par={user.getUserId(),name};
		List list=mailBoxDao.find("from ToaMailBox as box where box.userId=? and box.mailboxUserName=?", par);
		if(list==null||list.size()==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-10-14 20:58:46
	 * @des     
	 * @return  boolean
	 */
	public boolean chargeNoChange(String id,String name){
		String[] par={id,name};
		List list=mailBoxDao.find("from ToaMailBox as box where box.mailboxId=? and box.mailboxUserName=?", par);
		if(list.size()==1){
			return true;
		}else{
			if(chargeName(name)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:06:36 AM
	 * @desc: 根据邮箱ID获得邮箱
	 * @param id 邮箱ID
	 * @return ToaMailBox
	 */
	@Transactional(readOnly=true)
	public ToaMailBox getObjById(String id){
		return mailBoxDao.get(id);
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:07:26 AM
	 * @desc: 进行邮箱对象的保存
	 * @param toaMailBox  邮箱对象
	 */
	public void saveObj(ToaMailBox toaMailBox){
		mailBoxDao.save(toaMailBox);
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:09:45 AM
	 * @desc: 根据Session进行保存邮箱对象
	 * @param toaMailBox 
	 * @return Serializable 返回对应的UUID
	 */
	public Serializable saveBySession(ToaMailBox toaMailBox){
	    return (mailBoxDao.getSession()).save(toaMailBox);
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:10:28 AM
	 * @desc: 删除邮箱
	 * @param toaMailBox
	 */
	public void delObj(ToaMailBox toaMailBox){
		mailBoxDao.delete(toaMailBox);
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
