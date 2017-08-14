/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：词语管理实现类
 */

package com.strongit.oa.updatebadwords.phrasemanage;

import java.util.Date;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaSysmanageFiltrate;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * �������Service
 * 
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class PhraseManage implements IPhraseManage {
	private GenericDAOHibernate<ToaSysmanageFiltrate, java.lang.String> PhraseDao;

	/**
	 * @roseuid 493DE90902EE
	 */
	public PhraseManage() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 493DE31102DE
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		PhraseDao = new GenericDAOHibernate<ToaSysmanageFiltrate, java.lang.String>(
				sessionFactory, ToaSysmanageFiltrate.class);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:10:12
	 * @desc: 获取所有分页词语列表
	 * @param Page
	 *            <ToaSysmanageFiltrate> page 分页对象
	 * @return Page 分页词语列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageFiltrate> getAllPhrase(
			Page<ToaSysmanageFiltrate> page) throws SystemException,
			ServiceException {
		try {
			String hql = "from ToaSysmanageFiltrate t where 1=1";
			page = PhraseDao.find(page,hql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语管理分页列表" });
		}
		return page;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:11:08
	 * @desc: 根据查询条件查询
	 * @param Page
	 *            <ToaSysmanageFiltrate> page 分页对象
	 * @param String
	 *            filtrateWord 不良词语
	 * @param String
	 *            filtrateRaplace 替代词语
	 * @param Date
	 *            filtrateTime 词语添加日期
	 * @return Page 分页词语列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageFiltrate> searchPhrase(
			Page<ToaSysmanageFiltrate> page, String filtrateWord,
			String filtrateRaplace, Date filtrateTime) throws SystemException,
			ServiceException {
		try {
			Object[] obj = new Object[3];
			int i = 0;
			String hql = "from ToaSysmanageFiltrate t where 1=1";
			// Query query=PhraseDao.createQuery(hql);
			// 不良词语
			if (filtrateWord != null && !"".equals(filtrateWord)
					&& !"null".equals(filtrateWord)) {
				hql += " and t.filtrateWord like ?";
				obj[i] = "%" + filtrateWord + "%";
				i++;
			}
			// 替代词语
			if (filtrateRaplace != null && !"".equals(filtrateRaplace)
					&& !"null".equals(filtrateRaplace)) {
				hql += " and t.filtrateRaplace like ?";
				obj[i] = "%" + filtrateRaplace + "%";
				i++;
			}
			// 添加日期
			if (filtrateTime != null && !"".equals(filtrateTime)
					&& !"null".equals(filtrateTime)) {
				hql += " and t.filtrateTime = ?";
				obj[i] = filtrateTime;
				i++;
			}
			// if(i==1){
			// query = PhraseDao.createQuery(hql.toString(),obj[0]);
			// page=PhraseDao.find(page,hql.toString(),obj[0]);
			// }
			// else if(i==2){
			// page=PhraseDao.find(page,hql.toString(),obj[0],obj[1]);
			// query = PhraseDao.createQuery(hql.toString(),obj[0],obj[1]);
			// }
			// else if(i==3){
			// page=PhraseDao.find(page,hql.toString(),obj[0],obj[1],obj[2]);
			// query =
			// PhraseDao.createQuery(hql.toString(),obj[0],obj[1],obj[2]);
			// }
			Object[] param = new Object[i];
			for (int k = 0, t = 0; k < obj.length; k++) {
				if (obj[k] != null) {
					param[t] = obj[k];
					t++;
				}
			}
			// List list=query.list();
			// page.setResult(list);
			// page.setTotalCount(list.size());
			// return page;
			hql+=" order by t.filtrateId desc";
			return PhraseDao.find(page, hql, param);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语管理分页列表" });
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 保存词语对象
	 * @param ToaSysmanageFiltrate
	 *            phrase 词语对象
	 * @return void
	 */
	public String save(ToaSysmanageFiltrate phrase) throws SystemException,
			ServiceException {
		String msg="";
		boolean flag=false;
		if(phrase.getFiltrateId()==null){
			flag=true;
		}
		try {
			PhraseDao.save(phrase);
		} catch (ServiceException e) {
			if(flag){
				msg="增加不良词语失败！";
			}else{
				msg="编辑不良词语失败";
			}
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "词语管理对象" });
		}
		return msg;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:13:55
	 * @desc: 根据主键删除对象
	 * @param String
	 *            id 主键
	 * @return void
	 */
	public void delete(String id) throws SystemException, ServiceException {
		try {
			ToaSysmanageFiltrate obj = PhraseDao.get(id);
			if (obj != null)
				PhraseDao.delete(obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "词语管理对象" });
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:14:31
	 * @desc: 根据主键获取词语对象
	 * @param String
	 *            id 主键
	 * @return ToaSysmanageFiltrate 词语对象
	 */
	public ToaSysmanageFiltrate getPhrase(String id) throws SystemException,
			ServiceException {
		ToaSysmanageFiltrate obj = null;
		try {
			obj = PhraseDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语管理对象" });
		}
		return obj;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:14:31
	 * @desc: 获取所有的不良词语
	 * @param
	 * @return String
	 */
	public String getBadWords() throws SystemException, ServiceException {
		try {
			StringBuffer badwords = new StringBuffer(""); // 不良词语
			ToaSysmanageFiltrate modle = new ToaSysmanageFiltrate();// 词语对象
			List<ToaSysmanageFiltrate> allInfo = PhraseDao.findAll();// 获取所有词语列表
			if (allInfo.size() > 0) {
				for (int i = 0; i < allInfo.size(); i++) {
					modle = allInfo.get(i);
					badwords.append(",").append(modle.getFiltrateWord());
				}
			}
			if (!"".equals(badwords.toString()))
				return badwords.toString().substring(1);
			else
				return badwords.toString();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "不良词语" });
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:14:31
	 * @desc: 获取所有的替代词语
	 * @param
	 * @return String
	 */
	public String getRepWords() throws SystemException, ServiceException {
		try {
			StringBuffer repWords = new StringBuffer(""); // 替代词语
			ToaSysmanageFiltrate modle = new ToaSysmanageFiltrate();// 词语对象
			List<ToaSysmanageFiltrate> allInfo = PhraseDao.findAll();// 获取所有词语列表
			if (allInfo.size() > 0) {
				for (int i = 0; i < allInfo.size(); i++) {
					modle = allInfo.get(i);
					repWords.append(",").append(modle.getFiltrateRaplace());
				}
			}
			if (!"".equals(repWords.toString()))
				return repWords.toString().substring(1);
			else
				return repWords.toString();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "替代词语" });
		}
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-6-8上午10:17:25
	  * @desc: 
	  * @param
	  * @return
	 */
	public List getObject(String badword,String id) throws SystemException, ServiceException{
		List list=null;
		try{
			
			String hql="from ToaSysmanageFiltrate t where t.filtrateWord=?";
			if(id!=null&&!"".equals(id)){
				hql+=" and t.filtrateId<>?";
			}
			if(id!=null&&!"".equals(id)){
				list=PhraseDao.find(hql, badword,id);
			}else{
				list=PhraseDao.find(hql, badword);
			}
			
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语管理列表" });
		}
		return list;
	}
}
