package com.strongit.oa.mymail;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMailFolder;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
/**
 * 对邮箱文件夹的管理类
 * @author yuhz
 * @version 1.0
 */
@Service
@Transactional
public class MailFolderManager {
	
	private GenericDAOHibernate<ToaMailFolder,String> mailFolderDao;
	
	private MailManager mailManager;
	
	@Autowired
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mailFolderDao = new GenericDAOHibernate<ToaMailFolder,String>(sessionFactory,ToaMailFolder.class);
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:11:04 AM
	 * @desc: 根据邮箱文件夹ID获得邮箱文件夹
	 * @param id  邮箱文件夹ID
	 * @return ToaMailFolder
	 */
	@Transactional(readOnly=true)
	public ToaMailFolder getObjById(String id){
		return mailFolderDao.get(id);
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:11:54 AM
	 * @desc: 保存邮箱文件夹
	 * @param toaMailFolder  邮箱文件夹
	 * void
	 */
	public void save(ToaMailFolder toaMailFolder){
		mailFolderDao.save(toaMailFolder);
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:12:10 AM
	 * @desc: 删除邮箱文件夹
	 * @param toaMailFolder 邮箱文件夹对象
	 * void
	 */
	public void delObj(ToaMailFolder toaMailFolder){
		mailFolderDao.delete(toaMailFolder);
	}

	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:17:02 AM
	 * @desc: 根据邮箱的邮箱ID和邮件文件夹类型进行邮件文件夹的查询
	 * @param mailBoxId
	 * @param mailType
	 * @return ToaMailFolder
	 */
	@Transactional(readOnly=true)
	public ToaMailFolder getFolderByParent(String mailBoxId,String mailType){
		String[] par={mailBoxId,mailType};
		List<ToaMailFolder> list=mailFolderDao.find("from ToaMailFolder as folder where folder.toaMailBox.mailboxId=? and folder.mailfolderType=?",par);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-10-14 15:55:46
	 * @des     根据邮箱ID和邮件发文件夹名称查询邮件文件夹
	 * @return  List<ToaMailFolder>
	 */
	public boolean getFolder(String mailBoxId,String mailName){
		String[] par={mailBoxId,mailName};
		List<ToaMailFolder> list=mailFolderDao.find("from ToaMailFolder as folder where folder.toaMailBox.mailboxId=? and folder.mailfolderType='5' and folder.mailfolderName=?", par);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:17:42 AM
	 * @desc: 根据邮箱ID来获取邮箱文件夹的列表
	 * @param mailBoxId  邮箱ID
	 * @return List<ToaMailFolder>
	 */
	public List<ToaMailFolder> getFolderByPId(String mailBoxId){
		String[] par={mailBoxId};
		List<ToaMailFolder> list=mailFolderDao.find("from ToaMailFolder as folder where folder.toaMailBox.mailboxId=? order by folder.mailfolderType", par);
		return list;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:18:53 AM
	 * @desc: 根据邮箱ID获得邮箱的收件箱
	 * @param mailBoxId 邮箱ID
	 * @return ToaMailFolder
	 */
	@Transactional(readOnly=true)
	public ToaMailFolder getFolderByParent(String mailBoxId){
		String[] par={mailBoxId};
		List<ToaMailFolder> list=mailFolderDao.find("from ToaMailFolder as folder where folder.toaMailBox.mailboxId=? and folder.mailfolderType='1'",par);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}

}
