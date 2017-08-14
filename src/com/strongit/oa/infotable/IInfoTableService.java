/*
 * Copyright : Jiang Xi Strong Co.. Ltd.
 * All right reserved.
 * JDK 1.5.0_14;Struts：2.1.2;Spring 2.5.6;Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息表管理通用接口
 */
package com.strongit.oa.infotable;

import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

public interface IInfoTableService {

	/**
	 * 获取信息列表
	 * 
	 * @return java.util.List
	 * @roseuid 4947236102C4
	 */
	public List getTableList()throws SystemException,ServiceException;

	/**
	 * 获取信息分页列表
	 * 
	 * @param dataRowTitle
	 *            表头列表
	 * @param struct
	 *            信息集编号
	 * @param sql
	 *            查询语句
	 * @param page
	 *            分页对象
	 * @return com.strongmvc.orm.hibernate.Page
	 * @roseuid 494723C503CD
	 */
	public Page<List> getTablePage(List dataRowTitle, String struct, String sql,
			Page<List> page)throws SystemException,ServiceException;

	/**
	 * 初始化添加信息
	 * 
	 * @param struct
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 494723FE0295
	 */
	public List initTableAdd(String struct)
			throws SystemException,ServiceException;

	/**
	 * 初始化编辑信息
	 * 
	 * @param struct
	 *            信息集编号
	 * @param keyid
	 *            主键值
	 * @return java.util.List
	 * @roseuid 4947240A02B4
	 */
	public List initTableEdit(String struct, String keyid)
			throws SystemException,ServiceException;

	/**
	 * 添加信息
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param struct
	 *            信息集编号
	 * @return java.lang.String
	 * @roseuid 4947241B02C4
	 */
	public String addTable(HttpServletRequest request, String struct)
			throws SystemException,ServiceException;

	/**
	 * 编辑信息
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param struct
	 *            信息集编号
	 * @return java.lang.String
	 * @roseuid 494724210034
	 */
	public String editTable(HttpServletRequest request, String struct)
			throws SystemException,ServiceException;

	/**
	 * 删除信息
	 * 
	 * @param struct
	 *            信息集编号
	 * @param delids
	 *            要删除的信息集编号串
	 * @return java.lang.String
	 * @roseuid 4947242A0014
	 */
	public String delTable(String struct, String delids)
			throws SystemException,ServiceException;

	/**
	 * 获取表头列表
	 * 
	 * @param struct
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 494A40DE02BF
	 */
	public List getTableTitle(String struct)
			throws SystemException,ServiceException;
	
	/**
	 * 获取选择列
	 * 
	 * @param struct
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 494A47BA000F
	 */
	public List getSelectItems(String struct)
			throws SystemException,ServiceException;
	
	/**
	 * 通过信息项值获取表头列表
	 * 
	 * @param infoSetValues
	 *            信息集编号
	 * @param infoSetValues
	 *            信息项值
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public List getTableTitle(String struct,String infoSetValues)
			throws SystemException,ServiceException;

	/**
	 * author:zhangli description:获取父信息集名称 modifyer: description:
	 * 
	 * @param struct
	 *            信息集编号
	 * @return
	 */
	public String getParentStructName(String struct)
			throws SystemException,ServiceException;

	/**
	 * author:zhangli description:获取信息集名称 modifyer: description:
	 * 
	 * @param struct
	 *            信息集编号
	 * @return
	 */
	public String getStructName(String struct)
			throws SystemException,ServiceException;

	/**
	 * author:zhangli description:通过信息集主键获取信息集对象 modifyer: description:
	 * 
	 * @param infosetCode
	 *            信息集编号
	 * @return
	 */
	public ToaSysmanageStructure getInfoSet(String infosetCode)
			throws SystemException,ServiceException;

	/**
	 * author:zhangli description:通过信息集值获取信息集对象 modifyer: description:
	 * 
	 * @param infosetValue
	 *            信息集值
	 * @return
	 */
	public ToaSysmanageStructure getInfoSetByValue(String infosetValue)
			throws SystemException,ServiceException;

	/**
	 * author:zhangli description:通过信息集获取树节点列表 modifyer: description:
	 * 
	 * @param struct
	 *            信息集编号
	 * @param namepro
	 *            名称属性
	 * @param parentpro
	 *            对应父节点属性
	 * @param wheresql
	 *            查询语句
	 * @param tablename
	 *            表名
	 * @param fpro
	 *            主键属性
	 * @return
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public List getTreeByStruct(String struct, String namepro,
			String parentpro, String wheresql, String tablename, String fpro)
			throws SystemException,ServiceException;
	
	/*
	 * 
	 * Description:综合查询模块查询符合添加的列表信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 27, 2010 8:49:49 PM
	 */
	public List<ToaReportBean> getRecordListByCondition(List dataRowTitle, String struct,
			String sql,String orderStr,String groupFiled) throws SystemException,ServiceException;
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 5, 2010 10:04:29 AM
	 */
	public Page<List> getTablePage(List dataRowTitle, String infosetvalue,String infosetpkey,
			String sql, Page<List> page) throws SystemException,ServiceException;
}
