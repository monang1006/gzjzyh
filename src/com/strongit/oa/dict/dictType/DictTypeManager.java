/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-9
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典项管理MANAGER
 */
package com.strongit.oa.dict.dictType;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.util.MessagesConst;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DictTypeManager {
	private GenericDAOHibernate<ToaSysmanageDict, java.lang.String> dictTypeDao;

	private static final String YES = "1" ;
	/**
	 * @roseuid 493D192B0038
	 */
	public DictTypeManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 493CFE0101A5
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dictTypeDao = new GenericDAOHibernate<ToaSysmanageDict, java.lang.String>(
				sessionFactory, ToaSysmanageDict.class);
	}

	/**
	 * 获取所有字典类列表
	 * 
	 * @return java.util.List 字典类列表
	 * @roseuid 493CFEB5008C
	 */
	@Transactional(readOnly = true)
	public List<ToaSysmanageDict> getAllDictTypes() 
			throws SystemException,ServiceException{
		try{
			List<ToaSysmanageDict> findAll = dictTypeDao.findAll();
			return findAll;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典类列表"});
		}
	}

	/**
	 * 获取对应条件的字典类分页列表
	 * 
	 * @param page
	 *            分页对象
	 * @param model
	 *            字典类对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 493CFF9A037A
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageDict> getAllDictTypes(Page<ToaSysmanageDict> page,
			ToaSysmanageDict model) throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer("from ToaSysmanageDict t where 1=1");
			Object[] obj = new Object[4];
			int i = 0;
			if (model.getDictValue() != null && !model.getDictValue().equals("")) {
				hql.append(" and t.dictValue like ?");
				obj[i] = "%" + model.getDictValue() + "%";
				i++;
			}
			if (model.getDictName() != null && !model.getDictName().equals("")) {
				hql.append(" and t.dictName like ?");
				obj[i] = "%" + model.getDictName() + "%";
				i++;
			}
			if (model.getDictIsSystem() != null
					&& !model.getDictIsSystem().equals("")) {
				if (model.getDictIsSystem().equals("0"))
					hql.append(" and (t.dictIsSystem=? or t.dictIsSystem is null)");
				else
					hql.append(" and t.dictIsSystem=?");
				obj[i] = model.getDictIsSystem();
				i++;
			}
			if (model.getDictType() != null && !model.getDictType().equals("")) {
				hql.append(" and t.dictType=?");
				obj[i] = model.getDictType();
				i++;
			}
            hql.append(" order by t.dictValue asc");
			if (i == 0) {
				page = dictTypeDao.find(page,hql.toString());
			} else if (i == 1) {
				page = dictTypeDao.find(page, hql.toString(), obj[0]);
			} else if (i == 2) {
				page = dictTypeDao.find(page, hql.toString(), obj[0], obj[1]);
			} else if (i == 3) {
				page = dictTypeDao.find(page, hql.toString(), obj[0], obj[1],
						obj[2]);
			} else if (i == 4) {
				page = dictTypeDao.find(page, hql.toString(), obj[0], obj[1],
						obj[2], obj[3]);
			}

			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典类分页列表"});
		}
	}

	/**
	 * 根据字典类类型和字典类名称获取字典类列表
	 * 
	 * @param dictType
	 *            字典类类型
	 * @param name
	 *            字典类名称
	 * @return java.util.List 字典类列表
	 * @roseuid 493CFF190271
	 */
	@Transactional(readOnly = true)
	public List getDictTypesByType(String dictType, String name) 
				throws SystemException,ServiceException{
		try{
			if (name != null && !name.equals("") && dictType != null
					&& !dictType.equals(""))
				return dictTypeDao
						.find(
								"from ToaSysmanageDict t where t.dictType=? and t.dictName like ?",
								dictType, "%" + name + "%");
			else if (name != null && !name.equals("")
					&& !(dictType != null && !dictType.equals("")))
				return dictTypeDao.find(
						"from ToaSysmanageDict t where t.dictName like ?", "%"
								+ name + "%");
			else if (dictType != null && !dictType.equals(""))
				return dictTypeDao.find(
						"from ToaSysmanageDict t where t.dictType=?", dictType);
			else
				return dictTypeDao.findAll();// ("from ToaSysmanageDict t");
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典类列表"});
		}
	}

	/**
	 * 获取字典类对象
	 * 
	 * @param dictCode
	 *            字典类编号
	 * @return com.strongit.oa.bo.ToaSysmanageDict 字典类对象
	 * @roseuid 493D00030232
	 */
	public ToaSysmanageDict getDictType(String dictCode) 
				throws SystemException,ServiceException{
		try{
			return dictTypeDao.get(dictCode);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
				new Object[] {"字典类对象"});
		}
	}

	/**
	 * author:zhangli 
	 * description:根据字典类值获取字典类对象 
	 * modifyer: 
	 * description:
	 * 
	 * @param dictValue
	 *            字典类值
	 * @return ToaSysmanageDict 字典类对象
	 */
	public ToaSysmanageDict getDictByValue(String dictValue) 
				throws SystemException,ServiceException{
		try{
			return dictTypeDao.findUniqueByProperty("dictValue", dictValue);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
				new Object[] {"字典类对象"});
		}
	}
	
	/**
	 * author:zhangli 
	 * 查找系统中同字典类值不同编号的字典类
	 * @param dictValue  字典类值
	 * @param dictCode 字典类编号
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getOtherDictByValue(String dictValue,String dictCode) 
	throws SystemException,ServiceException{
		try{
			List list = null;
			if(dictCode!=null&&!"".equals(dictCode))
				list = dictTypeDao.find("from ToaSysmanageDict t where t.dictValue=? and t.dictCode<>?", dictValue,dictCode);
			else
				list = dictTypeDao.find("from ToaSysmanageDict t where t.dictValue=?", dictValue);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"字典类对象"});
		}
	}

	/**
	 * author:zhangli 
	 * description:根据字典类编号获取字典类名称 
	 * modifyer: 
	 * description:
	 * 
	 * @param dictCode
	 *            字典类编号
	 * @return String 字典类名称
	 */
	public String getDictName(String dictCode) throws SystemException,ServiceException{
		try{
			ToaSysmanageDict dict = getDictType(dictCode);
			return dict.getDictName();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
				new Object[] {"字典类名称"});
		}
	}

	/**
	 * author:zhangli 
	 * description:根据字典类值获取字典类名称 
	 * modifyer: 
	 * description:
	 * 
	 * @param dictValue
	 *            字典类值
	 * @return String 字典类名称
	 */
	public String getDictNameByValue(String dictValue) 
			throws SystemException,ServiceException{
		try{
			ToaSysmanageDict dict = getDictByValue(dictValue);
			//add by dengzc 2010年3月24日13:58:09
			if(dict == null){
				return null;
			}
			//end
			return dict.getDictName();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
				new Object[] {"字典类名称"});
		}
	}

	/**
	 * 保存字典类
	 * 
	 * @param dictType 字典类对象
	 * @return 保存结果
	 * @roseuid 493D004E034B
	 */
	public String saveDictType(ToaSysmanageDict dictType) 
			throws SystemException,ServiceException{
		try{
			StringBuffer message = new StringBuffer();
			List otherdict  = getOtherDictByValue(dictType.getDictValue(),dictType.getDictCode());
			if (dictType.getDictCode()==null&&otherdict != null&&otherdict.size()>0) {
				message.append("该字典类值已经存在，不可添加！");
			}else if (otherdict!=null &&otherdict.size()>0) {
				message.append("该字典类值已经存在，不可修改！");
			}else if (YES.equals(dictType.getDictIsSystem())) {
				message.append("该字典类值为系统字典类，不可修改！");
			} else {
				dictTypeDao.merge(dictType);
				message.append("保存字典类成功！");
			}
			return message.toString();
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
				new Object[] {"字典类"+dictType.getDictName()});
		}
	}
	/**
     * 保存字典类
     * 
     * @param dictType 字典类对象
     * @return 保存结果
     * @roseuid 493D004E034B
     */
    public void saveDictType1(ToaSysmanageDict dictType) 
            throws SystemException,ServiceException{
        try{
            
             dictTypeDao.merge(dictType);
                //message.append("保存字典类成功！");
            return;
        }catch(ServiceException e){
            e.printStackTrace();
            throw new ServiceException(MessagesConst.save_error,               
                new Object[] {"字典类"+dictType.getDictName()});
        }
    }
	/**
     * 保存字典类
     * 
     * @param dictType 字典类对象
     * @return 保存结果
     * @roseuid 493D004E034B
     */
    public String tryDictType(String dictCode,String dictValue) 
            throws SystemException,ServiceException{
        ToaSysmanageDict dictType=new ToaSysmanageDict();
        try{
            //String msg="保存成功";
            StringBuffer message = new StringBuffer();
             dictType=getDictType(dictCode);
             if(dictType==null){
                 dictType=new ToaSysmanageDict();
             }
            List otherdict  = getOtherDictByValue(dictValue,dictCode);
            if(dictCode!=null&&!"".equals(dictCode)){
                if (otherdict!=null &&otherdict.size()>0) {
                    message.append("该字典类值已经存在，不可修改。");
                }
                else if (YES.equals(dictType.getDictIsSystem())) {
                    message.append("该字典类值为系统字典类，不可修改。");
                }
            }else{
                if(otherdict != null&&otherdict.size()>0) {
                    message.append("该字典类值已经存在，不可添加。");
                }
            }
//            if (dictType.getDictCode()==null&&otherdict != null&&otherdict.size()>0) {
//                message.append("该字典类值已经存在，不可添加！");
//            }else if (otherdict!=null &&otherdict.size()>0) {
//                message.append("该字典类值已经存在，不可修改！");
//            }else if (YES.equals(dictType.getDictIsSystem())) {
//                message.append("该字典类值为系统字典类，不可修改！");
//            } else {
//                dictTypeDao.merge(dictType);
//                message.append("保存字典类成功！");
//            }
            if(message.toString()!=null&&!"".equals(message.toString())){
           
                return message.toString();
            }
            return "保存成功。";
        }catch(ServiceException e){
            e.printStackTrace();
            throw new ServiceException(MessagesConst.save_error,               
                new Object[] {"字典类"+dictType.getDictName()});
        }
    }
	/**
	 * 批量删除字典类
	 * 
	 * @param dictCode 字典类编号
	 * @return 删除结果
	 * @roseuid 493D0057035B
	 */
	public String deleteDictType(String dictCode) 
			throws SystemException,ServiceException{
		try{
			StringBuffer message = new StringBuffer();
			StringBuffer sysdict = new StringBuffer();
			StringBuffer susscessdict = new StringBuffer();
			String[] str = dictCode.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaSysmanageDict dictType = dictTypeDao.get(str[i]);
				if(YES.equals(dictType.getDictIsSystem())){
					sysdict.append(",").append(dictType.getDictName());
				}else{
					dictTypeDao.delete(dictType);
					susscessdict.append(",").append(dictType.getDictName());
				}
			}
			if(sysdict.length()>0){
				message.append("字典类 ").append(sysdict.toString().substring(1)).append(" 为系统字典类，不可删除！");
			}
			if(susscessdict.length()>0){
				message.append("字典类 ").append(susscessdict.toString().substring(1)).append(" 删除成功！");
			}
			return message.toString();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
				new Object[] {"字典类"});
		}
		
	}
}
