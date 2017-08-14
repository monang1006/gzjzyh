package com.strongit.doc.sends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.sends.util.DocType;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 发文管理服务类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-8-10 下午04:44:11
 * @version  2.0.7
 * @classpath com.strongit.doc.sends.TransDocManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
@OALogger
public class TransDocManager {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	GenericDAOHibernate<TtransDoc, String> docDao ;	//DAO处理类
	
	@Autowired IUserService userService ;						//统一用户服务类.
	
	@Autowired TransDocAttachManager attachManager ;
	
	/**
	 * 注入SESSION工厂
	 * @author:邓志城
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		docDao = new GenericDAOHibernate<TtransDoc, java.lang.String>(sessionFactory, TtransDoc.class);
	}

	/**
	 * 保存公文信息.
	 * @author:邓志城
	 * @date:2010-8-11 上午09:06:56
	 * @param model				公文对象
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	void save(TtransDoc model,OALogInfo...infos) throws ServiceException ,Exception {
		if("".equals(model.getDocId())){
			model.setDocId(null);
			model.setDocEntryTime(new Date());//创建日期
		}
		docDao.save(model);
		String deledAttachId = model.getDeledAttachId();//更新的时候删除掉的附件
		if(deledAttachId != null && !"".equals(deledAttachId)){
			if(deledAttachId.startsWith(",")){
				deledAttachId = deledAttachId.substring(1);
			}
			String[] deledIds = deledAttachId.split(",");
			for(int i=0;i<deledIds.length;i++){
				String id = deledIds[i];
				attachManager.delete(id);
			}
		}
		Set<TtransDocAttach> set = (Set<TtransDocAttach>)model.getObj();
		if(set != null && !set.isEmpty()){
			for(Iterator<TtransDocAttach> it = set.iterator();it.hasNext();){
				attachManager.save(it.next());
			}
		}
		docDao.flush();
	}

	/**
	 * 删除公文信息.这里只是逻辑删除
	 * @author:邓志城
	 * @date:2010-8-11 上午09:08:01
	 * @param id				公文主键id
	 * @throws ServiceException
	 */
	void delete(String id) throws ServiceException {
		TtransDoc model = docDao.get(id);
		model.setIsdelete(DocType.DELETE);
		docDao.update(model);
	}

	/**
	 * 采用JDBC支持批量更新操作
	 * @author:邓志城
	 * @date:2010-8-18 上午10:16:20
	 * @param ids
	 */
	void jdbcDelete(String id,OALogInfo...infos) {
		if(id!= null){
			StringBuilder sql = new StringBuilder("update T_TRANS_DOC t set t.ISDELETE = '"+DocType.DELETE+"' where t.DOC_ID = '");
			sql.append(id).append("'");
			/*StringBuilder param = new StringBuilder();
			for(int i=0;i<ids.length;i++){
				param.append(",").append("'").append(ids[i]).append("'"); 
			}
			sql.append(param.substring(1)).append(")");*/
			logger.info("更新草稿状态SQL：{}",sql);
			docDao.executeJdbcUpdate(sql.toString());
		}
	}
	
	/**
	 * 提交公文
	 * @author:邓志城
	 * @date:2010-8-17 下午01:24:44
	 * @param id				公文主键id
	 * @throws ServiceException
	 */
	void submit(String id) throws ServiceException {
		TtransDoc model = docDao.get(id);
		if(model != null){
			String docSealIs = model.getDocSealIs();
			if("1".equals(docSealIs)){//需要签章
				if(DocType.Sign.getValue().equals(model.getDocState())){//如果已经是签章状态,说明是从签章列表中提交
					model.setDocState(DocType.Send.getValue());//否则将公文状态置为待分发状态
				}else{
					model.setDocState(DocType.Sign.getValue());//将公文状态置为签章状态
				}
			}else{
				model.setDocState(DocType.Send.getValue());//否则将公文状态置为待分发状态
			}
			docDao.update(model);
		}else {
			logger.error("id为" + id + "的公文不存在或已删除！");
		}
	}

