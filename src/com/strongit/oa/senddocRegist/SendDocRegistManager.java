package com.strongit.oa.senddocRegist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSendDocRegist;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class SendDocRegistManager extends BaseManager{

	private GenericDAOHibernate<ToaSendDocRegist, String> registDocDAO;
	/** 用户管理Service接口*/
	private IUserService userservice;
	@Autowired protected IUserService userService;//统一用户服务
	@Autowired protected ITaskService iTaskService;//任务操作接口类
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		registDocDAO = new GenericDAOHibernate<ToaSendDocRegist, String>(
				sessionFactory, ToaSendDocRegist.class);
	}
	
	public Page<ToaSendDocRegist> getRegistDocs(Page<ToaSendDocRegist> page){
		String hql = "from ToaSendDocRegist t order by t.receiveTime DESC";
		return registDocDAO.find(page, hql, null);
	}
	
	public Page<ToaSendDocRegist> getRegistDocsBySearch(
			Page<ToaSendDocRegist> page, String searchDocTitle, String searchSend, String searchRoom, 
			String searchDocCode, String searchSecret, Date searchStaTime, Date searchEndTime,
			Date searchSendStaTime, Date searchSendEndTime){
		Object[] obj = new Object[10];
		int i = 0;
		StringBuffer hqlSb = new StringBuffer();
		hqlSb.append("from ToaSendDocRegist t where 1=1");
		if(searchDocTitle!=null && !"".equals(searchDocTitle)){
			hqlSb.append(" and t.docTitle like ?");
			if(searchDocTitle.indexOf("%")>-1){						//处理%号
				searchDocTitle = searchDocTitle.replaceAll("%", "/%");
				hqlSb.append(" ESCAPE '/'");
			}
			obj[i] = "%" + searchDocTitle + "%";
			i++;
		}
		if(searchSend!=null && !"".equals(searchSend)){
			hqlSb.append(" and t.send like ?");
			if(searchSend.indexOf("%")>-1){
				searchSend = searchSend.replace("%", "/%");
				hqlSb.append(" ESCAPE '/'");
			}
			obj[i] = "%" + searchSend + "%";
			i++;
		}
		if(searchRoom!=null && !"".equals(searchRoom)){
			hqlSb.append(" and t.toRoomName like ?");
			obj[i] = "%" + searchRoom + "%";
			i++;
		}
		if(searchDocCode!=null && "null".equals(searchDocCode)){
			hqlSb.append(" and t.docCode is null");
		}else if(searchDocCode!=null && "notNull".equals(searchDocCode)){
			hqlSb.append(" and t.docCode is not null");
		}else if(searchDocCode!=null && !"".equals(searchDocCode)){
			hqlSb.append(" and t.docCode like ?");
			if(searchDocCode.indexOf("%")>-1){
				searchDocCode = searchDocCode.replace("%", "/%");
				hqlSb.append(" ESCAPE '/'");
			}
			obj[i] = "%" + searchDocCode + "%";
			i++;
		}
		if(searchSecret!=null && !"".equals(searchSecret)){
			hqlSb.append(" and t.secret like ?");
			obj[i] = "%" + searchSecret + "%";
			i++;
		}
		if(searchStaTime!=null && !"".equals(searchStaTime)){
			searchStaTime.setHours(0);
			searchStaTime.setMinutes(0);
			searchStaTime.setSeconds(0);
			hqlSb.append(" and t.receiveTime >= ?");
			obj[i] = searchStaTime;
			i++;
		}
		if(searchEndTime!=null && !"".equals(searchEndTime)){
			searchEndTime.setHours(23);
			searchEndTime.setMinutes(59);
			searchEndTime.setSeconds(59);
			hqlSb.append(" and ? >= t.receiveTime");
			obj[i] = searchEndTime;
			i++;
		}
		if(searchSendStaTime!=null && !"".equals(searchSendStaTime)){
			searchSendStaTime.setHours(0);
			searchSendStaTime.setMinutes(0);
			searchSendStaTime.setSeconds(0);
			hqlSb.append(" and t.sendTime >= ?");
			obj[i] = searchSendStaTime;
			i++;
		}
		if(searchSendEndTime!=null && !"".equals(searchSendEndTime)){
			searchSendEndTime.setHours(23);
			searchSendEndTime.setMinutes(59);
			searchSendEndTime.setSeconds(59);
			hqlSb.append(" and ? >= t.sendTime");
			obj[i] = searchSendEndTime;
			i++;
		}
		hqlSb.append(" order by t.receiveTime DESC ");
		String hql = hqlSb.toString();
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}		
		return registDocDAO.find(page, hql, param);
	}
	
	public String saveRegistDoc(ToaSendDocRegist tsdr){
		registDocDAO.save(tsdr);
		return tsdr.getRegistId();
	}
	
	public void deleteRegistDoc(String id){
		registDocDAO.delete(id);
	}
	
	public ToaSendDocRegist getRegistDocById(String id){
		return registDocDAO.get(id);
	}
	
