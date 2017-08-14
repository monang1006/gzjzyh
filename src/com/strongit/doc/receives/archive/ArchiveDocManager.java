package com.strongit.doc.receives.archive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TtransArchive;
import com.strongit.doc.bo.TtransArchiveAttach;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.receives.RecvDocManager;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ArchiveDocManager extends BaseManager {
	
	private GenericDAOHibernate<TtransArchive, java.lang.String> archiveDao;
	
	private GenericDAOHibernate<TtransArchiveAttach, java.lang.String>archiveAttachDao;
	
	
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		archiveDao = new GenericDAOHibernate<TtransArchive, java.lang.String>(sessionFactory,
				TtransArchive.class);
		archiveAttachDao = new GenericDAOHibernate<TtransArchiveAttach, java.lang.String>(sessionFactory,
				TtransArchiveAttach.class);
	}
	
	/** 用户管理Service接口*/
	private IUserService userservice;
	
	@Autowired RecvDocManager recvDocManager;
	
	/*
	 * 获取所有档案中心文件
	 */
	public Page<TtransArchive> getPageArchive(Page<TtransArchive> page,String treeValue)  throws SystemException,ServiceException
	{	
		try{
			//List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			User user=userservice.getCurrentUser();
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());
			String orgId=org.getOrgSyscode();
//			Organization org=new Organization();
//			String orgIds="";
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			
			String hql="";
			if(treeValue!=null&&!treeValue.equals("")){
				
				String [] sr=treeValue.split(",");
				String str="";
				if(sr!=null&&sr.length>0){
					str=" and t.docYear=? ";
					if(sr.length>1){
						str=str+" and t.docMonth='"+sr[1]+"'";
						treeValue=sr[0];
					}
				}
				if(user!=null&& user.getUserLoginname().equals("admin")){										//超级用户
					hql="  from TtransArchive t where 1=1 and t.rest4 is null "+str+" order by t.docSendTime desc ";
				}else {					
					hql="  from TtransArchive t where 1=1 and t.rest4 is null and t.deptCode in ('"+orgId+"') "+str+" order by t.docSendTime desc ";
				}
				page=archiveDao.find(page, hql, treeValue);
			}else {
				if(user!=null&& user.getUserLoginname().equals("admin")){										//超级用户
					hql="  from TtransArchive t where 1=1 and t.rest4 is null order by t.docSendTime desc ";					
				}else {
					hql="  from TtransArchive t where 1=1 and t.rest4 is null and t.deptCode in ('"+orgId+"') order by t.docSendTime desc ";
				}
				page= archiveDao.find(page, hql);
			}
			return page;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"分页档案中心列表"}); 
		}
	}	
	
	/*
	 * 根据条件查询档案中心文件列表
	 */
	public  Page<TtransArchive> search(Page<TtransArchive> page,TtransArchive model,Date startDate,Date endDate) throws SystemException,ServiceException{
		try {
			List<Object> list = new ArrayList<Object>(0);
//			ArrayList list = new ArrayList();
			Calendar ca = Calendar.getInstance();
			User user=userservice.getCurrentUser();
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());
			String orgId=org.getOrgSyscode();
			StringBuffer hql = new StringBuffer("from TtransArchive t where 1=1 and t.rest4 is null ");
			if(user!=null&&!user.getUserLoginname().equals("admin")){										//不是超级用户
				hql.append("and t.deptCode in ('"+orgId+"') ");			
			}
			if(model.getDocTitle()!=null&&!"".equals(model.getDocTitle())){
				int t = model.getDocTitle().indexOf("%");
				if(t!=-1){
					String temp =model.getDocTitle().replaceAll("%","/%");;
					hql.append(" and t.docTitle like '%" + temp + "%'" +" escape '/'");
				}else{
					hql.append(" and t.docTitle like '%"+model.getDocTitle().trim()+"%' ");
				}
			}
			if(model.getDocCode()!=null&&!"".equals(model.getDocCode())){
				int t = model.getDocCode().indexOf("%");
				if(t!=-1){
					String temp =model.getDocCode().replaceAll("%","/%");;
					hql.append(" and t.docCode like '%" + temp + "%'" +" escape '/'");
				}else{
					hql.append(" and t.docCode like '%"+model.getDocCode().trim()+"%' ");
				}
			}
			if(model.getDocEmergency()!=null&&!"".equals(model.getDocEmergency())){
				hql.append(" and t.docEmergency =? ");
				list.add(model.getDocEmergency());
			}
			if(model.getDocSecretLvl()!=null&&!"".equals(model.getDocSecretLvl())){
				hql.append(" and t.docSecretLvl =? ");
				list.add(model.getDocSecretLvl());
			}
			if(model.getDocIssueDepartSigned()!=null&&!"".equals(model.getDocIssueDepartSigned())){
				hql.append(" and t.docIssueDepartSigned like '%"+model.getDocIssueDepartSigned().trim()+"%' ");
//				list.add(model.getDocIssueDepartSigned());
			}
			if(startDate != null && !"".equals(startDate)){//按发文时间搜索
				hql.append(" and t.docSendTime >=? ");
				list.add(startDate);
			}
			if(endDate != null && !"".equals(endDate)){//按录入时间搜索
				hql.append(" and t.docSendTime <= ?");
				ca.setTime(endDate);
				ca.set(Calendar.HOUR, 23);
				ca.set(Calendar.MINUTE, 59);
				ca.set(Calendar.SECOND, 59);
				list.add(ca.getTime());
			}
			if (list.isEmpty()) {
				page= archiveDao.find(page, hql.append(" order by t.docSendTime desc").toString());
			} else {
				page= archiveDao.find(page, hql.append(" order by t.docSendTime desc").toString(),list.toArray());
			}
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据条件查询档案中心文件列表" });
		}
	}
	
	
	/*
	 * 根据条件查询档案中心文件列表带上年月
	 */
	public  Page<TtransArchive> searchByYear(Page<TtransArchive> page,TtransArchive model,Date startDate,Date endDate,String treeValue) throws SystemException,ServiceException{
		try {
			List<Object> list = new ArrayList<Object>(0);
//			ArrayList list = new ArrayList();
			Calendar ca = Calendar.getInstance();
			User user=userservice.getCurrentUser();
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());
			String orgId=org.getOrgSyscode();
			StringBuffer hql = new StringBuffer("from TtransArchive t where 1=1 and t.rest4 is null ");
			if(user!=null&&!user.getUserLoginname().equals("admin")){										//不是超级用户
				hql.append("and t.deptCode in ('"+orgId+"') ");			
			}
			if(model.getDocTitle()!=null&&!"".equals(model.getDocTitle())){
				int t = model.getDocTitle().indexOf("%");
				if(t!=-1){
					String temp =model.getDocTitle().replaceAll("%","/%");;
					hql.append(" and t.docTitle like '%" + temp + "%'" +" escape '/'");
				}else{
					hql.append(" and t.docTitle like '%"+model.getDocTitle().trim()+"%' ");
				}
			}
			if(model.getDocCode()!=null&&!"".equals(model.getDocCode())){
				int t = model.getDocCode().indexOf("%");
				if(t!=-1){
					String temp =model.getDocCode().replaceAll("%","/%");;
					hql.append(" and t.docCode like '%" + temp + "%'" +" escape '/'");
				}else{
					hql.append(" and t.docCode like '%"+model.getDocCode().trim()+"%' ");
				}
			}
			if(model.getDocEmergency()!=null&&!"".equals(model.getDocEmergency())){
				hql.append(" and t.docEmergency =? ");
				list.add(model.getDocEmergency());
			}
			if(model.getDocSecretLvl()!=null&&!"".equals(model.getDocSecretLvl())){
				hql.append(" and t.docSecretLvl =? ");
				list.add(model.getDocSecretLvl());
			}
			if(model.getDocIssueDepartSigned()!=null&&!"".equals(model.getDocIssueDepartSigned())){
				hql.append(" and t.docIssueDepartSigned like '%"+model.getDocIssueDepartSigned().trim()+"%' ");
//				list.add(model.getDocIssueDepartSigned());
			}
			if(startDate != null && !"".equals(startDate)){//按发文时间搜索
				hql.append(" and t.docSendTime >=? ");
				list.add(startDate);
			}
			if(endDate != null && !"".equals(endDate)){//按录入时间搜索
				hql.append(" and t.docSendTime <= ?");
				ca.setTime(endDate);
				ca.set(Calendar.HOUR, 23);
				ca.set(Calendar.MINUTE, 59);
				ca.set(Calendar.SECOND, 59);
				list.add(ca.getTime());
			}
			if(treeValue!=null&&!"".equals(treeValue)){
				String [] sr=treeValue.split(",");
				hql.append(" and t.docYear=? ");
				if(sr.length>1){
					hql.append(" and t.docMonth='"+sr[1]+"'");
				}
				list.add(sr[0]);
			}
			if (list.isEmpty()) {
				page= archiveDao.find(page, hql.append(" order by t.docSendTime desc").toString());
			} else {
				page= archiveDao.find(page, hql.append(" order by t.docSendTime desc").toString(),list.toArray());
			}
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据条件查询档案中心文件列表" });
		}
	}
	
	/**
	 * 根据档案中心文件ID，查找文件
	 * @author zhengzb
	 * @desc 
	 * 2010-8-12 上午08:54:51 
	 * @param archiveId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TtransArchive getArchiveFile(String archiveId) throws SystemException,ServiceException{
		try {
			TtransArchive archiveFile=archiveDao.get(archiveId);
			return archiveFile;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"档案中心文件Id查找文件"});
		}
	}
	
	/**
	 * 根据年份分组查询文件
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * （注*）：发文归档的时候，设置备用字段“docYear”为：年度，备用字段“docMonth”为：月份
	 */
	public List<String> getTempfileYear()
	throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			String hql ="";
			if(user!=null&&user.getUserLoginname().equals("admin")){
			    hql = "select t.docYear from TtransArchive t where  1=1";
				hql = hql + " group by t.docYear";
			}else {
				
				List orgList=userservice.getOrgAndChildOrgByCurrentUser();
				Organization org=new Organization();
				String orgIds="";
				for(int i=0;i<orgList.size();i++){
					org=(Organization)orgList.get(i);
					orgIds+=",'"+org.getOrgSyscode()+"'";
				}
				orgIds=orgIds.substring(1);
				hql = "select t.docYear from TtransArchive t where  t.deptCode in ("+orgIds+")  ";
				hql = hql + " group by t.docYear";
			}
			Query query = archiveDao.createQuery(hql);
			List<String> list = query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据年份统计" });
		}
	}

	
	
	/**
	 * 根据月份分组查询文件
	 * 
	 * @param year
	 * @return
	 * （注*）：发文归档的时候，设置备用字段“docYear”为：年度，备用字段“docMonth”为：月份
	 */
	public List<String> getTempfileMonth(String year) throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			String hql ="";
			if(user!=null&&user.getUserLoginname().equals("admin")){
				hql = "select t.docMonth from TtransArchive t where t.docYear='"+year+"' ";
				hql = hql + "  group by t.docMonth";
			}else {
				List orgList=userservice.getOrgAndChildOrgByCurrentUser();
				Organization org=new Organization();
				String orgIds="";
				for(int i=0;i<orgList.size();i++){
					org=(Organization)orgList.get(i);
					orgIds+=",'"+org.getOrgSyscode()+"'";
				}
				orgIds=orgIds.substring(1);
				hql = "select t.docMonth from TtransArchive t where t.docYear='"+year+"' and  t.deptCode in ("+orgIds+")  ";
				hql = hql + " group by t.docMonth";				
			}
			Query query = archiveDao.createQuery(hql);
			List<String> list = query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据月份统计" });
		}
	}
	/**
	 * 查看档案文号，是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-8-23 上午10:42:21 
	 * @param fileNo
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getIsFileNo(String fileNo) throws SystemException,ServiceException{
		try {
			
			String hql="select count(*) from TtransArchive t where t.rest4 is null and t.fileNo =? ";
			return Integer.parseInt(archiveDao.findUnique(hql, fileNo).toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据档案文号获取列表" });
		}
	}
	
	public int getSendIsFileNo(String fileNo) throws SystemException,ServiceException{
		try {
			
			String hql="select count(*) from TtransArchive t where t.rest4 = '0' and t.fileNo =? ";
			return Integer.parseInt(archiveDao.findUnique(hql, fileNo).toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据档案文号获取列表" });
		}
	}
	
	/**
	 * 根据发文文号查询,该档案是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-8-23 上午10:50:51 
	 * @param docCode
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TtransArchive getIsDocCode(String docCode) throws SystemException,ServiceException{
		try {
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			Organization org=new Organization();
			String orgIds="";
			for(int i=0;i<orgList.size();i++){
				org=(Organization)orgList.get(i);
				orgIds+=",'"+org.getOrgSyscode()+"'";
			}
			orgIds=orgIds.substring(1);			
			String hql=" from TtransArchive t where t.docCode like '"+docCode+"'and t.rest4 is null and  t.deptCode in ("+orgIds+")  ";
			Query query = archiveDao.createQuery(hql);
			List<TtransArchive> list = query.list();
			if(list.size()>0){
				return list.get(0);
			}else {
				return null;
			}
//			return Integer.parseInt(archiveDao.findUnique(hql, docCode).toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据发文文号查询" });
		}
	}
	
	public TtransArchive getIsDocSendCode(String docCode) throws SystemException,ServiceException{
		try {
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			Organization org=new Organization();
			String orgIds="";
			for(int i=0;i<orgList.size();i++){
				org=(Organization)orgList.get(i);
				orgIds+=",'"+org.getOrgSyscode()+"'";
			}
			orgIds=orgIds.substring(1);			
			String hql=" from TtransArchive t where t.docCode like '"+docCode+"' and t.rest4 = '0' and  t.deptCode in ("+orgIds+")  ";
			Query query = archiveDao.createQuery(hql);
			List<TtransArchive> list = query.list();
			if(list.size()>0){
				return list.get(0);
			}else {
				return null;
			}
//			return Integer.parseInt(archiveDao.findUnique(hql, docCode).toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据发文文号查询" });
		}
	}
	
	/*
	 * 保存档案文件
	 */
	public String saveArchiveDoc(TtransArchive ttransArchive,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try {
			archiveDao.save(ttransArchive);
			String archiveId=ttransArchive.getArchiveId();
			return archiveId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "保存档案文件" });
		}
	}
	
	/*
	 *  重新归档，删除上次归档的档案文件
	 */
	public void deleteArchiveDoc(String archiveId) throws SystemException,ServiceException{
		try {
			TtransArchive ttransArchive=getArchiveFile(archiveId);
			if(ttransArchive!=null){																								//选择删除档案附件
				if(ttransArchive.getTtransArchiveAttaches()!=null&&ttransArchive.getTtransArchiveAttaches().size()>0){
					Set<TtransArchiveAttach> ttransArchiveAttachs=(Set<TtransArchiveAttach>)ttransArchive.getTtransArchiveAttaches();
					for(Iterator<TtransArchiveAttach> iterator=ttransArchiveAttachs.iterator();iterator.hasNext();){
						TtransArchiveAttach ttransArchiveAttach=iterator.next();
						if(ttransArchiveAttach!=null){
							deleteArchiveAttach(ttransArchiveAttach.getArchiveAttachId());
						}
					}
				}
			}
			archiveDao.delete(archiveId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { " 重新归档，删除上次归档的档案文件" });
		}
	}
	
	/*
	 * 重新归档，删除上次归档的档案文件附件
	 */
	public void deleteArchiveAttach(String archiveAttachId) throws SystemException,ServiceException{
		try {
			archiveAttachDao.delete(archiveAttachId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { " 重新归档，删除上次归档的档案文件附件" });
		}
	}
	/*
	 * 保存档案附件
	 */
	 public void saveAppend(TtransArchiveAttach tArchiveAttach,OALogInfo ... loginfos){
		   try{
			   archiveAttachDao.save(tArchiveAttach);
		   }catch(ServiceException e){
			   throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"保存档案附件"}); 
		   }
		   
	   }
	   
	public TtransArchiveAttach getArchiveAttachById(String archiveAttachId) throws SystemException,ServiceException{
		try {
			TtransArchiveAttach archiveAttach=archiveAttachDao.get(archiveAttachId);
			return archiveAttach;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"档案中心附件Id查找附件"});
		}
	}
	
	/**
	 * 更新打印信息
	 * @author zhengzb
	 * @desc 
	 * 2010-8-20 下午02:25:49 
	 * @return
	 */
	public void changePrintedNum(TdocSend docSend,OALogInfo ... loginfos) {
		try {
			recvDocManager.saveSends(docSend);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"保存公文打印份数信息"});
		}
	}
	
	
	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}
	

}