	/**
	 * 采用JDBC方式更新,避免Hibernate造成死锁.
	 * @author:邓志城
	 * @date:2010-8-18 上午10:36:58
	 * @param ids
	 * @throws SQLException
	 */
	void jdbcSubmit(TtransDoc model,OALogInfo...infos) throws SQLException {
		Connection conn = docDao.getConnection();
		StringBuilder sql = new StringBuilder("update T_TRANS_DOC t set t.DOC_STATE='");
		if(model != null){
			Date officalTime = model.getDocOfficialTime();
			String docSealIs = model.getDocSealIs();
			if("1".equals(docSealIs)){//需要签章
				if(DocType.Sign.getValue().equals(model.getDocState())){//如果已经是签章状态,说明是从签章列表中提交
					sql.append(DocType.Send.getValue()).append("'");//否则将公文状态置为待分发状态
				}else{
					sql.append(DocType.Sign.getValue()).append("'");//将公文状态置为签章状态
				}
			}else{
				sql.append(DocType.Send.getValue()).append("'");//否则将公文状态置为待分发状态
			}
			if(officalTime == null || "".equals(officalTime)){ 
				sql.append(",t.DOC_OFFICIAL_TIME = ?");
			}
			sql.append(" where t.DOC_ID = '").append(model.getDocId()).append("'");
			logger.info("更新草稿状态SQL：{}",sql);
			PreparedStatement pstm = conn.prepareStatement(sql.toString());
			//pstm.setDate(1, new java.sql.Date(System.currentTimeMillis()));//精确到日
			if(sql.indexOf("?")!=-1){
				pstm.setTimestamp(1, new Timestamp(System.currentTimeMillis()));//精确到秒,设定成文日期
			}
			pstm.executeUpdate();
			pstm.close();
		}else {
			logger.error("id为" + model.getDocId() + "的公文不存在或已删除！");
		}
	}
	
	/**
	 * 根据公文id得到公文信息
	 * @author:邓志城
	 * @date:2010-8-11 上午10:16:59
	 * @param id				公文主键id
	 * @return					公文信息
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	TtransDoc getDocById(String id) throws ServiceException {
		return docDao.get(id);
	}

	/**
	 * 更新公文信息
	 * @author:邓志城
	 * @date:2010-8-23 上午09:14:03
	 * @param doc				公文对象
	 * @throws ServiceException
	 */
	void update(TtransDoc doc,OALogInfo...infos) throws ServiceException {
		docDao.update(doc);
		docDao.flush();
	}
	
