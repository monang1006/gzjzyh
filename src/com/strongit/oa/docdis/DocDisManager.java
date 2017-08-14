package com.strongit.oa.docdis;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.strongit.oa.bo.ToaDocdisAttachment;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.bo.ToaSetCondition;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.docafterflow.DocAttachManager;
import com.strongit.oa.docafterflow.DocafterflowManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * <p>Title: DocDisManager.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 公文分发类
 * @date 	 2009-11-09 11:57:06
 * @version  1.0
 */
@Service
@Transactional
public class DocDisManager {
	
	GenericDAOHibernate<ToaDocDis, String> docDisDao;
	
	GenericDAOHibernate<ToaSetCondition, String>  setConditionDao;
	
	private SendDocManager sendDocManager;						//对应发文管理Manager
	
	private DocDisAttachManager attachManager;					//对应公文分发管理Manager
	
	private InfoSetManager infoManager;
	
	@Autowired private DocafterflowManager atferflowManager;
	
	@Autowired private DocAttachManager draftAttachManager;

	
	
	@Autowired
	protected IWorkflowAttachService workflowAttachManager; // 工作流附件管理
	
	private IUserService user;						
	
	private final static String NONE="0";						//都未分发
	
	private final static String OUT="1";						//已外部分发
	
	private final static String INNER="2";						//已内部分发
	
	private final static String ALL="3";						//都已分发
	
	private final static String SEND="1";						//未分发
	
	private final static String NOTSEND="0";					//已分发

	private IEFormService eform;								//电子表单服务
	
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	@Autowired
	public void setAttachManager(DocDisAttachManager attachManager) {
		this.attachManager = attachManager;
	}
	
