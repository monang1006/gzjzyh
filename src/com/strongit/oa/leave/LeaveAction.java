package com.strongit.oa.leave;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmDeptreport;
import com.strongit.oa.bo.TSwConfersort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/*******************************************************************************
 * 
 * @Description: NoticeConferenceAction.java
 * @Date:Mar 26, 2013
 * @Author 胡海亮
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 * 
 */
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "leave.action", type = ServletActionRedirectResult.class) })
public class LeaveAction extends BaseActionSupport<TOmDeptreport> {

	private static final long serialVersionUID = 1L;
	private TOmDeptreport model;
	
	@Autowired
	private ILeaveManager leaveManager;
	
	@Autowired
	private IUserService userService;
	
	private Page<TOmDeptreport> personpage = new Page<TOmDeptreport>(10, true);
	
	private String reportingId;
	
	private String conId;		//会议Id
	
	private String conName;
	
	private Date conStime;
	
	private Date conEtime;
	
	private String state; 
	
	@SuppressWarnings("unchecked")
	private Map typeMap = new HashMap(); 
	
	private Date leaveBegintime;
	
	private Date leaveEndtime;
	
	private String leavereason;
	
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
//		String depId=userManager.getCurrentUserInfo().getOrgId();
//		personpage=leaveManager.getConreportPage(personpage,model,conId,depId,state);
		   String	conferId=getRequest().getParameter("conferId");// 会议ID
		   String conid="";
		   if(conferId!=null && !conferId.equals("")){
			   TOmConference con=leaveManager.getBigConference(conferId);
			   if(con!=null){
			       conid=con.getConferenceId();
			   }else{
					  conid="null";
				  }
			  }
			getRequest().setAttribute("conferId", conid);
//			
			conId=getRequest().getParameter("conId");
			System.out.println("conId:"+conId);
			if(!"".equals(conId)&&null!=conId){
				
				/**
				 * 取得当前的用户单位ID
				 */
				String depId=userService.getCurrentUser().getOrgId();
				System.out.println("DepId:"+depId);
				
				/**
				 * 根据ConId得到参加人员信息Page
				 */
				personpage=leaveManager.getConreportPage(personpage,model,state,conId,depId);
				
				List<TOmDeptreport> list=personpage.getResult();
				
				/**
				 * 请假状态的判断
				 */
				if (list != null && list.size() > 0) {
					for (Iterator<TOmDeptreport> top = list.iterator(); top.hasNext();) {
						TOmDeptreport to = top.next();
						String state = to.getState();
						if (state.equals("2")) {
							//typeMap.put(to.getReportingId(),"<font color='red'>已请假（"+to.getLeaveBegintime()+"-"+to.getLeaveEndtime()+"）</font>");
							typeMap.put(to.getReportingId(),"<font color='red'>已请假</font>");
						} else if(state.equals("3")) {
							//typeMap.put(to.getReportingId(),"<font color='blue'>已销假（"+to.getLeaveBegintime()+"）</font>");
							typeMap.put(to.getReportingId(),"<font color='blue'>已销假</font>");
						}else{
							typeMap.put(to.getReportingId(),"<font color='green'>未请假</font>");
						}
					}
				}
			}else{
				/**
				 * Page == null
				 */
				
			}
//			//return "content";
			
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public TOmDeptreport getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public String leaveTree() throws Exception{
		
		return "tree";
	}
	/***************************************************************************
	 * 
	 * 方法简要描述：查询Page
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-18
	 * @Author 胡海亮
	 * @return String
	 * @version 1.0
	 * @throws Exception 
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public String content(){
		conId=getRequest().getParameter("conId");
		System.out.println("conId:"+conId);
		if(!"".equals(conId)&&null!=conId){
			
			/**
			 * 取得当前的用户单位ID
			 */
			String depId=userService.getCurrentUser().getOrgId();
			System.out.println("DepId:"+depId);
			
			/**
			 * 根据ConId得到参加人员信息Page
			 */
			personpage=leaveManager.getConreportPage(personpage,model,state,conId,depId);
			
			List<TOmDeptreport> list=personpage.getResult();
			
