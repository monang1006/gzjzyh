package com.strongit.oa.common.business.jbpmbusiness;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.axis.utils.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 业务数据管理类
 * 
 * @author yanjian
 * @company Strongit Ltd. (C) copyright
 */
@Service
@Transactional
@OALogger
public class JBPMBusinessManager {

	private GenericDAOHibernate<Tjbpmbusiness, java.lang.String> jbpmbusinessDao;

	public static Class Manager_Bean_CLASS = Tjbpmbusiness.class;

	@Autowired
	SendDocManager manager;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 注入SessionFactory
	 * 
	 * @param session
	 *            会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		jbpmbusinessDao = new GenericDAOHibernate<Tjbpmbusiness, java.lang.String>(
				session, Tjbpmbusiness.class);
	}

	private void save(Tjbpmbusiness model) {
		jbpmbusinessDao.merge(model);
	}

	/**
	 * 根据id获取数据
	 * 
	 * @author 严建
	 * @param id
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 5, 2012 11:00:46 AM
	 */
	public Tjbpmbusiness get(String id) throws DAOException, ServiceException,
			SystemException {
		StringBuilder hql;
		try {
			hql = new StringBuilder("from Tjbpmbusiness t where t.id = ?");
			List list = jbpmbusinessDao.find(hql.toString(), id);
			if (list != null && !list.isEmpty()) {
				return (Tjbpmbusiness) list.get(0);
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 
	 * 保存工作流和业务关联的中间表（用于解决流程关联多张业务数据情况下的数据查询缓慢的性能问题）
	 * 
	 * @author 严建
	 * @param model
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 5, 2012 11:06:49 AM
	 */
	public void saveModel(Tjbpmbusiness model) throws DAOException,
			ServiceException, SystemException {
		try {
			updateBusinessInfo(model);
			String businessId = model.getBusinessId();
			String id = existByBusinessId(businessId);
			if (id != null) {// 表中存在相应的数据,将id赋值给model
				model.setId(id);
			}
			save(model);

		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 
	 * 根据业务id判断数据是否存在，并返回数据的主键值
	 * 
	 * @author 严建
	 * @param businessId
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 5, 2012 12:27:17 PM
	 */
	public String existByBusinessId(String businessId) throws DAOException,
			ServiceException, SystemException {
		StringBuilder hql;
		try {
			if (businessId == null || businessId.equals("")) {
				logger.info("方法existByBusinessId的参数businessId不能为空或空字符串");
				throw new ServiceException(
						"方法existByBusinessId的参数businessId不能为空或空字符串");
			}
			hql = new StringBuilder(
					"select t.id from Tjbpmbusiness t where t.businessId = ?");
			List list = jbpmbusinessDao.find(hql.toString(), businessId);
			if (list != null && !list.isEmpty()) {
				return (String) list.get(0);
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 数据库中是否存在指定字段<br/> 前提：Manager_Bean_CLASS的字段对应的get方法设置了@Column
	 * 
	 * @author 严建
	 * @param DBFieldName
	 * @return
	 * @createTime Mar 8, 2012 5:56:28 PM
	 */
	public static boolean managerBeanClassExistDBFieldName(String DBFieldName) {
		Column type = null;
		Method[] methods = Manager_Bean_CLASS.getDeclaredMethods();
		for (Method method : methods) {
			type = method.getAnnotation(Column.class);
			if (type != null
					&& type.name().toUpperCase().equals(
							DBFieldName.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数据库中是否存在指定字段<br/> 前提：Manager_Bean_CLASS的字段对应的get方法设置了@Column
	 * 
	 * @author 严建
	 * @param BeanFieldName
	 * @return
	 * @createTime Mar 8, 2012 5:56:28 PM
	 */
	public static boolean managerBeanClassExistBeanFieldName(
			String BeanFieldName) {
		Field[] fields = Manager_Bean_CLASS.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(BeanFieldName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数据库中中对应的表名<br/> 前提：Manager_Bean_CLASS的字段对应的get方法设置了@Table
	 * 
	 * @author 严建
	 * @return
	 * @createTime Mar 8, 2012 6:02:49 PM
	 */
	@SuppressWarnings("unchecked")
	public static String getManagerBeanClassTableName() {
		Table table = (Table) Manager_Bean_CLASS.getAnnotation(Table.class);
		return table.name();
	}

	/**
	 * 查找指定字段的数据
	 * 
	 * @author 严建
	 * @param fieldName
	 * @return
	 * @createTime Mar 8, 2012 5:16:17 PM
	 */
	public List getFieldValue(String fieldName) {
		try {
			return getFieldValue(fieldName, null);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 根据业务id查找指定字段的数据
	 * 
	 * @author 严建
	 * @param fieldName
	 *            Manager_Bean_CLASS类字段名称
	 * @param businessId
	 *            业务id
	 * @return
	 * @createTime Mar 8, 2012 5:16:17 PM
	 */
	private List getFieldValue(String fieldName, String businessId) {
		StringBuilder hql;
		try {
			if (!managerBeanClassExistBeanFieldName(fieldName)) {
				throw new DAOException("类" + Manager_Bean_CLASS + "找不到指定字段："
						+ fieldName);
			}
			hql = new StringBuilder("select " + fieldName
					+ " from Tjbpmbusiness ");
			if (businessId != null && !"".equals(businessId)) {
				hql.append(" where businessId = '" + businessId + "' ");
			}
			List list = jbpmbusinessDao.find(hql.toString());
			if (list != null && !list.isEmpty()) {
				return list;
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 根据业务id获取实体信息
	 * 
	 * @author 严建
	 * @param businessId
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Mar 8, 2012 11:28:43 PM
	 */
	public Tjbpmbusiness findByBusinessId(String businessId)
			throws DAOException, ServiceException, SystemException {
		StringBuilder hql;
		try {
			if (businessId == null || businessId.equals("")) {
				logger.info("方法findByBusinessId的参数businessId不能为空或空字符串");
				throw new ServiceException(
						"方法findByBusinessId的参数businessId不能为空或空字符串");
			}
			hql = new StringBuilder("from Tjbpmbusiness t where t.businessId = ?");
			List list = jbpmbusinessDao.find(hql.toString(), businessId);
			if (list != null && !list.isEmpty()) {
				return (Tjbpmbusiness) list.get(0);
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 获取主办人员信息
	 * 
	 * @author 严建
	 * @param businessId
	 * @return
	 * @createTime Mar 8, 2012 6:32:04 PM
	 */
	public String getMainActorInfo(String businessId) {
		List list = getFieldValue("mainActorInfo", businessId);
		if (list != null && !list.isEmpty()) {
			return list.get(0).toString();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 仅用于更新业务表中的共性字段
	 * 
	 * @author 严建
	 * @param model
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 5, 2012 12:17:37 PM
	 */
	@SuppressWarnings("unchecked")
	protected void updateBusinessInfo(Tjbpmbusiness model,
			Map fieldNameMapdefault) throws DAOException, ServiceException,
			SystemException {
		ResultSet rs = null;
		try {
			if (model.getBusinessId() == null
					|| model.getBusinessId().equals("")) {
				logger.info("jbpmbusinesss属性businessId不能为空或空字符串");
				throw new ServiceException("jbpmbusinesss属性businessId不能为空或空字符串");
			}

			String[] tableInfo = model.getBusinessId().split(";");
			String tableName = tableInfo[0];
			String pkName = tableInfo[1];
			String pkValue = tableInfo[2];
			StringBuilder SQL = new StringBuilder();
			StringBuilder selectStr = new StringBuilder();
			List<String> parmlist = new ArrayList<String>(fieldNameMapdefault
					.keySet());
			Map<String, Boolean> map = manager.existColumsInTable(
					tableName, parmlist);
			List<String> keys = null;
			if (map != null || !map.isEmpty()) {
				keys = new ArrayList<String>(map.keySet());
			}
			if (keys != null) {
				for (int i = 0; i < keys.size(); i++) {
					String key = keys.get(i);
					if (map.get(key)) {
						selectStr.append(key + ",");
					}
					key = null;
				}
			}

			if (selectStr.length() == 0) {
				selectStr.append(" *,");
			}

			SQL.append("select ").append(
					selectStr.subSequence(0, selectStr.length() - 1)).append(
					" from ").append(tableName).append(" where ")
					.append(pkName).append(" = '").append(pkValue).append("'");
			logger.info(SQL.toString());
			rs = jbpmbusinessDao.executeJdbcQuery(SQL.toString());
			if (rs.next()) {
				Object fieldValue = null;
				for (String fieldName : parmlist) {
					if (map.get(fieldName) && rs.getString(fieldName) != null) {
						fieldValue = rs.getString(fieldName);
					} else {
						fieldValue = fieldNameMapdefault.get(fieldName);
					}
					BusinessRule.setModelBean(model, fieldName, fieldValue);

				}
			} else {
				logger.info("找不到表名为" + tableName + "主键列名为" + pkName + "主键值为"
						+ pkValue + "的记录");
				throw new DAOException("找不到表名为" + tableName + "主键列名为" + pkName
						+ "主键值为" + pkValue + "的记录");
			}

		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.warn(JavaUtils.stackToString(ex));
				}
			}
		}
	}

	/**
	 * 
	 * 仅用于更新业务表中的共性字段
	 * 
	 * @author 严建
	 * @param model
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 5, 2012 12:17:37 PM
	 */
	private void updateBusinessInfo(Tjbpmbusiness model) throws DAOException,
			ServiceException, SystemException {
		try {
			Map<String, Object> fieldNameMapdefault = new HashMap<String, Object>();
			fieldNameMapdefault.put(BusinessRule.PERSON_CONFIG_FLAG, "0");
			fieldNameMapdefault.put(BusinessRule.END_TIME, null);
			fieldNameMapdefault.put(BusinessRule.RECV_NUM, null);
			fieldNameMapdefault.put(BusinessRule.DOC_NUMBER, null);
			fieldNameMapdefault.put(BusinessRule.ISSUE_DEPART_SIGNED, null);
			updateBusinessInfo(model, fieldNameMapdefault);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 
	 * 根据业务id，获取表中记录的指定列的值（前提条件是表中存在该列）
	 * 
	 * @author 严建
	 * @param businessId
	 *            业务id
	 * @param ColumName
	 *            列名
	 * @return
	 * @createTime Feb 5, 2012 4:54:21 PM
	 */
	@SuppressWarnings("unused")
	private Object getResultObject(String businessId, String ColumName) {
		ResultSet rs = null;
		try {
			if (businessId == null || businessId.equals("")) {
				logger.info("businessId不能为空或空字符串");
				throw new ServiceException(
						"getResultObject()参数businessId不能为空或空字符串");
			}

			String[] tableInfo = businessId.split(";");
			String tableName = tableInfo[0];
			String pkName = tableInfo[1];
			String pkValue = tableInfo[2];
			StringBuilder SQL = new StringBuilder();
			SQL.append("select t.").append(ColumName).append(" from ").append(
					tableName).append(" t where t.").append(pkName).append(
					" = '").append(pkValue).append("'");
			logger.info(SQL.toString());
			rs = jbpmbusinessDao.executeJdbcQuery(SQL.toString());
			if (rs.next()) {
				return rs.getObject(1);
			} else {
				logger.info("找不到表名为" + tableName + "主键列名为" + pkName + "主键值为"
						+ pkValue + "的记录");
				throw new DAOException("找不到表名为" + tableName + "主键列名为" + pkName
						+ "主键值为" + pkValue + "的记录");
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.warn(JavaUtils.stackToString(ex));
				}
			}
		}
	}

	private static class BusinessRule {
		/**
		 * 紧急字段
		 */
		public static final String PERSON_CONFIG_FLAG = "PERSON_CONFIG_FLAG";
		public static final String RECV_NUM = "RECV_NUM";
		public static final String DOC_NUMBER = "DOC_NUMBER";
		public static final String ISSUE_DEPART_SIGNED = "ISSUE_DEPART_SIGNED";

		/**
		 * 公文限时章得数据
		 */
		public static final String END_TIME = "END_TIME";

		public static final String BUSINESS_TYPE = "BUSINESS_TYPE";


		public enum SetRule {
			PERSON_CONFIG_FLAG(BusinessRule.PERSON_CONFIG_FLAG), END_TIME(
					BusinessRule.END_TIME),  BUSINESS_TYPE(
					BusinessRule.BUSINESS_TYPE),
					RECV_NUM(BusinessRule.RECV_NUM),
					DOC_NUMBER(BusinessRule.DOC_NUMBER),
					ISSUE_DEPART_SIGNED(BusinessRule.ISSUE_DEPART_SIGNED);
			private final String value;

			public String getValue() {
				return value;
			}

			// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
			SetRule(String value) {
				this.value = value;
			}

		}

		/**
		 * 设置模型的值
		 * 
		 * @author 严建
		 * @param model
		 * @param fieldName
		 *            字段名称
		 * @param fieldValue
		 *            字段值
		 * @createTime Mar 9, 2012 12:10:58 AM
		 */
		public static void setModelBean(Tjbpmbusiness model, String fieldName,
				Object fieldValue) {
			switch (getSetRule(fieldName)) {
			case PERSON_CONFIG_FLAG:
				model.setPersonConfigFlag(StringUtil.castString(fieldValue));
				break;
			case END_TIME:
				model.setEndTime(StringUtil.castString(fieldValue));
				break;
			case RECV_NUM:
				model.setRecvNum(StringUtil.castString(fieldValue));
				break;
			case DOC_NUMBER:
				model.setDocNumber(StringUtil.castString(fieldValue));
				break;
			case ISSUE_DEPART_SIGNED:
				model.setIssueDepartSigned(StringUtil.castString(fieldValue));
				break;
			}
		}

		/**
		 * 获取枚举类型
		 * 
		 * @author 严建
		 * @param fieldName
		 * @return
		 * @createTime Mar 9, 2012 12:02:05 AM
		 */
		public static SetRule getSetRule(String fieldName) {
			try {
				SetRule result = null;
				for (SetRule setrule : SetRule.values()) {
					if (setrule.value.equals(fieldName)) {
						result = setrule;
					}
				}
				if (result == null) {
					throw new ServiceException();
				}
				return result;
			} catch (DAOException e) {
				throw e;
			} catch (ServiceException e) {
				throw e;
			} catch (SystemException e) {
				throw e;
			} catch (Exception e) {
				throw new SystemException(e);
			}

		}

	}
}