	@Autowired
	public void setManager(SendDocManager sendDocManager) {
		this.sendDocManager = sendDocManager;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		docDisDao = new GenericDAOHibernate<ToaDocDis, String>(sessionFactory,ToaDocDis.class);
		setConditionDao = new GenericDAOHibernate<ToaSetCondition, String>(sessionFactory,ToaSetCondition.class);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-10 20:16:43
	 * @des     根据各种条件进行查询数据的方法
	 * @param	page  		分页对象
	 * @param	title 		文档名称
	 * @param	state		状态是否已经分发(未进行分发:0,已经进行了分发:1)
	 * @param	beginDate	查询范围中的开始日期
	 * @param	endDate		查询范围中的结束日期
	 * @return  List<ToaDocDis>
	 */
	@Transactional(readOnly=true)
	public Page<ToaDocDis> getPageList(Page<ToaDocDis> page,String title,String state,String innerState,Date beginDate,Date endDate,String signState){
		Object[] values = new Object[6];
		boolean downView=user.isViewChildOrganizationEnabeld();		//是否允许向下看	    
		boolean disGive= true;			//分级授权是否打开			
		String nowUserOrgCode = (user.getSupOrgByUserIdByHa((user.getCurrentUser()).getUserId())).getOrgSyscode();
		String queryStr = "from ToaDocDis t where t.isDelete is null";
		if(title!=null&&!"".equals(title)){
			queryStr = queryStr+" and t.senddocTitle like ?";
			values[0] = "%"+title+"%";
		}else{
			queryStr = queryStr+" and 1=?";
			values[0] = 1;
		}
		
		if("0".equals(state)||"1".equals(state)){
			queryStr = queryStr+" and t.senddocDic = ?";
			values[1] = state;
		}else{
			queryStr=queryStr+" and 1=?";
			values[1] = 1;
		}
		
		if(beginDate!=null){
			queryStr = queryStr + " and t.senddocOfficialTime>=?";
			values[2] = beginDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[2] = 1;
		}
		
		if(endDate!=null){
			queryStr = queryStr + " and t.senddocOfficialTime<=?";
			values[3] = endDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[3] =1;
		}
		
		/***/
		if(disGive){
			if(downView){
				queryStr = queryStr + " and t.senddocDeptCode like ?";
				values[4] = nowUserOrgCode; //+ "%";
			}else{
				queryStr = queryStr + " and t.senddocDeptCode like ?";
				values[4] = nowUserOrgCode;
			}
		}else{
			queryStr = queryStr + " and 1=?";
			values[4] = 1;
		}
		
		if("0".equals(innerState)||"1".equals(innerState)){	//内部分发状态
			if("0".equals(innerState)){	//未分发
				queryStr = queryStr + " and (t.isDistribute is null or t.isDistribute =?)";
			}else{						//已分发
				queryStr = queryStr + " and t.isDistribute =?";
			}
			values[5] = innerState;
		}else{
			queryStr = queryStr + " and 1=?";
			values[5] = 1;
		}
		
		
		if("0".equals(signState)){
			List<String> notInIds = atferflowManager.getNotGetIds();
			queryStr = queryStr + " and t.senddocId in ("+returnListStr(notInIds)+")";
		}if("1".equals(signState)){
			if("1".equals(innerState)){	//内部分发状态
				List<String> notInIds = atferflowManager.getNotGetIds();
				queryStr = queryStr + " and t.senddocId not in ("+returnListStr(notInIds)+")";
			}else{
				queryStr = queryStr+" and t.senddocDic = 1";
				List<String> notInIds = atferflowManager.getNotGetIds();
				queryStr = queryStr + " and t.senddocId not in ("+returnListStr(notInIds)+")";
			}
		}
		
		queryStr = queryStr + " order by t.senddocOfficialTime desc";
		return docDisDao.find(page, queryStr, values);
	}
	
	
	/**
	 * @author  于宏洲
	 * @date    2010-03-18 16:28:51
	 * @des     生成in语句所需要的ID格式
	 * @return  String
	 */
	private String returnListStr(List<String> ids){
		String returnValue = "";
		if(ids==null || ids.size()==0){
			return "'none'";
		}else{
			for(int i=0;i<ids.size();i++){
				if(i==ids.size()-1){
					returnValue = returnValue+ "'" +ids.get(i) + "'";
				}else{
					returnValue = returnValue + "'" +ids.get(i) + "',";
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-11 17:19:51
	 * @des     判断当前文档是否已经被分发（0:都未分发;1：已外部分发；2：已内部分发;3:都被分发）彭小青已修改于2009-12-19
	 * @return  boolean
	 */
	public String chargeSendOrNot(String docId){
		ToaDocDis doc = getDocDisById(docId);
		if(doc == null){
			return ALL;
		}else{
			String outDistr=doc.getSenddocDic();		//外部分发标识
			String innerDistr=doc.getIsDistribute();	//内部分发标识
			if(outDistr==null||"".equals(outDistr)||"null".equals(outDistr))
				outDistr="0";
//			if(innerDistr==null||"".equals(innerDistr)||"null".equals(innerDistr))
//				innerDistr="0";
//			if(outDistr.equals(NOTSEND)&&innerDistr.equals(NOTSEND)){	//都未分发
//				return NONE;
//			}else if(outDistr.equals(SEND)&&innerDistr.equals(NOTSEND)){//已外部分发
//				return OUT;
//			}else if(outDistr.equals(NOTSEND)&&innerDistr.equals(SEND)){//已内部分发
//				return INNER;
//			}else{		//都已分发
//				return ALL;
//			}
			
			return outDistr;

		}
	}
	
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-11 17:15:40
	 * @des     根据公文分发ID获取相应公文分发表中的文档内容
	 * @return  ToaDocDis
	 */
	@Transactional(readOnly=true)
	public ToaDocDis getDocDisById(String docId){
		return docDisDao.get(docId);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-10 09:53:37
	 * @des     根据工作流的实例ID将发文文档转化为待分发文档
	 * @return  boolean
	 */
	public boolean copyDocToDocDis(String processInstanceId) throws ServiceException,SystemException{
		String sendDocId = getSendIdByInstanceId(processInstanceId);
		ToaSenddoc doc = sendDocManager.getDocById(sendDocId);
		ToaDocDis docDis = mapDocDisObj(doc);							//将发文中的内容映射进入待分发公文表中
		docDisDao.save(docDis);											//进行实例化存储
		List<Tdocattach> attList = getSendDocAttachsById(sendDocId,docDis.getSenddocId());
		if(attList!=null && attList.size()!=0){
			//attachManager.saveList(attList);  //分发表里，存附件
			for(int k=0;k<attList.size();k++){										//保存对应的附件
				Tdocattach docDisA = attList.get(k);
				WorkflowAttach model = new WorkflowAttach();
				model.setDocId(docDisA.getDocId());
				model.setAttachName(docDisA.getAttachName());
				model.setAttachContent(docDisA.getAttachContent());
				workflowAttachManager.saveWorkflowAttach(model);
				
//				docDisA.setDocId(docDis.getSenddocId());
//				try{
//					draftAttachManager.saveObj(docDisA);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
			}
		}
		return true;
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-10 11:25:02
	 * @des     公文分发对象持久化
	 * @return  boolean
	 */
	public boolean saveObj(ToaDocDis docDis) throws ServiceException,SystemException{
		try{
			docDisDao.save(docDis);
			return true;
		}catch(Exception e){
			throw new SystemException("公文分发过程中持久化失败！");
		}
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-10 11:43:21
	 * @des     根据发文ID进行对相应发文的附件进行取出(原生SQL语句)
	 * @return  List<ToaDocdisAttachment>
	 */
	public List<Tdocattach> getSendDocAttachsById(String sendDocId,String docDisId) throws ServiceException,SystemException{
		ByteArrayOutputStream bos = null;
		ResultSet rs = null;
		InputStream is = null;
		try{
			List<Tdocattach> list = new ArrayList<Tdocattach>();
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(sendDocId);
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
	 * @author  于宏洲
	 * @date    2009-11-10 10:06:55
	 * @des     根据传入的工作流实例ID获取发文ID
	 * @return  String
	 */
	private String getSendIdByInstanceId(String processInstanceId){
		List<Object[]> ret = sendDocManager.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String sendDocId = bussinessId.split(";")[2];
		return sendDocId;
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-10 10:38:00
	 * @des     将发文的信息映射进入我当前分发对象中
	 * @return  ToaDocDis
	 */
	private ToaDocDis mapDocDisObj(ToaSenddoc doc){
		//**还没有加入组织结构信息
		User userInfo = user.getUserInfoByUserId(doc.getWORKFLOWAUTHOR());//user.getUserInfoByLoginName(doc.getSenddocUser());
		ToaDocDis docDis = new ToaDocDis();
		docDis.setSenddocAttachNum(doc.getSenddocAttachNum());
		docDis.setSenddocAuthor(doc.getSenddocAuthor());
		docDis.setSenddocBussinessCode(doc.getSenddocBussinessCode());
		docDis.setSenddocCcDepart(doc.getSenddocCcDepart());
		docDis.setSenddocCode(doc.getWORKFLOWCODE());
//		docDis.setSenddocContent(null);
//		docDis.setSenddocContentName(null);
		
		docDis.setSenddocContent(doc.getSenddocContent());
		docDis.setSenddocContentName(doc.getSenddocContentName());
		docDis.setSenddocEmergency(doc.getSenddocEmergency());
		docDis.setSenddocEntryPeople(doc.getSenddocEntryPeople());
		docDis.setSenddocFormid(doc.getSenddocFormid());
		docDis.setSenddocHavePrintNum(doc.getSenddocHavePrintNum());
		docDis.setSenddocIssueDepartSigned(doc.getSenddocIssueDepartSigned());
		docDis.setSenddocIssuer(doc.getSenddocIssuer());
		docDis.setSenddocKeywords(doc.getSenddocKeywords());
		docDis.setSenddocLegalCode(doc.getSenddocLegalCode());
		docDis.setSenddocOfficialTime(doc.getSenddocOfficialTime());
		docDis.setSenddocPeriodSecrecy(doc.getSenddocPeriodSecrecy());
		docDis.setSenddocPrintDepart(doc.getSenddocPrintDepart());
		docDis.setSenddocPrintNum(doc.getSenddocPrintNum());
		docDis.setSenddocPrintTime(doc.getSenddocPrintTime());
		docDis.setSenddocProcDeadline(doc.getSenddocProcDeadline());
		docDis.setSenddocProofReader(doc.getSenddocProofReader());
		docDis.setSenddocRecvTime(doc.getSenddocRecvTime());
		docDis.setSenddocRemark(doc.getSenddocRemark());
		docDis.setSenddocSealPeople(doc.getSenddocSealPeople());
		docDis.setSenddocSealTime(doc.getSenddocSealTime());
		docDis.setSenddocSecretLvl(doc.getSenddocSecretLvl());
		docDis.setSenddocSenderDepartCode(doc.getSenddocSenderDepartCode());
		docDis.setSenddocState(doc.getSenddocState());
		docDis.setSenddocSubmittoDepart(doc.getSenddocSubmittoDepart());
		docDis.setSenddocTitle(doc.getWORKFLOWTITLE());
		docDis.setSenddocUser(doc.getSenddocUser());
		docDis.setSenddocDic("0");
		docDis.setIsDistribute("0");
		docDis.setRest1(doc.getRest1());
		docDis.setRest2(doc.getRest2());
		docDis.setRest3(doc.getRest3());
		docDis.setRest4(doc.getRest4());
		docDis.setRest5(doc.getRest5());
		docDis.setRest6(doc.getRest6());
		docDis.setRest7(doc.getRest7());
		docDis.setRest8(doc.getRest8());
		
		docDis.setSenddocDeptCode((user.getSupOrgByUserIdByHa(userInfo.getUserId())).getOrgSyscode());	//放置当前机构下的code;
		return docDis;
	}

	/*
	 * 
	 * Description:获取当前用户
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 19, 2009 4:10:01 PM
	 */
	public User getCurrentUser()throws SystemException{
		return user.getCurrentUser();
	}
	
	/*
	 * 
	 * Description:获取单位下的部门列表，及所有部门下的人员列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 19, 2009 10:22:01 PM
	 */
	public List<OrgAndUser> getOrgAndUserList(List<TUumsBaseOrg> orgList,TUumsBaseOrg nowOrg)throws SystemException,ServiceException{
		List<OrgAndUser> orgAndUserList=new ArrayList<OrgAndUser>();
		List<TUumsBaseOrg> nowOrgList=new ArrayList<TUumsBaseOrg>();	
		try{
			String userLoginName=user.getCurrentUser().getUserLoginname();
			List<TUumsBaseUser> userList;	
			TUumsBaseOrg 	org;		
			String			orgCode;			//部门CODE
			String 			parentCode;
			OrgAndUser 		tempBo;				//临时对象
			TUumsBaseUser  	baseUser;			//用户对象	
			nowOrgList.add(nowOrg);
			for(int j=0;orgList!=null&&j<orgList.size();j++){
				org=orgList.get(j);
				parentCode=org.getSupOrgCode();
				if(parentCode.equals(nowOrg.getOrgSyscode())){
					nowOrgList.add(org);
				}
			}

			for(int i=0;nowOrgList!=null&&i<nowOrgList.size();i++){	//循环部门列表
				org=nowOrgList.get(i);
				orgCode=org.getOrgSyscode();
				userList=user.getUserListByOrgId(org.getOrgId());		//根据部门ID获取用户列表
				tempBo=new OrgAndUser();
				tempBo.setId(orgCode);
				tempBo.setIsOrgOrUser("0");
				tempBo.setName(org.getOrgName());
				if(i==0){							
					tempBo.setParentId(null);
				}else{
					tempBo.setParentId(org.getSupOrgCode());
				}
				orgAndUserList.add(tempBo);
				if(userList!=null&&userList.size()>0){
					for(int j=0;j<userList.size();j++){//循环用户列表
						baseUser=(TUumsBaseUser)userList.get(j);
						if(userLoginName.equals(baseUser.getUserLoginname())){
						
						}else{
							tempBo=new OrgAndUser();
							tempBo.setId(baseUser.getUserId());
							tempBo.setIsOrgOrUser("1");
							tempBo.setName(baseUser.getUserName());
							tempBo.setParentId(orgCode);
							orgAndUserList.add(tempBo);
						}
					}	
				}else{
					orgAndUserList.remove(tempBo);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取单位下的部门列表，及所有部门下的人员列表"});
    	}	
		return orgAndUserList;
	}
	
	/*
	 * 
	 * Description:根据机构ID获取该机构设置的条件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 28, 2009 6:39:06 PM
	 */
	public ToaSetCondition getConditionByOrgId(String orgId){
		try{
			String hql="from ToaSetCondition setCon where setCon.orgId=?";
			List<ToaSetCondition> list=setConditionDao.find(hql, orgId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据机构ID获取该机构设置的条件"});
    	}	
	}
	
	/*
	 * 
	 * Description:保存查询条件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 29, 2009 10:14:35 AM
	 */
	public void saveConditon(ToaSetCondition condition){
		try{
			setConditionDao.save(condition);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存查询条件"});
    	}	
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 29, 2009 10:22:51 PM
	 */
	public List<String> getObjectListByConditon(ToaSetCondition condition){
		List<String> list=new ArrayList<String>();
		String keyValue="";
		if(condition==null){
			return null;
		}
		ToaSysmanageStructure structure=infoManager.getInfoSetByInfoSetName(condition.getTargetTable());
		String pkey=structure.getInfoSetPkey();
		String sql="select "+pkey+" from "+condition.getTargetTable();
		if(condition.getGenerateConsql()!=null&&!"".equals(condition.getGenerateConsql())){
			sql+=" where "+condition.getGenerateConsql();
		}
		ResultSet rs=setConditionDao.executeJdbcQuery(sql);
		if(rs!=null){
			try {
				while(rs.next()){
					keyValue=rs.getString(1);
					list.add(keyValue);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	

	/*
	 * 
	 * Description:获取符合条件的岗位及岗位下的人员列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 29, 2009 2:46:27 PM
	 */
//	public List<OrgAndUser> getPostAndUserListByCon(ToaSetCondition condition,TUumsBaseOrg nowOrg,String userId){
//		List<OrgAndUser> orgAndUserList=new ArrayList<OrgAndUser>();
//		try{
//			
//			//List<Position> templist;							//临时岗位列表	
//			//List<User> userList=user.getAllUserInfoByHa();		//获取当前用户所属机构的所有分级授权用户列表信息
//			List<User> userList=user.getCurrentUserOrgAndDeptUsers();
//			List<User> tempUserList;							//临时用户列表
//			String[] post;				//岗位对象
//			User currentuser;			//用户对象
//			OrgAndUser tempBo;			
//			List<String> list=this.getObjectListByConditon(condition);
//			//List<String[]> postList=user.getPostListByOrgId(nowOrg.getOrgId());//岗位列表 
//			List<String[]> postList=user.getCurrentUserOrgAndDeptPositions();
//			for(int k=0;postList!=null&&k<postList.size();k++){		//循环岗位列表
//				post=postList.get(k);
//				if(list!=null&&!list.contains(post[0])){
//					continue;
//				}
//				tempUserList=user.getUsersByPositionId(post[0]);	//获取岗位下的人员列表	
//				tempBo=new OrgAndUser();
//				tempBo.setId(post[0]);
//				tempBo.setName(post[1]);
//				tempBo.setIsOrgOrUser("0");
//				tempBo.setParentId(null);
//				orgAndUserList.add(tempBo);
//				for(int x=0;tempUserList!=null&&x<tempUserList.size();x++){	//循环人员列表
//					currentuser=tempUserList.get(x);
//					if(currentuser.getUserId().equals(userId)){	//是否是当前人员
//						continue;
//					}
//					if(userList.contains(currentuser)){			//人员是否在当前用户所属机构的所有分级授权用户列表信息
//						tempBo=new OrgAndUser();
//						tempBo.setId(currentuser.getUserId());
//						tempBo.setName(currentuser.getUserName());
//						tempBo.setIsOrgOrUser("1");
//						tempBo.setParentId(post[0]);
//						orgAndUserList.add(tempBo);
//					}
//				}
//			}
//			return orgAndUserList;
//		}catch(ServiceException e){
//			throw new ServiceException(MessagesConst.find_error,               
//					new Object[] {"获取符合条件的岗位及岗位下的人员列表"});
//    	}	
//	}
	
	/*
	 * 
	 * Description:获取符合条件的用户组及用户组下的人员列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 29, 2009 10:06:43 PM
	 */
//	public List<OrgAndUser> getGroupAndUserListByCon(ToaSetCondition condition,TUumsBaseOrg nowOrg,String userId){
//		List<OrgAndUser> orgAndUserList=new ArrayList<OrgAndUser>();
//		try{
//			List<UserGroup> groupList=new ArrayList<UserGroup>();	//用户组列表
//			List<UserGroup> templist;							//临时用户组列表	
//			//List<User> userList=user.getAllUserInfoByHa();		//获取当前用户所属机构的所有分级授权用户列表信息
//			List<User> userList=user.getCurrentUserOrgAndDeptUsers();
//			List<User> tempUserList;							//临时用户列表
//			UserGroup userGroup;				//用户组对象
//			User currentuser;			//用户对象
//			OrgAndUser tempBo;			
//			List<String> list=this.getObjectListByConditon(condition);
//
//			for(int i=0;userList!=null&&i<userList.size();i++){		//循环当前用户所属机构的所有分级授权用户列表信息
//				currentuser=userList.get(i);		
//				templist=user.getUserGroupByUserId(currentuser.getUserId());//根据人员ID获取人员用户组列表
//				for(int j=0;templist!=null&&j<templist.size();j++){	//循环该人员的用户组列表
//					userGroup=templist.get(j);
//					if(!groupList.contains(userGroup)&&(condition==null||list.contains(userGroup.getGroupId()))){	//是否已放入用户组列表中
//						groupList.add(userGroup);
//					}
//				}
//			}
//			
//			for(int k=0;groupList!=null&&k<groupList.size();k++){		//循环用户组列表
//				userGroup=groupList.get(k);
//				tempUserList=user.getUsersInfoByGroupId(userGroup.getGroupId());	//获取用户组下的人员列表	
//				tempBo=new OrgAndUser();
//				tempBo.setId(userGroup.getGroupId());
//				tempBo.setName(userGroup.getGroupName());
//				tempBo.setIsOrgOrUser("0");
//				tempBo.setParentId(null);
//				orgAndUserList.add(tempBo);
//				for(int x=0;tempUserList!=null&&x<tempUserList.size();x++){	//循环人员列表
//					currentuser=tempUserList.get(x);
//					if(currentuser.getUserId().equals(userId)){	//是否是当前人员
//						continue;
//					}
//					if(userList.contains(currentuser)){			//人员是否在当前用户所属机构的所有分级授权用户列表信息
//						tempBo=new OrgAndUser();
//						tempBo.setId(currentuser.getUserId());
//						tempBo.setName(currentuser.getUserName());
//						tempBo.setIsOrgOrUser("1");
//						tempBo.setParentId(userGroup.getGroupId());
//						orgAndUserList.add(tempBo);
//					}
//				}
//			}
//			return orgAndUserList;
//		}catch(ServiceException e){
//			throw new ServiceException(MessagesConst.find_error,               
//					new Object[] {"获取符合条件的用户组及用户组下的人员列表"});
//    	}	
//	}
	
	public String notRealDelDocs(String id){
		String[] ids = id.split(",");
		String docId="";
		try{
			for(int i=0;i<ids.length;i++){
				docId = ids[i];
				ToaDocDis docDis = docDisDao.get(docId);
				docDis.setIsDelete("1");
				docDisDao.save(docDis);
				docDisDao.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return "false";
		}
		return "true";
	}
	
	/*
	 * 
	 * Description:删除待分发公文
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 30, 2009 5:28:56 PM
	 */
	public String deleteDocs(String id) {
		String[] ids=id.split(",");
		String msg="";
		String docId="";
		for(int i=0;i<ids.length;i++){
			docId=ids[i];
			if("0".equals(this.chargeSendOrNot(docId))){
				List<ToaDocdisAttachment> list=attachManager.getAttachmentsByDocId(docId);
				attachManager.deleteAttachment(list);
				docDisDao.delete(ids[i]);
			}else{
			    ToaDocDis docdis=this.getDocDisById(ids[i]);
			    if(docdis!=null)
			    	msg+=","+docdis.getSenddocTitle();
			}
		}
		if(!"".equals(msg)){
			msg=msg.substring(1);
		}
		return msg;
	}
	

	@Autowired
	public void setEform(IEFormService eform) {
		this.eform = eform;
	}

	@Autowired
	public void setInfoManager(InfoSetManager infoManager) {
		this.infoManager = infoManager;
	}
}
