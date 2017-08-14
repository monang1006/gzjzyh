package com.strongit.oa.docafterflow;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.Tdocattach;
import com.strongit.oa.bo.ToaDocDis;
import com.strongit.oa.bo.ToaDocafterflow;
import com.strongit.oa.bo.ToaDocdisAttachment;
import com.strongit.oa.bo.ToaDraftRecvdoc;
import com.strongit.oa.bo.ToaMail;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.docdis.DocDisAttachManager;
import com.strongit.oa.docdis.DocDisManager;
import com.strongit.oa.mymail.util.StrongDate;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * <p>Title: DocafterflowManager.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 对已经分发到各个分机构的信息进行查询
 * @date 	 2009-11-12 09:48:47
 * @version  1.0
 */

@Service
@Transactional
public class DocafterflowManager {
	
	GenericDAOHibernate<ToaDocafterflow, String> afterflowDao;
	
	private IUserService user;
	
	private DraftRecvdocManager recManager;
	
	private DocDisAttachManager docDisAttachManager;
	
	private DocDisManager docDisManager;
	
	private DocAttachManager draftAttachManager;
	
	private static final String draftState ="0";
	
	private static final String efFormNum = "62";
	
	@Autowired
	protected IWorkflowAttachService workflowAttachManager; // 工作流附件管理

	@Autowired
	public void setDocDisManager(DocDisManager docDisManager) {
		this.docDisManager = docDisManager;
	}
	
	@Autowired
	public void setDocDisAttachManager(DocDisAttachManager docDisAttachManager) {
		this.docDisAttachManager = docDisAttachManager;
	}
	
	@Autowired
	public void setDraftAttachManager(DocAttachManager draftAttachManager) {
		this.draftAttachManager = draftAttachManager;
	}

	@Autowired
	public void setRecManager(DraftRecvdocManager recManager) {
		this.recManager = recManager;
	}
	
	@Autowired
	public void setUser(IUserService aUser) {
		user = aUser;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		afterflowDao = new GenericDAOHibernate<ToaDocafterflow, String>(sessionFactory,ToaDocafterflow.class);
	}
	
