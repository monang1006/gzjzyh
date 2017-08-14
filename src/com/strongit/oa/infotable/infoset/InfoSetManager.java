/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息集管理MANAGER
 */
package com.strongit.oa.infotable.infoset;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSysmanageFormManager;
import com.strongit.oa.bo.ToaSysmanagePmanager;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.formManager.IFormManagerService;
import com.strongit.oa.infoManager.IInfoManagerService;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author zhangli
 * @version 1.0
 */
@Service
@Transactional
public class InfoSetManager {
	private GenericDAOHibernate<ToaSysmanageStructure, java.lang.String> infoSetDao;
	/** 信息项管理Dao*/
	private GenericDAOHibernate<ToaSysmanageProperty, java.lang.String> infoItemDao;

	private static final String YES = "1";

	private static final String NO = "0";

	private InfoItemManager manager;
	
	private IFormManagerService formManagerService;		//表单管理
	
	private IInfoManagerService infoManagerService;     //信息项管理

	/**
	 * @roseuid 493692F500EA
	 */
	public InfoSetManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 49366EBF0203
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		infoSetDao = new GenericDAOHibernate<ToaSysmanageStructure, java.lang.String>(
				sessionFactory, ToaSysmanageStructure.class);
		infoItemDao = new GenericDAOHibernate<ToaSysmanageProperty, java.lang.String>(
				sessionFactory, ToaSysmanageProperty.class);
	}

	/**
	 * 获取所有信息集列表
	 * 
	 * @return java.util.List
	 * @roseuid 49366FA6033C
	 */
	@Transactional(readOnly = true)
	public List<ToaSysmanageStructure> getAllSets() throws SystemException,
			ServiceException {
		try {
			//根据信息集的排序号 排序
			List<ToaSysmanageStructure> list = infoSetDao.find("from ToaSysmanageStructure t order by t.infoSetOrderno");
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集列表" });
		}
	}

	/**
	 * 获取所有信息集列表(未构建不展现)
	 * 
	 * @return java.util.List
	 */
	@Transactional(readOnly = true)
	public List<ToaSysmanageStructure> getStructureSets() throws SystemException,
			ServiceException {
		try {
			//未构建不展现 infoSetState='1'为已构建
			List<ToaSysmanageStructure> list = infoSetDao.find("from ToaSysmanageStructure s where s.infoSetState='1'");
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集列表" });
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
		 long res = 0;
		String hqlStr = "select max(t.infoSetOrderno) from ToaSysmanageStructure t";
		try {

            List<Object> list = infoSetDao.createQuery(hqlStr).list();
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
        if (res == 9999999999L) {//信息集排序序号达到最大值9999999999，排序号就为9999999999
            res = 9999999999L;
        } else {
            res += 1;
        }
        return res;
	}
	/**
	 * 根据条件查找信息集分页列表
	 * 
	 * @param page
	 *            分页对象
	 * @return com.strongmvc.orm.hibernate.Page
	 * @roseuid 49367031006D
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageStructure> getAllSets(String infoSetParentid,
			Page<ToaSysmanageStructure> page, ToaSysmanageStructure model)
			throws SystemException, ServiceException {
		try {
			Object[] obj = new Object[5];
			StringBuffer hql = new StringBuffer(
					"from ToaSysmanageStructure t where t.infoSetParentid=?");
			int i = 0;
			/** 信息集值 */
//			if (model.getInfoSetValue() != null
//					&& !model.getInfoSetValue().equals("")) {
//				hql.append(" and t.infoSetValue like ?");
//				obj[i] = "%" + model.getInfoSetValue() + "%";
//				i++;
//			}
			if (null !=model.getInfoSetValue() && !("").equals(model.getInfoSetValue())) {
					if(model.getInfoSetValue().indexOf("%")!=-1){
					String InfoSetValue=model.getInfoSetValue();
					InfoSetValue=InfoSetValue.replaceAll("%","/%");
					hql.append("and t.infoSetValue like '%" + InfoSetValue + "%' ESCAPE '/' ");
				}else if(model.getInfoSetValue().indexOf("_")!=-1){
						String InfoSetValue=model.getInfoSetValue();
						InfoSetValue=InfoSetValue.replaceAll("_","/_");
						hql.append("and t.infoSetValue like '%" + InfoSetValue + "%' ESCAPE '/' ");
				}
				else {
					hql.append(" and t.infoSetValue like ? ");
					obj[i] = "%" + model.getInfoSetValue() + "%";
					i++;
				}
			}
			
			/** 信息集名称 */
//			if (model.getInfoSetName() != null
//					&& !model.getInfoSetName().equals("")) {
//				hql.append(" and t.infoSetName like ?");
//				obj[i] = "%" + model.getInfoSetName() + "%";
//				i++;
//			}
			if (null != model.getInfoSetName() && !("").equals(model.getInfoSetName())) {
					if(model.getInfoSetName().indexOf("%")!=-1){
					String InfoSetName=model.getInfoSetName();
					InfoSetName=InfoSetName.replaceAll("%","/%");
					hql.append("and t.infoSetName like '%" + InfoSetName + "%' ESCAPE '/' ");
				}else if(model.getInfoSetName().indexOf("_")!=-1){
					String InfoSetName=model.getInfoSetName();
					InfoSetName=InfoSetName.replaceAll("_","/_");
					hql.append("and t.infoSetName like '%" + InfoSetName + "%' ESCAPE '/' ");
			}
				else {
					hql.append(" and t.infoSetName like ? ");
					obj[i] = "%" + model.getInfoSetName() + "%";
					i++;
				}
			}
			/** 信息集主键值 */
			if (model.getInfoSetPkey() != null
					&& !model.getInfoSetPkey().equals("")) {
				hql.append(" and t.infoSetPkey like ?");
				obj[i] = "%" + model.getInfoSetPkey() + "%";
				i++;
			}
			/** 信息集构建状态 */
			if (model.getInfoSetState() != null
					&& !model.getInfoSetState().equals("")) {
				hql.append(" and t.infoSetState=?");
				obj[i] = model.getInfoSetState();
				i++;
			}
			/** 信息集系统状态 */
			if (model.getInfoSetIsSystem() != null
					&& !model.getInfoSetIsSystem().equals("")) {
				if ("1".equals(model.getInfoSetIsSystem())) {
					hql.append(" and t.infoSetIsSystem=? ");
					obj[i] = model.getInfoSetIsSystem();
					i++;
				} else {
					hql
							.append(" and (t.infoSetIsSystem=? or t.infoSetIsSystem=null) ");
					obj[i] = model.getInfoSetIsSystem();
					i++;
				}

			}
			/**信息集列表  根据信息集排序号 排序*/
			hql.append(" order by t.infoSetOrderno");
			if (i == 0) {
				page = infoSetDao.find(page, hql.toString(), infoSetParentid);
			} else if (i == 1) {
				page = infoSetDao.find(page, hql.toString(), infoSetParentid,
						obj[0]);
			} else if (i == 2) {
				page = infoSetDao.find(page, hql.toString(), infoSetParentid,
						obj[0], obj[1]);
			} else if (i == 3) {
				page = infoSetDao.find(page, hql.toString(), infoSetParentid,
						obj[0], obj[1], obj[2]);
			} else if (i == 4) {
				page = infoSetDao.find(page, hql.toString(), infoSetParentid,
						obj[0], obj[1], obj[2], obj[3]);
			} else if (i == 5) {
				page = infoSetDao.find(page, hql.toString(), infoSetParentid,
						obj[0], obj[1], obj[2], obj[3], obj[4]);
			}
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集列表" });
		}
	}

	/**
	 * 获取所有已构建的信息集列表
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午09:46:46 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSysmanageStructure> getAllListByInfoset() throws SystemException,ServiceException{
		try {
			StringBuffer hql = new StringBuffer("from ToaSysmanageStructure t where t.infoSetState=1");
			List<ToaSysmanageStructure> list=infoSetDao.find(hql.toString());
			if(list!=null&&list.size()>0){
				return list;
			}else {
				
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
	/**
	 * 通过信息集主键获取信息集对象
	 * 
	 * @param infosetCode
	 *            信息集主键值
	 * @return com.strongit.oa.bo.ToaSysmanageStructure
	 * @roseuid 493670EB0167
	 */
	public ToaSysmanageStructure getInfoSet(String infosetCode)
			throws SystemException, ServiceException {
		try {
			return infoSetDao.get(infosetCode);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集对象" });
		}
	}

	/**
	 * ������baocun保存信息集
	 * 
	 * @param infoSet
	 *            信息集对象
	 * @roseuid 4936719102DE
	 * 
	 */
	public String saveInfoSet(ToaSysmanageStructure infoSet)
			throws SystemException, ServiceException {
		String message = "";
		try {
			String infosetcode = infoSet.getInfoSetCode();
			if (infosetcode == null) {
				infoSet.setInfoSetIsexistchild(NO); // 初始化是否有子节点位0--没有子节点
				String parentid = infoSet.getInfoSetParentid();
				if (parentid != null && !parentid.equals("")
						&& !parentid.equals("0")) {
					ToaSysmanageStructure tgsStructure = getInfoSet(parentid);
					String isExsit = tgsStructure.getInfoSetIsexistchild();// 是否有子信息集存在
					if (null != isExsit && NO.equals(isExsit))// 如果已经存在子信息集则无需要修改判断是否有子信息集状态值

					{ // 注意hibernate提交的原理
						tgsStructure.setInfoSetIsexistchild(YES);
						infoSetDao.save(tgsStructure);
					}
				}
			}
			if(!(infoSet.getInfoSetState()!=null&&!infoSet.getInfoSetState().equals("")&&infoSet.getInfoSetState().equals("1"))){
				
				infoSet.setInfoSetState(NO);// 初始化是否已经构建位0--没有构建
			}
			/** 设置主键 */
			String pkey = infoSet.getInfoSetValue();
			if (pkey.indexOf("_") != -1)
				pkey = pkey.substring(pkey.lastIndexOf("_") + 1, pkey.length());
			infoSet.setInfoSetPkey(pkey.concat("ID"));
			infoSetDao.getSession().merge(infoSet);
			message = "信息集保存成功！";
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "信息集对象" });
		}
		return message;
	}

	
	public String saveInfo(ToaSysmanageStructure infoSet) throws SystemException, ServiceException {
		String message = "";
		try {
			String infosetcode = infoSet.getInfoSetCode();
			if (infosetcode == null) {
				infoSet.setInfoSetIsexistchild(NO); // 初始化是否有子节点位0--没有子节点
				String parentid = infoSet.getInfoSetParentid();
				if (parentid != null && !parentid.equals("")
						&& !parentid.equals("0")) {
					ToaSysmanageStructure tgsStructure = getInfoSet(parentid);
					String isExsit = tgsStructure.getInfoSetIsexistchild();// 是否有子信息集存在
					if (null != isExsit && NO.equals(isExsit))// 如果已经存在子信息集则无需要修改判断是否有子信息集状态值

					{ // 注意hibernate提交的原理
						tgsStructure.setInfoSetIsexistchild(YES);
						infoSetDao.save(tgsStructure);
					}
				}
			}

			infoSet.setInfoSetState(NO);// 初始化是否已经构建位0--没有构建
			/** 设置主键 */
			String pkey = infoSet.getInfoSetValue();
			if (pkey.indexOf("_") != -1)
				pkey = pkey.substring(pkey.lastIndexOf("_") + 1, pkey.length());
			infoSet.setInfoSetPkey(pkey.concat("ID"));
		//郑志斌  2010-11-16 修改保存方法，在save提交后，返回主键值
			infoSetDao.save(infoSet);
			infoSetDao.flush();
//			infoSetDao.getSession().merge(infoSet);
			message = "信息集保存成功！";
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "信息集对象" });
		}
		return message;
	}
	
	
	
	
	/**
	 * 根据信息集值查找
	 * @author zhengzb
	 * @desc 
	 * 2010-11-15 下午01:48:47 
	 * @param infoSetValue			信息集值
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSysmanageStructure getInfosetModel(String infoSetValue)throws SystemException,ServiceException {
		try {
			String hql="from ToaSysmanageStructure t where 1=1";
			hql=hql+" and t.infoSetValue=?";				
			List<ToaSysmanageStructure> list=infoSetDao.find(hql.toString(),infoSetValue);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"信息集是否存在"});	
		}
	}
	
	/**
	 * 批量删除信息集
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @roseuid 4936722901E4
	 */
	public String delInfoSet(String infoSetCode) throws SystemException,
			ServiceException {
		StringBuffer message = new StringBuffer();
		StringBuffer syslist = new StringBuffer();
		StringBuffer createlist = new StringBuffer();
		StringBuffer successlist = new StringBuffer();
		StringBuffer usedlist=new StringBuffer();
		String[] str = infoSetCode.split(",");
		try {
			for (int i = 0; str!=null&&i < str.length; i++) {
				ToaSysmanageStructure infoset = getInfoSet(str[i]);
				String flag = infoset.getInfoSetIsSystem();
				if (null != flag && flag.equalsIgnoreCase(YES)) {//是系统信息集

					syslist.append(",").append(infoset.getInfoSetName())
							.append("(").append(infoset.getInfoSetValue())
							.append(")");

				} else if (YES.equalsIgnoreCase(infoset.getInfoSetState())){//信息集已构建

					createlist.append(",").append(infoset.getInfoSetName())
							.append("(").append(infoset.getInfoSetValue())
							.append(")");

				} else {
					String tableName=infoset.getInfoSetValue();
					int count=infoManagerService.isHasInfoFormUsed(tableName);
					if(count>0){
						usedlist.append(",").append(infoset.getInfoSetName())
						.append("(").append(infoset.getInfoSetValue())
						.append(")");
					}else {
						
						infoSetDao.delete(infoset);
						successlist.append(",").append(infoset.getInfoSetName())
						.append("(").append(infoset.getInfoSetValue())
						.append(")");
					}

				}
			}

			if (syslist.length() > 0) {
				message.append(syslist.toString().substring(1)).append(
						" 是系统信息集，不可删除！\n");
			}
			if (createlist.length() > 0) {
				message.append(createlist.toString().substring(1)).append(
						" 已构建，请先撤销信息集构建！\n");
			}
			if (successlist.length() > 0) {
				message.append(successlist.toString().substring(1)).append(
						" 删除成功！\n");
			}
			if(usedlist.length()>0){
				message.append(usedlist.toString().substring(1)).append(
				" 已经使用不能删除！\n");
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "信息集对象" });
		}
		return message.toString();
	}

	/**
	 * 通过信息集值查找信息集对象
	 * 
	 * @param infoSetValue
	 *            信息集值
	 * @return com.strongit.oa.bo.ToaSysmanageStructure
	 * @roseuid 49367402035B
	 */
	public ToaSysmanageStructure getToaStructByValue(String infoSetValue)
			throws SystemException, ServiceException {
		try {
			return infoSetDao
					.findUniqueByProperty("infoSetValue", infoSetValue);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集对象" });
		}
	}

	/**
	 * 通过信息集值查找信息集对象列表
	 * 
	 * @param infoSetValue
	 *            信息集值
	 * @return List
	 * @roseuid 49367402035B
	 */
	public List getToaStructListByValue(String infoSetValue)
			throws SystemException, ServiceException {
		try {
			// infoSetDao.getSession().setFlushMode(FlushMode.COMMIT);
			return infoSetDao.findByProperty("infoSetValue", infoSetValue);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集对象列表" });
		}
	}

	/**
	 * 通过信息集编码查找信息集名称
	 * 
	 * @param infoSetCode
	 *            信息集编码
	 * @return String
	 * @roseuid 49367402035B
	 */
	public String getNameByCode(String infoSetCode) throws SystemException,
			ServiceException {
		try {
			ToaSysmanageStructure infoset = getInfoSet(infoSetCode);
			if (infoset == null)
				return null;
			return infoset.getInfoSetName();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息集名称" });
		}
	}

	/**
	 * 通过信息集编码查找父信息集名称
	 * 
	 * @param infoSetCode
	 *            信息集编码
	 * @return String
	 * @roseuid 49367402035B
	 */
	public String getParentNameByChild(String infoSetCode)
			throws SystemException, ServiceException {
		try {
			String infoSetParentid = (String) infoSetDao
					.findUnique(
							"select t.infoSetParentid "
									+ "from ToaSysmanageStructure t where t.infoSetCode=?",
							infoSetCode);
			if (infoSetParentid == null)
				return null;
			return getNameByCode(infoSetParentid);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "父信息集名称" });
		}
	}

	/**
	 * 通过父信息集编号获取对应子信息集对象列表
	 * 
	 * @param infoSetParentid
	 *            父信息集编号
	 * @return java.util.List
	 * @roseuid 4936754F02AF
	 */
	public List getChildInfoSet(String infoSetParentid) throws SystemException,
			ServiceException {
		try {
			return infoSetDao
					.findByProperty("infoSetParentid", infoSetParentid);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "子信息集对象列表" });
		}
	}

	/**
	 * 通过父信息集编号获取对应已构建的子信息集列表
	 * 
	 * @param infoSetParentid
	 *            父信息集编号
	 * @return java.util.List
	 * @roseuid 4936782003C8
	 */
	public List getChildCreatedInfoSet(String infoSetParentid)
			throws SystemException, ServiceException {
		try {
			return infoSetDao
					.find(
							"from ToaSysmanageStructure t where t.infoSetParentid=? and t.infoSetState=?",
							infoSetParentid, "1");
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "子信息集对象列表" });
		}
	}

	/**
	 * 构建信息集
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return String
	 * @roseuid 4936763601E4
	 */
	public String CreatedInfoSet(String infoSetCode) throws SystemException,
			ServiceException {
		String[] str = infoSetCode.split(",");
		StringBuffer message = new StringBuffer();
		StringBuffer hascreates = new StringBuffer();
		StringBuffer cannotcreates = new StringBuffer();
		StringBuffer successcreates = new StringBuffer();
		try {
			for (int i = 0; i < str.length; i++) {
				ToaSysmanageStructure struct = infoSetDao.get(str[i]);
				String parentCode = struct.getInfoSetParentid();
				String pKey = null;
				if (parentCode != null && !parentCode.equals("")) {
					ToaSysmanageStructure tgsStructP = infoSetDao
							.get(parentCode);
					/** 如果为分类，继续找其父信息集 */
					if (tgsStructP != null
							&& YES.equals(tgsStructP.getInfoSetType()))
						tgsStructP = infoSetDao.get(tgsStructP
								.getInfoSetParentid());

					pKey = tgsStructP == null ? null : tgsStructP
							.getInfoSetPkey();
				}
				if (YES.equals(struct.getInfoSetState())) {
					hascreates.append(",").append(struct.getInfoSetName())
							.append("(").append(struct.getInfoSetValue())
							.append(")");
				} else if (YES.equals(struct.getInfoSetType())) {
					cannotcreates.append(",").append(struct.getInfoSetName())
							.append("(").append(struct.getInfoSetValue())
							.append(")");
				} else { // 判断是否构建,是否可以构建的类型
					String key = struct.getInfoSetPkey();
					String tableName = struct.getInfoSetValue();
					String createSql = strSqlToCreate(tableName, key, pKey);
					//判断当前表名，在数据库中是否创建
					if(isHasTableCreate(tableName)){
						infoSetDao.executeJdbcUpdate(createSql);
					}
					
					// infoSetDao.getConnection().prepareStatement(createSql).execute();
					struct.setInfoSetState(YES);
					infoSetDao.save(struct);

					insertKeyItem(struct, pKey, key);										//添加信息集主外键信息项
					
					successcreates.append(",").append(struct.getInfoSetName())
							.append("(").append(struct.getInfoSetValue())
							.append(")");
				}// if条件结束

			}
			if (hascreates.length() > 0) {
				message.append(hascreates.toString().substring(1)).append(
						" 是已经构建，不可重复构建！\n");
			}
			if (cannotcreates.length() > 0) {
				message.append(cannotcreates.toString().substring(1)).append(
						" 为不可构建类型，无法构建！\n");
			}
			if (successcreates.length() > 0) {
				message.append(successcreates.toString().substring(1)).append(
						" 构建成功！\n");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		}
		return message.toString();
	}

	/**
	 * 同步已构建信息集的工作流初始化字段
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午11:10:03 
	 * @param struct
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String CreatedInfoSetByWorkflow(ToaSysmanageStructure struct) throws SystemException,ServiceException {
		try {				
			//判断当前表名，在数据库中是否创建
			if(isHasTableCreate(struct.getInfoSetValue())){
				String key = struct.getInfoSetPkey();
				String tableName = struct.getInfoSetValue();
				
				String parentCode = struct.getInfoSetParentid();
				String pKey = null;
				if (parentCode != null && !parentCode.equals("")) {
					ToaSysmanageStructure tgsStructP = infoSetDao.get(parentCode);
					/** 如果为分类，继续找其父信息集 */
					if (tgsStructP != null&& YES.equals(tgsStructP.getInfoSetType()))
						tgsStructP = infoSetDao.get(tgsStructP.getInfoSetParentid());

					pKey = tgsStructP == null ? null : tgsStructP.getInfoSetPkey();
				}
				
				String createSql = strSqlToCreate(tableName, key, pKey);
				infoSetDao.executeJdbcUpdate(createSql);
				
				struct.setInfoSetState(YES);
				infoSetDao.save(struct);

				insertKeyItem(struct, pKey, key);										//添加信息集主外键信息项
				
			}else {
				
				//获取构建默认工作段字段的SQL语句
				String createSql = strSqlToCreateDate(struct.getInfoSetValue());
				if(createSql!=null&&!createSql.equals("")&&createSql.length()>1){
					//执行SQL语句					
					infoSetDao.executeJdbcUpdate(createSql);
				}
				//工作流默认初始化字段保存到信息项中
				insertWorkflowInfoItem(struct);	
			}
			
			//同步信息项中可查询字段和可显示字段
			List<ToaSysmanageProperty> pList=manager.getAllItems(struct.getInfoSetCode()); 		//通过信息集编号获取信息项列表
			if(pList!=null&&pList.size()>0){
				for(ToaSysmanageProperty property:pList){
					if(property.getInfoItemState()!=null&&!property.getInfoItemState().equals("")&&property.getInfoItemState().equals("1")){
						if(property.getIsQuery()==null||property.equals("")){
							property.setIsQuery(NO);
						}
						if(property.getIsView()==null||property.getIsView().equals("")){
							property.setIsView(NO);
						}						
					}else {
						property.setIsQuery(NO);
						property.setIsView(NO);
					}
					manager.saveInfo(property);
				}
			}
				
			return "success";
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		}
	}
	
	
	/**
	 * author:zhangli description:自动插入对应信息集的主外键信息项 modifyer: description:
	 * 
	 * @param struct
	 *            信息集对象
	 * @param pKey
	 *            外键
	 * @param key
	 *            主键
	 */
	private void insertKeyItem(ToaSysmanageStructure struct, String pKey,
			String key) throws SystemException, ServiceException {
		try {
			ToaSysmanageProperty p1 = new ToaSysmanageProperty();
			ToaSysmanageProperty p2 = new ToaSysmanageProperty();
			ToaSysmanageProperty p3 = new ToaSysmanageProperty();
			ToaSysmanageProperty p4 = new ToaSysmanageProperty();
			ToaSysmanageProperty p5 = new ToaSysmanageProperty();
			ToaSysmanageProperty p6 = new ToaSysmanageProperty();
			ToaSysmanageProperty p7 = new ToaSysmanageProperty();
			p1.setInfoItemField(key);
			p1.setInfoItemSeconddisplay("主键");
			p1.setInfoItemDatatype("15");
			p1.setInfoItemLength("32");
			p1.setToaSysmanageStructure(struct);
			p1.setInfoItemState(YES);// 构建状态
			p1.setInfoItemProset("1");// 读写状态
			p1.setInfoItemFlag(NO);// 必填状态
			p1.setInfoItemOrderby(new Long("0")); // 排序字段
			p1.setIsQuery(NO);									//是否查询字段
			p1.setIsView(NO);									//是否显示字段
			manager.saveInfoItem(p1);
			
			
			

			if (pKey != null && !pKey.equals("") && !pKey.equals("null")) {
				p2.setInfoItemField(pKey);
				p2.setInfoItemSeconddisplay("外键");
				p2.setInfoItemDatatype("15");
				p2.setInfoItemLength("32");
				p2.setToaSysmanageStructure(struct);
				p2.setInfoItemState(YES); // 构建状态
				p2.setInfoItemProset("1");// 读写状态
				p2.setInfoItemFlag(NO);// 必填状态
				p2.setInfoItemOrderby(new Long("0")); // 排序字段
				p2.setIsQuery(NO);									//是否查询字段
				p2.setIsView(NO);									//是否显示字段
				manager.saveInfoItem(p2);
			}

			p3.setInfoItemField("PERSON_CONFIG_FLAG");
			p3.setInfoItemSeconddisplay("标志");
			p3.setInfoItemDatatype("0");
			p3.setInfoItemLength("1");
			p3.setToaSysmanageStructure(struct);
			p3.setInfoItemState(YES); // 构建状态
			p3.setInfoItemProset("1");// 读写状态
			p3.setInfoItemFlag(NO);// 必填状态
			p3.setInfoItemOrderby(new Long("0")); // 排序字段
			p3.setIsQuery(NO);									//是否查询字段
			p3.setIsView(NO);									//是否显示字段
			manager.saveInfoItem(p3);

			p4.setInfoItemField("PERSON_OPERATE_DATE");
			p4.setInfoItemSeconddisplay("创建日期");
			p4.setInfoItemDatatype("5");
			p4.setInfoItemLength("10");
			p4.setToaSysmanageStructure(struct);
			p4.setInfoItemState(YES); // 构建状态
			p4.setInfoItemProset("1");// 读写状态
			p4.setInfoItemFlag(NO);// 必填状态
			p4.setInfoItemOrderby(new Long("0")); // 排序字段
			p4.setIsQuery(NO);									//是否查询字段
			p4.setIsView(NO);									//是否显示字段
			manager.saveInfoItem(p4);

			p5.setInfoItemField("PERSON_OPERATER");
			p5.setInfoItemSeconddisplay("操作者");
			p5.setInfoItemDatatype("0");
			p5.setInfoItemLength("32");
			p5.setToaSysmanageStructure(struct);
			p5.setInfoItemState(YES); // 构建状态
			p5.setInfoItemProset("1");// 读写状态
			p5.setInfoItemFlag(NO);// 必填状态
			p5.setInfoItemOrderby(new Long("0")); // 排序字段
			p5.setIsQuery(NO);									//是否查询字段
			p5.setIsView(NO);									//是否显示字段
			manager.saveInfoItem(p5);

			p6.setInfoItemField("PERSON_DEMO");
			p6.setInfoItemSeconddisplay("内容");
			p6.setInfoItemDatatype("10");
			p6.setInfoItemLength("2000");
			p6.setToaSysmanageStructure(struct);
			p6.setInfoItemState(YES); // 构建状态
			p6.setInfoItemProset("1");// 读写状态
			p6.setInfoItemFlag(NO);// 必填状态
			p6.setInfoItemOrderby(new Long("0")); // 排序字段
			p6.setIsQuery(NO);									//是否查询字段
			p6.setIsView(NO);									//是否显示字段
			manager.saveInfoItem(p6);

			p7.setInfoItemField("PERSON_FILENAME");
			p7.setInfoItemSeconddisplay("文件名称");
			p7.setInfoItemDatatype("0");
			p7.setInfoItemLength("255");
			p7.setToaSysmanageStructure(struct);
			p7.setInfoItemState(YES); // 构建状态
			p7.setInfoItemProset("1");// 读写状态
			p7.setInfoItemFlag(NO);// 必填状态
			p7.setInfoItemOrderby(new Long("0")); // 排序字段
			p7.setIsQuery(NO);									//是否查询字段
			p7.setIsView(NO);									//是否显示字段
			manager.saveInfoItem(p7);
			
			insertWorkflowInfoItem(struct);										//工作流默认字段
			
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		}
	}

	
	/**
	 * 工作流默认初始化字段保存到信息项中
	 * @author zhengzb
	 * @desc 
	 * 2010-11-18 下午08:00:28 
	 * @param struct
	 * @throws SystemException
	 */
	private void insertWorkflowInfoItem(ToaSysmanageStructure struct) throws SystemException{
		try {
			List<String>list=manager.getSystemField();
			for(int i=0;i<list.size();i++){
				ToaSysmanageProperty infoItemModel=new ToaSysmanageProperty();
				String workFlowName=list.get(i);
				List<ToaSysmanageProperty> infoItemList=manager.stutsinfoAndItemField(workFlowName, struct.getInfoSetCode().toString());
				if(!(infoItemList!=null&&infoItemList.size()>0)){					
					infoItemModel.setInfoItemField(workFlowName);
					infoItemModel.setIsQuery(NO);									//是否查询字段
					infoItemModel.setIsView(NO);									//是否显示字段
					if(workFlowName.equals(BaseWorkflowManager.WORKFLOW_NAME)){
						infoItemModel.setInfoItemSeconddisplay("流程名称");
						infoItemModel.setInfoItemLength("255");
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_STATE)) {
						infoItemModel.setInfoItemSeconddisplay("流程状态");
						infoItemModel.setInfoItemLength("2");					
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_AUTHOR)) {
						infoItemModel.setInfoItemSeconddisplay("拟稿人");
						infoItemModel.setInfoItemLength("100");
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_CODE)) {
						infoItemModel.setInfoItemSeconddisplay("流水号");
						infoItemModel.setInfoItemLength("255");
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_TITLE)) {
						infoItemModel.setInfoItemSeconddisplay("流程标题");
						infoItemModel.setInfoItemLength("500");
						infoItemModel.setIsQuery(YES);									//是否查询字段
						infoItemModel.setIsView(YES);									//是否显示字段
						
					}else {
						infoItemModel.setInfoItemSeconddisplay(workFlowName);
						infoItemModel.setInfoItemLength("500");
					}
					
					infoItemModel.setInfoItemDatatype("0");
					
					infoItemModel.setToaSysmanageStructure(struct);
					infoItemModel.setInfoItemState(YES); // 构建状态
					infoItemModel.setInfoItemProset("1");// 读写状态
					infoItemModel.setInfoItemFlag(NO);// 必填状态
					infoItemModel.setInfoItemOrderby(new Long("0")); // 排序字段
				
					manager.saveInfoItem(infoItemModel);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * author:zhangli description:获取构建SQL语句 modifyer: description:
	 * 
	 * @param tableName
	 *            表名
	 * @param key
	 *            主键
	 * @param pKey
	 *            外键
	 * @return
	 */
	private String strSqlToCreate(String tableName, String key, String pKey)
			throws SystemException, ServiceException {
		try {
			StringBuffer strSql = new StringBuffer(); // 最好用StringBuffer

			String URL = getCurrentURL();// 获取jdbc.properties文件中hibernate.connection.url的值

			if (URL.indexOf("microsoft") != -1
					|| URL.indexOf("sqlserver") != -1) {// SQLServer数据库
				strSql.append("create table ").append(tableName).append("(")
						.append(key).append(" varchar(32)  primary key,");

				if (pKey != null && !pKey.equals(""))
					strSql.append(pKey).append(" varchar(32) not null,");

				strSql
						.append("PERSON_CONFIG_FLAG  char(1),PERSON_OPERATE_DATE varchar(10),PERSON_OPERATER varchar(20),PERSON_DEMO varchar(4000),PERSON_FILENAME varchar(255)").append(sqlWorkflowInfoItem(tableName)).append(" )");
			} else if (URL.indexOf("mysql") != -1) {
				strSql.append("create table ").append(tableName).append("(")
						.append(key).append(" varchar(32)  primary key,");

				if (pKey != null && !pKey.equals(""))
					strSql.append(pKey).append(" varchar(32) not null,");

				strSql
						.append("PERSON_CONFIG_FLAG  char(1),PERSON_OPERATE_DATE varchar(10),PERSON_OPERATER varchar(20),PERSON_DEMO varchar(4000),PERSON_FILENAME varchar(255)").append(sqlWorkflowInfoItem(tableName)).append(" )");

			} else if (URL.indexOf("db2") != -1) {

			} else if (URL.indexOf("oracle") != -1) {
				strSql.append("create table ").append(tableName).append("(")
						.append(key).append(" varchar(32) primary key,");

				if (pKey != null && !pKey.equals(""))
					strSql.append(pKey).append(" varchar(32) not null,");

				strSql
						.append("PERSON_CONFIG_FLAG  char(1),PERSON_OPERATE_DATE Date,PERSON_OPERATER varchar(20),PERSON_DEMO BLOB,PERSON_FILENAME varchar(255) ").append(sqlWorkflowInfoItem(tableName)).append(" )");
			}
			return strSql.toString();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		}

	}

	/**
	 * 获取构建默认工作段字段的SQL语句
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午11:01:05 
	 * @param tableName
	 * @param key
	 * @param pKey
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	private String strSqlToCreateDate(String tableName)throws SystemException, ServiceException {
		try {
			StringBuffer strSql = new StringBuffer(); // 最好用StringBuffer
			String creatStr=sqlWorkflowInfoItem(tableName);																	//判断所要创建 的默认工作流字段是否为空
			if(creatStr!=null&&!creatStr.equals("")&&creatStr.length()>3){
				creatStr=creatStr.substring(1);
				String URL = getCurrentURL();// 获取jdbc.properties文件中hibernate.connection.url的值
				if (URL.indexOf("microsoft") != -1
						|| URL.indexOf("sqlserver") != -1) {// SQLServer数据库
					strSql.append("alter table ").append(tableName).append(" add (");
					
					
					strSql.append(creatStr).append(" )");
				} else if (URL.indexOf("mysql") != -1) {
					strSql.append("alter table ").append(tableName).append(" add (");
					
					strSql.append(creatStr).append(" )");
					
				} else if (URL.indexOf("db2") != -1) {
					
				} else if (URL.indexOf("oracle") != -1) {
					strSql.append("alter table ").append(tableName).append(" add (");					
					strSql.append(creatStr).append(" )");
				}				
			}else {
				
			}
			 
			return strSql.toString();
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		}
	
	}
	
	/**
	 * 初始化工作流中的业务数据字段
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午10:43:13 
	 * @param tableName       表名
	 * @return
	 * @throws SystemException
	 * @throws ServiceException2
	 */
	private String sqlWorkflowInfoItem(String tableName)throws SystemException, ServiceException {
		try {
			String sqlString="";
			List<String>list=manager.getSystemField();      //得到默认产生的工作流字段
			for(int i=0;i<list.size();i++){
				String workFlowName=list.get(i);
				Boolean boo=true;
				if(!(isHasTableCreate(tableName))){
					
					boo=manager.isHasInfoItemInTable(tableName, workFlowName);											//判断创建的表中是否创建了当前字段
				}
					
				if(boo){					
					if(workFlowName.equals(BaseWorkflowManager.WORKFLOW_NAME)){
						sqlString=sqlString+","+workFlowName+ " varchar(255)";
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_STATE)) {
						sqlString=sqlString+","+workFlowName+ " varchar(2)";					
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_AUTHOR)) {
						sqlString=sqlString+","+workFlowName+ " varchar(100)";
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_CODE)) {
						sqlString=sqlString+","+workFlowName+ " varchar(255)";
					}else if (workFlowName.equals(BaseWorkflowManager.WORKFLOW_TITLE)) {
						sqlString=sqlString+","+workFlowName+ " varchar(500)";
					}else {
						sqlString=sqlString+","+workFlowName+ " varchar(500)";
					}
				}
			}
		
		return sqlString;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "工作流默认字段" });
		}
		
	}
	
	/**
	 * 判断信息集值是否在表中创建
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午10:37:23 
	 * @param tableName
	 * @return
	 * 返回：  false;								//存在
	 * 		   true;								//不存在
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Boolean isHasTableCreate(String tableName) throws SystemException,ServiceException {
		try {
			Boolean boo=true;
			int num=0;
			ResultSet rsTables=null;
			//是否为MicrosoftSQL
			Boolean dateBasetype = DataBaseUtil.isMicrosoftSQL();
			if(dateBasetype){ //sql server
				rsTables=infoSetDao.executeJdbcQuery("select   count(*)   from   sys.tables   where   tables.name= '"+tableName+"'");
			}else{//oracle
			   rsTables=infoSetDao.executeJdbcQuery("select   count(*)   from   user_tables   where   table_name= '"+tableName+"'");
			}
			if(rsTables!=null&&rsTables.next()){
				num=rsTables.getInt(1);
				if(num>0){
					boo=false;//存在					
				}
			}
//			
//			if(rsTables.next()){
//				
//				return false;								//存在
//			}else{
//				return true;								//不存在
//			}
		return boo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "判断信息集表是否存在" });
		}
	}
	
	/**
	 * author:zhangli description:获取当前连接的数据库类型 modifyer: description:
	 * 
	 * @return
	 */
	public String getCurrentURL() throws SystemException, ServiceException {
		try {
			Properties properties = new Properties();
			URL in = this.getClass().getClassLoader().getResource(
					"appconfig.properties");
			properties.load(new FileInputStream(in.getFile()));
			// String URL = properties.getProperty("jndi.name");//
			// 获取jdbc.properties文件中hibernate.connection.url的值
			// if (URL == null || URL.equals("") || URL.equals("null"))
			String URL = properties.getProperty("jdbc.url");
			return URL;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "信息集" });
		}
	}

	/**
	 * 批量撤销构建信息集
	 * 
	 * @param infoSetCode信息集编号
	 * @return String
	 * @roseuid 4936763601E4
	 */
	public String DestoryInfoSet(String infoSetCode) throws SystemException,
			ServiceException {
		String[] str = infoSetCode.split(",");
		StringBuffer message = new StringBuffer();
		StringBuffer hasnotcreates = new StringBuffer();
		StringBuffer cannotcreates = new StringBuffer();
		StringBuffer syscreates = new StringBuffer();
		StringBuffer successcreates = new StringBuffer();
		StringBuffer successdelete = new StringBuffer();
		StringBuffer usedlist=new StringBuffer();
		try {
			for (int i = 0; i < str.length; i++) {// 循环要撤销构建的信息集
				ToaSysmanageStructure struct = infoSetDao.get(str[i]);// 根据编号获取信息集对象
				String state = struct.getInfoSetState();// 信息集构建状态
				String type = struct.getInfoSetType();// 信息集类型
				String sys = struct.getInfoSetIsSystem();
				if (NO.equals(state) || state == null || state.equals("null")) {// 如果是未构建状态，则不需撤销构建
					hasnotcreates.append(",").append(struct.getInfoSetName())
							.append("(").append(struct.getInfoSetValue())
							.append(")");
				} else if (YES.equals(type)) {// 如果是不可构建类型，则不需撤销构建
					cannotcreates.append(",").append(struct.getInfoSetName())
							.append("(").append(struct.getInfoSetValue())
							.append(")");
				} else if (YES.equals(sys)) { // 如果是系统信息集，则不需撤销构建
					syscreates.append(",").append(struct.getInfoSetName())
							.append("(").append(struct.getInfoSetValue())
							.append(")");
				} else { // 判断是否构建,是否可以构建的类型
					
					
					String tableName = struct.getInfoSetValue();
					int count=infoManagerService.isHasInfoFormUsed(tableName);
					if(count>0){
						usedlist.append(",").append(struct.getInfoSetName())
						.append("(").append(struct.getInfoSetValue())
						.append(")");
					}else {
						try{
						String dropSql = "drop table " + tableName;
						infoSetDao.executeJdbcUpdate(dropSql);// 删除构建的表
						// infoSetDao.getConnection().prepareStatement(dropSql).execute();
						struct.setInfoSetState(NO);
						infoSetDao.save(struct);// 将该信息集的构建状态设为“未构建”
						// manager.delInfoItemBySet(str[i]);
						dropSql=" delete from t_oa_post_property  where info_set_code='"+str[i] +"'";
						infoSetDao.executeJdbcUpdate(dropSql);// 删除自动生成的信息项
						
						dropSql = "delete from T_OA_SYSMANAGE_PROPERTY where" +
								" (INFO_ITEM_DATATYPE='15'" +
								" or INFO_ITEM_FIELD='PERSON_CONFIG_FLAG'" +
								" or INFO_ITEM_FIELD='PERSON_OPERATE_DATE'" +
								" or INFO_ITEM_FIELD='PERSON_OPERATER'" +
								" or INFO_ITEM_FIELD='PERSON_DEMO'" +
								" or INFO_ITEM_FIELD='WORKFLOWTITLE'" +
								" or INFO_ITEM_FIELD='WORKFLOWCODE'" +
								" or INFO_ITEM_FIELD='WORKFLOWAUTHOR'" +
								" or INFO_ITEM_FIELD='WORKFLOWSTATE'" +
								" or INFO_ITEM_FIELD='WORKFLOWNAME'" +
								" or INFO_ITEM_FIELD='PERSON_FILENAME') and INFO_SET_CODE='"
							+ str[i] + "'";
						infoSetDao.executeJdbcUpdate(dropSql);// 删除自动生成的信息项
						
						dropSql = "update T_OA_SYSMANAGE_PROPERTY  set INFO_ITEM_STATE='0' where INFO_SET_CODE='"
							+ str[i] + "'";
						infoSetDao.executeJdbcUpdate(dropSql);// 将该信息集下的信息项的构建状态设为“未构建”
						successcreates.append(",").append(struct.getInfoSetName())
						.append("(").append(struct.getInfoSetValue())
						.append(")");
						} catch (Exception e) {
							
							/** 信息集撤销  对于表不存在 捕捉异常处理
							 *
							 * 将该信息集以及该信息集下的信息项删除
							 * */
							if(e.getMessage().toString().indexOf("因为它不存在")!=-1){
								 String deleteinfoItenSql="delete T_OA_SYSMANAGE_PROPERTY where INFO_SET_CODE='"+str[i]+"'";
								 infoItemDao.executeJdbcUpdate(deleteinfoItenSql);
								  String deleteinfoSetSql="delete T_OA_SYSMANAGE_STRUCTURE where INFO_SET_CODE='"+str[i]+"'";
								  infoSetDao.executeJdbcUpdate(deleteinfoSetSql);
								  successdelete.append(",").append(struct.getInfoSetName())
								  .append("(").append(struct.getInfoSetValue())
									.append(")");
							}

						}
					}
					
				}// if条件结束
			}
			if (hasnotcreates.length() > 0) {
				message.append(hasnotcreates.toString().substring(1)).append(
						" 还未构建，不需撤销构建！\n");
			}
			if (cannotcreates.length() > 0) {
				message.append(cannotcreates.toString().substring(1)).append(
						" 为不可构建类型，无法撤销构建！\n");
			}
			if (syscreates.length() > 0) {
				message.append(syscreates.toString().substring(1)).append(
						" 为系统信息集，无法撤销构建！\n");
			}
			if (successcreates.length() > 0) {
				message.append(successcreates.toString().substring(1)).append(
						" 撤销构建成功！\n");
			}
			if (successdelete.length() > 0) {
				message.append(successdelete.toString().substring(1)).append(
						" 表不存在，撤销构建的同时删除该信息集及其信息项！\n");
			}
			if (usedlist.length() > 0) {
				message.append(usedlist.toString().substring(1)).append(
						" 已使用，不能撤消构建！\n");
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.destory_error,
					new Object[] { "信息集" });
		}
		return message.toString();
	}

	/*
	 * 
	 * Description:通过信息集编号获取对应已构建的叶子节点子信息集列表 
	 * param: infoSetName 信息集名 
	 * param: iscontainself 是否包含信息集本身
	 * @author 彭小青
	 * @date Sep 25, 2009 10:03:43 AM
	 */
	public List<ToaSysmanageStructure>  getChildCreatedInfoSet2(String infoSetValue,boolean iscontainself)
			throws SystemException, ServiceException {
		List<ToaSysmanageStructure> list=new ArrayList<ToaSysmanageStructure>();
		try {
			ToaSysmanageStructure struct = this
					.getInfoSetByInfoSetName(infoSetValue);
			String sql="";
			if (struct != null) {
				if(iscontainself){
					sql= "select * from(select * from t_oa_sysmanage_structure where INFO_SET_CODE = '"+struct.getInfoSetCode()+"' " +
							"union select * from t_oa_sysmanage_structure where INFO_SET_STATE='1' and (INFO_SET_ISEXISTCHILD is null or INFO_SET_ISEXISTCHILD='0') " +
							"start with INFO_SET_CODE = '"+struct.getInfoSetCode()+"' connect by prior INFO_SET_CODE= INFO_SET_PARENTID) order by INFO_SET_ORDERNO";			
				}else{
					sql="select * from t_oa_sysmanage_structure where INFO_SET_STATE='1' and (INFO_SET_ISEXISTCHILD is null or INFO_SET_ISEXISTCHILD='0') " +
							" start with INFO_SET_CODE = '"+struct.getInfoSetCode()+"'connect by prior INFO_SET_CODE= INFO_SET_PARENTID order by INFO_SET_ORDERNO";
				}
				ResultSet rs = infoSetDao.executeJdbcQuery(sql);
				ToaSysmanageStructure structtemp=null;
				while(rs.next()){
					structtemp=new ToaSysmanageStructure();
					structtemp.setInfoSetCode(rs.getString("INFO_SET_CODE"));
					structtemp.setInfoSetValue(rs.getString("INFO_SET_VALUE"));
					structtemp.setInfoSetName(rs.getString("INFO_SET_NAME"));
					structtemp.setInfoSetType(rs.getString("INFO_SET_TYPE"));
					structtemp.setInfoSetShort(rs.getString("INFO_SET_SHORT"));
					structtemp.setInfoSetIsexistchild(rs.getString("INFO_SET_ISEXISTCHILD"));
					structtemp.setInfoSetCondition(rs.getString("INFO_SET_CONDITION"));
					structtemp.setInfoSetState(rs.getString("INFO_SET_STATE"));
					structtemp.setInfoSetOrderno(rs.getLong("INFO_SET_ORDERNO"));
					structtemp.setInfoSetPkey(rs.getString("INFO_SET_PKEY"));
					structtemp.setInfoSetParentid(rs.getString("INFO_SET_PARENTID"));
					structtemp.setInfoSetIsSystem(rs.getString("INFO_SET_IS_SYSTEM"));
					list.add(structtemp);
				}
				rs.close();
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "已构建的叶子节点子信息集对象列表" });
		}
		return list;
	}

	/*
	 * Description: param: @author 彭小青 @date Sep 25, 2009 10:03:18 AM
	 */
	public ToaSysmanageStructure getInfoSetByInfoSetName(String infoSetValue)
			throws SystemException, ServiceException {
		ToaSysmanageStructure struct = null;
		try {
			List list = infoSetDao.find(
					"from ToaSysmanageStructure t where t.infoSetValue=?",
					infoSetValue);
			if (list != null && list.size() > 0) {
				struct = (ToaSysmanageStructure) list.get(0);
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "子信息集对象列表" });
		}
		return struct;
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 28, 2009 7:16:55 PM
	 */
	public List<ToaSysmanageStructure> getStructureByCon(String flag){
		try {
			List list = infoSetDao.find("from ToaSysmanageStructure t where t.infoSetState=? and t.infoSetCondition=?","1",flag);
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "子信息集对象列表" });
		}
	}
	
	/*
	 * 
	 * Description:获取所有已构建的信息集
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 26, 2010 11:31:28 AM
	 */
	public List<ToaSysmanageStructure> getCreatedStructure(){
		try {
			List<ToaSysmanageStructure> list = infoSetDao.find("from ToaSysmanageStructure t where t.infoSetState=? ","1");
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取所有已构建的信息集" });
		}
	}

	@Autowired
	public void setManager(InfoItemManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setFormManagerService(IFormManagerService formManagerService) {
		this.formManagerService = formManagerService;
	}

	@Autowired
	public void setInfoManagerService(IInfoManagerService infoManagerService) {
		this.infoManagerService = infoManagerService;
	}
}