			/**
			 * 请假状态的判断
			 */
			if (list != null && list.size() > 0) {
				for (Iterator<TOmDeptreport> top = list.iterator(); top.hasNext();) {
					TOmDeptreport to = top.next();
					String state = to.getState();
					if (state.equals("2")) {
						//typeMap.put(to.getReportingId(),"<font color='red'>已请假（"+to.getLeaveBegintime()+"-"+to.getLeaveEndtime()+"）</font>");
						typeMap.put(to.getReportingId(),"<font color='red'>已请假</font>");
					} else if(state.equals("3")) {
						//typeMap.put(to.getReportingId(),"<font color='blue'>已销假（"+to.getLeaveBegintime()+"）</font>");
						typeMap.put(to.getReportingId(),"<font color='blue'>已销假</font>");
					}else{
						typeMap.put(to.getReportingId(),"<font color='green'>未请假</font>");
					}
				}
			}
		}else{
			/**
			 * Page == null
			 */
			
		}
		return "content";
	}
	/***************************************************************************
	 * 
	 * 方法简要描述：判断是否可以销假、请假
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-18
	 * @Author 胡海亮
	 * @return String
	 * @version 1.0
	 * @throws Exception 
	 * @see
	 */
	public String isLeave() throws Exception{
		
		TOmDeptreport tom=leaveManager.getDepRepById(reportingId);
		String flag="";
		if("2".equals(tom.getState())){
			//请假状态
			
			flag="OKXJ"+"@"+tom.getConferee().getPersonName()+"@"+tom.getState();
		}else if("3".equals(tom.getState())){
			//销假状态
			flag="OKQJ"+"@"+tom.getConferee().getPersonName()+"@"+tom.getState();
		}else{
			//其他状态
			flag="OKQJ"+"@"+tom.getConferee().getPersonName()+"@"+tom.getState();
		}
		
		return this.renderText(flag);
	}
	/**
	 * 申请请假
	 * @return
	 * @throws Exception
	 */
	public String leaveInput() throws Exception{
		model=leaveManager.getDepRepById(reportingId);
		System.out.println(model.getConferee().getPersonName());;
		conName=model.getTOmConferenceSend().getTOmConference().getConferenceTitle();
		System.out.println();
		conStime=model.getTOmConferenceSend().getTOmConference().getConferenceStime();
		conEtime=model.getTOmConferenceSend().getTOmConference().getConferenceEtime();
		state="2";
		return "leaveInput";
	}
	/**
	 * 修改请假
	 * @return
	 * @throws Exception
	 */
	public String leaveEdit() throws Exception{
		model=leaveManager.getDepRepById(reportingId);
		System.out.println(model.getConferee().getPersonName());;
		conName=model.getTOmConferenceSend().getTOmConference().getConferenceTitle();
		System.out.println();
		conStime=model.getTOmConferenceSend().getTOmConference().getConferenceStime();
		conEtime=model.getTOmConferenceSend().getTOmConference().getConferenceEtime();
		state="2";
		return "leaveEdit";
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述：进行销假或者请假
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-18
	 * @Author 胡海亮
	 * @return String
	 * @version 1.0
	 * @throws Exception 
	 * @see
	 */
	public String cancleLeave() throws Exception{
		String result="0";
		try {
			
			model=leaveManager.getDepRepById(reportingId);
			model.setState(state);
			/**
			 * 
			 * 1、2代表请假
			 * 
			 * 2、3代表销假
			 * 
			 */
			if("2".equals(state)){
				
				model.setLeaveBegintime(leaveBegintime);
				model.setLeaveEndtime(leaveEndtime);
				model.setLeavereason(leavereason);
				
			}else if("3".equals(state)){
				
				model.setLeaveBegintime(new Date());
				model.setLeaveEndtime(null);
				
			}
			
			leaveManager.saveLeave(model);
			
		} catch (Exception e) {
			// TODO: handle exception
			result="-1";
		}
		
		return this.renderText(result);
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述：显示所有会议类型的会议
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-11
	 * @Author 胡海亮
	 * @return String
	 * @version 1.0
	 * @throws Exception 
	 * @see
	 */
	public String showTree() throws Exception
	{	
		/**
		 * 
		 * 1、得到会议通知表
		 * 
		 * 2、得到会议类型：设置Pid为-1
		 * 
		 * 3、根据单位ID，得到会议下发对象，然后在得到得到会议对象 ：设置Pid为会议类型Id
		 * 
		 */
		/*
		 * 使用 TOmConType
		List<TOmConType> conTypeList=new ArrayList<TOmConType>();
		
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		conTypeList = leaveManager.getAllConType();
		
		TreeNode node;
		for(TOmConType row : conTypeList)
		{
			node = new TreeNode();
			node.setPid("-1");
			
			node.setId(row.getContypeId());
			node.setText(row.getContypeName());
			node.setValue(row.getContypeId());
		
			node.setComplete(true);
			node.setShowcheck(false);
			node.setIsexpand(false);
			nodeList.add(node);
		}
		*/
		
		//使用 TSwConfersort
		/**
		 * 
		 * 2、得到会议类型：设置Pid为-1
		 * 
		 */
		List<TSwConfersort> consortList=new ArrayList<TSwConfersort>();
		
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		consortList = leaveManager.getAllConSort();
		
		TreeNode node;
		for(TSwConfersort row : consortList)
		{
			node = new TreeNode();
			node.setPid("-1");
			
			node.setId(row.getConfersortId());
			node.setText(row.getConfersortName());
			node.setValue(row.getConfersortId());
		
			node.setComplete(true);
			node.setShowcheck(false);
			node.setIsexpand(false);
			nodeList.add(node);
		}
		
		/**
		 * 取得当前的用户单位ID
		 */
		String depId=userService.getCurrentUser().getOrgId();
		
		/**
		 * 
		 * 3、根据单位ID，得到会议下发对象，然后在得到得到会议对象 ：设置Pid为会议类型Id
		 * 
		 */
		List<TOmConferenceSend> conList=new ArrayList<TOmConferenceSend>();
		
		/**
		 * 得到List<TOmConferenceSend>
		 */
		conList=leaveManager.getConsendList(depId);
		
		TreeNode node1;
		for(TOmConferenceSend row : conList)
		{
			node1 = new TreeNode();
			
			node1.setPid(row.getTOmConference().getTsConfersort().getContypeId());
			
			node1.setId(row.getTOmConference().getConferenceId());
			node1.setText(row.getTOmConference().getConferenceTitle());
			node1.setValue(row.getTOmConference().getConferenceId());
		
			node1.setComplete(true);
			node1.setShowcheck(false);
			node1.setIsexpand(false);
			nodeList.add(node1);
		}
		
		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeList);
		
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		return null;
	}

	
	/**************************************************************************************
	 * 
	 * Get and Set 方法
	 * 
	 **************************************************************************************/
	public void setModel(TOmDeptreport model) {
		this.model = model;
	}

	public Page<TOmDeptreport> getPersonpage() {
		return personpage;
	}

	public void setPersonpage(Page<TOmDeptreport> personpage) {
		this.personpage = personpage;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@SuppressWarnings("unchecked")
	public Map getTypeMap() {
		return typeMap;
	}

	@SuppressWarnings("unchecked")
	public void setTypeMap(Map typeMap) {
		this.typeMap = typeMap;
	}

	public String getReportingId() {
		return reportingId;
	}

	public void setReportingId(String reportingId) {
		this.reportingId = reportingId;
	}

	public Date getLeaveBegintime() {
		return leaveBegintime;
	}

	public void setLeaveBegintime(Date leaveBegintime) {
		this.leaveBegintime = leaveBegintime;
	}

	public Date getLeaveEndtime() {
		return leaveEndtime;
	}

	public void setLeaveEndtime(Date leaveEndtime) {
		this.leaveEndtime = leaveEndtime;
	}

	public String getLeavereason() {
		return leavereason;
	}

	public void setLeavereason(String leavereason) {
		this.leavereason = leavereason;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public Date getConStime() {
		return conStime;
	}

	public void setConStime(Date conStime) {
		this.conStime = conStime;
	}

	public Date getConEtime() {
		return conEtime;
	}

	public void setConEtime(Date conEtime) {
		this.conEtime = conEtime;
	}
	
	
}
