package com.strongit.oa.systemset;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TUumsUserandip;
import com.strongit.oa.bo.ToaIMEI;
import com.strongit.oa.bo.ToaMapdata;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.pdf.iDBManager2000;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.utils.StringUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class SystemsetManager {
	private GenericDAOHibernate<ToaSystemset, String> systemsetDao;
	private GenericDAOHibernate<ToaMapdata, String> mapDataDao;
	private GenericDAOHibernate<ToaSysmanageProperty, String> infoItemDao;
	private GenericDAOHibernate<ToaIMEI, String> imeiDao;
	private GenericDAOHibernate<TUumsUserandip, String> userIpDao;
	@Autowired
	private SendDocManager sendDocManager;
	private static final String YES = "1";
	private InfoSetManager infosetmanager;
	 private static com.strongit.oa.pdf.iDBManager2000 DbaObj = new iDBManager2000();
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.systemsetDao = new GenericDAOHibernate<ToaSystemset, String>(
				sessionFactory, ToaSystemset.class);
		this.mapDataDao = new GenericDAOHibernate<ToaMapdata, String>(
				sessionFactory, ToaMapdata.class);
		this.infoItemDao = new GenericDAOHibernate<ToaSysmanageProperty, java.lang.String>(
				sessionFactory, ToaSysmanageProperty.class);
		this.imeiDao= new GenericDAOHibernate<ToaIMEI, java.lang.String>(
				sessionFactory, ToaIMEI.class);
		this.userIpDao= new GenericDAOHibernate<TUumsUserandip, java.lang.String>(
				sessionFactory, TUumsUserandip.class);
	}

	/**
	 * @author：zouhr
	 * @time：2009-8-4下午02:47:29 读取系统配置
	 * @return ToaSystemset
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSystemset getSystemset() throws SystemException, ServiceException {
		try {
			List list = this.systemsetDao.findAll();
			if (list.size() != 0) {
				return ((ToaSystemset) list.get(0));
			} else
				return null;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "读取系统配置" });
		}
	}

	public void addSystemset(ToaSystemset systemset) throws SystemException,
			ServiceException {
		try {
			this.systemsetDao.save(systemset);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "读取系统配置" });
		}
	}
	/**
	 * 获取OFFIC控件
	 * @return
	 * @throws Exception
	 */
    public String getOfficeSet() throws Exception{
    	String sql = " SELECT OFFICE_NEW FROM T_OA_SYSTEMSET ";
    	String officeNew=null;
    	try{
    	  if (DbaObj.OpenConnection()) {
 	          ResultSet result = DbaObj.ExecuteQuery(sql);
 	          if (result.next()) {
 	            try {
 	            	officeNew=result.getString("OFFICE_NEW");
 	            }catch (Exception e) {
					// TODO: handle exception
 	            	e.printStackTrace();
				}
 	        }
    	 }
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}finally{
			 DbaObj.CloseConnection();
		}
    	return officeNew;
    }
	/**
	 * 修改统配置
	 * 
	 * @time：2009-8-4下午02:47:29
	 * @param systemset
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void editSystemset(ToaSystemset systemset) throws SystemException,
			ServiceException {
		systemsetDao.save(systemset);
	}
	
	/**
	 * 保存IMEI
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 18, 2014 11:59:21 AM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void saveIMEI(ToaIMEI entity){
		try{
			if(entity!=null&&entity.getImeiId()!=null&&entity.getImeiId().length()>0){
				imeiDao.update(entity);
			}else{
				entity.setImeiId(null);
				imeiDao.save(entity);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除imei
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 20, 2014 7:07:40 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void deleteIMEI(String pkId){
		imeiDao.delete(pkId);
	}
	/*
	 * 
	 * Description:查找映射记录 param: parentTable 父流程启动表单对应的物理表 param: subTable
	 * 子流程启动表单对应的物理表 param: filed 父流程启动表单对应的物理表的某字段
	 * 
	 * @author 彭小青
	 * 
	 * @date Feb 25, 2010 8:01:29 PM
	 */
	public ToaMapdata getMapRecord(String tableName, String subTableName,
			String filed) {
		if (subTableName == null || "".equals(subTableName)) {
			return null;
		}
		List<ToaMapdata> list = mapDataDao
				.find(
						"from ToaMapdata t where t.parentTable=? and t.subTable=? and t.parentFiled=?",
						tableName, subTableName, filed);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/*
	 * 
	 * Description:查找映射记录 param: parentTable 父流程启动表单对应的物理表 param: subTable
	 * 子流程启动表单对应的物理表 param: filed 子流程启动表单对应的物理表的某字段
	 * 
	 * @author 彭小青
	 * 
	 * @date Feb 26, 2010 11:49:45 AM
	 */
	public ToaMapdata getMapRecord2(String tableName, String subTableName,
			String subfiled) {
		List<ToaMapdata> list = mapDataDao
				.find(
						"from ToaMapdata t where t.parentTable=? and t.subTable=? and t.subFiled=?",
						tableName, subTableName, subfiled);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/*
	 * 
	 * Description:删除映射 param:
	 * 
	 * @author 彭小青
	 * 
	 * @date Mar 16, 2010 10:47:43 AM
	 */
	public void deleteMapData(String tableName, String subTableName) {
		List<ToaMapdata> list = mapDataDao.find(
				"from ToaMapdata t where t.parentTable=? and t.subTable=? ",
				tableName, subTableName);
		if (list != null && list.size() > 0) {
			mapDataDao.delete(list);
		}
	}

	/*
	 * 
	 * Description: param:
	 * 
	 * @author 彭小青
	 * 
	 * @date Feb 26, 2010 4:16:47 PM
	 */
	public List<ToaSysmanageProperty> getFormFiledsAndMapItemsByValue(
			String tableName, String subTableName, String formId)
			throws SystemException, ServiceException {
		List<ToaSysmanageProperty> list = new ArrayList<ToaSysmanageProperty>();
		List<EFormField> eFormFiledList = sendDocManager
				.getFormTemplateFieldListWithColumnInfo(formId);
		ToaSysmanageProperty property;
		if (eFormFiledList != null && !eFormFiledList.isEmpty()) {
			for (EFormField eformField : eFormFiledList) {
				if (!"Blank".equals(eformField.getType())) {// 过滤表单卡
					String fieldClassName = eformField.getFieldClassName();
					if (eformField.getTablename().equals(tableName)
							&& fieldClassName != null
							&& !"".equals(eformField.getCaption())
							&& eformField.getType() != null
							&& !"Strong.Form.Controls.Line".equals(eformField.getType())
							&& !"Strong.Form.Controls.Label".equals(eformField.getType())
							&& !"Strong.Form.Controls.Button".equals(eformField.getType())) {
						property = new ToaSysmanageProperty();
						fieldClassName = fieldClassName.substring(
								fieldClassName.lastIndexOf(".") + 1,
								fieldClassName.length());
						property.setInfoItemField(eformField.getFieldname()); // 字段名
						property.setInfoItemSeconddisplay(eformField
								.getCaption()
								+ "(" + eformField.getTablename() + ")"); // 字段描述
						property.setInfoItemDatatype(fieldClassName);
						property.setInfoItemLength(String.valueOf(eformField
								.getFieldDiaplaySize()));
						// 数据类型
						ToaMapdata mapData = this.getMapRecord(tableName,
								subTableName, property.getInfoItemField()); // 查找映射记录
						if (mapData != null) { // 映射记录不为空
							property.setMapFiled(mapData.getSubFiled()); // 设置映射字段
							property.setMapFildDesc(mapData.getSubFileDesc()); // 设置映射字段描述
							property.setMapFildType(mapData.getFiledType2()); // 设置映射字段数据类型
							property.setMapFildSize(mapData.getFileSize2()); // 设置映射字段大小
						}
						list.add(property);
					}
				}
			}

		}
		return list;
	}

	/*
	 * 
	 * Description:获取子流程的字段列表 param:
	 * 
	 * @author 彭小青
	 * 
	 * @date Feb 26, 2010 4:43:46 PM
	 */
	public List<ToaSysmanageProperty> getSubFormFileds(String formId,
			String dataType) throws SystemException, ServiceException {
		List<ToaSysmanageProperty> list = new ArrayList<ToaSysmanageProperty>();
		List<EFormField> eFormFiledList = sendDocManager
				.getFormTemplateFieldListWithColumnInfo(formId); // 获取电子表单模板域信息列表
		ToaSysmanageProperty property;
		if (eFormFiledList != null && !eFormFiledList.isEmpty()) { // 表单中存在域
			if (dataType.trim().equalsIgnoreCase("Timestamp")) { // 日期
				dataType = "String,Timestamp";
			} else if (dataType.trim().equalsIgnoreCase("Long")
					|| dataType.trim().equalsIgnoreCase("Integer")) {// 整型
				dataType = "String,Long,Integer";
			} else if (dataType.trim().equalsIgnoreCase("BigDecimal")) { // 浮点型
				dataType = "String,BigDecimal";
			} else if (dataType.trim().equalsIgnoreCase("String")) { // 字符串型
				dataType = "Timestamp,String,Long,Integer,BigDecimal";
			}
			for (EFormField eformField : eFormFiledList) { // 循环表单域
				String filedClassName = eformField.getFieldClassName();
				if (filedClassName != null) {
					filedClassName = filedClassName.substring(filedClassName
							.lastIndexOf(".") + 1, filedClassName.length());
				}
				if (filedClassName != null
						&& dataType.indexOf(filedClassName) != -1
						&& !"Strong.Form.Controls.Line".equals(eformField.getType())
						&& !"Strong.Form.Controls.Label".equals(eformField.getType())
						&& !"Strong.Form.Controls.Button".equals(eformField.getType())
						&& !"".equals(eformField.getCaption())) {
					property = new ToaSysmanageProperty();
					property.setInfoItemField(eformField.getFieldname());
					property.setInfoItemSeconddisplay(eformField.getCaption()
							+ "(" + eformField.getTablename() + ")");
					property.setInfoItemDatatype(filedClassName);
					property.setInfoItemLength(String.valueOf(eformField
							.getFieldDiaplaySize()));
					list.add(property);
				}
			}

		}
		return list;
	}

	/*
	 * Description: 保存映射关系 param: params格式 filed1&filed1Desc=filed2&field2DescΓ
	 * 
	 * @author 彭小青
	 * 
	 * @date Feb 26, 2010 9:44:41 AM
	 */
	public void saveMapping(String params, String parentFormId, String subFormId)
			throws SystemException, ServiceException {
		try {
			if (params == null || params.length() == 0) {
				if (parentFormId != null && subFormId != null) {
					String tableName = sendDocManager
							.getTNByFormId(parentFormId);// 根据表单ID获取业务表
					String subTableName = sendDocManager
							.getTNByFormId(subFormId);// 根据表单ID获取业务表
					this.deleteMapData(tableName, subTableName); // 删除映射
				}
				return;
			}
			String[] strArr = params.split("Γ");// [filed1&filed1Desc&filed1DataType=filed2&field2Desc,filed11&filed11Desc&filed11DataType=filed12&field12Desc,...]
			String[] mapArr;
			String[] mapArr1;
			String[] mapArr2;
			ToaMapdata mapping;
			List<ToaMapdata> list = new ArrayList<ToaMapdata>();
			String tableName = ""; // 父流程业务表名
			String subTableName = ""; // 子流程业务表名
			if (strArr != null && strArr.length > 0) {
				mapArr = strArr[0].split("=");
				mapArr1 = mapArr[0].split("&"); // [filed1,filed1Desc,filed1DataType]父流程
				mapArr2 = mapArr[1].split("&"); // [filed2,filed2Desc]子流程
				tableName = mapArr1[1].substring(mapArr1[1].indexOf("(") + 1,
						mapArr1[1].indexOf(")")); // 获取父流程业务表名
				subTableName = mapArr2[1].substring(
						mapArr2[1].indexOf("(") + 1, mapArr2[1].indexOf(")")); // 获取子流程业务表名
			}
			this.deleteMapData(tableName, subTableName); // 删除映射
			for (int i = 0; strArr != null && i < strArr.length; i++) {
				mapping = new ToaMapdata();
				mapArr = strArr[i].split("="); // [filed1&filed1Desc&filed1DataType,filed2&field2Desc]
				mapArr1 = mapArr[0].split("&"); // [filed1,filed1Desc,filed1DataType]父流程
				mapArr2 = mapArr[1].split("&"); // [filed2,filed2Desc]子流程
				mapping.setParentTable(tableName); // 设置父表名
				mapping.setSubTable(subTableName); // 设置子表名
				mapping.setParentFiled(mapArr1[0]); // 设置父表字段
				mapping.setSubFiled(mapArr2[0]); // 设置子表字段
				mapping.setSubFileDesc(mapArr2[1]); // 设置子表字段描述
				mapping.setFiledType(mapArr1[2]); // 设置父表字段数据类型
				mapping.setFiledType2(mapArr2[2]); // 设置字表字段数据类型
				mapping.setFiledSize(mapArr1[3]); // 设置父表字段数据大小
				mapping.setFileSize2(mapArr2[3]); // 设置子表字段数据大小
				list.add(mapping);
			}
			mapDataDao.save(list);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息项列表" });
		}
	}

	/*
	 * 
	 * Description:判断是字符串是否为数字 param:
	 * 
	 * @author 彭小青
	 * 
	 * @date Mar 15, 2010 4:13:51 PM
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/*
	 * 
	 * Description:判断是否为日期 param:
	 * 
	 * @author 彭小青
	 * 
	 * @date Mar 15, 2010 4:25:32 PM
	 */
	public boolean isDate(String str_input) {
		if (str_input != null && !"".equals(str_input)
				&& !"null".equals(str_input)) {
			try {
				java.text.DateFormat.getDateInstance().parse(str_input);
				return true;
			} catch (java.text.ParseException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * Description:返回计算结果 param:
	 * 
	 * @author 彭小青
	 * 
	 * @date Mar 15, 2010 4:17:03 PM
	 */
	public String getCalValue(String filed, String filedType,
			String filedType2, String filedSize, String filedSize2) {
		if (filedType == null || filedType2 == null || filedSize == null
				|| filedSize2 == null) {
			return filed;
		}
		String url = infosetmanager.getCurrentURL();
		if (filedType.equalsIgnoreCase("String")
				&& filedType2.equalsIgnoreCase("Timestamp")) { // 字符串转日期
			if (url.indexOf("oracle") != -1) {
				return "to_date(" + filed + ",'yyyy-MM-dd')";
			}
		} else if (filedType.equalsIgnoreCase("Timestamp")
				&& filedType2.equalsIgnoreCase("String")) { // 日期转字符串
			if (url.indexOf("oracle") != -1) {
				return "to_char(" + filed + ",'yyyy-MM-dd')";
			}
		} else if (filedType.equalsIgnoreCase("String")
				&& (filedType2.equalsIgnoreCase("Integer")
						|| filedType2.equalsIgnoreCase("Long") || filedType2
						.equalsIgnoreCase("BigDecimal"))) { // 字符串转数值
			if (url.indexOf("oracle") != -1) {
				return "to_number(" + filed + ")";
			}
		} else if ((filedType.equalsIgnoreCase("Integer")
				|| filedType.equalsIgnoreCase("Long") || filedType
				.equalsIgnoreCase("BigDecimal"))
				&& filedType2.equalsIgnoreCase("String")) { // 数值转字符串
			if (url.indexOf("oracle") != -1) {
				return "to_char(" + filed + ")";
			}
		}
		return filed;
	}

	/*
	 * 
	 * Description:根据子流程业务字段找到对应的父流程业务字段 param: subProcessFiledStr
	 * 子流程业务表字段名字符串(字段1，字段2，字段3...)
	 * 
	 * @author 彭小青
	 * 
	 * @date Feb 26, 2010 11:42:59 AM
	 */
	public String getParentProccessFiledStr(String tableName,
			String subTableName, String subProcessFiledStr)
			throws SystemException, ServiceException {
		try {
			boolean flag = false;
			String filedStrs = ""; // 父流程业务表字段名字符串
			ToaMapdata mapData;
			if (tableName == null || "".equals(tableName)) { // 父流程业务表为空
				return null;
			}
			if (subTableName == null || "".equals(subTableName)) {// 子流程业务表位空
				return null;
			}
			String[] strArr = subProcessFiledStr.split(",");
			for (int i = 1; i < strArr.length; i++) {
				mapData = this
						.getMapRecord2(tableName, subTableName, strArr[i]);// 获取在父流程中对应的字段
				if (mapData != null) { // 找到了映射字段
					flag = true;
					filedStrs += ","
							+ this.getCalValue(mapData.getParentFiled(),
									mapData.getFiledType(), mapData
											.getFiledType2(), mapData
											.getFiledSize(), mapData
											.getFileSize2());
				} else {
					filedStrs += ",null";
				}
			}
			if (flag) {
				if (filedStrs.length() > 0) {
					filedStrs = filedStrs.substring(1);// 父表单对应的字段
				}
				return filedStrs;
			} else {
				return null;
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息项列表" });
		}
	}
	/**
	 * 获取所有imei设置列表
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 15, 2014 3:02:56 PM
	 * @param   
	 * @return  List<ToaIMEI>
	 * @throws
	 */
	public List<ToaIMEI> findIMEIList(String userId,String userName,String imeiCode,String isOpen)throws ServiceException{
		StringBuilder sb=new StringBuilder();
		sb.append("from ToaIMEI t where 1=1 ");
		if(StringUtil.isNotEmpty(userId)){
			sb.append(" and t.userId like '%"+userId+"%'");
		}
		if(StringUtil.isNotEmpty(userName)){
			sb.append(" and t.userName like '%"+userName+"%'");
		}
		if(StringUtil.isNotEmpty(imeiCode)){
			sb.append(" and t.iemiCode='"+imeiCode+"'");
		}
		if(StringUtil.isNotEmpty(isOpen)){
			sb.append(" and t.isOpen='"+isOpen+"'");
		}
		return imeiDao.find(sb.toString());
	}
	
	/**
	 * 获取imei对象
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 18, 2014 12:58:42 PM
	 * @param   
	 * @return  ToaIMEI
	 * @throws
	 */
	public ToaIMEI getIMEIbyId(String id){
		return imeiDao.get(id);
	}
	
	@Autowired
	public void setInfosetmanager(InfoSetManager infosetmanager) {
		this.infosetmanager = infosetmanager;
	}
	/**
	 * 登入时保存Ip至数据库
	 * author  taoji
	 * @param t
	 * @date 2014-1-21 上午11:09:33
	 */
	public void save(TUumsUserandip t){
		t.setId(null);
		userIpDao.save(t);
	}
	/**
	 * 根据用户id  获取记录中的ip以及上次ip
	 * author  taoji
	 * @param userId
	 * @return
	 * @date 2014-1-21 上午11:12:56
	 */
	public String getIpByUserId(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append(" from TUumsUserandip t where t.userid ='"+userId+"' order by t.logintime desc");
		List<TUumsUserandip> t = userIpDao.find(sb.toString());
		String ip = "";
		if(t!=null&&t.size()>1){
			ip =  t.get(0).getIp()+","+t.get(1).getIp();
		}else if(t!=null&&t.size()>0){
			ip = t.get(0).getIp();
		}
		return ip;
	}
}