	public void saveObj(ToaDocafterflow docAffterflow){
		afterflowDao.save(docAffterflow);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2010-03-18 11:16:30
	 * @des     查询出所有外部分发中未签收完毕的
	 * @return  List<String>
	 */
	public List<String> getNotGetIds(){
		Object[] values = new Object[1];
		String userId=user.getCurrentUser().getUserId();
		String nowUserOrgCode = (user.getSupOrgByUserIdByHa(userId)).getOrgSyscode();
		values[0] = nowUserOrgCode;
		List<String> ids = afterflowDao.find("select t.toaDocDis.senddocId from ToaDocafterflow t where t.getOrnot = '0' and (t.getType is null or t.getType='0') and t.toaDocDis.senddocDeptCode like ? order by t.getDocDate desc",values );
		return ids;
	}
	
	
	/**
	 * @author  于宏洲
	 * @date    2010-01-13 14:28:29
	 * @des     个人桌面展现接口，目前只在个人桌面上显示机构分发的内容
	 * @return  Page<ToaDocafterflow>
	 */
	@Transactional(readOnly=true)
	public List<ToaDocafterflow> getOrgNotReadDoc(Page<ToaDocafterflow> page){
		String userId=user.getCurrentUser().getUserId();								//当前用户ID
		String nowUserOrgCode = (user.getSupOrgByUserIdByHa(userId)).getOrgSyscode();	//当前用户所在机构
		String queryStr = "from ToaDocafterflow t where t.getOrnot = '0' and t.toaDocDis.isDelete is null and (t.getType is null or t.getType='0') and t.deptCode like ? order by t.getDocDate desc";
		Page<ToaDocafterflow> list = afterflowDao.find(page, queryStr, nowUserOrgCode);
		return list.getResult();
	}
	
	
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 09:50:33
	 * @des     根据表中主键获取对象
	 * @return  ToaDocafterflow
	 */
	@Transactional(readOnly=true)
	public ToaDocafterflow  getObjById(String id){
		return afterflowDao.get(id);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 09:53:04
	 * @des     根据文档关联ID和文档是否已经签收获取相关内容(此处是统计的时候根据对应文档进行相关的对已签收和未签收的统计，所以不必过滤)
	 * @param	docId 关联文档ID
	 * @param	read  true：已经签收；false:尚未签收
	 * @return  List<ToaDocafterflow>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaDocafterflow> getObjByReadOrNot(String docId,boolean read){
		String queryStr = "from ToaDocafterflow t where t.toaDocDis.senddocId=?  and (t.getType is null or t.getType='0')";
		
		Object[] values = new Object[3];
		
		values[0]=docId;
		
		if(read){
			queryStr=queryStr+" and ( t.getOrnot=?  or t.getOrnot=?) ";
			values[1]="1";
			values[2]="2";
		}else{
			queryStr=queryStr+" and ( t.getOrnot=?  and 1=?) ";
			values[1]="0";
			values[2]=1;
		}
		
		List<ToaDocafterflow> list = afterflowDao.find(queryStr,values);
		
		if(list==null){						//保证返回的list的size最小为0
			list = new ArrayList<ToaDocafterflow>();
		}
		return list;
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 20:14:16
	 * @des     数据列表查询不包括已经签收的
	 * @param	title 		标题
	 * @param	startDate 	查询范围开始日期
	 * @param	endDate		查询范围结束日期
	 * @return  Page<ToaDocafterflow>
	 */
	@Transactional(readOnly=true)
	public Page<ToaDocafterflow> getNotGetPageList(Page<ToaDocafterflow> page,String title,Date startDate,Date endDate){
		
		boolean downView=user.isViewChildOrganizationEnabeld();		//是否允许向下看
	    
		boolean disGive= true;			//分级授权是否打开	
		
		String userId=user.getCurrentUser().getUserId();
//		getUserDepartmentByUserId
//		String nowUserOrgCode = (user.getUserDepartmentByUserId(userId)).getOrgSyscode();
		
		String nowUserOrgCode = (user.getUserDepartmentByUserId(userId)).getSupOrgCode();

		
		String queryStr = "from ToaDocafterflow t where t.getOrnot = '0' and t.toaDocDis.isDelete is null";
		
		Object[] values = new Object[4];
		
		if(title!=null&&!"".equals(title)){
			queryStr = queryStr+" and t.toaDocDis.senddocTitle like ?";
			values[0] = "%"+title+"%";
		}else{
			queryStr = queryStr + " and 1=?";
			values[0] = 1;
		}
		
		if(startDate!=null){
			queryStr = queryStr + " and t.getDocDate>=?";
			values[1] = startDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[1] = 1;
		}
		
		if(endDate!=null){
			queryStr = queryStr + " and t.getDocDate<=?";
			values[2] = endDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[2] = 1;
		}
		
		if(disGive){
			if(downView){
				queryStr = queryStr + " and ((t.getType is null or t.getType='0') and t.deptCode like ?)";
				values[3] = nowUserOrgCode; //+ "%";
			}else{
				queryStr = queryStr + " and (t.getType is null or t.getType='0') and t.deptCode like ?";
				values[3] = nowUserOrgCode;
			}
		}else{
			queryStr = queryStr + "  and ((t.getType is null or t.getType='0') and 1=?)";
			values[3] = 1;
		}
		
//		queryStr=queryStr+" or (t.getType='1' and t.userId=?)) order by t.getDocDate desc" ;
//		values[4] = userId;
		
//		queryStr=queryStr+" or (t.getType='1' )) order by t.getDocDate desc" ;
		queryStr=queryStr+"   order by t.getDocDate desc" ;

		
		return afterflowDao.find(page, queryStr, values);
	}
	
	
	/**
	 * @author  penghj
	 * @date    2012-1-5 20:14:16
	 */
	@Transactional(readOnly=true)
	public Page<ToaDocafterflow> getGetPageList(Page<ToaDocafterflow> page,String title,Date startDate,Date endDate){
		
		boolean downView=user.isViewChildOrganizationEnabeld();		//是否允许向下看
	    
		boolean disGive= true;			//分级授权是否打开	
		
		String userId=user.getCurrentUser().getUserId();
//		getUserDepartmentByUserId
//		String nowUserOrgCode = (user.getUserDepartmentByUserId(userId)).getOrgSyscode();
		
		String nowUserOrgCode = (user.getUserDepartmentByUserId(userId)).getSupOrgCode();

		
		String queryStr = "from ToaDocafterflow t where   t.toaDocDis.isDelete is null";
		
		Object[] values = new Object[4];
		
		if(title!=null&&!"".equals(title)){
			queryStr = queryStr+" and t.toaDocDis.senddocTitle like ?";
			values[0] = "%"+title+"%";
		}else{
			queryStr = queryStr + " and 1=?";
			values[0] = 1;
		}
		
		if(startDate!=null){
			queryStr = queryStr + " and t.getDocDate>=?";
			values[1] = startDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[1] = 1;
		}
		
		if(endDate!=null){
			queryStr = queryStr + " and t.getDocDate<=?";
			values[2] = endDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[2] = 1;
		}
		
		if(disGive){
			if(downView){
				queryStr = queryStr + " and ((t.getType is null or t.getType='0') and t.deptCode like ?)";
				values[3] = nowUserOrgCode; //+ "%";
			}else{
				queryStr = queryStr + " and (t.getType is null or t.getType='0') and t.deptCode like ?";
				values[3] = nowUserOrgCode;
			}
		}else{
			queryStr = queryStr + "  and ((t.getType is null or t.getType='0') and 1=?)";
			values[3] = 1;
		}
		
//		queryStr=queryStr+" or (t.getType='1' and t.userId=?)) order by t.getDocDate desc" ;
//		values[4] = userId;
		
//		queryStr=queryStr+" or (t.getType='1' )) order by t.getDocDate desc" ;
		
		queryStr = queryStr + " and t.getOrnot='1'";

		queryStr=queryStr+"   order by t.getDocDate desc" ;

		
		return afterflowDao.find(page, queryStr, values);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-13 14:02:17
	 * @des     根据被分发的文档进行分发将对应的内容持久化
	 * @return  boolean
	 */
	public boolean realGetDoc(String docDisId,String ids,String names){
		//要进行相关的数据存储
		TUumsBaseOrg nowOrg = user.getSupOrgByUserIdByHa(user.getCurrentUser().getUserId());
		ToaDocDis toaDocDis = docDisManager.getDocDisById(docDisId);
		String now = StrongDate.getDate(new Date(),StrongDate.heng);
		Date nowDate = StrongDate.strToDate(now, StrongDate.heng);
		if(toaDocDis==null){			//对象不存在则进行失败提示
			return false;
		}
		
		if(ids==null||"".equals(ids)||names==null||"".equals(names)){
			return false;
		}else{
			try{
				String id[] =ids.split(",");
				String name[] = names.split(",");
				if(id.length!=name.length){
					return false;
				}else{
					for(int i=0;i<id.length;i++){
						//if(id[i].equals(nowOrg.getOrgSyscode())){			//如果当前分发人的机构也在所选择范围内则不进行保存
						//	continue;
						//}
						ToaDocafterflow tempObj = new ToaDocafterflow();
						tempObj.setDeptCode(id[i]);
						tempObj.setDeptname(name[i]);
						tempObj.setGetDocDate(nowDate);
						tempObj.setToaDocDis(toaDocDis);
						tempObj.setGetOrnot("0");
						tempObj.setGetType("0");
						saveObj(tempObj);
					}
					toaDocDis.setSenddocDic("1");
					docDisManager.saveObj(toaDocDis);
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}

	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 11:47:18
	 * @des     根据被分发的文档ID进行查询出相关的分发去处
	 * @return  Page<ToaDocafterflow>
	 */
	@Transactional(readOnly=true)
	public Page<ToaDocafterflow> getPageListByDocId(Page<ToaDocafterflow> page,String docId){
		String queryStr = "from ToaDocafterflow t where t.toaDocDis.senddocId=? and (t.getType is null or t.getType='0') order by t.getOrnot";
		return afterflowDao.find(page,queryStr, docId);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-13 09:12:28
	 * @des     更具传过来的ID进行批量的数据对应(将待分发公文对应至收文草稿箱中)
	 * @return  boolean
	 */
	public boolean getDocToDraft(String ids,String workflowName){
		if(ids!=null&&!"".equals(ids)){
			if(workflowName==null||"".equals(workflowName)){
				return false;
			}
			String id[] = ids.split(",");
			for(int i=0;i<id.length;i++){
				try{
					ToaDocafterflow myObj = getObjById(id[i]);
					ToaDocDis docDis = myObj.getToaDocDis();
					if(docDis!=null){
						ToaDraftRecvdoc recvDoc = recvDocBoMap(docDis,workflowName);						//首先将收文对象存储
						recManager.saveObj(recvDoc);										//保存草稿对象
//						List<ToaDocdisAttachment> list = docDisAttachManager.getAttachmentsByDocId(docDis.getSenddocId());
//						List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(docDis.getSenddocId());
						List<Tdocattach> attList = getSendDocAttachsById(docDis.getSenddocId(),recvDoc.getOarecvdocid());
						for(int k=0;k<attList.size();k++){										//保存对应的附件
//							WorkflowAttach docDisA = workflowAttachs.get(k);
//							Tdocattach draftA = new Tdocattach();
//							draftA.setDocId(recvDoc.getOarecvdocid());
//							draftA.setAttachName(docDisA.getAttachName());
//							draftA.setAttachContent(docDisA.getAttachContent());
//							draftA.setAttachPath(docDisA.getAttachPath());
							
							
							Tdocattach docDisA = attList.get(k);
							WorkflowAttach model = new WorkflowAttach();
							model.setDocId(recvDoc.getOarecvdocid());
							model.setAttachName(docDisA.getAttachName());
							model.setAttachContent(docDisA.getAttachContent());
							workflowAttachManager.saveWorkflowAttach(model);
//							try{
//								draftAttachManager.saveObj(draftA);
//							}catch(Exception e){
//								e.printStackTrace();
//							}
						}
					}else{
						return false;
					}
					myObj.setGetOrnot("2");													//对操作完成的一个对象进行签收状态改变
					String now = StrongDate.getDate(new Date(),StrongDate.heng);
					myObj.setGetDate(StrongDate.strToDate(now, StrongDate.heng));
					saveObj(myObj);															//对改变后的对象进行持久化
				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * @author  qibh
	 * @date    2012-06-25 10:27:20
	 * @des     根据分发公文ID进行对相应附件进行取出(原生SQL语句)
	 * @return  List<ToaDocdisAttachment>
	 */
	public List<Tdocattach> getSendDocAttachsById(String docDisId,String recvDocId) throws ServiceException,SystemException{
		ByteArrayOutputStream bos = null;
		ResultSet rs = null;
		InputStream is = null;
		try{
			List<Tdocattach> list = new ArrayList<Tdocattach>();
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(docDisId);
			if(workflowAttachs != null && !workflowAttachs.isEmpty()) {
				for(WorkflowAttach attach : workflowAttachs) {
					Tdocattach docdisAttach = new Tdocattach();
					docdisAttach.setDocId(docDisId);
					docdisAttach.setAttachName(attach.getAttachName());
					docdisAttach.setAttachContent(attach.getAttachContent());
					docdisAttach.setAttachPath(attach.getAttachPath());

					list.add(docdisAttach);
				}
			}
			return list;
		}catch(Exception e){
			throw new SystemException("进行发文附件查询复制的过程失败");
		}
	}
	
	/**
	 * @author  penghj
	 * @date    2012-1-5 09:12:28
	 * @des     签收待分发的公文，变为签收状态
	 * @return  boolean
	 */
	public boolean getDocCheck(String ids){
		if(ids!=null&&!"".equals(ids)){
			String id[] = ids.split(",");
			for(int i=0;i<id.length;i++){
				try{
					ToaDocafterflow myObj = getObjById(id[i]);
					myObj.setGetOrnot("1");													//对操作完成的一个对象进行签收状态改变
					String now = StrongDate.getDate(new Date(),StrongDate.heng);
					myObj.setGetDate(StrongDate.strToDate(now, StrongDate.heng));
					saveObj(myObj);															//对改变后的对象进行持久化
 				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-13 09:22:15
	 * @des     将待分发公文对象中的内容映射进入收文草稿箱对象
	 * @return  ToaDraftRecvdoc
	 */
	private ToaDraftRecvdoc recvDocBoMap(ToaDocDis docDis,String workflowName){
		ToaDraftRecvdoc recDoc = new ToaDraftRecvdoc();
		if(recDoc!=null){
			User currentUser = user.getCurrentUser();									//当前用户
			recDoc.setIssueDepartSigned(docDis.getSenddocIssueDepartSigned());			//来文机关
			//recDoc.setDocNumber(docDis.getSenddocCode());								//来文文号
			recDoc.setWorkflowCode(docDis.getSenddocCode());								//来文文号
			recDoc.setWorkflowTitle(docDis.getSenddocTitle());								//标题
			//正文为空导致表单报错，所有传null值，并且在表单中判断是否为null，如果为null则新建一个空文档
			recDoc.setContentName(null);												//文档名称
			recDoc.setContent(null);													//文档内容
			//recDoc.setEntryPeople(currentUser.getUserLoginname());						//签收人
			recDoc.setKeywords(docDis.getSenddocKeywords());							//主题词
			recDoc.setRecvFormid(efFormNum);											//收文表单ID定为62
			//recDoc.setRecvState(draftState);											//将处理状态改变为草稿类型
			recDoc.setPageCount(docDis.getRest8());										//页数
			recDoc.setRecvTime(new Date());												//设置收文时间
			recDoc.setWorkflowName(workflowName);										//设置流程名称
			recDoc.setWorkflowState("0");												//将相关内容设置为草稿
			recDoc.setWorkflowAuthor(currentUser.getUserId());
			return recDoc;
		}else{
			return null;
		}
	}
	
	/*
	 * 
	 * Description:内部分发公文
	 * param: 
	 * @author 	    彭小青
	 * @date 	   Dec 21, 2009 10:44:12 AM
	 */
	public boolean realGetDocOfInner(String docDisId,String ids,String names){
		//要进行相关的数据存储
		TUumsBaseOrg nowOrg = user.getSupOrgByUserIdByHa(user.getCurrentUser().getUserId());
		String deptCode=nowOrg.getOrgSyscode();
		String deptName=nowOrg.getOrgName();
		ToaDocDis toaDocDis = docDisManager.getDocDisById(docDisId);
		String now = StrongDate.getDate(new Date(),StrongDate.heng);
		Date nowDate = StrongDate.strToDate(now, StrongDate.heng);
		String tempIds="";
		if(toaDocDis==null){			//对象不存在则进行失败提示
			return false;
		}else if(ids==null||"".equals(ids)||names==null||"".equals(names)){
			return false;
		}else{
			try{
				String id[] =ids.split(",");
				String name[]=names.split(",");
				for(int i=0;i<id.length;i++){
					if(tempIds.indexOf(id[i])==-1){
						tempIds+=","+id[i];
						ToaDocafterflow tempObj = new ToaDocafterflow();
						tempObj.setDeptCode(deptCode);
						tempObj.setDeptname(deptName);
						tempObj.setGetDocDate(nowDate);
						tempObj.setToaDocDis(toaDocDis);
						tempObj.setGetOrnot("0");
						tempObj.setGetType("1");
						tempObj.setUserId(id[i]);
						tempObj.setUserName(name[i]);
						saveObj(tempObj);
					}
				}
				toaDocDis.setIsDistribute("1");
				docDisManager.saveObj(toaDocDis);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
	
	/*
	 * 
	 * Description:获取内部分发列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 21, 2009 2:52:33 PM
	 */
	public List<ToaDocafterflow> getInnerDistrByReadOrNot(String docId,boolean read){
		String queryStr = "from ToaDocafterflow t where t.toaDocDis.senddocId=? and t.getOrnot=? and t.getType='1'";
		
		Object[] values = new Object[2];
		
		values[0]=docId;
		
		if(read){
			values[1]="1";
		}else{
			values[1]="0";
		}
		
		List<ToaDocafterflow> list = afterflowDao.find(queryStr,values);
		
		if(list==null){						//保证返回的list的size最小为0
			list = new ArrayList<ToaDocafterflow>();
		}
		return list;
	}
	
	/*
	 * 
	 * Description:根据被分发的文档ID进行查询出相关的分发去处
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 21, 2009 3:15:31 PM
	 */
	public Page<ToaDocafterflow> getInnerPageListByDocId(Page<ToaDocafterflow> page,String docId){
		String queryStr = "from ToaDocafterflow t where t.toaDocDis.senddocId=? and  t.getType='1' order by t.getOrnot";
		return afterflowDao.find(page,queryStr, docId);
	}
}
