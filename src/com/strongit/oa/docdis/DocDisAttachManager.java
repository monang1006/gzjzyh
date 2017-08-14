package com.strongit.oa.docdis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDocdisAttachment;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * <p>Title: DocDisAttachManager.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 公文分发对应附件管理类
 * @date 	 2009-11-10 14:02:18
 * @version  1.0
 */
@Service
@Transactional
public class DocDisAttachManager {
	
	GenericDAOHibernate<ToaDocdisAttachment, String> attachDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		attachDao = new GenericDAOHibernate<ToaDocdisAttachment, String>(sessionFactory,ToaDocdisAttachment.class);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-10 15:44:45
	 * @des 	进行分发公文附件的批量保存   
	 * @return  boolean
	 */
	public boolean saveList(List<ToaDocdisAttachment> list) throws ServiceException,SystemException{
		try{
			attachDao.save(list);
			return true;
		}catch(Exception e){
			throw new SystemException("附件批量存储失败！");
		}
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-13 10:35:10
	 * @des 	根据待分发文档ID号查询对应的附件    
	 * @return  List<ToaDocdisAttachment>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaDocdisAttachment> getAttachmentsByDocId(String docId){
		List<ToaDocdisAttachment> list = attachDao.find("from ToaDocdisAttachment t where t.docId=?", docId);
		if(list == null){						//保证list不为空值
			return new ArrayList<ToaDocdisAttachment>();
		}
		return list;
	}
	
	/*
	 * 
	 * Description:删除附件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 5, 2010 4:45:12 PM
	 */
	public void deleteAttachment(List<ToaDocdisAttachment> list)throws ServiceException,SystemException{
		try{
			attachDao.delete(list);
		}catch(Exception e){
			throw new SystemException("附件批量存储失败！");
		}
	}

}