	/**
	 * 查询公文列表；草稿列表，签章列表，分发列表，etc.
	 * @author:邓志城
	 * @date:2010-8-10 下午05:23:33
	 * @param page				分页对象
	 * @param model				公文对象
	 * @param type				列表类型,从枚举中读取
	 * @param isDelete			是否删除状态
	 * @param startDate			开始日期
	 * @param endDate			结束日期
	 * @return					分页列表
	 * 		List<Object[]>{
	 * 			主键id,公文标题,发文文号,紧急程度,公文类别,录入时间,密级,签章人,是否锁定印章,公文状态
	 * }
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	Page<Object[]> getPageList(Page page,TtransDoc model,DocType type,Boolean isDelete,Date startDate,Date endDate) throws ServiceException {
		StringBuilder hql = new StringBuilder("select t.docId, t.docTitle,t.docCode,t.docEmergency,t.docClass,t.docEntryTime,t.docSecretLvl,t.docSealPeople,t.rest1,t.docState from TtransDoc t ");
		User user = userService.getCurrentUser();
		String usercod = user.getSupOrgCode();
		List<Object> params = new ArrayList<Object>(0);
		hql.append(" where 1 = 1 ");
//		if(usercod.equals("001"))
//		{
//			if(type == DocType.Draft || type == DocType.ReturnBack || type == DocType.Recycle){//查询草稿
//				hql.append(" and (t.ddocEntryPeople = ? or t.ddocEntryPeople = ?)");
//				params.add("001");										//录入人ID
//				params.add(user.getUserId());
//			}
//		}else
//		{
//			if(type == DocType.Draft || type == DocType.ReturnBack || type == DocType.Recycle){//查询草稿
//				hql.append(" and t.ddocEntryPeople = ?");
//				params.add(user.getUserId());										//录入人ID
//			}
//		}
		if(usercod.equals("001")) {//在机构范围内查询
			
			hql.append(" and t.rest3 = ?");
			params.add("001");
		}else{//在部门范围内查询
			Organization organization = userService.getDepartmentByOrgId(user.getOrgId());
			hql.append(" and t.rest3 = ?");
			params.add(organization.getOrgSyscode());
		}
		if(type != null){													//公文状态
			List<DocType> docTypeList = type.getDocTypeList();
			if(docTypeList != null && !docTypeList.isEmpty()){
				StringBuilder docTypeStr = new StringBuilder();
				for(DocType docType : docTypeList){
					docTypeStr.append("'").append(docType.getValue()).append("'").append(",");
				}
				String docTypeString = docTypeStr.substring(0, docTypeStr.length()-1);
				hql.append(" and t.docState in("+docTypeString+")");
				
			}else{
				if(type.getRule()){
					hql.append(" and t.docState = ?");
				}else{
					hql.append(" and t.docState <> ?");
				}
				params.add(type.getValue());
			}

			
			if(usercod.equals("001")) {//在机构范围内查询
				
				hql.append(" and t.rest3 = ?");
				params.add("001");
			}else if(type.getDataInDeptRange()||type.getDataInOrgRange()) {//在部门范围内查询
				Organization organization = userService.getDepartmentByOrgId(user.getOrgId());
				hql.append(" and t.rest3 = ?");
				params.add(organization.getOrgSyscode());
			}
			type.reset();
		}
		if(isDelete != null){
			hql.append(" and t.isdelete = ?");								//是否是删除的公文
			if(isDelete){
				params.add(DocType.DELETE);									//删除状态
			}else{
				params.add(DocType.NOTDELETE);								//非删除状态
			}
		}
		if(model.getDocTitle() != null && !"".equals(model.getDocTitle())){//根据标题搜索
			model.setDocTitle(model.getDocTitle().trim());		//去除空格
			
			int t = model.getDocTitle().indexOf("%");
			if(t!=-1){
				String temp =model.getDocTitle().replaceAll("%","/%");;
				hql.append(" and t.docTitle like '%" + temp + "%'" +" escape '/'");
			}else{
				hql.append(" and t.docTitle like ?");
				params.add("%"+model.getDocTitle()+"%");
			}
		}
		if(model.getDocCode() != null && !"".equals(model.getDocCode())){//根据文号搜索
			model.setDocCode(model.getDocCode().trim());
			
			int t = model.getDocCode().indexOf("%");
			if(t!=-1){
				String temp =model.getDocCode().replaceAll("%","/%");;
				hql.append(" and t.docCode like '%" + temp + "%'" +" escape '/'");
			}else{
				hql.append(" and t.docCode like ?");
				params.add("%"+model.getDocCode()+"%");
			}
		}
		if(model.getDocEmergency() != null && !"".equals(model.getDocEmergency())){//根据密级搜索,精确查询
			model.setDocEmergency(model.getDocEmergency().trim());
			hql.append(" and t.docEmergency = ?");
			params.add(model.getDocEmergency());
		}
		if(model.getDocClass() != null && !"".equals(model.getDocClass())){//根据公文种类搜索
			hql.append(" and t.docClass = ?");
			params.add(model.getDocClass());
		}
		if(startDate != null && !"".equals(startDate)){//按录入时间搜索
			hql.append(" and t.docEntryTime >= ?");
			params.add(startDate);
		}
		if(endDate != null && !"".equals(endDate)){//按录入时间搜索
			hql.append(" and t.docEntryTime <= ?");
			params.add(endDate);
		}
		if(model.getDocSecretLvl() != null && !"".equals(model.getDocSecretLvl())){//按密级搜索
			model.setDocSecretLvl(model.getDocSecretLvl().trim());
			hql.append(" and t.docSecretLvl = ?");
			params.add(model.getDocSecretLvl());
		}
		if(model.getDocSealPeople() != null && !"".equals(model.getDocSealPeople())){
			if("0".equals(model.getDocSealPeople())){//未签章
				hql.append(" and t.docSealPeople is null");
			}else if("1".equals(model.getDocSealPeople())){
				hql.append(" and t.docSealPeople is not null");
			}
		}
		hql.append(" order by t.docEntryTime desc");
		page = docDao.find(page, hql.toString(), params.toArray());
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	List<Object> getPageListReport(TtransDoc model,DocType type,Boolean isDelete,Date startDate,Date endDate) throws ServiceException {
		StringBuilder hql = new StringBuilder("select t.docId, t.docTitle,t.docCode,t.docEmergency,t.docClass,t.docEntryTime,t.docSecretLvl,t.docSealPeople,t.rest1,t.docState,t.docIssueDepartSigned from TtransDoc t ");
		User user = userService.getCurrentUser();
		List<Object> params = new ArrayList<Object>(0);
		hql.append(" where 1 = 1 ");
		String usercod = user.getSupOrgCode();
		
			if(type == DocType.Draft || type == DocType.ReturnBack || type == DocType.Recycle){//查询草稿
				hql.append(" and t.ddocEntryPeople = ?");
				params.add(user.getUserId());										//录入人ID
			}
		
		
		if(type != null){													//公文状态
			List<DocType> docTypeList = type.getDocTypeList();
			if(docTypeList != null && !docTypeList.isEmpty()){
				StringBuilder docTypeStr = new StringBuilder();
				for(DocType docType : docTypeList){
					docTypeStr.append("'").append(docType.getValue()).append("'").append(",");
				}
				String docTypeString = docTypeStr.substring(0, docTypeStr.length()-1);
				hql.append(" and t.docState in("+docTypeString+")");
				
			}else{
				if(type.getRule()){
					hql.append(" and t.docState = ?");
				}else{
					hql.append(" and t.docState <> ?");
				}
				params.add(type.getValue());
			}

			if(type.getDataInDeptRange()) {//在部门范围内查询
				Organization organization = userService.getDepartmentByOrgId(user.getOrgId());
				hql.append(" and t.rest3 = ?");
				params.add(organization.getOrgSyscode());
			}
			if(type.getDataInOrgRange()) {//在机构范围内查询
				TUumsBaseOrg baseOrg = userService.getSupOrgByUserIdByHa(user.getUserId());
				hql.append(" and t.rest3 = ?");
				params.add(baseOrg.getOrgSyscode());
			}
			type.reset();
		}
		if(isDelete != null){
			hql.append(" and t.isdelete = ?");								//是否是删除的公文
			if(isDelete){
				params.add(DocType.DELETE);									//删除状态
			}else{
				params.add(DocType.NOTDELETE);								//非删除状态
			}
		}
		if(model.getDocTitle() != null && !"".equals(model.getDocTitle())){//根据标题搜索
			model.setDocTitle(model.getDocTitle().trim());		//去除空格
			
			int t = model.getDocTitle().indexOf("%");
			if(t!=-1){
				String temp =model.getDocTitle().replaceAll("%","/%");;
				hql.append(" and t.docTitle like '%" + temp + "%'" +" escape '/'");
			}else{
				hql.append(" and t.docTitle like ?");
				params.add("%"+model.getDocTitle()+"%");
			}
		}
		if(model.getDocCode() != null && !"".equals(model.getDocCode())){//根据文号搜索
			model.setDocCode(model.getDocCode().trim());
			
			int t = model.getDocCode().indexOf("%");
			if(t!=-1){
				String temp =model.getDocCode().replaceAll("%","/%");;
				hql.append(" and t.docCode like '%" + temp + "%'" +" escape '/'");
			}else{
				hql.append(" and t.docCode like ?");
				params.add("%"+model.getDocCode()+"%");
			}
		}
//		if(model.getDocEmergency() != null && !"".equals(model.getDocEmergency())){//根据密级搜索,精确查询
//			model.setDocEmergency(model.getDocEmergency().trim());
//			hql.append(" and t.docEmergency = ?");
//			params.add(model.getDocEmergency());
//		}
//		if(model.getDocClass() != null && !"".equals(model.getDocClass())){//根据公文种类搜索
//			hql.append(" and t.docClass = ?");
//			params.add(model.getDocClass());
//		}
		if(startDate != null && !"".equals(startDate)){//按录入时间搜索
			hql.append(" and t.docEntryTime >= ?");
			params.add(startDate);
		}
		if(endDate != null && !"".equals(endDate)){//按录入时间搜索
			hql.append(" and t.docEntryTime <= ?");
			params.add(endDate);
		}
//		if(model.getDocSecretLvl() != null && !"".equals(model.getDocSecretLvl())){//按密级搜索
//			model.setDocSecretLvl(model.getDocSecretLvl().trim());
//			hql.append(" and t.docSecretLvl = ?");
//			params.add(model.getDocSecretLvl());
//		}
//		if(model.getDocSealPeople() != null && !"".equals(model.getDocSealPeople())){
//			if("0".equals(model.getDocSealPeople())){//未签章
//				hql.append(" and t.docSealPeople is null");
//			}else if("1".equals(model.getDocSealPeople())){
//				hql.append(" and t.docSealPeople is not null");
//			}
//		}
		hql.append(" order by t.docIssueDepartSigned desc");
		List<Object> pages = docDao.find(hql.toString(), params.toArray());
		return pages;
	}
	
	/**
	 * 
	 * @param page
	 * @param type
	 * @param isDelete
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaReportBean> getAllListReportWithCount(Page page,DocType type,Boolean isDelete,Date startDate,Date endDate) throws ServiceException, SQLException {
		List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select o.depart_name,count(o.depart_name) from (select t.rest3 as depart_name from t_trans_doc t ");
		User user = userService.getCurrentUser();
		List<Object> params = new ArrayList<Object>(0);
		hql.append(" where 1 = 1 ");
		if(type == DocType.Draft || type == DocType.ReturnBack || type == DocType.Recycle){//查询草稿
			hql.append(" and t.ddoc_entry_people = '"+user.getUserId()+"'");//录入人ID
		}
		if(type != null){													//公文状态
			List<DocType> docTypeList = type.getDocTypeList();
			if(docTypeList != null && !docTypeList.isEmpty()){
				StringBuilder docTypeStr = new StringBuilder();
				for(DocType docType : docTypeList){
					docTypeStr.append("'").append(docType.getValue()).append("'").append(",");
				}
				String docTypeString = docTypeStr.substring(0, docTypeStr.length()-1);
				hql.append(" and t.doc_state in("+docTypeString+")");
				
			}else{
				if(type.getRule()){
					hql.append(" and t.doc_state = '"+type.getValue()+"'");
				}else{
					hql.append(" and t.doc_state <> '"+type.getValue()+"'");
				}
				params.add(type.getValue());
			}

		}
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
			hql.append(" and  to_char(t.doc_entry_time, 'yyyy-MM-dd') >= '"+df.format(startDate)+"'");
//			params.add(startDate);
		}
		if(endDate != null && !"".equals(endDate)){//按录入时间搜索
			hql.append(" and  to_char(t.doc_entry_time, 'yyyy-MM-dd') <= '"+df.format(endDate)+"'");
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
	
//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true)
//	Page<Object[]> getPageAllListReport(Page page,DocType type,Boolean isDelete,Date startDate,Date endDate) throws ServiceException {
//		StringBuilder hql = new StringBuilder("select t.docIssueDepartSigned from TtransDoc t ");
//		User user = userService.getCurrentUser();
//		List<Object> params = new ArrayList<Object>(0);
//		hql.append(" where 1 = 1 ");
//		if(type == DocType.Draft || type == DocType.ReturnBack || type == DocType.Recycle){//查询草稿
//			hql.append(" and t.ddocEntryPeople = ?");
//			params.add(user.getUserId());										//录入人ID
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
//			}else{
//				if(type.getRule()){
//					hql.append(" and t.docState = ?");
//				}else{
//					hql.append(" and t.docState <> ?");
//				}
//				params.add(type.getValue());
//			}
//
////			if(type.getDataInDeptRange()) {//在部门范围内查询
////				Organization organization = userService.getDepartmentByOrgId(user.getOrgId());
////				hql.append(" and t.rest3 = ?");
////				params.add(organization.getOrgSyscode());
////			}
////			if(type.getDataInOrgRange()) {//在机构范围内查询
////				TUumsBaseOrg baseOrg = userService.getSupOrgByUserIdByHa(user.getUserId());
////				hql.append(" and t.rest3 = ?");
////				params.add(baseOrg.getOrgSyscode());
////			}
////			type.reset();
//		}
//		if(isDelete != null){
//			hql.append(" and t.isdelete = ?");								//是否是删除的公文
//			if(isDelete){
//				params.add(DocType.DELETE);									//删除状态
//			}else{
//				params.add(DocType.NOTDELETE);								//非删除状态
//			}
//		}
//		if(startDate != null && !"".equals(startDate)){//按录入时间搜索
//			hql.append(" and t.docEntryTime >= ?");
//			params.add(startDate);
//		}
//		if(endDate != null && !"".equals(endDate)){//按录入时间搜索
//			hql.append(" and t.docEntryTime <= ?");
//			params.add(endDate);
//		}
//		hql.append(" order by t.docIssueDepartSigned desc");
//		page = docDao.find(page, hql.toString(), params.toArray());
//		return page;
//	}
	
	/**
	 * 扩展查询方法
	 * @author:邓志城
	 * @date:2010-9-25 上午09:45:25
	 * @param page
	 * @param hql
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public Page getPageList(Page page,String hql,List<Object> params) throws ServiceException {
		return docDao.find(page, hql, params.toArray());
	}

	/**
	 * 得到公文草稿列表
	 * @author:邓志城
	 * @date:2010-8-10 下午05:38:23
	 * @param page					分页对象
	 * @param model					公文对象
	 * @param startDate				公文录入开始日期
	 * @param endDate				公文录入结束日期
	 * @return						公文草稿列表.	
	 * 	 * 		List<Object[]>{
	 * 			主键id,公文标题,发文文号,紧急程度,公文类别,录入时间,密级,签章人,是否锁定印章,公文状态
	 * }
	 * @throws ServiceException	
	 */
	@Transactional(readOnly = true)
	Page<Object[]> getDocDraft(Page page,DocType type,TtransDoc model,Date startDate,Date endDate) throws ServiceException {
		type.dataInOrgRange(Boolean.FALSE);
		return getPageList(page, model, type, Boolean.FALSE, startDate, endDate);
	}