//	public String exportExcel(String idss, HttpServletResponse response){
//		BaseDataExportInfo export = new BaseDataExportInfo();
//		String sheetTitle = "发文登记编号";
//		export.setSheetName("Sheet1");
//		String[] str = {"公文标题","签发","主办处室","公文编号","密级","收办时间","签发时间","备注"};
//		List<String> tableHead = Arrays.asList(str);
//		export.setWorkbookFileName(sheetTitle);
//		export.setSheetTitle(sheetTitle);
//		export.setTableHead(tableHead);
//		List<Vector<String>> rowList = new ArrayList<Vector<String>>();
//		List<ToaSendDocRegist> list = new ArrayList<ToaSendDocRegist>();
//		String[] ids = idss.split(",");
//		for(int i=0; i<ids.length; i++){
//			list.add(getRegistDocById(ids[i]));
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		int i = 1;
//		for(ToaSendDocRegist sdr : list){
//			Vector<String> cols = new Vector<String>();
//			cols.add(sdr.getDocTitle());
//			cols.add(sdr.getSend());
//			cols.add(sdr.getToRoomName());
//			if(sdr.getDocCode()!=null){
//				cols.add(sdr.getDocCode());
//			}else{
//				cols.add("");
//			}
//			if(sdr.getSecret()!=null){
//				cols.add(sdr.getSecret());
//			}else{
//				cols.add("");
//			}
//			cols.add(sdf.format(sdr.getReceiveTime()));
//			cols.add(sdf.format(sdr.getSendTime()));
//			cols.add("");
//			rowList.add(cols);
//			i++;
//		}
//		export.setRowList(rowList);
//		export.setColWidth(5500);
////		Map rowhigh = new HashMap();
////		rowhigh.put("0", "3000");
////		rowhigh.put("1", "3000");
////		export.setRowHigh(rowhigh);
//		ProcessXSL xsl = new ProcessXSL();
//		xsl.createWorkBookSheet(export);
//		xsl.writeWorkBook(response);
//		return "ok";
//	}
	
	/**
	 * author:zhangli 
	 * description:获取所有处室 
	 * modifyer: 
	 * description:
	 * 
	 * @return User 用户对象
	 */
	public List<TempPo> getOrgList(String moduletype) throws SystemException,ServiceException{
		try{
			List<Organization> orglist = new ArrayList<Organization>();
			List<TempPo> newList = new ArrayList<TempPo>();
			List<String> syscodeList = new ArrayList<String>();
//			String idddd = userservice.getCurrentUser().getUserId();
//			if ("pige".equals(moduletype)) {// 如果为处室管理员，只能查看对应处室档案
//				orglist = userservice.getOrgAndChildOrgByCurrentUser();	
//			}else if("wpige".equals(moduletype)){
//				orglist = userservice.getCurrentUserOrgAndDept();
//			}
//			else{
//				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
//				User user=userService.getCurrentUser();
//				if(!isView){
//					Organization org=userservice.getDepartmentByOrgId(user.getOrgId());
//					orglist.add(org);
//				}
//				else{
//					orglist=userservice.getOrgAndChildOrgByCurrentUser();					
//				}
//			}
			orglist = userservice.getOrgAndChildOrgByUserId("6006800BBDF034DFE040007F01001D9F");//超级管理员  任何用户都能看到所有部门
			
			if(orglist==null||orglist.size()<=0)
				return new ArrayList<TempPo>();
			
			for(int i=0;i<orglist.size();i++){
				Organization org = orglist.get(i);
				syscodeList.add(org.getOrgSyscode());
			}
			for(int i=0;i<orglist.size();i++){
				TempPo po = new TempPo();
				po.setType("org");
				Organization org = orglist.get(i);
				if((org.getRest1()!=null && "0".equals(org.getRest1())) || "001".equals(org.getOrgCode())){	//以"001"判断是否为江西省人民政府(虚机构)
					String orgSysCode = org.getOrgSyscode();
					Organization porg = userservice.getParentDepartmentBySyscode(orgSysCode);
					String porgsyscode = porg.getOrgSyscode();
					if(porg==null||orgSysCode.equals(porgsyscode)||!syscodeList.contains(porgsyscode))
							po.setParentId(null);
					else
						po.setParentId(porg.getOrgId());
					po.setId(org.getOrgId());
					po.setCodeId(orgSysCode);
					po.setName(org.getOrgName());
					newList.add(po);
				}				
			}
			return newList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户"});
		}
	}
	
	/**
	 * author:zhangli
	 * description:注册用户Service接口
	 * @param archivefilemanager 用户Service接口
	 */
	@Autowired
	public void setUserservice(IUserService userservice) throws SystemException,ServiceException{
		this.userservice = userservice;
	}

	
	public List<ToaReportBean> getBorrowReport(Date borrowMonth,Date end)throws SystemException,ServiceException{
	
			
			String sendOrg ="";
			String sendOrgName="";
			List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
			try {
				StringBuffer sql = new StringBuffer("");
			//	sql.append(" select distinct(t.to_room) as toRoom from T_OA_SENDDOCREGIST t ");
				String hql1="select tt.orgId,tt.orgName from  TUumsBaseOrg tt where "
					+"tt.orgId in(select t.toRoom from ToaSendDocRegist t where t.receiveTime>=? and t.receiveTime<=?) order by tt.orgSequence";			
			List<Object> list1=registDocDAO.find(hql1,borrowMonth,end);
			 
			 for(Object obj:list1){	
				 Object[] strobj=(Object[])obj;
				 sendOrg=strobj[0].toString();
				 sendOrgName=strobj[1].toString();
			String[] docList={"赣府发","赣府文","赣府字","赣府办通报","赣府党字","省政府令","赣府厅发","赣府厅字","赣府厅文","赣府厅秘","赣府厅抄字","赣府厅党字","其他"};
				String[] doc=new String[docList.length];
				
				ToaReportBean bean=null;//报表对象
				List<HashMap<String, String[]>> myList=new ArrayList<HashMap<String,String[]>>();
		
					bean=new ToaReportBean();
					
				String docCode="";
				bean=new ToaReportBean();
				bean.setText1(sendOrgName);
				HashMap<String, String[]> myMap=new HashMap();
				for(int i=0;i<docList.length;i++){
					String hql="";
					List<Object> list=new ArrayList<Object>();
				  if(i<12){
					docCode=docList[i];
				 hql="select count(t.registId)as num from ToaSendDocRegist t where "
						+" t.receiveTime>=? and t.receiveTime<=?  and t.toRoom like '"+sendOrg+"%' and t.docCode like '"+docCode+"%'";				
				 list=registDocDAO.find(hql, borrowMonth,end);
			     Object ox=list.get(0);
				  String nums=ox.toString();
				  doc[i]=String.valueOf(nums);
				}else{
					 hql="select count(t.registId)as num from ToaSendDocRegist t where "
							+ "( t.docCode not like '赣府发%' and t.docCode not like '赣府文%'"
                  +"and t.docCode not like '赣府字%'"
                +"and t.docCode not like '赣府办通报%'"
                +"and t.docCode not like '赣府党字%'"
                 +"and t.docCode not like '省政府令%'"
                +" and t.docCode not like '赣府厅发%'"
               +" and t.docCode not like '赣府厅字%'"
                    +" and t.docCode not like '赣府厅文%'"
              +"and t.docCode not like '赣府厅秘%'"
                +"and t.docCode not like '赣府厅抄字%'"
                  +"and t.docCode not like '赣府厅党字%' or t.docCode is null or t.docCode='') and  (t.receiveTime>=? and t.receiveTime<=?  and t.toRoom like '"+sendOrg+"%')";				
					 list=registDocDAO.find(hql, borrowMonth,end);
				     Object ox=list.get(0);
					  String nums=ox.toString();
					  doc[i]=String.valueOf(nums);
				}
				}	
				myMap.put(sendOrgName, doc);
				  myList.add(myMap);
				 for(HashMap map:myList){
					  for (Iterator iter = map.entrySet().iterator();iter.hasNext();) { 
					         Map.Entry entry = (Map.Entry) iter.next();
					         Object key = entry.getKey();
					         bean.setText1(key.toString());
					         Object val = entry.getValue();
					        String[] xx=(String[])val;
					        bean.setText2(xx[0]);
					        bean.setText3(xx[1]);
					        bean.setText4(xx[2]);
					        bean.setText5(xx[3]);
					        bean.setText6(xx[4]);
					        bean.setText7(xx[5]);
					        bean.setText8(xx[6]);
					        bean.setText9(xx[7]);
					        bean.setText10(xx[8]);
					        bean.setText11(xx[9]);
					        bean.setText12(xx[10]);
					        bean.setText13(xx[11]);
					        bean.setText14(xx[12]);
					        int tot=Integer.parseInt(xx[0])+Integer.parseInt(xx[1])+Integer.parseInt(xx[2])+Integer.parseInt(xx[3])+Integer.parseInt(xx[4])+Integer.parseInt(xx[5])+Integer.parseInt(xx[6])+Integer.parseInt(xx[7])+Integer.parseInt(xx[8])+Integer.parseInt(xx[9])+Integer.parseInt(xx[10])+Integer.parseInt(xx[11])+Integer.parseInt(xx[12]);
					        bean.setText16(String.valueOf(tot));
					}
					}
						reportList.add(bean);
				}	

				
			 return reportList;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	     	return null;
	    
	}
	
	/**
	 * 根据发文流程实例返回签发人和时间("省政府领导批示"or"秘书长签发"任务实例)
	 * @param processInstanceId 流程实例Id
	 * @return
	 */
	public Object[] getTaskOpinion(String processInstanceId ){
		IWorkflowService iWorkflowService = adapterBaseWorkflowManager.getWorkflowService();
		List<Object[]> nodeList = iWorkflowService.getProcessHandlesAndNodeSettingByPiId(processInstanceId);
		String taskId = null;//任务实例Id
		String taskUserName = null;//处理人名称
		Date processedTime = null;//处理时间
		for(Object[] objs : nodeList){
			String taskName = (String) objs[1];	//Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象}		
			if(taskName != null && "拟稿人呈领导".equals(taskName)){
				taskId = objs[0] + "";
			}
			if(taskName != null && ("省政府领导批示".equals(taskName) || "秘书长签发".equals(taskName))){
				String taskId2 = objs[0] + "";
				String taskUserNameTemp = (String)objs[6];
				List<TwfInfoApproveinfo> wfApproveInfo = iWorkflowService.getDataByHql(
		        		"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
		        		new Object[] { new Long(taskId2) });
				TwfInfoApproveinfo ia = wfApproveInfo.get(0);
				String aiAssignType = ia.getAiAssigntype();
				//是否为代理
				if(aiAssignType!=null && "2".equals(aiAssignType)){
					taskUserNameTemp = ia.getAiOpersonname();
				}			
				if(processedTime == null){
					taskUserName = taskUserNameTemp;
					processedTime = (Date) objs[7];
				}else if(processedTime.getTime() < ((Date)objs[7]).getTime()){
					taskUserName = taskUserNameTemp;
					processedTime = (Date) objs[7];
				}
			}
		}
		Object[] objss = new Object[]{taskId, taskUserName, processedTime};
		return objss;
	}
	
	/**
	 * 保存发文登记信息(自动处理类调用)
	 * @param bussiness 表名;主键;主键值
	 * @param instanceId 流程实例ID
	 * @return
	 */
	public void registModel(String bussiness, String instanceId){
		if (bussiness != null && !"".equals(bussiness)) {
			String[] bussinessIds = bussiness.split(";");
			Object[] objs = getTaskOpinion(instanceId);
			String userId = iTaskService.getProcessStartorByTaskId((String)objs[0]);
			User user = userservice.getUserInfoByUserId(userId);
			Organization org = userservice.getDepartmentByOrgId(user.getOrgId());
			String orgName = org.getOrgName();
//			Connection con = this.getConnection();
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {
//				con.setAutoCommit(false);
				String sql = "select WORKFLOWTITLE,SENDDOC_CODE from " + bussinessIds[0]
						+ " where  " + bussinessIds[1] + "='" + bussinessIds[2]
						+ "'";
				psmt = this.getConnection().prepareStatement(sql);
				rs = psmt.executeQuery();
				if(rs.next()){
					String title = rs.getString("WORKFLOWTITLE");
					String code = rs.getString("SENDDOC_CODE");
					ToaSendDocRegist model = new ToaSendDocRegist();				
					model.setRegistId(null);
					if(objs[1] != null){
						model.setSend((String)objs[1]);
					}
					if(objs[2] != null){
						model.setSendTime((Date)objs[2]);
					}
					if(org.getOrgId() != null){
						model.setToRoom(org.getOrgId());
					}
					if(orgName != null){
						model.setToRoomName(orgName);
					}
					if(title != null){
						model.setDocTitle(title);
					}
					if(code != null){
						model.setDocCode(code);
					}				
					model.setReceiveTime(new Date());
					registDocDAO.save(model);
				}
			} catch (SQLException e) {
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
				e.printStackTrace();
			}finally{
				try{
					if(rs != null){
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
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
}
