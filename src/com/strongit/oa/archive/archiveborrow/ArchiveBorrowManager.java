/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: zhangli
 * Version: V1.0
 * Description： 借阅档案管理MANAGER
 */
package com.strongit.oa.archive.archiveborrow;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaArchiveBorrow;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@OALogger
public class ArchiveBorrowManager {
	/** 档案借阅管理dao*/
	private GenericDAOHibernate<ToaArchiveBorrow, java.lang.String> borrowDao;

	/** 用户管理接口*/
	private IUserService userservice;

	/** 待审*/
	private static final String NO = "0";
	/** 全部*/
	private static final String ALL = "4";

	/**
	 * @roseuid 4958CFDE01E2
	 */
	public ArchiveBorrowManager() {
	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 4958CDB30343
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		borrowDao = new GenericDAOHibernate<ToaArchiveBorrow, java.lang.String>(
				sessionFactory, ToaArchiveBorrow.class);
	}

	/**
	 * author:zhangli
	 * description:注册用户service
	 * modifyer:
	 * description:
	 * @param userservice 用户service
	 */
	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	/**
	 * 获取对应文件下所有的借阅信息
	 * 
	 * @param fileId 档案文件编号
	 * @return java.util.List 借阅信息列表
	 * @roseuid 4958CDF40132
	 */
	public List getAllBorrow(String fileId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return borrowDao.find(
					"from ToaArchiveBorrow t where t.toaArchiveFile.fileId=?",
					fileId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"借阅信息列表"});
		}
	}

	/**
	 * 获取借阅信息分页列表
	 * 
	 * @param page 分页对象
	 * @param model 借阅对象
	 * @return com.strongmvc.orm.hibernate.Page 借阅分页对象
	 * @roseuid 4958CE150182
	 */
	public Page<ToaArchiveBorrow> getAllBorrow(Page<ToaArchiveBorrow> page,
			ToaArchiveBorrow model,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			Object[] obj = new Object[7];
			StringBuffer hql = new StringBuffer("from ToaArchiveBorrow t where 1=1");
			int i = 0;
			/** 获取档案文件对象*/
			ToaArchiveFile file = model.getToaArchiveFile();

			/** 如果文件编码不为空，则设置文件编码过滤条件*/
			if (file != null && file.getFileNo() != null
					&& !file.getFileNo().equals("")) {
				hql.append(" and t.toaArchiveFile.fileNo like ?");
				obj[i] = "%" + file.getFileNo() + "%";
				i++;
			}

			/** 如果文件标题不为空，则设置文件标题过滤条件*/
			if (file != null && file.getFileTitle() != null
					&& !file.getFileTitle().equals("")) {
				hql.append(" and t.toaArchiveFile.fileTitle like ?");
				obj[i] = "%" + file.getFileTitle() + "%";
				i++;
			}

			/** 如果借阅时间不为空，则设置借阅时间过滤条件*/
			if (model.getBorrowFromtime() != null && !model.getBorrowFromtime().equals("")) {
				hql.append(" and t.borrowFromtime=?");
				obj[i] = model.getBorrowFromtime();
				i++;
			}

			/** 如果归还时间不为空，则设置归还时间过滤条件*/
			if (model.getBorrowEndtime() != null && !model.getBorrowEndtime().equals("")) {
				hql.append(" and t.borrowEndtime=?");
				obj[i] = model.getBorrowEndtime();
				i++;
			}

			/** 如果借阅状态不为空，则设置借阅状态过滤条件*/
			if (model.getBorrowAuditing() != null
					&& !model.getBorrowAuditing().equals("")) {
				if(!ALL.equals(model.getBorrowAuditing())){
					hql.append(" and t.borrowAuditing=?");
					obj[i] = model.getBorrowAuditing();
					i++;
				}
			}

			/** 如果借阅者名称不为空，则设置借阅者名称过滤条件*/
			if (model.getBorrowPersonname() != null
					&& !model.getBorrowPersonname().equals("")) {
				hql.append(" and t.borrowPersonname like ?");
				obj[i] = "%" + model.getBorrowPersonname() + "%";
				i++;
			}

			/** 如果查看状态不为空，则设置查看状态过滤条件*/
			if (model.getBorrowViewState() != null
					&& !model.getBorrowViewState().equals("")) {
				hql.append(" and t.borrowViewState like ?");
				obj[i] = "%" + model.getBorrowViewState() + "%";
				i++;
			}

			/** 如果借阅者编号不为空，则设置借阅者编号过滤条件*/
			if (model.getBorrowPersonid() != null
					&& !model.getBorrowPersonid().equals("")) {
				hql.append(" and t.borrowPersonid like ?");
				obj[i] = "%" + model.getBorrowPersonid() + "%";
				i++;
			}

			/** 如果审核结果说明不为空，则设置审核结果说明过滤条件*/
			if (model.getBorrowAuditingDesc() != null
					&& !model.getBorrowAuditingDesc().equals("")) {
				hql.append(" and t.borrowAuditingDesc like ?");
				obj[i] = "%" + model.getBorrowAuditingDesc() + "%";
				i++;
			}

			hql.append(" order by t.borrowTime DESC ");

			if (i == 0) {
				page = borrowDao.findAll(page);
			} else if (i == 1) {
				page = borrowDao.find(page, hql.toString(), obj[0]);
			} else if (i == 2) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1]);
			} else if (i == 3) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2]);
			} else if (i == 4) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2],
						obj[3]);
			} else if (i == 5) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2],
						obj[3], obj[4]);
			} else if (i == 6) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2],
						obj[3], obj[4], obj[5]);
			} else if (i == 7) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2],
						obj[3], obj[4], obj[5], obj[6]);
			}else if (i == 8) {
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2],
						obj[3], obj[4], obj[5], obj[6], obj[7]);
			}else if(i==9){
				page = borrowDao.find(page, hql.toString(), obj[0], obj[1], obj[2],
						obj[3], obj[4], obj[5], obj[6], obj[7],obj[9]);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"借阅信息分页列表"});
		}
	}

	/**
	 * 获取借阅信息对象
	 * 
	 * @param borrowId 借阅记录编号
	 * @return com.strongit.oa.bo.ToaArchiveBorrow 借阅记录对象
	 * @roseuid 4958CE44000D
	 */
	public ToaArchiveBorrow getBorrow(String borrowId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return borrowDao.get(borrowId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"借阅记录对象"});
		}
	}

	/**
	 * 保存借阅申请信息
	 * 
	 * @param model 借阅记录对象
	 * @return java.lang.String 保存结果
	 * @roseuid 4958CE7103E7
	 */
	public String saveBorrow(ToaArchiveBorrow model,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			String message = null;
			if (model.getBorrowId() == null) {
				model.setBorrowAuditing(NO);
				model.setBorrowViewState(NO);
			}
			borrowDao.save(model);
			message = "申请借阅成功！";

			return message;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"借阅记录对象"});
		}
	}

	/**
	 * author:zhangli
	 * description:审核借阅申请
	 * modifyer:
	 * description:
	 * @param borrowId 借阅记录编号
	 * @param borrowAuditing 借阅审核状态
	 * @param borrowAuditingDesc 借阅审核描述
	 * @return
	 */
	public String auditBorrow(String borrowId, String borrowAuditing,
			String borrowAuditingDesc,OALogInfo ... loginfos) throws SystemException,ServiceException{
		String message = null;
		try {
			String[] ids = borrowId.split(",");
			for(int i=0; i<ids.length ;i++){
				/** 获取当前用户*/
				User userinfo = getCurrentUser();
				/** 根据借阅编号获取借阅信息对象*/
				ToaArchiveBorrow model = getBorrow(ids[i]);
				model.setBorrowAuditing(borrowAuditing);
				model.setBorrowAuditingDesc(borrowAuditingDesc);
				if (userinfo != null) {
					model.setBorrowAuditingName(userinfo.getUserName());
				}
				model.setBorrowAuditingTime(new Date());
				borrowDao.save(model);
				message = "操作成功！";
			}
			return message;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.aduit_error,               
					new Object[] {"借阅申请"});
		}
	}

	/**
	 * 删除借阅申请信息
	 * 
	 * @param borrowId 借阅记录编号
	 * @return java.lang.String 删除结果
	 * @roseuid 4958CE99011A
	 */
	public String delBorrow(String borrowId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		String message = null;
		try {
			String[] str = borrowId.split(",");
			for (String a : str) {
				borrowDao.delete(a);
			}
			message = "删除借阅记录成功！";
			return message;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"借阅记录"});
		}

	}

	/**
	 * author:zhangli
	 * description:获取当前用户对象
	 * modifyer:
	 * description:
	 * @return 当前用户对象
	 */
	public User getCurrentUser() {
		return userservice.getCurrentUser();
	}

	/**
	 * 
	 * @author：pengxq
	 * @time：2009-3-4上午09:55:30
	 * @desc: 根据部门id获取部门名称
	 * @param String orgId 部门id
	 * @return 部门名称
	 */
	public String getOrgNameById(String orgId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			Organization org=userservice.getDepartmentByOrgId(orgId);
			if(org!=null){
				return org.getOrgName();
			}else{
				return null;
			}	
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件编号是否存在"});
		}	
	}
	/**
	 * 根据日期和已审核状态统计文件借阅情况
	 * @author hull
	 * @date 2010-04-12
	 * @param borrowMonth
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportBean> getBorrowReport(Date borrowMonth)throws SystemException,ServiceException{
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String month=getDateToString(borrowMonth, "MM");
			String year=getDateToString(borrowMonth, "yyyy");
			Date end=null;
			/**
			 * 当前月份是否是小月：是true 否false
			 */
			if("04".equals(month)&&"06".equals(month)&&"09".equals(month)&&"11".equals(month)){
				end=df.parse(year="-"+month+"-30");
			}else if("02".equals(month)){
				if(Integer.parseInt(year)%4==0){
					end=df.parse(year+"-"+month+"-29");
				}else{
					end=df.parse(year+"-"+month+"-28");
				}
			}else{
				end=df.parse(year+"-"+month+"-31");
			}

			String hql="select t.borrowFromtime,t.borrowPersonid,t.borrowPersonname,t.toaArchiveFile.fileNo,t.toaArchiveFile.fileTitle,"+
			"t.toaArchiveFile.filePieceNo,t.borrowDesc,t.borrowEndtime from ToaArchiveBorrow t where t.borrowAuditing='1' and t.borrowFromtime>=? and t.borrowFromtime<=?";
			List<Object> list=borrowDao.find(hql, borrowMonth,end);
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			for(Object obj:list){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				Date begin=(Date)strobj[0];//借阅日期
				if(begin!=null){
					bean.setText1(df.format(begin));
				}else{
					bean.setText1("  ");
				}
				String userid=strobj[1].toString();//借阅人ID
				Organization org= userservice.getUserDepartmentByUserId(userid);
				bean.setText2(org.getOrgName());
				if(strobj[2]!=null){
					bean.setText3(strobj[2].toString());
				}else{
					bean.setText3("  ");
				}
				if(strobj[3]!=null){
					bean.setText4(strobj[3].toString());
				}else{
					bean.setText4("  ");
				}
				if(strobj[4]!=null){
					bean.setText8(strobj[4].toString());
				}else{
					bean.setText8("  ");
				}
				if(strobj[5]!=null){
					bean.setText5(strobj[5].toString());
				}else{
					bean.setText5("  ");
				}
				if(strobj[6]!=null){
					bean.setText6(strobj[6].toString());
				}else{
					bean.setText6("  ");
				}
				if(strobj[7]!=null){
					bean.setText7(getDateToString((Date)strobj[7],"yyyy-MM-dd"));
				}else{
					bean.setText7("  ");
				}
				reportList.add(bean);
			}
			return reportList;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 报日期转换成字符串
	 * @param date
	 * @param temp
	 * @return
	 */
	public String getDateToString(Date date,String temp){
		SimpleDateFormat df=new SimpleDateFormat(temp);
		return df.format(date);
	}
}
