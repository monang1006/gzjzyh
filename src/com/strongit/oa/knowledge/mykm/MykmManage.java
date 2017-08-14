/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 个人收藏夹manager
 */
package com.strongit.oa.knowledge.mykm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaInfopublishComment;
import com.strongit.oa.bo.ToaKnowledgeMykm;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MykmManage {

	private GenericDAOHibernate<ToaKnowledgeMykm, String> mykmDAO;

	private static final String YES = "1";

	private static final String NO = "0";

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mykmDAO = new GenericDAOHibernate<ToaKnowledgeMykm, String>(
				sessionFactory, ToaKnowledgeMykm.class);
	}

	/***
	 * 添加一条知识收藏
	 * @param mykm
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveMykm(ToaKnowledgeMykm mykm) throws SystemException,
	ServiceException {
		try {
			mykm.setMykmIsshow(YES);//是否显示
			mykm.setMykmDate(new Date());
			mykmDAO.save(mykm);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "添加我的知识" });
		}
	}

	/**
	 * 添加多条知识收藏
	 * @param mykmList
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveMykms(List<ToaKnowledgeMykm> mykmList)
	throws SystemException, ServiceException {
		try {
			//	 Date date=new Date();
			//    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			//     String time1= format.format(date);
			mykmDAO.save(mykmList);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "添加多条我的知识" });
		}
	}

	/***
	 * 根据ID删除我的知识（逻辑删除）
	 * @param mykmID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteMykm(List<ToaKnowledgeMykm> mykms)
	throws SystemException, ServiceException {
		try {
			for (ToaKnowledgeMykm mykm : mykms) {
				mykm.setMykmIsshow(NO);
			}
			mykmDAO.save(mykms);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "删除一条我的知识" });
		}
	}

	/**
	 * 根据URL删除收藏
	 * @param url
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByUrl(String url) throws SystemException,
	ServiceException {
		try {
			String sql = "from ToaKnowledgeMykm t where t.mykmUrl=?";
			List<ToaKnowledgeMykm> list = mykmDAO.find(sql, url);
			mykmDAO.delete(list);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "删除一条我的知识" });
		}
	}

	/***
	 * 根据ID查看知识详细信息
	 * @param mykmID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaKnowledgeMykm getMykmByID(String mykmID) throws SystemException,
	ServiceException {
		try {
			return mykmDAO.get(mykmID);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "查看知识信息" });
		}
	}

	/***
	 * 根据属性查看知识信息
	 * @param mykmUser
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaKnowledgeMykm> getMykmsByUser(Page<ToaKnowledgeMykm> page,
			String value) throws SystemException, ServiceException {
		try {
			String sql = "from ToaKnowledgeMykm mykm where mykm.mykmUser=? and mykm.mykmIsshow='1' order by mykm.mykmDate DESC";
			Object[] obj = { value };
			return mykmDAO.find(page, sql, obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "查看我的知识" });
		}
	}

	/***
	 * 根据用户和分类查看知识信息
	 * @param mykmUser
	 * @param mykmIssortId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaKnowledgeMykm> getMykmsBySort(Page<ToaKnowledgeMykm> page,
			String mykmUser, String mykmIssortId) throws SystemException,
			ServiceException {
		try {
			String sql = "from ToaKnowledgeMykm mykm where mykm.mykmUser=? and mykm.mykmIssortId=? and mykm.mykmIsshow='1' order by mykm.mykmDate DESC";
			Object[] obj = { mykmUser, mykmIssortId };
			return mykmDAO.find(page, sql, obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "根据类型查看我的知识" });
		}
	}

	/***
	 * 根据用户名和属性查看知识列表
	 * @param propertyName
	 * @param value
	 * @param mykmUser
	 * @return
	 */
	public Page<ToaKnowledgeMykm> getMykmByProUser(Page<ToaKnowledgeMykm> page,
			String value, String mykmUser,String sortid) {
		try {
			List<Object> list=new ArrayList<Object>();
			String sql = "from ToaKnowledgeMykm mykm where mykm.mykmUser=?";
		    list.add(mykmUser);
			if(value!=null&&!"".equals(value)){
			   sql=sql+"and mykm.mykmName like ? "	;
			   list.add(value+"%");
			}
			if(sortid!=null&&!"".equals(sortid)){
				sql=sql+" and mykm.mykmIssortId=?";
				list.add(sortid);
			}
			sql=sql+" and mykm.mykmIsshow='1' order by mykm.mykmDate DESC";
			Object[] obj = new Object[list.size()];
			for(int i=0;i<list.size();i++){
				obj[i]=list.get(i);
			}
			return mykmDAO.find(page, sql, obj);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "根据用户名和属性查看知识列表" });
		}
	}

	/***
	 * 根据存储路径查看知识信息
	 * @param url
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaKnowledgeMykm> getMykmByMykmUrl(Page<ToaKnowledgeMykm> page,
			String url) throws SystemException, ServiceException {
		try {
			String sql = "from ToaKnowledgeMykm mykm where mykm.mykmUrl=?  and  mykm.mykmIsshow='1' order by mykm.mykmDate DESC";
			Object[] obj = { url };
			return mykmDAO.find(page, sql, obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "根据存储路径查看知识信息" });

		}
	}
}
