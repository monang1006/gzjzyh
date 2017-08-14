/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2010-08-11
 * Autour: qint
 * Version: V1.0
 * Description：公文收文manager
 */
package com.strongit.doc.receives;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.Recivedoc;
import com.strongit.oa.bo.Tdocattach;
import com.strongit.oa.bo.ToaRecvdoc;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class RecvDocManager {
	// 统一用户服务
	private IUserService user;
	
	private GenericDAOHibernate<TdocSend, java.lang.String> sendsDao;
	
	private GenericDAOHibernate<TtransDoc, java.lang.String> ttranDocDao;
	
	private GenericDAOHibernate<TtransDocAttach, java.lang.String> docAttachDao;	//公文传输  附件dao
	
	private GenericDAOHibernate<Tdocattach, java.lang.String> tdocattachDao;	//公文附件dao

	static String UPLOADDIR = "transdoc.upload.dir";
	@Autowired IUserService userService ;						//统一用户服务类.
	@Autowired
	SessionFactory sessionFactory; // 提供session
	private List<ToaSysmanageDictitem> jjcdItems ;								//紧急程度字典项
	@Autowired IDictService dictService ;										//字典服务类
	@Autowired IAttachmentService iAachment;
	/**
	 * author:lanlc description:构造方法 modifyer: description:
	 * 
	 * @return
	 */
	public RecvDocManager() {
	}

	/**
	 * 注入SESSION工厂
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		sendsDao = new GenericDAOHibernate<TdocSend, java.lang.String>(
				sessionFactory, TdocSend.class);
		ttranDocDao= new GenericDAOHibernate<TtransDoc, java.lang.String>(
				sessionFactory, TtransDoc.class);
		docAttachDao= new GenericDAOHibernate<TtransDocAttach, java.lang.String>(
				sessionFactory, TtransDocAttach.class);
		tdocattachDao= new GenericDAOHibernate<Tdocattach, java.lang.String>(
				sessionFactory, Tdocattach.class);
	}

	/**
	 * 获取当前用户
	 * 
	 * @author:
	 * @date:
	 * @return
	 * @throws SystemException
	 */
	public User getCurrentUser() throws SystemException {
		return user.getCurrentUser();
	}

	/**
	 * 根据当前用户部门syscode获取所有拒签列表
	 * 
	 * @author:xiaolj
	 * @date:2013-2-26  16:06:12
	 * @return
	 * @throws SystemException
	 */
	@Transactional(readOnly = true)
	public Page getTtransSendDocs(Page page, TtransDoc modelDoc, String recvState,
			Date sendStartDate, Date sendEndDate, Date recvStartDate,
			Date recvEndDate) throws SystemException, ServiceException {
		return getTtransSendDocs(page, modelDoc, recvState, sendStartDate,
				sendEndDate, recvStartDate, recvEndDate, null);
	}
	
	/**
	 * author:xiaolj description:获取公文列表 modifyer:
	 * 
	 * @param page
	 * @param userLonginName
	 * @param searchType
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getTtransSendDocs(Page page, TtransDoc modelDoc, String recvState,
			Date sendStartDate, Date sendEndDate, Date recvStartDate,
			Date recvEndDate, String finingState) throws SystemException,
			ServiceException {
		try {
			Page<TdocSend> page1;
			ArrayList list = new ArrayList();
			Calendar te = Calendar.getInstance();

			String deptCode = user.getUserDepartmentByUserId(getCurrentUser().getUserId()).getOrgSyscode();
			
			//StringBuffer myhql = new StringBuffer("select d.docId from TtransDoc d where d.rest3='" + deptCode +"'");
			//List mylist = ttranDocDao.find(myhql.toString());
			
			String isSuperAdmin = getCurrentUser().getUserIsSupManager();
			StringBuffer hql = new StringBuffer(
					"select d.ttransDoc.docCode, d.ttransDoc.docTitle,d.ttransDoc.docIssueDepartSigned,d.ttransDoc.docSendTime,"
							+ "d.docRecvTime,d.docRecvRemark ,d.docFilingTime,d.operateIp ,d.ttransDoc.docSecretLvl ,d.ttransDoc.docEmergency,"
							+ "d.ttransDoc.docKeywords, d.senddocId, d.liyous,d.qita,d.ttransDoc.docSubmittoDepart,d.deptName from TdocSend d where (d.isdelete is null or d.isdelete <> '1') and d.recvState= "
							+ recvState 
							);//+ " and ('admin'= '"+ getCurrentUser().getUserLoginname() + "' or  '" + deptCode + "' like d.deptCode||'%')");
			/***
			 * 修改， 获取南昌市办公厅，获取发给001的公文
			 */
			User user = userService.getCurrentUser();
			String usercod = user.getSupOrgCode();
			/*if(!"1".equals(isSuperAdmin)){//如果不是超级管理员
				//hql.append(" and ('").append(deptCode).append("' like d.deptCode||'%')");
				if(usercod !=null&&usercod.trim().equals("001"))
				{
					hql.append(" and (d.deptCode= '"
							 + deptCode	+"' or d.deptCode='001')");
				}else
				{
					hql.append(" and d.deptCode= '"
							 + deptCode	+"'");
				}
				
			}*/
			
			if (modelDoc != null) {
				// 单位
				if (modelDoc.getDocIssueDepartSigned() != null
						&& !"".equals(modelDoc.getDocIssueDepartSigned())) {
					hql.append(" and d.ttransDoc.docIssueDepartSigned like '%"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocIssueDepartSigned()).trim() + "%'");
				}
				// 名
				if (modelDoc.getDocTitle() != null
						&& !"".equals(modelDoc.getDocTitle())) {
					int t = modelDoc.getDocTitle().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocTitle().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocTitle()).trim() + "%'");
					}
				}
				// 公文文号
				if (modelDoc.getDocCode() != null
						&& !"".equals(modelDoc.getDocCode())) {
					int t = modelDoc.getDocCode().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocCode().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docCode like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docCode like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocCode()).trim() + "%'");
					}
				}
				// 秘密等级
				if (modelDoc.getDocSecretLvl() != null
						&& !"".equals(modelDoc.getDocSecretLvl())) {
					hql.append(" and d.ttransDoc.docSecretLvl = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocSecretLvl()) + "'");
				}
				// 紧急程度
				if (modelDoc.getDocEmergency() != null
						&& !"".equals(modelDoc.getDocEmergency())) {
					hql.append(" and d.ttransDoc.docEmergency = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocEmergency()) + "'");
				}
				// 主送单位
				if(modelDoc.getDocSubmittoDepart()!=null&&!"".equals(modelDoc.getDocSubmittoDepart())){
					hql.append(" and d.ttransDoc.docSubmittoDepart = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocSubmittoDepart()) + "'");
				}
			}
			// 是否归档
			if (TdocSend.HAS_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is not null ");
			} else if (TdocSend.HAS_NO_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is null ");
			}
			// 在某个时间段内d.tdocSends.docRecvTime,
			if (sendStartDate != null && !"".equals(sendStartDate)) {
				te.setTime(sendStartDate);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime >=? ");

			}
			if (sendEndDate != null && !"".equals(sendEndDate)) {
				te.setTime(sendEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime <= ? ");
			}
			if (recvStartDate != null && !"".equals(recvStartDate)) {
				te.setTime(recvStartDate);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime >=? ");

			}
			if (recvEndDate != null && !"".equals(recvEndDate)) {
				te.setTime(recvEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime <= ? ");
			}
			
			hql.append(" and d.docId in (select t.docId from TtransDoc t where t.rest3='" + deptCode +"')");
			//hql.append(" and d.docId='8a928a703d0ff750013d1444a79400a6'");
			
			if (list.isEmpty()) {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString());
			} else {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString(),
						list.toArray());

			}
			return page1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取公文列表" });
		}
	}
	
	@Transactional(readOnly = true)
	public Page getTtransDocs(Page page, TtransDoc modelDoc, String recvState,
			Date sendStartDate, Date sendEndDate, Date recvStartDate,
			Date recvEndDate) throws SystemException, ServiceException {
		return getTtransDocs(page, modelDoc, recvState, sendStartDate,
				sendEndDate, recvStartDate, recvEndDate, null);
	}

	/**
	 * author:lanlc description:获取公文列表 modifyer:
	 * 
	 * @param page
	 * @param userLonginName
	 * @param searchType
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getTtransDocs(Page page, TtransDoc modelDoc, String recvState,
			Date sendStartDate, Date sendEndDate, Date recvStartDate,
			Date recvEndDate, String finingState) throws SystemException,
			ServiceException {
		try {
			Page<TdocSend> page1;
			ArrayList list = new ArrayList();
			Calendar te = Calendar.getInstance();

			String deptCode = user.getUserDepartmentByUserId(getCurrentUser().getUserId()).getOrgSyscode();
			String isSuperAdmin = getCurrentUser().getUserIsSupManager();
			StringBuffer hql = new StringBuffer(
					"select d.ttransDoc.docCode, d.ttransDoc.docTitle,d.ttransDoc.docIssueDepartSigned,d.ttransDoc.docSendTime,"
							+ "d.docRecvTime,d.docRecvRemark ,d.docFilingTime,d.operateIp ,d.ttransDoc.docSecretLvl ,d.ttransDoc.docEmergency,"
							+ "d.ttransDoc.docKeywords, d.senddocId, d.liyous,d.qita,d.ttransDoc.docSubmittoDepart,d.swbh from TdocSend d where (d.isdelete is null or d.isdelete <> '1') and d.recvState= "
							+ recvState 
							);//+ " and ('admin'= '"+ getCurrentUser().getUserLoginname() + "' or  '" + deptCode + "' like d.deptCode||'%')");
			/***
			 * 修改， 获取南昌市办公厅，获取发给001的公文
			 */
			User user = userService.getCurrentUser();
			String usercod = user.getSupOrgCode();
			if(!"1".equals(isSuperAdmin)){//如果不是超级管理员
				//hql.append(" and ('").append(deptCode).append("' like d.deptCode||'%')");
				if(usercod !=null&&usercod.trim().equals("001"))
				{
					hql.append(" and (d.deptCode= '"
							 + deptCode	+"' or d.deptCode='001' or d.deptCode='001999000')");
				}else
				{
					hql.append(" and d.deptCode= '"
							 + deptCode	+"'");
				}
				
			}
			
			if (modelDoc != null) {
				// 单位
				if (modelDoc.getDocIssueDepartSigned() != null
						&& !"".equals(modelDoc.getDocIssueDepartSigned())) {
					hql.append(" and d.ttransDoc.docIssueDepartSigned like '%"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocIssueDepartSigned()).trim() + "%'");
				}
				// 名
				if (modelDoc.getDocTitle() != null
						&& !"".equals(modelDoc.getDocTitle())) {
					int t = modelDoc.getDocTitle().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocTitle().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocTitle()).trim() + "%'");
					}
				}
				// 公文文号
				if (modelDoc.getDocCode() != null
						&& !"".equals(modelDoc.getDocCode())) {
					int t = modelDoc.getDocCode().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocCode().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docCode like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docCode like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocCode()).trim() + "%'");
					}
				}
				// 秘密等级
				if (modelDoc.getDocSecretLvl() != null
						&& !"".equals(modelDoc.getDocSecretLvl())) {
					hql.append(" and d.ttransDoc.docSecretLvl = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocSecretLvl()) + "'");
				}
				// 紧急程度
				if (modelDoc.getDocEmergency() != null
						&& !"".equals(modelDoc.getDocEmergency())) {
					hql.append(" and d.ttransDoc.docEmergency = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocEmergency()) + "'");
				}
				// 主送单位
				if(modelDoc.getDocSubmittoDepart()!=null&&!"".equals(modelDoc.getDocSubmittoDepart())){
					hql.append(" and d.ttransDoc.docSubmittoDepart like '%"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocSubmittoDepart().trim()) + "%'");
				}
			}
			// 是否归档
			if (TdocSend.HAS_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is not null ");
			} else if (TdocSend.HAS_NO_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is null ");
			}
			// 在某个时间段内d.tdocSends.docRecvTime,
			if (sendStartDate != null && !"".equals(sendStartDate)) {
				te.setTime(sendStartDate);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime >=? ");

			}
			if (sendEndDate != null && !"".equals(sendEndDate)) {
				te.setTime(sendEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime <= ? ");
			}
			if (recvStartDate != null && !"".equals(recvStartDate)) {
				te.setTime(recvStartDate);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime >=? ");

			}
			if (recvEndDate != null && !"".equals(recvEndDate)) {
				te.setTime(recvEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime <= ? ");
			}
			if (list.isEmpty()) {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString());
			} else {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString(),
						list.toArray());

			}
			return page1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取公文列表" });
		}
	}
	/**
	 * author:lanlc description:获取公文列表 modifyer:
	 * 用于公文传输转来问草稿(未转入列表)
	 * @param page
	 * @param userLonginName
	 * @param searchType
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getTtransDocsTurnDraft(Page page, TtransDoc modelDoc, String recvState,
			Date sendStartDate, Date sendEndDate, Date recvStartDate,
			Date recvEndDate, String finingState) throws SystemException,
			ServiceException {
		try {
			Page<TdocSend> page1;
			ArrayList list = new ArrayList();
			Calendar te = Calendar.getInstance();

			String deptCode = user.getUserDepartmentByUserId(getCurrentUser().getUserId()).getOrgSyscode();
//			String deptCode = getCurrentUser().getSupOrgCode();
			String isSuperAdmin = getCurrentUser().getUserIsSupManager();
			StringBuffer hql = new StringBuffer(
					"select d.ttransDoc.docCode, d.ttransDoc.docTitle,d.ttransDoc.docIssueDepartSigned,d.ttransDoc.docSendTime,"
							+ "d.docRecvTime,d.docRecvRemark ,d.docFilingTime,d.operateIp ,d.ttransDoc.docSecretLvl ,d.ttransDoc.docEmergency,"
							+ "d.ttransDoc.docKeywords, d.senddocId ,d.docTurnDraft from TdocSend d where (d.isdelete is null or d.isdelete <> '1') and d.recvState= "
							+ recvState +"and d.docTurnDraft is null " 
							);//+ " and ('admin'= '"+ getCurrentUser().getUserLoginname() + "' or  '" + deptCode + "' like d.deptCode||'%')");
			
			/***
			 * 修改， 获取南昌市办公厅，获取发给001的公文
			 */
			User user = userService.getCurrentUser();
			String usercod = user.getSupOrgCode();
			if(!"1".equals(isSuperAdmin)){//如果不是超级管理员
				//hql.append(" and ('").append(deptCode).append("' like d.deptCode||'%')");
				if(usercod !=null&&usercod.trim().equals("001"))
				{
					hql.append(" and (d.deptCode= '"
							 + deptCode	+"' or d.deptCode='001' or d.deptCode='001999000')");
				}else
				{
					hql.append(" and d.deptCode= '"
							 + deptCode	+"'");
				}
				
			}

			if (modelDoc != null) {
				// 单位
				if (modelDoc.getDocIssueDepartSigned() != null
						&& !"".equals(modelDoc.getDocIssueDepartSigned())) {
					hql.append(" and d.ttransDoc.docIssueDepartSigned like '%"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocIssueDepartSigned()).trim() + "%'");
				}
				// 名
				if (modelDoc.getDocTitle() != null
						&& !"".equals(modelDoc.getDocTitle())) {
					int t = modelDoc.getDocTitle().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocTitle().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocTitle()).trim() + "%'");
					}
				}
				// 公文文号
				if (modelDoc.getDocCode() != null
						&& !"".equals(modelDoc.getDocCode())) {
					int t = modelDoc.getDocCode().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocCode().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docCode like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docCode like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocCode()).trim() + "%'");
					}
				}
				// 秘密等级
				if (modelDoc.getDocSecretLvl() != null
						&& !"".equals(modelDoc.getDocSecretLvl())) {
					hql.append(" and d.ttransDoc.docSecretLvl = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocSecretLvl()) + "'");
				}
				// 紧急程度
				if (modelDoc.getDocEmergency() != null
						&& !"".equals(modelDoc.getDocEmergency())) {
					hql.append(" and d.ttransDoc.docEmergency = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocEmergency()) + "'");
				}
			}
			// 是否归档
			if (TdocSend.HAS_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is not null ");
			} else if (TdocSend.HAS_NO_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is null ");
			}
			// 在某个时间段内d.tdocSends.docRecvTime,
			if (sendStartDate != null && !"".equals(sendStartDate)) {
				te.setTime(sendStartDate);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime >=? ");

			}
			if (sendEndDate != null && !"".equals(sendEndDate)) {
				te.setTime(sendEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime <= ? ");
			}
			if (recvStartDate != null && !"".equals(recvStartDate)) {
				te.setTime(recvStartDate);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime >=? ");

			}
			if (recvEndDate != null && !"".equals(recvEndDate)) {
				te.setTime(recvEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime <= ? ");
			}
			if (list.isEmpty()) {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString());
			} else {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString(),
						list.toArray());

			}
			return page1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取公文列表" });
		}
	}
	
	/**
	 * author:lanlc description:获取公文列表 modifyer:
	 * 用于公文传输转来问草稿(已转入列表)
	 * @param page
	 * @param userLonginName
	 * @param searchType
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getTtransDocsTurnDrafts(Page page, TtransDoc modelDoc, String recvState,
			Date sendStartDate, Date sendEndDate, Date recvStartDate,
			Date recvEndDate, String finingState) throws SystemException,
			ServiceException {
		try {
			Page<TdocSend> page1;
			ArrayList list = new ArrayList();
			Calendar te = Calendar.getInstance();

			String deptCode = user.getUserDepartmentByUserId(getCurrentUser().getUserId()).getOrgSyscode();
			String isSuperAdmin = getCurrentUser().getUserIsSupManager();
			StringBuffer hql = new StringBuffer(
					"select d.ttransDoc.docCode, d.ttransDoc.docTitle,d.ttransDoc.docIssueDepartSigned,d.ttransDoc.docSendTime,"
							+ "d.docRecvTime,d.docRecvRemark ,d.docFilingTime,d.operateIp ,d.ttransDoc.docSecretLvl ,d.ttransDoc.docEmergency,"
							+ "d.ttransDoc.docKeywords, d.senddocId ,d.docTurnDraft from TdocSend d where (d.isdelete is null or d.isdelete <> '1') and d.recvState= "
							+ recvState +"and d.docTurnDraft is not null " 
							);//+ " and ('admin'= '"+ getCurrentUser().getUserLoginname() + "' or  '" + deptCode + "' like d.deptCode||'%')");
			
//			if(!"1".equals(isSuperAdmin)){//如果不是超级管理员
//				//hql.append(" and ('").append(deptCode).append("' like d.deptCode||'%')");
//				hql.append(" and d.deptCode= '"
//					 + deptCode	+"'");
//			}

			if (modelDoc != null) {
				// 单位
				if (modelDoc.getDocIssueDepartSigned() != null
						&& !"".equals(modelDoc.getDocIssueDepartSigned())) {
					hql.append(" and d.ttransDoc.docIssueDepartSigned like '%"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocIssueDepartSigned()).trim() + "%'");
				}
				// 名
				if (modelDoc.getDocTitle() != null
						&& !"".equals(modelDoc.getDocTitle())) {
					int t = modelDoc.getDocTitle().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocTitle().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docTitle like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocTitle()).trim() + "%'");
					}
				}
				// 公文文号
				if (modelDoc.getDocCode() != null
						&& !"".equals(modelDoc.getDocCode())) {
					int t = modelDoc.getDocCode().indexOf("%");
					if(t!=-1){
						String temp =modelDoc.getDocCode().replaceAll("%","/%");;
						hql.append(" and d.ttransDoc.docCode like '%"
								+ temp + "%'" +" escape '/'");
					}else{
						hql.append(" and d.ttransDoc.docCode like '%"
								+ FiltrateContent.getNewContent(modelDoc
										.getDocCode()).trim() + "%'");
					}
				}
				// 秘密等级
				if (modelDoc.getDocSecretLvl() != null
						&& !"".equals(modelDoc.getDocSecretLvl())) {
					hql.append(" and d.ttransDoc.docSecretLvl = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocSecretLvl()) + "'");
				}
				// 紧急程度
				if (modelDoc.getDocEmergency() != null
						&& !"".equals(modelDoc.getDocEmergency())) {
					hql.append(" and d.ttransDoc.docEmergency = '"
							+ FiltrateContent.getNewContent(modelDoc
									.getDocEmergency()) + "'");
				}
			}
			// 是否归档
			if (TdocSend.HAS_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is not null ");
			} else if (TdocSend.HAS_NO_FILIONG.equals(finingState)) {
				hql.append(" and d.docFilingTime is null ");
			}
			// 在某个时间段内d.tdocSends.docRecvTime,
			if (sendStartDate != null && !"".equals(sendStartDate)) {
				te.setTime(sendStartDate);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime >=? ");

			}
			if (sendEndDate != null && !"".equals(sendEndDate)) {
				te.setTime(sendEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.ttransDoc.docSendTime <= ? ");
			}
			if (recvStartDate != null && !"".equals(recvStartDate)) {
				te.setTime(recvStartDate);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime >=? ");

			}
			if (recvEndDate != null && !"".equals(recvEndDate)) {
				te.setTime(recvEndDate);
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				list.add(te.getTime());
				hql.append(" and d.docRecvTime <= ? ");
			}
			if (list.isEmpty()) {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString());
			} else {
				page1 = sendsDao.find(page, hql.append(
						" order by d.ttransDoc.docSendTime desc").toString(),
						list.toArray());

			}
			return page1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取公文列表" });
		}
	}
	
	/**
	 * author:dengzc description:更新收文信息 modifyer: description:
	 * 
	 * @param id
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateRecvDoc(String id) throws SystemException,
			ServiceException {
		try {
			String sql = "update T_OARECVDOC set RECV_STATE="
					+ WorkFlowTypeConst.RECVDOCDOCUMENT
					+ " where OARECVDOCID='" + id + "'";
			sendsDao.executeJdbcUpdate(sql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "更新收文状态" });
		}
	}

	//
	// /**
	// * author:dengzc description:获取收文标题 modifyer: description:
	// *
	// * @param id
	// * @return
	// * @throws SystemException
	// * @throws ServiceException
	// * @throws SQLException
	// */
	// public String getRecvInfo(String id) throws SystemException,
	// ServiceException, SQLException {
	// try {
	// String sql = "select DOC_TITLE from T_OARECVDOC where OARECVDOCID='"
	// + id + "'";
	// ResultSet rs = sendsDao.executeJdbcQuery(sql);
	// // String[] res = new String[rs.getMetaData().getColumnCount()];
	// if (rs.next()) {
	// System.out.println(rs.getString("DOC_TITLE"));
	// return rs.getString("DOC_TITLE");
	// } else {
	// return "";
	// }
	// } catch (ServiceException e) {
	// throw new ServiceException(MessagesConst.save_error,
	// new Object[] { "更新收文状态" });
	// }
	// }

	/**
	 * 获取当前会话Session
	 * 
	 * @author:
	 * @date:
	 * @return
	 */
	public HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * 根据来文ID获取附件
	 * 
	 * @author:
	 * @date:2009-7-14 下午03:15:23
	 * @param senddocId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<Map<String, Object>> getAttachByDocId(String docId)
			throws ServiceException, SystemException {
		ByteArrayOutputStream bos = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			List<Map<String, Object>> attachLst = new ArrayList<Map<String, Object>>();
			String sql = "select ATTACH_NAME,ATTACH_CONTENT from T_DOCATTACH t where t.DOC_ID='"
					+ docId + "'";
			rs = sendsDao.executeJdbcQuery(sql);
			while (rs.next()) {
				Map<String, Object> attachMap = new HashMap<String, Object>();
				String attachName = rs.getString("ATTACH_NAME");
				attachMap.put("ATTACH_NAME", attachName);
				is = rs.getBinaryStream("ATTACH_CONTENT");
				bos = new ByteArrayOutputStream();
				int input = 0;
				byte[] buf = new byte[8192];
				while ((input = is.read(buf)) != -1) {
					bos.write(buf, 0, input);
				}
				byte[] attachContent = bos.toByteArray();
				attachMap.put("ATTACH_CONTENT", attachContent);
				attachLst.add(attachMap);
			}
			return attachLst;
		} catch (Exception e) {
			throw new SystemException("查询公文附件出错了！");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (is != null) {
					is.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * author:luosy description:获取接收事务对象 modifyer: description:
	 * 
	 * @param 
	 *           事务id
	 * @return
	 */
	public TdocSend getSendDocById(String id) throws SystemException,
			ServiceException {
		try {
			return sendsDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取接收对象" });
		}
	}
	
	/**
	 * 删除公文记录
	 * 
	 * @author:
	 * @date:2010-4-9 下午02:47:13
	 * @param id
	 */
	public void delete(String id) {
		if (id != null && !"".equals(id)) {
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				TdocSend doc = (TdocSend) sendsDao.getSession().get(
						TdocSend.class, ids[i]);
				sendsDao.delete(doc);
			}
		}
	}

	/**
	 * 逻辑删除公文记录
	 * 
	 * @author:
	 * @date:2010-4-9 下午02:47:13
	 * @param id
	 */
	public void removeDoc(String id) {
		List<TdocSend> sends = new ArrayList<TdocSend>();
		if (id != null && !"".equals(id)) {
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				TdocSend doc = (TdocSend) sendsDao.getSession().get(
						TdocSend.class, ids[i]);
				doc.setIsdelete("1");
				sends.add(doc);
			}
			sendsDao.save(sends);
		}
	}

	/**
	 * author:luosy description:保存 modifyer: description:
	 * 
	 * @param calendar
	 * @param input
	 *            由"page"页面(单独的编辑页面)或"view"视图中操作
	 * @return
	 */
	@Transactional
	public String saveSends(TdocSend model,OALogInfo...infos)
			throws SystemException, ServiceException {
		try { 
			sendsDao.save(model);
			return model.getSenddocId();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "接收成功" });
		}
	}
	
	public TtransDoc getTtransDocById(String sendId) throws Exception{
		try {
			TtransDoc ttransDoc=ttranDocDao.get(sendId);
			return ttransDoc;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "根据发文ID，获取公文" });
		}
	}
	public void updatTtransDoc(TtransDoc ttransDoc){
		try {
			ttranDocDao.update(ttransDoc);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 公文传输转来文草稿（具体操作）
	 * author  taoji
	 * @return
	 * @throws SQLException 
	 * @throws HibernateException 
	 * @throws IOException 
	 * @date 2012-12-30 下午02:19:11
	 */
	public String docTurnDraftHandle(String id) throws HibernateException, SQLException, IOException{
		String mark = "true";
		
		/*** 更加分发id查找 公文传输（TtransDoc） ***/
		String hql = "select d.ttransDoc from TdocSend d where (d.isdelete is null or d.isdelete <> '1')" +
				"and d.senddocId='"+id+"' and d.recvState= '1'";
		List<TtransDoc> ts = sendsDao.find(hql);
		TtransDoc t = ts.get(0);
		
		TdocSend tdocSend = sendsDao.get(id);
		
		/***插入数据至来文草稿***/
		Recivedoc recivedoc = new Recivedoc();
		User user = userService.getCurrentUser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(new Date());
		String pageCount="";
		if(t.getDocHavePrintSum()!=null){
			pageCount = t.getDocHavePrintSum();
		}
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String uuid = (new UUIDGenerator().generate()).toString();
		String sql = "INSERT INTO T_OARECVDOC (OARECVDOCID,WORKFLOWSTATE,WORKFLOWAUTHOR,WORKFLOWTITLE,ISSUE_DEPART_SIGNED" +
				//",RECV_TIME,WORKFLOWNAME,CONTENT,CONTENT_NAME,DOC_NUMBER,PAGE_COUNT) " +
				//修改正文为PERSON_DEMO和PERSON_FILENAME
				",RECV_TIME,WORKFLOWNAME,PERSON_DEMO,PERSON_FILENAME,DOC_NUMBER,PAGE_COUNT,EMERGENCY,DOC_SUBMITTO_DEPART,DOC_ID,ZSORCS) " +
				"VALUES ( '"+uuid+"','0','"+user.getUserId()+"','"+t.getDocTitle()+"','"+t.getDocIssueDepartSigned()+"'" +
				",to_date('"+d+"','yyyy-MM-dd HH24:mi:ss'),'收文办件登记',?,'公文传输正文','"+t.getDocCode()+"','"+
				pageCount+"','"+t.getDocEmergency()+"','"+t.getDocSubmittoDepart()+"','"+id+"','"+tdocSend.getZsorcs()+"')";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		psmt.setBytes(1,t.getDocContent());
		rs = psmt.executeQuery();

		//公文附件list
		List<Tdocattach> tdocattach = new ArrayList<Tdocattach>();
		
		/*** 保存附件  ***/
		String rootPath = PathUtil.getRootPath();//得到工程根路径
		//查询出公文传输所有附件
		String l ="from TtransDocAttach t where 1=1 and t.ttransDoc.docId = '"+t.getDocId()+"'"; 
		List<TtransDocAttach> ttransDocAttach = docAttachDao.find(l);
		//附件复制转移
		if(ttransDocAttach!=null&&ttransDocAttach.size()>0){
			
			for(TtransDocAttach tda:ttransDocAttach){
				Tdocattach tdtt = new Tdocattach();
				tdtt.setAttachName(tda.getAttachFileName());
				//把公文传输附件保存到oa中的附件存放地址
				File fs = new File(rootPath+tda.getAttachFilePath());
				if(!fs.exists()){
					//  附件丢失
					//mark="1";
					
					//返回丢失的附件名；
					mark += tda.getAttachFileName()+";";
					break;
				 }
				String fujian = (new UUIDGenerator().generate()).toString();
				 String path = rootPath+"WEB-INF"+File.separatorChar+"classes"+File.separatorChar+"attachments"+File.separatorChar;
				 File fr = new File(path+fujian);
				//判断文件是否存在,如果不存在则创建文件
				if(!fr.exists()){
					 fr.createNewFile();
				 }
				//拷贝一份
				FileUtils.copyFile(fs, fr);
				
				tdtt.setAttachPath(fujian);
				tdtt.setDocId(uuid);
				tdocattach.add(tdtt);
			}
		}
		/***公文传输 正文  暂时用附件形式转入***/
		/*if(t.getDocContent()!=null&&!"".equals(t.getDocContent())){  //判断正文是否存在
			String tdContId = (new UUIDGenerator().generate()).toString();
			Tdocattach tdCont = new Tdocattach();
			byte[] b = t.getDocContent();
			tdCont.setAttachName("公文传输正文.doc");
			tdCont.setDocId(uuid);
			//公文默认路径创建  文件并写入
			File file = new File(rootPath+"WEB-INF"+File.separatorChar+"classes"+File.separatorChar+"attachments"+File.separatorChar+tdContId);
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			out.write(b);
			tdCont.setAttachPath(tdContId);
			tdocattach.add(tdCont);
		}*/
		
		//保存所有附件信息至数据库
		tdocattachDao.save(tdocattach);
		
		/***转入来文草稿后设置标识***/
//		TdocSend tdocSend = sendsDao.get(id);
		tdocSend.setDocTurnDraft("1");	//
		sendsDao.save(tdocSend);
		
		
		if(!"true".equals(mark)){
			mark = "false文件“"+t.getDocTitle()+"”丢失附件:"+mark;
		}else{
			//成功转入后 返回该记录id
			mark = mark + ";" + uuid;
		}
		return mark;
	}
	/**
	 * 公文传输
	 * 更改状态值  使其不能再提交
	 * author  taoji
	 * @param id
	 * @throws SQLException 
	 * @throws HibernateException 
	 * @date 2013-1-25 上午09:30:39
	 */
	public void setT_OARECVDOCFlag(String id) throws HibernateException, SQLException{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "UPDATE T_OARECVDOC set  DOC_FLAG = '2' WHERE OARECVDOCID = '"+id+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
	}
	/**
	 * 公文传输
	 * 更改状态值  使其不能再提交
	 * author  taoji
	 * @param id
	 * @throws SQLException 
	 * @throws HibernateException 
	 * @date 2013-1-25 上午09:30:39
	 */
	public void deltetT_OARECVDOCFlag(String id) throws HibernateException, SQLException{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "DELETE T_OARECVDOC WHERE OARECVDOCID = '"+id+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
	}
	
	/**
	 * 公文传输
	 * 更改状态值  使其不能再提交
	 * author  taoji
	 * @param id
	 * @throws SQLException 
	 * @throws HibernateException 
	 * @date 2013-1-25 上午09:30:39
	 */
	public String getDocSendIdByRecId(String id) throws HibernateException, SQLException{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT t.DOC_ID from T_OARECVDOC t where OARECVDOCID = '"+id+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		String docSendId = "";
		while(rs.next()){
			docSendId = rs.getString("DOC_ID");
		}
		return docSendId;
	}
	/**
	 * tj
	 * 查找收文编号
	 */
	public String getRecNum(String id) throws HibernateException, SQLException{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT t.RECV_NUM from T_OARECVDOC t where OARECVDOCID = '"+id+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		String RECV_NUM = "";
		while(rs.next()){
			RECV_NUM = rs.getString("RECV_NUM");
		}
		return RECV_NUM;
	}
	/**
	 * 
	 * author  taoji
	 * @param id
	 * @return
	 * @throws HibernateException
	 * @throws SQLException
	 * @date 2013-3-15 下午02:27:25
	 */
	public boolean getGwcs(String id) throws HibernateException, SQLException{
		String t = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT DOC_ID FROM T_OARECVDOC WHERE OARECVDOCID = '"+id+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		while(rs.next()){
			t = rs.getString("DOC_ID");
		}
		if(t!=null&&!"".equals(t)&&t.length()>4){
			return true;
		}
		return false;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}

}
