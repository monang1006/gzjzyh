package com.strongit.doc.sends;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javassist.expr.NewArray;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.agencygroup.GroupDetManager;
import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TdocSendPrintPassword;
import com.strongit.doc.bo.TtransArchive;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.sends.util.DocType;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.uums.bo.TUumsBaseOrg;
@Service
@Transactional
@OALogger
public class DocSendManager {
	
	private GenericDAOHibernate<TdocSend, java.lang.String> docSendDao;
	
	private GenericDAOHibernate<TUumsBaseOrg, java.lang.String> OrgDao;
	
	private GenericDAOHibernate<TtransArchive, java.lang.String> archiveDao;
	
	private GenericDAOHibernate<TdocSendPrintPassword, java.lang.String> printPasswordDao;
	@Autowired private TransDocManager transDocManager;
	GenericDAOHibernate<TtransDoc, String> docDao ;	//DAO处理类
	
	@Autowired private GroupDetManager groupDetManager;
	
	@Autowired private IUserService userService;
	
	/**
	 * author:luosy
	 * description: 设置Dao
	 * modifyer:
	 * description:
	 * @param session
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		docSendDao = new GenericDAOHibernate<TdocSend, java.lang.String>(
				session, TdocSend.class);
		archiveDao = new GenericDAOHibernate<TtransArchive, java.lang.String>(session,
				TtransArchive.class);
		printPasswordDao = new GenericDAOHibernate<TdocSendPrintPassword, java.lang.String>(session,
				TdocSendPrintPassword.class);
		OrgDao=new GenericDAOHibernate<TUumsBaseOrg, java.lang.String>(session,
				TUumsBaseOrg.class);
	docDao = new GenericDAOHibernate<TtransDoc, java.lang.String>(session, TtransDoc.class);
	}
	
	/**
	 * @author:luosy
	 * @description:
	 * @date : 2010-8-13
	 * @modifyer:
	 * @description:  获取公文列表
	 * @param page				分页对象
	 * @param model				公文对象
	 * @param type				列表类型,从枚举中读取
	 * @param isDelete			是否删除状态
	 * @param startDate			开始日期
	 * @param endDate			结束日期
	 * @return					分页列表
	 * 		List<Object[]>{
	 * 			主键id,公文标题,发文文号,紧急程度,公文类别,录入时间
	 * }
	 */
	public Page getDocPage(Page page, TtransDoc doc, DocType type, Boolean isDelete, Date startDate, Date endDate){
		String isSupMan = userService.getCurrentUser().getUserIsSupManager();
		if("1".equals(isSupMan)){
			type.dataInDeptRange(false);
			type.dataInOrgRange(false);
		}

		return transDocManager.getPageList(page, doc, type, isDelete, startDate, endDate);
		
//		StringBuilder hql = new StringBuilder("select t.docId, t.docTitle,t.docCode,t.docEmergency,t.docClass,t.docEntryTime,t.docSecretLvl,t.docSealPeople,t.rest1,t.docState from TtransDoc t ");
//		User user = userService.getCurrentUser();
//		List<Object> params = new ArrayList<Object>(0);
//		hql.append(" where 1 = 1 ");
//		if(type == DocType.Draft){//查询草稿
//			hql.append(" and t.ddocEntryPeople = ? ");
//			params.add(user.getUserId());										//录入人ID
//		}else{
//			if(!"1".equals(user.getUserIsSupManager())){
//				String orgCode = userService.getSupOrgByUserIdByHa(user.getUserId()).getOrgSyscode();
//				hql.append(" and t.rest3 = ? ");
//				params.add(orgCode);	
//			}
//		}
//		if(type != null){													//公文状态
//			List<DocType> docTypeList = type.getDocTypeList();
//			if(docTypeList != null && !docTypeList.isEmpty()){
//				StringBuilder docTypeStr = new StringBuilder();
//				for(DocType docType : docTypeList){
//					docTypeStr.append("'").append(docType.getValue()).append("'").append(",");
//				}
//				String docTypeString = docTypeStr.substring(0, docTypeStr.length()-1);
//				hql.append(" and t.docState in("+docTypeString+")");
//				
//				docTypeList.clear();
//			}else{
//				if(type.getRule()){
//					hql.append(" and t.docState = ?");
//				}else{
//					hql.append(" and t.docState <> ?");
//				}
//				params.add(type.getValue());
//			}
//			
//		}
//		if(isDelete != null){
//			hql.append(" and t.isdelete = ?");								//是否是删除的公文
//			if(isDelete){
//				params.add(DocType.DELETE);									//删除状态
//			}else{
//				params.add(DocType.NOTDELETE);								//非删除状态
//			}
//		}
//		if(doc.getDocTitle() != null && !"".equals(doc.getDocTitle())){//根据标题搜索
//			doc.setDocTitle(doc.getDocTitle().trim());		//去除空格
//			hql.append(" and t.docTitle like ?");
//			params.add("%"+doc.getDocTitle()+"%");
//		}
//		if(doc.getDocCode() != null && !"".equals(doc.getDocCode())){//根据文号搜索
//			doc.setDocCode(doc.getDocCode().trim());
//			hql.append(" and t.docCode like ?");
//			params.add("%"+doc.getDocCode()+"%");
//		}
//		if(doc.getDocEmergency() != null && !"".equals(doc.getDocEmergency())){//根据密级搜索,精确查询
//			doc.setDocEmergency(doc.getDocEmergency().trim());
//			hql.append(" and t.docEmergency = ?");
//			params.add(doc.getDocEmergency());
//		}
//		if(doc.getDocClass() != null && !"".equals(doc.getDocClass())){//根据公文种类搜索
//			hql.append(" and t.docClass = ?");
//			params.add(doc.getDocClass());
//		}
//		if(startDate != null && !"".equals(startDate)){//按录入时间搜索
//			hql.append(" and t.docEntryTime >= ?");
//			params.add(startDate);
//		}
//		if(endDate != null && !"".equals(endDate)){//按录入时间搜索
//			hql.append(" and t.docEntryTime <= ?");
//			params.add(endDate);
//		}
//		if(doc.getDocSecretLvl() != null && !"".equals(doc.getDocSecretLvl())){//按密级搜索
//			doc.setDocSecretLvl(doc.getDocSecretLvl().trim());
//			hql.append(" and t.docSecretLvl = ?");
//			params.add(doc.getDocSecretLvl());
//		}
//		if(doc.getDocSealPeople() != null && !"".equals(doc.getDocSealPeople())){
//			if("0".equals(doc.getDocSealPeople())){//未签章
//				hql.append(" and t.docSealPeople is null");
//			}else if("1".equals(doc.getDocSealPeople())){
//				hql.append(" and t.docSealPeople is not null");
//			}
//		}
//		hql.append(" order by t.docEntryTime desc");
//
//		
//		return transDocManager.getPageList(page, hql.toString(), params);
	}
	
