/*
 * Copyright : Jiang Xi Strong Co.. Ltd.
 * All right reserved.
 * JDK 1.5.0_14;Struts：2.1.2;Spring 2.5.6;Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息表管理通用接口实现类
 */
package com.strongit.oa.infotable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class InfoTableService implements IInfoTableService {
	private InfoItemManager itemanager;

	private InfoSetManager infosetmanager;

	private DictItemManager dictitemanaget;

	private static final String VAR = "0"; // 字符类型

	private static final String CODE = "1"; // 代码类型

	private static final String NUM = "2"; // 数值类型

	private static final String YEAR = "3"; // 年份

	private static final String MONTH = "4"; // 年月

	private static final String DATE = "5"; // 年月日
	
	private static final String DATEDATE="6"; //年月日时间

	private static final String FILE = "10"; // 文件类型

	private static final String PHOTO = "11"; // 照片类型

	private static final String PHONE = "12"; // 电话类型

	private static final String TEXT = "13"; // 文本类型

	private static final String DES = "14"; // 备注

	private static final String KEY = "15"; // 主外键
	
	private static final int CLOMNSIZE=45;	//ToaReportBean的属性数量（报表使用）

	/**
	 * @roseuid 494A31D1032C
	 */
	public InfoTableService() {

	}

	/**
	 * Sets the value of the itemanager property.
	 * 
	 * @param aItemanager
	 *            the new value of the itemanager property
	 */
	@Autowired
	public void setItemanager(InfoItemManager aItemanager) {
		itemanager = aItemanager;
	}

	/**
	 * Sets the value of the infosetmanager property.
	 * 
	 * @param aInfosetmanager
	 *            the new value of the infosetmanager property
	 */
	@Autowired
	public void setInfosetmanager(InfoSetManager aInfosetmanager) {
		infosetmanager = aInfosetmanager;
	}

	@Autowired
	public void setDictitemanaget(DictItemManager dictitemanaget) {
		this.dictitemanaget = dictitemanaget;
	}

	/**
	 * 根据信息项类表、表名、主键属性和主键值获取一条信息列表
	 * 
	 * @param proList
	 *            信息项列表
	 * @param table
	 *            表名
	 * @param key
	 *            主键属性
	 * @param id
	 *            主键值
	 * @return java.util.List
	 * @throws SQLException
	 * @throws HibernateException
	 * @roseuid 494A3D1B032C
	 */
	private List getSingleTableValue(List proList, String table, String key,
			String id) throws SystemException,ServiceException{
		try{
			if (proList != null && proList.size() > 0) {
				String sql = "select ";

				for (int i = 0; i < proList.size(); i++) {// 根据属性列表定义查询语句中要查询的属性值

					ToaSysmanageProperty pro = (ToaSysmanageProperty) proList
							.get(i);
					sql = sql + pro.getInfoItemField() + ",";
				}

				sql = sql.substring(0, sql.length() - 1) + " from " + table
						+ "  where " + key + "='" + id + "'"; // 设置查询的表和查询条件（根据主健值查询）
				
				return getFiledValueList(proList, sql);
				
			} else
				return null;
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {id});
		}
	}

	/**
	 * 根据查询语句和信息项列表获取信息列表
	 * 
	 * @param proList
	 *            信息项列表
	 * @param sql
	 *            查询语句
	 * @return java.util.List
	 * @throws SQLException
	 * @throws HibernateException
	 * @roseuid 494A3DDA0232
	 */
	private List getFiledValueList(List proList, String sql)
			throws SystemException,ServiceException {
		try{
			ResultSet rs = itemanager.executeJdbcQuery(sql);
			String url = infosetmanager.getCurrentURL();
			if (rs.next()) { // 如果有查询结果
				for (int i = 0; i < proList.size(); i++) {// 循环获取信息项对象进行相应操作

					ToaSysmanageProperty pro = (ToaSysmanageProperty) proList
							.get(i);
					String infofield = pro.getInfoItemField();
					String type = pro.getInfoItemDatatype();
					String value = null;

					if (type.equals(NUM)) {// 属性类型为“数值”型时,返回数值
						value = String.valueOf(rs.getInt(infofield));
					} else if (type.equals(PHOTO) || type.equals(FILE)) {// 属性类型为“图片”型或文件类型时,返回空
						// value = rs.getString(infofield);

						if (url.indexOf("oracle") != -1)
							value = "";
						else if (url.indexOf("microsoft") != -1
								|| url.indexOf("sqlserver") != -1)
							value = rs.getString(infofield);

					} else
						value = rs.getString(infofield);
					if (value == null || value.equals("null"))
						value = "";
					if (type.equals(CODE) && value != null && !value.equals("")) {// 如果字段类型为代码引用型

						ToaSysmanageDictitem dictitem = dictitemanaget
								.getDictItem(value);

						if (dictitem != null) {// 如果字典项对象不为空,则获得该字典项编码及其中文描述
							value = value + "," + dictitem.getDictItemName();
							pro.setRefernceDesc(dictitem.getDictItemName());
						}
					}
					pro.setInfoItemValue(value);
				}

			}
			rs.close();
			return proList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.execute_error,               
					new Object[] {"\""+sql+"\" 语句"});
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException();
		}
	}

	/**
	 * 通过表名、表主健属性、表主健值、图片路径、对应字段名 修改表中某图片字段的图片
	 * 
	 * @param table
	 *            表名
	 * @param key
	 *            表主健属性
	 * @param id
	 *            表主健值
	 * @param file
	 *            文件
	 * @param pro
	 *            对应字段名
	 * @throws SQLException
	 * @throws IOException
	 */
	public void updateBlob(String table, String key, String id, String value,
			String pro) throws SystemException,ServiceException {
		try{
			if (value != null && !value.equals("")) {
				// 修改数据库中对应图片字段值为空BLOB值
				itemanager.executeJdbcUpdate("update " + table + " set " + pro
						+ "=empty_blob() where " + key + "='" + id + "'");

				StringBuffer sqlstr = new StringBuffer("select ").append(pro)
						.append(" from ").append(table).append("  where ").append(
								key + "='").append(id).append("'"); // 设置查询的表和查询条件（根据主健值查询）

				ResultSet rs = itemanager.executeJdbcQuery(sqlstr.toString());
				if (rs.next()) {
					Blob blob = rs.getBlob(1);
					URL url = this.getClass().getClassLoader().getResource("/"); // 动态得到classload.xml文件的绝对路径
					String strPath = url.getPath();
					String fileName = strPath + value;// 得到服务器地址
					OutputStream outStream = blob.setBinaryStream(0);// 构造BLOB对象的输出字节流
					// FileOutputStream fos= (FileOutputStream)
					// blob.getBinaryOutputStream();
					byte[] b = new byte[1024];
					int len = 0;
					File file = new File(fileName);// 通过文件路径得到相应文件对象
					InputStream fin = new FileInputStream(file);// 构造该文件的输入字节流
					while ((len = fin.read(b)) != -1) {// 将文件对象通过字节流保存入数据库
						outStream.write(b, 0, len);
					}
					fin.close();
					outStream.flush();
					outStream.close();
					file.delete();
				}
				rs.close();
			}
		}catch(ServiceException e){
			throw new ServiceException("图片修改失败");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new SystemException();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			throw new SystemException();
		}
	}

	/**
	 * 保存信息
	 * 
	 * @param provalueList
	 *            信息项列表
	 * @param struct
	 *            信息集编号
	 * @return java.lang.String
	 * @throws SQLException
	 * @throws IOException 
	 * @roseuid 494A43FB03B9
	 */
	private String saveSingleTableValue(List provalueList, String struct)
		throws SystemException,ServiceException {
		try{
			ToaSysmanageStructure structobj = infosetmanager.getInfoSet(struct);
			// 获取对应表中的最大主键值后一个主键值
			String table = structobj.getInfoSetValue();
			String key = structobj.getInfoSetPkey();
			UUIDGenerator uuidgenerator = new UUIDGenerator();
			String primaryKeyValue = String.valueOf(uuidgenerator.generate());

			String url = infosetmanager.getCurrentURL();
			// 获取对应表的主键字段
			//System.out
			//		.println("*******************************************************"
			//				+ primaryKeyValue + "**********************");

			List<ToaSysmanageProperty> BlobList = new ArrayList<ToaSysmanageProperty>();

			String sql = "insert into " + table + "(" + key + ",";// 修改表

			for (int i = 0; i < provalueList.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) provalueList
						.get(i);
				if (!pro.getInfoItemField().equals(key))
					sql = sql + pro.getInfoItemField() + ",";// 根据属性插入
			}

			sql = sql.substring(0, sql.length() - 1) + ") values('"
					+ primaryKeyValue + "',";

			for (int i = 0; i < provalueList.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) provalueList
						.get(i);
				String value = pro.getInfoItemValue();
				String type = pro.getInfoItemDatatype();

				if (pro.getInfoItemField().equals(key))
					;
				else if (type.equals("15")) {
					if (value == null || value.equals(""))
						value = "0";

					sql = sql + "'" + value + "',";// 插入具体值
				} else if (type.equals(NUM)) {// 如果为数值类型

					if (value == null || value.equals(""))
						value = "0";

					sql = sql + value + ",";// 插入具体值
				} else if (type.equals(YEAR)) {// 如果为日期类型

					if (value == null || value.equals(""))
						sql = sql + "'',";// 根据属性修改表中具体记录
					else if (url.indexOf("oracle") != -1)
						sql = sql + "to_date('" + value + "','yyyy'),";// 根据属性修改表中具体记录
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
				} else if (type.equals(MONTH)) {// 如果为日期类型

					if (value == null || value.equals(""))
						sql = sql + "'',";// 根据属性修改表中具体记录
					else if (url.indexOf("oracle") != -1)
						sql = sql + "to_date('" + value + "','yyyy-MM'),";// 根据属性修改表中具体记录
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
				} else if (type.equals(DATE)) {// 如果为日期类型

					if (value == null || value.equals(""))
						sql = sql + "'',";// 根据属性修改表中具体记录
					else if (url.indexOf("oracle") != -1)
						sql = sql + "to_date('" + value + "','yyyy-MM-dd'),";// 根据属性修改表中具体记录
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
				} else if (type.equals(PHOTO) || type.equals(FILE)) {// 如果为图片类型或文件类型
					// sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
					if (url.indexOf("oracle") != -1){
						sql = sql +"empty_blob(),";
						BlobList.add(pro);
					}
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + "'" + value + "',";// 根据属性修改表中具体记录
				} else
					sql = sql + "'" + value + "',";// 插入具体值
			}
			sql = sql.substring(0, sql.length() - 1) + ")";// 设置查询条件
			//System.out.println("sqlstr:" + sql);

			itemanager.executeJdbcUpdate(sql.toString());// 插入除图片类型字段外所有的字段值记录

			for (int i = 0; i < BlobList.size(); i++) {// 循环将所有图片字段的图片进行修改
				ToaSysmanageProperty pro = (ToaSysmanageProperty) BlobList.get(i);
				updateBlob(table, key, primaryKeyValue, pro.getInfoItemValue(), pro
						.getInfoItemField());
			}

			// itemanager.executeJdbcUpdate("update T_BASE_KEYNAME set
			// KEY_VALUE="+primaryKeyValue+" where KEY_TABLENAME='"+table+"'",
			// null);
			return primaryKeyValue;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error);
		}
	}

	/**
	 * 修改信息
	 * 
	 * @param provalueList
	 *            信息项编号
	 * @param table
	 *            表名
	 * @param key
	 *            主键属性
	 * @param id
	 *            主键值
	 * @return java.lang.String
	 * @throws SQLException
	 * @throws IOException 
	 * @roseuid 494A469E0109
	 */
	private String updateSingleTableValue(List provalueList, String table,
			String key, String id) throws SystemException,ServiceException {
		try{
			String url = infosetmanager.getCurrentURL();
			String sql = "update " + table + "  set ";// 修改表

			for (int i = 0; i < provalueList.size(); i++) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) provalueList
						.get(i);
				String value = pro.getInfoItemValue();
				String type = pro.getInfoItemDatatype();
				String name = pro.getInfoItemField();
				if (type.equals(NUM)) {// 如果为数值类型

					if (value == null || value.equals(""))
						value = "0";

					sql = sql + name + "=" + value + ",";// 根据属性修改表中具体记录
				} else if (type.equals(YEAR)) {// 如果为日期类型

					if (value == null || value.equals(""))
						sql = sql + name + "='',";// 根据属性修改表中具体记录
					else if (url.indexOf("oracle") != -1)
						sql = sql + name + "=to_date('" + value + "','yyyy'),";// 根据属性修改表中具体记录
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
				} else if (type.equals(MONTH)) {// 如果为日期类型

					if (value == null || value.equals(""))
						sql = sql + name + "='',";// 根据属性修改表中具体记录
					else if (url.indexOf("oracle") != -1)
						sql = sql + name + "=to_date('" + value + "','yyyy-MM'),";// 根据属性修改表中具体记录
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
				} else if (type.equals(DATE)) {// 如果为日期类型

					if (value == null || value.equals(""))
						sql = sql + name + "='',";// 根据属性修改表中具体记录
					else if (url.indexOf("oracle") != -1)
						sql = sql + name + "to_date('" + value + "','yyyy-MM-dd'),";// 根据属性修改表中具体记录
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
				} else if (type.equals(PHOTO) || type.equals(FILE)) {// 如果为图片类型或文件类型
					// sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录

					if (url.indexOf("oracle") != -1)
						updateBlob(table, key, id, pro.getInfoItemValue(), pro
								.getInfoItemField());
					else if (url.indexOf("microsof") != -1
							|| url.indexOf("sqlserver") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
					else if (url.indexOf("mysql") != -1)
						sql = sql + name + "='" + value + "',";// 根据属性修改表中具体记录
				} else
					sql = sql + name + "='" + value + "',";// 插入具体值
			}

			sql = sql.substring(0, sql.length() - 1) + " where " + key + "='" + id
					+ "'";// 设置查询条件
			// sql=datesty.getSql(sql);//通过日期格式处理类将SQL语句中的日期格式进行处理
			//System.out.println("sqlstr:" + sql);
			itemanager.executeJdbcUpdate(sql);// 执行修改语句
			return null;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error);
		}
	}

	/**
	 * 获取信息列表
	 * 
	 * @return java.util.List
	 * @roseuid 494A47B80399
	 */
	public List getTableList() {
		return null;
	}

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
	 * @roseuid 494A47B803B9
	 */
	public Page<List> getTablePage(List dataRowTitle, String struct,
			String sql, Page<List> page) throws SystemException,ServiceException{
		try{
			ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
			String infosetpkey = infoset.getInfoSetPkey();
			String infosetvalue = infoset.getInfoSetValue();
			int currentpage = page.getPageNo();
			int recordsperpage = page.getPageSize();
			int totalRecord = 0;
			StringBuffer sqlstr = new StringBuffer("select count(" + infosetpkey
					+ ") from " + infosetvalue);
			// sql.append(" t,T_GS_COMPANY_INFO c");
			if (sql != null && !"".equals(sql)) {
				sqlstr.append(" where ").append(sql);
			}

			ResultSet rs;
			try {
				rs = itemanager.executeJdbcQuery(sqlstr.toString());
				if (rs.next())
					totalRecord = rs.getInt(1);
				rs.close();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rs = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rs = null;
			}

			// int totalPage = (totalRecord % recordsperpage == 0) ? totalRecord
			// / recordsperpage : (totalRecord / recordsperpage) + 1;
			if (currentpage == 0)
				currentpage = 1;
			page.setPageNo(currentpage);
			int start = (currentpage - 1) * recordsperpage + 1;
			int end = currentpage * recordsperpage;
			page.setTotalCount(totalRecord);

			sqlstr = buildSelectSql(dataRowTitle.iterator(), infosetpkey,
					infosetvalue, sql, start, end);
			if (sqlstr == null){
				page.setResult(null);				
			}
			
			if(page.isOrderBySetted()) {
				sqlstr.append(" ").append(page.getOrderBy()); 
			}
			
			rs = itemanager.executeJdbcQuery(sqlstr.toString());
			page.setResult(getTableFormData(dataRowTitle, rs));
			
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}catch (HibernateException e) {
			// TODO Auto-generated catch block
			throw new DAOException();
		}
	}

	/**
	 * author:zhangli description:构建分页信息列表查询语句 modifyer: description:
	 * 
	 * @param tableFormTitle
	 *            表头列表
	 * @param infosetpkey
	 *            主键属性
	 * @param infosetvalue
	 *            表名
	 * @param condition
	 *            查询条件
	 * @param start
	 *            查询开始位置
	 * @param end
	 *            查询结束位置
	 * @return
	 */
	private StringBuffer buildSelectSql(Iterator tableFormTitle,
			String infosetpkey, String infosetvalue, String condition,
			int start, int end) throws SystemException,ServiceException{
		try{
			ToaSysmanageProperty field = new ToaSysmanageProperty();
			StringBuffer sql = new StringBuffer();
			StringBuffer fieldStrBuffer = new StringBuffer();
			String fieldStr = "";
			String url = infosetmanager.getCurrentURL();
			if (url.indexOf("oracle") != -1) {
				sql.append("select * from(select rownum rn,").append(infosetpkey)
						.append(",");
				while (tableFormTitle.hasNext()) {
					field = (ToaSysmanageProperty) tableFormTitle.next();
					String datatype = field.getInfoItemDatatype();
					if (!KEY.equals(datatype)) {
						fieldStrBuffer.append(field.getInfoItemField() + ",");
					}
				}
				fieldStr = fieldStrBuffer.toString();
				if (fieldStr == null || "".equals(fieldStr))
					return null;
				sql.append(fieldStr.substring(0, fieldStr.length() - 1));
				sql.append(" from " + infosetvalue);
				//if (infosetvalue.equals("T_GS_PERSON"))
				//	sql.append(" t,T_GS_COMPANY_INFO c");
				if (condition != null && !"".equals(condition)) {
					sql.append(" where " + condition);
				}
				sql.append(")m where m.rn between " + start + " and " + end);
			} else if (url.indexOf("microsoft") != -1
					|| url.indexOf("sqlserver") != -1) {
				sql.append("select top " + (end - start) + " " + infosetpkey + ",");
				while (tableFormTitle.hasNext()) {
					field = (ToaSysmanageProperty) tableFormTitle.next();
					if (!KEY.equals(field.getInfoItemDatatype()))
						fieldStrBuffer.append(field.getInfoItemField() + ",");
				}
				fieldStr = fieldStrBuffer.toString();
				if (fieldStr == null || "".equals(fieldStr))
					return null;
				sql.append(fieldStr.substring(0, fieldStr.length() - 1));
				sql.append(" from " + infosetvalue);
				// sql.append(" t,T_GS_COMPANY_INFO c");
				sql.append(" where(" + infosetpkey + " not in (select top "
						+ (start - 1) + " " + infosetpkey + " from " + infosetvalue
						+ "))");
				if (condition != null && !"".equals(condition)) {
					sql.append(" and " + condition);
				}
			}else if (url.indexOf("mysql") != -1) {
				int pagesize=end-start+1;
				int startrecode=start-1;
				sql.append("select" + " " + infosetpkey + ",");
				while (tableFormTitle.hasNext()) {
					field = (ToaSysmanageProperty) tableFormTitle.next();
					if (!KEY.equals(field.getInfoItemDatatype()))
						fieldStrBuffer.append(field.getInfoItemField() + ",");
				}
				fieldStr = fieldStrBuffer.toString();
				if (fieldStr == null || "".equals(fieldStr))
					return null;
				sql.append(fieldStr.substring(0, fieldStr.length() - 1));
				sql.append(" from " + infosetvalue);
			/*	sql.append(" where(" + infosetpkey + " not in (select top "
						+ (start - 1) + " " + infosetpkey + " from " + infosetvalue
						+ "))");*/
				if (condition != null && !"".equals(condition)) {
					sql.append(" where " + condition);
				}
				sql.append(" LIMIT " + startrecode + ","+pagesize);
			}
			return sql;
		}catch(ServiceException e){
			throw new ServiceException("构建SQL语句失败");
		}
	}

	/**
	 * author:zhangli description:根据信息项列表和结果集构造信息结果列表 modifyer: description:
	 * 
	 * @param tableFormTitle
	 *            信息项列表
	 * @param rs
	 *            结果集对象
	 * @return
	 */
	private List<List> getTableFormData(List tableFormTitle, ResultSet rs) 
			throws SystemException,ServiceException{
		List<List> tableFormDataRowlist = new ArrayList<List>();
		BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			while (rs.next()) {
				String url = infosetmanager.getCurrentURL();
				//20090902添加
				int i=2;
				if (url.indexOf("oracle") != -1) {
				   i= 3;
				}
				List<ToaSysmanageProperty> tableFormDataColumnlist = new ArrayList<ToaSysmanageProperty>();
				Iterator it = tableFormTitle.iterator();
				ToaSysmanageProperty field1 = new ToaSysmanageProperty();
				field1.setInfoItemValue(rs.getString(2));
				field1.setInfoItemDatatype(KEY);
				tableFormDataColumnlist.add(field1);
				while (it.hasNext()) {
					ToaSysmanageProperty field = new ToaSysmanageProperty();
					try {
						beanUtilsBean.copyProperties(field, it.next());
					} catch (IllegalAccessException e) {
						// TODO 自动生成 catch 块

						e.printStackTrace();
					}
					if (KEY.equals(field.getInfoItemDatatype()))
						continue;
					if (CODE.equals(field.getInfoItemDatatype())) {// 如果字段类型为代码引用型
						if (rs.getString(i) != null
								&& !rs.getString(i).equals("")
								&& !rs.getString(i).equals("null")) {
							String dictitemname = dictitemanaget
									.getDictItemName(rs.getString(i));
							if (dictitemname != null) {
								field.setInfoItemValue(rs.getString(i));
								field.setRefernceDesc(dictitemname);
							}
						}

					}else if (PHOTO.equals(field.getInfoItemDatatype())
							|| FILE.equals(field.getInfoItemDatatype())) {
						field.setInfoItemValue(rs.getString(i));
						/*
						 * if(url.indexOf("microsof")!=-1||url.indexOf("sqlserver")!=-1){//SQLServer数据库
						 * field.setInfoItemValue(rs.getString(i)); }else
						 * if(url.indexOf("oracle")!=-1){
						 * field.setInfoItemValue(""); }
						 */
					}else if(field.getInfoItemDatatype().equals("6")){
						if (rs.getString(i) != null
								&& !rs.getString(i).equals("")
								&& !rs.getString(i).equals("null")) {	
							date=sdf.parse(rs.getString(i));
							field.setInfoItemValue(sdf.format(date));						
						}
					}else {
						if(field.getInfoItemField().equals("ISDEL")){//注意所有的子集的是否删除字段必须命名为ISDEL，方便以后的代码编写
							if(rs.getString(i)!=null&&rs.getString(i).equals("1")){
								field.setInfoItemValue("是");
							}else{
								field.setInfoItemValue("否");
							}
						}else{
							field.setInfoItemValue(rs.getString(i));
						}
					}

					tableFormDataColumnlist.add(field);
					i++;
				}
				tableFormDataRowlist.add(tableFormDataColumnlist);
			}
			rs.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new SystemException();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}
		return tableFormDataRowlist;
	}

	/**
	 * 初始化添加信息
	 * 
	 * @param struct
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 494A47B90109
	 */
	public List initTableAdd(String struct) 
			throws SystemException,ServiceException{
		return itemanager.getAllCreatedItems(struct);
	}

	/**
	 * 初始化编辑信息
	 * 
	 * @param struct
	 *            信息集编号
	 * @param keyid
	 *            主键值
	 * @return java.util.List
	 * @roseuid 494A47B90167
	 */
	public List initTableEdit(String struct, String keyid) 
			throws SystemException,ServiceException{
		List list = initTableAdd(struct);
		ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
		return getSingleTableValue(list, infoset.getInfoSetValue(), infoset
				.getInfoSetPkey(), keyid);
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param struct
	 *            信息集编号
	 * @return java.lang.String
	 * @roseuid 494A47B90203
	 */
	public String addTable(HttpServletRequest request, String struct) 
			throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		String keyid = "0";
		try {
			ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
			List<ToaSysmanageProperty> personList = new ArrayList<ToaSysmanageProperty>();
			List proSet = itemanager.getAllCreatedItems(struct);
			Iterator it = proSet.iterator();
			// UserInfo userInfo = (UserInfo)
			// ThreadLocalUtils.get(CommonDataSupport.CURRENT_USER);
			// personList.add(new TgsProperty());//得到操作员
			// personList.add(new TgsProperty());//得到操作日期
			// personList.add(new TgsProperty());//是否作为工资计算依据标志
			while (it.hasNext()) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) it.next();
				pro.setInfoItemValue(request.getParameter(pro
						.getInfoItemField()));
				if (pro.getInfoItemState().equals("1"))
					personList.add(pro);// 得到对应属性的属性值
			}

			keyid = saveSingleTableValue(personList, struct);// 增加记录
			message.append(infoset.getInfoSetName()).append("增加成功！");
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error);
		}
		return message.toString();
	}

	/**
	 * 编辑信息
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param struct
	 *            信息集编号
	 * @return java.lang.String
	 * @roseuid 494A47B9029F
	 */
	public String editTable(HttpServletRequest request, String struct) 
			throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		try {
			ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
			String Structvalue = infoset.getInfoSetValue();
			List<ToaSysmanageProperty> personList = new ArrayList<ToaSysmanageProperty>();
			List proSet = itemanager.getAllCreatedItems(struct);
			Iterator it = proSet.iterator();

			while (it.hasNext()) {
				ToaSysmanageProperty pro = (ToaSysmanageProperty) it.next();
				pro.setInfoItemValue(request.getParameter(pro
						.getInfoItemField()));
				String type = pro.getInfoItemDatatype();
				if (!type.equals("15") && pro.getInfoItemState().equals("1"))
					personList.add(pro);// 得到对应属性的属性值
			}
			String keyid = request.getParameter(infoset.getInfoSetPkey());
			updateSingleTableValue(personList, Structvalue, infoset
					.getInfoSetPkey(), keyid);// 增加人员记录
			message.append(infoset.getInfoSetName()).append("保存成功！");
		}  catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error);
		}
		return message.toString();
	}

	/**
	 * 删除信息
	 * 
	 * @param struct
	 *            信息集编号
	 * @param delids
	 *            要删除的信息集编号串
	 * @return java.lang.String
	 * @roseuid 494A47B9034B
	 */
	public String delTable(String struct, String delids) 
			throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
		try {
			List childSetlist = null;
			if("1".equals(infoset.getInfoSetIsexistchild())){
				childSetlist = infosetmanager.getChildInfoSet(struct);//获取子信息集列表
			}
		String table = infoset.getInfoSetValue();
		String key = infoset.getInfoSetPkey();
		String sql = null;
		if (delids != null && !delids.equals("")) {
			String delarr[] = delids.split(",");
			for (int i = 0; i < delarr.length; i++) {
				if(childSetlist!=null&&childSetlist.size()>0){
					for(int k =0;k<childSetlist.size();k++){
						ToaSysmanageStructure childinfoset = (ToaSysmanageStructure)childSetlist.get(k);
						sql = "delete from " + childinfoset.getInfoSetValue() + " where " + key + "='"
						+ delarr[i] + "'";
						itemanager.executeJdbcUpdate(sql);
					}
				}
				sql = "delete from " + table + " where " + key + "='"
						+ delarr[i] + "'";// 设置原生SQL语句
				//System.out.println("sqlstr:" + sql);
				
					itemanager.executeJdbcUpdate(sql);
					message.append(infoset.getInfoSetName()).append("删除成功！");
				
			}
		}
		} catch (ServiceException e) {
			throw new ServiceException(infoset.getInfoSetName()+MessagesConst.del_error);
		}// 执行删除操作
		return message.toString();
	}

	/**
	 * 获取表头列表
	 * 
	 * @param struct
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 494A47BA000F
	 */
	public List getTableTitle(String struct) 
			throws SystemException,ServiceException{
		try{
			return itemanager.getAllCreatedBTItems(struct);
		} catch (ServiceException e) {
			throw new ServiceException("表头列表查找出错！");
		}
	}
	
	/**
	 * 获取选择列
	 * 
	 * @param struct
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 494A47BA000F
	 */
	public List getSelectItems(String struct) 
			throws SystemException,ServiceException{
		return itemanager.getAllCreatedItems(struct);
	}
	
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
			throws SystemException,ServiceException{
		return itemanager.getItemsByValues(struct,infoSetValues);
	}

	/**
	 * author:zhangli description:获取父信息集名称 modifyer: description:
	 * 
	 * @param struct
	 *            信息集编号
	 * @return
	 */
	public String getParentStructName(String struct) 
			throws SystemException,ServiceException{
		// TODO Auto-generated method stub
		return infosetmanager.getParentNameByChild(struct);
	}

	/**
	 * author:zhangli description:获取信息集名称 modifyer: description:
	 * 
	 * @param struct
	 *            信息集编号
	 * @return
	 */
	public String getStructName(String struct) 
			throws SystemException,ServiceException{
		// TODO Auto-generated method stub
		return infosetmanager.getNameByCode(struct);
	}

	/**
	 * author:zhangli d escription:通过信息集获取树节点列表 modifyer: description:
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
			throws SystemException,ServiceException{
		// TODO Auto-generated method stub
		try{
			List<TreeHelp> list = new ArrayList<TreeHelp>();
			ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
			String table = infoset.getInfoSetValue();
			String key = infoset.getInfoSetPkey();
			StringBuffer sql = new StringBuffer("select ").append(key).append(",")
					.append(namepro);
			if (parentpro != null)
				sql.append(",").append(parentpro);
			sql.append(" from ").append(table);
			if (wheresql != null)
				sql.append(" where ").append(wheresql);
			ResultSet rs = itemanager.executeJdbcQuery(sql.toString());
			while (rs.next()) {
				TreeHelp tree = new TreeHelp();
				tree.setNodeid(rs.getString(1));
				tree.setNodename(rs.getString(2));
				tree.setFpro(fpro);
				tree.setTableName(tablename);
				if (parentpro != null)
					tree.setNodeparentid(rs.getString(3));
				else
					tree.setNodeparentid("0");
				list.add(tree);
			}
			return list;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException();
		} catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}
	}

	/**
	 * author:zhangli description:通过信息集主键获取信息集对象 modifyer: description:
	 * 
	 * @param infosetCode
	 *            信息集编号
	 * @return
	 */
	public ToaSysmanageStructure getInfoSet(String infosetCode) 
			throws SystemException,ServiceException{
		return infosetmanager.getInfoSet(infosetCode);
	}

	/**
	 * author:zhangli description:通过信息集值获取信息集对象 modifyer: description:
	 * 
	 * @param infosetValue
	 *            信息集值
	 * @return
	 */
	public ToaSysmanageStructure getInfoSetByValue(String infosetValue) 
			throws SystemException,ServiceException{
		// TODO Auto-generated method stub
		return infosetmanager.getToaStructByValue(infosetValue);
	}
	
	
	/*
	 * 
	 * Description:综合查询模块查询符合添加的列表信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 27, 2010 8:49:49 PM
	 */
	public List<ToaReportBean> getRecordListByCondition(List dataRowTitle, String struct,
			String sql,String orderStr,String groupFiled) throws SystemException,ServiceException{
		try{
			List<ToaReportBean> recordlist=new ArrayList<ToaReportBean>();
			ToaSysmanageStructure infoset = infosetmanager.getInfoSet(struct);
			String infosetpkey = infoset.getInfoSetPkey();
			String infosetvalue = infoset.getInfoSetValue();
			StringBuffer sqlstr = buildSelectSql(dataRowTitle.iterator(), infosetpkey,
					infosetvalue, sql, orderStr,groupFiled);
			ResultSet rs = itemanager.executeJdbcQuery(sqlstr.toString());
			recordlist=this.getDataList(dataRowTitle, rs);
			return recordlist;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}
	}

	/*
	 * 
	 * Description:综合查询模块，构造SQL
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 27, 2010 8:49:46 PM
	 */
	private StringBuffer buildSelectSql(Iterator tableFormTitle,
			String infosetpkey, String infosetvalue, String condition,String orderStr,String groupFiled) throws SystemException,ServiceException{
		try{
			ToaSysmanageProperty field = new ToaSysmanageProperty();
			StringBuffer sql = new StringBuffer();
			StringBuffer fieldStrBuffer = new StringBuffer();
			String fieldStr = "";
			if(groupFiled!=null&&!"".equals(groupFiled)&&!"null".equals(groupFiled)){//如果分组字段不为空
				sql.append("select ")
				/*.append(infosetpkey)
				.append(",")*/
				.append(groupFiled)
				.append(",");
				while (tableFormTitle.hasNext()) {
					field = (ToaSysmanageProperty) tableFormTitle.next();
					String datatype = field.getInfoItemDatatype();
					if (!KEY.equals(datatype)&&!groupFiled.equals(field.getInfoItemField())) {
						fieldStrBuffer.append(field.getInfoItemField() + ",");
					}
				}
			}else{
				sql.append("select ");
				/*.append(infosetpkey)
				.append(",");*/
				while (tableFormTitle.hasNext()) {
					field = (ToaSysmanageProperty) tableFormTitle.next();
					String datatype = field.getInfoItemDatatype();
					if (!KEY.equals(datatype)) {
						fieldStrBuffer.append(field.getInfoItemField() + ",");
					}
				}
			}		
			fieldStr = fieldStrBuffer.toString();
			if (fieldStr == null || "".equals(fieldStr))
				return null;
			sql.append(fieldStr.substring(0, fieldStr.length() - 1));
			sql.append(" from " + infosetvalue);
			if (condition != null && !"".equals(condition)) {
				sql.append(" where " + condition);
			}
			if(orderStr!=null&&!"".equals(orderStr)){
				sql.append(" order by "+orderStr);
			}
			return sql;
		}catch(ServiceException e){
			throw new ServiceException("构建SQL语句失败");
		}
	}


	/*
	 * 
	 * Description:组装数据
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 28, 2010 9:37:29 AM
	 */
	private List<ToaReportBean> getDataList(List tableFormTitle, ResultSet rs) 
	throws SystemException,ServiceException{	
		List<ToaReportBean> recordlist=new ArrayList<ToaReportBean>();
		ToaReportBean reportBean;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		int columnsize=tableFormTitle.size();
		try {
			while (rs.next()) {
				int i=1;
				String[] tableFormDataColumn=new String[CLOMNSIZE];
				Iterator it = tableFormTitle.iterator();
				while (it.hasNext()&&i<=CLOMNSIZE){
					String tempValue="  ";
					ToaSysmanageProperty field = (ToaSysmanageProperty)it.next();
					if (KEY.equals(field.getInfoItemDatatype()))	//主键
						tempValue=rs.getString(i);
					if (CODE.equals(field.getInfoItemDatatype())){	// 如果字段类型为代码引用型
						if (rs.getString(i) != null
								&& !rs.getString(i).equals("")
								&& !rs.getString(i).equals("null")) {
							String dictitemname = dictitemanaget.getDictItemName(rs.getString(i));
							if (dictitemname != null) {
								tempValue=dictitemname;
							}
						}
					}/*else if(field.getInfoItemDatatype().equals(NUM)){
							tempValue=String.valueOf(rs.getDouble(i));
						}*/else if(field.getInfoItemDatatype().equals(DATEDATE)){//为日期
							if (rs.getString(i) != null
									&& !rs.getString(i).equals("")
									&& !rs.getString(i).equals("null")) {	
								date=sdf.parse(rs.getString(i));
								tempValue=sdf.format(date);		
							}
						}else {
							if("ISDEL".equals(field.getInfoItemField())||"ORG_ISDEL".equals(field.getInfoItemField())){//注意所有的子集的是否删除字段必须命名为ISDEL，方便以后的代码编写
								if(rs.getString(i)!=null&&rs.getString(i).equals("1")){
									tempValue="是";
								}else{
									tempValue="否";
								}
							}else{
								tempValue=rs.getString(i);
								if(tempValue==null){
									tempValue=" ";
								}
							}
						}
					tableFormDataColumn[i-1]=tempValue;
					i++;
				}
				for(int j=columnsize;columnsize<=CLOMNSIZE&&j<CLOMNSIZE;j++){
					tableFormDataColumn[j]="";
				}
				reportBean=new ToaReportBean(tableFormDataColumn);
				recordlist.add(reportBean);
			}
			rs.close();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}catch(Exception e){
			e.printStackTrace();
		}
		return recordlist;
	}
	
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 5, 2010 10:04:29 AM
	 */
	public Page<List> getTablePage(List dataRowTitle, String infosetvalue,String infosetpkey,
			String sql, Page<List> page) throws SystemException,ServiceException{
		try{
			int currentpage = page.getPageNo();
			int recordsperpage = page.getPageSize();
			int totalRecord = 0;
			StringBuffer sqlstr = new StringBuffer("select count(" + infosetpkey
					+ ") from " + infosetvalue);
			if (sql != null && !"".equals(sql)) {
				sqlstr.append(" where ").append(sql);
			}
			ResultSet rs;
			try {
				rs = itemanager.executeJdbcQuery(sqlstr.toString());
				if (rs.next())
					totalRecord = rs.getInt(1);
				rs.close();
			} catch (HibernateException e) {
				e.printStackTrace();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
				rs = null;
			}
			if (currentpage == 0)
				currentpage = 1;
			page.setPageNo(currentpage);
			int start = (currentpage - 1) * recordsperpage + 1;
			int end = currentpage * recordsperpage;
			page.setTotalCount(totalRecord);
			if(dataRowTitle==null){
				return page;
			}
			sqlstr = buildSelectSql(dataRowTitle.iterator(), infosetpkey,
					infosetvalue, sql, start, end);
			if (sqlstr == null)
				page.setResult(null);
			rs = itemanager.executeJdbcQuery(sqlstr.toString());
			page.setResult(getTableFormData(dataRowTitle, rs));
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}catch (HibernateException e) {
			throw new DAOException();
		}
	}
}
