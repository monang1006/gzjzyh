/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息项管理MANAGER
 */
package com.strongit.oa.infotable.infoitem;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.infotable.infotype.InfoTypeManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class InfoItemManager {
	/** 信息项管理Dao*/
	private GenericDAOHibernate<ToaSysmanageProperty, java.lang.String> infoItemDao;

	/** 是*/
	private static final String YES = "1";

	/** 否*/
	private static final String NO = "0";

	/** 字符类型*/
	private static final String VAR = "0"; 

	/** 代码类型*/
	private static final String CODE = "1";  

	/** 数值类型*/
	private static final String NUM = "2";  

	/** 年份*/
	private static final String YEAR = "3";  

	/** 年月*/
	private static final String MONTH = "4";  

	/** 年月日*/
	private static final String DATE = "5"; 
	
	/** 年月日时间*/
	private static final String TIME = "6"; 

	/** 文件类型*/
	private static final String FILE = "10";  

	/** 照片类型*/
	private static final String PHOTO = "11";  

	/** 电话类型*/
	private static final String PHONE = "12";  

	/** 文本类型*/
	private static final String TEXT = "13";  

	/** 备注*/
	private static final String DES = "14"; 
	
	/** 大字段*/
	private static final String BLOB = "20"; 
	
	/** 主外键*/
	private static final String KEY = "15"; 

	/** 信息集Manager*/
	private InfoSetManager manager;

	/** 信息项分类Manager*/
	private InfoTypeManager typemanager;

	@Autowired
	public void setTypemanager(InfoTypeManager typemanager) {
		this.typemanager = typemanager;
	}

	/**
	 * @roseuid 49479C710290
	 */
	public InfoItemManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 49464D3601BA
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		infoItemDao = new GenericDAOHibernate<ToaSysmanageProperty, java.lang.String>(
				sessionFactory, ToaSysmanageProperty.class);
	}

	/**
	 * 根据表名称得到信息项列表
	 * @author:邓志城
	 * @date:2010-11-9 下午04:10:03
	 * @param tableName 表名称
	 * @return
	 */
	public List<ToaSysmanageProperty> getItemList(String tableName) {
		StringBuilder hql = new StringBuilder("from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetValue=? order by t.infoItemOrderby");
		return infoItemDao.find(hql.toString(),tableName);
	}

	/**
	 * 得到默认产生的工作流字段
	 * @author:邓志城
	 * @date:2010-11-13 上午09:35:35
	 * @return
	 */
	public List<String> getSystemField() {
		return Arrays.asList(BaseWorkflowManager.SYSTEM_INFO_ITEM);
	}
	
	/**
	 * 通过信息集编号获取信息项列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public List getAllItems(String infoSetCode) throws SystemException,ServiceException{
		try{
			List list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetCode=? order by t.infoItemOrderby",
					infoSetCode);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	/**
	 * 获取信息类中最大的排序号值
	 * 
	 * @param 
	 * @return com.strongit.oa.bo.ToaSysmanageStructure
	 * @roseuid 493670EB0167
	 */
	public long maxNo() throws Exception{
		 long res = (long)0;
		String hqlStr = "select max(t.infoItemOrderby) from ToaSysmanageProperty t";
		try {

            List<Object> list = infoItemDao.createQuery(hqlStr).list();
            if(list != null && list.size() > 0){
                Object obj =list.get(0);
                if(obj!=null){
                    res = (Long) obj;
                }
            } 
//            
//            Iterator<?> iterator = dao.createQuery(hqlStr).iterate();
//            while (iterator.hasNext()) {
//                Object obj = iterator.next();
//                if (obj != null) {
//                    res = (Integer) obj;
//                }
//            }

        } catch (Exception e) {
        	new ServiceException(MessagesConst.find_error,
					new Object[] { "根据表名查找最大可用排序值出现异常" });
           
        }
        if (res == 999) {//三位数的最大值999，最大排序号就为999
            res =999;
        } else {
            res += 1;
        }
        return res;
	}
	/**
	 * 通过信息集编号获取已构建的信息项列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public List<ToaSysmanageProperty> getAllCreatedItems(String infoSetCode) 
			throws SystemException,ServiceException{
		try{
			List<ToaSysmanageProperty> list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetCode=? and t.infoItemState=? and t.infoItemField<>'ISDEL' order by t.properTypeId,t.infoItemOrderby",
					infoSetCode,YES);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	
	/*
	 * 
	 * Description:人事信息查询模块专用
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 8, 2010 2:31:07 PM
	 */
	public List<ToaSysmanageProperty> getCreatedAndReadItems(String infoSetCode) 
	throws SystemException,ServiceException{
		try{
			List<ToaSysmanageProperty> list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetCode=? and t.infoItemState=? and t.infoItemProset<>? order by t.properTypeId,t.infoItemOrderby",
					infoSetCode,YES,YES);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	
	/**
	 * 通过信息集编号获取已构建的信息项列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public List<ToaSysmanageProperty> getAllCreatedItemsByValue(String infoSetValue) 
			throws SystemException,ServiceException{
		try{
			List<ToaSysmanageProperty> list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetValue=? and t.infoItemState=?  order by t.infoItemOrderby",
					infoSetValue,YES);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 30, 2009 2:49:55 PM
	 * Desc:
	 * param:
	 */
	public List getCreatedItemsByValue(String infoSetValue) 
		throws SystemException,ServiceException{
		try{
			List list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetValue=? and t.infoItemState=? " +
					"and t.systemIdentify=? and t.infoItemProset<>'1' order by t.infoItemOrderby" ,
					infoSetValue,YES,YES);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 4, 2009 10:21:43 AM
	 * Desc:
	 * param:
	 */
	public List<ToaSysmanageProperty> getCreatedItemsByCode(String infoSetCode) 
	throws SystemException,ServiceException{
		try{
			List<ToaSysmanageProperty> list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetCode=? and t.infoItemState=? and t.infoItemField<>'ISDEL' order by t.infoItemOrderby",
					infoSetCode,YES);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}

	
	/**
	 * 通过信息集编号获取已构建的信息项列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public List getAllCreatedBTItems(String infoSetCode) 
			throws SystemException,ServiceException{
		try{
			List list = infoItemDao
			.find(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetCode=? and t.infoItemState=? and t.infoItemFlag=? order by t.infoItemOrderby",
					infoSetCode,YES,YES);
			for (int i = 0; list!=null&&i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	
	/**
	 * 通过信息项值获取信息项列表
	 * 
	 * @param infoSetValues
	 *            信息集编号
	 * @param infoSetValues
	 *            信息项值
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public List getItemsByValues(String infosetcode,String infoSetValues) 
			throws SystemException,ServiceException{
		try{
			String[] values = infoSetValues.split(",");
			List list = new ArrayList();
			for(String infoItemField:values){
				ToaSysmanageProperty pro = getInfoItemByValue(infoItemField,infosetcode);
				if(pro==null) continue;
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
				list.add(pro);
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}

	/**
	 * 通过信息集编号获取信息集名称
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public String getInfoSetName(String infoSetCode) 
			throws SystemException,ServiceException{
		try{
			return manager.getNameByCode(infoSetCode);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息集名称"});
		}
	}

	/**
	 * 根据条件查找信息项分页列表
	 * 
	 * @param infoSetCode
	 * @param page
	 *            分页对象
	 * @param model
	 *            信息项对象
	 * @return com.strongmvc.orm.hibernate.Page
	 * @roseuid 49464DAC0343
	 */
	public Page<ToaSysmanageProperty> getAllItems(String infoSetCode,
			Page<ToaSysmanageProperty> page, ToaSysmanageProperty model)
			throws SystemException,ServiceException {
		try{
			Object[] obj = new Object[7];
			StringBuffer hql = new StringBuffer(
					"from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetCode=?");
			int i = 0;
			/** 信息项值*/
			if (model.getInfoItemField() != null
					&& !model.getInfoItemField().equals("")) {
				hql.append(" and t.infoItemField like ?");
				obj[i] = "%" + model.getInfoItemField() + "%";
				i++;
			}
			
			/** 信息项简称*/
			if (model.getInfoItemSeconddisplay() != null
					&& !model.getInfoItemSeconddisplay().equals("")) {
				if(model.getInfoItemSeconddisplay().indexOf("%")!=-1){
					String infoItemSeconddisplay=model.getInfoItemSeconddisplay();
					infoItemSeconddisplay=infoItemSeconddisplay.replaceAll("%","/%");
					hql.append("and t.infoItemSeconddisplay like '%" + infoItemSeconddisplay + "%' ESCAPE '/' ");
				}else if(model.getInfoItemSeconddisplay().indexOf("_")!=-1){
					String infoItemSeconddisplay=model.getInfoItemSeconddisplay();
					infoItemSeconddisplay=infoItemSeconddisplay.replaceAll("_","/_");
					hql.append("and t.infoItemSeconddisplay like '%" + infoItemSeconddisplay + "%' ESCAPE '/' ");
			    }else {
					hql.append(" and t.infoItemSeconddisplay like ?");
					obj[i] = "%" + model.getInfoItemSeconddisplay() + "%";
					i++;
				}
				
			}
			/** 信息项数据类型*/
			if (model.getInfoItemDatatype() != null
					&& !model.getInfoItemDatatype().equals("")) {
				hql.append(" and t.infoItemDatatype=?");
				obj[i] = model.getInfoItemDatatype();
				i++;
			}
			/** 信息项长度*/
			if (model.getInfoItemLength() != null
					&& !model.getInfoItemLength().equals("")) {
				hql.append(" and t.infoItemLength=?");
				obj[i] = model.getInfoItemLength();
				i++;
			}
			/** 信息项状态*/
			if (model.getInfoItemState() != null
					&& !model.getInfoItemState().equals("")) {
				hql.append(" and t.infoItemState=?");
				obj[i] = model.getInfoItemState();
				i++;
			}
			/** 信息项读写属性*/
			if (model.getInfoItemProset() != null
					&& !model.getInfoItemProset().equals("")) {
				hql.append(" and t.infoItemProset=?");
				obj[i] = model.getInfoItemProset();
				i++;
			}
			/** 信息项必填属性*/
			if (model.getInfoItemFlag() != null
					&& !model.getInfoItemFlag().equals("")) {
				hql.append(" and t.infoItemFlag=?");
				obj[i] = model.getInfoItemFlag();
				i++;
			}

			hql.append(" order by t.infoItemOrderby,t.infoItemCode");
			if (i == 0) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode);
			} else if (i == 1) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0]);
			} else if (i == 2) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0],
						obj[1]);
			} else if (i == 3) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0],
						obj[1], obj[2]);
			} else if (i == 4) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0],
						obj[1], obj[2], obj[3]);
			} else if (i == 5) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0],
						obj[1], obj[2], obj[3], obj[4]);
			} else if (i == 6) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0],
						obj[1], obj[2], obj[3], obj[4], obj[5]);
			} else if (i == 7) {
				page = infoItemDao.find(page, hql.toString(), infoSetCode, obj[0],
						obj[1], obj[2], obj[3], obj[4], obj[5], obj[6]);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项分页列表"});
		}
	}

	/**
	 * 根据信息项编号查找信息项对象
	 * 
	 * @param infoItemCode
	 *            信息项编号
	 * @return com.strongit.oa.bo.ToaSysmanageProperty
	 * @roseuid 49464DFB02A7
	 */
	public ToaSysmanageProperty getInfoItem(String infoItemCode) 
			throws SystemException,ServiceException{
		try{
			return infoItemDao.get(infoItemCode);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项对象"});
		}
	}

	/**
	 * 根据信息集编号和信息项值查找信息项对象
	 * 
	 * @param infoItemField
	 *            信息项值
	 * @param infoSetCode
	 *            信息集编号
	 * @return com.strongit.oa.bo.ToaSysmanageProperty
	 * @roseuid 49464DFB02A7
	 */
	public ToaSysmanageProperty getInfoItemByValue(String infoItemField,
			String infoSetCode) throws SystemException,ServiceException{
		try{
			StringBuffer sql = new StringBuffer("from ToaSysmanageProperty t");
			sql.append(" where t.infoItemField=?");
			sql.append(" and t.toaSysmanageStructure.infoSetCode=?");
			List list = infoItemDao.find(sql.toString(),infoItemField, infoSetCode);
			if(list==null||list.size()==0)
				return null;
			return (ToaSysmanageProperty) list.get(0);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项对象"});
		}
	}

	/**
	 * 保存信息项
	 * 
	 * @param model
	 *            信息项对象
	 * @roseuid 49464E4602E7
	 * 
	 */
	public String saveInfoItem(ToaSysmanageProperty model) 
			throws SystemException,ServiceException{
		String message = null;
		try {
			String infoItemCode = model.getInfoItemCode();
			ToaSysmanageProperty otherpro = getInfoItemByValue(model.getInfoItemField(), model
					.getToaSysmanageStructure().getInfoSetCode());
			String othercode = null;
			if(otherpro!=null)
				othercode = otherpro.getInfoItemCode();
			if (otherpro!=null&&(infoItemCode==null||(infoItemCode!=null && !infoItemCode.equals(othercode)))){
				message = "该信息项已经存在！";
			} else {
				//model.setInfoItemState(NO);

				infoItemDao.merge(model);
				message = "信息项保存成功！";
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"信息项对象"});
		}
		return message;
	}
	
	/**
	 * 保存信息项， 
	 * 在表单上传时，要获取信息项保存后的主键
	 * @author zhengzb
	 * @desc 
	 * 2010-11-18 上午08:53:58 
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String saveInfo(ToaSysmanageProperty model) throws SystemException,ServiceException{
		String message = null;
		try {
			String infoItemCode = model.getInfoItemCode();
			ToaSysmanageProperty otherpro = getInfoItemByValue(model.getInfoItemField(), model
					.getToaSysmanageStructure().getInfoSetCode());
			String othercode = null;
			if(otherpro!=null)
				othercode = otherpro.getInfoItemCode();
			if (otherpro!=null&&(infoItemCode==null||(infoItemCode!=null && !infoItemCode.equals(othercode)))){
				message = "该信息项已经存在！";
			} else {
				//model.setInfoItemState(NO);
			//郑志斌  2010-11-16 修改，save提交后，返回主键值
				infoItemDao.save(model);
				infoItemDao.flush();
//				infoItemDao.merge(model);
				message = "信息项保存成功！";
			}
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"信息项对象"});
		}
		return message;
	}
	
	

	
	/**
	 * 根据信息项编号删除信息项
	 * 
	 * @param infoItemCode
	 *            信息项编号
	 * @roseuid 49464E830151
	 */
	public String delInfoItem(String infoItemCode) 
			throws SystemException,ServiceException{
		try{
			String[] str = infoItemCode.split(",");
			StringBuffer message = new StringBuffer();
			StringBuffer syslist = new StringBuffer();
			StringBuffer createlist = new StringBuffer();
			StringBuffer successlist = new StringBuffer();

			for (int i = 0; i < str.length; i++) {	
				ToaSysmanageProperty pro = getInfoItem(str[i]);
				String flag = pro.getInfoItemIsSystem();
				if (null != flag && flag.equalsIgnoreCase(YES)) {	//是系统信息项
					syslist.append(",").append(pro.getInfoItemSeconddisplay())
							.append("(").append(pro.getInfoItemField()).append(")");
				} else if (YES.equalsIgnoreCase(pro.getInfoItemState())) {//信息项已构建
					createlist.append(",").append(pro.getInfoItemSeconddisplay())
							.append("(").append(pro.getInfoItemField()).append(")");
				} else {
					infoItemDao.delete(pro);
					successlist.append(",").append(pro.getInfoItemSeconddisplay())
							.append("(").append(pro.getInfoItemField()).append(")");
				}
			}
			if (syslist.length() > 0) {
				message.append(syslist.toString().substring(1)).append(
						" 是系统信息项，不可删除！\n");
			}
			if (createlist.length() > 0) {
				message.append(createlist.toString().substring(1)).append(
						" 已构建，请先撤销信息项构建！\n");
			}
			if (successlist.length() > 0) {
				message.append(successlist.toString().substring(1)).append(
						" 删除成功！\n");
			}
			return message.toString();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"信息项对象"});
		}
	}

	/**
	 * 根据信息集编号删除对应信息项列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @roseuid 49470D8C0256
	 */
	public void delInfoItemBySet(String infoSetCode) 
			throws SystemException,ServiceException{
		try{
			List list = getAllItems(infoSetCode);
			infoItemDao.delete(list);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"信息项对象"});
		}
	}

	/** 
	 * 构建信息项
	 * 
	 * @param infoItemCode
	 *            信息项编号
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.lang.String
	 * @roseuid 494652FE0335
	 */
	public String createdInfoItem(String infoItemCode, String infoSetCode) 
		throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		StringBuffer createlist = new StringBuffer();

		ToaSysmanageStructure infoSet = manager.getInfoSet(infoSetCode);
		if (infoSet.getInfoSetState().equalsIgnoreCase(NO)) {
			message.append("该信息项所属的信息集还未构建。");
		} else {
			String[] str = infoItemCode.split(",");
			try {
				for (int i = 0; i < str.length; i++) {

					String dictItemType = "";
					String dictItemValue = "";
					ToaSysmanageProperty tgsProperty = getInfoItem(str[i]);

					if (YES.equals(tgsProperty.getInfoItemState())) {
						message.append(" 信息项").append(
								tgsProperty.getInfoItemSeconddisplay()).append(
								"(").append(tgsProperty.getInfoItemField())
								.append(")已构建，不需重复构建。");
					} else {

						dictItemType = tgsProperty.getInfoItemDatatype();
						String tableName = tgsProperty
								.getToaSysmanageStructure().getInfoSetValue();
						dictItemValue = typeToStr(dictItemType);
						try {
							Boolean boo=isHasInfoItemInTable(tableName, tgsProperty.getInfoItemField());
//							ResultSet rsTables=infoItemDao.getSession().connection().getMetaData().getColumns(null, null, tableName, tgsProperty.getInfoItemField());
							if(!boo){
								tgsProperty.setInfoItemState(YES);
								infoItemDao.save(tgsProperty);
								message.append("当前信息项‘"+tgsProperty.getInfoItemField()+"'在【"+tableName+"】表中已存在。\n");
							}else{
								
								message.append(strToSqlExcute(tableName, tgsProperty,
										dictItemType, tgsProperty.getInfoItemField(),
										dictItemValue));
								
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						

						// 历史表
						/*
						 * String historysql = "select * from t_gs_history_log";
						 * ResultSet historySet =
						 * infoItemDao.executeJdbcQuery(historysql); List<String>
						 * hisList = new ArrayList<String>();
						 * while(historySet.next()) { StringBuffer
						 * historytableName = new StringBuffer(); String
						 * historydate =
						 * historySet.getString("T_GS_HISTORY_LOG_YEAR_MONTH");
						 * historytableName.append(infoSet.getInfoSetValue()).append(historydate);
						 * hisList.add(historytableName.toString());
						 *  } for(int j=0;j<hisList.size();j++) {
						 * message.append(strToSqlExcute(hisList.get(j).toString(),tgsProperty,dictItemType,str[i].toString(),dictItemValue));
						 *  }
						 */
					}
				}
			} catch(ServiceException e){
				throw new ServiceException(MessagesConst.create_error,               
						new Object[] {"信息项对象"});
			}
		}
		return message.toString();
	}

	/**
	 * 判断信息项是否在表中创建
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午10:21:29 
	 * @param tableName					表名
	 * @param infoItemField				信息项值
	 * @return
	 *   返回 :  false 存在 ;  true 不存在
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Boolean isHasInfoItemInTable(String tableName,String infoItemField)throws SystemException,ServiceException{
		try {
			Boolean boo=true;
			int num=0;
			List<String>list=getAllColumsByTable(tableName);
			if(list!=null&&list.size()>0){
				for(String columsName:list){
					if(columsName!=null&&!columsName.equals("")&&columsName.equals(infoItemField)){
						boo=false;//存在
						break;
					}
				}
			}
		return boo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,               
					new Object[] {"判断信息项对象是否在表中创建"});
		}
	}
	
	/**
	 * 获取数据库表中所有列名
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 下午05:13:06 
	 * @param tableName       表名
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getAllColumsByTable(String tableName)throws SystemException,ServiceException{
		try {
			List<String>list=new ArrayList<String>();
			ResultSet rsTables=infoItemDao.executeJdbcQuery("select t.* from "+tableName+" t");
			int count=rsTables.getMetaData().getColumnCount();
			if(count>0){
				for(int i=1;i<=count;i++){
					String columsName=rsTables.getMetaData().getColumnName(i);
					list.add(columsName);
				}
			}
//			if(rsTables!=null&&rsTables.next()){
//				String columsName=rsTables.getRow();
//				list.add(columsName);
//				
//			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,               
					new Object[] {"查询表中所有列名"});
		}
	}
	
	/**
	 * 撤销构建信息项
	 * 
	 * @param infoItemCode
	 *            信息项编号
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.lang.String
	 * @roseuid 4946530F0066
	 */
	public String destroyInfoItem(String infoItemCode, String infoSetCode) 
		throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		StringBuffer syslist = new StringBuffer();
		StringBuffer unlist = new StringBuffer();
		StringBuffer keylist = new StringBuffer();
		StringBuffer successlist = new StringBuffer();
		String[] str = infoItemCode.split(",");
		try {
			ToaSysmanageStructure infoSet = manager.getInfoSet(infoSetCode);
			for (int i = 0; i < str.length; i++) {
				ToaSysmanageProperty tgsProperty = getInfoItem(str[i]);
				if (null != tgsProperty.getInfoItemIsSystem()
						&& tgsProperty.getInfoItemIsSystem().equals(YES)) {// 为系统信息项
					syslist.append(",").append(
							tgsProperty.getInfoItemSeconddisplay()).append("(")
							.append(tgsProperty.getInfoItemField()).append(")");
				} else if (tgsProperty.getInfoItemState().equals(NO)) {// 未构建
					unlist.append(",").append(
							tgsProperty.getInfoItemSeconddisplay()).append("(")
							.append(tgsProperty.getInfoItemField()).append(")");
				} else if (tgsProperty.getInfoItemDatatype().equals(KEY)) {// 未构建
					keylist.append(",").append(
							tgsProperty.getInfoItemSeconddisplay()).append("(")
							.append(tgsProperty.getInfoItemField()).append(")");
				} else {
					String strSql = "alter table " + infoSet.getInfoSetValue()
							+ " drop column " + tgsProperty.getInfoItemField();
					infoItemDao.executeJdbcUpdate(strSql);
					//infoItemDao.getConnection().prepareStatement(strSql).execute();

					/*
					 * String historysql="select * from t_gs_history_log";
					 * ResultSet historySet =
					 * infoItemDao.executeJdbcQuery(historysql);
					 * 
					 * List<String> hisList = new ArrayList<String>();
					 * while(historySet.next()){ StringBuffer
					 * historytableName=new StringBuffer(); String
					 * historydate=historySet.getString("T_GS_HISTORY_LOG_YEAR_MONTH");
					 * historytableName.append(infoSet.getInfoSetValue()).append(historydate);
					 * hisList.add(historytableName.toString()); } for(int j=0;j<hisList.size();j++){
					 * String persql = "alter table " +
					 * hisList.get(j).toString() + " drop column " +
					 * tgsProperty.getInfoItemField();
					 * infoItemDao.executeJdbcUpdate(persql); }
					 */

					tgsProperty.setInfoItemState(NO);
					infoItemDao.save(tgsProperty);
					successlist.append(",").append(
							tgsProperty.getInfoItemSeconddisplay()).append("(")
							.append(tgsProperty.getInfoItemField()).append(")");
				}
			}
			if (unlist.length() > 0)
				message.append(unlist.toString().substring(1)).append(
						" 还未构建，不可撤销！\n");
			if (syslist.length() > 0)
				message.append(syslist.toString().substring(1)).append(
						" 是系统信息项，不可撤销！\n");
			if (keylist.length() > 0)
				message.append(keylist.toString().substring(1)).append(
						" 是主外键，不可撤销！\n");
			if (successlist.length() > 0)
				message.append(successlist.toString().substring(1)).append(
						" 信息项撤销成功！\n");
		} catch(ServiceException e){
			throw new ServiceException(MessagesConst.destory_error,               
					new Object[] {"信息项对象"});
		}
		return message.toString();
	}

	/**
	 * 将对应类型代码解析为数据库中对应类型
	 * 
	 * @param infoItemType
	 *            信息项类型
	 * @return java.lang.String
	 * @roseuid 4946530F0066
	 */
	private String typeToStr(String infoItemType) 
			throws SystemException,ServiceException{
		try{
			String dictItemValue = "";
			String URL = manager.getCurrentURL();// 获取jdbc.properties文件中hibernate.connection.url的值

			if (URL.indexOf("microsoft") != -1 || URL.indexOf("sqlserver") != -1) {// SQLServer数据库
				if (infoItemType.equalsIgnoreCase(VAR)
						|| infoItemType.equalsIgnoreCase(CODE)
						|| infoItemType.equalsIgnoreCase(DES)
						|| infoItemType.equalsIgnoreCase(PHONE)
						||infoItemType.equalsIgnoreCase(TIME)) {

					dictItemValue = "varchar";

				} else if (infoItemType.equalsIgnoreCase(NUM)) {

					dictItemValue = "numeric";

				} else if (infoItemType.equalsIgnoreCase(PHOTO)
						|| infoItemType.equalsIgnoreCase(FILE)|| infoItemType.equalsIgnoreCase(BLOB)) {

					dictItemValue = "image";

				} else if (infoItemType.equalsIgnoreCase(TEXT)) {

					dictItemValue = "text";

				} else if (infoItemType.equalsIgnoreCase(YEAR)) {

					dictItemValue = "varchar(4)";

				} else if (infoItemType.equalsIgnoreCase(MONTH)) {

					dictItemValue = "varchar(7)";

				} else if (infoItemType.equalsIgnoreCase(DATE)) {

					dictItemValue = "varchar(10)"; // 类型转化为数据库可识别类型

				}
			
		} else if (URL.indexOf("mysql") != -1) {
			if (infoItemType.equalsIgnoreCase(VAR)
					|| infoItemType.equalsIgnoreCase(CODE)
					|| infoItemType.equalsIgnoreCase(DES)
					|| infoItemType.equalsIgnoreCase(PHONE)
					||infoItemType.equalsIgnoreCase(TIME)) {

				dictItemValue = "varchar";

			} else if (infoItemType.equalsIgnoreCase(NUM)) {

				dictItemValue = "numeric";

			} else if (infoItemType.equalsIgnoreCase(PHOTO)
					|| infoItemType.equalsIgnoreCase(FILE)|| infoItemType.equalsIgnoreCase(BLOB)) {

				dictItemValue = "longblob";

			} else if (infoItemType.equalsIgnoreCase(TEXT)) {

				dictItemValue = "text";

			} else if (infoItemType.equalsIgnoreCase(YEAR)) {

				dictItemValue = "varchar(4)";

			} else if (infoItemType.equalsIgnoreCase(MONTH)) {

				dictItemValue = "varchar(7)";

			} else if (infoItemType.equalsIgnoreCase(DATE)) {

				dictItemValue = "varchar(10)"; // 类型转化为数据库可识别类型

			}
		} else if (URL.indexOf("db2") != -1) {

		} else if (URL.indexOf("oracle") != -1) {
			if (infoItemType.equalsIgnoreCase(VAR)
					|| infoItemType.equalsIgnoreCase(CODE)
					|| infoItemType.equalsIgnoreCase(DES)
					|| infoItemType.equalsIgnoreCase(PHONE)
					||infoItemType.equalsIgnoreCase(TIME)) {

				dictItemValue = "varchar2";

			} else if (infoItemType.equalsIgnoreCase(NUM)) {

				dictItemValue = "number";

			} else if (infoItemType.equalsIgnoreCase(PHOTO)
					|| infoItemType.equalsIgnoreCase(FILE)||infoItemType.equalsIgnoreCase(BLOB)) {

				dictItemValue = "blob";

			} else if (infoItemType.equalsIgnoreCase(TEXT)) {

				dictItemValue = "clob";

			} else if (infoItemType.equalsIgnoreCase(YEAR)) {

				dictItemValue = "varchar2(4)";

			} else if (infoItemType.equalsIgnoreCase(MONTH)) {

				dictItemValue = "varchar2(7)";

			} else if (infoItemType.equalsIgnoreCase(DATE)) {

				dictItemValue = "varchar2(10)"; // 类型转化为数据库可识别类型
			}

		}
			return dictItemValue;
		} catch(ServiceException e){
			throw new ServiceException("将对应类型代码解析为数据库中对应类型出错!");
		}
	}

	/**
	 * author:zhangli description:根据信息项设置生成对应SQL语句 modifyer: description:
	 * 
	 * @param tableName
	 *            表名
	 * @param tgsProperty
	 *            信息项对象
	 * @param dictItemType
	 *            信息项类型
	 * @param itemName
	 *            信息项名称
	 * @param dictItemValue
	 *            解析后在数据库中对应类型
	 * @return
	 */
	private String strToSqlExcute(String tableName,
			ToaSysmanageProperty tgsProperty, String dictItemType,
			String itemName, String dictItemValue)throws SystemException,ServiceException {
		String message = "";
		String strSql = "";
		// 可以尝试使用switch
		try {
			if (dictItemType.equalsIgnoreCase(YEAR)
					|| dictItemType.equalsIgnoreCase(MONTH)
					|| dictItemType.equalsIgnoreCase(DATE)
					|| dictItemType.equalsIgnoreCase(PHOTO)
					|| dictItemType.equalsIgnoreCase(FILE)
					|| dictItemType.equalsIgnoreCase(TEXT)||dictItemType.equalsIgnoreCase(BLOB)) {

				strSql = "alter table " + tableName + " add " + itemName + " "
						+ dictItemValue;
				//构建时，判断字段是否必填
				if(tgsProperty.getInfoItemFlag()!=null&&!tgsProperty.getInfoItemFlag().equals("")&&tgsProperty.getInfoItemFlag().equals("1")){		
					strSql=strSql+" not null ";
				}
				infoItemDao.executeJdbcUpdate(strSql);
				//infoItemDao.getConnection().prepareStatement(strSql).execute();
				tgsProperty.setInfoItemState(YES);
				infoItemDao.save(tgsProperty);
				message = "信息项构建成功。\n";
			} else if (dictItemType.equalsIgnoreCase(VAR)
					|| dictItemType.equalsIgnoreCase(CODE)
					|| dictItemType.equalsIgnoreCase(DES)
					|| dictItemType.equalsIgnoreCase(PHONE)
					|| dictItemType.equalsIgnoreCase(TIME)) {
				// 为考虑汉字问题，故实际长度应为所设置长度两倍，这样才不会因为汉字而出错(张丽)
				int lenth = 0;

				if (tgsProperty.getInfoItemLength() != null
						&& !tgsProperty.getInfoItemLength().equals("")
						&& !tgsProperty.getInfoItemLength().equals("null"))
					lenth = Integer.parseInt(tgsProperty.getInfoItemLength());

				if (lenth == 0)
					message = "信息项长度不能为零。\n";
				else {
					strSql = "alter table " + tableName + " add " + itemName
							+ " " + dictItemValue.concat("(")
							+ String.valueOf(lenth * 2).concat(")");
					if(tgsProperty.getInfoItemFlag()!=null&&!tgsProperty.getInfoItemFlag().equals("")&&tgsProperty.getInfoItemFlag().equals("1")){
						strSql=strSql+" not null ";
					}
					infoItemDao.executeJdbcUpdate(strSql);
					//infoItemDao.getConnection().prepareStatement(strSql).execute();
					tgsProperty.setInfoItemState(YES);
					infoItemDao.save(tgsProperty);
					message = "信息项构建成功。\n";
				}
			} else if (dictItemType.equalsIgnoreCase(NUM)) {
				int lenth = 0;
				if (tgsProperty.getInfoItemLength() != null
						&& !tgsProperty.getInfoItemLength().equals("")
						&& !tgsProperty.getInfoItemLength().equals("null"))
					lenth = Integer.parseInt(tgsProperty.getInfoItemLength());

				if (lenth == 0)
					message = "信息项长度不能为零。\n";
				else {
					strSql = "alter table " + tableName + " add " + itemName
							+ " " + dictItemValue.concat("(")
							+ tgsProperty.getInfoItemLength().concat(",")
							+ tgsProperty.getInfoItemDecimal().concat(")");
					
//					构建时，判断字段是否必填
					if(tgsProperty.getInfoItemFlag()!=null&&!tgsProperty.getInfoItemFlag().equals("")&&tgsProperty.getInfoItemFlag().equals("1")){		
						strSql=strSql+" not null ";
					}
					infoItemDao.executeJdbcUpdate(strSql);
					//infoItemDao.getConnection().prepareStatement(strSql).execute();
					tgsProperty.setInfoItemState(YES);
					infoItemDao.save(tgsProperty);
					message = "信息项构建成功。\n";
				}
			} else {
				message = "所设置的数据类型为非法类型，无法构建信息项。\n";
			}
		} catch(ServiceException e){
			throw new ServiceException("根据信息项设置生成对应SQL语句出错。");
		}
		return tgsProperty.getInfoItemSeconddisplay() + "("
				+ tgsProperty.getInfoItemField() + ")" + message;
	}

	@Autowired
	public void setManager(InfoSetManager manager) {
		this.manager = manager;
	}

	public ResultSet executeJdbcQuery(String sql) throws SystemException,ServiceException {
		try{
			return infoItemDao.executeJdbcQuery(sql);
			//return infoItemDao.getConnection().prepareStatement(sql).executeQuery();
		} catch(ServiceException e){
			throw new ServiceException("根据信息项设置生成对应SQL语句出错。");
		}
	}

	public void executeJdbcUpdate(String sql) throws SystemException,ServiceException {
		try{
			infoItemDao.executeJdbcUpdate(sql);
			//infoItemDao.getConnection().prepareStatement(sql).execute();
		} catch(ServiceException e){
			throw new ServiceException("根据信息项设置生成对应SQL语句出错。");
		}
	}
	/**
	 * 判断信息项值是否存在
	 * @param infoItemField
	 * @return
	 */
	public boolean stutsinfoItemField(ToaSysmanageProperty model) {
		Boolean stuts = false;
		String hql = "from ToaSysmanageProperty as t where t.infoItemField=? and t.toaSysmanageStructure.infoSetCode=?";
		List list = infoItemDao.find(hql, model.getInfoItemField(),model.getToaSysmanageStructure().getInfoSetCode());
		if(model.getInfoItemCode()==null || "".equals(model.getInfoItemCode())){//新增
			if(list.size()>0)
				stuts = true;
		}else{//修改
			if(list.size()>0){
				for(Iterator it = list.iterator(); it.hasNext();){
					ToaSysmanageProperty bo = (ToaSysmanageProperty) it.next();
					if(!bo.getInfoItemCode().equals(model.getInfoItemCode())){
						stuts = true;
					}
				}
			
			}
		}
		infoItemDao.clear();
		return stuts;
	}
	