	/**
	 * 得到需要签章的公文列表
	 * @author:邓志城
	 * @date:2010-8-11 上午09:00:46
	 * @param page					分页对象
	 * @param model					公文对象
	 * @param startDate				公文录入开始日期
	 * @param endDate				公文录入结束日期
	 * @return						需要签章的公文列表
	 * 	 * 		List<Object[]>{
	 * 			主键id,公文标题,发文文号,紧急程度,公文类别,录入时间,密级,签章人,是否锁定印章,公文状态
	 * }
	 * @throws ServiceException		
	 */
	@Transactional(readOnly = true)
	Page<Object[]> getDocSign(Page page,TtransDoc model,Date startDate,Date endDate) throws ServiceException {
		DocType type = DocType.Sign;
		User user = userService.getCurrentUser();
		String isSuperManager = user.getUserIsSupManager();
		if(isSuperManager != null && "1".equals(isSuperManager)) {//当前用户为超级管理员
			type.dataInOrgRange(Boolean.FALSE);
		}
		return getPageList(page, model, type, Boolean.FALSE, startDate, endDate);
	}

	/**
	 * 得到需要分发的公文列表
	 * @author:邓志城
	 * @date:2010-8-11 上午09:00:46
	 * @param page					分页对象
	 * @param model					公文对象
	 * @param startDate				公文录入开始日期
	 * @param endDate				公文录入结束日期
	 * @return						需要分发的公文列表
	 * 	 * 		List<Object[]>{
	 * 			主键id,公文标题,发文文号,紧急程度,公文类别,录入时间,密级,签章人,是否锁定印章,公文状态
	 * }
	 * @throws ServiceException		
	 */
	@Transactional(readOnly = true)
	Page<Object[]> getDocSend(Page page,TtransDoc model,Date startDate,Date endDate) throws ServiceException {
		return getPageList(page, model, DocType.Send, Boolean.FALSE, startDate, endDate);
	}
	
}
