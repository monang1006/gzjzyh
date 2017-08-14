package com.strongit.oa.personnel.baseperson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.driver.OracleConnection;
import oracle.sql.BLOB;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attendance.arrange.ScheGroupManager;
import com.strongit.oa.bo.PersonDeployInfo;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaPersonDeploy;
import com.strongit.oa.bo.ToaPersonPrivil;
import com.strongit.oa.bo.ToaStructure;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.service.UserService;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.personnel.deploymanage.Deploymanager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.oa.personnel.structure.PersonStructureManager;
import com.strongit.oa.personnel.veteranmanage.Veteranmanager;
import com.strongit.oa.util.FieldNameValue;
import com.strongit.oa.util.FieldValue;
import com.strongit.oa.util.GetDataSty;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * ��������ACTION
 * 
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class PersonManager extends BaseManager{

	private static final String VAR = "0"; // 类型为Vachar

	private static final String CODE = "1"; // 类型为代码

	private static final String NUM = "2"; // 类型为数值

	private static final String YEAR = "3"; // 类型为年

	private static final String MONTH = "4"; // 类型为年月

	private static final String DATE = "5"; // 类型为年月日

	private static final String DATETIME = "6"; // 类型为年月日时间

	private static final String FILE = "10"; // 文件

	private static final String PHOTO = "11"; // 图片类型

	private final static String ZZRY = "4028822723fece180123fed551ae0008"; // 在职人员

	private final static String TXRY = "ff8080812015288501201792ab090061"; // 退休人员

	private final static String LEADER = "4028822723fece180123ff510d37000f"; // 领导职务

	private final static String NOLEADER = "4028822723fece180123ff5142120010"; // 非领导职务

	private final static String personstructcode = "40288239230c361b01230c7a60f10015";// 人员信息集编码

	private String defaultshow = "PERSONID,PERSON_NAME,PERSON_SAX,"; // 人员调配时默认展现的一些信息项
	
	private final static String PERSON_TABLE_NAME="T_OA_BASE_PERSON";
	
	private final static String HOLIDAY_TABLE_NAME = "T_OA_HOLIDAY";
	
	private final static String PERSON_PKEY="PERSONID";
	
	private final static String holidayfkey=HOLIDAY_TABLE_NAME+"."+PERSON_PKEY;			//休假子集外键
	
	private final static String personkey=PERSON_TABLE_NAME+"."+PERSON_PKEY;			//人员信息集主键
	
	private final static String holidaytable=HOLIDAY_TABLE_NAME+" "+HOLIDAY_TABLE_NAME;	//休假子集
	
	private final static String persontable=PERSON_TABLE_NAME+" "+PERSON_TABLE_NAME;	//人员信息集
	
	private final static String IS_LEGAL="4028822723fece180123ff4dbc37000c";

	private final static String ZERO = "0";

	private final static String ONE = "1";

	private final static String TWO = "2";

	private final static String THREE = "3";

	private final static String FOUR = "4";

	private GenericDAOHibernate<ToaBasePerson, String> personDAO;
	
	@Autowired private ScheGroupManager groupManager;
	
	@Autowired private UserService userService;

	private DictItemManager manager; // 字典项Manager

	private InfoSetManager infoManager; // 信息集manager

	private PersonStructureManager strucManager;// 机构编制manager

	private InfoItemManager itemmanager; // 信息项manager

	private PersonOrgManager orgmanager; // 机构manager

	private GetDataSty datesty = new GetDataSty();// 处理数据库中日期格式的类

	private Deploymanager deploymanager; // 调配类别manager

	private Veteranmanager veteramanager; // 老干部manager

	public PersonManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		personDAO = new GenericDAOHibernate<ToaBasePerson, String>(
				sessionFactory, ToaBasePerson.class);
	}

	/**
	 * 添加人员基本信息
	 * 
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void save(ToaBasePerson model) throws SystemException,
			ServiceException {
		try {
			personDAO.save(model);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "添加人员基本信息出错！" });
		}
	}

	/**
	 * 修改人员信息
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-27 下午04:42:24
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */

	public void updatePerson(ToaBasePerson model) throws SystemException,
			ServiceException {
		try {
			personDAO.update(model);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "修改人员基本信息出错！" });
		}
	}

	/**
	 * 根据人员ID删除人员基本信息
	 * 
	 * @param id
	 *            人员ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String id) throws SystemException, ServiceException {
		try {
			personDAO.delete(id);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "根据ID删除人员基本信息出错！" });
		}
	}
	
	 /**
	   * 逻辑删除机构时删除该机构下人员
	   * @param id
	   * @throws SystemException
	   * @throws ServiceException
	   */
	  public void deleteByCode(String id) throws SystemException, ServiceException {
			try {
				ToaBaseOrg org= orgmanager.getOrgByID(id);
				List<Object> list1=new ArrayList<Object>();
				String hql0 = "from ToaBasePerson t where t.baseOrg.orgid=?";
				list1.add(id);
				if(org!=null){
					String code=org.getOrgSyscode();
					hql0=hql0+" or t.baseOrg.orgSyscode like ?";
					list1.add(org.getOrgSyscode()+"%");
				}
				Object[] obj=new Object[list1.size()];
				for(int i=0;i<obj.length;i++){
					obj[i]=list1.get(i);
				}
				Query query = personDAO.createQuery(hql0, obj);
			//	query.executeUpdate();
				List<ToaBasePerson> personlist=query.list();
				if(personlist!=null && personlist.size()>0){
					for(int i=0;i<personlist.size();i++){
						ToaBasePerson per=personlist.get(i);
						per.setPersonIsdel("1");
						this.updatePerson(per);
					}
				}
			} catch (Exception e) {
				throw new ServiceException(MessagesConst.del_error,
						new Object[] { "逻辑删除机构下人员出错！" });
			}
		}

	/**
	 * 根据人员ID批量删除人员基本信息
	 * 
	 * @param ids
	 *            人员ID数组
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String[] ids) throws SystemException, ServiceException {
		// String hql="delete ToaBasePerson t where t.personid in (";
		String hql = "update ToaBasePerson t set t.personIsdel='" + ONE
				+ "' where t.personid in (";
		String hql1 = "delete PersonDeployInfo t where t.personId in (";
		for (int i = 0; i < ids.length; i++) {
			if (i < ids.length - 1) {
				hql = hql + "?,";
				hql1 = hql1 + "?,";
			} else {
				hql = hql + "?)";
				hql1 = hql1 + "?)";
			}
		}
		Query query = personDAO.createQuery(hql, ids);
		query.executeUpdate();
		query = personDAO.createQuery(hql1, ids);
		query.executeUpdate();

	}

	/**
	 * 根据机构ID删除人员基本信息
	 * 
	 * @param orgid
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByOrg(String orgid) throws SystemException,
			ServiceException {
		try {
			String hql = "delete ToaBasePerson t where t.baseOrg.orgid=?";
			Query query = personDAO.createQuery(hql, orgid);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "根据机构ID删除人员基本信息出错！" });
		}
	}

	/**
	 * 统计编辑下人员数量
	 * 
	 * @param strucId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCountByStrucId(String strucId) throws SystemException,
			ServiceException {
		String hql = "select count(*) from ToaBasePerson t where t.personStructId=?";
		return Integer.parseInt(personDAO.findUnique(hql, strucId).toString());
	}

	/**
	 * 根据编制ID删除人员基本信息
	 * 
	 * @param strucId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByStructure(String strucId) throws SystemException,
			ServiceException {
		try {
			String hql = "delete ToaBasePerson t where t.personStructId=?";
			Query query = personDAO.createQuery(hql, strucId);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "根据编制ID删除人员基本信息出错！" });
		}
	}

	/**
	 * 根据人员ID查询人员基本信息
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaBasePerson getPersonByID(String id) throws SystemException,
			ServiceException {
		try {
			return personDAO.get(id);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据ID查询人员基本信息" });
		}
	}

	/**
	 * 查询所有人员信息
	 * 
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaBasePerson> getAllPerson(Page<ToaBasePerson> page)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.personIsdel=? order by t.personWorkTime";
			return personDAO.find(page, hql, ZERO);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查询所有人员信息" });
		}
	}
	
	/**
	 * 根据统一用户机构ID得到导过来的统一用户机构人员
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-27 下午04:34:25
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaBasePerson> getUumsPersons(String orgid)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.baseOrg.rest=?";
			return personDAO.find(hql, orgid);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查询所有人员信息" });
		}
	}

	/**
	 * 根据机构查询人员信息
	 * 
	 * @param page
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaBasePerson> getPersonByOrg(String orgid)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.baseOrg.orgid=? and t.personIsdel=? order by t.personWorkTime";
			return personDAO.find(hql, orgid, ZERO);

		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据机构查询人员信息" });
		}
	}

	/**
	 * 根据机构编制查询人员信息
	 * 
	 * @author 胡丽丽
	 * @createDate:2009-10-19 14:36:00
	 * @param page
	 * @param strucId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaBasePerson> getPersonByStruc(Page<ToaBasePerson> page,
			String strucId) throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.personStructId=? and t.personIsdel=? and t.personPersonKind<>?";
			return personDAO.find(page, hql, strucId, ZERO, TXRY);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据机构编制查询人员信息出错！" });
		}
	}

	/**
	 * 根据机构编制查询人员信息
	 * 
	 * @author 胡丽丽
	 * @createDate:2009-10-19 14:36:00
	 * @param page
	 * @param strucId
	 * @return LIST
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaBasePerson> getPersonByStruc(String strucId)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.personStructId=? and t.personIsdel=? and t.personPersonKind<>?";
			return personDAO.find(hql, strucId, ZERO, TXRY);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据机构编制查询人员信息出错！" });
		}
	}

	/*
	 * 
	 * Description:获取某机构下没有被删除的除退休的人员信息列
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 4, 2010 2:58:50 PM
	 */
	public List<ToaBasePerson> getEffectivPersonByOrg(String orgid)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.baseOrg.orgid=? and t.personIsdel=? and (t.personPersonKind is null or t.personPersonKind<>?) order by t.personName";
			return personDAO.find(hql, orgid, ZERO, TXRY);

		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据机构查询人员信息" });
		}
	}

	/*
	 * 
	 * Description:根据查询条件查询人员信
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 4, 2010 2:56:49 PM
	 */
	@Transactional(readOnly = true)
	public Page<ToaBasePerson> getPersonByOrg(Page<ToaBasePerson> page,
			ToaBasePerson model, String orgid) throws SystemException,
			ServiceException {
		try {
			List<Object> objlist = new ArrayList<Object>();
			String hql = "select t.personid,t.personName,t.personSax,t.personNativePlace," +
					"t.personWorkTime,t.personBorn,t.personPersonKind,t.personStructId,t.baseOrg.orgid,t.baseOrg.orgName" +
					" from ToaBasePerson t,ToaBaseOrg o where t.baseOrg.orgid=o.orgid and (t.personIsdel is null or t.personIsdel=?)"; //pengxq于20110105修改
			objlist.add(ZERO);
			if (orgid != null && !"".equals(orgid)) {// 单位id
				hql = hql + " and t.baseOrg.orgid=?";
				objlist.add(orgid);
			}else{	 //pengxq于20110105添加
				  boolean flag=userService.isViewChildOrganizationEnabeld();
				  User user=userService.getCurrentUser();
				  TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
				  if(org!=null){
					  if(flag){
						  hql+=" and o.userOrgcode like ?";
						  objlist.add(org.getOrgSyscode()+"%");
					  }else{
						  hql+=" and o.userOrgid=?";
						  objlist.add(org.getOrgId());
					  }
				  }else{
					  hql+=" and o.userOrgcode='0'";
				  }
			}
			if (model.getPersonName() != null
					&& !"".equals(model.getPersonName())) {// 人员姓名
				hql = hql + " and t.personName like ?";
				objlist.add("%" + model.getPersonName() + "%");
			}
			if (model.getBaseOrg() != null
					&& model.getBaseOrg().getOrgName() != null
					&& !"".equals(model.getBaseOrg().getOrgName())) {// 所属机构
				hql = hql + " and t.baseOrg.orgName like ?";
				objlist.add("%" + model.getBaseOrg().getOrgName() + "%");
			}
			if (model.getPersonSax() != null
					&& !"".equals(model.getPersonSax())) {// 性别
				hql = hql + " and t.personSax = ?";
				objlist.add(model.getPersonSax());
			}
			if (model.getPersonPset() != null
					&& !"".equals(model.getPersonPset())) {// 职位
				hql = hql + " and t.personPset = ?";
				objlist.add(model.getPersonPset());
			}
			if (model.getPersonNativePlace() != null
					&& !"".equals(model.getPersonNativePlace())) {// 籍贯
				hql = hql + " and t.personNativePlace = ?";
				objlist.add(model.getPersonNativePlace());
			}
			if (model.getPersonPersonKind() != null
					&& !"".equals(model.getPersonPersonKind())) {// 人员类别
				hql = hql + " and t.personPersonKind = ?";
				objlist.add(model.getPersonPersonKind());
			}
			if (model.getPersonStructId() != null
					&& !"".equals(model.getPersonStructId())) {// 人员编制
				hql = hql + " and t.personStructId = ?";
				objlist.add(model.getPersonStructId());
			}
			if (model.getPersonWorkTime()!= null
					&& !"".equals(model.getPersonWorkTime())) {// 人员编制
				hql = hql + " and t.personWorkTime = ?";
				objlist.add(model.getPersonWorkTime());
			}
			if (model.getPersonBorn() != null
					&& !"".equals(model.getPersonBorn())) {// 人员编制
				hql = hql + " and t.personBorn = ?";
				objlist.add(model.getPersonBorn());
			}
			Object[] objs = new Object[objlist.size()];
			for (int i = 0; i < objlist.size(); i++) {
				objs[i] = objlist.get(i);
			}
			page = personDAO.find(page, hql, objs);
			return page;
		} catch (SystemException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据查询条件查询人员信息" });
		}
	}
	
	/*
	 * 
	 * Description:获取两个日期间的天数，包括开始时间和结束时间在内
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 4, 2010 3:20:38 PM
	 */
	public String getDaysBetweenTwoDate(String beginDate,String endDate){
		String days="0";
		try {
			if(beginDate==null||"".equals(beginDate)){
				return days;
			}
			if(endDate==null||"".equals(endDate)){
				return days;
			}
			/*SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			sdf1.parse(beginDate);
			Calendar begin=sdf1.getCalendar();
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
			sdf1.parse(afterDate);
			Calendar end=sdf2.getCalendar();*/
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		    long begin= df.parse(beginDate).getTime(); 
		    long end  = df.parse(endDate).getTime(); 
		    days=String.valueOf((end-begin)/(1000 * 60 * 60 * 24)+1); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/*
	 * 
	 * Description:保存人员或人员子集 
	 * param: 
	 * @author 彭小青 
	 * @date Sep 27, 2009 11:45:04 AM
	 */
	public String addPersonInfo(HttpServletRequest request,
			ToaSysmanageStructure structobj) throws SystemException,
			ServiceException {
		String primaryKeyValue = "0";// 初始化主键值
		String startDate="";//休假开始时间
		String endDate="";	//休假结束时间
		try {
			String proList = request.getParameter("propertyList");// 获取信息项列表
			String Structvalue = structobj.getInfoSetValue(); // 信息集值，即表名
			String[] proarray = proList.substring(0, proList.length() - 1)
					.split(",");
			List<FieldNameValue> provalueList = new ArrayList<FieldNameValue>();
			for (int i = 0; i < proarray.length; i++) {// 将信息项数组组装成存放FieldNameValue对象的list，FieldNameValue对象包括信息项值、信息项类型、信息项对应的值
				String type = this.getSingleProValue("INFO_ITEM_DATATYPE", "0",
						"T_OA_SYSMANAGE_PROPERTY", "INFO_ITEM_FIELD",
						proarray[i]);
				String value = request.getParameter(proarray[i]);// 从request范围内获取对应信息项的值
				/***********以下是对休假子集的特殊业务的处理***********/
				if("T_OA_HOLIDAY".equals(Structvalue)&&"HOLIDYA_STIME".equals(proarray[i])){//休假开始时间
					startDate=value;
				}
				if("T_OA_HOLIDAY".equals(Structvalue)&&"HOLIDAY_ETIME".equals(proarray[i])){//休假结束时间
					endDate=value;
				}
				if("T_OA_HOLIDAY".equals(Structvalue)&&"REST_DAYS".equals(proarray[i])){	//已休假天数
					value=this.getDaysBetweenTwoDate(startDate, endDate);
				}
				/***********对休假子集的特殊业务的处理结束***********/
				FieldNameValue fnvalue = new FieldNameValue(proarray[i], type,
						value);
				provalueList.add(fnvalue);// 得到对应属性的属性值
			}
			primaryKeyValue = this.NewSingleTableValue(request, provalueList,
					Structvalue);// 增加人员或人员子集记录
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "保存人员或人员子集" });
		}
		return primaryKeyValue;
	}

	/*
	 * Description:通过属性、表名、表主健属性、表主健值查找某张表某条记录对应属性的值（此方法只能查询类型为字符型的值） 
	 * @param pro  	属性 
	 * @param type 	类型 
	 * @param table 表名 
	 * @param key 	表主健属性 
	 * @param id 	表主健值 
	 * @return    	String类型的值 对应属性的值 
	 * @author 		彭小青 
	 * @date Sep 27, 2009 11:52:52 AM
	 */
	public String getSingleProValue(final String pro, final String type,
			String table, String key, String id) throws SystemException,
			ServiceException {
		String returnvalue = null;
		try {
			// 设置查询的表和查询条件（根据主健值查询）
			StringBuffer sql = new StringBuffer("select ").append(pro).append(
					" from ").append(table).append("  where ").append(key)
					.append("='").append(id).append("'");
			ResultSet rs = personDAO.executeJdbcQuery(sql.toString());
			if (rs.next()) {
				if (type == VAR) {// 类型为字符
					returnvalue = rs.getString(pro);
				} else { // 类型为数字
					returnvalue = String.valueOf(rs.getInt(pro));
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "某张表某条记录对应属性的值" });
		}
		return returnvalue;
	}

	/*
	 * 
	 * Description:保存表中一条记录 
	 * @param provalueList 信息项列表 
	 * @param table 表名 
	 * @return 		新插入行的主键值 
	 * @author 彭小青 
	 * @date ep 27, 2009 2:13:01 PM
	 */
	public String NewSingleTableValue(HttpServletRequest request,
			List provalueList, String table) {
		String primaryKeyValue = "0";
		String tempsql = "";
		try {
			List<FieldNameValue> blobList = new ArrayList<FieldNameValue>();
			UUIDGenerator uuidgenerate = new UUIDGenerator(); // uuid生成器生成主键值
			primaryKeyValue = (String) uuidgenerate.generate(); // 获取对应表的主键字段
			String key = this.getSingleProValue("INFO_SET_PKEY", VAR,
					"T_OA_SYSMANAGE_STRUCTURE", "INFO_SET_VALUE", table);
			/* 构造insert语句开始 */
			StringBuffer sql = new StringBuffer("insert into ").append(table)
					.append("(" + key + ",");
			for (int i = 0; i < provalueList.size(); i++) {
				FieldNameValue fvalue = (FieldNameValue) provalueList.get(i);
				sql.append(fvalue.getProname()).append(",");
			}
			tempsql = sql.toString();
			sql = new StringBuffer(tempsql.substring(0, tempsql.length() - 1));
			sql.append(") values('").append(primaryKeyValue).append("',");
			for (int i = 0; i < provalueList.size(); i++) {
				FieldNameValue fvalue = (FieldNameValue) provalueList.get(i);
				String value = fvalue.getValue();
				String type = fvalue.getType();
				if (type.equals(YEAR) || type.equals(MONTH)
						|| type.equals(DATE)) {
					type = VAR;
				}
				if (type.equals(NUM)) { // 为数值
					if (value == null || value.equals(""))
						value = "0";
					sql.append(value).append(",");
				} else if (type.equals(DATETIME)) { // 为日期
					if (value == null || value.equals(""))
						sql.append("'',");
					else
						sql.append("to_date('").append(value).append(
								"','yyyy-MM-dd'),");
				} else if (type.equals(PHOTO) || type.equals(FILE)) { // 为图片
					sql.append("empty_blob(),");
					blobList.add(fvalue);
				} else { // 其它
					sql = sql.append("'").append(value).append("',");
				}
			}
			tempsql = sql.toString();
			sql = new StringBuffer(tempsql.substring(0, tempsql.length() - 1));
			sql.append(")");
			/* 构造insert语句结束 */
			Connection conn = this.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());// 执行插入语句
			conn.commit();
			/*for (int i = 0; i < blobList.size(); i++) { // 循环将所有图片字段的图片进行修改
				FieldNameValue fvalue = (FieldNameValue) blobList.get(i);
				this.testBlob(conn, table, key, primaryKeyValue, fvalue
						.getValue(), fvalue.getProname());// 更新BLOB字段数据
			}
			conn.commit();*/
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return primaryKeyValue;
	}

	/*
	 * 
	 * Description:获取数据库连接 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 26, 2009 4:01:27 PM
	 */
	public Connection getConnection() {
		Properties properties = this.getProperties();
		Connection conn = null;
		try {
			conn = personDAO.getConnection();
			Class.forName(properties.getProperty("jdbc.driverClassName"));
			conn = (OracleConnection) DriverManager.getConnection(properties
					.getProperty("jdbc.url"), properties
					.getProperty("jdbc.username"), properties
					.getProperty("jdbc.password"));
			conn = (OracleConnection) conn;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/*
	 * 
	 * Description: 
	 * param: 
	 * @author 彭小青 
	 * @date Sep 27, 2009 10:01:59 PM
	 */
	public List getSingleTableValue(HttpServletRequest request,
			final List proList, String table, String key, String id)
			throws SystemException, ServiceException {
		List propertyList = null;
		String tempsql = "";
		try {
			if (proList != null && proList.size() > 0) {
				StringBuffer sql = new StringBuffer("select ");
				for (int i = 0; i < proList.size(); i++) {// 根据属性列表定义查询语句中要查询的属性值
					ToaSysmanageProperty pro = (ToaSysmanageProperty) proList
							.get(i);
					sql.append(pro.getInfoItemField()).append(",");
				}
				tempsql = sql.toString();
				sql = new StringBuffer(tempsql.substring(0, sql.length() - 1))
						.append(" from ").append(table).append("  where ")
						.append(key).append("='").append(id).append("'");
				propertyList = getFiledValueList(request, proList, sql
						.toString());
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取信息项及对应信息项的值对象的列表" });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return propertyList;
	}

	/*
	 * 返回信息项及对应信息项的值对象的列表 
	 * @param proList 要查询的信息项列表 
	 * @param sql 原生SQL查询语句 @return
	 * FieldValue对象 记录信息项对象和对应项在数据库表中的值
	 */
	private List<FieldValue> getFiledValueList(HttpServletRequest request,
			final List proList, String sql) {
		List<FieldValue> propertyList = new ArrayList<FieldValue>();
		String url = infoManager.getCurrentURL();
		try {
			ResultSet rs = personDAO.executeJdbcQuery(sql);
			if (rs.next()) {// 如果有查询结果
				for (int i = 0; i < proList.size(); i++) {// 循环获取信息项对象进行相应操作
					ToaSysmanageProperty pro = (ToaSysmanageProperty) proList
							.get(i);
					String infofield = pro.getInfoItemField();
					String type = pro.getInfoItemDatatype();
					String value = null;
					if (type.equals(NUM)) { // 属性类型为“数值”型时,返回数值
						value = String.valueOf(rs.getInt(infofield));
					} else if (type.equals(PHOTO) || type.equals(FILE)) { // 属性类型为“图片”型时,返回空
						if (url.indexOf("oracle") != -1)
							value = "";
						else if (url.indexOf("microsoft") != -1
								|| url.indexOf("sqlserver") != -1)
							value = rs.getString(infofield);
					} else {
						value = rs.getString(infofield);
					}
					if (value == null || value.equals("null"))
						value = "";

					value = this.getValue(infofield, type, value);
					FieldValue fvalue = new FieldValue(pro, value);// 用信息项对象和从数据库中查询出来的值构造FieldValue对象
					propertyList.add(fvalue); // 将FieldValue对象加入列表中
					if (infofield.equals("STRUC_ID")) {
						request.setAttribute("oldstruct", value);
					}
				}
			} else { // 如果没有查询结果,则返回值为空的FieldValue对象
				for (int i = 0; i < proList.size(); i++) {
					ToaSysmanageProperty pro = (ToaSysmanageProperty) proList
							.get(i);
					FieldValue fvalue = new FieldValue(pro, "");
					propertyList.add(fvalue);
				}
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return propertyList;
	}

	/*
	 * 
	 * Description: 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 21, 2009 2:46:01 PM
	 */
	public String getValue(String infofield, String type, String value) {
		if (infofield.equals("PERSON_IS_OUT_LIMIT") && value != null
				&& !value.equals("")) {// 是否超编字段
			if (value.equals(ZERO)) {
				value = value + ",否";
			} else {
				value = value + ",是";
			}
		} else if (infofield.equals("STRUC_ID") && value != null
				&& !value.equals("")) { // 编制ID
			ToaStructure struct = (ToaStructure) strucManager
					.getStructureByID(value);
			if (struct != null) { // 如果字典项对象不为空,则获得该字典项编码及其中文描述
				value = value + "," + struct.getStrucTypeName();
			}
		} else if (type.equals(CODE) && value != null && !value.equals("")) { // 如果字段类型为代码引用型
			ToaSysmanageDictitem dictitem = (ToaSysmanageDictitem) manager
					.getDictItem(value);
			if (dictitem != null) { // 如果字典项对象不为空,则获得该字典项编码及其中文描述
				value = value + "," + dictitem.getDictItemShortdesc();
			}
		}
		return value;
	}

	/*
	 * 
	 * Description:编辑人员信息 
	 * param: 
	 * @author 彭小青 
	 * @date Sep 29, 2009 6:44:03 PM
	 */
	public void editPersonInfo(HttpServletRequest request)
			throws SystemException, ServiceException {
		try {
			List<FieldNameValue> changepropertyList = new ArrayList<FieldNameValue>();
			String propertyList = request.getParameter("propertyList");
			String keyid = request.getParameter("keyid");
			String infoSetCode = request.getParameter("infoSetCode");
			ToaSysmanageStructure struct = infoManager.getInfoSet(infoSetCode);
			String Structvalue = struct.getInfoSetValue();
			String[] proarray = propertyList.substring(0,
					propertyList.length() - 1).split(",");
			for (int i = 0; i < proarray.length; i++) {
				String ftype = this.getSingleProValue("INFO_ITEM_DATATYPE",
						"0", "T_OA_SYSMANAGE_PROPERTY", "INFO_ITEM_FIELD",
						proarray[i]);
				String type2 = VAR;
				if (ftype.equals(NUM)) { // 为数值型
					type2 = NUM;
				}
				// 获取机构表中对应属性的值
				String oldvalue = null;
				if (!ftype.equals(PHOTO) && !ftype.equals(FILE))
					oldvalue = this.getSingleProValue(proarray[i], type2,
							Structvalue, struct.getInfoSetPkey(), keyid);

				// 对应属性新修改的值
				String newvalue = request.getParameter(proarray[i]);
				if (newvalue == null || newvalue.equals("")) {// 如果所得值为空，做以下操作
					if (ftype.equals(NUM))// 如果为数值型，则赋值为0
						newvalue = "0";
					else
						// 如果为其他类型，则为null
						newvalue = null;
				}
				// 当数据库中值与编辑的值不一致时进行操作
				if ((oldvalue != null && newvalue != null && !oldvalue
						.equals(newvalue))
						|| (oldvalue == null && newvalue != null
								&& !ftype.equals(PHOTO) && !ftype.equals(FILE))
						|| (oldvalue != null && newvalue == null
								&& !ftype.equals(PHOTO) && !ftype.equals(FILE))) {
					FieldNameValue fnvalue = new FieldNameValue(proarray[i],
							ftype, newvalue);
					changepropertyList.add(fnvalue); // 得到对应属性的属性值
				}
				/*if ((ftype.equals(PHOTO) || ftype.equals(FILE))
						&& newvalue != null && !"".equals(newvalue)) { // 修改照片记录
					FieldNameValue fnvalue = new FieldNameValue(proarray[i],
							ftype, newvalue);
					changepropertyList.add(fnvalue); // 得到对应属性的属性值
				}*/
			}
			if("T_OA_HOLIDAY".equals(Structvalue)){	//休假子集
				String beginDate=request.getParameter("HOLIDYA_STIME");
				String endDate=request.getParameter("HOLIDAY_ETIME");
				String days=this.getDaysBetweenTwoDate(beginDate, endDate);
				FieldNameValue fnvalue = new FieldNameValue("REST_DAYS",
						NUM, days);
				changepropertyList.add(fnvalue); // 得到对应属性的属性值
			}
			this.updateSingleTableValue(changepropertyList, Structvalue, struct
					.getInfoSetPkey(), keyid); // 更新人员或人员子集记录
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "修改人员及人员子集信息" });
		}

	}

	/*
	 * 
	 * Description:更新人员或人员子集记录 
	 * param: 
	 * @author 彭小青
	 * @date Sep 29, 2009 6:44:36 PM
	 */
	public void updateSingleTableValue(List provalueList, String table,
			String key, String id) {
		Connection conn =null;
		try {
			conn= this.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			String tempsql = "";
			String lastsql = "";
			int k = 0;
			StringBuffer sql = new StringBuffer("update ").append(table)
					.append("  set ");// 修改表
			for (int i = 0; i < provalueList.size(); i++) {
				FieldNameValue fvalue = (FieldNameValue) provalueList.get(i);
				String pro = fvalue.getProname();
				String type = fvalue.getType();
				String value = fvalue.getValue();
				if (type.equals(YEAR) || type.equals(MONTH)
						|| type.equals(DATE)) {// 信息项为年、月、日期的话，但是数据库表对应的字段是varchar型，所以要转成varchar型
					type = VAR;
				}
				if (type.equals(NUM)) { // 如果为数值类型
					if (value == null || value.equals("")
							|| value.equals("null"))
						value = "0";
					sql.append(pro).append("=").append(value).append(","); // 根据属性修改表中具体记录
					k++;
				} else if (type.equals(DATETIME)) {// 如果为日期类型
					if (value == null || value.equals("")
							|| value.equals("null"))
						sql.append("'',");
					else
						sql.append(pro).append("=to_date('").append(value)
								.append("','yyyy-MM-dd'),");
					k++;
				} else if (type.equals(PHOTO) || type.equals(FILE)) {// 如果为图片类型
					/*if (value == null || value.equals("")
							|| value.equals("null")) {
						sql.append(pro).append("=empty_blob(),");
					} else {
						testBlob(conn, table, key, id, value, pro);// 修改图片
					}*/
				} else {
					sql.append(pro).append("='").append(fvalue.getValue())
							.append("',");
					k++;
				}
			}
			tempsql = sql.toString();
			if (k > 0) {
				sql = new StringBuffer(tempsql.substring(0,
						tempsql.length() - 1)).append(" where ").append(key)
						.append("='").append(id).append("'");
				lastsql = datesty.getSql(sql.toString()); // 通过日期格式处理类将SQL语句中的日期格式进行处理
				personDAO.executeJdbcUpdate(lastsql); // 执行修改语句
			}
			conn.commit();
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			if(conn!=null){
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 
	 * Description:保存人员照片
	 * param: 
	 * @author 彭小青 
	 * @date Sep 27, 2009 10:01:38 PM
	 */
	public void testBlob(Connection conn, String table, String key, String id,
			String value, String pro) {
		try {
			if (value != null && !"".equals(value)) {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate("update " + table + " set " + pro
						+ "=empty_blob() where " + key + "='" + id + "'");
				StringBuffer sql = new StringBuffer("select ").append(pro)
						.append(" from ").append(table).append(" where ")
						.append(key).append("='").append(id).append("'");
				ResultSet rs = stmt.executeQuery(sql.toString());
				if (rs.next()) {
					BLOB blob = (BLOB) rs.getBlob(1);
					OutputStream outStream = blob.getBinaryOutputStream();// 构造BLOB对象的输出字节流
					File f = new File(value);
					FileInputStream fin = new FileInputStream(f);
					byte[] b = new byte[blob.getBufferSize()];
					int len = 0;
					while ((len = fin.read(b)) != -1) {// 将文件对象通过字节流保存入数据库
						outStream.write(b, 0, len);
					}
					fin.close();
					outStream.flush();
					outStream.close();
				}
				rs.close();
				stmt.close();
				conn.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * @author：pengxq 
	 * @time：2009-1-5下午01:57:11 
	 * @desc:	文档流直接写入HttpServletResponse请求 
	 * @param 	response HttpServletResponse请求
	 * @param	String tempFileId 年内文件id 
	 * @return void
	 */
	public void setContentToHttpResponse(HttpServletResponse response,
			String personId) {
		try {
			ToaBasePerson person = this.getPersonByID(personId);
			response.reset();
			response.setContentType("application/octet-stream");
			OutputStream output = null;
			output = response.getOutputStream();
			output.write(person.getPersonPhoto());
			output.close();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "读取照片" });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * Description:获取单位里某种编制的人员数 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 8, 2009 5:36:28 PM
	 */
	public int getQuatoNum(String orgId, String strucId, String personId) {
		int num = 0;
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append(" select count(personid) from t_oa_base_person ")
					.append("where struc_id ='").append(strucId).append(
							"' and person_person_kind <> '").append(TXRY)
					.append("' and PERSON_ISDEL<>'").append(ONE).append(
							"' and PERSON_IS_OUT_LIMIT <>'").append(ONE)
					.append("'");
			ResultSet rs = personDAO.executeJdbcQuery(sql.toString());
			if (rs.next()) {
				num = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/*
	 * 
	 * Description:判断是否超编 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 8, 2009 5:53:35 PM
	 */
	public String isOutOfQuata(String orgId, String strucId, String personId) {
		int totalnum = 0;
		int personnum = this.getQuatoNum(orgId, strucId, personId);
		ToaStructure struct = strucManager.getStructureByID(strucId);
		if (struct != null) {
			totalnum = Integer.parseInt(struct.getStrucNumber());
		} else { // 该单位没有编制
			return null;
		}
		/*
		 * if(personId!=null&&!"".equals(personId)){//如果personId不为空，则+1
		 * personnum=personnum+1; }
		 */
		if (totalnum > personnum) { // 没有超编
			return ZERO;
		} else if (totalnum == personnum) { // 满编
			return ONE;
		} else { // 超编
			return TWO;
		}
	}

	/*
	 * 
	 * Description:删除数据库表的信息记录 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 12, 2009 8:48:45 AM
	 */
	public String deleteRelationInfo(ToaSysmanageStructure struct, String keyid)
			throws SystemException, ServiceException {
		String msg = "";
		try {
			String sql = "";
			String table = struct.getInfoSetValue();
			String key = struct.getInfoSetPkey();
			if (keyid != null && !keyid.equals("")) {
				String delarr[] = keyid.split(",");
				for (int i = 0; i < delarr.length; i++) {
					sql = "update " + table + " set ISDEL='" + ONE + "' where "
							+ key + "='" + delarr[i] + "'";
					personDAO.executeJdbcUpdate(sql);
				}
			}
		} catch (ServiceException e) {
			msg = "删除失败！";
			throw new ServiceException(MessagesConst.del_error);
		}
		return msg;
	}

	/*
	 * 
	 * Description:获取人员调配的信息项列表(人员调配专用方法) 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 13, 2009 3:00:24 PM
	 */
	public List<FieldValue> getSelectFiledValue(HttpServletRequest request,
			String fileds, String personId) throws SystemException,
			ServiceException {
		List<FieldValue> propertyList = new ArrayList<FieldValue>();
		StringBuffer beforeChangeStr = new StringBuffer("【");
		try {
			String filedarry[] = fileds.split(",");
			String sql = "select * from T_OA_BASE_PERSON where PERSONID='"
					+ personId + "'";
			ResultSet rs = personDAO.executeJdbcQuery(sql);
			if (rs.next()) {// 如果有查询结果
				for (int i = 0; i < filedarry.length; i++) {// 循环获取信息项对象进行相应操作
					ToaSysmanageProperty pro = itemmanager.getInfoItemByValue(
							filedarry[i], personstructcode);
					String infofield = pro.getInfoItemField();
					String type = pro.getInfoItemDatatype();
					String value = null;
					if (type.equals(NUM)) { // 属性类型为“数值”型时,返回数值
						value = String.valueOf(rs.getInt(infofield));
					} else if (type.equals(PHOTO) || type.equals(FILE)) {// 属性类型为“图片”型时,返回空
						value = "";
					} else {
						value = rs.getString(infofield);
					}
					if (value == null || value.equals("null"))
						value = "";
					value = this.buildChangeRecord(request, infofield, type,
							value);// 获取一些特殊信息项和数据类型为代码的信息项的中文描述
					FieldValue fvalue = new FieldValue(pro, value);// 用信息项对象和从数据库中查询出来的值构造FieldValue对象
					propertyList.add(fvalue); // 将FieldValue对象加入列表中

					// 如果调配类别的调配字段没有是否超编信息项，则添加上是否超编字典项
					if (infofield.equals("STRUC_ID")
							&& fileds.indexOf("PERSON_IS_OUT_LIMIT") == -1) {
						ToaSysmanageProperty pro1 = itemmanager
								.getInfoItemByValue("PERSON_IS_OUT_LIMIT",
										personstructcode);
						String tempvalue = rs.getString("PERSON_IS_OUT_LIMIT");
						if (tempvalue==null||ZERO.equals(tempvalue)||"".equals(tempvalue)) {
							tempvalue = value + ",否";
						} else {
							tempvalue = value + ",是";
						}
						FieldValue fvalue1 = new FieldValue(pro1, tempvalue);// 用信息项对象和从数据库中查询出来的值构造FieldValue对象
						propertyList.add(fvalue1);
						fileds+=",PERSON_IS_OUT_LIMIT";
					}

					if (defaultshow.indexOf(infofield) == -1) { // 记录调配前的人员信息
						beforeChangeStr.append(pro.getInfoItemSeconddisplay())
								.append(":").append(value).append(" ");
					}
				}
			} else { // 如果没有查询结果,则返回值为空的FieldValue对象
				for (int i = 0; i < filedarry.length; i++) {// 循环获取信息项对象进行相应操作
					ToaSysmanageProperty pro = itemmanager.getInfoItemByValue(
							filedarry[i], personstructcode);
					FieldValue fvalue = new FieldValue(pro, "");
					propertyList.add(fvalue);
				}
			}
			beforeChangeStr.append("】");
			request.setAttribute("beforeChangeStr", beforeChangeStr.toString());
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error);
		}
		return propertyList;
	}

	/*
	 * 
	 * Description:获取各个单位人数 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 13, 2009 2:23:53 PM
	 */
	public List getSumPersonPerOrg() throws SystemException, ServiceException {
		List list = new ArrayList();
		try {
			StringBuffer hql = new StringBuffer("");
			hql
					.append(
							"select t.baseOrg.orgid,count(t.personid) from ToaBasePerson t  ")
					.append("where t.personIsdel<>? ").append(
							" and t.personPersonKind<>?").append(
							" and t.personStructId is not null").append(
							" and t.personStructId<>'null'").append(
							" group by t.baseOrg.orgid");
			list = personDAO.find(hql.toString(), ONE, TXRY);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error);
		}
		return list;
	}

	/*
	 * 
	 * Description:获取单位内个编制下人员数量 
	 * param: 
	 * @author 胡丽丽 
	 * @date Oct 14, 2009 PM
	 */
	public List getSumPersonByStructure(String orgId) throws SystemException,
			ServiceException {
		List list = new ArrayList();
		try {
			StringBuffer hql = new StringBuffer("");
			hql
					.append(
							"select t.personStructId,count(t.personid) from ToaBasePerson t")
					.append(" where t.personIsdel<>?").append(
							" and t.baseOrg.orgid=?").append(
							" and t.personPersonKind<>?").append(
							" and t.personStructId is not null").append(
							" and t.personStructId<>'null'").append(
							" group by t.personStructId");
			list = personDAO.find(hql.toString(), ONE, orgId, TXRY);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error);
		}
		return list;
	}

	/*
	 * 
	 * Description:保存人员调配结果 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 14, 2009 10:23:05 PM
	 */
	public String ChangePersonInfo(HttpServletRequest request,
			PersonDeployInfo deployinfo) throws SystemException,
			ServiceException {
		String msg = "";
		try {
			List<FieldNameValue> changepropertyList = new ArrayList<FieldNameValue>();// 需调配的信息信息项
			boolean flag = false; // 是否转老干部标识
			String sfzr = null;
			String deployId = request.getParameter("deployId"); // 调配类别ID
			ToaPersonDeploy deployType = null; // 调配类别对象
			if (deployId != null && !"".equals(deployId)) {
				deployType = deploymanager.getOnePersonDeploy(deployId); // 根据调配类别ID获取调配类别对象
				sfzr = deployType.getPdepIsveteran();
			}
			String keyid = request.getParameter("keyid"); // 主键ID
			String infoSetCode = request.getParameter("infoSetCode"); // 信息集CODE
			ToaSysmanageStructure struct = infoManager.getInfoSet(infoSetCode); // 信息集对象
			String Structvalue = struct.getInfoSetValue(); // 信息集值
			StringBuffer afterChangeStr = new StringBuffer("【"); // 调配前的信息
			String propertyList = request.getParameter("propertyList"); // 调配信息项值字符串
			String[] proarray=null;
			if(propertyList.length()>0){
				 proarray = propertyList.substring(0,propertyList.length() - 1).split(",");
			}
			for (int i = 0; proarray!=null&&i < proarray.length; i++) { // 循环信息项
				ToaSysmanageProperty pro = itemmanager.getInfoItemByValue(
						proarray[i], personstructcode);
				String infofield = pro.getInfoItemField();
				String ftype = pro.getInfoItemDatatype();
				String value = request.getParameter(proarray[i]);
				FieldNameValue fnvalue = new FieldNameValue(proarray[i], ftype,
						value);
				changepropertyList.add(fnvalue);// 得到对应属性的属性值
				if ("PERSON_PERSON_KIND".equals(infofield) && value != null
						&& value.equals(TXRY)) {// 如果调配人员退休的话，需将该人员转为老干部
					flag = true;
				}
				if (defaultshow.indexOf(infofield) == -1) {// 记录调配后的人员信息
					String showvalue = this.buildChangeRecord(request,
							infofield, ftype, value);// 获取一些特殊信息项和数据类型为代码的信息项的中文描述
					afterChangeStr.append(pro.getInfoItemSeconddisplay())
							.append(":").append(showvalue).append(" ");
				}
			}
			if(propertyList.indexOf("ORG_ID")!=-1&&propertyList.indexOf("STRUC_ID")==-1){	//如果调配类别中包含了机构，单没包含编制
				FieldNameValue orgId = new FieldNameValue("STRUC_ID","0","");
				changepropertyList.add(orgId);
				FieldNameValue structId = new FieldNameValue("PERSON_IS_OUT_LIMIT","0","0");
				changepropertyList.add(structId);
			}
			if (sfzr != null && sfzr.equals("1")&&!flag) { // 如果该调配类别需将人员转入老干部去
				flag = true;
				FieldNameValue fnvalue = new FieldNameValue(
						"PERSON_PERSON_KIND", CODE, TXRY);
				changepropertyList.add(fnvalue);
				afterChangeStr.append("人员类别")
					.append(":").append("退休人员").append(" ");
			}
			afterChangeStr.append("】");
			this.updateSingleTableValue(changepropertyList, Structvalue, struct
					.getInfoSetPkey(), keyid); // 更新人员或人员子集记录

			/* 添加人员调配信息 */
			deployinfo.setPersonId(keyid); 			// 设置人员ID
			deployinfo.setExchangeTime(new Date()); // 设置调配时间
			deployinfo.setDeployInfo(deployType); 	// 设置调配类别
			deployinfo.setNewInfos(afterChangeStr.toString()); // 设置调配后的人员记录
			deploymanager.saveDeployInfo(deployinfo); // 保存调配信息

			/* 转老干部 */
			if (flag && keyid != null) {
				ToaBasePerson person = this.getPersonByID(keyid);
				if (person != null) {
					veteramanager.saveToaVeteranByPerson(person);
				}
			}
		} catch (DAOException e) {
			msg = "转老干部失败！";
			e.printStackTrace();
		} catch (ParseException e) {
			msg = "转老干部失败！";
			e.printStackTrace();
		} catch (ServiceException e) {
			msg = "人员调配失败！";
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "人员调配" });
		}
		return msg;
	}

	/*
	 * 
	 * Description:获取资源对象 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 15, 2009 3:04:46 PM
	 */
	public Properties getProperties() throws SystemException, ServiceException {
		try {
			Properties properties = new Properties();
			URL in = this.getClass().getClassLoader().getResource(
					"appconfig.properties");
			properties.load(new FileInputStream(in.getFile()));
			return properties;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "获取资源对象" });
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "获取资源对象" });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(MessagesConst.create_error,
					new Object[] { "获取资源对象" });
		}
	}

	/*
	 * 
	 * Description:获取一些特殊信息项和数据类型为代码的信息项的中文描述 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 16,2009 10:04:37 AM
	 */
	public String buildChangeRecord(HttpServletRequest request,
			String infofield, String type, String value) {
		String showvalue = ""; // 存入调配信息表的信息
		try {
			if (infofield.equals("ORG_ID") && !"".equals(value)) { // 组织机构ID
				ToaBaseOrg org = orgmanager.getOrgByID(value);
				if (org != null) {
					showvalue = org.getOrgName();
				}
			} else if (infofield.equals("PERSON_IS_OUT_LIMIT") && value != null
					&& !value.equals("")) {// 是否超编
				if (value.equals(ZERO)) {
					showvalue = "否";
				} else {
					showvalue = "是";
				}
			} else if (infofield.equals("STRUC_ID") && value != null
					&& !value.equals("")) {// 编制ID
				ToaStructure structs = (ToaStructure) strucManager
						.getStructureByID(value);
				if (structs != null) { // 如果字典项对象不为空,则获得该字典项编码及其中文描述
					showvalue = structs.getStrucTypeName();
					request.setAttribute("oldstruct", value + ","
							+ structs.getStrucTypeName());
				}
			} else if (type.equals(CODE) && value != null && !value.equals("")) {// 如果字段类型为代码引用型
				ToaSysmanageDictitem dictitem = (ToaSysmanageDictitem) manager
						.getDictItem(value);
				if (dictitem != null) { // 如果字典项对象不为空,则获得该字典项编码及其中文描述
					showvalue = dictitem.getDictItemShortdesc();
				}
				if (infofield.equals("PERSON_PERSON_KIND")) {
					request.setAttribute("personkind", value);
				}
			} else if (type.equals(PHOTO) || type.equals(FILE)) { // 照片
				showvalue = "";
			} else {
				showvalue = value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return showvalue;
	}

	public List<ToaBasePerson> getPersonsByIsdel(String isdel)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.personIsdel=? ";
			return personDAO.find(hql, isdel);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { " 根据删除清楚查询出错！" });
		}
	}

	/*
	 * 
	 * Description:获取单位中各种身份人员的人数 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 13, 2009 2:23:53PM
	 */
	public int getSumPersonByPerstatus(String orgId, String personstatus,
			String personId) throws SystemException, ServiceException {
		int num = 0;
		try {
			Object obj = null;
			StringBuffer hql = new StringBuffer("");
			hql.append("select count(t.personid) from ToaBasePerson t").append(
					" where t.personIsdel<>?").append(" and t.baseOrg.orgid=?")
					.append(" and t.personPersonKind<>?").append(
							" and t.person_status = ?");
			if (personId != null && !"".endsWith(personId)) {
				hql.append(" and t.personid<>?");
				obj = personDAO.findUnique(hql.toString(), ONE, orgId, TXRY,
						personstatus, personId);
			} else {
				obj = personDAO.findUnique(hql.toString(), ONE, orgId, TXRY,
						personstatus);
			}
			if (obj != null) {
				num = Integer.parseInt(obj.toString());
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return num;
	}

	/*
	 * 
	 * Description:判断单位领导职数和非领导职数是否超编 
	 * param: 
	 * @author 彭小青 
	 * @date Oct 8, 2009 5:53:35 PM
	 */
	public String isOutOfNum(String orgId, String personstatus, String personId) {
		int totalnum = 0;
		int personnum = 0;
		ToaBaseOrg org = orgmanager.getOrgByID(orgId);
		if (org != null) { // 机构对象不为空
			if (LEADER.equals(personstatus)) {// 人员身份为领导
				if (org.getLeadNumbe() == null || "".equals(org.getLeadNumbe())) {// 为空
					totalnum = 0;
				} else {
					totalnum = Integer.parseInt(org.getLeadNumbe());
				}
			} else { // 人员身份为非领导
				if (org.getNoLeadNumber() == null
						|| "".equals(org.getNoLeadNumber())) {
					totalnum = 0;
				} else {
					totalnum = Integer.parseInt(org.getNoLeadNumber());
				}
			}
		} else {
			return null;
		}
		personnum = this.getSumPersonByPerstatus(orgId, personstatus, personId);
		if (totalnum > personnum) { // 名额没有满
			return FOUR;
		} else {
			return THREE;
		}
	}

	/*
	 * 
	 * Description:获取所有人员信息列表，除已删除的和退休的人员 
	 * param: 
	 * @author 彭小青 
	 * @date Nov 13, 2009 2:52:07 PM
	 */
	public List<ToaBasePerson> getAllPerson() throws SystemException,
			ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.personIsdel=? and (t.personPersonKind is null or t.personPersonKind<>?) order by t.baseOrg.orgSyscode, t.personName";
			List<ToaBasePerson> list=personDAO.find(hql, ZERO, TXRY);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error);
		}
	}

	/*
	 * 
	 * @author: 彭小青 
	 * @date: Nov 17, 2009 10:16:39 AM 
	 * Desc: 根据统一用户ID获取对应人事的人员
	 * param:
	 */
	public ToaBasePerson getPersonByUumsPerId(String userid)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaBasePerson t where t.personIsdel<>? and t.uums_person_id=?";
			List<ToaBasePerson> list = personDAO.find(hql,ONE,userid);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error);
		}
		return null;
	}
	
	/*
	 * 
	 * Description:根据人员ID获取人员对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 13, 2010 3:00:30 PM
	 */
	public List<ToaBasePerson> getPersonsByIds(String personIds)throws SystemException,ServiceException {
		List<ToaBasePerson> returnList = new ArrayList<ToaBasePerson>();
		try {
			String personid="";
			String ids[]=personIds.split(",");
			for(int i=0;i<ids.length;i++){
				personid+=",'"+ids[i]+"'";
			}
			personid=personid.substring(1);
			String sql=" select t.personid, t.person_name,o.orgid,o.org_name from  t_oa_base_org o,t_oa_base_person t " +
					"where t.org_id=o.orgid  and t.personid in ("+personid+") order by t.org_id,t.person_name";
			ResultSet rs= personDAO.executeJdbcQuery(sql);
			if(rs!=null){
				ToaBasePerson person;
				ToaBaseOrg baseOrg;
				while(rs.next()){
					person=new ToaBasePerson();
					person.setPersonid(rs.getString(1));
					person.setPersonName(rs.getString(2));
					baseOrg=new ToaBaseOrg();
					baseOrg.setOrgid(rs.getString(3));
					baseOrg.setOrgName(rs.getString(4));
					person.setBaseOrg(baseOrg);
					returnList.add(person);
				}
			}
		}catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"根据人员ID获取人员对象"});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnList;
	}
	
	/*
	 * 
	 * Description:构造查询条件
	 * param:   	columnList 	当前用户的信息项权限列表
	 * param:		orgId		机构ID
	 * param:		condition  	复合查询设置的查询条件
	 * @author 	    彭小青
	 * @date 	    May 6, 2010 2:19:36 PM
	 */
	public String buildCondition(List<ToaSysmanageProperty> columnList,String orgId,String condition){
		StringBuffer con=new StringBuffer("");
		if(columnList!=null&&columnList.size()>0){	//信息项权限列表不为空	
			User user=userService.getCurrentUser();
			ToaPersonPrivil privil=groupManager.getPersonPrivil(user.getUserId());	//查找用户的人事人员访问权限
			if(privil!=null){								
				byte[] ids = privil.getPersonIds();
				if(ids!=null&&ids.length>0){
					String pids = new String(ids,0,ids.length);
					pids=HtmlUtils.htmlEscape(pids);
					String personIds="";
					String[] idArr=pids.split(",");
					for(int i=0;i<idArr.length;i++){
						personIds+=",'"+idArr[i]+"'";
					}
					personIds=personIds.substring(1);
					con.append(PERSON_TABLE_NAME)
						.append(".")
						.append(PERSON_PKEY)
						.append(" in(")
						.append(personIds)
						.append(")");
				}else{
					con.append(PERSON_TABLE_NAME)
						.append(".")
						.append(PERSON_PKEY)
						.append("='")
						.append(ZERO)
						.append("'");
					return con.toString();
				}
			}
			con.append(" and ( ")
				.append(PERSON_TABLE_NAME)
				.append(".PERSON_ISDEL is null or ")
				.append(PERSON_TABLE_NAME)
				.append(".PERSON_ISDEL='0') and ( ")
				.append(PERSON_TABLE_NAME)
				.append(".person_person_kind is null or ")
				.append(PERSON_TABLE_NAME)
				.append(".person_person_kind<>'")
				.append(TXRY)
				.append("') ");
			if(orgId!=null&&!"".equals(orgId)){			//机构ID不为空
				con.append(" and ")
					.append(PERSON_TABLE_NAME)
					.append(".ORG_ID='")
					.append(orgId)
					.append("' ");
			}else{		//pengxq于20110106添加
				boolean flag=userService.isViewChildOrganizationEnabeld();
				TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
				if(org!=null){
					con.append(" and ")
						.append(PERSON_TABLE_NAME)
						.append(".ORG_ID=o.ORGID");
					if(flag){
						con.append(" and o.USER_ORGCODE like '").append(org.getOrgSyscode()).append("%'");
					}else{
						con.append(" and o.USER_ORGID='").append(org.getOrgId()).append("'");
					}
				}else{
					con.append(" and o.USER_ORGCODE='0'");
				}
			}
			if(condition!=null&&!"".equals(condition)){	//复合查询条件不为空
				con.append(" and (")
					.append(condition)
					.append(")");
			}
		}
		return con.toString();
	}
	
	
	/*
	 * 
	 * Description:统计人员休假总天数SQL语句
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 6, 2010 2:34:53 PM
	 */
	public String buildTotalDaysSQL(String tables,String con){
		StringBuffer sql=new StringBuffer("");
		sql.append(" (select ")
			.append(HOLIDAY_TABLE_NAME)
			.append(".")
			.append(PERSON_PKEY)
			.append(",sum(")
			.append(HOLIDAY_TABLE_NAME)
			.append(".rest_days) totaldays ")
			.append(" from  ")
			.append(tables)
			.append(" where ")
			.append(con)
			.append(" group by ")
			.append(HOLIDAY_TABLE_NAME)
			.append(".")
			.append(PERSON_PKEY)
			.append(")");
		return sql.toString();
	}
	
	/*
	 * 
	 * Description:统计人员公休假天数SQL语句
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 6, 2010 2:34:53 PM
	 */
	public String buildHolidayDaysSQL(String tables,String con){
		String con1=con+" and "+HOLIDAY_TABLE_NAME+".IS_LEGAL='"+IS_LEGAL+"'";
		StringBuffer sql=new StringBuffer("");
		sql.append(" (select ").append(HOLIDAY_TABLE_NAME)
			.append(".")
			.append(PERSON_PKEY)
			.append(",sum(")
			.append(HOLIDAY_TABLE_NAME)
			.append(".rest_days) publicdays ")
			.append(" from  ")
			.append(tables)
			.append(" where ")
			.append(con1)
			.append(" group by ")
			.append(HOLIDAY_TABLE_NAME)
			.append(".")
			.append(PERSON_PKEY)
			.append(")");
		return sql.toString();
	}
	
	/*
	 * 
	 * Description:构造分页的休假情况统计sql
	 * param: 		tables  from子句内容
	 * param: 		flag 	区分是统计已休假列表还是未休假列表
	 * @author 	    彭小青
	 * @date 	    May 6, 2010 10:11:53 AM
	 */
	public String buildSql(String tables,String con,int start,int end,String forward)throws SystemException,ServiceException{
		try{
			int pagesize=end-start+1;
			int startrecode=start-1;
			String url = infoManager.getCurrentURL();
			String content=" person.personid,person.person_name,total.totaldays,holiday.publicdays";	//要查询的内容
			StringBuffer sql=new StringBuffer("select");
			if(url.indexOf("oracle")!=-1){
				sql.append(" m.personid,m.person_name,m.totaldays,m.publicdays from( select rownum rn,");			
			}else if(url.indexOf("microsoft")!=-1||url.indexOf("sqlserver")!=-1){
				sql.append(" top ").append(pagesize);
			}
			sql.append(content)
				.append(" from ")
				.append(this.buildTotalDaysSQL(tables,con))
				.append("total")
				.append(" left outer join ")
				.append(this.buildHolidayDaysSQL(tables,con))
				.append("holiday on total.personid=holiday.personid")
				.append(" left outer join ")
				.append(PERSON_TABLE_NAME)
				.append(" person on total.personid=person.personid");
			if("norest".equals(forward)){	//未休假统计
				sql.append(" where holiday.publicdays is null or holiday.publicdays=0 ");
			}
			sql.append(" order by total.personid");
			if(url.indexOf("oracle")!=-1){
				sql.append(")m where m.rn between ")
					.append(start)
					.append(" and ")
					.append(end);
			}else if (url.indexOf("mysql")!=-1){
				sql.append(" limit ")
					.append(startrecode)
					.append(",")
					.append(pagesize);
			}
			return sql.toString();
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[]{"构造休假情况统计sql"});
		} 
	}
	
	/*
	 * 
	 * Description:构造休假情况统计分页列表
	 * param：		page		休假情况统计分页列表
	 * param：		columnList  当前用户的拥有权限的信息项列表
	 * param: 		tables  	from子句内容
	 * param: 		condition   复合查询设置的查询条件
	 * param：		orgId		机构ID
	 * param: 		objName     休假统计列表上按姓名查询
	 * param：		forward     区分是统计已休假列表还是未休假列表
	 * @author 	     彭小青
	 * @date 	   	May 6, 2010 10:52:41 AM
	 */
	public Page<Object[]> getHolidayStatistic(Page<Object[]> page,List<ToaSysmanageProperty> columnList,String tables,String condition,String orgId,String objName,String forward)throws SystemException,ServiceException{
		try{
			Object[] obj;
			List<Object[]> list=new ArrayList<Object[]>();
			int totalRecord=0;	//总记录数
			int recordsperpage = page.getPageSize(); 				//每页记录数
			int currentpage =page.getPageNo()==0?1:page.getPageNo();//当前页
			int start=(currentpage-1)*recordsperpage+1;
			int end  =currentpage*recordsperpage;		
			if(tables==null||"".equals(tables)){			//当没有设置查询条件时
    			tables=persontable;
    		}
    		if(tables.indexOf(HOLIDAY_TABLE_NAME)==-1){		//如果from自子句中没有休假子集，则添加休假子集
    			tables+=","+holidaytable;	
    		}
    		if(orgId==null||"".equals(orgId)||"null".equals(orgId)){//pengxq于20110106添加
				tables+=" ,T_OA_BASE_ORG o";
    		}
    		StringBuffer con=new StringBuffer("");			
    		con.append(this.buildCondition(columnList,orgId,condition))	//根据当前用户的信息项权限列表和人事人员访问权限构造查询条件
	    			.append(" and ")
	    			.append(holidayfkey)				//查询条件中增加休假子集与人员关联条件
	    			.append("=")
	    			.append(personkey);					
    		if(objName!=null&&!"".equals(objName)){		//休假统计列表上按姓名查询
    			con.append(" and ")
    				.append(PERSON_TABLE_NAME)
    				.append(".PERSON_NAME like '%")
    				.append(objName)
    				.append("%' ");	
    		}
			StringBuffer sqlstr=new StringBuffer("");
			sqlstr.append(" select count(m.personid) from ")
					.append(this.buildTotalDaysSQL(tables,con.toString()))
					.append("m");	//构造统计总记录数sql
			ResultSet rs=personDAO.executeJdbcQuery(sqlstr.toString());
			if(rs.next()){
				totalRecord = rs.getInt(1);
			}
			rs.close();
			page.setPageNo(currentpage);
			page.setTotalCount(totalRecord);
			String sql=this.buildSql(tables,con.toString(),start,end,forward);	//构建sql
			rs=personDAO.executeJdbcQuery(sql);
			while(rs.next()){
				obj=new Object[4];
				obj[0]=rs.getString(1);	//人员ID
				obj[1]=rs.getString(2); //人员姓名
				obj[2]=rs.getInt(3);	//已休假天数
				obj[3]=rs.getInt(4);	//公休假天数
				list.add(obj);
			}
			page.setResult(list);
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[]{"构造休假情况统计分页列表"});
		} 
		return page;
	}
	
	/*
	 * 
	 * Description:查看休假情况统计明细
	 * param: 		tables 	  from子句内容
	 * param：		condition 复合查询条件
	 * param：		personId  人员ID
	 * @author 	    彭小青
	 * @date 	    May 7, 2010 6:45:44 PM
	 */
	public List<Object[]> getHolidayStatisticDetail(String tables,String condition,String personId)throws SystemException,ServiceException{
		List<Object[]> list=new ArrayList<Object[]>();
		try{
			Object[] obj;
			if(tables==null||"".equals(tables)){		//当没有设置查询条件时
	    		tables=persontable;
	    	}
	    	if(tables.indexOf(HOLIDAY_TABLE_NAME)==-1){	//如果from自子句中没有休假子集，则添加休假子集
	    		tables+=","+holidaytable;	
	    	}
			StringBuffer querySql=new StringBuffer("");
			querySql.append("select T_OA_HOLIDAY.* from ")
					.append(tables)
					.append(" where ")
			    	.append(holidayfkey)
		    		.append("=")
		    		.append(personkey)
		    		.append(" and ")
		    		.append(holidayfkey)
		    		.append("='")
		    		.append(personId)
		    		.append("'");
	    	if(condition!=null&&!"".equals(condition)){
	    		querySql.append(" and (")
	    				.append(condition)
	    				.append(")");   
	    	}
	    	querySql.append(" order by T_OA_HOLIDAY.HOLIDYA_STIME");
			ResultSet rs=personDAO.executeJdbcQuery(querySql.toString());
			while(rs.next()){
				obj=new Object[7];
				obj[0]=rs.getString(1);	//主键
				obj[1]=rs.getString(5); //休假开始时间
				obj[2]=rs.getString(6); //休假结束时间
				obj[3]=rs.getString(3); //休假事由
				obj[4]=rs.getString(8);	//是否公休假
				obj[5]=rs.getInt(7);	//休假天数
				obj[6]=rs.getString(4); //备注
				list.add(obj);
			}
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查看休假情况统计明细"});
		} 
		return list;
	}
	
	/*
	 * 
	 * Description:满足任职年限统计
	 * param: 		tables 			from子句内容
	 * param：		con  			组装好的查询条件
	 * param：		dictItemList 	职务字典项
	 * @author 	    彭小青
	 * @date 	    May 7, 2010 10:51:23 AM
	 */
	public List<Object[]> getDutyStatistic(String tables,String con,List<ToaSysmanageDictitem> dictItemList)throws SystemException,ServiceException{
		try{
			List<Object[]> list=new ArrayList<Object[]>();
			Map<String,String> map=new HashMap<String,String>();
			for(int i=0;i<dictItemList.size();i++){
				map.put(dictItemList.get(i).getDictItemCode(), dictItemList.get(i).getDictItemShortdesc());
			}
			if(tables==null||"".equals(tables)){							//当没有设置查询条件时
				tables=persontable;
			}
			tables+=" ,T_OA_BASE_ORG o";
			StringBuffer querySql=new StringBuffer("");
			querySql.append("select t.person_pset,count(t.personid) from ")
					.append(PERSON_TABLE_NAME)
					.append(" t where t.person_pset is not null and t.personid in (select ")
					.append(personkey)
					.append(" from ")
					.append(tables)
					.append(" where ")
					.append(con)
					.append(") group by t.person_pset");
			ResultSet rs=personDAO.executeJdbcQuery(querySql.toString());
			if(rs!=null){
				Object[] obj=new Object[2];
				while(rs.next()){
					obj=new Object[3];
					obj[0]=rs.getString(1);	//职务字典项ID
					obj[1]=map.get(obj[0]); //职务字典项名
					obj[2]=rs.getInt(2);	//人数
					list.add(obj);
				}
			}
			rs.close();
			return list;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"满足任职年限统计"});
		} 
		return null;
	}
	
	/*
	 * 
	 * Description:查看满足任职年限人员明细（分页）
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 7, 2010 10:08:18 PM
	 */
	public Page<Object[]> getDutyStatisticDetail(Page<Object[]> page,String tables,String condition,String dictCode)throws SystemException,ServiceException{
		try{
			Object[] obj;
			List<Object[]> list=new ArrayList<Object[]>();
			int recordsperpage = page.getPageSize(); 				//每页记录数
			int currentpage =page.getPageNo()==0?1:page.getPageNo();//当前页
			int start=(currentpage-1)*recordsperpage+1;
			int end  =currentpage*recordsperpage;	
			int pagesize=end-start+1;
			int startrecode=start-1;
			int totalRecord=0;	//总记录数
			String content="";
			String queryContent=" t.personid,t.person_name,t.person_born,t.person_work_time,t.person_pset,t.person_pset_time,t.simple_level_time ";
			String url = infoManager.getCurrentURL();
			if(tables==null||"".equals(tables)){	//当没有设置查询条件时
				tables=persontable;
			}
			tables+=" ,T_OA_BASE_ORG o";
			if(url.indexOf("oracle")!=-1){
				content=" rownum rn,";
			}else if(url.indexOf("microsoft")!=-1||url.indexOf("sqlserver")!=-1){
				content=" top "+(pagesize);
			}
			StringBuffer querySql=new StringBuffer("");
			querySql.append("select ")
					.append(content)
					.append(queryContent)
					.append(" from ")
					.append(PERSON_TABLE_NAME)
					.append(" t where t.person_pset is not null and t.personid in (select ")
					.append(personkey)
					.append(" from ")
					.append(tables)
					.append(" where ")
					.append(condition)
					.append(" and t.person_pset='")
					.append(dictCode)
					.append("')")
					.append(" order by t.personid");
			StringBuffer sqlstr=new StringBuffer("");
			sqlstr.append("select count(m.personid) from ( ")
					.append(querySql.toString())
					.append(")m");	//构造统计总记录数sql
			ResultSet rs=personDAO.executeJdbcQuery(sqlstr.toString());
			if(rs.next()){
				totalRecord = rs.getInt(1);
			}
			rs.close();
			page.setPageNo(currentpage);
			page.setTotalCount(totalRecord);
			sqlstr=new StringBuffer("");
			if(url.indexOf("oracle")!=-1){			//oracle数据库
				sqlstr.append(" select ")
					.append(queryContent)
					.append(" from (")
					.append(querySql)
					.append(")t where t.rn between ")
					.append(start)
					.append(" and ")
					.append(end);
			}else if(url.indexOf("microsoft")!=-1||url.indexOf("sqlserver")!=-1){//sqlserver数据库
				sqlstr.append(querySql);
			}else if (url.indexOf("mysql")!=-1){	//mysql数据库
				sqlstr.append(querySql)
					.append(" limit ")
					.append(startrecode)
					.append(",")
					.append(pagesize);
			}
			rs=personDAO.executeJdbcQuery(sqlstr.toString());
			while(rs.next()){
				obj=new Object[7];
				obj[0]=rs.getString(1);	//人员ID
				obj[1]=rs.getString(2); //人员姓名
				obj[2]=rs.getString(3); //出生日期
				obj[3]=rs.getString(4); //参加工作时间
				obj[4]=rs.getString(5); //现任职职务
				obj[5]=rs.getString(6); //现任职时间
				obj[6]=rs.getString(7); //任同职、级时间
				list.add(obj);
			}
			page.setResult(list);
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查看满足任职年限人员明细"});
		} 
		return page;
	}
	
    /*
     * 
     * Description:保存图片或文件
     * param: 
     * @author 	    彭小青
     * @date 	    Jul 29, 2010 3:32:51 PM
     */
    public String savePhoto(File file,String personId)throws SystemException,ServiceException,FileNotFoundException{
    	String msg="";
    	try{
    		ToaBasePerson person=this.getPersonByID(personId);
    		FileInputStream fis = new FileInputStream(file);
    		byte[] buf = new byte[(int)file.length()];
    		fis.read(buf);
    		person.setPersonPhoto(buf);
    		personDAO.save(person);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}catch(ServiceException e){
    		throw new ServiceException(MessagesConst.find_error,new Object[]{"保存图片或文件"});
    	} 
    	return msg;
    }

	@Autowired
	public void setManager(DictItemManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setInfoManager(InfoSetManager infoManager) {
		this.infoManager = infoManager;
	}

	@Autowired
	public void setStrucManager(PersonStructureManager strucManager) {
		this.strucManager = strucManager;
	}

	@Autowired
	public void setItemmanager(InfoItemManager itemmanager) {
		this.itemmanager = itemmanager;
	}

	@Autowired
	public void setOrgmanager(PersonOrgManager orgmanager) {
		this.orgmanager = orgmanager;
	}

	@Autowired
	public void setDeploymanager(Deploymanager deploymanager) {
		this.deploymanager = deploymanager;
	}

	@Autowired
	public void setVeteramanager(Veteranmanager veteramanager) {
		this.veteramanager = veteramanager;
	}

}
