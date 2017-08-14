/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-19
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典项管理MANAGER
 */
package com.strongit.oa.dict.dictItem;

import com.strongmvc.exception.SystemException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.util.MessagesConst;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DictItemManager {
	/** 字典项Dao*/
	private GenericDAOHibernate<ToaSysmanageDictitem, java.lang.String> dictItemDao;
	
	private static final String YES = "1" ;

	/**
	 * @return
	 * @roseuid 494265D3036B
	 */
	public void DictItemManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 493F38A103D8
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dictItemDao = new GenericDAOHibernate<ToaSysmanageDictitem, java.lang.String>(
				sessionFactory, ToaSysmanageDictitem.class);
	}

	/**
	 * 根据字典类编号获取对应所有字典项列表
	 * 
	 * @param dictCode
	 *            字典类编号
	 * @return java.util.List 字典项列表
	 * @roseuid 493F38F7033C
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaSysmanageDictitem> getAllDictItems(String dictCode) throws SystemException,ServiceException{
		try{
			List<ToaSysmanageDictitem> findAll = dictItemDao
				.find(
						"from ToaSysmanageDictitem t where t.toaSysmanageDict.dictCode=? and  t.dictItemIsSelect<>1  order by t.dictItemDescspell desc,t.dictItemValue asc",
						dictCode);
			return findAll;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}
	}

	/**
	 * 获取字典项分页列表
	 * 
	 * @param page
	 *            分页对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 493F391A0242
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageDictitem> getAllDictItems (
			Page<ToaSysmanageDictitem> page) throws SystemException,ServiceException{
		try{
			return dictItemDao.findAll(page);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项分页列表"});
		}
	}

	/**
	 * 根据字典类编号和字典项对象信息获取对应字典项列表
	 * 
	 * @param dictCode
	 *            字典类编号
	 * @param model
	 *            字典项对象
	 * @return java.util.List 字典项列表
	 * @roseuid 493F393E0290
	 */
	@Transactional(readOnly = true)
	public List getItemsByDict(String dictCode, ToaSysmanageDictitem model) 
		throws SystemException,ServiceException{
		try{
			List list = null;
			String dictitemcode = model.getDictItemCode();
			if (dictitemcode == null || dictitemcode.equals("")//字典项编码为空
					|| dictitemcode.equals("null"))
				list = dictItemDao
						.find(
								"from ToaSysmanageDictitem t where t.toaSysmanageDict.dictCode=?",
								dictCode);
			else	//字典项编码不为空
				list = dictItemDao.find(
						"from ToaSysmanageDictitem t where t.toaSysmanageDict.dictCode=?"
								+ " and t.dictItemCode<>?", dictCode, dictitemcode);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}
	}

	/**
	 * 根据字典类编号和字典项对象信息获取对应字典项分页列表
	 * 
	 * @param dictCode
	 *            字典类编号
	 * @param page
	 *            分页对象
	 * @param model
	 *            字典项对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 493F3976037A
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageDictitem> getItemsByDict(String dictCode,
			Page<ToaSysmanageDictitem> page, ToaSysmanageDictitem model) 
			throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer(
					"from ToaSysmanageDictitem t where t.toaSysmanageDict.dictCode='"+dictCode+"'");
			//int i = 0;
			if (model.getDictItemValue() != null
					&& !model.getDictItemValue().equals("")) {
				hql.append(" and t.dictItemValue like ");
				hql.append( "'%" + model.getDictItemValue() + "%'");
			
			}
			if (model.getDictItemName() != null
					&& !model.getDictItemName().equals("")) {
				hql.append(" and t.dictItemName like ");
				hql.append("'%" + model.getDictItemName()+ "%'");
			
			}
			if (model.getDictItemDescspell() != null
					&& !model.getDictItemDescspell().equals("")) {
				hql.append(" and t.dictItemDescspell like ");
				hql.append("'%" + model.getDictItemDescspell() + "%'");
			
			}
			if (model.getDictItemIsSelect() != null
					&& !model.getDictItemIsSelect().equals("")) {
				hql.append(" and t.dictItemIsSelect=");
				hql.append(model.getDictItemIsSelect());
			}
			
			hql.append(" order by t.dictItemDescspell");

			page = dictItemDao.find(page, hql.toString());
		
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项分页列表"});
		}
	}

	/**
	 * 获取字典项对象
	 * 
	 * @param dictItemCode
	 *            字典项编号
	 * @return com.strongit.oa.bo.ToaSysmanageDictitem 字典项对象
	 * @roseuid 493F39B7034B
	 */
	public ToaSysmanageDictitem getDictItem(String dictItemCode) throws SystemException,ServiceException{
		try{
			return dictItemDao.get(dictItemCode);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项对象"});
		}
	}

	/**
	 * author:zhangli 
	 * description: 获取字典项名称 
	 * modifyer: 
	 * description:
	 * 
	 * @param dictItemCode 字典项编码
	 * @return String 字典项名称
	 */
	public String getDictItemName(String dictItemCode) 
			throws SystemException,ServiceException{
		try{
			ToaSysmanageDictitem dictitem = getDictItem(dictItemCode);
			if (dictitem == null)
				return null;
			return dictitem.getDictItemShortdesc();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项名称"});
		}
	}

	/**
	 * author:zhangli 
	 * description:获取父字典项名称 
	 * modifyer: 
	 * description:
	 * 
	 * @param dictItemCode 字典项编码
	 * @return 父字典项名称
	 */
	public String getDictItemParentName(String dictItemCode) 
			throws SystemException,ServiceException{
		try{
			String dictItemParentvalue = (String) dictItemDao.findUnique(
					"select t.dictItemParentvalue "
							+ "from ToaSysmanageDictitem t where t.dictItemCode=?",
					dictItemCode);
			if (dictItemParentvalue == null)
				return null;
			return getDictItemName(dictItemParentvalue);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"父字典项名称"});
		}
	}

	/**
	 * 保存字典项
	 * 
	 * @param dictitem
	 *            字典项对象
	 * @param dictid
	 *            字典类编号
	 * @return String 保存结果
	 * @roseuid 493F39F1003E
	 */
	public String saveDictItem(ToaSysmanageDictitem dictitem, String dictid) 
						throws SystemException,ServiceException{
		try{
			StringBuffer message = new StringBuffer();
			List list = getOtherDictItemByValue(dictitem.getDictItemValue(), dictid,dictitem.getDictItemCode());
			if (list != null && list.size() > 0) {
				message.append("该字典项已经存在");
			}else if (YES.equals(dictitem.getDictItemIsSystem())) {
				message.append("该字典项值为系统字典项，不可修改！");
			} else {
				dictItemDao.save(dictitem);
				message.append("保存字典项成功");
			}
			return message.toString();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"字典项 "+dictitem.getDictItemName()});
		}
	}

	/**
	 * 批量删除字典项
	 * 
	 * @param dictItemCode
	 *            字典项编号
	 * @return String 删除结果
	 * @roseuid 493F39FA0271
	 */
	public String deleteDictItem(String dictItemCode) 
				throws SystemException,ServiceException{
		try{
			StringBuffer message = new StringBuffer();
			StringBuffer sysdict = new StringBuffer();
			StringBuffer susscessdict = new StringBuffer();
			String[] str = dictItemCode.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaSysmanageDictitem dictitem = dictItemDao.get(str[i]);
				if(YES.equals(dictitem.getDictItemIsSystem())){
					sysdict.append(",").append(dictitem.getDictItemName());
				}else{
					dictItemDao.delete(str[i]);
					susscessdict.append(",").append(dictitem.getDictItemName());
				}
			}
			if(sysdict.length()>0){
				message.append("字典项 ").append(sysdict.toString().substring(1)).append(" 为系统字典项，不可删除！");
			}
			else if(susscessdict.length()>0){
				message.append("字典项 ").append(susscessdict.toString().substring(1)).append(" 删除成功！");
			}
			return message.toString();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"字典项 "});
		}
	}

	/**
	 * author:zhangli 
	 * description:根据字典项值和字典类查找字典项列表 
	 * modifyer: 
	 * description:
	 * 
	 * @param value 字典项值
	 * @param dictid 字典类编号
	 * @return List 字典项列表
	 */
	public List getDictItemByValue(String value, String dictid) 
				throws SystemException,ServiceException{
		try{
			return dictItemDao
			.find(
					"from ToaSysmanageDictitem t "
							+ "where t.dictItemValue = ? and t.toaSysmanageDict.dictCode=?",
					value, dictid);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}
	}
	
	/**
	 * author:zhangli 
	 * description:根据字典项值和字典类查找同字典项值不同编号的字典项列表 
	 * modifyer: 
	 * description:
	 * 
	 * @param value 字典项值
	 * @param dictid 字典类编号
	 * @param dictitemid 字典项编号
	 * @return List 字典项列表
	 */
	public List getOtherDictItemByValue(String value, String dictid,String dictitemid) 
				throws SystemException,ServiceException{
		try{
			if(dictitemid==null)//如果字典项编号为空，则查找系统中是否存在对应字典项值的字典项
				return dictItemDao
				.find(
					"from ToaSysmanageDictitem t "
							+ "where t.dictItemValue = ? and t.toaSysmanageDict.dictCode=?",
					value, dictid);
			else	//否则，查找系统中是否存在同字典项值不同编号的字典项列表 
				return dictItemDao
				.find(
						"from ToaSysmanageDictitem t "
								+ "where t.dictItemValue = ? and t.toaSysmanageDict.dictCode=? and t.dictItemCode<>?",
						value, dictid,dictitemid);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}
	}

	/**
	 * author:zhangli 
	 * description:根据字典名称和字典类查找字典项列表 
	 * modifyer: 
	 * description:
	 * 
	 * @param value 字典项名称
	 * @param dictid 字典类编号
	 * @return 字典项列表
	 */
	public List getDictItem(String value, String dictid) 
				throws SystemException,ServiceException{
		try{
			return dictItemDao
			.find(
					"from ToaSysmanageDictitem t "
							+ "where t.dictItemShortdesc like ? and t.toaSysmanageDict.dictCode=?",
					"%" + value + "%", dictid);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 24, 2009 11:21:04 AM
	 * Desc:
	 * param:
	 */
	public ToaSysmanageDictitem getDictitemByShort(String dictItemShortdesc,String dictCode)
	{
		try{
			String strQuery="select dictItem from ToaSysmanageDictitem dictItem where dictItem.dictItemShortdesc=? and dictItem.toaSysmanageDict.dictCode=?";
			List<ToaSysmanageDictitem> list=dictItemDao.find(strQuery, dictItemShortdesc,dictCode);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}	
	}
	
	/*
	 * 
	 * Description:获取某字典可选的字典项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jul 1, 2010 6:13:44 PM
	 */
	public List<ToaSysmanageDictitem> getDictItemsByCode(String dictCode) throws SystemException,ServiceException{
		try{
			List<ToaSysmanageDictitem> findAll = dictItemDao
				.find(
						"from ToaSysmanageDictitem t where t.toaSysmanageDict.dictCode=? and t.dictItemIsSelect=0 order by t.dictItemValue asc",
						dictCode);
			return findAll;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典项列表"});
		}
	}
}