/**
 * 根据 信息集ID和信息项值查询列表
 * @author zhengzb
 * @desc 
 * 2010-11-22 上午09:57:18 
 * @param infoItemField  信息项值
 * @param infoSetCode    信息集ID
 * @return
 */
	public List<ToaSysmanageProperty> stutsinfoAndItemField(String infoItemField,String infoSetCode) {
		String hql = "from ToaSysmanageProperty as t where t.infoItemField=? and t.toaSysmanageStructure.infoSetCode=?";
		List<ToaSysmanageProperty> list = infoItemDao.find(hql, infoItemField,infoSetCode);
		return list;
	}
	
	/*
	 * 
	 * Description:根据查询条件查询符合条件的信息项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 23, 2009 2:30:11 PM
	 */
	public List getCreatedItemsByValueAndCon(String infoSetValue,String infoItemCondition) throws SystemException,ServiceException{
		try{
			StringBuffer hql=new StringBuffer("");
			hql.append("from ToaSysmanageProperty t where t.toaSysmanageStructure.infoSetValue=? and t.infoItemState=?");
			if(infoItemCondition!=null&&!"".equals(infoItemCondition)&&!"null".equals(infoItemCondition)){
				hql.append(" and t.infoItemCondition like '%")
				.append(infoItemCondition)
				.append("%'");
			}else{
				hql.append(" and (t.infoItemCondition is null or t.infoItemCondition<>'hidden')");
			}
			hql.append(" order by t.infoItemOrderby");
			List list = infoItemDao.find(hql.toString(),infoSetValue,YES);
			for (int i = 0; i < list.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(i);
				if (pro.getProperTypeId() != null)
					pro.setProperTypeName(typemanager.getInfoTypeName(pro
							.getProperTypeId()));
				pro.setInfoItemValue(pro.getInfoItemDefaultvalue());
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}
	
	/*
	 * 
	 * Description:根据信息项编码数组查找信息项列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Feb 3, 2010 9:31:16 AM
	 */
	public List<ToaSysmanageProperty> getInfoItemsByCodeArray(String[] infoItemCode){
		List<ToaSysmanageProperty> list=new ArrayList<ToaSysmanageProperty>();
		for(int i=0;i<infoItemCode.length;i++){
			ToaSysmanageProperty tgsPropery=this.getInfoItem(infoItemCode[i]);
			list.add(tgsPropery);
		}
		return list;
	}
	
	/*
	 * 
	 * Description:根据信息集值和信息项数据类型查找符合条件的信息项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Feb 27, 2010 10:06:40 AM
	 */
	public List<ToaSysmanageProperty> getAllCreatedItemsByValueAndDataType(String[] infoSetValues,String dataType) 
									throws SystemException,ServiceException{
		try{
			List<ToaSysmanageProperty> resultList=new ArrayList<ToaSysmanageProperty>();
			//for(int i=0;infoSetValues!=null&&i<infoSetValues.length;i++){
				List<ToaSysmanageProperty> list = infoItemDao.find("from ToaSysmanageProperty t where " +
						"t.toaSysmanageStructure.infoSetValue=? and t.infoItemState=? and t.infoItemDatatype=? " +
						"order by t.infoItemOrderby",infoSetValues[0],YES,dataType);
				for (int j = 0; list!=null&&j < list.size();j++) {
					ToaSysmanageProperty pro = (ToaSysmanageProperty) list.get(j);
					pro.setRefernceDesc(pro.getInfoItemSeconddisplay()+"("+infoSetValues[0]+")");
					resultList.add(pro);
				}
			//}
			return resultList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项列表"});
		}
	}

	/*
	 * 
	 * Description:根据信息集值和信息项值获取信息项对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 4, 2010 5:54:41 PM
	 */
	public ToaSysmanageProperty getItemByTableNameAndFiled(String infoSetValue,
			String infoItemFiled) throws SystemException,ServiceException{
		try{
			StringBuffer sql = new StringBuffer("from ToaSysmanageProperty t");
			sql.append(" where t.toaSysmanageStructure.infoSetValue=?");
			sql.append(" and t.infoItemField=?");
			List<ToaSysmanageProperty> list = infoItemDao.find(sql.toString(),infoSetValue, infoItemFiled);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
			return null;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项对象"});
		}
	}
	/**
	 * 判断排序号是否重复
	 * author  taoji
	 * @return
	 * @date 2014-3-21 下午08:58:33
	 */
	public String check(String infoSetCode,String infoItemOrderby){
		StringBuffer sql = new StringBuffer("from ToaSysmanageProperty t ");
		sql.append(" where t.toaSysmanageStructure.infoSetCode='"+infoSetCode+"' and t.infoItemOrderby='"+infoItemOrderby+"'");
		List<ToaSysmanageProperty> list = infoItemDao.find(sql.toString());
		if(list!=null&&list.size()>0){
			return "false";
		}
		return "true";
	}
}