	public List<Object> getDocPageReport(TtransDoc doc, DocType type, Boolean isDelete, Date startDate, Date endDate){
//		String isSupMan = userService.getCurrentUser().getUserIsSupManager();
//		if("1".equals(isSupMan)){
//			type.dataInDeptRange(false);
//			type.dataInOrgRange(false);
//		}

		return transDocManager.getPageListReport(doc, type, isDelete, startDate, endDate);
		}
	public  List<Object> getDocPageReportAll(TtransDoc doc, DocType type, Boolean isDelete, Date startDate, Date endDate){
		String isSupMan = userService.getCurrentUser().getUserIsSupManager();
		if("1".equals(isSupMan)){
			type.dataInDeptRange(false);
			type.dataInOrgRange(false);
		}

		return transDocManager.getPageListReport(doc, type, isDelete, startDate, endDate);
		}
	
	public List<ToaReportBean> getPageAllListReport(Page page,DocType type,Boolean isDelete,Date startDate,Date endDate) throws ServiceException, SQLException{
		return transDocManager.getAllListReportWithCount(page,type,isDelete,startDate,endDate);
	}
	/**
	 * @author:luosy
	 * @description:  获取所有已分发和已回收的分发对象列表
	 * @date : 2010-8-13
	 * @modifyer:
	 * @description:
	 * @param page
	 * @param model
	 * @return
	 */
	public Page getSendsPage(Page page,TdocSend model,String docId)throws ServiceException ,Exception {
		try{
			StringBuilder hql = new StringBuilder("from TdocSend t  where");
			List<Object> params = new ArrayList<Object>(1);
//			User user = userService.getCurrentUser();
//			hql.append(" t.ttransDoc.ddocEntryPeople = ?");
//			params.add(user.getUserId());
			hql.append(" 1 = ?");
			params.add(1);
			
			if(docId != null && !"".equals(docId)){
				hql.append(" and t.ttransDoc.docId = ? ");
				params.add(docId);
				hql.append(" and (t.ttransDoc.docState = ? ");
				params.add(DocType.Sended.getValue());
				hql.append(" or t.ttransDoc.docState = ? )");
				params.add(DocType.Recycle.getValue());
			}
			
			if(model.getDocRecvTime() != null && !"".equals(model.getDocRecvTime())){
				hql.append(" and t.docRecvTime <= ?");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = model.getDocRecvTime();
				Date  rdate = new Date(sdf.parse(sdf.format(date)).getTime()+24*3600*1000);   //加一天
				
				params.add(rdate);
			}
			
			if(model.getDeptName() != null && !"".equals(model.getDeptName())){
				hql.append(" and t.deptName like ? ");
				params.add("%"+model.getDeptName()+"%");
			}

			if(model.getDocRecvUser() != null && !"".equals(model.getDocRecvUser())){
				hql.append(" and t.docRecvUser like ? ");
				params.add("%"+model.getDocRecvUser()+"%");
			}

			if(model.getDocHavePrintSum() != null && !"".equals(model.getDocHavePrintSum())){
				hql.append(" and t.docHavePrintSum = ? ");
				params.add(model.getDocHavePrintSum());
			}

			if(model.getDocHavePrintNum() != null && !"".equals(model.getDocHavePrintNum())){
				hql.append(" and t.docHavePrintNum = ? ");
				params.add(model.getDocHavePrintNum());
			}

			if(model.getDocRecvRemark() != null && !"".equals(model.getDocRecvRemark())){
				hql.append(" and t.docRecvRemark like ? ");
				params.add("%"+model.getDocRecvRemark()+"%");
			}

			if(null!=model.getRecvState()&&!"".equals(model.getRecvState())){
				hql.append(" and t.recvState = ? ");
				params.add(model.getRecvState());
			}
			
			page = docSendDao.find(page, hql.toString(), params.toArray());
			
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发列表"});
		}catch (Exception e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发列表"});
		}
	}
	
	/**
	 * @author:xiaolj
	 * @description:  获取所有已分发和已回收的分发对象列表
	 * @date : 2010-8-13
	 * @modifyer:
	 * @description:
	 * @param docId
	 * @return list
	 */
	public List getSendsList(String docId)throws ServiceException ,Exception {
		try{
			StringBuilder hql = new StringBuilder("from TdocSend t  where");
			List<Object> params = new ArrayList<Object>(1);
			hql.append(" 1 = ?");
			params.add(1);
			
			List list = new ArrayList();
			
			if(docId != null && !"".equals(docId)){
				hql.append(" and t.ttransDoc.docId = ? ");
				params.add(docId);
				hql.append(" and (t.ttransDoc.docState = ? ");
				params.add(DocType.Sended.getValue());
				hql.append(" or t.ttransDoc.docState = ? )");
				params.add(DocType.Recycle.getValue());
			}
			System.out.println("打印出来啊：" + hql.toString());
			list = docSendDao.find(hql.toString(), params.toArray());
			
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发列表"});
		}catch (Exception e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发列表"});
		}
	}
	
	
	/**
	 * @author:luosy
	 * @description:	保存分发对象
	 * @date : 2010-8-17
	 * @modifyer:
	 * @description:
	 * @param docId		要分发的公文
	 * @param orgIds	要分发到的单位
	 * @return
	 */
	public String saveDocSends(TtransDoc doc,String orgIds,String printSum,String orgIdss,String printSums, OALogInfo ... loginfos)throws ServiceException ,Exception {
		try{
			String[] orgid = null; //主送
			String[] docprint = null;
			String[] orgids = null; //主送
			String[] docprints = null;
			if(orgIds!=null&&!"".equals(orgIds)){
				orgid = orgIds.split(";"); //主送
				docprint = printSum.split(";");
			}
			if(orgIdss!=null&&!"".equals(orgIdss)){
				orgids = orgIdss.split(";");//抄送
				docprints = printSums.split(";");
			}
			if(docprint!=null){
				for(int i=0;i<docprint.length;i++){
					TdocSend docsend = new TdocSend();
					String[] org = orgid[i].split(",");
					docsend = this.getDocSendBydocOrg(doc.getDocId(), org[0]);
					docsend.setDeptCode(org[0]);
					docsend.setDeptName(org[1]);
					docsend.setZsorcs("1");
					docsend.setDocHavePrintSum(docprint[i]);
					docsend.setLiyous(null);
					docsend.setQita(null);
					docsend.setDocHavePrintNum("0");
					docsend.setIsdelete(TdocSend.DOCSEND_NOTDEL);
					docsend.setRecvState(TdocSend.DOCSEND_WAITRECV);
					docsend.setTtransDoc(doc);
					docsend.setDocFilingTime(null);
					docsend.setDocRecvRemark(null);
					docsend.setDocRecvTime(null);
					docsend.setDocRecvUser(null);
					docsend.setDocRemark(null);
					docsend.setFilingUser(null);
					docsend.setOperateIp(null);
					
					docSendDao.save(docsend);
					docSendDao.flush();
				}
			}
			if(docprints!=null){
				for(int i=0;i<docprints.length;i++){
					TdocSend docsend = new TdocSend();
					String[] org = orgids[i].split(",");
					docsend = this.getDocSendBydocOrg(doc.getDocId(), org[0]);
					docsend.setDeptCode(org[0]);
					docsend.setDeptName(org[1]);
					docsend.setZsorcs("0");
					docsend.setDocHavePrintSum(docprints[i]);
					docsend.setLiyous(null);
					docsend.setQita(null);
					docsend.setDocHavePrintNum("0");
					docsend.setIsdelete(TdocSend.DOCSEND_NOTDEL);
					docsend.setRecvState(TdocSend.DOCSEND_WAITRECV);
					docsend.setTtransDoc(doc);
					docsend.setDocFilingTime(null);
					docsend.setDocRecvRemark(null);
					docsend.setDocRecvTime(null);
					docsend.setDocRecvUser(null);
					docsend.setDocRemark(null);
					docsend.setFilingUser(null);
					docsend.setOperateIp(null);
					
					docSendDao.save(docsend);
					docSendDao.flush();
				}
			}
			
			doc.setDocState(DocType.Sended.getValue());
			doc.setDocSendTime(new Date());
			transDocManager.save(doc);
			return "succ";
		}catch (Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	

	
	public int RecycleAll(String[] ids) {
		String hql = "update t_doc_sends t set t.RECV_STATE = '3' where t.senddoc_id in (:list)";
		Query query = docSendDao.getSession().createSQLQuery(hql);
		query.setParameterList("list", ids); 
		return query.executeUpdate();
	}
	
	public int SetAll(String[] ids,int num) {
		
		String hql = "update t_doc_sends t set t.doc_have_print_sum = (:num) where t.senddoc_id in (:list)";
		Query query = docSendDao.getSession().createSQLQuery(hql).setString("num",String.valueOf(num));
		query.setParameterList("list", ids); 
		return query.executeUpdate();
	}
	
	/***
	 * 
	 * @param 公文ID
	 * @return 已发公文列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<Object[]> getHasSentList(String docid)
	throws DAOException, SystemException, ServiceException{
		String sql ="     select t3.zsorcs," + 
		            "            substr(max(sys_connect_by_path(t3.dept_name, ',')), 2) as names," + 
		            "            substr(max(sys_connect_by_path(t3.dept_code, ',')), 2) ids " + 
					"      from  (select t2.*," +
					"                    row_number() over(partition by t2.zsorcs order by t2.dept_name) as rown" +
					"               from (select t.dept_code, t.dept_name, t.recv_state, t.zsorcs" + 
					"         				from t_doc_sends t" + 
					"         			   where t.doc_id = ?" + 
			        "        				 and t.recv_state != 3) t2) t3 " + 
					"start with t3.rown = 1 " +
					"connect by prior t3.rown = t3.rown - 1 " +
					"       and prior t3.zsorcs = t3.zsorcs " +
					"  group by t3.zsorcs " + 
					"  order by t3.zsorcs "; 
				
		SQLQuery query = docSendDao.getSession().createSQLQuery(sql);
		query.setString(0, docid);
		List<Object[]> orgHasSent = query.list();
		return orgHasSent;
	}
	
	/**
	 * @author:luosy
	 * @description: 根据公文ID和机构CODE获取分发对象
	 * @modifyer:
	 * @description:
	 * @param docId		公文ID
	 * @param orgCode	机构CODE
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public TdocSend getDocSendBydocOrg(String docId,String orgCode)throws ServiceException ,Exception {
		try{
			List list = docSendDao.find("from TdocSend t where t.ttransDoc.docId='"+docId+"' and t.deptCode='"+orgCode+"'");
			if(null==list||list.size()<1){
				return new TdocSend();
			}else{
				return (TdocSend)list.get(0);
			}
			
		}catch (Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发对象"});
		}
	}
	
	/**
	 * @author:luosy
	 * @description:  根据ID获取分发对象
	 * @date : 2010-8-17
	 * @modifyer:
	 * @description:
	 * @param docSendId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public TdocSend getDocSendById(String docSendId)throws ServiceException ,Exception {
		try{
			return docSendDao.get(docSendId);
		}catch (Exception e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发对象"});
		}
	}
	

	/**
	 * @author:luosy
	 * @description:  保存分发对象
	 * @date : 2010-8-17
	 * @modifyer:
	 * @description:
	 * @param docSendId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public String saveDocSend(TdocSend docsend, OALogInfo ... loginfos)throws ServiceException ,Exception {
		try{
			docSendDao.save(docsend);
			return "0";
		}catch (Exception e){
			return "-1";
		}
	}
	
	/**
	 * @author:luosy
	 * @description: 回收公文及其分发对象
	 * @date : 2010-8-20
	 * @modifyer:
	 * @description:
	 * @param docId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public String recycleDoc(TtransDoc doc, OALogInfo ... loginfos)throws ServiceException ,Exception {
		try{
			Set<TdocSend> sends = doc.getTdocSends();
			for(Iterator<TdocSend> it=sends.iterator();it.hasNext();){
				TdocSend docsend = it.next();
				docsend.setDocHavePrintNum("0");
				docsend.setIsdelete(TdocSend.DOCSEND_NOTDEL);
				docsend.setRecvState(TdocSend.DOCSEND_RECYCLER);
				docsend.setTtransDoc(doc);
				docsend.setDocFilingTime(null);
				docsend.setDocRecvRemark(null);
				docsend.setDocRecvTime(null);
				docsend.setDocRecvUser(null);
				docsend.setDocRemark(null);
				docsend.setFilingUser(null);
				docsend.setOperateIp(null);
				
				docSendDao.save(docsend);
				docSendDao.flush();
			}
			doc.setDocState(DocType.Recycle.getValue());
			transDocManager.save(doc);
			return "0";
		}catch (Exception e){
			return "-1";
		}
	}
	
	/**
	 * @author:luosy
	 * @description:  取得分发机构信息
	 * @date : 2010-8-18
	 * @modifyer:
	 * @description:
	 * @param docId
	 * @param state
	 * @param flag
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List getSendsorgList(String docId,String state,Boolean flag)throws ServiceException ,Exception {
		try{
			User user = userService.getCurrentUser();
			StringBuilder hql = new StringBuilder("select t.deptCode,t.deptName from TdocSend t ");
			List<Object> params = new ArrayList<Object>(1);
			hql.append(" where 1 = ?");
			params.add(1);
			
			if(docId != null && !"".equals(docId)){
				hql.append(" and t.ttransDoc.docId = ? ");
				params.add(docId);
			}
			
			if(flag){
				hql.append(" and t.recvState = ? ");
				params.add(state);
			}else{
				hql.append(" and t.recvState <> ? ");
				params.add(state);
			}
			
			return docSendDao.find(hql.toString(), params.toArray());
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发列表"});
		}catch (Exception e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"获取分发列表"});
		}
	}
	
	/**
	 * @author:luosy
	 * @description:  根据用户ID获取分组机构
	 * @date : 2010-8-18
	 * @modifyer:
	 * @description:
	 * @param userId
	 * @return
	 * 		Object[]:分组id，分组名，机构编码，机构名
	 * 			groupAgencyId,groupAgencyName,orgSyscode,orgName 
	 * 
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getAgencyListByUserId() throws SystemException,ServiceException {
		try {
			String userId = userService.getCurrentUser().getUserId();
			return groupDetManager.getAgencyListByUserId(userId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"机构"});
		}
	}
	
	
	public List<ToaReportBean> getBorrowReport(Date borrowMonth,Date end,String orgCode,TtransDoc docModel)throws SystemException,ServiceException{
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String hql = "";
			List<Object> list =null;
			if(docModel.getDocTitle()!=null&&!"".equals(docModel.getDocTitle())){
				hql+="select t.docRecvTime, t.ttransDoc.docIssueDepartSigned, t.ttransDoc.docCode,t.ttransDoc.docTitle, t.ttransDoc.docSecretLvl, t.ttransDoc.docEmergency,t.docRemark from TdocSend t where t.recvState='1'"
					+" and t.ttransDoc.docTitle like ? and t.docRecvTime>=? and t.docRecvTime<=? and t.deptCode=?";
				list=docSendDao.find(hql,"%"+docModel.getDocTitle()+"%", borrowMonth,end,orgCode);
			}else if(docModel.getDocCode()!=null&&!"".equals(docModel.getDocCode())){
				hql+="select t.docRecvTime, t.ttransDoc.docIssueDepartSigned, t.ttransDoc.docCode,t.ttransDoc.docTitle, t.ttransDoc.docSecretLvl, t.ttransDoc.docEmergency,t.docRemark from TdocSend t where t.recvState='1'"
					+" and t.ttransDoc.docCode like ? and t.docRecvTime>=? and t.docRecvTime<=? and t.deptCode=?";
				list=docSendDao.find(hql,"%"+docModel.getDocCode()+"%", borrowMonth,end,orgCode);
			}else{
				hql+="select t.docRecvTime, t.ttransDoc.docIssueDepartSigned, t.ttransDoc.docCode,t.ttransDoc.docTitle, t.ttransDoc.docSecretLvl, t.ttransDoc.docEmergency,t.docRemark from TdocSend t where t.recvState='1'"
					+" and t.docRecvTime>=? and t.docRecvTime<=? and t.deptCode=?";
				list=docSendDao.find(hql, borrowMonth,end,orgCode);
			}
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			for(Object obj:list){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				Date begin=(Date)strobj[0];//收文日期
				if(begin!=null){
					bean.setText1(df.format(begin));
				}else{
					bean.setText1("  ");
				}
				if(strobj[1]!=null){
					bean.setText2(strobj[1].toString());
				}else{
					bean.setText2("  ");
				}
	
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
					bean.setText5(strobj[4].toString());
				}else{
					bean.setText5("  ");
				}
				if(strobj[5]!=null){
					bean.setText6(strobj[5].toString());
				}else{
					bean.setText6("  ");
				}
				if(strobj[6]!=null){
					bean.setText7(strobj[6].toString());
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
		} 
		return null;
	}
	///////////////////////// 报表部分  公文查询模块   收文部分
	public List<ToaReportBean> getBorrowReports(TtransDoc docModel,String orgCode)throws SystemException,ServiceException{
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String hql="select t.docRecvTime, t.ttransDoc.docIssueDepartSigned, t.ttransDoc.docCode,t.ttransDoc.docTitle, t.ttransDoc.docSecretLvl, t.ttransDoc.docEmergency,t.recvState from TdocSend t where 1=1 and t.recvState = '1' and t.docRecvTime is not null ";
			
			List<Object> list = new ArrayList<Object>();
			if(docModel.getDocTitle()!=null&&!"".equals(docModel.getDocTitle())){
				int t = docModel.getDocTitle().indexOf("%");
				if(t!=-1){
					String temp =docModel.getDocTitle().replaceAll("%","/%");;
					hql=hql+" and t.ttransDoc.docTitle like '%" + temp + "%'" +" escape '/'";
				}else{
					hql=hql+" and t.ttransDoc.docTitle like '%"+docModel.getDocTitle()+"%'";
				}
				hql=hql+" and t.deptCode= '"+orgCode+"')";
				
				list=docSendDao.find(hql);
			}else if(docModel.getDocCode()!=null&&!"".equals(docModel.getDocCode())){
				int t = docModel.getDocCode().indexOf("%");
				if(t!=-1){
					String temp =docModel.getDocCode().replaceAll("%","/%");;
					hql=hql+" and t.ttransDoc.docCode like '%" + temp + "%'" +" escape '/'"+" and t.deptCode= '"+orgCode+"')";
				}else{
					hql=hql+" and t.ttransDoc.docCode like '%"+docModel.getDocCode()+"%'"+" and t.deptCode= '"+orgCode+"')";
				}
				list=docSendDao.find(hql);
			}else{
				hql=hql+" and t.deptCode = ?)";
				list=docSendDao.find(hql,orgCode);
			}
			
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			for(Object obj:list){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				Date begin=(Date)strobj[0];//收文日期
				if(begin!=null){
					bean.setText1(df.format(begin));
				}else{
					bean.setText1("  ");
				}
				if(strobj[1]!=null){
					bean.setText2(strobj[1].toString());
				}else{
					bean.setText2("  ");
				}
	
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
					bean.setText5(strobj[4].toString());
				}else{
					bean.setText5("  ");
				}
				if(strobj[5]!=null){
					bean.setText6(strobj[5].toString());
				}else{
					bean.setText6("  ");
				}
				if(strobj[6]!=null){
					if("0".equals(strobj[6].toString())){
						bean.setText7("待收");
					}else if("1".equals(strobj[6].toString())){
						bean.setText7("已收");
					}else if("2".equals(strobj[6].toString())){
						bean.setText7("拒收");
					}else if("3".equals(strobj[6].toString())){
						bean.setText7("回收");
					}
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
		} 
		return null;
	}
	
	public List<ToaReportBean> pageToToaReportBean(List<Object> pages)throws SystemException,ServiceException{
		try {
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			for(Object obj:pages){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				if(strobj[1]!=null){
					bean.setText1(strobj[1].toString());
				}else{
					bean.setText1("  ");
				}
				if(strobj[2]!=null){
					bean.setText2(strobj[2].toString());
				}else{
					bean.setText2("  ");
				}
	
				if(strobj[3]!=null){
					bean.setText3(strobj[3].toString());
				}else{
					bean.setText3("  ");
				}
				if(strobj[6]!=null){
					bean.setText4(strobj[6].toString());
				}else{
					bean.setText4("  ");
				}
				Date begin=(Date)strobj[5];//收文日期
				if(begin!=null){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.setText5(df.format(begin));
				}else{
					bean.setText5("  ");
				}
				
				if(strobj[9]!=null){
					if("0".equals(strobj[9].toString())){
						bean.setText6("草稿");
					}
					if("1".equals(strobj[9].toString())){
						bean.setText6("待签章提交");
					}
					if("2".equals(strobj[9].toString())){
						bean.setText6("待分发");
					}
					if("3".equals(strobj[9].toString())){
						bean.setText6("已分发");
					}
				}else{
					bean.setText6("  ");
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
		} 
		return null;
	}
	public List<ToaReportBean> pageToToaReportBeanGroup(Page page)throws SystemException,ServiceException{
		try {
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			for(Object obj:page.getResult()){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				if(strobj[1]!=null){
					bean.setText1(strobj[1].toString());
				}else{
					bean.setText1("  ");
				}
				if(strobj[2]!=null){
					bean.setText2(strobj[2].toString());
				}else{
					bean.setText2("  ");
				}
	
				if(strobj[3]!=null){
					bean.setText3(strobj[3].toString());
				}else{
					bean.setText3("  ");
				}
				if(strobj[6]!=null){
					bean.setText4(strobj[6].toString());
				}else{
					bean.setText4("  ");
				}
				Date begin=(Date)strobj[5];//收文日期
				if(begin!=null){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.setText5(df.format(begin));
				}else{
					bean.setText5("  ");
				}
				
				if(strobj[9]!=null){
					if("0".equals(strobj[9].toString())){
						bean.setText6("草稿");
					}
					if("1".equals(strobj[9].toString())){
						bean.setText6("待签章提交");
					}
					if("2".equals(strobj[9].toString())){
						bean.setText6("待分发");
					}
					if("3".equals(strobj[9].toString())){
						bean.setText6("已分发");
					}
				}else{
					bean.setText6("  ");
				}
				if(strobj[10]!=null){
					bean.setText7(strobj[10].toString());
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
		} 
		return null;
	}
	public List<ToaReportBean> pageToToaReportBeanGroup(List<Object> pages)throws SystemException,ServiceException{
		try {
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			for(Object obj:pages){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				if(strobj[1]!=null){
					bean.setText1(strobj[1].toString());
				}else{
					bean.setText1("  ");
				}
				if(strobj[2]!=null){
					bean.setText2(strobj[2].toString());
				}else{
					bean.setText2("  ");
				}
	
				if(strobj[3]!=null){
					bean.setText3(strobj[3].toString());
				}else{
					bean.setText3("  ");
				}
				if(strobj[6]!=null){
					bean.setText4(strobj[6].toString());
				}else{
					bean.setText4("  ");
				}
				Date begin=(Date)strobj[5];//收文日期
				if(begin!=null){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.setText5(df.format(begin));
				}else{
					bean.setText5("  ");
				}
				
				if(strobj[9]!=null){
					if("0".equals(strobj[9].toString())){
						bean.setText6("草稿");
					}
					if("1".equals(strobj[9].toString())){
						bean.setText6("待签章提交");
					}
					if("2".equals(strobj[9].toString())){
						bean.setText6("待分发");
					}
					if("3".equals(strobj[9].toString())){
						bean.setText6("已分发");
					}
				}else{
					bean.setText6("  ");
				}
				if(strobj[10]!=null){
					bean.setText7(strobj[10].toString());
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
		} 
		return null;
	}
	
	public List<ToaReportBean> getBorrowReportSend(Date borrowMonth,Date end,String orgCode,TtransDoc docModel)throws SystemException,ServiceException{
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String hql = "";
			if(docModel.getDocTitle()!=null&&!"".equals(docModel.getDocTitle())){
				hql="select t.docId,t.docSendTime,t.docCode,t.docTitle, t.docSecretLvl, t.docEmergency,t.docRemark from TtransDoc t where t.docState='3'"
					+" and t.docTitle like '%"+docModel.getDocTitle()+"%' and t.docSendTime>=? and t.docSendTime<=? and t.rest3=?";
			}else if(docModel.getDocCode()!=null&&!"".equals(docModel.getDocCode())){
				hql="select t.docId,t.docSendTime,t.docCode,t.docTitle, t.docSecretLvl, t.docEmergency,t.docRemark from TtransDoc t where t.docState='3'"
					+" and t.docCode like '%"+docModel.getDocCode()+"%' and t.docSendTime>=? and t.docSendTime<=? and t.rest3=?";
			}else{
				hql="select t.docId,t.docSendTime,t.docCode,t.docTitle, t.docSecretLvl, t.docEmergency,t.docRemark from TtransDoc t where t.docState='3'"
					+" and t.docSendTime>=? and t.docSendTime<=? and t.rest3=?";
			}
			List<Object> list=docDao.find(hql, borrowMonth,end,orgCode);
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			ToaReportBean bean=null;//报表对象
			String orgName="";
			for(Object obj:list){
				bean=new ToaReportBean();
				Object[] strobj=(Object[])obj;
				if(strobj[0]!=null){
					String id=strobj[0].toString();	
				String sql="select t.deptName,t.docRecvTime,t.docRemark from TdocSend t where t.ttransDoc.docId=?";			
					List<Object> mlist=docSendDao.find(sql,id);
					StringBuffer org=new StringBuffer();
					for(Object mobj:mlist){
						Object[] mstrobj=(Object[])mobj;
						if(mstrobj[0]!=null){
							org.append(mstrobj[0].toString());
							}
						   org.append(",");
						}
					orgName=org.toString();
				}
				
				Date begin=(Date)strobj[1];//发文日期
				if(begin!=null){
					bean.setText1(df.format(begin));
				}else{
					bean.setText1("  ");
				}
				if(orgName!=null){
					bean.setText2(orgName.substring(0,orgName.length()-1));  //去除最后的逗号后加入到text2
				}else{
					bean.setText2("  ");
				}
	
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
					bean.setText5(strobj[4].toString());
				}else{
					bean.setText5("  ");
				}
				if(strobj[5]!=null){
					bean.setText6(strobj[5].toString());
				}else{
					bean.setText6("  ");
				}
				if(strobj[6]!=null){
					bean.setText7(strobj[6].toString());
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
		} 
		return null;
	}
	
	/*
	 * 根据条件查询档案中心文件列表
	 */
	public  Page<TtransArchive> search(Page<TtransArchive> page,TtransArchive model,Date startDate,Date endDate) throws SystemException,ServiceException{
		try {
			List<Object> list = new ArrayList<Object>(0);
//			ArrayList list = new ArrayList();
			Calendar ca = Calendar.getInstance();
			User user=userService.getCurrentUser();
			Organization org=userService.getUserDepartmentByUserId(user.getUserId());
			String orgId=org.getOrgSyscode();
			StringBuffer hql = new StringBuffer("from TtransArchive t where 1=1 and t.rest4 = '0'");
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
	 * 获取所有档案中心文件
	 */
	public Page<TtransArchive> getPageArchive(Page<TtransArchive> page,String treeValue)  throws SystemException,ServiceException
	{	
		try{
			//List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			User user=userService.getCurrentUser();
			Organization org=userService.getUserDepartmentByUserId(user.getUserId());
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
					hql="  from TtransArchive t where 1=1 and t.rest4 = '0' "+str+" order by t.docSendTime desc ";
				}else {					
					hql="  from TtransArchive t where 1=1 and t.rest4 = '0' and t.deptCode in ('"+orgId+"') "+str+" order by t.docSendTime desc ";
				}
				page=archiveDao.find(page, hql, treeValue);
			}else {
				if(user!=null&& user.getUserLoginname().equals("admin")){										//超级用户
					hql="  from TtransArchive t where 1=1 and t.rest4 = '0' order by t.docSendTime desc ";					
				}else {
					hql="  from TtransArchive t where 1=1 and t.rest4 = '0' and t.deptCode in ('"+orgId+"') order by t.docSendTime desc ";
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
			User user=userService.getCurrentUser();
			String hql ="";
			if(user!=null&&user.getUserLoginname().equals("admin")){
			    hql = "select t.docYear from TtransArchive t where  1=1";
				hql = hql + " group by t.docYear";
			}else {
				
				List orgList=userService.getOrgAndChildOrgByCurrentUser();
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
			User user=userService.getCurrentUser();
			String hql ="";
			if(user!=null&&user.getUserLoginname().equals("admin")){
				hql = "select t.docMonth from TtransArchive t where t.docYear='"+year+"' and t.rest4 = '0'";
				hql = hql + "  group by t.docMonth";
			}else {
				List orgList=userService.getOrgAndChildOrgByCurrentUser();
				Organization org=new Organization();
				String orgIds="";
				for(int i=0;i<orgList.size();i++){
					org=(Organization)orgList.get(i);
					orgIds+=",'"+org.getOrgSyscode()+"'";
				}
				orgIds=orgIds.substring(1);
				hql = "select t.docMonth from TtransArchive t where t.docYear='"+year+"' and  t.deptCode in ("+orgIds+")  and t.rest4 = '0'";
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
	 * 日期转换成字符串
	 * @param date
	 * @param temp
	 * @return
	 */
	public String getDateToString(Date date,String temp){
		SimpleDateFormat df=new SimpleDateFormat(temp);
		return df.format(date);
	}
	
	/**
	 * 收文统计
	 * @author: qibh
	 *@param page
	 *@param type
	 *@param isDelete
	 *@param startDate
	 *@param endDate
	 *@return
	 *@throws ServiceException
	 *@throws SQLException
	 * @created: 2012-10-31 上午03:17:55
	 * @version :5.0
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaReportBean> getBorrowWithCount(Page page,DocType type,Boolean isDelete,Date startDate,Date endDate) throws ServiceException, SQLException {
		List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select o.depart_name,count(o.depart_name) from (select t.dept_code as depart_name from t_doc_sends t ");
		User user = userService.getCurrentUser();
		List<Object> params = new ArrayList<Object>(0);
		hql.append(" where 1 = 1 ");
//		if(type == DocType.Draft || type == DocType.ReturnBack || type == DocType.Recycle){//查询草稿
//			hql.append(" and t.ddoc_entry_people = '"+user.getUserId()+"'");//录入人ID
//		}
//		if(type != null){													//公文状态
//			List<DocType> docTypeList = type.getDocTypeList();
//			if(docTypeList != null && !docTypeList.isEmpty()){
//				StringBuilder docTypeStr = new StringBuilder();
//				for(DocType docType : docTypeList){
//					docTypeStr.append("'").append(docType.getValue()).append("'").append(",");
//				}
//				String docTypeString = docTypeStr.substring(0, docTypeStr.length()-1);
//				hql.append(" and t.doc_state in("+docTypeString+")");
//				
//			}else{
//				if(type.getRule()){
//					hql.append(" and t.doc_state = '"+type.getValue()+"'");
//				}else{
//					hql.append(" and t.doc_state <> '"+type.getValue()+"'");
//				}
//				params.add(type.getValue());
//			}
//
//		}
		if(isDelete != null){
//			hql.append(" and t.isdelete = ?");								//是否是删除的公文
			if(isDelete){
				hql.append(" and t.isdelete = '"+DocType.DELETE+"'");
//				params.add(DocType.DELETE);									//删除状态
			}else{
				hql.append(" and t.isdelete = '"+DocType.NOTDELETE+"'");
//				params.add(DocType.NOTDELETE);								//非删除状态
			}
		}
		if(startDate != null && !"".equals(startDate)){//按录入时间搜索
			hql.append(" and  to_char(t.doc_recv_time, 'yyyy-MM-dd') >= '"+df.format(startDate)+"'");
//			params.add(startDate);
		}
		if(endDate != null && !"".equals(endDate)){//按录入时间搜索
			hql.append(" and  to_char(t.doc_recv_time, 'yyyy-MM-dd') <= '"+df.format(endDate)+"'");
//			params.add(endDate);
		}
		hql.append(" ) o group by o.depart_name");
		String sql=hql.toString();
		ResultSet rs= docDao.executeJdbcQuery(sql);
		ToaReportBean bean=null;//报表对象
		while(rs.next()){
			try {
				bean=new ToaReportBean();
				if(rs.getString(1)!=null){
					bean.setText1(rs.getString(1));
				}else{
					bean.setText1("  ");
				}
				
				if(rs.getString(2)!=null){
					bean.setText2(rs.getString(2));
				}else{
					bean.setText2("  ");
				}
				reportList.add(bean);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return reportList;
	}
	
	
	/**
	 * 生成柱状图形
	 * @author: qibh
	 *@param page
	 *@param realPath
	 *@param docType
	 *@param startDate
	 *@param endDate
	 *@throws SystemException
	 *@throws ServiceException
	 * @created: 2012-10-31 上午03:09:12
	 * @version :5.0
	 */
	@Transactional(readOnly=true)
	public void printBarChart(Page page,String realPath,DocType docType,String Identification,Date startDate,Date endDate) throws SystemException,ServiceException{
		try{
			FileOutputStream barJpg = null;
			
			DefaultCategoryDataset barData = new DefaultCategoryDataset();
			List<ToaReportBean> dataList = new ArrayList<ToaReportBean>();
			String pieTit = "";
			if(Identification.equals("send")){
				pieTit = "各部门发文的情况";
				dataList = transDocManager.getAllListReportWithCount(page,docType,false,startDate,endDate);
			}else if(Identification.equals("receive")){
				pieTit = "各部门收文的情况";
				dataList = this.getBorrowWithCount(page, docType, false, startDate, endDate);
			}
			for(ToaReportBean obj:dataList){
				TUumsBaseOrg org= userService.getOrgInfoBySyscode(obj.getText1().toString());
				String depa = org.getOrgName();
				barData.addValue(new Double(new Double(obj.getText2().toString()).doubleValue()),depa, depa);
			}
			
			//pieTit =	"收发文统计图";
			String rangeAxisTitle = "部门";
			
			JFreeChart chart = ChartFactory.createBarChart3D(pieTit, rangeAxisTitle, "数量", barData, PlotOrientation.VERTICAL, true, true, false);
			
			chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
			chart.setBackgroundPaint(Color.white);//new Color(0xECF4F9));   //设定背景色
			CategoryPlot plot = chart.getCategoryPlot();    ////获得 plot：CategoryPlot			
			NumberAxis numberAxis=(NumberAxis)plot.getRangeAxis();//取得纵轴   
			//numberAxis.setAutoRangeIncludesZero(false);
			
			//设置柱图曲线图纵坐标间距
			numberAxis.setAutoTickUnitSelection(false);	
			numberAxis.setTickUnit(new NumberTickUnit(5));	
			BarRenderer renderer = (BarRenderer) plot.getRenderer();//获得renderer 注意这里是下嗍造型到BarRenderer
			renderer.setDrawBarOutline(false);  // Bar的外轮廓线不画
			//设置柱子上显示值
			//renderer.setIncludeBaseInRange(true); 
			renderer.setMaximumBarWidth(0.08); 
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
			renderer.setBaseItemLabelsVisible(true);
			plot.setRenderer(renderer); 
			String path = realPath+"oa\\image\\docSend\\DrawBar.jpg";
			File flie = new File(path);
			if(flie.exists()){
				flie.delete();
			}
			try {
				barJpg = new FileOutputStream(path);
				ChartUtilities.writeChartAsJPEG(barJpg, 100, chart, 900, 450, null);
			} catch (Exception e) {
			} finally {
				try {
					if(barJpg!=null){
						barJpg.close();
					}
				} catch (Exception e) {
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"创建柱状图"});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 查找是否有打印密码
	public TdocSendPrintPassword getPrintPassword(String userId){
		String sql = "from TdocSendPrintPassword t where t.userId = '"+userId+"'";
		List<TdocSendPrintPassword> list = null;
		try {
			list = printPasswordDao.find(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list==null||list.size()==0){
			return new TdocSendPrintPassword() ;
		}
		return list.get(0);
	}
	//修改保存 打印密码
	public void PrintPasswordSaveOrUpdat(TdocSendPrintPassword t){
		try {
			if(t.getId()!=null&&!"".equals(t.getId())){
				printPasswordDao.update(t);
			}else printPasswordDao.save(t);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TtransDoc getTtransDocBysenddocId(String senddocId){
		String hql = "select t.ttransDoc from TdocSend t where t.senddocId='"+senddocId+"'";
		List<TtransDoc> t = docSendDao.find(hql.toString());
		if(t!=null&&t.size()>0){
			return t.get(0);
		}
		return null;
	}
	
	/**
	 * author:dengzc
	 * description:搜索--自动完成功能
	 * modifyer:
	 * description:
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public StringBuffer searchByAutoComplete(String str) throws SystemException,ServiceException{
		StringBuffer sb = new StringBuffer();
		try {
			String queryStr = "select t.orgName,t.orgSyscode from TUumsBaseOrg t where t.orgIsdel=0 and t.isOrg=1";
			List<String[]> l = OrgDao.find(queryStr,new Object[]{});
			if(l != null && !l.isEmpty()) {
				
				for(int i = 0;i<l.size();i++){
					
					Object [] obj=(Object[])l.get(i);	
					if(obj[0].toString().equals("市委办公厅")){
						l.remove(i);//过滤掉市委办公厅
						i--;					
					}
				}		
			}
			
			for(int j=0;j<l.size();j++)	{	
				Object [] obj=(Object[])l.get(j);		
				 String orgName=obj[0].toString();
				 String orgSyscode=obj[1].toString();
				 if(orgName.toLowerCase().indexOf(str)!=-1){
						sb.append(orgName).append(","+orgSyscode).append("\n");				
				}
				}
			//sb.append("南昌市政府办公厅").append(","+"001999000").append("\n");
//			for(Iterator<String[]> it=l.iterator();it.hasNext();){
//			  String orgName = it.next()[0];
//			  String orgSyscode=it.next()[1];
//				if(orgName.toLowerCase().indexOf(str)!=-1){
//					sb.append(orgName).append(orgSyscode).append("\n");				
//				}
//			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"机构名称"});
		}
		return sb;
	}
	
}
